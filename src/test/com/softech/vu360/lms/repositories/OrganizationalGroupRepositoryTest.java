package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LmsTestConfig.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class OrganizationalGroupRepositoryTest {// extends
												// SpringJUnitConfigAbstractTest
												// {
	@Inject
	private OrganizationalGroupRepository orgGroupRepository;

	@Autowired
	private LearnerGroupRepository lgRepository;

	//@Test
	@Transactional
	public void OrganizationalGroup_should_deleteOrgGrp_with_orgChildrens() {
		long orgGroupIdArray[] = {72866, 72865};
		Long orgGroupIdArray2[] = ArrayUtils.toObject(orgGroupIdArray);
		for (int i = 0; i < orgGroupIdArray2.length; ++i) 
			orgGroupRepository.deleteById(orgGroupIdArray2[i]);
		
		OrganizationalGroup orgGroup = orgGroupRepository.findOne(44307L);
		if (orgGroup == null) {
			System.out.println("OrganizationalGroup deleted");
		}
	}

	//@Test
	public void OrganizationalGroup_should_deleteOrgGrp_with_orgChildrens_with_LearnerGroup() {

		OrganizationalGroup orgGroup = orgGroupRepository.findOne(65306L);
		List<LearnerGroup> ogLearnerGroups = lgRepository.findByOrganizationalGroupId(orgGroup.getId());
		for (LearnerGroup lg : ogLearnerGroups) {
			lgRepository.delete(lg.getId());
		}

		orgGroupRepository.deleteById(15406L);
	}

	// //@Test
	// public void brand_should_getById() {
	// Brand brand = brandRepository.findOne(16L);
	// System.out.println("Brand : ID = "+brand.getId()+" Name =
	// "+brand.getBrandName());
	// }
	//
	// @Test
	// public void brand_should_getAllBrand() {
	// Iterable<Brand> brandList = brandRepository.findAll();
	// System.out.println("BrandList contains =
	// "+((Collection<Brand>)brandList).size());
	// }
	//
	// //@Test
	// public void brand_should_deleteByName() {
	// brandRepository.deleteByBrandNameIgnoreCase("1nov");
	// Brand deleted=brandRepository.findOne(183L);
	// if(deleted==null){
	// System.out.println("Brand deleted");
	// }
	// }
	//
	// //@Test
	// public void brand_should_save(){
	//
	// brandRepository.save("Test_JPA_BrandName_1", "Test_JPA_BrandKey_2");
	// }

	// @Test
	public void findByCustomerIdAndNameIn() {
		String arr[] = { "MM", "MQA" };
		List<OrganizationalGroup> og = orgGroupRepository.findByCustomerIdAndNameIn(103L, arr);
		System.out.println(og.get(0).getName());
	}

	// @Test
	public void findByIdIn() {
		String arr[] = { "104", "106" };
		List<OrganizationalGroup> og = orgGroupRepository.findByIdIn(arr);
		System.out.println(og.get(0).getName());
	}

	// @Test
	public void findByCustomerId() {
		List<OrganizationalGroup> og = orgGroupRepository.findByCustomerId(1L);
		System.out.println(og.get(0).getName());
	}

	// @Test
	public void findById() {
		OrganizationalGroup og = orgGroupRepository.findOne(1L);
		System.out.println(og.getName());
	}

	// @Test
	public void findByCustomerIdAndRootOrgGroupIsNull() {
		List<OrganizationalGroup> og = orgGroupRepository.findByCustomerIdAndRootOrgGroupIsNull(1L);
		System.out.println(og.get(0).getName());
	}

	@Test
	@Transactional
	public void getOrganisationGroupsUnderAlertRecipient() {
		Long alertRecipientId = 9037L;
		String groupName = "%%";
		List<OrganizationalGroup> organizationalGroups = orgGroupRepository.getOrganisationGroupsUnderAlertRecipient(alertRecipientId, groupName);
		System.out.println(organizationalGroups);
	}

	//@Test
	@Transactional
	public void getOrganisationGroupsUnderAlertTriggerFilter() {
		Long alertTriggerFilterId = 1L;
		String groupName = "";
		List<OrganizationalGroup> organizationalGroups = orgGroupRepository.getOrganisationGroupsUnderAlertTriggerFilter(alertTriggerFilterId, groupName);
		System.out.println(organizationalGroups);
		
	}

}
