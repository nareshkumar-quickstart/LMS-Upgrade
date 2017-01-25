package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Distributor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CourseGroupRepositoryTest {

	@Inject
	private CourseGroupRepository courseGroupRepository;

	@Inject
	private CourseRepository courseRepository;

	@Inject
	private DistributorRepository distributorRepository;
	
	//@Test
	public void CourseGroup_Should_find_By_ContentOwner () {
		
		try{
			List<CourseGroup> cg = this.courseGroupRepository.findByContentOwnerId(2173L);
				System.out.println(cg.size());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	
	//@Test
	public void CourseGroup_Should_find_By_CourseId_And_Name () {
		
		try{
			List<CourseGroup> cg = this.courseGroupRepository.findByCoursesIdAndNameIgnoreCaseLike(54L, "%Testing Azhar CG 2%");
			for(CourseGroup courseGroup: cg)	
				System.out.println(courseGroup.getId());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	//@Test
	public void CourseGroup_Should_find_By_GUID () {
		
		try{
			CourseGroup cg = this.courseGroupRepository.findByGuid("55710CF8D3A94BCAA84DB1CA29D3F681");
			//for(CourseGroup courseGroup: cg)	
				System.out.println(cg.getId());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	//@Test
	public void CourseGroup_Should_find_By_CourseGroupID () {
		
		try{
			String[] ids = {"red color","color cg"}; // new String[2];
			
			List<CourseGroup> cg = this.courseGroupRepository.findByCourseGroupIDIn(ids);
			for(CourseGroup courseGroup: cg)	
				System.out.println(courseGroup.getId());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	
	//@Test
	public void CourseGroup_with_Parent_Should_find_By_CourseGroupID () {
		
		try{
			//2434,2439
			
			Set<Long> groupList = new HashSet<Long>();
			
			groupList.add(2439L);
			groupList.add(2434L);
			List<Long> l = new ArrayList<Long>();
			l.add(2439L);
			l.add(2434L);
		//	Long[] cg = new Long[2];
		//	cg[0] = 2439L;
		//	cg[1] = 2434L;
			List<Object[]> rMap =  this.courseGroupRepository.getAllParentCourseGroups(groupList);// courseAndCourseGroupDAO.getAllParentCourseGroups(groupList.toArray());
			
			for (Object[] objMap : rMap) {
				Long courseGroupid =  new Long((Integer)objMap[0]); //new Long((Integer)objMap.get("CGID"));
				String courseGroupName =  (String)objMap[2];  //(String)objMap.get("CGNAME");
				Long parentCourseGroupId = null;
				//if(objMap.get("PARENTCGID")!=null){
				if(objMap[1]!=null){
					parentCourseGroupId =  new Long((Integer)objMap[1]); //new Long((Integer)objMap.get("PARENTCGID"));
				}
			}
		//	String[] ids = {"red color","color cg"}; // new String[2];
		//	List<Map<Object, Object>>  rMap = this.courseGroupRepository.getAllParentCourseGroups();
		//	for (Map objMap : rMap) {
		//		System.out.println((Integer)objMap.get("CGID"));
		//	}
//			List<CourseGroup> cg = this.courseGroupRepository.findByCourseGroupIDIn(ids);
//			for(CourseGroup courseGroup: cg)	
//				System.out.println(courseGroup.getId());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	
	//@Test
	public void CourseGroup_Should_find_By_Courses_CourseGUID () {
		
		try{
		//	String[] ids = {"red color","color cg"}; // new String[2];
			String[] courseGuid = new String[3];
			
			courseGuid[0] = "18d416df0f62405c99a694ac52f13781";
			courseGuid[1] = "7400551CAFC7496FA72C079C910680DA";
			courseGuid[2] = "B3D74D75CDFA459587A3F2864E729A1E";
			List<CourseGroup>  cgl = this.courseGroupRepository.findByCoursesCourseGUIDIn(courseGuid);
			for (CourseGroup cg : cgl) {
				System.out.println(cg.getName());
			}
//			List<CourseGroup> cg = this.courseGroupRepository.findByCourseGroupIDIn(ids);
//			for(CourseGroup courseGroup: cg)	
//				System.out.println(courseGroup.getId());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	
	}
	
	//Added By Marium Saud
	//@Test
	public void CourseGroup_should_getCourseGroups_By_ContentOwner(){
		RepositorySpecificationsBuilder<CourseGroup> builder =new RepositorySpecificationsBuilder<CourseGroup>();
		builder.with("contentOwner_id", builder.JOIN_EQUALS, 2169L, "AND");
		builder.with("name", builder.LIKE_IGNORE_CASE, "CG", "AND");
		builder.with("keywords", builder.LIKE_IGNORE_CASE,"", "AND");
		List<CourseGroup>  cgl = courseGroupRepository.findAll(builder.build());
		for (CourseGroup cg : cgl) {
			System.out.println(cg.getName());
		}
	}
	
	//@Test
	public void findAvailableCourseGroups(){
		Distributor obj = new Distributor();
		obj.setId(153L);
		List<Map<Object, Object>> lst = courseGroupRepository.findByAvailableCourseGroups(obj, "nested", "NCG", "TRS,LCMS-6099");
		
		for(Map map : lst){
			System.out.println(map.get("sdfr"));
		}
	}
	
	//@Test
	public void findAvailableCourseGroups2(){
		Distributor obj = new Distributor();
		obj.setId(153L);
		List<Long> ids = new ArrayList();
		ids.add(4L);
		
		List<Map<Object, Object>> lst = courseGroupRepository.findByAvailableCourseGroups(obj, ids);
		
		for(Map map : lst){
			System.out.println(map.get("sdfr"));
		}
	}
	
//	@Test
	public void getCourseGroupPath(){
		
		String s = courseGroupRepository.getCourseGroupPath(new Long(2439));
		System.out.println(s);
		
		//List<Map<Object, Object>> lst = courseGroupRepository.findByAvailableCourseGroups(obj, ids);
		
		
	}
	
	// @Test
	public void findByName() {

		List<CourseGroup> courseGroupList = courseGroupRepository
				.findByNameContaining("my");
		if (courseGroupList != null && courseGroupList.size() > 0) {
			for (CourseGroup courseGroup : courseGroupList) {
				System.out.println(courseGroup.getGuid());
			}
		}
	}

	// @Test
	public void findByIdInAndNameContaining() {

		long idArray[] = { 13, 2757, 26 };
		List<CourseGroup> crs1 = courseGroupRepository
				.findByIdInAndNameContaining(idArray, "0417");
		if (crs1 != null && crs1.size() > 0) {
			for (CourseGroup crs : crs1) {
				System.out.println(crs.getGuid());
			}
		}
	}

	// @Test
	public void getCourseGroupsByDistributor() {
		List<CourseGroup> crs1 = courseGroupRepository
				.getCourseGroupsByDistributor(153l);
		if (crs1 != null && crs1.size() > 0) {
			for (CourseGroup cG : crs1) {
				System.out.println(cG.getGuid());
			}
		}
	}

	//@Test
	public void getAllChildCourseGroupsForCourseGroups() {
		List<CourseGroup> cGList = new ArrayList<CourseGroup>();
		CourseGroup cG1 = courseGroupRepository
				.findByGuid("0000F843AFFB4AD39003E470FB2E5224");
		CourseGroup cG2 = courseGroupRepository
				.findByGuid("000BF00CF4324E8C92416433615C3802");
		cGList.add(cG1);
		cGList.add(cG2);
		List<CourseGroup> childList = courseGroupRepository
				.getAllChildCourseGroupsForCourseGroups(cGList);
		if (childList != null && childList.size() > 0) {
			for (CourseGroup cGroup : childList) {
				System.out.println(cGroup.getKey());
			}
		}
	}

//	@Test
	public void findCoursesAndCourseGroupsByDistributor() {
		Distributor distributor = distributorRepository.findOne(3l);
		List<CourseGroup> cGList = courseGroupRepository
				.findCoursesAndCourseGroupsByDistributor(distributor, "course",
						"1", "my", "permenant");
		if(cGList!=null && cGList.size()>0){
			for(CourseGroup cG:cGList){
				System.out.println(cG.getGuid());
			}
		}

	}
	
	@Test
	public void CourseGroup_should_findCourseGroups(){
		List<Map<Object, Object>> lst = courseGroupRepository.findCourseGroups("NestedCourseGroup", "NCG", "");
		for(Map map : lst){
			System.out.println(map.get("COURSEGROUP_ID"));
		}
	}
	
}
