package com.softech.vu360.lms.service.impl;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.repositories.LanguageRepository;
import com.softech.vu360.lms.service.LanguageService;

/**
 * @author jason
 *
 */
@Service
public class LanguageServiceImpl implements LanguageService {
	
	private static final Logger log = Logger.getLogger(LanguageServiceImpl.class.getName());
	
	@Inject
	private LanguageRepository languageRepository;

	@Override
	public Language getUserLanguageById(Long id) {
		return languageRepository.findOne(id);
	}
	
	
	
	
}