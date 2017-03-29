package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.VU360UserService;

/*
 * @ author Kaunain Wajeeh
 * 
 * 
 */

public class LMSRoleRepositoryImpl implements LMSRoleRepositoryCustom {

	@PersistenceContext
	protected EntityManager entityManager;

	@Inject
	private VU360UserService vu360UserService;
	
	@Deprecated
	public LMSRole getDefaultRole(Customer customer) {
		LMSRole role = null;

		String jpq = "SELECT p FROM LMSRole p WHERE p.isDefaultForRegistration = true AND p.owner.id = :customerId";
		Query query = entityManager.createQuery(jpq);
		query.setParameter("customerId", customer.getId());
		@SuppressWarnings("unchecked")
		List<LMSRole> roleList = (List<LMSRole>) query.getResultList();
		if(roleList!=null && roleList.size()>0){
			if(roleList.size()>1){
				for (LMSRole lmsRole : roleList) {
					if(lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)){
						return lmsRole;
					}
				}
				return (LMSRole) roleList.get(0);
			}
			else{
				role=roleList.get(0);
			}
			return role;
			
		}
		else{
			return null;
		}
		

	}
	
	public List<LMSRole> getAllRoles(Customer customer, VU360User loggedInUser) {

		List<LMSRole> userList = null;
		StringBuilder builder = new StringBuilder();

		if (vu360UserService.hasAdministratorRole(loggedInUser)) {// get all roles for this customer
			builder.append("SELECT p FROM LMSRole p WHERE p.owner.id = :customerId");
		} else if (vu360UserService.hasTrainingAdministratorRole(loggedInUser)) {
			builder.append("SELECT p FROM LMSRole p WHERE p.owner.id = :customerId AND p.roleType != :role1 AND p.roleType  != :role2");
		}

		TypedQuery<LMSRole> query = entityManager.createQuery(builder.toString(), LMSRole.class);
		if (vu360UserService.hasAdministratorRole(loggedInUser)) {// get all roles for this customer
			query.setParameter("customerId", customer.getId());
		} else if (vu360UserService.hasTrainingAdministratorRole(loggedInUser)) {
			query.setParameter("customerId", customer.getId());
			query.setParameter("role1", LMSRole.ROLE_LMSADMINISTRATOR);
			query.setParameter("role2", LMSRole.ROLE_REGULATORYANALYST);
		}
		userList = query.getResultList();

		return userList;
	}

	public List<LMSRole> getLMSRolesByUserById(long id) {
		VU360User user = null;
		List<LMSRole> lmsRoleList = new ArrayList<LMSRole>();
		StringBuilder builder = new StringBuilder();

		builder.append("SELECT p FROM VU360User p WHERE p.id = :vu360Id");

		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("vu360Id", id);
		user = (VU360User) query.getSingleResult();
		if (user != null) {
			lmsRoleList.addAll(user.getLmsRoles());
		}

		return lmsRoleList;
	}

	@SuppressWarnings("unchecked")
	public List<LMSRole> getSystemRolesByCustomer(Customer customer) {
		List<LMSRole> userList = null;
		StringBuilder builder = new StringBuilder();

		builder.append("SELECT p FROM LMSRole p WHERE p.isSystemCreated = true AND p.owner.id = :customerId");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("customerId", customer.getId());
		userList = query.getResultList();

		return userList;
	}

	@SuppressWarnings("unchecked")
	public List<LMSRole> findRolesByName(String name, Customer customer,
			VU360User loggedInUser) {
		List<LMSRole> userList = null;
		StringBuilder builder = new StringBuilder();

		if (vu360UserService.hasAdministratorRole(loggedInUser)) {
			builder.append("SELECT p FROM LMSRole p WHERE p.owner.id = :customerId AND p.roleName LIKE :rolename ");
		} else if (vu360UserService.hasTrainingAdministratorRole(loggedInUser)) {
			builder.append("SELECT p FROM LMSRole p WHERE p.owner.id = :customerId AND p.roleName LIKE :rolename AND p.roleType != :role1 AND p.roleType != :role2");
		}

		Query query = entityManager.createQuery(builder.toString());
		
		if (vu360UserService.hasAdministratorRole(loggedInUser)) {
			query.setParameter("customerId", customer.getId());
			query.setParameter("rolename", "%"+name+"%");
			
		} else if (vu360UserService.hasTrainingAdministratorRole(loggedInUser)) {
			query.setParameter("customerId", customer.getId());
			query.setParameter("rolename", "%"+name+"%");
			query.setParameter("role1", LMSRole.ROLE_LMSADMINISTRATOR);
			query.setParameter("role2", LMSRole.ROLE_REGULATORYANALYST);
		}
		userList = query.getResultList();

		return userList;
	}
	
	public int checkNoOfBefaultReg(Customer customer) {

		int count = 0;
		StringBuilder builder = new StringBuilder();

		builder.append("SELECT count(p) from LMSRole p where p.owner.id = :customerId and p.roleType = :roleType and p.isDefaultForRegistration = :flag");
		Query query = entityManager.createQuery(builder.toString());
		query.setParameter("customerId", customer.getId());
		query.setParameter("roleType", LMSRole.ROLE_LEARNER);
		query.setParameter("flag", true);
		count=((Long)(query.getSingleResult())).intValue();
		return count;
	}

	public LMSRole getOptimizedBatchImportLearnerDefaultRole(Customer customer) {
		LMSRole role = null;

		String jpq = "SELECT p FROM LMSRole p JOIN FETCH p.lmsPermissions lp WHERE p.isDefaultForRegistration = true AND p.owner.id = :customerId";
		Query query = entityManager.createQuery(jpq);
		query.setParameter("customerId", customer.getId());
		@SuppressWarnings("unchecked")
		List<LMSRole> roleList = (List<LMSRole>) query.getResultList();
		if(roleList!=null && roleList.size()>0){
			if(roleList.size()>1){
				for (LMSRole lmsRole : roleList) {
					if(lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)){
						return lmsRole;
					}
				}
				return (LMSRole) roleList.get(0);
			}
			else{
				role=roleList.get(0);
			}
			return role;
			
		}
		else{
			return null;
		}
	}

	public Map<String, String> findDistinctEnabledFeatureFeatureGroupsForDistributorAndCustomer(Long distributorId,
			Long customerId) {
		
		Map<String, String> disabledFeature = new HashMap<String, String>();

		StringBuilder builder = new StringBuilder("SELECT featuregroup, featurecode\n" + 
				"FROM (\n" + 
				"        (\n" + 
				"          select distinct featuregroup, featurecode\n" + 
				"          from DISTRIBUTORLMSFEATURE DISTRIBUTORLMSFEATURE \n" + 
				"          left outer join LMSFEATURE LMSFEATURE on DISTRIBUTORLMSFEATURE.LMSFEATURE_ID=LMSFEATURE.id \n" + 
				"          where DISTRIBUTORLMSFEATURE.distributor_id= :distributorId\n" + 
				"          and DISTRIBUTORLMSFEATURE.ENABLEDTF=0\n" + 
				"        ) \n" + 
				"        UNION \n" + 
				"        (\n" + 
				"          select distinct featuregroup, featurecode\n" + 
				"          from CUSTOMERLMSFEATURE CUSTOMERLMSFEATURE \n" + 
				"          left outer join LMSFEATURE LMSFEATURE on CUSTOMERLMSFEATURE.LMSFEATURE_ID=LMSFEATURE.id \n" + 
				"          where CUSTOMERLMSFEATURE.CUSTOMER_id= :customerId\n" + 
				"          and CUSTOMERLMSFEATURE.ENABLEDTF=0\n" + 
				"        )\n" + 
				"      ) as disabledfeatures");

		
		Query query = entityManager.createNativeQuery(builder.toString());
		query.setParameter("distributorId", distributorId);
		query.setParameter("customerId", customerId);
		List<Object[]> results = query.getResultList();
		
		if(results != null) {
			results.forEach(r -> {
				disabledFeature.put(r[0].toString(), r[1].toString());
			});
		}
		
		return disabledFeature;
	}

	public Map<String, String> countLearnerByRoles(Long [] roleIds){
		if(roleIds == null || roleIds.length<=0) { return null;}
		StringBuffer strBuffSQL= new StringBuffer();
		strBuffSQL.append(" select R.id RoleId, count(UR.USER_ID) as LearnerCount  ");
		strBuffSQL.append(" from VU360USER_ROLE UR inner join LMSROLE R on UR.ROLE_ID=R.id ");
		strBuffSQL.append(" where R.id in (:arrRoleIds) ");
		strBuffSQL.append(" group by R.id ");
		
		Query qry=entityManager.createNativeQuery(strBuffSQL.toString());
		qry.setParameter("arrRoleIds", Arrays.asList(roleIds));
		
		Map<String, String> mapA = new HashMap<String, String>();
		List<Object[]> roleList = qry.getResultList();
		if(roleList!=null && roleList.size()>0){
			for(Object[] objects : roleList){
				mapA.put(objects[0].toString(), objects[1].toString());
			}
		}
		return mapA;
	}

	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
}