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
		String keyword = request.getParameter("search");

		//TODO: session management
		 //Initialize Main Model
		 JenaFrame.init();
		
	 //Make requests to datasources
 		//Query DBpedia
		DataDBpedia.query_dbpedia(keyword);
		//String dbpedia_rdf = DataDBpedia.show_model();
		
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
		  DataLastFm.addToModel(lastfm_res);	  	 
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
		out.print(rdf_model);
			  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		doGet(request, response);
	}

}
