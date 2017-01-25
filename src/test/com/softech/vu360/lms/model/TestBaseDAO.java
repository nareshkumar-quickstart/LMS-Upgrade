package com.softech.vu360.lms.model;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml"})
public abstract class TestBaseDAO<T> extends AbstractJUnit4SpringContextTests {
    public TestBaseDAO() {
        super();
    }
    
    @PersistenceUnit(unitName="lmsPersistenceUnit")
	EntityManagerFactory emf;
    
	
	public T getById(Long id,Class<T> klass) {
		EntityManager entityManager = emf.createEntityManager();
		return entityManager.find(klass, id);
	}

	@Transactional
	public T save(T entity) {
		
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entity = entityManager.merge(entity);
		entityManager.getTransaction().commit();
		return entity;
	}
	
	public Object crudSave(Class clazz, Object obj){
		try{
			EntityManager entityManager = emf.createEntityManager();
			entityManager.getTransaction().begin();
			Object savedObj  = (clazz.cast(entityManager.merge((clazz.cast(obj)))));
			entityManager.getTransaction().commit();
			
			return savedObj;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Object crudFindById(Class clazz, Object obj){
		try{
			EntityManager entityManager = emf.createEntityManager();
			return entityManager.find(clazz, obj);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List getAll(String entityName,Class klass) {
		EntityManager entityManager = emf.createEntityManager();
		List result = null;
		try{
			String SQL_QUERY = "select p from "+entityName+" p ";
			
			Query query = entityManager.createQuery(SQL_QUERY, klass);
			
			query.setFirstResult(0);
			query.setMaxResults(5);
			//query.setParameter("username", "wajahat");
			result = query.getResultList();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

	
	@Transactional
	public T update(T entity) {
		return save(entity);
	}
	
	@Transactional
	public void persist(T entity) throws DataAccessException {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(entity);
		entityManager.getTransaction().commit();
	}

	@Transactional
	public void delete(T entity) {
		EntityManager entityManager = emf.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.remove(entity);
		entityManager.getTransaction().commit();
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

	public void setEmf(EntityManagerFactory emf) {
		this.emf = emf;
	}

}