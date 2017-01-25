package com.softech.vu360.lms.service;

import java.util.ArrayList;

import com.softech.vu360.lms.model.AffidavitTemplate;

public interface AffidavitTemplateService {

	public AffidavitTemplate getAffidavitTemplateById(long id);

	public ArrayList<AffidavitTemplate> getAllAffidavitTemplates();

	public AffidavitTemplate getDefaultAffidavitTemplate();

	public void save(AffidavitTemplate affidavitTemplate) throws Exception;

	public void update(AffidavitTemplate affidavitTemplate) throws Exception;

	public void delete(AffidavitTemplate affidavitTemplate);

	public AffidavitTemplate getAffidavitTemplateByName(
			String affidavitTemplateName);

}
