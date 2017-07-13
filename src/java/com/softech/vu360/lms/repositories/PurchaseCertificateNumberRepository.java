package com.softech.vu360.lms.repositories;

import com.softech.vu360.lms.model.CourseApproval;
import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;

import java.util.List;
import java.util.Set;

public interface PurchaseCertificateNumberRepository extends CrudRepository<PurchaseCertificateNumber, Long>, PurchaseCertificateNumberRepositoryCustom {
	public List<PurchaseCertificateNumber> findByCourseApprovalAndCertificateNumberIn(CourseApproval courseApproval, Set<String> purchaseCertificateNumbers);
}
