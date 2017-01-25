/**
 * 
 */
package com.softech.vu360.lms.web.controller.accreditation;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.exception.ReportNotExecutableException;
import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.TimeZone;
import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360ReportExecutionSummary;
import com.softech.vu360.lms.model.VU360ReportField;
import com.softech.vu360.lms.model.VU360ReportFilter;
import com.softech.vu360.lms.model.VU360ReportFilterOperand;
import com.softech.vu360.lms.model.VU360ReportFilterValue;
import com.softech.vu360.lms.model.VU360ReportParameter;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ReportExecutionService;
import com.softech.vu360.lms.service.ReportingConfigurationService;
import com.softech.vu360.lms.service.TimeZoneService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ReportFilterItem;
import com.softech.vu360.lms.web.controller.model.ReportForm;
import com.softech.vu360.lms.web.controller.validator.ReportConfigurationValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.RedirectUtils;

/**
 * @author Somnath
 *
 */
public class ReportController extends VU360BaseMultiActionController{

	private ReportingConfigurationService reportingConfigurationService;
	private ReportExecutionService reportExecutionService;
	private String redirectTemplate;
	private String summaryTemplate;
	private String fieldMenuTemplate;
	private String fieldSelectionTemplate;
	private String fieldOrderTemplate;
	private String fieldSortTemplate;
	private String editReportTemplate;
	private String filterReportTemplate;
	private String htmlViewTemplate;
	private String summaryReportForProctor;
	private String summaryReportForProctorConcurrentLnr;
	private final String SQLTEMPLATE_FOR_PROCTOR = "vm/reportsql/accr_RegulatorReportForProctor.vm";
	private final String SQLTEMPLATE_FOR_PROCTOR_CONCURRENT_LEARNER = "vm/reportsql/accr_RegulatorRptProctor_ConcurrentLnr.vm";
	private final String CSTTimeZoneInDB = "Central Standard Time - CST";
	
	private final String DEFAULT_START_DATE = "2001-01-01 00:00:00";
	DateFormat dfChanged = new SimpleDateFormat("yyyy-MM-dd");
	DateFormat dfActual = new SimpleDateFormat("MM/dd/yyyy");
	private TimeZoneService timeZoneService = null;
  
	

	Logger log=Logger.getLogger(ReportController.class);
	public ReportController() {
		super();
	}

	public ReportController(Object delegate) {
		super(delegate);
	}

	
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			CustomDateEditor editor = new CustomDateEditor(df, false);
			binder.registerCustomEditor(Date.class, editor);
			HttpSession ssn=request.getSession();
			if(StringUtils.isBlank(request.getParameter("fav"))){
				//Do Nothing. Only to avoid null condition
			}else if(request.getParameter("fav").toString().equals("true")){
				ssn.setAttribute("fav","true");
			}else{
				ssn.removeAttribute("fav");
			}
			super.initBinder(request, binder);
	}
	
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		ReportForm form = (ReportForm)command;
		com.softech.vu360.lms.vo.VU360User owner = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userMode = "Accreditation";
		
		HttpSession session = null;
		session = request.getSession();
		if(session.getAttribute("userIsProctor") == null && owner.getProctor() !=null)
			session.setAttribute("userIsProctor",owner.getProctor().getStatus());
			
		
		
		if(!form.getUserMode().equalsIgnoreCase(userMode)){
			form.setUserMode(userMode);
			List<VU360Report> ownedReports = reportingConfigurationService.getOwnedReports(owner.getId(),userMode);
			//List<VU360Report> ownedReports = reportingConfigurationService.getOwnedReports(owner);
			form.setOwnedReports(ownedReports);
			//set the current report as current report
			List<VU360Report> favourites = form.getFavouriteReports();
			if(favourites.size()>0)
				form.setCurrentReport(favourites.get(0));
			else{
				Map<String, ArrayList<VU360Report>> reportMap = form.getReportsByCategory();
				if(!reportMap.keySet().isEmpty()){
					while(reportMap.keySet().iterator().hasNext()){
						String category = reportMap.keySet().iterator().next();
						ArrayList<VU360Report> categoryReports = reportMap.get(category);
						if(categoryReports.size()>0){
							form.setCurrentReport(categoryReports.get(0));
							break;
						}
					}
				}
			}
		}
		
		if(methodName.equals("saveReportFilters")){
			String strHasFilters = request.getParameter("hasFilters");
			if(!StringUtils.isBlank(strHasFilters)){
				if(strHasFilters.equals("false")){
					form.getReportFilterItems().clear();
				}else{
					List<ReportFilterItem> filterItems = form.getReportFilterItems();
					
//					for(int i=filterItems.size()-1; i>=0; i--){
//						ReportFilterItem item = filterItems.get(i);
//						if(item.isMarkedForDeletion())
//							filterItems.remove(i);
//					}
					
				}
			}
		}
	}

	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		ReportForm form = (ReportForm)command;
		ReportConfigurationValidator reportConfigValidator = (ReportConfigurationValidator)this.getValidator();
		log.debug("methodName=>"+methodName);
		if(methodName.equals("saveReportSummary")){
			//TODO do the necessary validation on the edit object
			reportConfigValidator.validateSystemOwnedReport(form, errors);
			reportConfigValidator.validateEditReport(form, errors);
		}else if(methodName.equals("cloneReport")){
			//TODO do the necessary validation on the edit object
			//reportConfigValidator.validateSystemOwnedReport(form, errors);
			reportConfigValidator.validateEditReport(form, errors);
		}else if(methodName.equals("setFavouriteReport")){
			//TODO do the necessary validation on the edit object
			reportConfigValidator.validateSystemOwnedReport(form, errors);
		}else if(methodName.equals("saveReportFields")){
			//TODO do the necessary validation on the edit object
			reportConfigValidator.validateSystemOwnedReport(form, errors);
		}else if(methodName.equals("saveReportFieldsOrder")){
			//TODO do the necessary validation on the edit object
			reportConfigValidator.validateSystemOwnedReport(form, errors);
		}else if(methodName.equals("saveReportFieldsSortOrder")){
			//TODO do the necessary validation on the edit object
			reportConfigValidator.validateSystemOwnedReport(form, errors);
		}else if(methodName.equals("saveReportFilters")){
			//TODO do the necessary validation on the edit object
			reportConfigValidator.validateSystemOwnedReport(form, errors);
		}/*else if(methodName.equals("executeReport") && form.getCurrentReport().getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR)){
			//TODO do the necessary validation on the Proctor report
			reportConfigValidator.validateProctorReport(form, errors);
		}*/
		
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map context = new HashMap();
		context.put("target", "browseReports");
		ModelAndView view = new ModelAndView(redirectTemplate,"context",context); 
		return view;
	}

	public void globalExceptionHandler(HttpServletRequest request, HttpServletResponse response,  Exception ex) throws IOException{
		//Map context = new HashMap();
		//context.put("target", "browseReports");
		//ModelAndView view = new ModelAndView(redirectTemplate,"context",context); 
		//return view;

		String url = "/acc_ManageReports.do?method=browseReports";
		RedirectUtils.sendRedirect(request, response, url, false);
	}
	
	/**
	 * 1.
	 * The Reports Summary section...
	 * Contains the methods for edit/create report
	 * Also contains methods for executing reports
	 */
	//default landing page for the report user
	public ModelAndView browseReports(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		//setup the current report last execution summary in form object
		ReportForm form = (ReportForm)command;
		
		VU360Report report = form.getCurrentReport();
		if(report!=null){
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			VU360ReportExecutionSummary executionSummary = reportingConfigurationService.lastExecutionSummary(report,user.getId());
			form.setCurrentReportLastExecutionSummary(executionSummary);
		}
		
		//forward to the view template
		return this.getModelAndView(report);
	}
	
	//display the default summary page with a report selected
	public ModelAndView selectReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		//TODO setup the current report from form
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
			}catch(NumberFormatException ne){}
		}
		
		HttpSession session = request.getSession();
		session.setAttribute("errorOnProctorReportForSameDate", null);
		session.setAttribute("errorOnProctorReport", null);
		session.setAttribute("errorOnProctorReportCourseID", null);
		
		return this.browseReports(request, response, command, errors);
	}
	
	//set/unset the current report as favourite
	public ModelAndView setFavouriteReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report report = form.getCurrentReport();
		if(report==null)
			throw new Exception("current report not selected");
		
		reportingConfigurationService.changeReportFavouriteStatus(report, !report.isFavorite());
		return this.getModelAndView(report);
	}
	
	//display the edit summary page for a selected report
	public ModelAndView selectReportForEdit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		//setup the report object to be edited from current report
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		VU360Report edit_report = new VU360Report();
		edit_report.setId(curr_report.getId());
		edit_report.setCategory(curr_report.getCategory());
		edit_report.setTitle(curr_report.getTitle());
		edit_report.setDescription(curr_report.getDescription());
		form.setReportSummaryEdit(edit_report);
		
		return new ModelAndView(editReportTemplate);
	}
	
	//implementation of the **save as new** of a selected report 
	public ModelAndView cloneReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		if(errors.hasErrors())
			return new ModelAndView(editReportTemplate);
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		VU360Report edit_report = form.getReportSummaryEdit();
		com.softech.vu360.lms.vo.VU360User owner = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		VU360Report clonedReport = reportingConfigurationService.cloneReport(curr_report, edit_report.getTitle(),
				edit_report.getCategory(), edit_report.getDescription(), owner);
		
		form.addOwnedReport(clonedReport);
		form.setCurrentReport(clonedReport);
		
		form.setReportSummaryEdit(null);
		return this.getModelAndView(curr_report);
	}
	
	//save the edited report summary
	public ModelAndView saveReportSummary(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		if(errors.hasErrors())
			return new ModelAndView(editReportTemplate);
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		VU360Report edit_report = form.getReportSummaryEdit();
		reportingConfigurationService.editReportSummary(edit_report);

		curr_report.setCategory(edit_report.getCategory());
		curr_report.setTitle(edit_report.getTitle());
		curr_report.setDescription(edit_report.getDescription());
		form.setReportSummaryEdit(null);
		return this.getModelAndView(curr_report);
	}
	
	
//	set backToDefaults to the edited report
	public ModelAndView backToDefaults(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		if(errors.hasErrors())
			return new ModelAndView(editReportTemplate);
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		VU360Report edit_report = form.getReportSummaryEdit();
		edit_report.setSystemOwned(true);
		//reportingConfigurationService.editReportSummary(edit_report);
		
		com.softech.vu360.lms.vo.VU360User vu360UserVO = new com.softech.vu360.lms.vo.VU360User();
		vu360UserVO.setId(curr_report.getOwner().getId());
		
		VU360Report defaultReport = reportingConfigurationService.cloneReport(curr_report, curr_report.getTitle(),
				curr_report.getCategory(), curr_report.getDescription(), vu360UserVO);
		curr_report.setCategory(defaultReport.getCategory());
		curr_report.setTitle(defaultReport.getTitle());
		curr_report.setDescription(defaultReport.getDescription());
		form.setReportSummaryEdit(null);
		return this.getModelAndView(curr_report);
	}
	
	//cancel the editing of selected report
	public ModelAndView cancelReportSummary(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		ReportForm form = (ReportForm)command;
		form.setReportSummaryEdit(null);
		
		return this.getModelAndView(form.getCurrentReport());
	}
	
	//execute the selected report
	public ModelAndView executeReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		 //SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:ss");
	     SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm:ss a");
	     
		ReportForm form = (ReportForm)command;
		VU360Report report = form.getCurrentReport();
		if( report == null ) {
			throw new Exception("current report not selected");
		}
		
		com.softech.vu360.lms.vo.VU360User owner = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		   
		// ---------Start ------
		// Changes for Proctor Report 
		
		// start date and end date cannot be null/empty on same time
		boolean isError=false, isErrorOnConcurrentLnr=false;
		if(form.getCurrentReport().getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR)){
			HttpSession session = request.getSession();
			
			if ((form.getCourseId() == null || form.getCourseId().equals(""))) {
				session.setAttribute("errorOnProctorReportCourseID",
						"error.addNewSynchrounousCourse.courseID.required");
				isError = true;
			} else
				session.setAttribute("errorOnProctorReportCourseID", null);

			if ((form.getStartDate() == null || form.getStartDate().equals(""))
					&& (form.getEndDate() == null || form.getEndDate().equals(""))) {
				session.setAttribute("errorOnProctorReport","lms.proctor.proctorReport.error.selectStartorEndDate.Message");
				isError = true;
			} else {
				
				boolean isStartDateFormated = false, isEndDateFormated = false;;
				if (!form.getStartDate().equals("")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					try {
						sdf.parse(form.getStartDate());
						isStartDateFormated = true;
					} catch (ParseException e) {
						session.setAttribute("errorOnProctorReport", "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
						isError = true;
						isStartDateFormated = false;
					}
				} 
				
				if (!form.getEndDate().equals("")) {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					try {
						sdf.parse(form.getEndDate());
						isEndDateFormated = true;
					} catch (ParseException e) {
						session.setAttribute("errorOnProctorReport",
								"lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
						isError = true;
						isEndDateFormated = false;
					}
				} 
				
				if(isStartDateFormated && isEndDateFormated)
					session.setAttribute("errorOnProctorReport", null);
			}
		}else if(form.getCurrentReport().getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR_CONCURRENT_LEARNER)){
			HttpSession session = request.getSession();
			
			    if (form.getStartDate() == null || form.getStartDate().equals("")) {
			    	session.setAttribute("errorOnProctorReport","lms.proctor.proctorReport.error.selectDate.Message");
			    	isErrorOnConcurrentLnr = true;
			    } else {
				
				
					if (!form.getStartDate().equals("")) {
						SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
						try {
							sdf.parse(form.getStartDate());
							session.setAttribute("errorOnProctorReport", null);
						} catch (ParseException e) {
							session.setAttribute("errorOnProctorReport", "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
							isErrorOnConcurrentLnr = true;
						}
					} 
	            }
			    
				if (form.getStartTime().equals(form.getEndTime())) 
				{
					session.setAttribute("errorOnProctorReportForSameDate", "lms.proctor.proctorReport.error.selectStartEndTime.Message");
					isErrorOnConcurrentLnr = true;
				}else if (parseFormat.parse(form.getEndTime()).
							compareTo( parseFormat.parse(form.getStartTime()))<0 
						){
					session.setAttribute("errorOnProctorReportForSameDate", "lms.proctor.proctorReport.error.selectStartTimeCannotLessToEndTime.Message");
					isErrorOnConcurrentLnr = true;
			    }
				else
					 session.setAttribute("errorOnProctorReportForSameDate", null);
		}
		
		
		if(isError)
			return new ModelAndView(summaryReportForProctor);
		else if(isErrorOnConcurrentLnr)
			return new ModelAndView(summaryReportForProctorConcurrentLnr);
		
		
		// Adding Parameters values in Reports object  if report name is 'ANSI - CFP Proctor Report'
		if(form.getCurrentReport() != null && form.getCurrentReport().getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR))
		{
		   List<VU360ReportParameter> parameter = new ArrayList<VU360ReportParameter>();
		   
		   VU360ReportParameter p3 = new VU360ReportParameter();
		   p3.setParamName("_courseId");
		   p3.setParamValue(form.getCourseId());
		   parameter.add(p3);
		   form.getCurrentReport().setParameter(parameter);
		   
		   
		   VU360ReportParameter p1 = new VU360ReportParameter();
		   p1.setParamName("_startDate");
		   
		   if(form.getStartDate() == null || form.getStartDate().equals(""))
			   p1.setParamValue(DEFAULT_START_DATE);
		   else
			   p1.setParamValue(dfChanged.format(dfActual.parse(form.getStartDate())) + " 00:00:00");
		   parameter.add(p1);
		   
		   
		   VU360ReportParameter p2 = new VU360ReportParameter();
		   p2.setParamName("_endDate");
		   if(form.getEndDate() == null || form.getEndDate().equals(""))
			   p2.setParamValue(dfChanged.format(new Date()) + " 23:59:59");
		   else
			   p2.setParamValue(dfChanged.format(dfActual.parse(form.getEndDate())) + " 23:59:59");
		   parameter.add(p2);
		   form.getCurrentReport().setParameter(parameter);
		}
		// ---------End ------
		
		// Adding Parameters values in Reports object  if report name is 'ANSI - Concurrent Learners Report'
		else if(form.getCurrentReport() != null && form.getCurrentReport().getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR_CONCURRENT_LEARNER))
		{

			
		   List<VU360ReportParameter> parameter = new ArrayList<VU360ReportParameter>();
		   
		   VU360ReportParameter p1 = new VU360ReportParameter();
		   p1.setParamName("_startDate");
		   
		   Date dtStartDateTime = convertLearnerTimetoCST(owner, dfActual.parse(form.getStartDate()), parseFormat.parse(form.getStartTime()));
		   //p1.setParamValue(dfChanged.format(dfActual.parse(form.getStartDate())) + " " + displayFormat.format(parseFormat.parse(form.getStartTime()))  );
		   p1.setParamValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dtStartDateTime));
		   parameter.add(p1);
		   
		   
		   VU360ReportParameter p2 = new VU360ReportParameter();
		   p2.setParamName("_endDate");
		   Date dtEndDateTime = convertLearnerTimetoCST(owner, dfActual.parse(form.getStartDate()), parseFormat.parse(form.getEndTime()));
		   //p2.setParamValue(dfChanged.format(dfActual.parse(form.getStartDate())) + " " + displayFormat.format(parseFormat.parse(form.getEndTime())) );
		   p2.setParamValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dtEndDateTime));
			  
		   parameter.add(p2);
		   form.getCurrentReport().setParameter(parameter);
		}
		// ---------End ------
		
		reportExecutionService.executeReport(report, owner);
		

		VU360ReportExecutionSummary executionSummary = reportingConfigurationService.lastExecutionSummary(report,owner.getId());
		form.setCurrentReportLastExecutionSummary(executionSummary);

		return this.getModelAndView(report);
	}
	
	/* Rehan Rana
	 * 9 July, 2012
	 * Following function convert learner date time into CST [Central Standard Time] format for Proctor Reports
	 */
	public Date convertLearnerTimetoCST(com.softech.vu360.lms.vo.VU360User owner, Date date, Date time){
		
		TimeZone timeZoneCST;
		com.softech.vu360.lms.vo.TimeZone timeZoneVO;
		TimeZone timeZone;

		timeZoneCST = timeZoneService.getTimeZoneByName(CSTTimeZoneInDB);
		timeZoneVO = owner.getLearner().getLearnerProfile().getTimeZone();
		timeZone = null;

		if (timeZoneVO == null) {
			timeZone = timeZoneService.getTimeZoneByName(CSTTimeZoneInDB);
			timeZoneVO = ProxyVOHelper.createTimeZoneVO(timeZone);
		}

		java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Karachi"));

		int hrDiff = timeZoneCST.getHours() - timeZoneVO.getHours();
		int minDiff = (timeZoneCST.getMinutes() + timeZoneVO.getMinutes()) % 60;

		if (hrDiff < 0) {
			hrDiff -= ((timeZoneCST.getMinutes() + timeZoneVO.getMinutes()) / 60);
			minDiff *= (-1);
		} else {
			hrDiff += ((timeZoneCST.getMinutes() + timeZoneVO.getMinutes()) / 60);
		}

		Date dtDate = date;
		dtDate.setHours(time.getHours());
		dtDate.setMinutes(time.getMinutes());
		dtDate.setSeconds(time.getSeconds());

		Calendar startTime = Calendar.getInstance();
		startTime.setTime(dtDate);
		startTime.add(Calendar.HOUR_OF_DAY, hrDiff);
		startTime.add(Calendar.MINUTE, minDiff);
		dtDate = startTime.getTime();
		return dtDate;
		
	}
	/**
	 * @param report
	 * @return ModelAndView
	 */
	public ModelAndView getModelAndView(VU360Report report){
		if(report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR))
			return new ModelAndView(summaryReportForProctor); 
		if(report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR_CONCURRENT_LEARNER))
			return new ModelAndView(summaryReportForProctorConcurrentLnr); 
		else
			return new ModelAndView(summaryTemplate); 
	}
	
	//execute all the favourite reports
	public ModelAndView executeAllFavouriteReports(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if( curr_report == null ) {
			throw new Exception("current report not selected");
		}
		List<VU360Report> favourites = form.getFavouriteReports();
		com.softech.vu360.lms.vo.VU360User owner = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		for(int i=0; i<favourites.size(); i++){
			try {
				VU360Report report = favourites.get(i); 
				reportExecutionService.executeReport(report, owner);
				if(report.getId().longValue()==curr_report.getId().longValue()){
					VU360ReportExecutionSummary executionSummary = reportingConfigurationService.lastExecutionSummary(report,owner.getId());
					form.setCurrentReportLastExecutionSummary(executionSummary);
				}
			} catch (ReportNotExecutableException e) {
				e.printStackTrace();//TODO how to handle this error???
			}
		}
		return this.getModelAndView(curr_report);
	}

	//display the html view of the selected report as was last run
	public ModelAndView displayReportResultsHTML(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		String htmlData="";
		String filePath="";

		String filename = form.getCurrentReportLastExecutionSummary().getHtmlLocation();
		try{
			htmlData=FileCopyUtils.copyToString(new FileReader(filePath+filename));
		}catch (Exception e){
			System.out.println(e);
		}
		return new ModelAndView(htmlViewTemplate,"htmlData",htmlData); 
	}
	
	//download the csv view of the selected report as was last run
	public ModelAndView displayReportResultsCSV(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		String filename = form.getCurrentReportLastExecutionSummary().getCsvLocation();
		try{
		String filePath="";
		
		File f= new File(filePath+filename);
		
		FileInputStream in = new FileInputStream(f);
		
		response.setContentType("application/csv");
		response.setContentLength( (int)f.length() );
		response.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");

		ServletOutputStream ouputStream=response.getOutputStream();
		FileCopyUtils.copy(in, ouputStream);
		in.close();
		ouputStream.flush();ouputStream.close();
		
		}catch (Exception e){
			System.out.println(e);
		}
		return this.getModelAndView(curr_report);
	}
	
	/**
	 * 2. 
	 * The Reports Fields Section...
	 * Contains the methods for editing the report fields details
	 * of a given report.
	 */
	//default landing page for the report fields...
	//shows the fields menu page
	public ModelAndView reportFieldMenu(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");
		return new ModelAndView(fieldMenuTemplate);
	}

	//shows the report fields...with checkboxes
	//to select for making them visible/invisible in the results
	public ModelAndView displayReportFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");
		List<VU360ReportField> curr_reportFields = reportingConfigurationService.getVU360ReportFieldsbyReportId(curr_report.getId());//curr_report.getFields();

		form.setReportFieldsEdit(this.getReportFieldEditList(curr_reportFields,"DISPLAYORDER"));
		
		return new ModelAndView(fieldSelectionTemplate);
	}
	
	//save the selected report fields as visible for this report
	public ModelAndView saveReportFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		if(errors.hasErrors()){
			return new ModelAndView(fieldSelectionTemplate);
		}
		//the save routine of the selected fields
		List<VU360ReportField> curr_reportFields = form.getCurrentReport().getFields();
		
		List<VU360ReportField> edit_reportFields = form.getReportFieldsEdit();
		VU360Report savedReport = reportingConfigurationService.saveReportFieldVisibility(curr_report, edit_reportFields);
		List<VU360ReportField> saved_reportFields = savedReport.getFields();
		
		for(int i = 0; i<curr_reportFields.size(); i++){
			VU360ReportField currField = curr_reportFields.get(i);
			for(int j = 0; j<saved_reportFields.size(); j++){
				VU360ReportField editedField = saved_reportFields.get(j);
				if(editedField.getId().longValue() == currField.getId().longValue()){
					currField.setVisible(editedField.getVisible());
					break;
				}
			}
		}

		form.setReportFieldsEdit(null);
		return new ModelAndView(fieldMenuTemplate);
	}

	//cancel the editing of fields visible for this report
	public ModelAndView cancelReportFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		ReportForm form = (ReportForm)command;
		
		form.setReportFieldsEdit(null);
		return new ModelAndView(fieldMenuTemplate);
	}

	//display the ordered list of visible report fields for this report
	public ModelAndView displayReportFieldsOrder(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		List<VU360ReportField> curr_reportFields = reportingConfigurationService.getVU360ReportFieldsbyReportId(curr_report.getId());//curr_report.getFields();
		form.setReportFieldsEdit(this.getReportFieldEditList(curr_reportFields,"DISPLAYORDER"));
		return new ModelAndView(fieldOrderTemplate);
	}
	
	//save the order of visible report fields for this report
	public ModelAndView saveReportFieldsOrder(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");
		
		if(errors.hasErrors()){
			return new ModelAndView(fieldOrderTemplate);
		}
		//the save routine of the selected fields

		List<VU360ReportField> curr_reportFields = form.getCurrentReport().getFields();
		
		List<VU360ReportField> edit_reportFields = form.getReportFieldsEdit();
		VU360Report savedReport = reportingConfigurationService.saveReportFieldOrder(curr_report, edit_reportFields);
		//TODO setup the current report field order
		List<VU360ReportField> saved_reportFields = savedReport.getFields();
		
		for(int i = 0; i<curr_reportFields.size(); i++){
			VU360ReportField currField = curr_reportFields.get(i);
			for(int j = 0; j<saved_reportFields.size(); j++){
				VU360ReportField editedField = saved_reportFields.get(j);
				if(editedField.getId().longValue() == currField.getId().longValue()){
					currField.setSortOrder(editedField.getDisplayOrder());
					break;
				}
			}
		}
		
		form.setReportFieldsEdit(null);
		return new ModelAndView(fieldMenuTemplate);
	}
	
	//cancel the editing of fields order for this report
	public ModelAndView cancelReportFieldsOrder(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		ReportForm form = (ReportForm)command;
		
		form.setReportFieldsEdit(null);
		return new ModelAndView(fieldMenuTemplate);
	}
	
	//display the sort order for each of the visible report fields for this report
	public ModelAndView displayReportFieldsSortOrder(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		List<VU360ReportField> curr_reportFields = reportingConfigurationService.getVU360ReportFieldsbyReportId(curr_report.getId());//curr_report.getFields();

		form.setReportFieldsEdit(this.getReportFieldEditList(curr_reportFields,"SORTORDER"));

		return new ModelAndView(fieldSortTemplate);
	}
	
	//save the sort order of visible report fields for this report
	public ModelAndView saveReportFieldsSortOrder(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");
		
		if(errors.hasErrors()){
			return new ModelAndView(fieldSortTemplate);
		}
		//the save routine of the selected fields
		List<VU360ReportField> curr_reportFields = form.getCurrentReport().getFields();
		
		List<VU360ReportField> edit_reportFields = form.getReportFieldsEdit();

		VU360Report savedReport = reportingConfigurationService.saveReportFieldSortOrder(curr_report, edit_reportFields);
		//setup the current report field sort order
		List<VU360ReportField> saved_reportFields = savedReport.getFields();
		
		for(int i = 0; i<curr_reportFields.size(); i++){
			VU360ReportField currField = curr_reportFields.get(i);
			for(int j = 0; j<saved_reportFields.size(); j++){
				VU360ReportField editedField = saved_reportFields.get(j);
				if(editedField.getId().longValue() == currField.getId().longValue()){
					currField.setSortOrder(editedField.getSortOrder());
					break;
				}
			}
		}

		form.setReportFieldsEdit(null);
		return new ModelAndView(fieldMenuTemplate);

	}
	
	//cancel the editing of fields sort order for this report
	public ModelAndView cancelReportFieldsSortOrder(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		ReportForm form = (ReportForm)command;
		
		form.setReportFieldsEdit(null);
		return new ModelAndView(fieldMenuTemplate);
	}
	
	/**
	 * 3. 
	 * The Reports Filter Section...
	 * Contains the methods for editing the report filters details
	 * of a given report.
	 */
	//display the filters for this report
	public ModelAndView displayReportFilters(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");
		
		Map<String, List<VU360ReportFilterOperand>> operandMap = reportingConfigurationService.getReportFilterOperandMap();
		form.setOperandMap(operandMap);
		this.createFilterList(form);
		List<VU360ReportField> curr_reportFields = reportingConfigurationService.getVU360ReportFieldsbyReportId(curr_report.getId());
		curr_report.setFields(new ArrayList());
		curr_report.setFields(this.getReportFieldEditList(curr_reportFields, "DISPLAYORDER"));
		
		return new ModelAndView(filterReportTemplate);
	}
	
	//save the filters for this report
	public ModelAndView saveReportFilters(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
		ReportForm form = (ReportForm)command;
		VU360Report curr_report = form.getCurrentReport();
		if(curr_report==null)
			throw new Exception("current report not selected");

		if(errors.hasErrors()){
			Map<String, List<VU360ReportFilterOperand>> operandMap = reportingConfigurationService.getReportFilterOperandMap();
			form.setOperandMap(operandMap);
			return new ModelAndView(filterReportTemplate);
		}
		List<ReportFilterItem> filterItems = form.getReportFilterItems();
		VU360Report edited_report = reportingConfigurationService.saveReportFilters(curr_report, filterItems);
		curr_report.setFilters(edited_report.getFilters());
		this.createFilterList(form);
		return new ModelAndView(filterReportTemplate);
	}
	
	//cancel the editing of filters for this report
	public ModelAndView cancelReportFilters(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
		ReportForm form = (ReportForm)command;

		this.createFilterList(form);

		return new ModelAndView(filterReportTemplate);
	}

	
	/**
	 * Spring Injection methods
	 */
	/**
	 * @param redirectTemplate the redirectTemplate to set
	 */
	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	/**
	 * @param reportingConfigurationService the reportingConfigurationService to set
	 */
	public void setReportingConfigurationService(ReportingConfigurationService reportingConfigurationService) {
		this.reportingConfigurationService = reportingConfigurationService;
	}

	public void setSummaryTemplate(String summaryTemplate) {
		this.summaryTemplate = summaryTemplate;
	}

	/**
	 * @param fieldMenuTemplate the fieldMenuTemplate to set
	 */
	public void setFieldMenuTemplate(String fieldMenuTemplate) {
		this.fieldMenuTemplate = fieldMenuTemplate;
	}

	public void setEditReportTemplate(String editReportTemplate) {
		this.editReportTemplate = editReportTemplate;
	}

	/**
	 * @param fieldSelectionTemplate the fieldSelectionTemplate to set
	 */
	public void setFieldSelectionTemplate(String fieldSelectionTemplate) {
		this.fieldSelectionTemplate = fieldSelectionTemplate;
	}

	public void setFieldOrderTemplate(String fieldOrderTemplate) {
		this.fieldOrderTemplate = fieldOrderTemplate;
	}

	public void setFieldSortTemplate(String fieldSortTemplate) {
		this.fieldSortTemplate = fieldSortTemplate;
	}
	
	public void setFilterReportTemplate(String filterReportTemplate) {
		this.filterReportTemplate = filterReportTemplate;
	}
	/**
	 * Helper Methods Section...
	 */

	private List<VU360ReportField> getReportFieldEditList(List<VU360ReportField> curr_reportFields, String orderField){
		List<VU360ReportField> edit_reportFields = new ArrayList<VU360ReportField>(); 
		for(VU360ReportField field:curr_reportFields){
			VU360ReportField editField = new VU360ReportField();
			editField.setId(field.getId());
			editField.setDataType(field.getDataType());
			editField.setDisplayOrder(field.getDisplayOrder());
			editField.setDisplayName(field.getDisplayName());
			editField.setVisible(field.getVisible());
			editField.setSortOrder(field.getSortOrder());
			editField.setSortType(field.getSortType());
			editField.setFieldOrder(field.getFieldOrder());
			editField.setFilterable(field.getFilterable());
			edit_reportFields.add(editField);
		}
		Collections.sort(edit_reportFields, new ReportFieldComparator(orderField));
		return edit_reportFields;
	}
	
	
	private class ReportFieldComparator implements Comparator<VU360ReportField>{
		private String sortField = "DISPLAYORDER";
		public ReportFieldComparator() {
			super();
		}

		public ReportFieldComparator(String sortField) {
			super();
			this.sortField = sortField;
		}

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(VU360ReportField arg0, VU360ReportField arg1) {
			if(arg0==null && arg1 == null) return 0;
			if(arg0==null && arg1 != null) return 1;
			if(arg0!=null && arg1 == null) return -1;
			if(sortField.equalsIgnoreCase("DISPLAYORDER"))
				return new Integer(arg0.getDisplayOrder()).compareTo(new Integer(arg1.getDisplayOrder()));
			else if(sortField.equalsIgnoreCase("SORTORDER"))
				return new Integer(arg0.getSortOrder()).compareTo(new Integer(arg1.getSortOrder()));
			else	
				return new Integer(arg0.getDisplayOrder()).compareTo(new Integer(arg1.getDisplayOrder()));
		}
		
	}

	private void createFilterList(ReportForm form){
		List<VU360ReportFilter> currFilters = reportingConfigurationService.getVU360ReportFilter(form.getCurrentReport());//form.getCurrentReport().getFilters();
		form.getReportFilterItems().clear();
		form.getCurrentReport().setFilters(new ArrayList());
		VU360ReportFilter filterUseForWeb = new VU360ReportFilter();
		for(VU360ReportFilter filter : currFilters){
			filterUseForWeb.setField(filter.getField());
			filterUseForWeb.setId(filter.getId());
			filterUseForWeb.setManditoryFilter(filter.getManditoryFilter());
			filterUseForWeb.setOperand(filter.getOperand());
			filterUseForWeb.setReport(filter.getReport());
			filterUseForWeb.setReportFilterType(filter.getReportFilterType());
			filterUseForWeb.setValue(filter.getValue());
			
			form.getCurrentReport().getFilters().add(filterUseForWeb);
			filterUseForWeb = new VU360ReportFilter();
		}
		form.setReportFilterItems(new ArrayList());
		ReportFilterItem item =new ReportFilterItem(); 
		if(currFilters!=null && currFilters.size()>0){
			for(int i=0; i<currFilters.size(); i++){
				VU360ReportFilter filter = currFilters.get(i);
				//ReportFilterItem item = form.getReportFilterItems().get(i);
				item.setId(filter.getId());
				
				VU360ReportField field = filter.getField();
				item.setReportFieldId(field.getId());
				String fieldDatatype = field.getDataType();
				item.setReportFieldDatatype(fieldDatatype);
				
				item.setFilterOperandId(filter.getOperand().getId());
				
				//String filterType = filter.getReportFilterType();
				String filterType = filter.getReportFilterType()==null?VU360ReportFilter.STRING_TYPE:filter.getReportFilterType();//temporary fix
				item.setFilterType(filterType);
				
				VU360ReportFilterValue itemValue = filter.getValue();
				if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_STRING))
					item.setFilterStringValue(itemValue.getStringValue());
				else if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_INTEGER))
					item.setFilterIntegerValue(Integer.parseInt(itemValue.getNumericValue().toString()));
				else if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_DOUBLE))
					item.setFilterDoubleValue(Double.parseDouble(itemValue.getNumericValue().toString()));
				else if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_DATE))
					item.setFilterDateValue(itemValue.getDateValue());//formatting???
				else if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_BOOLEAN)){
					Integer boolVal=Integer.parseInt(itemValue.getNumericValue().toString());
					item.setFilterBooleanValue(boolVal>0?true:false);
				}else
					item.setFilterValue(itemValue.getStringValue());//others to come..this is not correct
				form.getReportFilterItems().add(item);
				item = new ReportFilterItem();
			}
		}
	}

	public void setHtmlViewTemplate(String htmlViewTemplate) {
		this.htmlViewTemplate = htmlViewTemplate;
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

	public String getSummaryReportForProctor() {
		return summaryReportForProctor;
	}

	public void setSummaryReportForProctor(String summaryReportForProctor) {
		this.summaryReportForProctor = summaryReportForProctor;
	}

	public String getSummaryReportForProctorConcurrentLnr() {
		return summaryReportForProctorConcurrentLnr;
	}

	public void setSummaryReportForProctorConcurrentLnr(
			String summaryReportForProctorConcurrentLnr) {
		this.summaryReportForProctorConcurrentLnr = summaryReportForProctorConcurrentLnr;
	}

	public TimeZoneService getTimeZoneService() {
		return timeZoneService;
	}

	public void setTimeZoneService(TimeZoneService timeZoneService) {
		this.timeZoneService = timeZoneService;
	}

}
