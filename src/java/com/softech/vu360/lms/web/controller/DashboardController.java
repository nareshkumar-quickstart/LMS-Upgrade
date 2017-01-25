package com.softech.vu360.lms.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.UserWidget;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.Widget;
import com.softech.vu360.lms.service.UserWidgetService;
import com.softech.vu360.lms.service.WidgetService;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.vo.WidgetVO;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.widget.WidgetLogic;

public class DashboardController extends MultiActionController {
	
	protected static final Logger log = Logger.getLogger(DashboardController.class);
	protected static final String jsonViewName = "json";
	
	protected String dashBoardPage;
	protected WidgetService widgetService;
	protected UserWidgetService userWidgetService;
	
	public UserWidgetService getUserWidgetService() {
		return userWidgetService;
	}

	public void setUserWidgetService(UserWidgetService userWidgetService) {
		this.userWidgetService = userWidgetService;
	}

	public WidgetService getWidgetService() {
		return widgetService;
	}

	public void setWidgetService(WidgetService widgetService) {
		this.widgetService = widgetService;
	}

	public String getDashBoardPage() {
		return dashBoardPage;
	}

	public void setDashBoardPage(String dashBoardPage) {
		this.dashBoardPage = dashBoardPage;
	}

	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex,HttpServletRequest request, HttpServletResponse response) throws Exception {
		return renderDashboard(request, response);
	}
	
	public ModelAndView renderDashboard(HttpServletRequest request, HttpServletResponse response) {
		log.debug("in renderDashboard");
		ModelAndView modelAndView = new ModelAndView(dashBoardPage);
		Map<String, Object> context = new HashMap<String, Object>();
		
		modelAndView.addObject("context", context);
		return modelAndView;
	}
	
	public ModelAndView getAvailableWidgetList(HttpServletRequest request, HttpServletResponse response) {
		log.debug("in getAvailableWidgetList");
		ModelAndView modelAndView = new ModelAndView(jsonViewName);
		
		List<Widget> widgets = widgetService.getAllWidget();
//		List<UserWidget> userWidgets = getCurrentUser().getUserWidget();
		List<UserWidget> userWidgets = userWidgetService.getUserWidgetsByUser(getPrincipalUser().getId());
		List<WidgetVO> vos = new ArrayList<WidgetVO>();
		userWidgets.size();
		for(Widget widget : widgets) {
			WidgetVO widgetVO = new WidgetVO();
			BeanUtils.copyProperties(widget, widgetVO);
			widgetVO.setZoneId(-1L);
			for (UserWidget userWidget : userWidgets) {
				if(widget.getId().longValue()==userWidget.getWidget().getId().longValue()) {
					widgetVO.setZoneId(userWidget.getPosition());
					break;
				}
			}
			widgetVO.setFilters(widget.getFilters());  //LMS-14316
			vos.add(widgetVO);
		}
		
		modelAndView.addObject("availableWidgets", vos);
		
		return modelAndView;
	}
	
	public ModelAndView getWidgetData(HttpServletRequest request, HttpServletResponse response) {
		log.debug("in getWidgetData");
		ModelAndView modelAndView = new ModelAndView(jsonViewName);
		
		Long widgetId = ServletRequestUtils.getLongParameter(request, "id", 0);
		Collection<WidgetData> datas = new ArrayList<WidgetData>();
		if(widgetId>0L) {
			Widget widget = widgetService.getWidgetById(widgetId);
			if(widget!=null && widget.getId()>0) {
				String expression = widget.getExpression();
				if(expression!=null && expression.trim().length()>0) {
					WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
					WidgetLogic widgetLogic = (WidgetLogic) springContext.getBean(expression);
					log.debug("got " + widgetLogic.getClass());
					Map<String, Object> params = new HashMap<String, Object>();
					Iterator iterator = request.getParameterMap().keySet().iterator();
					while (iterator.hasNext()) {
						String key = (String) iterator.next();
						String value = ((String[]) request.getParameterMap().get(key))[0];
						params.put(key, value);
					}
					datas = widgetLogic.getWidgetDataList(VU360UserAuthenticationDetails.getCurrentUser(), params, request);
				}
			}
		}
		modelAndView.addObject("data", datas);
		modelAndView.addObject("widgetId", widgetId);
		
		return modelAndView;
	}
	
	public ModelAndView addDashboardWidget(HttpServletRequest request, HttpServletResponse response) {
		log.debug("in addDashboardWidget");
		ModelAndView modelAndView = new ModelAndView(jsonViewName);
		
		Long widgetId = ServletRequestUtils.getLongParameter(request, "id", 0);
		Long zoneId = ServletRequestUtils.getLongParameter(request, "zoneId", 0);
		
		if(widgetId.longValue()>0 && zoneId.longValue()>0) {
			VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
			//Widget widget = widgetService.getWidgetById(widgetId);
			Widget widget = new Widget();
			widget.setId(widgetId);
			UserWidget userWidget = new UserWidget();
			userWidget.setUser(user);
			userWidget.setWidget(widget);
			userWidget.setPosition(zoneId);
			userWidget = userWidgetService.save(userWidget);
			modelAndView.addObject("response", "OK");
			modelAndView.addObject("responseCode", "200");
		} else {
			modelAndView.addObject("response", "not a valid id or zoneId");
			modelAndView.addObject("responseCode", "-100");
		}
		
		return modelAndView;
	}
	
	public ModelAndView closeWidget(HttpServletRequest request, HttpServletResponse response) {
		log.debug("in closeWidget");
		ModelAndView modelAndView = new ModelAndView(jsonViewName);
		
		Long widgetId = ServletRequestUtils.getLongParameter(request, "id", 0);
		
		if(widgetId.longValue()>0) {
			com.softech.vu360.lms.vo.VU360User user = getPrincipalUser();
			UserWidget userWidget = userWidgetService.getUserWidgetByWidgetId(widgetId, user.getId());
			if(userWidget!=null) {
				userWidgetService.delete(userWidget);
			}
			modelAndView.addObject("response", "OK");
			modelAndView.addObject("responseCode", "200");
		} else {
			modelAndView.addObject("response", "not a valid id");
			modelAndView.addObject("responseCode", "-100");
		}
		
		return modelAndView;
	}
	
	protected com.softech.vu360.lms.vo.VU360User getPrincipalUser() {
		return  (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
