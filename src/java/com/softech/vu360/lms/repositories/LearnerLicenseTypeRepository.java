package com.softech.vu360.lms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.LearnerLicenseType;

public interface LearnerLicenseTypeRepository  extends CrudRepository<LearnerLicenseType, Long> {

	List<LearnerLicenseType> findByLearnerId(Long learnerId);
}
