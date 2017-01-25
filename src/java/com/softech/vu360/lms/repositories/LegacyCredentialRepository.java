package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.LegacyCredential;

/**
 * Repository interface for {@code LegacyCredential}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface LegacyCredentialRepository  extends CrudRepository<LegacyCredential, Long> {
	LegacyCredential findTop1ByLmsEnrollmentId(Long enrollmentId);
}
