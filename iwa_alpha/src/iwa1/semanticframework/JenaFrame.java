package iwa1.semanticframework;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.util.Iterator;



import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;


public class JenaFrame {
	public static Model model = ModelFactory.createDefaultModel();

	public static void init(String rdf_data)
	{

		
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
	

}
