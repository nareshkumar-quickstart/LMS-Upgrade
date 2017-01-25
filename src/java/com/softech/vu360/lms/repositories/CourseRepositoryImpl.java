package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LegacyCourse;
import com.softech.vu360.util.FormUtil;

public class CourseRepositoryImpl implements CourseRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Inject
	private CourseRepository courseRepository;

	@Override
	public List<Course> findCoursesByContentOwner(Collection<Long> coids, String courseTitle, String courseStatus, Boolean retired, String courseId) {

		StringBuilder queryString = new StringBuilder("SELECT c FROM Course c WHERE c.courseTitle LIKE :courseTitle AND c.courseStatus=:courseStatus AND c.retired=:retired");
		
		if (!StringUtils.isBlank(courseId)) {
			queryString.append(" AND c.bussinesskey LIKE :courseId");
		}
		if (coids!=null && coids.size() > 0){
			queryString.append(" AND c.contentOwner IN :coids");
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("courseTitle", "%"+courseTitle+"%");
		query.setParameter("courseStatus", courseStatus);//Course.PUBLISHED
		query.setParameter("retired", retired);
		
		if (!StringUtils.isBlank(courseId)) {
			query.setParameter("courseId", "%"+courseId+"%");
		}
		if (coids!=null && coids.size() > 0){
			query.setParameter("coids", coids);
		}
		
		
		List<Course> courseList = query.getResultList(); 
		
		return courseList;
	}
	
	@Override
	public List<Course> findBycourseTitle(List<Long> courseIdArray , String courseName, String courseGUID, String keywords,String searchCriteria){
		StringBuilder queryString = new StringBuilder("SELECT c FROM Course c WHERE c.id IN (:Id) AND c.courseStatus='Published'");

		if (!StringUtils.isBlank(searchCriteria)) {
			queryString.append(" AND (c.courseTitle LIKE :searchCriteria OR courseGUID LIKE :searchCriteriaGUID OR keywords LIKE :searchCriteria)");
		}
		else{
			if (!StringUtils.isBlank(courseName)) 
				queryString.append(" AND c.courseTitle LIKE :courseTitle");
			
			if (!StringUtils.isBlank(courseGUID)) 
				queryString.append(" AND c.courseGUID =:courseGUID");
			
			if (!StringUtils.isBlank(keywords)) 
				queryString.append(" AND c.keywords LIKE :keywords");
		}
		
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("Id", courseIdArray);
		
		
		if (!StringUtils.isBlank(searchCriteria)) {
			query.setParameter("searchCriteria", "%" + searchCriteria + "%");
			query.setParameter("searchCriteriaGUID", searchCriteria+"%");
			
		}
		else{
			if (!StringUtils.isBlank(courseName)) 
				query.setParameter("courseTitle", "%"+courseName+"%");
			
			if (!StringUtils.isBlank(courseGUID)) 
				query.setParameter("courseGUID", courseGUID);
			
			if (!StringUtils.isBlank(keywords)) 
				query.setParameter("keywords", "%"+keywords+"%");
		}
		
		List<Course> courseList = query.getResultList(); 
		return courseList;
	}

	@Override
	public List<Map<Object, Object>> findByCustomerIdBycourseNameByCourseIdByEntitlementIdByExpiryDate(Long customerId, String courseName,String courseId, String entitlementName, Date date){
		StringBuilder queryString = new StringBuilder("select * from CourseViewForEnrollLearner where Customer_id=" + customerId);

		if(! StringUtils.isBlank(courseName))
			queryString.append(" and COURSENAME like '%" + courseName +"%'");
		if(! StringUtils.isBlank(courseId))
			queryString.append(" and COURSECODE like '%" +courseId + "%'");
		if(! StringUtils.isBlank(entitlementName))
			queryString.append(" and ENTITLEMENT_NAME like '%" +entitlementName+ "%'");
		if((date!=null))
			queryString.append(" and EXPIRATION_DATE <= '"+ FormUtil.getInstance().formatDate(date, "yyyy-MM-dd") +"'");

		queryString.append(" order by courseName");
		
		Query query = entityManager.createNativeQuery(queryString.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 List rows = query.getResultList();  
		 List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		 mapList.addAll(rows);
 		return mapList;
	}
	
	@Override
	public List<Map<Object, Object>> getCoursesForEnrollmentByCourseIdsAndCustomer(Long customerId, List<Long> trainingPlanCourseIds){ 
	StringBuilder queryString = new StringBuilder("select * from CourseViewForEnrollLearner where Customer_id = :customerId ");
		
		if(trainingPlanCourseIds.size() >0)
			queryString.append(" and COURSE_ID in (:courseIds) ");	
			
		queryString.append(" order by courseName ");

		Query query = entityManager.createNativeQuery(queryString.toString());
		query.setParameter("customerId", customerId);
		
		if(trainingPlanCourseIds.size() > 0)
			query.setParameter("courseIds", trainingPlanCourseIds);
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();  
		List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		mapList.addAll(rows);
 		return mapList;	
	}
	
	@Override
	public List<Map<Object, Object>> findAvailableCourses(Distributor distributor,String courseName, String entityId, String keywords){
		String strSQL ="Select distinct COURSEGROUP_ID, DISTRIBUTOR_ID,	COURSEGROUPNAME,	BUSINESSKEY,	COURSENAME,	COURSE_ID,	KEYWORDS,	COURSEBUSINESSKEY,	PARENTCOURSEGROUPNAME10,	PARENTCOURSEGROUPNAME9,	PARENTCOURSEGROUPNAME8,	PARENTCOURSEGROUPNAME7,	PARENTCOURSEGROUPNAME6,	PARENTCOURSEGROUPNAME5,	PARENTCOURSEGROUPNAME4,	PARENTCOURSEGROUPNAME3,	PARENTCOURSEGROUPNAME2,	PARENTCOURSEGROUPNAME1,	PARENTCOURSEGROUP_ID10,	PARENTCOURSEGROUP_ID9,	PARENTCOURSEGROUP_ID8,	PARENTCOURSEGROUP_ID7,	PARENTCOURSEGROUP_ID6,	PARENTCOURSEGROUP_ID5,	PARENTCOURSEGROUP_ID4,	PARENTCOURSEGROUP_ID3,	PARENTCOURSEGROUP_ID2,	PARENTCOURSEGROUP_ID1 from LMS_CONTRACTSEARCH_VIEW where Distributor_ID=" + distributor.getId();

		String strSQLSubQuery=" AND (";
		
		if(StringUtils.isNotEmpty(courseName)) 	{
			String []courseNameList = courseName.split(",");
			
			for(int i=0;i<courseNameList.length;i++){
				if(i==0) //1st iteration
					strSQLSubQuery+="(CourseName LIKE '%" + courseNameList[i].trim().replace("'", "''") +"%'";
				else
					strSQLSubQuery+=" OR CourseName LIKE '%" + courseNameList[i].trim().replace("'", "''") +"%'";
			}
			strSQLSubQuery+=")";
		}
		
		if(StringUtils.isNotEmpty(entityId)) 	{
			String []courseIDList = entityId.split(",");
			
			if(strSQLSubQuery.length()>10)
				strSQLSubQuery+="OR(";
			else
				strSQLSubQuery+="(";
			
			for(int i=0;i<courseIDList.length;i++){
				if(i==0) //1st iteration
					strSQLSubQuery+=" COURSEBUSINESSKEY LIKE '%" + courseIDList[i].trim().replace("'", "''") +"%'";
				else
					strSQLSubQuery+=" OR COURSEBUSINESSKEY LIKE '%" + courseIDList[i].trim().replace("'", "''") +"%'";
			}
			strSQLSubQuery+=")";
		}
		
		if(StringUtils.isNotEmpty(keywords)) 	{
			if(strSQLSubQuery.length()>10)
				strSQLSubQuery+="OR(";
			else
				strSQLSubQuery+="(";
			strSQLSubQuery+=" KEYWORDS LIKE '%" + keywords.trim().replace("'","''")+ "%'";
			strSQLSubQuery+=")";
		}
		strSQLSubQuery+=")"; 
			
		if(strSQLSubQuery.length()>10)
			strSQL+=strSQLSubQuery;
		
		Query query = entityManager.createNativeQuery(strSQL);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 List rows = query.getResultList();  
		 List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		 mapList.addAll(rows);
 		return mapList;
	}
	public List<Map<Object, Object>> getByCourseAndCourseGroupId(List<CustomerEntitlement> customerEntitlements, 
			Long trainingPlanCourseId, Long courseGroupId){
		
		StringBuilder queryString = new StringBuilder("select * from CourseViewForEnrollLearner where CUSTOMERENTITLEMENT_ID in (:ceIds) ");
		
		if(trainingPlanCourseId >0)
			queryString.append(" and COURSE_ID = :trainingPlanCourseId ");	
			
		
		if(courseGroupId >0)
			queryString.append(" and COURSEGROUP_ID = :courseGroupId ");	
			
		
		queryString.append(" order by CUSTOMERENTITLEMENT_ID, courseName");

		
		Query query = entityManager.createNativeQuery(queryString.toString());

		if (customerEntitlements != null && customerEntitlements.size() > 0) 
			query.setParameter("ceIds", customerEntitlements);
		else
			query.setParameter("ceIds", -999999);
		
		
		if(trainingPlanCourseId >0)
			query.setParameter("trainingPlanCourseId", trainingPlanCourseId);
		
		if(courseGroupId >0)
			query.setParameter("courseGroupId", courseGroupId);
			
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 List rows = query.getResultList();  
		 List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		 mapList.addAll(rows);
 		return mapList;
	}
	
	public Long[] getPublishedEntitledCourses(Long customerID){
		StringBuilder queryString = new StringBuilder("SELECT DISTINCT COURSE_ID FROM CourseViewForCourseSurveyByCustomer courseview  ");
		queryString.append(" WHERE customer_id = :cusId ORDER BY COURSE_ID");
		
		Query query = entityManager.createNativeQuery(queryString.toString());
		query.setParameter("cusId", customerID);
		
		//TODO: Re-factor the following code 
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();  
		List<Map<Object, Object>> resultSet =new   ArrayList<Map<Object,Object>>();
		resultSet.addAll(rows);
		
		int index= 0;
		Long[] courseIDArray= new Long[resultSet.size()];
		for(Map<Object, Object> row : resultSet)
			courseIDArray[index++]= Long.parseLong(String.valueOf(row.get("COURSE_ID")));
		
		return courseIDArray; 
	}
	
	public Long[] getPublishedEntitledCourseIdArray(Long distributorId){
		StringBuilder queryString = new StringBuilder(" SELECT DISTINCT COURSE_ID FROM CourseViewForCourseSurveyByDistributor courseview  ");
								   queryString.append(" WHERE distributor_id= :dId  ORDER BY COURSE_ID");
		
		Query query = entityManager.createNativeQuery(queryString.toString());
		query.setParameter("dId", distributorId);
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();  
		List<Map<Object, Object>> resultSet =new   ArrayList<Map<Object,Object>>();
		resultSet.addAll(rows);
		
		int index= 0;
		Long[] courseIDArray= new Long[resultSet.size()];
		for(Map<Object, Object> row : resultSet)
			courseIDArray[index++]= Long.parseLong(String.valueOf(row.get("COURSE_ID")));
		
		return courseIDArray; 
	}
	@Override
	public List<Map<Object, Object>> findByCustomerEntitlementsByCourseIds(List<CustomerEntitlement> customerEntitlements,	Long[] trainingPlanCourseIds){
		StringBuilder queryString = new StringBuilder("select * from CourseViewForEnrollLearner ");
		queryString.append(" where CUSTOMERENTITLEMENT_ID in (:ceIds) ");
		
		if (trainingPlanCourseIds != null && trainingPlanCourseIds.length > 0) 
			queryString.append(" and COURSE_ID in (:courseIds)");
				
		queryString.append(" order by courseName");
		
		Query query = entityManager.createNativeQuery(queryString.toString());
		

		if (customerEntitlements != null) 
			query.setParameter("ceIds", customerEntitlements);
		else
			query.setParameter("ceIds", -999999);
		
		
		if (trainingPlanCourseIds != null && trainingPlanCourseIds.length > 0) 
			query.setParameter("courseIds", Arrays.asList(trainingPlanCourseIds));
		
	
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 List rows = query.getResultList();  
		 List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		 mapList.addAll(rows);
 		return mapList;
	}
	
	
	public List<Map<Object, Object>> findAvailableCourses(Long distributorId, List<Long> courseIdList){
		StringBuilder queryString = new StringBuilder(" Select distinct * from LMS_CONTRACTSEARCH_VIEW  ");
		queryString.append(" where Distributor_ID= :dId ");
		
		if (courseIdList != null && courseIdList.size() > 0) 
			queryString.append(" AND COURSE_ID IN (:courseIds)");
				
		Query query = entityManager.createNativeQuery(queryString.toString());
		
		query.setParameter("dId", distributorId);
		
		if (courseIdList != null && courseIdList.size() > 0) 
			query.setParameter("courseIds", courseIdList);
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		 List rows = query.getResultList();  
		 List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		 mapList.addAll(rows);
 		return mapList;
	}
	
	@Override
	public List<Map<Object, Object>> findCatalogSPById(Long learner_Id,Long Customer_id , String search,String sp_Name) {
		
		Query query = entityManager.createNativeQuery("EXEC :procedure_name :LEARNERID, :CUSTOMERID, :COURSETEXT");
		query.setParameter("LEARNERID", learner_Id);
		query.setParameter("CUSTOMERID", Customer_id);//Course.PUBLISHED
		if (StringUtils.isNotBlank(search) && !search.equals("Search"))
		{
			query.setParameter("COURSETEXT", search);
		}else{
			query.setParameter("COURSETEXT", null);
		}
		query.setParameter("procedure_name", sp_Name);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();  
		List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		mapList.addAll(rows);
		return mapList;
 		
	}
   
	@Override
	public List<Course> findCoursesByCustomer(Long[] courseIdArray, String courseTitle, String courseGuid, String keywords, String searchCriteria) {
		
		String courseStatus = "published";
		List<Course> allCourses = new ArrayList<>();
		
		if (!ArrayUtils.isEmpty(courseIdArray)) {
			List<Long> courseIds = Arrays.asList(courseIdArray);
			int IN_PARAMETER_LIMIT = 999;
			int listSize  = courseIds.size();
			for (int i=0; i<listSize ; i += IN_PARAMETER_LIMIT) {
				List<Long> courseIdsSubList = breakCourseIds(courseIds, listSize, IN_PARAMETER_LIMIT, i);
				if (!CollectionUtils.isEmpty(courseIdsSubList)) {
					try (Stream<Course> courseStream = getCourseStream(courseIdsSubList, courseTitle, courseGuid, keywords, searchCriteria, courseStatus);) {
						if (courseStream != null) {
							List<Course> publishedCourses = courseStream.collect(Collectors.<Course>toList());
							allCourses.addAll(publishedCourses);
						}
					}
				}			
			}
		}
		return allCourses;
	}
	
	@Override
	public List<Course> findCoursesByCustomer(Long[] courseIdArray, String courseTitle, String courseType) {
		
		String courseStatus = "published";
		List<Course> allCourses = new ArrayList<>();
		if (!ArrayUtils.isEmpty(courseIdArray)) {
			List<Long> courseIds = Arrays.asList(courseIdArray);
			int IN_PARAMETER_LIMIT = 999;
			int listSize  = courseIds.size();
			for (int i=0; i<listSize ; i += IN_PARAMETER_LIMIT) {
				List<Long> courseIdsSubList = breakCourseIds(courseIds, listSize, IN_PARAMETER_LIMIT, i);
				if (!CollectionUtils.isEmpty(courseIdsSubList)) {
					try (Stream<Course> courseStream = getCourseStream(courseIdsSubList, courseTitle, courseType, courseStatus);) {
						if (courseStream != null) {
							List<Course> publishedCourses = courseStream.collect(Collectors.<Course>toList());
							allCourses.addAll(publishedCourses);
						}
					}
				}
			}
		}
		return allCourses;
	}

	private List<Long> breakCourseIds(List<Long> courseIds, int listSize, int inParamLimit, int index) {
		
		List<Long> courseIdsSubList = null;
		if (!CollectionUtils.isEmpty(courseIds)) {
			if (listSize  > index + inParamLimit) {
		        courseIdsSubList = courseIds.subList(index, (index + inParamLimit));
		    } else {
		        courseIdsSubList = courseIds.subList(index, listSize);
		    }
		}
        return courseIdsSubList;
	}
	
	@Transactional(readOnly = true)
	private Stream<Course> getCourseStream(List<Long> courseIds, String courseTitle, String courseGuid, String keywords, String searchCriteria, String courseStatus) {
		
		Stream<Course> courseStream = null;
		
		if (StringUtils.isNotBlank(searchCriteria)) {
			courseStream = courseRepository.findByIdInAndCourseTitleLikeOrCourseGUIDLikeOrKeywordsLikeAndCourseStatusAllIgnoreCase(courseIds, searchCriteria, searchCriteria, searchCriteria, courseStatus);
		} else if (StringUtils.isNotBlank(courseTitle) && StringUtils.isNotBlank(courseGuid) && StringUtils.isNotBlank(keywords)) {
			courseStream = courseRepository.findByIdInAndCourseTitleLikeAndCourseGUIDLikeAndKeywordsLikeAndCourseStatusAllIgnoreCase(courseIds, courseTitle, courseGuid, keywords, courseStatus);
		} else if (StringUtils.isNotBlank(courseTitle) && StringUtils.isNotBlank(courseGuid)) {
			courseStream = courseRepository.findByIdInAndCourseTitleLikeAndCourseGUIDLikeAndCourseStatusAllIgnoreCase(courseIds, courseTitle, courseGuid, courseStatus);
		} else if (StringUtils.isNotBlank(courseTitle) && StringUtils.isNotBlank(keywords)) {
			courseStream = courseRepository.findByIdInAndCourseTitleLikeAndKeywordsLikeAndCourseStatusAllIgnoreCase(courseIds, courseTitle, keywords, courseStatus);
		}  else if (StringUtils.isNotBlank(courseGuid) && StringUtils.isNotBlank(keywords)) {
			courseStream = courseRepository.findByIdInAndCourseGUIDLikeAndKeywordsLikeAndCourseStatusAllIgnoreCase(courseIds, courseGuid, keywords, courseStatus);
		} else if (StringUtils.isNotBlank(courseTitle)) {
			courseStream = courseRepository.findByIdInAndCourseTitleLikeAndCourseStatusAllIgnoreCase(courseIds, courseTitle, courseStatus);
		} else if (StringUtils.isNotBlank(courseGuid)) {
			courseStream = courseRepository.findByIdInAndCourseGUIDLikeAndCourseStatusAllIgnoreCase(courseIds, courseGuid, courseStatus);
		} else if (StringUtils.isNotBlank(keywords)) {
			courseStream = courseRepository.findByIdInAndKeywordsLikeAndCourseStatusAllIgnoreCase(courseIds, keywords, courseStatus);
		} else {
			courseStream = courseRepository.findByIdIn(courseIds);
		}
		return courseStream;
	}
	
	@Transactional(readOnly = true)
	private Stream<Course> getCourseStream(List<Long> courseIds, String courseTitle, String courseType, String courseStatus) {
		
		Stream<Course> courseStream = null;
		if (StringUtils.isNotBlank(courseTitle) && StringUtils.isNotBlank(courseType)) {
			if (!courseType.equalsIgnoreCase("all")) {
				//courseType = getCourseType(courseType);
				courseStream = courseRepository.findByIdInAndCourseTitleLikeIgnoreCaseAndAndCourseTypeAndCourseTypeNotAndCourseStatusIgnoreCase(courseIds, courseTitle, courseType, LegacyCourse.COURSE_TYPE, courseStatus);
			} else {
				courseStream = courseRepository.findByIdInAndCourseTitleLikeIgnoreCaseAndAndCourseTypeNotAndCourseStatusIgnoreCase(courseIds, courseTitle, courseType, courseStatus);
			}
			
		} else if (StringUtils.isNotBlank(courseTitle) && StringUtils.isBlank(courseType)) {
			courseStream = courseRepository.findByIdInAndCourseTitleLikeIgnoreCaseAndAndCourseTypeNotAndCourseStatusIgnoreCase(courseIds, courseTitle, courseType, courseStatus);
		} else if (StringUtils.isBlank(courseTitle) && StringUtils.isNotBlank(courseType)) {
			if (!courseType.equalsIgnoreCase("all")) {
				//courseType = getCourseType(courseType);
				courseStream = courseRepository.findByIdInAndCourseTypeAndCourseTypeNotAndCourseStatusIgnoreCase(courseIds, courseType, LegacyCourse.COURSE_TYPE, courseStatus);
			} else {
				courseStream = courseRepository.findByIdInAndCourseTypeNotAndCourseStatusIgnoreCase(courseIds, LegacyCourse.COURSE_TYPE, courseStatus);
			}
		} else {
			courseStream = courseRepository.findByIdIn(courseIds);
		}
		return courseStream;
	}
	
	@Override
	public List<Course> getCourseUnderAlertTriggerFilter(Long alertTriggerId, String courseName, String courseType) {
		
		StringBuilder queryString = new StringBuilder();
		
		if(courseType == null || courseType.equalsIgnoreCase("all") || courseType.equalsIgnoreCase(""))	{
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
					   .append("'%").append(courseName).append("%'")
					   .append(" and COURSETYPE IN ('")
					   .append(Course.Types.Scorm.toString()).append("', '")
					   .append(Course.Types.Weblink.toString()).append("', '")
					   .append(Course.Types.DiscussionForum.toString()).append("', '")
					   .append(Course.Types.Synchronous.toString()).append("', '")
					   .append(Course.Types.Webinar.toString()).append("', '")
					   .append(Course.Types.HomeworkAssignment.toString()).append("', '")
					   .append(Course.Types.InstructorConnect.toString()).append("', '")
					   .append(Course.Types.SelfPaced.toString())
					   .append("')")
					   .append(" and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
					   .append(")");
		} else if (courseType.equalsIgnoreCase(Course.Types.Synchronous.toString())) {
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
			   .append("'%").append(courseName).append("%'")
			   .append(" and COURSETYPE IN ('")
			   .append(Course.Types.Synchronous.toString())
			   .append("')")
			   .append(" and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
			   .append(")");
		} else if (courseType.equalsIgnoreCase(Course.Types.Scorm.toString())) {
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
			   .append("'%").append(courseName).append("%'")
			   .append(" and COURSETYPE IN ('")
			   .append(Course.Types.Scorm.toString())
			   .append("')")
			   .append(" and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
			   .append(")");
		} else if (courseType.equalsIgnoreCase(Course.Types.Weblink.toString())) {
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
			   .append("'%").append(courseName).append("%'")
			   .append(" and COURSETYPE IN ('")
			   .append(Course.Types.Weblink.toString())
			   .append("')")
			   .append(" and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
			   .append(")");
		} else if (courseType.equalsIgnoreCase(Course.Types.DiscussionForum.toString())) {
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
			   .append("'%").append(courseName).append("%'")
			   .append(" and COURSETYPE IN ('")
			   .append(Course.Types.DiscussionForum.toString())
			   .append("')")
			   .append(" and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
			   .append(")");
		} else if (courseType.equalsIgnoreCase(Course.Types.Webinar.toString())) {
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
			   .append("'%").append(courseName).append("%'")
			   .append(" and COURSETYPE IN ('")
			   .append(Course.Types.Webinar.toString())
			   .append("')")
			   .append(" and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
			   .append(")");
		} else if (courseType.equalsIgnoreCase(Course.Types.HomeworkAssignment.toString())) {
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
			   .append("'%").append(courseName).append("%'")
			   .append(" and COURSETYPE IN ('")
			   .append(Course.Types.HomeworkAssignment.toString())
			   .append("')")
			   .append(" and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
			   .append(")");
		} else if (courseType.equalsIgnoreCase(Course.Types.InstructorConnect.toString())) {
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
			   .append("'%").append(courseName).append("%'")
			   .append(" and COURSETYPE IN ('")
			   .append(Course.Types.InstructorConnect.toString())
			   .append("')")
			   .append("  and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
			   .append(")");
		} else if (courseType.equalsIgnoreCase(Course.Types.SelfPaced.toString())) {
			queryString.append("SELECT COURSE.* FROM COURSE WHERE NAME LIKE ")
			   .append("'%").append(courseName).append("%'")
			   .append(" and COURSETYPE IN ('")
			   .append(Course.Types.SelfPaced.toString())
			   .append("')")
			   .append(" and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId")
			   .append(")");
		}
		
		System.out.print(queryString.toString());
		Query query = entityManager.createNativeQuery(queryString.toString(), Course.class);
		query.setParameter("alertTriggerFilterId", alertTriggerId);
		List<Course> courseList =  (ArrayList<Course>)query.getResultList();
		return courseList;
		
	}
	
	@Override
	public List<Object[]> getEntitlementsByCourseGroupId(Long learner_Id, Long courseGroupId) {
		//on uat rec= 2731 >> EXEC SP_COURSECATALOGWITHCOURSEGROUPSBYCG 559515,null,73
		Query query = entityManager.createNativeQuery("EXEC SP_COURSECATALOGWITHCOURSEGROUPSBYCG :LEARNERID, :CUSTOMERID, :COURSEGROUPID");
		query.setParameter("LEARNERID", learner_Id);
		query.setParameter("CUSTOMERID", null);
		query.setParameter("COURSEGROUPID", courseGroupId);
		//query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return query.getResultList();  
		/*List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		mapList.addAll(rows);
		return mapList;
		*/

		}
	
	@Override
	public List<Map<Object, Object>> getEntitlementsByTrainingPlanId(Long learner_Id, Long trainingPlanId) {
		
		Query query = entityManager.createNativeQuery("EXEC SP_COURSECATALOGWITHTRAININGPLANSBYCG :LEARNERID, :CUSTOMERID, :TRAININGPLANID");
		query.setParameter("LEARNERID", learner_Id);
		query.setParameter("CUSTOMERID", null);
		query.setParameter("TRAININGPLANID", trainingPlanId);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List rows = query.getResultList();  
		List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		mapList.addAll(rows);
		return mapList;
		}
}
