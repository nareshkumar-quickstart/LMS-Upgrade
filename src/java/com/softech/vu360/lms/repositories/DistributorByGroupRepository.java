package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSAdministrator;

public interface DistributorByGroupRepository  extends CrudRepository<LMSAdministrator, Long>{
	
	//public List<Distributor> findBy

}
