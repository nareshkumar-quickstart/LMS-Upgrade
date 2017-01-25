package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author haider.ali
 * 
 */
@Entity
@Table(name = "LEARNINGSESSION")
public class LearningSession implements SearchableKey {

	private static final long serialVersionUID = -8411988685196455948L;
	public static final String SOURCE_VU360_LMS = "VU360-LMS";
	public static final String SOURCE_AICC = "AICC";
	public static final int LMS_LS360 = 0;
	public static final int LMS_PLATEAU = 1;

    @Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqLearningSessionId")
	@GenericGenerator(name = "seqLearningSessionId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "LEARNINGSESSION") })
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "COURSECODE")
	private String courseId = null;
    @Column(name = "ENROLLMENT_ID")
	private Long enrollmentId;
    @Column(name = "LEARNER_ID")
	private Long learnerId;
    @Column(name = "LEARNINGSESSIONGUID")
	private String learningSessionID = null;
    @Column(name = "EXTERNALLMSSESSIONID")
	private String externalLMSSessionID = null;
    @Column(name = "EXTERNALLMSURL")
	private String externalLMSUrl = null;
    @Column(name = "UNIQUEUSERGUID")
	private String uniqueUserGUID = null;
    @Column(name = "STARTTIME")
	private Date sessionStartDateTime = null;
    @Column(name = "ENDTIME")
	private Date sessionEndDateTime = null;
    @Column(name = "REDIRECTURL")
	private String redirectURI = null; 
    @Column(name = "BRANDNAME")
	private String brandName = null; 
    @Column(name = "COURSEAPPROVALID")
	private Long courseApprovalId;
    
	@OneToOne (fetch=FetchType.LAZY)
    @JoinColumn(name="LANGUAGE_ID")
	private Language language = null;
	
    @Column(name = "source")
	private String source = SOURCE_VU360_LMS;
    @Column(name = "LMSPROVIDER")
	private Integer lmsProvider = LMS_LS360;

	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public Long getEnrollmentId() {
		if(this.enrollmentId==null){
			this.enrollmentId=0L;
		}
		
		return enrollmentId;
	}

	public void setEnrollmentId(Long enrollmentId) {
		if(enrollmentId==null){
			this.enrollmentId=0L;
		}
		else{
			this.enrollmentId = enrollmentId;
		}
	}

	public Long getLearnerId() {
		return learnerId;
	}

	public void setLearnerId(Long learnerId) {
		this.learnerId = learnerId;
	}

	public String getLearningSessionID() {
		return learningSessionID;
	}

	public void setLearningSessionID(String learningSessionID) {
		this.learningSessionID = learningSessionID;
	}

	public String getExternalLMSSessionID() {
		return externalLMSSessionID;
	}

	public void setExternalLMSSessionID(String externalLMSSessionID) {
		this.externalLMSSessionID = externalLMSSessionID;
	}

	public String getExternalLMSUrl() {
		return externalLMSUrl;
	}

	public void setExternalLMSUrl(String externalLMSUrl) {
		this.externalLMSUrl = externalLMSUrl;
	}

	public String getUniqueUserGUID() {
		return uniqueUserGUID;
	}

	public void setUniqueUserGUID(String uniqueUserGUID) {
		this.uniqueUserGUID = uniqueUserGUID;
	}

	public Date getSessionStartDateTime() {
		return sessionStartDateTime;
	}

	public void setSessionStartDateTime(Date sessionStartDateTime) {
		this.sessionStartDateTime = sessionStartDateTime;
	}

	public Date getSessionEndDateTime() {
		return sessionEndDateTime;
	}

	public void setSessionEndDateTime(Date sessionEndDateTime) {
		this.sessionEndDateTime = sessionEndDateTime;
	}

	public String getRedirectURI() {
		return redirectURI;
	}

	public void setRedirectURI(String redirectURI) {
		this.redirectURI = redirectURI;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Long getCourseApprovalId() {
		return courseApprovalId;
	}

	public void setCourseApprovalId(Long courseApprovalId) {
		this.courseApprovalId = courseApprovalId;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getLmsProvider() {
		return lmsProvider;
	}

	public void setLmsProvider(Integer lmsProvider) {
		this.lmsProvider = lmsProvider;
	}

	public static String getSourceVu360Lms() {
		return SOURCE_VU360_LMS;
	}

	public static String getSourceAicc() {
		return SOURCE_AICC;
	}

	
	
	
}
