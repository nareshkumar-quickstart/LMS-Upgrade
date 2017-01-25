package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.LMSProducts;

public interface LMSProductsRepository extends CrudRepository<LMSProducts, Long> {
	//public LMSProducts findLMSProductsPurchasedByCustomer(long productId,long customerId)
	
	
}
