package com.softech.vu360.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.softech.vu360.lms.model.AuditCourseStatus;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.RegulatorCategory;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.AuditCourseStatusService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.StatisticsService;

public class UserStatusUpdateUtil {

	private AccreditationService accreditationService;
	private EntitlementService entitlementService=null;
	private StatisticsService statisticsService = null;
    private LearnersToBeMailedService learnersToBeMailedService;
    private AuditCourseStatusService auditCourseStatusService;

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}
	
	public boolean updateStatuses(List<Long> userstatuses, String update_courseStatus, Brander brander, Long loggedInUserID, boolean reversalPermission)	{
		boolean returnStatusFlag = true;
		String affidavitType = "";
		
		// list of learnerEnrollments for emailing later.
		List<LearnerEnrollment> learnerEnrollments = new ArrayList<LearnerEnrollment>();

		// foreach learner-enrollment selected on the form.
		for (Long userstatus : userstatuses) {
			// Learner-Enrollment-Id is provided from the form.
			LearnerEnrollment learnerEnrollment = entitlementService.getLearnerEnrollmentById(userstatus);
			// get learnerCourseStatistics from the learnerEnrollment Id.
			LearnerCourseStatistics learnerCourseStatistics = statisticsService.getLearnerCourseStatisticsByLearnerEnrollmentId(userstatus);
			// get all the learningSessions for the learnerEnrollment.
			List<LearningSession> learningSessions = accreditationService.getLearningSessionByEnrollmentId(learnerEnrollment.getId());
			
			// get all the courseApprovals for each learningSession.
			List<CourseApproval> courseApprovals = new ArrayList<CourseApproval>();			
			for (LearningSession learningSession : learningSessions) {
				courseApprovals.add(accreditationService.getCourseApprovalById(learningSession.getCourseApprovalId()));
			}

			// foreach courseApproval in courseApprovals
			for (CourseApproval courseApproval : courseApprovals) {
				if(courseApproval != null)	{
					// get RegulatorCategories for courseApproval.
					List<RegulatorCategory> regulatorCategories = courseApproval.getRegulatorCategories();
					// for each regulatorCategory in regulatorCategories				
					for (RegulatorCategory regulatorCategory : regulatorCategories) {
						// check if statusChange is allowed.
						
						if(courseApproval.getAffidavit() != null && courseApproval.getAffidavit().getAffidavitType()!=null){
							if(courseApproval.getAffidavit().getAffidavitType().equalsIgnoreCase("file"))
								affidavitType = "File";
							else
								affidavitType = "TemplateID";
						}
						
						boolean statuschangeAllowed = statusChangeAllowed(update_courseStatus, learnerCourseStatistics.getStatus(),
								(courseApproval.getAffidavit() == null ? false : true),
								regulatorCategory.getReportingRequired(),
								affidavitType, 
								reversalPermission);
						
						// if allowed 
						if (statuschangeAllowed)	{
							String old_courseStatus = learnerCourseStatistics.getStatus();
							learnerCourseStatistics.setStatus(update_courseStatus);
							// update status
							LearnerCourseStatistics newStats = statisticsService.updateLearnerCourseStatistics(learnerCourseStatistics.getId().longValue(), learnerCourseStatistics);
							
							//	If Successful updated
							if(newStats.getStatus().equals(update_courseStatus))	{
								
								// Audit Log
								AuditCourseStatus auditCourseStatus = new AuditCourseStatus();
								auditCourseStatus.setCreatedDate(new Date());
								auditCourseStatus.setCurrentStatus(newStats.getStatus());
								auditCourseStatus.setLearnerEnrollmentId(learnerEnrollment.getId());
								auditCourseStatus.setPreviousStatus(old_courseStatus);
								auditCourseStatus.setUserId(loggedInUserID);
								auditCourseStatusService.logCourseStatusChangeForAudit(auditCourseStatus);
								
								// Notification
								// send notification only is the Course is marked as "Reported"
								if(newStats.getStatus().equals(LearnerCourseStatistics.REPORTED))
									learnerEnrollments.add(learnerEnrollment);
							}
						}
						// else set flag to false
						else	{
							returnStatusFlag = false;
						}
					}
				}
			}
		}

		learnersToBeMailedService.sendAffidavitReportedNotificationEmail(learnerEnrollments, brander);

		// return flag.
		return returnStatusFlag;
	}
	
	public boolean statusesChangeAllowed(List<Long> userstatuses, String update_courseStatus, boolean reversalPermission)	{		

		boolean returnStatusFlag = true;
		String affidavitType = "";
		
		// foreach learner-enrollment selected on the form.
		for (Long userstatus : userstatuses) {
			// Learner-Enrollment-Id is provided from the form.
			LearnerEnrollment learnerEnrollment = entitlementService.getLearnerEnrollmentById(userstatus);
			// get learnerCourseStatistics from the learnerEnrollment Id.
			LearnerCourseStatistics learnerCourseStatistics = statisticsService.getLearnerCourseStatisticsByLearnerEnrollmentId(userstatus);
			// get all the learningSessions for the learnerEnrollment.
			List<LearningSession> learningSessions = accreditationService.getLearningSessionByEnrollmentId(learnerEnrollment.getId());
			
			// get all the courseApprovals for each learningSession.
			List<CourseApproval> courseApprovals = new ArrayList<CourseApproval>();			
			for (LearningSession learningSession : learningSessions) {
				courseApprovals.add(accreditationService.getCourseApprovalById(learningSession.getCourseApprovalId()));
			}
			
			// foreach courseApproval in courseApprovals
			for (CourseApproval courseApproval : courseApprovals) {
				if(courseApproval != null)	{
					// get RegulatorCategories for courseApproval.
					List<RegulatorCategory> regulatorCategories = courseApproval.getRegulatorCategories();
					// for each regulatorCategory in regulatorCategories
					for (RegulatorCategory regulatorCategory : regulatorCategories) {
						// check if statusChange is allowed.
						
						if(courseApproval.getAffidavit() != null && courseApproval.getAffidavit().getAffidavitType()!=null){
							if(courseApproval.getAffidavit().getAffidavitType().equalsIgnoreCase("file"))
								affidavitType = "File";
							else
								affidavitType = "TemplateID";
						}
						
						boolean statuschangeAllowed = statusChangeAllowed(update_courseStatus, learnerCourseStatistics.getStatus(),
								(courseApproval.getAffidavit() == null ? false : true),
								regulatorCategory.getReportingRequired(),
								affidavitType,
								reversalPermission);
						
						// if either of the selected are not allowed, set the flag to false.
						if (!statuschangeAllowed)
							returnStatusFlag = false;
					}
				}
			}
		}
		// return flag.
		return returnStatusFlag;		
	}
	
	private boolean statusChangeAllowed(String update_courseStatus, String current_coourseStatus, boolean hasAffidavit, boolean isReportingRequired, String affidavitType, boolean reversalPermission) {
		
		boolean returnStatus = false;
		
		//	Reporting without affidavit
		if(hasAffidavit == false && isReportingRequired)	{
			if(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.REPORTED))
				returnStatus = true;
			
			//	reversal allowed
			if(reversalPermission)	{
				if(current_coourseStatus.equals(LearnerCourseStatistics.REPORTED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED))
					returnStatus = true;				
			}
		}
		
		//Affidavit with no reporting
		if(hasAffidavit == true && !isReportingRequired)	{
			if(affidavitType.equalsIgnoreCase("TemplateID"))	{
				if((current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						// LMS-15096
						//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.REPORTED)) ||
						// LCMS-11974
						(current_coourseStatus.equals(LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED))||
						(current_coourseStatus.equals(LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) )	
					{
						returnStatus = true;
					}
				
				//	reversal allowed
				if(reversalPermission)	{
					if(//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
							//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)))	{
						returnStatus = true;
					}					
				}
			}

			if(affidavitType.equalsIgnoreCase("File"))	{
				if((current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						// LMS-15096
						//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.REPORTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)))	{
					returnStatus = true;
				}
				
				//	reversal allowed
				if(reversalPermission)	{
					if((current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING)) ||
							//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
							//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)))	{
						returnStatus = true;
					}					
				}
			}
		}

		//	Affidavit with reporting
		if(hasAffidavit == true && isReportingRequired)	{
			if(affidavitType.equalsIgnoreCase("TemplateID"))	{
				if((current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
						// LMS-15096
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.REPORTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.REPORTED))||
						// LCMS-11974
						(current_coourseStatus.equals(LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED))||
						(current_coourseStatus.equals(LearnerCourseStatistics.USER_DECLINED_AFFIDAVIT) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING)) )	{
					returnStatus = true;
				}
				
				//	reversal allowed
				if(reversalPermission)	{
					if(//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
							//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.REPORTED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)))	{
						returnStatus = true;
					}
					
				}
			}

			if(affidavitType.equalsIgnoreCase("File"))	{
				if((current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)) ||
						// LMS-15096
						(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.REPORTED)) ||
						(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.REPORTED)))	{
					returnStatus = true;
				}
				
				//	reversal allowed
				if(reversalPermission)	{
					if((current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_PENDING)) ||
							//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
							//(current_coourseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_DISPUTED)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.COMPLETED) && update_courseStatus.equals(LearnerCourseStatistics.AFFIDAVIT_RECEIVED)) ||
							(current_coourseStatus.equals(LearnerCourseStatistics.REPORTED) && update_courseStatus.equals(LearnerCourseStatistics.COMPLETED)))	{
						returnStatus = true;
					}					
				}
			}
		}
		
		return returnStatus;
	}

	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}

	public AuditCourseStatusService getAuditCourseStatusService() {
		return auditCourseStatusService;
	}

	public void setAuditCourseStatusService(AuditCourseStatusService auditCourseStatusService) {
		this.auditCourseStatusService = auditCourseStatusService;
	}

}
