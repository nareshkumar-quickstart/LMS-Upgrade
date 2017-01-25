package com.softech.vu360.lms.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.UserWidget;

/**
 * 
 * @author haider.ali
 *
 */

public class UserWidgetRepositoryImpl implements UserWidgetRepositoryCustom {

	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Override
	@Transactional
	public UserWidget saveUserWidget(UserWidget userWidget) {
		return entityManager.merge(userWidget);
	}

}
