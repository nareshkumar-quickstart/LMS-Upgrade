
package com.softech.vu360.lms.webservice.message.storefront.coursecatalog;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CourseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CourseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CourseID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CourseName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ImageofCourse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DemoVideoURL" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ApprovedCourseHours" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="CourseType_Category" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DeliveryMethod" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CEUs" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CourseDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LearningObjectives" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TopicsCovered" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QuizInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FinalExamInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CoursePre-req" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="State_RegReq" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EndofCourseInstructions" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ApprovalNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="AdditionalMaterials" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SubjectMatterExpertInfo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProductPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ProductOfferPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="currency" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TOS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isDemo" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="Audience" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CourseAuthor" type="{http://www.threesixtytraining.com/CourseCatalog.xsd}CourseAuthorType" maxOccurs="unbounded"/>
 *         &lt;element name="CourseClassRoomWebinar" type="{http://www.threesixtytraining.com/CourseCatalog.xsd}ClassRoomWebinarType"/>
 *         &lt;element name="Location" type="{http://www.threesixtytraining.com/CourseCatalog.xsd}CourseLocationType" maxOccurs="unbounded"/>
 *         &lt;element name="CourseContentOwner" type="{http://www.threesixtytraining.com/CourseCatalog.xsd}ContentOwnerType"/>
 *         &lt;element name="CourseGroups" type="{http://www.threesixtytraining.com/CourseCatalog.xsd}GroupType"/>
 *         &lt;element name="CourseBusinessKey" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CourseType", propOrder = {
    "courseID",
    "courseName",
    "imageofCourse",
    "demoVideoURL",
    "approvedCourseHours",
    "courseTypeCategory",
    "deliveryMethod",
    "ceUs",
    "courseDescription",
    "learningObjectives",
    "topicsCovered",
    "quizInfo",
    "finalExamInfo",
    "coursePreReq",
    "stateRegReq",
    "endofCourseInstructions",
    "approvalNumber",
    "additionalMaterials",
    "subjectMatterExpertInfo",
    "productPrice",
    "productOfferPrice",
    "currency",
    "tos",
    "isDemo",
    "audience",
    "courseAuthor",
    "courseClassRoomWebinar",
    "location",
    "courseContentOwner",
    "courseGroups",
    "courseBusinessKey"
})
public class CourseType {

    @XmlElement(name = "CourseID", required = true)
    protected String courseID;
    @XmlElement(name = "CourseName", required = true)
    protected String courseName;
    @XmlElement(name = "ImageofCourse", required = true)
    protected String imageofCourse;
    @XmlElement(name = "DemoVideoURL", required = true)
    protected String demoVideoURL;
    @XmlElement(name = "ApprovedCourseHours")
    protected double approvedCourseHours;
    @XmlElement(name = "CourseType_Category", required = true)
    protected String courseTypeCategory;
    @XmlElement(name = "DeliveryMethod", required = true)
    protected String deliveryMethod;
    @XmlElement(name = "CEUs", required = true)
    protected String ceUs;
    @XmlElement(name = "CourseDescription", required = true)
    protected String courseDescription;
    @XmlElement(name = "LearningObjectives", required = true)
    protected String learningObjectives;
    @XmlElement(name = "TopicsCovered", required = true)
    protected String topicsCovered;
    @XmlElement(name = "QuizInfo", required = true)
    protected String quizInfo;
    @XmlElement(name = "FinalExamInfo", required = true)
    protected String finalExamInfo;
    @XmlElement(name = "CoursePre-req", required = true)
    protected String coursePreReq;
    @XmlElement(name = "State_RegReq", required = true)
    protected String stateRegReq;
    @XmlElement(name = "EndofCourseInstructions", required = true)
    protected String endofCourseInstructions;
    @XmlElement(name = "ApprovalNumber", required = true)
    protected BigInteger approvalNumber;
    @XmlElement(name = "AdditionalMaterials", required = true)
    protected String additionalMaterials;
    @XmlElement(name = "SubjectMatterExpertInfo", required = true)
    protected String subjectMatterExpertInfo;
    @XmlElement(name = "ProductPrice", required = true)
    protected BigDecimal productPrice;
    @XmlElement(name = "ProductOfferPrice", required = true)
    protected BigDecimal productOfferPrice;
    @XmlElement(required = true)
    protected String currency;
    @XmlElement(name = "TOS", required = true)
    protected String tos;
    @XmlElement(required = true)
    protected BigInteger isDemo;
    @XmlElement(name = "Audience", required = true)
    protected String audience;
    @XmlElement(name = "CourseAuthor", required = true)
    protected List<CourseAuthorType> courseAuthor;
    @XmlElement(name = "CourseClassRoomWebinar", required = true)
    protected ClassRoomWebinarType courseClassRoomWebinar;
    @XmlElement(name = "Location", required = true)
    protected List<CourseLocationType> location;
    @XmlElement(name = "CourseContentOwner", required = true)
    protected ContentOwnerType courseContentOwner;
    @XmlElement(name = "CourseGroups", required = true)
    protected GroupType courseGroups;
    @XmlElement(name = "CourseBusinessKey", required = true)
    protected String courseBusinessKey;

    /**
     * Gets the value of the courseID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * Sets the value of the courseID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseID(String value) {
        this.courseID = value;
    }

    /**
     * Gets the value of the courseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the value of the courseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseName(String value) {
        this.courseName = value;
    }

    /**
     * Gets the value of the imageofCourse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImageofCourse() {
        return imageofCourse;
    }

    /**
     * Sets the value of the imageofCourse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImageofCourse(String value) {
        this.imageofCourse = value;
    }

    /**
     * Gets the value of the demoVideoURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDemoVideoURL() {
        return demoVideoURL;
    }

    /**
     * Sets the value of the demoVideoURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDemoVideoURL(String value) {
        this.demoVideoURL = value;
    }

    /**
     * Gets the value of the approvedCourseHours property.
     * 
     */
    public double getApprovedCourseHours() {
        return approvedCourseHours;
    }

    /**
     * Sets the value of the approvedCourseHours property.
     * 
     */
    public void setApprovedCourseHours(double value) {
        this.approvedCourseHours = value;
    }

    /**
     * Gets the value of the courseTypeCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseTypeCategory() {
        return courseTypeCategory;
    }

    /**
     * Sets the value of the courseTypeCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseTypeCategory(String value) {
        this.courseTypeCategory = value;
    }

    /**
     * Gets the value of the deliveryMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    /**
     * Sets the value of the deliveryMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeliveryMethod(String value) {
        this.deliveryMethod = value;
    }

    /**
     * Gets the value of the ceUs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEUs() {
        return ceUs;
    }

    /**
     * Sets the value of the ceUs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEUs(String value) {
        this.ceUs = value;
    }

    /**
     * Gets the value of the courseDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseDescription() {
        return courseDescription;
    }

    /**
     * Sets the value of the courseDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseDescription(String value) {
        this.courseDescription = value;
    }

    /**
     * Gets the value of the learningObjectives property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLearningObjectives() {
        return learningObjectives;
    }

    /**
     * Sets the value of the learningObjectives property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLearningObjectives(String value) {
        this.learningObjectives = value;
    }

    /**
     * Gets the value of the topicsCovered property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopicsCovered() {
        return topicsCovered;
    }

    /**
     * Sets the value of the topicsCovered property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopicsCovered(String value) {
        this.topicsCovered = value;
    }

    /**
     * Gets the value of the quizInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuizInfo() {
        return quizInfo;
    }

    /**
     * Sets the value of the quizInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuizInfo(String value) {
        this.quizInfo = value;
    }

    /**
     * Gets the value of the finalExamInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalExamInfo() {
        return finalExamInfo;
    }

    /**
     * Sets the value of the finalExamInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalExamInfo(String value) {
        this.finalExamInfo = value;
    }

    /**
     * Gets the value of the coursePreReq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoursePreReq() {
        return coursePreReq;
    }

    /**
     * Sets the value of the coursePreReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoursePreReq(String value) {
        this.coursePreReq = value;
    }

    /**
     * Gets the value of the stateRegReq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateRegReq() {
        return stateRegReq;
    }

    /**
     * Sets the value of the stateRegReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateRegReq(String value) {
        this.stateRegReq = value;
    }

    /**
     * Gets the value of the endofCourseInstructions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndofCourseInstructions() {
        return endofCourseInstructions;
    }

    /**
     * Sets the value of the endofCourseInstructions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndofCourseInstructions(String value) {
        this.endofCourseInstructions = value;
    }

    /**
     * Gets the value of the approvalNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getApprovalNumber() {
        return approvalNumber;
    }

    /**
     * Sets the value of the approvalNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setApprovalNumber(BigInteger value) {
        this.approvalNumber = value;
    }

    /**
     * Gets the value of the additionalMaterials property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalMaterials() {
        return additionalMaterials;
    }

    /**
     * Sets the value of the additionalMaterials property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalMaterials(String value) {
        this.additionalMaterials = value;
    }

    /**
     * Gets the value of the subjectMatterExpertInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubjectMatterExpertInfo() {
        return subjectMatterExpertInfo;
    }

    /**
     * Sets the value of the subjectMatterExpertInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubjectMatterExpertInfo(String value) {
        this.subjectMatterExpertInfo = value;
    }

    /**
     * Gets the value of the productPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getProductPrice() {
        return productPrice;
    }

    /**
     * Sets the value of the productPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setProductPrice(BigDecimal value) {
        this.productPrice = value;
    }

    /**
     * Gets the value of the productOfferPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getProductOfferPrice() {
        return productOfferPrice;
    }

    /**
     * Sets the value of the productOfferPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setProductOfferPrice(BigDecimal value) {
        this.productOfferPrice = value;
    }

    /**
     * Gets the value of the currency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the value of the currency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrency(String value) {
        this.currency = value;
    }

    /**
     * Gets the value of the tos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTOS() {
        return tos;
    }

    /**
     * Sets the value of the tos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTOS(String value) {
        this.tos = value;
    }

    /**
     * Gets the value of the isDemo property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIsDemo() {
        return isDemo;
    }

    /**
     * Sets the value of the isDemo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIsDemo(BigInteger value) {
        this.isDemo = value;
    }

    /**
     * Gets the value of the audience property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAudience() {
        return audience;
    }

    /**
     * Sets the value of the audience property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAudience(String value) {
        this.audience = value;
    }

    /**
     * Gets the value of the courseAuthor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the courseAuthor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCourseAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseAuthorType }
     * 
     * 
     */
    public List<CourseAuthorType> getCourseAuthor() {
        if (courseAuthor == null) {
            courseAuthor = new ArrayList<CourseAuthorType>();
        }
        return this.courseAuthor;
    }

    /**
     * Gets the value of the courseClassRoomWebinar property.
     * 
     * @return
     *     possible object is
     *     {@link ClassRoomWebinarType }
     *     
     */
    public ClassRoomWebinarType getCourseClassRoomWebinar() {
        return courseClassRoomWebinar;
    }

    /**
     * Sets the value of the courseClassRoomWebinar property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClassRoomWebinarType }
     *     
     */
    public void setCourseClassRoomWebinar(ClassRoomWebinarType value) {
        this.courseClassRoomWebinar = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the location property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourseLocationType }
     * 
     * 
     */
    public List<CourseLocationType> getLocation() {
        if (location == null) {
            location = new ArrayList<CourseLocationType>();
        }
        return this.location;
    }

    /**
     * Gets the value of the courseContentOwner property.
     * 
     * @return
     *     possible object is
     *     {@link ContentOwnerType }
     *     
     */
    public ContentOwnerType getCourseContentOwner() {
        return courseContentOwner;
    }

    /**
     * Sets the value of the courseContentOwner property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentOwnerType }
     *     
     */
    public void setCourseContentOwner(ContentOwnerType value) {
        this.courseContentOwner = value;
    }

    /**
     * Gets the value of the courseGroups property.
     * 
     * @return
     *     possible object is
     *     {@link GroupType }
     *     
     */
    public GroupType getCourseGroups() {
        return courseGroups;
    }

    /**
     * Sets the value of the courseGroups property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupType }
     *     
     */
    public void setCourseGroups(GroupType value) {
        this.courseGroups = value;
    }

    /**
     * Gets the value of the courseBusinessKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourseBusinessKey() {
        return courseBusinessKey;
    }

    /**
     * Sets the value of the courseBusinessKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourseBusinessKey(String value) {
        this.courseBusinessKey = value;
    }

}
