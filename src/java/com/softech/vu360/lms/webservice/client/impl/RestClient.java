package com.softech.vu360.lms.webservice.client.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.apache.axis.utils.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.softech.vu360.util.VU360Properties;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.webservice.client.RestOperations;

public class RestClient implements RestOperations{

	private static final Logger log = Logger.getLogger(RestClient.class);
	
	/*
	 * Make the HTTP POST request, marshaling the request(java-object) to JSON,
	 * and the response to a String
	 */
	public String postForObject(final Object obj, final String servicePath,final int connTimeOut) throws IOException {
		final String POST_JSON = JSONObject.fromObject(obj).toString();
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),connTimeOut);
		HttpPost httpPost = new HttpPost(servicePath);
		StringEntity entity = new StringEntity(POST_JSON, "UTF-8");
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		// httpPost.getParams().setBooleanParameter("http.protocol.expect-continue",false);
		entity.setContentType(basicHeader);
		httpPost.setEntity(entity);
		HttpResponse predictResponse = httpClient.execute(httpPost);
		InputStream inStream = predictResponse.getEntity().getContent();
		return read(inStream);

	}

	
	public String getExpertise(String parmGuid){
		String endpoint = VU360Properties.getVU360Property("magento.endpoint.getexpertisebyGuid");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		
		try{
			String url = endpoint+""+parmGuid;
			String response =  restTemplate.getForObject(url, String.class); 
			JSONObject JSO = JSONObject.fromObject("{\"expertiseResponse\":"+ StringUtils.strip(response.replace("\\", ""), "\"") +"}");
			
			
			JSONObject parentObject = (JSONObject) JSO.get("expertiseResponse");
			    
			List<String> topics = ((List<String>)parentObject.get("topics"));
			List<String> guid = (List<String>)parentObject.get("guid");
			String message = (String)parentObject.get("message");
			   
			StringBuffer returnMessage = new StringBuffer();
			if(message.equalsIgnoreCase("Success")){
			   if(topics.size()>0){
				   returnMessage.append(String.join(",", topics));
				   returnMessage.append("|" );
				   returnMessage.append(String.join(",", guid));
			   }else{
				   returnMessage.append("Not Available");
			   }
			}else{
				   return message;
			}
			
			return returnMessage.toString();
		}catch(Exception ex){
			return "ExceptionCaught: " + ex.getMessage();
		}
	}
	
	public String postForObject(Object obj, String servicePath)
			throws IOException {
		return postForObject(obj, servicePath, 10000);
	}

	/*
	 * Make the HTTP GET request, marshaling the the response to a String
	 */
	public String get(final String servicePath,final int connTimeOut) throws IOException {
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),connTimeOut);
		HttpGet httpGet = new HttpGet(servicePath);
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		httpGet.setHeader(basicHeader );
		HttpResponse predictResponse = httpClient.execute(httpGet);
		InputStream inStream = predictResponse.getEntity().getContent();
		return read(inStream);
	}

	/*
	 * Make the HTTP GET request, making the response to JSONObject
	 */
	public JSONObject getForObject(final String servicePath,final int connTimeOut) throws IOException {
		final HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),connTimeOut);
		HttpGet httpGet = new HttpGet(servicePath);
		BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE,"application/json");
		httpGet.setHeader(basicHeader );
		HttpResponse predictResponse = httpClient.execute(httpGet);
        JSONObject jsonObj = JSONObject.fromObject(read(predictResponse.getEntity().getContent()));
		int status = predictResponse.getStatusLine().getStatusCode();
		if(status == 200){
			// OK
			return jsonObj;
		}else{
			// Error occur
			return jsonObj;
		}
	}

	public JSONObject postFile(String servicePath, File file) throws IOException {
		return postFile(servicePath, file, (5*60*1000));
	}
	
	public JSONObject postFile(String servicePath, File file, int timeout) throws IOException {
		log.debug("posting file=" + file.getName() + " on " + servicePath);
		HttpClient httpClient = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), timeout);
		HttpPost httpPost = new HttpPost(servicePath);
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		multipartEntity.addPart(file.getName(), new FileBody(file));
		httpPost.setEntity(multipartEntity);
		HttpResponse response = httpClient.execute(httpPost);
		log.debug(response.getStatusLine());
		log.debug(response.getEntity().getContentType());
		String responseString = read(response.getEntity().getContent());
		log.debug(responseString);
		JSONObject jsonObj = JSONObject.fromObject(responseString);
		return jsonObj;
	}
	
	private String read(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(in),
				1024);
		for (String line = bReader.readLine(); line != null; line = bReader.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}
}
