package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.Certificate;

public interface CertificateRepositoryCustom {
	public List<Certificate> getCertificatesWhereAssetVersionIsEmpty(Long fromCertificateId, Long toCertificateId);
}
