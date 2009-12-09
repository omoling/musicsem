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

	 
	 //Set Prefixes
	 model.setNsPrefix("dbpedia2","http://dbpedia.org/property/");
	 model.setNsPrefix("mindswap","http://www.mindswap.org/~glapizco/technical.owl#");
	 model.setNsPrefix("dbpedia-owl","http://dbpedia.org/ontology/");
	 model.setNsPrefix("m","http://www.kanzaki.com/ns/music#");
	 
	 //Load the dbpedia ontology
	 model.read(inputStream, null);
	 //Load the image ontology
     model.read("http://www.mindswap.org/~glapizco/technical.owl");
     //Load the music ontology
     model.read("http://www.kanzaki.com/ns/music.rdf");
     //Load the foaf
     model.read("http://xmlns.com/foaf/0.1/index.rdf");
     //Load the vCard
     model.read("http://www.w3.org/2001/vcard-rdf/3.0");
     //Load the geo ontology
     model.read("http://www.w3.org/2003/01/geo/wgs84_pos.rdf");
 
	 
	 /*
	 
	   xmlns:j.0="http://www.mindswap.org/~glapizco/simpleABC.owl#"
		    xmlns:j.1="http://dbpedia.org/ontology/"
		    xmlns:foaf="http://xmlns.com/foaf/0.1/"
		    xmlns:j.2="http://xmlns.com/wordnet/1.6/"
		    xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
		    xmlns:owl="http://www.w3.org/2002/07/owl#"
		    xmlns:m="http://www.kanzaki.com/ns/music#"
		    xmlns:j.3="http://dbpedia.org/property/"
		    xmlns:j.4="http://www.megginson.com/exp/ns/airports#"
		    xmlns:j.5="http://www.mindswap.org/~glapizco/technical.owl#"
      */
	 
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

            "CONSTRUCT { "+
			"?s rdf:type foaf:Agent; "+
			"foaf:name ?name; "+
			"dbpedia2:abstract ?abstract; "+
			"dbpedia2:yearsActive ?yearsActive; "+
			"dbpedia2:genre ?genre; "+
			"dbpedia-owl:thumbnail ?thumbnail; "+
			"dbpedia2:currentMembers ?currentMembers. "+
			"?currentMembers foaf:name ?member_name. "+
			"?s dbpedia2:url ?url. " +
			//Photos
			"?s foaf:img ?img."+
			//Videos
			"?s mindswap:Video ?Video."+
			"?Video dc:title ?video_tile."+
			"?Video dc:identifier ?video_id."+
			"?Video mindswap:hasDuration ?video_duration."+
			//Events
			"?s m:Concert ?Concert."+
			"?Concert dc:title ?evnt_tile."+
			"?Concert dc:date ?evnt_date."+
			"?Concert vCard:URL ?evnt_url."+
			"?Concert dc:identifier ?evnt_venue. "+
			"?Concert m:event_description ?evnt_venue."+
			"?evnt_venue dc:title ?evnt_venue_title."+
			"?Concert vCard:Street ?evnt_street."+
			"?Concert vCard:Locality ?evnt_locality."+
			"?Concert vCard:Pcode ?evnt_pcode."+
			"?Concert vCard:Country ?evnt_country."+
	   		"?Concert geo:lat ?evnt_lat. "+
			"?Concert geo:long ?evnt_long. "+

			
		//	"?evnt_venue vCard:ADRTYPES ?evnt_venue_adr."+
		//	"?evnt_venue_adr vCard:Street ?evnt_street."+
		//	"?evnt_venue_adr vCard:Locality ?evnt_locality."+
		//	"?evnt_venue_adr vCard:Pcode ?evnt_pcode."+
		//	"?evnt_venue_adr vCard:Country ?evnt_country."+
			
			"} "+
			"WHERE {" +
			"?s rdf:type foaf:Agent. "+
			"?s foaf:name ?name. "+
			"?s dbpedia2:abstract ?abstract. "+
			"OPTIONAL { ?s dbpedia2:currentMembers ?currentMembers." +
			"?currentMembers foaf:name ?member_name. }. "+
			"?s  dbpedia-owl:thumbnail ?thumbnail. "+
			"?s dbpedia2:yearsActive ?yearsActive. "+
			"?s  dbpedia2:genre ?genre. "+
			"?s  dbpedia2:url ?url. "+
			//Images
			"?Image rdf:type foaf:Image. "+
			"?Image  foaf:depicts ?m. "+
			"?m foaf:name ?name. "+
			"?Image foaf:img ?img. "+
			//Videos
			"?Video rdf:type mindswap:Video. "+
			"?Video dc:title ?video_title. "+
			"?Video dc:identifier ?video_id. "+
			"?Video mindswap:hasDurationSeconds ?video_duration. "+
			"?Video mindswap:depicts ?x. "+
			"?x foaf:name ?name. "+
			//Events
			"?Concert rdf:type m:Concert. "+
			"?Concert dc:date ?evnt_date. "+
			"?Concert m:performerName ?name. "+
			"?Concert vCard:URL ?evnt_url. "+
			"?Concert dc:identifier ?evnt_venue. "+
			"?Concert vCard:Street ?evnt_street."+
			"?Concert vCard:Locality ?evnt_locality."+
			"?Concert vCard:Pcode ?evnt_pcode."+
			"?Concert vCard:Country ?evnt_country."+
    		"?Concert geo:lat ?evnt_lat. "+
			"?Concert geo:long ?evnt_long. "+
			
			
		//	"?evnt_venue rdf:type m:Venue. "+
		//	"?Concert m:event_description ?evnt_venue. "+
		//	"?evnt_venue dc:title ?evnt_venue_title. "+			
	//  	"?evnt_venue_adr rdf:type vCard:ADRTYPES."+
	//		"?evnt_venue vCard:ADRTYPES ?evnt_venue_adr."+
	//		"?evnt_venue_adr vCard:Street ?evnt_street."+
	//		"?evnt_venue_adr vCard:Locality ?evnt_locality."+
	//		"?evnt_venue_adr vCard:Pcode ?evnt_pcode."+
	//		"?evnt_venue_adr vCard:Country ?evnt_country."+
			
			"FILTER (regex(?name, \""+keyword+"\")). "+
			"}";
            		
		Query query = QueryFactory.create(queryString);
		//Query local model
		QueryExecution local_exec= QueryExecutionFactory.create(query,model);
		try {			
			 //ResultSet results = local_exec.execSelect();
	    	 qmodel = local_exec.execConstruct(); 
			 ByteArrayOutputStream rdf_stream= new ByteArrayOutputStream();
			 qmodel.write(rdf_stream);
			 return rdf_stream.toString();	    
            } 
          finally 
            {
        	  local_exec.close() ;
            }  
		
	}
	

}
