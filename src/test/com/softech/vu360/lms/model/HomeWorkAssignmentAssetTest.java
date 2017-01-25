package com.softech.vu360.lms.model;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Before;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class HomeWorkAssignmentAssetTest extends TestBaseDAO<HomeWorkAssignmentAsset> {


	@Before
	public void setRequiredService() {
	}

	//@Test
	public void HomeWorkAssignmentAsset_should_save() throws Exception {

		HomeWorkAssignmentAsset homeworkAssignmentAsset = new HomeWorkAssignmentAsset();
		// Adding Course Objects type of HomeWorkAssignmentCourse
		HomeworkAssignmentCourse course = new HomeworkAssignmentCourse();
		course.setName("Test_COurse_HomeWork1");
		course.setCourseStatus("Not Started1");
		course.setAssignmentDueDate(new Date());
		course.setGradingMethod("Test_Grading_Method1");
		course.setMasteryScore(new Double(100));
		course.setCourseTitle("Test_CourseTitle1");

		// Setting CoursePlayerType
		CoursePlayerType playerType = new CoursePlayerType();
		playerType.setCourse(course);
		playerType.setDescription("Test_Description1");
		playerType.setPlayerVersion("Test_PlayerVersion1");
		//course.setCoursePlayerType(playerType);

		// Setting AICCAssignableUnit
		AICCAssignableUnit unit = new AICCAssignableUnit();
		unit.setCommandLine("Test_CommandLine1");
		unit.setCreatedDate(new Date());
		Course courseForAICC = new Course();
		courseForAICC.setCourseTitle("TestCourse_AICC1");
		courseForAICC.setCourseStatus("Initiated1");
		unit.setCourse(courseForAICC);
		//course.setAICCAssignableUnit(unit);

		// Setting CourseConfigurationTemplate (One mapping is missing)
		CourseConfigurationTemplate template = new CourseConfigurationTemplate();
		template.setActive(true);
		template.setName("Test_Template1");
		// Setting content Owner
		ContentOwner contentOwnerforTemplate = new ContentOwner();
		contentOwnerforTemplate.setCurrentAuthorCount(new Long(3));
		contentOwnerforTemplate.setCurrentCourseCount(new Long(3));
		contentOwnerforTemplate
				.setName("Test_Owner for contentOwnerforTemplat1e");
		template.setContentOwner(contentOwnerforTemplate);
		course.setCourseConfigTemplate(template);

		// Setting Asset object of type doc
		Document doc = new Document();
		doc.setDescription("Test_Description2");
		doc.setActive(true);
		doc.setFileName("Test_FileName2");
		doc.setNoOfDocumentsPerPage(2);
		doc.setName("Test_Doc_Homework Assignment1");
		// Setting content owner for asset
		ContentOwner contentOwner = new ContentOwner();
		contentOwner.setCurrentAuthorCount(new Long(3));
		contentOwner.setCurrentCourseCount(new Long(3));
		contentOwner.setName("Test_Owner for Home Work Assignment1");
		doc.setContentowner(contentOwner);

		// Setting objects
		homeworkAssignmentAsset.setAsset(doc);
		homeworkAssignmentAsset.setHomeWorkAssignmentCourse(course);
		save(homeworkAssignmentAsset);

	}

//	@Test
	public void HomeWorkAssignmentAsset_should_update() throws Exception {

		HomeWorkAssignmentAsset homeworkAssignmentAssetUpdate = getById(new Long(1262), HomeWorkAssignmentAsset.class);
		homeworkAssignmentAssetUpdate.setAsset(null);
		homeworkAssignmentAssetUpdate.getHomeWorkAssignmentCourse().setCourseTitle("Updated_Title");
		update(homeworkAssignmentAssetUpdate);
		

	}

	
}
