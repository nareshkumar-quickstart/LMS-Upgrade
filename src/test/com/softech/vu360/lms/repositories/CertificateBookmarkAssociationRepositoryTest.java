package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CertificateBookmarkAssociation;
import com.softech.vu360.util.PdfFieldsEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CertificateBookmarkAssociationRepositoryTest {

	@Inject
	private CertificateBookmarkAssociationRepository repoCertificateBookmarkAssociation;
	
	//@Test
	public void certificateBookmarkAssociation_should_save() {
		CertificateBookmarkAssociation saveCertificateBookmarkAssociation = repoCertificateBookmarkAssociation.findOne(new Long(2105));
		saveCertificateBookmarkAssociation.setId(null);
		
		CertificateBookmarkAssociation savedCertificateBookmarkAssociation = repoCertificateBookmarkAssociation.save(saveCertificateBookmarkAssociation);
		
		System.out.println("..........");
	}
	
	//@Test
	public void certificateBookmarkAssociation_should_find() {
		CertificateBookmarkAssociation saveCertificateBookmarkAssociation = repoCertificateBookmarkAssociation.findOne(new Long(2));
		
		System.out.println("..........");
	}
	
	//@Test
	public void certificateBookmarkAssociation_should_deleteAll(List<CertificateBookmarkAssociation> objCertificateBookmarkAssociations) {
		repoCertificateBookmarkAssociation.delete(objCertificateBookmarkAssociations);
		
		System.out.println("..........");
	}
	
	//@Test
	public void certificateBookmarkAssociation_should_find_by_bookmarkLabel() {
		String bookmarkLabel = "Certificate_Expiration_Date";
		List<CertificateBookmarkAssociation> listCertificateBookmarkAssociation = repoCertificateBookmarkAssociation.findByBookmarkLabelLikeIgnoreCase('%'+bookmarkLabel+'%');
		
		System.out.println("..........");
	}

	//@Test
	public void certificateBookmarkAssociation_should_find_by_reportingFields() {
		Long[] reportingFields = new Long[]{9658L};
		
		try{
			List<CertificateBookmarkAssociation> listCertificateBookmarkAssociation = repoCertificateBookmarkAssociation.findByreportingFieldIdIn(reportingFields);
		
			System.out.println("..........");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Test
	public void certificateBookmarkAssociation_should_find_by_type() {
		
		try{
			List<CertificateBookmarkAssociation> listCertificateBookmarkAssociation = repoCertificateBookmarkAssociation.findByFieldsType(PdfFieldsEnum.REPORTINGFIELDS);
			List<CertificateBookmarkAssociation> listCertificateBookmarkAssociation1 = repoCertificateBookmarkAssociation.findByFieldsType(PdfFieldsEnum.CUSTOMFIELDS);
			List<CertificateBookmarkAssociation> listCertificateBookmarkAssociation2 = repoCertificateBookmarkAssociation.findByFieldsType(PdfFieldsEnum.LMSFIELDS);
		
			System.out.println("..........");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
