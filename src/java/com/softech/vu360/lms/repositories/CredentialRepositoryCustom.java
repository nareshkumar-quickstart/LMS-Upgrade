package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.Credential;

public interface CredentialRepositoryCustom  {
	public List<Credential> findCredentialByContentOwner(Collection<Long> contentOwnerIds, String officialLicenseName, String shortLicenseName, Boolean active);
	
	
	
}