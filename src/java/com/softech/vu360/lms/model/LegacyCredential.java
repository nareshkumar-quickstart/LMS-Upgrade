package com.softech.vu360.lms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "RPT.COMPLETIONCERTIFICATES_LEGACY")
public class LegacyCredential {
	
	@Id
	@javax.persistence.TableGenerator(name = "COMPLETIONCERTIFICATES_LEGACY_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COMPLETIONCERTIFICATES_LEGACY_ID")
	private Integer id;
	
	@Column(name="Legacy_Student_ID")
	private Integer legacyStudentID;
	
	@Column(name="firstname")
	private String firstName;
	
	@Column(name="mi")
	private String middleName;
	
	@Column(name="lastname")
	private String lastName;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	
	@Column(name="dob")
	private String dob;
	
	@Column(name="ssn")
	private String ssn;
	
	@Column(name="license_num")
	private String licenseNum;
	
	@Column(name="employee_id")
	private String employeeId;
	
	@Column(name="drivers_license")
	private String driversLicense;
	
	@Column(name="license_exp")
	private String licenseExp;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="address")
	private String address;
	
	@Column(name="fulladdress")
	private String fullAddress1;
	
	@Column(name="full_address")
	private String fullAddress2;
	
	@Column(name="address2")
	private String address2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="zip")
	private String zip;
	
	@Column(name="female")
	private String female;
	
	@Column(name="male")
	private String male;
	
	@Column(name="gender")
	private String gender;
	
	@Column(name="city_state")
	private String cityState;
	
	@Column(name="height")
	private String height;
	
	@Column(name="CID")
	private Integer cid;
	
	@Column(name="student_Company")
	private String studentCompany;
	
	@Column(name="company")
	private String company;
	
	@Column(name="comp_address")
	private String compAddress;
	
	@Column(name="comp_city")
	private String compCity;
	
	@Column(name="comp_state")
	private String compState;
	
	@Column(name="comp_zip")
	private String compZip;
	
	@Column(name="LEGACY_ENROLLMENT_ID")
	private Integer legacyEnrollmentId;
	
	@Column(name="course_id")
	private Long courseId;
	
	@Column(name="course_name")
	private String courseName;
	
	@Column(name="course_number")
	private String courseNumber;
	
	@Column(name="reg_date")
	private String regDate;
	
	@Column(name="start_date")
	private String startDate;
	
	@Column(name="comp_date")
	private String compDate;
	
	@Column(name="comp_date2")
	private String compDate2;
	
	@Column(name="comp_date3")
	private String compDate3;
	
	@Column(name="comp_date4")
	private String compDate4;
	
	@Column(name="comp_date5")
	private String compDate5;
	
	@Column(name="comp_date6")
	private String compDate6;
	
	@Column(name="comp_dateUK")
	private String compDateUK;
	
	@Column(name="comp_date3UK")
	private String compDate3UK;
	
	@Column(name="certificate_id")
	private String certificateId;
	
	@Column(name="certificate_num")
	private String certificateNum;
	
	@Column(name="Certificate_Number")
	private String certificateNumber;
	
	@Column(name="course_hours")
	private String courseHours;
	
	@Column(name="course_num")
	private String courseNum;
	
	@Column(name="grade")
	private String grade;
	
	@Column(name="courseSeq")
	private String courseSeq;
	
	@Column(name="court")
	private String court;
	
	@Column(name="ticket_num")
	private String ticketNum;
	
	@Column(name="approval_date_start")
	private String approvalDateStart;
	
	@Column(name="approval_date_end")
	private String approvalDateEnd;
	
	@Column(name="career")
	private String career;
	
	@Column(name="career_title_id")
	private String careerTitleId;
	
	@Column(name="ASV_ID")
	private Integer asvId;
	
	@Column(name="State_ID")
	private Integer stateID;
	
	@Column(name="Regulator_State")
	private String regulatorState;
	
	@Column(name="Vert_ID")
	private Integer vertId;
	
	@Column(name="Vertical")
	private String vertical;
	
	@Column(name="EnrollmentIntegrationDate")
	private Date enrollmentIntegrationDate;
	
	@Column(name="IsExecuted")
	private Boolean isExecuted;
	
	@Column(name="ExecutionDate")
	private Date executionDate;
	
	@Column(name="IsError")
	private Boolean isError;
	
	@Column(name="ErrorDescription")
	private String errorDescription;
	
	@Column(name="LMS_Enrollment_ID")
	private Long lmsEnrollmentId;
	
	@Column(name="Certificate_Template")
	private String certificateTemplate;
	
	@Column(name="EPOCH")
	private String epoch;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLegacyStudentID() {
		return legacyStudentID;
	}
	public void setLegacyStudentID(Integer legacyStudentID) {
		this.legacyStudentID = legacyStudentID;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getLicenseNum() {
		return licenseNum;
	}
	public void setLicenseNum(String licenseNum) {
		this.licenseNum = licenseNum;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getDriversLicense() {
		return driversLicense;
	}
	public void setDriversLicense(String driversLicense) {
		this.driversLicense = driversLicense;
	}
	public String getLicenseExp() {
		return licenseExp;
	}
	public void setLicenseExp(String licenseExp) {
		this.licenseExp = licenseExp;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFullAddress1() {
		return fullAddress1;
	}
	public void setFullAddress1(String fullAddress1) {
		this.fullAddress1 = fullAddress1;
	}
	public String getFullAddress2() {
		return fullAddress2;
	}
	public void setFullAddress2(String fullAddress2) {
		this.fullAddress2 = fullAddress2;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getFemale() {
		return female;
	}
	public void setFemale(String female) {
		this.female = female;
	}
	public String getMale() {
		return male;
	}
	public void setMale(String male) {
		this.male = male;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCityState() {
		return cityState;
	}
	public void setCityState(String cityState) {
		this.cityState = cityState;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cID) {
		cid = cID;
	}
	public String getStudentCompany() {
		return studentCompany;
	}
	public void setStudentCompany(String studentCompany) {
		this.studentCompany = studentCompany;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getCompAddress() {
		return compAddress;
	}
	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
	}
	public String getCompCity() {
		return compCity;
	}
	public void setCompCity(String compCity) {
		this.compCity = compCity;
	}
	public String getCompState() {
		return compState;
	}
	public void setCompState(String compState) {
		this.compState = compState;
	}
	public String getCompZip() {
		return compZip;
	}
	public void setCompZip(String compZip) {
		this.compZip = compZip;
	}
	public Integer getLegacyEnrollmentId() {
		return legacyEnrollmentId;
	}
	public void setLegacyEnrollmentId(Integer legacyEnrollmentId) {
		this.legacyEnrollmentId = legacyEnrollmentId;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getCompDate() {
		return compDate;
	}
	public void setCompDate(String compDate) {
		this.compDate = compDate;
	}
	public String getCompDate2() {
		return compDate2;
	}
	public void setCompDate2(String compDate2) {
		this.compDate2 = compDate2;
	}
	public String getCompDate3() {
		return compDate3;
	}
	public void setCompDate3(String compDate3) {
		this.compDate3 = compDate3;
	}
	public String getCompDate4() {
		return compDate4;
	}
	public void setCompDate4(String compDate4) {
		this.compDate4 = compDate4;
	}
	public String getCompDate5() {
		return compDate5;
	}
	public void setCompDate5(String compDate5) {
		this.compDate5 = compDate5;
	}
	public String getCompDate6() {
		return compDate6;
	}
	public void setCompDate6(String compDate6) {
		this.compDate6 = compDate6;
	}
	public String getCompDateUK() {
		return compDateUK;
	}
	public void setCompDateUK(String compDateUK) {
		this.compDateUK = compDateUK;
	}
	public String getCompDate3UK() {
		return compDate3UK;
	}
	public void setCompDate3UK(String compDate3UK) {
		this.compDate3UK = compDate3UK;
	}
	public String getCertificateId() {
		return certificateId;
	}
	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}
	public String getCertificateNum() {
		return certificateNum;
	}
	public void setCertificateNum(String certificateNum) {
		this.certificateNum = certificateNum;
	}
	public String getCertificateNumber() {
		return certificateNumber;
	}
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	public String getCourseHours() {
		return courseHours;
	}
	public void setCourseHours(String courseHours) {
		this.courseHours = courseHours;
	}
	public String getCourseNum() {
		return courseNum;
	}
	public void setCourseNum(String courseNum) {
		this.courseNum = courseNum;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getCourseSeq() {
		return courseSeq;
	}
	public void setCourseSeq(String courseSeq) {
		this.courseSeq = courseSeq;
	}
	public String getCourt() {
		return court;
	}
	public void setCourt(String court) {
		this.court = court;
	}
	public String getTicketNum() {
		return ticketNum;
	}
	public void setTicketNum(String ticketNum) {
		this.ticketNum = ticketNum;
	}
	public String getApprovalDateStart() {
		return approvalDateStart;
	}
	public void setApprovalDateStart(String approvalDateStart) {
		this.approvalDateStart = approvalDateStart;
	}
	public String getApprovalDateEnd() {
		return approvalDateEnd;
	}
	public void setApprovalDateEnd(String approvalDateEnd) {
		this.approvalDateEnd = approvalDateEnd;
	}
	public String getCareer() {
		return career;
	}
	public void setCareer(String career) {
		this.career = career;
	}
	public String getCareerTitleId() {
		return careerTitleId;
	}
	public void setCareerTitleId(String careerTitleId) {
		this.careerTitleId = careerTitleId;
	}
	public Integer getAsvId() {
		return asvId;
	}
	public void setAsvId(Integer aSV_ID) {
		asvId = aSV_ID;
	}
	public Integer getStateID() {
		return stateID;
	}
	public void setStateID(Integer stateID) {
		this.stateID = stateID;
	}
	public String getRegulatorState() {
		return regulatorState;
	}
	public void setRegulatorState(String regulatorState) {
		this.regulatorState = regulatorState;
	}
	public Integer getVertId() {
		return vertId;
	}
	public void setVertId(Integer vertId) {
		this.vertId = vertId;
	}
	public String getVertical() {
		return vertical;
	}
	public void setVertical(String vertical) {
		this.vertical = vertical;
	}
	public Date getEnrollmentIntegrationDate() {
		return enrollmentIntegrationDate;
	}
	public void setEnrollmentIntegrationDate(Date enrollmentIntegrationDate) {
		this.enrollmentIntegrationDate = enrollmentIntegrationDate;
	}
	public Boolean isExecuted() {
		return isExecuted;
	}
	public void setExecuted(Boolean isExecuted) {
		this.isExecuted = isExecuted;
	}
	public Date getExecutionDate() {
		return executionDate;
	}
	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}
	public Boolean isError() {
		return isError;
	}
	public void setError(Boolean isError) {
		this.isError = isError;
	}
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public Long getLmsEnrollmentId() {
		return lmsEnrollmentId;
	}
	public void setLmsEnrollmentId(Long lmsEnrollmentId) {
		this.lmsEnrollmentId = lmsEnrollmentId;
	}
	public String getCertificateTemplate() {
		return certificateTemplate;
	}
	public void setCertificateTemplate(String certificateTemplate) {
		this.certificateTemplate = certificateTemplate;
	}
	public String getEpoch() {
		return epoch;
	}
	public void setEpoch(String epoch) {
		this.epoch = epoch;
	}
}
