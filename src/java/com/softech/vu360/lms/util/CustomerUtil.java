package com.softech.vu360.lms.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;


public class CustomerUtil {

	private CustomerUtil(){
		
	}
	
	/*
	 * The method will return true if a customer is B2B Customer else it will return false
	 */
	public static boolean checkB2BCustomer(Authentication auth){
		boolean b2bCustomer = false;
		
		try{
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails  ){
				com.softech.vu360.lms.vo.Customer currentCustomer = ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentVOCustomer();
				
				/*
				Customer currentCustomer2 = ((VU360UserAuthenticationDetails)auth.getDetails()).getCurrentCustomer();
				System.out.println(currentCustomer.getId() + " - " + currentCustomer2.getId());
				System.out.println(currentCustomer.getName() + " - " + currentCustomer2.getName());
				System.out.println(currentCustomer.getCustomerType() + " - " + currentCustomer2.getCustomerType());
				if(currentCustomer.getContentOwner()!=null){
					System.out.println(currentCustomer.getContentOwner().getId() + " - " + currentCustomer2.getContentOwner().getId());
					System.out.println(currentCustomer.getContentOwner().getPlanTypeId() + " - " + currentCustomer2.getContentOwner().getPlanTypeId());
				}			
				*/
				
				if(currentCustomer.getCustomerType().equalsIgnoreCase("B2B") ){ 
					if(currentCustomer.getContentOwner()==null){
						b2bCustomer = true;
					}
					else if(currentCustomer.getContentOwner()!=null && 
							(currentCustomer.getContentOwner().getPlanTypeId()==null || 
							(currentCustomer.getContentOwner().getPlanTypeId()!=null && currentCustomer.getContentOwner().getPlanTypeId()==1))){
						b2bCustomer = true;
					}
				}
			}
		}
		catch(Exception e){
			b2bCustomer = false;
		}
		return b2bCustomer;
	}
	
	public static boolean checkB2BCustomer(Customer customer){
		boolean b2bCustomer = false;
		
		try{
			if(customer.getCustomerType().equalsIgnoreCase("B2B") ){ 
				if(customer.getContentOwner()==null){
					b2bCustomer = true;
				}
				else if(customer.getContentOwner()!=null && 
						(customer.getContentOwner().getPlanTypeId()==null || 
						(customer.getContentOwner().getPlanTypeId()!=null && customer.getContentOwner().getPlanTypeId()==1))
						){
					b2bCustomer = true;
				}
			}
		}
		catch(Exception e){
			b2bCustomer = false;
		}
		return b2bCustomer;
	}
	
	
	/*
	 * LMS-16308
	 * If User is not created through SF then it is not required to make un-necessary call to SF Web service
	 * This method will take input customer object and return true or false.
	 */
	public static boolean isUserProfileUpdateOnSF(Customer customer){
		boolean isProfileUpdateOnSF = true;
		try{
			//LMS-16308: Customer is not created through SF and hence not required to send request on SF
			if(customer.getDistributor()==null || (customer.getDistributor()!=null 
					&& (customer.getDistributor().getDistributorCode()==null || StringUtils.isBlank(customer.getDistributor().getDistributorCode()) ))){
				isProfileUpdateOnSF = false;
			}
		}
		catch(Exception e){
			isProfileUpdateOnSF = true;
		}
		return isProfileUpdateOnSF;
	}
	
} 