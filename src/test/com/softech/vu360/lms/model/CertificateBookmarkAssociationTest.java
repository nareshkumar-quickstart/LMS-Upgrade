package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class CertificateBookmarkAssociationTest extends
		TestBaseDAO<CertificateBookmarkAssociation> {
	

	@Before
	public void setRequiredService() {

	}

//	@Test
	public void CertificateBookmarkAssociation_should_save() throws Exception {
		
		CertificateBookmarkAssociation cba= new CertificateBookmarkAssociation();
		
		cba.setBookmarkLabel("Test_BookMark_Label");		
		
		CreditReportingField crf=(CreditReportingField)crudFindById(CreditReportingField.class, new Long(9658));
		
		CustomField customField=(CustomField)crudFindById(CustomField.class, new Long(15663));
		
		cba.setCustomField(customField);
		cba.setReportingField(crf);
		cba.setLMSFieldName("Test_LMS_Field_name");
			
		try {

			cba=save(cba);
		} catch (Exception ex) {
			System.out.println(cba.getId());
		}

	}
	
	@Test
	public void CertificateBookmarkAssociation_should_update(){
		
		CertificateBookmarkAssociation cba=getById(new Long(2602), CertificateBookmarkAssociation.class);
		cba.setCustomField(null);
		cba.setBookmarkLabel("Test_BookMark_Label_Updated");
		save(cba);
	}


}
