package com.softech.vu360.lms.web.controller.administrator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;

/**
 * This controller manages the assignment and removal of course groups to the 
 * distributor entitlement.
 * 
 * @author abdul.aziz
 */
public class ManageAndEditDistributorContractController extends VU360BaseMultiActionController {

    private static final Logger logger = Logger.getLogger(
            ManageAndEditDistributorContractController.class.getName());
    protected String showContractItemTemplate = null;
    protected String updateEntitlementTemplate = null;
    protected String addCourseTemplate = null;
    protected String failureTemplate = null;
    protected EntitlementService entitlementService = null;
    protected CourseAndCourseGroupService courseGroupService = null;

    @Override
    protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
        String strEntitlementId = request.getParameter("distributorEntitlementId");
        logger.debug("onBind - entitlementId: " + strEntitlementId);

        if (NumberUtils.isNumber(strEntitlementId)) {
            //We don't want to refresh the reseller entitlement if we are saving it.
            if (StringUtils.isNotEmpty(methodName) && "saveContract".equalsIgnoreCase(methodName) == false) {
                DistributorEntitlement entitlement = (DistributorEntitlement) command;
                //Perf. optimization. We don't want to ReadAndCopy DistributorEntitlement everytime since
                //the object is very heavy.
                Long entitlementId = Long.valueOf(strEntitlementId);
                if (null == entitlement || entitlement.getId() == null
                        || entitlementId.equals(entitlement.getId()) == false
                        || "showContractItems".equalsIgnoreCase(methodName)) {
                    long startTime = System.currentTimeMillis();
                    DistributorEntitlement distributorEntitlement = entitlementService.loadForUpdateDistributorEntitlement(entitlementId);
                    long finishedTime = System.currentTimeMillis();

                    logTimeTaken(entitlementId, startTime, finishedTime);
                    initializeCommand(entitlement, distributorEntitlement);
                }
            }
        }
    }

    @Override
    protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
    }

    /**
     * @param showContractItemTemplate the showContractItemTemplate to set
     */
    public void setShowContractItemTemplate(String showContractItemTemplate) {
        this.showContractItemTemplate = showContractItemTemplate;
    }

    public ModelAndView addCourseGroups(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
        DistributorEntitlement distributorEntitlement = (DistributorEntitlement) command;

        String[] strCourseGroupIds = request.getParameterValues("courseGroups");

        logger.debug("courseGroupIds: " + strCourseGroupIds);

        if (ArrayUtils.isEmpty(strCourseGroupIds) == false) {
            logger.debug("Adding course groups: " + strCourseGroupIds.length);

            long[] courseGroupIds = new long[strCourseGroupIds.length];
            for (int i = 0; i < strCourseGroupIds.length; i++) {
                courseGroupIds[i] = Long.valueOf(strCourseGroupIds[i]);
            }

            List<CourseGroup> courseGroups = courseGroupService.getCourseGroupsByIds(courseGroupIds);

            distributorEntitlement.getCourseGroups().addAll(courseGroups);
        }

        return showContractItems(request, response, command, errors);
    }

    public ModelAndView removeCourseGroup(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
        DistributorEntitlement distributorEntitlement = (DistributorEntitlement) command;

        List<CourseGroup> courseGroups = distributorEntitlement.getCourseGroups();
        List<CourseGroup> copiedCourseGroup = new CopyOnWriteArrayList<CourseGroup>(courseGroups);

        logger.debug("Contractual courses: " + copiedCourseGroup.size());

        String[] courseGroupIds = request.getParameterValues("courseGroups");

        logger.debug("Selected Course Group Ids: " + courseGroupIds.length);

        for (String courseGroupIdStr : courseGroupIds) {
            Long courseGroupId = Long.valueOf(courseGroupIdStr);
            logger.debug("Request course group id: " + courseGroupId);

            for (CourseGroup courseGroup : copiedCourseGroup) {
                if (courseGroupId.equals(courseGroup.getId())) {
                    logger.debug("Id matched: " + courseGroupId);
                    copiedCourseGroup.remove(courseGroup);
                    break;
                }
            }
        }

        logger.debug("Contractual courses: " + copiedCourseGroup.size());

        distributorEntitlement.setCourseGroups(copiedCourseGroup);

        return showContractItems(request, response, command, errors);
    }

    public ModelAndView saveContract(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {

        DistributorEntitlement distributorEntitlement = (DistributorEntitlement) command;
        
        // Condition Added because of LMS-12491 - Rehan Rana - 11 Jan, 2012
        if (null == distributorEntitlement || distributorEntitlement.getId() == null) 
        	return new ModelAndView("redirect:/adm_distributorEntitlements.do");
        
        entitlementService.saveDistributorEntitlement(distributorEntitlement);

        return new ModelAndView("redirect:adm_updateDistributorEntitlement.do?"
                + "distributorEntitlementId=" + distributorEntitlement.getId());
    }

    /**
     * Provides a page from the user can select from course groups available to the 
     * Distributor.
     * 
     * @param request
     * @param response
     * @param command
     * @param errors
     * @return
     * @throws Exception 
     */
    public ModelAndView showCoursesToAdd(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
        Map context = new HashMap();
        context.put("pageNo", 0);
        context.put("totalRecord", 0);
        context.put("recordShowing", 0);

        DistributorEntitlement distributorEntitlement = (DistributorEntitlement) command;
        context.put("distributorEntitlementId", distributorEntitlement.getId());
        context.put("contract", distributorEntitlement);

        return new ModelAndView(addCourseTemplate, "context", context).
                addObject("distributorEntitlementId", distributorEntitlement.getId()).
                addObject("contract", distributorEntitlement);
    }

    public ModelAndView showContractItems(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
        ModelAndView mnv = redierctIfNotDistributor();

        DistributorEntitlement distributorEntitlement = (DistributorEntitlement) command;

        request.getSession(true).setAttribute("feature", "LMS-ADM-0022");

        if (null != mnv) {
            return mnv;
        } else {
            Set<CourseGroup> contractCourseGroups = new HashSet<CourseGroup>();

            List<TreeNode> treeAsList = entitlementService.getCourseGroupTreeForDistributorEntitlement(
                    distributorEntitlement, contractCourseGroups);

            Map<Object, Object> context = new HashMap<Object, Object>();
            context.put("contractCourseGroups", contractCourseGroups);
            context.put("coursesTreeAsList", treeAsList);
            context.put("contractType", "CourseGroup");
            context.put("contractId", distributorEntitlement.getId());

            mnv = new ModelAndView(showContractItemTemplate, "context", context);
            mnv.addObject("distributorEntitlementId", distributorEntitlement.getId());
            mnv.addObject("contract", distributorEntitlement);
        }

        return mnv;
    }

    public ModelAndView cancelAddCourseGroup(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
        return showContractItems(request, response, command, errors);
    }

    public ModelAndView searchCourseGroups(HttpServletRequest request, HttpServletResponse response,
            Object command, BindException errors) throws Exception {
        DistributorEntitlement entitlement = (DistributorEntitlement) command;
        
        // Condition Added because of LMS-12491 - Rehan Rana - 11 Jan, 2012
        if (null == entitlement || entitlement.getId() == null) 
        	return new ModelAndView("redirect:/adm_distributorEntitlements.do");
        
        
        String keywords = request.getParameter("keywords").trim();
        String title = request.getParameter("title").trim();
        String entityId = request.getParameter("courseGroupID").trim();

        Map context = new HashMap();
        List<TreeNode> courseGroupTree = entitlementService.searchCourseGroups(
                title, entityId, keywords);

        context.put("courseGroupTree", courseGroupTree);
        if (courseGroupTree == null) {
            String[] error = {"error.admin.customerEnt.course.errorMsg1", "error.admin.customerEnt.course.errorMsg2", "error.admin.customerEnt.course.errorMsg3"};
            context.put("error", error);
        }

        context.put("callMacroForChildren", "false");
        context.put("contractType", "CourseGroup");
        context.put("contractId", entitlement.getId());
        context.put("pageNo", 0);
        context.put("totalRecord", courseGroupTree.size());
        context.put("recordShowing", courseGroupTree.size());
        context.put("distributorEntitlementId", entitlement.getId());
        context.put("contract", entitlement);
        ModelAndView modelAndView = new ModelAndView(addCourseTemplate, "context", context);

        modelAndView.addObject("contract", entitlement);
        modelAndView.addObject("distributorEntitlementId", entitlement.getId());

        return modelAndView;
    }

    /**
     * Sets up the view to be redirected to the administrator search page
     * if the user is not distributor.
     * @return 
     */
    private ModelAndView redierctIfNotDistributor() {
        ModelAndView mnv = null;

        Map<Object, Object> context = new HashMap<Object, Object>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Distributor distributor = null;
        if (auth.getDetails() != null && auth.getDetails() instanceof VU360UserAuthenticationDetails) {
            VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) auth.getDetails();
            if (details.getCurrentDistributor() != null) {
                distributor = details.getCurrentDistributor();
                context.put("selectedDistributor", distributor);
            } else {
                mnv = new ModelAndView(failureTemplate, "isRedirect", "d");
            }
        } else {
            // admin has not selected any distributor
            mnv = new ModelAndView(failureTemplate, "isRedirect", "d");
        }

        return mnv;
    }

    /**
     * @param entitlementService the entitlementService to set
     */
    public void setEntitlementService(EntitlementService entitlementService) {
        this.entitlementService = entitlementService;
    }

    /**
     * @param failureTemplate the failureTemplate to set
     */
    public void setFailureTemplate(String failureTemplate) {
        this.failureTemplate = failureTemplate;
    }

    /**
     * @param updateEntitlementTemplate the updateEntitlementTemplate to set
     */
    public void setUpdateEntitlementTemplate(String viewDistributorEntitlementTemplate) {
        this.updateEntitlementTemplate = viewDistributorEntitlementTemplate;
    }

    /**
     * @param addCourseTemplate the addCourseTemplate to set
     */
    public void setAddCourseTemplate(String addCourseTemplate) {
        this.addCourseTemplate = addCourseTemplate;
    }

    /**
     * @param courseGroupService the courseGroupService to set
     */
    public void setCourseGroupService(CourseAndCourseGroupService courseGroupService) {
        this.courseGroupService = courseGroupService;
    }

    /**
     * Logs the time taken to load the distributor entitlement object.
     * @param entitlementId
     * @param startTimeMillis
     * @param finishedTimeMillis 
     */
    private void logTimeTaken(long entitlementId, long startTimeMillis, long finishedTimeMillis) {
        long timeSpent = finishedTimeMillis - startTimeMillis;
        String timeTaken = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(timeSpent),
                TimeUnit.MILLISECONDS.toSeconds(timeSpent)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeSpent)));

        logger.debug(
                String.format("Time taken to load distributorEntitlement "
                + "with id: %d %s.", entitlementId, timeTaken));
    }

    /**
     * Initializes the command object with the object loaded from the database.
     * @param entitlement command object.
     * @param distributorEntitlement object loaded from the database.
     */
    private void initializeCommand(DistributorEntitlement entitlement,
            DistributorEntitlement distributorEntitlement) {
        entitlement.setAllowSelfEnrollment(distributorEntitlement.isAllowSelfEnrollment());
        entitlement.setAllowUnlimitedEnrollments(distributorEntitlement.isAllowUnlimitedEnrollments());
        entitlement.setCourseGroups(distributorEntitlement.getCourseGroups());
        entitlement.setDefaultTermOfServiceInDays(distributorEntitlement.getDefaultTermOfServiceInDays());
        entitlement.setDistributor(distributorEntitlement.getDistributor());
        entitlement.setEndDate(distributorEntitlement.getEndDate());
        entitlement.setId(distributorEntitlement.getId());
        entitlement.setMaxNumberSeats(distributorEntitlement.getMaxNumberSeats());
        entitlement.setName(distributorEntitlement.getName());
        entitlement.setNumberSeatsUsed(distributorEntitlement.getNumberSeatsUsed());
        entitlement.setStartDate(distributorEntitlement.getStartDate());
    }
}
