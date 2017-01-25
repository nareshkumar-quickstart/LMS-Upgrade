package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LMSFeature;

public class LMSFeatureRepositoryTest extends SpringJUnitConfigAbstractTest {

	@Inject
	LMSFeatureRepository lmsFeatureRepository;

	// @Test
	public void findByRoleType() {

		List<LMSFeature> lfList = lmsFeatureRepository
				.findByRoleType("ROLE_LEARNER");
		if (lfList != null && lfList.size() > 0) {
			for (LMSFeature lf : lfList) {
				System.out.println(lf.getFeatureCode());
			}
		}
	}

	//@Test
	public void findByRoleTypeAndIdNotIn() {
		List<LMSFeature> featureList = null;
		Long l[] = new Long[2];
		l[0] = 16l;
		l[1] = 160l;
		List<Long> list = Arrays.asList(l);
		String roleType = "ROLE_LEARNER";
		featureList = lmsFeatureRepository.findByRoleTypeAndIdNotIn(roleType,
				list);
		if (featureList != null && featureList.size() > 0) {
			for (LMSFeature feature : featureList) {
				System.out.println(feature.getFeatureCode());
			}
		}

	}
	
	
	@Test
	public void getAllDefaultFeatures() {
		List<LMSFeature> lfList = (List<LMSFeature>) lmsFeatureRepository.findAll();
		Assert.assertNotNull(lfList);
	}
}
