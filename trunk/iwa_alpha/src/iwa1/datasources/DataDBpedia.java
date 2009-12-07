package iwa1.datasources;

import iwa1.semanticframework.*;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.sparql.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.*;

public class DataDBpedia {
	public static Model dbpedia_model = ModelFactory.createDefaultModel();
	public static void query_dbpedia(String keyword) {
		String queryString=
		        "PREFIX owl: <http://www.w3.org/2002/07/owl#> "+
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "+
                "PREFIX dc: <http://purl.org/dc/elements/1.1/> "+
                "PREFIX dbpedia2: <http://dbpedia.org/property/> "+
                "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> "+
                "PREFIX dbpedia: <http://dbpedia.org/resource/> "+
                "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "+
				"CONSTRUCT { "+
				"?s rdf:type foaf:Agent; "+
				"foaf:name ?name; "+
				"dbpedia2:abstract ?abstract; "+
				"dbpedia2:currentMembers ?currentMembers; "+
				"dbpedia2:yearsActive ?yearsActive; "+
				"dbpedia2:genre ?genre; "+
				"dbpedia-owl:thumbnail ?thumbnail; "+
				"dbpedia2:url ?url.} "+
				"WHERE {" +
				"GRAPH ?g{ "+
				"?s a dbpedia-owl:Band. "+
				"?s foaf:name ?name. "+
				"?s dbpedia2:abstract ?abstract. "+
				"?s  dbpedia-owl:thumbnail ?thumbnail. "+
				"?s dbpedia2:currentMembers ?currentMembers. "+
				"?s dbpedia2:yearsActive ?yearsActive. "+
				"?s  dbpedia2:genre ?genre. "+
				"?s  dbpedia2:url ?url. "+
				"FILTER (regex(?name, \""+keyword+"\")). "+
				"FILTER langMatches( lang(?abstract), 'en'). "+
				"}.}";
              
		// Create a query object
		Query query = QueryFactory.create(queryString);
		query.addGraphURI("http://dbpedia.org/");
		
		
		// initializing queryExecution factory with remote service.
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
		
		//Initialize model
		init_model();
		
		//Execute query and process results
		try {
	        
			 dbpedia_model = qexec.execConstruct(); 
	            
	        } 
	    finally 
	        {
	       	  qexec.close() ;
	        }      

	}
	
	public static void init_model()
	{
		 FileInputStream inputStream = null;	
		 //Import DBpedia Ontology
		 try 
		 {	
		    inputStream = new FileInputStream("/Users/"+System.getProperty("user.name")+"/Documents/workspace/iwa_alpha/WebContent/WEB-INF/lib/Ontologies/dbpedia_3.4.owl");
		  }
		 catch (FileNotFoundException e)
		  {
			 e.printStackTrace();
		  }
		 //load the dbpedia ontology into the model
		 dbpedia_model.read(inputStream, null);
	}
	
	
	public static String show_model()
	{
			
		ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
		dbpedia_model.write(rdf_stream);
		return rdf_stream.toString();
	
	}
	
	public static void addNflush()
	{
		//Add flickr model to applications main model and clear data
		JenaFrame.model.add(dbpedia_model);
		//clear data
		dbpedia_model.removeAll();
	}
	


}
