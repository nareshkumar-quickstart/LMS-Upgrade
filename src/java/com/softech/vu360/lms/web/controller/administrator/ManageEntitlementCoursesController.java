package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ManageEntitlementCourseGroupsForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeCourseGroupTree;
import com.softech.vu360.util.TreeNode;

/**
 * Controller class for Managing Contract Courses.
 * 
 * @author muzammil.shaikh
 */
public class ManageEntitlementCoursesController extends VU360BaseMultiActionController {

	private static final Logger log = Logger.getLogger(ManageEntitlementCoursesController.class.getName());

	private String manageEntitlementCoursesTemplate = null;
	
	private EntitlementService entitlementService;
	private CourseAndCourseGroupService courseAndCourseGroupService;
	
	

	public ManageEntitlementCoursesController(){
		super();
	}
	
	public ManageEntitlementCoursesController(Object delegate){
		super(delegate);
	}
	
	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return displayEntitlementCourseGroupTree( request,  response);
	}
	
	/**
	 * Callback method for doing extra binding related stuff.
	 */
	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		
	}
	
	/**
	 * Generates the Contract Courses Tree object to be rendered in the view. 
	 * 
	 * @param request
	 * @param response
	 * @return	ModelAndView
	 * @author muzammil.shaikh
	 */
	public ModelAndView displayEntitlementCourseGroupTree(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			List<TreeNode> treeAsList = getEntitlementCourseGroupTreeNode(null);
			String contractId = request.getParameter("contractId") != null && !request.getParameter("contractId").trim().equals("") 
										? request.getParameter("contractId").trim() : "";
			
			CustomerEntitlement contract=entitlementService.getCustomerEntitlementById(new Long(contractId));
			
			
			
			if(contract.getEnrollmentType().equals("CourseGroup"))
				treeAsList=this.getTreeForContract((CourseGroupCustomerEntitlement)contract);
			else
				treeAsList=this.getTreeForContract((CourseCustomerEntitlement)contract);
										
			context.put("coursesTreeAsList", treeAsList);
			context.put("contractType", contract.getEnrollmentType());
			context.put("contractId", contractId);
			
			return new ModelAndView(manageEntitlementCoursesTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(manageEntitlementCoursesTemplate);
	}

	public ModelAndView saveCourseGroups(HttpServletRequest request, 
			HttpServletResponse response, Object command, BindException errors ) {
		
		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			ManageEntitlementCourseGroupsForm form = (ManageEntitlementCourseGroupsForm)command;
			String contractId = form.getContractId();
			String contractType = form.getContractType();
			String[] selectedCourseGroups = form.getCourseGroups();
			String[] selectedCourses = form.getCourses();
			
			if (selectedCourseGroups != null && selectedCourseGroups.length > 0){
				log.info(selectedCourseGroups.toString());
			}
			if (selectedCourses != null && selectedCourses.length > 0){
				log.info(selectedCourses.toString());
			}
			List<TreeNode> treeAsList = getEntitlementCourseGroupTreeNode(selectedCourseGroups);
	
			context.put("coursesTreeAsList", treeAsList);
			context.put("contractType", contractType);
			context.put("selectedCourses", selectedCourses);
			context.put("contractId", contractId);
			
			return new ModelAndView(manageEntitlementCoursesTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(manageEntitlementCoursesTemplate);
	}
	
	public ModelAndView removeCourseGroup(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors ) {

		try {
			Map<Object, Object> context = new HashMap<Object, Object>();
			ManageEntitlementCourseGroupsForm form = (ManageEntitlementCourseGroupsForm)command;
			String contractId = form.getContractId();
			String contractType = form.getContractType();
			String[] selectedCourseGroups = form.getCourseGroups();
			String[] selectedCourses = form.getCourses();
			
			if (selectedCourseGroups != null && selectedCourseGroups.length > 0)
				log.info(selectedCourseGroups.toString());
			if (selectedCourses != null && selectedCourses.length > 0)
				log.info(selectedCourses.toString());
			List<TreeNode> treeAsList = getEntitlementCourseGroupTreeNode(selectedCourseGroups);
	
			context.put("coursesTreeAsList", treeAsList);
			context.put("contractType", contractType);
			context.put("selectedCourses", selectedCourses);
			context.put("contractId", contractId);
			
			return new ModelAndView(manageEntitlementCoursesTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(manageEntitlementCoursesTemplate);
	}
	
	private List<TreeNode> getEntitlementCourseGroupTreeNode(String[] selectedCourseGroups) {
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
		if (customer != null && customer.getDistributor() != null) {
			List<DistributorEntitlement> distributorEntitlements = entitlementService.getAllDistributorEntitlements(customer.getDistributor());
			//List<TreeNode> courseGroupTreeNodes = new ArrayList<TreeNode>();
			for(DistributorEntitlement distributorEntitlement : distributorEntitlements){
				for (CourseGroup courseGroup : distributorEntitlement.getCourseGroups()) {
						TreeNode courseGroupTreeNode = ArrangeCourseGroupTree.getSelectedCourseGroupTree(null, courseGroup, selectedCourseGroups);
						List<TreeNode> courseGroupTreeNodeList = courseGroupTreeNode.bfs();
						
						for(TreeNode treeNode : courseGroupTreeNodeList){
							if (!treeNodesList.contains(treeNode))
								treeNodesList.add(treeNode);
						}
				}
			}
		}
		
		return treeNodesList;
	}
	
	
	private List<TreeNode> getTreeForContract(CourseGroupCustomerEntitlement entitlement) {
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		
		
		List<CourseGroupCustomerEntitlementItem> items = entitlementService.getItemsByEntitlement(entitlement);
		
		for (CourseGroupCustomerEntitlementItem item : items) {
						TreeNode courseGroupTreeNode = ArrangeCourseGroupTree.getSelectedCourseGroupTree(null, item.getCourseGroup(), null);
						List<TreeNode> courseGroupTreeNodeList = courseGroupTreeNode.bfs();
						
						for(TreeNode treeNode : courseGroupTreeNodeList){
							if (!treeNodesList.contains(treeNode))
								treeNodesList.add(treeNode);
						}
				}
		
		
		return treeNodesList;
	}
	
	
	/*
	 * reference: LMS-6741
	 */
	private List<TreeNode> getTreeForContract(CourseCustomerEntitlement entitlement) {
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		List<CourseCustomerEntitlementItem> items = entitlementService.getItemsByEntitlement(entitlement);
		String []strSelectedCourseIds =new String[items.size()] ;
		
		for(int i=0;i<strSelectedCourseIds.length; i++){
			strSelectedCourseIds[i]=items.get(i).getCourse().getId().toString();
		}
		
		TreeNode courseTreeNode = null;
		List<TreeNode> courseTreeNodeList = null;
		for (CourseCustomerEntitlementItem item : items) {
		
			courseTreeNode = ArrangeCourseGroupTree.getSelectedCourseGroupTree(null, item.getCourseGroup(), strSelectedCourseIds);
			courseTreeNodeList = courseTreeNode.bfs();
			
			for(TreeNode treeNode : courseTreeNodeList){
				if (!treeNodesList.contains(treeNode))
					treeNodesList.add(treeNode);
			}
		}

//		for (Course course : entitlement.getCourses()) {
//			for(CourseGroup courseGroup : courseAndCourseGroupService.getCourseGroupsForCourse(course)){
//						TreeNode courseTreeNode = ArrangeCourseGroupTree.getSelectedCourseGroupTree(null, courseGroup, strSelectedCourseIds);
//						List<TreeNode> courseTreeNodeList = courseTreeNode.bfs();
//						
//						for(TreeNode treeNode : courseTreeNodeList){
//							if (!treeNodesList.contains(treeNode))
//								treeNodesList.add(treeNode);
//						}
//				}
//		}
		
		
		return treeNodesList;
	}
	
	
	/**
	 * Callback method for doing validations before the processing method is executed.
	 * Be sure to check if there are any errors before doing anything
	 * in the processing method
	 */
	protected void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception {
		
	}

	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	public String getManageEntitlementCoursesTemplate() {
		return manageEntitlementCoursesTemplate;
	}

	public void setManageEntitlementCoursesTemplate(
			String manageEntitlementCoursesTemplate) {
		this.manageEntitlementCoursesTemplate = manageEntitlementCoursesTemplate;
	}

	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}
}	