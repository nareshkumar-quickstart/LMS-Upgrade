package com.softech.vu360.lms.web.restful.request.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TestUtil {
	
	public static void callwebService(String userPass, String apiKey, String url,  String method, String jsonToWrite) {
	
    	HttpURLConnection conn = null;
    	try {
    		conn = getUrlConnection(url);
    		setConnectionProperties(conn, method, apiKey);
    		writeJsonToConnStream(conn, jsonToWrite);
    		printResponse(conn);
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		if (conn != null) {
    			conn.disconnect();
    		}
    	}
	}
	
	public static void callHttpsWebService(String userPass, String apiKey, String url,  String method, String jsonToWrite) {
		
    	HttpURLConnection conn = null;
    	try {
    		conn = getHttpsConnection(url);
    		setConnectionProperties(conn, method, apiKey);
    		writeJsonToConnStream(conn, jsonToWrite);
    		printResponse(conn);
    	} catch(Exception e) {
    		e.printStackTrace();
    	} finally {
    		if (conn != null) {
    			conn.disconnect();
    		}
    	}
	}
	
	public static HttpURLConnection getUrlConnection(String url) throws MalformedURLException, IOException {
			
		String errorMessage = null;
		HttpURLConnection conn = null;
			
		try {
			URL connectionUrl = new URL(url);
		    conn = (HttpURLConnection) connectionUrl.openConnection();
		} catch (MalformedURLException e) {
			errorMessage = e.getMessage();
			System.out.println(errorMessage);
		} catch (IOException e) {
			errorMessage = e.getMessage();
			System.out.println(errorMessage);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			System.out.println(errorMessage);
		}
	    return conn;
	}
	
	public static HttpsURLConnection getHttpsConnection(String url) throws Exception {
		
		HttpsURLConnection urlConnection = null;
		
		// Create a trust manager that does not validate certificate chains
	    final TrustManager[] trustAllCerts = new TrustManager[] { 	
	    		new X509TrustManager() {   	
	    			public void checkClientTrusted( final X509Certificate[] chain, final String authType ) {
	    				//do nothing
	                }
	        
	                public void checkServerTrusted( final X509Certificate[] chain, final String authType ) {
	                	//do nothing
	                }
	        
	                public X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
	    		}		
	    } ;
	    
	    // Install the all-trusting trust manager
	    final SSLContext sslContext = SSLContext.getInstance("SSL");
	    
	    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
	    	 
	    // Create all-trusting host name verifier
	    HostnameVerifier allHostsValid = new HostnameVerifier() {            
	    	public boolean verify(String hostname, SSLSession session) {               
	    		return true;         
	    	}	        
	    };
	    
	   
	   //System.setProperty("javax.net.ssl.trustStore","D://Java//jdk1.6.0_43//jre//lib//security//cacerts");
	   //System.setProperty("javax.net.ssl.trustStorePassword","changeit");
	   //System.setProperty("javax.net.debug ","ssl"); 
	    	 
	    // Install the all-trusting host verifier
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    
	   // sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
	    
	    // Create an ssl socket factory with our all-trusting manager
	    //final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		
		try {
			URL myUrl = new URL(url);
			urlConnection = (HttpsURLConnection) myUrl.openConnection();
			
			// Tell the url connection object to use our socket factory which bypasses security checks
		    //( (HttpsURLConnection) urlConnection ).setSSLSocketFactory( sslSocketFactory );
			
		} catch (Exception e) {
			throw new Exception("Error in getting connecting to url: " + url + " :: " + e.getMessage());	
		}
		
		return urlConnection;
		
	} //end of getHttpsConnection()
	
	public static void setConnectionProperties(HttpURLConnection conn, String method, String key) {
		
		String charset = "UTF-8";
		try {
			if ("POST".equalsIgnoreCase(method)) {        
				conn.setDoOutput(true);   // set request method to POST
			} else if ("GET".equalsIgnoreCase(method)) {  
				conn.setDoOutput(false);  // set request method to GET
			}
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Charset", charset);
	        conn.setRequestProperty("key", key);
		} catch (Exception e) {
			
		}
	}
	
	public static void printResponse(HttpURLConnection conn) throws IOException {
			
		try {
			if (conn.getResponseCode() != 200) {
				InputStream errorStream = conn.getErrorStream();
				printResponse(errorStream);
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode() + "-" + conn.getResponseMessage());
			}
			InputStream inStream = conn.getInputStream();
			printResponse(inStream);
			inStream.close();
		} catch (IOException e) {
			String errorMessage = e.getMessage();
			System.out.println(errorMessage);
		}
	}
		
	private static void printResponse(InputStream inStream) throws IOException {
			
		String sCurrentLine;
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inStream))) {
			while ((sCurrentLine = br.readLine()) != null) {
			    System.out.println(sCurrentLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void writeJsonToConnStream(HttpURLConnection connection, String json) {
		
		if (json == null || json.isEmpty()) {
			return;
		}
		
		try (OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream())) {
			out.write(json);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	} //end of writeJsonToConnStream()
	
} //end of class TestUtil
