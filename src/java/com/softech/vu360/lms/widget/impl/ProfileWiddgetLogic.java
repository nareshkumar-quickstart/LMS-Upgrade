package com.softech.vu360.lms.widget.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.widget.WidgetLogic;

public class ProfileWiddgetLogic implements WidgetLogic {

	protected static final Logger log = Logger.getLogger(ProfileWiddgetLogic.class);
	private static final String profileLink = "lms_handleMenu.do?featureGroup=My Profile&feature=LMS-LRN-0003&actionUrl=lrn_learnerProfile.do";
	
	@Override
	public Collection<WidgetData> getWidgetDataList(VU360User vu360User, Map<String, Object> params, HttpServletRequest request) {
		log.debug("in getWidgetDataList vu360User.id=" + vu360User.getId() + " vu360User.username=" + vu360User.getUsername() + " params=" + params);
		List<WidgetData> datas = new ArrayList<WidgetData>();
		String filter = (String) ((params.get("filter")!=null) ? params.get("filter") : "all");
		
		if(filter.compareTo("learner")==0 || filter.compareTo("all")==0) {
			if(vu360User.getLearner()!=null && vu360User.getLearner().getLearnerProfile()!=null) {
				WidgetData widgetData = new WidgetData();
				widgetData.setId((long)datas.size());
				datas.add(widgetData);
				Map<String, Object> dataMap = widgetData.getDataMap();
				
				dataMap.put("firstName", vu360User.getFirstName());
				dataMap.put("lastName", vu360User.getLastName());
				dataMap.put("middleName", vu360User.getMiddleName());
				dataMap.put("emailAddress", vu360User.getEmailAddress());
				dataMap.put("phone", vu360User.getLearner().getLearnerProfile().getMobilePhone());
				dataMap.put("link", profileLink);
				
				if(vu360User.getLearner().getLearnerProfile().getLearnerAddress()!=null) {
					Address address = vu360User.getLearner().getLearnerProfile().getLearnerAddress();
					dataMap.put("streetAddress", address.getStreetAddress());
					dataMap.put("streetAddress2", address.getStreetAddress2());
					dataMap.put("streetAddress3", address.getStreetAddress3());
					dataMap.put("city", address.getCity());
					dataMap.put("state", address.getState());
					dataMap.put("zipCode", address.getZipcode());
					dataMap.put("country", address.getCountry());
				}
			}
		}
		
		if(filter.compareTo("customer")==0 || filter.compareTo("all")==0) {
			if(vu360User.getLearner()!=null && vu360User.getLearner().getCustomer()!=null) {
				WidgetData widgetData = new WidgetData();
				widgetData.setId((long)datas.size());
				datas.add(widgetData);
				Map<String, Object> dataMap = widgetData.getDataMap();
				
				Customer customer = vu360User.getLearner().getCustomer();
				dataMap.put("firstName", customer.getFirstName());
				dataMap.put("lastName", customer.getLastName());
				dataMap.put("emailAddress", customer.getEmail());
				dataMap.put("phone", customer.getPhoneNumber() + "-" + customer.getPhoneExtn());
				dataMap.put("link", profileLink);
				if(customer.getBillingAddress()!=null) {
					Address address = customer.getBillingAddress();
					dataMap.put("streetAddress", address.getStreetAddress());
					dataMap.put("streetAddress2", address.getStreetAddress2());
					dataMap.put("streetAddress3", address.getStreetAddress3());
					dataMap.put("city", address.getCity());
					dataMap.put("state", address.getState());
					dataMap.put("zipCode", address.getZipcode());
					dataMap.put("country", address.getCountry());
				}
			}
		}
		
		return datas;
	}

}
