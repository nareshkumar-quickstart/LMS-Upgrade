package com.softech.vu360.lms.service.impl.lmsapi;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.lmsapi.LmsApiLearnerService;
import com.softech.vu360.lms.util.LmsApiEmailAsyncTask;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

@Service
public class LmsApiLearnerServiceImpl implements LmsApiLearnerService {

	@Autowired
	private LearnersToBeMailedService learnersToBeMailedService;
	
	@Autowired
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
	
	public void setLearnersToBeMailedService(LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}
	
	public void setAsyncTaskExecutorWrapper(AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}

	@Override
	public void sendEmailToLearners(Map<Learner, List<LearnerEnrollment>> learnerEnrollmentEmailMap, VU360User manager) {
		
		Brander brander = getBrander(null, null);
		String loginURL = getLoginUrl();
		LmsApiEmailAsyncTask emailTask = new LmsApiEmailAsyncTask(brander, loginURL, manager, learnerEnrollmentEmailMap, 
				learnersToBeMailedService);
		asyncTaskExecutorWrapper.execute(emailTask);

	}
	
	private Brander getBrander(String brandName, com.softech.vu360.lms.vo.Language language ) {
		
		if (StringUtils.isEmpty(brandName) && StringUtils.isBlank(brandName)) {
			brandName = "default";
		}
		
		if (language == null) {
			language = new com.softech.vu360.lms.vo.Language();
			language.setLanguage(Language.DEFAULT_LANG);
		}
		Brander brander = VU360Branding.getInstance().getBrander(brandName, new com.softech.vu360.lms.vo.Language());
		return brander;
	}
	
	private String getLoginUrl() {
		String loginURL = VU360Properties.getVU360Property("lms.loginURL") + "login.do";
		return loginURL;
	}

}
