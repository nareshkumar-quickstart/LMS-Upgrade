package com.softech.vu360.lms.web.controller.model;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;


public class BrandingForm  implements ILMSBaseInterface{	
	private static final long serialVersionUID = 1L;
	private String brandName="";	
	private String aboutUsSelectionOption;
	private String aboutUsURL;
	private String aboutUsEmail;
	private String aboutUslmsTemplateText;		
	
	private String contactUsSelectionOption;
	private String contactUsURL;
	private String contactUsEmail;
	private String contactUslmsTemplateText;
		
	private String touSelectionOption;
	private String touURL;
	private String touEmail;
	private String toulmsTemplateText;
	
	private String privacySelectionOption;
	private String privacyURL;
	private String privacyEmail;
	private String privacylmsTemplateText;
	
	private String emailTopic;
	private String emailTitle;
	private String emailSubject;
	private String emailAddresses;
	private String emailTemplateText;	
	
	private MultipartFile file;
	private MultipartFile certificate;
	private MultipartFile lmsLogo;
	private MultipartFile coursePlayerLogo;
	private byte[] fileData;
	private Document document = null;
	
	private String loginMessage;
	private String usernameLabel;
	private String passwordLabel; 	 	  	
	private String loginButtonLabel; 	  	
	private String forgotPasswordLinkLabel; 	  		 	  	
	private String helpToolTipText; 	  	
	private String invalidUsernameErrorMessage;
	
	private String loginDefaultMessage;
	private String usernameDefaultLabel;
	private String passwordDefaultLabel; 	 	  	
	private String loginButtonDefaultLabel; 	  	
	private String forgotPasswordLinkDefaultLabel; 	  		 	  	
	private String helpToolTipDefaultText; 	  	
	private String invalidUsernameErrorDefaultMessage;
	
	private String invalidPasswordErrorMessage;
	
	private String lmsLogoPath;
	private boolean showLmsLogoRemoveLink;
	private String coursePlayerLogoPath;
	private boolean showCoursePlayerLogoRemoveLink;
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getAboutUsSelectionOption() {
		return aboutUsSelectionOption;
	}

	public void setAboutUsSelectionOption(String aboutUsSelectionOption) {
		this.aboutUsSelectionOption = aboutUsSelectionOption;
	}

	public String getAboutUsURL() {
		return aboutUsURL;
	}

	public void setAboutUsURL(String aboutUsURL) {
		this.aboutUsURL = aboutUsURL;
	}

	public String getAboutUsEmail() {
		return aboutUsEmail;
	}

	public void setAboutUsEmail(String aboutUsEmail) {
		this.aboutUsEmail = aboutUsEmail;
	}

	public String getAboutUslmsTemplateText() {
		return aboutUslmsTemplateText;
	}

	public void setAboutUslmsTemplateText(String aboutUslmsTemplateText) {
		this.aboutUslmsTemplateText = aboutUslmsTemplateText;
	}

	public String getContactUsSelectionOption() {
		return contactUsSelectionOption;
	}

	public void setContactUsSelectionOption(String contactUsSelectionOption) {
		this.contactUsSelectionOption = contactUsSelectionOption;
	}

	public String getContactUsURL() {
		return contactUsURL;
	}

	public void setContactUsURL(String contactUsURL) {
		this.contactUsURL = contactUsURL;
	}

	public String getContactUsEmail() {
		return contactUsEmail;
	}

	public void setContactUsEmail(String contactUsEmail) {
		this.contactUsEmail = contactUsEmail;
	}

	public String getContactUslmsTemplateText() {
		return contactUslmsTemplateText;
	}

	public void setContactUslmsTemplateText(String contactUslmsTemplateText) {
		this.contactUslmsTemplateText = contactUslmsTemplateText;
	}

	public String getTouSelectionOption() {
		return touSelectionOption;
	}

	public void setTouSelectionOption(String touSelectionOption) {
		this.touSelectionOption = touSelectionOption;
	}

	public String getTouURL() {
		return touURL;
	}

	public void setTouURL(String touURL) {
		this.touURL = touURL;
	}

	public String getTouEmail() {
		return touEmail;
	}

	public void setTouEmail(String touEmail) {
		this.touEmail = touEmail;
	}

	public String getToulmsTemplateText() {
		return toulmsTemplateText;
	}

	public void setToulmsTemplateText(String toulmsTemplateText) {
		this.toulmsTemplateText = toulmsTemplateText;
	}

	public String getPrivacySelectionOption() {
		return privacySelectionOption;
	}

	public void setPrivacySelectionOption(String privacySelectionOption) {
		this.privacySelectionOption = privacySelectionOption;
	}

	public String getPrivacyURL() {
		return privacyURL;
	}

	public void setPrivacyURL(String privacyURL) {
		this.privacyURL = privacyURL;
	}

	public String getPrivacyEmail() {
		return privacyEmail;
	}

	public void setPrivacyEmail(String privacyEmail) {
		this.privacyEmail = privacyEmail;
	}

	public String getPrivacylmsTemplateText() {
		return privacylmsTemplateText;
	}

	public void setPrivacylmsTemplateText(String privacylmsTemplateText) {
		this.privacylmsTemplateText = privacylmsTemplateText;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(String emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public String getEmailTemplateText() {
		return emailTemplateText;
	}

	public void setEmailTemplateText(String emailTemplateText) {
		this.emailTemplateText = emailTemplateText;
	}

	public String getEmailTopic() {
		return emailTopic;
	}

	public void setEmailTopic(String emailTopic) {
		this.emailTopic = emailTopic;
	}

	public String getEmailTitle() {
		return emailTitle;
	}

	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public MultipartFile getCertificate() {
		return certificate;
	}

	public void setCertificate(MultipartFile certificate) {
		this.certificate = certificate;
	}

	public MultipartFile getLmsLogo() {
		return lmsLogo;
	}

	public void setLmsLogo(MultipartFile lmsLogo) {
		this.lmsLogo = lmsLogo;
	}

	public MultipartFile getCoursePlayerLogo() {
		return coursePlayerLogo;
	}

	public void setCoursePlayerLogo(MultipartFile coursePlayerLogo) {
		this.coursePlayerLogo = coursePlayerLogo;
	}

	public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}

	public String getUsernameLabel() {
		return usernameLabel;
	}

	public void setUsernameLabel(String usernameLabel) {
		this.usernameLabel = usernameLabel;
	}

	public String getPasswordLabel() {
		return passwordLabel;
	}

	public void setPasswordLabel(String passwordLabel) {
		this.passwordLabel = passwordLabel;
	}

	public String getLoginButtonLabel() {
		return loginButtonLabel;
	}

	public void setLoginButtonLabel(String loginButtonLabel) {
		this.loginButtonLabel = loginButtonLabel;
	}

	public String getForgotPasswordLinkLabel() {
		return forgotPasswordLinkLabel;
	}

	public void setForgotPasswordLinkLabel(String forgotPasswordLinkLabel) {
		this.forgotPasswordLinkLabel = forgotPasswordLinkLabel;
	}

	public String getHelpToolTipText() {
		return helpToolTipText;
	}

	public void setHelpToolTipText(String helpToolTipText) {
		this.helpToolTipText = helpToolTipText;
	}

	public String getInvalidUsernameErrorMessage() {
		return invalidUsernameErrorMessage;
	}

	public void setInvalidUsernameErrorMessage(String invalidUsernameErrorMessage) {
		this.invalidUsernameErrorMessage = invalidUsernameErrorMessage;
	}

	public String getInvalidPasswordErrorMessage() {
		return invalidPasswordErrorMessage;
	}

	public void setInvalidPasswordErrorMessage(String invalidPasswordErrorMessage) {
		this.invalidPasswordErrorMessage = invalidPasswordErrorMessage;
	}

	public String getLoginDefaultMessage() {
		return loginDefaultMessage;
	}

	public void setLoginDefaultMessage(String loginDefaultMessage) {
		this.loginDefaultMessage = loginDefaultMessage;
	}

	public String getUsernameDefaultLabel() {
		return usernameDefaultLabel;
	}

	public void setUsernameDefaultLabel(String usernameDefaultLabel) {
		this.usernameDefaultLabel = usernameDefaultLabel;
	}

	public String getPasswordDefaultLabel() {
		return passwordDefaultLabel;
	}

	public void setPasswordDefaultLabel(String passwordDefaultLabel) {
		this.passwordDefaultLabel = passwordDefaultLabel;
	}

	public String getLoginButtonDefaultLabel() {
		return loginButtonDefaultLabel;
	}

	public void setLoginButtonDefaultLabel(String loginButtonDefaultLabel) {
		this.loginButtonDefaultLabel = loginButtonDefaultLabel;
	}

	public String getForgotPasswordLinkDefaultLabel() {
		return forgotPasswordLinkDefaultLabel;
	}

	public void setForgotPasswordLinkDefaultLabel(
			String forgotPasswordLinkDefaultLabel) {
		this.forgotPasswordLinkDefaultLabel = forgotPasswordLinkDefaultLabel;
	}

	public String getHelpToolTipDefaultText() {
		return helpToolTipDefaultText;
	}

	public void setHelpToolTipDefaultText(String helpToolTipDefaultText) {
		this.helpToolTipDefaultText = helpToolTipDefaultText;
	}

	public String getInvalidUsernameErrorDefaultMessage() {
		return invalidUsernameErrorDefaultMessage;
	}

	public void setInvalidUsernameErrorDefaultMessage(
			String invalidUsernameErrorDefaultMessage) {
		this.invalidUsernameErrorDefaultMessage = invalidUsernameErrorDefaultMessage;
	}

	public String getLmsLogoPath() {
		return lmsLogoPath;
	}

	public void setLmsLogoPath(String lmsLogoPath) {
		this.lmsLogoPath = lmsLogoPath;
	}

	public String getCoursePlayerLogoPath() {
		return coursePlayerLogoPath;
	}

	public void setCoursePlayerLogoPath(String coursePlayerLogoPath) {
		this.coursePlayerLogoPath = coursePlayerLogoPath;
	}

	public boolean isShowLmsLogoRemoveLink() {
		return showLmsLogoRemoveLink;
	}

	public void setShowLmsLogoRemoveLink(boolean showLmsLogoRemoveLink) {
		this.showLmsLogoRemoveLink = showLmsLogoRemoveLink;
	}

	public boolean isShowCoursePlayerLogoRemoveLink() {
		return showCoursePlayerLogoRemoveLink;
	}

	public void setShowCoursePlayerLogoRemoveLink(
			boolean showCoursePlayerLogoRemoveLink) {
		this.showCoursePlayerLogoRemoveLink = showCoursePlayerLogoRemoveLink;
	}
	
	
	
}
