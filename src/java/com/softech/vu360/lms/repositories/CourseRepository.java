package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Course;

public interface CourseRepository extends JpaRepository<Course,Long>, CourseRepositoryCustom, JpaSpecificationExecutor<Course> {

	//Author findByVu360UserId(Long id);
	//Course  findByCourseId(Long id);
		
	//public List<Course> getCourseById(long[] courseIdArray)
	List<Course>  findByIdIn(Long[] ls1);

	
	//public Course getCourseByCourseId(String courseId) 
	Course  findFirstByBussinesskeyOrderByIdDesc(String courseId);
	
	//public Course getCourseByGUID(String guid) 
	Course  findFirstByCourseGUIDOrderByIdDesc(String courseId);
	
	//public List<Course> getCoursesByGUIDs(String[] guids)
	List<Course>  findByCourseGUIDIn(String[] guids);
	
	//getActiveCourses (Active Course in a course group is not being pull using Othere methods so Native Query has been used. replace when solution will be found)
	@Query(value = "Select c.* from Course c, course_CourseGroup ccg " +
			"where ccg.courseGroup_id = :CourseGroupId and c.id = ccg.course_id " +
			" and RETIREDTF = 0 and COURSESTATUS = 'Published'", nativeQuery=true)
	List<Course> getActiveCoursOfCourseGroup( @Param("CourseGroupId") Long CourseGroupId);
	
	//public List<Course> getCourseByGUID(Vector<String> guid)
	List<Course> findByCourseGUIDIn(Vector<String> guid);
	
	//List<Course> getAllCoursesByContentOwnerId(Long instructorContentOwnerId)
	List<Course> findByContentOwnerId(Long ContentOwnerId);
	
	//List<Course> getCoursesByCourseConfigurationTemplate(long ccTemplateId)
	List<Course> findByCourseConfigTemplateId(Long CourseConfigTemplateId);
	
	
	//public void deleteCourse(long courseIdArray[]) 
	void deleteByIdIn(Long CourseIds[]);
	
	Course findByCourseGUID(String guid);
	
	@Query(value = "select COURSE.* from ALERT\n" +
            "inner join ALERTTRIGGER on ALERTTRIGGER.ALERT_ID = ALERT.ID\n" +
            "inner join ALERTTRIGGERFILTER on ALERTTRIGGERFILTER.ALERTTRIGGER_ID = ALERTTRIGGER.ID\n" +
            "inner join ALERTTRIGGERFILTER_COURSE on ALERTTRIGGERFILTER_COURSE.ALERTTRIGGERFILTER_ID = ALERTTRIGGERFILTER.ID\n" +
            "inner join COURSE on COURSE.ID = ALERTTRIGGERFILTER_COURSE.COURSE_ID\n" +
            "where ALERT.ID =:alertId", nativeQuery=true)
	public List<Course> findCourseByAlertId(@Param("alertId") Long alertId);
	
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTitleLikeOrCourseGUIDLikeOrKeywordsLikeAndCourseStatusAllIgnoreCase(Collection<Long> ids, String courseTitle, String courseGuid, String keywords, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTitleLikeAndCourseGUIDLikeAndKeywordsLikeAndCourseStatusAllIgnoreCase(Collection<Long> ids, String courseTitle, String courseGuid, String keywords, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTitleLikeAndCourseGUIDLikeAndCourseStatusAllIgnoreCase(Collection<Long> ids, String courseTitle, String courseGuid, String courseStatus);
	
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTitleLikeAndKeywordsLikeAndCourseStatusAllIgnoreCase(Collection<Long> ids, String courseTitle, String keywords, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseGUIDLikeAndKeywordsLikeAndCourseStatusAllIgnoreCase(Collection<Long> ids, String courseGuid, String keywords, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTitleLikeAndCourseStatusAllIgnoreCase(Collection<Long> ids, String courseTitle, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseGUIDLikeAndCourseStatusAllIgnoreCase(Collection<Long> ids, String courseGuid, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndKeywordsLikeAndCourseStatusAllIgnoreCase(Collection<Long> ids, String keywords, String courseStatus);
	
	@EntityGraph(value = "Course.findByCourseStatusGraph", type = EntityGraphType.LOAD)
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000" ))
	Stream<Course> findByCourseStatus(String courseStatus);
	
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTitleLikeIgnoreCaseAndAndCourseTypeAndCourseTypeNotAndCourseStatusIgnoreCase(Collection<Long> ids, String courseTitle, String courseType, String legacyCourseType, String courseStatus);
	
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTitleLikeIgnoreCaseAndAndCourseTypeNotAndCourseStatusIgnoreCase(Collection<Long> ids, String courseTitle, String courseType, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTypeAndCourseTypeNotAndCourseStatusIgnoreCase(Collection<Long> ids, String courseType, String legacyCourseType, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdInAndCourseTypeNotAndCourseStatusIgnoreCase(Collection<Long> ids, String legacyCourseType, String courseStatus);
	
	@QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "1000"))
	Stream<Course> findByIdIn(Collection<Long> ids);
	
	List<Course> findByKeywordsAndCourseStatus(String keyWords, String courseStatus);
	
	/*@Query(value="SELECT ID , NAME , COURSETYPE  FROM COURSE WHERE NAME\n"
			+ " LIKE '%:name%' and COURSETYPE IN('"+ScormCourse.COURSE_TYPE+"','"+WebLinkCourse.COURSE_TYPE +"'\n"
			+ ",'"+DiscussionForumCourse.COURSE_TYPE+"','"+SynchronousCourse.COURSE_TYPE+"'\n"
			+ ",'"+WebinarCourse.COURSE_TYPE+"','"+HomeworkAssignmentCourse.COURSE_TYPE+"'\n"
			+ ",'"+InstructorConnectCourse.COURSE_TYPE+"','"+SelfPacedCourse.COURSE_TYPE+"')\n"
			+ "  and ID IN (SELECT COURSE_ID FROM ALERTTRIGGERFILTER_COURSE WHERE ALERTTRIGGERFILTER_ID = :alertTriggerFilterId)",nativeQuery=true)
	public List<Course> getCourseUnderAlertTriggerFilters(@Param("alertTriggerFilterId") Long alertTriggerFilterId ,@Param("name") String name);*/
	
	//@Query("SELECT id , courseType,courseTitle  FROM Course WHERE courseTitle like '%:name%' and courseType IN (:courseType) and id IN (:alertTriggerFilterId)")
	//public List<Course> findByNameLikeAndCourseTypeInAndIdIn(String name,String courseType,Long alertTriggerFilterId);
	
	public List<Course> findByBusinessKeyEquals(String businessKey);
	
	
	
	
}