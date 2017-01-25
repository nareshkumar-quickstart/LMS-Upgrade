package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.impl.EntitlementServiceImpl;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
@Transactional
public class OrgGroupEntitlementRepositoryTest {

	@Inject
	private OrgGroupEntitlementRepository repoOrgGroupEntitlement;
	@Inject
	public OrgGroupEntitlementRepository orgGroupEntitlementRepository;
	@Inject
	public CustomerEntitlementRepository customerEntitlementRespository;
	@Inject
	public CustomerRepository customerRespository;
	
	@Autowired
	public EntitlementService entitlementServiceImpl;
	
	 @Autowired 
     ApplicationContext context;
	
	@Before
	public void CustomerRepositoryInit(){
		entitlementServiceImpl = (EntitlementService) context.getBean("entitlementService");
    }
	
	//@Test
	public void getValidOrgGroupEntitlementsByCutomerEntitlement()
	{
		Customer c = customerRespository.findOne(1L);
		List<OrgGroupEntitlement>  d = orgGroupEntitlementRepository.findByIdAndMaxNumberSeatsGreaterThan(c, (long) 0);
		Assert.assertNotNull(d);
	}
	
	//@Test
	public void getOrgGroupsEntilementsForCustomerEntitlement()
	{
		CustomerEntitlement ce = customerEntitlementRespository.findOne(1L);
		List<OrgGroupEntitlement>  d = (List<OrgGroupEntitlement>) orgGroupEntitlementRepository.findById(ce);
		Assert.assertNotNull(d);
	}
	
	//@Test
	public void getOrgGroupEntitlementByOrgGroupId()
	{
		CustomerEntitlement ce = customerEntitlementRespository.findOne(1L);
		OrgGroupEntitlement f =  orgGroupEntitlementRepository.findById(ce.getId());
		Assert.assertNotNull(f);
	}

	//@Test
	public void deleteOrgGroupEntitlementInCustomerEntitlement()
	{
		Long[] l = new Long[]{43l,56l};
		List<OrgGroupEntitlement> listOGE = (List<OrgGroupEntitlement>) orgGroupEntitlementRepository.findAll(Arrays.asList(l));
		orgGroupEntitlementRepository.delete(listOGE);
		
	}

	//@Test
	public void getAvailableOrgGroupEntitlementsOfLearner()
	{
		List<OrganizationalGroup> orgGroupList=null;;
 		Learner learner=null;
		List<OrgGroupEntitlement> d = orgGroupEntitlementRepository.getAvailableOrgGroupEntitlementsOfLearner(learner, orgGroupList);
		
		
	}
	
	
	//@Test
	//@Deprecated   //This method not longer in use.
	public void orgGroupEntitlement_getOrgGroupEntitlemnetsForLearner(){
	
		List<OrgGroupEntitlement>  listOrgGroupEntitlement = repoOrgGroupEntitlement.getOrgGroupEntitlemnetsForLearner(108008L);
		System.out.println("..................");
	}

	
	//@Test
	@Transactional
	public void addOrgGroupEntitlementInCustomerEntitlement() {
		
		CustomerEntitlement ce = customerEntitlementRespository.findOne(1L);
		Long[] l = new Long[]{101l,102l};
		List<OrgGroupEntitlement> listOGE = (List<OrgGroupEntitlement>) orgGroupEntitlementRepository.findAll(Arrays.asList(l));
		entitlementServiceImpl.addOrgGroupEntitlementInCustomerEntitlement(ce, listOGE);
		
	}

	@Test
	@Transactional
	public void findByOrganizationalGroupId() {
		OrgGroupEntitlement ogE = orgGroupEntitlementRepository.findFirstByOrganizationalGroupId(76561L);
		System.out.println(ogE.getId());
		
	}
}
