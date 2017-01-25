package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.PersonalInformationSurveyQuestion;
import com.softech.vu360.lms.model.SurveyFlag;
import com.softech.vu360.lms.model.SurveyQuestion;
import com.softech.vu360.lms.model.SurveyResult;
import com.softech.vu360.lms.model.SurveyResultAnswer;
import com.softech.vu360.lms.model.SurveyReviewComment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.SurveyApprovalForm;
import com.softech.vu360.lms.web.controller.model.SurveyFlagAndSurveyResult;
import com.softech.vu360.lms.web.controller.model.SurveyResponseBuilder;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.lms.web.filter.VU360UserMode;
import com.softech.vu360.util.AprovalSort;

/**
 * @author Dyutiman
 * created on 31st May 2010.
 *
 */
public class ManageSurveyApprovalController extends VU360BaseMultiActionController {

	private String approvalsTemplate = null;
	private String redirectTemplate = null;
	private String surveyResponseTemplate = null;

	private SurveyService surveyService = null;
	private VU360UserService vu360UserService = null;
	private HttpSession session = null;

	protected void onBind( HttpServletRequest request, Object command, String methodName ) throws Exception {
		if( command instanceof SurveyApprovalForm ) {
			SurveyApprovalForm form = (SurveyApprovalForm)command;
			if( methodName.equals("showSurveyView")){

				VU360User user = vu360UserService.getUserById(form.getLearnerId());
				form.setUser(user);
			}

		}
	}

	protected ModelAndView handleNoSuchRequestHandlingMethod( NoSuchRequestHandlingMethodException ex, HttpServletRequest request, 
			HttpServletResponse response ) throws Exception {
		
		/**
		 * LMS-9512 | S M Humayun | 29 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0009");
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("methodName", "displayMagAlert");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	
	
	public ModelAndView displayMagAlert( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {
		
		/**
		 * LMS-9512 | S M Humayun | 29 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0009");
		
		SurveyApprovalForm form = (SurveyApprovalForm)command;
		//TODO
		return new ModelAndView(approvalsTemplate);
	}
	public ModelAndView searchApprovals( HttpServletRequest request, HttpServletResponse response, 
			Object command, BindException errors ) throws Exception {
		session = request.getSession();
		
		/**
		 * LMS-9512 | S M Humayun | 29 Mar 2011
		 */
		session.setAttribute("feature", "LMS-ADM-0009");
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		SurveyApprovalForm form = (SurveyApprovalForm)command;

		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentCustomer();
		Distributor distributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentDistributor();
		Set<SurveyFlagAndSurveyResult> surveyResultAndFlagSet = null;
		if( distributor != null && ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().
				getAuthentication().getDetails()).getCurrentMode().equals(VU360UserMode.ROLE_LMSADMINISTRATOR) ) {
			//surveyResultSet = (Set<SurveyResult>) surveyService.getNotReviewedFlaggedSurveyResult(distributor);
		} else {
			if(form.getStatus().equalsIgnoreCase("All")){
				form.setStatus("");
			}
			surveyResultAndFlagSet = (Set<SurveyFlagAndSurveyResult>) surveyService.getNotReviewedFlaggedSurveyResult2(customer,form.getSurveyName(),form.getStatus(),form.getFirstName(),form.getLastName(),form.getUserName()); //??? all surveys...no filters???
		}

		List<SurveyFlagAndSurveyResult> surveyResultsAndFlags = new ArrayList<SurveyFlagAndSurveyResult>();


		if (surveyResultAndFlagSet != null && surveyResultAndFlagSet.size() > 0) {
			Iterator<SurveyFlagAndSurveyResult> itr = surveyResultAndFlagSet.iterator(); 
			while(itr.hasNext()) {
				SurveyFlagAndSurveyResult sr = itr.next(); 
				surveyResultsAndFlags.add(sr);
			} 
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
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("firstName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
				} else {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("firstName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 0);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
				}
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("lastName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
				} else {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("lastName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 1);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
				}
			}
			else if( sortColumnIndex.equalsIgnoreCase("2") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("userName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 2);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 2);
				} else {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("userName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 2);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 2);
				}
			}
			else if( sortColumnIndex.equalsIgnoreCase("3") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("surveyName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 3);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 3);
				} else {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("surveyName");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 3);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 3);
				}
			}
			else if( sortColumnIndex.equalsIgnoreCase("4") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("status");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 0);
					session.setAttribute("sortColumnIndex", 4);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 4);
				} else {
					AprovalSort sort = new AprovalSort();
					sort.setSortBy("status");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(surveyResultsAndFlags,sort);
					session.setAttribute("sortDirection", 1);
					session.setAttribute("sortColumnIndex", 4);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 4);
				}
			}
		}	

		form.setSurveyResultsAndFlags(surveyResultsAndFlags);

		//Set<SurveyResult> surveyResults = surveyService.getAllSurvey(surveyowner);
		context.put("surveyResultsAndFlags", surveyResultsAndFlags);
		// TODO
		return new ModelAndView(approvalsTemplate, "context", context);
	}

	
	public ModelAndView reviewedSurveyResponse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		/**
		 * LMS-9512 | S M Humayun | 29 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0009");
		
		SurveyApprovalForm form = (SurveyApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		int i=0, j=0;
		SurveyFlagAndSurveyResult item0;
		String selecteditem0;
		List<SurveyFlagAndSurveyResult> selectedsurveyFlagAndsurveyResult = new ArrayList<SurveyFlagAndSurveyResult>();
		

		
			if(form.getSelectedAll()!=null)
				for(;i<form.getSurveyResultsAndFlags().size();){
					item0 = form.getSurveyResultsAndFlags().get(i);
					for(;j<form.getSelectedAll().length;){
						selecteditem0 = (form.getSelectedAll())[j];
						if(item0 != null){
							if(item0.getSurveyResult().getId()==(Long.parseLong(selecteditem0))){
								selectedsurveyFlagAndsurveyResult.add(item0);



								break;
							}
						}
						j++;
					}
					j=0;
					i++;
				}
		form.setSelectedsurveyFlagAndsurveyResult(selectedsurveyFlagAndsurveyResult);
		
		for(int k=0;k<form.getSelectedsurveyFlagAndsurveyResult().size();k++){			
			SurveyFlag surveyFlag = form.getSelectedsurveyFlagAndsurveyResult().get(k).getSurveyFlag();
			surveyFlag = surveyService.loadSurveyFlagForUpdate(surveyFlag.getId());
			surveyFlag.setReviewed(true);
			surveyService.saveSurveyFlag(surveyFlag);			
		}
		
		//form.setSelectedsurveyResults(selectedsurveyResults);
		
		return new ModelAndView("redirect:mgr_surveyApprovals.do?&method=searchApprovals");
	}
	public ModelAndView showSurveyView(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) 
	throws Exception {
		
		/**
		 * LMS-9512 | S M Humayun | 29 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0009");
		
		SurveyApprovalForm form = (SurveyApprovalForm)command;
		//Learner learner = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentLearner();
		VU360User user = vu360UserService.getUserById(Long.parseLong(request.getParameter("learnerId")));
        Learner learner = user.getLearner();
		form.setLearner(learner);
		int nextPageIndex = 0;
		int questionIndex = 0;
		int questionNo = 0;
		int i; int j = 0; int index = 0;

		/*Long surveyId = Long.parseLong(request.getParameter("id"));
		form.setSurveyId(surveyId);*/
		Map<Object, Object> context = new HashMap<Object, Object>();

		SurveyResult surveyResult = surveyService.loadForUpdateSurveyResult(form.getSrId());
		com.softech.vu360.lms.model.Survey survey = surveyResult.getSurvey();
		form.setSurveyResult(surveyResult);
		List<SurveyResultAnswer> surveyResultAnswers = null;
		if(surveyResult != null) {
			surveyResultAnswers = surveyResult.getAnswers();
		}

		if( request.getParameter("nextPageIndex") != null && !survey.getIsShowAll() ) {
			index = Integer.parseInt(request.getParameter("nextPageIndex"));
			questionIndex = index*survey.getQuestionsPerPage();
			questionNo = index*survey.getQuestionsPerPage();;
		}
		SurveyResponseBuilder builder = new SurveyResponseBuilder(survey); 
		List<SurveyQuestion> surveyQuestionList = survey.getQuestions();
		Collections.sort(surveyQuestionList);


		boolean flag = false;
		for( i = questionIndex ; i<surveyQuestionList.size() ; i++ ) {
			j++;
			com.softech.vu360.lms.model.SurveyQuestion question = surveyQuestionList.get(i);
			flag = false;
			if (surveyResultAnswers != null && surveyResultAnswers.size() > 0) {
				for(SurveyResultAnswer surveyResultAnswer : surveyResultAnswers) {
					if (question.getId().compareTo(surveyResultAnswer.getQuestion().getId()) == 0) {
						if (question instanceof PersonalInformationSurveyQuestion) {
							builder.buildPersonalInformationQuestion(question, surveyResultAnswer, learner);
							flag = true;
						} else {
							if (surveyResultAnswer.getQuestion().getId().compareTo(question.getId()) == 0) {
								builder.buildQuestion(question, surveyResultAnswer);
								flag = true;
								break;
							} 
						}
					}
				}
			}
			if (!flag) {
				builder.buildQuestion(question, null);
			}

			/*
			 * In case of comment lets show the survey question and answer in a single page. So commenting out following lines.

			if( !survey.isShowAll() && j == survey.getQuestionsPerPage() ) {
				i++;
				break;
			}
			 */
		}
		com.softech.vu360.lms.web.controller.model.survey.Survey surveyView = builder.getSurveyView();
		//set it in the form...
		form.setSurvey(surveyView);
		if( i == surveyQuestionList.size() || survey.getIsShowAll() ) 
			nextPageIndex = 0;
		else
			nextPageIndex = index + 1;
		/*
		 * Assume nextPageIndex=0 for now
		 */
		nextPageIndex = 0;
		context.put("nextPageIndex", nextPageIndex);
		context.put("questionNo", questionNo);
		return new ModelAndView(surveyResponseTemplate, "context", context);
	}

	public ModelAndView saveSurveyResponse(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		/**
		 * LMS-9512 | S M Humayun | 29 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0009");
		
		SurveyApprovalForm form = (SurveyApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		com.softech.vu360.lms.web.controller.model.survey.Survey survey = form.getSurvey();
		List<com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion> questionList = survey.getQuestionList();
		SurveyResult surveyResult = form.getSurveyResult();
		List<SurveyResultAnswer> surveyResultAnswers = null;
		if(surveyResult != null) {
			surveyResultAnswers = surveyResult.getAnswers();
			if (surveyResultAnswers != null) {
				List<SurveyResultAnswer> newResultAnswersList = new ArrayList<SurveyResultAnswer>();
				for (com.softech.vu360.lms.web.controller.model.survey.SurveyQuestion question : questionList) {
					for(int i =0; i<surveyResultAnswers.size(); i++) {
						SurveyResultAnswer resultAnswer = surveyResultAnswers.get(i);
						if (question.getSurveyQuestionRef().getId().compareTo(resultAnswer.getQuestion().getId()) == 0) {
							List<SurveyReviewComment> commentList = new ArrayList<SurveyReviewComment>();
							String comment = question.getComment();
							SurveyReviewComment reviewComment = new SurveyReviewComment();
							reviewComment.setComment(comment);
							reviewComment.setCommentedBy(surveyResult.getSurveyee().getName());
							reviewComment.setCommentDate(new Date());
							reviewComment.setAnswer(resultAnswer);
							commentList.add(reviewComment);
							resultAnswer.setComments(commentList);
							newResultAnswersList.add(resultAnswer);
						}
					}
				}
				surveyResult.setAnswers(newResultAnswersList);
			}
			surveyService.saveSurveyResult(surveyResult);
		}
		context.put("methodName", "searchApprovals");
		return new ModelAndView(redirectTemplate, "context", context);
	}
	
	public ModelAndView sendBack(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		
		/**
		 * LMS-9512 | S M Humayun | 29 Mar 2011
		 */
		request.getSession(true).setAttribute("feature", "LMS-ADM-0009");
		
		SurveyApprovalForm form = (SurveyApprovalForm)command;
		Map<Object, Object> context = new HashMap<Object, Object>();
		int i=0, j=0;
		SurveyFlagAndSurveyResult item0;
		String selecteditem0;
		List<SurveyFlagAndSurveyResult> selectedsurveyFlagAndsurveyResult = new ArrayList<SurveyFlagAndSurveyResult>();
		

		
			if(form.getSelectedAll()!=null)
				for(;i<form.getSurveyResultsAndFlags().size();){
					item0 = form.getSurveyResultsAndFlags().get(i);
					for(;j<form.getSelectedAll().length;){
						selecteditem0 = (form.getSelectedAll())[j];
						if(item0 != null){
							if(item0.getSurveyResult().getId()==(Long.parseLong(selecteditem0))){
								selectedsurveyFlagAndsurveyResult.add(item0);



								break;
							}
						}
						j++;
					}
					j=0;
					i++;
				}
		form.setSelectedsurveyFlagAndsurveyResult(selectedsurveyFlagAndsurveyResult);
		
		for(int k=0;k<form.getSelectedsurveyFlagAndsurveyResult().size();k++){			
			SurveyFlag surveyFlag = form.getSelectedsurveyFlagAndsurveyResult().get(k).getSurveyFlag();
			surveyFlag = surveyService.loadSurveyFlagForUpdate(surveyFlag.getId());
			surveyFlag.setSentBackToLearner(true);
			surveyService.saveSurveyFlag(surveyFlag);			
		}
		
		
		
		return new ModelAndView("redirect:mgr_surveyApprovals.do?&method=searchApprovals");
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// Auto-generated method stub
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getApprovalsTemplate() {
		return approvalsTemplate;
	}

	public void setApprovalsTemplate(String approvalsTemplate) {
		this.approvalsTemplate = approvalsTemplate;
	}

	public SurveyService getSurveyService() {
		return surveyService;
	}

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public String getSurveyResponseTemplate() {
		return surveyResponseTemplate;
	}

	public void setSurveyResponseTemplate(String surveyResponseTemplate) {
		this.surveyResponseTemplate = surveyResponseTemplate;
	}

	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	/**
	 * @param vu360UserService the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

}