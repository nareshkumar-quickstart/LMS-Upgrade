package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author raja.ali 
 */
@Transactional
public class SurveyTest extends TestBaseDAO<Survey> {

	
	@Before
	public void setRequiredService() {
		

	}

	@Test
	public void survey_test() throws Exception {
		System.out.println("#### Unit Test Survey using JUnit ####");
		
		//** Survey ****
//		Survey survey = survey_should_save();
//		Survey foundSurvey = getById(new Long("19490"), Survey.class);
		
		//** SurveyAnswerItem ****
//		surveyAnswerItem_should_save();
//		SurveyAnswerItem sureveyAnswerItem = (SurveyAnswerItem) crudFindById(SurveyAnswerItem.class, new Long("1"));
		
		
		//** SurveyFlag ****
//		surveyFlag_should_save();
//		SurveyFlag surveyFlag = (SurveyFlag) crudFindById(SurveyFlag.class, new Long("3"));
		
		//** SurveyFlagNotification ****
//		surveyFlagNotification_should_save();
//		SurveyFlagNotification surveyFlagNotification = (SurveyFlagNotification) crudFindById(SurveyFlagNotification.class, new Long("5"));
		
		//** SurveyFlagTemplate ****
//		surveyFlagTemplate_should_save();
//		SurveyFlagTemplate flagTemplate = (SurveyFlagTemplate)crudFindById(SurveyFlagTemplate.class, new Long("253"));
		
		//** SurveyLearner ****
//		surveyLearner_should_save();
//		surveyLearner_should_find();
		
		//** SurveyLink ****
//		surveyLink_should_save();
//		SurveyLink surveyLink = (SurveyLink)crudFindById(SurveyLink.class, new Long("15"));
		
		//** SurveyQuestion ****
//		SurveyQuestion surveyQuestion = (SurveyQuestion)crudFindById(SurveyQuestion.class, new Long("31309"));
		
		//** SurveyQuestionBank ****
//		surveyQuestionBank_should_save();
//		SurveyQuestionBank surveyQuestionBank = (SurveyQuestionBank)crudFindById(SurveyQuestionBank.class, new Long("1402"));
		
		//** SurveyResult ****
//		surveyResult_should_save();
		SurveyResult surveyResult = (SurveyResult)crudFindById(SurveyResult.class, new Long("7217"));//82530
		

		//** SurveyResultAnswer ****
//		surveyResultAnswer_should_save();
//		SurveyResultAnswer surveyResultAnswer = (SurveyResultAnswer)crudFindById(SurveyResultAnswer.class, new Long("100505"));

		//** SurveyResultAnswerFile ****
//		surveyResultAnswerFile_should_save();
//		SurveyResultAnswerFile surveyResultAnswerFile = (SurveyResultAnswerFile)crudFindById(SurveyResultAnswerFile.class, new Long("1000"));
		
		
		//** SurveyReviewComment ****
//		surveyReviewComment_should_save();
//		SurveyReviewComment surveyReviewComment = (SurveyReviewComment)crudFindById(SurveyReviewComment.class, new Long("1951"));
		
		//** SurveySection ****
//		surveySection_should_save();
//		SurveySection surveySection = (SurveySection)crudFindById(SurveySection.class, new Long("43285"));
				
		//** SuggestedTraining ****
//		suggestedTraining_should_save();
//		SuggestedTraining suggestedTraining = (SuggestedTraining)crudFindById(SuggestedTraining.class, new Long("453"));
		
		
		//** SurveySectionSurveyQuestionBank ****
//		surveySectionSurveyQuestionBank_should_save();
		
		//** AggregateSurveyQuestion ****
//		aggregateSurveyQuestion_should_save();
		
		//** MultipleSelectSurveyQuestion ****
//		multipleSelectSurveyQuestion_should_save();
//		MultipleSelectSurveyQuestion multipleSelectSurveyQuestion = (MultipleSelectSurveyQuestion)crudFindById(MultipleSelectSurveyQuestion.class, new Long("37355"));
		
		//** SingleSelectSurveyQuestion ****
//		SingleSelectSurveyQuestion singleSelectSurveyQuestion = (SingleSelectSurveyQuestion) crudFindById(SingleSelectSurveyQuestion.class, new Long("1"));
		
		//Added by Marium Saud
		/*Survey survey=survey_should_save();
		Survey read=getById(survey.getId(), Survey.class);
		if(read.getOwner() instanceof Distributor){
			Distributor distributor=(Distributor)read.getOwner();
			System.out.println(distributor.getId()+distributor.getName());
		}*/
		
		
		System.out.print("..............");
		
	}
	

	private SurveyLearner surveyLearner_should_save() {
		SurveyLearner objSurveyLearner = new SurveyLearner();
		
		Learner learner = (Learner)crudFindById(Learner.class, new Long("983494"));
		objSurveyLearner.setLearner(learner);
		
		Survey survey = getById(new Long("201"), Survey.class);
		objSurveyLearner.setSurvey(survey);
		
		objSurveyLearner.setEndDate(new Date(System.currentTimeMillis()));
		objSurveyLearner.setSurveyNeverExpired(false);
		
		VU360User userToNotify = (VU360User)crudFindById(VU360User.class, new Long("211080"));
		objSurveyLearner.setUserToNotify(userToNotify);
		
		objSurveyLearner.setNotifyOnCompletion(true);
		objSurveyLearner.setStartDate(new Date(System.currentTimeMillis()));
		
		
		try{
			
			objSurveyLearner = (SurveyLearner) crudSave(SurveyLearner.class, objSurveyLearner);
			System.out.println("objSurvey.id::"+objSurveyLearner.getLearner());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyLearner;
	}
	
	private void surveyLearner_should_find(){
		SurveyLearner objSurveyLearner = new SurveyLearner();
		Learner learner = (Learner)crudFindById(Learner.class, new Long("983494"));
		objSurveyLearner.setLearner(learner);
		Survey survey = getById(new Long("201"), Survey.class);
		objSurveyLearner.setSurvey(survey);
		
		SurveyLearner surveyLearner2 = (SurveyLearner)crudFindById(SurveyLearner.class, objSurveyLearner);

	}

	private SurveyFlag surveyFlag_should_save(){
		SurveyFlag objSurveyFlag = new SurveyFlag();
		objSurveyFlag.setReviewedBy("R");
		objSurveyFlag.setReviewed(false);
		objSurveyFlag.setTriggerDate(new Date(System.currentTimeMillis()));
		objSurveyFlag.setSentBackToLearner(false);
		
		
		try{
			
			objSurveyFlag = (SurveyFlag) crudSave(SurveyFlag.class, objSurveyFlag);
			System.out.println("objSurvey.id::"+objSurveyFlag.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyFlag;
	}
	
	private Survey survey_should_save(){
		Survey objSurvey = new Survey();
		objSurvey.setName("Unit Test Survey using JUnit - 011");
		
		List<Course> courses = (List<Course>)getAll("Course", Course.class);
		objSurvey.setCourses(courses);
		
		objSurvey.setAllowAnonymousResponse(false);
		objSurvey.setEvent("Testing Event");
		objSurvey.setStatus(Survey.NOTPUBLISHED);
		objSurvey.setIsShowAll(false);
		objSurvey.setQuestionsPerPage(Integer.parseInt("5"));
		objSurvey.setRememberPriorResponse(false);
		objSurvey.setAllowAnonymousResponse(false);
		objSurvey.setElectronicSignatureRequire(false);
		objSurvey.setElectronicSignature("Signature");
		objSurvey.setLinkSelected(false);
		
		List<SurveyQuestion> questions = new ArrayList<SurveyQuestion>();
		SurveyQuestion q1=new SurveyQuestion();
		q1.setText("Unit Test-I");
		q1.setNotes("NOtes Unit Test-I");
		SurveyQuestion q2=new SurveyQuestion();
		q2.setText("Unit Test-II");
		q1.setNotes("NOtes Unit Test-II");
		questions.add(q1);
		questions.add(q2);
		
		objSurvey.setQuestions(questions);
		
		//Added by Marium Saud : Tested SurveyOwner by passing owner of type 'Customer' and 'Distributor'
//		Customer customer=(Customer)crudFindById(Customer.class,new Long(1));
//		objSurvey.setOwner(customer);
		
		Distributor distributor= (Distributor)crudFindById(Distributor.class,new Long(1));
		objSurvey.setOwner(distributor);
		
		try{
			objSurvey = save(objSurvey);
			System.out.println("objSurvey.id::"+objSurvey.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurvey;
	}	
	private Survey survey_should_be_found_by_id(Long id){
				
		Survey survey = getById(id, Survey.class);
		
		return survey;
	}
	
	private SurveyFlagNotification surveyFlagNotification_should_save(){
		SurveyFlagNotification objSurveyFlagNotification = new SurveyFlagNotification();
		objSurveyFlagNotification.setEmailaddress("R");
		
		SurveyFlagTemplate flagTemplate = (SurveyFlagTemplate)crudFindById(SurveyFlagTemplate.class, new Long("2"));
		objSurveyFlagNotification.setFlagTemplate(flagTemplate);
		
		try{
			
			objSurveyFlagNotification = (SurveyFlagNotification) crudSave(SurveyFlagNotification.class, objSurveyFlagNotification);
			System.out.println("objSurveyFlagNotification.id::"+objSurveyFlagNotification.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyFlagNotification;
	}

	private SurveyFlagTemplate surveyFlagTemplate_should_save(){
		SurveyFlagTemplate objSurveyFlagTemplate = new SurveyFlagTemplate();
		objSurveyFlagTemplate.setFlagName("Name");
		objSurveyFlagTemplate.setMessage("MESSAGE");
		objSurveyFlagTemplate.setSendMe(true);
		objSurveyFlagTemplate.setSubject("Subj");
		
		
		objSurveyFlagTemplate.setSurvey(survey_should_be_found_by_id(new Long("19490")));
		
		objSurveyFlagTemplate.setTo("to");
		
		
		
		try{
			
			objSurveyFlagTemplate = (SurveyFlagTemplate) crudSave(SurveyFlagTemplate.class, objSurveyFlagTemplate);
			System.out.println("objSurveyFlagTemplate.id::"+objSurveyFlagTemplate.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyFlagTemplate;
	}


	private SurveyLink surveyLink_should_save(){
		SurveyLink objSurveyLink = new SurveyLink();
		Survey survey = getById(new Long("201"), Survey.class);
		objSurveyLink.setSurvey(survey);
		objSurveyLink.setUrlName("urlName");
		objSurveyLink.setDisplayOrder(1);
		
		
		try{
			
			objSurveyLink = (SurveyLink) crudSave(SurveyLink.class, objSurveyLink);
			System.out.println("objSurveyLink.id::"+objSurveyLink.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyLink;
	}
	
	private SurveyQuestionBank surveyQuestionBank_should_save(){
		SurveyQuestionBank objSurveyQuestionBank = new SurveyQuestionBank();
		
		objSurveyQuestionBank.setDescription("Unit Test Description");
		objSurveyQuestionBank.setName("name");
		
		
		try{
			
			objSurveyQuestionBank = (SurveyQuestionBank) crudSave(SurveyQuestionBank.class, objSurveyQuestionBank);
			System.out.println("objSurveyQuestionBank.id::"+objSurveyQuestionBank.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyQuestionBank;
	}
	
	
	private SurveyResult surveyResult_should_save(){
		SurveyResult objSurveyResult = new SurveyResult();
		
		objSurveyResult.setCourse((Course)crudFindById(Course.class, new Long("570")));
		objSurveyResult.setSurvey(getById(new Long("19490"), Survey.class));
		objSurveyResult.setResponseDate(new Date(System.currentTimeMillis()));
		objSurveyResult.setSurveyee((VU360User)crudFindById(VU360User.class, new Long("211080")));
		//objSurveyResult.setLearningSession(learningSession);
		
		try{
			
			objSurveyResult = (SurveyResult) crudSave(SurveyResult.class, objSurveyResult);
			System.out.println("objSurveyResult.id::"+objSurveyResult.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyResult;
	}
	
	private SurveyResultAnswer surveyResultAnswer_should_save(){
		SurveyResultAnswer objSurveyResultAnswer = new SurveyResultAnswer();
		
		objSurveyResultAnswer.setSurveyAnswerText("surveyAnswerText");
		objSurveyResultAnswer.setQuestion((SurveyQuestion)crudFindById(SurveyQuestion.class, new Long("31309")));
		objSurveyResultAnswer.setSurveyQuestionBank((SurveyQuestionBank)crudFindById(SurveyQuestionBank.class, new Long("1402")));
		objSurveyResultAnswer.setSurveyResult((SurveyResult)crudFindById(SurveyResult.class, new Long("3303")));
		objSurveyResultAnswer.setSurveySection((SurveySection)crudFindById(SurveySection.class, new Long("2766")));
		List<SurveyAnswerItem> listSurveyAnswerItem = new ArrayList<SurveyAnswerItem>();
		listSurveyAnswerItem.add((SurveyAnswerItem) crudFindById(SurveyAnswerItem.class, new Long("1")));
		listSurveyAnswerItem.add((SurveyAnswerItem) crudFindById(SurveyAnswerItem.class, new Long("105097")));
		listSurveyAnswerItem.add((SurveyAnswerItem) crudFindById(SurveyAnswerItem.class, new Long("105096")));
		objSurveyResultAnswer.setSurveyAnswerItems(listSurveyAnswerItem);
		
		try{
			
			objSurveyResultAnswer = (SurveyResultAnswer) crudSave(SurveyResultAnswer.class, objSurveyResultAnswer);
			System.out.println("SurveyResultAnswer.id::"+objSurveyResultAnswer.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyResultAnswer;
	}
	
	
	private SurveyResultAnswerFile surveyResultAnswerFile_should_save(){
		SurveyResultAnswerFile objSurveyResultAnswerFile = new SurveyResultAnswerFile();
		
		objSurveyResultAnswerFile.setFileName("Test File name");
		objSurveyResultAnswerFile.setFilePath("D:/opt/lmsDocs/DEPLOYMENT_COMMANDS.txt");
		objSurveyResultAnswerFile.setSurveyResultAnswer((SurveyResultAnswer)crudFindById(SurveyResultAnswer.class, new Long("100505")));
		
		
		try{
			
			objSurveyResultAnswerFile = (SurveyResultAnswerFile) crudSave(SurveyResultAnswerFile.class, objSurveyResultAnswerFile);
			System.out.println("objSurveyResultAnswerFile.id::"+objSurveyResultAnswerFile.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyResultAnswerFile;
	}
	
	
	private SurveySection surveySection_should_save(){
		SurveySection objSurveySection = new SurveySection();
		
		objSurveySection.setDescription("description");
		objSurveySection.setName("name");
		List<SurveySection> listSurveySection = new ArrayList<SurveySection>();
		listSurveySection.add((SurveySection)crudFindById(SurveySection.class, new Long("8459")));
		listSurveySection.add((SurveySection)crudFindById(SurveySection.class, new Long("8458")));
		objSurveySection.setChildren(listSurveySection);
		objSurveySection.setSurvey(getById(new Long("19490"), Survey.class));
		objSurveySection.setParent((SurveySection)crudFindById(SurveySection.class, new Long("2766")));
		
		try{
			
			objSurveySection = (SurveySection) crudSave(SurveySection.class, objSurveySection);
			System.out.println("objSurveyResultAnswerFile.id::"+objSurveySection.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveySection;
	}

	private SurveyReviewComment surveyReviewComment_should_save() {
		SurveyReviewComment objSurveyReviewComment = new SurveyReviewComment();
		
		objSurveyReviewComment.setComment("comment");
		objSurveyReviewComment.setCommentDate(new Date(System.currentTimeMillis()));
		objSurveyReviewComment.setCommentedBy("commentedBy");
		objSurveyReviewComment.setAnswer((SurveyResultAnswer)crudFindById(SurveyResultAnswer.class, new Long("15799")));
		
		try{
			
			objSurveyReviewComment = (SurveyReviewComment) crudSave(SurveyReviewComment.class, objSurveyReviewComment);
			System.out.println("objSurveyResultAnswerFile.id::"+objSurveyReviewComment.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyReviewComment;
	}

	private SurveyAnswerItem surveyAnswerItem_should_save() {
		SurveyAnswerItem objSurveyAnswerItem = new SurveyAnswerItem();
		
		objSurveyAnswerItem.setDisplayOrder(1);
		objSurveyAnswerItem.setLabel("label");
		objSurveyAnswerItem.setValue("value");
		objSurveyAnswerItem.setSurveyQuestion((SurveyQuestion)crudFindById(SurveyQuestion.class, new Long("37349")));
		
		try{
			
			objSurveyAnswerItem = (SurveyAnswerItem) crudSave(SurveyAnswerItem.class, objSurveyAnswerItem);
			System.out.println("objSurveyAnswerItem.id::"+objSurveyAnswerItem.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveyAnswerItem;
	}

	private SuggestedTraining suggestedTraining_should_save() {
		SuggestedTraining objSuggestedTraining = new SuggestedTraining();
		
		objSuggestedTraining.setSurvey(getById(new Long("19490"), Survey.class));
		
		List<Course> courses = (List<Course>)getAll("Course", Course.class);
		objSuggestedTraining.setCourses(courses);
		
		List<SurveyAnswerItem> responses = (List<SurveyAnswerItem>)getAll("SurveyAnswerItem", SurveyAnswerItem.class);
		objSuggestedTraining.setResponses(responses);
		
		try{
			
			objSuggestedTraining = (SuggestedTraining) crudSave(SuggestedTraining.class, objSuggestedTraining);
			System.out.println("objSuggestedTraining.id::"+objSuggestedTraining.getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSuggestedTraining;
		
	}


	private SurveySectionSurveyQuestionBank surveySectionSurveyQuestionBank_should_save() {
		
		SurveySectionSurveyQuestionBank objSurveySectionSurveyQuestionBank = new SurveySectionSurveyQuestionBank();
		
		objSurveySectionSurveyQuestionBank.setSurveyQuestionBank((SurveyQuestionBank)crudFindById(SurveyQuestionBank.class, new Long("486")));
		objSurveySectionSurveyQuestionBank.setSurveySection((SurveySection)crudFindById(SurveySection.class, new Long("2511")));
		
		
		try{
			
			objSurveySectionSurveyQuestionBank = (SurveySectionSurveyQuestionBank) crudSave(SurveySectionSurveyQuestionBank.class, objSurveySectionSurveyQuestionBank);
			System.out.println("objSurveySectionSurveyQuestionBank.id::"+objSurveySectionSurveyQuestionBank.getSurveyQuestionBank().getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objSurveySectionSurveyQuestionBank;
	}

	private AggregateSurveyQuestion aggregateSurveyQuestion_should_save() {
		
		AggregateSurveyQuestion objAggregateSurveyQuestion = new AggregateSurveyQuestion();
		
		objAggregateSurveyQuestion.setCanHaveFile(true);
		objAggregateSurveyQuestion.setDisplayOrder(1);
		objAggregateSurveyQuestion.setFileRequired(true);
		objAggregateSurveyQuestion.setNotes("notes");
		objAggregateSurveyQuestion.setRequired(Boolean.TRUE);
		objAggregateSurveyQuestion.setSurvey(getById(new Long("19490"), Survey.class));
		objAggregateSurveyQuestion.setSurveyAnswerLines("surveyAnswerLines");
		//objAggregateSurveyQuestion.setSurveyQuestionBank((SurveyQuestionBank)crudFindById(SurveyQuestionBank.class, new Long("486")));
		objAggregateSurveyQuestion.setText("text");
		
		try{
			
			objAggregateSurveyQuestion = (AggregateSurveyQuestion) crudSave(AggregateSurveyQuestion.class, objAggregateSurveyQuestion);
			//System.out.println("objAggregateSurveyQuestion.id::"+objAggregateSurveyQuestion.getSurveyQuestionBank().getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objAggregateSurveyQuestion;
	}
	
	private MultipleSelectSurveyQuestion multipleSelectSurveyQuestion_should_save() {
		
		MultipleSelectSurveyQuestion objMultipleSelectSurveyQuestion = new MultipleSelectSurveyQuestion();
		
		objMultipleSelectSurveyQuestion.setCanHaveFile(true);
		objMultipleSelectSurveyQuestion.setDisplayOrder(1);
		objMultipleSelectSurveyQuestion.setFileRequired(true);
		objMultipleSelectSurveyQuestion.setNotes("notes");
		objMultipleSelectSurveyQuestion.setRequired(Boolean.TRUE);
		objMultipleSelectSurveyQuestion.setSurvey(getById(new Long("19490"), Survey.class));
		objMultipleSelectSurveyQuestion.setSurveyAnswerLines("surveyAnswerLines");
		//objMultipleSelectSurveyQuestion.setSurveyQuestionBank((SurveyQuestionBank)crudFindById(SurveyQuestionBank.class, new Long("486")));
		objMultipleSelectSurveyQuestion.setText("text");
		objMultipleSelectSurveyQuestion.setAlignment("align");
		
		
		try{
			
			objMultipleSelectSurveyQuestion = (MultipleSelectSurveyQuestion) crudSave(MultipleSelectSurveyQuestion.class, objMultipleSelectSurveyQuestion);
			//System.out.println("objMultipleSelectSurveyQuestion.id::"+objMultipleSelectSurveyQuestion.getSurveyQuestionBank().getId());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return objMultipleSelectSurveyQuestion;
	}
	
}
