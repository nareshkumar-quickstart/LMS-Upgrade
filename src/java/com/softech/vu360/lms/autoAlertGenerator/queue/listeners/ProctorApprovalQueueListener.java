package com.softech.vu360.lms.autoAlertGenerator.queue.listeners;

import com.softech.vu360.lms.autoAlertGenerator.MailConfigProperties;
import com.softech.vu360.lms.autoAlertGenerator.enums.TableNameEnum;
import com.softech.vu360.lms.autoAlertGenerator.queue.events.QueueEvent;
import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.SurveyService;

/**
 * There are certain courses which require
 * Proctor approvals. These courses has
 * defined Business Key which can be used
 * to distinguish them from other courses.
 * And, when Learners get completed
 * or enrolled into such course and an
 * alert is configured to send a
 * notification to the Learner,
 * the manager of the Learner should also
 * be notified
 *
 * @author ramiz.uddin
 * @version 0.1 10/19/12 4:29 PM
 */
public class ProctorApprovalQueueListener extends QueueListener {

    public ProctorApprovalQueueListener(SurveyService surveyService,
                                        EnrollmentService enrollmentService,
                                        EntitlementService entitlementService
    ) {
        super(surveyService, enrollmentService, entitlementService);
    }

    @Override
    public void BeforeQueueing(QueueEvent queueEvent) {
        // Do nothing
    }

    /*
    * To also notify the Proctor group, Learners
    * who completed or enrolled into a course
    * which requires Proctor Approval, Leaner
    * e-mail address will be concatenate with
    * Proctor group e-mail address
    *
    * @param QueueEvent
    * */
    @Override
    public void Saving(QueueEvent queueEvent) {

        AlertQueue queue = queueEvent.getQueue();

        String[] businessKeys = getBusinessKeys();
        String emailAddress;
        String proctorEmailAddress = getProctorEmailAddress();

        if (
                (queue.getTableName() != null
                        && queue.getTableName().equalsIgnoreCase(TableNameEnum.LearnerCourseStatistics.tableName))
                        && (businessKeys != null && businessKeys.length > 0)
                        && (proctorEmailAddress != null && !proctorEmailAddress.isEmpty())
                        && surveyService.isRequiredProctorApproval(queue.getTableNameId(), businessKeys)
                ) {

            emailAddress = queue.getEmailAddress();

            if (emailAddress != null) {

                queue.setEmailAddress(emailAddress + ";" + proctorEmailAddress);

            }

        }
    }

    private String[] getBusinessKeys() {
        String getProperty = MailConfigProperties.getMailProperty("businessKeys");
        String[] businessKeys = null;
        if (getProperty != null && !getProperty.isEmpty()) {
            businessKeys = getProperty.split(",");
        }

        return businessKeys;
    }

    private String getProctorEmailAddress() {
        return MailConfigProperties.getMailProperty("proctorEmail");
    }

}
