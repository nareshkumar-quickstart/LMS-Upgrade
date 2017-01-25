/**
 *
 */
package com.softech.vu360.lms.web.controller.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.microsoft.schemas.sqlserver._2005._06._30.reporting.reportingservices.ParameterValue;
import com.softech.vu360.lms.exception.ReportNotExecutableException;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360ReportExecutionSummary;
import com.softech.vu360.lms.model.VU360ReportField;
import com.softech.vu360.lms.model.VU360ReportFilter;
import com.softech.vu360.lms.model.VU360ReportFilterOperand;
import com.softech.vu360.lms.model.VU360ReportFilterValue;
import com.softech.vu360.lms.service.ReportExecutionService;
import com.softech.vu360.lms.service.ReportingConfigurationService;
import com.softech.vu360.lms.util.SSRSUtility;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ReportFilterItem;
import com.softech.vu360.lms.web.controller.model.ReportForm;
import com.softech.vu360.lms.web.controller.validator.ReportConfigurationValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.RedirectUtils;
import com.softech.vu360.util.SSRSReportsEnum;
import com.softech.vu360.util.VU360Properties;

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
    private String ssrsTemplate;

    private final String DEFAULT_START_DATE = "2001-01-01 00:00:00";
    private final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final String INPUT_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    private final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
    private final String TIMESTAMP_OF_ALMOST_AN_HOUR =  " 23:59:59";
    private final String TIMESTAMP_OF_ZERO_SECONDS =  " 00:00:00";

    Logger log=Logger.getLogger(ReportController.class);

    public static final String SQLTEMPLATE_LEARNER_PERFORMANCE_BY_COURSE = "vm/reportsql/mgr_PrfLearnerByCourse.vm";
    public static final String SQLTEMPLATE_LEARNER_PERFORMANCE_BY_COURSE_COMPREHENSIVE = "vm/reportsql/mgr_PrfLearnerByCourseComp.vm";

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
        com.softech.vu360.lms.vo.VU360User voUser = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userMode = "Manager";

        if(!form.getUserMode().equalsIgnoreCase(userMode)){
            form.setUserMode(userMode);
            List<VU360Report> ownedReports = reportingConfigurationService.getOwnedReports(voUser.getId(),userMode);

            //LMS-10508: Faisal Abdul Aziz.
            //Filtering out reports not owned by the customer.
            //Right now, the Reports page for manager shows system reports
            //not owned by the customer.
            //WARNNING: FRAGILE HACK.
            //We may need to associate Features with the report to properly
            //check if the user is authorized for the reports.
            List<VU360Report> reports = new ArrayList<VU360Report>(ownedReports.size());
            Set<com.softech.vu360.lms.vo.LMSRole> roles = voUser.getLmsRoles();
            for (Iterator<com.softech.vu360.lms.vo.LMSRole> it = roles.iterator(); it.hasNext();)
            {
                com.softech.vu360.lms.vo.LMSRole role = it.next();
                log.info("role.type = " + role.getRoleType());
                List<com.softech.vu360.lms.vo.LMSRoleLMSFeature> permissions = role.getLmsPermissions();
                log.info("permissions = " + permissions);
                if(com.softech.vu360.lms.vo.LMSRole.ROLE_TRAININGMANAGER.equals(role.getRoleType()) && permissions != null && permissions.size() > 0)
                {
                    log.info("permissions.size = " + permissions.size());
                    for (com.softech.vu360.lms.vo.LMSRoleLMSFeature feature : permissions) {
                        String desc = feature.getLmsFeature().getFeatureDescription();

                        String[] split = desc.split(" ");
                        if(split.length == 2 && "REPORTS".equals(split[1].toUpperCase())){
                            for (VU360Report report : ownedReports) {
                                log.debug("-------report category: " + report.getCategory());
                                if( split[0].toUpperCase().contains(report.getCategory().toUpperCase())
                                        && UserPermissionChecker.hasAccessToFeature(feature.getLmsFeature().getFeatureCode(), voUser, request.getSession(true))){
                                    reports.add(report);
                                }
                            }
                        }
                    }
                }
            }

            //List<VU360Report> ownedReports = reportingConfigurationService.getOwnedReports(owner);
            form.setOwnedReports(reports);

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
        }


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

        String url = "/mgr_ManageReports.do?method=browseReports";
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
            com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            VU360ReportExecutionSummary executionSummary = reportingConfigurationService.lastExecutionSummary(report,loggedInUserVO.getId());
            form.setCurrentReportLastExecutionSummary(executionSummary);
        }

        //forward to the view template
        ModelAndView view = new ModelAndView(summaryTemplate);
        return view;
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
        return this.browseReports(request, response, command, errors);
    }

    //set/unset the current report as favourite
    public ModelAndView setFavouriteReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
        ReportForm form = (ReportForm)command;
        VU360Report report = form.getCurrentReport();
        if(report==null)
            throw new Exception("current report not selected");

        report=reportingConfigurationService.changeReportFavouriteStatus(report, !report.isFavorite());
        // Added By Marium Saud 
        // LMS- 19215 : Fixed this issue in a way that form.getCurrentReport.favorite was not set to true after marking the report as 'Favorite'. Which doesn't move the selected
        // report to Favorite Tab and also not marking the bookmark star with yellow color.
        if(report.isFavorite()){
        	List<VU360Report> ownerReportList=form.getOwnedReports();
        	for(VU360Report vu360Report : ownerReportList){
        		if(report.getId().equals(vu360Report.getId())){
        			vu360Report.setFavorite(true);
        			form.setOwnedReports(ownerReportList);
        			break;
        		}
        	}
        	
        	form.setCurrentReport(report);	
        }
        else{
        	List<VU360Report> ownerReportList=form.getOwnedReports();
        	for(VU360Report vu360Report : ownerReportList){
        		if(report.getId().equals(vu360Report.getId())){
        			vu360Report.setFavorite(false);
        			form.setOwnedReports(ownerReportList);
        			break;
        		}
        	}
        	
        	form.setCurrentReport(report);
        }
        ModelAndView view = new ModelAndView(summaryTemplate);
        return view;
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
		
        com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		VU360Report clonedReport = reportingConfigurationService.cloneReport(curr_report, edit_report.getTitle(),
				edit_report.getCategory(), edit_report.getDescription(), loggedInUserVO);

        form.addOwnedReport(clonedReport);
        form.setCurrentReport(clonedReport);

        form.setReportSummaryEdit(null);
        return new ModelAndView(summaryTemplate);
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
        return new ModelAndView(summaryTemplate);
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
        //edit_report.setSystemOwned(true);
        edit_report.setFavorite(false);
        //edit_report.setFilters(new ArrayList<VU360ReportFilter>());

        List<VU360ReportFilter> filters=reportingConfigurationService.getVU360ReportFilter(edit_report);
        List<ReportFilterItem> filterItems=new ArrayList<ReportFilterItem>();
        ReportFilterItem filterItem=null;
        for(VU360ReportFilter filter : filters){
            filterItem=new ReportFilterItem();
            filterItem.setMarkedForDeletion(true);
            filterItem.setReportFieldId(filter.getId());
            filterItem.setReportFieldDatatype(filter.getReportFilterType());
            filterItem.setFilterOperandId(filter.getOperand().getId());
            filterItem.setId(filter.getId());

            filterItems.add(filterItem);
        }

        if(filterItems!=null){
            reportingConfigurationService.saveReportFilters(edit_report, filterItems);
        }



        for(VU360ReportField currentField : curr_report.getFields()){
            currentField.setDisplayOrder(currentField.getDefaultDisplayOrder());
            currentField.setSortOrder(currentField.getDefaultSortOrder());
            currentField.setVisible(currentField.getVisibleByDefault());
        }


        reportingConfigurationService.editReportSummary(edit_report);

        curr_report.setCategory(edit_report.getCategory());
        curr_report.setTitle(edit_report.getTitle());
        curr_report.setDescription(edit_report.getDescription());
        form.setReportSummaryEdit(null);
        return new ModelAndView(summaryTemplate);
    }

    //cancel the editing of selected report
    public ModelAndView cancelReportSummary(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors){
        ReportForm form = (ReportForm)command;
        form.setReportSummaryEdit(null);

        return new ModelAndView(summaryTemplate);
    }

    //execute the selected report
    public ModelAndView executeReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

        ReportForm form = (ReportForm)command;
        VU360Report report = form.getCurrentReport();
        report = reportingConfigurationService.getReportCopy(report.getId());
        List<VU360ReportFilter> customFitlers = null;


        if( report == null ) {
            throw new Exception("current report not selected");
        }
        com.softech.vu360.lms.vo.VU360User owner = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Only for Learner Performance By Course report
        if(report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_LEARNER_PERFORMANCE_BY_COURSE) 
        		|| report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_LEARNER_PERFORMANCE_BY_COURSE_COMPREHENSIVE))        	
        {
            if(validateStartandEndDate(form, request.getSession()))
                return new ModelAndView(summaryTemplate);

            if(form.getCurrentReport() != null)
            {
                customFitlers = getStartAndDateFilters(form, report);
                form.getCurrentReport().setFilters(null);
                form.getCurrentReport().setFilters(reportingConfigurationService.getVU360ReportFilter(form.getCurrentReport()));
                form.getCurrentReport().getFilters().addAll(customFitlers);
                //Adding the filters to VU360Report obj 'report' to append filters at time of execution as 'report' object has been passed for further manipulation
                report.getFilters().addAll(form.getCurrentReport().getFilters());
              }
        }

        reportExecutionService.executeReport(report, owner);

        VU360ReportExecutionSummary executionSummary = reportingConfigurationService.lastExecutionSummary(report,owner.getId());
        form.setCurrentReportLastExecutionSummary(executionSummary);

        if(customFitlers != null) {
            form.getCurrentReport().getFilters().removeAll(customFitlers);
        }

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
        return new ModelAndView(summaryTemplate);
    }

    //display the html view of the selected report as was last run
    public ModelAndView displayReportResultsHTML(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
        ReportForm form = (ReportForm)command;
        VU360Report curr_report = reportingConfigurationService.getReportCopy(form.getCurrentReport().getId());
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

        VU360Report curr_report = reportingConfigurationService.getReportCopy(form.getCurrentReport().getId());
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
        return new ModelAndView(summaryTemplate);
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
        VU360Report curr_report = reportingConfigurationService.getReportCopy(form.getCurrentReport().getId());
        if(curr_report==null)
            throw new Exception("current report not selected");
        List<VU360ReportField> curr_reportFields = curr_report.getFields();

        form.setReportFieldsEdit(this.getReportFieldEditList(curr_reportFields,"DISPLAYORDER"));
        
        //	DEV - Organization Group Performance by Training Plan **** 1091
        boolean orgGrpByTrnPln = false;
        //LMS-18266
        long orgGrpByTrnPlnReportId =  Long.parseLong(VU360Properties.getVU360Property("lms.report.OrganizationGroupPerformancebyTrainingPlan.id"));
        
        if(curr_report.getDerivedFrom() != null && curr_report.getDerivedFrom().getId() == orgGrpByTrnPlnReportId ) {
        	orgGrpByTrnPln = true;
        }

        return new ModelAndView(fieldSelectionTemplate,"orgGrpByTrnPln",orgGrpByTrnPln);
    }

    //save the selected report fields as visible for this report
    public ModelAndView saveReportFields(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
        ReportForm form = (ReportForm)command;
        VU360Report curr_report = reportingConfigurationService.getReportCopy(form.getCurrentReport().getId());
        if(curr_report==null)
            throw new Exception("current report not selected");

        if(errors.hasErrors()){
            return new ModelAndView(fieldSelectionTemplate);
        }
        //the save routine of the selected fields
        List<VU360ReportField> curr_reportFields = curr_report.getFields();

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
        VU360Report curr_report = reportingConfigurationService.getReportCopy(form.getCurrentReport().getId());
        if(curr_report==null)
            throw new Exception("current report not selected");

        List<VU360ReportField> curr_reportFields = curr_report.getFields();
        form.setReportFieldsEdit(this.getReportFieldEditList(curr_reportFields,"DISPLAYORDER"));
        return new ModelAndView(fieldOrderTemplate);
    }

    //save the order of visible report fields for this report
    public ModelAndView saveReportFieldsOrder(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
        ReportForm form = (ReportForm)command;
        VU360Report curr_report = reportingConfigurationService.getReportCopy(form.getCurrentReport().getId());
        if(curr_report==null)
            throw new Exception("current report not selected");

        if(errors.hasErrors()){
            return new ModelAndView(fieldOrderTemplate);
        }
        //the save routine of the selected fields

        List<VU360ReportField> curr_reportFields = curr_report.getFields();

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
        VU360Report curr_report = reportingConfigurationService.getReportCopy(form.getCurrentReport().getId());
        if(curr_report==null)
            throw new Exception("current report not selected");

        List<VU360ReportField> curr_reportFields = curr_report.getFields();

        form.setReportFieldsEdit(this.getReportFieldEditList(curr_reportFields,"SORTORDER"));

        return new ModelAndView(fieldSortTemplate);
    }

    //save the sort order of visible report fields for this report
    public ModelAndView saveReportFieldsSortOrder(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception{
        ReportForm form = (ReportForm)command;
        VU360Report curr_report = reportingConfigurationService.getReportCopy(form.getCurrentReport().getId());
        if(curr_report==null)
            throw new Exception("current report not selected");

        if(errors.hasErrors()){
            return new ModelAndView(fieldSortTemplate);
        }
        //the save routine of the selected fields
        List<VU360ReportField> curr_reportFields = curr_report.getFields();

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
                try
                {
                    if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_STRING))
                        item.setFilterStringValue(itemValue.getStringValue());
                    else if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_INTEGER))
                        item.setFilterIntegerValue(itemValue.getNumericValue().intValue());
                    else if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_DOUBLE))
                        item.setFilterDoubleValue(Double.parseDouble(itemValue.getNumericValue().toString()));
                    else if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_DATE))
                        item.setFilterDateValue(itemValue.getDateValue());//formatting???
                    else if(fieldDatatype.equalsIgnoreCase(VU360ReportField.DT_BOOLEAN)){
                        Integer boolVal=Integer.parseInt(itemValue.getNumericValue().toString());
                        item.setFilterBooleanValue(boolVal>0?true:false);
                    }else
                        item.setFilterValue(itemValue.getStringValue());//others to come..this is not correct
                }
                catch(Exception ex){
                    if(itemValue.getStringValue()!=null)
                        item.setFilterValue(itemValue.getStringValue());
                    log.error(ex.getMessage());
                }
                form.getReportFilterItems().add(item);
                item =new ReportFilterItem();
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

    private boolean validateStartandEndDate(ReportForm form, HttpSession session) {

        boolean isError = false;

        if ((form.getStartDate() == null || form.getStartDate().equals(""))
                && (form.getEndDate() == null || form.getEndDate().equals(""))) {
            session.setAttribute("errorOnPerformanceReport","lms.proctor.proctorReport.error.selectStartorEndDate.Message");
            isError = true;
        } else {

            boolean isStartDateFormated = false, isEndDateFormated = false;

            Date startDate = null, endDate = null;

            if (!form.getStartDate().equals("")) {
                try {
                    startDate = DATE_FORMATTER.parse(form.getStartDate().concat(TIMESTAMP_OF_ZERO_SECONDS));
                    isStartDateFormated = true;
                } catch (ParseException e) {
                    session.setAttribute("errorOnPerformanceReport", "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
                    isError = true;
                    isStartDateFormated = false;
                }
            }

            if (!form.getEndDate().equals("")) {
                try {
                    endDate = DATE_FORMATTER.parse(form.getEndDate().concat(TIMESTAMP_OF_ALMOST_AN_HOUR));
                    isEndDateFormated = true;
                } catch (ParseException e) {
                    session.setAttribute("errorOnPerformanceReport",
                            "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
                    isError = true;
                    isEndDateFormated = false;
                }
            }

            if(isStartDateFormated && isEndDateFormated) {
                if(startDate.after(endDate)) {
                    session.setAttribute("errorOnPerformanceReport", "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
                    isError = true;
                    isStartDateFormated = false;
                }
                if(endDate.before(startDate)) {
                    session.setAttribute("errorOnPerformanceReport",
                            "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
                    isError = true;
                    isEndDateFormated = false;
                }
            }

            if(isStartDateFormated || isEndDateFormated)
                session.setAttribute("errorOnPerformanceReport", null);
        }
        return isError;
    }

    /**
     * Prepare report filters of start and end date
     * These filters (start and end date) performs
     * (additional, if there is any other) on the report
     * (record set)
     *
     * @param form
     * @param report
     * @return
     * @throws ParseException
     */
    private List<VU360ReportFilter> getStartAndDateFilters(ReportForm form, VU360Report report) throws ParseException {

        List<VU360ReportFilter> filters = new ArrayList<VU360ReportFilter>();

        VU360ReportField enrollmentStartDate = new VU360ReportField();
        enrollmentStartDate.setDataType(VU360ReportField.DT_DATE);
        enrollmentStartDate.setColumnFormat(VU360ReportField.FORMAT_DATETIME);
        enrollmentStartDate.setDbColumnName("EnrollmentStartDate");
        enrollmentStartDate.setFilterable(true);
        enrollmentStartDate.setVu360report(report);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DB_DATE_FORMAT);

        Date startDate;
        startDate = dateFormat.parse(getStartDate(form));

        Date endDate;
        endDate = dateFormat.parse(getEndDate(form));

        VU360ReportFilterValue enrollmentStartDateStartRangeFilterValue = new VU360ReportFilterValue();
        enrollmentStartDateStartRangeFilterValue.setDateValue(startDate);

        VU360ReportFilterValue enrollmentStartDateEndDateRangeFilterValue = new VU360ReportFilterValue();
        enrollmentStartDateEndDateRangeFilterValue.setDateValue(endDate);

        VU360ReportFilterOperand enrollmentStartDateGreaterThanAndEqualFilterOrperand = new VU360ReportFilterOperand();
        enrollmentStartDateGreaterThanAndEqualFilterOrperand.setValue(VU360ReportFilterOperand.GREATER_THAN_OR_EQUAL_OP);

        VU360ReportFilterOperand enrollmentStartDateLessThanAndEqualFilterOrperand = new VU360ReportFilterOperand();
        enrollmentStartDateLessThanAndEqualFilterOrperand.setValue(VU360ReportFilterOperand.LESS_THAN_OR_EQUAL_OP);


        VU360ReportFilter enrollmentStartDateGreaterThanFilter = new VU360ReportFilter();
        enrollmentStartDateGreaterThanFilter.setOperand(enrollmentStartDateGreaterThanAndEqualFilterOrperand);
        enrollmentStartDateGreaterThanFilter.setReportFilterType(VU360ReportFilter.DATE_TYPE);
        enrollmentStartDateGreaterThanFilter.setField(enrollmentStartDate);
        enrollmentStartDateGreaterThanFilter.setValue(enrollmentStartDateStartRangeFilterValue);
        enrollmentStartDateGreaterThanFilter.setId(Long.valueOf(1));

        filters.add(enrollmentStartDateGreaterThanFilter);

        VU360ReportFilter enrollmentStartDateLessThanFilter = new VU360ReportFilter();
        enrollmentStartDateLessThanFilter.setOperand(enrollmentStartDateLessThanAndEqualFilterOrperand);
        enrollmentStartDateLessThanFilter.setReportFilterType(VU360ReportFilter.DATE_TYPE);
        enrollmentStartDateLessThanFilter.setField(enrollmentStartDate);
        enrollmentStartDateLessThanFilter.setValue(enrollmentStartDateEndDateRangeFilterValue);
        enrollmentStartDateLessThanFilter.setId(Long.valueOf(2));

        filters.add(enrollmentStartDateLessThanFilter);

        return filters;

    }

    /**
     * Get a valid start date
     *
     * @param form
     * @return
     * @throws ParseException
     */
    private String getStartDate(ReportForm form) throws ParseException {

        String result = DEFAULT_START_DATE;
        String startDate = form.getStartDate();

        if((startDate != null) && (startDate.trim().length() > 0)) {

            result = ConvertToDBDateFormat(startDate.concat(TIMESTAMP_OF_ZERO_SECONDS));

        }
        return result;
    }

    /**
     * Get a valid end date
     *
     * @param form
     * @return
     * @throws ParseException
     */
    private String getEndDate(ReportForm form) throws ParseException {

        String result;
        String endDate = form.getEndDate();

        if((endDate == null) || (endDate.trim().length() == 0)) {
            result = ConvertToDBDateFormat((new SimpleDateFormat(INPUT_DATE_FORMAT)).format(new Date()));
        } else {
            result = ConvertToDBDateFormat(endDate.concat(TIMESTAMP_OF_ALMOST_AN_HOUR));
        }
        return result;
    }


    private String ConvertToDBDateFormat(String input) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(INPUT_DATE_FORMAT);
        Date date = dateFormat.parse(input.concat(TIMESTAMP_OF_ZERO_SECONDS));
        dateFormat.applyPattern(DB_DATE_FORMAT);
        return dateFormat.format(date);
    }

    public String getSsrsTemplate() {
        return ssrsTemplate;
    }

    public void setSsrsTemplate(String ssrsTemplate) {
        this.ssrsTemplate = ssrsTemplate;
    }
    public ModelAndView loadReportPage(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ReportForm form = (ReportForm)command;
		VU360Report report = form.getCurrentReport();
		HashMap<Object, Object> context = new HashMap<Object, Object>();

		logger.info("Load Report Method in Manager Report Controller");

		if(report.getSqlTemplateUri() != null && (report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_LEARNER_PERFORMANCE_BY_COURSE) 
				|| report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_LEARNER_PERFORMANCE_BY_COURSE_COMPREHENSIVE)))
		{	
			if(validateStartandEndDate(form, request.getSession()))
				return new ModelAndView(summaryTemplate);
		}
		
		String reportName = report.getTitle().replaceAll("\\s+","");
		reportName = request.getParameter("type")==null || request.getParameter("type").equalsIgnoreCase("g")?reportName+ "-Graph":reportName+ "-Tabular";

		ParameterValue[] parameters = getSSRSReportParameters(form);
		try {
			String reportHTML = SSRSUtility.loadReport("/LMSReports/" + reportName, parameters);
			String browserType = request.getHeader("User-Agent");
		
			if (reportName.toUpperCase().indexOf("GRAPH")>0 && browserType.contains("MSIE")){// for graphical report and for IE only
				  int rnd = (int)(Math.random() * (1000000));
				  int indexOfImgSrc = reportHTML.indexOf("ssrsImages/C_");
				  if (indexOfImgSrc > 0){
					  String imageRef = reportHTML.substring(indexOfImgSrc,  reportHTML.indexOf("\"" ,indexOfImgSrc));
					  reportHTML = reportHTML.replace(imageRef, imageRef + "?r="+ rnd);
				  }
			}
			
			context.put("reportHTML", reportHTML);	
		}
		catch (java.lang.OutOfMemoryError e) {
			context.put("reportHTML", "Dataset is too large. Please reconstruct your data fields and narrow down the criteria.");	
		}		

		ModelAndView mav = new ModelAndView(ssrsTemplate, "context", context);

		return mav;
	}	

	public ModelAndView exportReport(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		ReportForm form = (ReportForm)command;
		VU360Report report = form.getCurrentReport();

		logger.info("Export Report Method in Manager Report Controller");

		if(report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_LEARNER_PERFORMANCE_BY_COURSE))
		{			
			if(validateStartandEndDate(form, request.getSession()))
				return new ModelAndView(summaryTemplate);
		}

		String reportName = report.getTitle().replaceAll("\\s+","");
		ParameterValue[] parameters = getSSRSReportParameters(form);
		reportName = request.getParameter("type")==null || request.getParameter("type").equalsIgnoreCase("g")?reportName+ "-Graph":reportName+ "-Tabular";
		//Valid options are HTML4.0, MHTML, EXCEL, CSV, PDF, etc
		String exportFormat = request.getParameter("format")==null || request.getParameter("format").equalsIgnoreCase("EXCEL")?"EXCEL":"PDF";
		
		String reportFile = SSRSUtility.exportReport("/LMSReports/" + reportName, parameters, exportFormat);
		downloadSSRSExportedReport(reportFile, exportFormat, response);

		return null;
	}

	private ParameterValue[] getSSRSReportParameters(ReportForm form){
		ParameterValue[] parameters;
		String reportTitle = form.getCurrentReport().getDerivedFrom()==null?form.getCurrentReport().getTitle(): form.getCurrentReport().getDerivedFrom().getTitle();
		String reportName = reportTitle.replaceAll("\\s+","");

		switch (SSRSReportsEnum.valueOf(reportName)){
		case LearnerPerformancebyCourse:
			parameters = new ParameterValue[3];

			try {
				String startDate = getStartDate(form);
				String endDate = getEndDate(form);

				parameters[0] = new ParameterValue();
				parameters[0].setName("StartDate");
				parameters[0].setValue(startDate);

				parameters[1] = new ParameterValue();
				parameters[1].setName("EndDate");
				parameters[1].setValue(endDate);

				parameters[2] = new ParameterValue();
				parameters[2].setName("CustomerId");
				parameters[2].setValue(getCustomerId().toString());

			} catch (ParseException e) {				
				log.error(e);
			}
			return parameters;

		case LearnerPerformancebyTrainingPlan:
			parameters = new ParameterValue[1];

			parameters[0] = new ParameterValue();
			parameters[0].setName("CustomerId");
			parameters[0].setValue(getCustomerId().toString());

			return parameters;
		}
		return null;
	}

	private void downloadSSRSExportedReport(String reportFile, String contentType, HttpServletResponse response){

		try{
			File f= new File(reportFile);
			FileInputStream in = new FileInputStream(f);

			response.setContentType(contentType);
			response.setContentLength( (int)f.length() );
			response.setHeader("Content-Disposition", "attachment; filename=\""+reportFile+"\"");

			ServletOutputStream ouputStream=response.getOutputStream();
			FileCopyUtils.copy(in, ouputStream);
			in.close();
			ouputStream.flush();
			ouputStream.close();

		}catch (Exception e){
			System.out.println(e);
		}
	}

	private BigDecimal getCustomerId(){
		Long customerId= ((VU360UserAuthenticationDetails)SecurityContextHolder.
				getContext().getAuthentication().getDetails()).getCurrentCustomerId();

		BigDecimal customerIdBig;

		if( customerId == null ){
			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			customerIdBig = new BigDecimal(user.getLearner().getCustomer().getId());
		}else{
			customerIdBig = new BigDecimal(customerId);
		}
		return customerIdBig;
	}
}
