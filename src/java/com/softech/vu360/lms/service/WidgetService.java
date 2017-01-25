package com.softech.vu360.lms.service;

import java.util.List;

import com.softech.vu360.lms.model.Widget;

public interface WidgetService {
	public List<Widget> getAllWidget();
	public Widget getWidgetById(Long id);
	public Widget save(Widget widget);
	public Widget update(Widget widget);
	public void delete(Widget widget);
}

