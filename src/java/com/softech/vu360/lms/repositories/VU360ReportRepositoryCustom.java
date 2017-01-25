package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Map;

public interface VU360ReportRepositoryCustom {
	
	public List<Map<Object, Object>> executeSqlString(String sqlString, List<Object> params);
	public List<Map<Object, Object>> executeSqlString(String sqlString);

}
