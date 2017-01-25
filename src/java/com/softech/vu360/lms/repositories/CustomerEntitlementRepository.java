package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;

/**
 * 
 * @author haider.ali
 *
 */
public interface CustomerEntitlementRepository extends CrudRepository<CustomerEntitlement, Long>, CustomerEntitlementRepositoryCustom {

	public List<CustomerEntitlement> findByCustomer(Customer customer);

	public List<CustomerEntitlement> findByCustomerId(Long id);

	@Query("Select p from CustomerEntitlement p WHERE p.customer.id=:customerId ORDER BY p.contractCreationDate ASC")
	public List<CustomerEntitlement> findCustomersWithEntitlementByDistributor(@Param("customerId") Long customerId);

	public List<CustomerEntitlement> findByCustomerIdAndAllowSelfEnrollmentTrue(Long customerId);
	
	@Query("Select p from CustomerEntitlement p WHERE (((p.customer.id = :customerId) AND ((((( p.endDate IS NULL) OR (p.endDate >= CURRENT_TIMESTAMP ))  AND ( p.startDate IS NULL)) OR (p.startDate <=  CURRENT_TIMESTAMP )) AND (( p.allowUnlimitedEnrollments = 1) OR (p.maxNumberSeats > p.numberSeatsUsed ))))) ")
	public List<CustomerEntitlement> getActiveCustomerEntitlementsForCustomer(@Param("customerId") Long customerId);

	public List<CustomerEntitlement> findByCustomerOrderByIdDesc(Customer customer);

	public List<CustomerEntitlement> findByCustomerAndIdNotIn(Customer customer, List<Long>ntitlmentIdsNotToGet);

	public List<CustomerEntitlement> findByNameIgnoreCase(String entitlementName);
	public CustomerEntitlement findById(Long Id);
	
	public List<CustomerEntitlement> findByCustomerAndIsSystemManagedTrue(Customer customer);
	
	@Query(value = "Select cc from CourseCustomerEntitlement cc where cc.customer.id = :customerId And isSystemManaged=true")
	public List<CustomerEntitlement> findByCourseTypeByCustomerAndIsSystemManagedTrue(@Param("customerId") Long customerId);
	
	public List<CustomerEntitlement> findByCustomerAndMaxNumberSeatsGreaterThan(Customer customer, Integer maxSeats);

	@Query(value = "Select cc.* from CUSTOMERENTITLEMENT cc, Customer c, COURSE_CUSTOMERENTITLEMENT co where co.CUSTOMERENTITLEMENT_ID=cc.ID And c.id = :cid And cc.id != :ccid And cc.CUSTOMER_ID = c.ID And (endDate = null Or ENDDATE > GETDATE()) And allowUnlimitedEnrollments=1 and (cc.SEATS - cc.NUMBERSEATSUSED) > :totalEnrollments And co.COURSE_ID in ( :courseIds )", nativeQuery = true)
	public List<CustomerEntitlement> findByCustomerEntitlement(@Param("cid") Long cid, @Param("ccid") Long ccid,  @Param("totalEnrollments")  Long totalEnrollments, @Param("courseIds") List<Long> courseIds);

	@Query(value = "Select ce from CourseCustomerEntitlementItem ccei inner join ccei.customerEntitlement ce where ccei.course.id = :courseId ")
	public List<CustomerEntitlement> getCustomerEntitlementsByCourseId(@Param("courseId") Long courseId);

}