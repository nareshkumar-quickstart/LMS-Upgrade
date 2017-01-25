package com.softech.vu360.lms.repositories;


import java.util.Collection;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.TimeZone;


public class TimeZoneRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private TimeZoneRepository timeZoneRepository;
	
//	@Test
	public void TimeZone_should_getAllTimeZone() {
		Iterable<TimeZone> timeZoneList = timeZoneRepository.findAll();
		System.out.println("TimeZone List contains = "+((Collection<TimeZone>)timeZoneList).size());
	}
	
	//@Test
	public void TimeZone_should_findById(){
		TimeZone timeZone=timeZoneRepository.findOne(17);
		System.out.println("Zone = "+timeZone.getZone());
	}
	
	@Test
	public void TimeZone_should_findByZone(){
		TimeZone timeZone=timeZoneRepository.findByZone("West Asia (Islamabad)");
		System.out.println("Zone = "+timeZone.getZone());
	}
	
}
