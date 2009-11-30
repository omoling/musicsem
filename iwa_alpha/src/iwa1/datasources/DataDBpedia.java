package iwa1.datasources;

import iwa1.semanticframework.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.sparql.*;

public class DataDBpedia {
	public static void query_dbpedia(String keyword) {
		String queryString=
			"PREFIX owl: <http://www.w3.org/2002/07/owl#>"+
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"+
            "PREFIX dc: <http://purl.org/dc/elements/1.1/>"+
            "PREFIX : <http://dbpedia.org/resource/>"+
            "PREFIX dbpedia2: <http://dbpedia.org/property/>"+
            "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>"+
            "PREFIX dbpedia: <http://dbpedia.org/resource/>"+
            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
            
               
            "SELECT ?resource  ?property ?hasValue "+
            "WHERE { "+
            "?resource a dbpedia-owl:Artist."+	
            "?resource rdfs:label ?artistName."+
            "?resource  ?property ?hasValue."+
            "FILTER (regex(?artistName, \"Jimmy Page\"))"+
            "		}";
            
		/*	
			"PREFIX p: <http://dbpedia.org/property/>"+
			"PREFIX dbpedia: <http://dbpedia.org/resource/>"+
			"PREFIX category: <http://dbpedia.org/resource/Category:>"+
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"+
			"PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"+
			"PREFIX geo: <http://www.georss.org/georss/>"+

			"SELECT DISTINCT ?m ?n ?p ?d"+
			"WHERE {"+
			" ?m rdfs:label ?n."+
			" ?m skos:subject ?c."+
			" ?c skos:broader category:Churches_in_Paris."+
			" ?m p:abstract ?d."+
			" ?m geo:point ?p"+
			 "}";
           */ 
			// Create a query object
			Query query = QueryFactory.create(queryString);
			// initializing queryExecution factory with remote service.
			//try
			//{
			QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
			//}
			//catch(QueryException e)
			//{
		    //    e.printStackTrace();
			//}
								
			//Execute query and process results
		    try {
		    	
	            ResultSet results = qexec.execSelect();
	            ResultSetFormatter.asRDF(JenaFrame.model, results);
	            //String xml = ResultSetFormatter.asXMLString( results );
	            //return xml;        
	            
	            } 
	          finally 
	            {
	        	  qexec.close() ;
	            }      

	}
	


}
