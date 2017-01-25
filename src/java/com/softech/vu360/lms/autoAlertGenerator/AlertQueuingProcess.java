package com.softech.vu360.lms.autoAlertGenerator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.softech.vu360.lms.autoAlertGenerator.queue.Queue;
import com.softech.vu360.lms.autoAlertGenerator.queue.listeners.EmailToManagerQueueListener;
import com.softech.vu360.lms.autoAlertGenerator.queue.listeners.ProctorApprovalQueueListener;
import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.util.SendMailService;


public class AlertQueuingProcess {

    
	
	public static void main(String args[]) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{
                		"applicationContext.xml"
                }
        );
        
//        LearnerLicenseAlertService learnerlicensealertService = (LearnerLicenseAlertService)applicationContext.getBean("learneralertService");
//    	LearnerService learnerService = (LearnerService)applicationContext.getBean("learnerService");
        
        SurveyService surveyService =
                (SurveyService) applicationContext.getBean("surveyService");
        EnrollmentService enrollmentService =
                (EnrollmentService) applicationContext.getBean("enrollmentService");
        EntitlementService entitlementService =
                (EntitlementService) applicationContext.getBean("entitlementService");
    	

        
        
        VU360UserService vu360UserService = 
        		(VU360UserService) applicationContext.getBean("userDetailsService");

        Queue queue = Queue.GetInstance(surveyService, enrollmentService, entitlementService, vu360UserService);

        // Attach Listeners
        queue.addQueueListener(
                (
                        new ProctorApprovalQueueListener(
                            queue.surveyService,
                            queue.enrollmentService,
                            queue.entitlementService
                        )
                )
        );
        queue.addQueueListener(
                (
                        new EmailToManagerQueueListener(
                                queue.surveyService,
                                queue.enrollmentService,
                                queue.entitlementService
                        )
                )
        );

      //  new FillUpAlertQueueTable(queue);
        //Converting to single threaded application
        new ScanDatabase(queue);
        
        //getLicenseAlert(learnerlicensealertService,learnerService);

        System.out.println("Press Control-C to stop.");

    }
    
	public static boolean isExpirationDateToday(Date dueDate) { //returns true if duedate is today

        if(dueDate == null)
            return false;

		Date todaysDate = new Date();
		
		
		//double diff = Math.abs(dueDate.getTime() - System.currentTimeMillis());
		//double days = diff / MILLISECONDS_IN_DAY;
		//System.out.println("********************************************"+dueDate+"*****"+diff+"****"+days);

        // truncate time from both
        // today and due dates
        todaysDate = DateUtils.truncate(todaysDate, Calendar.DATE);
        dueDate = DateUtils.truncate(dueDate, Calendar.DATE);

        if(dueDate.equals(todaysDate)) {
//		if(todaysDate.getDate()==dueDate.getDate() && todaysDate.getMonth()==dueDate.getMonth() && todaysDate.getYear()==dueDate.getYear()){
			return true;
		}else{
			return false;
		}



	}
	
    public static void getLicenseAlert(LearnerLicenseAlertService learnerlicensealertService, LearnerService learnerService)
	{
		List <LearnerLicenseAlert> alertlist = null;
		alertlist = learnerlicensealertService.getLearnerLicenseAlert();
		if(alertlist != null)
		{
			for(LearnerLicenseAlert learnerlicensealert:alertlist)
			{
				if(isExpirationDateToday(learnerlicensealert.getTriggerSingleDate()))
						{
					     
					SendMailService.sendSMTPMessage("abdullah.mashood@360training.com", "abdullah.mashood@360training.com", "friendlyName", "Learner License Alert", "Template");      
					/*
					SendMailService.sendSMTPMessage(
									new String[] { learnerService.getLearnerByID(learnerlicensealert.getLearner().getId()).getVu360User().getEmailAddress() },
									null,"lmsalerts@360training.com",
									VU360Properties.getVU360Property("lms.email.globalException.title"),
									"Learner License Alert Email",
									"<br> Request for Lernere License Registration. ");
					      
					      System.out.println("Licese Alert");
					      */
						}
			}
		}
	}
}

class FillUpAlertQueueTable implements Runnable {

    private Queue _queue;

    FillUpAlertQueueTable(Queue queue) {

        this._queue = queue;

        new Thread(this, "FillUpAlertQueueTable").start();

    }

    public void run() {

        while (true) {
            _queue.Save();
        }

    }


}

class ScanDatabase implements Runnable  {

    private Queue _queue;

    ScanDatabase(Queue queue) {

        this._queue = queue;

        new Thread(this, "ScanDatabase").start();

    }

    public void run()  {

    //	long lSleep = 1200000L; // 20 mins delay
    	
   	long lSleep = Long.parseLong(MailConfigProperties.getMailProperty("scanDatabase.threadsleep").toString()); // 20 mins delay    	
    	
        while (true) {
            _queue.Begin();
            try {
				Thread.sleep(lSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            _queue.Save();
        }

    }

}