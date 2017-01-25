package com.softech.vu360.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


/**
 * @author Raja Wajahat Ali
 * @created on Jun 18 2015
 * @Modifid by muhammad.saleem
 */
@Entity
@DiscriminatorValue("Subscription")
public class SubscriptionCustomerEntitlement extends CustomerEntitlement {

	private static final long serialVersionUID = 1L;
	private static final String ENROLLMENT_TYPE="Subscription";
	
	public String getEnrollmentType() {
		return ENROLLMENT_TYPE;
	}

}