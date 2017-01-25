/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CreditReportingFieldValueChoice;

/**
 * @author marium.saud
 *
 */
public interface CreditReportingFieldValueChoiceRepository extends CrudRepository<CreditReportingFieldValueChoice, Long> {
	
	//public List<CreditReportingFieldValueChoice>   getChoicesByCreditReportingField(CreditReportingField creditReportingField);
	public List<CreditReportingFieldValueChoice> findByCreditReportingFieldId(Long creditReportingFieldID);
	public List<CreditReportingFieldValueChoice> findByCreditReportingFieldIdOrderByDisplayOrderAsc(Long creditReportingFieldID);
	
}
