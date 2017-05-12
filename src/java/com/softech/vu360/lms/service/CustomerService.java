package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.LMSRoleLMSFeature;
import com.softech.vu360.lms.model.LcmsResource;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.web.controller.model.AddCustomerForm;
import com.softech.vu360.lms.web.controller.model.AdminSearchMember;
import com.softech.vu360.lms.web.controller.model.CEPlannerForm;
import com.softech.vu360.lms.web.controller.model.EditCustomerForm;

/**
 * CustomerService defines the set of interfaces 
 * to control the interactions and business logic
 * of customers within the LMS.
 * 
 * @author jason
 *
 */
public interface CustomerService {
	
	public List<Customer> findCustomersByName(String name,String orderId,VU360User loggedInUser,boolean isExact);
	//public List<Map<Object, Object>> findCustomersSimpleSearch(String name,String orderId, String orderDate, String emailAddress, VU360User loggedInUser,boolean isExact);
	//Added By Marium Saud -- Admin Searches 
	public List<Map<Object, Object>> findCustomersSimpleSearch(String name,String orderId, String orderDate, String emailAddress, VU360User loggedInUser,boolean isExact, int startRowIndex, int noOfRecords, ResultSet resultSet,String sortBy,int sortDirection);
	public List<Map<Object, Object>> findCustomersAdvanceSearch(String name, String operator1 ,String orderId , String operator2,
			String orderDate, String operator3 ,String emailAddress , VU360User loggedInUser,boolean isExact, int startRowIndex,
			int noOfRecords, ResultSet resultSet,String sortBy,int sortDirection);
	public List<Customer> findCustomersByDistributor(Distributor dist);
	public List<CustomerEntitlement> findCustomerEntitlementByCustomerId(Long customerId);

	
	public List<Customer> findCustomersWithEntitlementByDistributor(Distributor dist, String customerName, String emailAddress, int pageIndex, int retrieveRowCount, ResultSet resultSet, String sortBy,int sortDirection);
		
	public Customer getCustomerById(long id);
	public Customer loadForUpdateCustomer(long customerId);
	public Customer getCustomerByCustomerCode(String customerCode);
	public List<Customer> findCustomersByEmailAddess(String emailAddress);
	public Customer findCustomerByCustomerGUID(String custCode);
	
	public Customer addCustomer(boolean shouldAddInAD, Long userId, Distributor dist, AddCustomerForm addCustomerForm);
	public Customer addCustomer(Long userId, Distributor dist, AddCustomerForm customer);
	public Customer addCustomer(Long userId, AddCustomerForm customer);
	
	public CustomerPreferences saveCustomerPreferences(CustomerPreferences customerPreferences);
	public void deleteCustomers(long customerIdArray[]);
	public Customer saveCustomer(Customer customer) ;
	public Customer saveCustomer(Customer customer,boolean isSendProfileUpdateOnSF);
	public Customer updateCustomer(EditCustomerForm editCustomerForm, Customer customer);
	public Customer updateCustomer(Customer customer);
	public Customer getCustomerByEmail(String email);
	public Customer findByContentOwner(ContentOwner owner );
	public List<LMSRoleLMSFeature> getLMSPermissions(Distributor distributor,String roleType,LMSRole lmsRole);
	public Map<Object,Object> getCustomersByCurrentDistributor(String firstName, String lastName,
			String email, int pageIndex, int pageSize, String sortBy, int sortDirection);
	
	public Customer addCustomer(Long userId, Distributor dist, CEPlannerForm addCustomerForm);
	public CustomerPreferences loadForUpdateCustomerPreferences(long customerPreferecnesId);
    public LcmsResource loadForUpdateResource(long brandingId);
    public void savelcmsResource(LcmsResource lcmsResource);
    public List<Customer> findCustomersWithCustomFields(VU360User loggedInUser);
    
	/**
	 * Update customer lms features
	 * @param customer
	 * @param customerLMSFeatures
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public void updateLMSFeatures (
			List<CustomerLMSFeature> customerLMSFeatures
			);
	
	/**
	 * Add customer lms features
	 * @param customer
	 * @param customerLMSFeatures
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public void addLMSFeatures (
			List<CustomerLMSFeature> customerLMSFeatures
			);

	/**
	 * Delete given <code>customerLMSFeatures</code> 
	 * @param customerLMSFeatures
	 * @author sm.humayun
	 * @since 4.13 {LMS-8108}
	 */
	public void deleteCustomerLMSFeatures (
			List<CustomerLMSFeature> customerLMSFeatures);
	public boolean updateFeaturesForCustomer(Customer customer, List<LMSRoleLMSFeature> lmsPermissions );
	public CustomerLMSFeature loadCustomerLMSFeatureForUpdate(long customerLMSFeatureId);
	public Customer findDefaultCustomerByDistributor(Distributor dist);
	public boolean updateDefaultCustomer(Distributor distributorId);
	public Customer setDefaultAddressIfNull(Customer customer);
}