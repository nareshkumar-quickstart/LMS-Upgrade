/**
 * 
 */
package com.softech.vu360.lms.service.impl;

// Java Imports
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.repositories.BusinessObjectSequenceRepository;
import com.softech.vu360.lms.service.BusinessObjectSequenceService;
// Application Imports

/**
 * @author sana.majeed
 * The Service Implementation class to call out to the DAO
 * and read the the sequence information from the table via the descriptor
 * for given BusinessObjectName
 * 
 * Implemented for: [10/27/2010] LMS-6389 :: Business Object Sequencing for OSHA Certificates
 *
 */
public class BusinessObjectSequenceServiceImpl implements BusinessObjectSequenceService {
	
	private static final Logger log = Logger.getLogger( BusinessObjectSequenceServiceImpl.class.getName() );
	@Inject
	private BusinessObjectSequenceRepository businessObjectSequenceRepository;

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.BusinessObjectSequenceService#getNextBusinessObjectSequence(java.lang.String)
	 */
	@Override
	public String getNextBusinessObjectSequence(String businessObjectName) {
		
		log.debug( "Business Object Name: " + businessObjectName );		
		return this.businessObjectSequenceRepository.getNextBusinessObjectSequence( businessObjectName );		
	}

}
