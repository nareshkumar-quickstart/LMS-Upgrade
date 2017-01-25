package com.softech.vu360.lms.service;

import java.util.List;

import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.model.LicenseOfLearner;


public interface LearnerLicenseAlertService {
	
	public LearnerLicenseAlert addLearnerLicenseAlert(LearnerLicenseAlert learnerLicenseAlert);
	public List<LearnerLicenseAlert> getLearnerLicenseAlert(long learnerid);
	public LearnerLicenseAlert getLearnerLicenseAlertById(long id);
	public List<LicenseOfLearner> getLicenseOfLearner(long id);
	public List<LearnerLicenseAlert> getLearnerLicenseAlert(long[] learnerlicenseid);
	public List<LearnerLicenseAlert> deleteLearnerLicenseAlert(List<LearnerLicenseAlert> learnerlicensealertIds) ;
	public LearnerLicenseAlert loadForUpdateLearnerLicenseAlert(long id);
	public LearnerLicenseAlert saveLearnerLicenseAlert(LearnerLicenseAlert learnerlicensealert);
	public int getLearnerLicenseAlert(long learnerid,long learnerlicenseid);
	public List<LearnerLicenseAlert> getLearnerLicenseAlert();
	public List<LearnerLicenseAlert> getFilteredLearnerLicenseAlert(long learnerId, String alertname);
}
