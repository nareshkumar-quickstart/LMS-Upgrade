package com.softech.vu360.lms.service.impl;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.UpdateProfileService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.webservice.message.lcms.UpdateProfileRequest;


public class UpdateProfileServiceImpl implements UpdateProfileService{

	private static final Logger log = Logger.getLogger(UpdateProfileServiceImpl.class.getName());
	private VU360UserService vu360UserService = null;
	@Override
	public boolean updateProfile(UpdateProfileRequest request) {
		// TODO Auto-generated method stub
		
		com.softech.vu360.lms.webservice.message.lcms.VU360User user=request.getUser();
		String userName=user.getUserName();
		VU360User vu360User=vu360UserService.findUserByUserName(userName);
		boolean result=false;
		if(vu360User!=null)
		{
			vu360User=vu360UserService.loadForUpdateVU360User(vu360User.getId());
			vu360User.setFirstName(user.getFirstName());
			vu360User.setMiddleName(user.getMiddleName());
			vu360User.setLastName(user.getLastName());
			vu360User.setEmailAddress(user.getEmailAddress());
			com.softech.vu360.lms.webservice.message.lcms.LearnerProfile profile=user.getLearnerProfile();
			LearnerProfile lp=vu360User.getLearner().getLearnerProfile();
			lp.setMobilePhone(profile.getMobilePhone());
			lp.setOfficePhone(profile.getOfficePhone());
			lp.setOfficePhoneExtn(profile.getOfficePhoneExt());

			com.softech.vu360.lms.webservice.message.lcms.Address billingAddress=profile.getBillingAddress();
			if(billingAddress!=null)
			{
				log.debug("billingAddress is not null");
				Address address=lp.getLearnerAddress();
				if(address==null)
				{
					address=new Address();
					log.debug("billing address was null in learner profile["+address+"]");
				}
				address.setStreetAddress(billingAddress.getAddressLine1());
				address.setStreetAddress2(billingAddress.getAddressLine2());
				address.setStreetAddress3(billingAddress.getAddressLine3());
				address.setCity(billingAddress.getCity());
				address.setState(billingAddress.getState());
				address.setCountry(billingAddress.getCountry());
				address.setZipcode(billingAddress.getZipCode());
				lp.setLearnerAddress(address);
			}
			
			com.softech.vu360.lms.webservice.message.lcms.Address shippingAddress=profile.getShippingAddress();
			if(shippingAddress!=null)
			{
				log.debug("shippingAddress is not null");
				Address address=lp.getLearnerAddress2();
				if(address==null)
				{
					address=new Address();
					log.debug("shipping address was null in learner profile["+address+"]");
				}
				address.setStreetAddress(shippingAddress.getAddressLine1());
				address.setStreetAddress2(shippingAddress.getAddressLine2());
				address.setStreetAddress3(shippingAddress.getAddressLine3());
				address.setCity(shippingAddress.getCity());
				address.setState(shippingAddress.getState());
				address.setCountry(shippingAddress.getCountry());
				address.setZipcode(shippingAddress.getZipCode());
				lp.setLearnerAddress(address);
			}
			try
			{
				vu360UserService.updateUser(vu360User.getId(), vu360User);
				result=true;
			}catch(Exception e)
			{
				log.error("exception occured in updateUser ["+e.getMessage()+"]");
				result=false;
			}
		}
		else
		{
			log.debug("No user found in LMS against username ["+request.getUser().getUserName()+"]");
			result=false;
		}
		return result;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

}
