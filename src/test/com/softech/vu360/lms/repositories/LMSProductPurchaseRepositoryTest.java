package com.softech.vu360.lms.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.LMSProductPurchase;
import com.softech.vu360.lms.repositories.LMSProductPurchaseRepository;

public class LMSProductPurchaseRepositoryTest  extends SpringJUnitConfigAbstractTest {
	@Autowired
	private LMSProductPurchaseRepository lMSProductPurchaseRepository;
	
	@Test
	public void findByCustomerIdByLmsProductId() {
		LMSProductPurchase lc = lMSProductPurchaseRepository.findTop1ByCustomerIdAndLmsProductId(1L, 1L);
		System.out.println(lc.getLmsProduct().getId());
	}

}
