package iwa1.datasources;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.datatypes.*;

/*
import com.google.gdata.client.*;
import com.google.gdata.client.youtube.*;
import com.google.gdata.data.*;
import com.google.gdata.data.geo.impl.*;
import com.google.gdata.data.media.*;
import com.google.gdata.data.media.mediarss.*;
import com.google.gdata.data.youtube.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
*/

public class DataYouTube {
	//public static YouTubeService service = new YouTubeService("musicsem", "AI39si7EoXyvmGkxvEVXmxhzGld68uvsX8N4zZC7v1k7m6lNCo7ZsAesh58KhF_KhOq92jnTZcul4ct_Fj7hBA0XL4qnSes70Q");
	public static Model youtube_model = ModelFactory.createDefaultModel();
	public static Property videotext = null; 
	public static  Property duration = null; 
	
	/*
	public static void search_youtube (String keyword)
	{
		YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
		// order results by the number of views (most viewed first)
		query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
		
		// search for videos
		query.setFullTextQuery(keyword);
		query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
		
		VideoFeed videoFeed = service.query(query, VideoFeed.class);
	}
	*/
	
	
	
	
	public static void init_model()
	{
		
	 youtube_model.setNsPrefix("mindswap","http://www.mindswap.org/~glapizco/technical.owl#");
	 videotext = youtube_model.createProperty("http://www.mindswap.org/~glapizco/technical.owl#","VideoText");
	 duration = youtube_model.createProperty("http://www.mindswap.org/~glapizco/technical.owl#","hasDurationSeconds");
	//public static Resource res = youtube_model.createResource("http://www.kanzaki.com/ns/music#Artist");
	// res.addProperty(name, artist);
	 
	 
	}
	
	
	public static String show_model()
	{
		ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
		youtube_model.write(rdf_stream);
		return rdf_stream.toString();	
	}
	
	
	
	
	public static String youtube(String keyword) {
		try
		{
			//set method URL
			URL url = new URL("http://gdata.youtube.com/feeds/api/videos??q=" +
					URLEncoder.encode(keyword, "UTF-8") +"&key=AI39si7EoXyvmGkxvEVXmxhzGld68uvsX8N4zZC7v1k7m6lNCo7ZsAesh58KhF_KhOq92jnTZcul4ct_Fj7hBA0XL4qnSes70Q");
			URLConnection urlc = url.openConnection();
			urlc.setDoOutput(true);
			urlc.setAllowUserInteraction(false);
	                     		
			//String return
			BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line+"\n");
			}
			br.close();
			return sb.toString();
			
		  }
		catch(Exception e) {
	        e.printStackTrace();
	        return null;
		}

	  }
     
}
