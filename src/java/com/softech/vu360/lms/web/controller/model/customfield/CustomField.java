package com.softech.vu360.lms.web.controller.model.customfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.exception.TemplateServiceException;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.service.impl.TemplateServiceImpl;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.Brander;

public class CustomField  implements ILMSBaseInterface{
	private static Logger log = Logger.getLogger(CustomField.class);
	private static final long serialVersionUID = 1L;
	public CustomField(com.softech.vu360.lms.model.CustomField customFieldRef,CustomFieldValue customFieldValueRef) {
		super();
		this.customFieldRef = customFieldRef;
		this.customFieldValueRef = customFieldValueRef;
		if(null != this.customFieldValueRef && null == this.customFieldValueRef.getCustomField())
			this.customFieldValueRef.setCustomField(customFieldRef);
	}
	
	private com.softech.vu360.lms.model.CustomField customFieldRef;
	private com.softech.vu360.lms.model.CustomFieldValue customFieldValueRef;
	private String templatePath;
	private VU360UserMode adminCurrentMode;
	private List<CustomFieldValueChoice> customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
	private String[] selectedChoices;
	private int status=0;

	/**
	 * @return the customFieldRef
	 */
	public com.softech.vu360.lms.model.CustomField getCustomFieldRef() {
		return customFieldRef;
	}

	/**
	 * @param customFieldRef the customFieldRef to set
	 */
	public void setCustomFieldRef(
			com.softech.vu360.lms.model.CustomField customFieldRef) {
		this.customFieldRef = customFieldRef;
	}

	/**
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
	
	public String renderCustomField(String id,String name,String customFieldValueIdentifire,String customFieldIdentifire){
		return this.renderCustomField(id,name,customFieldValueIdentifire,customFieldIdentifire, null);
	}
	
	public String renderCustomField(String id,String name,String customFieldValueIdentifire,String customFieldIdentifire, Brander brander){
		TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
		HashMap<Object, Object> attrs = new HashMap<Object, Object>();
		attrs.put("id", id);
		attrs.put("name", name);
		attrs.put("customFieldValueIdentifire", customFieldValueIdentifire);
		attrs.put("customFieldIdentifire", customFieldIdentifire);
		attrs.put("field", this);
		if(brander!=null)
			attrs.put("brander", brander);
		try {
			return tmpSvc.renderTemplate(this.templatePath, attrs);
		} catch (TemplateServiceException e) {
			log.error(e);
			return "";
		}
	}

	/**
	 * @return the customFieldValueRef
	 */
	public com.softech.vu360.lms.model.CustomFieldValue getCustomFieldValueRef() {
		return customFieldValueRef;
	}

	/**
	 * @param customFieldValueRef the customFieldValueRef to set
	 */
	public void setCustomFieldValueRef(
			com.softech.vu360.lms.model.CustomFieldValue customFieldValueRef) {
		this.customFieldValueRef = customFieldValueRef;
	}
	
	public void addCustomFieldValueChoice(CustomFieldValueChoice customFieldValueChoice){
		if(this.customFieldValueChoices==null)
			customFieldValueChoices = new ArrayList<CustomFieldValueChoice>();
		this.customFieldValueChoices.add(customFieldValueChoice);
	}

	/**
	 * @return the singleSelectCustomFieldOptions
	 */
	public List<CustomFieldValueChoice> getCustomFieldValueChoices() {
		return customFieldValueChoices;
	}

	/**
	 * @param customFieldValueChoices the singleSelectCustomFieldOptions to set
	 */
	public void setCustomFieldValueChoices(
			List<CustomFieldValueChoice> customFieldValueChoices) {
		this.customFieldValueChoices = customFieldValueChoices;
	}

	/**
	 * @return the selectedChoices
	 */
	public String[] getSelectedChoices() {
		return selectedChoices;
	}

	/**
	 * @param selectedChoices the selectedChoices to set
	 */
	public void setSelectedChoices(String[] selectedChoices) {
		this.selectedChoices = selectedChoices;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getEncriptedValue(){
		String encriptedValue = "";
		if (customFieldRef.getFieldEncrypted()){
			int strLen =  customFieldValueRef.getValue().toString().length();
			for ( int i = 0; i<strLen; i++){
				encriptedValue = encriptedValue + "*";
			}
		}
		return encriptedValue;
	}

	/**
	 * @return the adminCurrentMode
	 */
	public VU360UserMode getAdminCurrentMode() {
		return adminCurrentMode;
	}

	/**
	 * @param adminCurrentMode the adminCurrentMode to set
	 */
	public void setAdminCurrentMode(VU360UserMode adminCurrentMode) {
		this.adminCurrentMode = adminCurrentMode;
	}

}
