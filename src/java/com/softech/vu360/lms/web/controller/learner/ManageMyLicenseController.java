package com.softech.vu360.lms.web.controller.learner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.lms.service.LearnerLicenseService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageLicenseForm;
import com.softech.vu360.lms.web.controller.validator.ManageLicenseValidator;
import com.softech.vu360.util.LearnerOfLicenseSort;

/**
 * 
 * @author haider.ali
 *
 */
public class ManageMyLicenseController extends VU360BaseMultiActionController{

	
    private String displayManageLicenseTemplate = StringUtils.EMPTY;
    private String displayManageLicenseAlertTemplate = StringUtils.EMPTY;

    private static Logger log = Logger.getLogger(ManageMyLicenseController.class.getName());
    private LearnerLicenseService learnerLicenseServices =null;
    private LearnerLicenseAlertService learnerLicenseAlertService =null;

    

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<Object,Object> context = new HashMap<Object,Object>();
        context.put("target", "displayt");

        return new ModelAndView(displayManageLicenseTemplate, "context",context);	
		
	}
	

	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView displayManageLicenseAlert(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		ManageLicenseForm form = (ManageLicenseForm) command;
		Map<Object,Object> context = new HashMap<Object,Object>();
		List<LearnerLicenseAlert> alertlist = null;
		
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String alertname = (request.getParameter("alertName") == null) ? "" : request.getParameter("alertName");
		if(!alertname.equals(""))
		{
			alertlist = learnerLicenseAlertService.getFilteredLearnerLicenseAlert(user.getLearner().getId(),alertname);
		}
		else if(alertname.equals(""))
		{
		    alertlist = learnerLicenseAlertService.getLearnerLicenseAlert(user.getLearner().getId());
		}  
		
		

		HttpSession session = request.getSession();

		//============================For Sorting============================
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
		Map<String,Object> pagerAttributeMap = new HashMap<String,Object>();
		pagerAttributeMap.put("pageIndex",pageIndex);

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					//Collections.sort(alertlist, sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					//Collections.sort(alertlist, sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			}

		}	

		//form.setLicenseOfLearner(list);
		form.setLicenseOfLearnerAlert(alertlist);
		//request.setAttribute("myPageSize", 5);

		return new ModelAndView(displayManageLicenseAlertTemplate, "context", context);	
	}
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param command
	 * @param errors
	 * @return
	 * @throws Exception
	 */
	public ModelAndView displayManageLicense(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		ManageLicenseForm form = (ManageLicenseForm) command;
		Map<Object,Object> context = new HashMap<Object,Object>();
		List<LicenseOfLearner> list = null;
		
		com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String licensename = (request.getParameter("licenseName") == null) ? "" : request.getParameter("licenseName");
		//List<LicenseOfLearner> list = learnerLicenseServices.getAllLicensesOfLearner(user.getLearner().getId());
		if(!licensename.equals(""))
		{
		  list = learnerLicenseServices.getFilteredLicensesOfLearner(user.getLearner().getId(),licensename);
		}
		else if(licensename.equals(""))
		{
			list = learnerLicenseServices.getAllLicensesOfLearner(user.getLearner().getId());
		}


		HttpSession session = request.getSession();

		//============================For Sorting============================
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
		Map<String,Object> pagerAttributeMap = new HashMap<String,Object>();
		pagerAttributeMap.put("pageIndex",pageIndex);

		if( sortColumnIndex != null && sortDirection != null ) {

			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(list, sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseType");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(list, sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			}
			if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseExpiration");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(list, sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					LearnerOfLicenseSort sort = new LearnerOfLicenseSort();
					sort.setSortBy("licenseExpiration");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(list, sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			} 

		}	

		form.setLicenseOfLearner(list);
		//request.setAttribute("myPageSize", 5);

		return new ModelAndView(displayManageLicenseTemplate, "context", context);	
		
	}

	public ModelAndView deleteLicense(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		if(errors.hasErrors()){
			return displayManageLicense(request,response,command,errors);
		}
		
		ManageLicenseForm form = (ManageLicenseForm) command;
		
		
		List<LicenseOfLearner> list = learnerLicenseServices.getLicenseOfLearner(form.getLicenseOfLearnerId());
		
		List<LicenseOfLearner> list1 = learnerLicenseServices.deleteLicenseOfLearner(list);
		
		return displayManageLicense(request,response,command,errors);
		
	}	
	
	public ModelAndView deleteAlertLicense(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		ManageLicenseForm form = (ManageLicenseForm) command;
		
		
		List<LearnerLicenseAlert> list = learnerLicenseAlertService.getLearnerLicenseAlert(form.getLicenseAlertId());
		
		List<LearnerLicenseAlert> list1 = learnerLicenseAlertService.deleteLearnerLicenseAlert(list);
		
		return displayManageLicenseAlert(request,response,command,errors);
		
	}	
	
	public String getDisplayManageLicenseTemplate() {
		return displayManageLicenseTemplate;
	}

	public void setDisplayManageLicenseTemplate(String displayManageLicenseTemplate) {
		this.displayManageLicenseTemplate = displayManageLicenseTemplate;
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		
		if(methodName.equals("deleteLicense"))
		{
			ManageLicenseValidator validator = (ManageLicenseValidator)this.getValidator();
			ManageLicenseForm form = (ManageLicenseForm)command;
			errors.setNestedPath("");
			
			validator.validate(command, errors);
		
		//super.validate(command, errors);
		}	
	}

	public LearnerLicenseService getLearnerLicenseServices() {
		return learnerLicenseServices;
	}

	public void setLearnerLicenseServices(
			LearnerLicenseService learnerLicenseServices) {
		this.learnerLicenseServices = learnerLicenseServices;
	}

	public LearnerLicenseAlertService getLearnerLicenseAlertService() {
		return learnerLicenseAlertService;
	}

	public void setLearnerLicenseAlertService(
			LearnerLicenseAlertService learnerLicenseAlertService) {
		this.learnerLicenseAlertService = learnerLicenseAlertService;
	}



	public String getDisplayManageLicenseAlertTemplate() {
		return displayManageLicenseAlertTemplate;
	}


	public void setDisplayManageLicenseAlertTemplate(
			String displayManageLicenseAlertTemplate) {
		this.displayManageLicenseAlertTemplate = displayManageLicenseAlertTemplate;
	}
	
	
}
