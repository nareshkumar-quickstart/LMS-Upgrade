package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.util.Brander;

/**
 * @author Saptarshi
 */
public class LocationForm implements ILMSBaseInterface {
	
	private static final long serialVersionUID = 1L;
	
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	
	private long id = 0;
	
	
	
	private Location location = new Location();
	private String streetAddress = null;
	private String streetAddress2 = null;
	private Brander brander = null;

	

	private String city = null;
	private String state = null;
	private String zipcode = null;
	private String country = null;
	// for checking the validation...
	public String eventSource = null;
	private String description = null;
	
	
	
	
	// for add Custom Field
	private List<ManageCustomField> manageCustomField = new ArrayList<ManageCustomField>();
	
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();


	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public Brander getBrander() {
		return brander;
	}

	public void setBrander(Brander brander) {
		this.brander = brander;
	}


	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getStreetAddress2() {
		return streetAddress2;
	}

	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	public List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> getCustomFields() {
		return customFields;
	}

	public void setCustomFields(
			List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields) {
		this.customFields = customFields;
	}

	public String getEventSource() {
		return eventSource;
	}

	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}

	public List<ManageCustomField> getManageCustomField() {
		return manageCustomField;
	}

	public void setManageCustomField(List<ManageCustomField> manageCustomField) {
		this.manageCustomField = manageCustomField;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	


	


}