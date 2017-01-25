/**
 * 
 */
package com.softech.vu360.lms.service;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialProctors;

/**
 * @author syed.mahmood
 *
 */
public interface ProctorService {

	public List<LearnerEnrollment> getLearnersByProctor(Proctor proctor, String firstName, String lastName, String emailAddress,Date startDate,Date endDate , String courseTitle, String status[]);
	
	public List<LearnerEnrollment> getLearnersByProctor(Proctor proctor, String firstName, String lastName, String emailAddress,Date startDate,Date endDate , String courseTitle);
	
	public ByteArrayOutputStream generateCompletionCertificates(long[] proctorEnrollmentIDs);
	
	public Proctor loadForUpdate(long proctorId);
	
	public Proctor saveProctor(Proctor proctor);

	public List<LearnerEnrollment> getProctorEnrollmentsByIds(Long[] proctorEnrollmentIds);

	public Proctor getProctorByUserId(long vu360UserId);
	
	public List<LearnerEnrollment> getLearnersByCredentialTrainingCourse(Credential credential, String learnerFirstName, String lastName, String email, String companyName, Date startDate, Date endDate, Long[] arrProctorAlreadyExist);
	
	public List<LearnerEnrollment> getLearnersByCredentialTrainingCourse(Long[] credentialIds, String learnerFirstName, String lastName, String email, String companyName, Date startDate, Date endDate, Long[] arrProctorAlreadyExist);
	
	/**
	 * Funtion use to save Proctor
	 * @param credentialProctors
	 * @param credential
	 * @return
	 */
	public boolean saveProctorWithCredential( List<CredentialProctors> credentialProctors, Credential credential);
	
	public Proctor updateProctor(Proctor proctor);
	
	public List<LearnerEnrollment> getEnrollmentsByProctor(Proctor proctor, String firstName, String lastName, String emailAddress,
					Date startDate,Date endDate, String courseTitle, String status[]);
}
