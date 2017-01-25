package com.softech.vu360.lms.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.softech.vu360.lms.model.IndustryCredential;
import com.softech.vu360.lms.model.LearnerLicenseType;
import com.softech.vu360.lms.model.LicenseIndustry;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.repositories.IndustryCredentialRepository;
import com.softech.vu360.lms.repositories.LearnerLicenseTypeRepository;
import com.softech.vu360.lms.repositories.LicenseIndustryRepository;
import com.softech.vu360.lms.repositories.LicenseOfLearnerRepository;
import com.softech.vu360.lms.service.LearnerLicenseService;

/**
 * 
 * @author haider.ali
 *
 */
public class LearnerLicenseServiceImpl implements LearnerLicenseService{

	
	//private LernerLicenseDAO learnerLicenseDAO = null;
	@Autowired
	LicenseIndustryRepository licenseIndustryRepository;

	@Autowired
	LicenseOfLearnerRepository licenseOfLearnerRepository;
	
	@Autowired
	IndustryCredentialRepository industryCredentialRepository;
	
	@Autowired
	LearnerLicenseTypeRepository learnerLicenseTypeRepository;
	
	@Override
	public List<com.softech.vu360.lms.model.LicenseIndustry> getLicenseIndustries() {
		return licenseIndustryRepository.findAll();
	}
	@Override
	public int getLicenseAlert(long[] LearnerId) 
	{
		Long[] arrLearnerId = ArrayUtils.toObject(LearnerId);
		List<Long> lstLearnerId = Arrays.asList(arrLearnerId);
		List<Map<Object, Object>> coll = licenseOfLearnerRepository.findByLearnerLicenseIds(lstLearnerId);
		return coll.size();
	}
	
	public LicenseOfLearner getLicenseOfLearner(Long id) {
		return licenseOfLearnerRepository.findOne(id);
	}
	
	public LicenseIndustry getLicenseIndustry(Long id) {
		return licenseIndustryRepository.findOne(id);
	}
	
	@Override
	public List<Map<Object, Object>> getLicenseIndusrtyCredentials(Long industryid, String state){
		return industryCredentialRepository.findByLearnerLicenseIds(industryid,state);
	}
	
	@Override
	public IndustryCredential getLicenseIndustryCredential(Long licenseIndustryCredentialId)
	{
		return industryCredentialRepository.findOne(licenseIndustryCredentialId);
	}

	
	@Override
	public List<com.softech.vu360.lms.model.LearnerLicenseType> getLearnerRequestedLicenseTypes(Long learnerId) {
		return learnerLicenseTypeRepository.findByLearnerId(learnerId);
	}

	@Override
	public List<Map<Object, Object>> getLicenseName(Long customerId) {
		List<Map<Object, Object>> licenseOfLearnersList = new ArrayList<Map<Object,Object>>();;
		// Added By Marium Saud LMS-19846
		// Method refactored to avoid breakdown of learnerids into chunks and resolve "StackOverflow Error"

		licenseOfLearnersList = licenseOfLearnerRepository.findLicenseNameByLearnerIds(customerId);
		return licenseOfLearnersList;
	}
	
	@Override
	public LearnerLicenseType getLearnerRequestedLicenseTypesById(Long learnerRequestedLicenseTypeId) {
		return learnerLicenseTypeRepository.findOne(learnerRequestedLicenseTypeId);
	}

	@Override
	public LicenseOfLearner save(LicenseOfLearner licenseOfLearner) {
		return licenseOfLearnerRepository.save(licenseOfLearner);
	}

	@Override
	public List<LicenseOfLearner> getAllLicensesOfLearner(Long learnerId) {
		return licenseOfLearnerRepository.findByActiveTrueAndLearnerId(learnerId);
	}
	
	@Override
	public List<LicenseOfLearner> getFilteredLicensesOfLearner(Long learnerId, String licensename) {
		licensename = "%" + licensename +"%";
		if(!licensename.equals(""))
			return licenseOfLearnerRepository.findByActiveTrueAndLearnerIdAndIndustryCredential_Credential_OfficialLicenseNameLike(learnerId, licensename);
		else
			return licenseOfLearnerRepository.findByActiveTrueAndLearnerId(learnerId);
	}

	@Override
	public List<LicenseOfLearner> getLicenseOfLearner(Long[] LicenseOfLearnerId) {
		return licenseOfLearnerRepository.findByIdIn(LicenseOfLearnerId);
	}

	@Override
	public List<LicenseOfLearner> deleteLicenseOfLearner(List<LicenseOfLearner> learnerIds) {
		for (LicenseOfLearner learnerId : learnerIds){
			learnerId.setActive(Boolean.FALSE);
			learnerId.setUpdateOn(new Timestamp(System.currentTimeMillis()));
			licenseOfLearnerRepository.save(learnerId);
		}
		return learnerIds;
	}
	@Override
	public List<Map<Object, Object>> getLicenseName(Long customerId, String firstName, String lastName, String emailAddress) {
		List<Map<Object, Object>> licenseOfLearnersList = new ArrayList<Map<Object,Object>>();;
		
		// Added By Marium Saud LMS-19846
		// Method refactored to avoid breakdown of learnerids into chunks and resolve "StackOverflow Error"

		licenseOfLearnersList = licenseOfLearnerRepository.findLicenseNameByLearnerIds(customerId,firstName.toUpperCase(),lastName.toUpperCase(),emailAddress.toUpperCase());
		return licenseOfLearnersList;
	}
}
