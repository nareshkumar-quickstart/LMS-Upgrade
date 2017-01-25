package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.PurchaseCertificateNumber;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class PurchaseCertificateNumberRepositoryTest {

	@Inject
	private PurchaseCertificateNumberRepository purchaseCertificateNumberRepository;
	
	//@Test
	public void purchaseCertificateNumber_should_save() {
		PurchaseCertificateNumber savePurchaseCertificateNumber = purchaseCertificateNumberRepository.findOne(new Long(2105));
		savePurchaseCertificateNumber.setId(null);
		
		PurchaseCertificateNumber savedPurchaseCertificateNumber = purchaseCertificateNumberRepository.save(savePurchaseCertificateNumber);
		
		System.out.println("..........");
	}
	
	//@Test
	public void purchaseCertificateNumber_should_find() {
		PurchaseCertificateNumber savePurchaseCertificateNumber = purchaseCertificateNumberRepository.findOne(new Long(2105));
		
		System.out.println("..........");
	}
	
	//@Test
	public void purchaseCertificateNumber_should_deleteAll() {
		List<PurchaseCertificateNumber> purchaseCertificateNumbers= new ArrayList<PurchaseCertificateNumber>();
		purchaseCertificateNumberRepository.delete(purchaseCertificateNumbers);
		
		System.out.println("..........");
	}
	
	@Test
	public void purchaseCertificateNumber_getUnusedPurchaseCertificateNumber() {
		PurchaseCertificateNumber i =purchaseCertificateNumberRepository.getUnusedPurchaseCertificateNumber(13805L);
		
		System.out.println("..........");
	}

}
