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

public class DataFlickr {
	
	//The flickr datasource model
	public static OntModel flickr_model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
	public static Resource res = null;
	
	//Properties of the model
	public static Property title = null;
	public static Property identifier = null;
	public static Property name = null; 
	public static Property depicts = null;
	public static Property img_width = null;
	public static Property img_height = null;
	
	//FOAF Ontology
	public static String foafURL = "http://www.nachlin.com/foaf.rdf";
	
	//Parameters for the Flick Service
	public static String api_key = "6d3d002ca456197a60df58cec7a9fadb";
	public static String flickr_srv = "www.flickr.com";
    public static String artist= null;
	//Using Flickj API library
	public static void searchPhotos(String keyword)
	{
	 artist = keyword;	
		try
		{
	     REST rest = new REST();
	     rest.setHost(flickr_srv);
	     
	     //initialize Flickr object with key and rest
	     Flickr flickr=new Flickr(api_key,rest);
	     Flickr.debugStream=false;
	     
	     //initialize SearchParameter object, this object stores the search keyword
	     SearchParameters searchParams=new SearchParameters();
	     searchParams.setSort(SearchParameters.RELEVANCE);
	    
	     //Create tag keyword array
	     String[] tags=new String[] {keyword};
	     searchParams.setTags(tags);
	     
	     //Initialize PhotosInterface object
	     PhotosInterface photosInterface=flickr.getPhotosInterface();
	     //Execute search with entered tags
	     PhotoList photoList=photosInterface.search(searchParams,20,1);

	     //get search result and fetch the photo object and get medium size url
	     if(photoList!=null)
	     {
	    	 //Initialize model
	    	 init_model();
	        //Get search result and check the size of photo result
	        for(int i=0;i<photoList.size();i++)
	        {
	           //Get photo object
	           Photo photo=(Photo)photoList.get(i);
	           //Get medium size photo URL
	           String photo_url= photo.getMediumUrl();
	           //Get title
	           String photo_title = photo.getTitle();
	           //Get size
	           //Size photo_size = photo.getMediumSize();
	           //int photo_width = photo_size.getWidth();
	           //int photo_height = photo_size.getHeight();
	           
	           //Add photo to model
	           addPhoto(photo_url,photo_title);
	           
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
	 //Set prefixes	
	 flickr_model.setNsPrefix("foaf","http://xmlns.com/foaf/0.1/");	
	 flickr_model.setNsPrefix("dc", "http://purl.org/dc/elements/1.1/");
	 flickr_model.setNsPrefix("tiff", "http://ns.adobe.com/tiff/1.0/");
	 
	 //Load Ontology
	 flickr_model.read(foafURL);
	  	 
	 //Set properties	 
	 depicts = flickr_model.getProperty(FOAF.getURI(), "depicts");
	 name = flickr_model.getProperty(FOAF.getURI(), "name");
	 title = flickr_model.getProperty(DC.getURI(),"title");
	 identifier = flickr_model.getProperty(DC.getURI(),"identifier"); 
	 img_width = flickr_model.createProperty("http://ns.adobe.com/tiff/1.0/","ImageWidth");
	 img_height = flickr_model.createProperty("http://ns.adobe.com/tiff/1.0/","ImageLength");
	 
	 //Create a foaf:Agent Resource
	 res = flickr_model.createResource(FOAF.Agent);
	 res.addProperty(name,artist);
	 	  
	}
	
	public static void addPhoto(String photoURL,String photo_title)
	{
		//Create a foaf:Image Resource
		Resource photo = flickr_model.createResource(FOAF.Image);
		photo.addProperty(identifier,photoURL);
		photo.addProperty(title, photo_title);
        photo.addProperty(depicts,res);
		
	}
	
	
	
	public static String show_model()
	{
		ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
		flickr_model.write(rdf_stream);
		return rdf_stream.toString();	
	}
	
}
