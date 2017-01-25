package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.CertificateBookmarkAssociation;
import com.softech.vu360.util.PdfFieldsEnum;

public interface CertificateBookmarkAssociationRepositoryCustom {
	List<CertificateBookmarkAssociation> findByFieldsType(PdfFieldsEnum fieldType);
}
