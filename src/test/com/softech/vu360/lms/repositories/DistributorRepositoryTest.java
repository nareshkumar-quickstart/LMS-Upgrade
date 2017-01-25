package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class DistributorRepositoryTest {
	
	@Inject
	private DistributorRepository distributorRepository;

	@Inject
	private VU360UserRepository vU360UserRepository;
	
	@Inject
	private ContentOwnerRepository contentOwnerRespository;
	
	@Autowired 
    ApplicationContext context;
	 
	
	public DistributorService distributorService; 
	
	@Before
	public void DistributorRepositoryInit(){
		distributorService = (DistributorService) context.getBean("distributorService");
    }


	//@Test
	public void findFirstByContentOwner() {
		ContentOwner contentOwner=contentOwnerRespository.findFirstByGuidOrderByIdAsc("373fc0fa-8f32-401c-b0c7-129939760d22");
		Distributor distributor = distributorRepository.findFirstByContentOwner(contentOwner);
		if(distributor!=null)
			System.out.println(distributor.getFirstName());
	}

	//@Test
	public void findByName() {
		Distributor d = distributorRepository.findByName("sds,ad-7431");
		Assert.notNull(d);
	}	

	//@Test
	public void findByDistributorCode() {
		Distributor d = distributorRepository.findByDistributorCode("Test_Distributor_1");
		Assert.notNull(d);
	}	

	//@Test
	public void getAllowedDistributorByGroupId() {
		List<Distributor> d = distributorRepository.getAllowedDistributorByGroupId("54","353");
		Assert.notNull(d);
	}	
	
	//@Test
	//@Transactional
	public void deleteDistributorByGroupIdAndAdministratorIdAndAllowedDistId() {
		distributorRepository.deleteDistributorByGroupIdAndAdministratorIdAndAllowedDistributorId("1355","53", "3610");
	}	

		
	//@Test
	public void addDistributor() {
		Distributor d = distributorRepository.findOne(304L);
		d.setId(null);
		d.getDistributorAddress().setId(null);
		d.getDistributorAddress2().setId(null);
		d.getDistributorPreferences().setId(null);
		Distributor sd = distributorRepository.save(d);
		Assert.notNull(sd);
	}	
	
	//@Test
	public void save() {
		Distributor d = distributorRepository.findOne(304L);
		d.setId(null);
		d.getDistributorAddress().setId(null);
		d.getDistributorAddress2().setId(null);
		d.getDistributorPreferences().setId(null);
		Distributor sd = distributorRepository.save(d);
		Assert.notNull(sd);
	}	
	
	//@Test
	public void findResellersWithCustomFields() {
		List<Distributor> d = distributorRepository.findResellersWithCustomFields();
		Assert.notNull(d);
	}

	
	
	//@Test
	//@Transactional
	public void findDistributorsByName() {
		
		VU360User u = vU360UserRepository.findOne(1L);		
		Map<Object, Object> d = distributorService.findDistributorsByName("dis", u, 1, 2, "name", 1);
		System.out.println(d);
		
		Assert.notNull(d);
	}
	
	@Test
	@Transactional
	public void findDistributorGroupsByName() {
		
		VU360User u = vU360UserRepository.findOne(1L);		
		List<DistributorGroup> d = distributorService.findDistributorGroupsByName("d%", ProxyVOHelper.setUserProxy(u), false);
		System.out.println(d);
		
		Assert.notNull(d);
	}
	

	public DistributorService getDistributorService() {
		return distributorService;
	}


	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}	

	
	
}
