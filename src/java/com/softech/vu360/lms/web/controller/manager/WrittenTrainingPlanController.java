package com.softech.vu360.lms.web.controller.manager;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.TPQuestion;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WrittenTrainingPlan;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

public class WrittenTrainingPlanController extends MultiActionController {

	private static final Logger log = Logger.getLogger(ChangeMemberRoleController.class.getName());
	
	private SurveyService surveyService = null;
	private LearnerService learnerService = null;

	private String writtenTrainingPlanTemplate; 

	
	
	public ModelAndView displayTrainingPlanResult(HttpServletRequest request,
			HttpServletResponse response) {
		
		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
		LMSRole lmsRole = null;
		Map<Object, Object> context = new HashMap<Object, Object>();
		Long surveyId = new Long(0);
		if(request.getParameter("surveyId")!=null){
			surveyId = Long.parseLong(request.getParameter("surveyId"));
		}
		
		HttpSession session = request.getSession();
		BufferedWriter writer = null;
		
		WrittenTrainingPlan tp = surveyService.generateWrittenTrainingPlanSurveyResults(loggedInUser, surveyId);
		
		
		
		
		
		
		//Generate Map for Q. 5
		
		List<String>strExtensive = new ArrayList<String>();
		List<String>strModerate = new ArrayList<String>();
		List<String>strLimited = new ArrayList<String>();
		
		this.populateForMap(strExtensive,strModerate,strLimited, tp);
		
		
		List<Learner> learnerList = new ArrayList<Learner>();
		learnerList = learnerService.getAllLearnersOfCustomer(loggedInUser.getLearner().getCustomer());
		
		
		
		
		
		
		
		
		
		
		
		context.put("extensiveList", strExtensive);
		context.put("moderateList", strModerate);
		context.put("limitedList", strLimited);
		context.put("learnerList", learnerList);
		context.put("tp", tp);
		
		
		
		HashMap<Object, Object> attrs = new HashMap<Object, Object>(context);
		attrs.put("tp1", tp);
		
		attrs.put("extensiveList", strExtensive);
		attrs.put("moderateList", strModerate);
		attrs.put("limitedList", strLimited);
		attrs.put("learnerList", learnerList);
		
//		try{
//			java.io.File file = new java.io.File("d:\\testOWS.htm");
//			if(!file.exists())
//			file.createNewFile();
//		
//		
//			writer = new BufferedWriter(new FileWriter(file));
//		
//		
//		
//		TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
//		tmpSvc.renderTemplateToWriter("vm/"+writtenTrainingPlanTemplate+"1.vm",attrs, writer);
//		
//		//String strTest=tmpSvc.renderTemplate("vm/"+writtenTrainingPlanTemplate+"1.vm", attrs);
//		//writer.write(strTest);
//		writer.flush();
//		writer.close();
//		
//		}
//		catch(Exception ex){
//			log.error(ex.getMessage());
//			
//		}
		
		return new ModelAndView(writtenTrainingPlanTemplate,"context",context);
		
		
		
		
		
		
	}



	private void populateForMap(List<String> strExtensive,
			List<String> strModerate, List<String> strLimited, WrittenTrainingPlan tp) {
		
		List<String> strListQuestionText = new ArrayList<String>();
		strListQuestionText.add("Certificate of Deposit (CD)");strListQuestionText.add("Blue Chip Equities");strListQuestionText.add("Margin Accounts");
		strListQuestionText.add("Private Placements");strListQuestionText.add("Options");strListQuestionText.add("Uncovered Options and Sophisticated Option Strategies");
		strListQuestionText.add("Municipal Securities");strListQuestionText.add("Penny Stocks");
		strListQuestionText.add("Growth Equities");strListQuestionText.add("Commodities or Futures");strListQuestionText.add("Mutual Funds");
		strListQuestionText.add("Listed Corporate Bonds");strListQuestionText.add("Insurance Products");strListQuestionText.add("Limited Partnerships");
		strListQuestionText.add("Low-rated Corporate Bonds");strListQuestionText.add("Low-rated Municipal Securities");
		strListQuestionText.add("Investment Banking");strListQuestionText.add("Covered Options");
		strListQuestionText.add("Fixed Income, US Government Securities");strListQuestionText.add("Growth or Small Cap sector Mutual Funds");
		
		
		TPQuestion question= null;
		for(String strText : strListQuestionText){
			
			question = tp.getExactQuestion(strText);
			if(question!=null){
				
				if(question.getAnswerText("Extensive")){
					strExtensive.add(strText);
				}
				if(question.getAnswerText("Moderate")){
					strModerate.add(strText);
				}
				if(question.getAnswerText("Limited")){
					strLimited.add(strText);
				}
					
				
				
			}
			
		}
		
		

		
	}



	public SurveyService getSurveyService() {
		return surveyService;
	}



	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}



	public String getWrittenTrainingPlanTemplate() {
		return writtenTrainingPlanTemplate;
	}



	public void setWrittenTrainingPlanTemplate(String writtenTrainingPlanTemplate) {
		this.writtenTrainingPlanTemplate = writtenTrainingPlanTemplate;
	}



	public LearnerService getLearnerService() {
		return learnerService;
	}



	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
	
	
	
	
	
	
	
}
