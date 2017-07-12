package com.softech.vu360.lms.repositories;

import com.softech.vu360.lms.model.CourseApproval;
import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;

public interface PurchaseCertificateNumberRepository extends CrudRepository<PurchaseCertificateNumber, Long>, PurchaseCertificateNumberRepositoryCustom {
	public PurchaseCertificateNumber findOneByCourseApprovalAndCertificateNumber(CourseApproval courseApproval, String certificateNumber);
}
