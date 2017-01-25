package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.ContentOwner;

public interface AffidavitRepository extends CrudRepository<Affidavit, Long> {
	List<Affidavit> findByNameLikeIgnoreCaseAndActiveIsTrue(String name);
}
