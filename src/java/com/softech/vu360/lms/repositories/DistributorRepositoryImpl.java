package com.softech.vu360.lms.repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;

/**
 * 
 * @author haider.ali
 *
 */
public class DistributorRepositoryImpl implements DistributorRepositoryCustom {

	
	@Inject
	LMSAdministratorAllowedDistributorRepository  lMSAdministratorAllowedDistributorRepository;
	
	@Inject
	DistributorRepository distributorRepository;

	@PersistenceContext
	protected EntityManager entityManager;


	public List<Distributor> getAllowedDistributorByGroupId(String groupId,String administratorId){

		List<Distributor> distributorList = null;
		List<LMSAdministratorAllowedDistributor> allowedDistributors = lMSAdministratorAllowedDistributorRepository.findByDistributorGroupIdAndLmsAdministratorId(Long.parseLong(groupId), Long.parseLong(administratorId));
			
/*			Collection<Integer> ids = Collections2.transform(allowedDistributors, new Function<LMSAdministratorAllowedDistributor, Integer>() {
						public Integer apply(LMSAdministratorAllowedDistributor arg0) {
							// TODO Auto-generated method stub
							return Integer.parseInt(arg0.getAllowedDistributorId().toString());
						}
					}
			);
*/
			//TODO OPTIMIZE BELOW CODE AS ABOVE GIVEN (USING Google Guava ).
			Set<Long> uniqueIds = new HashSet<Long>();
			for(int i=0;i<allowedDistributors.size();i++){
				uniqueIds.add(allowedDistributors.get(i).getAllowedDistributorId());
			}
			
			if(!uniqueIds.isEmpty()){
				distributorList =  (List<Distributor>) distributorRepository.findAll(uniqueIds);
			}
			
		
		return distributorList;
	}
	
	
	@Override
	public void deleteDistributorByGroupIdAndAdministratorIdAndAllowedDistributorId(String groupId, String administratorId, String allowedDistributorId) {
		LMSAdministratorAllowedDistributor d = lMSAdministratorAllowedDistributorRepository.findByDistributorGroupIdAndLmsAdministratorIdAndAllowedDistributorId(Long.parseLong(groupId), Long.parseLong(administratorId),  Long.parseLong(allowedDistributorId) );
		lMSAdministratorAllowedDistributorRepository.delete(d);
	}



	
	@Override
	public Page<Distributor> findDistributorsByName(RepositorySpecificationsBuilder<Distributor> repositorySpecificationsBuilder, PageRequest pageRequest) {
		
			Page<Distributor> page = distributorRepository.findAll( repositorySpecificationsBuilder.build(), pageRequest);
			return page;

	}
	
	
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}






	


	
}
