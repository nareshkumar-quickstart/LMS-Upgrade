package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.softech.vu360.lms.model.VU360UserNew;
import com.softech.vu360.lms.repositories.VU360UserNewRepository;
import com.softech.vu360.lms.service.VU360UserNewService;

/**
 * @author jason
 *
 */
@Service
public class VU360UserNewServiceImpl implements VU360UserNewService {
	
	private static final Logger log = Logger.getLogger(VU360UserNewServiceImpl.class.getName());
	
	@Inject
	private VU360UserNewRepository vu360UserRepositoryNew;
	
	public VU360UserNew getSimpleUserById(Long id) {
		return vu360UserRepositoryNew.findOne(id);
	}
	
	public VU360UserNew updateUser(VU360UserNew updatedUser) {
		return vu360UserRepositoryNew.save(updatedUser);
	}
	
	public VU360UserNew getVU360UserNewById(Long id) {
		return vu360UserRepositoryNew.findOne(id);
	}
	
	public  VU360UserNew  loadForUpdateVU360UserNew(Long id){
		return vu360UserRepositoryNew.findOne(id);
	}
	
	public VU360UserNew updateNumLogons(VU360UserNew updatedUser){
		return updateUser(updatedUser);
	}
	
	public VU360UserNew getVU360UserByLearnerId(Long learnerId) {
		return vu360UserRepositoryNew.getUserByLearnerId(learnerId);
	}
	
	
}