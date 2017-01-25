package com.softech.vu360.lms.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.softech.vu360.lms.model.LegacyCredential;

public class LegacyCredentialExractor implements ResultSetExtractor {

	@Override
	public LegacyCredential extractData(ResultSet rs) throws SQLException,
			DataAccessException {
		// TODO Auto-generated method stub
		LegacyCredential credential = new LegacyCredential();
		if(rs.next()){
			credential.setId(rs.getInt("ID"));
			credential.setLegacyStudentID(rs.getInt("Legacy_Student_ID"));
			credential.setFirstName(rs.getString("firstname"));
			credential.setMiddleName(rs.getString("mi"));
			credential.setLastName(rs.getString("lastname"));
			credential.setName(rs.getString("name"));
			credential.setEmail(rs.getString("email"));
			credential.setDob(rs.getString("decryptedDOB"));
			credential.setSsn(rs.getString("decryptedSSN"));
			credential.setLicenseNum(rs.getString("decryptedLicenseNum"));
			credential.setEmployeeId(rs.getString("employee_id"));
			credential.setDriversLicense(rs.getString("decryptedDriversLicense"));
			credential.setLicenseExp(rs.getString("license_exp"));
			credential.setPhone(rs.getString("phone"));
			credential.setAddress(rs.getString("address"));
			credential.setFullAddress1(rs.getString("fulladdress"));
			credential.setFullAddress2(rs.getString("full_address"));
			credential.setAddress2(rs.getString("address2"));
			credential.setCity(rs.getString("city"));
			credential.setState(rs.getString("state"));
			credential.setZip(rs.getString("zip"));
			credential.setFemale(rs.getString("female"));
			credential.setMale(rs.getString("male"));
			credential.setGender(rs.getString("gender"));
			credential.setCityState(rs.getString("city_state"));
			credential.setHeight(rs.getString("height"));
			credential.setCid(rs.getInt("CID"));
			credential.setStudentCompany(rs.getString("student_Company"));
			credential.setCompany(rs.getString("company"));
			credential.setCompAddress(rs.getString("comp_address"));
			credential.setCompCity(rs.getString("comp_city"));
			credential.setCompState(rs.getString("comp_state"));
			credential.setCompZip(rs.getString("comp_zip"));
			credential.setLegacyEnrollmentId(rs.getInt("LEGACY_ENROLLMENT_ID"));
			credential.setCourseId(rs.getLong("course_id"));
			credential.setCourseName(rs.getString("course_name"));
			credential.setCourseNumber(rs.getString("course_number"));
			credential.setRegDate(rs.getString("reg_date"));
			credential.setStartDate(rs.getString("start_date"));
			credential.setCompDate(rs.getString("comp_date"));
			credential.setCompDate2(rs.getString("comp_date2"));
			credential.setCompDate3(rs.getString("comp_date3"));
			credential.setCompDate4(rs.getString("comp_date4"));
			credential.setCompDate5(rs.getString("comp_date5"));
			credential.setCompDate6(rs.getString("comp_date6"));
			credential.setCompDateUK(rs.getString("comp_dateUK"));
			credential.setCompDate3UK(rs.getString("comp_date3UK"));
			credential.setCertificateId(rs.getString("certificate_id"));
			credential.setCertificateNum(rs.getString("certificate_num"));
			credential.setCertificateNumber(rs.getString("Certificate_Number"));
			credential.setCourseHours(rs.getString("course_hours"));
			credential.setCourseNum(rs.getString("course_num"));
			credential.setGrade(rs.getString("grade"));
			credential.setCourseSeq(rs.getString("courseSeq"));
			credential.setCourt(rs.getString("court"));
			credential.setTicketNum(rs.getString("ticket_num"));
			credential.setApprovalDateStart(rs.getString("approval_date_start"));
			credential.setApprovalDateEnd(rs.getString("approval_date_end"));
			credential.setCareer(rs.getString("career"));
			credential.setCareerTitleId(rs.getString("career_title_id"));
			credential.setAsvId(rs.getInt("ASV_ID"));
			credential.setStateID(rs.getInt("State_ID"));
			credential.setRegulatorState(rs.getString("Regulator_State"));
			credential.setVertId(rs.getInt("Vert_ID"));
			credential.setVertical(rs.getString("Vertical"));
			credential.setEnrollmentIntegrationDate(rs.getDate("EnrollmentIntegrationDate"));
			credential.setExecuted(rs.getBoolean("IsExecuted"));
			credential.setExecutionDate(rs.getDate("ExecutionDate"));
			credential.setError(rs.getBoolean("IsError"));
			credential.setErrorDescription(rs.getString("ErrorDescription"));
			credential.setLmsEnrollmentId(rs.getLong("LMS_Enrollment_ID"));
			credential.setCertificateTemplate(rs.getString("Certificate_Template"));
		}
		return credential;
	}

}
