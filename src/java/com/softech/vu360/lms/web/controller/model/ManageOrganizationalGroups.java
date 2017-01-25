package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.lms.web.controller.manager.ManageLearnerController;

/**
 * 
 * @author Dyutiman
 *
 */
public class ManageOrganizationalGroups extends HashMap<String, Object> implements ILMSBaseInterface {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(ManageLearnerController.class.getName());
	private Map<Object, Object> internalMap = new HashMap<Object, Object>();
	private int row = 0;
	private int column = 0;
	private LearnerService learnerService;

	public List<Map> createOrgGroupView(OrganizationalGroup rootOrgGroup, List<OrganizationalGroup> selectedOrgGroups
			, List<OrganizationalGroup> managableOrgGroups){

		List<Map> OrgGroupViews = new ArrayList<Map>();

		List<Map> arrangedChildrenOrgGroups = new ArrayList<Map>();

		internalMap = new HashMap<Object, Object>();
		internalMap.put("row", 0);
		internalMap.put("column", 0);
		internalMap.put("value", rootOrgGroup);
		internalMap.put("isChecked", false);
		arrangedChildrenOrgGroups.add(internalMap);
		arrangeInTree(rootOrgGroup,arrangedChildrenOrgGroups);

		if(selectedOrgGroups != null){
			OrgGroupViews = compareOrgGroups(arrangedChildrenOrgGroups,selectedOrgGroups);
		}else{
			OrgGroupViews = arrangedChildrenOrgGroups;
		}

		if(managableOrgGroups != null){
			OrgGroupViews = checkManagableOrgGroups(arrangedChildrenOrgGroups,managableOrgGroups);
		}else{
			OrgGroupViews = arrangedChildrenOrgGroups;
		}

		return OrgGroupViews;
	}


	public List<Map> arrangeInTree(OrganizationalGroup orgGroup, List<Map> groupsInTree) {
		internalMap = new HashMap<Object, Object>();
		row++;
		if(orgGroup.getChildrenOrgGroups().size() != 0) {
			column++;
			for(int groupNumber=0; groupNumber<orgGroup.getChildrenOrgGroups().size(); groupNumber++) {
				internalMap.put("row", row);
				internalMap.put("column", column);
				internalMap.put("value", orgGroup.getChildrenOrgGroups().get(groupNumber));
				internalMap.put("isChecked", false);
				internalMap.put("isDisabled", true);
				groupsInTree.add(internalMap);
				arrangeInTree(orgGroup.getChildrenOrgGroups().get(groupNumber), groupsInTree);
				column--;
			}
		}
		else {
			column++;
			return groupsInTree;
		}
		return null;
	}

	public List<Map> compareOrgGroups(List<Map> groupsInTree, List<OrganizationalGroup> selectedOrgGroups) {
		for( int orgGroupNumber=0; orgGroupNumber<groupsInTree.size(); orgGroupNumber++ ){
			Long id = ((OrganizationalGroup)groupsInTree.get(orgGroupNumber).get("value")).getId();
			for( int selectedOrgGroupNumber=0; selectedOrgGroupNumber<selectedOrgGroups.size(); selectedOrgGroupNumber++ ) {
				if(selectedOrgGroups.get(selectedOrgGroupNumber).getId().equals(id)) {
					//log.debug("GOT -  "+((OrganizationalGroup)groupsInTree.get(orgGroupNumber).get("value")).getName());
					groupsInTree.get(orgGroupNumber).put("isChecked", true);
				}
			}
		}
		return groupsInTree;
	}

	public List<Map> checkManagableOrgGroups(List<Map> groupsInTree, List<OrganizationalGroup> managableOrgGroups) {
		for( int orgGroupNumber=0; orgGroupNumber<groupsInTree.size(); orgGroupNumber++ ){
			groupsInTree.get(orgGroupNumber).put("isDisabled", false);
			Long id = ((OrganizationalGroup)groupsInTree.get(orgGroupNumber).get("value")).getId();
			for( int selectedOrgGroupNumber=0; selectedOrgGroupNumber<managableOrgGroups.size(); selectedOrgGroupNumber++ ) {
				if(managableOrgGroups.get(selectedOrgGroupNumber).getId().equals(id)) {
					//FIXME
					log.debug("MANAGABLE ORG GROUP - "+((OrganizationalGroup)groupsInTree.get(orgGroupNumber).get("value")).getName());
					groupsInTree.get(orgGroupNumber).put("isDisabled", true);
				} 
			}
		}
		return groupsInTree;
	}

/*	public List<LearnerGroup> searchLearnerGrops( OrganizationalGroup orgGroup, List<LearnerGroup>learnerGroups ) {
		if( orgGroup.getLearnerGroups() != null ) {
			learnerGroups.addAll(orgGroup.getLearnerGroups());
		}
		if( orgGroup.getParentOrgGroup() != null ) {
			searchLearnerGrops(orgGroup.getParentOrgGroup(),learnerGroups);
		} else {
			return learnerGroups;
		}
		return null;
	}*/

	public List<LearnerGroup> compareLearnerGroups( List<LearnerGroup>selectedLearnerGroupsAssociatedWithOrgGroup,
			List<LearnerGroup>selectedLearnerGroups) {
		for( int selectedLearnerGroupNo=0; selectedLearnerGroupNo<selectedLearnerGroups.size(); selectedLearnerGroupNo++) {
			for( int learnerGroupNo=0; learnerGroupNo<selectedLearnerGroupsAssociatedWithOrgGroup.size(); learnerGroupNo++) {
				if(selectedLearnerGroupsAssociatedWithOrgGroup.get(learnerGroupNo).getId().equals(selectedLearnerGroups.get(selectedLearnerGroupNo).getId())) {
					// removing learner group.
					selectedLearnerGroupsAssociatedWithOrgGroup.remove(learnerGroupNo);
				}
			}
		}
		return selectedLearnerGroupsAssociatedWithOrgGroup;
	}

	/**
	 * @author arijit
	 * this method calculates the number of learners of each org group
	 * @param listOrganizationalGroup1
	 * @param listVU360User
	 * @return Map<Object, Object>
	 */
	public Map<Object, Object> getNumberofMembersbyOrgGroup(List<VU360User> listVU360User) {
		Map map = new HashMap<Integer, Integer>();
		//TODO will implement after new DAO written comment on 02/04/09.
		/*for(int i=0;i<listVU360User.size();i++){
			VU360User vu360User = listVU360User.get(i);
			Learner learner = vu360User.getLearner();
			if(learner!=null){
				List<OrganizationalGroup> listOrganizationalGroup2 = learner.getOrganizationalGroups();
				for(int j=0;j<listOrganizationalGroup2.size();j++){
					OrganizationalGroup orgGroup2=listOrganizationalGroup2.get(j);
					if(i==0){
						map.put(orgGroup2.getId(),1);
					}else{
						if(map.get(orgGroup2.getId()) == null){
							map.put(orgGroup2.getId(),0);
						}
						map.put(orgGroup2.getId(), ((Integer)map.get(orgGroup2.getId())).intValue()+1);
					}
				}
			}
		}*/
		//TODO will implement after new DAO written comment on 02/04/09.
		return map;
	}

	/**
	 * @author arijit
	 * this method finds members of each org group
	 * @param OrganizationalGroup
	 * @param listVU360User
	 * @return List<Learner>
	 */
	public List<Learner> getMembersbyOrgGroup(OrganizationalGroup organizationalGroup, List<VU360User> listVU360User) {
		List<Learner> listLearners = new ArrayList();
		//TODO will implement after new DAO written comment on 02/04/09.
	/*	for(int i=0;i<listVU360User.size();i++){
			VU360User vu360User = listVU360User.get(i);
			Learner learner = vu360User.getLearner();
			if(learner!=null){
				List<OrganizationalGroup> listOrganizationalGroup = learner.getOrganizationalGroups();
				for(int j=0;j<listOrganizationalGroup.size();j++){
					if(organizationalGroup.getId() == listOrganizationalGroup.get(j).getId()){
						listLearners.add(learner);
					}
				}
			}
		}*/
		//TODO will implement after new DAO written comment on 02/04/09.
		return listLearners;
	}

	/**
	 * @author arijit
	 * this method finds all Learner groups
	 * @param listOrganizationalGroup
	 * @return List<LearnerGroup>
	 */
/*	public List<LearnerGroup> getAllLearnerGroups(List<OrganizationalGroup> listOrganizationalGroup) {
		Set<LearnerGroup> setOfLearnerGroups = new HashSet<LearnerGroup>();
		for(OrganizationalGroup og:listOrganizationalGroup){
			setOfLearnerGroups.addAll(og.getLearnerGroups());
		}
		// TODO should re-factor the return type of this method from List to Set 
		List<LearnerGroup> listOfLearnerGroups = new ArrayList<LearnerGroup>();
		listOfLearnerGroups.addAll(setOfLearnerGroups);
		return listOfLearnerGroups;
	}
*/	
	/**
	 * @author arijit
	 * this method calculates the number of learners of each Learner group
	 * @param listLearnerGroup1
	 * @param listVU360User
	 * @return Map<Object, Object>
	 */
	public Map<Object, Object> getNumberofMembersbyLearnerGroup(List<VU360User> listVU360User) {
		Map map = new HashMap<Integer, Integer>();
		//TODO will implement after new DAO written comment on 02/04/09.
		/*for(int i=0;i<listVU360User.size();i++){
			VU360User vu360User = listVU360User.get(i);
			Learner learner = vu360User.getLearner();
			if(learner!=null){
				List<LearnerGroup> listLearnerGroup2 = learner.getLearnerGroups();
				for(int j=0;j<listLearnerGroup2.size();j++){
					LearnerGroup learnerGroup2=listLearnerGroup2.get(j);
					if(i==0){
						map.put(learnerGroup2.getId(),1);
					}else{
						if(map.get(learnerGroup2.getId()) == null){
							map.put(learnerGroup2.getId(),0);
						}
						map.put(learnerGroup2.getId(), ((Integer)map.get(learnerGroup2.getId())).intValue()+1);
					}
				}
			}
		}*/
		//TODO will implement after new DAO written comment on 02/04/09.
		return map;
	}
	
	/**
	 * @author arijit
	 * this method finds members of each learner group
	 * @param LearnerGroup
	 * @param listVU360User
	 * @return List<Learner>
	 */
	public List<Learner> getMembersbyLearnerGroup(LearnerGroup learnerGroup, List<VU360User> listVU360User) {
		List<Learner> listLearners = new ArrayList();
		//TODO will implement after new DAO written comment on 02/04/09.
		/*for(int i=0;i<listVU360User.size();i++){
			VU360User vu360User = listVU360User.get(i);
			Learner learner = vu360User.getLearner();
			if(learner!=null){
				List<LearnerGroup> listLearnerGroup = learner.getLearnerGroups();
				for(int j=0;j<listLearnerGroup.size();j++){
					if(learnerGroup.getId() == listLearnerGroup.get(j).getId()){
						listLearners.add(learner);
					}
				}
			}
		}*/
		//TODO will implement after new DAO written comment on 02/04/09.
		return listLearners;
	}
}