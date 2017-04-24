package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.LMSAdministrator;
import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;
import com.softech.vu360.lms.model.VU360User;

/**
 * 
 * @author haider.ali
 *
 */
public class LMSAdministratorRepositoryImpl implements LMSAdministratorRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Inject
	LMSAdministratorRepository lmsAdministratorRepository;

	@Inject
	DistributorGroupRepository distributorGroupRepository;

	@Inject
	DistributorRepository distributorRepository;

	@Inject
	LMSAdministratorAllowedDistributorRepository lMSAdministratorAllowedDistributorRepository;

	@Override
	@Transactional
	public void assignLMSAdministratorsToDistributorGroup(
			List<LMSAdministratorAllowedDistributor> allowedResellerOfSelectedGroup) {

		lMSAdministratorAllowedDistributorRepository.save(allowedResellerOfSelectedGroup);

	}

	@Override
	public List<LMSAdministrator> findLMSAdministrators(String firstName, String lastName, String email,
			VU360User loggedInUser) {

		List<Distributor> ls = getAdminRestrictionExpression(loggedInUser);
		List<LMSAdministrator> lmsAdministrator = null;
		if (ls != null && !ls.isEmpty())
			lmsAdministrator = lmsAdministratorRepository
					.findByDistributorGroupsDistributorsInAndGlobalAdministratorFalseAndVu360UserFirstNameLikeOrVu360UserLastNameLikeOrVu360UserEmailAddressLike(
							ls, firstName, lastName, email);
		else {
			try {
				StringBuilder queryString = new StringBuilder(
						"SELECT p FROM LMSAdministrator p Where (p.globalAdministrator is null or p.globalAdministrator = FALSE) ");

				if (firstName != null) {
					queryString.append(" AND p.vu360User.firstName LIKE :firstName ");
				}

				if (lastName != null) {
					queryString.append(" AND p.vu360User.lastName LIKE :lastName ");
				}

				if (email != null) {
					queryString.append(" AND p.vu360User.emailAddress LIKE :email ");
				}

				Query query = entityManager.createQuery(queryString.toString());

				if (firstName != null) {
					query.setParameter("firstName", "%"+firstName+"%");
				}

				if (lastName != null) {
					query.setParameter("lastName", "%"+lastName+"%");
				}

				if (lastName != null) {
					query.setParameter("email", "%"+email+"%");
				}

				lmsAdministrator = query.getResultList();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return lmsAdministrator;

	}

	private List<Distributor> getAdminRestrictionExpression(VU360User loggedInUser) {

		/*
		 * List<Distributor> distributorList = new ArrayList<Distributor>();
		 * 
		 * if(!loggedInUser.getLmsAdministrator().isGlobalAdministrator()){
		 * List<LMSAdministratorAllowedDistributor> lmsAdminAllowedDist=new
		 * ArrayList<LMSAdministratorAllowedDistributor>();
		 * 
		 * //TODO OPTIMIZE IT. Duplicate iteration for(DistributorGroup
		 * distGroup :
		 * loggedInUser.getLmsAdministrator().getDistributorGroups()){
		 * List<LMSAdministratorAllowedDistributor> allowedDistributors =
		 * lMSAdministratorAllowedDistributorRepository.
		 * findByDistributorGroupIdAndLmsAdministratorId(distGroup.getId(),
		 * loggedInUser.getLmsAdministrator().getId());
		 * lmsAdminAllowedDist.addAll(allowedDistributors); }
		 * 
		 * @SuppressWarnings("unchecked") Collection<Long> allowedDistIds =
		 * CollectionUtils.collect(lmsAdminAllowedDist, new Transformer() {
		 * public Long transform(Object o) { return
		 * ((LMSAdministratorAllowedDistributor) o).getAllowedDistributorId(); }
		 * });
		 * 
		 * if(allowedDistIds!=null && allowedDistIds.size()>0){
		 * 
		 * @SuppressWarnings({ "unchecked", "rawtypes" }) Set<Long> set = new
		 * HashSet(allowedDistIds); distributorList = (List<Distributor>)
		 * distributorRepository.findAll(set); }
		 * 
		 * } return distributorList;
		 */

		// Highly optimized
		List<Distributor> distributorList = new ArrayList<Distributor>();
		if (loggedInUser.getLmsAdministrator() != null && !loggedInUser.getLmsAdministrator().isGlobalAdministrator()) {

			List<DistributorGroup> ls = loggedInUser.getLmsAdministrator().getDistributorGroups();
			Set<DistributorGroup> uniqueDistributorGroups = new HashSet<DistributorGroup>(ls);

			// get id from collection.
			Collection<Long> dgIds = Collections2.transform(uniqueDistributorGroups,
					new Function<DistributorGroup, Long>() {
						public Long apply(DistributorGroup arg0) {
							return Long.parseLong(arg0.getId().toString());
						}
					});

			List<LMSAdministratorAllowedDistributor> allowedDistributors = lMSAdministratorAllowedDistributorRepository
					.findByDistributorGroupIdInAndLmsAdministratorId(dgIds, loggedInUser.getLmsAdministrator().getId());

			// get id from collection.
			Collection<Long> ids = Collections2.transform(allowedDistributors,
					new Function<LMSAdministratorAllowedDistributor, Long>() {
						public Long apply(LMSAdministratorAllowedDistributor arg0) {
							return Long.parseLong(arg0.getAllowedDistributorId().toString());
						}
					});

			if (ids != null && !ids.isEmpty()) {
				Set<Long> uniqueIds = new HashSet<Long>(ids);
				distributorList = (List<Distributor>) distributorRepository.findAll(uniqueIds);
			}

		}
		return distributorList;

	}

}
