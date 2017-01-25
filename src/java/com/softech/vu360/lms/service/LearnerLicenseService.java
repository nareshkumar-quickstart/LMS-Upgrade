package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.IndustryCredential;
import com.softech.vu360.lms.model.LearnerLicenseType;
import com.softech.vu360.lms.model.LicenseIndustry;
import com.softech.vu360.lms.model.LicenseOfLearner;

public interface LearnerLicenseService {
	
	public List<LicenseIndustry> getLicenseIndustries();
	public LicenseIndustry getLicenseIndustry(Long Id);
	
	public List<LearnerLicenseType> getLearnerRequestedLicenseTypes(Long learnerId) ;
	
	public LearnerLicenseType getLearnerRequestedLicenseTypesById(Long learnerRequestedLicenseTypeId);
	
	public LicenseOfLearner getLicenseOfLearner(Long Id);

	public LicenseOfLearner save(LicenseOfLearner licenseOfLearner) ;
	
	public List<com.softech.vu360.lms.model.LicenseOfLearner> getAllLicensesOfLearner(Long learnerId) ;
	
	public List<LicenseOfLearner> getFilteredLicensesOfLearner(Long learnerId,String alertname);
	
	public List<LicenseOfLearner> getLicenseOfLearner(Long[] LicenseOfLearnerId) ;
	
	
	public List<LicenseOfLearner> deleteLicenseOfLearner(List<LicenseOfLearner> learnerIds) ;
	
	
	public List<Map<Object, Object>> getLicenseIndusrtyCredentials(Long industryid, String state);

	public IndustryCredential getLicenseIndustryCredential(Long licenseIndustryCredentialId);
	
	public List<Map<Object, Object>> getLicenseName(Long customerId, String firstName, String lastName, String emailAddress) ;
	
	public List<Map<Object, Object>> getLicenseName(Long customerId) ;
	
	public int getLicenseAlert(long[] LearnerId) ;

	
}
