package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;

public class RegistrationInvitationRepositoryTest extends SpringJUnitConfigAbstractTest{

	@Inject
	private RegistrationInvitationRepository registrationInvitationRepository;
	@Inject
	private VU360UserRepository userRepository;


	
//	@Test
	public void RegistrationInvitation_should_getById() {
		RegistrationInvitation registrationInvitation = registrationInvitationRepository.findOne(702L);
		System.out.println("InvitationMessage  = "+registrationInvitation.getInvitationMessage()+" ,InvitationName = "+registrationInvitation.getInvitationName());
	}
	
	//@Test
	public void RegistrationInvitation_should_getByCustomerId_And_InvitationNameLikeIgnoreCase(){
		List<RegistrationInvitation> customerAndInvitationName=registrationInvitationRepository.findByCustomerIdAndInvitationNameContainingIgnoreCase(474L, "Shahan's Invitation");
		System.out.println("customerAndInvitationName  = "+customerAndInvitationName.get(0).getInvitationName());
		
	}
	
	//@Test
	public void RegistrationInvitation_should_findByOrgGroupsInAndCustomerIdAndInvitationNameContainingIgnoreCase(){
		Object[] obj=new Object[] {1671L , 5306L , 9554L , 5306L , 27404L , 27404L , 27854L , 27854L , 28809L , 28810L , 28811L , 28809L , 28810L , 28811L , 9554L , 3059L , 4266L , 4266L , 5458L , 5458L , 7717L , 12554L , 7717L , 12554L , 19860L , 19860L};
		List<RegistrationInvitation> orgList=registrationInvitationRepository.findDistinctByOrgGroupsIdInAndCustomerIdAndInvitationNameContainingIgnoreCase(obj, 474L, "Shahan's Invitation");
		System.out.println("orgList  = "+orgList.size());
	}

	@Test
	public void RegistrationInvitation_should_AddRegistration_StoreProcedure(){
		RegistrationInvitation registrationInv=registrationInvitationRepository.findOne(105L);
		Object[] new_registrationInv=registrationInvitationRepository.addRegistrationInvitation(registrationInv.getId(), 1);
		System.out.println("Stored Procedure called updated value is = "+new_registrationInv[0]);
		
	}
	
	//@Test
	public void RegistrationInvitation_should_save(){
		RegistrationInvitation registrationInv=registrationInvitationRepository.findOne(105L);
		registrationInv.setId(null);
		RegistrationInvitation new_registrationInv=registrationInvitationRepository.save(registrationInv);
		System.out.println("Stored Procedure called new id is = "+new_registrationInv.getId());
	}
	
	@Test
	public void RegistrationInvitation_should_delete(){
		
		registrationInvitationRepository.delete(6752L);
		RegistrationInvitation deleted=registrationInvitationRepository.findOne(6752L);
		if(deleted==null){
			System.out.println("RegistrationInvitation deleted");
		}
	}
}
