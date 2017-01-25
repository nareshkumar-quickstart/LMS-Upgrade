package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;

/**
 * 
 * @author haider.ali,
 * @dated: 16-09-15
 *
 */
@Entity
@Table(name = "SYNCHRONOUSCLASS")
public class SynchronousClass implements SearchableKey {

	private static final long serialVersionUID = 7115273783881904434L;
	@Id
	@javax.persistence.TableGenerator(name = "SYNCHRONOUSCLASS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SYNCHRONOUSCLASS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SYNCHRONOUSCLASS_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "CLASSNAME")
	private String sectionName = null;
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "COURSE_ID")
	private Course course ;
	@Column(name = "CLASSSTARTDATE")
	private Date classStartDate = null;
	@Column(name = "CLASSENDDATE")
	private Date classEndDate = null;
	@Column(name = "MAXIMUMCLASSSIZE")
	private Long maxClassSize = null;
	@Column(name = "MINIMUMCLASSSIZE")
	private Long minClassSize = null;
	@Column(name = "ENROLLMENTCLOSEDATE")
	private Date enrollmentCloseDate = null;
	@Column(name = "CLASSSTATUS")
	private String classStatus = null;
	@Column(name = "MEETING_ID")
	private String meetingID = null;
	@Column(name = "MEETINGPASSCODE")
	private String meetingPassCode = null;
	@Column(name = "MEETINGTYPE")
	private String meetingType = null;
	@Transient
	private String meetingURL = null;
	@Transient
	private  Boolean currentlyInSession = false;
	
	@OneToMany(mappedBy="synchronousClass")
	private List<SynchronousSession> synchronousSessions = null;
	
	@Transient
	private  Boolean selected = false;
	@Column(name = "GUID")
	private String guid = null;
	
	@OneToOne
    @JoinColumn(name = "TIMEZONE_ID")
	private TimeZone timeZone ;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="SYNCHRONOUSCLASS_LMSRESOURCE", joinColumns = @JoinColumn(name="SYNCHRONOUSCLASS_ID"),inverseJoinColumns = @JoinColumn(name="LMSRESOURCE_ID"))
	private List<Resource> resources = new ArrayList<Resource>();
	@OneToOne
	@JoinColumn(name = "LOCATION_ID")
	private  Location location ;
	
	public static final String MEETINGTYPE_DIMDIM = "DimDim";
	public static final String MEETINGTYPE_WEBEX = "WebEx";
	public static final String MEETINGTYPE_WEBINAR = "Webinar";
	public static final String MEETINGTYPE_OTHERS = "Others";
	@Column(name = "PRESENTER_FIRST_NAME")
	private String presenterFirstName = null;
	@Column(name = "PRESENTER_LAST_NAME")
	private String presenterLastName = null;
	@Column(name = "PRESENTER_EMAIL_ADDRESS")
	private String presenterEmailAddress = null;
	@Column(name = "AUTOMATIC")
	private Boolean automatic = true;
	
	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public  Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public List<SynchronousSession> getSynchronousSessions() {
		return synchronousSessions;
	}

	public void setSynchronousSessions(
			List<SynchronousSession> synchronousSessions) {
		this.synchronousSessions = synchronousSessions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.softech.vu360.lms.model.SearchableKey#getKey()
	 */
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getClassStartDate() {
		return classStartDate;
	}

	public String getClassStartDateString() {
		FormUtil formUtil = FormUtil.getInstance();
		String classSDate = (String) formUtil.formatDate(classStartDate,
				"MM/dd/yyyy");
		return classSDate;
	}

	public void setClassStartDate(Date classStartDate) {
		this.classStartDate = classStartDate;
	}

	public Date getClassEndDate() {
		return classEndDate;
	}

	public String getClassEndDateString() {
		FormUtil formUtil = FormUtil.getInstance();
		String classEDate = (String) formUtil.formatDate(classEndDate,
				"MM/dd/yyyy");
		return classEDate;
	}

	public void setClassEndDate(Date classEndDate) {
		this.classEndDate = classEndDate;
	}

	public Long getMaxClassSize() {
		return maxClassSize;
	}

	public void setMaxClassSize(Long maxClassSize) {
		this.maxClassSize = maxClassSize;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	/*
	 * public SynchronousCourse getSynchronousCourse() { return
	 * (SynchronousCourse) synchronousCourse.getValue(); } public void
	 * setSynchronousCourse(SynchronousCourse synchronousCourse) {
	 * this.synchronousCourse.setValue(synchronousCourse); }
	 */


	public String getClassStatus() {
		return classStatus;
	}

	public void setClassStatus(String classStatus) {
		this.classStatus = classStatus;
	}

	public String getMeetingID() {
		return meetingID;
	}

	public void setMeetingID(String meetingID) {
		this.meetingID = meetingID;
	}

	public String getMeetingPassCode() {
		return meetingPassCode;
	}

	public void setMeetingPassCode(String meetingPassCode) {
		this.meetingPassCode = meetingPassCode;
	}

	public String getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(String meetingType) {
		this.meetingType = meetingType;
	}

	public String getMeetingURL() {
		return meetingURL;
	}
	
	public void setMeetingURL(String meetingURL) {
		this.meetingURL = meetingURL;
	}
	
	public Long getMinClassSize() {
		return minClassSize;
	}

	public void setMinClassSize(Long minClassSize) {
		this.minClassSize = minClassSize;
	}

	public Date getEnrollmentCloseDate() {
		return enrollmentCloseDate;
	}

	public void setEnrollmentCloseDate(Date enrollmentCloseDate) {
		this.enrollmentCloseDate = enrollmentCloseDate;
	}

	public String getEnrollmentCloseDateStr() {
		return FormUtil.getInstance().formatDate(this.enrollmentCloseDate,
				"MM/dd/yyyy");
	}

	public  Boolean isCurrentlyInSession() {
		return currentlyInSession;
	}

	public void setCurrentlyInSession(Boolean currentlyInSession) {
		this.currentlyInSession = currentlyInSession;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public  Boolean isOnlineMeeting() {
		return (StringUtils.isBlank(meetingType) ? false : true);
	}

	/*
	 * public SynchronousSession getSynchronousSession() { return
	 * (SynchronousSession) synchronousSession.getValue(); }
	 * 
	 * 
	 * public void setSynchronousSession(SynchronousSession synchronousSession)
	 * { this.synchronousSession.setValue(synchronousSession); }
	 */
	/*
	 * public InstructorSynchronousClass getInstructorSynchronousClass() {
	 * return (InstructorSynchronousClass)
	 * instructorSynchronousClass.getValue(); }
	 * 
	 * public void setInstructorSynchronousClass(InstructorSynchronousClass
	 * instSynchClass) {
	 * this.instructorSynchronousClass.setValue(instSynchClass); }
	 */
	/**
	 * @return the resources
	 */
	public List<Resource> getResources() {
		return resources;
	}

	/**
	 * @param resources
	 *            the resources to set
	 */
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public  Boolean isDimDim() {
		return getMeetingType().equalsIgnoreCase(MEETINGTYPE_DIMDIM);
	}

	public String getLaunchURL(Brander brander) {
		if (this.getMeetingType() != null) {
			if ("dimdim".equalsIgnoreCase(this.getMeetingType().trim())) {
				return "/ins_launchClass.do?classId="
						+ this.getId().longValue();// brander.getBrandElement("lms.dimdim.instructor.launch.url");
			} else {
				return brander
						.getBrandElement("lms.instructor.class.launch.url");
			}
		} else {
			return "#";
		}

	}

	public String getPresenterFirstName() {
		return presenterFirstName;
	}

	public void setPresenterFirstName(String presenterFirstName) {
		this.presenterFirstName = presenterFirstName;
	}

	public String getPresenterLastName() {
		return presenterLastName;
	}

	public void setPresenterLastName(String presenterLastName) {
		this.presenterLastName = presenterLastName;
	}

	public String getPresenterEmailAddress() {
		return presenterEmailAddress;
	}

	public void setPresenterEmailAddress(String presenterEmailAddress) {
		this.presenterEmailAddress = presenterEmailAddress;
	}

	public  Boolean isAutomatic() {
		return automatic;
	}

	public void setAutomatic(Boolean automatic) {
		this.automatic = automatic;
	}

}
