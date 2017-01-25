package com.softech.vu360.lms.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.UserWidget;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.Widget;
import com.softech.vu360.lms.repositories.UserWidgetRepository;
import com.softech.vu360.lms.service.UserWidgetService;

public class UserWidgetServiceImpl implements UserWidgetService {

	@Inject
	private UserWidgetRepository userWidgetRepository;
	
	@Override
	public UserWidget update(UserWidget userWidget) {
		return save(userWidget);
	}

	@Override
	@Transactional
	public void delete(UserWidget userWidget) {
		userWidgetRepository.delete(userWidget);
	}

	@Override
	public UserWidget getUserWidgetByWidgetId(Long widgetId, Long userId) {
		return userWidgetRepository.findByWidgetIdAndUserId(widgetId, userId);
	}
	@Override
	public List<UserWidget> getUserWidgetsByUser(Long userId) {
		return userWidgetRepository.findByUserId(userId);
	}
	
	@Override
	@Transactional
	public void addUserWidget(VU360User user, Widget widget, long zoneId) {
		UserWidget userWidget = new UserWidget();
		userWidget.setUser(user);
		userWidget.setWidget(widget);
		userWidget.setPosition(zoneId);
		userWidget = save(userWidget);
	}

	@Override
	@Transactional
	public UserWidget save(UserWidget userWidget) {
		userWidget = userWidgetRepository.saveUserWidget(userWidget);
		return userWidget;
	}
}
