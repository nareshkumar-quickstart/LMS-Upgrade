package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.batchjob.enrollment.LearnerCourseStatisticsBatch;
import com.softech.vu360.lms.batchjob.enrollment.LearnerEnrollmentBatch;
import com.softech.vu360.lms.batchjob.enrollment.LearnerEnrollmentJob;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SynchronousClassService;

/**
 * Refactored enrollment service class that has a new
 * enroll learners method that is optimized for batch operations
 * 
 * @author jason
 *
 */
public class RefactoredEnrollmentServiceImpl {//implements EnrollmentService {

	private static final Logger log = Logger.getLogger(RefactoredEnrollmentServiceImpl.class.getName());
	private EntitlementService entitlementService = null;
	private LearnerService learnerService = null;
    private SynchronousClassService synchronousClassService=null;
    
    /**
     * the only way to enroll learners in courses.  This new version
     * of enrollLearners is optimized to bypass the caching and traditional
     * object graph of a LearnerEnrollment and instead created LearnerEnrollmentBatch
     * objects which only have IDs to facilitate the creation in the database.
     * 
     * @param enrollJob
     */
    public void enrollLearners(LearnerEnrollmentJob enrollJob) {
    	log.info("***new batch enrollment job recieved:"+enrollJob.getId()+"****");
    	try {
    		
    		// if this is a self enrollment request it should not be in a separate
    		// thread as the launch process needs it.
    		if ( !enrollJob.isSelfEnrollmentJob() ) {
    			// TODO:  RefactoredEnrollmentServiceImpl.enrollLearners - implement thread logic here
    		}
    		
	    	// first validate that the job is valid
	    	if ( !enrollJob.isValid() ) {
	    		throw new IllegalArgumentException("illegal arugment - LearnerEnrollmentJob is not valid");
	    	}
	    	
	    	// second check to see if there are enough available seats
	    	boolean enoughSeats = availableSeatsFor(enrollJob);
	    	
	    	if ( enoughSeats ) {
		    	// third actually create the enrollments
	    		enrollJob.setStatus(LearnerEnrollmentJob.STATUS_PROCESSING);
		    	createEnrollmentsFor(enrollJob);
		    	enrollJob.setStatus(LearnerEnrollmentJob.STATUS_COMPLETED_NOERROR);
	    	}
	    	else {
	    		log.error("not enough seats available for learner enrollment job:"+enrollJob.getId());
	    		enrollJob.setStatus(LearnerEnrollmentJob.STATUS_COMPLETED_ERROR);
	    		enrollJob.setErrorCode(LearnerEnrollmentJob.ERROR_NOSEATS);
	    		enrollJob.setErrorInformation("there was not enough available seats in an availabl contract");
	    	}
	    	// fourth perform clean-up and notification
	    	sendNotificationsFor(enrollJob);
    	}
    	catch (Exception ex) {
    		log.error("exception throw during processing of LearnerEnrollmentJob with ID of:"+enrollJob.getId(), ex);
    		enrollJob.setStatus(LearnerEnrollmentJob.STATUS_COMPLETED_ERROR);
    		enrollJob.setErrorCode("unkown-exception");
    		enrollJob.setErrorInformation(ex.getMessage());
    	}
    	
    	// TODO:  RefactoredEnrollmentServiceImpl.enrollLearners - save our work and results
    	//enrollmentDAO.updateEnrollmentJob(enrollJob);
    	log.info("***new batch enrollment job completed:"+enrollJob.getId()+"****");
    }
    
    /**
     * check to make sure that there are enough available seats to 
     * do this enrollment job
     * 
     * @param enrollJob
     * @return
     */
    private boolean availableSeatsFor(LearnerEnrollmentJob enrollJob) {
    	// TODO : implement logic for RefactoredEnrollmentServiceImpl.availableSeatsFor
    	
    	// for each course 
    	
    	   // first check to see if there are any available seats
    	
    	   // if available then decrement by the total number of learners in one step
    	
    	   // call enrollJob.setCustomerEntitlementFor(courseId, customerEntitlementId) 
    	   //     so that later enrollments may correctly be tied
    	return true;
    }
    
    /**
     * this is the meat of the enrollLearners method.  It does all of
     * the setup and actual creation of the LearnerEnrollmentBatch objects.
     * 
     * @param enrollJob
     */
    private void createEnrollmentsFor(LearnerEnrollmentJob enrollJob) {
    	List<LearnerEnrollmentBatch> enrollmentsToAdd = new ArrayList<LearnerEnrollmentBatch>();
    	
    	Date enrollmentDate = new Date(System.currentTimeMillis());
    	LearnerEnrollmentBatch leb;
    	LearnerCourseStatisticsBatch lcsb;
    	for (Long learnerId  : enrollJob.getLearnerIds() ) {
			for (Long courseId : enrollJob.getCourseIds() ) {
				// check for existing enrollments
				if ( learnerHasActiveEnrollment(learnerId, courseId) ) {
					// learner already has an enrollment skip
					continue;
				}
				
				leb = new LearnerEnrollmentBatch();
				leb.setLearnerId(learnerId);
				leb.setCourseId(courseId);
				leb.setEnrollmentDate(enrollmentDate);
				leb.setSynchronousClassId(enrollJob.getSynchronousCourseIdFor(courseId));
				leb.setEnrollmentStartDate(formatStartDate(enrollJob.getEnrollmentStartDateFor(courseId)));
				leb.setEnrollmentEndDate(formatEndDate(enrollJob.getEnrollmentEndDateFor(courseId)));
				leb.setEnrollmentRequiredCompletionDate(formatEndDate(enrollJob.getRequiredCompleationDateFor(courseId)));
				leb.setCustomerEntitlementId(enrollJob.getCustomerEntitlementFor(courseId));
				
				// create the course Statistics object and association
				lcsb = new LearnerCourseStatisticsBatch();
				lcsb.setLearnerEnrollment(leb);
				leb.setCourseStatistics(lcsb);
				
				// add to list to be created
				enrollmentsToAdd.add(leb);
			}
		}
    	
    	// TODO:  RefactoredEnrollmentServiceImpl.createEnrollmentsFor - bulk insert objects into database
    	//this.enrollmentDAO.addAllEnrollments(enrollmentsToAdd);
    	
    	// update enrollmentJob
    	enrollJob.setTotalNumberEnrollmentsCreated(enrollmentsToAdd.size());
    }
    
    /**
     * this method handles all of the communication of the results if required
     * 
     * @param enrollJob
     */
    private void sendNotificationsFor(LearnerEnrollmentJob enrollJob) {
    	// TODO : implement logic for RefactoredEnrollmentServiceImpl.sendNotificationsFor
    }
    
    private boolean learnerHasActiveEnrollment(Long learnerId, Long courseId) {
    	// TODO : implement logic for RefactoredEnrollmentServiceImpl.learnerHasActiveEnrollment
    	return false;
    }
    
    /**
     * we want all enrollments to start at exactly
     * midnight (12:00:00:000) on the date set
     * 
     * @param theDate
     * @return
     */
    private Date formatStartDate(Date theDate) {
    	Calendar cal = new GregorianCalendar();
    	cal.setTime(theDate);
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);
    	Date startDate = new Date(cal.getTimeInMillis());
    	
    	return startDate; 
    }

    /**
     * we want all enrollments to end at exactly
     * 11:59:59:999 PM on the date set
     * 
     * @param theDate
     * @return
     */
    private Date formatEndDate(Date theDate) {
    	Calendar cal = new GregorianCalendar();
    	cal.setTime(theDate);
    	cal.set(Calendar.HOUR_OF_DAY, 23);
    	cal.set(Calendar.MINUTE, 59);
    	cal.set(Calendar.SECOND, 59);
    	cal.set(Calendar.MILLISECOND, 999);
    	Date endDate = new Date(cal.getTimeInMillis());
    	
    	return endDate;     
    }
   
    
	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}
	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	/**
	 * @return the learnerService
	 */
	public LearnerService getLearnerService() {
		return learnerService;
	}
	/**
	 * @param learnerService the learnerService to set
	 */
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	/**
	 * @return the synchronousClassService
	 */
	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}
	/**
	 * @param synchronousClassService the synchronousClassService to set
	 */
	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}
}