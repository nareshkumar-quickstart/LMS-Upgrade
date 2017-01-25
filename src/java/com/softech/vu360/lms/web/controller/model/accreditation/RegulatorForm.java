package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Contact;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.Document;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;

/**
 * @author Dyutiman
 *
 */
public class RegulatorForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	
	private Brander brander = null;
	private Regulator regulator;
	// for adding one single contact
	private Contact contact;
	// for adding one single document
	private Document document;
	// for showing list of contacts -
	private List<Contact> contacts = new ArrayList <Contact>();
	private List<Provider> providers = new ArrayList <Provider>();
	private List<RegulatorCredential> regCredential = new ArrayList <RegulatorCredential>();
	private List<RegulatorContact> regContact = new ArrayList <RegulatorContact>();
	private List<DocumentForm> regDocument = new ArrayList <DocumentForm>();
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	private CustomField customField; //for add CustomField
	private List<RegulatorCategory> categories = new ArrayList<RegulatorCategory>();
	
	// for validation check -
	private String eventSource = "false";
	// for show all action
	private String ShowAllAction = "false";
	// for showing records
	private String showRecords = "false";
	
	private List<ManageCustomField> manageCustomField = new ArrayList<ManageCustomField>(); //for manage CustomField
	private long rID = 0;
	
	
	public Regulator getRegulator() {
		return regulator;
	}
	public void setRegulator(Regulator regulator) {
		this.regulator = regulator;
	}
	public List<Contact> getContacts() {
		return contacts;
	}
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public List<Provider> getProviders() {
		return providers;
	}
	public void setProviders(List<Provider> providers) {
		this.providers = providers;
	}
	public List<RegulatorCredential> getRegCredential() {
		return regCredential;
	}
	public void setRegCredential(List<RegulatorCredential> regCredential) {
		this.regCredential = regCredential;
	}
	public List<RegulatorContact> getRegContact() {
		return regContact;
	}
	public void setRegContact(List<RegulatorContact> regContact) {
		this.regContact = regContact;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public String getShowAllAction() {
		return ShowAllAction;
	}
	public void setShowAllAction(String showAllAction) {
		ShowAllAction = showAllAction;
	}

	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public String getShowRecords() {
		return showRecords;
	}
	public void setShowRecords(String showRecords) {
		this.showRecords = showRecords;
	}
	/**
	 * @return the customFields
	 */
	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFields() {
		return customFields;
	}
	/**
	 * @param customFields the customFields to set
	 */
	public void setCustomFields(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		this.customFields = customFields;
	}
	
	/**
	 * @return the customField
	 */
	public CustomField getCustomField() {
		return customField;
	}
	/**
	 * @param customField the customField to set
	 */
	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}
	/**
	 * @return the manageCustomField
	 */
	public List<ManageCustomField> getManageCustomField() {
		return manageCustomField;
	}
	/**
	 * @param manageCustomField the manageCustomField to set
	 */
	public void setManageCustomField(List<ManageCustomField> manageCustomField) {
		this.manageCustomField = manageCustomField;
	}
	/**
	 * @return the regDocument
	 */
	public List<DocumentForm> getRegDocument() {
		return regDocument;
	}
	/**
	 * @param regDocument the regDocument to set
	 */
	public void setRegDocument(List<DocumentForm> regDocument) {
		this.regDocument = regDocument;
	}
	public long getRID() {
		return rID;
	}
	public void setRID(long rid) {
		rID = rid;
	}
	public String getEventSource() {
		return eventSource;
	}
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
	public Brander getBrander() {
		return brander;
	}
	public void setBrander(Brander brander) {
		this.brander = brander;
	}
	public List<RegulatorCategory> getCategories() {
		return categories;
	}
	public void setCategories(List<RegulatorCategory> categories) {
		this.categories = categories;
	}

	

}