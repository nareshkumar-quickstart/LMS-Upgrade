package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;

public interface PurchaseCertificateNumberRepository extends CrudRepository<PurchaseCertificateNumber, Long>, PurchaseCertificateNumberRepositoryCustom {
	
}
