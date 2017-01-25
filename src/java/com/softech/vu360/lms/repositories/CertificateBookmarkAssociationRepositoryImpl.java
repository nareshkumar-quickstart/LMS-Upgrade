/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.softech.vu360.lms.model.CertificateBookmarkAssociation;
import com.softech.vu360.util.PdfFieldsEnum;


public class CertificateBookmarkAssociationRepositoryImpl implements CertificateBookmarkAssociationRepositoryCustom {
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public List<CertificateBookmarkAssociation> findByFieldsType(PdfFieldsEnum fieldType) {

		StringBuilder queryString = new StringBuilder("SELECT p FROM  CertificateBookmarkAssociation p ");
		
		switch(fieldType)
		{
			case LMSFIELDS:
				queryString.append(" Where LMSFieldName is NOT NULL ");
				break;
			case REPORTINGFIELDS:
				queryString.append(" Where reportingField is NOT NULL ");
				break;
			case CUSTOMFIELDS:
				queryString.append(" Where customField is NOT NULL ");
				break;
			default:
				break;
				
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		
		
		List<CertificateBookmarkAssociation> listCertificateBookmarkAssociation = query.getResultList(); 
		
		return listCertificateBookmarkAssociation;
	}



}
