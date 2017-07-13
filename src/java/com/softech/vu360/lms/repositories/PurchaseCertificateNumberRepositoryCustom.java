package com.softech.vu360.lms.repositories;

import com.softech.vu360.lms.model.PurchaseCertificateNumber;

import java.util.Set;

public interface PurchaseCertificateNumberRepositoryCustom {
	public PurchaseCertificateNumber getUnusedPurchaseCertificateNumber(Long courseApprovalId);
	public boolean savePurchaseNumberAsBatch(Set<PurchaseCertificateNumber> purchaseCertificateNumbers);
}
