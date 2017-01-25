package com.softech.vu360.lms.web.controller.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.learner.MyCoursesController;

/**
 * 
 * 
 * @author jason
 * 
 */
@SuppressWarnings("unchecked")
public class MyCoursesItem extends HashMap<String, Object> implements
		ILMSBaseInterface, Comparable<MyCoursesItem> {

	// this will be serialized in the session...
	private static final long serialVersionUID = -2449128335007281620L;
	private Logger log = Logger.getLogger(MyCoursesController.class.getName());
	private Map internalMap = new HashMap();
	private String courseTitle = "jason";
	private Date lastAccessDate;
	private String courseStatus = StringUtils.EMPTY;
	private String courseStatusDisplayText = StringUtils.EMPTY;
	private String enrollmentStatus = StringUtils.EMPTY;
	private String courseID = StringUtils.EMPTY;
	private String courseType = StringUtils.EMPTY;
	private String learnerEnrollmentID = StringUtils.EMPTY;


	private double completionPercent;
	private String courseCategoryType = StringUtils.EMPTY;
	private String instructorType = StringUtils.EMPTY;
	private Long courseIdKey;
	private double courseCreditHours;
	private double courseDuration;
	private boolean courseCompleted = false;
	private LearnerEnrollment enrollment;
	private String reportingFieldMissing;
	private Integer allowFacebook = 0;
	private Integer viewAssessmentResults = 0;

	public MyCoursesItem(LearnerEnrollment enrollment) {
		internalMap = new HashMap();
		/* The below line is added to avoid lazy initialization while calling 
		-- accreditationService.getCourseConfigurationByTemplateId(learnerEnrollment.getCourse().getCourseConfigTemplate().getId(),true);
		*/
		//enrollment.getCourse().getCourseConfigTemplate(); 
		internalMap.put("instructorType", "WebEx");
		internalMap.put("allowFacebook", 0);
		internalMap.put("viewAssessmentResults", 0);
		internalMap.put("courseTitle", enrollment.getCourse().getCourseTitle());
		internalMap.put("lastAccessDate", enrollment.getCourseStatistics()
				.getLastAccessDate());
		internalMap.put("courseStatus", enrollment.getCourseStatistics()
				.getStatus());
		internalMap.put("courseStatusDisplayText", enrollment
				.getCourseStatistics().getStatusDisplayText());
		internalMap.put("enrollmentStatus", enrollment.getEnrollmentStatus());
		internalMap.put("courseID", enrollment.getCourse().getCourseGUID());
		internalMap.put("courseType", enrollment.getCourse()
				.getCourseMediaType());
		internalMap.put("learnerEnrollmentID", enrollment.getId().toString());
		internalMap.put("completionPercent", enrollment.getCourseStatistics()
				.getPercentComplete());
		internalMap.put("courseCategoryType", enrollment.getCourse()
				.getCourseType());
		internalMap.put("courseIdKey", enrollment.getCourse().getId());
		internalMap.put("courseCreditHours", enrollment.getCourse()
				.getApprovedcoursehours());
		
		if(enrollment.getCourse().getCeus()!=null){
			internalMap.put("courseDuration", enrollment.getCourse().getCeus()); // LMS-12236
		}else{
			internalMap.put("courseDuration", 0.0); 
		}
		internalMap.put("courseCompleted", enrollment.getCourseStatistics()
				.isCourseCompleted());
		internalMap.put("enrollment", enrollment);
		if (enrollment.getSynchronousClass() != null) {
			internalMap.put("synchronousClass",
					enrollment.getSynchronousClass());
			internalMap.put("meetingType", enrollment.getSynchronousClass()
					.getMeetingType());

		}
		this.enrollment = enrollment;
		allowFacebook = 0;
		viewAssessmentResults = 0;
		instructorType = "WebEx";
		lastAccessDate = enrollment.getCourseStatistics().getLastAccessDate();
		courseStatus = enrollment.getCourseStatistics().getStatus();
		courseStatusDisplayText = enrollment.getCourseStatistics()
				.getStatusDisplayText();
		enrollmentStatus = enrollment.getEnrollmentStatus();
		courseID = enrollment.getCourse().getCourseGUID();
		courseType = enrollment.getCourse().getCourseMediaType();
		learnerEnrollmentID = enrollment.getId().toString();
		completionPercent = enrollment.getCourseStatistics()
				.getPercentComplete();
		courseCategoryType = enrollment.getCourse().getCourseType();
		courseIdKey = enrollment.getCourse().getId();
		if(enrollment.getCourse().getApprovedcoursehours()!=null)
			courseCreditHours = enrollment.getCourse().getApprovedcoursehours();
		
		if(enrollment.getCourse().getCeus()!=null){
			courseDuration = enrollment.getCourse().getCeus();
		}
		courseCompleted = enrollment.getCourseStatistics().isCourseCompleted();
		courseTitle = enrollment.getCourse().getCourseTitle();
		
		
	}

	public Date getLastAccessDate() {
		return lastAccessDate;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public String getCourseStatusDisplayText() {
		return courseStatusDisplayText;
	}

	public String getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public String getCourseID() {
		return courseID;
	}

	public String getCourseType() {
		return courseType;
	}

	public double getCompletionPercent() {
		return completionPercent;
	}

	public String getCourseCategoryType() {
		return courseCategoryType;
	}

	public Long getCourseIdKey() {
		return courseIdKey;
	}

	public double getCourseCreditHours() {
		return courseCreditHours;
	}

	public double getCourseDuration() {
		return courseDuration;
	}

	public boolean getCourseCompleted() {
		return courseCompleted;
	}

	/**
	 * @return the courseTitle
	 */
	public String getCourseTitle() {
		return courseTitle;
	}

	/**
	 * @param courseTitle
	 *            the courseTitle to set
	 */
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getReportingFieldMissing() {
		return reportingFieldMissing;
	}

	public void setReportingFieldMissing(String reportingFieldMissing) {
		this.reportingFieldMissing = reportingFieldMissing;
	}

	public String getLearnerEnrollmentID() {
		return learnerEnrollmentID;
	}

	
	public MyCoursesItem(Course course) {
		internalMap = new HashMap();
		internalMap.put("courseTitle", course.getCourseTitle());
		internalMap.put("courseStatus", LearnerCourseStatistics.NOT_STARTED);
		internalMap.put("courseID", course.getCourseGUID());
		internalMap.put("courseLongId", course.getId());
		internalMap.put("courseType", course.getCourseMediaType());
		internalMap.put("courseCategoryType", course.getCourseType());
		courseTitle = course.getCourseTitle();
	}

	public Object put(String key, Object obj) {
		return internalMap.put(key, obj);
	}

	public Object get(String key) {
		return internalMap.get(key);
	}

	public boolean equals(Object obj) {
		boolean flag = false;
		if (obj instanceof MyCoursesItem) {
			MyCoursesItem item = (MyCoursesItem) obj;
			String objCoursId = (String) item.get("courseID");
			String thisCourseId = (String) this.get("courseID");
			String enrollmentId = (String) item.get("learnerEnrollmentID");
			String thisEnrollmentId = (String) this.get("learnerEnrollmentID");

			if (StringUtils.isNotBlank(thisEnrollmentId)) {
				flag = thisCourseId.equalsIgnoreCase(objCoursId)
						&& thisEnrollmentId.equalsIgnoreCase(enrollmentId);
			} else {
				flag = thisCourseId.equalsIgnoreCase(objCoursId);
			}
		}
		return flag;
	}

	public int hashCode() {
		String courseId = (String) this.get("courseID");
		if(courseId!=null)
		return courseId.hashCode();
		else return 0;
	}

	@Override
	public int compareTo(MyCoursesItem o) {
		// TODO Auto-generated method stub
		// log.debug(getCourseTitle()+" -- "+o.getCourseTitle());
		// log.debug(this.getCourseTitle().compareToIgnoreCase(o.getCourseTitle()));
		return this.getCourseTitle().compareToIgnoreCase(o.getCourseTitle());
	}

	public LearnerEnrollment getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(LearnerEnrollment enrollment) {
		this.enrollment = enrollment;
	}

	public Integer getAllowFacebook() {
		return allowFacebook;
	}

	public void setAllowFacebook(Integer allowFacebook) {
		this.allowFacebook = allowFacebook;
	}

	public Integer getViewAssessmentResults() {
		return viewAssessmentResults;
	}

	public void setViewAssessmentResults(Integer viewAssessmentResults) {
		this.viewAssessmentResults = viewAssessmentResults;
	}

}
