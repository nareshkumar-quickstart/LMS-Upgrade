package com.softech.vu360.lms.repositories;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.util.FormUtil;

public class SynchronousClassRepositoryImpl implements
		SynchronousClassRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Map<Object, Object> findSynchClassesByCourseId(Long courseId,
			String sectionName, String startDate, String endDate,
			int pageIndex, int pageSize, String sortBy, int sortDirection,
			int maxLimit) {
		int count = 0;
		Map<Object, Object> resultMap = null;
		List<SynchronousClass> userList = null;
		StringBuilder builder = new StringBuilder(
				"SELECT p FROM SynchronousClass p WHERE p.course.id = :courseId ");
		boolean snFlag = false;
		boolean sdFlag = false;
		boolean edFlag = false;
		boolean sortFlag = false;
		Date classStartDate = null;
		Date classEndDate = null;

		if (!StringUtils.isBlank(sortBy)) {
			sortFlag = true;
		}
		if (!StringUtils.isBlank(sectionName)
				|| !StringUtils.isBlank(startDate)
				|| !StringUtils.isBlank(endDate)) {
			builder.append("AND ");
			FormUtil formUtil = FormUtil.getInstance();
			if (!StringUtils.isBlank(sectionName)) {
				builder.append(" p.sectionName LIKE :sectionName");
				snFlag = true;

			}
			if (!StringUtils.isBlank(startDate)) {

				if (snFlag) {
					builder.append(" AND p.classStartDate = :classStartDate");
					classStartDate = (Date) formUtil.getDate(startDate,
							"MM/dd/yyyy");
					sdFlag = true;
				} else {
					builder.append(" p.classStartDate = :classStartDate");
					classStartDate = (Date) formUtil.getDate(startDate,
							"MM/dd/yyyy");
					sdFlag = true;
				}

			}
			if (!StringUtils.isBlank(endDate)) {
				if (sdFlag) {
					builder.append(" AND p.classEndDate LIKE :classEndDate");
					classEndDate = (Date) formUtil.getDate(endDate,
							"MM/dd/yyyy");
					edFlag = true;
				} else {
					builder.append(" p.classEndDate LIKE :classEndDate");
					classEndDate = (Date) formUtil.getDate(endDate,
							"MM/dd/yyyy");
					edFlag = true;
				}

			}
		}
		if (sortDirection == 0) {
			builder.append(" ORDER BY ");
			if (sortFlag) {
				builder.append("p.").append(sortBy).append(" ASC");
			}
		} else {
			builder.append(" ORDER BY ");
			if (sortFlag) {
				builder.append("p.").append(sortBy).append(" DESC");
			}
		}
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("courseId", courseId);
		if (snFlag)
			query.setParameter("sectionName", "%" + sectionName + "%");
		if (sdFlag)
			query.setParameter("classStartDate", classStartDate);
		if (edFlag)
			query.setParameter("classEndDate", classEndDate);

		query.setFirstResult(pageIndex * pageSize);
		query.setMaxResults(pageSize);
		userList = query.getResultList();
		if (userList != null && userList.size() > 0) {
			count = userList.size();
		}
		resultMap = new HashMap<Object, Object>();
		resultMap.put("recordSize", count);
		resultMap.put("list", userList);
		return resultMap;
	}

	@Override
	public Map<Object, Object> findAllSynchClassesByCourseId(Long courseId,
			String sectionName, String startDate, String endDate,
			String sortBy, int sortDirection, int maxLimit) {
		int count = 0;
		Map<Object, Object> resultMap = null;
		List<SynchronousClass> userList = null;
		StringBuilder builder = new StringBuilder(
				"SELECT p FROM SynchronousClass p");
		boolean snFlag = false;
		boolean sdFlag = false;
		boolean edFlag = false;
		boolean sortFlag = false;
		Date classStartDate = null;
		Date classEndDate = null;

		if (!StringUtils.isBlank(sortBy))
			sortFlag = true;

		if (!StringUtils.isBlank(sectionName)
				|| !StringUtils.isBlank(startDate)
				|| !StringUtils.isBlank(endDate)) {
			builder.append(" WHERE");
			FormUtil formUtil = FormUtil.getInstance();
			if (!StringUtils.isBlank(sectionName)) {
				builder.append(" p.sectionName LIKE :sectionName");
				snFlag = true;

			}
			if (!StringUtils.isBlank(startDate)) {

				if (snFlag) {
					builder.append(" AND p.classStartDate LIKE :classStartDate");
					classStartDate = (Date) formUtil.getDate(startDate,
							"MM/dd/yyyy");
					sdFlag = true;
				} else {
					builder.append(" p.classStartDate LIKE :classStartDate");
					classStartDate = (Date) formUtil.getDate(startDate,
							"MM/dd/yyyy");
					sdFlag = true;
				}

			}
			if (!StringUtils.isBlank(endDate)) {
				if (sdFlag) {
					builder.append(" AND p.classEndDate LIKE :classEndDate");
					classEndDate = (Date) formUtil.getDate(endDate,
							"MM/dd/yyyy");
					edFlag = true;
				} else {
					builder.append(" p.classEndDate LIKE :classEndDate");
					classEndDate = (Date) formUtil.getDate(endDate,
							"MM/dd/yyyy");
					edFlag = true;
				}

			}
		}
		if (sortDirection == 0) {
			builder.append(" ORDER BY ");
			if (sortFlag) {
				builder.append("p.").append(sortBy).append(" ASC");
			}
		} else {
			builder.append(" ORDER BY ");
			if (sortFlag) {
				builder.append("p.").append(sortBy).append(" DESC");
			}
		}
		Query query = entityManager.createQuery(builder.toString());
		if (snFlag)
			query.setParameter("sectionName", "%" + sectionName + "%");
		if (sdFlag)
			query.setParameter("classStartDate", classStartDate);
		if (edFlag)
			query.setParameter("classEndDate", classEndDate);

		query.setMaxResults(maxLimit);
		userList = query.getResultList();
		if (userList != null) {
			count = userList.size();
		}
		resultMap = new HashMap<Object, Object>();
		resultMap.put("recordSize", count);
		resultMap.put("list", userList);
		return resultMap;
	}

	@Transactional
	public void deleteByIdIn(List<Long> ids){
		
		if(ids != null && ids.size()>0){
			
			Query querySynchronousSession = entityManager.createQuery("delete from SynchronousSession p where p.synchronousClass.id IN (:ids)");
			entityManager.joinTransaction();
			querySynchronousSession.setParameter("ids", ids);
			querySynchronousSession.executeUpdate();
			
			Query querySynchronousClass = entityManager.createQuery("delete from SynchronousClass p where id IN (:ids)");
			querySynchronousClass.setParameter("ids", ids);
			entityManager.joinTransaction();
			querySynchronousClass.executeUpdate();
					
					
		}
		
		
	}
	
	
}
