package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LMSProductPurchase;
import com.softech.vu360.lms.model.LMSProducts;

public interface LMSProductPurchaseRepository extends CrudRepository<LMSProductPurchase, Long> {
	//public LMSProducts findLMSProductsPurchasedByCustomer(long productId,long customerId) // legacy DAO function converted into following
	LMSProductPurchase findTop1ByCustomerIdAndLmsProductId(Long customerId, Long lmsProductId);
}
