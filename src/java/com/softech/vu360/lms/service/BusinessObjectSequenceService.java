/**
 * 
 */
package com.softech.vu360.lms.service;

/**
 * @author sana.majeed
 * The Service Interface to call out to the DAO (through its implementation class)
 * and read the the sequence information from the table via the descriptor
 * for given BusinessObjectName
 * 
 * Implemented for: [10/27/2010] LMS-6389 :: Business Object Sequencing for OSHA Certificates
 *
 */
public interface BusinessObjectSequenceService {
	
	public String getNextBusinessObjectSequence( String businessObjectName );
	
}
