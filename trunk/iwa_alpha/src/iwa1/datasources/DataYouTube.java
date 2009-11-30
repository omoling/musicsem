package iwa1.datasources;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class DataYouTube {
	public static String youtube(String keyword) {
		try
		{
			//set method URL
			URL url = new URL("http://gdata.youtube.com/feeds/api/videos??q=" +
					URLEncoder.encode(keyword, "UTF-8") +"&key=AI39si7EoXyvmGkxvEVXmxhzGld68uvsX8N4zZC7v1k7m6lNCo7ZsAesh58KhF_KhOq92jnTZcul4ct_Fj7hBA0XL4qnSes70Q");
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
