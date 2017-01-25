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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.FormUtil;

/**
 * 
 * @author haider.ali,
 * @dated: 16-09-15
 *
 */
@Entity
@Table(name = "SYNCHRONOUSSESSION")
public class SynchronousSession implements SearchableKey {
	
	private static final long serialVersionUID = 2688408452179538928L;
	@Id
	@javax.persistence.TableGenerator(name = "SYNCHRONOUSSESSION_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SYNCHRONOUSSESSION", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "SYNCHRONOUSSESSION_ID")
	private Long id;
	
	@Column(name = "STARTDATETIME")
	private Date startDateTime = null;
	@Column(name = "ENDDATETIME")
	private Date endDateTime = null;
	@Transient
	private Date startDateTimeZ=null;
	@Transient
	private Date endDateTimeZ = null;
	@Transient
	private String MeetingGUID=null;
	
	public static final String INPROGRESS="In Progress";
	public static final String COMPLETED="Completed";
	public static final String NOTSTARTED="Not Started";
	
	@OneToOne ( cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name="SYNCHRONOUSCLASS_ID")
	private SynchronousClass synchronousClass ;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="SYNCHRONOUSCLASS_LMSRESOURCE", joinColumns = @JoinColumn(name="SYNCHRONOUSCLASS_ID"),inverseJoinColumns = @JoinColumn(name="LMSRESOURCE_ID"))
	private List<Resource> resources = new ArrayList<Resource>();
	
	@OneToOne
	@JoinColumn(name = "LOCATION_ID")
	private Location location;
	
	@Override
	public String getKey() {
		return id.toString();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}
	
	public String getStartDate() {
		return FormUtil.getInstance().formatDate(startDateTime, "MM/dd/yyyy");
	}

	public String getEndDate() {
		return FormUtil.getInstance().formatDate(endDateTime, "MM/dd/yyyy");
	}

	public String getStartTime() {
		if(startDateTimeZ!=null)
			return FormUtil.getInstance().formatDate(startDateTimeZ, "hh:mm a");
		else
			return FormUtil.getInstance().formatDate(startDateTime, "hh:mm a");
	}

	public String getEndTime() {
		if(endDateTimeZ!=null)
			return FormUtil.getInstance().formatDate(endDateTimeZ, "hh:mm a");
		else
			return FormUtil.getInstance().formatDate(endDateTime, "hh:mm a");
	}

	
	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public SynchronousClass getSynchronousClass() {
		return synchronousClass;
	}

	public void setSynchronousClass(SynchronousClass synchronousClass) {
		this.synchronousClass = synchronousClass;
	}

	public String getDayOfStartSession(){
		return FormUtil.getInstance().formatShortDayOfWeek(startDateTime);
	}
	
	public String getStartDate(String strPattern) {
		return FormUtil.getInstance().formatDate(startDateTime, strPattern);
	}

	public String getEndDate(String strPattern) {
		return FormUtil.getInstance().formatDate(endDateTime, strPattern);
	}

	@Deprecated
	public String getStatus(){
		/*
		 * @Wajahat: Handling Server time with Timezone and Day light saving options
		 */
		Date currentDateTime = DateUtil.getCurrentServerTimeGMT();//.compareTo(gmtStartDateTime);
		Date gmtStartDateTime = DateUtil.getGMTDateForTimezoneHours(startDateTime, getSynchronousClass().getTimeZone().getHours());
		Date gmtEndDateTime = DateUtil.getGMTDateForTimezoneHours(endDateTime, getSynchronousClass().getTimeZone().getHours());
		
		if(currentDateTime.compareTo(gmtStartDateTime)>=0 && currentDateTime.compareTo(gmtEndDateTime)<0){
			return INPROGRESS;
		}
		if(currentDateTime.compareTo(gmtStartDateTime)<0){
			return NOTSTARTED;
		}
		if(currentDateTime.compareTo(gmtEndDateTime)>=0){
			return COMPLETED;
		}
		return "";
	}

	public Date getStartDateTimeZ() {
		return startDateTimeZ;
	}

	public void setStartDateTimeZ(Date startDateTimeZ) {
		this.startDateTimeZ = startDateTimeZ;
	}

	public Date getEndDateTimeZ() {
		return endDateTimeZ;
	}

	public void setEndDateTimeZ(Date endDateTimeZ) {
		this.endDateTimeZ = endDateTimeZ;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getMeetingGUID() {
		return MeetingGUID;
	}

	public void setMeetingGUID(String meetingGUID) {
		MeetingGUID = meetingGUID;
	}

}
