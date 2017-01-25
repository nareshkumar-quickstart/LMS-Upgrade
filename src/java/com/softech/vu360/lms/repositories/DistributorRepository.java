
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Distributor;

/**
 * @author muhammad.junaid
 * @author haider.ali
 *
 */
public interface DistributorRepository extends CrudRepository<Distributor, Long>, DistributorRepositoryCustom, JpaRepository<Distributor, Long>, JpaSpecificationExecutor<Distributor>  {
	
	Distributor findFirstByContentOwner(ContentOwner contentOwner);
	Distributor findByName(String name);
	//Distributor findById(Long id); // This should be removed as findOne (built-in function) does the same
	Distributor findByDistributorCode(String code);
	
	@Query("select distinct d from Distributor d inner join d.customFields")
	List<Distributor> findResellersWithCustomFields();
	List<Distributor> findByNameEquals(String name);
	List<Distributor> findByNameLike(String name);

}
