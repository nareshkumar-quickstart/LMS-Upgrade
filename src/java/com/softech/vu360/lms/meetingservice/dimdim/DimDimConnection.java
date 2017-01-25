package com.softech.vu360.lms.meetingservice.dimdim;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

/**
 * @author Faisal Ahmed Siddiqui
 */
public class DimDimConnection {

	Logger log = Logger.getLogger(DimDimConnection.class);
	public DimDimResponse sendAndReceive(DimDimRequest request){
		log.debug(request.toJSON().toString());
		JSONObject jsonResponse = sendAndReceive(request.getURL(),request.toJSON(),request.getAuthToken());
		DimDimResponse response = null;
		if(request instanceof DimDimLoginRequest){
		
			response = new DimDimLoginResponse();
		
		} else if(request instanceof DimDimReadUserRequest){
			
			 response = new DimDimReadUserResponse();
		
		}else if (request instanceof DimDimUpdateUserRequest){
		
			response = new DimDimUpdateUserResponse();
		
		}else if(request instanceof DimDimCreateUserRequest){
		
			response = new DimDimCreateUserResponse();
		
		}else if (request instanceof DimDimCheckMeetingRequest){
		
			response = new DimDimCheckMeetingResponse();
		
		}else if (request instanceof DimDimStartMeetingRequest){
		
			response = new DimDimStartMeetingResponse();
		
		}
		response.setResult(jsonResponse.getString("result"));
		response.setJsonResponse(jsonResponse);
		response.parse(jsonResponse);
		return response;
	}
	private JSONObject sendAndReceive(String URL, JSONObject request,String authToken) {
		HttpURLConnection connection = null;
		BufferedReader rd = null;
		StringBuilder result = null;
		String line = null;
		JSONObject response = null;
		try {

			log.debug("URL:" + URL);

			URL serverAddress = null;

			serverAddress = new URL(URL);
			// set up out communications stuff
			connection = null;
			
			StringBuffer content = new StringBuffer();
			// content.append("{\"request\":{\"account\":\"faisal.ahmed\",\"password\":\"360training!\",\"groupName\":\"all\"}}");
			content.append(request.toString());
			// Set up the initial connection
			connection = (HttpURLConnection) serverAddress.openConnection();
			connection.setRequestMethod("POST");
			if(authToken!=null){
				//connection.setRequestProperty("authToken", authToken);
				log.debug("setting authToken:"+authToken);
				connection.setRequestProperty("X-Dimdim-Auth-Token", authToken);
			}
			//connection.setRequestProperty("Referer", "http://dimdim1.360training.com/testapi");
							
			connection.setRequestProperty("CONTENT-TYPE",
					"application/x-www-form-urlencoded; charset=UTF-8");
					//"text/html; charset=UTF-8");
					//"application/x-www-form-urlencoded;charset=utf-8");
//			connection.setRequestProperty("Content-Length", ""
//					+ content.toString().getBytes().length);
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);
			// connection.setDoInput(true);

			// Send POST output.
			OutputStreamWriter wr = new OutputStreamWriter(connection
					.getOutputStream());
			wr.write(content.toString());
			wr.flush();

			connection.connect();

			// read the result from the server
			rd = new BufferedReader(new InputStreamReader(connection
					.getInputStream()));
			result = new StringBuilder();

			while ((line = rd.readLine()) != null) {
				result.append(line + '\n');
			}
			wr.close();
			log.info("message back from LMS putparam:" + result.toString()+" length="+result.length());
			response = JSONObject.fromObject(result.toString());
		} catch (Exception e) {
			log.error("error during putparam call:" + e.getMessage(), e);
		} finally {
			// close the connection, set all objects to null
			if (connection != null) {
				connection.disconnect();
			}
			rd = null;
			connection = null;
		}
		return response;
	}

}
