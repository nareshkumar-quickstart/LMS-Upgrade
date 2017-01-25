package com.softech.vu360.lms.meetingservice.webex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.softech.vu360.lms.meetingservice.MeetingServiceException;

public class Transport {

	public static WebExResponse sendAndReceive(WebExRequest request)throws MeetingServiceException
	{
        
		WebExResponse response = getResponseObject(request);;
		URLConnection urlConnection = null;
		try
		{
			urlConnection = openConnection();
		}catch (MalformedURLException e) 
		{
			response.setStatus(WebExResponse.ERROR);
			response.setErrorCode("401");
			response.setErrorReason(e.getMessage());
		}catch(IOException ioe)
		{
			response.setStatus(WebExResponse.ERROR);
			response.setErrorCode("402");
			response.setErrorReason(ioe.getMessage());
		}
		try
		{
			writeRequest(urlConnection,request);
		}catch(IOException ioe)
		{
			response.setStatus(WebExResponse.ERROR);
			response.setErrorCode("501");
			response.setErrorReason(ioe.getMessage());
		}
		String strResponse = null;
		try
		{
			strResponse = readResponse(urlConnection);
		}catch(IOException ioe)
		{
			response.setStatus(WebExResponse.ERROR);
			response.setErrorCode("502");
			response.setErrorReason(ioe.getMessage());
		}
		try {
			handleResponse(strResponse,response, request);
		} catch (IOException e) {
			response.setStatus(WebExResponse.ERROR);
			response.setErrorCode("301");
			response.setErrorReason(e.getMessage());
			e.printStackTrace();
		}
			
		return response;
	}
	private static WebExResponse getResponseObject(WebExRequest request)
	{
		WebExResponse response=null;
		if(request instanceof JoinMeetingURLRequest)
		{
			response=new JoinMeetingURLResponse();
		}
		return response;
	}
	private static WebExResponse handleResponse(String strResponse,WebExResponse response, WebExRequest request) throws IOException
	{
		return response.deserialize(strResponse);
	}
	private static URLConnection openConnection() throws MalformedURLException,IOException{
        URL webExServer = new URL(WebExProperties.getServerURL());
        // URLConnection supports HTTPS protocol only with JDK 1.4+
        URLConnection urlConnection = webExServer.openConnection();
        return urlConnection;
	}
	private static void writeRequest(URLConnection connection,WebExRequest request) throws IOException{
		connection.setDoOutput(true);
        // send request
        PrintWriter out = new PrintWriter(connection.getOutputStream());
        out.println(request.serialize());
        out.close();
	}
	private static String readResponse(URLConnection connection) throws IOException
	{
        // read response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuffer responseXML=new StringBuffer();
        while ((line = in.readLine()) != null) {
        	responseXML.append(line);
        }
        in.close();
        return responseXML.toString();
	}
}
