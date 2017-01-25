/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.RegulatoryAnalyst;

/**
 * @author marium.saud
 *
 */
public interface ContentOwnerRepository extends CrudRepository<ContentOwner, Long> {
	
	@Query(value = " select new map ((co.id) as ID, (co.name) as NAME, (co.guid) as GUID ) from ContentOwner as co " +
					" join co.customer as c" +
					" where c.distributor.id in (:lstDistributorId)") 
	List<Map<Object, Object>> getContentOwnerByDistributorIDs(@Param("lstDistributorId")Collection<Long> lstDistributorId);
	    
	ContentOwner findFirstByGuidOrderByIdAsc(String contentOwnerGUID);
	
	ContentOwner findByCustomer(Customer customer);
	
	List<ContentOwner> findByRegulatoryAnalysts(RegulatoryAnalyst ra);
	
	
	
}
