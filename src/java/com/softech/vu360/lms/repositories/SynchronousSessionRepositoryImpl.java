package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.vo.SynchrounousSessionVO;

public class SynchronousSessionRepositoryImpl implements
		SynchronousSessionRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<SynchronousSession> findSynchronousSessionByClassId(
			Long synchClassId, Date startDateTime, Date endDateTime) {
		List<SynchronousSession> syncList = null;
		StringBuilder jpq = new StringBuilder("SELECT p FROM SynchronousSession p WHERE p.synchronousClass.id = :syncClassId");
		if (startDateTime != null) {
			jpq.append(" AND p.startDateTime = :startDate");
		}
		if (endDateTime != null) {
			jpq.append(" AND p.endDateTime = :endDate");
		}

		Query query = entityManager.createQuery(jpq.toString());
		query.setParameter("syncClassId", synchClassId);
		if (startDateTime != null) {
			query.setParameter("startDate", startDateTime);

		}
		if (endDateTime != null) {
			query.setParameter("endDate", endDateTime);
		}
		syncList = query.getResultList();

		return syncList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, Object> getSynchronousSessionsByClassId(
			Long synchClassId, int pageIndex, int pageSize, String sortBy,
			int sortDirection) {
		List<SynchronousSession> syncList = null;
		StringBuilder jpq = new StringBuilder(
				"SELECT p FROM SynchronousSession p WHERE p.synchronousClass.id = :syncClassId");
		if (sortBy != null && sortBy.length() > 0)
			jpq.append(" ORDER BY ").append("p.").append(sortBy);
		if (sortDirection == 0)
			jpq.append(" ASC");
		else {
			jpq.append(" DESC");
		}
		Query query = entityManager.createQuery(jpq.toString());
		query.setParameter("syncClassId", synchClassId);
		query.setFirstResult(pageIndex);
		query.setMaxResults(pageSize);
		syncList = query.getResultList();
		int recordSize = 0;
		if (syncList != null) {
			recordSize = syncList.size();
		}

		Map<Object, Object> resultMap = new HashMap<Object, Object>();
		resultMap.put("totalRecords", recordSize);
		resultMap.put("synchronousSessionList", syncList);
		return resultMap;
	}

	@Override
	public Date getLastSessionStartDate(Long classId) {
		Date dt = null;
		Object session = null;
		StringBuilder jpq = new StringBuilder(
				"SELECT max(p.startDateTime) FROM SynchronousSession p WHERE p.synchronousClass.id = :syncClassId");
		Query query = entityManager.createQuery(jpq.toString());
		query.setParameter("syncClassId", classId);

		session = (Object) query.getSingleResult();
		if (session != null) {
			dt = (Date) session;
		}
		return dt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SynchronousSession> getSynchronousSessionsByClassId(
			Long synchClassId) {
		List<SynchronousSession> syncList = null;
		StringBuilder jpq = new StringBuilder(
				"SELECT p FROM SynchronousSession p WHERE p.synchronousClass.id = :syncClassId");
		Query query = entityManager.createQuery(jpq.toString());
		query.setParameter("syncClassId", synchClassId);

		syncList = query.getResultList();

		return syncList;
	}

	@Override
	public void deleteSynchronousSessions(Long[] sessionIdArray) {
		StringBuilder jpq = new StringBuilder(
				"DELETE FROM SynchronousSession p WHERE p.id IN (:sessionIds)");
		Query query = entityManager.createQuery(jpq.toString());
		query.setParameter("sessionIds", Arrays.asList(sessionIdArray));
		query.executeUpdate();
	}

	@Override
	public SynchrounousSessionVO getMinMaxScheduleDateForSynchronousClass(
			Long classId) {
		SynchrounousSessionVO syncVO = new SynchrounousSessionVO();
		Object[] session = null;
		StringBuilder jpq = new StringBuilder(
				"SELECT min(p.startDateTime), max(p.endDateTime) FROM SynchronousSession p WHERE p.synchronousClass.id = :syncClassId");
		Query query = entityManager.createQuery(jpq.toString());
		query.setParameter("syncClassId", classId);

		session = (Object[]) query.getSingleResult();
		if (session != null) {
			syncVO.setMinStartDateTime((Date) session[0]);
			syncVO.setMaxEndDateTime((Date) session[1]);
		}
		return syncVO;
	}

	public SynchronousSession addSynchronousClassSession(
			SynchronousSession synchSession) {
		SynchronousSession session = entityManager.merge(synchSession);
		return session;
	}

}
