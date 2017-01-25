package com.softech.vu360.lms.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.HomeworkAssignmentCourse;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.LearnerHomeworkAssignmentSubmissionService;
import com.softech.vu360.lms.web.controller.helper.SortPagingAndSearch;
import com.softech.vu360.lms.web.controller.model.instructor.HomeworkAssignmentLearner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LearnerEnrollmentRepositoryTest {
	

	
	
	@Inject
	private LearnerEnrollmentRepository leanrEnrollmentRepository;
	
	@Inject
	private InstructorSynchronousClassRepository instructorSynchronousClassRepository;
	
	@Inject
	private CustomerEntitlementRepository customerEntitlementRepository;
	
	@Inject
	private EnrollmentService enrollmentService = null;
	
	@Inject
    private LearnerHomeworkAssignmentSubmissionService homeworkAssignmentService = null;
    
	@Inject
	private ProctorRepository proctorRepository;
	//@Test
	public void getHomeworkAssignmentLearnersByInstructor(){
		
		//Pageable pageRequest = createPageRequest();
		//RepositorySpecificationsBuilder<LearnerEnrollment> specificationBuilder = new RepositorySpecificationsBuilder<LearnerEnrollment>();
		//specificationBuilder.with("course_courseType", "::IC", HomeworkAssignmentCourse.COURSE_TYPE, "AND");
		//specificationBuilder.with("course_instructorCourses_instructor_id", "::",5954L, "AND");
		
		//List<LearnerEnrollment> lst2;
		
		///Page<LearnerEnrollment> lst = leanrEnrollmentRepository.findAll(specificationBuilder.build(), pageRequest);
		//lst2 = lst.getContent();
		
		
		SortPagingAndSearch sps = new SortPagingAndSearch();
		sps.setDoSearchData(false);
		sps.setPageIndex(0);
		sps.setPageSize(10);
		sps.setShowAll("false");
		sps.setSortColumnIndex("0");
		sps.setSortColumnName("learner.vu360User.firstName");
		sps.setSortDirection("desc");
		sps.setTotalRecordCount(0);
		sps.setValue("firstName", "learner abc 5");
		sps.setValue("lastName", "learner abc 5");
		sps.setValue("courseName", "LEARNER ASSIGNMENT 2");
		sps.setValue("status", "Pending");
		
		Long instrutorId = 5954L;
		List<HomeworkAssignmentLearner> lst3 = homeworkAssignmentService.getHomeworkAssignmentLearnersByInstructor(instrutorId, sps);
		System.out.print(lst3.get(0).getId());
	}
		
	private Pageable createPageRequest() {
	    return new PageRequest(0, 10);
	}
	
	//@Test
	public void findByIdAndCourseIdAndEnrollmentStatus(){
		
		long learnerId=new Long(500);
		long courseId=new Long(59);		
		List<LearnerEnrollment> learners = leanrEnrollmentRepository.findByLearnerIdAndCourseIdAndEnrollmentStatus(learnerId, courseId, LearnerEnrollment.ACTIVE);
		LearnerEnrollment obj = learners.get(0);
		System.out.print(obj.getId());
	}
	
	//@Test
	public void  findByCourseIdAndEnrollmentStatus(){
		
		long courseId=new Long(59);		
		List<LearnerEnrollment> learners = leanrEnrollmentRepository.findByCourseIdAndEnrollmentStatus(courseId, LearnerEnrollment.ACTIVE);
		LearnerEnrollment obj = learners.get(0);
		System.out.print(learners.size());
	}
	
	//@Test
	public void findBySynchronousClassIdInAndEnrollmentDateBetween(){
		
		List<InstructorSynchronousClass> synchronousClasses = instructorSynchronousClassRepository.findByInstructorIdAndSynchronousClassCourseRetired(new Long(653), false);
		List<Long> synchIds = new ArrayList();
		for(InstructorSynchronousClass obj : synchronousClasses){
			System.out.println("*********"+obj.getId());
			synchIds.add(obj.getSynchronousClass().getId());
		}
		
		
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf =new java.text.SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); 
		// roll down the month
		
		c1.set(2010, 1, 31);
		//c1.setTime(new Date());  // 1999 jan 20
		//c1.add(, amount);(Calendar.DATE, +1);
		Calendar c2 = Calendar.getInstance(); 
		//c2.add(Calendar.MONTH, -11); // substract 12 month
		c2.set(2010, 10, 31);
		
		System.out.println("************"+leanrEnrollmentRepository.findBySynchronousClassIdInAndEnrollmentDateBetween(synchIds, c2.getTime(), c1.getTime()).get(0).getId());
		
		
		
		
	}
	
	//@Test
	public void findBySynchronousClassIdInAndCourseStatisticsCompletionDateBetween(){
		
		List<InstructorSynchronousClass> synchronousClasses = instructorSynchronousClassRepository.findByInstructorIdAndSynchronousClassCourseRetired(new Long(653), false);
		List<Long> synchIds = new ArrayList();
		for(InstructorSynchronousClass obj : synchronousClasses){
			System.out.println("*********"+obj.getId());
			synchIds.add(obj.getSynchronousClass().getId());
		}
		
		
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf =new java.text.SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); 
		// roll down the month
		
		c1.set(2010, Calendar.JANUARY, 15);
		//c1.setTime(new Date());  // 1999 jan 20
		//c1.add(, amount);(Calendar.DATE, +1);
		Calendar c2 = Calendar.getInstance(); 
		//c2.add(Calendar.MONTH, -11); // substract 12 month
		c2.set(2010, 10, 31);
		Long id = leanrEnrollmentRepository.findBySynchronousClassIdInAndCourseStatisticsCompletionDateBetween(synchIds, c1.getTime(), c2.getTime()).get(0).getId();
		System.out.println("************"+id);
		
		
		
		
	}
	
	//@Test
	public void findSynchronousClassIdsInandfirstNameandLastNameandEmailAddress(){
		
		List<InstructorSynchronousClass> objs = instructorSynchronousClassRepository.findByInstructorId(new Long(3252));
		
		List<Long> synchronousClasses = new ArrayList();
		for(InstructorSynchronousClass obj : objs){
			synchronousClasses.add(obj.getSynchronousClass().getId());
			System.out.println("************** ID = "+obj.getSynchronousClass().getId());
		}
		
		try{
		
					
			System.out.print("------------------- ID ="+leanrEnrollmentRepository.findSynchronousClassIdsInandfirstNameandLastNameandEmailAddress(synchronousClasses,"3603","train","ramiz.uddin@360training.com").get(0).getId());
		}catch(Exception e){
			e.printStackTrace();
		}
	
		
		
	}
	
	
//	@Test
	public void findSynchronousClassIdInandfirstNameandLastNameandEmailAddress(){
		
		long id = 6204;
		
		try{
		
			List<LearnerEnrollment> lst = leanrEnrollmentRepository.findSynchronousClassIdInandfirstNameandLastNameandEmailAddress(id,"OWS Customer","OWS Customer","owsCust@ows.com");
			for(LearnerEnrollment obj : lst){
				System.out.print("------------------- ID ="+obj.getId());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
		
		
	}
	
	//@Test
	public void findById(){
		System.out.print("**************** "+leanrEnrollmentRepository.findOne(51L).getLearner().getId());
	}
	
	//@Test
	public void findByLearnerId(){
		List<LearnerEnrollment> lst = leanrEnrollmentRepository.findByLearnerId(105L);
		
		for(LearnerEnrollment obj : lst){
			
			System.out.print("**************** "+obj.getId());
			
		}
		
		
	}
	
	//@Test
	public void findByCustomerEntitlementId(){
		List<LearnerEnrollment> lst = leanrEnrollmentRepository.findByCustomerEntitlementId(51L);
		
		for(LearnerEnrollment obj : lst){
			
			System.out.print("**************** "+obj.getId());
			
		}
	}
	
	
	//@Test
	public void findBylearnerIdAndCourseId(){
		
		List<LearnerEnrollment> lst = leanrEnrollmentRepository.findByLearnerIdAndCourseId(105L, 2L);
		
		for(LearnerEnrollment obj : lst){
			
			System.out.print("**************** "+obj.getId());
			
		}
	}

	//@Test
	public void findbyLearnerIdandEnrollmentStatusandEnrollmentEndDateandCourseTitleorDescorKeywords(){
		
		try{
			String search=null;
			List<String> enrollmentStatus = new ArrayList();
			enrollmentStatus.add(LearnerEnrollment.EXPIRED);
			enrollmentStatus.add(LearnerEnrollment.ACTIVE);
			
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	
			java.util.Date enrollmentEndDate =null; 
			java.util.Date endRange = null;
			String startDate="04/08/2009";
			enrollmentEndDate=format.parse(startDate);
			List<LearnerEnrollment> lst= new ArrayList();
			if(!StringUtils.isBlank(search) && !search.equals("Search")){
				lst = leanrEnrollmentRepository.findbyLearnerIdandEnrollmentStatusandEnrollmentEndDateandCourseTitleorDescorKeywords(107L, enrollmentStatus, enrollmentEndDate, search);
			}else
				lst = leanrEnrollmentRepository.findbyLearnerIdandEnrollmentStatusandEnrollmentEndDate(107L, enrollmentStatus, enrollmentEndDate);
			
			for(LearnerEnrollment obj : lst){
				System.out.println("******************* ID = "+obj.getId());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		 
		
		
	}
	
	@Test
	public void findbyLearnerIdandEnrollmentStatusandCourseTitleorDescorKeywords(){
		
		try{
			String search="profess";
//			List<String> enrollmentStatus = new ArrayList();
//			enrollmentStatus.add(LearnerEnrollment.EXPIRED);
//			enrollmentStatus.add(LearnerEnrollment.ACTIVE);
			
			List<LearnerEnrollment> lst= new ArrayList();
			if(!StringUtils.isBlank(search) && !search.equals("Search")){
				lst = leanrEnrollmentRepository.findbyLearnerIdandEnrollmentStatusandCourseTitleorDescorKeywords(107L, LearnerEnrollment.EXPIRED, LearnerEnrollment.ACTIVE, search);
			}else
				lst = leanrEnrollmentRepository.findbyLearnerIdandEnrollmentStatus(107L, LearnerEnrollment.EXPIRED, LearnerEnrollment.ACTIVE);
			
			for(LearnerEnrollment obj : lst){
				System.out.println("******************* ID = "+obj.getId());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//@Test
	public void findbyLearnerIdandCourseStatisticsStatusandEnrollmentStatusandEnrollmentEndDateandCourseTitleorDescorKeywords(){
		
		try{
			String search="";
			List<String> enrollmentStatus = new ArrayList();
			enrollmentStatus.add(LearnerEnrollment.EXPIRED);
			enrollmentStatus.add(LearnerEnrollment.ACTIVE);
			String statsStatus = LearnerCourseStatistics.REPORTING_PENDING;
			
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	
			java.util.Date enrollmentEndDate =null; 
			java.util.Date endRange = null;
			String startDate="04/08/2010";
			enrollmentEndDate=format.parse(startDate);
			List<LearnerEnrollment> lst= new ArrayList();
			if(!StringUtils.isBlank(search) && !search.equals("Search")){
				lst = leanrEnrollmentRepository.findbyLearnerIdandCourseStatisticsStatusandEnrollmentStatusandEnrollmentEndDateandCourseTitleorDescorKeywords(705L,statsStatus, LearnerEnrollment.EXPIRED, LearnerEnrollment.ACTIVE,enrollmentEndDate, search);
			}else
				lst = leanrEnrollmentRepository.findbyLearnerIdandCourseStatisticsStatusandEnrollmentStatusandEnrollmentEndDate(705L, statsStatus,LearnerEnrollment.EXPIRED, LearnerEnrollment.ACTIVE, enrollmentEndDate);
			
			for(LearnerEnrollment obj : lst){
				System.out.println("******************* ID = "+obj.getId());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	//@Test
	public void findByCustomerEntitlementByIdandSave(){
		
		List<LearnerEnrollment> lst = leanrEnrollmentRepository.findByCustomerEntitlementId(1228L);
		CustomerEntitlement customerEntitlement = new CustomerEntitlement();
		customerEntitlement = customerEntitlementRepository.findOne(916L);
		for(LearnerEnrollment obj : lst){
			
			obj.setCustomerEntitlement(customerEntitlement);
			obj.setOrgGroupEntitlement( null );
		}
		
		leanrEnrollmentRepository.save(lst);
	}
	
	
	/*
	 * ToplinkEnrollmentDAO Method:
	 * public List<LearnerEnrollment> getLearerEnrollmentsByCourse(Long courseId);
	 */
	//@Test
	public void LearnerEnrollments_findByIdAndCourseIdAndEnrollmentStatus(){
		
		Long courseId=new Long(59);		
		List<LearnerEnrollment> enrollments = leanrEnrollmentRepository.findByCourseIdAndEnrollmentStatus(courseId, LearnerEnrollment.ACTIVE);
		if(enrollments!=null){
			System.out.print(enrollments.size());
		}
		System.out.print("...................");
	}	


	/*
	 * ToplinkEnrollmentDAO Method:
	 * public List<LearnerEnrollment> getLearerEnrollmentsByCourses(List<Long> lstCourseIds);
	 */
	//@Test
	public void LearnerEnrollments_findByIdInAndCourseIdAndEnrollmentStatus(){
		
		Long[] courseId=new Long[]{59L};		
		List<LearnerEnrollment> enrollments = leanrEnrollmentRepository.findByCourseIdInAndEnrollmentStatus(Arrays.asList(courseId), LearnerEnrollment.ACTIVE);
		if(enrollments!=null){
			System.out.print(enrollments.size());
		}
		System.out.print("...................");
	}	


	/*
	 * ToplinkEnrollmentDAO Method:
	 * public List<LearnerEnrollment> getLearnerEnrollmentByEnrollmentIds(List<Long> lstEnrollmentIds);
	 */
	//@Test
	public void LearnerEnrollments_getLearnerEnrollmentByEnrollmentIds(){
		
		Long[] enrollmentIds=new Long[]{51L,52L,53L};		
		List<LearnerEnrollment> enrollments = leanrEnrollmentRepository.findByIdInAndEnrollmentStatus(Arrays.asList(enrollmentIds), LearnerEnrollment.ACTIVE);
		if(enrollments!=null){
			System.out.print(enrollments.size());
		}
		System.out.print("...................");
	}	
	
	/*
	 * ToplinkEnrollmentDAO Method:
	 * public List<LearnerEnrollment> getAllLearnerEnrollmentsByLearner(Learner learner);
	 */
//	@Test
	public void LearnerEnrollments_getAllLearnerEnrollmentsByLearner(){
		
		Long learnerId = 105L;		
		List<LearnerEnrollment> enrollments = leanrEnrollmentRepository.findByLearnerId(learnerId);
		if(enrollments!=null){
			System.out.print(enrollments.size());
		}
		System.out.print("...................");
	}	

	//Added By Marium Saud
//	@Test
	public void LearnerEnrollment_should_getEnrolledLearnerInfoByClassId(){
		List<VU360User> users=enrollmentService.getEnrolledLearnerInfoByClassId(152L);
		for(VU360User user:users){
			System.out.println("******************* ID = "+user.getId());
		}
	}
	
//	@Test
	public void LearnerEnrollment_should_getNumberofEnrollmentsOfThisCourse(){
		List<LearnerEnrollment> lst = leanrEnrollmentRepository.findByCourseId(1675L);
		System.out.println("******************* Length = "+lst.size());
	}
	
//	@Test
	public void LearenrEnrollment_should_findBy_LearnerId_And_CourseId_And_EnrollmentEndDate_LessThan(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dtStartDate = null;
		try {
			dtStartDate=sdf.parse("01/29/2013");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<LearnerEnrollment> lst = leanrEnrollmentRepository.findByLearnerIdAndCourseIdAndEnrollmentEndDateLessThan(1L, 1L, dtStartDate);
		System.out.println("******************* Length = "+lst.size());
	}
	
//	@Test
	public void LearnerEnrollment_should_getBy_SubscriptionId(){
		List<LearnerEnrollment> lst =leanrEnrollmentRepository.findBySubscriptionId(426L);
		System.out.println("******************* Length = "+lst.size());
		for(LearnerEnrollment le:lst){
			System.out.println("******************* ID = "+le.getId());
		}
	}
	
//	@Test
	public void LearnerEnrollment_should_getCompletedLearnerCourses(){
		RepositorySpecificationsBuilder<LearnerEnrollment> sb_learnerEnrollment=new RepositorySpecificationsBuilder<LearnerEnrollment>();
		
		sb_learnerEnrollment.with("learner_id", sb_learnerEnrollment.JOIN_EQUALS, 112L, "AND");
		sb_learnerEnrollment.with("enrollmentStatus", sb_learnerEnrollment.IN, new Object[]{LearnerEnrollment.EXPIRED,LearnerEnrollment.ACTIVE}, "AND");
		sb_learnerEnrollment.with("course_courseTitle", sb_learnerEnrollment.JOIN_LIKE_IGNORE_CASE, "Anti-Money Laundering for Insurance ProfessionalsFAS", "OR");
		sb_learnerEnrollment.with("course_description", sb_learnerEnrollment.JOIN_LIKE_IGNORE_CASE, "Anti-Money Laundering for Insurance ProfessionalsFAS", "OR");
		sb_learnerEnrollment.with("course_keywords", sb_learnerEnrollment.JOIN_LIKE_IGNORE_CASE, "Anti-Money Laundering for Insurance ProfessionalsFAS", "OR");
		
		List<LearnerEnrollment> result = leanrEnrollmentRepository.findAll(sb_learnerEnrollment.build());
		for(LearnerEnrollment le:result){
			System.out.println("******************* ID = "+le.getId());
		}
	}
	
	
//	@Test
	public void LearnerEnrollment_should_getEnrollmentsByProctor(){
		Proctor proctor=proctorRepository.findOne(1456L);
		List<LearnerEnrollment> result=leanrEnrollmentRepository.getEnrollmentsByProctor(proctor, "Zulfiqar", "Ali", "shifa.zehra@360training.com", null, null, "Anatomy in Sectional Images I", null);
		System.out.println(result.size());
	}
	
	//@Test
	public void LearnerEnrollment_should_count_By_CustomerEntitlementID(){
		Long count = leanrEnrollmentRepository.countByCustomerEntitlementId(51L);
		System.out.println(count);
	}

		//@Test
		public void LearnerEnrollment_FindBy_EnrollmentStatusNotIn_And_LearnerID(){
			
			List<String> lst = new ArrayList();
			
			lst.add(LearnerEnrollment.DROPPED);
			lst.add(LearnerEnrollment.SWAPPED);
			
			
			
			List<LearnerEnrollment> le = leanrEnrollmentRepository.findByLearnerIdAndEnrollmentStatusNotIn(105L, lst);
			
			for(LearnerEnrollment obj : le){
				System.out.println("**************** ID = "+obj.getId());
			}
			
			
			
		}
}
