package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class DistributorGroupTest extends
		TestBaseDAO<DistributorGroup> {

	@Before
	public void setRequiredService() {

	}

//	@Test
	public void DistributorGroup_should_save() throws Exception {

		DistributorGroup distributorGroup = new DistributorGroup();

		distributorGroup.setActive(true);
		distributorGroup.setName("Test_Distributor");
		
		Brand brand=(Brand)crudFindById(Brand.class, new Long(1));
		distributorGroup.setBrand(brand);
		
		List<Distributor> distributor=new ArrayList<Distributor>();
		distributor.add((Distributor) crudFindById(Distributor.class, new Long(1)));
		distributorGroup.setDistributors(distributor);
		try {

			distributorGroup = save(distributorGroup);
			System.out.println(distributorGroup.getId());
		} catch (Exception ex) {
			System.out.println(distributorGroup.getId());
		}

	}

	@Test
	public void DistributorGroup_should_update() {

		DistributorGroup group=getById(new Long(4304), DistributorGroup.class);
		group.setName("Test_Distributor_Updated");
		group.setDistributors(null);
		save(group);
		
	}

}
