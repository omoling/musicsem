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
		String artists=request.getParameter("artist");
		//Make request to datasource
		String result= DataLastFm.lastfm("artist.getevents",artists);
		response.setContentType("text/xml;charset=utf-8");
		
		//Forward XML result to RdfProducer (XSLT)
		String rdf_data = RdfProducer.XmlToRdf(result, "/Users/psyle/Documents/workspace/iwa_alpha/WebContent/test.xsl");
		//Import RDF/XML to model
		JenaFrame.import_rdf(rdf_data);
		//Show model
		String rdf_model=JenaFrame.show_model();
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