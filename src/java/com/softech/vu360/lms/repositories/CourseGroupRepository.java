package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.CourseGroup;

public interface CourseGroupRepository  extends CrudRepository<CourseGroup, Long> , JpaSpecificationExecutor<CourseGroup>,CourseGroupRepositoryCustom {

	//private List<CourseGroup> getCourseGroupsByIdFromDB(long[] subArray)
	List<CourseGroup> findByIdIn(Long[] CourseGroupId);
	
	//public List<CourseGroup> getAllCourseGroupsByContentOwnerId
	List<CourseGroup> findByContentOwnerId(Long ContentOwnerId);
	
	//public List<CourseGroup> getAllCourseGroupsByCourseId( Long varCourseId, String varCourseGroupName) 
	 List<CourseGroup> findByCoursesIdAndNameIgnoreCaseLike( Long CourseId, String CourseGroupName);

	 //CourseGroup getCourseGroupByguid(String guid)
	 CourseGroup findByGuid(String guid);
	
	 //List<CourseGroup> getCourseGroupsByBusinessKeys(String[] businessKeys) 
	 List<CourseGroup> findByCourseGroupIDIn(String[] businessKeys); 
	 
	 // List<Map<Object, Object>> getAllParentCourseGroups(Object[] courseGroupidArray)
//	 @Query(value = "Select c.* from Course c, course_CourseGroup ccg " +
//				"where ccg.courseGroup_id = :CourseGroupId and c.id = ccg.course_id " +
//				" and RETIREDTF = 0 and COURSESTATUS = 'Published'", nativeQuery=true)
	
	 @Query(value = " WITH CTE AS ( select cg.id as CGID , cg.parentcoursegroup_id as PARENTCGID, cg.name as CGNAME from coursegroup cg where cg.id in ( :CourseGroupIds ) " +
				    " union all " +
				    " select cgp.id as CGID, cgp.parentcoursegroup_id as PARENTCGID, cgp.name as CGNAME from coursegroup cgp " +
				    " inner join cte cg_cte on cgp.id = cg_cte.PARENTCGID ) " +
				    " select CGID,PARENTCGID,CGNAME from CTE ", nativeQuery=true) 
	 List<Object[]>  getAllParentCourseGroups(@Param("CourseGroupIds") Set<Long> CourseGroupIds);
//	 List<Object[]> getAllParentCourseGroups();
	 
	 List<CourseGroup> findByCoursesCourseGUIDIn(String[] courseGUIDs); 
	 List<CourseGroup> findByCoursesId( Long CourseId);
	 
	 @Query(value = " select dbo.GetCourseGroupHierarchy (:CourseGroupId) as CoursePath ", nativeQuery=true) 
	 String  getCourseGroupPath(@Param("CourseGroupId") Long CourseGroupId);
	 
	 List<CourseGroup> findByNameContaining(String courseTitle);
	 List<CourseGroup> findByIdInAndNameContaining(long ids[], String courseGroupName);
	 
}
