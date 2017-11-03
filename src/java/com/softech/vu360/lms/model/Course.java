package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;
/**
 * 
 * @author haider.ali
 * Modify by muhammad saleem, marium.saud
 * 
 */
@Entity
@Table(name = "COURSE")
@NamedEntityGraph(name="Course.findByCourseStatusGraph", 
    attributeNodes={
		@NamedAttributeNode("id"),
		@NamedAttributeNode("courseGUID"),
		@NamedAttributeNode(value="courseTitle"),
		@NamedAttributeNode("courseStatus"),
		@NamedAttributeNode("name"),
		@NamedAttributeNode("retired"),
		@NamedAttributeNode("courseType"),
		@NamedAttributeNode("bussinesskey"),
//		@NamedAttributeNode(value="coursePlayerType", subgraph="coursePlayerTypeSubGraph"),
//		@NamedAttributeNode(value="AICCAssignableUnit", subgraph="aiccAssignableUnitSubGraph")
    
})
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="COURSETYPE")
public class Course implements Serializable {

	private static final long serialVersionUID = 1L;
    public static final String PUBLISHED = "Published";

    @Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqCourseId")
	@GenericGenerator(name = "seqCourseId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "COURSE") })
    @Column(name = "ID", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "GUID")
    private String courseGUID = null;
    
	@Nationalized
    @Column(name = "NAME")
    private String courseTitle = null;
    @Column(name = "COURSESTATUS")
    private String courseStatus = null;
    @Column(name = "VERSION")
    private String version = null;
    @Column(name = "COURSEID")
    private String courseId = null;
    
	@Nationalized
    @Column(name = "NAME",insertable=false ,updatable=false)
    private String name = null;
	@Transient
	private Location location;
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name = "LANGUAGE_ID")
    private Language language ;
	
	@OneToMany(mappedBy="course", fetch=FetchType.LAZY)
	private List<InstructorCourse> instructorCourses = new ArrayList<InstructorCourse>();
	
    @Column(name = "RETIREDTF")
    private Boolean retired = false;
    @Column(name = "KEYWORDS")
    private String keywords = null;
    @Column(name = "COURSEMEDIATYPE")
    private String courseMediaType = null;
    @Column(name = "CODE")
    private String code = null;
    
	@Nationalized
    @Column(name = "DESCRIPTION")
    private String description = null;
	
	@Nationalized
    @Column(name = "COURSEGUIDE")
    private String courseGuide = null;
	
    @Column(name = "IMAGEOFCOURSE")
    private String imageofCourse = null;
    
	@Nationalized
    @Column(name = "DELIVERYMETHOD")
    private String deliveryMethod = null;
    @Column(name = "DELIVERYMETHOD_ID")
    private Long deliveryMethodId = null;
    
	@Nationalized
    @Column(name = "LEARNINGOBJECTIVES")
    private String learningObjectives = null;
    
	@Nationalized
    @Column(name = "TOPICSCOVERED")
    private String topicsCovered = null;
	
	@Nationalized
    @Column(name = "QUIZINFO")
    private String quizInfo = null;
	
	@Nationalized
    @Column(name = "FINALEXAMINFO")
    private String finalExamInfo = null;
	
	@Nationalized
    @Column(name = "COURSEPRE_REQ")
    private String coursePrereq = null;
	
	@Nationalized
    @Column(name = "ENDOFCOURSEINSTRUCTIONS")
    private String endofCourseInstructions = null;
	
	@Nationalized
    @Column(name = "ADDITIONALMATERIALS")
    private String additionalMaterials = null;
	
	@Nationalized
    @Column(name = "SUBJECTMATTEREXPERTINFO")
    private String subjectMatterExpertInfo = null;
    @Column(name = "MSRP")
    private Double msrp = 0.00;
    
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "CONTENTOWNER_ID")
	private ContentOwner contentOwner ;
	
	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "BRANDING_ID")
    private Brand brand ;
	
	@Column(name = "COURSETYPE" , insertable=false, updatable=false)
    private String courseType = null;
    @Column(name = "PUBLISHEDONVU")
    private Boolean publishOnVU = false;
    @Column(name = "PUBLISHEDONSTOREFRONT")
    private Boolean publishedonstorefront = false;
    @Column(name = "BUSSINESSKEY")
    private String bussinesskey = null;
    @Column(name = "PRODUCTPRICE")
    private Double productprice = 0.00;
    @Column(name = "CURRENCY")
    private String currency = null;
    @Column(name = "APPROVEDCOURSEHOURS")
    private Double approvedcoursehours = 0.00;
    @Column(name = "CEUS")
    private Double ceus = 0.00;
    
	@Nationalized
    @Column(name = "STATE_REGREQ")
    private String stateRegistartionRequired = null;
    @Column(name = "APPROVALNUMBER")
    private String approvalNumber = null;
    
	@Nationalized
    @Column(name = "REGULATINGBODY")
    private String regulatingBody = null;
	
	@Nationalized
    @Column(name = "ASSESMENTS")
    private String assesments = null;
    @Column(name = "TOS")
    private Integer tos = 0;
    @Column(name = "COURSECATEGORY")
    private String courseCategory = null;
    @Column(name = "CREDITHOUR")
    private String credithour = null;
	
    //No reference found in project.
    /*@OneToOne 
    @JoinColumn(name = "ASSET_ID")*/
    @Transient
    private Certificate certificate ;
   
    @Column(name = "RoyaltyPartner")
    private String royaltyPartner = null;
    @Column(name = "RoyaltyType")
    private String royaltyType = null;
    @Column(name = "WholeSalePrice")
    private Double wholeSalePrice = null;
    @Column(name = "RoyaltyAmount")
    private Double royaltyAmount = null;
    @Column(name = "BUSINESSUNIT_ID")
    private Integer  businessUnitId = 0;
    
	@Nationalized
    @Column(name = "BUSINESSUNIT_NAME")
    private String businessUnitName = null;
	
    @OneToOne(fetch=FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "COURSECONFIGURATIONTEMPLATE_ID")
    private CourseConfigurationTemplate courseConfigTemplate = null;
	   
    //Jurisdiction Suggestion.
    @Column(name = "ENABLEDADDITIONALINFORONCOURSEAPPROVAL")
    private Boolean enableCoursApprovalInfo; 
    
	@Nationalized
    @Column(name = "INFORMATIONFORLEARNERREGARDING_REGULATOR")
    private String instructionForLearnerFromRegulator;
    
	@Column(name = "LABTYPE_ID")
	private Long labType_id;
	
    @Transient
    private String demoVideoURL = null;
    
    public Integer getBusinessUnitId() {
		return businessUnitId;
	}
    
    public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}
    
    public String getBusinessUnitName() {
		return businessUnitName;
	}
    
    public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}
    
    public List<InstructorCourse> getInstructorCourses() {
		return instructorCourses;
	}

	public void setInstructorCourses(List<InstructorCourse> instructorCourses) {
		this.instructorCourses = instructorCourses;
	}
  
    public String getCourseGUID() {
        return courseGUID;
    }

    public void setCourseGUID(String courseGUID) {
        this.courseGUID = courseGUID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

    public Boolean isRetired() {
        return retired;
    }

    public void setRetired(Boolean retired) {
        this.retired = retired;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCourseMediaType() {
        return courseMediaType;
    }

    public void setCourseType(String courseType) {
    	this.courseType = courseType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseGuide() {
        return courseGuide;
    }

    public void setCourseGuide(String courseGuide) {
        this.courseGuide = courseGuide;
    }

    public String getImageofCourse() {
        return imageofCourse;
    }

    public void setImageofCourse(String imageofCourse) {
        this.imageofCourse = imageofCourse;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public String getTopicsCovered() {
        return topicsCovered;
    }

    public void setTopicsCovered(String topicsCovered) {
        this.topicsCovered = topicsCovered;
    }

    public String getQuizInfo() {
        return quizInfo;
    }

    public void setQuizInfo(String quizInfo) {
        this.quizInfo = quizInfo;
    }

    public String getFinalExamInfo() {
        return finalExamInfo;
    }

    public void setFinalExamInfo(String finalExamInfo) {
        this.finalExamInfo = finalExamInfo;
    }

    public String getCoursePrereq() {
        return coursePrereq;
    }

    public void setCoursePrereq(String coursePrereq) {
        this.coursePrereq = coursePrereq;
    }

    public String getEndofCourseInstructions() {
        return endofCourseInstructions;
    }

    public void setEndofCourseInstructions(String endofCourseInstructions) {
        this.endofCourseInstructions = endofCourseInstructions;
    }

    public String getAdditionalMaterials() {
        return additionalMaterials;
    }

    public void setAdditionalMaterials(String additionalMaterials) {
        this.additionalMaterials = additionalMaterials;
    }

    public String getSubjectMatterExpertInfo() {
        return subjectMatterExpertInfo;
    }

    public void setSubjectMatterExpertInfo(String subjectMatterExpertInfo) {
        this.subjectMatterExpertInfo = subjectMatterExpertInfo;
    }

    public Double getMsrp() {
        return msrp;
    }

    public void setMsrp(Double msrp) {
        this.msrp = msrp;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getKey() {
        return id.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContentOwner getContentOwner() {
		return contentOwner;
	}

	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

    public  Boolean isPublishOnVU() {
        return publishOnVU;
    }

    public void setPublishOnVU(Boolean publishOnVU) {
        this.publishOnVU = publishOnVU;
    }

    public  Boolean isPublishedonstorefront() {
        return publishedonstorefront;
    }

    public void setPublishedonstorefront(Boolean publishedonstorefront) {
        this.publishedonstorefront = publishedonstorefront;
    }

    public String getBussinesskey() {
        return bussinesskey;
    }

    public void setBussinesskey(String bussinesskey) {
        this.bussinesskey = bussinesskey;
    }

    public Double getProductprice() {
        return productprice;
    }

    public void setProductprice(Double productprice) {
        this.productprice = productprice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getApprovedcoursehours() {
        return approvedcoursehours;
    }

    public void setApprovedcoursehours(Double approvedcoursehours) {
        this.approvedcoursehours = approvedcoursehours;
    }

    public Double getCeus() {
        return ceus;
    }

    public void setCeus(Double ceus) {
        this.ceus = ceus;
    }

    public String getStateRegistartionRequired() {
        return stateRegistartionRequired;
    }

    public void setStateRegistartionRequired(String stateRegistartionRequired) {
        this.stateRegistartionRequired = stateRegistartionRequired;
    }

    public String getApprovalNumber() {
        return approvalNumber;
    }

    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }

    public String getRegulatingBody() {
        return regulatingBody;
    }

    public void setRegulatingBody(String regulatingBody) {
        this.regulatingBody = regulatingBody;
    }

    public String getAssesments() {
        return assesments;
    }

    public void setAssesments(String assesments) {
        this.assesments = assesments;
    }

    public Integer getTos() {
        return tos;
    }

    public void setTos(Integer tos) {
        this.tos = tos;
    }

    public String getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(String courseCategory) {
        this.courseCategory = courseCategory;
    }

    public String getCredithour() {
        return credithour;
    }

    public void setCredithour(String credithour) {
        this.credithour = credithour;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseMediaType(String courseMediaType) {
        this.courseMediaType = courseMediaType;
    }

    public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public  boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Course other = (Course) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getRoyaltyPartner() {
		return royaltyPartner;
	}

	public void setRoyaltyPartner(String royaltyPartner) {
		this.royaltyPartner = royaltyPartner;
	}

	public String getRoyaltyType() {
		return royaltyType;
	}

	public void setRoyaltyType(String royaltyType) {
		this.royaltyType = royaltyType;
	}

	public Double getWholeSalePrice() {
		return wholeSalePrice;
	}

	public void setWholeSalePrice(Double wholeSalePrice) {
		this.wholeSalePrice = wholeSalePrice;
	}

	public Double getRoyaltyAmount() {
		return royaltyAmount;
	}

	public void setRoyaltyAmount(Double royaltyAmount) {
		this.royaltyAmount = royaltyAmount;
	}
	public  Boolean isActive(){
		return (Course.PUBLISHED.equalsIgnoreCase(courseStatus) && !retired);
	}

	public CourseConfigurationTemplate getCourseConfigTemplate() {
		return courseConfigTemplate;
	}

	public void setCourseConfigTemplate(
			CourseConfigurationTemplate courseConfigTemplate) {
		this.courseConfigTemplate = courseConfigTemplate;
	}
	
	public Long getDeliveryMethodId() {
		return deliveryMethodId;
	}

	public void setDeliveryMethodId(Long deliveryMethodId) {
		this.deliveryMethodId = deliveryMethodId;
	}

	public Boolean getEnableCoursApprovalInfo() {
		return enableCoursApprovalInfo;
	}

	public void setEnableCoursApprovalInfo(Boolean enableCoursApprovalInfo) {
		this.enableCoursApprovalInfo = enableCoursApprovalInfo;
	}

	public String getInstructionForLearnerFromRegulator() {
		return instructionForLearnerFromRegulator;
	}

	public void setInstructionForLearnerFromRegulator(
			String instructionForLearnerFromRegulator) {
		this.instructionForLearnerFromRegulator = instructionForLearnerFromRegulator;
	}
    
    public String getDemoVideoURL() {
		return demoVideoURL;
	}

	public void setDemoVideoURL(String demoVideoURL) {
		this.demoVideoURL = demoVideoURL;
	}

	
	public Long getLabType_id() {
		return labType_id;
	}

	public void setLabType_id(Long labType_id) {
		this.labType_id = labType_id;
	}


	public enum Types {
    	
    	Scorm(ScormCourse.COURSE_TYPE), Webinar(WebinarCourse.COURSE_TYPE), Synchronous(SynchronousCourse.COURSE_TYPE), Weblink(WebLinkCourse.COURSE_TYPE), 
    	DiscussionForum(DiscussionForumCourse.COURSE_TYPE), HomeworkAssignment(HomeworkAssignmentCourse.COURSE_TYPE), 
    	InstructorConnect(InstructorConnectCourse.COURSE_TYPE), SelfPaced(SelfPacedCourse.COURSE_TYPE);
    	
    	private String value;
    	
    	private Types(String value) {
    		this.value = value;
    	}
    	
    	public String toString() {
    		return this.value;
    	}
    	
    }
}
