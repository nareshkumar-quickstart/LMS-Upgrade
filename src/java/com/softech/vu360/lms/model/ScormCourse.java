/**
 * 
 */
package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("Scorm Course")
public class ScormCourse extends Course {

    private static final long serialVersionUID = 1L;
	public static final String SCORM_1_2 = "SCORM 1.2";
	public static final String SCORM_2004 = "SCORM 2004";
	public static final String COURSE_TYPE="Scorm Course";
	
	
	@OneToMany(mappedBy = "course" , cascade = { CascadeType.MERGE })
    private List<SCO> scos = new ArrayList<SCO>();
	
	@Column(name = "COURSEVENDOR")
	private String courseVendor = null;
	
	@Column(name = "REPORTEDSCORMVERSION")
	private String reportedSCORMVersion = SCORM_2004;
	
	

    public ScormCourse() {
        super();
        setCourseType(COURSE_TYPE);
    }

	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}
	/**
	 * @return the scos
	 */
	public List<SCO> getScos() {
		return scos;
	}

	/**
	 * @param scos the scos to set
	 */
	public void setScos(ArrayList<SCO> scos) {
		this.scos = scos;
	}

	/**
	 * @return the courseVendor
	 */
	public String getCourseVendor() {
		return courseVendor;
	}

	/**
	 * @param courseVendor the courseVendor to set
	 */
	public void setCourseVendor(String courseVendor) {
		this.courseVendor = courseVendor;
	}

	/**
	 * @return the reportedSCORMVersion
	 */
	public String getReportedSCORMVersion() {
		return reportedSCORMVersion;
	}

	/**
	 * @param reportedSCORMVersion the reportedSCORMVersion to set
	 */
	public void setReportedSCORMVersion(String reportedSCORMVersion) {
		this.reportedSCORMVersion = reportedSCORMVersion;
	}
}

