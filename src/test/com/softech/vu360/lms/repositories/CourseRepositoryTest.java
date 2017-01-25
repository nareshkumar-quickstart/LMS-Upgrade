package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LmsTestConfig.class)
public class CourseRepositoryTest {
 
	@Inject
	private CourseRepository courseRepository;

	@Inject
	private EntitlementService entitlementService;

	CourseAndCourseGroupService courseAndcourseGroupService;

	@Autowired
	ApplicationContext context;

	@Before
	public void CustomerRepositoryInit() {
		courseAndcourseGroupService = (CourseAndCourseGroupService) context.getBean("courseAndcourseGroupService");
	}

	// @Test
	public void findCoursesByDistributor() {
		long courseGroupIdArray[] = { 1 };
		String courseName = "testing";
		String courseGUID = "14F93637DED84498BCBB8A336A1A1861";
		String keywords = "1";
		String searchCriteria = "testing";

		List<Course> courses = entitlementService.findCoursesByDistributor(courseGroupIdArray, courseName, courseGUID,
				keywords, searchCriteria);

		for (Course obj : courses) {
			System.out.println("**************** ID = " + obj.getId());
		}
	}

	@Test
	public void findBycourseTitleByCode() {
		Customer customer = new Customer();
		customer.setId(36805L);
		String courseName = "";//December09Cours";
		String courseId = "";//1";
		String entitlementName = "";//Nov Customer";
		Date date =null;// new Date();
		// int resultSetLimit =1;

		List<Map<Object, Object>> resultSet = courseRepository.findByCustomerIdBycourseNameByCourseIdByEntitlementIdByExpiryDate(customer.getId(), courseName, courseId,
				entitlementName, date);
		for (Map<Object, Object> row : resultSet) {
			EnrollmentCourseView enrollmentCourseView = new EnrollmentCourseView();
			enrollmentCourseView.setCourseName((String) row.get("COURSENAME"));
			System.out.println(enrollmentCourseView.getCourseName());
		}
	}

	// @Test
	public void findByIdIn() {
		List<CustomerEntitlement> customerEntitlementList = new ArrayList<CustomerEntitlement>();
		Long[] courseIds = null;// {99250L, 99254L};
		CustomerEntitlement ce = new CustomerEntitlement();
		ce.setId(201505L);
		CustomerEntitlement ce2 = new CustomerEntitlement();
		ce2.setId(206455L);
		customerEntitlementList.add(ce);
		customerEntitlementList.add(ce2);

		List<Map<Object, Object>> resultSet = courseRepository.findByCustomerEntitlementsByCourseIds(customerEntitlementList, courseIds);
		for (Map<Object, Object> row : resultSet) {
			EnrollmentCourseView enrollmentCourseView = new EnrollmentCourseView();
			enrollmentCourseView.setCourseName((String) row.get("COURSENAME"));
			System.out.println(enrollmentCourseView.getCourseName());
		}
	}

	// @Test
	public void getCoursesForEnrollmentByCourseAndCourseGroupId() {
		List<CustomerEntitlement> customerEntitlementList = new ArrayList<CustomerEntitlement>();
		CustomerEntitlement ce = new CustomerEntitlement();
		ce.setId(201505L);
		CustomerEntitlement ce2 = new CustomerEntitlement();
		ce2.setId(206455L);
		customerEntitlementList.add(ce);
		customerEntitlementList.add(ce2);
		Long courseId = 5861L;
		Long courseGroupId = 73L;

		List<Map<Object, Object>> resultSet = courseRepository.getByCourseAndCourseGroupId(customerEntitlementList,
				courseId, courseGroupId);
		for (Map<Object, Object> row : resultSet) {
			EnrollmentCourseView enrollmentCourseView = new EnrollmentCourseView();
			enrollmentCourseView.setCourseName((String) row.get("COURSENAME"));
			System.out.println(enrollmentCourseView.getCourseName());
		}
	}

	//@Test
	public void getCoursesForEnrollmentByCourseIdsAndCustomer() {
		Long courseGroupIdArray[] = {96055L ,94541L };
		
		List<Map<Object, Object>> resultSet  = courseRepository.getCoursesForEnrollmentByCourseIdsAndCustomer(36805L, Arrays.asList(courseGroupIdArray));
		
		for (Map<Object, Object> row : resultSet) {
			System.out.println( row.get("COURSE_ID")) ;
		}       
	}
	
	// @Test
	public void getPublishedEntitledCourses() {
		Customer customer = new Customer();
		customer.setId(1L);

		Long[] resultSet = courseRepository.getPublishedEntitledCourses(customer.getId());
		for (Long row : resultSet) {
			System.out.println(row);
		}
	}

	// @Test
	public void getPublishedEntitledCourseIdArray() {
		Long[] resultSet = courseRepository.getPublishedEntitledCourseIdArray(111111L);
		long[] resultSet2 = ArrayUtils.toPrimitive(resultSet);
		for (Long row : resultSet) {
			System.out.println(row);
		}
	}

	// @Test
	public void findAvailableCourses() {
		Distributor distributor = new Distributor();
		distributor.setId(1L);
		String courseName = "is, op";
		String entityId = "1, 2";
		String keywords = "s, d";
		List<Map<Object, Object>> resultSet = courseRepository.findAvailableCourses(distributor, courseName, entityId,
				keywords);

		for (Map<Object, Object> record : resultSet) {
			System.out.println(record.get("COURSE_ID").toString());
		}
	}

	//@Test
	public void findAvailableCourses2() {
		List<Long> courseIdList = new ArrayList<Long>();
		courseIdList.add(1L);
		courseIdList.add(2L);
		List<Map<Object, Object>> resultSet = courseRepository.findAvailableCourses(1L, courseIdList);

		for (Map<Object, Object> record : resultSet) {
			System.out.println(record.get("COURSE_ID").toString());
		}
	}

	// @Test
	public void Course_Should_find() {

		try {
			// // Course course = courseRepository.findOne(1L);
			// System.out.println(course);

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_List_Should_find() {
		try {

			Long[] ls = new Long[4];
			ls[0] = 1L;
			ls[1] = 2L;
			ls[2] = 53L;
			ls[3] = 54L;

			List<Course> crs = courseRepository.findByIdIn(ls);
			System.out.println();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	// @Test
	public void Course_Should_find_by_Course_Id() {
		try {

			Course course = courseRepository.findFirstByBussinesskeyOrderByIdDesc("000001-151203-849");
			System.out.println(course.getBussinesskey());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_Should_find_by_Course_GUID() {
		try {

			Course course = courseRepository.findFirstByCourseGUIDOrderByIdDesc("DF8F7756A3224422B5D84717808B3DD2");
			System.out.println(course.getBussinesskey());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_List_Should_find_by_Course_GUID() {
		try {

			String[] courseGuid = new String[3];

			courseGuid[0] = "18d416df0f62405c99a694ac52f13781";
			courseGuid[1] = "7D701C1D6AEC4C2099E4449A699F609A";
			courseGuid[2] = "5136132a-4daa-4235-94ba-5c08cf320f83";
			List<Course> crs = courseRepository.findByCourseGUIDIn(courseGuid);

			System.out.println(crs.get(0).getCourseGuide());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_List_Should_find_Active_Course() {
		try {

			List<Course> crs = courseRepository.getActiveCoursOfCourseGroup(8L);
			for (Course course : crs)
				System.out.println(course.getId());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_List_Should_find_by_Course_GUIDs_Vector() {
		try {
			List<String> courseGUIDList = new ArrayList<String>();

			courseGUIDList.add("18d416df0f62405c99a694ac52f13781");
			courseGUIDList.add("7D701C1D6AEC4C2099E4449A699F609A");
			courseGUIDList.add("5136132a-4daa-4235-94ba-5c08cf320f83");

			@SuppressWarnings("unchecked")
			Vector courseGUIDListVector = new Vector(courseGUIDList);

			List<Course> crs = courseRepository.findByCourseGUIDIn(courseGUIDListVector);

			System.out.println(crs.get(0).getCourseGuide());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_List_Should_find_By_ContentOwner() {
		try {

			List<Course> crs = courseRepository.findByContentOwnerId(53L);
			for (Course course : crs)
				System.out.println(course.getId());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_List_Should_find_By_CourseConfigurationTemplateId() {
		try {

			List<Course> crs = courseRepository.findByCourseConfigTemplateId(2678L);
			for (Course course : crs)
				System.out.println(course.getId());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_List_Should_find_By_ContenetOwner_CourseName_BussinessKey() {
		try {
			Long[] l = new Long[3];
			l[0] = 57L;
			l[1] = 58L;
			l[2] = 165L;
			// new SearchCriteria("firstName", ":", "jo")
			// RepositorySpecification<Course> courseStatus = new
			// RepositorySpecification<Course>(new
			// SearchCriteria("courseStatus", ":", Course.PUBLISHED));
			// RepositorySpecification<Course> retired = new
			// RepositorySpecification<Course>(new SearchCriteria("retired",
			// ":", false));
			// /////////////////CoureRepositorySpecification ID = new
			// CoureRepositorySpecification(new SearchCriteria("id", ":NL",
			// 628));
			// RepositorySpecification<Course> ID = new
			// RepositorySpecification<Course>(new SearchCriteria("id", ":L",
			// l));
			// RepositorySpecification<Course> courseTitle = new
			// RepositorySpecification<Course>(new SearchCriteria("courseTitle",
			// ":IC", "oSHA"));
			// ///////////////CoureRepositorySpecification bussinesskey = new
			// CoureRepositorySpecification(new SearchCriteria("courseTitle",
			// ":IC", "bussinesskey"));
			// RepositorySpecification<Course> bussinesskey = new
			// RepositorySpecification<Course>(new
			// SearchCriteria("bussinesskey", ":IC", "11"));
			//
			// List<Course> crs =
			// courseRepository.findAll(Specifications.where(courseStatus).and(retired).and(ID).and(courseTitle).and(bussinesskey));

			RepositorySpecificationsBuilder<Course> rsp = new RepositorySpecificationsBuilder<Course>();
			rsp.with("courseStatus", ":", Course.PUBLISHED, "AND");
			rsp.with("retired", ":", false, "AND");
			rsp.with("courseTitle", ":IC", "oSHA", "AND");
			rsp.with("id", ":L", l, "AND");
			rsp.with("bussinesskey", ":IC", "10", "AND");

			List<Course> crs1 = courseRepository.findAll(rsp.build());

			// repository.findAll(Specifications.where(spec1).and(spec2));

			// List<Course> crs =
			// courseRepository.findByCourseConfigTemplateId(2678L);
			for (Course course : crs1)
				System.out.println(course.getId());

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	// @Test
	public void Course_List_Should_find_By_SP() {
		try {
			Long courseGroupId = 5L;
			// List<Object[]> objs =
			// this.courseRepository.getCourseCatalogData(1L,null , "TX-NFPA 70E
			// Changes - V3B", "SP_COURSECATALOGWITHCOURSEGROUPS") ;
			// List<Object[]> objs =
			// this.courseRepository.getCourseCatalogData(1L,null , "1101
			// Control Performance Requirements",
			// "SP_COURSECATALOGWITHTRAININGPLANS") ;
			// List<Object[]> objs =
			// this.courseRepository.getCourseCatalogData(1L,null , "1101
			// Control Performance Requirements", "SP_COURSECATALOGWITHMISC") ;
			// List<Map<Object, Object>> mapList =
			// this.courseRepository.findCatalogSPById(1L,null , "1101 Control
			// Performance Requirements", "SP_COURSECATALOGWITHMISC") ;
			List<Map<Object, Object>> mapList = this.courseRepository.findCatalogSPById(1L, null,
					courseGroupId.toString(), "SP_COURSECATALOGWITHCOURSEGROUPSBYCG");
					//

			// List<Map<Object, Object>> mapList =
			// this.courseRepository.findCatalogSPById(1L,null , null,
			// "CourseCatalogWithCourseGroups") ;

			// CourseCatalogWithCourseGroups
			// List<Course> crs =
			// courseRepository.findByCourseConfigTemplateId(2678L);
			// for(Object obj[]:objs)
			// System.out.println(obj[3]);
			for (Map row : mapList) {
				System.out.println("id = " + row.get("COURSETYPE"));

				// Map row = (Map) obj;
				// mapList.add(row);
				// System.out.println("id = " + row.get("ID"));
				// System.out.println("name = " + row.get("NAME"));
				// System.out.println("age = " + row.get("AGE"));

			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	/*
	 * @Test public void getCourseUnderAlertTriggerFilter(){ String courseType=
	 * "Webinar Course"; List<Course> courses = new ArrayList();
	 * 
	 * if(courseType == null || courseType.equalsIgnoreCase("all") ||
	 * courseType.equalsIgnoreCase("")){ //courses =
	 * courseRepository.getCourseUnderAlertTriggerFilter(5053L,"course"); }else{
	 * 
	 * if (courseType.equalsIgnoreCase(SynchronousCourse.COURSE_TYPE)) {
	 * courseType = SynchronousCourse.COURSE_TYPE; } else if
	 * (courseType.equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) { courseType =
	 * WebinarCourse.COURSE_TYPE; } else if (courseType.equalsIgnoreCase(
	 * "SCORM Package")) { courseType = ScormCourse.COURSE_TYPE; } else if
	 * (courseType.equalsIgnoreCase("Weblink Course")) { courseType =
	 * WebLinkCourse.COURSE_TYPE; } else if (courseType.equalsIgnoreCase(
	 * "Discussion Forum")) { courseType = DiscussionForumCourse.COURSE_TYPE; }
	 * else if
	 * (courseType.equalsIgnoreCase(HomeworkAssignmentCourse.COURSE_TYPE)) {
	 * courseType = HomeworkAssignmentCourse.COURSE_TYPE; } else if
	 * (courseType.equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
	 * courseType = InstructorConnectCourse.COURSE_TYPE; } else if
	 * (courseType.equalsIgnoreCase(SelfPacedCourse.COURSE_TYPE)) { courseType =
	 * SelfPacedCourse.COURSE_TYPE; } List<Long> ids = new ArrayList();
	 * ids.add(5053L); //String[] courseType courses =
	 * courseRepository.findByNameLikeAndCourseTypeInAndIdIn("%course%",
	 * courseType,115896L);
	 * 
	 * }
	 * 
	 * for(Course obj : courses) System.out.println("********** "
	 * +obj.getName()); }
	 */

	// Added By Marium Saud
	// @Test
	public void Courses_should_getCourses_with_CourseType_StringArray_HardCoded() {
		Map<String, Object> list = courseAndcourseGroupService.getCourses(3L, "4.6.0 DFC", null, "All", "Published",
				"name", 0, 0, 10);
		List<Course> result = (List<Course>) list.get("list");
		for (Course course : result) {
			System.out.println("CourseName is " + course.getCourseTitle());
		}
	}

	// @Test
	public void Courses_should_getCourses_with_CourseType_StringArray_Parameter() {
		String[] courseType = new String[2];
		courseType[0] = "Self Paced Course";
		courseType[1] = "Classroom Course";
		Map<String, Object> list = courseAndcourseGroupService.getCourses(3L, null, null, "All", courseType,
				"Published", "name", 0, 1, 4);
		List<Course> result = (List<Course>) list.get("list");
		System.out.println("Record Size is " + list.get("recordSize"));
		for (Course course : result) {
			System.out.println("CourseName is " + course.getCourseTitle());
		}
	}

	// @Test
	public void Courses_should_getCourses() {
		Long[] courseIds = new Long[1];
		courseIds[0] = 6890L;
		List<Course> list = courseAndcourseGroupService.getCourses(3L, null, null, courseIds);
		for (Course course : list) {
			System.out.println("CourseName is " + course.getCourseTitle());
		}
	}

	//@Test
	public void findCoursesByCustomer5Params() {

		String courseTitle = "";
		String courseGuid = "";
		String keywords = "1";
		String searchCriteria = "1";
		String courseStatus = "published";

		List<Course> courseList = courseRepository.findByKeywordsAndCourseStatus(keywords, courseStatus);
		try {
			// List<Long> courseIds = getAllPublishedCourseIds("Published");
			List<Long> courseIds = getCourseIds(courseList);
			if (!CollectionUtils.isEmpty(courseIds)) {

				Long[] courseIdArray = courseIds.toArray(new Long[courseIds.size()]);
				List<Course> courses = courseRepository.findCoursesByCustomer(courseIdArray, courseTitle, courseGuid,
						keywords, searchCriteria);
				if (!CollectionUtils.isEmpty(courses)) {
					List<Long> coursesIds = getCourseIds(courses);
					System.out.println(coursesIds);
				}
				System.out.println();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void findCoursesByCustomer3Params() {
		
		String courseTitle = "";
		String courseGuid = "";
		String keywords = "1";
		String searchCriteria = "1";
		String courseStatus = "published";
		
	}

	private List<Long> getAllPublishedCourseIds(String courseStatus) {

		List<Long> courseIds = null;

		try (Stream<Course> courseStream = courseRepository.findByCourseStatus(courseStatus);) {
			List<Course> courseList = courseStream.parallel().collect(Collectors.<Course>toList());
			courseIds = getCourseIds(courseList);
		}
		return courseIds;
	}

	private List<Long> getCourseIds(List<Course> courses) {

		List<Long> courseIds = null;
		if (!CollectionUtils.isEmpty(courses)) {
			//courseIds = courses.stream().map(Course::getId).collect(Collectors.<Course>toList());
		}
		return courseIds;

	}

	private List<Long> breakCourseIds(List<Long> courseIds, int listSize, int inParamLimit, int index) {

		List<Long> courseIdsSubList;
		if (listSize > index + inParamLimit) {
			courseIdsSubList = courseIds.subList(index, (index + inParamLimit));
		} else {
			courseIdsSubList = courseIds.subList(index, listSize);
		}
		return courseIdsSubList;

	}

	@Test
	public void Course_should_getCourseUnderAlertTriggerFilter() {
		courseRepository.getCourseUnderAlertTriggerFilter(1L, "Some course", "all");
	}
}
