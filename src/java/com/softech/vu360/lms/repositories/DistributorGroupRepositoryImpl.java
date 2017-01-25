package com.softech.vu360.lms.repositories;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;

/**
 * 
 * @author haider.ali
 *
 */
public class DistributorGroupRepositoryImpl implements DistributorGroupRepositoryCustom {
	
	@Inject
	DistributorGroupRepository  distributorGroupRepository;

	@Inject
	DistributorRepository  distributorRepository;

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	@Transactional
	public void deleteDistributorGroups(Long[] distributorGroupIdArray) {

		List<DistributorGroup> gList=null;
		gList = (List<DistributorGroup>) distributorGroupRepository.findAll(Arrays.asList(distributorGroupIdArray));
		for (Iterator<DistributorGroup> iterator = gList.iterator(); iterator.hasNext();) {
			DistributorGroup distributorGroup = (DistributorGroup) iterator.next();
			distributorGroup.setActive(false);
		}
		distributorGroupRepository.save(gList);	
	}
	
	@Override
	@Transactional
	public void assignDistributorsInDistributorGroup(Long distributorGroupId,Long distributorIdArray[]){
		DistributorGroup distributorGroup = distributorGroupRepository.findById(distributorGroupId);
		List<Distributor> dList = (List<Distributor>) distributorRepository.findAll(Arrays.asList(distributorIdArray));
		distributorGroup.setDistributors(dList);
		 distributorGroupRepository.save(distributorGroup);
	}
	
	@Override
	public List<DistributorGroup> findDistributorGroupsByLMSAdministratorId(
			@Param("LMSAdministratorId") Long lmsAdministratorId) {

		final String content = "select dg from LMSAdministrator la JOIN la.distributorGroups dg "
				+ "where la.id = (:LMSAdministratorId)";

		TypedQuery<DistributorGroup> query = entityManager.createQuery(content, DistributorGroup.class);
		query.setParameter("LMSAdministratorId", lmsAdministratorId);

		return query.getResultList();

	}
}
