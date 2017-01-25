package com.softech.vu360.lms.repositories;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;

public interface PurchaseCertificateNumberRepositoryCustom {
	public PurchaseCertificateNumber getUnusedPurchaseCertificateNumber(Long courseApprovalId);
}
