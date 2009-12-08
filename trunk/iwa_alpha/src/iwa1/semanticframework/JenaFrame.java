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
	public static OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
    public static Model qmodel = ModelFactory.createDefaultModel();

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

	 
	 //load the dbpedia ontology into the model
	 model.read(inputStream, null);
	 
     model.read("http://www.mindswap.org/~glapizco/technical.owl");
	 
	 //Set Prefixes
	 model.setNsPrefix("dbpedia2","http://dbpedia.org/property/");
	 model.setNsPrefix("mindswap","http://www.mindswap.org/~glapizco/technical.owl#");
	 model.setNsPrefix("dbpedia-owl","http://dbpedia.org/ontology/");	
	 
	//Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
	//reasoner.bindSchema(model);

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
	
	/*
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
*/	
	
	public static String query_model(String keyword) {		
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
            "PREFIX mindswap: <http://www.mindswap.org/~glapizco/technical.owl#> "+
            "PREFIX m: <http://www.kanzaki.com/ns/music#> "+
            "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>" +
            "PREFIX vCard: <http://www.w3.org/2001/vcard-rdf/3.0#>" +
			///*
            "CONSTRUCT { "+
			"?s rdf:type foaf:Agent; "+
			"foaf:name ?name; "+
			"dbpedia2:abstract ?abstract; "+
			"dbpedia2:currentMembers ?currentMembers; "+
			"dbpedia2:yearsActive ?yearsActive; "+
			"dbpedia2:genre ?genre; "+
			"dbpedia-owl:thumbnail ?thumbnail; "+
			"dbpedia2:url ?url; " +
			"foaf:img ?img;"+
			"mindswap:Video ?Video;"+
			"dc:title ?video_tile;"+
			"dc:identifier ?video_id;"+
			"mindswap:hasDuration ?video_duration."+
			"} "+
			"WHERE {" +
			//"GRAPH ?g{ "+
			"?s rdf:type foaf:Agent. "+
			"?s foaf:name ?name. "+
			"?s dbpedia2:abstract ?abstract. "+
			"OPTIONAL { ?s dbpedia2:currentMembers ?currentMembers }. "+
			"?s  dbpedia-owl:thumbnail ?thumbnail. "+
			"?s dbpedia2:yearsActive ?yearsActive. "+
			"?s  dbpedia2:genre ?genre. "+
			"?s  dbpedia2:url ?url. "+
			//Images
			"?Image foaf:img ?img. "+
			"?Image  foaf:depicts ?m. "+
			"?m foaf:name ?name. "+
			//Videos
			"?Video rdf:type mindswap:Video. "+
			"?Video dc:title ?video_title. "+
			"?Video dc:identifier ?video_id. "+
			"?Video mindswap:hasDurationSeconds ?video_duration. "+
			"?Video mindswap:depicts ?x. "+
			"?x foaf:name ?name. "+
			"FILTER (regex(?name, \""+keyword+"\")). "+
			"}";
            //*/
            
            /*
            "SELECT ?s  ?p ?o "+
            "WHERE { "+
            "?s a dbpedia-owl:Artist."+
            "?s foaf:name ?artistName. "+
            "?s ?p ?o. "+
            "FILTER (regex(?artistName, \"Moby\")) "+
            "}";
		    */
		
		Query query = QueryFactory.create(queryString);
		//Query local model
		QueryExecution local_exec= QueryExecutionFactory.create(query,model);
		try {			
			 //ResultSet results = local_exec.execSelect();
	    	 qmodel = local_exec.execConstruct(); 
			 ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
			 qmodel.write(rdf_stream);
			 return rdf_stream.toString();	    
            //String xml = ResultSetFormatter.asXMLString(results);
            //return xml;
            } 
          finally 
            {
        	  local_exec.close() ;
            }  
		
	}
	

}
