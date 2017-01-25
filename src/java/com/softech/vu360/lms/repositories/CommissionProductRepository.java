/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CommissionProduct;

/**
 * @author muhammad.junaid
 *
 */
public interface CommissionProductRepository extends CrudRepository<CommissionProduct, Long> {
	List<CommissionProduct> findByCommissionId(Long commissionId);
	List<CommissionProduct> findByCommissionProductCategoryIdIn(Long[] catId);
	List<CommissionProduct> findByIdIn(Long[] ids);
}
