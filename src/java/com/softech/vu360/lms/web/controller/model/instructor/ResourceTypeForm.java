package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.ResourceType;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Saptarshi
 */
public class ResourceTypeForm implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	//for manage resource type.
	private List<ResourceType> resourceTypes = new ArrayList<ResourceType>();
	private long id = 0;
	
	//for add resource type.
	private ResourceType resourceType = new ResourceType();
	
	

	/**
	 * @return the resourceTypes
	 */
	public List<ResourceType> getResourceTypes() {
		return resourceTypes;
	}

	/**
	 * @param resourceTypes the resourceTypes to set
	 */
	public void setResourceTypes(List<ResourceType> resourceTypes) {
		this.resourceTypes = resourceTypes;
	}

	/**
	 * @return the resourceType
	 */
	public ResourceType getResourceType() {
		return resourceType;
	}

	/**
	 * @param resourceType the resourceType to set
	 */
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	
}
