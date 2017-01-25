package com.softech.vu360.lms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.LicenseIndustry;

public interface LicenseIndustryRepository extends CrudRepository<LicenseIndustry, Long> {
	List<LicenseIndustry> findAll();
}
