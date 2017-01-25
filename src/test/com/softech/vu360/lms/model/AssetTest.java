package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class AssetTest extends TestBaseDAO<Asset> {

	
// @Test
	public void Document_should_save() throws Exception {
		Document doc = new Document();
		doc.setDescription("Test_Description1");
		doc.setActive(false);
		doc.setFileName("Test_FileName1");
		doc.setNoOfDocumentsPerPage(2);
		doc.setName("Test_Name1");
		save(doc);

	}

	// @Test
	public void Affidavit_should_save() throws Exception {
		Affidavit affidavit = new Affidavit();
		affidavit.setActive(false);
		affidavit.setContent("Test_Content");
		affidavit.setName("Test_Name2");
		crudSave(Affidavit.class, affidavit);
	}
}
