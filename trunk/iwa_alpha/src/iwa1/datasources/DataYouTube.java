package iwa1.datasources;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.datatypes.*;
import com.hp.hpl.jena.vocabulary.*;
import com.sun.media.jai.opimage.DCTOpImage;


import com.google.gdata.client.*;
import com.google.gdata.client.youtube.*;
import com.google.gdata.data.*;
import com.google.gdata.data.geo.impl.*;
import com.google.gdata.data.youtube.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.wireformats.*;
import com.google.gdata.data.maps.*;
import com.google.api.gbase.client.*;
import javax.mail.*;
import javax.activation.*;

import jena.test.rdfparse;




public class DataYouTube {
	//The YouTube Model
	public static OntModel youtube_model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
	public static Resource res = null;
	//Properties of the model
	public static Property name = null;
	public static Property identifier = null;
	public static Property title = null; 
	public static Property duration = null; 
	public static Property depicts = null;

	
	//FOAF Ontology
	public static String foafURL = "http://www.nachlin.com/foaf.rdf";
	//Image Ontology
	public static String imgOntURL = "http://www.mindswap.org/~glapizco/technical.owl";
	
	public static String artist = null;
	
	//Parameters for the YouTube Service
	public static String client_id = "musicsem";
	public static String api_key = "AI39si7EoXyvmGkxvEVXmxhzGld68uvsX8N4zZC7v1k7m6lNCo7ZsAesh58KhF_KhOq92jnTZcul4ct_Fj7hBA0XL4qnSes70Q";
	public static YouTubeService service = new YouTubeService(client_id, api_key);
	
	public static void search_youtube (String keyword)
	{
		artist=keyword;
		try
		{
		 YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
		 // order results by the number of views (most viewed first)
		 query.setOrderBy(YouTubeQuery.OrderBy.RELEVANCE);
		
		// search for videos
		query.setFullTextQuery(keyword);
		query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
		
		VideoFeed videoFeed = service.query(query, VideoFeed.class);
		if(videoFeed.getTotalResults()!=0)
		 {	
			init_model();
			 for( VideoEntry video : videoFeed.getEntries())
			 {
			    YouTubeMediaGroup mediaGroup = video.getMediaGroup(); 
				String video_id = mediaGroup.getVideoId();
				String title = video.getTitle().getPlainText();
				long duration = mediaGroup.getDuration();
				addVideo(video_id,title,""+duration);
				
				
			 }
		 }	
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
		
	
	public static void init_model()
	{

	 //Set Prefixes	
	 youtube_model.setNsPrefix("foaf","http://xmlns.com/foaf/0.1/");	
	 youtube_model.setNsPrefix("dc", "http://purl.org/dc/elements/1.1/");
	 youtube_model.setNsPrefix("dctype", "http://purl.org/dc/dcmitype/");
	 youtube_model.setNsPrefix("mindswap", "http://www.mindswap.org/~glapizco/technical.owl#");
		
	 //Load foaf ontology
	 youtube_model.read(foafURL);
	 //Load Image Ontology
	 youtube_model.read(imgOntURL);
	 
	 //Set Properties
	 name = youtube_model.getProperty(FOAF.getURI(), "name");
	 title = youtube_model.getProperty(DC.getURI(),"title");
	 identifier = youtube_model.getProperty(DC.getURI(),"identifier");
	 depicts = youtube_model.getProperty("http://www.mindswap.org/~glapizco/technical.owl#","depicts");
	 duration = youtube_model.getProperty("http://www.mindswap.org/~glapizco/technical.owl#","hasDurationSeconds");
	 
	 //Create a foaf:Agent Resource
	 res = youtube_model.createResource(FOAF.Agent);
	 res.addProperty(name,artist);
	  
	}
	
	
	public static void addVideo(String videoURL,String video_title, String video_duration)
	{
			
      OntClass video_class = youtube_model.getOntClass("http://www.mindswap.org/~glapizco/technical.owl#Video");
      Individual video = youtube_model.createIndividual(video_class);
	  video.addProperty(identifier,videoURL);
	  video.addProperty(title,video_title);
	  video.addProperty(duration,""+video_duration);
	  video.addProperty(depicts,res);
	  
	  
	}
	
	
	public static String show_model()
	{
		ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
		youtube_model.write(rdf_stream);
		return rdf_stream.toString();	
	}

}
