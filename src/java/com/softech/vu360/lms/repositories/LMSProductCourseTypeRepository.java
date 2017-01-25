package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.LMSProductCourseType;

public interface LMSProductCourseTypeRepository extends CrudRepository<LMSProductCourseType, Long> {
	//public List<LMSProductCourseType> getCustomerCourseTypes(long customerid);
	@Query("SELECT pct FROM  #{#entityName} pct where pct.lmsProduct.id in (select lmsProduct.id "
			+ "from LMSProductPurchase where customer.id=:Customer_ID)")
	List<LMSProductCourseType> findByLmsProductIdByCustomerId(@Param("Customer_ID") Long customerId);
	
	
	
}
