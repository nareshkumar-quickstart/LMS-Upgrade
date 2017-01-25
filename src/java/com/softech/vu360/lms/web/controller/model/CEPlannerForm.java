package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.model.survey.Survey;

/**
 * @author Dyutiman
 * created on 17th June 2010
 *
 */
public class CEPlannerForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String companyName = null;
	private String firstName = null;
	private String middleName = null;
	private String lastName = null;
	private String wesiteURL = null;
	private String emailAdd = null;
	private String phone = null;
	private String ext = null;
	private String deleteIndex=null;
	private String address1 = null;
	private String address1a = null;
	private String city1 = null;
	private String state1 = null;
	private String zip1 = null;
	private String country1 = null;
	
	private String address2 = null;
	private String address2a = null;
	private String city2 = null;
	private String state2 = null;
	private String zip2 = null;
	private String country2 = null;
	
	private String username = null;
	private String password = null;
	private String confirmpassword = null;
	
	private String surveyId = null;
	private Survey survey = null;
	
	private String numberOfReps = null;
	private String validateAction="";
	
	private String wizardControl = "0";
	private String action = "";
	private int numberOfLearners = 0;
	private ArrayList<String> firstNames = new ArrayList <String>();
	private ArrayList<String> lastNames = new ArrayList <String>();
	private ArrayList<String> emailAddresses = new ArrayList <String>();
	private ArrayList<String> passwords = new ArrayList <String>();
	private List<Course> courses = new ArrayList<Course>();
	
	//private TakeSurveyForm takeSurveyForm = null;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getWesiteURL() {
		return wesiteURL;
	}
	public void setWesiteURL(String wesiteURL) {
		this.wesiteURL = wesiteURL;
	}
	public String getEmailAdd() {
		return emailAdd;
	}
	public void setEmailAdd(String emailAdd) {
		this.emailAdd = emailAdd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress1a() {
		return address1a;
	}
	public void setAddress1a(String address1a) {
		this.address1a = address1a;
	}
	public String getCity1() {
		return city1;
	}
	public void setCity1(String city1) {
		this.city1 = city1;
	}
	public String getState1() {
		return state1;
	}
	public void setState1(String state1) {
		this.state1 = state1;
	}
	public String getZip1() {
		return zip1;
	}
	public void setZip1(String zip1) {
		this.zip1 = zip1;
	}
	public String getCountry1() {
		return country1;
	}
	public void setCountry1(String country1) {
		this.country1 = country1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress2a() {
		return address2a;
	}
	public void setAddress2a(String address2a) {
		this.address2a = address2a;
	}
	public String getCity2() {
		return city2;
	}
	public void setCity2(String city2) {
		this.city2 = city2;
	}
	public String getState2() {
		return state2;
	}
	public void setState2(String state2) {
		this.state2 = state2;
	}
	public String getZip2() {
		return zip2;
	}
	public void setZip2(String zip2) {
		this.zip2 = zip2;
	}
	public String getCountry2() {
		return country2;
	}
	public void setCountry2(String country2) {
		this.country2 = country2;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmpassword() {
		return confirmpassword;
	}
	public void setConfirmpassword(String confirmpassword) {
		this.confirmpassword = confirmpassword;
	}
	public String getNumberOfReps() {
		return numberOfReps;
	}
	public void setNumberOfReps(String numberOfReps) {
		this.numberOfReps = numberOfReps;
	}
//	public Survey getSurvey() {
//		return survey;
//	}
//	public void setSurvey(Survey survey) {
//		this.survey = survey;
//	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getNumberOfLearners() {
		return numberOfLearners;
	}
	public void setNumberOfLearners(int numberOfLearners) {
		this.numberOfLearners = numberOfLearners;
	}
	public ArrayList<String> getFirstNames() {
		return firstNames;
	}
	public void setFirstNames(ArrayList<String> firstNames) {
		this.firstNames = firstNames;
	}
	public ArrayList<String> getLastNames() {
		return lastNames;
	}
	public void setLastNames(ArrayList<String> lastNames) {
		this.lastNames = lastNames;
	}
	public ArrayList<String> getEmailAddresses() {
		return emailAddresses;
	}
	public void setEmailAddresses(ArrayList<String> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}
	public ArrayList<String> getPasswords() {
		return passwords;
	}
	public void setPasswords(ArrayList<String> passwords) {
		this.passwords = passwords;
	}
//	public String getSurveyId() {
//		return surveyId;
//	}
//	public void setSurveyId(String surveyId) {
//		this.surveyId = surveyId;
//	}
	/**
	 * @return the courses
	 */
	public List<Course> getCourses() {
		return courses;
	}
	/**
	 * @param courses the courses to set
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getValidateAction() {
		return validateAction;
	}
	public void setValidateAction(String validateAction) {
		this.validateAction = validateAction;
	}
	public String getDeleteIndex() {
		return deleteIndex;
	}
	public void setDeleteIndex(String deleteIndex) {
		this.deleteIndex = deleteIndex;
	}
	public String getWizardControl() {
		return wizardControl;
	}
	public void setWizardControl(String wizardControl) {
		this.wizardControl = wizardControl;
	}
//	public TakeSurveyForm getTakeSurveyForm() {
//		return takeSurveyForm;
//	}
//	public void setTakeSurveyForm(TakeSurveyForm takeSurveyForm) {
//		this.takeSurveyForm = takeSurveyForm;
//	}
	
	private Long courseId;
	private Learner learner;
	private VU360User surveyee;

	/**
	 * @return the courseId
	 */
	public Long getCourseId() {
		return courseId;
	}

	/**
	 * @param courseId the courseId to set
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	
	

	
	private String learningSessionId; //For Survey Response View

	/**
	 * @return the learningSessionId
	 */
	public String getLearningSessionId() {
		return learningSessionId;
	}

	/**
	 * @param learningSessionId the learningSessionId to set
	 */
	public void setLearningSessionId(String learningSessionId) {
		this.learningSessionId = learningSessionId;
	}
	
	private List<com.softech.vu360.lms.model.Survey> surveys = new ArrayList<com.softech.vu360.lms.model.Survey>(); //For Survey Response View

	/**
	 * @return the surveys
	 */
	public List<com.softech.vu360.lms.model.Survey> getSurveys() {
		return surveys;
	}

	/**
	 * @param surveys the surveys to set
	 */
	public void setSurveys(List<com.softech.vu360.lms.model.Survey> surveys) {
		this.surveys = surveys;
	}

	/**
	 * @return the learner
	 */
	public Learner getLearner() {
		return learner;
	}

	/**
	 * @param learner the learner to set
	 */
	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public VU360User getSurveyee() {
		return surveyee;
	}

	public void setSurveyee(VU360User surveyee) {
		this.surveyee = surveyee;
	}
	public String getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}
	public Survey getSurvey() {
		return survey;
	}
	public void setSurvey(Survey survey) {
		this.survey = survey;
	}
	

	
	
	
}