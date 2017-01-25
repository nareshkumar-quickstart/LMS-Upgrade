package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.RegulatorCategory;

public interface RegulatorCategoryRepositoryCustom {

	List<RegulatorCategory> findByCriteria(String categoryType, String categoryName, Long regulatorId);

	List<RegulatorCategory> findByCategoryTypeCategoryNameRegulatorId(String categoryType, String categoryName, Long regulatorId);
	
	boolean isRegulatorCategoryTypeAlreadyAssociatedWithRegulator(Long regulatorId, String regulatorCategoryType, Long excludeCategoryId);
}
