package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.CustomerPreferences;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LcmsResource;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.util.ResultSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CustomerRepositoryTest {

	public CustomerService customerService;
	
	
	@Inject
	private CustomerRepository customerRepository;
	
	@Inject
	CustomerLMSFeatureRepository customerLMSFeatureRepository;
	
	@Inject
	DistributorRepository distributorRepository;

	@Inject
	VU360UserRepository vU360UserRepository;

	@Inject
	ContentOwnerRepository contentOwnerRepository;
	
	
	 @Autowired 
     ApplicationContext context;
	
	@Before
	public void CustomerRepositoryInit(){
	       customerService = (CustomerService) context.getBean("customerService");
    }

	
	//@Test
	//@Transactional DONE
	public void findByVu360UserIds() {
		VU360User u = vU360UserRepository.findOne(new Long(3));
		List ls = customerService.findCustomersByName("Asma", null, u, false);
		System.out.println("Recieved."+ls.size()+"");
	}

	@Test
	@Transactional
	public void findCustomersSimpleSearch() {
		
		VU360User u = vU360UserRepository.findOne(new Long(3));
		List<Map<Object, Object>>  d = customerService.findCustomersSimpleSearch("abc", "", "", "@", u, false,0,10,new ResultSet(),null,0);
		System.out.print(d);
	}

	//DONE
	//@Test
	//@Transactional
	public void findCustomersAdvanceSearch() {
		List ls = customerService.findCustomersAdvanceSearch("abc", "", "", null, "", "", "", null, false,0,10,new ResultSet(),null,0);
		
	}

	//@Test Done
	public void saveCustomer() {
		Customer c = new Customer();
		c = customerService.saveCustomer(c);
	}

	//@Test Done
	public void updateCustomer() {
		Customer c = new Customer();
		c = customerService.updateCustomer(c);
	}
	
	//@Test  Done
	public void getCustomerByContentOwner() {
		ContentOwner contentOwner=contentOwnerRepository.findFirstByGuidOrderByIdAsc("40d78332-de10-4193-8828-d1e4b04d6af0");
		Customer c = customerService.findByContentOwner(contentOwner);
		System.out.println(c);
	}

	//@Test Done
	public void findCustomerByCustomerGUID() {
		Customer c = customerService.findCustomerByCustomerGUID("dcda5167-8af6-48d3-99cf-4337d19d378a");
	}
	
	//@Test Done
	public void findCustomerbyCustomerCode() {
		String customerCode = "VUCUS-60919";
		customerService.getCustomerByCustomerCode(customerCode);
	}
	
	//@Test Done
	public void findCustomersByDistributor(){
		Distributor distributor =new Distributor();
		distributor.setId(new Long(30356));
		List s = customerService.findCustomersByDistributor(distributor);
		System.out.println(s);
	}
	

	//@Test done
	public void findCustomerEntitlementByCustomerId(){
		List<CustomerEntitlement> ls =  customerService.findCustomerEntitlementByCustomerId(new Long(60919));
	}

	//@Test  done
	public void findDefaultCustomerByDistributor(){

		Distributor distributor =new Distributor();
		distributor.setId(new Long(30356));
		Customer c =  customerService.findDefaultCustomerByDistributor(distributor);
		System.out.println(c);
	}

	//@Test
	public void findCustomerbyEmail() {
		String email = "haider.ali@360training.com";
		Customer customer = customerService.getCustomerByEmail(email);
		System.out.print(customer);
	}
	
	//@Test
	public void deleteCustomer() {
		long[] l1 = new long[]{1};
		customerService.deleteCustomers(l1);
	}
	
	//@Test
	public void getCustomersByCurrentDistributor() {
		Map map = customerService.getCustomersByCurrentDistributor("haider","haider","haider@asdf.com",1,5,"lastName",1);
		System.out.println(map);
		
	}
	
	//@Test
	public void loadForUpdateCustomerPreferences() {
		CustomerPreferences c = customerService.loadForUpdateCustomerPreferences(new Long(53));
		System.out.println(c);
		
	}
	
	//@Test
	public void loadCustomerLMSFeatureForUpdate() {
		CustomerLMSFeature lcms = customerService.loadCustomerLMSFeatureForUpdate(new Long(53));
		System.out.println(lcms);
			
	}
	//@Test
	public void loadForUpdateResource() {
		LcmsResource lcms = customerService.loadForUpdateResource(new Long(5));
		System.out.println(lcms);
			
	}
		

	//@Test
	public void saveCustomerLMSFeature() {

		Customer customer = new Customer();
		customer.setId(new Long(21354));
		List<CustomerLMSFeature> ls = customerLMSFeatureRepository.findByCustomer(customer);
		customerService.addLMSFeatures(ls);
		customerService.deleteCustomerLMSFeatures(ls);
			
	}
	
	//@Test
	public void updateDefaultCustomer() {

		Distributor distributor = distributorRepository.findOne(new Long(2253));
		Boolean b = customerService.updateDefaultCustomer(distributor);
		System.out.print("ddddddddddddddd"+b);
		
		
	}
	
	//@Test
	//@Transactional
	public void findCustomersWithCustomFields() {

		VU360User u = vU360UserRepository.findOne(new Long(1220));
		List<Customer> c = customerService.findCustomersWithCustomFields(u);
		System.out.println(c);
		
		
	}
	
	
	
	
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	
		
	
}
