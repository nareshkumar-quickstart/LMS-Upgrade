package com.softech.vu360.lms.widget;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.vo.WidgetData;

public interface WidgetLogic {
	public Collection<WidgetData> getWidgetDataList(VU360User vu360User, Map<String, Object> params, HttpServletRequest request);
}
