/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.BusinessObjectSequence;

/**
 * @author marium.saud
 *
 */
public class BusinessObjectSequenceRepositoryImpl implements BusinessObjectSequenceRepositoryCustom {
	
	private static final Logger log = Logger.getLogger( BusinessObjectSequenceRepositoryImpl.class.getName() );

	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	public synchronized String getNextBusinessObjectSequence(String businessObjectName) {
		
		log.debug( "Repository: Business Object Sequence Name: " + businessObjectName );
		
		String result = null;
		
		try {
		Query query = entityManager.createNamedQuery("BusinessObjectSequence.findBusinessObjectSequenceByName");
		query.setParameter("name", businessObjectName);
		BusinessObjectSequence businessObject = (BusinessObjectSequence) query.getSingleResult();
		
		if (businessObject != null) {
			
			entityManager.refresh(businessObject, LockModeType.PESSIMISTIC_WRITE);
			
			businessObject.setNextSequence(businessObject.getNextSequence()+1);
			NumberFormat formatter = new DecimalFormat( businessObject.getNumberFormat() );				
			result = businessObject.getSequenceRoot() + formatter.format( businessObject.getNextSequence() );
			
		}
		
		}
		catch (Exception e) {			
			log.debug(e);
		}
		return result;
	}

}
