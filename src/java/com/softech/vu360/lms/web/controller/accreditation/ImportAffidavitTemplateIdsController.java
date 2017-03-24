package com.softech.vu360.lms.web.controller.accreditation;

import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVReader;

import com.softech.vu360.lms.model.AffidavitTemplate;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AffidavitTemplateService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ImportAffidavitTemplateIdsForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * 
 * @author haider.ali
 *
 */
public class ImportAffidavitTemplateIdsController extends VU360BaseMultiActionController{

	private static final Logger log = Logger.getLogger(ImportAffidavitTemplateIdsController.class.getName());
	private AffidavitTemplateService affidavitTemplateService = null;
	private MultipartResolver multipartResolver=null;
	private String startTemplate = null;
	private String finishTemplate = null;
	private String closeTemplate = null;
	public static final String CONTEXT = "context";

	
	
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
			if( methodName.equals("importTemplateId")) {			
				this.getValidator().validate(command, errors);
			}
		// TODO Auto-generated method stub
		
	}

	
	public ModelAndView initializePage(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors) throws Exception {

		ImportAffidavitTemplateIdsForm form = (ImportAffidavitTemplateIdsForm) command;
		//form.resetForm();
		//form.setManageUserStatus(null);
		ModelAndView modelAndView = new ModelAndView(startTemplate, CONTEXT, new HashMap());

		return modelAndView;
		
	}
	
	
	public ModelAndView importTemplateId(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors) throws Exception {
		
		ModelAndView modelAndView = null;
		ImportAffidavitTemplateIdsForm form = (ImportAffidavitTemplateIdsForm) command;
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		
		if(!errors.hasErrors())	{

			MultipartFile file = form.getFile();
			log.debug("FILE NAME "+ file.getOriginalFilename() +" ("+file.getSize()+"kb)");
			form.setFileName(file.getOriginalFilename());
			byte[] filed=file.getBytes();
			form.setFileData(filed);

			
			CSVReader reader;
			reader = new CSVReader(new InputStreamReader(form.getFile().getInputStream()), '\t', '\'', 1);  // 1 means skip first line
		    String [] nextLine;
		    String delimiter = ",";
		    String[] strSplittedIds;
		    int totTempID=0,addedtempID=0;
		    log.debug("FILE NAME "+ file.getOriginalFilename() +" (start parsing...)");
		   
		    while ((nextLine = reader.readNext()) != null) {
		    	if(StringUtils.isNotEmpty(nextLine[0])){
		    			strSplittedIds = nextLine[0].split(delimiter);
		    			
	    		    if(strSplittedIds!=null && strSplittedIds.length==2 && strSplittedIds[0]!=null && strSplittedIds[1]!=null){
	    		    	AffidavitTemplate affidavitTemplate = new AffidavitTemplate();
			    		affidavitTemplate.setTemplateGUID(strSplittedIds[0]);
			    		affidavitTemplate.setTemplateName(strSplittedIds[1]);
			    		
			    		
			    		affidavitTemplate.setStatus(Boolean.TRUE);
			    		
			    		affidavitTemplate.setCreatedDate(new Date());
			    		affidavitTemplate.setCreatedVU360User(loggedInUser);
			    		AffidavitTemplate alreadyExist = affidavitTemplateService.getAffidavitTemplateByName(affidavitTemplate.getTemplateGUID());
			    		if(alreadyExist==null){
			    			affidavitTemplateService.save(affidavitTemplate);
			    			addedtempID++;
			    		}else{
			    			alreadyExist.setTemplateName(strSplittedIds[1]);
			    			affidavitTemplateService.update(alreadyExist);
			    		}
		    	    }else{
		    	    	 errors.rejectValue("file", "error.AddDocuments.fileParseError");
		    	    	 modelAndView = new ModelAndView(startTemplate, CONTEXT, new HashMap());	
		    	    	 return modelAndView;
		    	    }
		    		totTempID++;
		    	}
		    }
		    log.debug("FILE NAME "+ file.getOriginalFilename() +" (start done...)");
		    log.info("About "+totTempID +" found in file and "+ addedtempID +" IDs inserted successfully.//n");
			 modelAndView = new ModelAndView(finishTemplate, CONTEXT, new HashMap());
			 
		}else{
			
			 modelAndView = new ModelAndView(startTemplate, CONTEXT, new HashMap());			
		}
		return modelAndView;


	}

	
	public String getStartTemplate() {
		return startTemplate;
	}
	public void setStartTemplate(String startTemplate) {
		this.startTemplate = startTemplate;
	}
	public AffidavitTemplateService getAffidavitTemplateService() {
		return affidavitTemplateService;
	}
	public void setAffidavitTemplateService(
			AffidavitTemplateService affidavitTemplateService) {
		this.affidavitTemplateService = affidavitTemplateService;
	}
	public MultipartResolver getMultipartResolver() {
		return multipartResolver;
	}
	public void setMultipartResolver(MultipartResolver multipartResolver) {
		this.multipartResolver = multipartResolver;
	}
	public String getFinishTemplate() {
		return finishTemplate;
	}
	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}
	public String getCloseTemplate() {
		return closeTemplate;
	}
	public void setCloseTemplate(String closeTemplate) {
		this.closeTemplate = closeTemplate;
	}
}
