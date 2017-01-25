package com.softech.vu360.lms.repositories;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.LcmsResource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class LcmsResourceRepositoryTest {

	
	
	
	@Inject
	private LcmsResourceRepository lcmsResourceRepository;
	
	 @Autowired 
     ApplicationContext context;
	
	 public void saveLcmsResource(){
		 LcmsResource l = lcmsResourceRepository.findOne(new Long(89));
		 l.setId(null);
		 lcmsResourceRepository.save(l);
	 }
	 
	 @Test
	 public void lcmsResourceRepository_should_findBy_BrandId_LocaleId_LanguageId_ResourceKey(){
		 LcmsResource l = lcmsResourceRepository.findByBrandIdAndLocaleIdEqualsAndLanguageIdEqualsAndResourceKeyEquals(240L, 1, 1L, "ImageComanyLogo");
		 System.out.println(l.getId());
	 }
	
}
