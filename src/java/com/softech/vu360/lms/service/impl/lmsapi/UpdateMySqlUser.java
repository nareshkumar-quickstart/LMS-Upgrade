package com.softech.vu360.lms.service.impl.lmsapi;

import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateMySqlUser implements Runnable {

	private String url;
	
	public UpdateMySqlUser(String url) {
		super();
		this.url = url;
	}
	
	@Override
	public void run() {
		
		try {
			sendGet(url);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			System.out.println("Error in calling URL: " + url);
		}	
	}
	
	// HTTP GET request
	private void sendGet(String url) throws Exception {
	 
		//String url = "http://www.google.com/search?q=mkyong";
	 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
		// optional default is GET
		con.setRequestMethod("GET");
			
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		
		/**
		 * 
		 
		BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
	    */
	}

}
