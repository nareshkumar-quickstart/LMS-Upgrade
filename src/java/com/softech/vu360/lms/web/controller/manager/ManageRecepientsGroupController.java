package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.MngAlert;
import com.softech.vu360.util.AlertSort;

public class ManageRecepientsGroupController extends VU360BaseMultiActionController{
	
	private String redirectTemplate;
	private String displayEditAlertManageRecepientTemplate;
	private HttpSession session = null;

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<Object,Object> context = new HashMap<Object,Object>();
		context.put("target", "showEditAlertManageRecepientPage");

		return new ModelAndView(redirectTemplate,"context",context);
	}
	
	public ModelAndView showEditAlertManageRecepientPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(displayEditAlertManageRecepientTemplate);		
		
	}
	public ModelAndView showRecepientGroupPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		return new ModelAndView(displayEditAlertManageRecepientTemplate);		
		
	}
	
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		List<Alert> alerts = new ArrayList<Alert>();
		List<MngAlert> mngAlerts = new ArrayList<MngAlert>();
		session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();

		for(Alert alert : alerts){
			MngAlert mngAlt =new MngAlert();
			mngAlt.setAlert(alert);
			//mngAlt.setRecipents(alert.getRecipents());
			mngAlerts.add(mngAlt);
		}

		//============================For Sorting============================
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		String sortColumnIndex = request.getParameter("sortColumnIndex");
		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
		if( sortDirection == null && session.getAttribute("sortDirection") != null )
			sortDirection = session.getAttribute("sortDirection").toString();
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) {
			if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
			else pageIndex = session.getAttribute("pageCurrIndex").toString();
		}

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AlertSort sort = new AlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					AlertSort sort = new AlertSort();
					sort.setSortBy("alertName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AlertSort sort = new AlertSort();
					sort.setSortBy("recipents");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					AlertSort sort = new AlertSort();
					sort.setSortBy("lastName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(mngAlerts,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			}
		}	

		//Set<SurveyResult> surveyResults = surveyService.getAllSurvey(surveyowner);
		context.put("alerts", alerts);
		// TODO
		return new ModelAndView("");
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getDisplayEditAlertManageRecepientTemplate() {
		return displayEditAlertManageRecepientTemplate;
	}

	public void setDisplayEditAlertManageRecepientTemplate(
			String displayEditAlertManageRecepientTemplate) {
		this.displayEditAlertManageRecepientTemplate = displayEditAlertManageRecepientTemplate;
	}
	
	
	
}