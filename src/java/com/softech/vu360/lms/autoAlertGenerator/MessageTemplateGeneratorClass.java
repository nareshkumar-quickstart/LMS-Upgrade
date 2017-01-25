package com.softech.vu360.lms.autoAlertGenerator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.softech.vu360.lms.model.MessageTemplate;
import com.softech.vu360.lms.service.SurveyService;

public class MessageTemplateGeneratorClass {

    /*ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"com/softech/vu360/lms/autoAlertGenerator/applicationContext.xml"});
     SurveyService surveyService = (SurveyService)context.getBean("surveyService");*/


    public Map<String,String> createMessageForCourseCompletion(String firstName , String lastName , String CourseName , Date dateOfCourseCompletion , String eventType , SurveyService surveyService){

        MessageTemplate messageTemplate = surveyService.getMessageTemplateByEventType(eventType);
        Map<String,String> messageTemplateMap = new HashMap();
        try{
            String messageTemplateBody=messageTemplate.getBody();
            String messageTemplateSubjectLine=messageTemplate.getSubjectLine();

            String ReplacedMessageTemplateBody = messageTemplateBody.replace("<NEWLINE>","\n");
            ReplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<COURSENAME>",CourseName);
            if(dateOfCourseCompletion != null){
                ReplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<DATE>",dateOfCourseCompletion.toString());
            }
            //String actualRegistrationLinkURL = registrationLinkURL; //+ "?emailID=" + emailid;
            String finalreplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<NAME>"," " +firstName + " " + lastName + " ");
            String replacedMessageTemplateSubjectLine=messageTemplateSubjectLine.replace("","");

            messageTemplateMap.put("body", finalreplacedMessageTemplateBody);
            messageTemplateMap.put("subject", messageTemplateSubjectLine);
        }catch (Exception e){
            e.printStackTrace();
        }
        return messageTemplateMap;
    }

    public Map<String,String> createMessageForRegistration(String firstName , String lastName , Date dateOfRegistrationCompletion , String eventType , SurveyService surveyService){

        MessageTemplate messageTemplate = surveyService.getMessageTemplateByEventType(eventType);
        Map<String,String> messageTemplateMap = new HashMap();
        try{
            String messageTemplateBody=messageTemplate.getBody();
            String messageTemplateSubjectLine=messageTemplate.getSubjectLine();

            String ReplacedMessageTemplateBody = messageTemplateBody.replace("<NEWLINE>","\n");
            //String actualRegistrationLinkURL = registrationLinkURL; //+ "?emailID=" + emailid;
            if(dateOfRegistrationCompletion != null) {
                ReplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<DATE>",dateOfRegistrationCompletion.toString());
            }
            String finalreplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<NAME>"," " +firstName + " " + lastName + " ");
            String replacedMessageTemplateSubjectLine=messageTemplateSubjectLine.replace("","");

            messageTemplateMap.put("body", finalreplacedMessageTemplateBody);
            messageTemplateMap.put("subject", messageTemplateSubjectLine);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return messageTemplateMap;
    }
    
    /**
     * 
     * @param firstName
     * @param lastName
     * @param dateOfRegistrationCompletion
     * @param eventType
     * @param surveyService
     * @return
     */
    public Map<String,String> createMessageForLearnerLicense(String firstName , String lastName , Date dateOfRegistrationCompletion , String eventType , SurveyService surveyService){

        MessageTemplate messageTemplate = surveyService.getMessageTemplateByEventType(eventType);
        Map<String,String> messageTemplateMap = new HashMap();
        try{
            String messageTemplateBody=messageTemplate.getBody();
            String messageTemplateSubjectLine=messageTemplate.getSubjectLine();

            String ReplacedMessageTemplateBody = messageTemplateBody.replace("<NEWLINE>","\n");
            //String actualRegistrationLinkURL = registrationLinkURL; //+ "?emailID=" + emailid;
            if(dateOfRegistrationCompletion != null) {
                ReplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<DATE>",dateOfRegistrationCompletion.toString());
            }
            String finalreplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<NAME>"," " +firstName + " " + lastName + " ");
            String replacedMessageTemplateSubjectLine=messageTemplateSubjectLine.replace("","");

            messageTemplateMap.put("body", finalreplacedMessageTemplateBody);
            messageTemplateMap.put("subject", messageTemplateSubjectLine);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return messageTemplateMap;
    }

    public Map<String,String> createMessageForDateDrivenTrigger(String firstName , String lastName , Date dateOfRegistrationCompletion , SurveyService surveyService , String triggerType){

        MessageTemplate messageTemplate = surveyService.getMessageTemplateByTriggerType(triggerType);
        Map<String,String> messageTemplateMap = new HashMap();
        try{
            String messageTemplateBody=messageTemplate.getBody();
            String messageTemplateSubjectLine=messageTemplate.getSubjectLine();

            String ReplacedMessageTemplateBody = messageTemplateBody.replace("<NEWLINE>","\n");
            //String actualRegistrationLinkURL = registrationLinkURL; //+ "?emailID=" + emailid;
            if(dateOfRegistrationCompletion != null){
                ReplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<DATE>",dateOfRegistrationCompletion.toString());
            }
            String finalreplacedMessageTemplateBody = ReplacedMessageTemplateBody.replace("<NAME>"," " +firstName + " " + lastName + " ");
            String replacedMessageTemplateSubjectLine=messageTemplateSubjectLine.replace("","");

            messageTemplateMap.put("body", finalreplacedMessageTemplateBody);
            messageTemplateMap.put("subject", messageTemplateSubjectLine);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return messageTemplateMap;
    }
}

