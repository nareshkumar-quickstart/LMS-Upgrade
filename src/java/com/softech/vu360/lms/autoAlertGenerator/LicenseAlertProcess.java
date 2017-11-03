package com.softech.vu360.lms.autoAlertGenerator;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.util.SendMailService;


public class LicenseAlertProcess implements Runnable {


    private LearnerLicenseAlertService learnerlicensealertService = null;
	private static ApplicationContext applicationContext=null;

	public static void main(String args[]) {
		 
		 
	        applicationContext = new ClassPathXmlApplicationContext(
	                new String[]{
	                		//"com/softech/vu360/lms/autoAlertGenerator/applicationContext.xml"
	                		"applicationContext.xml"
	                }
	        );

	        (new Thread(new LicenseAlertProcess())).start();
	    }
	
    public void run() {
    	
        while(true){
        	
        	try {
    	        learnerlicensealertService = (LearnerLicenseAlertService) applicationContext.getBean("learnerLicenseAlertService");
    	        
    	        List<LearnerLicenseAlert> lst = learnerlicensealertService.getLearnerLicenseAlert();
    	        
    	        if(lst!=null && lst.size() > 0 ){
    	        	for (LearnerLicenseAlert learnerLicenseAlert : lst) {
						String email = learnerLicenseAlert.getLearner().getVu360User().getEmailAddress();
						
						String licenseexpirationdate = learnerLicenseAlert.getLearnerlicense().getSupportingInformation();
						if(!StringUtils.isEmpty(email)) {
							
							SendMailService.sendSMTPMessage(email, "support@domain.com", "QuickStart.com Support", "License Expiration Alert","Your "+learnerLicenseAlert.getAlertName()+ " will expire on " + licenseexpirationdate);
							System.out.println("Your "+learnerLicenseAlert.getAlertName()+ " will expire on " + licenseexpirationdate);
						}			
						 
					}
    	        	
    	        }else{
    	        	System.out.println("No license alert found.");
    	        }

    	        long lSleep = (Long.parseLong(MailConfigProperties.getMailProperty("readLicenseAlert.threadsleep"))*60000);
    	        Thread.sleep(lSleep);
    	        
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

}


