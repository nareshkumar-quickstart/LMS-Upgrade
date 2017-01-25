package com.softech.vu360.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebProxyNetworkUtil {
	
	public static HttpURLConnection getHttpConnection(String url) throws Exception {
		
		HttpURLConnection urlConnection = null;
		try {
			URL myUrl = new URL(url);
			urlConnection = (HttpURLConnection) myUrl.openConnection();
		} catch (Exception e) {
			throw new Exception("Error in getting connecting to url: " + url + " :: " + e.getMessage());
		}
		
		return urlConnection;
		
	} //end of getHttpConnection()

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
	
	    // Install the all-trusting host verifier
	    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		try {
			URL clientUrl = new URL(url);
			urlConnection = (HttpsURLConnection) clientUrl.openConnection();
			
			// Tell the url connection object to use our socket factory which bypasses security checks
		    //( (HttpsURLConnection) urlConnection ).setSSLSocketFactory( sslSocketFactory );
			
		} catch (Exception e) {
			throw new Exception("Error in getting connecting to url: " + url + " :: " + e.getMessage());	
		}
		
		return urlConnection;
		
	} //end of getHttpsConnection()
	
	public static String getResponseAsString(String certificateFilePath, String httpsUrl) throws Exception {
		
		URL url = new URL(httpsUrl);            
        SSLExcludeCipherConnectionHelper sslExclHelper = new SSLExcludeCipherConnectionHelper(certificateFilePath);
        String response = sslExclHelper.getResponseAsString(url);
        return response;
		
	}
	
	public static InputStream getResponseAsInputStream(String certificateFilePath, String httpsUrl) throws Exception {
		
		URL url = new URL(httpsUrl);            
        SSLExcludeCipherConnectionHelper sslExclHelper = new SSLExcludeCipherConnectionHelper(certificateFilePath);
        InputStream response = sslExclHelper.getResponseAsInputStream(url);
        return response;
		
	}
	
}
