package com.softech.vu360.lms.autoAlertGenerator.queue.listeners;

import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SurveyService;

/**
 * QueueListener - provides a set of
 * properties and a constructor which
 * takes beans as parameters
 *
 * @author ramiz.uddin
 * @version 0.1 10/31/2012
 */

public abstract class QueueListener implements IQueueListener {

    protected SurveyService surveyService;
    protected EnrollmentService enrollmentService;
    protected EntitlementService entitlementService;

    protected QueueListener(SurveyService surveyService,
                            EnrollmentService enrollmentService,
                            EntitlementService entitlementService
    ) {
        this.surveyService = surveyService;
        this.enrollmentService = enrollmentService;
        this.entitlementService = entitlementService;
    }

}