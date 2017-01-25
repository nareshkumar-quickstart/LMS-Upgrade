package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.model.accreditation.ManageCustomField;
import com.softech.vu360.util.Brander;

public class CustomLocationForm  implements ILMSBaseInterface{
	
	
	private static final long serialVersionUID = 1L;
	public static final String CUSTOMFIELD_SINGLE_LINE_OF_TEXT = "Single Line of Text";
	public static final String CUSTOMFIELD_DATE = "Date";
	public static final String CUSTOMFIELD_MULTIPLE_LINES_OF_TEXT = "Multiple Lines of Text";
	public static final String CUSTOMFIELD_NUMBER = "Number";
	public static final String CUSTOMFIELD_RADIO_BUTTON = "Radio Button";
	public static final String CUSTOMFIELD_CHOOSE = "Choose Menu";
	public static final String CUSTOMFIELD_CHECK_BOX = "Check Box";
	public static final String CUSTOMFIELD_SOCIAL_SECURITY_NUMBER = "Social Security Number";
	
	public static final String PROVIDER_APPROVAL = "Provider";
	public static final String INSTRUCTOR_APPROVAL = "Instructor";
	public static final String COURSE_APPROVAL = "Course";
	
	private Brander brander = null;
	private long locId = 0;
	private Location location = new Location();
	
	private String entity = null;
	private ProviderApproval providerApproval = null;
	private InstructorApproval instructorApproval = null;
	private CourseApproval courseApproval = null;
	private List<Location> locations = new ArrayList<Location>();
	private List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFields = new ArrayList<com.softech.vu360.lms.web.controller.model.customfield.CustomField>();
	
	private String name = null;
	private String locationId = null;
	
	private String pageIndex;
	private String showAll = "false";
	private String pageCurrIndex;
	
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	
	// for add Custom Field
	private List<ManageCustomField> manageCustomField = new ArrayList<ManageCustomField>();
	
	// for checking the validation...
	public String eventSource = null;
	
	

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the providerApproval
	 */
	public ProviderApproval getProviderApproval() {
		return providerApproval;
	}

	/**
	 * @param providerApproval the providerApproval to set
	 */
	public void setProviderApproval(ProviderApproval providerApproval) {
		this.providerApproval = providerApproval;
	}

	/**
	 * @return the instructorApproval
	 */
	public InstructorApproval getInstructorApproval() {
		return instructorApproval;
	}

	/**
	 * @param instructorApproval the instructorApproval to set
	 */
	public void setInstructorApproval(InstructorApproval instructorApproval) {
		this.instructorApproval = instructorApproval;
	}

	/**
	 * @return the providers
	 */
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the providerId
	 */
	

	/**
	 * @return the courseApproval
	 */
	public CourseApproval getCourseApproval() {
		return courseApproval;
	}

	/**
	 * @param courseApproval the courseApproval to set
	 */
	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
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
	 * @return the provId
	 */
	

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
	 * @return the pageIndex
	 */
	public String getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the showAll
	 */
	public String getShowAll() {
		return showAll;
	}

	/**
	 * @param showAll the showAll to set
	 */
	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}

	/**
	 * @return the pageCurrIndex
	 */
	public String getPageCurrIndex() {
		return pageCurrIndex;
	}

	/**
	 * @param pageCurrIndex the pageCurrIndex to set
	 */
	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}

	/**
	 * @return the sortColumnIndex
	 */
	public String getSortColumnIndex() {
		return sortColumnIndex;
	}

	/**
	 * @param sortColumnIndex the sortColumnIndex to set
	 */
	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public long getLocId() {
		return locId;
	}

	public void setLocId(long locId) {
		this.locId = locId;
	}
	


}
