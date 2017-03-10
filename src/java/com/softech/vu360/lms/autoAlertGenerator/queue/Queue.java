package com.softech.vu360.lms.autoAlertGenerator.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
// Async I/O
import java.util.concurrent.Callable;

import javax.inject.Inject;

import com.softech.vu360.lms.autoAlertGenerator.MailConfigProperties;
import com.softech.vu360.lms.autoAlertGenerator.MessageTemplateGeneratorClass;
import com.softech.vu360.lms.autoAlertGenerator.enums.DateDrivenEnum;
import com.softech.vu360.lms.autoAlertGenerator.enums.TableNameEnum;
import com.softech.vu360.lms.autoAlertGenerator.logging.Logger;
import com.softech.vu360.lms.autoAlertGenerator.queue.events.QueueEvent;
import com.softech.vu360.lms.autoAlertGenerator.queue.listeners.IQueueListener;
import com.softech.vu360.lms.autoAlertGenerator.queue.utils.Executor;
import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.lms.service.VU360UserService;

/**
 * @author ramiz.uddin
 * @version 0.1 10/15/12
 */
public class Queue extends Executor<AlertTrigger> {

    // open/read the application context file
    static final String EVENT_DRIVEN_TRIGGER = "eventDrivenTrigger";
    static final String DATE_DRIVEN_TRIGGER = "dateDrivenTrigger";
    static final String REGISTRATION_EVENT_TYPE = "registrationEvent";
    static final String COURSECOMPLETION_EVENT_TYPE = "courseCompletionEvent";
    static final String LEARNERENROLLMENT_EVENT_TYPE = "EnrollmentEndDateEvent ";
    static final String LEARNERLICENSE_EVENT_TYPE = "LearnerLicenseEvent ";
    static final String RECURRENCE_TYPE_DAILY = "daily";
    static final String RECURRENCE_TYPE_WEEKLY = "weekly";
    static final String RECURRENCE_TYPE_MONTHLY = "monthly";
    static final String RECURRENCE_TYPE_YEARLY = "yearly";

    // SurveyService Bean
    public SurveyService surveyService = null;
    // EnrollmentService Bean
    public EnrollmentService enrollmentService = null;
    // EntitlementService Bean
    public EntitlementService entitlementService = null;
    // Vu360userService Bean
    public VU360UserService vu360userService = null;

    // Listeners
    private List _listeners = new ArrayList();

    // All queues
    public List<AlertQueue> Items = new ArrayList<AlertQueue>();


    private boolean valueSet = true;

    /*
	*
	* The constructor is restricted
	* to initiate instance of the class
	*
	* <p>
	* A function 'getInstance' is there which
	* can be used to initiate the instance and
	* get in return.
	*/
    private Queue(SurveyService surveyService,
                  EnrollmentService enrollmentService,
                  EntitlementService entitlementService,
                  VU360UserService vu360UserService
    ) {
        super();
        this.surveyService = surveyService;
        this.enrollmentService = enrollmentService;
        this.entitlementService = entitlementService;
        this.vu360userService = vu360UserService;
    }

    // local instance
    private static Queue _queue;

    /*
    * This methods create an instance of
    * this class and send in return
    * */
    public static Queue GetInstance(SurveyService surveyService,
                                    EnrollmentService enrollmentService,
                                    EntitlementService entitlementService,
                                    VU360UserService vu360UserService
    ) {
        if (_queue == null) {
            _queue = new Queue(surveyService, enrollmentService, entitlementService, vu360UserService);
        }

        return _queue;
    }

    /*
    * Attach Listener to the Queue
    * */
    public synchronized void addQueueListener( IQueueListener listener ) {
        _listeners.add( listener );
    }

    /*
   * Remove Listener to the Queue
   * */
    public synchronized void removeQueueListener( IQueueListener listener ) {
        _listeners.remove( listener );
    }


    /*
    * Sending notification to all Listeners
    * that a queue is about add
    * */
    private synchronized void notifyListenersBeforeQueueing(AlertQueue queue) {
        QueueEvent queueEvent = new QueueEvent(this, queue);
        Iterator listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (IQueueListener) listeners.next() ).BeforeQueueing( queueEvent );
        }
    }

    /*
    * Sending notification to all Listeners
    * that a queue is about to save
    * */
    private synchronized void notifyListenersBeforeSave(AlertQueue queue) {
        QueueEvent queueEvent = new QueueEvent(this, queue);
        Iterator listeners = _listeners.iterator();
        while( listeners.hasNext() ) {
            ( (IQueueListener) listeners.next() ).Saving(queueEvent);
        }
    }



    /*
    * Returns Learners of an Alert
    * <p>
    * This method returns all Learners of
    * an Alert based on Recipients defined
    * under Alert. While creating an Alert,
    * you can add a Recipient as Learners,
    * a User Group, an Organization Group.
    * Furthermore, if an Alert has Filter(s)
    * then those Filter(s) will be applied
    * and Learners will be filtered based
    * on the Filter(s)
    *
    * @param alert Alert
    * @return      filtered Learners based on the Filters, if there is any
    * */
    private List<Learner> getLearnersByAlertRecipients(Alert alert) {

        Long alertId = alert.getId();

        List<Learner> learnerList = new ArrayList<Learner>();

        // If Alert has no recipient
        if (surveyService.findAlertRecipient("", "", alertId).size() == 0) {

            // Then, the default Learner
            // will be the Alert creator
            learnerList.add(vu360userService.getLearnerByVU360UserId(alert.getCreatedBy().getId()));

        } else {

            // Otherwise, get all the learners
            // by Recipient (Learners, UserGroup,
            // Organization Group)
            learnerList = surveyService.getFilteredRecipientsByAlert(alertId);

        }

        // Avoid duplicate Learner
        // and defining HashSet
        // in a scope
        {
            HashSet<Learner> learnersHash = new HashSet<Learner>(learnerList);
            learnerList = new ArrayList<Learner>(learnersHash);
        }

        return learnerList;

    }

    /*
	* An asynchronous call to process
	* a trigger
	*/
    void ProcessTrigger(AlertTrigger alertTrigger) {

        Trigger trigger = new Trigger(alertTrigger);
        executor.submit(trigger);

    }

    /*
     * The method begin the process of iterating
     * alerts and trigger the event
     */
    public synchronized void Begin() {

        // And, get all Triggers created
        // of each Alert
        List<AlertTrigger> triggers = surveyService.getAllAlertTrigger();

        if (!valueSet) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        } else {

            try {

                // Instantiate our spring service object from the application context
                wait(10);


                for (AlertTrigger trigger : triggers) {

                    Logger.write("Beginning to process '" + trigger.getTriggerName() + "' trigger.");

                    // Process this trigger in a
                    // asynchronous manner
                    ProcessTrigger(trigger);
                    
                    //Thread.sleep (60000); // 1 min delay
                    //Thread.sleep (60);
                    long lSleep = Long.parseLong(MailConfigProperties.getMailProperty("processTrigger.threadsleep")); // 20 mins delay
    				Thread.sleep(lSleep);

                }
            } catch (Exception e) {

                e.printStackTrace();

            }

            valueSet = false;
            notify();

        }
    }

    /*
     * The method begin the process of iterating
     * alert queues and save the queue
     */
    public synchronized void Save() {

//        if (valueSet) {
//            try {
//
//                wait();
//
//            } catch (InterruptedException e) {
//
//                System.out.println("InterruptedException caught");
//
//            }
//
//        } else {

            AlertQueue alertQueueFromDb;

            for (AlertQueue alertQueue : this.Items) {

                if (alertQueue != null) {

                    alertQueueFromDb = surveyService.getAlertQueueByLearnerIdAndEventTable(
                            alertQueue.getLearner().getId(), alertQueue.getTableName(),
                            alertQueue.getTableNameId(), alertQueue.getTrigger_Id());

                    if (alertQueueFromDb == null) {

                        // means no record present for that
                        // learner for that particular event,
                        // so insert record

                        // notify all listeners before
                        // saving the queue
                        notifyListenersBeforeSave(alertQueue);

                        surveyService.saveAlertQueue(alertQueue);

                        Logger.write("A new queue has been saved.");
                        try {
							//Thread.sleep (60000);
                        	long lSleep = Long.parseLong(MailConfigProperties.getMailProperty("saveAlertqueue.threadsleep")); // 20 mins delay
            				Thread.sleep(lSleep);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // 1 minute delay
                    }

                }

            }
            this.Items = new ArrayList<AlertQueue>();
//            valueSet = true;
//            notify();
//        }
    }

    /*
    * This method create a queue for
    * course completion
    *
    * @param learners
    * @param alertQueues
    * @param trigger
    * @param triggerType
    * */
    public void setupQueueForCourseCompletion(
            List<Learner> learners, AlertTrigger trigger,
            String triggerType) {

        int after, before;

        if (trigger.getDaysBeforeEvent() == null) {
            before = 0;
        } else {
            before = trigger.getDaysBeforeEvent();
        }
        if (trigger.getDaysAfterEvent() == null) {
            after = 0;
        } else {
            after = trigger.getDaysAfterEvent();
        }

        int triggerBeforeEventDate = 0;
        int triggerAfterEventDate = after;

        List<Learner> learnerList = learners;

        long alertId;
        VU360UserNew vu360User;
        LearnerCourseStatistics learnerCourseStatistics;
        List<LearnerEnrollment> learnerEnrollment;
        Course enrolledCourse;
        List<Course> filteredCourses;

        alertId = trigger.getAlert().getId();
        filteredCourses = surveyService.getFilteredCoursesByAlert(alertId);

        for (Learner learner : learnerList) {

            learnerEnrollment = entitlementService.getLearnerEnrollmentsForLearner(learner);
            vu360User = vu360userService.getVU360UserNewById(learner.getVu360User().getId());

            for (int enrollments = 0; enrollments < learnerEnrollment.size(); enrollments++) {

                learnerCourseStatistics =
                        enrollmentService.getLearnerCourseStatisticsBylearnerEnrollment(
                                learnerEnrollment.get(enrollments)
                        );
                try {
                    enrolledCourse = learnerEnrollment.get(enrollments).getCourse();
                } catch (Exception ex) {
                    enrolledCourse = null;
                }

                // If a course filter applied
                if (filteredCourses.size() > 0 && enrolledCourse != null) {
                    if (!filteredCourses.contains(enrolledCourse)) {
                        continue;
                    }
                }

                // Only those course completion
                // which were happened after
                // alert creation
                Date courseCompletionDate =  learnerCourseStatistics.getCompletionDate();
                if(courseCompletionDate != null
                        && !learnerCourseStatistics.getCompletionDate().after(trigger.getAlert().getCreatedDate())) {
                    continue;
                }

                if (learnerCourseStatistics != null && learnerEnrollment != null
                        && learnerCourseStatistics.isCourseCompleted()
                        && learnerCourseStatistics.getCompletionDate() != null) {
                	
                	if(trigger.getAlert().getIsDefault()) {
                		
                		Map<String, String> messageTemplateMap =
                            new MessageTemplateGeneratorClass().createMessageForCourseCompletion(
                                    vu360User.getFirstName(), vu360User.getLastName(),
                                    learnerEnrollment.get(enrollments).getCourse().getCourseTitle(),
                                    learnerCourseStatistics.getCompletionDate(),
                                    COURSECOMPLETION_EVENT_TYPE, surveyService
                            );

                		Date dueDate = calculateDueDateForEventDrivenTrigger(
                            triggerBeforeEventDate, triggerAfterEventDate,
                            learnerCourseStatistics.getCompletionDate()
                		);

                		Add(
                            null,
                            TableNameEnum.LearnerCourseStatistics, learner,
                            vu360User.getFirstName(),
                            vu360User.getLastName(),
                            learnerCourseStatistics.getId(),
                            vu360User.getId(), messageTemplateMap.get("body"),
                            messageTemplateMap.get("subject"),
                            vu360User.getEmailAddress(),
                            COURSECOMPLETION_EVENT_TYPE, triggerType, trigger,
                            dueDate, false
                		);
                	} else {
                		
                		Date dueDate = calculateDueDateForEventDrivenTrigger(
                                triggerBeforeEventDate, triggerAfterEventDate,
                                learnerCourseStatistics.getCompletionDate()
                    	);

                    	Add(
                                null,
                                TableNameEnum.LearnerCourseStatistics, learner,
                                vu360User.getFirstName(),
                                vu360User.getLastName(),
                                learnerCourseStatistics.getId(),
                                vu360User.getId(), trigger.getAlert().getAlertMessageBody(),
                                trigger.getAlert().getAlertSubject(),
                                vu360User.getEmailAddress(),
                                COURSECOMPLETION_EVENT_TYPE, triggerType, trigger,
                                dueDate, false
                    	);
                	}

                }

            }

        }
    }

   /*
   * This method creates queue for Learner
   * Enrollment
   *
   * @param learners Learners
   * @param alertQueues
   * @param trigger
   * @param triggerType
   * */
    public void setupQueueForLearnerEnrollment(
            List<Learner> learners, AlertTrigger trigger,
            String triggerType, String alertMessage) {

        int after, before;

        if (trigger.getDaysBeforeEvent() == null) {
            before = 0;
        } else {
            before = trigger.getDaysBeforeEvent();
        }
        if (trigger.getDaysAfterEvent() == null) {
            after = 0;
        } else {
            after = trigger.getDaysAfterEvent();
        }
        int triggerBeforeEventDate = before;
        int triggerAfterEventDate = after;

        List<Learner> learnerList = learners;

        long alertId;
        VU360UserNew vu360User;
        List<LearnerEnrollment> learnerEnrollment;
        LearnerCourseStatistics learnerCourseStatistics;
        Course enrolledCourse;
        List<Course> filteredCourses;

        alertId = trigger.getAlert().getId();
        filteredCourses = surveyService.getFilteredCoursesByAlert(alertId);

        for (Learner learner : learnerList) {

            vu360User = vu360userService.getVU360UserNewById(learner.getVu360User().getId());

            learnerEnrollment = entitlementService.getLearnerEnrollmentsForLearner(learner);

            for (int enrollments = 0; enrollments < learnerEnrollment.size(); enrollments++) {

                learnerCourseStatistics = enrollmentService.getLearnerCourseStatisticsBylearnerEnrollment(
                        learnerEnrollment.get(enrollments));

                enrolledCourse = learnerEnrollment.get(enrollments).getCourse();

                // If a course filter applied
                if (filteredCourses.size() > 0) {
                    if (!filteredCourses.contains(enrolledCourse)) {
                        continue;
                    }
                }

                // Only those enrollment which were
                // happened after alert creation
                Date enrollmentEndDate  = learnerEnrollment.get(enrollments).getEnrollmentEndDate();
                if(enrollmentEndDate != null && !enrollmentEndDate.after(trigger.getAlert().getCreatedDate())) {
                    continue;
                }

                if (learnerCourseStatistics != null
                        && learnerEnrollment != null
                        && enrollmentEndDate != null) {

                    if(trigger.getAlert().getIsDefault()) {
                    	
                    	Map<String, String> messageTemplateMap = new MessageTemplateGeneratorClass().createMessageForCourseCompletion(
                                vu360User.getFirstName(), vu360User.getLastName(),
                                learnerEnrollment.get(enrollments).getCourse().getCourseTitle(),
                                enrollmentEndDate,
                                LEARNERENROLLMENT_EVENT_TYPE, surveyService
                        );

                        Date dueDate = calculateDueDateForEventDrivenTrigger(
                                triggerBeforeEventDate, triggerAfterEventDate,
                                enrollmentEndDate
                        );

                        Add(
                                null,
                                TableNameEnum.LearnerEnrollment, learner,
                                vu360User.getFirstName(),
                                vu360User.getLastName(),
                                learnerEnrollment.get(enrollments).getId(),
                                vu360User.getId(), messageTemplateMap.get("body"),
                                messageTemplateMap.get("subject"),
                                vu360User.getEmailAddress(),
                                LEARNERENROLLMENT_EVENT_TYPE, triggerType, trigger,
                                dueDate, false
                        );
                    } else {
                    	
                    	Date dueDate = calculateDueDateForEventDrivenTrigger(
                                triggerBeforeEventDate, triggerAfterEventDate,
                                enrollmentEndDate
                        );

                        Add(
                                null,
                                TableNameEnum.LearnerEnrollment, learner,
                                vu360User.getFirstName(),
                                vu360User.getLastName(),
                                learnerEnrollment.get(enrollments).getId(),
                                vu360User.getId(), trigger.getAlert().getAlertMessageBody(),
                                trigger.getAlert().getAlertSubject(),
                                vu360User.getEmailAddress(),
                                LEARNERENROLLMENT_EVENT_TYPE, triggerType, trigger,
                                dueDate, false
                        );
                    }
                	
                }
            }

        }

    }

    /*
    * This method creates queue for Learner
    * Registration
    *
    * @param learners Learners
    * @param alertQueues
    * @param trigger
    * @param triggerType
    * */
    public void setupQueueForLearnerRegistration(
            List<Learner> learners, AlertTrigger trigger,
            String triggerType) {

        int after, before;

        if (trigger.getDaysBeforeEvent() == null) {
            before = 0;
        } else {
            before = trigger.getDaysBeforeEvent();
        }
        if (trigger.getDaysAfterEvent() == null) {
            after = 0;
        } else {
            after = trigger.getDaysAfterEvent();
        }
        int triggerBeforeEventDate = 0;
        int triggerAfterEventDate = after;

        List<Learner> learnerList = learners;

        for (Learner learner : learnerList) {

            VU360UserNew vu360User = vu360userService.getVU360UserNewById(learner.getVu360User().getId());
            
           if(vu360User.getCreatedDate() !=null && trigger.getCreatedDate() !=null && 
        		   vu360User.getCreatedDate().after(trigger.getCreatedDate())){
           
	            if(trigger.getAlert().getIsDefault()) {
	            	
	            	Map<String, String> messageTemplateMap = new MessageTemplateGeneratorClass().createMessageForRegistration(
	                        vu360User.getFirstName(), vu360User.getLastName(),
	                        vu360User.getCreatedDate(),
	                        REGISTRATION_EVENT_TYPE, surveyService
	                );
	
	                Date dueDate = calculateDueDateForEventDrivenTrigger(
	                        triggerBeforeEventDate, triggerAfterEventDate,
	                        vu360User.getCreatedDate());
	
	                Add(
	                        null,
	                        TableNameEnum.VU360User, learner,
	                        vu360User.getFirstName(),
	                        vu360User.getLastName(),
	                        vu360User.getId(),
	                        vu360User.getId(), messageTemplateMap.get("body"),
	                        messageTemplateMap.get("subject"),
	                        vu360User.getEmailAddress(),
	                        REGISTRATION_EVENT_TYPE, triggerType, trigger,
	                        dueDate, false
	                );
	            } else {
	            	
	            	Date dueDate = calculateDueDateForEventDrivenTrigger(
	                        triggerBeforeEventDate, triggerAfterEventDate,
	                        vu360User.getCreatedDate());
	
	                Add(
	                        null,
	                        TableNameEnum.VU360User, learner,
	                        vu360User.getFirstName(),
	                        vu360User.getLastName(),
	                        vu360User.getId(),
	                        vu360User.getId(), trigger.getAlert().getAlertMessageBody(),
	                        trigger.getAlert().getAlertSubject(),
	                        vu360User.getEmailAddress(),
	                        REGISTRATION_EVENT_TYPE, triggerType, trigger,
	                        dueDate, false
	                );
	            }
           	}
        }

    }

    /*
     * This method create a queue for Learner license expiration 
     *
     *CE Training Alerts will enable manger and students to set alerts for the expiration of student’s license
     * @param learners
     * @param alertQueues
     * @param trigger
     * @param triggerType
     * */
     public void setupQueueForLearnerLicense(
             List<Learner> learners, AlertTrigger trigger,
             String triggerType) {

         int after, before;

         if (trigger.getDaysBeforeEvent() == null) {
             before = 0;
         } else {
             before = trigger.getDaysBeforeEvent();
         }
         if (trigger.getDaysAfterEvent() == null) {
             after = 0;
         } else {
             after = trigger.getDaysAfterEvent();
         }

         int triggerBeforeEventDate = before;
         int triggerAfterEventDate = after;

         List<Learner> learnerList = learners;

         long alertId;
         VU360UserNew vu360User;

         alertId = trigger.getAlert().getId();

         for (Learner learner : learnerList) {
        	 
             vu360User = vu360userService.getVU360UserNewById(learner.getVu360User().getId());

             // Only those learners which were happened after alert creation
             if(!vu360User.getCreatedDate().after(trigger.getAlert().getCreatedDate())) {
                 continue;
             }
             
        	 if(trigger.getAlert().getIsDefault()) {
        		
        		Date dueDate = calculateDueDateForEventDrivenTrigger(triggerBeforeEventDate, triggerAfterEventDate, trigger.getLicenseExpiratrionDate() );
        		
            	Map<String, String> messageTemplateMap = new MessageTemplateGeneratorClass().createMessageForLearnerLicense(
                        vu360User.getFirstName(), vu360User.getLastName(),
                        vu360User.getCreatedDate(),
                        LEARNERLICENSE_EVENT_TYPE, surveyService
                );
            	
            	Add(
                        null,
                        TableNameEnum.VU360User, learner,
                        vu360User.getFirstName(),
                        vu360User.getLastName(),
                        vu360User.getId(),
                        vu360User.getId(), messageTemplateMap.get("body"),
                        messageTemplateMap.get("subject"),
                        vu360User.getEmailAddress(),
                        LEARNERLICENSE_EVENT_TYPE, triggerType, trigger,
                        dueDate, false
                );

        	 }else{
        		 
             	Date dueDate = calculateDueDateForEventDrivenTrigger(triggerBeforeEventDate, triggerAfterEventDate, trigger.getLicenseExpiratrionDate() );

             	Add(
                        null,
                        TableNameEnum.VU360User, learner,
                        vu360User.getFirstName(),
                        vu360User.getLastName(),
                        vu360User.getId(),
                        vu360User.getId(), trigger.getAlert().getAlertMessageBody(),
                        trigger.getAlert().getAlertSubject(),
                        vu360User.getEmailAddress(),
                        LEARNERLICENSE_EVENT_TYPE, triggerType, trigger,
                        dueDate, false
                );

        	 }
         }
     }
     
    /*
    * This methods calculate due
    * date of date driven trigger
    *
    * @param triggerBeforeEventDate
    * @param triggerAfterEventDate
    * @param eventDate
    * @return Due date of date driven trigger
    * */
    public Date calculateDueDateForEventDrivenTrigger(int triggerBeforeEventDate,
                                                      int triggerAfterEventDate, Date eventDate) {

        Date dueDateOfEventDrivenTrigger = new Date();

        try {
            if (eventDate == null) {
                return dueDateOfEventDrivenTrigger;
            } else if (triggerBeforeEventDate <= 0 && triggerAfterEventDate <= 0) {
                return eventDate;
            } else if (triggerBeforeEventDate > 0) {
                dueDateOfEventDrivenTrigger.setTime(eventDate.getTime() - triggerBeforeEventDate * 24 * 60 * 60 * 1000);
            } else if (triggerAfterEventDate > 0) {
                dueDateOfEventDrivenTrigger.setTime(eventDate.getTime() + triggerAfterEventDate * 24 * 60 * 60 * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dueDateOfEventDrivenTrigger;

    }

    /*
     *
     * This method creates a queue for
     * Date Driven Trigger which
     * is not recurring
     *
     * @param learners
     * @param trigger
     * @param triggerType
     */
    public void createAlertQueueForDateDrivenTriggerIfTriggerNotRecurring(
            List<Learner> learners, AlertTrigger trigger,
            String triggerType) {


        // Learners of Alert's Recipients (based on
        // Alert Filter)
        List<Learner> learnerList = learners;

        for (Learner learner : learnerList) {


            // Get Learner details
            VU360UserNew vu360User = vu360userService.getVU360UserNewById(learner.getVu360User().getId());

            if(trigger.getAlert().getIsDefault()) {
            	
            	// Get Message details
                Map<String, String> messageTemplateMap = new MessageTemplateGeneratorClass().createMessageForDateDrivenTrigger(
                        vu360User.getFirstName(), vu360User.getLastName(),
                        vu360User.getCreatedDate(),
                        surveyService, DATE_DRIVEN_TRIGGER
                );

                // Add alert to queue
                Add(
                        DateDrivenEnum.NotRecurring,
                        null, learner,
                        vu360User.getFirstName(),
                        vu360User.getLastName(),
                        vu360User.getId(),
                        vu360User.getId(), messageTemplateMap.get("body"),
                        messageTemplateMap.get("subject"),
                        vu360User.getEmailAddress(),
                        null, triggerType, trigger,
                        null, true
                );
            } else {
            	
            	// Add alert to queue
                Add(
                        DateDrivenEnum.NotRecurring,
                        null, learner,
                        vu360User.getFirstName(),
                        vu360User.getLastName(),
                        vu360User.getId(),
                        vu360User.getId(), trigger.getAlert().getAlertMessageBody(),
                        trigger.getAlert().getAlertSubject(),
                        vu360User.getEmailAddress(),
                        null, triggerType, trigger,
                        null, true
                );
            }
            

        }

    }

    /*
     *
     * This method creates a queue for
     * Date Driven Trigger which
     * is recurring
     *
     * @param learners
     * @param trigger
     * @param triggerType
     */
    public void createAlertQueueForDateDrivenTriggerIfTriggerIsRecurring(
            List<Learner> learners, AlertTrigger trigger,
            String triggerType) {


        List<Learner> learnerList = learners;

        for (Learner learner : learnerList) {

            VU360UserNew vu360User = vu360userService.getVU360UserNewById(learner.getVu360User().getId());

            if(trigger.getAlert().getIsDefault()) {
            	
            	Map<String, String> messageTemplateMap = new MessageTemplateGeneratorClass().createMessageForDateDrivenTrigger(
                        vu360User.getFirstName(), vu360User.getLastName(),
                        vu360User.getCreatedDate(),
                        surveyService, DATE_DRIVEN_TRIGGER
                );

                Add(
                        DateDrivenEnum.TriggerRecurring,
                        TableNameEnum.AlertTriggerTable, learner,
                        vu360User.getFirstName(),
                        vu360User.getLastName(),
                        vu360User.getId(),
                        vu360User.getId(), messageTemplateMap.get("body"),
                        messageTemplateMap.get("subject"),
                        vu360User.getEmailAddress(),
                        null, triggerType, trigger,
                        null, true
                );
            } else {
            	
            	Add(
                        DateDrivenEnum.TriggerRecurring,
                        TableNameEnum.AlertTriggerTable, learner,
                        vu360User.getFirstName(),
                        vu360User.getLastName(),
                        vu360User.getId(),
                        vu360User.getId(), trigger.getAlert().getAlertMessageBody(),
                        trigger.getAlert().getAlertSubject(),
                        vu360User.getEmailAddress(),
                        null, triggerType, trigger,
                        null, true
                );
            }
            

        }

    }

    /*
   * Add a queue in the pool
   *
   * <p>
   * This methods add a queue
   *
   * */
    private void Add(DateDrivenEnum dateDrivenEnum, TableNameEnum tableNameEnum,
                     Learner learner, String firstName, String lastName,
                     long tableNameId, long userId,
                     String messageBody, String messageSubject,
                     String emailAddress, String eventType,
                     String triggerType, AlertTrigger trigger,
                     Date eventDueDate, boolean isMyAlert) {

        AlertQueue alertQueue = new AlertQueue();

        alertQueue.setLearnerFirstName(firstName);
        alertQueue.setLearnerLastName(lastName);

        alertQueue.setTableNameId(tableNameId);
        if (tableNameEnum != null) {
            alertQueue.setTableName(tableNameEnum.tableName);
        }

        alertQueue.setPendingMailStatus(true);

        alertQueue.setMessageSubject(messageSubject);
        alertQueue.setMessageBody(messageBody);

        alertQueue.setEmailAddress(emailAddress);

        if (dateDrivenEnum == DateDrivenEnum.NotRecurring) {

            if (triggerType.equalsIgnoreCase(eventType)) {

                alertQueue.setEventDueDate(trigger.getTriggerSingleDate());
                alertQueue.setRecurrence(trigger.getIsRecurring());

            }

        } else if (dateDrivenEnum == DateDrivenEnum.TriggerRecurring) {

            alertQueue.setEventDueDate(trigger.getTriggerSingleDate());
            alertQueue.setRecurrence(trigger.getIsRecurring());
            alertQueue.setMaxOccurences(trigger.getRangeOfRecurrrenceEndAfter());

            if (trigger.getRangeOfRecurrrenceStartDay() != null)

                alertQueue.setStartDate(trigger.getRangeOfRecurrrenceStartDay());

            else {

                alertQueue.setStartDate(trigger.getTriggerSingleDate());

            }

            alertQueue.setEndDate(trigger.getRangeOfRecurrrenceEndDay());
            alertQueue.setRecurrenceType(trigger.getRecurrrenceSchedule()); // means daily,weekly,monthly or yearly

            if (trigger.getRecurrrenceSchedule() != null) {

                if (trigger.getRecurrrenceSchedule().equals(RECURRENCE_TYPE_DAILY)) {

                    alertQueue.setDailyEveryDayInterval(Integer.parseInt(trigger.getDailyRecurrrenceEveryDay()));

                    if (trigger.getDailyRecurrrenceEveryWeekDay() != null && trigger.getDailyRecurrrenceEveryWeekDay().equals("everyWeekDay")) {

                        alertQueue.setDailyEveryWeekDay(true);

                    }

                } else if (trigger.getRecurrrenceSchedule().equals(RECURRENCE_TYPE_WEEKLY)) {

                    alertQueue.setWeeklyEveryWeekInterval(trigger.getWeeklyRecureEveryWeek());
                    alertQueue.setWeeklyEveryWeekOn(trigger.getWeeklyRecureEveryWeekOn());

                } else if (trigger.getRecurrrenceSchedule().equals(RECURRENCE_TYPE_MONTHLY)) {

                    alertQueue.setMonthlyRecurrenceTheEvery(trigger.getMonthlyRecurrenceTheEvery());
                    alertQueue.setMonthlyRecurrenceMonth(trigger.getMonthlyRecurrenceMonth());
                    alertQueue.setMonthlyRecurrenceType(trigger.getMonthlyRecurrenceType());
                    alertQueue.setMonthlyRecurrenceTypeDescriptor(trigger.getMonthlyRecurrenceTypeDescriptor());

                } else if (trigger.getRecurrrenceSchedule().equals(RECURRENCE_TYPE_YEARLY)) {

                    alertQueue.setYearlyRecurrenceTheEvery(trigger.getYearlyRecurrenceTheEvery());

                    if (trigger.getYearlyRecurrenceTheEvery().equals("every")) {

                        alertQueue.setYearlyEveryMonthName(trigger.getYearlyEveryMonthName());
                        alertQueue.setYearlyEveryMonthDay(trigger.getYearlyEveryMonthDay());

                    } else if (trigger.getYearlyRecurrenceTheEvery().equals("the")) {

                        alertQueue.setYearlyTheDayDescription(trigger.getYearlyTheDayDescription());
                        alertQueue.setYearlyTheDayTerm(trigger.getYearlyTheDayTerm());
                        alertQueue.setYearlyTheMonthDescription(trigger.getYearlyTheMonthDescription());

                    }

                }

            }

        } else if (dateDrivenEnum == null) {

            if (triggerType.equalsIgnoreCase(EVENT_DRIVEN_TRIGGER)) {

                alertQueue.setEventDueDate(eventDueDate);

            }

        }

        alertQueue.setLearner(learner);

        alertQueue.setEventTime(new Date());
        alertQueue.setTriggerType(triggerType);

        alertQueue.setAlert_Id(trigger.getAlert().getId());
        alertQueue.setUserId(userId);
        alertQueue.setTrigger_Id(trigger.getId());

        // notify all listeners before
        // add to queue
        notifyListenersBeforeQueueing(alertQueue);

        this.Items.add(alertQueue);

    }

    /*
    * Alert can have many triggers
    * and each trigger has their own
    * filters (if any)
    *
    * <p>
    * The instance of class represent a
    * Trigger of an Alert and on
    * instantiating it executes the trigger
    * rules
    *
    * Trigger is Callable which means when
    * instantiate it's object it will be
    * call back when it is done
    *
    * @param trigger AlertTrigger
    * */

    private class Trigger implements Callable {

        private AlertTrigger _trigger;

        public Trigger(AlertTrigger trigger) {

            Alert alert = null;
            List<Learner> alertLearnersByFilter = null;
            String alertMessage = "";

            // Trigger's Alert
            alert = trigger.getAlert();

            // Learners of all Recipients
            // (by Filter)
            alertLearnersByFilter = getLearnersByAlertRecipients(alert);

            // Alert message (which will be
            // sent off to the user when the
            // event occurred)
            alertMessage = alert.getAlertMessageBody();


            // Validate Trigger and it's Alert
            // Don't process any Trigger which
            // is already set to delete
            if (!trigger.isDelete() && !alert.getIsDelete()) {

                // Trigger's Events
                List<AvailableAlertEvent> availableAlertEvents = trigger.getAvailableAlertEvents();

                // If Trigger is Event Driven, then
                if (availableAlertEvents != null && availableAlertEvents.size() > 0) {

                    for (int i = 0; i < availableAlertEvents.size(); i++) {

                        String tableNameToScanFor = availableAlertEvents.get(i).getDbTableName();
                        String columnNameToRead = availableAlertEvents.get(i).getDbColumnName();

                        try {
                            if (alertLearnersByFilter != null && alertLearnersByFilter.size() > 0
                                    && tableNameToScanFor != null && availableAlertEvents.size() > 0) {

                                // If Trigger's event is Learner Registration
                                if (tableNameToScanFor.equalsIgnoreCase("VU360USER") && trigger != null) {

                                    // setup queue on Learner Registration
                                    setupQueueForLearnerRegistration(
                                            alertLearnersByFilter, trigger,
                                            EVENT_DRIVEN_TRIGGER
                                    );

                                }
                                // If Trigger's event is Course Completion
                                else if (tableNameToScanFor.equalsIgnoreCase("LEARNERCOURSESTATISTICS") && trigger != null) {

                                    // setup queue on course completion
                                    setupQueueForCourseCompletion(
                                            alertLearnersByFilter,
                                            trigger, EVENT_DRIVEN_TRIGGER
                                    );

                                } else if (tableNameToScanFor.equalsIgnoreCase("LEARNERENROLLMENT") && trigger != null) {


                                    // setup queue on learner enrollment
                                    setupQueueForLearnerEnrollment(
                                            alertLearnersByFilter, trigger,
                                            EVENT_DRIVEN_TRIGGER, alertMessage
                                    );

                                }else if (tableNameToScanFor.equalsIgnoreCase("LEARNERLICENSE") && trigger != null){

                                	// setup queue on learner license
                                	setupQueueForLearnerLicense( alertLearnersByFilter, trigger, EVENT_DRIVEN_TRIGGER );

                                }else{
                                	
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else if (availableAlertEvents.size() == 0) {    // means trigger is date driven

                    if (alert != null) {

                        if (!trigger.getIsRecurring() && alertLearnersByFilter != null && alertLearnersByFilter.size() > 0) { // if trigger is not recurring

                            createAlertQueueForDateDrivenTriggerIfTriggerNotRecurring(alertLearnersByFilter, trigger, DATE_DRIVEN_TRIGGER);

                        } else if (trigger.getIsRecurring() && alertLearnersByFilter != null && alertLearnersByFilter.size() > 0) { // means trigger is recurring

                            createAlertQueueForDateDrivenTriggerIfTriggerIsRecurring(alertLearnersByFilter, trigger, DATE_DRIVEN_TRIGGER);

                        }

                    }
                }
            }
            this._trigger = trigger;
        }

        @Override
        public AlertTrigger call() {
            return this._trigger;
        }
    }

}
