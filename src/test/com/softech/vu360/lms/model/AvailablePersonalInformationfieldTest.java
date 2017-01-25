package com.softech.vu360.lms.model;

import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class AvailablePersonalInformationfieldTest extends
		TestBaseDAO<AvailablePersonalInformationfield> {


//	@Test
	public void AvailablePersonalInformationfield_should_save_with_AvailablePersonalInformationfieldItem() throws Exception {

		AvailablePersonalInformationfield info = new AvailablePersonalInformationfield();

		AvailablePersonalInformationfieldItem item = new AvailablePersonalInformationfieldItem();
		item.setDbColumnName("Test_DBColumnName");
		item.setDbTableName("Test_TableName");
		item.setDisplayName("Test_DisplayName");
		info.setAvailablePersonalInformationfieldItem(item);
		info.setDisplayOrder(3);
		info.setFieldsRequired(true);
		try {

			info = save(info);
		} catch (Exception ex) {
			System.out.println(info.getId());
		}

	}

	@Test
	public void AvailablePersonalInformationfield_should_update() {

		AvailablePersonalInformationfield updateRecord = getById(new Long(1405),
				AvailablePersonalInformationfield.class);
		updateRecord.setDisplayOrder(5);
		updateRecord.getAvailablePersonalInformationfieldItem().setDisplayName(
				"Test_DisplayName_Updated");
		
			updateRecord = save(updateRecord);
		
	}

}
