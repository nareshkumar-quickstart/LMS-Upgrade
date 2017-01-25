package com.softech.vu360.lms.web.controller.instructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.MultiSelectCreditReportingField;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.SingleSelectCreditReportingField;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;
import com.softech.vu360.lms.web.controller.model.customfield.CustomFieldBuilder;
import com.softech.vu360.lms.web.controller.model.instructor.CourseCourseStatisticsPair;
import com.softech.vu360.lms.web.controller.model.instructor.InsSearchMember;
import com.softech.vu360.lms.web.controller.model.instructor.InsmodeLearnerSearchForm;
import com.softech.vu360.util.InsmodeLearnerSort;

/**
 * @author Dyutiman
 * created on 6 Apr 2010
 *
 */
public class SearchLearnerController extends VU360BaseMultiActionController {

	private String searchLearnerTemplate = null;
	private String redirectTemplate = null;
	private String profileTemplate = null;
	private String transcriptTemplate = null;

	private ResourceService resourceService;
	private LearnerService learnerService;
	private EntitlementService entitlementService;
	private CustomFieldService customFieldService;
//	HttpSession session = null;

	public SearchLearnerController() {
		super();
	}

	public SearchLearnerController(Object delegate) {
		super(delegate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {
		if( command instanceof  InsmodeLearnerSearchForm ) {
			InsmodeLearnerSearchForm form = (InsmodeLearnerSearchForm)command;
			if( methodName.equalsIgnoreCase("searchMembers") ) {
				//form.reset();
			}else if( methodName.equalsIgnoreCase("showProfile") ) {
				String learnerId = request.getParameter("Id");
				Learner selectedLearner = learnerService.getLearnerByID(Long.parseLong(learnerId));
				form.setLearnerId(Long.parseLong(learnerId));
				form.setUser(selectedLearner.getVu360User());

				/*  Getting the credit reporting fields and the custom fields
				 *  for the selected user...
				 */
				if( selectedLearner.getVu360User() != null ) {

					CreditReportingFieldBuilder fieldBuilder = new CreditReportingFieldBuilder();
					List<CreditReportingField> customFieldList = this.getLearnerService().getCreditReportingFieldsByLearnerCourseApproval(selectedLearner);
					List<CreditReportingFieldValue> customFieldValueList = this.getLearnerService().getCreditReportingFieldValues(selectedLearner);

					Map<Long,List<CreditReportingFieldValueChoice>> existingCreditReportingFieldValueChoiceMap = new HashMap<Long,List<CreditReportingFieldValueChoice>>();

					for( CreditReportingField creditReportingField : customFieldList ) {
						if( creditReportingField instanceof SingleSelectCreditReportingField || 
								creditReportingField instanceof MultiSelectCreditReportingField ) {

							List<CreditReportingFieldValueChoice> creditReportingFieldValueOptionList = this.getLearnerService().getChoicesByCreditReportingField(creditReportingField);
							fieldBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList,creditReportingFieldValueOptionList);

							if( creditReportingField instanceof MultiSelectCreditReportingField ) {
								CreditReportingFieldValue creditReportingFieldValue = this.getCreditReportingFieldValueByCreditReportingField(creditReportingField, customFieldValueList);
								existingCreditReportingFieldValueChoiceMap.put(creditReportingField.getId(), creditReportingFieldValue.getCreditReportingValueChoices());
							}

						} else {
							fieldBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList);
						}
					}
					List<com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField> creditReportingFields = fieldBuilder.getCreditReportingFieldList();

					for( com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : creditReportingFields ) {
						if( field.getCreditReportingFieldRef() instanceof MultiSelectCreditReportingField ) {
							List<CreditReportingFieldValueChoice> existingChoices = existingCreditReportingFieldValueChoiceMap.get(field.getCreditReportingFieldRef().getId());
							Map<Long,CreditReportingFieldValueChoice> existingChoicesMap = new HashMap<Long,CreditReportingFieldValueChoice>();

							for (CreditReportingFieldValueChoice choice : existingChoices){
								existingChoicesMap.put(choice.getId(), choice);
							}
							for (com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldValueChoice tempChoice : field.getCreditReportingFieldValueChoices()){
								if(existingChoicesMap.containsKey(tempChoice.getCreditReportingFieldValueChoiceRef().getId())){
									tempChoice.setSelected(true);
								}
							}
						}
					}
					form.setCreditReportingFields(creditReportingFields);

					List<CustomFieldValue> customFieldValues = new ArrayList<CustomFieldValue>();
					List<CustomField> totalCustomFieldList = new ArrayList<CustomField>();
					Distributor reseller = selectedLearner.getCustomer().getDistributor();
					CustomFieldBuilder fieldBuilder2 = new CustomFieldBuilder();
					customFieldValues = selectedLearner.getLearnerProfile().getCustomFieldValues() ;
					totalCustomFieldList.addAll(selectedLearner.getCustomer().getCustomFields());
					totalCustomFieldList.addAll(reseller.getCustomFields());

					Map<Long,List<CustomFieldValueChoice>> existingCustomFieldValueChoiceMap = new HashMap<Long,List<CustomFieldValueChoice>>();

					for( CustomField customField : totalCustomFieldList ) {
						if( customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ) {
							List<CustomFieldValueChoice> customFieldValueChoices = this.getCustomFieldService().getOptionsByCustomField(customField);
							fieldBuilder2.buildCustomField(customField,0,customFieldValues,customFieldValueChoices);

							if( customField instanceof MultiSelectCustomField ) {
								CustomFieldValue customFieldValue = this.getCustomFieldValueByCustomField(customField, customFieldValues);
								existingCustomFieldValueChoiceMap.put(customField.getId(), customFieldValue.getValueItems());
							}
						} else {
							fieldBuilder2.buildCustomField(customField,0,customFieldValues);
						}
					}

					List<com.softech.vu360.lms.web.controller.model.customfield.CustomField> customFieldList2 = fieldBuilder2.getCustomFieldList();

					for( com.softech.vu360.lms.web.controller.model.customfield.CustomField customField : customFieldList2 ) {
						if( customField.getCustomFieldRef() instanceof MultiSelectCustomField ) {
							List<CustomFieldValueChoice> existingCustomFieldValueChoiceList = existingCustomFieldValueChoiceMap.get(customField.getCustomFieldRef().getId());
							Map<Long,CustomFieldValueChoice> tempChoiceMap = new HashMap<Long,CustomFieldValueChoice>();

							for(CustomFieldValueChoice customFieldValueChoice :existingCustomFieldValueChoiceList){
								tempChoiceMap.put(customFieldValueChoice.getId(), customFieldValueChoice);
							}
							for (com.softech.vu360.lms.web.controller.model.customfield.CustomFieldValueChoice customFieldValueChoice : customField.getCustomFieldValueChoices()){
								if (tempChoiceMap.containsKey(customFieldValueChoice.getCustomFieldValueChoiceRef().getId())){
									customFieldValueChoice.setSelected(true);
								}
							}
						}
					}
					form.setCustomFields(customFieldList2);
				}
			}else if( methodName.equalsIgnoreCase("showTranscript") ) {
				String learnerId = request.getParameter("Id");
				Learner selectedLearner = learnerService.getLearnerByID(Long.parseLong(learnerId));
				if( selectedLearner != null ) {
					form.setLearnerId(Long.parseLong(learnerId));
					form.setUser(selectedLearner.getVu360User());
				}
			}
		}
	}

	protected void validate(HttpServletRequest request, Object command,	BindException errors, 
			String methodName) throws Exception {
		// do nothing
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod( NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> context = new HashMap <String, String>();
		context.put("target", "loadPage");
		return new ModelAndView(redirectTemplate,"context",context);
	}
	
	public ModelAndView loadPage(HttpServletRequest request, HttpServletResponse response, 
			Object command,	BindException errors) throws Exception {
		return new ModelAndView(searchLearnerTemplate);
	}

	/**
	 * Method used to search instructor mode learners...
	 */
	public ModelAndView searchMembers(HttpServletRequest request, HttpServletResponse response, 
			Object command,	BindException errors) throws Exception {

//		session = request.getSession();
		String sortDirection = request.getParameter("sortDirection");
//		if( sortDirection == null && session.getAttribute("sortDirection") != null )
//			sortDirection = session.getAttribute("sortDirection").toString();
		String sortColumnIndex = request.getParameter("sortColumnIndex");
//		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		if(sortDirection == null )sortDirection ="0";
		if(sortColumnIndex == null )sortColumnIndex ="0";
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		if ( showAll.isEmpty() ) showAll = "true";

		Map<Object, Object> context = new HashMap<Object, Object>();

		InsmodeLearnerSearchForm form = (InsmodeLearnerSearchForm)command;
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		List<InsSearchMember> adminSearchMemberList = new ArrayList<InsSearchMember>();

		HashSet<VU360User> searchedUsers = resourceService.findLearnersByInstructor(form.getSearchFirstName(), form.getSearchLastName(),
				form.getSearchEmailAddress(), loggedInUser.getInstructor().getId());

		for( VU360User vu360User : searchedUsers ) {
			InsSearchMember adminSearchMember = new InsSearchMember();
			adminSearchMember.setId(vu360User.getLearner().getId().toString());
			adminSearchMember.setFirstName(vu360User.getName());
			adminSearchMember.setLastName(vu360User.getName());
			adminSearchMember.setEMail(vu360User.getEmailAddress());
			adminSearchMemberList.add(adminSearchMember);
		}
		form.setInsSearchLernerList(adminSearchMemberList);

		context.put("showAll", showAll);
		String pageIndex = request.getParameter("pageCurrIndex");
		if( pageIndex == null ) pageIndex = "0";
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();
		PagerAttributeMap.put("pageIndex", pageIndex);

		// manually sorting
		if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex) ) {

			request.setAttribute("PagerAttributeMap", PagerAttributeMap);

			// sorting against FIRST NAME
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					InsmodeLearnerSort insSort = new InsmodeLearnerSort();
					insSort.setSortBy("firstName");
					insSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(adminSearchMemberList,insSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					InsmodeLearnerSort insSort = new InsmodeLearnerSort();
					insSort.setSortBy("firstName");
					insSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(adminSearchMemberList,insSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against LAST NAME
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					InsmodeLearnerSort insSort = new InsmodeLearnerSort();
					insSort.setSortBy("lastName");
					insSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(adminSearchMemberList,insSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					InsmodeLearnerSort insSort = new InsmodeLearnerSort();
					insSort.setSortBy("lastName");
					insSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(adminSearchMemberList,insSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
				// sorting against E-MAIL ADDRESS
			} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					InsmodeLearnerSort insSort = new InsmodeLearnerSort();
					insSort.setSortBy("email");
					insSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(adminSearchMemberList,insSort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 2);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 2);
				} else {
					InsmodeLearnerSort insSort = new InsmodeLearnerSort();
					insSort.setSortBy("email");
					insSort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(adminSearchMemberList,insSort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 2);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 2);
				}
			}
		}					
		form.setInsSearchLernerList(adminSearchMemberList);				
		return new ModelAndView(searchLearnerTemplate, "context", context);
	}

	/**
	 * Method used to show details of any learner.
	 * However, details are shown in non editable mode. i.e., can not be modified. 
	 */
	public ModelAndView showProfile(HttpServletRequest request, HttpServletResponse response, 
			Object command,	BindException errors) throws Exception {
		return new ModelAndView(profileTemplate);
	}

	/**
	 *  Method used to show learner transcripts. i.e., performance against each course
	 */
	public ModelAndView showTranscript(HttpServletRequest request, HttpServletResponse response, 
			Object command,	BindException errors) throws Exception {

		InsmodeLearnerSearchForm form = (InsmodeLearnerSearchForm)command;
		List<LearnerEnrollment> enrolls = entitlementService.getLearnerEnrollmentsForLearner(form.getUser().getLearner());
		List<CourseCourseStatisticsPair> results = new ArrayList<CourseCourseStatisticsPair>();

		for( LearnerEnrollment le : enrolls ) {
			CourseCourseStatisticsPair pair = new CourseCourseStatisticsPair();
			LearnerCourseStatistics stat = le.getCourseStatistics();
			if( stat != null ) {
				pair.setCourseName(le.getCourse().getCourseTitle());
				pair.setCourseComplete(stat.getCompleted());
				pair.setComplitionDate(stat.getCompletionDate());
				pair.setPercentComplete(stat.getPercentComplete());
				pair.setPreTestScore(stat.getPretestScore());
				//pair.setPreTestPassed(stat.getPreTestPassed());
				pair.setHigestPostScore(stat.getHighestPostTestScore());
				//pair.setPostTestPassed(stat.getPostTestPassed());
				pair.setQuizes(stat.getNumberQuizesTaken());
				pair.setCourseStatus(stat.getStatus());
				pair.setFirstAccessDate(stat.getFirstAccessDate());
				pair.setLastAccessDate(stat.getLastAccessDate());
				pair.setAverageQuizScore(stat.getAverageQuizScore());
				pair.setNumberPostTestsTaken(stat.getNumberPostTestsTaken());
				pair.setTotalTimeInSeconds(stat.getTotalTimeInSeconds());
				pair.setNumberQuizesTaken(stat.getNumberQuizesTaken());
				
				results.add(pair);
			}
		}
		form.setResults(results);
		return new ModelAndView(transcriptTemplate);
	}


	/**
	 * Following two methods are used to render the credit reporting fields and
	 * the custom fields for the selected user -
	 */
	private CreditReportingFieldValue getCreditReportingFieldValueByCreditReportingField(
			com.softech.vu360.lms.model.CreditReportingField creditReportingField, List<CreditReportingFieldValue> creditReportingFieldValues) {
		if( creditReportingFieldValues != null ) {
			for( CreditReportingFieldValue creditReportingFieldValue : creditReportingFieldValues ) {
				if( creditReportingFieldValue.getReportingCustomField() != null ) {
					if( creditReportingFieldValue.getReportingCustomField().getId().compareTo(creditReportingField.getId()) == 0 ) {
						return creditReportingFieldValue;
					}
				}
			}
		}
		return new CreditReportingFieldValue();
	}

	private CustomFieldValue getCustomFieldValueByCustomField(com.softech.vu360.lms.model.CustomField customField, 
			List<CustomFieldValue> customFieldValues) {
		if( customFieldValues != null ) {
			for( CustomFieldValue customFieldValue : customFieldValues ) {
				if( customFieldValue.getCustomField() != null ) {
					if( customFieldValue.getCustomField().getId().compareTo(customField.getId()) == 0 ) {
						return customFieldValue;
					}
				}
			}
		}
		return new CustomFieldValue();
	}


	public String getSearchLearnerTemplate() {
		return searchLearnerTemplate;
	}
	public void setSearchLearnerTemplate(String searchLearnerTemplate) {
		this.searchLearnerTemplate = searchLearnerTemplate;
	}
	public String getRedirectTemplate() {
		return redirectTemplate;
	}
	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}
	public ResourceService getResourceService() {
		return resourceService;
	}
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	public String getProfileTemplate() {
		return profileTemplate;
	}
	public void setProfileTemplate(String profileTemplate) {
		this.profileTemplate = profileTemplate;
	}
	public LearnerService getLearnerService() {
		return learnerService;
	}
	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	public CustomFieldService getCustomFieldService() {
		return customFieldService;
	}
	public void setCustomFieldService(CustomFieldService customFieldService) {
		this.customFieldService = customFieldService;
	}
	public String getTranscriptTemplate() {
		return transcriptTemplate;
	}
	public void setTranscriptTemplate(String transcriptTemplate) {
		this.transcriptTemplate = transcriptTemplate;
	}
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

}	