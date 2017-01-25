/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.LineItem;

/**
 * @author muhammad.junaid
 *
 */
public interface LineItemRepository extends CrudRepository<LineItem, Long> {
	LineItem findByOrderInfoIdAndItemGUID(Long orderId,String courseGUID);
/*	@Query(value="SELECT li FROM LineItem li WHERE li.orderInfo.id=?1")
	List<LineItem> findByOrderInfoId(Long orderId);*/
}
