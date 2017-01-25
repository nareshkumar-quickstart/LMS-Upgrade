package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.ContentOwner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CertificateRepositoryTest {

	@Inject
	private CertificateRepository certificateRepository;
	
	//@Test
	public void certificate_should_find_By_Id() {
		
		Certificate findCertificate = certificateRepository.findOne(5374L);
		
		System.out.println("..........");
	}
	
	//@Test
	public void certificate_should_save() {
		
		Certificate savedCertificate = certificateRepository.findOne(5374L);
		savedCertificate.setId(null);
		
		Certificate newSavedCertificate = certificateRepository.save(savedCertificate);
		
		System.out.println("..........");
	}
	
	//@Test
	public void certificate_should_getCertificatesWhereAssetVersionIsEmpty() {
		Long toCertificateId = 300L;
		Long fromCertificateId = 1000L;
		List<Certificate> listCertificate = certificateRepository.getCertificatesWhereAssetVersionIsEmpty(fromCertificateId, toCertificateId);
		
		
		System.out.println("..........");
	}
	
	@Test
	public void findByNameAndRegulatoryAnalysts() {
		String name = "IELTS-1";
		
		List<Certificate> listCertificate = certificateRepository.findByNameLikeIgnoreCaseAndActiveIsTrue('%'+name+'%');
		
		
		System.out.println("..........");
	}
	
}
