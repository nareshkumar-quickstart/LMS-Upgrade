package com.softech.vu360.lms.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Widget;
import com.softech.vu360.lms.repositories.WidgetRepository;
import com.softech.vu360.lms.service.WidgetService;

public class WidgetServiceImpl implements WidgetService {

	@Inject
	private WidgetRepository widgetRepository;

	@Override
	public List<Widget> getAllWidget() {
		return (List<Widget>) widgetRepository.findAll();
	}

	@Override
	public Widget getWidgetById(Long id) {
		return widgetRepository.findOne(id);
	}

	@Override
	@Transactional
	public Widget save(Widget widget) {
		return widgetRepository.save(widget);
	}

	@Override
	public Widget update(Widget widget) {
		return save(widget);
	}

	@Override
	@Transactional
	public void delete(Widget widget) {
		widgetRepository.delete(widget);
	}

}
