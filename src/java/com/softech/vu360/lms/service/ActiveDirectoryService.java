package com.softech.vu360.lms.service;

import com.softech.vu360.lms.model.ActiveDirectoryUser;
import com.softech.vu360.lms.model.VU360User;

public interface ActiveDirectoryService {
	public boolean isADIntegrationEnabled();
	public ActiveDirectoryUser updateUser(VU360User updatedUser);
	public ActiveDirectoryUser updateUser(com.softech.vu360.lms.vo.VU360User updatedUser);
	public ActiveDirectoryUser addUser(VU360User user);
	public ActiveDirectoryUser addUser(com.softech.vu360.lms.vo.VU360User user);
	public boolean authenticateADUser(String user, String password);
	public boolean findADUser(String username);//Wajahat


}
