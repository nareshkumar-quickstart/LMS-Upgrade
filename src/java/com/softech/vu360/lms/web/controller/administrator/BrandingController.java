package com.softech.vu360.lms.web.controller.administrator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.LcmsResource;
import com.softech.vu360.lms.model.Mapping;
import com.softech.vu360.lms.service.BrandService;
import com.softech.vu360.lms.service.BrandingService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.BrandingForm;
import com.softech.vu360.lms.web.controller.validator.BrandingValidator;
import com.softech.vu360.lms.webservice.client.LCMSClientWS;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.MappingSort;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class BrandingController extends VU360BaseMultiActionController {

	private String brandingBasicTemplate;
	private String brandingAdvancedTemplate;
	private String editEmailTemplate;
	private final String SYSTEMCODE= "lms.userCreated.";
	private String failureTemplate= null;
	private BrandingService brandingService;
	private String aboutUsTemplate;
	private String contactUsTemplate;	
	private String termsOfUseTemplate;
	private String onlineProvacyPolicyTemplate;
	private CustomerService customerService;
	private BrandService brandService;
	private int MAX_FILE_SIZE=1024*50;//50kb
	private static String NO_BRANDNAME="unselected";
	private LCMSClientWS lcmsClientWS = null;
	private static String En_US="En-US";
	
	private static final Logger log = Logger.getLogger(BrandingController.class.getName());
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("newPage","true");
		return this.basicBranding(request, response, null, null);

	}	
	@Override
	protected void onBind(HttpServletRequest request, Object command,String methodName) throws Exception {
        
		request.setAttribute("formContext", "");
		BrandingForm form = (BrandingForm)command;
		if(this.isAllDataValid(request,form)){								
			Properties brands= brandingService.getBrandProperties(form.getBrandName());
			if(methodName.equalsIgnoreCase("basicBranding")){					
			form.setBrandName(form.getBrandName());
			
			//Since we don't want logos to be updated from default, load empty values beforehand if no logos exist for this brand
			//This would be a one time operation perbrand
			boolean saveFlag=false;
			String defImgPath=VU360Properties.getVU360Property("brands.basefolder")+"default/en/images/logo_360_52.gif"; //LMS-13716
			String imageRelativePath= defImgPath.substring(defImgPath.indexOf("brands"));
			imageRelativePath=addPathCorrection().concat(imageRelativePath);
			if(brands.getProperty("lms.header.logo.src")==null || brands.getProperty("lms.header.logo.src").length()<=0){
				brands.setProperty("lms.header.logo.src", imageRelativePath);
				saveFlag=true;				
			}
			String path=brandingService.getProperty(brands,form.getBrandName(),"lms.header.logo.src");
			form.setLmsLogoPath(path.length()>0?path:null);
			form.setShowLmsLogoRemoveLink(path.indexOf("logo_360_52.gif")<0?true:false);
			
			if(brands.getProperty("lms.courseplayer.logo.src")==null || brands.getProperty("lms.courseplayer.logo.src").length()<=0){								
				brands.setProperty("lms.courseplayer.logo.src", imageRelativePath);
				saveFlag=true;				
			}
			if(brands.getProperty("lms.learner.SelfRegistrationlogin.caption.Welcome")==null || brands.getProperty("lms.learner.SelfRegistrationlogin.caption.Welcome").length()<=0){								
				brands.setProperty("lms.learner.SelfRegistrationlogin.caption.Welcome", "Dear Student, Welcome!");
				saveFlag=true;				
			}
			
			if(saveFlag){
				brandingService.saveBrands(form.getBrandName(), brands);
			}
			path=brandingService.getProperty(brands,form.getBrandName(),"lms.courseplayer.logo.src");
			form.setCoursePlayerLogoPath(path);
			form.setShowCoursePlayerLogoRemoveLink(path.indexOf("logo_360_52.gif")<0?true:false);
			
			form.setLoginMessage(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.message"));
			form.setUsernameLabel(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.username"));
			form.setPasswordLabel(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.Password"));
			form.setLoginButtonLabel(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.loginButton"));
			form.setForgotPasswordLinkLabel(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.forgotPassword"));
			form.setHelpToolTipText(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.help.tooltip"));
			form.setInvalidUsernameErrorMessage(
                            StringEscapeUtils.unescapeHtml(
                                brandingService.getProperty(brands,form.getBrandName(),"lms.login.loginInfo"))); 
			//form.setInvalidPasswordErrorMessage(brandingService.getProperty(brands,form.getBrandName(),"lms.login.loginInfo"));
			
			form.setLoginDefaultMessage(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.message.default"));
			form.setUsernameDefaultLabel(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.username.default"));
			form.setPasswordDefaultLabel(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.Password.default"));
			form.setLoginButtonDefaultLabel(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.loginButton.default"));
			form.setForgotPasswordLinkDefaultLabel(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.forgotPassword.default"));
			form.setHelpToolTipDefaultText(brandingService.getProperty(brands,form.getBrandName(),"lms.login.caption.help.tooltip.default"));
			form.setInvalidUsernameErrorDefaultMessage(
                            StringEscapeUtils.unescapeHtml(
                                brandingService.getProperty(brands,form.getBrandName(),"lms.login.loginInfo.default")));
			
			String aboutusDefaultClause="";
			if(request.getParameter("showDefaultValue")!=null && request.getParameter("templateText")!=null &&request.getParameter("templateText").equals("aboutus")){
				aboutusDefaultClause=".default";
			}
			form.setAboutUsSelectionOption(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.aboutus.selection"));
			form.setAboutUsURL(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.aboutus.URL"));
			form.setAboutUsEmail(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.aboutus.email"));
			form.setAboutUslmsTemplateText(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.aboutus.templateText"+aboutusDefaultClause));
			
			String contactusDefaultClause="";
			if(request.getParameter("showDefaultValue")!=null && request.getParameter("templateText")!=null &&request.getParameter("templateText").equals("contactus")){
				contactusDefaultClause=".default";
			}
			form.setContactUsSelectionOption(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.contactus.selection"));
			form.setContactUsURL(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.contactus.URL"));
			form.setContactUsEmail(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.contactus.email"));
			form.setContactUslmsTemplateText(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.contactus.templateText"+contactusDefaultClause));
						
			String termsofuseDefaultClause="";
			if(request.getParameter("showDefaultValue")!=null && request.getParameter("templateText")!=null &&request.getParameter("templateText").equals("termsofuse")){
				termsofuseDefaultClause=".default";
			}
			form.setTouSelectionOption(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.tou.selection"));
			form.setTouURL(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.tou.URL"));
			form.setTouEmail(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.tou.email"));
			form.setToulmsTemplateText(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.tou.templateText"+termsofuseDefaultClause));
			
			String privacypolicyDefaultClause="";
			if(request.getParameter("showDefaultValue")!=null && request.getParameter("templateText")!=null &&request.getParameter("templateText").equals("privacypolicy")){
				privacypolicyDefaultClause=".default";
			}
			form.setPrivacySelectionOption(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.privacy.selection"));
			form.setPrivacyURL(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.privacy.URL"));
			form.setPrivacyEmail(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.privacy.email"));
			form.setPrivacylmsTemplateText(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.privacy.templateText"+privacypolicyDefaultClause));
			}
			if(methodName.equalsIgnoreCase("editEmail")){
				String emailTopic=request.getParameter("emailTopic");
				String defaultClause=(request.getParameter("showDefaultValue")!=null)?".default":"";
			 if(emailTopic.equalsIgnoreCase("LMS Enrollment Result")){
				 form.setEmailTopic(emailTopic);
			   form= this.loadEmailTemplate(form,"lms.branding.email.enrollmentResult.title",
					                   "lms.email.managerenrollment.subject", "lms.email.managerenrollment.fromAddress",
					                   "lms.branding.email.enrollmentResult.templateText"+defaultClause);				  							
			 }
			 if(emailTopic.equalsIgnoreCase("Forgot Password")){								  
				   form= this.loadEmailTemplate(form,"lms.branding.email.forgotPassword.title",
						            "lms.email.forgetPassword.subject","lms.email.forgetPassword.fromAddress",
						            "lms.branding.email.forgotPassword.templateText"+defaultClause);					  							
			 }
			 if(emailTopic.equalsIgnoreCase("Your Password Has Been Updated")){								  
				   form= this.loadEmailTemplate(form,"lms.branding.email.passwordUpdated.title",
		                   "lms.emil.resetPassWord.subject", "lms.email.resetPassWord.fromAddress",
		                   "lms.branding.email.passwordUpdated.templateText"+defaultClause);	  							
             }			
			 if(emailTopic.equalsIgnoreCase("LMS Enrollment Notice")){								  
				   form= this.loadEmailTemplate(form,"lms.branding.email.enrollmentNotice.title",
						                   "lms.email.enrollment.subject", "lms.email.enrollment.fromAddress",
						                   "lms.branding.email.enrollmentNotice.templateText"+defaultClause);					  							
			 }
			 if(emailTopic.equalsIgnoreCase("LMS Account Details")){								  
				   form= this.loadEmailTemplate(form,"lms.branding.email.accountDetails.title",
						                   "lms.email.batchUpload.subject", "lms.email.batchUpload.fromAddress",
						                   "lms.branding.email.accountDetails.templateText"+defaultClause);					  							
			 }
				
			}
			
//			if( methodName.equals("saveCertificate")) {
//				//if( request.getParameter("id") != null ) {
//					//String CertId = request.getParameter("id");
//					//Certificate certificate = accreditationService.loadForUpdateCertificate(Long.parseLong(CertId));
//					//form.setDocument(certificate);
//				//}
//				MultipartFile file = form.getCertificate();
//				if ( file == null || file.isEmpty() ) {
//					log.debug(" file is null ");
//				} else {
//					byte[] filedata = file.getBytes();
//					form.setFileData(filedata);
//				}
//			}
			/*if(methodName.equalsIgnoreCase("advancedBranding")){
				brandingService.searchKeys("systemGenerated.");
			}*/			
			
	  }
		//else if( )  
		
	}
	
	
	/**
	 * This action createDirectory Structure of brand
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView createBrandDirectoryStructure(HttpServletRequest request, 
            HttpServletResponse response, 
            Object command, 
            BindException errors) throws Exception {
		
		BrandingForm brandingForm = (BrandingForm) command;
		Brand brand = null;
		
		if(!StringUtils.isEmpty(brandingForm.getBrandName())){

			brand = brandService.getBrandByName(brandingForm.getBrandName());
			if(brand==null){
				brand = new Brand();
				brand.setBrandName(brandingForm.getBrandName());
				brand.setBrandKey(brandingForm.getBrandName());
				brandService.save(brand);

				// get brand name and pass it to Script for creating directory structure  
				VU360Branding vu360Branding =  VU360Branding.getInstance();
				int isProcessExitSuccessfully = vu360Branding.constructBrandDirectoryStructure(brand.getBrandName());
				if(isProcessExitSuccessfully!=0){  
					String msg = "Unable to create brand directory structure, becasuse (Linux) OS process did not exit gracefully.";
					log.error(msg);
					brandService.delete(brand);				
					throw new Exception(msg);
				}
			}else{
				log.error("Brand name already exist...");
			}

		}else{
			return new ModelAndView(brandingBasicTemplate);
		}
		return  new ModelAndView("redirect:branding.do?method=basicBranding&brandName="+brandingForm.getBrandName());
	}
	
        
        /**
         * Handles reverting email templates for About Us, Contact Us, TOU and
         * Privacy Policy options.
         * 
         * @param request
         * @param response
         * @param command
         * @param errors
         * @return
         * @throws Exception 
         */	
	public ModelAndView showDefaultValue(HttpServletRequest request, 
            HttpServletResponse response, 
            Object command, 
            BindException errors) throws Exception {
            
            logger.info("showDefaultValue -- start");
            BrandingForm form = (BrandingForm)command;
            Properties brands= brandingService.getBrandProperties(form.getBrandName());
            String defaultClause = ".default";
        
            String templateType = request.getParameter("templateType");
            logger.debug("templateType: " + templateType);
            
            if( StringUtils.isEmpty(templateType) == false){
                if( "aboutusTemplate".equalsIgnoreCase(templateType)){
                    String property = brandingService.getProperty(
                                  brands,form.getBrandName(),
                                  "lms.branding.aboutus.templateText" + defaultClause);
                    logger.debug("default about us templateText: " + property);

                    form.setAboutUslmsTemplateText(property);
                }else if( "contactUSTemplate".equalsIgnoreCase(templateType)){
                    String property = brandingService.getProperty(
                    brands,form.getBrandName(),
                    "lms.branding.contactus.templateText" + defaultClause);
                    logger.debug("default contact us templateText: " + property);

                    form.setContactUslmsTemplateText(property);
                }else if( "touTemplate".equalsIgnoreCase(templateType)) {
                    String property = brandingService.getProperty(
                        brands,form.getBrandName(),
                        "lms.branding.tou.templateText" + defaultClause);
                    logger.debug("default TOU templateText: " + property);

                    form.setToulmsTemplateText(property);
                }else if( "privacyTemplate".equalsIgnoreCase(templateType)) {
                    String property = brandingService.getProperty(
                    brands,form.getBrandName(),
                    "lms.branding.privacy.templateText" + defaultClause);
                    logger.debug("default privacy policy templateText: " + property);

                    form.setPrivacylmsTemplateText(property);
                }
            }
            
            setRequestContextForDialogDisplay(request);
            logger.info("showDefaultValue -- end");
            return new ModelAndView(brandingBasicTemplate);
	}
        
       
        
	private BrandingForm loadEmailTemplate(BrandingForm form, String title,String subject,String addresses, String text){
		  Properties brands= brandingService.getBrandProperties(form.getBrandName());		  	
		  form.setEmailTitle(brandingService.getProperty(brands,form.getBrandName(),title));
		  form.setEmailSubject(brandingService.getProperty(brands,form.getBrandName(),subject));
		  form.setEmailAddresses(brandingService.getProperty(brands,form.getBrandName(),addresses));
		  form.setEmailTemplateText(brandingService.getProperty(brands,form.getBrandName(),text));							
		  
		  return form;
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	
	public ModelAndView basicBranding(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {						
		BrandingForm form = (BrandingForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
						
								
            List<String> emailList = new ArrayList<String>();
            emailList.add(new String("LMS Enrollment Result"));
            emailList.add(new String("Forgot Password"));
            emailList.add(new String("Your Password Has Been Updated "));
            emailList.add(new String("LMS Enrollment Notice"));
            emailList.add(new String("LMS Account Details"));
            
            //List<BrandProperty> searchKeys = brandingService.searchKeys(SYSTEMCODE,brandingService.getBrandProperties(form.getBrandName()));
         if(request.getParameter("pageTarget")!= null && request.getParameter("pageTarget").equalsIgnoreCase("emailList")){						
    			context.put("pageTarget", request.getParameter("pageTarget"));    	        
            Map<String, String> pagerAttributes = new HashMap <String, String>();
            
            //For sorting branding list based on keys.
            String sortColumnIndex = request.getParameter("sortColumnIndex");
            String sortDirection = request.getParameter("sortDirection");
            
            String pageIndex = request.getParameter("pageIndex");
            if( pageIndex == null ) pageIndex = "0";

            pagerAttributes.put("pageIndex", pageIndex);
            /*if( searchKeys.size() <= 10 ) {
                pagerAttributes.put("pageIndex", pageIndex);
            }*/
            
            String showAll = request.getParameter("showAll");
            if ( showAll == null ) 
                showAll = "false";
            if ( showAll.isEmpty() ) 
                showAll = "true";
            context.put("showAll", showAll);
            
            logger.debug("sortColumnIndex: " + sortColumnIndex);
            logger.debug("sortDirection: " + sortDirection);
            logger.debug("pageIndex: " + pageIndex);
            
            if( sortColumnIndex != null && (sortDirection != null && sortDirection.isEmpty() == false) ) {
                request.setAttribute("PagerAttributeMap", pagerAttributes);
                Integer order = Integer.valueOf(sortDirection);
                MappingSort sort = new MappingSort();
                sort.setSortDirection(order);
                final int  sortDirectionVal=Integer.parseInt(sortDirection);              
                Collections.sort(emailList,new Comparator<String>(){
					@Override
					public int compare(String o1, String o2) {
						if(sortDirectionVal==0){
							return o1.compareTo(o2)>=0?0:1;
						}else{
							return o1.compareTo(o2)>=0?1:0;
						}						
					}					                	
                });
                context.put("sortDirection", order);
                context.put("sortColumnIndex", 0);
            }/*else {
                filteredList = getFilteredList(searchKeys);
            }*/
            				
			}
         context.put("emailList", emailList);	 										
		return new ModelAndView(brandingBasicTemplate, "context", context);		
	}
		
	
	private boolean isAllDataValid(HttpServletRequest request,BrandingForm form){
		 
		boolean isSuccess= false;
		if(brandingService.getBrandProperties(form.getBrandName())!=null){			
			request.setAttribute("displayPage", true);
			isSuccess =true;
		}else{			
			form.setBrandName(form.getBrandName().length()>0?form.getBrandName():"unselected");
			request.setAttribute("displayPage", false);
			isSuccess=false;
		}
		return isSuccess;
			 
	}
		
	public ModelAndView advancedBranding(HttpServletRequest request,HttpServletResponse response, 
            Object command,BindException errors) throws Exception {
		        BrandingForm form = (BrandingForm)command;
                List<Mapping> filteredList = new ArrayList<Mapping>();
                List<Mapping> searchKeys = brandingService.searchKeys(
                    SYSTEMCODE,brandingService.getBrandProperties(form.getBrandName()));
                
		        Map<Object, Object> context = new HashMap<Object, Object>();
                Map<String, String> pagerAttributes = new HashMap <String, String>();
                
                //For sorting branding list based on keys.
                String sortColumnIndex = request.getParameter("sortColumnIndex");
                String sortDirection = request.getParameter("sortDirection");
                
                String pageIndex = request.getParameter("pageIndex");
                if( pageIndex == null ) pageIndex = "0";

                pagerAttributes.put("pageIndex", pageIndex);
                if( searchKeys.size() <= 10 ) {
                    pagerAttributes.put("pageIndex", pageIndex);
                }
                
                String showAll = request.getParameter("showAll");
                if ( showAll == null ) 
                    showAll = "false";
                if ( showAll.isEmpty() ) 
                    showAll = "true";
                context.put("showAll", showAll);
                
                logger.debug("sortColumnIndex: " + sortColumnIndex);
                logger.debug("sortDirection: " + sortDirection);
                logger.debug("pageIndex: " + pageIndex);
                
                if( sortColumnIndex != null && (sortDirection != null && sortDirection.isEmpty() == false) ) {
                    request.setAttribute("PagerAttributeMap", pagerAttributes);
                    Integer order = Integer.valueOf(sortDirection);
                    MappingSort sort = new MappingSort();
                    sort.setSortDirection(order);
                    
                    filteredList = getFilteredList(searchKeys);
                    Collections.sort(filteredList, sort);
                    context.put("sortDirection", sortDirection);
                    context.put("sortColumnIndex", sortColumnIndex);
                }else {
                    filteredList = getFilteredList(searchKeys);
                }
                
		context.put("systemBrandList", filteredList);

                logger.debug("context: " + context);
		return new ModelAndView(brandingAdvancedTemplate, "context", context);
		
	}
        
        private List<Mapping> getFilteredList(List<Mapping> searchKeys){
            List<Mapping> filteredList = new ArrayList<Mapping>();
            for( Mapping bp: searchKeys){
                bp.setKey(bp.getKey().substring(SYSTEMCODE.length()));
                filteredList.add(bp);
            }
            
            return filteredList;
        }
	
	public ModelAndView saveBranding(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		BrandingForm form = (BrandingForm) command;		
		//form= this.loadFormFromRequest(request,form);
		Properties brands= brandingService.getBrandProperties(form.getBrandName());
		brands.setProperty("lms.branding.aboutus.selection", "" + form.getAboutUsSelectionOption());
                
                setRequestContextForDialogDisplay(request);
		if(form.getAboutUsSelectionOption().equals("1")){
		   brands.setProperty("lms.login.footer.aboutusURL", "footerLinks.do?action=aboutus");
		}else if(form.getAboutUsSelectionOption().equals("2")){
			brands.setProperty("lms.login.footer.aboutusURL", ""+form.getAboutUsURL());
		}else if(form.getAboutUsSelectionOption().equals("3")){
			brands.setProperty("lms.login.footer.aboutusURL", "mailto:"+form.getAboutUsEmail());
		}				
		
		brands.setProperty("lms.branding.aboutus.URL", form.getAboutUsURL());
		brands.setProperty("lms.branding.aboutus.email", form.getAboutUsEmail());
		brands.setProperty("lms.branding.aboutus.templateText", form.getAboutUslmsTemplateText());								 
		
		
		if(form.getContactUsSelectionOption().equals("1")){
		   brands.setProperty("lms.login.footer.contactusURL", "footerLinks.do?action=contactus");
		}else if(form.getContactUsSelectionOption().equals("2")){
		   brands.setProperty("lms.login.footer.contactusURL", ""+form.getContactUsURL());
		}else if(form.getContactUsSelectionOption().equals("3")){
			brands.setProperty("lms.login.footer.contactusURL", "mailto:"+form.getContactUsEmail());
		}
		
		brands.setProperty("lms.branding.contactus.selection", ""+form.getContactUsSelectionOption());
		brands.setProperty("lms.branding.contactus.URL", form.getContactUsURL());
		brands.setProperty("lms.branding.contactus.email", form.getContactUsEmail());
		brands.setProperty("lms.branding.contactus.templateText", form.getContactUslmsTemplateText());
		
		if(form.getTouSelectionOption().equals("1")){
		   brands.setProperty("lms.login.footer.termofuserURL", "footerLinks.do?action=termsofuse");
		}else if(form.getTouSelectionOption().equals("2")){
		   brands.setProperty("lms.login.footer.termofuserURL", ""+form.getTouURL());
		}else if(form.getTouSelectionOption().equals("3")){
			brands.setProperty("lms.login.footer.termofuserURL", "mailto:"+form.getTouEmail());
		}
		
		brands.setProperty("lms.branding.tou.selection", ""+form.getTouSelectionOption());
		brands.setProperty("lms.branding.tou.URL", form.getTouURL());
		brands.setProperty("lms.branding.tou.email", form.getTouEmail());
		brands.setProperty("lms.branding.tou.templateText", form.getToulmsTemplateText());
		
		if(form.getPrivacySelectionOption().equals("1")){
		   brands.setProperty("lms.login.footer.onlineprivacypracticesURL", "footerLinks.do?action=onlineprivacy");
		}else if(form.getPrivacySelectionOption().equals("2")){
		   brands.setProperty("lms.login.footer.onlineprivacypracticesURL", ""+form.getPrivacyURL());
		}else if(form.getPrivacySelectionOption().equals("3")){
			brands.setProperty("lms.login.footer.onlineprivacypracticesURL", "mailto:"+form.getPrivacyEmail());
		}
		
		brands.setProperty("lms.branding.privacy.selection", ""+form.getPrivacySelectionOption());
		brands.setProperty("lms.branding.privacy.URL", form.getPrivacyURL());
		brands.setProperty("lms.branding.privacy.email", form.getPrivacyEmail());
		brands.setProperty("lms.branding.privacy.templateText", form.getPrivacylmsTemplateText());
		
		
		brands.setProperty("lms.branding.code", form.getBrandName()) ;
		brandingService.saveBrands(form.getBrandName(),brands);                
		
		return new ModelAndView(brandingBasicTemplate);
		
	}
	
	/*This method can be used with slight modifications when certificate feature is enabled
	 * public ModelAndView saveCertificate(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		BrandingForm form = (BrandingForm) command;			
		MultipartFile file = form.getCertificate();
		Properties brands= brandingService.getBrandProperties(form.getBrandName());
		File path= new File(brandingService.getProperty(brands,form.getBrandName(),"lms.branding.certificatesavePath")+file.getOriginalFilename());
		FileUtils.writeByteArrayToFile(path, file.getBytes());
		return new ModelAndView(brandingBasicTemplate);
	}*/
	
  public ModelAndView saveLmsLogo(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
	  if( errors.hasErrors() ) {
			return new ModelAndView(brandingBasicTemplate);
		}
		BrandingForm form = (BrandingForm) command;
		Brand brand = brandService.getBrandByName(form.getBrandName());
		//check brand exists LMS-13704
		if(brand!=null){
			MultipartFile file = form.getLmsLogo();		
			String pretext= "lmsLogo";
			String key= "lms.header.logo.src";
			if(file.getSize()>0 && file.getSize()<=MAX_FILE_SIZE){
				String imageRelativePath = saveLogo(form, file, pretext, key);						
				form.setLmsLogoPath(imageRelativePath);
				form.setShowLmsLogoRemoveLink(true);
			}    
		}
		return new ModelAndView(brandingBasicTemplate);
	}
  
  public ModelAndView removeLmsLogo(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {		
		BrandingForm form = (BrandingForm) command;
		String key= "lms.header.logo.src";						
		form.setLmsLogoPath(removeLogo(form, key));
		form.setShowLmsLogoRemoveLink(false);
		return new ModelAndView(brandingBasicTemplate);
	}
      
  
  public ModelAndView saveCoursePlayerLogo(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		if (errors.hasErrors()) {
			return new ModelAndView(brandingBasicTemplate);
		}
		
		BrandingForm form = (BrandingForm) command;
		Brand brandModel = brandService.getBrandByName(form.getBrandName());
		//check brand exists LMS-13704
		if (brandModel != null) {
			MultipartFile file = form.getCoursePlayerLogo();
			String pretext = "coursePlayerLogo";
			String key = "lms.courseplayer.logo.src";
			if (file.getSize() > 0 && file.getSize() <= MAX_FILE_SIZE) {
				String imageRelativePath = saveLogo(form, file, pretext, key);
				form.setCoursePlayerLogoPath(imageRelativePath);
				form.setShowCoursePlayerLogoRemoveLink(true);
				long brandId = 0;
				for (Brand brand : brandService.getAllBrands()) {
					if (brand.getBrandKey().equalsIgnoreCase(form.getBrandName()))
						brandId = brand.getId();
				}
				LcmsResource lcmsResource = customerService.loadForUpdateResource(brandId);
				// Brander brander =
				// VU360Branding.getInstance().getBrander(form.getBrandName(),
				// new Language());
				String lmsDomain = VU360Properties
						.getVU360Property("lms.domain");// FormUtil.getInstance().getLMSDomain(brander);
				String storablePath = lmsDomain.concat("/lms/"
						+ imageRelativePath);
				lcmsResource.setResourceValue(storablePath);
				customerService.savelcmsResource(lcmsResource);
				
				//invalidate brand cache so that course player show update image
				if(getLcmsClientWS()!=null)
					getLcmsClientWS().invalidateBrandCache(lcmsResource.getBrand().getBrandName(),  En_US);

			}
		}
		return new ModelAndView(brandingBasicTemplate);
  }
    	
  
  public ModelAndView removeCoursePlayerLogo(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {			  		
		BrandingForm form = (BrandingForm) command;
		String key= "lms.courseplayer.logo.src";
		String defaultImageRelativePath= this.removeLogo(form, key);
		form.setCoursePlayerLogoPath(defaultImageRelativePath);
		form.setShowCoursePlayerLogoRemoveLink(false);
		
		long brandId= 0;
		for(Brand brand:brandService.getAllBrands()){
			 if(brand.getBrandKey().equalsIgnoreCase(form.getBrandName()))
				 brandId= brand.getId();	 
		 }
		LcmsResource lcmsResource= customerService.loadForUpdateResource(brandId);		 
		//Brander brander = VU360Branding.getInstance().getBrander(form.getBrandName(), new Language());		 
        String lmsDomain=VU360Properties.getVU360Property("lms.domain");//FormUtil.getInstance().getLMSDomain(brander);			                
        String storablePath= lmsDomain.concat("/lms/"+defaultImageRelativePath);        
		lcmsResource.setResourceValue(storablePath);		 
		customerService.savelcmsResource(lcmsResource);
		
		//invalidate brand cache so that course player show update image
		if(getLcmsClientWS()!=null)
			getLcmsClientWS().invalidateBrandCache(lcmsResource.getBrand().getBrandName(), En_US);
			
	 return new ModelAndView(brandingBasicTemplate);
   }
  
  private String saveLogo(BrandingForm form, MultipartFile file, String pretext, String key) throws IOException {
		Properties brands= brandingService.getBrandProperties(form.getBrandName());						              		
		String absolutePathToNewImage=VU360Properties.getVU360Property("brands.basefolder")+form.getBrandName()+"/en/images/"+pretext;
		File javaFile= new File(absolutePathToNewImage);				
		javaFile= new File(absolutePathToNewImage);		
		if(!javaFile.exists()){
		  javaFile.createNewFile();
		}
		FileUtils. writeByteArrayToFile(javaFile, file.getBytes());
		String relativePathToNewImage= absolutePathToNewImage.substring(absolutePathToNewImage.indexOf("brands"));
		relativePathToNewImage= addPathCorrection().concat(relativePathToNewImage);//deploy env. compat
		brands.setProperty(key, relativePathToNewImage);
		brandingService.saveBrands(form.getBrandName(), brands);				
		return relativePathToNewImage;
	}

  private String addPathCorrection(){
	  String env=VU360Properties.getVU360Property("running.environment");
	  String pathCorrection="";
	  if(env!=null && !env.equalsIgnoreCase("DEV")){
		  pathCorrection="../";
	  }
	  return pathCorrection;
  }
  /**
   * This method updates the brands file with the path to default image 
   * @param form
   * @param key
   * @return
   */
  private String removeLogo(BrandingForm form, String key) {
		Properties brands= brandingService.getBrandProperties(form.getBrandName());								
		String absolutePathToDefaultImage=VU360Properties.getVU360Property("brands.basefolder")+"default/en/images/logo_360_52.gif";		
		String relativePathToDefaultImage= absolutePathToDefaultImage.substring(absolutePathToDefaultImage.indexOf("brands"));
		relativePathToDefaultImage=addPathCorrection().concat(relativePathToDefaultImage);//deploy env. compat				 		
		brands.setProperty(key, relativePathToDefaultImage);		
		brandingService.saveBrands(form.getBrandName(), brands);		
		return relativePathToDefaultImage;
	}  
	
  public ModelAndView editEmail(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
				
		return new ModelAndView(editEmailTemplate);
		
	}

	public ModelAndView saveEmail(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		BrandingForm form = (BrandingForm) command;
		 
		
			/*form.setEmailSubject(request.getParameter("emailSubject"));
			form.setEmailAddresses(request.getParameter("emailAddresses"));
			form.setEmailTemplateText(request.getParameter("emailTemplateText"));*/
			
			 if(form.getEmailTopic().equalsIgnoreCase("LMS Enrollment Result")){								  
				      this.loadandSaveEmailFromTemplate(form,"lms.email.managerenrollment.subject", 
						                   "lms.email.managerenrollment.fromAddress","lms.branding.email.enrollmentResult.templateText");				  							
				 }
				 if(form.getEmailTopic().equalsIgnoreCase("Forgot Password")){								  
					   this.loadandSaveEmailFromTemplate(form,"lms.email.forgetPassword.subject","lms.email.forgetPassword.fromAddress",
							            "lms.branding.email.forgotPassword.templateText");					  							
				 }
				 if(form.getEmailTopic().equalsIgnoreCase("Your Password Has Been Updated")){								  
					   this.loadandSaveEmailFromTemplate(form,"lms.emil.resetPassWord.subject", "lms.email.resetPassWord.fromAddress",
			                   "lms.branding.email.passwordUpdated.templateText");	  							
	             }			
				 if(form.getEmailTopic().equalsIgnoreCase("LMS Enrollment Notice")){								  
					   this.loadandSaveEmailFromTemplate(form,"lms.email.enrollment.subject", "lms.email.enrollment.fromAddress",
							                   "lms.branding.email.enrollmentNotice.templateText");					  							
				 }
				 if(form.getEmailTopic().equalsIgnoreCase("LMS Account Details")){								  
					   this.loadandSaveEmailFromTemplate(form,"lms.email.batchUpload.subject", "lms.email.batchUpload.fromAddress",
							                   "lms.branding.email.accountDetails.templateText");					  							
				 }
				 			
                request.setAttribute("formContext", "Emails");
		return new ModelAndView(editEmailTemplate);
		
	}
	
	private void loadandSaveEmailFromTemplate(BrandingForm form,String subject,String addresses, String text){
		 Properties brands= brandingService.getBrandProperties(form.getBrandName());
		 brands.setProperty(subject, form.getEmailSubject());
		 /*for now only save emails f they are valid*/
		 if(FieldsValidation.isEmailValid(form.getEmailAddresses())){
		    brands.setProperty(addresses, form.getEmailAddresses());
		 }
		 brands.setProperty(text, form.getEmailTemplateText());		 
		 brandingService.saveBrands(form.getBrandName(),brands);	  
		  
	}
	
	public void saveBrandName(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		BrandingForm form = (BrandingForm)command;
		String brandName= request.getParameter("brandName");
		if(brandName.length()>0){
		  Properties brands= brandingService.getBrandProperties(form.getBrandName());
		  brands.setProperty("lms.branding.code", brandName);
		  brandingService.saveBrands(form.getBrandName(),brands);
		}		

	}
	
	public ModelAndView saveLoginPageBrands(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		BrandingForm form = (BrandingForm)command;
		Properties brands= brandingService.getBrandProperties(form.getBrandName());
		
				  
		 brands.setProperty("lms.login.caption.message", form.getLoginMessage());
		 brands.setProperty("lms.login.caption.username", form.getUsernameLabel());
		 brands.setProperty("lms.login.caption.Password", form.getPasswordLabel());
		 brands.setProperty("lms.login.caption.loginButton", form.getLoginButtonLabel());
		 brands.setProperty("lms.login.caption.forgotPassword", form.getForgotPasswordLinkLabel());
		 brands.setProperty("lms.login.caption.help.tooltip", form.getHelpToolTipText());
		 brands.setProperty("lms.login.loginInfo", form.getInvalidUsernameErrorMessage());
		 //brands.setProperty("lms.login.loginInfo", form.getInvalidPasswordErrorMessage());		 
		 brandingService.saveBrands(form.getBrandName(),brands);
                
                 request.setAttribute("formContext", "Login Page");
                 
		 return new ModelAndView(brandingBasicTemplate);
	}
		
	public void saveAdvancedProperty(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		BrandingForm form = (BrandingForm)command;
		String item= request.getParameter("item");
		String key= request.getParameter("key");
		key= SYSTEMCODE.concat(key);
		String value= request.getParameter("value");
		Properties brands= brandingService.getBrandProperties(form.getBrandName());
		if(item.equalsIgnoreCase("key")){
			value=SYSTEMCODE.concat(value);
			brandingService.changeKeyName(key,value,form.getBrandName(),brands);
		}else{
			brands.setProperty(key,value);
			brandingService.saveBrands(form.getBrandName(),brands);
		}				  		 
	}
	
	public ModelAndView deleteAdvancedProperty(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{
		BrandingForm form = (BrandingForm)command;
		if  ( request.getParameterValues("brand") != null ) {
			long[] id = new long[request.getParameterValues("brand").length];
			String []keyArray = request.getParameterValues("brand");
			Properties brands= brandingService.getBrandProperties(form.getBrandName());
			for (int i=0;i<id.length;i++) {				
				brands.remove(SYSTEMCODE.concat(keyArray[i]));
			}
			brandingService.saveBrands(form.getBrandName(),brands);
		}
		
			return advancedBranding(request, response, command, errors);	
								  		 
	}
	
	public ModelAndView addNewKey(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{
		BrandingForm form = (BrandingForm)command;
		Properties brands= brandingService.getBrandProperties(form.getBrandName());
		int count= brandingService.searchKeys(SYSTEMCODE.concat("NewKeyName."),brands).size()+1; 				
		brands.setProperty(SYSTEMCODE.concat("NewKeyName.").concat(""+count), "Key Value");		
		brandingService.saveBrands(form.getBrandName(),brands);
		return advancedBranding(request,  response, command, errors);
	}
	
	public void addNewProperty(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		BrandingForm form = (BrandingForm)command;
		Properties brands= brandingService.getBrandProperties(form.getBrandName());				
		brands.setProperty("System.generated.New.Property", "New Property Text");
		brandingService.saveBrands(form.getBrandName(),brands);						  		 
	}
	
	public ModelAndView searchProperty(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		BrandingForm form = (BrandingForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		ArrayList<Mapping> filteredList= new ArrayList<Mapping>();
		String searchText= request.getParameter("name");
		Properties brands= brandingService.getBrandProperties(form.getBrandName());
		for(Mapping bp:brandingService.searchKeys(searchText,brands)){
			bp.setKey(bp.getKey().substring(SYSTEMCODE.length()));
			filteredList.add(bp);
		}		
		context.put("systemBrandList", filteredList);				
		return new ModelAndView(brandingAdvancedTemplate, "context", context);
								  		 
	}
	
	public ModelAndView showFooterPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		if(request.getParameter("page").equalsIgnoreCase("aboutus"))
		  return new ModelAndView(aboutUsTemplate);
		else if(request.getParameter("page").equalsIgnoreCase("contactus"))
			  return new ModelAndView(contactUsTemplate);
		else if(request.getParameter("page").equalsIgnoreCase("termsofuse"))
			  return new ModelAndView(termsOfUseTemplate);
		else
			  return new ModelAndView(onlineProvacyPolicyTemplate);
		
	}
	
	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		BrandingForm form = (BrandingForm)command; 
		BrandingValidator validator = (BrandingValidator)this.getValidator();
		validator.validate(form, errors);
		
		
	}
	public String getBrandingBasicTemplate() {
		return brandingBasicTemplate;
	}

	public void setBrandingBasicTemplate(String brandingBasicTemplate) {
		this.brandingBasicTemplate = brandingBasicTemplate;
	}

	public String getBrandingAdvancedTemplate() {
		return brandingAdvancedTemplate;
	}

	public void setBrandingAdvancedTemplate(String brandingAdvancedTemplate) {
		this.brandingAdvancedTemplate = brandingAdvancedTemplate;
	}
	public String getEditEmailTemplate() {
		return editEmailTemplate;
	}
	public void setEditEmailTemplate(String editEmailTemplate) {
		this.editEmailTemplate = editEmailTemplate;
	}
	public String getFailureTemplate() {
		return failureTemplate;
	}
	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}
	public BrandingService getBrandingService() {
		return brandingService;
	}
	public void setBrandingService(BrandingService brandingService) {
		this.brandingService = brandingService;
	}
	public String getAboutUsTemplate() {
		return aboutUsTemplate;
	}
	public void setAboutUsTemplate(String aboutUsTemplate) {
		this.aboutUsTemplate = aboutUsTemplate;
	}
	public String getContactUsTemplate() {
		return contactUsTemplate;
	}
	public void setContactUsTemplate(String contactUsTemplate) {
		this.contactUsTemplate = contactUsTemplate;
	}
	public String getTermsOfUseTemplate() {
		return termsOfUseTemplate;
	}
	public void setTermsOfUseTemplate(String termsOfUseTemplate) {
		this.termsOfUseTemplate = termsOfUseTemplate;
	}
	public String getOnlineProvacyPolicyTemplate() {
		return onlineProvacyPolicyTemplate;
	}
	public void setOnlineProvacyPolicyTemplate(String onlineProvacyPolicyTemplate) {
		this.onlineProvacyPolicyTemplate = onlineProvacyPolicyTemplate;
	}
	public CustomerService getCustomerService() {
		return customerService;
	}
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
    public LCMSClientWS getLcmsClientWS() {
		return lcmsClientWS;
	}
	public void setLcmsClientWS(LCMSClientWS lcmsClientWS) {
		this.lcmsClientWS = lcmsClientWS;
	}
	
	private void setRequestContextForDialogDisplay(HttpServletRequest request) {
        String saveContext = (String) request.getParameter("savecontext");
        logger.debug("saveContext: " + saveContext);
        
        if( StringUtils.isEmpty(saveContext)){
            request.setAttribute("formContext", "");
        }else if( saveContext.equalsIgnoreCase("aboutUs")){
            request.setAttribute("formContext", "About Us");
        }else if( saveContext.equalsIgnoreCase("contactUs")){
            request.setAttribute("formContext", "Contact Us");
        }else if( saveContext.equalsIgnoreCase("TOU")){
            request.setAttribute("formContext", "Terms of Use");
        }else if( saveContext.equalsIgnoreCase("privacy")){
            request.setAttribute("formContext", "Privacy Policy");
        }
    }
	public BrandService getBrandService() {
		return brandService;
	}
	public void setBrandService(BrandService brandService) {
		this.brandService = brandService;
	}	
        
}
