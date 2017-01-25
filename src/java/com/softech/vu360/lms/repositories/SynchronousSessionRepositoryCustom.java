package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.vo.SynchrounousSessionVO;

public interface SynchronousSessionRepositoryCustom {

	public List<SynchronousSession> findSynchronousSessionByClassId(
			Long synchClassId, Date startDateTime, Date endDateTime);

	public Map<Object, Object> getSynchronousSessionsByClassId(
			Long synchClassId, int pageIndex, int pageSize, String sortBy,
			int sortDirection);

	public Date getLastSessionStartDate(Long classId);

	public List<SynchronousSession> getSynchronousSessionsByClassId(
			Long synchClassId);

	public void deleteSynchronousSessions(Long[] sessionIdArray);

	public SynchrounousSessionVO getMinMaxScheduleDateForSynchronousClass(
			Long classId);

	public SynchronousSession addSynchronousClassSession(
			SynchronousSession synchSession);
}
