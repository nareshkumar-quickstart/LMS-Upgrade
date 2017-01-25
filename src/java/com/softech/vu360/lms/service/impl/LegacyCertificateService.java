package com.softech.vu360.lms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.model.LegacyCredential;
import com.softech.vu360.lms.repositories.LegacyCredentialRepository;

public class LegacyCertificateService {

	
	@Autowired
	private LegacyCredentialRepository lcRepository = null;
	
	public LegacyCredential getCertificateCredential(int enrollmentId){		
		lcRepository.findTop1ByLmsEnrollmentId(Long.valueOf(enrollmentId));
		return lcRepository.findTop1ByLmsEnrollmentId(Long.valueOf(enrollmentId));
	}
}
