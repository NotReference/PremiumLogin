package it.notreference.bungee.premiumlogin.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 
 * PremiumLogin 1.6 By NotReference
 * 
 * @author NotReference
 * @version 1.6
 * @destination BungeeCord
 *
 */

/**
 * 
 * @since 1.5
 * 
 */
public class HTTPClient {

	/**
	 * 
	 * Read all contents of an url.
	 * 
	 * @param urlString
	 * @return The content of an url.
	 * @throws Exception
	 */
	private static String readUrl(String urlString) throws Exception {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	    	HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
	    	conn.setDoOutput( true );
	    	conn.setInstanceFollowRedirects( false );
	    	conn.setRequestMethod( "GET" );
	    	conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
	    	conn.setRequestProperty( "charset", "utf-8");
	    	conn.addRequestProperty("User-Agent", "Chrome");
	    	conn.setUseCaches( false );
	        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 

	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}

	/**
	 * 
	 * Returns a JSON Value.
	 * 
	 * @param valueName
	 * @param targetUrl
	 * @return
	 */
	public static String getValue(String val, String url) {
	try {
		JsonObject j = new Gson().fromJson(readUrl(url), JsonObject.class);
        double risultato = j.get(val).getAsDouble();
        return Double.toString(risultato);
        
	} catch (Exception e) {
		 e.printStackTrace();
		return "ERROR";
	}
	}
	
}
