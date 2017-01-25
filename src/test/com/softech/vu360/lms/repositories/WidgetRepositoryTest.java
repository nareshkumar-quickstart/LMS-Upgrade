package com.softech.vu360.lms.repositories;

import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.softech.vu360.lms.LmsTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class WidgetRepositoryTest {

	@Inject
	private WidgetRepository widgetRepository;
	
	@Test
	public void widgetRepository_should_findByTitle() {
		widgetRepository.findByTitle("Courses");
	}
	
}
