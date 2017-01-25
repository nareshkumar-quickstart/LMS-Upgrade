package com.softech.vu360.lms.web.controller.learner;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360ReportExecutionSummary;
import com.softech.vu360.lms.service.ReportExecutionService;
import com.softech.vu360.lms.service.ReportingConfigurationService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ReportForm;
import com.softech.vu360.util.RedirectUtils;

public class LearnerReportController extends VU360BaseMultiActionController{

	private ReportingConfigurationService reportingConfigurationService;
	private ReportExecutionService reportExecutionService;
	private String redirectTemplate;
	private String summaryTemplate;
	private String htmlViewTemplate;

	public LearnerReportController() {
		super();
	}
	public LearnerReportController(Object delegate) {
		super(delegate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		ReportForm form = (ReportForm)command;
		com.softech.vu360.lms.vo.VU360User owner = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(form.getOwnedReports()==null || (form.getOwnedReports()!=null && form.getOwnedReports().size()<=0)){
			List<VU360Report> ownedReports = reportingConfigurationService.getOwnedReports(owner.getId(),"Learner");
			form.setOwnedReports(ownedReports);
			if(ownedReports!=null && ownedReports.size()>0){
				form.setCurrentReport(ownedReports.get(0));
			}
		}
	}

	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map context = new HashMap();
		context.put("target", "browseReports");
		ModelAndView view = new ModelAndView(redirectTemplate,"context",context); 
		return view;
	}
	
	public void globalExceptionHandler(HttpServletRequest request, HttpServletResponse response,  Exception ex) throws IOException{
		String url = "/lrn_ViewReports.do?method=browseReports";
		RedirectUtils.sendRedirect(request, response, url, false);
	}
	
	//default landing page for the report user
	public ModelAndView browseReports(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		//setup the current report last execution summary in form object
//		ReportForm form = (ReportForm)command;
//		VU360Report report = form.getCurrentReport();
//		if(report!=null){
//			VU360ReportExecutionSummary executionSummary = reportingConfigurationService.lastExecutionSummary(report);
//			form.setCurrentReportLastExecutionSummary(executionSummary);
//		}
		
		//forward to the view template
		ModelAndView view = new ModelAndView(summaryTemplate); 
		return view;
	}
	
	//display the default summary page with a report selected
	public ModelAndView selectReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		//setup the current report from form
		String strSelectedReportId = request.getParameter("reportId");
		ReportForm form = (ReportForm)command;
		if(!StringUtils.isBlank(strSelectedReportId)){
			try{
				Long selectedReportId = Long.parseLong(strSelectedReportId);
				List<VU360Report> reportList = form.getOwnedReports();
				if(reportList!=null && reportList.size()>0){
					for(VU360Report report:reportList){
						if(report.getId().longValue() == selectedReportId){
							form.setCurrentReport(report);
							break;
						}
					}
				}
			}catch(NumberFormatException ne){
				ne.printStackTrace();
			}
		}
		return this.browseReports(request, response, command, errors);
	}
	
	//execute the selected report
	public ModelAndView executeReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		ReportForm form = (ReportForm)command;
		VU360Report report = form.getCurrentReport();
		if( report == null ) {
			throw new Exception("current report not selected");
		}
		com.softech.vu360.lms.vo.VU360User owner = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		reportExecutionService.executeReport(report, owner);
		
		VU360ReportExecutionSummary executionSummary = reportingConfigurationService.lastExecutionSummary(report,owner.getId());
		form.setCurrentReportLastExecutionSummary(executionSummary);

		String htmlData="";
		String filePath="";

		String filename = form.getCurrentReportLastExecutionSummary().getHtmlLocation();
		try{
			htmlData=FileCopyUtils.copyToString(new FileReader(filePath+filename));
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return new ModelAndView(htmlViewTemplate,"htmlData",htmlData); 

	}
	
	/**
	 * @return the htmlViewTemplate
	 */
	public String getHtmlViewTemplate() {
		return htmlViewTemplate;
	}
	/**
	 * @param htmlViewTemplate the htmlViewTemplate to set
	 */
	public void setHtmlViewTemplate(String htmlViewTemplate) {
		this.htmlViewTemplate = htmlViewTemplate;
	}
	/**
	 * @return the redirectTemplate
	 */
	public String getRedirectTemplate() {
		return redirectTemplate;
	}
	/**
	 * @param redirectTemplate the redirectTemplate to set
	 */
	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}
	/**
	 * @return the reportExecutionService
	 */
	public ReportExecutionService getReportExecutionService() {
		return reportExecutionService;
	}
	/**
	 * @param reportExecutionService the reportExecutionService to set
	 */
	public void setReportExecutionService(
			ReportExecutionService reportExecutionService) {
		this.reportExecutionService = reportExecutionService;
	}
	/**
	 * @return the reportingConfigurationService
	 */
	public ReportingConfigurationService getReportingConfigurationService() {
		return reportingConfigurationService;
	}
	/**
	 * @param reportingConfigurationService the reportingConfigurationService to set
	 */
	public void setReportingConfigurationService(
			ReportingConfigurationService reportingConfigurationService) {
		this.reportingConfigurationService = reportingConfigurationService;
	}
	/**
	 * @return the summaryTemplate
	 */
	public String getSummaryTemplate() {
		return summaryTemplate;
	}
	/**
	 * @param summaryTemplate the summaryTemplate to set
	 */
	public void setSummaryTemplate(String summaryTemplate) {
		this.summaryTemplate = summaryTemplate;
	}

	
	
	
}
