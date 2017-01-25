package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.DistributorGroup;


/**
 * 
 * @author haider.ali
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class DistributorGroupRepositoryTest {

	@Inject
	private DistributorGroupRepository distributorGroupRepository;

	@Inject
	private DistributorRepository distributorRepository;

	
	
	//@Test
	public void getByName(){
		DistributorGroup d = distributorGroupRepository.findByName("131014ResellerGroup");
		Assert.notNull(d);
	}

	//@Test
	public void addDistributorGroup(){
		DistributorGroup g = distributorGroupRepository.findOne(4304L);
		g.setId(null);
		g = distributorGroupRepository.save(g);
		Assert.notNull(g.getId());
	}

	//@Test
	public void getDistributorGroupById(){
		DistributorGroup g = distributorGroupRepository.findById(4304L);
		Assert.notNull(g.getId());
	}

	//@Test
	public void saveDistributorGroup(){
		DistributorGroup g = distributorGroupRepository.findById(4304L);
		Assert.notNull(g.getId());
	}

	

	//@Test
	public void deleteDistributorGroups(){
		Long dgId[] = new Long[2];
		dgId[0]= 207L;
		dgId[1]= 206L;

		distributorGroupRepository.deleteDistributorGroups(dgId);
	}


	//@Test
	public void assignDistributorsInDistributorGroup(){

		Long dgId[]  = new Long[]{30613L, 30614L};
		distributorGroupRepository.assignDistributorsInDistributorGroup(104L, dgId);
	}

	//@Test
	public void getDistributorGroupsBydistributorId(){

		List<DistributorGroup> ls = distributorGroupRepository.findByDistributorsId(3L);
		System.out.print(ls);
	}
	
	@Test
	public void findDistributorGroupsByLMSAdministratorId() {
		List<DistributorGroup> dgs = distributorGroupRepository.findDistributorGroupsByLMSAdministratorId(1L);
		System.out.println(dgs);
	}

}
