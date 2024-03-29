package iwa1.datasources;

import iwa1.semanticframework.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;




public class DataLastFm {
	public static String  lastfm(String method, String property, String keyword) {
	try{	
	  //set method URL
		URL url = new URL("http://ws.audioscrobbler.com/2.0/?method=" +
				URLEncoder.encode(method, "UTF-8") + "&" + URLEncoder.encode(property, "UTF-8")  +"=" +
				URLEncoder.encode(keyword, "UTF-8") +"&api_key=a34d984da70897b3e4959c707d893c4e");
		URLConnection urlc = url.openConnection();
		urlc.setDoOutput(true);
		urlc.setAllowUserInteraction(false);
                     		
		//String return
		BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

		StringBuffer sb = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line+"\n");
		}
		br.close();
		return sb.toString();
		
	  }
	catch(Exception e) {
        e.printStackTrace();
        return null;
	}

  }

	public static String Lastfm_rdf (String lastfm_xml)
	{
		  //Perfoms the XSL Transformation to RDF/XML using the RdfProducer class	
		  String xsl_source = "/Users/"+System.getProperty("user.name")+"/Documents/workspace/iwa_alpha/WebContent/XSL/datasources/lastfm_evnt_artist.xsl";
		  String lastfm_rdf = RdfProducer.XmlToRdf(lastfm_xml, xsl_source);	
		  return lastfm_rdf;
	}
	

	
}
