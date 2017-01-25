package com.softech.vu360.lms.repositories;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.vo.SynchrounousSessionVO;

@Transactional
public class SynchronousSessionRepositoryTest extends
		SpringJUnitConfigAbstractTest {

	@Inject
	private SynchronousSessionRepository synchronousSessionRepository;
	@Inject
	private SynchronousClassRepository synchronousClassRepository;

	// @Test
	public void findBySynchronousClass() {

		SynchronousClass sC = synchronousClassRepository.findOne(2l);
		List<SynchronousSession> sessionList = synchronousSessionRepository
				.findBySynchronousClass(sC);
		if (sessionList != null && sessionList.size() > 0) {
			for (SynchronousSession ss : sessionList) {
				System.out.println(ss.getEndDate());
			}
		}
	}

	//@Test
	public void findOneTest() {
		SynchronousSession sC = synchronousSessionRepository.findOne(2l);// synchronousSessionRepository.findOne(2l);
		if (sC != null) {
			System.out.println(sC.getMeetingGUID());
		}
	}

	// @Test
	public void deleteTest() {
		synchronousSessionRepository.delete(2l);
	}

	// @Test
	public void save() {
		SynchronousSession session = synchronousSessionRepository.findOne(3l);
		session.setId(null);
		synchronousSessionRepository.save(session);
	}

	// @Test
	public void findSynchronousSessionByClassId() {
		List<SynchronousSession> sessionList = synchronousSessionRepository
				.findSynchronousSessionByClassId(2l, null, null);
		if (sessionList != null && sessionList.size() > 0) {
			for (SynchronousSession session : sessionList) {
				System.out.println(session.getEndTime());
			}
		}

	}

	// @Test
	public void getSynchronousSessionsByClassId() {

		List<SynchronousSession> sessionList = synchronousSessionRepository
				.getSynchronousSessionsByClassId(2l);
		if (sessionList != null && sessionList.size() > 0) {
			for (SynchronousSession session : sessionList) {
				System.out.println(session.getEndTime());
			}
		}
	}

	public void getLastSessionStartDate() {
		Date dt = synchronousSessionRepository.getLastSessionStartDate(2l);
		if (dt != null) {
			System.out.println(dt.toString());
		}
	}

	public void getSynchronousSessionsByClassId2() {
		Map<Object, Object> sessionMap = synchronousSessionRepository
				.getSynchronousSessionsByClassId(2l, 0, 9, "startDateTime", 0);
		if (sessionMap != null && sessionMap.size() > 0) {
			int n = (Integer) sessionMap.get("totalRecords");
			@SuppressWarnings("unchecked")
			List<SynchronousSession> sessionList = (List<SynchronousSession>) sessionMap
					.get("synchronousSessionList");
		}
	}

	//@Test
	public void deleteSynchronousSessions() {
		// Warning! - Please run this method with caution
		Long[] sessionIdArray = { 13L, 55L, 66L };
		synchronousSessionRepository.deleteSynchronousSessions(sessionIdArray);
	}

	
	public void getMinMaxScheduleDateForSynchronousClass() {
		SynchrounousSessionVO vo = synchronousSessionRepository
				.getMinMaxScheduleDateForSynchronousClass(2l);
		System.out.println(vo.getMaxEndDateTime());
		System.out.println(vo.getMinStartDateTime());
	}

	public void addSynchronousClassSession() {
		// use this method with caution
		SynchronousSession session = synchronousSessionRepository.findOne(2l);
		// session.setMeetingGUID("caution");
		synchronousSessionRepository.addSynchronousClassSession(session);
	}
	
	

}
