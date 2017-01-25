package com.softech.vu360.lms.repositories;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CommissionableParty;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)public class CommissionablePartyRepositoryTest {

	@Inject
	private CommissionablePartyRepository commissionablePartyRepository;
	@Inject
	private CommissionParticipationRepository commissionParticipationRepository;
	//@Test
	public void findOne() {
		CommissionableParty entity = commissionablePartyRepository.findOne(805L);
		if(entity!=null)
			System.out.println(entity.getName());
	}

	//@Test
	public void findByDistributorId() {
		HashSet<CommissionableParty> aSet = commissionablePartyRepository.findByDistributorId(11506L);
		if(aSet!=null)
			System.out.println(aSet.size());
	}
	
	@Test
	public void deleteCommissionableParties() {
		Long[] commPartyIds =new Long[3];
		commPartyIds[0]=1757L;
		commPartyIds[1]=1758L;
		commPartyIds[2]=1759L;
		
    	if(commissionParticipationRepository.countByCommissionablePartyIdIn(commPartyIds)>0){
        	List<CommissionableParty> aListCommissionableParty= commissionablePartyRepository.findByIdIn(commPartyIds);
    		commissionablePartyRepository.delete(aListCommissionableParty);	
    	}
    	
	}
}
