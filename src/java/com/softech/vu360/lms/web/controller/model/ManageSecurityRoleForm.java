package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class ManageSecurityRoleForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public ManageSecurityRoleForm() {
	}
	
	private List<LMSRole> allUserRoles = new ArrayList <LMSRole>();

	public List<LMSRole> getAllUserRoles() {
		return allUserRoles;
	}

	public void setAllUserRoles(List<LMSRole> allUserRoles) {
		this.allUserRoles = allUserRoles;
	}
	
}