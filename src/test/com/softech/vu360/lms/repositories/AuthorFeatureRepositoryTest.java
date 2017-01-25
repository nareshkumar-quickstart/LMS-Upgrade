package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.AuthorFeature;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AuthorFeatureRepositoryTest {
	@Inject
	private AuthorFeatureRepository authorFeatureRepository;

	//@Test
	public void findAll() {
		List<AuthorFeature> features = (List<AuthorFeature>) authorFeatureRepository.findAll();
		if(features!=null)
			System.out.println(features.size());
	}

//Course Editing
	@Test
	public void getAuthorFeaturesForSelfServiceUsers() {
		List<AuthorFeature> features = null;
		List<String> lstDisplayName = new ArrayList<String>();
		lstDisplayName.add("Course Editing");
			
		features = authorFeatureRepository.getAuthorFeaturesForSelfServiceUsers(lstDisplayName);
		if(features!=null)
			System.out.println(features.size());
	}
}
