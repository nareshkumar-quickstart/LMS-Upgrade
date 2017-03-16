package com.softech.vu360.lms.web.controller.jason;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;


public class GetLearnerGroupController extends MultiActionController{
	private LearnerService learnerService = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService=null;
	private static final Logger log = Logger.getLogger(GetLearnerGroupController.class.getName());

	
	public void getLearnerGroupByOrgGroup(HttpServletRequest request,HttpServletResponse response) throws Exception {
		//if(request.getMethod().equals("POST")){
		List<LearnerGroup> learnerGroups = new ArrayList<LearnerGroup>();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
		.getAuthentication().getPrincipal();
			JSONObject jsonObject = new JSONObject();
			//LearnerGroup learnerGroup = new LearnerGroup();
			String idStr = request.getParameter("groups")==null?"0":request.getParameter("groups");
			String[] orgGroupIdArray;
			
			String delimeter = ",";
			
			orgGroupIdArray = idStr.split(delimeter);
			List<OrganizationalGroup> orgGroups =orgGroupLearnerGroupService.getOrgGroupsById(orgGroupIdArray);

			//OrganizationalGroup orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(idStr));
			//learnerService.get
			//List<LearnerGroup> learnerGroupList = orgGroup.getLearnerGroups();
			JSONArray jsonFeedEntries =new JSONArray();
			//List<LearnerGroup> learnerGroups = orgGroup.getLearnerGroups();
			
		//////////tapas
			//learnerGroups=orgGroup.getLearnerGroups();
			Set<LearnerGroup> set = new HashSet<LearnerGroup>();
			List<LearnerGroup> ogLearnerGroups = null;
			for(OrganizationalGroup orgGroup :orgGroups){
				//set.addAll(orgGroup.getLearnerGroups());
				ogLearnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByOrgGroup(orgGroup.getId());
				set.addAll(ogLearnerGroups);
				if(loggedInUser.isAdminMode() || loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
					while (orgGroup.getParentOrgGroup() != null) {
			               
						orgGroup=orgGroup.getParentOrgGroup();
						//Set<LearnerGroup> lgs= orgGroup.getLearnerGroups();
						List<LearnerGroup> lgs = orgGroupLearnerGroupService.getLearnerGroupsByOrgGroup(orgGroup.getId());
						if(lgs != null && lgs.size()>0){
							learnerGroups.addAll(lgs);
							set.addAll(lgs);
						}
			       }
				}
				
			}
		/*	set.addAll(orgGroup.getLearnerGroups());
			
			while (orgGroup.getParentOrgGroup() != null) {
	               
				orgGroup=orgGroup.getParentOrgGroup();
				List<LearnerGroup> lgs= orgGroup.getLearnerGroups();
				if(lgs != null && lgs.size()>0){
					learnerGroups.addAll(lgs);
					set.addAll(lgs);
				}
	       }*/
			// Iterating over the elements in the set
		    Iterator it = set.iterator();
		    while (it.hasNext()) {
		        // Get element
		    	LearnerGroup learnerGroup =(LearnerGroup) it.next();
		        
		        learnerGroups.add(learnerGroup);
		    }



			///////tapas
			//this is not required
			/*ManageOrganizationalGroups arrangedOrgGroup = new ManageOrganizationalGroups();
			arrangedOrgGroup.searchLearnerGrops(orgGroup,learnerGroups);
			
			// Deleting learnerGroups in case of there is any repetition
			for(int learnerGroupNo=0; learnerGroupNo<learnerGroups.size(); learnerGroupNo++) {
				for(int repeatedLearnerGroupNo=0; repeatedLearnerGroupNo<learnerGroups.size(); repeatedLearnerGroupNo++) {
					if( repeatedLearnerGroupNo != learnerGroupNo ) {
						if(learnerGroups.get(repeatedLearnerGroupNo).getId() == learnerGroups.get(learnerGroupNo).getId()){
							log.debug("deleted learner group:: "+learnerGroups.get(repeatedLearnerGroupNo).getName());
							learnerGroups.remove(repeatedLearnerGroupNo);
						}
					}
				}
			}
*/
			
			for(int i=0;i<learnerGroups.size();i++){
				JSONObject jsonObject1 = new JSONObject();			
				jsonObject1.put("id", learnerGroups.get(i).getId());
				jsonObject1.put("name", learnerGroups.get(i).getName());            
				jsonFeedEntries.add(jsonObject1);
			}
			jsonObject.put("entries",jsonFeedEntries);
			response.setHeader("Content-Type", "application/json ; charset= UTF-8");
			PrintWriter writer = response.getWriter();        
			writer.write(jsonObject.toString());
		//}else{
		//}
	}


	public LearnerService getLearnerService() {
		return learnerService;
	}


	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}


	/**
	 * @return the orgGroupLearnerGroupService
	 */
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}


	/**
	 * @param orgGroupLearnerGroupService the orgGroupLearnerGroupService to set
	 */
	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}
}



