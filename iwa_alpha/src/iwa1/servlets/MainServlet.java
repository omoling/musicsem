package iwa1.servlets;

import  iwa1.datasources.*
;

import iwa1.semanticframework.*;


import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




/**
 * Servlet implementation class MainServlet
 */
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		
		PrintWriter out = response.getWriter();
		
		/**
		 * artist : overall search
		 * eventsnearby : ask for events in the area
		 */
		String type = request.getParameter("type");
		String keyword = request.getParameter("search");
		
		//Contact only last fm and return events on rdf/xml format
		if (type.equals("eventsnearby"))
		{
			String lastfm_res= DataLastFm.lastfm("geo.getEvents","location",keyword);
			String lastfm_rdf = DataLastFm.Lastfm_rdf(lastfm_res);
			//Response
			response.setContentType("text/xml;charset=utf-8");
			out.print(lastfm_rdf);	
	
		} 
		
		//Contact all datasources
		else if (type.equals("artist"))
		{
			//TODO: session management
			 //Initialize Main Model
			 JenaFrame.init();
			
		 //Make requests to datasources
	 		//Query DBpedia
			DataDBpedia.query_dbpedia(keyword);
			String dbpedia_rdf = DataDBpedia.show_model();
			
	 		//Last.fm
			//Events by artist 
	 		String lastfm_res= DataLastFm.lastfm("artist.getevents","artist",keyword);
	 		
	 		//Events by location
	 		//String lastfm_res= DataLastFm.lastfm("geo.getEvents","location","Amsterdam");
			 
	 		//YouTube
	 		DataYouTube.search_youtube(keyword);
	 		//String youtube_rdf = DataYouTube.show_model();
			
	 		//Flickr
	 		DataFlickr.searchPhotos(keyword);
	 		//String flickr_model = DataFlickr.show_model();
	 
		//Import data to main model
	 		//DBpedia import
	 		DataDBpedia.addNflush();
	 		
	        //Lastfm
			 if(lastfm_res!=null)
			 { 
			  String lastfm_rdf = DataLastFm.Lastfm_rdf(lastfm_res);	 
			  JenaFrame.import_rdf(lastfm_rdf);
			 }	 
			
			//Flickr import
			DataFlickr.addNflush();
			
			//YouTube import
			DataYouTube.addNflush();
			
			//Show model
			String rdf_model=JenaFrame.show_model();
			
			 //Query local model
			 //String local_res = JenaFrame.query_model();
			 
			//Response
			response.setContentType("text/xml;charset=utf-8");
			out.print(dbpedia_rdf);
			
		} 
		
		else {
			//stop
			return;
		}


			  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		doGet(request, response);
	}

}
