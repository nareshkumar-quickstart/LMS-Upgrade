package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.CertificateBookmarkAssociation;

public interface CertificateBookmarkAssociationRepository extends CrudRepository<CertificateBookmarkAssociation, Long>, CertificateBookmarkAssociationRepositoryCustom {
	List<CertificateBookmarkAssociation> findByBookmarkLabelLikeIgnoreCase(String bookmarkLabel);
	List<CertificateBookmarkAssociation> findByreportingFieldIdIn(Long[] reportingFields);
	
	
	

}
