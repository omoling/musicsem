package iwa1.servlets;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import iwa1.semanticframework.*;
import iwa1.datasources.*;

/**
 * Application Lifecycle Listener implementation class SessionManager
 *
 */
public class SessionManager  implements HttpSessionListener {

	/**
     * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
     */
    public void sessionDestroyed(HttpSessionEvent arg0) {
    	DataYouTube.youtube_model.close();
    	DataFlickr.flickr_model.close();
    	DataDBpedia.dbpedia_model.close();
    	JenaFrame.model.removeAll();
    	JenaFrame.model.close();
    }

	/**
     * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
     */
    public void sessionCreated(HttpSessionEvent arg0) {

        //Initialize Main Model
		JenaFrame.init();
    }
	
}
