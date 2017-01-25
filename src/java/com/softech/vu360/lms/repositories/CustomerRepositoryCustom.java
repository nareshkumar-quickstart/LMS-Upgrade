package com.softech.vu360.lms.repositories;


import java.util.List;
import java.util.Map;

import javax.persistence.NamedQuery;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LcmsResource;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.ResultSet;

/**
 * 
 * @author haider.ali
 * created date 02-DEC-2015
 */
public interface CustomerRepositoryCustom {

	public static final String PERCENT = "%";
	public static final String COL_NAME = "name";
	public static final String COL_ORDER_ID = "orderId";
	public static final String COL_FIRSTNAME = "firstName";
	public static final String COL_EMAIL = "email";
	public static final String COL_LASTNAME = "lastName";

	public List<Customer> findCustomersByName(String name,String orderId,VU360User loggedInUser,boolean isExact);
	//public List<Map<Object, Object>> findCustomersSimpleSearch(String name,String orderId, String orderDate, String emailAddress, VU360User loggedInUser);
	public List<Map<Object, Object>> findCustomersAdvanceSearch(String name, String operator1 ,String orderId , String operator2, String orderDate, String operator3 ,String emailAddress ,VU360User loggedInUser,boolean isExact, int startRowIndex,
			int noOfRecords, ResultSet resultSet,String sortBy, int sortDirection);
	public List<Customer> getCustomersByCurrentDistributor(String firstName, String lastName, String email, int pageIndex, int pageSize, String sortBy, int sortDirection);
	public Long getCustomersByCurrentDistributorRecordCount(String firstName, String lastName, String email, int pageIndex, int pageSize, String sortBy, int sortDirection);
	public List<Customer> findCustomersWithCustomFields(VU360User loggedInUser);
	//Added By Marium Saud -- Admin Searches 
	public List<Map<Object, Object>> findCustomersSimpleSearch(String name,String orderId, String orderDate, String emailAddress, VU360User loggedInUser,boolean isExact, int startRowIndex, int noOfRecords, ResultSet resultSet,String sortBy,int sortDirection);

}
