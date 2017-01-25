package com.softech.vu360.lms.webservice.client.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.apache.log4j.Logger;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CourseConfiguration;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.webservice.client.LCMSClientWS;
import com.softech.vu360.lms.webservice.message.lcms.ArrayOfLong;
import com.softech.vu360.lms.webservice.message.lcms.client.ArrayOfUnsignedByte;
import com.softech.vu360.lms.webservice.message.lcms.client.DownloadAssetInChunk;
import com.softech.vu360.lms.webservice.message.lcms.client.DownloadAssetInChunkResponse;
import com.softech.vu360.lms.webservice.message.lcms.client.GetAssetFileInfo;
import com.softech.vu360.lms.webservice.message.lcms.client.GetAssetFileInfoResponse;
import com.softech.vu360.lms.webservice.message.lcms.client.SaveAsset;
import com.softech.vu360.lms.webservice.message.lcms.client.SaveAssetResponse;
import com.softech.vu360.lms.webservice.message.lcms.client.UpdateAssetStatus;
import com.softech.vu360.lms.webservice.message.lcms.client.UpdateAssetStatusResponse;
import com.softech.vu360.lms.webservice.message.lcms.client.UploadAssetInChunk;
import com.softech.vu360.lms.webservice.message.lcms.client.UploadAssetInChunkResponse;
import com.softech.vu360.lms.webservice.message.lcms.player.InvalidateBrandCache;
import com.softech.vu360.lms.webservice.message.lcms.player.InvalidateBrandCacheResponse;
import com.softech.vu360.lms.webservice.message.lcms.player.InvalidateCourseApprovalCache;
import com.softech.vu360.lms.webservice.message.lcms.player.InvalidateCourseApprovalCacheResponse;
import com.softech.vu360.lms.webservice.message.lcms.player.InvalidateCourseConfigurationCache;
import com.softech.vu360.lms.webservice.message.lcms.player.InvalidateCourseConfigurationCacheResponse;
import com.softech.vu360.util.VU360Properties;

public class LCMSClientWSImpl extends LCMSClientWS {

	public static final String LCMS_WEBSERVICE_ENDPONT = VU360Properties.getVU360Property("lcms.webservice.client.asset.endpoint").trim();
	public static final String LCMS_PLAYER_UTILITY_WEBSERVICE_ENDPONT = VU360Properties.getVU360Property("lcms.webservice.player.utility.endpoint").trim();
	public static final String LCMS_WEBSERVICE_PACKAGE = "com.softech.vu360.lms.webservice.message.lcms.client";
	public static final String LCMS_PLAYER_UTILITY_WEBSERVICE_PACKAGE = "com.softech.vu360.lms.webservice.message.lcms.player";
	
	public static final Logger logger = Logger.getLogger(LCMSClientWSImpl.class.getName());
	
	@Override
	public long saveAsset(Asset asset, boolean fileUploaded, long loggedInUserID) {

		SaveAsset saveAsset = new SaveAsset();
		if(asset.getId() != null && asset.getId().longValue() > 0){
			saveAsset.setAssetId(asset.getId());
		}else{
			saveAsset.setAssetId(0);
		}
		
		saveAsset.setAssetType(asset.getAssetType());
		saveAsset.setContentOwnerId(asset.getContentowner().getId().intValue());
		saveAsset.setName(asset.getName());
		saveAsset.setLoggedInUserID((int)loggedInUserID);
		saveAsset.setFileUploaded(fileUploaded);
		
		if(asset instanceof Document){
			
			Document document = (Document)asset;
			if(document.getDescription() != null && !document.getDescription().isEmpty()){
				saveAsset.setDescription(document.getDescription());	
			}else{
				saveAsset.setDescription("N/A");
			}
			saveAsset.setFileName(document.getFileName());
			saveAsset.setItemsPerPage(document.getNoOfDocumentsPerPage());
			saveAsset.setKeywords(document.getKeywords());
			/*Passed Language ID '1' hard coded as going in LCMS, currently in LCMS*/
			saveAsset.setLanguageId(1);
			if(document instanceof Affidavit){
				//	HJamil: Because content will not be populated by LMS anymore, that is now a local field to LCMS as discussed with Bilal and Mahmood
				//saveAsset.setContent(((Affidavit)document).getContent());
				
				//	All the content fields (1, 2 and 3) would be sent in DisplayTexts (1, 2 and 3)
				saveAsset.setDisplayText1(((Affidavit)document).getContent());				
				saveAsset.setDisplayText2(((Affidavit)document).getContent2());
				saveAsset.setDisplayText3(((Affidavit)document).getContent3());
				
				Affidavit Objaff = (Affidavit)asset;
				
				if(Objaff !=null && Objaff.getTemplateId() !=null && Objaff.getAffidavitType().equalsIgnoreCase("template") && ((Affidavit)asset).getTemplateId() > 0){
					saveAsset.setAffidavitTemplateID(((Affidavit)asset).getTemplateId());
				}else{
					saveAsset.setAffidavitTemplateID(0l);
				}
				//saveAsset.setAffidavitTemplateID(((Affidavit)asset).getTemplateId() == 0 ? null : ((Affidavit)asset).getTemplateId());
				
				saveAsset.setAffidavitType(((Affidavit)asset).getAffidavitType());
			}
			
		}
		
		try {
			SaveAssetResponse response = (SaveAssetResponse)getWebServiceTemplate(LCMS_WEBSERVICE_ENDPONT, LCMS_WEBSERVICE_PACKAGE).marshalSendAndReceive(saveAsset, new SoapActionCallback("http://360training.com/SaveAsset"));
			return response.getSaveAssetResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;		
		
	}

	@Override
	public boolean uploadAsset(String fileName, byte[] bytes) {

		int offset = Integer.parseInt(VU360Properties.getVU360Property("lcms.webservice.client.asset.chunkSize"));		

		if(offset > bytes.length){
			offset = bytes.length;
		}
		
		int beginPoint = 0;
		int endPoint = offset;
		
        try {
			
        	WebServiceTemplate template = getWebServiceTemplate(LCMS_WEBSERVICE_ENDPONT, LCMS_WEBSERVICE_PACKAGE);
        	
        	DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        	
        	while(beginPoint < bytes.length){

        		UploadAssetInChunk appendChunk = new UploadAssetInChunk();
        		
        		ArrayOfUnsignedByte buffer = new ArrayOfUnsignedByte(offset);        		
        		
        		for(int i=beginPoint; i<endPoint; i++ ){
        			buffer.getUnsignedByte().add((short)dis.readUnsignedByte());
        		}
        		
        		appendChunk.setBuffer(buffer);
        		appendChunk.setOffset(beginPoint);
	            appendChunk.setFileName(fileName);
	            
				UploadAssetInChunkResponse response = (UploadAssetInChunkResponse)template.marshalSendAndReceive(appendChunk, new SoapActionCallback("http://360training.com/UploadAssetInChunk"));
	            
	            beginPoint = endPoint;            
	        	endPoint += offset;        	
	        	
	        	if(endPoint > bytes.length){
	        		endPoint = bytes.length;
	        	}
	        	
	        } 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
				
		return true;
	}

	@Override
	public int updateAssetStatus(long[] assetIds, boolean active, long loggedInUserID) {
		
		UpdateAssetStatus request = new UpdateAssetStatus();
		ArrayOfLong assetIdsArr = new ArrayOfLong();
		
		for(long assetId : assetIds){
			assetIdsArr.getLong().add(assetId);
		}
				
		request.setActive(active);
		request.setAssetIds(assetIdsArr);
		request.setLoggedInUserID((int)loggedInUserID);
		
		try {
			UpdateAssetStatusResponse response = (UpdateAssetStatusResponse)getWebServiceTemplate(LCMS_WEBSERVICE_ENDPONT, LCMS_WEBSERVICE_PACKAGE).marshalSendAndReceive(request, new SoapActionCallback("http://360training.com/UpdateAssetStatus"));
			return response.getUpdateAssetStatusResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return 0;		
	}

	@Override
	public byte[] downloadAssetStatus(long assetId) {
		
		try{
			WebServiceTemplate template = getWebServiceTemplate(LCMS_WEBSERVICE_ENDPONT, LCMS_WEBSERVICE_PACKAGE);
			
			GetAssetFileInfo request = new GetAssetFileInfo();
			request.setAssetId(assetId);
			
			GetAssetFileInfoResponse response = (GetAssetFileInfoResponse)template.marshalSendAndReceive(request, new SoapActionCallback("http://360training.com/GetAssetFileInfo"));

			if(response.getGetAssetFileInfoResult() == null || response.getGetAssetFileInfoResult().getString() == null || response.getGetAssetFileInfoResult().getString().size() == 0){
				return null;
			}
			
			String filePath = response.getGetAssetFileInfoResult().getString().get(0);
			int fileSize = Integer.valueOf(response.getGetAssetFileInfoResult().getString().get(1));
			
			int bufferSize = Integer.parseInt(VU360Properties.getVU360Property("lcms.webservice.client.asset.chunkSize"));			
			
			
			if(fileSize < bufferSize){
				bufferSize = fileSize;
			}
			
			if(fileSize <= 0){
				return null;
			}
			
			byte[] bytes = new byte[fileSize];
			
			for(int offset = 0; offset <= fileSize; offset += bufferSize ){
				
				DownloadAssetInChunk downloadRequest = new DownloadAssetInChunk();
				downloadRequest.setFilePath(filePath);
				downloadRequest.setOffset(offset);
				downloadRequest.setBufferSize(bufferSize);
				
				DownloadAssetInChunkResponse downloadResponse = (DownloadAssetInChunkResponse)template.marshalSendAndReceive(downloadRequest, new SoapActionCallback("http://360training.com/DownloadAssetInChunk"));
				
				if(downloadResponse.getDownloadAssetInChunkResult().getUnsignedByte().size() > 0){
					int index = offset;
					for(Short value : downloadResponse.getDownloadAssetInChunkResult().getUnsignedByte()){
						bytes[index++] = value.byteValue();
					}
				}
				
			}
			
			return bytes;
			
			
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		
		return null;
	}	
	
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.webservice.client.LCMSClientWS#invokeCourseApprovalUpdated(long)
	 */
	@Override
	public boolean invokeCourseApprovalUpdated(CourseApproval couraeApproval) {
		
		if(couraeApproval.getCourse() == null || couraeApproval.getCourse().getId().intValue() <= 0){
			return false;
		}
		
		InvalidateCourseApprovalCache request = new InvalidateCourseApprovalCache();
		request.setCourseApprovalID(couraeApproval.getId().intValue());
		request.setCourseID(couraeApproval.getCourse().getId().intValue());
		
		try {
			InvalidateCourseApprovalCacheResponse response = (InvalidateCourseApprovalCacheResponse)getWebServiceTemplate(LCMS_PLAYER_UTILITY_WEBSERVICE_ENDPONT, LCMS_PLAYER_UTILITY_WEBSERVICE_PACKAGE).marshalSendAndReceive(request, new SoapActionCallback("http://tempuri.org/InvalidateCourseApprovalCache"));
			return response.isInvalidateCourseApprovalCacheResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		
	}

	@Override
	public boolean invalidateBrandCache(String brandName, String language){
		
		InvalidateBrandCache request = new InvalidateBrandCache();
		request.setBrandCode(brandName);
		request.setVariant(language);

		try {
			InvalidateBrandCacheResponse response = (InvalidateBrandCacheResponse)getWebServiceTemplate(LCMS_PLAYER_UTILITY_WEBSERVICE_ENDPONT, LCMS_PLAYER_UTILITY_WEBSERVICE_PACKAGE).marshalSendAndReceive(request, new SoapActionCallback("http://tempuri.org/InvalidateBrandCache"));
			boolean bolResponse = response.isInvalidateBrandCacheResult();
			if(bolResponse)
				logger.info("Branding cache refreshed successfully.");
				
			return bolResponse;

		} catch (Exception e) {
			logger.error("some exception occured during refressing lcms branding cache.");
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.webservice.client.LCMSClientWS#invokeCourseConfigurationUpdated(long)
	 */
	@Override
	public boolean invokeCourseConfigurationUpdated(CourseConfiguration courseConfiguration) {

		InvalidateCourseConfigurationCache request = new InvalidateCourseConfigurationCache();
		request.setCourseConfigurationID(courseConfiguration.getId().intValue());
		try {
			InvalidateCourseConfigurationCacheResponse response = (InvalidateCourseConfigurationCacheResponse)getWebServiceTemplate(LCMS_PLAYER_UTILITY_WEBSERVICE_ENDPONT, LCMS_PLAYER_UTILITY_WEBSERVICE_PACKAGE).marshalSendAndReceive(request, new SoapActionCallback("http://tempuri.org/InvalidateCourseConfigurationCache"));
			return response.isInvalidateCourseConfigurationCacheResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
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

	

}
