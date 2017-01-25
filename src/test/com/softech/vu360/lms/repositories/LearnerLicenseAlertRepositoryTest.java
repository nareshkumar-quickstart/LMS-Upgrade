package com.softech.vu360.lms.repositories;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LearnerLicenseAlert;


public class LearnerLicenseAlertRepositoryTest extends SpringJUnitConfigAbstractTest{
	@Inject
	private LearnerLicenseAlertRepository learnerLicenseAlert;
	@Inject
	LicenseOfLearnerRepository licenseOfLearner;
	
	//@Test
	public void findByLearnerIdByAndInActive() {
		long l = 3;
		List<LearnerLicenseAlert> r = learnerLicenseAlert.findByLearnerIdByAndInActive(l);
		System.out.println(r.get(0).getAlertName());
		System.out.println(r.get(0).getLearner().getId());
	}
	
	//@Test
	public void findBytriggerSingleDateByActiveFalse() throws ParseException {
		/*
	    String s = df.format(new Date());
	    Date date2 =df.parse(s);
		List<LearnerLicenseAlert> r = learnerLicenseAlert.findBytriggerSingleDateAndIsDeleteFalse(date2);
		System.out.println(r.get(0).getAlertName()); Fri Jan 24 00:00:00 PKT 2014
		*/
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(new Date());
		Date date2 =df.parse(s);
		List<LearnerLicenseAlert> r = learnerLicenseAlert.findBytriggerSingleDateAndIsDeleteFalse(date2);
		System.out.println(r.get(0).getAlertName());
	}
	
	
	
	//@Test
	public void countByLearnerIdByLearnerlicenseId() throws ParseException {
		List<LearnerLicenseAlert> r = learnerLicenseAlert.countByLearnerIdByLearnerlicenseId(1040044L, 1802L );
		System.out.println(r.get(0).getAlertName());
	}
	
	//@Test
	public void findByIdIn() throws ParseException {
		long [] l = {2502L,	2503L};
		List<LearnerLicenseAlert> r = learnerLicenseAlert.findByIdIn( ArrayUtils.toObject(l) );
		System.out.println(r.get(0).getAlertName());
	}
	
	@Test
	public void findByAlertNameAndLearnerIdByAndInActive() throws ParseException {
		List<LearnerLicenseAlert> r = learnerLicenseAlert.findByAlertNameAndLearnerIdByAndInActive( 1040044L, "Lic" );
		System.out.println(r.get(0).getAlertName());
	}
	
	//@Test
	//@Transactional
	public void insert() {
		LearnerLicenseAlert r = new LearnerLicenseAlert();
		r.setAlertName("A one");
		r.setCreatedDate(new Date());
		r.setDaysAfterEvent(1);
		r.setDelete(true);
		r.setLearnerlicense(licenseOfLearner.findOne(1102L));
		//r.setLicenseoflearnerId(licenseoflearnerId);
		//r.setTriggerSingleDate(triggerSingleDate);
		learnerLicenseAlert.save(r);
	}
}
