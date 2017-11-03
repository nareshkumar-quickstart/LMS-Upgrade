package com.softech.vu360.lms.model;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class AffidavitTemplateTest extends TestBaseDAO<AffidavitTemplate> {


	@Before
	public void setRequiredService() {
	}
	
//	@Test
	public void AffidavitTemplate_should_save(){
		
		AffidavitTemplate affidavitTemplate = new AffidavitTemplate();
		VU360User vu360User = (VU360User)crudFindById(VU360User.class, new Long(3));
		affidavitTemplate.setCreatedVU360User(vu360User);
		affidavitTemplate.setCreatedDate(new Date());
		affidavitTemplate.setStatus(true);
		affidavitTemplate.setTemplateGUID("Test_Template_GUID");
		affidavitTemplate.setTemplateName("Test_Template_Name");
		try{
			affidavitTemplate=save(affidavitTemplate);	
		}
		catch(Exception ex){
			System.out.println(affidavitTemplate.getId());
		}
		
	}
	
	@Test
	public void AffidavitTemplate_should_update(){
		AffidavitTemplate template=(AffidavitTemplate)crudFindById(AffidavitTemplate.class, new Long(1053));
		template.setTemplateName("Test_Template_Name_Updated");
		update(template);
	}
	
	@Test
		public void AffidavitTemplate1(){
			try{
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
				HttpEntity entity = new HttpEntity(headers);
				String url = "https://dev-itskills.quickstart.com/rest/default/V1/itskills-expertconnect/experties?sku=acb66c7336bb483c83fafb2c5963a8c4";
			//	ExpertiesResponse expertiesResponse =  restTemplate.getForObject(url, ExpertiesResponse.class);  // .postForEntity(url, entity);
				String response =  restTemplate.getForObject(url, String.class); 
	
				
				
				//ObjectMapper mapper = new ObjectMapper();
				//ExpertiesResponse expertiesResponse = mapper.readValue("{\"product\":{"+respone+"}", ExpertiesResponse.class);
				
				//JSONObject JSO = JSONObject.fromObject("{\"product\":{\"topics\":[\"Java\",\"Javascript\",\"Microsoft\",\"Apple\"],\"guid\":[\"java\",\"javascript\",\"microsoft\",\"apple\"],\"message\":\"Success\"}}");
				JSONObject JSO = JSONObject.fromObject("{\"product\":"+ StringUtils.strip(response.replace("\\", ""), "\"") +"}");
				//RestTemplate restTemplate = new RestTemplate();
				//ResponseEntity<ExpertiesResponse> person =  restTemplate.getForEntity("https://dev-itskills.quickstart.com/rest/default/V1/itskills-expertconnect/experties?sku=acb66c7336bb483c83fafb2c5963a8c4", ExpertiesResponse.class);
				JSONObject parentObject = (JSONObject) JSO.get("product");
		        
		       List<String> topics = ((List<String>)parentObject.get("topics"));
		       List<String> guid = (List<String>)parentObject.get("guid");
		       String message = (String)parentObject.get("message");
		       
		       StringBuffer returnMessage = new StringBuffer();
		       if(message.equalsIgnoreCase("Success")){
		    	   if(topics.size()>0){
		    		   returnMessage.append(String.join(",", topics));
		    		   returnMessage.append("|" );
		    		   returnMessage.append(String.join(",", guid));
		    	   }
		       }else{
		    	   //return message;
		       }
		        System.out.println("aaa"  + returnMessage);
			}
		    catch (final Exception e) {
		        System.out.println(e);
		        
		    }
		}
		
}
