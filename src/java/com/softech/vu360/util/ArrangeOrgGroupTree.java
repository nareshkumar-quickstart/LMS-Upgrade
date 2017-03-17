package com.softech.vu360.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.VU360UserService;

@Component
public class ArrangeOrgGroupTree {
	
	@Autowired
	private VU360UserService _vu360UserService;
	
	private static VU360UserService vu360UserService;
	
	@PostConstruct
	private void init() {
		ArrangeOrgGroupTree.vu360UserService = this._vu360UserService;
	}
	
	public static TreeNode getOrgGroupTree(TreeNode parentNode, OrganizationalGroup orgGroup, List<Long> selectedOrgGroups,VU360User loggedInUser){
		boolean isEnabled=true;
		if(orgGroup!=null){
			TreeNode node = new TreeNode(orgGroup);
			if(selectedOrgGroups !=null )
			for(Long selectedId:selectedOrgGroups){
				if(orgGroup.getId().longValue() == selectedId.longValue()){
					node.setSelected(true);
					break;
				}
			}
			if(!vu360UserService.hasAdministratorRole(loggedInUser) && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
				if( loggedInUser.getTrainingAdministrator().getManagedGroups().size()>0) {
					for(OrganizationalGroup og : loggedInUser.getTrainingAdministrator().getManagedGroups()) {
						if(	og.getId().longValue() == orgGroup.getId().longValue()) {
							isEnabled=true;
							break;
						}
						else
							isEnabled=false;
					}
				}else {
					isEnabled=false;
				}
					
				
			}
			if(isEnabled)
				node.setEnabled(true);
			else
				node.setEnabled(false);
			//Using hashset to remove any possible duplication
			List<OrganizationalGroup> childGroups = new ArrayList<OrganizationalGroup>(new HashSet<OrganizationalGroup>(orgGroup.getChildrenOrgGroups()));
			Collections.sort(childGroups);
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i),selectedOrgGroups,loggedInUser);
			}
			
			
			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}
	

}
