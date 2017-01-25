package com.softech.vu360.lms.web.controller.accreditation.migration;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.webservice.client.LCMSClientWS;
import com.softech.vu360.util.VU360Properties;

/** 
 * 
 * @author Shoaib Ashrafi
 */
public class UploadLMSAssetToLCMSController extends VU360BaseMultiActionController implements Runnable {
	
	private static final Logger log = Logger.getLogger(UploadLMSAssetToLCMSController.class.getName());
	private LCMSClientWS lcmsClientWS = null;
		
	private AccreditationService accreditationService = null;
	private String searchAssetTemplate;
	private List<Certificate> certificates = null;
	private Integer fromCertificateId = null;
	private Integer toCertificateId = null;
	private com.softech.vu360.lms.vo.VU360User loggedInUser = null;
	StringBuffer logs = new StringBuffer();
	
	public UploadLMSAssetToLCMSController(){
		super();
	}

	public UploadLMSAssetToLCMSController(Object delegate) {		
		super(delegate);
	}
	
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {		
		return execute(request, response);
	}
	
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response ) throws Exception {
		
		if(StringUtils.isNotBlank(request.getParameter("fromCertificateId"))){
			fromCertificateId = new Integer(request.getParameter("fromCertificateId"));
		}

		if(StringUtils.isNotBlank(request.getParameter("toCertificateId"))){
			toCertificateId = new Integer(request.getParameter("toCertificateId"));
		}

		loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		certificates = getAccreditationService().getCertificatesWhereAssetVersionIsEmpty(fromCertificateId, toCertificateId);

		new Thread(this).start();
		
		return new ModelAndView(searchAssetTemplate);
	}

	/**
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the searchAssetTemplate
	 */
	public String getSearchAssetTemplate() {
		return searchAssetTemplate;
	}

	/**
	 * @param searchAssetTemplate the searchAssetTemplate to set
	 */
	public void setSearchAssetTemplate(String searchAssetTemplate) {
		this.searchAssetTemplate = searchAssetTemplate;
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public LCMSClientWS getLcmsClientWS() {
		return lcmsClientWS;
	}

	public void setLcmsClientWS(LCMSClientWS lcmsClientWS) {
		this.lcmsClientWS = lcmsClientWS;
	}

	@Override
	public void run() {
		try{
			
			
			String documentLocation = VU360Properties.getVU360Property("document.saveLocation");
			
			for(Certificate cert1 : certificates){
				
				logs.append("\n");
				logs.append("Certificate ID: ").append(cert1.getId()).append(", Name: ").append(cert1.getName()).append(", File Name: ").append(cert1.getFileName());
			
				Certificate cert = accreditationService.loadForUpdateCertificate(cert1.getId());
				
				StringBuffer filePath = new StringBuffer();
	        	filePath.append(documentLocation);			
				filePath.append(cert.getFileName());

				logs.append(", File Path: ").append(filePath.toString());
				
		    	File file = new File(filePath.toString());
		    	
		    	if(!file.exists()){
		    		logs.append(", ERROR : File Not Found!");
		    	}
		    	if(file.exists())
		    	{
		    		logs.append(", MESSAGE : File Found!");
		    		FileInputStream fis = new FileInputStream(file);
		    		byte[] bytes = IOUtils.toByteArray(fis);
	
//		    		LCMSClientWS lcmsClient = LCMSClientWS.getDefaultClient();
		    		if(getLcmsClientWS().uploadAsset(cert.getFileName(), bytes)){	
		    			logs.append(", Uploaded Successfully.");
		    			if( getLcmsClientWS().saveAsset(cert, true, loggedInUser.getId()) > 0 ){
			    			getAccreditationService().getCertificateById(cert.getId(), true);
			    			logs.append(", Asset Updated Successfully.");
			    		}
		    		}
		    	}	
			}
			
			File file = new File("lms_lcms_migration_logs.log");
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream os = new FileOutputStream(file);
			os.write(logs.toString().getBytes());
			os.close();
			
		}catch(Exception e){
			log.error(e.getMessage(), e);
//			e.printStackTrace();
		}
	}
	
	
	
	
	
}