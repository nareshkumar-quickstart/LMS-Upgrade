package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.softech.vu360.lms.model.RegulatoryAnalyst;
import com.softech.vu360.lms.repositories.RegulatoryAnalystRepository;
import com.softech.vu360.lms.service.RegulatoryAnalystService;

/**
 * @author jason
 *
 */
@Service
public class RegulatoryAnalystServiceImpl implements RegulatoryAnalystService {
	
	private static final Logger log = Logger.getLogger(RegulatoryAnalystServiceImpl.class.getName());
	
	@Inject
	private RegulatoryAnalystRepository regulatoryAnalystRepository;

	@Override
	public RegulatoryAnalyst getRegulatoryAnalystById(Long id) {
		return regulatoryAnalystRepository.findOne(id);
	}
	
	
	
	
}