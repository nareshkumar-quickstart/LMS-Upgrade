package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.LcmsResource;

/**
 * 
 * @author haider.ali
 *
 */
public interface LcmsResourceRepository extends CrudRepository<LcmsResource, Long> {
	
	public LcmsResource findByBrandIdAndLocaleIdEqualsAndLanguageIdEqualsAndResourceKeyEquals(Long brandID, Integer LocaleID, Long languageID, String resourcekey);
	
}