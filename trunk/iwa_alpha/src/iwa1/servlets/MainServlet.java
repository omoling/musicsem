package iwa1.servlets;

import  iwa1.datasources.*;
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
		 
 		//Last.fm
 		String lastfm_res= DataLastFm.lastfm("artist.getevents",keyword);
 		//YouTube
 		 String youtube_res= DataYouTube.youtube(keyword);
		
 		//Flicr
 		String flickr_res = DataFlickr.searchFlickr(keyword);
		
		
		
 		//Forward XML result to RdfProducer (XSLT)
 		//TODO: relative path??
		//String xsl_source = getServletContext().getResource("/WEB-INF/test.xsl").toString();
		String xsl_source = "/Users/"+System.getProperty("user.name")+"/Documents/workspace/iwa_alpha/WebContent/test.xsl";
		
		String rdf_data = RdfProducer.XmlToRdf(lastfm_res, xsl_source);
		//Import RDF/XML to model
		JenaFrame.import_rdf(rdf_data);
		
		//Show model
		String rdf_model=JenaFrame.show_model();
		
		 //Query local model
		 String local_res = JenaFrame.query_model();
		 
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
