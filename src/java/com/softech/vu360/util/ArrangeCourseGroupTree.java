package com.softech.vu360.util;

import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;

public class ArrangeCourseGroupTree {
	
	public static TreeNode getCourseGroupTree(TreeNode parentNode, CourseGroup orgGroup, List<CourseGroup> selectedCourseGroups){
		boolean isEnabled=true;
		if(orgGroup!=null){
			TreeNode node = new TreeNode(orgGroup);
			/*
			if(selectedOrgGroups !=null )
			for(Long selectedId:selectedOrgGroups){
				if(orgGroup.getId().longValue() == selectedId.longValue()){
					node.setSelected(true);
					break;
				}
			}
			if(!loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
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
			*/	
			//node.setSelected(true);
			node.setEnabled(true);
				
			List<CourseGroup> childGroups = orgGroup.getChildrenCourseGroups();
			//if(selectedCourseGroups != null){
				for(int i=0; i<childGroups.size(); i++){
					node = getCourseGroupTree(node, childGroups.get(i),selectedCourseGroups);
					
				}
			//}
			
			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}
	
	
	public static TreeNode getSelectedCourseGroupTree(TreeNode parentNode, CourseGroup courseGroup, String[] selectedCourseGroups){
		
		if(courseGroup!=null){
			TreeNode node = new TreeNode(courseGroup);
			
			if(selectedCourseGroups !=null )
			for(String selectedId:selectedCourseGroups){
				if(courseGroup.getId().longValue() == Long.valueOf(selectedId)){
					node.setSelected(true);
					break;
				}
			}
			/*
			if(!loggedInUser.isLMSAdministrator() && !loggedInUser.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
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
			*/	
			//node.setSelected(true);
			node.setEnabled(true);
				
			List<CourseGroup> childGroups = courseGroup.getChildrenCourseGroups();
			//if(selectedCourseGroups != null){
				for(CourseGroup childGroup : childGroups){
					node = getSelectedCourseGroupTree(node, childGroup, selectedCourseGroups);
					
				}
			//}
			
			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}
	
	public static TreeNode getSelectedCourseTree(TreeNode parentNode, Course course, String[] selectedCourses){
		
		if(course!=null){
			TreeNode node = new TreeNode(course);
			
			if(selectedCourses !=null )
			for(String selectedId:selectedCourses){
				if(course.getId().longValue() == Long.valueOf(selectedId)){
					node.setSelected(true);
					break;
				}
			}
		
			node.setEnabled(true);
				
			
			
				
			
			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}
	
	public static TreeNode getCourseGroupTreeNodeForCourse(CourseGroup courseGroup, Course course){

		if (courseGroup != null) {
			TreeNode node = new TreeNode(courseGroup);
			node.setEnabled(true);
			
			if (course != null) {
				TreeNode childNode = new TreeNode(course);
				node.addChild(childNode);
				childNode.setEnabled(true);
			}
			
			while (courseGroup.getParentCourseGroup() != null) {
				CourseGroup parentCourseGroup = courseGroup.getParentCourseGroup();
				TreeNode parentNode = new TreeNode(parentCourseGroup);
				//node.setParent(parentNode);
				parentNode.addChild(node);
				parentNode.setEnabled(true);
				node = parentNode;
				courseGroup = parentCourseGroup;
			}
			
			return node;
		}
		
		return null;
	}
	
	/**
	 * @author 		noman.liaquat	
	 * @return 		TreeNode
	 * 
	 */
	public static TreeNode getCourseGroupTreeNodeForCourse(
			CourseGroup courseGroup, 
			Course course, 
			CourseAndCourseGroupService courseAndCourseGroupService){

		if (courseGroup != null) {
			TreeNode courseGroupNode = new TreeNode(courseGroup);
			courseGroupNode.setEnabled(true);
			
			if (course != null) {
				TreeNode childCourseGroupNode = new TreeNode(course);
				courseGroupNode.addChild(childCourseGroupNode);
				childCourseGroupNode.setEnabled(true);
			}
			
			while (courseGroup.getParentCourseGroup() != null) {
				CourseGroup parentCourseGroup = courseGroup.getParentCourseGroup();
				parentCourseGroup.setCourses( courseAndCourseGroupService.getActiveCourses(parentCourseGroup.getId()));
				TreeNode parentCourseGroupNode = new TreeNode(parentCourseGroup);
				parentCourseGroupNode.addChild(courseGroupNode);
				parentCourseGroupNode.setEnabled(true);
				courseGroupNode = parentCourseGroupNode;
				courseGroup = parentCourseGroup;
			}
			
			return courseGroupNode;
		}
		
		return null;
	}
	
	public static TreeNode getCourseGroupTreeNodeForCourses(CourseGroup courseGroup, List<Course> courses){

		if (courseGroup != null) {
			TreeNode node = new TreeNode(courseGroup);
			node.setEnabled(true);
			
			//if (course != null) {
			for(Course course : courses){
				TreeNode childNode = new TreeNode(course);
				node.addChild(childNode);
				childNode.setEnabled(true);
			}
			
			while (courseGroup.getParentCourseGroup() != null) {
				CourseGroup parentCourseGroup = courseGroup.getParentCourseGroup();
				TreeNode parentNode = new TreeNode(parentCourseGroup);
				//node.setParent(parentNode);
				parentNode.addChild(node);
				parentNode.setEnabled(true);
				node = parentNode;
				courseGroup = parentCourseGroup;
			}
			
			return node;
		}
		
		return null;
	}
	public static boolean traverseTreeToAddCourse(TreeNode courseGroupTreeNode, CourseGroup courseGroup, Course course, List<CourseGroup> childCourseGroups) {
		
		if (courseGroupTreeNode != null) {
			if (courseGroupTreeNode.getValue() instanceof CourseGroup) {
				List<TreeNode> treeNodeChildren = courseGroupTreeNode.getChildren();
				if (courseGroupTreeNode.getValue().equals(courseGroup)) {
					
					
					//LMS-15206 -- Commenting out following 9 lines because child COURSE GROUP can hold same COURSE that exists in his parent COURSE GROUP 
					
					//This means that the course is to be added under this course group
					//But first check its course children to see if the course already exists to avoid duplicates.		
//					if (treeNodeChildren != null) {
//						for (TreeNode courseGroupChildNode : treeNodeChildren) {
//							if ((courseGroupChildNode.getValue() instanceof Course)) {
//								if (courseGroupChildNode.getValue().equals(course)) {
//									return true;
//								}
//							}
//						}
//					}
					
					TreeNode nodeToAddChild = courseGroupTreeNode;
					for (int index=childCourseGroups.size()-1;index>=0;index--) {
						CourseGroup childCourseGroup = childCourseGroups.get(index);
						TreeNode childCourseGroupTreeNode = new TreeNode(childCourseGroup);
						nodeToAddChild.addChild(childCourseGroupTreeNode);
						childCourseGroupTreeNode.setEnabled(true);
						nodeToAddChild = childCourseGroupTreeNode;
					}
					TreeNode newCourseTreeNode = new TreeNode(course);
					nodeToAddChild.addChild(newCourseTreeNode);
					newCourseTreeNode.setEnabled(true);
					return true;
				}
				else {
					if (treeNodeChildren != null) {
						for (TreeNode courseGroupChildNode : treeNodeChildren) {
							if (traverseTreeToAddCourse(courseGroupChildNode, courseGroup, course, childCourseGroups)) {
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean traverseTreeToAddCourseGroup(TreeNode courseGroupTreeNode, CourseGroup courseGroup, List<CourseGroup> childCourseGroups) {
		
		if (courseGroupTreeNode != null) {			
			if (courseGroupTreeNode.getValue().equals(courseGroup)) {

					TreeNode nodeToAddChild = courseGroupTreeNode;
					for (int index=childCourseGroups.size()-1;index>=0;index--) {
						CourseGroup childCourseGroup = childCourseGroups.get(index);
						TreeNode childCourseGroupTreeNode = new TreeNode(childCourseGroup);
						nodeToAddChild.addChild(childCourseGroupTreeNode);
						childCourseGroupTreeNode.setEnabled(true);
						nodeToAddChild = childCourseGroupTreeNode;
					}				
					return true;
				}
				else {
				List<TreeNode> treeNodeChildren = courseGroupTreeNode.getChildren();
				if (treeNodeChildren != null) {
					for (TreeNode courseGroupChildNode : treeNodeChildren) {
						if ((courseGroupChildNode.getValue() instanceof CourseGroup)&& traverseTreeToAddCourseGroup(courseGroupChildNode, courseGroup, childCourseGroups)) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
}
