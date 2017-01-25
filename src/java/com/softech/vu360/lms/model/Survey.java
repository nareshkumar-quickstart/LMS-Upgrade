/**
 * 
 */
package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

/**
 * 
 * @author raja.ali
 *
 */
@Entity
@Table(name = "SURVEY")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(
		name = "Survey.getCurrentSurveyListByCoursesForUser", 
		procedureName = "GetCurrentSurveyListByCoursesForUser",
	    resultClasses = Survey.class,
	    parameters = {
		    @StoredProcedureParameter(mode = ParameterMode.IN, name = "LEARNERID", type = Long.class),
		    @StoredProcedureParameter(mode = ParameterMode.IN, name = "OWNERID", type = Long.class) 
		}
	),
	@NamedStoredProcedureQuery(
		name = "Survey.isAlertQueueRequiredProctorApproval", 
		procedureName = "GET_IS_ALERTQUEUE_REQUIRED_PROCTORAPPROVAL",
		parameters = {
		    @StoredProcedureParameter(mode = ParameterMode.IN, name = "tableNameId", type = Long.class),
	        @StoredProcedureParameter(mode = ParameterMode.IN, name = "businessKeys", type = String.class) 
		}
	),
	@NamedStoredProcedureQuery(
	    name = "Survey.SP.GetNonFinishedManualSurveyByLearner", 
		procedureName = "GET_NONFINISHEDMANUALSURVEYBYLEARNER", 
		parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "LEARNERID", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "OWNERID", type = Long.class),
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "VU360USERID", type = Long.class)
		},
		resultClasses=Survey.class
	)
})
public class Survey implements SearchableKey, Comparable<Survey>, Serializable  {
	
	private static final long serialVersionUID = -2141723703982049541L;

	public static final String PUBLISHED = "Published";
	public static final String NOTPUBLISHED = "Unpublished";
	public static final String SURVEY_EVENT_BEFORE_COURSE_START_CODE = "survey.event.before.course";
	public static final String SURVEY_EVENT_MIDDLE_COURSE_CODE = "survey.event.middle.course";
	public static final String SURVEY_EVENT_AFTER_COURSE_ENDS_CODE = "survey.event.after.course";
	public static final String SURVEY_EVENT_MANUAL_CODE = "survey.event.manual";
	public static final String SURVEY_EVENT_CE_PLANNER = "survey.event.ceplanner";
	public enum RETIRE_SURVEY { All, No, Yes;}
	public enum Editable { All, No, Yes;}
	public static final String[] SURVEY_EVENTS = {
			SURVEY_EVENT_MANUAL_CODE,
			SURVEY_EVENT_AFTER_COURSE_ENDS_CODE,
			SURVEY_EVENT_BEFORE_COURSE_START_CODE,
			SURVEY_EVENT_MIDDLE_COURSE_CODE ,
			SURVEY_EVENT_CE_PLANNER };
	
	@Id
	@javax.persistence.TableGenerator(name = "Survey_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SURVEY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "Survey_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Nationalized
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ISLOCKEDTF")
	private Boolean isLocked = Boolean.FALSE;
	
    @ManyToMany
    @JoinTable(name="SURVEY_COURSE", joinColumns = @JoinColumn(name="SURVEY_ID"),inverseJoinColumns = @JoinColumn(name="COURSE_ID"))
    private List<Course> courses = new ArrayList<Course>();
    
    @OneToMany(mappedBy = "survey" )
	private List<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
	
	@Column(name = "EVENT")
	private String event;
	
	@Column(name = "SHOWALLTF")
	private Boolean isShowAll = Boolean.TRUE;
	
	@Column(name = "QUESTIONSPERPAGE")
	private Integer questionsPerPage = 0;
	
	@Embedded
	@AttributeOverrides({
	    @AttributeOverride(name="id", column=@Column(name="OWNER_ID")),
	    @AttributeOverride(name="ownerType", column=@Column(name="OWNER_TYPE"))
	})
    private Owner owner = null;
	
	@Column(name = "REMEMBERPRIORRESPONSESTF")
	private Boolean rememberPriorResponse = false;
	
	@Column(name = "ALLOWANONYMOUSRESPONSETF")
	private Boolean allowAnonymousResponse = false;
	
	@Column(name = "ELECTRONICSIGNATURETF")
	private Boolean electronicSignatureRequire = false;
	
	@Column(name = "ELECTRONICSIGNATURE")
	private String electronicSignature;
	
	@Column(name = "LINKSTF")
	private Boolean linkSelected = false;
	
	@Column(name = "READONLYTF")
	private Boolean readonly = false;
	
	@OneToMany(mappedBy = "survey" )
	private List<SurveySection> sections = new ArrayList<SurveySection>();
	
	@Column(name = "isInspection")
	private Boolean isInspection = false;


	public String getKey() {
		return id.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getIsLocked() {
		if(isLocked==null){
			isLocked=Boolean.FALSE;
		}
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		if(isLocked==null){
			isLocked=Boolean.FALSE;
		}
		this.isLocked = isLocked;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public List<SurveyQuestion> getQuestions() {
		return questions;
	}

	public void setQuestions(List<SurveyQuestion> questions) {
		this.questions = questions;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Boolean getIsShowAll() {
		if(isShowAll==null){
			isShowAll=Boolean.TRUE;
		}
		return isShowAll;
	}

	public void setIsShowAll(Boolean isShowAll) {
		if(isShowAll==null){
			isShowAll=Boolean.TRUE;
		}
		this.isShowAll = isShowAll;
	}

	public Integer getQuestionsPerPage() {
		return questionsPerPage;
	}

	public void setQuestionsPerPage(Integer questionsPerPage) {
		this.questionsPerPage = questionsPerPage;
	}

	public SurveyOwner getOwner() {
		return owner;
	}

	public void setOwner(SurveyOwner owner) {
		this.owner = (Owner)owner;
	}
	
	public Boolean isRememberPriorResponse() {
		if(this.rememberPriorResponse==null)
    		this.rememberPriorResponse= Boolean.FALSE;
		return rememberPriorResponse;
	}

	public void setRememberPriorResponse(Boolean rememberPriorResponse) {
		this.rememberPriorResponse = rememberPriorResponse;
	}

	public Boolean isAllowAnonymousResponse() {
		if(this.allowAnonymousResponse==null)
    		this.allowAnonymousResponse= Boolean.FALSE;
		return allowAnonymousResponse;
	}

	public void setAllowAnonymousResponse(Boolean allowAnonymousResponse) {
		this.allowAnonymousResponse = allowAnonymousResponse;
	}

	public Boolean isElectronicSignatureRequire() {
		if(this.electronicSignatureRequire==null)
    		this.electronicSignatureRequire= Boolean.FALSE;
		return electronicSignatureRequire;
	}

	public void setElectronicSignatureRequire(Boolean electronicSignatureRequire) {
		this.electronicSignatureRequire = electronicSignatureRequire;
	}

	public String getElectronicSignature() {
		return electronicSignature;
	}

	public void setElectronicSignature(String electronicSignature) {
		this.electronicSignature = electronicSignature;
	}

	public Boolean isLinkSelected() {
		if(this.linkSelected==null)
    		this.linkSelected= Boolean.FALSE;
		return linkSelected;
	}

	public void setLinkSelected(Boolean linkSelected) {
		this.linkSelected = linkSelected;
	}

	public Boolean isReadonly() {
		if(this.readonly==null)
    		this.readonly= Boolean.FALSE;
		return readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public Survey getClone(){
		Survey survey = new Survey();
		survey.setAllowAnonymousResponse(allowAnonymousResponse);
		List<Course> coursesNew = new ArrayList<Course>();
		Collections.copy(coursesNew, courses);
		survey.setCourses(coursesNew);
		survey.setElectronicSignature(electronicSignature);
		survey.setElectronicSignatureRequire(electronicSignatureRequire);
		survey.setEvent(event);
		survey.setLinkSelected(linkSelected);
		survey.setIsLocked(isLocked);
		survey.setName(name);
		survey.setQuestionsPerPage(questionsPerPage);
		survey.setReadonly(readonly);
		survey.setRememberPriorResponse(rememberPriorResponse);
		survey.setIsShowAll(isShowAll);
		survey.setStatus(status);
		
		return survey;
	}

	public List<SurveySection> getSections() {
		return sections;
	}

	public void setSections(List<SurveySection> sections) {
		this.sections = sections;
	}

	public Boolean isInspection() {
		if(this.isInspection==null)
    		this.isInspection= Boolean.FALSE;
		return isInspection;
	}

	public void setInspection(Boolean isInspection) {
		this.isInspection = isInspection;
	}

	@Override
	public int compareTo(Survey survey) {
		int comparsionInt = survey.getName().compareTo(this.getName()) ;
        if( comparsionInt == 0 )
        	return comparsionInt ;
        else if( comparsionInt > 0 )
        return -1 ;
        else if( comparsionInt < 0 )
            return 1 ;
        
        return 0;
	}

}
