package com.softech.vu360.lms.repositories;

import java.util.List;

import com.softech.vu360.lms.model.ManageUserStatus;


public interface AuthorRepositoryCustom {
	public boolean isAuthor(Long userId);
	boolean isThisAuthor(String userName);
	List<Object[]> getUserStatus(ManageUserStatus criteria);
}
