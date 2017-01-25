package com.softech.vu360.lms.service;

import java.util.List;

import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.VU360User;

public interface CustomFieldService {

	public CustomField save(CustomField field);
	public List<CustomField> save(List<CustomField> customField) ;
	public void deleteDistributorCustomFields(Distributor distributor,Long[] customFields);
	public void deleteCustomerCustomFields(Customer customer,Long[] customFields);
	public List<CustomFieldValueChoice> getOptionsByCustomField(CustomField customfield);
	public List<CustomField> getUsedCustomFieldsList(Long[] customFiledsIdList);
	public List<CustomField> getUnUsedCustomFields(Long[] customFiledsIdList , List<CustomField> cfList);
	public CustomFieldValueChoice saveOption(CustomFieldValueChoice option);
	public void deleteCustomerCustomFieldsRelation(Customer customer,Long[] idsToDelete);
	public CustomField  loadForUpdateCustomField(Long id);
	public List<CustomField>  loadForUpdateCustomFields(List<CustomField> l);
	public void createValueRecordForStaticReportingField(VU360User vu360User,	List<CreditReportingField> customFieldList,
			List<CreditReportingFieldValue> customFieldValueList); 
    public boolean customFieldHasValue(CustomField customField);

	
}
