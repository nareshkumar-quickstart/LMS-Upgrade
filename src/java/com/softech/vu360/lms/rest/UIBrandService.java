package com.softech.vu360.lms.rest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import org.apache.log4j.Logger;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.softech.JWTValidation.JwtValidation;
import com.softech.JWTValidation.model.JwtPayload;
import com.softech.vu360.lms.model.VU360User;

import com.softech.vu360.lms.service.impl.VU360UserServiceImpl;
import com.softech.vu360.lms.web.restful.request.BrandRequest;
import com.softech.vu360.lms.web.restful.request.BrandResponse;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

           
@RestController
@RequestMapping(value = "/uibrand")
public class UIBrandService {
	private static final Logger log = Logger.getLogger(UIBrandService.class);
	
	//@Inject
	//BrandService brandingService;
	
	@Inject
	VU360UserServiceImpl vU360UserServiceImp;
	
	
	@RequestMapping(value="brandIds", method=RequestMethod.POST,headers = "Accept=application/json",consumes = "application/json", produces = "application/json")
	@ResponseBody
	public BrandResponse getBrand(@RequestBody BrandRequest brandIds,@RequestHeader(value="token") String token)	
	{			
		// calling jar
		JwtPayload jwtPayload = null;
		try{
		jwtPayload = JwtValidation.validateJWTToken(token);		
		}catch(Exception e){
			return new BrandResponse();
		}
		if (jwtPayload == null){
			return new BrandResponse();
		}
		//  end calling jar	
		
		List<VU360User> user = vU360UserServiceImp.getActiveUserByUsername(jwtPayload.getUser_name());
		Iterator<VU360User> iterator = user.iterator();
		VU360User userItr = null;
		while (iterator.hasNext()) {
			userItr= (VU360User)iterator.next();			
		}
		
		//todo
		Brander brand=VU360Branding.getInstance().getBranderByUser(null,userItr);			
		
		String localUrl = 	VU360Properties.getVU360Property("lms.domain");
		localUrl = localUrl+"/lms/";
		List reqList=brandIds.getBrandKeyValue();
		Iterator it = reqList.iterator();	
		
		BrandResponse response = new BrandResponse();
		Map<String, String> aMap = new HashMap();
		
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = brand.getBrandElement(key);
			if (key.equals("lms.header.logo.src")){				
				value = localUrl+value;
			}			
			aMap.put(key, value);
		}
		
		if(brand.getName().equals("default")){
			String contactusUrl = brand.getBrandElement("lms.login.footer.contactusURL");	
			contactusUrl = localUrl+contactusUrl;
			aMap.put("contactusContent", null);
			aMap.put("contactusURL", contactusUrl);
			
			String touUrl = brand.getBrandElement("lms.login.footer.termofuserURL");
			touUrl = localUrl+touUrl;
			aMap.put("touContent", null);
			aMap.put("touURL", touUrl);
			
			String privacyUrl = brand.getBrandElement("lms.login.footer.onlineprivacypracticesURL");
			privacyUrl = localUrl+privacyUrl;
			aMap.put("privacyContent", null);
			aMap.put("privacyURL", privacyUrl);
			
		}else{		
		// contact us			
			String contactusSelection = brand.getBrandElement("lms.branding.contactus.selection");			
			String contactusEmail = brand.getBrandElement("lms.branding.contactus.email");
			contactusEmail = "to:"+contactusEmail;
			String contactustemplateText = brand.getBrandElement("lms.branding.contactus.templateText");
			String contactUrl = brand.getBrandElement("lms.branding.contactus.URL");			
			String contactusContent = "contactusContent";
			String contactusResponseURL = "contactusURL";			
			if(contactusSelection.equals("1")){
				aMap.put(contactusContent, contactustemplateText);
				aMap.put(contactusResponseURL, null);
			}else if(contactusSelection.equals("2")){
				aMap.put(contactusContent, null);
				aMap.put(contactusResponseURL, contactUrl);
			}else if(contactusSelection.equals("3")){
				aMap.put(contactusContent, null);
				aMap.put(contactusResponseURL, contactusEmail);
			}
		//end contact us
		// terms of use		
			String touSelection = brand.getBrandElement("lms.branding.tou.selection");			
			String touEmail = brand.getBrandElement("lms.branding.tou.email");
			touEmail = "to:"+touEmail;
			String toutemplateText = brand.getBrandElement("lms.branding.tou.templateText");
			String touUrl = brand.getBrandElement("lms.branding.tou.URL");			
			String touContent = "touContent";
			String touResponseURL = "touURL";			
			if(touSelection.equals("1")){
				aMap.put(touContent, toutemplateText);
				aMap.put(touResponseURL, null);
			}else if(touSelection.equals("2")){
				aMap.put(touContent, null);
				aMap.put(touResponseURL, touUrl);
			}else if(touSelection.equals("3")){
				aMap.put(touContent, null);
				aMap.put(touResponseURL, touEmail);
			}			
		//end terms of use
		// privacy		
			String privacySelection = brand.getBrandElement("lms.branding.privacy.selection");			
			String privacyEmail = brand.getBrandElement("lms.branding.privacy.email");
			privacyEmail = "to:"+privacyEmail;
			String privacytemplateText = brand.getBrandElement("lms.branding.privacy.templateText");
			String privacyUrl = brand.getBrandElement("lms.branding.privacy.URL");			
			String privacyContent = "privacyContent";
			String privacyResponseURL = "privacyURL";			
			if(privacySelection.equals("1")){
				aMap.put(privacyContent, privacytemplateText);
				aMap.put(privacyResponseURL, null);
			}else if(privacySelection.equals("2")){
				aMap.put(privacyContent, null);
				aMap.put(privacyResponseURL, privacyUrl);
			}else if(privacySelection.equals("3")){
				aMap.put(privacyContent, null);
				aMap.put(privacyResponseURL, privacyEmail);
			}			
		//end privacy	
		}
		response.setBrandKeyValue(aMap);
		
        return response;

	}
	
	private String getResponse(String restEndPoint,String token) throws Exception {
		
		URL url = new URL(restEndPoint);        
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        
        /**
         * To do a POST with HttpURLConnection, you need to write the parameters to the connection after you have opened the 
         * connection.
         */
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
       
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
        	 sb.append((char)c);
        }
        String response = sb.toString().trim().replaceAll("[\r\n]", "");
        response = response.toString().trim().replaceAll(" ", "");
        
        return response;
		
	}
	
	
}

