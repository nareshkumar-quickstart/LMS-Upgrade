package com.softech.vu360.lms.webservice.client.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.webservice.client.LMSCourseApprovalPublishWSClient;
import com.softech.vu360.lms.webservice.message.lmsCourseApprovalPublish.CourseApproval;
import com.softech.vu360.lms.webservice.message.lmsCourseApprovalPublish.Credential;
import com.softech.vu360.lms.webservice.message.lmsCourseApprovalPublish.CredentialCategory;
import com.softech.vu360.lms.webservice.message.lmsCourseApprovalPublish.CredentialCategoryRequirement;
import com.softech.vu360.lms.webservice.message.lmsCourseApprovalPublish.LMSCourseApprovalRequest;
import com.softech.vu360.lms.webservice.message.lmsCourseApprovalPublish.LMSCourseApprovalResponse;
import com.softech.vu360.lms.webservice.message.lmsCourseApprovalPublish.Regulator;
import com.softech.vu360.util.VU360Properties;


/**
 * This WebService implemented in SF project and this client send course approval information to SF
 * Location: Accreditation Mode -> search and select Course Approval in edit mode-> Publish Approval to SF button 
 * TPMO-914, LMS-15308 
 * @author muhammad.rehan
 * @since 4.27.2 Credit Hours Mapping
 */

public class LMSCourseApprovalPublishWSClientImpl implements LMSCourseApprovalPublishWSClient{

	private static final Logger log = Logger.getLogger(LMSCourseApprovalPublishWSClientImpl.class.getName());
	public static final String STOREFRONT_SUGGESTEDCOURSES_ENDPOINT = VU360Properties.getVU360Property("storeFrontCourseApprovalPublishServiceEndPoint");
	public static final String LMS_COURSEAPPROVAL_WEBSERVICE_PACKAGE = "com.softech.vu360.lms.webservice.message.lmsCourseApprovalPublish";
	private AccreditationService accreditationService = null;
	
	@Override
	public String publishCourseApproval(com.softech.vu360.lms.model.CourseApproval objCA) throws Exception{		
		try{
			
		
			LMSCourseApprovalRequest lmsrequest = new LMSCourseApprovalRequest(); 
			lmsrequest.setCourseGuid(objCA.getCourse().getCourseGUID());
			
			//CourseGroup is collection so that many course group can link/attach with approval. But for now, there is 1 to 1 relationship
			if(objCA.getCourseGroups()!=null && objCA.getCourseGroups().size()>0)
				lmsrequest.setCourseGroupGUID(objCA.getCourseGroups().get(0).getGuid());
			else
				throw new Exception("Course Group is Missing!");
			
		
			CourseApproval mdlCA = new CourseApproval();
			mdlCA.setApprovedCourseName(avoidNullString(objCA.getApprovedCourseName()));
			mdlCA.setCourseApprovalNumber(avoidNullString(objCA.getCourseApprovalNumber()));
			
			if(objCA.getRegulatorCategory()!=null)
				mdlCA.setCourseApprovaltype(avoidNullString(objCA.getRegulatorCategory().getCategoryType()));
			else
				mdlCA.setCourseApprovaltype("");
			
			mdlCA.setApprovedCreditHours(avoidNullString(objCA.getApprovedCreditHours()));
			
			
			
			//====================================================================================================================================
			
			Regulator mdlRegulator = new Regulator();
			mdlRegulator.setName(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getName()));
			mdlRegulator.setRegulatorCategoryType(avoidNullString(objCA.getCourseApprovaltype()));
			
			
			try{
				mdlRegulator.setAddressLine1(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getAddress().getStreetAddress()));
			}catch(Exception sube){
				mdlRegulator.setAddressLine1("");
			}
			
			try{
				mdlRegulator.setAddressLine2(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getAddress().getStreetAddress2()));
			}catch(Exception sube){
				mdlRegulator.setAddressLine2("");
			}
			
			mdlRegulator.setPhone(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getPhone()));
			mdlRegulator.setCity(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getAddress().getCity()));
			mdlRegulator.setState(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getAddress().getState()));
			mdlRegulator.setFax(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getFax()));
			mdlRegulator.setJurisdiction(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getJurisdiction()));
			mdlRegulator.setZip(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getAddress().getZipcode()));
			mdlRegulator.setWebsite(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getWebsite()));
			mdlRegulator.setEmailAddress(avoidNullString(objCA.getRegulatorCategories().get(0).getRegulator().getEmailAddress()));
			
			
			List<CustomFieldValue> customFieldValueList = new ArrayList<CustomFieldValue>();
			customFieldValueList = objCA.getCustomFieldValues();
			String courseApprovalLink = null;
			if(customFieldValueList != null && customFieldValueList.size() > 0) {
				for(CustomFieldValue customFieldValue : customFieldValueList) {
					if(customFieldValue != null && customFieldValue.getCustomField() != null) {
						if(customFieldValue.getCustomField().getFieldLabel().equalsIgnoreCase("Course Approval Link")) {
							courseApprovalLink = (String) customFieldValue.getValue();
						}
					}
				}
			}
			
			mdlRegulator.setCourseApprovalLink(avoidNullString(courseApprovalLink)); 
			
			//====================================================================================================================================
			
			List <com.softech.vu360.lms.model.CredentialCategoryRequirement> requirements = objCA.getRequirements();
			Set<com.softech.vu360.lms.model.Credential> credentialSet = new HashSet<com.softech.vu360.lms.model.Credential>();
			Set<com.softech.vu360.lms.model.CredentialCategory> credentialCategorySet = new HashSet<com.softech.vu360.lms.model.CredentialCategory>();
			
			for (com.softech.vu360.lms.model.CredentialCategoryRequirement cReq : requirements) {
				credentialSet.add(cReq.getCredentialCategory().getCredential());
				credentialCategorySet.add(cReq.getCredentialCategory());
			}
			
			
			List<Credential> lstModelCredential = new ArrayList<Credential>();
			Credential mdlCrd ; 
			
			for(com.softech.vu360.lms.model.Credential fromDBCredential : credentialSet){
				
				mdlCrd = new Credential();
				mdlCrd.setOfficialLicenseName(avoidNullString(fromDBCredential.getOfficialLicenseName()));
				mdlCrd.setShortLicenseName(avoidNullString(fromDBCredential.getShortLicenseName()));
				mdlCrd.setCredentialType(avoidNullString(fromDBCredential.getCredentialType()));
				mdlCrd.setRenewalDeadlineType(avoidNullString(fromDBCredential.getRenewalDeadlineType()));
				mdlCrd.setStaggeredBy(avoidNullString(fromDBCredential.getStaggeredBy()));
				mdlCrd.setStaggeredTo(avoidNullString(fromDBCredential.getStaggeredTo()));
				mdlCrd.setRenewalFrequency(avoidNullString(fromDBCredential.getRenewalFrequency()));
				mdlCrd.setHardDeadlineMonth(avoidNullString(fromDBCredential.getHardDeadlineMonth()));
				mdlCrd.setHardDeadlineDay(avoidNullString(fromDBCredential.getHardDeadlineDay()));
				mdlCrd.setHardDeadlineYear(avoidNullString(fromDBCredential.getHardDeadlineYear()));
				mdlCrd.setDescription(avoidNullString(fromDBCredential.getDescription()));
				mdlCrd.setPreRequisite(avoidNullString(fromDBCredential.getPreRequisite()));
				
				
				CredentialCategory mdlCrdCategory;
				
				for(com.softech.vu360.lms.model.CredentialCategory fromDBCredentialCategory : credentialCategorySet){
					if(fromDBCredentialCategory.getCredential().getId()==fromDBCredential.getId()){
						mdlCrdCategory = new CredentialCategory();
						mdlCrdCategory.setName(avoidNullString(fromDBCredentialCategory.getName()));
						mdlCrdCategory.setHours(avoidNullString(fromDBCredentialCategory.getHours() + ""));
						mdlCrdCategory.setCategoryType(avoidNullString(fromDBCredentialCategory.getCategoryType()));
						
						
						List<com.softech.vu360.lms.model.CredentialCategoryRequirement> fromDBcredentialCategoryRequirement 
											= accreditationService.getCredentialCategoryRequirementsByCategory (fromDBCredentialCategory.getId());
						
						
						CredentialCategoryRequirement mdlCCR;
						for(com.softech.vu360.lms.model.CredentialCategoryRequirement fromDBCrdntlCtgryRqurmnt : fromDBcredentialCategoryRequirement){
							if(fromDBCrdntlCtgryRqurmnt.getCredentialCategory().getId() == fromDBCredentialCategory.getId()){
								mdlCCR = new CredentialCategoryRequirement();
								mdlCCR.setName(avoidNullString(fromDBCrdntlCtgryRqurmnt.getName()));
								mdlCCR.setDescription(avoidNullString(fromDBCrdntlCtgryRqurmnt.getDescription()));
								mdlCCR.setCreditHours(avoidNullString(fromDBCrdntlCtgryRqurmnt.getNumberOfHours() + ""));
								mdlCrdCategory.getCredentialCategoryRequirement().add(mdlCCR);
							}
						}
						
						mdlCrd.getCredentialCategory().add(mdlCrdCategory);
					}
				}
				
				lstModelCredential.add(mdlCrd);
			}
			
			mdlRegulator.getCredentials().addAll(lstModelCredential);
			mdlCA.setRegulator(mdlRegulator);
			
			
			lmsrequest.setCourseApproval(mdlCA);
			
			
			LMSCourseApprovalResponse response = (LMSCourseApprovalResponse)
				getWebServiceTemplate(STOREFRONT_SUGGESTEDCOURSES_ENDPOINT, LMS_COURSEAPPROVAL_WEBSERVICE_PACKAGE).marshalSendAndReceive(lmsrequest);

			return response.getResponseMessage();
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	 private String avoidNullString(String str) {
		 if( str == null)
			 return "" ;
		 else
			 return str ;
	 }
	
	
	
	private WebServiceTemplate getWebServiceTemplate(String endPoint, String packagePrefix)throws Exception{
		
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setDefaultUri(endPoint);
    	org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
    	org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
    	webServiceTemplate.setMarshaller( marshaller );
    	webServiceTemplate.setUnmarshaller( unmarshaller );
    	
    	//Need to set the contextpath which is just the complete package name used from ObjectFactory class
    	marshaller.setContextPath(packagePrefix);
    	unmarshaller.setContextPath(packagePrefix);
    	
    	//Need to call following methods to make sure the properties are properly set
    	marshaller.afterPropertiesSet();
    	unmarshaller.afterPropertiesSet();
    	
    	//Now make the call and pray that everything will work :)    	
		
		return webServiceTemplate;
		
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	
	
}
