/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.AffidavitTemplate;

/**
 * @author muhammad.junaid
 *
 */
public interface AffidavitTemplateRepository extends CrudRepository<AffidavitTemplate, Long> {
	AffidavitTemplate findByTemplateGUIDIgnoreCase(String templateGUID);
}
