package com.softech.vu360.lms.service;

import java.util.List;

import com.softech.vu360.lms.model.ManageUserStatus;


/**
 * 
 * @author haider.ali
 *
 */
public interface ManageUserStatusService {
	public List<ManageUserStatus> getUserStatusList(ManageUserStatus criteria);
}
