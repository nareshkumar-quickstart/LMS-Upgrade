package com.softech.vu360.lms.web.controller.accreditation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialForm;
import com.softech.vu360.lms.web.controller.model.accreditation.CredentialProctors;
import com.softech.vu360.util.CredentialProctorSort;
import com.softech.vu360.util.FormUtil;

//AbstractWizardFormController
public class AddProctorsInCredentialController extends AbstractWizardFormController{

	private static final Logger log = Logger.getLogger(AddProctorsInCredentialController.class.getName());
	
	private String showTrainingCoursesForm;
	private String showTrainingCourses;
	private String showProctors;
	
	private String cancelTemplate = null;
	private String finishTemplate = null;
	
	private AccreditationService accreditationService = null;
	//private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private ProctorService proctorService;
	
	public AddProctorsInCredentialController() {
		super();
		setCommandName("credentialForm");
		setCommandClass(CredentialForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/Credential/proctor/addProctorsInCredential_step1" 
				, "accreditation/Credential/proctor/addProctorsInCredential_step2" 
		}); 
	}
	
	
	protected Object formBackingObject( HttpServletRequest request ) throws Exception {
		Object command = super.formBackingObject(request);
		CredentialForm form=(CredentialForm) command;

			long courseApprovalId = Long.parseLong(request.getParameter("credentialID"));
			Credential credential = accreditationService.loadForUpdateCredential(courseApprovalId);
			form.setCredential(credential);
		

		return command;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#onBindAndValidate(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.BindException, int)
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors, int page) throws Exception {
		super.onBindAndValidate(request, command, errors, page);
	}
	
	
	
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {
	
		CredentialForm form = (CredentialForm)command;
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
		HttpSession session = null;
		session = request.getSession();
		
		String firstName = form.getFirstName();
		String lastName =  form.getLastName();
		String email =  form.geteMailAddress();
		
		// CompanyName filter point to Customer name in DB
		String companyName =  form.getCompanyName();
		String startDate =  form.getStartDate();
		String endDate =  form.getEndDate();
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dtStartDate = null, dtEndDate = null;
		
		if(form.getGridAction().equalsIgnoreCase("search")){
			
			if (!form.getStartDate().equals("")) {
				try {
					dtStartDate = sdf.parse(startDate);
				} catch (ParseException e) {
					errors.rejectValue("startDate", "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
					return super.referenceData(request, page);
				}
			}
			
			if (!form.getEndDate().equals("")) {
				try {
					dtEndDate = sdf.parse(endDate);
				} catch (ParseException e) {
					errors.rejectValue("startDate", "lms.proctor.proctorReport.error.IncorrectStartorEndDate.Message");
					return super.referenceData(request, page);
				}
			} 
		}


		String sortDirection="0",  pageIndex="0";
		List<LearnerEnrollment> lstLearnerEnrollment = new ArrayList<LearnerEnrollment>();
		
		
		if (page == 0) {
			
			 // START -- I add courses in Credential, training courses List wouldn't display which are already exists in Credential
			 // get all training courses Is from Credential and remove from List
			 List<Proctor> lstProctors = accreditationService.getAllProctorsByCredential(form.getCredential().getId());
			 int i = -1;
			 Long[] arrProctorAlreadyExist = new Long[lstProctors.size()];
			 for (Proctor proctor : lstProctors)
				 //if(proctor.getStatus().equalsIgnoreCase(ProctorStatusEnum.Active.toString()))
					 arrProctorAlreadyExist[++i]=proctor.getUser().getId();
				 //else
				 //	 arrProctorAlreadyExist[++i] = new Long(-1);
			 lstProctors = null;
			// END -------------------------------------------------------------------------------
			
			 
			 
			lstLearnerEnrollment = this.proctorService.getLearnersByCredentialTrainingCourse(form.getCredential(), firstName, lastName, email, companyName, dtStartDate, dtEndDate, arrProctorAlreadyExist);
			List<CredentialProctors> lstCredProctors=null;			
				 
					String sortColumnIndex = form.getSortColumnIndex();
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					try{
						 sortDirection = String.valueOf(form.getSortDirection());
					}catch(Exception exp){
						 sortDirection= "0";
					}
					
					
					if (lstLearnerEnrollment != null) {
						lstCredProctors = new ArrayList<CredentialProctors>();
						
						List<CredentialProctors> appCredentialList =  form.getCredentialProctors();
						CredentialProctors cProctor = null;
						
						for (LearnerEnrollment learnerEnrollment : lstLearnerEnrollment) {
							boolean sel = false;
							for( CredentialProctors regCred : appCredentialList ) {
								if( learnerEnrollment.getId().compareTo(regCred.getId()) == 0 ) {
									sel = regCred.isSelected();
									break;
								}
							}
							
							
							cProctor = new CredentialProctors();
							cProctor.setId(learnerEnrollment.getId());
							cProctor.setFirstName(learnerEnrollment.getLearner().getVu360User().getFirstName());
							cProctor.setLastName(learnerEnrollment.getLearner().getVu360User().getLastName());
							cProctor.setEmail(learnerEnrollment.getLearner().getVu360User().getEmailAddress());
							cProctor.setVu360UserId(learnerEnrollment.getLearner().getVu360User().getId());
							cProctor.setUserName(learnerEnrollment.getLearner().getVu360User().getUsername());
							cProctor.setCompanyName(learnerEnrollment.getLearner().getCustomer().getName());
							cProctor.setCourseId(learnerEnrollment.getCourse().getBussinesskey());
							cProctor.setSelected(sel);
							if(learnerEnrollment.getCourseStatistics().getCompletionDate()!=null)
								cProctor.setCompletionDate(FormUtil.getInstance().formatDate(learnerEnrollment.getCourseStatistics().getCompletionDate(), "MM/dd/yyyy hh:mm aaa z"));
							
							
							lstCredProctors.add(cProctor);
						}
						
					}
					
					
					
					// Sorting collection
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					 pageIndex = form.getPageCurrIndex();
					if( pageIndex == null ) pageIndex = form.getPageIndex();
					
					if( sortColumnIndex != null && sortDirection != null ) {

						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("firstName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(0);
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
							} else {
								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("firstName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(1);
								form.setSortColumnIndex("0");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
							}
							
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("lastName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(0);
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
							} else {
								
								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("lastName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(1);
								form.setSortColumnIndex("1");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
							}
							
						} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("email");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(0);
								form.setSortColumnIndex("2");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 2);
							} else {
								
								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("email");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(1);
								form.setSortColumnIndex("2");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 2);
							}
						}else if( sortColumnIndex.equalsIgnoreCase("3") ) {
								if( sortDirection.equalsIgnoreCase("0") ) {

									CredentialProctorSort sort = new CredentialProctorSort();
									sort.setSortBy("companyName");
									sort.setSortDirection(Integer.parseInt(sortDirection));
									Collections.sort(lstCredProctors,sort);
									form.setSortDirection(0);
									form.setSortColumnIndex("3");
									session.setAttribute("sortDirection", 0);
									session.setAttribute("sortColumnIndex", 3);
								} else {
									
									CredentialProctorSort sort = new CredentialProctorSort();
									sort.setSortBy("companyName");
									sort.setSortDirection(Integer.parseInt(sortDirection));
									Collections.sort(lstCredProctors,sort);
									form.setSortDirection(1);
									form.setSortColumnIndex("3");
									session.setAttribute("sortDirection", 1);
									session.setAttribute("sortColumnIndex", 3);
								}
							
						} else if( sortColumnIndex.equalsIgnoreCase("4") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("courseId");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(0);
								form.setSortColumnIndex("4");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 4);
							} else {
								
								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("courseId");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(1);
								form.setSortColumnIndex("4");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 4);
							}
						}else if( sortColumnIndex.equalsIgnoreCase("5") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {

								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("completionDate");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(0);
								form.setSortColumnIndex("5");
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 5);
							} else {
								
								CredentialProctorSort sort = new CredentialProctorSort();
								sort.setSortBy("completionDate");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(lstCredProctors,sort);
								form.setSortDirection(1);
								form.setSortColumnIndex("5");
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 5);
							}
						}
						
					}	
					form.setCredentialProctors(lstCredProctors);
				}
				pagerAttributeMap.put("showAll", form.getShowAll());
				pagerAttributeMap.put("pageIndex", pageIndex);
				request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				return super.referenceData(request, page);
			}


	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractWizardFormController#postProcessPage(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)
	 */
	protected void validatePage(Object command, Errors errors, int page, boolean finish) {
		CredentialForm form = (CredentialForm)command;
		errors.setNestedPath("");
		switch(page) {
		
		case 0:
			if(!form.getGridAction().equalsIgnoreCase("search")){
				List<CredentialProctors> appCredentialList =  form.getCredentialProctors();
			
				boolean sel = true;
				if(appCredentialList!=null){
					for( CredentialProctors regCred : appCredentialList ) {
						if( regCred.isSelected()) {
							sel = false;
							break;
						}
					}
				}
				if(sel)
					errors.rejectValue("trainingCourseName", "error.Credential.addProctor.notselected");
			}
			
			break;
		default:
			break;
		}
		super.validatePage(command, errors, page, finish);
	}
	@Override
	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(cancelTemplate);
	}
	
	@Override
	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
	
		CredentialForm form = (CredentialForm)command;
		Credential credential = form.getCredential();
		List<CredentialProctors> lstProctor = form.getCredentialProctors();
		
		proctorService.saveProctorWithCredential(lstProctor, credential);
		return new ModelAndView(finishTemplate);
	}
	
	public String getShowTrainingCourses() {
		return showTrainingCourses;
	}

	public void setShowTrainingCourses(String showTrainingCourses) {
		this.showTrainingCourses = showTrainingCourses;
	}

	public String getShowProctors() {
		return showProctors;
	}

	public void setShowProctors(String showProctors) {
		this.showProctors = showProctors;
	}

	public String getShowTrainingCoursesForm() {
		return showTrainingCoursesForm;
	}

	public void setShowTrainingCoursesForm(String showTrainingCoursesForm) {
		this.showTrainingCoursesForm = showTrainingCoursesForm;
	}

	
	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}
	
	
	
	@Override
	protected int getTargetPage(HttpServletRequest request, Object command, Errors errors, int currentPage) {
	        return super.getTargetPage(request, command, errors, currentPage);
	}


	public AccreditationService getAccreditationService() {
		return accreditationService;
	}


	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}


	public ProctorService getProctorService() {
		return proctorService;
	}


	public void setProctorService(ProctorService proctorService) {
		this.proctorService = proctorService;
	}
	
	
}