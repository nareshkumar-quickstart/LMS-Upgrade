package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "SUBSCRIPTION_KIT")
public class SubscriptionKit  implements SearchableKey{


	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	@Column(name = "NAME")
	private String name = null;
	
	@Column(name = "GUID")
	private String guid = null;
	
	@OneToMany
	@JoinColumn(name="SUBSCRIPTION_KIT_ID")
	private List<SubscriptionKitCourses> subscriptionKitCourses = new ArrayList<SubscriptionKitCourses>(); //No reference for set method in project
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public List<SubscriptionKitCourses> getSubscriptionKitCourses() {
		return subscriptionKitCourses;
	}

	public void setSubscriptionKitCourses(
			List<SubscriptionKitCourses> subscriptionKitCourses) {
		this.subscriptionKitCourses = subscriptionKitCourses;
	}

	@Override
	public String getKey() {
		return id.toString();
	}
}
