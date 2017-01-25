package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author muhammad.rehan
 */

@Transactional
public class DistributorEntitlementTest extends TestBaseDAO<DistributorEntitlement> {

	//@Test
	public void DistributorEntitlement_should_find(){
		DistributorEntitlement de =getById(new Long(23352), DistributorEntitlement.class);
		System.out.println(de.getName());
		System.out.println(de.getMaxNumberSeats());
	}
	
	
	//@Test
	public void DistributorEntitlement_should_save() throws Exception {
		
		Distributor d =(Distributor)crudFindById(Distributor.class, new Long(3));
		List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
		CourseGroup courseGrp=(CourseGroup)crudFindById(CourseGroup.class, new Long(73));
		courseGroups.add(courseGrp);
		
		DistributorEntitlement de = new DistributorEntitlement();
		de.setAllowSelfEnrollment(true);
		de.setAllowUnlimitedEnrollments(true);
		de.setCourseGroups(courseGroups);
		de.setDefaultTermOfServiceInDays(1);
		de.setDistributor(d);
		de.setEndDate(new Date());
		
		de.setMaxNumberSeats(2);
		de.setName("this is Distributor Ent");
		de.setNumberSeatsUsed(1);
		de.setStartDate(new Date());
		de.setTransactionAmount(50.0);
		de=(DistributorEntitlement)crudSave(DistributorEntitlement.class, de);
		
	}
	
	// this test update DistributorEntitlement and delete existing coourse group and add new assigned course group
	@Test
	public void DistributorEntitlement_should_update() throws Exception {
		List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
		CourseGroup courseGrp=(CourseGroup)crudFindById(CourseGroup.class, new Long(10858));
		courseGroups.add(courseGrp);
		
		DistributorEntitlement de = (DistributorEntitlement)crudFindById(DistributorEntitlement.class, new Long(1182550));
		de.setAllowSelfEnrollment(true);
		de.setAllowUnlimitedEnrollments(true);
		de.setCourseGroups(courseGroups);
		de.setDefaultTermOfServiceInDays(1);
		
		de.setEndDate(new Date());
		
		de.setMaxNumberSeats(2);
		de.setName("this is Distributor Ent. . .. . . ");
		de.setNumberSeatsUsed(1);
		de.setStartDate(new Date());
		de.setTransactionAmount(50.0);
		de=(DistributorEntitlement)crudSave(DistributorEntitlement.class, de);
		
	}
}
