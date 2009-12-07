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
		 //Initialize Model
		 JenaFrame.init();
		
	 	//Make requests to datasources
 		//Query DBpedia
 		//String dbpedia_res = DataDBpedia.query_dbpedia(keyword);
		DataDBpedia.query_dbpedia(keyword);
		String dbpedia_rdf = DataDBpedia.show_model();
		
		
 		//Last.fm
		//Events by artist 
 		//String lastfm_res= DataLastFm.lastfm("artist.getevents","artist",keyword);
 		
 		//Events by location
 		//String lastfm_res= DataLastFm.lastfm("geo.getEvents","location","Amsterdam");
		
         /*
		 if(lastfm_res!=null)
		 { 
			
		 //Import to model	 
		 //DataLastFm.addToModel(lastfm_res);	 
		  	 
		 }	 
		 */
		 
 		//YouTube
 		//DataYouTube.search_youtube(keyword);
 		//String youtube_rdf = DataYouTube.show_model();
		
 		//Flickr
 		//DataFlickr.searchPhotos(keyword);
 		//String flickr_model = DataFlickr.show_model();
 
		
		//Flickr import
		//JenaFrame.model.add(DataFlickr.flickr_model);
		
		//Show model
		//String rdf_model=JenaFrame.show_model();
		//Show infModel
		//String rdf_infModel=JenaFrame.show_infModel();
		
		
		 //Query local model
		 //String local_res = JenaFrame.query_model();
		 


		//Response
		response.setContentType("text/xml;charset=utf-8");
		out.print(dbpedia_rdf);
			  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		doGet(request, response);
		
		
	}

}
