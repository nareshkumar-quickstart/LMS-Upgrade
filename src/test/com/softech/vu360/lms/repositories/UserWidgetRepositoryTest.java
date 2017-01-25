package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.UserWidget;
import com.softech.vu360.lms.model.Widget;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class UserWidgetRepositoryTest {

	@Inject
	private UserWidgetRepository userWidgetRepository;
	@Inject
	private WidgetRepository widgetRepository;
	@Inject
	private VU360UserRepository userRepository;
	
	//@Test
	public void findWidgetById(){
		Widget entity=widgetRepository.findOne(4L);
		System.out.println(entity.getTitle());
	}
	
	//@Test
	public void findByWidgetIdAndUserId(){
		Long widgetId=4L;
		Long userId=182483L;
		
		UserWidget entity=userWidgetRepository.findByWidgetIdAndUserId(widgetId, userId);
		System.out.println(entity.getWidget().getId());
		
	} 

	//@Test
	public void findByUserId(){
		Long userId=182483L;
		
		List<UserWidget> entity=userWidgetRepository.findByUserId(userId);
		System.out.println(entity.get(0).getWidget().getId());
		
	} 
	
	@Test
	public void save(){
		Long userId=182483L;
		
		UserWidget userWidget = new UserWidget();
		userWidget.setUser(userRepository.findOne(userId));
		userWidget.setWidget(widgetRepository.findOne(4L));
		userWidget.setPosition(1L);
		userWidgetRepository.saveUserWidget(userWidget);
		
	} 
}
