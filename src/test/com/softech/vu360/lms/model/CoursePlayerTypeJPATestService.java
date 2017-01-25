package com.softech.vu360.lms.model;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

/*
 * @author kaunain.wajeeh
 */

public class CoursePlayerTypeJPATestService extends TestBaseDAO<CoursePlayerType> {

	@Test
	public void getAllTemplates() throws Exception {

		@SuppressWarnings("unchecked")
		List<CoursePlayerType> templateList = getAll("CoursePlayerType",
				CoursePlayerType.class);
		for (CoursePlayerType mt : templateList) {
			assertNotNull(mt);
		}
	}

	@Test
	public void testPersist() {

		CoursePlayerType template = new CoursePlayerType();
		@SuppressWarnings("unchecked")
		List<Course> courseList=this.getAll("Course", Course.class);
		template.setCourse(courseList.get(0));
		template.setDescription("TestDescription");
		template.setPlayerVersion("TestVersion");
		CoursePlayerType msg = (CoursePlayerType) crudSave(CoursePlayerType.class,
				template);
		assertNotNull(msg);

	}

	@Test
	public void testUpdate() {
		CoursePlayerType template = this.getById(1l, CoursePlayerType.class);
		template.setDescription("TestUpdatedDescription");
		template = update(template);
		assertNotNull(template);
	}

}
