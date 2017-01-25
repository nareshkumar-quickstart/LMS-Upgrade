package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CustomerOrder;

public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long> {
	CustomerOrder findByOrderGUID(String guid);
	List<CustomerOrder> findByCustomerId(Long customerId);
}
