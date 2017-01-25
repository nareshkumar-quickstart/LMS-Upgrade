package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import com.softech.vu360.lms.model.AICCAssignableUnit;
import com.softech.vu360.lms.repositories.AICCAssignableUnitRepository;
import com.softech.vu360.lms.service.AICCAssignableUnitService;

public class AICCAssignableUnitServiceImpl implements AICCAssignableUnitService {
	
	@Inject
	private AICCAssignableUnitRepository aiccAssignableUnitRepository;

	@Override
	public AICCAssignableUnit findAICCAssignableUnitByCourseId(Long courseID) {
		return aiccAssignableUnitRepository.findByCourseId(courseID);
	}

}
