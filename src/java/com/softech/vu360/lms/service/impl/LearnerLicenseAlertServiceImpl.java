package com.softech.vu360.lms.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.model.LearnerLicenseAlert;
import com.softech.vu360.lms.model.LicenseOfLearner;
import com.softech.vu360.lms.repositories.LearnerLicenseAlertRepository;
import com.softech.vu360.lms.repositories.LicenseOfLearnerRepository;
import com.softech.vu360.lms.service.LearnerLicenseAlertService;


public class LearnerLicenseAlertServiceImpl implements LearnerLicenseAlertService {
	
	private static DateFormat dfYMD = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	LearnerLicenseAlertRepository learnerLicenseAlertRepository;
	
	@Autowired
	LicenseOfLearnerRepository licenseOfLearnerRepository;
	
	@Override
	public LearnerLicenseAlert addLearnerLicenseAlert(LearnerLicenseAlert learnerLicenseAlert)
	{
		return learnerLicenseAlertRepository.save(learnerLicenseAlert);
	}
    @Override
	public List<LearnerLicenseAlert> getLearnerLicenseAlert(long learnerId)
	{
    	return learnerLicenseAlertRepository.findByLearnerIdByAndInActive(learnerId);
	}
    
    @Override
	public List<LearnerLicenseAlert> getFilteredLearnerLicenseAlert(long learnerId, String alertname)
	{
    	if(!alertname.equals(""))
    		return learnerLicenseAlertRepository.findByAlertNameAndLearnerIdByAndInActive(learnerId, alertname);
    	else
    		return learnerLicenseAlertRepository.findByLearnerIdByAndInActive(learnerId);
	}
    
    @Override
	public List<LearnerLicenseAlert> getLearnerLicenseAlert()
	{
    	
		String strDte = dfYMD.format(new Date());
		Date date = null;
		try {
			date = dfYMD.parse(strDte);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return learnerLicenseAlertRepository.findBytriggerSingleDateAndIsDeleteFalse(date);
	}
    
    @Override
    public LearnerLicenseAlert saveLearnerLicenseAlert(LearnerLicenseAlert learnerlicensealert) {
		return learnerLicenseAlertRepository.save(learnerlicensealert);
	}
    
    @Override
    public List<LearnerLicenseAlert> getLearnerLicenseAlert(long[] learnerlicenseid)
    {
    	return learnerLicenseAlertRepository.findByIdIn(ArrayUtils.toObject(learnerlicenseid));
    }
    
    @Override
    public LearnerLicenseAlert loadForUpdateLearnerLicenseAlert(long id){
    	return learnerLicenseAlertRepository.findOne(id);
    }
    
    @Override
    public List<LearnerLicenseAlert> deleteLearnerLicenseAlert(List<LearnerLicenseAlert> learnerlicensealertIds) 
    {
    	for (LearnerLicenseAlert lla : learnerlicensealertIds) {
    		lla.setDelete(Boolean.TRUE);
    		learnerLicenseAlertRepository.save(lla);
    	}
    	return learnerlicensealertIds;
    }
    @Override
    public int getLearnerLicenseAlert(long learnerid,long learnerlicenseid)
    {
    	List<LearnerLicenseAlert> lst = learnerLicenseAlertRepository.countByLearnerIdByLearnerlicenseId(learnerid,learnerlicenseid);
    	return lst.size();
    }
    
    @Override
    public LearnerLicenseAlert getLearnerLicenseAlertById(long id){
    	return learnerLicenseAlertRepository.findOne(id);
    }
    
	public List<LicenseOfLearner> getLicenseOfLearner(long id)
	{
		return licenseOfLearnerRepository.findByLearnerId(id);
	}
}
