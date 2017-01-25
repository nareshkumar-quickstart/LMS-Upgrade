package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author raja.ali
 * Date: 2015/11/18
 *
 */

@Entity
@Table(name = "LEARNERGROUP")
public class LearnerGroup implements ILMSBaseInterface{
	
	private static final long serialVersionUID = -965493718316683169L;
	
	@Id
	@javax.persistence.TableGenerator(name = "LEARNERGROUP_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "LEARNERGROUP", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "LEARNERGROUP_ID")
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "NAME")
	private String name = null;
	
	@OneToOne
	@JoinColumn(name="ORGANIZATIONALGROUP_ID")
	private OrganizationalGroup organizationalGroup = null;
	
	@OneToOne
	@JoinColumn(name="CUSTOMER_ID")
	private Customer customer = null;
	
	@OneToMany(mappedBy = "learnerGroup", cascade={CascadeType.MERGE, CascadeType.REMOVE} )
	private List<LearnerGroupItem> learnerGroupItems = new ArrayList<LearnerGroupItem>();

	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the organizationalGroup
	 */
	public OrganizationalGroup getOrganizationalGroup() {
		return organizationalGroup;
	}

	/**
	 * @param organizationalGroup
	 *            the organizationalGroup to set
	 */
	public void setOrganizationalGroup(OrganizationalGroup organizationalGroup) {
		this.organizationalGroup = organizationalGroup;
	}


	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<LearnerGroupItem> getLearnerGroupItems() {
		return learnerGroupItems;
	}

	public void setLearnerGroupItems(List<LearnerGroupItem> learnerGroupItems) {
		this.learnerGroupItems = learnerGroupItems;
	}
	
	public void addLearnerGroupItem(Course course, CourseGroup courseGroup) {
		LearnerGroupItem item = new LearnerGroupItem();
		item.setCourse(course);
		item.setCourseGroup(courseGroup);
		item.setLearnerGroup(this);
		if (!learnerGroupItems.contains(item)) {
			learnerGroupItems.add(item);
		}
	}
	
	public LearnerGroupItem findLearnerGroupItem(Long courseGroupId, Long courseId){
		
		LearnerGroupItem learnerGroupItem = null;		
		if(this.learnerGroupItems   !=null){
			for(LearnerGroupItem item : this.learnerGroupItems){
				if(item.getCourseGroup() == null ){//for Custom Courses
					if(item.getCourse().getId().equals(courseId)){
						learnerGroupItem=item;
						break;
					}
				}
				else{//for other courses which are associated with course groups
					if(item.getCourse().getId().equals(courseId) && item.getCourseGroup().getId().equals(courseGroupId)){					
						learnerGroupItem=item;
						break;
					}
				}
			}
		}		
		return learnerGroupItem;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public  boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LearnerGroup other = (LearnerGroup) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
