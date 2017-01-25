package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.AffidavitTemplate;
import com.softech.vu360.lms.repositories.AffidavitTemplateRepository;
import com.softech.vu360.lms.service.AffidavitTemplateService;



public class AffidavitTemplateServiceImpl implements AffidavitTemplateService 
{
	public static final int DEFAULT_AFFIDAVIT_TEMPLATE_ID=1;
	@Inject
	private AffidavitTemplateRepository affidavitTemplateRepository;
   
	public AffidavitTemplate getAffidavitTemplateById(long id) 
	{
		AffidavitTemplate affidavitTemplate = null;
		try {
			affidavitTemplate = affidavitTemplateRepository.findOne(id);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return affidavitTemplate ;
	}
	
	public ArrayList<AffidavitTemplate> getAllAffidavitTemplates(){
		return (ArrayList<AffidavitTemplate>) affidavitTemplateRepository.findAll();
	}
	/*
	 * All service will use AffidavitTemplateService to retrieve Default AffidavitTemplate
	 * AffidavitTemplateService is responsible to decide the default AffidavitTemplate
	 */
	public AffidavitTemplate getDefaultAffidavitTemplate()
	{
		return this.getAffidavitTemplateById(DEFAULT_AFFIDAVIT_TEMPLATE_ID);
	}

	@Transactional
	@Override
	public void save(AffidavitTemplate affidavitTemplate) throws Exception {
		affidavitTemplateRepository.save(affidavitTemplate);
	}

	@Override
	public void update(AffidavitTemplate affidavitTemplate) throws Exception {
		affidavitTemplateRepository.findOne(affidavitTemplate.getId());
	}

	@Transactional
	@Override
	public void delete(AffidavitTemplate affidavitTemplate) {
		affidavitTemplateRepository.delete(affidavitTemplate);
	}


	@Override
	public AffidavitTemplate getAffidavitTemplateByName(String affidavitTemplateGUID) {
		return affidavitTemplateRepository.findByTemplateGUIDIgnoreCase(affidavitTemplateGUID);
	}

}
