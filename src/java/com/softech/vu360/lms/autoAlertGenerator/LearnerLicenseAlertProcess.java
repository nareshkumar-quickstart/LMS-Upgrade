package com.softech.vu360.lms.autoAlertGenerator;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.util.SendMailService;


/**
 * 
 * @author haider.ali
 *
 */
public class LearnerLicenseAlertProcess{

  public static void main( String[] args ){

	  TimerTask task = new LearnerLicenseAlertTimer();
	  Timer timer = new Timer();

	  long delay = (Long.parseLong(MailConfigProperties.getMailProperty("readLicenseAlert.threadsleep"))*60000);
	  timer.schedule(task, 1000, delay);


    }
}


class LearnerLicenseAlertTimer  extends TimerTask {

	private static ApplicationContext applicationContext = null;
	
	static{
  	applicationContext = new ClassPathXmlApplicationContext(
            new String[]{
            		//"com/softech/vu360/lms/autoAlertGenerator/applicationContext.xml"
            		"applicationContext.xml"
            }
    );}

    @Override
    public void run() {

    	LearnerLicenseAlertService learnerlicensealertService = (LearnerLicenseAlertService) applicationContext.getBean("learneralertService");
        List<LearnerLicenseAlert> lst = learnerlicensealertService.getLearnerLicenseAlert();
        
	        if(lst!=null && lst.size() > 0 ){
	        	for (LearnerLicenseAlert learnerLicenseAlert : lst) {
					String email = learnerLicenseAlert.getLearner().getVu360User().getEmailAddress();
					
					String licenseexpirationdate = learnerLicenseAlert.getLearnerlicense().getSupportingInformation();
					if(!StringUtils.isEmpty(email)) {
						
						SendMailService.sendSMTPMessage(email, "support@360training.com", "360training.com Support", "License Expiration Alert","Your "+learnerLicenseAlert.getAlertName()+ " will expire on " + licenseexpirationdate);
						System.out.println("Your "+learnerLicenseAlert.getAlertName()+ " will expire on " + licenseexpirationdate);
					}			
					 
				}
	        	
	        }else{
	        	System.out.println("No license alert found.");
	        }


	    }
    
}

