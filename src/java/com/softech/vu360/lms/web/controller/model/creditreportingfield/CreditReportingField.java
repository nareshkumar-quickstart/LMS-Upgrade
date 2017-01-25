/**
 * 
 */
package com.softech.vu360.lms.web.controller.model.creditreportingfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.exception.TemplateServiceException;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.service.impl.TemplateServiceImpl;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;

/**
 * @author Arijit
 *
 */
public class CreditReportingField implements Comparable<CreditReportingField> , ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(CreditReportingField.class);

	public CreditReportingField(com.softech.vu360.lms.model.CreditReportingField creditReportingFieldRef,CreditReportingFieldValue creditReportingFieldValueRef) {
		super();
		this.creditReportingFieldRef = creditReportingFieldRef;
		this.creditReportingFieldValueRef = creditReportingFieldValueRef;
	}
	
	private com.softech.vu360.lms.model.CreditReportingField creditReportingFieldRef;
	private com.softech.vu360.lms.model.CreditReportingFieldValue creditReportingFieldValueRef;
	private String templatePath;
	private String[] selectedChoices;
	private List<CreditReportingFieldValueChoice> creditReportingFieldValueChoices = new ArrayList<CreditReportingFieldValueChoice>();
	private int status=0;

	/**
	 * @return the creditReportingFieldRef
	 */
	public com.softech.vu360.lms.model.CreditReportingField getCreditReportingFieldRef() {
		return creditReportingFieldRef;
	}
	/**
	 * @param creditReportingFieldRef the creditReportingFieldRef to set
	 */
	public void setCreditReportingFieldRef(
			com.softech.vu360.lms.model.CreditReportingField creditReportingFieldRef) {
		this.creditReportingFieldRef = creditReportingFieldRef;
	}
	
	/**
	 * @return the creditReportingFieldValueRef
	 */
	public com.softech.vu360.lms.model.CreditReportingFieldValue getCreditReportingFieldValueRef() {
		return creditReportingFieldValueRef;
	}
	/**
	 * @param creditReportingFieldValueRef the creditReportingFieldValueRef to set
	 */
	public void setCreditReportingFieldValueRef(
			com.softech.vu360.lms.model.CreditReportingFieldValue creditReportingFieldValueRef) {
		this.creditReportingFieldValueRef = creditReportingFieldValueRef;
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
	/**
	 * @return the creditReportingFieldValueChoices
	 */
	public List<CreditReportingFieldValueChoice> getCreditReportingFieldValueChoices() {
		return creditReportingFieldValueChoices;
	}
	/**
	 * @param creditReportingFieldValueChoices the creditReportingFieldValueChoices to set
	 */
	public void setCreditReportingFieldValueChoices(
			List<CreditReportingFieldValueChoice> creditReportingFieldValueChoices) {
		this.creditReportingFieldValueChoices = creditReportingFieldValueChoices;
	}
	
	public void addCreditReportingFieldValueChoice(CreditReportingFieldValueChoice creditReportingFieldValueChoice){
		if(this.creditReportingFieldValueChoices==null)
			creditReportingFieldValueChoices = new ArrayList<CreditReportingFieldValueChoice>();
		this.creditReportingFieldValueChoices.add(creditReportingFieldValueChoice);
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
	@Override
	public int compareTo(CreditReportingField o) {
		return this.getCreditReportingFieldRef().getWeight()<= 
			o.getCreditReportingFieldRef().getWeight()?-1:1;
		
	}
	
}
