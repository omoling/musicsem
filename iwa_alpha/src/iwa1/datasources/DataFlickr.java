package iwa1.datasources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DataFlickr {
	public static String searchFlickr(String keyword) {
		try
		{
			//set method URL
			URL url = new URL("http://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=6d3d002ca456197a60df58cec7a9fadb&text=" +
					URLEncoder.encode(keyword, "UTF-8") );
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

}
