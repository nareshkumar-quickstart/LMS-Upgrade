package com.softech.vu360.lms.util;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LearnersToBeMailedService;

public class LmsApiEmailAsyncTask implements Runnable {
	
	private static final Logger log = Logger.getLogger(LmsApiEmailAsyncTask.class.getName());
	
	private volatile boolean running = true;
	private LearnersToBeMailedService learnersToBeMailedService;
	private Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap;
	private Brander brander;
	private String loginURL;
	private VU360User user;
	
	public LmsApiEmailAsyncTask(Brander brander, String loginURL, VU360User user, Map<Learner, 
			List<LearnerEnrollment>> learnerEnrollmentEmailMap, LearnersToBeMailedService learnersToBeMailedService) {
		this.brander = brander;
		this.loginURL = loginURL;
		this.user = user;
		this.learnerEnrollmentEmailMap = learnerEnrollmentEmailMap;
		this.learnersToBeMailedService = learnersToBeMailedService;
	}
	
	public void terminate() {
        running = false;
    }
	
	@Override
	public void run() {
		//while (running) {
			try {
				for (Map.Entry<Learner, List<LearnerEnrollment>> entry : learnerEnrollmentEmailMap.entrySet()) {
					Learner learner = entry.getKey();
					List<LearnerEnrollment> learnerEnrollmentEmailList = entry.getValue();
					sendEmailToLearner(learnerEnrollmentEmailList);
				}	
				running = false;
            } catch (Exception e) {
            	running = false;
            	String errorMessage = e.getMessage();
            	log.debug(errorMessage);
            	log.error("Exception", e);
            }
		//} //end of while()
	} // end of run()
	
	private boolean sendEmailToLearner(List<LearnerEnrollment> learnerEnrollmentEmailList) throws Exception {
		return sendEmailToLearner(learnerEnrollmentEmailList, brander, loginURL, user);
	}
	
	private boolean sendEmailToLearner(List<LearnerEnrollment> learnerEnrollmentEmailList, Brander brander, String loginURL, 
			VU360User user) throws Exception {
		return sendEmailToLearner(learnersToBeMailedService, learnerEnrollmentEmailList, brander, loginURL, user);
	}
	
	public boolean sendEmailToLearner(LearnersToBeMailedService learnersToBeMailedService, 
			List<LearnerEnrollment> learnerEnrollmentEmailList, Brander brander, String loginURL, VU360User user) throws Exception {
		
		if (learnersToBeMailedService == null) {
			throw new Exception("learnersToBeMailedService is null");
		}
		
		if (brander == null) {
			throw new Exception("brander is null");
		}
		
		if (user == null) {
			throw new Exception("VU360User is null");
		}
		
		boolean result = learnersToBeMailedService.SendMailToLearners(learnerEnrollmentEmailList, loginURL, user, brander);
		return result;
		
	}
	
} //end of class LmsApiEmailAsyncTask
