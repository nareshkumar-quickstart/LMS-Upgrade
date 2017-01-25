package com.softech.vu360.lms.repositories;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LicenseOfLearner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class LicenseOfLearnerRepositoryTest {

	@Inject
	private LicenseOfLearnerRepository objLOL;
	
	//@Test
	public void findByIdIn() {
		Long [] ids = {1102L, 1152L};
		List<LicenseOfLearner> lh = objLOL.findByIdIn(ids);
		System.out.println(lh.get(0).getState());
	}
	
	//@Test
	public void findByLearnerIdInByActiveTrue() {
		Long ids = 1038796L;
		List<LicenseOfLearner> lh = objLOL.findByActiveTrueAndLearnerId(ids);
		System.out.println(lh.get(0).getState());
	}
	
	//public List<LicenseOfLearner> deleteLicenseOfLearner(List<LicenseOfLearner> licenseOfLearner )
	//@Test
	@Transactional
	public void updateLicenseOfLearner() {
		Long ids = 1038796L;
		List<LicenseOfLearner> licenseOfLearner = objLOL.findByActiveTrueAndLearnerId(ids);
		
		if (licenseOfLearner!=null && licenseOfLearner.size()>0) {
			for (LicenseOfLearner l : licenseOfLearner) {
				l.setActive(Boolean.FALSE);
				l.setUpdateOn(new Timestamp(System.currentTimeMillis()));
				objLOL.save(l);
			}
		}
	}
	
	
	//@Test
	@Transactional
	public void findByindustryCredentialcredentialOfficialLicenseName() {
		Long ids = 1038796L;
		List<LicenseOfLearner> licenseOfLearner = objLOL.findByActiveTrueAndLearnerIdAndIndustryCredential_Credential_OfficialLicenseNameLike(ids, "%metology Lice%");
		System.out.println(licenseOfLearner.get(0).getState());
	}
	
	
	//@Test
		public void getLicenseNameByLearnerIds(){

			List<Map<Object, Object>> results=objLOL.findLicenseNameByLearnerIds(1L);
			for(Map map : results){
				System.out.println("officialLicenseName	 : "+map.get("OFFICIALLICENSENAME"));
				System.out.println("ID : "+map.get("ID"));
				System.out.println("LEARNER_ID : "+map.get("LEARNER_ID"));
			}
		}
		
		@Test
		public void getLicenseNameByLearnerIds_with_User_Criteria(){

			List<Map<Object, Object>> results=objLOL.findLicenseNameByLearnerIds(1L,"3603","training3","ramiz.uddin@360training.com");
			for(Map map : results){
				System.out.println("officialLicenseName	 : "+map.get("OFFICIALLICENSENAME"));
				System.out.println("ID : "+map.get("ID"));
				System.out.println("LEARNER_ID : "+map.get("LEARNER_ID"));
			}
		}
	
	//@Test
	public void getByLearnerLicenseId(){
		
		List<Long> lst = new ArrayList<Long>();
		lst.add(1852L);lst.
		add(1902L);
		
		List<Map<Object, Object>> results=objLOL.findByLearnerLicenseIds(lst);
		for(Map map : results){
			System.out.println("ID : "+map.get("ID"));
			System.out.println("LICENSE_ID : "+map.get("LICENSE_ID"));
		}
	}
	
	//@Test
	public void findByLearnerId(){
		long id=1037844l;
		List<LicenseOfLearner>  results=objLOL.findByLearnerId(id);
		for(LicenseOfLearner map : results){
			System.out.println(map.getId());
			
		}
	}
	
	
}
