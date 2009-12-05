package iwa1.semanticframework;

import java.io.BufferedReader;






import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.util.Iterator;



import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;


public class JenaFrame {
	public static Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
	public static Model dbmodel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
	public static InfModel infModel = null;

	public static void init()
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
	 //load the ontology into the model
	 //model.read(inputStream, null);

		
	}
	
	public static void import_rdf(String rdf_data)
	{
	
		InputStream ins = new ByteArrayInputStream(rdf_data.getBytes());
		model.read(ins, null, "RDF/XML");
		
	}
	
	public static String show_model()
	{
			
		ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
		model.write(rdf_stream);
		return rdf_stream.toString();
	
	}
	
	public static String show_infModel()
	{
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner.bindSchema(model);
		InfModel infModel = ModelFactory.createInfModel(reasoner,dbmodel);	
		ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
		infModel.write(rdf_stream);
		return rdf_stream.toString();
	
	}

	public static void export_infModel()
	{
		try
		{
		 FileOutputStream out = new FileOutputStream("InfModel.rdf");
		 Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		 reasoner.bindSchema(model);
		 InfModel infModel = ModelFactory.createInfModel(reasoner,dbmodel);	
		 infModel.prepare();
		 infModel.write(out,"RDF/XML");
	     out.close();
		}
		catch (Exception e) 
		{
			 e.printStackTrace();
		}
	}
	
	
	public static String query_model() {
		/*
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner.bindSchema(model);
		infModel = ModelFactory.createInfModel(reasoner,dbmodel);	
		*/
		
		
		
		
		String queryString=
			"PREFIX owl: <http://www.w3.org/2002/07/owl#> "+
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "+
            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "+
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "+
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/> "+
            "PREFIX dc: <http://purl.org/dc/elements/1.1/> "+
            "PREFIX : <http://dbpedia.org/resource/> "+
            "PREFIX dbpedia2: <http://dbpedia.org/property/> "+
            "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> "+
            "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> "+
            "PREFIX j.0: <http://www.w3.org/2001/sw/DataAccess/tests/result-set#> "+
            "SELECT  ?resource ?property ?hasValue "+
            "WHERE { "+
            "?resource rdfs:label ?artistName."+
            "?resource  ?property ?hasValue."+
            "FILTER (regex(?artistName, \"Jimmy Page\"))"+
            "}";

		
		
		Query query = QueryFactory.create(queryString);
		//Query local model
		QueryExecution local_exec= QueryExecutionFactory.create(query, JenaFrame.infModel);
	    try {
	    	
            ResultSet results = local_exec.execSelect();
            String xml = ResultSetFormatter.asXMLString( results );
            return xml;        
            
            } 
          finally 
            {
        	  local_exec.close() ;
            }  
		
	}
	

}
