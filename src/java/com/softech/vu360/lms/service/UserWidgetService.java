package com.softech.vu360.lms.service;

import java.util.List;

import com.softech.vu360.lms.model.UserWidget;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.Widget;

public interface UserWidgetService {
	public UserWidget save(UserWidget userWidget);
	public UserWidget update(UserWidget userWidget);
	public void delete(UserWidget userWidget);
	public UserWidget getUserWidgetByWidgetId(Long widgetId, Long userId);
	public List<UserWidget> getUserWidgetsByUser(Long userId);
	public void addUserWidget(VU360User user, Widget widget, long zoneId);
}
