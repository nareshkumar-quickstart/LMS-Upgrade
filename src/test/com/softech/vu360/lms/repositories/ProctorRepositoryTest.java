package com.softech.vu360.lms.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.util.ProctorStatusEnum;

@Transactional
public class ProctorRepositoryTest  extends SpringJUnitConfigAbstractTest {

	@Inject
	private ProctorRepository proctorRepository;
	
	@Inject 
	private LearnerEnrollmentRepository learnerEnrollmentRepo;
	
	private ProctorService proctorService;
	
	 @Autowired 
     ApplicationContext context;
	
	@Before
	public void CustomerRepositoryInit(){
		proctorService = (ProctorService) context.getBean("proctorService");
    }
	
	//@Test
	public void Language_List_Should() {
		try{
			
			
			Proctor lng = proctorRepository.findByUserId(1672L);
		//	Language lng1 = this.languageRepository.findOne(1L);
		//	for(Language language:lng)
		//		System.out.println(language.getDisplayName());
			System.out.println(lng.getUsername());
			
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
		
	}
	
	
//	@Test
	public void proctor_should_Find_All(){
		Long credentialId = 6L;
		String status = ProctorStatusEnum.Expired.toString();
		List<Proctor> cas = proctorRepository.findByCredentialsIdAndStatusNot(credentialId, status);
		
		System.out.println("..................");
	}
	

	//Added By Marium Saud
//	@Test
	public void Proctor_should_getLearners_By_Credential_TrainingCourse(){
		Long[] courseId=new Long[5];
		courseId[0]=5689L;
		courseId[1]=4035L;
		courseId[2]=4523L;
		courseId[3]=2437L;
		courseId[4]=7002L;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Date dtStartDate = null, dtEndDate = null;
		try {
			dtStartDate=sdf.parse("06/02/2009");
			dtEndDate=sdf.parse("09/22/2009");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Long[] l=null;
		List<LearnerEnrollment> result=proctorService.getLearnersByCredentialTrainingCourse(courseId, "Learner1", "22dec09", "learner1@22dec09.com", "15th December 09 Customer", dtStartDate, dtEndDate, l);
		System.out.println(result.size());
	}

}
