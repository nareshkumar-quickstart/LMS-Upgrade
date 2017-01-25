package com.softech.vu360.lms.autoAlertGenerator.queue.listeners;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.autoAlertGenerator.queue.events.QueueEvent;
import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SurveyService;

public class EmailToManagerQueueListener extends QueueListener {

    public EmailToManagerQueueListener(SurveyService surveyService,
                                          EnrollmentService enrollmentService,
                                          EntitlementService entitlementService
    ) {
        super(surveyService, enrollmentService, entitlementService);
    }

    @Override
	public void BeforeQueueing(QueueEvent queueEvent) {


	}

	/**
	 * System will send email to learners as well as their managers too.  
	 */
	@Override
	public void Saving(QueueEvent queueEvent) {
		String learnerEmailAddress = StringUtils.EMPTY;
		String customerEmail =  StringUtils.EMPTY;
		AlertQueue queue = queueEvent.getQueue();
		Customer customerOfLearner = queue.getLearner().getCustomer();
		customerEmail = customerOfLearner.getEmail();
		
		if(customerEmail == null || StringUtils.isEmpty(customerEmail)) {
			return;
		}
		
		learnerEmailAddress = queue.getEmailAddress();

		if (!StringUtils.isEmpty(learnerEmailAddress)) {
			queue.setEmailAddress(learnerEmailAddress + ";" + customerEmail);
		}else{
			queue.setEmailAddress(customerEmail);
		}
	}

}
