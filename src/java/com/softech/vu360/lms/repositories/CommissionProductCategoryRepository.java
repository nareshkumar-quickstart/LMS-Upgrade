/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.CommissionProductCategory;

/**
 * @author muhammad.junaid
 *
 */
public interface CommissionProductCategoryRepository extends CrudRepository<CommissionProductCategory, Long> {
	List<CommissionProductCategory> findByCommissionId(Long commissionId);
	@Query("SELECT p FROM CommissionProductCategory p WHERE p.parentCommissionProductCategory.id IN(:pIds)")
	List<CommissionProductCategory> findByParentIdIn(@Param("pIds") List<Long> pIds);
	CommissionProductCategory findByProductCategoryCodeAndCommissionId(String catCode, Long commId);
	List<CommissionProductCategory> findByIdIn(Long[] ids);
}
