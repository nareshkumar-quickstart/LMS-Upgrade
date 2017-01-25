package com.softech.vu360.lms.autoAlertGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.SurveyService;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Properties;

//A correct implementation of a producer and consumer.
class ReadAndSendAlert { // this is a shared class between the two threads

	static final String EVENT_DRIVEN_TRIGGER = "eventDrivenTrigger";
	static final String DATE_DRIVEN_TRIGGER = "dateDrivenTrigger";

	static final String DAILY = "daily";
	static final String WEEKLY = "weekly";
	static final String MONTHLY = "monthly";
	static final String YEARLY = "yearly";
	static final String FOR_DAY = "day";
	static final String FOR_THE = "the";
	static final String FOR_EVERY = "every";

	int sendmailFlag = 0;
	static String[] weekArr = { "sunday", "monday", "tuesday", "wednesday",
			"thursday", "friday", "saturday" };
	Date newDate;

	// open/read the application context file
	ApplicationContext context = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	// instantiate our spring service object from the application context
	SurveyService surveyService = (SurveyService) context
			.getBean("surveyService");
	LearnerLicenseAlertService learnerlicensealertService = (LearnerLicenseAlertService) context
			.getBean("learneralertService");
	LearnerService learnerService = (LearnerService) context
			.getBean("learnerService");

	public final static double MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

	int n;
	boolean valueSet = true;

	List<AlertQueue> alertQueues = new ArrayList<AlertQueue>();

	public void getLicenseAlert() {
		List<LearnerLicenseAlert> alertlist = null;
		alertlist = learnerlicensealertService.getLearnerLicenseAlert();
		if (alertlist != null) {
			for (LearnerLicenseAlert learnerlicensealert : alertlist) {
				if (isExpirationDateToday(learnerlicensealert
						.getTriggerSingleDate())) {

					SendMailService
							.sendSMTPMessage(
									new String[] { learnerService
											.getLearnerByID(
													learnerlicensealert
															.getLearnerid())
											.getVu360User().getEmailAddress() },
									null,
									"lmsalerts@360training.com",
									VU360Properties
											.getVU360Property("lms.email.globalException.title"),
									VU360Properties
											.getVU360Property("lms.email.globalException.subject"),
									"<br> Request for Lernere License Registration. ");
				}
			}
		}
	}

	synchronized void readAlertQueue() { // method to read all alert queue with
											// pending mail status
		if (!valueSet) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException caught");
			}
		} else {
			try {
				// Thread.sleep(60000);
				long lSleep = Long.parseLong(MailConfigProperties
						.getMailProperty("readAlertQueue.threadsleep")); // 20
																			// mins
																			// delay
				Thread.sleep(lSleep);

				alertQueues = surveyService
						.getAllAlertQueuesWithPendingMailStatus();

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (alertQueues != null && alertQueues.size() > 0)
				valueSet = true;
			else
				valueSet = false;
			// notify();

		}
	}

	synchronized void sandMailAndUpdateStatus() { // methods used to send mail
													// and update mail send
													// status
		if (valueSet)

			for (int i = 0; i < alertQueues.size(); i++) {

				try {
					// Thread.sleep (60000); // 1 min delay
					long lSleep = Long
							.parseLong(MailConfigProperties
									.getMailProperty("sandMailAndUpdateStatus.threadsleep")); // 20
																								// mins
																								// delay
					Thread.sleep(lSleep);

					String mesg = (alertQueues.get(i).getMessageBody());
					String from = surveyService.getAlertByID(
							alertQueues.get(i).getAlert_Id()).getFromName();
					if (from == null) {
						from = "from";
					}
					if (alertQueues.get(i).isPendingMailStatus() == true
							&& alertQueues.get(i).getTriggerType()
									.equalsIgnoreCase(EVENT_DRIVEN_TRIGGER)) { // means
																				// mail
																				// not
																				// sent

						if (isDueDateToday(alertQueues.get(i).getEventDueDate()) == true) {
							String[] to = { alertQueues.get(i)
									.getEmailAddress() };
							sendSMTPMessage(
									MailConfigProperties
											.getMailProperty("HostName"),
									to, null, null, from, "", alertQueues
											.get(i).getMessageSubject(), mesg,
									null);
							AlertQueue alertQueue = surveyService
									.loadAlertQueueForUpdate(alertQueues.get(i)
											.getId());
							alertQueue.setPendingMailStatus(false); // means
																	// mail is
																	// sent
							alertQueue = surveyService
									.saveAlertQueue(alertQueue);
						}
					} else if (alertQueues.get(i).isPendingMailStatus() == true
							&& alertQueues.get(i).getTriggerType()
									.equalsIgnoreCase(DATE_DRIVEN_TRIGGER)) { // means
																				// trigger
																				// is
																				// date
																				// driven
						if (alertQueues.get(i).getRecurrence() == false) { // if
																			// trigger
																			// not
																			// recurring

							/*
							 * if trigger created with recurring=false, its
							 * queue created with null EventDueDate. Thats why
							 * it should fire on it's trigger Date.
							 */
							AlertTrigger alertTrigger = surveyService
									.getAlertTriggerByID(alertQueues.get(i)
											.getTrigger_Id());

							if (alertQueues.get(i).getEventDueDate() == null
									|| alertQueues.get(i).getEventDueDate()
											.equals(""))
								alertQueues.get(i).setEventDueDate(
										alertTrigger.getTriggerSingleDate());

							if (isDueDateToday(alertQueues.get(i)
									.getEventDueDate()) == true) {
								String[] to = { alertQueues.get(i)
										.getEmailAddress() };
								sendSMTPMessage(
										MailConfigProperties
												.getMailProperty("HostName"),
										to, null, null, from, "", alertQueues
												.get(i).getMessageSubject(),
										mesg, null);
								AlertQueue alertQueue = surveyService
										.loadAlertQueueForUpdate(alertQueues
												.get(i).getId());
								alertQueue.setPendingMailStatus(false); // means
																		// mail
																		// is
																		// sent
								alertQueue.setEventDueDate(alertTrigger
										.getTriggerSingleDate());
								alertQueue = surveyService
										.saveAlertQueue(alertQueue);
							}
						} else if (alertQueues.get(i).getRecurrence() == true) { // if
																				// trigger
																				// recurring
							if (alertQueues.get(i).getRecurrenceType() != null) {

								/*
								 * In case of start date is given, then do not
								 * consider eventDueDate For this purpose, just
								 * put startDate of trigger into evenDueDate in
								 * case of first iteration only and it is
								 * valid/run DAILY & WEEKLY, after that its
								 * updates EvenDueDate & OccuranceCount that
								 * help to recursive condition. for more info
								 * --> (LMS-8987)
								 */
								/*
								 * LMS-13210 No Need to set due date using start
								 * date
								 */
								/*
								 * if( (alertQueues.get(i).getRecurrenceType().
								 * equalsIgnoreCase(DAILY) ||
								 * alertQueues.get(i).
								 * getRecurrenceType().equalsIgnoreCase(WEEKLY))
								 * && alertQueues.get(i).getStartDate()!=null &&
								 * alertQueues.get(i).getOccuranceCount()==0){
								 * 
								 * //alertQueues.get(i).setEventDueDate(alertQueues
								 * .get(i).getStartDate()); }
								 */

								if (alertQueues.get(i).getRecurrenceType()
										.equalsIgnoreCase(DAILY)
										&& isDueDateToday(alertQueues.get(i)
												.getEventDueDate()) == true) {
									// Every x days Start
									if (alertQueues.get(i)
											.getDailyEveryDayInterval() == 0) {
										alertQueues.get(i)
												.setDailyEveryDayInterval(1);
									}
									// For Recurrence Schedule -Daily,
									// Recurrence - Every x days, Range of
									// Recurrence - nothing or no enddate
									if (alertQueues.get(i)
											.isDailyEveryWeekDay() == false
											&& alertQueues.get(i)
													.getMaxOccurences() == 0
											&& alertQueues.get(i).getEndDate() == null) {
										String[] to = { alertQueues.get(i)
												.getEmailAddress() };

										/* LMS-13210 */
										if (DateUtils.isSameDay(alertQueues
												.get(i).getEventDueDate(),
												new java.util.Date()) == true)
											sendSMTPMessage(
													MailConfigProperties
															.getMailProperty("HostName"),
													to,
													null,
													null,
													from,
													"",
													alertQueues
															.get(i)
															.getMessageSubject(),
													mesg, null);

										AlertQueue alertQueue = surveyService
												.loadAlertQueueForUpdate(alertQueues
														.get(i).getId());
										newDate = calculateDueDateForDateDrivenTrigger(
												alertQueues
														.get(i)
														.getDailyEveryDayInterval(),
												alertQueues.get(i)
														.getEventDueDate());
										if (newDate == alertQueues.get(i)
												.getEventDueDate()) {
											alertQueue
													.setPendingMailStatus(false); // means
																					// mail
																					// is
																					// sent
										} else if (newDate != alertQueues
												.get(i).getEventDueDate()) {
											alertQueue.setEventDueDate(newDate);
											alertQueue
													.setOccuranceCount(alertQueues
															.get(i)
															.getOccuranceCount() + 1);
											alertQueue
													.setPendingMailStatus(true);
										}
										alertQueue = surveyService
												.saveAlertQueue(alertQueue);
									} else if (alertQueues.get(i)
											.isDailyEveryWeekDay() == false
											&& alertQueues.get(i)
													.getMaxOccurences() > 0) {// For
																				// Recurrence
																				// Schedule
																				// -Daily,
																				// Recurrence
																				// -
																				// Every
																				// x
																				// days,
																				// Range
																				// of
																				// Recurrence
																				// -
																				// Fixed
																				// Occurences
										if (alertQueues.get(i)
												.getOccuranceCount() <= alertQueues
												.get(i).getMaxOccurences()) {
											AlertQueue alertQueue = surveyService
													.loadAlertQueueForUpdate(alertQueues
															.get(i).getId());
											if (alertQueues.get(i)
													.getOccuranceCount() == alertQueues
													.get(i).getMaxOccurences()) {
												alertQueue
														.setPendingMailStatus(false);// If
																						// MaxOccurences
											} else {// Less than MaxOccurences
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
												newDate = calculateDueDateForDateDrivenTrigger(
														alertQueues
																.get(i)
																.getDailyEveryDayInterval(),
														alertQueues
																.get(i)
																.getEventDueDate());
												if (newDate == alertQueues.get(
														i).getEventDueDate()) {
													alertQueue
															.setPendingMailStatus(false); // means
																							// mail
																							// is
																							// sent
												} else if (newDate != alertQueues
														.get(i)
														.getEventDueDate()
														&& alertQueues
																.get(i)
																.getOccuranceCount() < alertQueues
																.get(i)
																.getMaxOccurences()) {
													alertQueue
															.setEventDueDate(newDate);
													alertQueue
															.setOccuranceCount(alertQueues
																	.get(i)
																	.getOccuranceCount() + 1);
													alertQueue
															.setPendingMailStatus(true);
												}

											}
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										}
									} else if (alertQueues.get(i)
											.isDailyEveryWeekDay() == false
											&& alertQueues.get(i)
													.getMaxOccurences() == 0
											&& alertQueues.get(i).getEndDate() != null) { // For
																							// Recurrence
																							// Schedule
																							// -Daily,
																							// Recurrence
																							// -
																							// Every
																							// x
																							// days,
																							// Range
																							// of
																							// Recurrence
																							// -
																							// End
																							// date
																							// available
										Date endDateTime = alertQueues.get(i)
												.getEndDate();
										Date eventDateTime = alertQueues.get(i)
												.getEventDueDate();
										Date endDate = new java.util.Date(
												endDateTime.getDate());
										Date eventDate = new java.util.Date(
												eventDateTime.getDate());

										if (endDateTime.after(eventDateTime)) {
											String[] to = { alertQueues.get(i)
													.getEmailAddress() };
											sendSMTPMessage(
													MailConfigProperties
															.getMailProperty("HostName"),
													to,
													null,
													null,
													from,
													"",
													alertQueues
															.get(i)
															.getMessageSubject(),
													mesg, null);
											AlertQueue alertQueue = surveyService
													.loadAlertQueueForUpdate(alertQueues
															.get(i).getId());
											// System.out.println("---------------------------------"+alertQueues.get(i).getEventDueDate());
											newDate = calculateDueDateForDateDrivenTrigger(
													alertQueues
															.get(i)
															.getDailyEveryDayInterval(),
													alertQueues.get(i)
															.getEventDueDate());
											if (newDate == alertQueues.get(i)
													.getEventDueDate()) {
												alertQueue
														.setPendingMailStatus(false); // means
																						// mail
																						// is
																						// sent
											} else if (newDate != alertQueues
													.get(i).getEventDueDate()) {
												alertQueue
														.setEventDueDate(newDate);
												alertQueue
														.setOccuranceCount(alertQueues
																.get(i)
																.getOccuranceCount() + 1);
												alertQueue
														.setPendingMailStatus(true);
											}
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										}

										else if (endDate.equals(eventDate)) {

											String[] to = { alertQueues.get(i)
													.getEmailAddress() };
											sendSMTPMessage(
													MailConfigProperties
															.getMailProperty("HostName"),
													to,
													null,
													null,
													from,
													"",
													alertQueues
															.get(i)
															.getMessageSubject(),
													mesg, null);
											AlertQueue alertQueue = surveyService
													.loadAlertQueueForUpdate(alertQueues
															.get(i).getId());
											// System.out.println("---------------------------------"+alertQueues.get(i).getEventDueDate());
											newDate = calculateDueDateForDateDrivenTrigger(
													alertQueues
															.get(i)
															.getDailyEveryDayInterval(),
													alertQueues.get(i)
															.getEventDueDate());
											alertQueue
													.setPendingMailStatus(false);
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);

										}
									}

									// Every x days Close

									// Every Weekday Start
									// For Recurrence Schedule -Daily,
									// Recurrence - Every Weekday, Range of
									// Recurrence - nothing or no enddate
									if (alertQueues.get(i)
											.isDailyEveryWeekDay() == true
											&& alertQueues.get(i)
													.getMaxOccurences() == 0
											&& alertQueues.get(i).getEndDate() == null) {
										String[] to = { alertQueues.get(i)
												.getEmailAddress() };

										if (DateUtil.isTodayAWeekDay()) {
											/* LMS-13210 */
											if (DateUtils.isSameDay(alertQueues
													.get(i).getEventDueDate(),
													new java.util.Date()) == true)
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
										}
										AlertQueue alertQueue = surveyService
												.loadAlertQueueForUpdate(alertQueues
														.get(i).getId());
										newDate = calculateDueDateForDateDrivenTrigger(
												1, alertQueues.get(i)
														.getEventDueDate());
										if (newDate == alertQueues.get(i)
												.getEventDueDate()) {
											alertQueue
													.setPendingMailStatus(false); // means
																					// mail
																					// is
																					// sent
										} else if (newDate != alertQueues
												.get(i).getEventDueDate()) {
											alertQueue.setEventDueDate(newDate);
											alertQueue
													.setPendingMailStatus(true);
										}
										alertQueue = surveyService
												.saveAlertQueue(alertQueue);
									} else if (alertQueues.get(i)
											.isDailyEveryWeekDay() == true
											&& alertQueues.get(i)
													.getMaxOccurences() > 0) {// For
																				// Recurrence
																				// Schedule
																				// -Daily,
																				// Recurrence
																				// -
																				// Every
																				// Weekday,
																				// Range
																				// of
																				// Recurrence
																				// -
																				// Fixed
																				// Occurences
										if (alertQueues.get(i)
												.getOccuranceCount() <= alertQueues
												.get(i).getMaxOccurences()) {
											AlertQueue alertQueue = surveyService
													.loadAlertQueueForUpdate(alertQueues
															.get(i).getId());
											if (alertQueues.get(i)
													.getOccuranceCount() == alertQueues
													.get(i).getMaxOccurences()) {
												alertQueue
														.setPendingMailStatus(false);// If
																						// MaxOccurences
											} else {// Less than MaxOccurences
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												if (DateUtil.isTodayAWeekDay()) {
													/* LMS-13210 */
													sendSMTPMessage(
															MailConfigProperties
																	.getMailProperty("HostName"),
															to,
															null,
															null,
															from,
															"",
															alertQueues
																	.get(i)
																	.getMessageSubject(),
															mesg, null);
												}
												newDate = calculateDueDateForDateDrivenTrigger(
														1,
														alertQueues
																.get(i)
																.getEventDueDate());
												if (newDate == alertQueues.get(
														i).getEventDueDate()) {
													alertQueue
															.setPendingMailStatus(false); // means
																							// mail
																							// is
																							// sent
												} else if (newDate != alertQueues
														.get(i)
														.getEventDueDate()
														&& alertQueues
																.get(i)
																.getOccuranceCount() < alertQueues
																.get(i)
																.getMaxOccurences()) {
													alertQueue
															.setEventDueDate(newDate);
													if (DateUtil
															.isTodayAWeekDay()) {
														alertQueue
																.setOccuranceCount(alertQueues
																		.get(i)
																		.getOccuranceCount() + 1);
													}
													alertQueue
															.setPendingMailStatus(true);
												}

											}
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										}
									}

									else if (alertQueues.get(i)
											.isDailyEveryWeekDay() == true
											&& alertQueues.get(i)
													.getMaxOccurences() == 0
											&& alertQueues.get(i).getEndDate() != null) { // For
																							// Recurrence
																							// Schedule
																							// -Daily,
																							// Recurrence
																							// -
																							// Every
																							// Weekday,
																							// Range
																							// of
																							// Recurrence
																							// -
																							// End
																							// date
																							// available

										Date endDateTimeWeekday = alertQueues
												.get(i).getEndDate();
										Date eventDateTimeWeekday = alertQueues
												.get(i).getEventDueDate();
										Date endDateWeekday = new java.util.Date(
												endDateTimeWeekday.getDate());
										Date eventDateWeekday = new java.util.Date(
												eventDateTimeWeekday.getDate());

										if (endDateTimeWeekday
												.after(eventDateTimeWeekday)) {
											String[] to = { alertQueues.get(i)
													.getEmailAddress() };
											if (DateUtil.isTodayAWeekDay()) {
												/* LMS-13210 */
												if (DateUtils
														.isSameDay(
																alertQueues
																		.get(i)
																		.getEventDueDate(),
																new java.util.Date()) == true)
													sendSMTPMessage(
															MailConfigProperties
																	.getMailProperty("HostName"),
															to,
															null,
															null,
															from,
															"",
															alertQueues
																	.get(i)
																	.getMessageSubject(),
															mesg, null);
											}
											AlertQueue alertQueue = surveyService
													.loadAlertQueueForUpdate(alertQueues
															.get(i).getId());
											newDate = calculateDueDateForDateDrivenTrigger(
													1, alertQueues.get(i)
															.getEventDueDate());
											if (newDate == alertQueues.get(i)
													.getEventDueDate()) {
												alertQueue
														.setPendingMailStatus(false); // means
																						// mail
																						// is
																						// sent
											} else if (newDate != alertQueues
													.get(i).getEventDueDate()) {
												alertQueue
														.setEventDueDate(newDate);
												alertQueue
														.setPendingMailStatus(true);
											}
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										}

										else if (endDateWeekday
												.equals(eventDateWeekday)) {

											String[] to = { alertQueues.get(i)
													.getEmailAddress() };
											if (DateUtil.isTodayAWeekDay()) {
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
											}
											AlertQueue alertQueue = surveyService
													.loadAlertQueueForUpdate(alertQueues
															.get(i).getId());
											// System.out.println("---------------------------------"+alertQueues.get(i).getEventDueDate());
											newDate = calculateDueDateForDateDrivenTrigger(
													1, alertQueues.get(i)
															.getEventDueDate());
											alertQueue
													.setPendingMailStatus(false);
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);

										}

										// Every Weekday Close

									}
								}// is today end

								// WEEKLY Start

								else if (alertQueues.get(i).getRecurrenceType()
										.equalsIgnoreCase(WEEKLY)
										&& isDueDateToday(alertQueues.get(i)
												.getEventDueDate()) == true) {
									int weekIntervalToSendAlert = alertQueues
											.get(i)
											.getWeeklyEveryWeekInterval();
									if (weekIntervalToSendAlert == 0) {
										alertQueues.get(i)
												.setWeeklyEveryWeekInterval(1); // 0
																				// means
																				// user
																				// input
																				// was
																				// wrong
																				// at
																				// the
																				// time
																				// of
																				// creation
										weekIntervalToSendAlert = 1;
									}
									String weeklyEveryWeekOn[];
									AlertQueue alertQueue = surveyService
											.loadAlertQueueForUpdate(alertQueues
													.get(i).getId());
									// no end date , max occ 0 ,
									if (alertQueues.get(i)
											.getWeeklyEveryWeekInterval() > 0
											&& alertQueues.get(i).getEndDate() == null
											&& alertQueues.get(i)
													.getMaxOccurences() == 0) {

										if (alertQueues.get(i)
												.getWeeklyEveryWeekOn() != null) {
											weeklyEveryWeekOn = alertQueues
													.get(i)
													.getWeeklyEveryWeekOn()
													.split(":");
											for (int x = 0; x < weeklyEveryWeekOn.length; x++) {
												Calendar calendar = Calendar
														.getInstance();
												// if(weeklyEveryWeekOn[x].equalsIgnoreCase(weekArr[calendar.get(Calendar.WEEK_OF_MONTH)])){
												// // if weekday matches
												int dayNumber = 0;
												long dateDiff = DateUtil
														.getTodaysDayDifferenceFromAParticularDate(alertQueues
																.get(i)
																.getStartDate());
												long weeks = dateDiff / 7; // weeks
																			// intervel
												dayNumber = dayNumberFinder(weeklyEveryWeekOn[x]);
												if ((weeks % weekIntervalToSendAlert) == 0
														&& alertQueues
																.get(i)
																.getEventDueDate()
																.getDay() == dayNumber) { // if
																							// condition
																							// matches
																							// week
																							// interval
													String[] to = { alertQueues
															.get(i)
															.getEmailAddress() };
													sendSMTPMessage(
															MailConfigProperties
																	.getMailProperty("HostName"),
															to,
															null,
															null,
															from,
															"",
															alertQueues
																	.get(i)
																	.getMessageSubject(),
															mesg, null);
													alertQueue
															.setPendingMailStatus(true); // means
																							// mail
																							// is
																							// sent
													alertQueue = surveyService
															.saveAlertQueue(alertQueue);
												}
												// }
											}
										}
										newDate = calculateDueDateForDateDrivenTrigger(
												1, alertQueues.get(i)
														.getEventDueDate());
										alertQueue.setEventDueDate(newDate);// updates
																			// every
																			// time
																			// ,
																			// as
																			// we
																			// can
																			// not
																			// add
																			// x*7
																			// for
																			// due
																			// date
																			// calculation.
										alertQueue.setOccuranceCount(alertQueue
												.getOccuranceCount() + 1);
										alertQueue = surveyService
												.saveAlertQueue(alertQueue);
									}
									// end date , max occ 0 ,
									else if (alertQueues.get(i)
											.getWeeklyEveryWeekInterval() > 0
											&& alertQueues.get(i).getEndDate() != null
											&& alertQueues.get(i)
													.getMaxOccurences() == 0) {
										weeklyEveryWeekOn = alertQueues.get(i)
												.getWeeklyEveryWeekOn()
												.split(":");
										if (alertQueues.get(i)
												.getWeeklyEveryWeekOn() != null) {
											for (int x = 0; x < weeklyEveryWeekOn.length; x++) {
												Calendar calendar = Calendar
														.getInstance();
												// if(weeklyEveryWeekOn[x].equalsIgnoreCase(weekArr[calendar.get(Calendar.WEEK_OF_MONTH)])){
												// // if weekday matches
												int dayNumber = 0;
												long dateDiff = DateUtil
														.getTodaysDayDifferenceFromAParticularDate(alertQueues
																.get(i)
																.getStartDate());
												long weeks = dateDiff / 7; // weeks
																			// intervel
												dayNumber = dayNumberFinder(weeklyEveryWeekOn[x]);
												if ((weeks % weekIntervalToSendAlert) == 0
														&& alertQueues
																.get(i)
																.getEventDueDate()
																.getDay() == dayNumber) { // if
																							// condition
																							// matches
																							// week
																							// interval
													String[] to = { alertQueues
															.get(i)
															.getEmailAddress() };
													sendSMTPMessage(
															MailConfigProperties
																	.getMailProperty("HostName"),
															to,
															null,
															null,
															from,
															"",
															alertQueues
																	.get(i)
																	.getMessageSubject(),
															mesg, null);
													alertQueue
															.setPendingMailStatus(true); // means
																							// mail
																							// is
																							// sent
													alertQueue = surveyService
															.saveAlertQueue(alertQueue);
												}
												// }
											}
										}
										Date endDate = alertQueues.get(i)
												.getEndDate();
										Date eventDate = alertQueues.get(i)
												.getEventDueDate();
										Date endDateWeekly = new java.util.Date(
												endDate.getDate());
										Date eventDateWeekly = new java.util.Date(
												eventDate.getDate());
										// Checks the end date condition
										if (endDateWeekly
												.equals(eventDateWeekly)) {
											alertQueue
													.setPendingMailStatus(false);
										}
										newDate = calculateDueDateForDateDrivenTrigger(
												1, alertQueues.get(i)
														.getEventDueDate());
										alertQueue.setEventDueDate(newDate);// updates
																			// every
																			// time
																			// ,
																			// as
																			// we
																			// can
																			// not
																			// add
																			// x*7
																			// for
																			// due
																			// date
																			// calculation.
										alertQueue
												.setOccuranceCount(alertQueues
														.get(i)
														.getOccuranceCount() + 1);
										alertQueue = surveyService
												.saveAlertQueue(alertQueue);
									}

									// no end date , max occ > 0 ,
									else if (alertQueues.get(i)
											.getWeeklyEveryWeekInterval() > 0
											&& alertQueues.get(i).getEndDate() == null
											&& alertQueues.get(i)
													.getMaxOccurences() > 0) {
										weeklyEveryWeekOn = alertQueues.get(i)
												.getWeeklyEveryWeekOn()
												.split(":");
										if (alertQueues.get(i)
												.getWeeklyEveryWeekOn() != null) {
											for (int x = 0; x < weeklyEveryWeekOn.length; x++) {
												Calendar calendar = Calendar
														.getInstance();
												// if(weeklyEveryWeekOn[x].equalsIgnoreCase(weekArr[calendar.get(Calendar.WEEK_OF_MONTH)])){
												// // if weekday matches
												int dayNumber = 0;
												long dateDiff = DateUtil
														.getTodaysDayDifferenceFromAParticularDate(alertQueues
																.get(i)
																.getStartDate());
												long weeks = dateDiff / 7; // weeks
																			// intervel
												dayNumber = dayNumberFinder(weeklyEveryWeekOn[x]);
												if ((weeks % weekIntervalToSendAlert) == 0
														&& alertQueues
																.get(i)
																.getEventDueDate()
																.getDay() == dayNumber) { // if
																							// condition
																							// matches
																							// week
																							// interval
													String[] to = { alertQueues
															.get(i)
															.getEmailAddress() };
													sendSMTPMessage(
															MailConfigProperties
																	.getMailProperty("HostName"),
															to,
															null,
															null,
															from,
															"",
															alertQueues
																	.get(i)
																	.getMessageSubject(),
															mesg, null);
													alertQueue
															.setPendingMailStatus(true); // means
																							// mail
																							// is
																							// sent
													alertQueue
															.setOccuranceCount(alertQueues
																	.get(i)
																	.getOccuranceCount() + 1);
													alertQueue = surveyService
															.saveAlertQueue(alertQueue);
												}
												// }
											}
										}

										// Checks the end date condition
										if (alertQueues.get(i)
												.getMaxOccurences() == alertQueues
												.get(i).getOccuranceCount()) {
											alertQueue
													.setPendingMailStatus(false);
										}
										newDate = calculateDueDateForDateDrivenTrigger(
												1, alertQueues.get(i)
														.getEventDueDate());
										alertQueue.setEventDueDate(newDate);// updates
																			// every
																			// time
																			// ,
																			// as
																			// we
																			// can
																			// not
																			// add
																			// x*7
																			// for
																			// due
																			// date
																			// calculation.
										alertQueue = surveyService
												.saveAlertQueue(alertQueue);
									}
								}

								// End of Weekly type.

								// START MONTHLY
								else if (alertQueues.get(i).getRecurrenceType()
										.equalsIgnoreCase(MONTHLY)
										&& isDueDateToday(alertQueues.get(i)
												.getEventDueDate()) == true) {
									Calendar gcal = Calendar.getInstance();
									SimpleDateFormat dateFormat = new SimpleDateFormat(
											"MM/dd/yyyy hh:mm:ss");
									String dateString = "";
									Date nextDateMonthly;
									Date eventDateMonthly;

									if (alertQueues.get(i)
											.getMonthlyRecurrenceType() == 0) {
										alertQueues.get(i)
												.setMonthlyRecurrenceType(1);
									}
									if (alertQueues.get(i)
											.getMonthlyRecurrenceMonth() == 0) {
										alertQueues.get(i)
												.setMonthlyRecurrenceMonth(1);
									}
									Date newDate;
									if (isDueDateToday(alertQueues.get(i)
											.getEventDueDate()) == true) {
										AlertQueue alertQueue = surveyService
												.loadAlertQueueForUpdate(alertQueues
														.get(i).getId());
										// MONTHLY WITH ENDDATE , TYPE DAY,
										// MonthlyRecurrence >0
										if (alertQueues.get(i)
												.getMonthlyRecurrenceTheEvery() != null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTheEvery()
														.equalsIgnoreCase(
																FOR_DAY)
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceType() > 0
												&& alertQueues.get(i)
														.getEndDate() != null
												&& alertQueues.get(i)
														.getMaxOccurences() == 0) {
											Date endDateMonthly = alertQueues
													.get(i).getEndDate();
											eventDateMonthly = alertQueues.get(
													i).getEventDueDate();

											if (endDateMonthly
													.after(eventDateMonthly)) {
												int dayOn = alertQueues
														.get(i)
														.getMonthlyRecurrenceType();
												int everyMonth = alertQueues
														.get(i)
														.getMonthlyRecurrenceMonth();
												int count = 0;
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);

												try {
													do {
														if (dayOn > 0) {
															int month = eventDateMonthly
																	.getMonth();
															int hour = eventDateMonthly
																	.getHours();
															int min = eventDateMonthly
																	.getMinutes();
															int second = eventDateMonthly
																	.getSeconds();
															eventDateMonthly
																	.setDate(dayOn);
															eventDateMonthly
																	.setMonth(month
																			+ everyMonth);
															eventDateMonthly
																	.setHours(hour);
															eventDateMonthly
																	.setMinutes(min);
															eventDateMonthly
																	.setSeconds(second);
															count++;
															dateString = dateFormat
																	.format(eventDateMonthly);
															nextDateMonthly = new Date(
																	dateString);
															alertQueue
																	.setPendingMailStatus(true); // means
																									// mail
																									// is
																									// sent
															alertQueue
																	.setEventDueDate(nextDateMonthly);
															System.out
																	.println(nextDateMonthly);
															alertQueue = surveyService
																	.saveAlertQueue(alertQueue);
														}
													} while (count < 1);
												} catch (Exception e) {
													e.printStackTrace();
												}

											} else {
												alertQueue
														.setPendingMailStatus(false);
												alertQueue = surveyService
														.saveAlertQueue(alertQueue);
											}
										}
										// IF END DATE NOT AVAILABLE AND
										// MAXOCCOURANCE
										if (alertQueues.get(i)
												.getMonthlyRecurrenceTheEvery() != null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTheEvery()
														.equalsIgnoreCase(
																FOR_DAY)
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceType() > 0
												&& alertQueues.get(i)
														.getEndDate() == null
												&& alertQueues.get(i)
														.getMaxOccurences() > 0) {

											eventDateMonthly = alertQueues.get(
													i).getEventDueDate();
											int maxOccourance = alertQueues
													.get(i).getMaxOccurences();
											if (maxOccourance > alertQueues
													.get(i).getOccuranceCount()) {
												int dayOn = alertQueues
														.get(i)
														.getMonthlyRecurrenceType();
												int everyMonth = alertQueues
														.get(i)
														.getMonthlyRecurrenceMonth();
												int count = 0;
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
												alertQueue
														.setPendingMailStatus(true); // means
																						// mail
																						// is
																						// sent
												try {
													do {
														if (dayOn > 0) {
															int month = eventDateMonthly
																	.getMonth();
															int hour = eventDateMonthly
																	.getHours();
															int min = eventDateMonthly
																	.getMinutes();
															int second = eventDateMonthly
																	.getSeconds();
															eventDateMonthly
																	.setDate(dayOn);
															eventDateMonthly
																	.setMonth(month
																			+ everyMonth);
															eventDateMonthly
																	.setHours(hour);
															eventDateMonthly
																	.setMinutes(min);
															eventDateMonthly
																	.setSeconds(second);
															count++;

															dateString = dateFormat
																	.format(eventDateMonthly);
															nextDateMonthly = new Date(
																	dateString);
															alertQueue
																	.setEventDueDate(nextDateMonthly);
															alertQueue
																	.setOccuranceCount(alertQueues
																			.get(i)
																			.getOccuranceCount() + 1);
															alertQueue = surveyService
																	.saveAlertQueue(alertQueue);
														}
													} while (count < 1);
												} catch (Exception e) {
													e.printStackTrace();
												}

											} else {
												alertQueue
														.setPendingMailStatus(false);
												alertQueue = surveyService
														.saveAlertQueue(alertQueue);
											}
										}

										// IF END DATE NOT AVAILABLE AND
										// MAXOCCOURANCE NOT AVAILABLE
										if (alertQueues.get(i)
												.getMonthlyRecurrenceTheEvery() != null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTheEvery()
														.equalsIgnoreCase(
																FOR_DAY)
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceType() > 0
												&& alertQueues.get(i)
														.getEndDate() == null
												&& alertQueues.get(i)
														.getMaxOccurences() == 0) {

											eventDateMonthly = alertQueues.get(
													i).getEventDueDate();
											int dayOn = alertQueues.get(i)
													.getMonthlyRecurrenceType();
											int everyMonth = alertQueues
													.get(i)
													.getMonthlyRecurrenceMonth();
											int count = 0;
											String[] to = { alertQueues.get(i)
													.getEmailAddress() };
											sendSMTPMessage(
													MailConfigProperties
															.getMailProperty("HostName"),
													to,
													null,
													null,
													from,
													"",
													alertQueues
															.get(i)
															.getMessageSubject(),
													mesg, null);
											alertQueue
													.setPendingMailStatus(true); // means
																					// mail
																					// is
																					// sent
											try {
												do {
													if (dayOn > 0) {
														int month = eventDateMonthly
																.getMonth();
														int hour = eventDateMonthly
																.getHours();
														int min = eventDateMonthly
																.getMinutes();
														int second = eventDateMonthly
																.getSeconds();
														eventDateMonthly
																.setDate(dayOn);
														eventDateMonthly
																.setMonth(month
																		+ everyMonth);
														eventDateMonthly
																.setHours(hour);
														eventDateMonthly
																.setMinutes(min);
														eventDateMonthly
																.setSeconds(second);
														count++;

														dateString = dateFormat
																.format(eventDateMonthly);
														nextDateMonthly = new Date(
																dateString);
														alertQueue
																.setEventDueDate(nextDateMonthly);

														alertQueue = surveyService
																.saveAlertQueue(alertQueue);
													}
												} while (count < 1);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
										// TYPE THE ENDDATE AVAILABLE
										if (alertQueues.get(i)
												.getMonthlyRecurrenceTheEvery() != null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTheEvery()
														.equalsIgnoreCase(
																FOR_THE)
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceType() > 0
												&& alertQueues.get(i)
														.getEndDate() != null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceMonth() > 0
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTypeDescriptor() != null
												&& alertQueues.get(i)
														.getMaxOccurences() == 0) {
											Date endDateMonthly = alertQueues
													.get(i).getEndDate();
											eventDateMonthly = alertQueues.get(
													i).getEventDueDate();
											if (endDateMonthly
													.after(eventDateMonthly)) {
												int myweek = alertQueues
														.get(i)
														.getMonthlyRecurrenceType();
												int mymonth = alertQueues
														.get(i)
														.getMonthlyRecurrenceMonth();
												String myday = dayNameFinder(alertQueues
														.get(i)
														.getMonthlyRecurrenceTypeDescriptor());
												int count = 0;
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
												// means mail is sent
												try {
													do {
														if (myweek > 0) {
															newDate = duedate(
																	gcal,
																	eventDateMonthly,
																	myweek,
																	mymonth,
																	myday);
															count++;

															dateString = dateFormat
																	.format(newDate);
															nextDateMonthly = new Date(
																	dateString);
															alertQueue
																	.setEventDueDate(nextDateMonthly);
															alertQueue
																	.setPendingMailStatus(true);
															alertQueue = surveyService
																	.saveAlertQueue(alertQueue);
														}
													} while (count < 1);
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

											else {
												alertQueue
														.setPendingMailStatus(false);
												alertQueue = surveyService
														.saveAlertQueue(alertQueue);
											}
										}

										// TYPE THE ENDDATE NOT AVAILABLE ,
										// MAXOCCOURANCE AVAILABLE
										if (alertQueues.get(i)
												.getMonthlyRecurrenceTheEvery() != null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTheEvery()
														.equalsIgnoreCase(
																FOR_THE)
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceType() > 0
												&& alertQueues.get(i)
														.getEndDate() == null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceMonth() > 0
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTypeDescriptor() != null
												&& alertQueues.get(i)
														.getMaxOccurences() > 0) {

											eventDateMonthly = alertQueues.get(
													i).getEventDueDate();
											int maxOccourance = alertQueues
													.get(i).getMaxOccurences();

											if (maxOccourance > alertQueues
													.get(i).getOccuranceCount()) {
												int myweek = alertQueues
														.get(i)
														.getMonthlyRecurrenceType();
												int mymonth = alertQueues
														.get(i)
														.getMonthlyRecurrenceMonth();
												String myday = dayNameFinder(alertQueues
														.get(i)
														.getMonthlyRecurrenceTypeDescriptor());
												int count = 0;
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
												// means mail is sent
												try {
													do {
														if (myweek > 0) {
															newDate = duedate(
																	gcal,
																	eventDateMonthly,
																	myweek,
																	mymonth,
																	myday);
															count++;
															System.out
																	.println("########### 1 "
																			+ newDate);
															System.out
																	.println("########### 1 "
																			+ eventDateMonthly
																			+ "##"
																			+ myweek
																			+ "##"
																			+ mymonth
																			+ "##"
																			+ myday);

															dateString = dateFormat
																	.format(newDate);
															nextDateMonthly = new Date(
																	dateString);
															alertQueue
																	.setEventDueDate(nextDateMonthly);
															alertQueue
																	.setPendingMailStatus(true);
															alertQueue
																	.setOccuranceCount(alertQueues
																			.get(i)
																			.getOccuranceCount() + 1);
															alertQueue = surveyService
																	.saveAlertQueue(alertQueue);
														}
													} while (count < 1);
												} catch (Exception e) {
													e.printStackTrace();
												}
											}

											else {
												alertQueue
														.setPendingMailStatus(false);
												alertQueue = surveyService
														.saveAlertQueue(alertQueue);
											}
										}

										// NO END DATE
										if (alertQueues.get(i)
												.getMonthlyRecurrenceTheEvery() != null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTheEvery()
														.equalsIgnoreCase(
																FOR_THE)
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceType() > 0
												&& alertQueues.get(i)
														.getEndDate() == null
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceMonth() > 0
												&& alertQueues
														.get(i)
														.getMonthlyRecurrenceTypeDescriptor() != null
												&& alertQueues.get(i)
														.getMaxOccurences() == 0) {

											eventDateMonthly = alertQueues.get(
													i).getEventDueDate();

											int myweek = alertQueues.get(i)
													.getMonthlyRecurrenceType();
											int mymonth = alertQueues
													.get(i)
													.getMonthlyRecurrenceMonth();
											String myday = dayNameFinder(alertQueues
													.get(i)
													.getMonthlyRecurrenceTypeDescriptor());
											int count = 0;
											// means mail is sent
											try {
												do {
													if (myweek > 0) {
														String[] to = { alertQueues
																.get(i)
																.getEmailAddress() };
														sendSMTPMessage(
																MailConfigProperties
																		.getMailProperty("HostName"),
																to,
																null,
																null,
																from,
																"",
																alertQueues
																		.get(i)
																		.getMessageSubject(),
																mesg, null);

														newDate = duedate(
																gcal,
																eventDateMonthly,
																myweek,
																mymonth, myday);
														System.out
																.println("########### 2 "
																		+ newDate);
														System.out
																.println("########### 2 "
																		+ eventDateMonthly
																		+ "##"
																		+ myweek
																		+ "##"
																		+ mymonth
																		+ "##"
																		+ myday);
														count++;
														dateString = dateFormat
																.format(newDate);
														nextDateMonthly = new Date(
																dateString);

														alertQueue
																.setEventDueDate(nextDateMonthly);
														alertQueue
																.setPendingMailStatus(true);
														alertQueue = surveyService
																.saveAlertQueue(alertQueue);
													}
												} while (count < 1);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}
								} else if (alertQueues.get(i)
										.getRecurrenceType()
										.equalsIgnoreCase(YEARLY)
										&& isDueDateToday(alertQueues.get(i)
												.getEventDueDate()) == true) {
									Calendar calYearly = Calendar.getInstance();
									int MonthNumber = 0;
									int MonthNow = 0;
									SimpleDateFormat dateFormatYearly = new SimpleDateFormat(
											"MM/dd/yyyy hh:mm:ss");
									String dateStringYearly = "";
									Date nextDateYearly;
									Date nextYearly;
									Date endDateYearly;
									Date eventDateYearly;
									Date eventDateYearlyOldDate;
									Date today = new Date();

									if (alertQueues.get(i)
											.getYearlyEveryMonthDay() == 0) {
										alertQueues.get(i)
												.setYearlyEveryMonthDay(1);
									}

									AlertQueue alertQueue = surveyService
											.loadAlertQueueForUpdate(alertQueues
													.get(i).getId());
									if (alertQueues.get(i)
											.getYearlyRecurrenceTheEvery()
											.equalsIgnoreCase(FOR_EVERY)
											&& alertQueues.get(i)
													.getYearlyEveryMonthName() != null
											&& alertQueues.get(i).getEndDate() != null
											&& alertQueues.get(i)
													.getYearlyEveryMonthDay() > 0
											&& alertQueues.get(i)
													.getMaxOccurences() == 0) {
										endDateYearly = alertQueues.get(i)
												.getEndDate();
										eventDateYearly = alertQueues.get(i)
												.getEventDueDate();
										eventDateYearlyOldDate = alertQueues
												.get(i).getEventDueDate();
										if (endDateYearly
												.after(eventDateYearly)) {
											MonthNumber = monthNumber(alertQueues
													.get(i)
													.getYearlyEveryMonthName());
											try {
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };

												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
												eventDateYearly
														.setDate(alertQueues
																.get(i)
																.getYearlyEveryMonthDay());
												eventDateYearly
														.setMonth(MonthNumber - 1);

												eventDateYearly
														.setYear(eventDateYearly
																.getYear());
												if (eventDateYearlyOldDate
														.after(eventDateYearly)) {

													eventDateYearly
															.setYear(eventDateYearly
																	.getYear() + 1);
												} else if (eventDateYearlyOldDate
														.equals(eventDateYearly)) {

													eventDateYearly
															.setYear(eventDateYearly
																	.getYear() + 1);

												}

												if (endDateYearly
														.after(eventDateYearly)) {
													dateStringYearly = dateFormatYearly
															.format(eventDateYearly);
													nextDateYearly = new Date(
															dateStringYearly);
													alertQueue
															.setEventDueDate(nextDateYearly);
													alertQueue
															.setPendingMailStatus(true);
													alertQueue = surveyService
															.saveAlertQueue(alertQueue);
												} else {
													alertQueue
															.setPendingMailStatus(false);
													alertQueue = surveyService
															.saveAlertQueue(alertQueue);
												}

											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											alertQueue
													.setPendingMailStatus(false);
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										}
									}

									if (alertQueues.get(i)
											.getYearlyRecurrenceTheEvery()
											.equalsIgnoreCase(FOR_EVERY)
											&& alertQueues.get(i)
													.getYearlyEveryMonthName() != null
											&& alertQueues.get(i).getEndDate() == null
											&& alertQueues.get(i)
													.getYearlyEveryMonthDay() > 0
											&& alertQueues.get(i)
													.getMaxOccurences() > 0) {
										eventDateYearly = alertQueues.get(i)
												.getEventDueDate();
										if (alertQueues.get(i)
												.getMaxOccurences() > alertQueues
												.get(i).getOccuranceCount()) {
											MonthNumber = monthNumber(alertQueues
													.get(i)
													.getYearlyEveryMonthName());
											try {
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
												eventDateYearly
														.setDate(alertQueues
																.get(i)
																.getYearlyEveryMonthDay());
												eventDateYearly
														.setMonth(MonthNumber - 1);

												eventDateYearly
														.setYear(eventDateYearly
																.getYear());
												if (today
														.after(eventDateYearly)) {
													eventDateYearly
															.setYear(eventDateYearly
																	.getYear() + 1);
												}
												dateStringYearly = dateFormatYearly
														.format(eventDateYearly);
												nextDateYearly = new Date(
														dateStringYearly);
												alertQueue
														.setEventDueDate(nextDateYearly);
												alertQueue
														.setOccuranceCount(alertQueues
																.get(i)
																.getOccuranceCount() + 1);
												alertQueue
														.setPendingMailStatus(true);

												alertQueue = surveyService
														.saveAlertQueue(alertQueue);

											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											alertQueue
													.setPendingMailStatus(false);
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										}
									}
									if (alertQueues.get(i)
											.getYearlyRecurrenceTheEvery()
											.equalsIgnoreCase(FOR_EVERY)
											&& alertQueues.get(i)
													.getYearlyEveryMonthName() != null
											&& alertQueues.get(i).getEndDate() == null
											&& alertQueues.get(i)
													.getYearlyEveryMonthDay() > 0
											&& alertQueues.get(i)
													.getMaxOccurences() == 0) {

										eventDateYearly = alertQueues.get(i)
												.getEventDueDate();

										MonthNumber = monthNumber(alertQueues
												.get(i)
												.getYearlyEveryMonthName());
										try {
											String[] to = { alertQueues.get(i)
													.getEmailAddress() };
											sendSMTPMessage(
													MailConfigProperties
															.getMailProperty("HostName"),
													to,
													null,
													null,
													from,
													"",
													alertQueues
															.get(i)
															.getMessageSubject(),
													mesg, null);
											eventDateYearly.setDate(alertQueues
													.get(i)
													.getYearlyEveryMonthDay());
											eventDateYearly
													.setMonth(MonthNumber - 1);
											eventDateYearly
													.setYear(eventDateYearly
															.getYear());
											if (today.after(eventDateYearly)) {
												eventDateYearly
														.setYear(eventDateYearly
																.getYear() + 1);
											}
											dateStringYearly = dateFormatYearly
													.format(eventDateYearly);
											nextDateYearly = new Date(
													dateStringYearly);
											alertQueue
													.setEventDueDate(nextDateYearly);
											alertQueue
													.setOccuranceCount(alertQueues
															.get(i)
															.getOccuranceCount() + 1);
											alertQueue
													.setPendingMailStatus(true);

											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									// FOR THE TYPE ONLY END DATE AVAILABLE
									if (alertQueues.get(i)
											.getYearlyRecurrenceTheEvery()
											.equalsIgnoreCase(FOR_THE)
											&& alertQueues.get(i)
													.getYearlyTheDayTerm() != null
											&& alertQueues.get(i).getEndDate() != null
											&& alertQueues
													.get(i)
													.getYearlyTheDayDescription() != null
											&& alertQueues
													.get(i)
													.getYearlyTheMonthDescription() != null
											&& alertQueues.get(i)
													.getMaxOccurences() == 0) {
										eventDateYearly = alertQueues.get(i)
												.getEventDueDate();
										endDateYearly = alertQueues.get(i)
												.getEndDate();
										if (endDateYearly
												.after(eventDateYearly)) {
											MonthNumber = monthNumber(alertQueues
													.get(i)
													.getYearlyTheMonthDescription());
											MonthNow = today.getMonth() + 1;
											if (MonthNumber > MonthNow)
												MonthNumber = MonthNumber
														- MonthNow;
											else if (MonthNow > MonthNumber)
												MonthNumber = 12 - MonthNow
														+ MonthNumber;

											try {
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
												nextYearly = duedate(
														calYearly,
														eventDateYearly,
														Integer.parseInt(alertQueues
																.get(i)
																.getYearlyTheDayTerm()),
														MonthNumber,
														dayNameFinder(alertQueues
																.get(i)
																.getYearlyTheDayDescription()));
												dateStringYearly = dateFormatYearly
														.format(nextYearly);
												nextDateYearly = new Date(
														dateStringYearly);
												alertQueue
														.setEventDueDate(nextDateYearly);
												alertQueue
														.setPendingMailStatus(true);
												alertQueue = surveyService
														.saveAlertQueue(alertQueue);

											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											alertQueue
													.setPendingMailStatus(false);
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										}

									}
									// FOR THE TYPE ONLY MAXOCCOURANCE AVAILABLE
									if (alertQueues.get(i)
											.getYearlyRecurrenceTheEvery()
											.equalsIgnoreCase(FOR_THE)
											&& alertQueues.get(i)
													.getYearlyTheDayTerm() != null
											&& alertQueues.get(i).getEndDate() == null
											&& alertQueues
													.get(i)
													.getYearlyTheDayDescription() != null
											&& alertQueues
													.get(i)
													.getYearlyTheMonthDescription() != null
											&& alertQueues.get(i)
													.getMaxOccurences() > 0) {
										eventDateYearly = alertQueues.get(i)
												.getEventDueDate();
										if (alertQueues.get(i)
												.getMaxOccurences() > alertQueues
												.get(i).getOccuranceCount()) {
											MonthNumber = monthNumber(alertQueues
													.get(i)
													.getYearlyTheMonthDescription());
											MonthNow = today.getMonth() + 1;
											if (MonthNumber > MonthNow)
												MonthNumber = MonthNumber
														- MonthNow;
											else if (MonthNow > MonthNumber)
												MonthNumber = 12 - MonthNow
														+ MonthNumber;
											try {
												String[] to = { alertQueues
														.get(i)
														.getEmailAddress() };
												sendSMTPMessage(
														MailConfigProperties
																.getMailProperty("HostName"),
														to,
														null,
														null,
														from,
														"",
														alertQueues
																.get(i)
																.getMessageSubject(),
														mesg, null);
												nextYearly = duedate(
														calYearly,
														eventDateYearly,
														Integer.parseInt(alertQueues
																.get(i)
																.getYearlyTheDayTerm()),
														MonthNumber,
														dayNameFinder(alertQueues
																.get(i)
																.getYearlyTheDayDescription()));

												dateStringYearly = dateFormatYearly
														.format(nextYearly);
												nextDateYearly = new Date(
														dateStringYearly);
												alertQueue
														.setEventDueDate(nextDateYearly);
												alertQueue
														.setOccuranceCount(alertQueue
																.getOccuranceCount() + 1);
												alertQueue
														.setPendingMailStatus(true);
												alertQueue = surveyService
														.saveAlertQueue(alertQueue);

											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											alertQueue
													.setPendingMailStatus(false);
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);
										}

									}

									// FOR THE , NO END DATE
									if (alertQueues.get(i)
											.getYearlyRecurrenceTheEvery()
											.equalsIgnoreCase(FOR_THE)
											&& alertQueues.get(i)
													.getYearlyTheDayTerm() != null
											&& alertQueues.get(i).getEndDate() == null
											&& alertQueues
													.get(i)
													.getYearlyTheDayDescription() != null
											&& alertQueues
													.get(i)
													.getYearlyTheMonthDescription() != null
											&& alertQueues.get(i)
													.getMaxOccurences() == 0) {
										eventDateYearly = alertQueues.get(i)
												.getEventDueDate();
										MonthNumber = monthNumber(alertQueues
												.get(i)
												.getYearlyTheMonthDescription());
										MonthNow = today.getMonth() + 1;
										if (MonthNumber > MonthNow)
											MonthNumber = MonthNumber
													- MonthNow;
										else if (MonthNow > MonthNumber)
											MonthNumber = 12 - MonthNow
													+ MonthNumber;

										try {
											String[] to = { alertQueues.get(i)
													.getEmailAddress() };
											sendSMTPMessage(
													MailConfigProperties
															.getMailProperty("HostName"),
													to,
													null,
													null,
													from,
													"",
													alertQueues
															.get(i)
															.getMessageSubject(),
													mesg, null);
											nextYearly = duedate(
													calYearly,
													eventDateYearly,
													Integer.parseInt(alertQueues
															.get(i)
															.getYearlyTheDayTerm()),
													MonthNumber,
													dayNameFinder(alertQueues
															.get(i)
															.getYearlyTheDayDescription()));

											dateStringYearly = dateFormatYearly
													.format(nextYearly);
											nextDateYearly = new Date(
													dateStringYearly);
											alertQueue
													.setEventDueDate(nextDateYearly);
											alertQueue
													.setOccuranceCount(alertQueue
															.getOccuranceCount() + 1);
											alertQueue
													.setPendingMailStatus(true);
											alertQueue = surveyService
													.saveAlertQueue(alertQueue);

										} catch (Exception e) {
											e.printStackTrace();
										}
									}

								}

							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		valueSet = true;
		notify();
	}

	public boolean sendSMTPMessage(String smtpHost, String[] toAddr,
			String[] ccAddr, String[] bccAddr, String fromAddr,
			String fromAddrPersonalName, String subject, String body,
			HashMap<Object, Object> headers) {

		if (fromAddr == "from"
				|| fromAddr.trim().toLowerCase().equals("default@default.com")) {
			fromAddr = MailConfigProperties
					.getMailProperty("lmsalertsEmailAddress");
		}

		// If email addressess suplied with
		// semicolon then,
		for (int i = 0; i < toAddr.length; ++i) {
			if (toAddr[i].contains(";")) {
				if (bccAddr == null) {
					bccAddr = new String[] { toAddr[i].split(";")[1] };
				} else {
					bccAddr[ccAddr.length] = toAddr[i].split(";")[1];
				}
				toAddr[i] = toAddr[i].split(";")[0];
			}
		}

		String smtpPort = MailConfigProperties.getMailProperty("Port");
		if (smtpPort == null || StringUtils.isBlank(smtpPort))
			smtpPort = "25";

		System.out.println("Sending LMS Alert from " + fromAddr + " to "
				+ StringUtils.join(toAddr, ','));
		SendMailService.sendSMTPMessage(smtpHost, smtpPort, toAddr, null,
				null, fromAddr, null, subject, body, null);

		System.out.println("Sent LMS Alert from " + fromAddr + " to "
				+ StringUtils.join(toAddr, ','));
		/*
		 * Properties tempProps = new Properties();
		 *//*
			 * tempProps.put(MailConfigProperties.getMailProperty("smtpHost"),
			 * smtpHost); Session session =
			 * Session.getDefaultInstance(tempProps, null);
			 *//*
				 * 
				 * //---------------------------
				 * tempProps.put(MailConfigProperties
				 * .getMailProperty("smtpHost"), smtpHost);
				 * tempProps.put(MailConfigProperties
				 * .getMailProperty("smtpPort"),
				 * MailConfigProperties.getMailProperty("Port"));
				 * tempProps.put(MailConfigProperties
				 * .getMailProperty("socketFactory"),
				 * MailConfigProperties.getMailProperty("Port"));
				 * tempProps.put("mail.smtp.socketFactory.class"
				 * ,"javax.net.ssl.SSLSocketFactory");
				 * 
				 * tempProps.put(MailConfigProperties.getMailProperty("auth"),
				 * "true");
				 * 
				 * 
				 * //Session session = Session.getDefaultInstance(tempProps,
				 * null);
				 * 
				 * Session session = Session.getDefaultInstance(tempProps, new
				 * javax.mail.Authenticator() { protected PasswordAuthentication
				 * getPasswordAuthentication() { return new
				 * PasswordAuthentication
				 * ("360trainingalert@gmail.com","360@alert"); } });
				 * 
				 * //---------------------------
				 * 
				 * try {
				 * 
				 * // Create a message MimeMessage msg = new
				 * MimeMessage(session);
				 * 
				 * // Set the from address InternetAddress address = new
				 * InternetAddress(fromAddr); if ( fromAddrPersonalName != null
				 * ) { try { address.setPersonal(fromAddrPersonalName); }
				 * catch(java.io.UnsupportedEncodingException uee) {
				 * uee.printStackTrace(); } } msg.setFrom(address);
				 * 
				 * // Set the to addresses InternetAddress[] toAddresses = new
				 * InternetAddress[toAddr.length]; for ( int i = 0; i <
				 * toAddr.length; ++i) { if(toAddr[i].contains(";")) {
				 * if(bccAddr == null) { bccAddr = new String[]
				 * {toAddr[i].split(";")[1]}; } else { bccAddr[ccAddr.length] =
				 * toAddr[i].split(";")[1]; } toAddr[i] =
				 * toAddr[i].split(";")[0]; } toAddresses[i] = new
				 * InternetAddress(toAddr[i]); }
				 * msg.setRecipients(Message.RecipientType.TO, toAddresses);
				 * 
				 * // Set the CC addresses if ( ccAddr != null && ccAddr.length
				 * > 0 ) { InternetAddress[] ccAddresses = new
				 * InternetAddress[ccAddr.length]; for ( int i = 0; i <
				 * ccAddr.length; ++i) { ccAddresses[i] = new
				 * InternetAddress(ccAddr[i]); }
				 * msg.setRecipients(Message.RecipientType.CC, ccAddresses); }
				 * 
				 * // Set the BCC addresses if ( bccAddr != null &&
				 * bccAddr.length > 0 ) { InternetAddress[] bccAddresses = new
				 * InternetAddress[bccAddr.length]; for ( int i = 0; i <
				 * bccAddr.length; ++i) { bccAddresses[i] = new
				 * InternetAddress(bccAddr[i]); }
				 * msg.setRecipients(Message.RecipientType.BCC, bccAddresses); }
				 * 
				 * msg.setSubject(subject == null || subject == "" ? "Default" :
				 * subject); msg.setSentDate(new Date());
				 * 
				 * // Create the Multipart Multipart mp = new MimeMultipart();
				 * 
				 * // Create the body part MimeBodyPart bodyPart = new
				 * MimeBodyPart(); bodyPart.setContent(body == null || body ==
				 * "" ? "Default" : body, "text/html");
				 * mp.addBodyPart(bodyPart);
				 * 
				 * // Set the body part headers... if any were specified if
				 * (headers!=null && !headers.isEmpty()) { for (Iterator<Object>
				 * iter=headers.keySet().iterator(); iter.hasNext(); ) { String
				 * key = (String) iter.next(); String value = (String)
				 * headers.get(key); bodyPart.setHeader(key, value); } }
				 * 
				 * // Add the Multipart to the message msg.setContent(mp);
				 * 
				 * // Send the message
				 * 
				 * Transport.send(msg); } catch (MessagingException mex) {
				 * //log.
				 * error("Exception occurred during send of email message",
				 * mex); mex.printStackTrace(); Exception ex = null; if ((ex =
				 * mex.getNextException()) != null) {
				 * //log.error("NestedException:", ex); ex.printStackTrace(); }
				 * return false; }
				 */
		return true;

	}

	public boolean isExpirationDateToday(Date dueDate) { // returns true if
															// duedate is today

		if (dueDate == null)
			return false;

		Date todaysDate = new Date();

		// double diff = Math.abs(dueDate.getTime() -
		// System.currentTimeMillis());
		// double days = diff / MILLISECONDS_IN_DAY;
		// System.out.println("********************************************"+dueDate+"*****"+diff+"****"+days);

		// truncate time from both
		// today and due dates
		todaysDate = DateUtils.truncate(todaysDate, Calendar.DATE);
		dueDate = DateUtils.truncate(dueDate, Calendar.DATE);

		if (dueDate.equals(todaysDate)) {
			// if(todaysDate.getDate()==dueDate.getDate() &&
			// todaysDate.getMonth()==dueDate.getMonth() &&
			// todaysDate.getYear()==dueDate.getYear()){
			return true;
		} else {
			return false;
		}

	}

	public boolean isDueDateToday(Date dueDate) { // returns true if duedate is
													// today

		if (dueDate == null)
			return false;

		Date todaysDate = new Date();

		// double diff = Math.abs(dueDate.getTime() -
		// System.currentTimeMillis());
		// double days = diff / MILLISECONDS_IN_DAY;
		// System.out.println("********************************************"+dueDate+"*****"+diff+"****"+days);

		// truncate time from both
		// today and due dates
		todaysDate = DateUtils.truncate(todaysDate, Calendar.DATE);
		dueDate = DateUtils.truncate(dueDate, Calendar.DATE);

		if (dueDate.equals(todaysDate) || dueDate.before(todaysDate)) {
			// if(todaysDate.getDate()==dueDate.getDate() &&
			// todaysDate.getMonth()==dueDate.getMonth() &&
			// todaysDate.getYear()==dueDate.getYear()){
			return true;
		} else {
			return false;
		}

	}

	public int dayNumberFinder(String weeklyEveryWeekOn) {
		int dayNum = 1;

		if (weeklyEveryWeekOn.equals("monday"))
			dayNum = 1;
		if (weeklyEveryWeekOn.equals("tuesday"))
			dayNum = 2;
		if (weeklyEveryWeekOn.equals("wednesday"))
			dayNum = 3;
		if (weeklyEveryWeekOn.equals("thursday"))
			dayNum = 4;
		if (weeklyEveryWeekOn.equals("friday"))
			dayNum = 5;
		if (weeklyEveryWeekOn.equals("saturday"))
			dayNum = 6;
		if (weeklyEveryWeekOn.equals("sunday"))
			dayNum = 7;

		return dayNum;
	}

	public String dayNameFinder(String dayNum) {
		String dayName = "";

		if (dayNum.equals("1"))
			dayName = "Monday";
		else if (dayNum.equals("2"))
			dayName = "Tuesday";
		else if (dayNum.equals("3"))
			dayName = "Wednesday";
		else if (dayNum.equals("4"))
			dayName = "Thursday";
		else if (dayNum.equals("5"))
			dayName = "Friday";
		else if (dayNum.equals("6"))
			dayName = "Saturday";
		else if (dayNum.equals("7"))
			dayName = "Sunday";
		else if (dayNum.equals("0"))
			dayName = "Sunday";
		else {
			dayName = "Monday";
		}
		return dayName;
	}

	public int monthNumber(String monthName) {
		int monthNum = 0;

		if (monthName.equals("January"))
			monthNum = 1;
		else if (monthName.equals("February"))
			monthNum = 2;
		else if (monthName.equals("March"))
			monthNum = 3;
		else if (monthName.equals("April"))
			monthNum = 4;
		else if (monthName.equals("May"))
			monthNum = 5;
		else if (monthName.equals("June"))
			monthNum = 6;
		else if (monthName.equals("July"))
			monthNum = 7;
		else if (monthName.equals("August"))
			monthNum = 8;
		else if (monthName.equals("September"))
			monthNum = 9;
		else if (monthName.equals("October"))
			monthNum = 10;
		else if (monthName.equals("November"))
			monthNum = 11;
		else if (monthName.equals("December"))
			monthNum = 12;
		return monthNum;
	}

	public Date calculateDueDateForDateDrivenTrigger(
			int triggerRecurranceEventDateDuration, Date eventDate) {

		Date d2 = new Date();
		try {
			if (eventDate == null) {
				return d2;
			} else if (triggerRecurranceEventDateDuration > 0) {
				d2.setTime(eventDate.getTime()
						+ triggerRecurranceEventDateDuration * 24 * 60 * 60
						* 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d2;
	}

	public static Date duedate(Calendar gcal, Date dateStart, int myweek,
			int mymonth, String myday) {
		int noneFlag = 0;
		if (myweek > 0) {
			int initialDay = 0;

			if (gcal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 1 && myweek == 1) {
				initialDay = 1;
			} else if (gcal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 2
					&& myweek == 2) {
				initialDay = 8;
			} else if (gcal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 3
					&& myweek == 3) {
				initialDay = 15;
			} else if (gcal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 4
					&& myweek == 4) {
				initialDay = 22;
			} else if (gcal.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 5
					&& myweek == 5) {
				initialDay = 29;
			} else {
				if (dateStart.getMonth() < 12) {
					if (myweek == 1) {
						dateStart.setDate(1);
						initialDay = 1;
					} else if (myweek > 1) {
						dateStart.setDate(7 * myweek - 6);
						initialDay = 7 * myweek - 6;
						// System.out.println("#################initialDay"+
						// initialDay);
					}

					dateStart.setMonth(dateStart.getMonth() + mymonth);
				} else if (dateStart.getMonth() == 12) {
					if (myweek == 1) {
						dateStart.setDate(1);
						initialDay = 1;
					} else if (myweek > 1) {
						dateStart.setDate(7 * myweek - 6);
						initialDay = 7 * myweek - 6;
					}
					// System.out.println("-------initialDay"+ initialDay);
					dateStart.setMonth(dateStart.getMonth() + mymonth);
					dateStart.setYear(dateStart.getYear() + 1);

				}
				noneFlag = 1;
			}

			int flag = 0;
			dateStart.setDate(initialDay);
			if (initialDay < 29) {
				if (noneFlag == 0)
					dateStart.setMonth(dateStart.getMonth() + mymonth);
				for (int i = 1; i <= 7; i++) {
					if (flag == 0 && initialDay < 29) {
						if (sayDayName(dateStart).equals(myday)) {

							flag = 1;
						} else {
							initialDay++;
							dateStart.setDate(initialDay);

						}
					}
				}
			}
			if (initialDay == 29) {
				// System.out.println("fff"+dateStart);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dateStart);
				int totalDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

				System.out.println("totalDay" + totalDay);
				for (int i = 29; i <= totalDay; i++) {
					// System.out.println("eeee"+dateStart);
					if (flag == 0) {
						if (sayDayName(dateStart).equals(myday)) {

							flag = 1;
						} else {
							initialDay++;
							dateStart.setDate(initialDay - 1);
							// System.out.println(dateStart);
						}
					}
				}
				if (flag == 0) {
					duedate(gcal, dateStart, myweek, mymonth, myday);
				}
			}
		}

		return dateStart;
	}

	public static String sayDayName(Date d) {
		DateFormat f = new SimpleDateFormat("EEEE");
		try {
			return f.format(d);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}

class ReadFromAlertQueueTable implements Runnable {
	ReadAndSendAlert readAndSendAlert;

	ReadFromAlertQueueTable(ReadAndSendAlert readAndSendAlert) {
		this.readAndSendAlert = readAndSendAlert;
		new Thread(this, "ReadFromAlertQueueTable").start();
	}

	public void run() {
		while (true) {
			readAndSendAlert.readAlertQueue();
		}
	}
}

class SendAlertMail implements Runnable {
	ReadAndSendAlert readAndSendAlert;

	SendAlertMail(ReadAndSendAlert readAndSendAlert) {
		this.readAndSendAlert = readAndSendAlert;
		new Thread(this, "SendAlertMail").start();
	}

	public void run() {
		int i = 0;
		// long lSleep = 1200000L; // 20 mins delay
		// long lSleep = 60000L; // 1 mins delay
		// long lSleep = 60L; // 1 mins delay
		long lSleep = Long.parseLong(MailConfigProperties
				.getMailProperty("sendAlertMail.threadsleep")); // 20 mins delay
		while (true) {

			readAndSendAlert.readAlertQueue();
			try {
				Thread.sleep(lSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readAndSendAlert.sandMailAndUpdateStatus();
		}
	}

}

public class SendAlertProcess {
	public static void main(String args[]) {
		ReadAndSendAlert readAndSendAlert = new ReadAndSendAlert();

		// new ReadFromAlertQueueTable(readAndSendAlert);
		new SendAlertMail(readAndSendAlert);
		System.out.println("Press Control-C to stop.");
	}
}
