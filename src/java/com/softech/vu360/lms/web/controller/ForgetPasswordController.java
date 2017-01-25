package com.softech.vu360.lms.web.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.CustomerUtil;
import com.softech.vu360.lms.web.controller.model.ChangeUsernamePasswordForm;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Saptarshi
 * @updated by muhammad.rehan
 */
public class ForgetPasswordController extends VU360BaseMultiActionController {
	
    private static final Logger log = Logger.getLogger(ForgetPasswordController.class.getName());
    private VU360UserService vu360UserService;
    private DistributorService distributorService;
    private String forgetUsernameTemplate=null;
	private String selectForgetTypeTemplate = null;
	private String incorrectInfoForUsernameTemplate= null;
	private String correctInfoForUsernameTemplate= null;
	private String forgetPasswordTemplate= null;
	private String incorrectInfoForPasswordTemplate= null;
	private String correctInfoForPasswordTemplate= null;
	private String redirectTemplate= null;
	private VelocityEngine velocityEngine;
    

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		setValuesInSession(request);
	}
	
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response ) throws Exception {

			setValuesInSession(request);
	        return new ModelAndView(selectForgetTypeTemplate);
	}
	
	void setValuesInSession(HttpServletRequest request){
		 String storeId = request.getParameter("storeId");
	     String lang = request.getParameter("lang");
	     com.softech.vu360.lms.vo.Language language = new com.softech.vu360.lms.vo.Language();
	     if (lang!=null && StringUtils.isNotBlank(lang))
	         language.setLanguage(lang);
	     if (storeId!=null && StringUtils.isNotBlank(storeId)) {
	         Distributor dist = distributorService.findDistibutorByDistributorCode(storeId);
	         if (dist != null) {
	             String brandName = dist.getBrandName();
	             if (brandName != null && brandName.trim().length() > 0) {
	                 request.getSession().setAttribute(VU360Branding.BRAND, brandName);
	             }
	         }
	     }
	     String storeURL = request.getParameter("storeURL");
	     if (storeURL!=null && StringUtils.isNotBlank(storeURL)){
	         request.getSession().setAttribute("_sf_storeURL", storeURL);
	         log.debug("-----------------storeURL ----------------->> " + storeURL);
	         log.info("-----------------storeURL ----------------->> " + storeURL);
	     }
	}

	public ModelAndView moveToHavingTrouble( HttpServletRequest request, HttpServletResponse response,
			Object command ) throws Exception {
		
		return new ModelAndView(selectForgetTypeTemplate);
	}
	
	 
	public ModelAndView submitForgetOptionSelection( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		
		ChangeUsernamePasswordForm form = (ChangeUsernamePasswordForm) command;
		
		if(form.getForgetTypeSelection().equalsIgnoreCase("username"))
			return new ModelAndView(forgetUsernameTemplate);
		else if(form.getForgetTypeSelection().equalsIgnoreCase("password"))
			return new ModelAndView(forgetPasswordTemplate);
		else{ 
			Map<Object, Object> context = new HashMap<Object, Object>();
            Map<Object, Object> status = new HashMap<Object, Object>();
            String[] errorCodes = new String[]{ "lms.forgetPassword.HavingTroubleSigningIn.forgetTypeSelection.errorMessage"};
            status.put("error", true);
            status.put("errorCodes", errorCodes);
            context.put("status", status);
			return new ModelAndView(selectForgetTypeTemplate, "context", context);
		}
	}
	
	
	public ModelAndView submitDataForUserNameRecovery( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		
		ChangeUsernamePasswordForm form = (ChangeUsernamePasswordForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> status = new HashMap<Object, Object>();
		List<String> lstErrorMeg = new ArrayList<String>();
		  
		try {
            Map<String, Object> model = new HashMap<String, Object>();
           // VU360User user = null;
            Brander brander = null;
		          
	        	boolean isError=false;
	        	if(form.getEmail().equalsIgnoreCase("")){
	        		lstErrorMeg.add("lms.forgetPassword.HavingTroubleSigningIn.enteremail.errorMessage");
	                isError=true;
	            }
	        	if(form.getFirstName().equalsIgnoreCase("") || form.getFirstName().equalsIgnoreCase("First Name")){
	        		lstErrorMeg.add("lms.forgetPassword.HavingTroubleSigningIn.enterfirstname.errorMessage");
	                isError=true;
	            }
	        	if(form.getLastName().equalsIgnoreCase("") || form.getLastName().equalsIgnoreCase("Last Name")){
	        	   lstErrorMeg.add("lms.forgetPassword.HavingTroubleSigningIn.enterlastname.errorMessage");
	                isError=true;
	        	}	
	            
	        	if( isError){
	        		 	status.put("error", true);
		                status.put("errorCodes", lstErrorMeg);
		                context.put("status", status);
		                return new ModelAndView(forgetUsernameTemplate, "context", context);
	        	}		        
		        
	            	List<VU360User> vu360UserList = vu360UserService.getUserByEmailFirstNameLastName(form.getEmail(),form.getFirstName(),form.getLastName());
	            	
	            	if (!vu360UserList.isEmpty()) {
	                	try{
	                		model.put("vu360UserList",vu360UserList);
	                		String usernameList= "";
	                		for(VU360User passUser:vu360UserList){
	                			usernameList=usernameList.concat("<b>Username: </b>").concat(passUser.getUsername()).concat("<br><br>");
	                		} 
	        				
	                		model.put("url", VU360Properties.getVU360Property("lms.loginURL"));
			                brander = VU360Branding.getInstance().getBranderByUser(request, vu360UserList.get(0));
			                String batchImportTemplatePath = brander.getBrandElement("lms.email.forgetPassword.body");
			                String fromAddress = brander.getBrandElement("lms.email.forgetPassword.fromAddress");
			                String fromCommonName = brander.getBrandElement("lms.email.forgetPassword.fromCommonName");
			                String subject = brander.getBrandElement("lms.email.forgetUsername.subject");
			                
			                String templateText=brander.getBrandElement("lms.branding.email.forgotuserName.newtemplate.templateText");			                
			                templateText=templateText.replaceAll("&lt;usernamelist&gt;", usernameList);
			                templateText=templateText.replaceAll("&lt;userName&gt;", vu360UserList.get(0).getFirstName() + " " + vu360UserList.get(0).getLastName());
			                
			                String lmsDomain=VU360Properties.getVU360Property("lms.domain");			                
			                String loginUrl=lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());
			                templateText=templateText.replaceAll("&lt;loginurl&gt;", loginUrl);
			                
			                model.put("brander", brander);
			                model.put("support", fromCommonName);
			                model.put("templateText", templateText);
			                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, batchImportTemplatePath, model);
			                SendMailService.sendSMTPMessage(vu360UserList.get(0).getEmailAddress(),fromAddress,fromCommonName,subject,text);
			                
	                	}
	                	catch(Exception ex){
	                		log.error(ex.getMessage());
	                		/*LMS-14237*/
	                		String storeURL = (String) request.getSession().getAttribute("_sf_storeURL");
	                        if (storeURL!=null && StringUtils.isNotBlank(storeURL)) {
	                            response.sendRedirect(storeURL);
	                            return null;
	                        }else{
                            	return new ModelAndView("redirect:login.do?message=true");
                            }
	                	}
	                }
	                else{
	                	 //In-correct data provided by user
	                	 return new ModelAndView(redirectTemplate, "target", "rediretToIncorrectInfoForUsernameTemplate");
	                }
	                return new ModelAndView(redirectTemplate, "target", "rediretToCorrectInfoForUsernameTemplate");
        } catch (Exception e) {
            e.printStackTrace();
            /*LMS-14237*/
            String storeURL = (String) request.getSession().getAttribute("_sf_storeURL");
            if (storeURL!=null && StringUtils.isNotBlank(storeURL)) {
                response.sendRedirect(storeURL);
                return null;
            }
            else{
            	return new ModelAndView("redirect:login.do?message=true");
            }
        }
		

	}
	

	

	
	
	public ModelAndView submitDataForPasswordRecovery( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
			
		ChangeUsernamePasswordForm form = (ChangeUsernamePasswordForm) command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object> status = new HashMap<Object, Object>();
		List<String> lstErrorMeg = new ArrayList<String>();
		  
		try {
            Map<String, Object> model = new HashMap<String, Object>();
            Brander brander = null;
		            if (form.getForgetPasswordSelectionType().equals("byUsername") && form.getUsername().equals("")) {
		            		lstErrorMeg.add("lms.forgetPassword.HavingTroubleSigningIn.enterusername.errorMessage");
			                status.put("error", true);
			                status.put("errorCodes", lstErrorMeg);
			                context.put("status", status);
			            	return new ModelAndView(forgetPasswordTemplate, "context", context);
		            } else if(form.getForgetPasswordSelectionType().equals("byEmailAddress")){
		            	boolean isError=false;
		            	if(form.getEmail().equalsIgnoreCase("")){
		            		lstErrorMeg.add("lms.forgetPassword.HavingTroubleSigningIn.enteremail.errorMessage");
			                isError=true;
			            }
		            	if(form.getFirstName().equalsIgnoreCase("") || form.getFirstName().equalsIgnoreCase("First Name")){
		            		lstErrorMeg.add("lms.forgetPassword.HavingTroubleSigningIn.enterfirstname.errorMessage");
			                isError=true;
			            }
		            	if(form.getLastName().equalsIgnoreCase("") || form.getLastName().equalsIgnoreCase("Last Name")){
		            	   lstErrorMeg.add("lms.forgetPassword.HavingTroubleSigningIn.enterlastname.errorMessage");
			                isError=true;
		            	}	
			            
		            	if( isError){
		            		 	status.put("error", true);
				                status.put("errorCodes", lstErrorMeg);
				                context.put("status", status);
				                return new ModelAndView(forgetPasswordTemplate, "context", context);
		            	}
		            }
	            	
	            	List<VU360User> vu360UserList = null;
	            	
	            	if(form.getForgetPasswordSelectionType().equalsIgnoreCase("byEmailAddress"))
	            		vu360UserList = vu360UserService.getUserByEmailFirstNameLastName(form.getEmail(),form.getFirstName(),form.getLastName());
	            	else if(form.getForgetPasswordSelectionType().equalsIgnoreCase("byUsername"))
	            		vu360UserList = vu360UserService.getActiveUserByUsername(form.getUsername());
	            	else
	            		return new ModelAndView(forgetPasswordTemplate);
	            		
	            	if (!vu360UserList.isEmpty()) {
	                	for(VU360User vu360UserToResetPassword:vu360UserList) {
	                		if (vu360UserToResetPassword.getAccountNonExpired() && vu360UserToResetPassword.getAccountNonLocked()) {
	                			
	                			String password = GUIDGeneratorUtil.generatePassword();
	                			
	                			VU360User userObj = vu360UserService.loadForUpdateVU360User(vu360UserToResetPassword.getId());
	                			userObj.setPassword(password);		                        
		                        vu360UserService.changeUserPassword(userObj.getId(), userObj);
		                        
		                        log.debug("Updating Password ON SF");
		                        try {
		                        	Customer customer=userObj.getLearner().getCustomer();
		                        	if(customer!=null && customer.getCustomerType().equals(Customer.B2C) 
		                        			&& CustomerUtil.isUserProfileUpdateOnSF(customer)){ //LMS-16308
		                        		new StorefrontClientWSImpl().updatePassword(userObj, password);
		                        		log.debug("Password updated successfully!!!!");
		                        	}
		                        	
		                        } catch (Exception e) {
		                            log.error("Exception occurred while updating Password on SF:  " + e.getMessage());
		                            
		                            /*LMS-14237*/
		                            String storeURL = (String) request.getSession().getAttribute("_sf_storeURL");
		                            if (storeURL!=null && StringUtils.isNotBlank(storeURL)) {
		                                response.sendRedirect(storeURL);
		                                return null;
		                            }else{
		                            	return new ModelAndView("redirect:login.do?message=true");
		                            }
		                        } 
		                        
		                        vu360UserToResetPassword.setPassword(password);  
	                    	}
	                	}
	                	try{
	                		model.put("vu360UserList",vu360UserList);
	                		String passwordList= "";
	                		for(VU360User passUser:vu360UserList){
	                			passwordList=passwordList.concat("<b>Password: </b>").concat(passUser.getPassword()).concat("<br><br>");
	                		} 

	                		model.put("url", VU360Properties.getVU360Property("lms.loginURL"));
			                brander = VU360Branding.getInstance().getBranderByUser(request, vu360UserList.get(0));
			                String batchImportTemplatePath = brander.getBrandElement("lms.email.forgetPassword.body");
			                String fromAddress = brander.getBrandElement("lms.email.forgetPassword.fromAddress");
			                String fromCommonName = brander.getBrandElement("lms.email.forgetPassword.fromCommonName");
			                String subject = brander.getBrandElement("lms.email.forgetpassword.newTemplate.subject");
			                
			                String templateText=brander.getBrandElement("lms.branding.email.forgotPassword.newtemplate.templateText");			                
			                templateText=templateText.replaceAll("&lt;passwordlist&gt;", passwordList);
			                templateText=templateText.replaceAll("&lt;userName&gt;", vu360UserList.get(0).getFirstName() + " " + vu360UserList.get(0).getLastName());
			                
			                String lmsDomain=VU360Properties.getVU360Property("lms.domain");			                
			                String loginUrl=lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());
			                templateText=templateText.replaceAll("&lt;loginurl&gt;", loginUrl);
			                
			                model.put("brander", brander);
			                model.put("support", fromCommonName);
			                model.put("templateText", templateText);
			                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, batchImportTemplatePath, model);
			                SendMailService.sendSMTPMessage(vu360UserList.get(0).getEmailAddress(),fromAddress,fromCommonName,subject,text);
			                
	                	}
	                	catch(Exception ex){
	                		log.error(ex.getMessage());
	                		/*LMS-14237*/
	                		String storeURL = (String) request.getSession().getAttribute("_sf_storeURL");
	                        if (storeURL!=null && StringUtils.isNotBlank(storeURL)) {
	                            response.sendRedirect(storeURL);
	                            return null;
	                        }else{
                            	return new ModelAndView("redirect:login.do?message=true");
                            }
	                	}
	                }
	                else{
	                	// data provided is incorrect
	                	return new ModelAndView(redirectTemplate, "target", "rediretToIncorrectInfoForPasswordTemplate");
	                }
	                /*String storeURL = (String) request.getSession().getAttribute("_sf_storeURL");
	                if (StringUtils.isNotBlank(storeURL)) {
	                    response.sendRedirect(storeURL);
	                    return null;
	                }*/

	            	
	                return new ModelAndView(redirectTemplate, "target", "rediretToCorrectInfoForPasswordTemplate");
        } catch (Exception e) {
            e.printStackTrace();
            /*LMS-14237*/
            String storeURL = (String) request.getSession().getAttribute("_sf_storeURL");
            if (storeURL!=null && StringUtils.isNotBlank(storeURL)) {
                response.sendRedirect(storeURL);
                return null;
            }
            else{
            	return new ModelAndView("redirect:login.do?message=true");
            }
        }
		
	}
	
	
	public ModelAndView rediretToCorrectInfoForUsernameTemplate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		
		 return new ModelAndView(correctInfoForUsernameTemplate);
	}
	
	public ModelAndView rediretToIncorrectInfoForUsernameTemplate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		
		 return new ModelAndView(incorrectInfoForUsernameTemplate);
	}
	
	public ModelAndView rediretToCorrectInfoForPasswordTemplate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		
		 return new ModelAndView(correctInfoForPasswordTemplate);
	}
	
	public ModelAndView rediretToIncorrectInfoForPasswordTemplate( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		
		 return new ModelAndView(incorrectInfoForPasswordTemplate);
	}
	
	/*public ModelAndView saveLocation( HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors ) throws Exception {
		
		return new ModelAndView("");
	}*/

	

	public String getSelectForgetTypeTemplate() {
		return selectForgetTypeTemplate;
	}

	public void setSelectForgetTypeTemplate(String selectForgetTypeTemplate) {
		this.selectForgetTypeTemplate = selectForgetTypeTemplate;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
	public String getForgetUsernameTemplate() {
		return forgetUsernameTemplate;
	}

	public void setForgetUsernameTemplate(String forgetUsernameTemplate) {
		this.forgetUsernameTemplate = forgetUsernameTemplate;
	}

	
    /*private boolean validate(HttpServletRequest request, Map<Object, Object> model) {
        boolean check = false;
        if (!StringUtils.isBlank(request.getParameter("isFromSubmit"))) {
	        if (StringUtils.isBlank(request.getParameter("email"))) {
	            model.put("email1", "error.forgetPassword.emailrequired");
	            check = true;
	        } 
        }
        return check;
    }*/
	
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public String getIncorrectInfoForUsernameTemplate() {
		return incorrectInfoForUsernameTemplate;
	}

	public void setIncorrectInfoForUsernameTemplate(
			String incorrectInfoForUsernameTemplate) {
		this.incorrectInfoForUsernameTemplate = incorrectInfoForUsernameTemplate;
	}

	public String getCorrectInfoForUsernameTemplate() {
		return correctInfoForUsernameTemplate;
	}

	public void setCorrectInfoForUsernameTemplate(
			String correctInfoForUsernameTemplate) {
		this.correctInfoForUsernameTemplate = correctInfoForUsernameTemplate;
	}

	public String getForgetPasswordTemplate() {
		return forgetPasswordTemplate;
	}

	public void setForgetPasswordTemplate(String forgetPasswordTemplate) {
		this.forgetPasswordTemplate = forgetPasswordTemplate;
	}

	public String getIncorrectInfoForPasswordTemplate() {
		return incorrectInfoForPasswordTemplate;
	}

	public void setIncorrectInfoForPasswordTemplate(
			String incorrectInfoForPasswordTemplate) {
		this.incorrectInfoForPasswordTemplate = incorrectInfoForPasswordTemplate;
	}

	public String getCorrectInfoForPasswordTemplate() {
		return correctInfoForPasswordTemplate;
	}

	public void setCorrectInfoForPasswordTemplate(
			String correctInfoForPasswordTemplate) {
		this.correctInfoForPasswordTemplate = correctInfoForPasswordTemplate;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	

}
