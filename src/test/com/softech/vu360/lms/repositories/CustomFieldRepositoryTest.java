package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.util.CustomFieldEntityType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class CustomFieldRepositoryTest {

	@Inject
	private CustomFieldRepository customFieldRepository;
	@Inject
	private CustomFieldValueRepository customFieldValueRepository;
	
	//@Test
	public void customFields_should_find_by_customFieldEntityType_And_fieldType_And_name() {

		String fieldType = "";
		String name = "";
		
		List<CustomField> findCustomFields = customFieldRepository.findCustomFieldByCustomFieldEntityTypeAndFieldTypeAndName(Enum.valueOf(CustomFieldEntityType.class, "CUSTOMFIELD_COURSEAPPROVAL"), fieldType, name);
		System.out.println("..........");
	}

	//@Test
	public void Save() {

		CustomField cf = customFieldRepository.findOne(new Long(10));
		cf.setId(null);
		cf.getCustomFieldContext().setId(null);
		customFieldRepository.save(cf);
		System.out.println("..........");
	}

	//@Test
	public void deleteWithOption() {
		Long s[] = new Long[3];
		s[0]= 14254L;
		s[1]= 14254L;
		s[2]= 14254L;
		
		customFieldRepository.deleteCustomFields(s);
		System.out.println("..........");
		
	}

	//@Test
	public void delete() {

		CustomField cf = customFieldRepository.findOne(new Long(10));
		cf.setId(null);
		cf.getCustomFieldContext().setId(null);
		cf = customFieldRepository.save(cf);

		customFieldRepository.delete(cf);
		System.out.println("..........");
		
	}

	//@Test
	public void getCustomFieldIds() {

		Long s[] = new Long[3];
		s[0]= 14254L;
		s[1]= 14254L;
		s[2]= 14254L;

		List ls = customFieldRepository.findByIdIn(Arrays.asList(s));
		System.out.println("..........");
		
	}
	
	//@Test
	public void customFieldHasData() {

		CustomField cs = customFieldRepository.findOne(new Long(20L));
		Long s = customFieldValueRepository.countByCustomField(cs);
		
		System.out.println(".........."+s);
		
	}
	@Test
	public void customFields_should_compare_by_customFieldEntityType_And_fieldType() {

		String fieldType = "";
		String name = "Gender";
		
		Boolean findCustomFields = customFieldRepository.isUniqueCustomFieldName(fieldType, name);
		System.out.println("..........");
	}

}
