package com.softech.vu360.lms.web.controller.instructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.instructor.SynchronousCourseGroupForm;
import com.softech.vu360.lms.web.controller.validator.AddCourseGroupValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;

public class AddCourseGroupController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddCourseGroupController.class.getName());
	private String finishTemplate = null;
	private String cancelTemplate = null;
	private CustomerService customerService = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService;
	private LearnerService learnerService;
	private CourseAndCourseGroupService courseAndCourseGroupService = null ;
	private AuthorService authorService = null;
	
	public AddCourseGroupController() {
		super();
		setCommandName("addCourseGroupForm");
		setCommandClass(SynchronousCourseGroupForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"instructor/manageSynchronousCourseGroup/addCourseGroup/Step1"
				, "instructor/manageSynchronousCourseGroup/addCourseGroup/Step2"});
	}

	protected Object formBackingObject(HttpServletRequest request) throws Exception {

		log.debug("IN formBackingObject");
		Object command = super.formBackingObject(request);
		return command;
	}

	protected Map<Object, Object> referenceData( HttpServletRequest request, Object command, 
			Errors errors, int page) throws Exception {

		log.debug("IN referenceData");
		SynchronousCourseGroupForm form = (SynchronousCourseGroupForm)command;		

		switch(page) {

		case 0:
			Map<Object,Object> context = new HashMap<Object, Object>();
			
			// [LMS-7106] Get Content Owner			
			VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
			ContentOwner contentOwner = user.getLearner().getCustomer().getContentOwner();
			if (contentOwner == null) {
				contentOwner = user.getLearner().getCustomer().getDistributor().getContentOwner();
			}
			 
			if(contentOwner == null){
				contentOwner = authorService.addContentOwnerIfNotExist(user.getLearner().getCustomer(), user.getId());
				Customer newCustomer = this.getCustomerService().loadForUpdateCustomer(user.getLearner().getCustomer().getId());
				newCustomer.setContentOwner(contentOwner);
				this.getCustomerService().updateCustomer(newCustomer);
			}
			
			List<CourseGroup> courseGroupList = courseAndCourseGroupService.getAllCourseGroupsByContentOwnerId(contentOwner.getId());
			
			// Create Tree for display
			List<List<TreeNode>> courseGroupTreeList = this.courseAndCourseGroupService.getCourseGroupTreeList( courseGroupList, true );				
						
			context.put("courseGroupTreeList", courseGroupTreeList);
			return context;

		case 1:
			
			if( form.getGroups() != null ) {
				String[] orgGroupIds = form.getGroups();
				if ( orgGroupIds.length > 0 ){
					Long courseGroupId = Long.parseLong(orgGroupIds[0]);
					CourseGroup parentGroup = courseAndCourseGroupService.getCourseGroupById(courseGroupId);
					form.setParentGroupName(parentGroup.getName());
					form.setParentCourseGroupId( parentGroup.getId());
					form.setCourseGroupId(courseGroupId);
				} else
				{
					form.setParentGroupName("");
				}
			}
			break;
			
		default :
			break;
		}

		return super.referenceData(request, page);
	}	

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException excep) throws Exception {
		SynchronousCourseGroupForm form = (SynchronousCourseGroupForm)command;
		
		CourseGroup courseGroup = new CourseGroup();
		courseGroup.setName(form.getNewGroupName());
		courseGroup.setKeywords("");
		
		// [LMS-7106] Get Content Owner			
		VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
		ContentOwner contentOwner = user.getLearner().getCustomer().getContentOwner();
		if (contentOwner == null) {
			contentOwner = user.getLearner().getCustomer().getDistributor().getContentOwner();
		}	
		
		courseGroup.setContentOwner(contentOwner);
		
		CourseGroup parentGroup = courseAndCourseGroupService.getCourseGroupById(form.getParentCourseGroupId());
		if( parentGroup  != null ){
			courseGroup.setParentCourseGroup(parentGroup );
		}
		
		CourseGroup cg = courseAndCourseGroupService.saveCourseGroup(courseGroup);
		if( parentGroup  != null && cg !=null ){
			parentGroup.getChildrenCourseGroups().add(cg);
		}
		return new ModelAndView(finishTemplate);
	}

	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors, int page) throws Exception {

		AddCourseGroupValidator validator = (AddCourseGroupValidator )this.getValidator();
		SynchronousCourseGroupForm form = (SynchronousCourseGroupForm)command;
		
		switch(page) {
		case 0:
			validator.validateFirstPage(form, errors);
			break;
		case 1:
			break;
		default:
			break;
		}	
		super.onBindAndValidate(request, command, errors, page);
	}

	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {
		log.debug("PAGE - "+page);
		super.postProcessPage(request, command, errors, page);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)	throws Exception {
		return new ModelAndView(cancelTemplate);
		//return super.processCancel(request, response, command, errors);
	}
	
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	public AuthorService getAuthorService() {
		return authorService;
	}

	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	
}