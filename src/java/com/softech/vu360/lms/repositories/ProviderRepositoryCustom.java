package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.RegulatoryAnalyst;

public interface ProviderRepositoryCustom {
	List<Provider> findProviders(String name, List<ContentOwner> cos);
	List<Provider> searchProviders(String name, List<ContentOwner> cos);
}
