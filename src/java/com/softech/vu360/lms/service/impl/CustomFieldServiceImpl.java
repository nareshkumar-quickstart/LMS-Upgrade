package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.StaticCreditReportingField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.CustomFieldRepository;
import com.softech.vu360.lms.repositories.CustomFieldValueChoiceRepository;
import com.softech.vu360.lms.repositories.CustomFieldValueRepository;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingFieldBuilder;

public class CustomFieldServiceImpl implements CustomFieldService {

	@Inject
	CustomFieldRepository customFieldRepository;
	@Inject
	CustomFieldValueRepository customFieldValueRepository;
	@Inject
	CustomFieldValueChoiceRepository customFieldValueChoiceRepository;
	
	DistributorService distributorService;
	CustomerService customerService;
	LearnerService learnerService;
	
	/*
	 * delete CustomFields associated with given Distributor
	 * first it will delete the association by removing customfields from customer and then
	 * individually delete the fields
	 */
	@Transactional
	public void deleteDistributorCustomFields(Distributor distributor,Long[] idsToDelete)
	{
		if(idsToDelete!=null)
		{
			List<CustomField> customFields=distributor.getCustomFields();
			customFields=removeCustomFields(customFields, idsToDelete);
			distributor.setCustomFields(customFields);
			distributorService.saveDistributor(distributor);
			customFieldRepository.deleteCustomFields(idsToDelete);
		}
	}
	public  CustomField  loadForUpdateCustomField(Long id){
		return customFieldRepository.findOne(id);
	}

	public List<CustomField>  loadForUpdateCustomFields(List<CustomField> l){
		
		//get id from collection.
		Collection<Long> cfids = Collections2.transform(l, new Function<CustomField, Long>() {
				public Long apply(CustomField arg0) {
					return Long.parseLong(arg0.getId().toString());
				}
			}
		);
		
		return customFieldRepository.findByIdIn( new ArrayList<Long>(cfids));
	}
	
	/*
	 * delete CustomFields associated with given customer
	 */
	public void deleteCustomerCustomFieldsRelation(Customer customer,Long[] idsToDelete)
	{
		if(idsToDelete!=null)
		{
			List<CustomField> customFields=customer.getCustomFields();
			customFields=removeCustomFields(customFields, idsToDelete);
			customer.setCustomFields(customFields);
			customerService.updateCustomer(customer);
		}
	}
	
	
	/*
	 * delete CustomFields associated with given customer
	 * first it will delete the association by removing customfields from customer and then
	 * individually delete the fields
	 */
	public void deleteCustomerCustomFields(Customer customer,Long[] idsToDelete)
	{
		if(idsToDelete!=null)
		{
			List<CustomField> customFields=customer.getCustomFields();
			customFields=removeCustomFields(customFields, idsToDelete);
			customer.setCustomFields(customFields);
			
			// [2/23/2011] LMS-9110 :: Administrator Mode > Global Error on deleting Customers: Custom Fields
			Customer newCustomer = customerService.loadForUpdateCustomer(customer.getId());
			newCustomer.setCustomFields(customFields);			
			customer = customerService.updateCustomer(newCustomer);
			
			customFieldRepository.deleteCustomFields(idsToDelete);
		}
	}
	/*
	 * utility method that will match the idsToRemove in the list and remove from collection
	 * since its pass by reference therefore no need to return the list
	 */
	public List<CustomField> removeCustomFields(List<CustomField> fields,Long[] idsToRemove)
	{
		for(int i=0;i<idsToRemove.length;i++){
			for(CustomField field:fields)
			{
				if(field.getId().longValue()==idsToRemove[i])
				{
					fields.remove(field);
					break;
				}
			}
		}
		return fields;
	}
	
	public List<CustomField> getUsedCustomFieldsList(Long[] customFiledsIds){
		List<CustomFieldValue> customFieldValueList  = (List<CustomFieldValue>) customFieldValueRepository.findByCustomFieldIdIn(customFiledsIds);
		List<CustomField> customFieldList = new ArrayList<CustomField>();
		
		if( customFieldValueList  == null){
			return null;	
		}
		else{
			for( CustomFieldValue cfv:  customFieldValueList){
				customFieldList.add( cfv.getCustomField() );
			}
		}
		
		return customFieldList;
	}

	public List<CustomField> getUnUsedCustomFields(Long[] customFiledsIdList , List<CustomField> usedCustomFields){
		
		List<Long> unUsedcustomFiledsList = new ArrayList<Long>();
		List<CustomField> unUsedCustomFields = null;
		
		boolean unUsed= false;
		for(int index = 0 ; index < customFiledsIdList.length ; index++){
			unUsed = true;
			for(CustomField cf : usedCustomFields){
				if( cf.getId().longValue() == customFiledsIdList[index]) { // already a used cf
					unUsed = false;
					break; 
				}
			}
			if(unUsed){
				unUsedcustomFiledsList.add(customFiledsIdList[index]);
			}
		}
		unUsedCustomFields = customFieldRepository.findByIdIn( unUsedcustomFiledsList );
		return unUsedCustomFields;
	}
	
	
	/**
	 * This method is written to help in printing certificates when the course is no longer connected to static reporting field.
	 * @param vu360User
	 * @param customFieldList
	 * @param customFieldValueList
	 */
    public void createValueRecordForStaticReportingField(VU360User vu360User,
			List<CreditReportingField> customFieldList,
			List<CreditReportingFieldValue> customFieldValueList) {
		CreditReportingFieldBuilder staticFieldsBuilder = new CreditReportingFieldBuilder();
		for (CreditReportingField creditReportingField : customFieldList) {				
			if(creditReportingField instanceof StaticCreditReportingField && learnerService.getCreditReportingFieldValueById(creditReportingField.getId()) == null)
			staticFieldsBuilder.buildCreditReportingField(creditReportingField, 0, customFieldValueList);				
		}		    		    
		/*save creditreportingfieldvalue record for static field*/
		for(com.softech.vu360.lms.web.controller.model.creditreportingfield.CreditReportingField field : staticFieldsBuilder.getCreditReportingFieldList()) { 
		 if(field.getCreditReportingFieldRef() instanceof StaticCreditReportingField) {		        
			 CreditReportingFieldValue creditReportingFieldValue = field.getCreditReportingFieldValueRef();
		    creditReportingFieldValue.setReportingCustomField(field.getCreditReportingFieldRef());
		    creditReportingFieldValue.setLearnerprofile(vu360User.getLearner().getLearnerProfile());		        		        
		    learnerService.saveCreditReportingfieldValue(creditReportingFieldValue);
		  }		     
		}
	}
		
    public boolean customFieldHasValue(CustomField customField){
		CustomField cs = customFieldRepository.findOne(new Long(20L));
		Long s = customFieldValueRepository.countByCustomField(cs);
		if(s!=null && s.intValue()>0)
			return true;
		return false;
    }

	
	public CustomFieldValueChoice saveOption(CustomFieldValueChoice option){
		return customFieldValueChoiceRepository.save(option);
	}

		
	public List<CustomFieldValueChoice> getOptionsByCustomField(
			CustomField customfield) {
		return customFieldValueChoiceRepository.findByCustomFieldOrderByDisplayOrderAsc(customfield);
	}
	
	public CustomField save(CustomField customField) {
		return customFieldRepository.save(customField);
	}

	public List<CustomField> save(List<CustomField> customField) {
		return (List<CustomField>) customFieldRepository.save(customField);
	}

	
	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	

}
