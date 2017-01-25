package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.RegulatoryAnalyst;
import com.softech.vu360.lms.model.VU360User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class InstructorRepositoryTest {

	@Inject
	private InstructorRepository repoInstructor;
	@Inject
	private RegulatoryAnalystRepository regulatoryAnalystRepository;
	
	@Inject
	private VU360UserRepository vU360UserRepository;
	
	//@Test
	public void instructor_should_save() {
		Instructor saveInstructor = repoInstructor.findOne(new Long(2105));
		saveInstructor.setId(null);
		
		Instructor savedInstructor = repoInstructor.save(saveInstructor);
		
		System.out.println("..........");
	}
	
	//@Test
	public void instructor_should_find() {
		Instructor saveInstructor = repoInstructor.findOne(new Long(2));
		
		System.out.println("..........");
	}
	
	//@Test
	public void instructor_should_deleteAll(List<Instructor> objInstructors) {
		repoInstructor.delete(objInstructors);
		
		System.out.println("..........");
	}

	//@Test
	public void instructor_find_by_firstName_lastName_email() {
		String firstName = "NA-FN";
		String lastName = "MN-LN";
		String emailAddress = "mnaeem@gmail.com";//mnaeem@gmail.com
		List<Instructor> listInstructor = repoInstructor.findInstructorByfirstNamelastNameemailAddress(firstName, lastName, emailAddress,"","");
		
		System.out.println("..........");
	}

	//@Test
	public void instructor_find_by_firstName_lastName_email_regulatorAnalyst() {
		VU360User user =   vU360UserRepository.findOne(55L); // Global
		com.softech.vu360.lms.vo.VU360User userVO = new com.softech.vu360.lms.vo.VU360User();
		userVO = ProxyVOHelper.setUserProxy(user);
		//RegulatoryAnalyst user = regulatoryAnalystRepository.findOne(55L); // Global
		String firstName = "NA-FN";
		String lastName = "MN-LN";
		String emailAddress = "mnaeem@gmail.com";//mnaeem@gmail.com
		List<Instructor> listInstructor = repoInstructor.findInstructor(null, null, null, userVO, "", "");
		
		System.out.println("..........");
	}

	//@Test 
	public void findByIdNotInAndFirstNameAndLastNameAndContentOwnerId(){
		
		Vector<Long> ids = new Vector();
		ids.add(new Long(652));
		ids.add(new Long(704));
		String firstName = "14jan";
		String lastName = "Customer";
		List<Instructor> list = repoInstructor.findByIdNotInAndFirstNameAndLastNameAndContentOwnerId(ids,firstName,lastName,new Long(3));
		Long expectedResult = list.get(0).getId();
		
		System.out.println("653 = "+expectedResult);
		
		
		
		
	}
	
	@Test 
	public void getInstructorsExceptInstructorIds(){
		
		Vector<Long> ids = new Vector();
		ids.add(new Long(2));
		ids.add(new Long(3));
		ids.add(new Long(4));
		ids.add(new Long(5));
		ids.add(new Long(52));
		ids.add(new Long(102));
		ids.add(new Long(104));
		ids.add(new Long(152));
		ids.add(new Long(153));
		ids.add(new Long(202));
		
		ids.add(new Long(203));
		ids.add(new Long(252));
		ids.add(new Long(302));
		ids.add(new Long(352));
		ids.add(new Long(402));
		ids.add(new Long(452));
		ids.add(new Long(502));
		ids.add(new Long(552));
		ids.add(new Long(602));
		ids.add(new Long(603));
		ids.add(new Long(604));
		long contentOwnerId =1;
		String firstName=null; //= "jan";
		String lastName=null; //= "er";
		Map<Object,Object> list; 
		//Long expectedResult = list.get(0).getId();
		List<Instructor> ls = repoInstructor.findInstructorByContentIdfirstNamelastName(ids, firstName, lastName, contentOwnerId, 2, 2, 1, "firstName");
		System.out.println("653 = "+ls.get(0).getFirstName());
		
		
		
		
	}
	
	
	@Test
	public void accreditation_search_Instructor(){
		List<Instructor> lstInstructor = repoInstructor.searchInstructorByFirstName("");
		System.out.println("Instructor UserName = "+lstInstructor.get(0).getUserName());
		
	}
}
