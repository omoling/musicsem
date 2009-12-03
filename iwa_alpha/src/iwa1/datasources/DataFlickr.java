package iwa1.datasources;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.SearchParameters;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.Photo;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.datatypes.*;

public class DataFlickr {
	public static Model flickr_model = ModelFactory.createDefaultModel();
	public static Resource res = flickr_model.createResource("http://www.kanzaki.com/ns/music#Artist");
	public static Property name = null; 
	public static  Property img = null; 
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
	     searchParams.setSort(SearchParameters.INTERESTINGNESS_DESC);
	    
	     //Create tag keyword array
	     String[] tags=new String[] {keyword};
	     searchParams.setTags(tags);
	     
	     //Initialize PhotosInterface object
	     PhotosInterface photosInterface=flickr.getPhotosInterface();
	     //Execute search with entered tags
	     PhotoList photoList=photosInterface.search(searchParams,20,1);

	     //get search result and fetch the photo object and get small square imag's url
	     if(photoList!=null)
	     {
	    	 //Initialize model
	    	 init_model();
	        //Get search result and check the size of photo result
	        for(int i=0;i<photoList.size();i++)
	        {
	           //get photo object
	           Photo photo=(Photo)photoList.get(i);
	           //Get  photo URL
	           String photo_url= photo.getMediumUrl();
	           //Add photo to model
	           res.addProperty(img, photo_url); 
	           
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
		
	 flickr_model.setNsPrefix("foaf","http://xmlns.com/foaf/0.1/");	
	 flickr_model.setNsPrefix("m","http://www.kanzaki.com/ns/music#");
	 name = flickr_model.createProperty("http://xmlns.com/foaf/0.1/","name");
	 img = flickr_model.createProperty("http://xmlns.com/foaf/0.1/","img");
	 res.addProperty(name, artist);
	 
	 
	}
	
	
	public static String show_model()
	{
		ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
		flickr_model.write(rdf_stream);
		return rdf_stream.toString();	
	}
	
	/*
	public static String searchFlickr(String keyword) {
		try
		{
			//set method URL
			URL url = new URL("http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=6d3d002ca456197a60df58cec7a9fadb&text=" +
					URLEncoder.encode(keyword, "UTF-8") );
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
     */
}
