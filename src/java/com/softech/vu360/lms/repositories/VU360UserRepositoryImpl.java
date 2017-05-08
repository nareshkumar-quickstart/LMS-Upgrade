package com.softech.vu360.lms.repositories;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.*;

import com.softech.vu360.lms.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ProctorStatusEnum;

public class VU360UserRepositoryImpl implements VU360UserRepositoryCustom {

	private static final Logger log = Logger.getLogger(VU360UserRepositoryImpl.class.getName());
	
	@Inject
	private LMSAdministratorAllowedDistributorRepository lMSAdministratorAllowedDistributorRepository;
	@Inject
	LearnerRepository learnerRepository;
	@Inject
	DistributorRepository distributorRepository; 	
	@Inject
	VU360UserRepository vU360UserRepository; 	

	@Inject
	private LMSRoleRepository lmsRoleRepository;

	@PersistenceContext
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<VU360User> getUserByFirstNameAndLastName(Customer cust,String firstName, String lastName) {
		String jpq =null;
		boolean fNameBlank=StringUtils.isBlank(firstName);
		boolean lNameBlank=StringUtils.isBlank(lastName);

		if (!fNameBlank && !lNameBlank) {
			jpq="SELECT p FROM VU360User p WHERE p.learner.customer.id = :id AND p.firstName = :firstName AND p.lastName = :lastName";
		} else if (!fNameBlank) {
			jpq="SELECT p FROM VU360User p WHERE p.learner.customer.id = :id AND p.firstName = :firstName";
		} else if (!lNameBlank) {
			jpq="SELECT p FROM VU360User p WHERE p.learner.customer.id = :id AND p.lastName = :lastName";
		}
		Query query = entityManager.createQuery(jpq);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		query.setParameter("id", cust.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VU360User> getActiveUserByUsername(String username) {
		String jpq = "SELECT p FROM VU360User p WHERE p.username = :username AND p.learner.customer.active = true AND p.learner.customer.distributor.active = true";
		Query query = entityManager.createQuery(jpq);
		query.setParameter("username", username);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VU360User> findUserByEmailAddress(String emailAddress) {
		String jpq = "SELECT p FROM VU360User p WHERE p.emailAddress = :emailAddress AND p.learner.customer.active = true AND p.learner.customer.distributor.active = true";
		Query query = entityManager.createQuery(jpq);
		query.setParameter("emailAddress", emailAddress);
		return query.getResultList();
	}

	@Override
	public VU360User findUserByUserName(String username) {
		VU360User user = null;
		try{
			String jpq = "SELECT p FROM VU360User p WHERE p.username = :username";
			Query query = entityManager.createQuery(jpq);
			query.setParameter("username", username);
			user = (VU360User) query.getSingleResult();
		}catch(Exception e){
			log.debug("username:"+username+" not found in DB>>"+e.getMessage());
		}
		return user;
	}

	@Override
	@Transactional
	public VU360User findByIdForBatchImport(Long userId) {
		VU360User user = null;
		try{
			String jpq = "SELECT p FROM VU360User p WHERE p.id = :userId";
			Query query = entityManager.createQuery(jpq, VU360User.class);
			query.setParameter("userId", userId);
			user = (VU360User) query.getSingleResult();
			if(user != null) {
				Hibernate.initialize(user.getLearner());
				Hibernate.initialize(user.getTrainingAdministrator());
				if (user.getTrainingAdministrator() != null)
					Hibernate.initialize(user.getTrainingAdministrator().getManagedGroups());
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return user;
	}

	@Override
	@Transactional
	public VU360User loadUserForBatchImport(Long userId) {
		VU360User user = null;
		try{
			String jpq = "SELECT p FROM VU360User p WHERE p.id = :userId";
			Query query = entityManager.createQuery(jpq);
			query.setParameter("userId", userId);
			user = (VU360User) query.getSingleResult();
			if(user != null) {
				Hibernate.initialize(user.getLearner());
				Hibernate.initialize(user.getTrainingAdministrator());
				if (user.getTrainingAdministrator() != null)
					Hibernate.initialize(user.getTrainingAdministrator().getManagedGroups());
				Set<LMSRole> roles = lmsRoleRepository.getRoleWithFeatures(user.getLmsRoles());
				user.setLmsRoles(roles);
				Hibernate.initialize(user.getInstructor());
				Hibernate.initialize(user.getProctor());
				Hibernate.initialize(user.getRegulatoryAnalyst());
				Hibernate.initialize(user.getLmsAdministrator());
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
		return user;
	}

	@Override
	@Transactional
	public VU360User saveUser(VU360User user) {
		VU360User attachedUser=new VU360User();
		try {
			if(user!=null && user.getId()!=null){
				attachedUser = vU360UserRepository.findOne(user.getId());
				attachedUser.updateDeepValues(user);
			}else{
				attachedUser=user;
			}
			attachedUser = vU360UserRepository.save(attachedUser);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return attachedUser;
	}

	@Transactional
	@Override
	public VU360User updateUser(VU360User updatedUser) {
		return entityManager.merge(updatedUser);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VU360User> getUserByEmailFirstNameLastName(String email,String firstName, String lastName) {
		String jpq = "SELECT p FROM VU360User p WHERE p.emailAddress = :email AND p.firstName = :firstName AND p.lastName = :lastName AND p.learner.customer.active = true AND p.learner.customer.distributor.active = true";
		Query query = entityManager.createQuery(jpq);
		query.setParameter("email", email);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VU360User> getListOfUsersByGUID(List<String> guids) {
		String jpq = "SELECT p FROM VU360User p WHERE p.userGUID IN (:guids)";
		Query query = entityManager.createQuery(jpq);
		query.setParameter("guids", guids);
		return query.getResultList();
	}

	@Override
	public VU360User getUserByGUID(String userGUID) {
		String jpq = "SELECT p FROM VU360User p WHERE p.userGUID = :userGUID";
		Query query = entityManager.createQuery(jpq);
		query.setParameter("userGUID", userGUID);
		return (VU360User) query.getSingleResult();
	}

	@Override
	public Map<Object, Object> findUsers(String firstName, String lastName, String email,
			VU360User loggedInUser, int pageIndex, int pageSize, String sortBy,
			int sortDirection) {

		int count = 0;
		Map<Object, Object> resultMap;
		List<VU360User> userList;
		StringBuilder builder = new StringBuilder("SELECT p FROM VU360User p");
		boolean fnFlag = false;
		boolean lnFlag = false;
		boolean eFlag = false;
		boolean sortFlag = false;
		if (!StringUtils.isBlank(sortBy))
			sortFlag = true;

		if (!StringUtils.isBlank(firstName) || !StringUtils.isBlank(lastName)
				|| !StringUtils.isBlank(email)) {
			builder.append("WHERE");

			if (!StringUtils.isBlank(firstName)) {
				builder.append(" p.firstName LIKE :firstName");
				fnFlag = true;
			}
			if (!StringUtils.isBlank(lastName)) {

				if (fnFlag) {
					builder.append(" AND p.lastName LIKE :lastName");
					lnFlag = true;
				} else {
					builder.append(" p.lastName LIKE :lastName");
					lnFlag = true;
				}
			}
			if (!StringUtils.isBlank(email)) {
				if (lnFlag) {
					builder.append(" AND p.emailAddress LIKE :emailAddress");
					eFlag = true;
				} else {
					builder.append(" p.emailAddress LIKE :emailAddress");
					eFlag = true;
				}
			}
		}
		if (sortDirection == 0)
			builder.append(" ORDER BY :sortBy ASC");
		else {
			builder.append(" ORDER BY :sortBy DESC");
		}
		Query query = entityManager.createQuery(builder.toString());
		if (fnFlag)
			query.setParameter("firstName", "%" + firstName + "%");
		if (lnFlag)
			query.setParameter("lastName", "%" + lastName + "%");
		if (eFlag)
			query.setParameter("emailAddress", "%" + email + "%");
		if (sortFlag) {
			query.setParameter("sortBy", sortBy);
		}
		query.setFirstResult(pageIndex);
		pageSize = pageIndex * pageSize;
		query.setMaxResults(pageSize);
		userList = query.getResultList();
		if (userList != null) {
			count = userList.size();
		}
		resultMap = new HashMap<Object, Object>();
		resultMap.put("recordSize", count);
		resultMap.put("list", userList);
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, Object> findAllLearners(String firstName,
			String lastName, String email, VU360User loggedInUser,
			String sortBy, int sortDirection) {
		int count = 0;
		Map<Object, Object> resultMap;
		List<VU360User> userList;
		StringBuilder builder = new StringBuilder("SELECT p FROM VU360User p");
		boolean fnFlag = false;
		boolean lnFlag = false;
		boolean eFlag = false;
		boolean sortFlag = false;
		if (!StringUtils.isBlank(sortBy))
			sortFlag = true;

		if (!StringUtils.isBlank(firstName) || !StringUtils.isBlank(lastName)
				|| !StringUtils.isBlank(email)) {
			builder.append("WHERE");

			if (!StringUtils.isBlank(firstName)) {
				builder.append(" p.firstName LIKE :firstName");
				fnFlag = true;
			}
			if (!StringUtils.isBlank(lastName)) {
				if (fnFlag) {
					builder.append(" AND p.lastName LIKE :lastName");
					lnFlag = true;
				} else {
					builder.append(" p.lastName LIKE :lastName");
					lnFlag = true;
				}
			}
			if (!StringUtils.isBlank(email)) {
				if (lnFlag) {
					builder.append(" AND p.emailAddress LIKE :emailAddress");
					eFlag = true;
				} else {
					builder.append(" p.emailAddress LIKE :emailAddress");
					eFlag = true;
				}
				;
			}
		}
		if (sortDirection == 0)
			builder.append(" ORDER BY :sortBy ASC");
		else {
			builder.append(" ORDER BY :sortBy DESC");
		}
		Query query = entityManager.createQuery(builder.toString());
		if (fnFlag)
			query.setParameter("firstName", "%" + firstName + "%");
		if (lnFlag)
			query.setParameter("lastName", "%" + lastName + "%");
		if (eFlag)
			query.setParameter("emailAddress", "%" + email + "%");
		if (sortFlag) {
			query.setParameter("sortBy", sortBy);
		}
		userList = query.getResultList();
		if (userList != null) {
			count = userList.size();
		}
		resultMap = new HashMap<>();
		resultMap.put("recordSize", count);
		resultMap.put("list", userList);
		return resultMap;

	}

	@Override
	public VU360User deleteLMSAdministrator(VU360User user) {
		if (user.getLmsAdministrator() != null) {
			LMSAdministrator lmsAdministrator = user.getLmsAdministrator();
			entityManager.remove(lmsAdministrator);
		}
		user.setLmsAdministrator(null);
		return entityManager.merge(user);
	}

	@Override
	public void deleteLMSTrainingAdministrator(TrainingAdministrator trainingAdministrator) {
		entityManager.remove(trainingAdministrator);
	}

	@Override
	public VU360User deleteLMSTrainingManager(VU360User user) {
		if (user.getTrainingAdministrator() != null) {
			TrainingAdministrator trainingAdministrator = user.getTrainingAdministrator();
			entityManager.remove(trainingAdministrator);
		}
		user.setTrainingAdministrator(null);
		return entityManager.merge(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, Object> searchCustomerUsers(Customer customer,
			String firstName, String lastName, String email, int pageIndex,
			int pageSize, String sortBy, int sortDirection) {
		int count = 0;
		Map<Object, Object> resultMap;
		List<VU360User> userList;
		StringBuilder builder = new StringBuilder("SELECT p FROM VU360User p WHERE p.learner.customer.id = :customerId");
		boolean fnFlag = false;
		boolean lnFlag = false;
		boolean eFlag = false;
		boolean sortFlag = false;
		if (!StringUtils.isBlank(sortBy))
			sortFlag = true;

		if (!StringUtils.isBlank(firstName)) {
			builder.append(" AND p.firstName LIKE :firstName");
			fnFlag = true;

		}
		if (!StringUtils.isBlank(lastName) && fnFlag) {
			builder.append(" AND p.lastName LIKE :lastName");
			lnFlag = true;

		}
		if (!StringUtils.isBlank(email) && lnFlag) {
			builder.append(" AND p.emailAddress LIKE :emailAddress");
			eFlag = true;
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
		query.setParameter("customerId", customer.getId());
		if (fnFlag)
			query.setParameter("firstName", "%" + firstName + "%");
		if (lnFlag)
			query.setParameter("lastName", "%" + lastName + "%");
		if (eFlag)
			query.setParameter("emailAddress", "%" + email + "%");
		query.setFirstResult(pageIndex);
		pageSize = pageIndex * pageSize;
		query.setMaxResults(pageSize);
		userList = query.getResultList();
		if (userList != null) {
			count = userList.size();
		}
		resultMap = new HashMap<Object, Object>();
		resultMap.put("recordSize", count);
		resultMap.put("UsersList", userList);

		return resultMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VU360User> searchCustomerUsers(Customer customer,
			String firstName, String lastName, String email, String sortBy,
			int sortDirection) {
		List<VU360User> userList;
		StringBuilder builder = new StringBuilder(
				"SELECT p FROM VU360User p WHERE p.learner.customer.id = :cusomerId");
		boolean fnFlag = false;
		boolean lnFlag = false;
		boolean eFlag = false;
		boolean sortFlag = false;
		if (!StringUtils.isBlank(sortBy))
			sortFlag = true;

		if (!StringUtils.isBlank(firstName)) {
			builder.append(" AND p.firstName LIKE :firstName");
			fnFlag = true;

		}
		if (!StringUtils.isBlank(lastName) && fnFlag) {
			builder.append(" AND p.lastName LIKE :lastName");
			lnFlag = true;
		}
		if (!StringUtils.isBlank(email) && lnFlag) {
			builder.append(" AND p.emailAddress LIKE :emailAddress");
			eFlag = true;
		}

		if (sortDirection == 0)
			builder.append(" ORDER BY :sortBy ASC");
		else {
			builder.append(" ORDER BY :sortBy DESC");
		}
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("customerId", customer.getId());
		if (fnFlag)
			query.setParameter("firstName", "%" + firstName + "%");
		if (lnFlag)
			query.setParameter("lastName", "%" + lastName + "%");
		if (eFlag)
			query.setParameter("emailAddress", "%" + email + "%");
		if (sortFlag) {
			query.setParameter("sortBy", sortBy);
		}

		userList = query.getResultList();
		return userList;
	}

	@Override
	public VU360User updateUserWithLoad(VU360User updatedUser) {
		return entityManager.merge(updatedUser);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VU360User> getUserByIds(String[] idl) {
		Long ida[] = new Long[idl.length];

		for (int i = 0; i < ida.length; i++) {
			ida[i] = (Long) Long.parseLong(idl[i]);
		}
		List<Long> ids = Arrays.asList(ida);
		String jpq = "SELECT p FROM VU360User p WHERE p.id IN (:ids)";
		Query query = entityManager.createQuery(jpq);
		query.setParameter("ids", ids);
		return query.getResultList();
	}

	@Override
	public VU360UserAudit saveVU360UserAudit(VU360User user, String operation,Long modifingUser) {
		VU360UserAudit userAudit = null;
		if (user != null) {
			userAudit = new VU360UserAudit();
			userAudit.setAcceptedEULA(user.isAcceptedEULA());
			userAudit.setAccountNonExpired(user.getAccountNonExpired());
			userAudit.setAccountNonLocked(user.getAccountNonLocked());
			userAudit.setChangePasswordOnLogin(user.getChangePasswordOnLogin());
			userAudit.setCreatedDate(user.getCreatedDate());
			userAudit.setCredentialsNonExpired(user.isCredentialsNonExpired());
			userAudit.setEmailAddress(user.getEmailAddress());
			userAudit.setExpirationDate(user.getExpirationDate());
			userAudit.setEnabled(user.getEnabled());
			userAudit.setFirstName(user.getFirstName());
			userAudit.setLastLogonDate(user.getLastLogonDate());
			userAudit.setLastName(user.getLastName());
			userAudit.setLastUpdatedDate(user.getLastUpdatedDate());
			userAudit.setMiddleName(user.getMiddleName());
			userAudit.setNewUser(user.isNewUser());
			userAudit.setNumLogons(user.getNumLogons());
			userAudit.setPassword(user.getPassword());
			userAudit.setShowGuidedTourScreenOnLogin(user.getShowGuidedTourScreenOnLogin());
			userAudit.setUserGUID(user.getUserGUID());
			userAudit.setUsername(user.getUsername());
			userAudit.setVissibleOnReport(user.getVissibleOnReport());
			userAudit.setVu360userId(user.getId());
			userAudit.setOperation(operation);

			if (operation.equalsIgnoreCase("update")) {
				userAudit.setUpdatedOn(new Date(System.currentTimeMillis()));
				userAudit.setUpdatedBy(modifingUser);
			} else {
				userAudit.setCreatedOn(new Date(System.currentTimeMillis()));
				userAudit.setCreatedBy(modifingUser);
			}

			try {
				userAudit = entityManager.merge(userAudit);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
		return userAudit;
	}

	@Override
	public List<VU360User> getAllLearners(String firstName, String lastName, String email, String searchCriteria, VU360User loggedInUser) {
		StringBuilder queryString = new StringBuilder("SELECT u FROM VU360User u Join u.learner l where l.id>1 ");
		
		if (!StringUtils.isBlank(searchCriteria)) {
			queryString.append("and u.firstName like :firstName Or u.lastName like :lastName or u.emailAddress like :emailAddress ");
		}else{
			if ( !StringUtils.isBlank(firstName) ) {
				queryString.append(" and u.firstName like :firstName ");
			}
			if ( !StringUtils.isBlank(lastName) ) {
				queryString.append(" and u.lastName like :lastName ");
			}
			if ( !StringUtils.isBlank(email) ) {
				queryString.append(" and u.emailAddress like :emailAddress ");
			}
		}
		
		//LMS-14184
		if(!loggedInUser.getLmsAdministrator().isGlobalAdministrator()){ 	// apply administrator filtering
			queryString.append(" and l.customer.distributor.id in :distributors ");
		}
		Query query = entityManager.createQuery(queryString.toString());
		
		if (!StringUtils.isBlank(searchCriteria)) {
			query.setParameter("firstName", firstName+"%");
			query.setParameter("lastName", lastName+"%");
			query.setParameter("emailAddress", email+"%");
		}else{
			if ( !StringUtils.isBlank(firstName) ) {
				query.setParameter("firstName", firstName+"%");
			}
			if ( !StringUtils.isBlank(lastName) ) {
				query.setParameter("lastName", lastName+"%");
			}
			if ( !StringUtils.isBlank(email) ) {
				query.setParameter("emailAddress", email+"%");
			}
		}
		
		if(!loggedInUser.getLmsAdministrator().isGlobalAdministrator()){ 	// apply administrator filtering
			List<Distributor> distributors = getAdminRestrictionExpression(loggedInUser);
			
			@SuppressWarnings("unchecked")
			Collection<Long> distributorIds = CollectionUtils.collect(distributors, new Transformer() {
			      public Long transform(Object o) {
			          return ((Distributor) o).getId();
			      }
			  });
			query.setParameter("distributors", distributorIds);
		}
		return query.getResultList(); 
	}
	
	@Override
	public List<VU360User> findBylearnerIn(Collection<Long> ids){
		return vU360UserRepository.findAll(ids);
	}

	private List<Distributor> getAdminRestrictionExpression(VU360User loggedInUser){
		List<Distributor> distributorList=null;
		if (loggedInUser!=null) {
			distributorList = new ArrayList<>();
			if (loggedInUser.getLmsAdministrator() != null
					&& !loggedInUser.getLmsAdministrator().isGlobalAdministrator()) {

				List<DistributorGroup> ls = loggedInUser.getLmsAdministrator().getDistributorGroups();
				Set<DistributorGroup> uniqueDistributorGroups = new HashSet<DistributorGroup>(ls);

				//get id from collection.
				Collection<Long> dgIds = Collections2.transform(
						uniqueDistributorGroups,
						new Function<DistributorGroup, Long>() {
							public Long apply(DistributorGroup arg0) {
								return Long.parseLong(arg0.getId().toString());
							}
						});

				List<LMSAdministratorAllowedDistributor> allowedDistributors = lMSAdministratorAllowedDistributorRepository
						.findByDistributorGroupIdInAndLmsAdministratorId(dgIds,
								loggedInUser.getLmsAdministrator().getId());

				//get id from collection.			
				Collection<Long> ids = Collections2
						.transform(
								allowedDistributors,
								new Function<LMSAdministratorAllowedDistributor, Long>() {
									public Long apply(
											LMSAdministratorAllowedDistributor arg0) {
										return Long.parseLong(arg0
												.getAllowedDistributorId()
												.toString());
									}
								});

				if (ids != null && !ids.isEmpty()) {
					Set<Long> uniqueIds = new HashSet<Long>(ids);
					distributorList = (List<Distributor>) distributorRepository
							.findAll(uniqueIds);
				}

			}
		}
		return distributorList;
	}
	
	@Override
	public Set<Long> getOrganizational_Group_Members(Long userId,Long customerId) {
		StoredProcedureQuery query;
		Set<Long> userIds = new HashSet<>();
		List<BigInteger> users;
		
		query = entityManager.createNamedStoredProcedureQuery("VU360User.getOrganizational_Group_Members");
		query.setParameter("VU360USER_ID", userId);
		query.setParameter("CUSTOMER_ID", customerId);
		query.execute();
		users = query.getResultList();
		
		for(BigInteger user: users) {
			userIds.add( user.longValue()); 
		}
		
		return userIds;
	}

	@Override
	public List<VU360User> getLearnersByCustomer(String firstName, String lastName, String email, String searchCriteria,Long customerId) {
		StringBuilder queryString = new StringBuilder("SELECT u FROM VU360User u Join u.learner l where l.customer.id = ").append(customerId);
		
		if (!StringUtils.isBlank(searchCriteria)) {
			queryString.append(" and u.firstName like :firstName Or u.lastName like :lastName or u.emailAddress like :emailAddress");
		}else{
			if ( !StringUtils.isBlank(firstName) ) {
				queryString.append(" and u.firstName like :firstName");
			}
			if ( !StringUtils.isBlank(lastName) ) {
				queryString.append(" and u.lastName like :lastName");
			}
			if ( !StringUtils.isBlank(email) ) {
				queryString.append(" and u.emailAddress like :emailAddress");
			}
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		
		if (!StringUtils.isBlank(searchCriteria)) {
			query.setParameter("firstName", "%"+firstName+"%");
			query.setParameter("lastName", "%"+lastName+"%");
			query.setParameter("email", "%"+email+"%");
		}else{
			if ( !StringUtils.isBlank(firstName) ) {
				query.setParameter("firstName", "%"+firstName+"%");
			}
			if ( !StringUtils.isBlank(lastName) ) {
				query.setParameter("lastName", "%"+lastName+"%");
			}
			if ( !StringUtils.isBlank(email) ) {
				query.setParameter("email", "%"+email+"%");
			}
		}
		return query.getResultList(); 
	}

	@Override
	public List<VU360User> getLearnersByDistributor(String firstName, String lastName, String email,String searchCriteria, Long distributorId) {
		StringBuilder queryString = new StringBuilder("SELECT u FROM VU360User u Join u.learner l where l.customer.distributor.id = ").append(distributorId);
		
		if (!StringUtils.isBlank(searchCriteria)) {
			queryString.append(" and u.firstName like :firstName Or u.lastName like :lastName or u.emailAddress like :emailAddress");
		}else{
			if ( !StringUtils.isBlank(firstName) ) {
				queryString.append(" and u.firstName like :firstName");
			}
			if ( !StringUtils.isBlank(lastName) ) {
				queryString.append(" and u.lastName like :lastName");
			}
			if ( !StringUtils.isBlank(email) ) {
				queryString.append(" and u.emailAddress like :emailAddress");
			}
		}
		
		Query query = entityManager.createQuery(queryString.toString());
		
		if (!StringUtils.isBlank(searchCriteria)) {
			query.setParameter("firstName", "%"+firstName+"%");
			query.setParameter("lastName", "%"+lastName+"%");
			query.setParameter("email", "%"+email+"%");
		}else{
			if ( !StringUtils.isBlank(firstName) ) {
				query.setParameter("firstName", "%"+firstName+"%");
			}
			if ( !StringUtils.isBlank(lastName) ) {
				query.setParameter("lastName", "%"+lastName+"%");
			}
			if ( !StringUtils.isBlank(email) ) {
				query.setParameter("email", "%"+email+"%");
			}
		}
		return query.getResultList(); 
	}
	
	/** Added By Marium Saud : This method caters the requirement for searchAll feature for User.
	 * Following Params are passed from  LearnerServiceImpl.getAllUsersByCriteria method
	 * @Param LMSRole
	 * @Param VU360User
	 * @Param String searchCriteria
	 * @Param String firstName
	 * @Param String lastName
	 * @Param String email
	 * @Param Long[] idbucket
	 * @Param Boolean isProctorRole
	 * @Param Boolean notLmsRole
	 * */
	/** Following Params are passed from  LearnerServiceImpl.findActiveLearners method
	 * @Param Boolean accountNonExpired
	 * @Param Boolean accountNonLocked
	   @Param Boolean enabled
	   @Param String sortBy
	   @Param int sortDirection
	 */
	
	public List<VU360User> showAll(LMSRole lmsRole, 
			boolean isLMSAdministrator, boolean trainingAdmin_isManagesAllOrganizationalGroups, Long customerId, Long userId, 
			String searchCriteria, String firstName,String lastName, String email, Long[] idbucket, Boolean isProctorRole, Boolean notLmsRole, Boolean accountNonExpired, Boolean accountNonLocked,
			Boolean enabled, String sortBy, int sortDirection) {
		StringBuilder builder=new StringBuilder();
		// Adding check for LMSRole as it's conditional Param , received as an argument from LearnerServiceImpl.getAllUsersByCriteria method only.
		int checkValue=0;
		
		if(lmsRole!=null && !notLmsRole){
			checkValue = 1;
		}
		if(lmsRole!=null && notLmsRole){
			checkValue = 2;
		}
		builder.append("Select u.ID, u.FIRSTNAME, u.LASTNAME, u.USERNAME, u.EMAILADDRESS, u.ACCOUNTNONLOCKEDTF, l.ID as LEARNERID, la.ID as LMSADMINISTRATOR_ID, ta.ID as TRAININGADMINISTRATOR_ID,");
		builder.append("(CASE WHEN ").append(checkValue).append("= 1").append(" THEN ");
		builder.append("(select ROLENAME from LMSROLE where id IN "
				+ "(select top 1 ROLE_ID from VU360USER_ROLE ur1 where ur1.USER_ID = u.id and ur1.ROLE_ID = ").
				append(checkValue > 0 ? lmsRole.getId():0).append(")) ").
				append("WHEN ").append(checkValue).append("= 2").append(" THEN ");
		builder.append("(select ROLENAME from LMSROLE where id IN "
				+ "(select top 1 ROLE_ID from VU360USER_ROLE ur1 where ur1.USER_ID = u.id and ur1.ROLE_ID != ").
				append(checkValue > 0 ? lmsRole.getId():0).append(")) ").
				append(" else null END ) as ROLENAME, ");
		builder.append("(CASE WHEN ").append(checkValue).append("= 1").append(" THEN ");
		builder.append("(select top 1 ROLE_ID from VU360USER_ROLE ur1 where ur1.USER_ID = u.id and ur1.ROLE_ID = ").
				append(checkValue > 0 ? lmsRole.getId():0).append(") ").
				append("WHEN ").append(checkValue).append("= 2").append(" THEN ");
		builder.append("(select top 1 ROLE_ID from VU360USER_ROLE ur1 where ur1.USER_ID = u.id and ur1.ROLE_ID != ").
				append(checkValue > 0 ? lmsRole.getId():0).append(") ").
				append(" else null END ) as  ROLEID "); 
		
		//'FROM' clause
		builder.append(" from VU360User u ");
		builder.append(" left outer join LEARNER l on l.VU360USER_ID = u.id ");
		builder.append(" left outer join CUSTOMER c on c.ID = l.CUSTOMER_ID ");
		builder.append(" left outer join LMSADMINISTRATOR la on la.VU360USER_ID=u.ID ");
		builder.append(" left outer join TRAININGADMINISTRATOR ta on ta.VU360USER_ID=u.ID ");
		
		//Conditional Join if Proctor is not 'Null'
		if (isProctorRole && lmsRole != null && lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR)) {
			builder.append(" left outer join PROCTOR p on p.VU360USER_ID = u.id ");
		}
		
		builder.append(" where 1=1 ");
		
		if(idbucket!=null){
			builder.append(" and u.id IN ").append("(").append(StringUtils.join(idbucket,",")).append(")").append(" ");
		}
		else{
			if (isLMSAdministrator) {
				Long currentCustomerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
				builder.append(" and c.ID = ").append(currentCustomerId);
			} else {
				if (trainingAdmin_isManagesAllOrganizationalGroups) {
					builder.append(" and c.ID = ").append(customerId);
				} 
			}
		}
		
		if (isProctorRole && lmsRole != null && lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_PROCTOR)) {
			builder.append(" and p.STATUS = '").append(ProctorStatusEnum.Active.toString()).append("' ");
		}
		if(!StringUtils.isEmpty(firstName)){
			builder.append(" and UPPER(u.FIRSTNAME) LIKE ").append("'").append("%").append(firstName.toUpperCase()).append("%").append("'");
		}
		if(!StringUtils.isEmpty(lastName)){
			builder.append(" and UPPER(u.LASTNAME) LIKE ").append("'").append("%").append(lastName.toUpperCase()).append("%").append("'");
		}
		if(!StringUtils.isEmpty(email)){
			builder.append(" and UPPER(u.EMAILADDRESS) LIKE ").append("'").append("%").append(email.toUpperCase()).append("%").append("'");
		}
		if(!StringUtils.isEmpty(searchCriteria)){
			builder.append(" and UPPER(u.FIRSTNAME) LIKE ").append("'").append("%").append(firstName.toUpperCase()).append("%").append("'");
			builder.append(" OR UPPER(u.LASTNAME) LIKE ").append("'").append("%").append(lastName.toUpperCase()).append("%").append("'");
			builder.append(" OR UPPER(u.EMAILADDRESS LIKE ").append("'").append("%").append(email.toUpperCase()).append("%").append("'");
		}
		if(accountNonExpired != null){
			builder.append(" and u.ACCOUNTNONEXPIREDTF = ").append("'").append(accountNonExpired ? 1 : 0).append("'");
		}
		if(accountNonLocked != null){
			builder.append(" and u.ACCOUNTNONLOCKEDTF = ").append("'").append(accountNonLocked ? 1 : 0).append("'");
		}
		if(enabled != null){
			builder.append(" and u.ENABLEDTF = ").append("'").append(enabled ? 1 : 0).append("'");
		}
		if(!StringUtils.isEmpty(sortBy)){
			if (sortDirection == 0)
				builder.append(" ORDER BY ").append(sortBy).append(" ASC");
			else {
				builder.append(" ORDER BY ").append(sortBy).append(" DESC");
			}	
		}
		
		log.debug("Qry generated in showAll()>> " +builder.toString());
		Query query = entityManager.createNativeQuery(builder.toString());
		List<Object[]> results = query.getResultList();  
		List<VU360User> users = new ArrayList<>(results.size());
        results.stream().forEach((record) -> users.add(new VU360User(((BigInteger) record[0]).longValue(), (String) record[1], ((String) record[2]), (String) record[3], (String) record[4], ((Integer)record[5]),((BigInteger) record[6]).longValue(), record[7]==null?0:((BigInteger) record[7]).longValue(), record[8]==null?0:((BigInteger) record[8]).longValue(), record[9]==null?"":(String)record[9],record[10]==null?0:((BigInteger) record[10]).longValue())));
		return users;
	}

	@Override
	public List<Long> findLearnerIdsByVu360UserIn(List<VU360User> users) {
		Query query = this.entityManager.createQuery("SELECT L.id FROM VU360User U JOIN U.learner L where U in :users");
		query.setParameter("users", users);
		return query.getResultList();
	}

	@Override
	@Transactional
	public List<VU360User> findByUsernameInForBatchImport(Collection<String> vList) {
		EntityGraph vu360UserGraph = this.entityManager.createEntityGraph(VU360User.class);
		Subgraph<Learner> learnerGraph = vu360UserGraph.addSubgraph("learner");

		Query query = this.entityManager.createQuery("SELECT U FROM VU360User U WHERE U.username IN (:userNames)", VU360User.class).
				setHint("javax.persistence.loadgraph", vu360UserGraph);
		query.setParameter("userNames", vList);
		List<VU360User> users = query.getResultList();
		return users;
	}

	@Override
	@Transactional
	public VU360User saveUserForBatchImport(VU360User user) {
		VU360User updatedUser = saveUser(user);
		return updatedUser;
	}
}
