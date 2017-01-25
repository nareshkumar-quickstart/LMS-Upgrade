package com.softech.vu360.lms.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author marium.saud
 * This Unit Test contains insert/update/delete operations for LearnerGroupMember that sues Embedded Class LearnerGroupMemberPK
 * 
 * 
 */
@Transactional
public class LearnerGroupMemberTest extends TestBaseDAO<LearnerGroupMember> {
	
	//@Test
	public void LearnerGroupMember_should_save(){
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		LearnerGroupMember member=new LearnerGroupMember();
		Learner learner= entityManager.find(Learner.class, new Long(111));
		
		LearnerGroup group=entityManager.find(LearnerGroup.class, new Long(254));
		member.setLearner(learner);
		member.setLearnerGroup(group);

		entityManager.persist(member);
		entityManager.getTransaction().commit();
	}
	
	@Test
	public void LearnerGroupMember_should_update(){
		LearnerGroupMember member;
		EntityManager entityManager = emf.createEntityManager();
		
		Query qry=entityManager.createQuery("select p from LearnerGroupMember p where p.learner.id=:id");
		qry.setParameter("id", new Long(111130));
		member=(LearnerGroupMember)qry.getSingleResult();

		entityManager.getTransaction().begin();
		entityManager.remove(member);
		entityManager.getTransaction().commit();
		
	}

	
	@PersistenceUnit(unitName="lmsPersistenceUnit")
	EntityManagerFactory emf;
	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

}
