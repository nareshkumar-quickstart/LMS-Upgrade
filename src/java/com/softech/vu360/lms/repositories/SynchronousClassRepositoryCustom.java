package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

public interface SynchronousClassRepositoryCustom {

	public Map<Object, Object> findSynchClassesByCourseId(Long courseId,
			String sectionName, String startDate, String endDate,
			int pageIndex, int pageSize, String sortBy, int sortDirection,
			int maxLimit);

	public Map<Object, Object> findAllSynchClassesByCourseId(Long courseId,
			String sectionName, String startDate, String endDate,
			String sortBy, int sortDirection, int maxLimit);
	
	public void deleteByIdIn(List<Long> ids );
	
}
