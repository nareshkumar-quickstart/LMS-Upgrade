package com.softech.vu360.lms.service.impl.lmsapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.LearnerEnrollmentRepository;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.lmsapi.LmsApiCourseService;
import com.softech.vu360.lms.service.lmsapi.LmsApiEnrollmentService;
import com.softech.vu360.lms.service.lmsapi.LmsApiEntitlementService;
import com.softech.vu360.lms.service.lmsapi.LmsApiLearnerService;
import com.softech.vu360.lms.service.lmsapi.response.LmsApiEnrollmentResponseService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiCourseValidationService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiEnrollmentValidationService;
import com.softech.vu360.lms.service.lmsapi.validation.LmsApiLearnerValidationService;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.DuplicatesEnrollment;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerEnrollCourses;
import com.softech.vu360.lms.webservice.message.lmsapi.types.enrollment.LearnerEnrolledCourses;

@Service
public class LmsApiEnrollmentServiceImpl implements LmsApiEnrollmentService {

	private static final Logger log = Logger.getLogger(LmsApiUserServiceImpl.class.getName());
	
	private static final String ERROR_CODE_ONE  = "1";
	
	@Inject
	private LearnerEnrollmentRepository learnerEnrollmentRepository;
	
	@Autowired
	private LmsApiEnrollmentValidationService lmsApiEnrollmentValidationService;
	
	@Autowired
	private CourseAndCourseGroupService courseCourseGrpService;
	
	@Autowired
	private EntitlementService entitlementService;
	
	@Autowired
	private EnrollmentService enrollmentService;
	
	@Autowired
	private LmsApiCourseService lmsApiCourseService;
	
	@Autowired
	private LmsApiCourseValidationService lmsApiCourseValidationService;
	
	@Autowired
	private LmsApiEntitlementService lmsApiEntitlementService;
	
	@Autowired
	private LmsApiLearnerValidationService lmsApiLearnerValidationService;
	
	@Autowired
	private LmsApiLearnerService lmsApiLearnerService;
	
	@Autowired
	private LmsApiEnrollmentResponseService lmsApiEnrollmentResponseService;
	
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	
	public void setLmsApiEnrollmentValidationService(LmsApiEnrollmentValidationService lmsApiEnrollmentValidationService) {
		this.lmsApiEnrollmentValidationService = lmsApiEnrollmentValidationService;
	}
	
	public void setCourseCourseGrpService(CourseAndCourseGroupService courseCourseGrpService) {
		this.courseCourseGrpService = courseCourseGrpService;
	}
	
	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}
	
	public void setLmsApiCourseService(LmsApiCourseService lmsApiCourseService) {
		this.lmsApiCourseService = lmsApiCourseService;
	}
	
	public void setLmsApiCourseValidationService(LmsApiCourseValidationService lmsApiCourseValidationService) {
		this.lmsApiCourseValidationService = lmsApiCourseValidationService;
	}
	
	public void setLmsApiEntitlementService(LmsApiEntitlementService lmsApiEntitlementService) {
		this.lmsApiEntitlementService = lmsApiEntitlementService;
	}
	
	public void setLmsApiLearnerValidationService(LmsApiLearnerValidationService lmsApiLearnerValidationService) {
		this.lmsApiLearnerValidationService = lmsApiLearnerValidationService;
	}
	
	public void setLmsApiLearnerService(LmsApiLearnerService lmsApiLearnerService) {
		this.lmsApiLearnerService = lmsApiLearnerService;
	}
	
	public void setLmsApiEnrollmentResponseService(LmsApiEnrollmentResponseService lmsApiEnrollmentResponseService) {
		this.lmsApiEnrollmentResponseService = lmsApiEnrollmentResponseService;
	}

	@Override
	public List<LearnerEnrolledCourses> enrollLearnerCourses(Map<Boolean, List<LearnerCourses>> learnerCoursesMap, Customer customer, VU360User manager, boolean notifyLearnersByEmail, DuplicatesEnrollment duplicatesEnrollment) {
		
		List<LearnerEnrolledCourses> learnerEnrolledCoursesResponseList = new ArrayList<LearnerEnrolledCourses>();
		List<LearnerCourses> validLearnerCoursesList = lmsApiEnrollmentValidationService.getValidLearnerCourses(learnerCoursesMap);
		Map<LearnerCourses, String> invalidLearnerCourses = lmsApiEnrollmentValidationService.getInvalidLearnerCourses(learnerCoursesMap);
		if (!CollectionUtils.isEmpty(validLearnerCoursesList)) {
			Map<Learner, List<LearnerEnrollment>> learnersNotifyEmailMap = new HashMap<>();
			String customerCode = customer.getCustomerCode();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			GregorianCalendar currentDate = getTodayDate();
			Date todayDate = currentDate.getTime();
			for (LearnerCourses learnerCourses : validLearnerCoursesList) {
				try {
					String userName = learnerCourses.getUserId();
					LearnerEnrollCourses courses = learnerCourses.getCourses();
					List<String> courseGuidList = courses.getCourseId();
					Date enrollmentStartDate = getEnrollmentStartDate(courses, formatter);
					Date enrollmentEndDate = getEnrollmentEndDate(courses, formatter);
					Learner learner = lmsApiLearnerValidationService.getValidLearner(userName, customerCode);
					String[] courseGuidArray = courseGuidList.toArray(new String[courseGuidList.size()]);
					courseCourseGrpService.refreshCoursesCache(courseGuidArray);
					Map<String, Course> coursesMap = lmsApiCourseService.getCoursesMap(courseGuidList);
					List<Course> validCourses = lmsApiCourseValidationService.getValidCourses(customer, coursesMap, todayDate, enrollmentStartDate, enrollmentEndDate);
					Map<String, String> invalidCoursesMap = lmsApiCourseValidationService.getInValidCourses(customer, coursesMap, todayDate, enrollmentStartDate, enrollmentEndDate);
					if (!CollectionUtils.isEmpty(validCourses)) {
						List<LearnerEnrollment> learnerEnrollments = enrollLearnerIntoCourses(customer, learner, validCourses, enrollmentStartDate, enrollmentEndDate, duplicatesEnrollment);
						if (notifyLearnersByEmail) {
							learnersNotifyEmailMap.put(learner, learnerEnrollments);
						}
					
						LearnerEnrolledCourses learnerEnrolledCourses = lmsApiEnrollmentResponseService.getLearnerEnrolledCourses(userName, learnerEnrollments, invalidCoursesMap);
						learnerEnrolledCoursesResponseList.add(learnerEnrolledCourses);	
						
					} else {
						String errorMessage = "No course found for enrollment";
						LearnerEnrolledCourses learnerEnrolledCourses = lmsApiEnrollmentResponseService.getLearnerEnrolledCourses(userName, errorMessage, invalidCoursesMap);
						learnerEnrolledCoursesResponseList.add(learnerEnrolledCourses);
					}
					
				} catch (Exception e) {
					if (invalidLearnerCourses == null) {
						invalidLearnerCourses = new HashMap<>();
					}
					invalidLearnerCourses.put(learnerCourses, e.getMessage());
				}
			} //end of for
			
			if (!CollectionUtils.isEmpty(learnersNotifyEmailMap)) {
				lmsApiLearnerService.sendEmailToLearners(learnersNotifyEmailMap, manager);
			}
			
		}
		
		if (!CollectionUtils.isEmpty(invalidLearnerCourses)) {
			for (Map.Entry<LearnerCourses, String> entry : invalidLearnerCourses.entrySet()) {
				LearnerCourses learnerCourses = entry.getKey();
				String errorMessage = entry.getValue();
				String userName = learnerCourses.getUserId();
				LearnerEnrolledCourses learnerEnrolledCoursesError = lmsApiEnrollmentResponseService.getLearnerEnrolledCourses(userName, ERROR_CODE_ONE, errorMessage);
				learnerEnrolledCoursesResponseList.add(learnerEnrolledCoursesError);
			}
		}
		
		return learnerEnrolledCoursesResponseList;
	}
	
	private List<LearnerEnrollment> enrollLearnerIntoCourses(Customer customer, Learner learner, List<Course> courses, Date enrollmentStartDate, Date enrollmentEndDate, DuplicatesEnrollment duplicatesEnrollment) {
		List<LearnerEnrollment> learnerEnrollments = null;
		if (!CollectionUtils.isEmpty(courses)) {
			learnerEnrollments = new ArrayList<>();
			for (Course course : courses) {
				try {
					LearnerEnrollment learnerEnrollment = enrollLearnerIntoCourse(customer, learner, course, enrollmentStartDate, enrollmentEndDate, duplicatesEnrollment);
					learnerEnrollments.add(learnerEnrollment);
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
		return learnerEnrollments;
	}
	
	private LearnerEnrollment enrollLearnerIntoCourse(Customer customer, Learner learner, Course course, Date enrollmentStartDate, Date enrollmentEndDate, DuplicatesEnrollment duplicatesEnrollment) throws Exception {
		LearnerEnrollment successfulLearnerEnrollment = null;
		List<LearnerEnrollment> duplicateEnrollments = getActiveDuplicateEnrollments(learner.getId(), course.getId(), enrollmentStartDate);
		if (!CollectionUtils.isEmpty(duplicateEnrollments)) {
			switch(duplicatesEnrollment) {
			    // Must use the unqualified name UPDATE, not DuplicatesEnrollment.UPDATE
	            case UPDATE:
	            	// Update existing learner enrollment for same course (Enrollment Start Date, Enrollment End Date, Sync. Class ID)
					for (LearnerEnrollment enrollment : duplicateEnrollments) {
						LearnerEnrollment learnerEnrollment = enrollmentService.loadForUpdateLearnerEnrollment(enrollment.getId());
					    learnerEnrollment.setEnrollmentStartDate(enrollmentStartDate);
					    learnerEnrollment.setEnrollmentEndDate(enrollmentEndDate);
					    enrollmentService.updateEnrollment(learnerEnrollment);
					    successfulLearnerEnrollment = learnerEnrollment;
					}
	            break;
	            case IGNORE:
	            	for (LearnerEnrollment enrollment : duplicateEnrollments) {
	            		successfulLearnerEnrollment = enrollment;
	            	}
	            break;
	            	
			} //end of switch
		} else {
			successfulLearnerEnrollment = addEnrollment(learner, customer, course, enrollmentStartDate, enrollmentEndDate);
		}
		return successfulLearnerEnrollment;
	}
	
	private List<LearnerEnrollment> getActiveDuplicateEnrollments(Long learnerId, Long courseId, Date newEnrollmentStartDate) {

		List<LearnerEnrollment> duplicateEnrollmentList = null;

		List<LearnerEnrollment> enrollmentList = learnerEnrollmentRepository.findByLearnerIdAndCourseIdAndEnrollmentStatus(learnerId, courseId, LearnerEnrollment.ACTIVE);
		if (!CollectionUtils.isEmpty(enrollmentList)) {
			duplicateEnrollmentList = new ArrayList<>();
		    for (LearnerEnrollment enrollment : enrollmentList) {
		    	Date enrollmentEndDate = enrollment.getEnrollmentEndDate();
		    	if (enrollmentEndDate != null && enrollmentEndDate.compareTo(newEnrollmentStartDate) > 0) {
		    		LearnerCourseStatistics courseStatistics = enrollmentService.getLearnerCourseStatisticsBylearnerEnrollment(enrollment);
		    		if (!courseStatistics.isCourseCompleted()) {
		    			duplicateEnrollmentList.add(enrollment);
		    		}
		    	}
		    }
		}
		return duplicateEnrollmentList;
	}
	
	private LearnerEnrollment addEnrollment(Learner learner, Customer customer, Course course, Date enrollmentStartDate, 
			Date enrollmentEndDate) throws Exception{
		
		CustomerEntitlement custEntitlementWithMaximumEntitlementEndDate = lmsApiEntitlementService.getCustEntitlementWithMaximumEntitlementEndDate(customer, course.getId());
		
		// create new enrollment
		log.info("creating new enrollment");
		LearnerEnrollment newEnrollment = new LearnerEnrollment();
		newEnrollment.setCourse(course);
		newEnrollment.setCustomerEntitlement(custEntitlementWithMaximumEntitlementEndDate);
		newEnrollment.setLearner(learner);
		newEnrollment.setEnrollmentStartDate(enrollmentStartDate);
		newEnrollment.setEnrollmentStatus(LearnerEnrollment.ACTIVE);
		newEnrollment.setEnrollmentEndDate(enrollmentEndDate);
		log.debug("calling service to create enrollment");
	
		Long customerEntitlementId = custEntitlementWithMaximumEntitlementEndDate.getId();
		OrgGroupEntitlement orgGroupContract = this.entitlementService.getMaxAvaiableOrgGroupEntitlementByLearner(learner,customerEntitlementId);
		if (orgGroupContract != null && !orgGroupContract.hasAvailableSeats(1)) {
		    orgGroupContract = null;
		}
		LearnerEnrollment enrollment = enrollmentService.addEnrollment(newEnrollment, custEntitlementWithMaximumEntitlementEndDate, orgGroupContract);
		log.debug("creating new enrollment complete");
		return enrollment;
	}
	
	private GregorianCalendar getTodayDate() {
		
		GregorianCalendar todayDate =new GregorianCalendar();
		todayDate.setTime(new Date());
		todayDate.set(GregorianCalendar.HOUR_OF_DAY,0);
		todayDate.set(GregorianCalendar.MINUTE,0);
		todayDate.set(GregorianCalendar.SECOND,0);
		todayDate.set(GregorianCalendar.MILLISECOND,0);
		
		return todayDate;
		
	} //end of getTodayDate()
	
	private Date getEnrollmentStartDate(LearnerEnrollCourses courses, SimpleDateFormat formatter) throws Exception {
		
		String startDate = courses.getEnrollmentStartDate().toString();
		startDate = startDate + " 00:00:00.000";
		Date enrollmentStartDate = formatter.parse(startDate);
		return enrollmentStartDate;
		
	}
	
	private Date getEnrollmentEndDate(LearnerEnrollCourses courses, SimpleDateFormat formatter) throws Exception {
		
		String endDate = courses.getEnrollmentEndDate().toString();
		endDate = endDate + " 23:59:59.000";
		Date enrollmentEndDate = formatter.parse(endDate);
		return enrollmentEndDate;
		
	}
	
}
