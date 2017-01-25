/**
 * 
 */
package com.softech.vu360.lms.web.controller.learner;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerPreferences;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.SimpleFormController;
import com.softech.vu360.lms.web.controller.helper.FileUploadUtils;
import com.softech.vu360.lms.web.controller.model.LearnerAvatarForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

/**
 * @author sana.majeed
 *
 */
public class LearnerAvatarController extends SimpleFormController {
		
	private static final Logger log = Logger.getLogger(LearnerAvatarController.class.getName());
	private VU360UserService vu360UserService = null;
	private LearnerService learnerService = null;
	private VelocityEngine velocityEngine = null;
	
	private static final String AVATAR_CONTENT_TYPE_JPEG = "image/jpeg";
	private static final String AVATAR_CONTENT_TYPE_GIF = "image/gif";
	private static final String AVATAR_CONTENT_TYPE_PNG = "image/png";
	private static final String AVATAR_CONTENT_TYPE_JPG = "image/pjpeg";
	private static final int AVATAR_MAX_SIZE = 51200;	
	private static final int AVATAR_MAX_WIDTH = 75;
	private static final int AVATAR_MAX_HEIGHT = 75;
	
	private static final String AVATAR_ROOT = VU360Properties.getVU360Property("document.avatar.root");
	private static final String AVATAR_URL = VU360Properties.getVU360Property("avatar.URL");
	private static final String AVATAR_UPLOAD_DIRECTORY = VU360Properties.getVU360Property("document.avatar.upload.location");
	private static final String AVATAR_DAFAULT_DIRECTORY = VU360Properties.getVU360Property("document.avatar.default.location");
	
	
	private int avatarWidth = 0;
	private int avatarHeight = 0;
	
	public LearnerAvatarController(){
		super();
		setCommandName("learnerAvatarForm");
		setCommandClass(LearnerAvatarForm.class);
		setSessionForm(true);
		setFormView("learner/learnerAvatar");
		setSuccessView("redirect:lrn_learnerPreferences.do");
		setBindOnNewForm(true);
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)throws Exception {
		return super.formBackingObject(request);
	}
		
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData (HttpServletRequest request, Object command, Errors errors) throws Exception {
		
		log.debug("In referenceData");
		
		LearnerAvatarForm form = (LearnerAvatarForm) command;
		List<String> defaultAvatarList = new ArrayList<String>();
		
		// Load default avatar
		Brander brander = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());
		String[] avatarArray = brander.getBrandElements("lms.learner.default.avatars");
		for ( String curAvatar : avatarArray ) {
			defaultAvatarList.add( AVATAR_DAFAULT_DIRECTORY + curAvatar );
		}
				
		// Load user avatar, if exists
		VU360User vu360User = VU360UserAuthenticationDetails.getCurrentUser();
		if ( vu360User.getLearner().getPreference() != null && vu360User.getLearner().getPreference().getAvatar() != null ) {
			if ( StringUtils.isNotBlank( vu360User.getLearner().getPreference().getAvatar() ) ) {
				defaultAvatarList.add( AVATAR_UPLOAD_DIRECTORY + vu360User.getLearner().getPreference().getAvatar() );
			}
		}		
    	
		form.setDefaultAvatarList(defaultAvatarList);
		form.setAvatarURL( AVATAR_URL );
		
    	return super.referenceData(request, command, errors);
	}
	
	/**
	 * Validate before Submission
	 */
	protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		log.debug("In processFormSubmission");
						
		LearnerAvatarForm form = (LearnerAvatarForm) command;		
				
		if ( StringUtils.isNotBlank(form.getAvatar().getOriginalFilename()) ) {
			// Validate custom image
			MultipartFile avatar = form.getAvatar();	
			
			if ( (!avatar.getContentType().equalsIgnoreCase(AVATAR_CONTENT_TYPE_JPEG)) 
					&& (!avatar.getContentType().equalsIgnoreCase(AVATAR_CONTENT_TYPE_GIF)) 
					&& (!avatar.getContentType().equalsIgnoreCase(AVATAR_CONTENT_TYPE_PNG))
					&& (!avatar.getContentType().equalsIgnoreCase(AVATAR_CONTENT_TYPE_JPG))) {
								
				errors.rejectValue("avatar", "errors.learner.preference.avatar.type");
			}
			
			if ( avatar.getSize() > AVATAR_MAX_SIZE ) {
				errors.rejectValue("avatar", "errors.learner.preference.avatar.size");
			}
			
			try {				
				BufferedImage image =  ImageIO.read( new ByteArrayInputStream( avatar.getBytes()) );				
				if ( image.getWidth() <= 0 
						|| image.getWidth() > AVATAR_MAX_WIDTH 
						|| image.getHeight() <= 0
						|| image.getHeight() > AVATAR_MAX_HEIGHT ) {
					
					errors.rejectValue("avatar", "errors.learner.preference.avatar.dimension");
					this.avatarWidth = 0;
					this.avatarHeight = 0;
				}
				else {
					this.avatarWidth = image.getWidth();
					this.avatarHeight = image.getHeight();
				}
			}
			catch (Exception e) {
				log.debug("Error while getting custom image dimensions: " + e);
			}
			
		}
		else {
			// Validate default image
			int selectedIndex = Integer.parseInt( request.getParameter("defaultAvatar") );
			String fileName = form.getDefaultAvatarList().get(selectedIndex);
			
			try {
				File avatar = new File( AVATAR_ROOT + fileName );
				BufferedImage image = ImageIO.read( avatar );
				
				if ( image.getWidth() <= 0 
						|| image.getWidth() > AVATAR_MAX_WIDTH 
						|| image.getHeight() <= 0
						|| image.getHeight() > AVATAR_MAX_HEIGHT ) {
					
					errors.rejectValue("avatar", "errors.learner.preference.avatar.dimension");
					this.avatarWidth = 0;
					this.avatarHeight = 0;
				}
				else {
					this.avatarWidth = image.getWidth();
					this.avatarHeight = image.getHeight();
				}					
			}				
			catch (Exception e) {
				log.debug("Error while getting default image dimensions: " + e);
			}				
		}
				
		return super.processFormSubmission(request, response, command, errors);
	}
	
	
	protected ModelAndView onSubmit (HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws ServletException, IOException {
		
		log.debug("In onSubmit");
		ModelAndView modelAndView = new ModelAndView();
		try {
			LearnerAvatarForm form = (LearnerAvatarForm) command;
			
			VU360User vu360User = VU360UserAuthenticationDetails.getCurrentUser();
			String avatarName = null;
			if ( StringUtils.isNotBlank(form.getAvatar().getOriginalFilename()) ) {
				avatarName = this.uploadCustomAvatar( form.getAvatar(), vu360User.getLearner() );
			}
			else {
				int selectedIndex = Integer.parseInt( request.getParameter("defaultAvatar") );				
				avatarName = this.uploadAvatar( form.getDefaultAvatarList().get(selectedIndex), vu360User.getLearner() );
			}
						
			// Save avatar information in LMS DB
			LearnerPreferences learnerPreferences = new LearnerPreferences();
			learnerPreferences.setLearner( vu360User.getLearner() );
			if (vu360User.getLearner().getPreference() != null) {
				learnerPreferences = vu360User.getLearner().getPreference();
			}
			learnerPreferences.setAvatar( avatarName );
			learnerPreferences = this.learnerService.saveLearnerPreferencesToVCS(vu360User.getLearner(), learnerPreferences, this.avatarWidth, this.avatarHeight);
			vu360User.getLearner().setPreference( learnerPreferences );
			
			modelAndView = super.onSubmit(request, response, command, errors);
		}
		catch (Exception e) {
			errors.rejectValue("avatar", "errors.learner.preference.avatar.upload");
			log.info(e.getMessage() + e.getStackTrace());
			
			try {
				modelAndView = super.showForm(request, response, errors);
			} catch (Exception e1) {				
				log.info(e.getMessage() + e.getStackTrace());
			}
		}		
		return modelAndView;
	}
	
	/**
	 * Upload avatar which user has browsed from their computer
	 * @param avatar
	 * @param learner
	 * @throws IOException
	 */
	private String uploadCustomAvatar (MultipartFile avatar, Learner learner) throws IOException {
		
		// Create destination file
		File destFile = new File( AVATAR_ROOT + AVATAR_UPLOAD_DIRECTORY 
				+ this.learnerService.getLearnerAvatarNameFromVCS( learner )
				+ avatar.getOriginalFilename().substring( avatar.getOriginalFilename().indexOf('.') ) );
		log.info("Destination File name: " + destFile.getCanonicalPath());
		
		// Upload file to server
		FileUploadUtils.copyFile(avatar, destFile);	
		
		return destFile.getName();
	}
	
	private String uploadAvatar (String srcFileName, Learner learner) throws IOException {
		
		// Create destination file
		String destFileName = this.learnerService.getLearnerAvatarNameFromVCS( learner ) + srcFileName.substring( srcFileName.indexOf('.') );
		log.info("Destination File name: " + destFileName);
		
		// Upload file to server
		FileUploadUtils.copy(AVATAR_ROOT + srcFileName, AVATAR_ROOT + AVATAR_UPLOAD_DIRECTORY + destFileName);
		
		return destFileName;
	}
	
	
	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}
	
	/**
	 * @param velocityEngine the velocityEngine to set
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	/**
	 * @return the velocityEngine
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}


	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}


	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}
}
