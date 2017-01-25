package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.VU360User;

/**
 * 
 * @author haider.ali
 *
 */
public interface CustomerRepository extends CrudRepository<Customer, Long>, CustomerRepositoryCustom, JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

	public Customer findByContentOwner(ContentOwner contentOwner);
	public Customer findByCustomerGUID(String guid);
	public Customer findByCustomerCode(String customerCode);
	public List<Customer> findByDistributorId(Long distributorId);
	public List<Customer> findByDistributor(Distributor distributor);
	public List<Customer> findByEmail(String email);
	@Query(value = "select COUNT(customer0_.ID) from CUSTOMER customer0_ left outer join DISTRIBUTOR distributo1_ on customer0_.DISTRIBUTOR_ID=distributo1_.id where distributo1_.id = :id", nativeQuery = true)
	public Long getAssociatedCustomerCount(@Param("id")Long distributorId);

	@Query(value = "select customer0_.* from CUSTOMER customer0_ left outer join DISTRIBUTOR distributo1_ on customer0_.DISTRIBUTOR_ID=distributo1_.id where customer0_.IS_DEFAULT=1 and distributo1_.id = :id", nativeQuery = true)
	public Customer findDefaultCustomerByDistributor(@Param("id")Long distributorId);
}