package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class LocationTest extends
		TestBaseDAO<Location> {
	

	@Before
	public void setRequiredService() {

	}

//	@Test
	public void Location_should_save_with_Address() throws Exception {
		
		Location location = new Location();
		location.setName("Test_Location");
		
		Address address = new Address();
		address.setStreetAddress("Test_Street_Address1");
		address.setStreetAddress2("Test_Street_Address2");
		address.setCity("Test_City");
		address.setCountry("Test_Country");
		address.setState("Test_State");
		address.setZipcode("Test_Zipcode");
		location.setAddress(address);
		
		location.setPhone("2354434");
		location.setDescription("Test_Description");
		
		ContentOwner owner=(ContentOwner)crudFindById(ContentOwner.class, new Long(1));
		location.setContentowner(owner);
		
		List<CustomField> field_list=new ArrayList<CustomField>();
		CustomField field=(MultipleLineTextCustomfield)crudFindById(CustomField.class, new Long(4));
		field_list.add(field);
		location.setCustomfields(field_list);
		
		List<CustomFieldValue> value_List=new ArrayList<CustomFieldValue>();
		CustomFieldValue value=(CustomFieldValue)crudFindById(CustomFieldValue.class, new Long(4));
		value_List.add(value);
		location.setCustomfieldValues(value_List);
		
			
		try {

			location=(Location)crudSave(Location.class, location);
			System.out.println(location.getId());
		} catch (Exception ex) {
			System.out.println(location.getId());
		}

	}
	
	@Test
	public void Location_should_update(){
		
		Location location=getById(new Long(2093), Location.class);
		location.setName("Test_Location_Updated");
		location.setCustomfields(null);
		location.setCustomfieldValues(null);
		save(location);
	}


}
