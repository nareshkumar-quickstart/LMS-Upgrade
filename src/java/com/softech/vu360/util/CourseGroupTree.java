package com.softech.vu360.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.softech.vu360.lms.model.CourseGroup;

public class CourseGroupTree {

	List<TreeNode> rootNodes = new ArrayList<TreeNode>();
	HashMap<Long, TreeNode> map = new HashMap<Long, TreeNode>();
	boolean activeTF;
	public CourseGroupTree(boolean activeTF){
		this.activeTF = activeTF;
	}
	/*
	 * This method will take course group as an argument and create hierarchies
	 * of all parents
	 */
	private TreeNode createParentNodes(CourseGroup cg, TreeNode childNode) {
		TreeNode mapEntry = map.get(cg.getId());
		if (mapEntry == null) {
			TreeNode node = new TreeNode();
			node.setValue(cg);
			node.addChild(childNode);
			node.setEnabled(activeTF);
			childNode.setParent(node);
			map.put(cg.getId(), node);
			if (cg.getParentCourseGroup() == null) {
				rootNodes.add(node);
				return node;
			} else {
				return createParentNodes(cg.getParentCourseGroup(), node);
			}
		} else {
			mapEntry.addChild(childNode);
			childNode.setParent(mapEntry);
			return mapEntry;
		}
	}

	public TreeNode addNode(CourseGroup cg) {
		TreeNode existNode = map.get(cg.getId());
		if (existNode != null)// it means this coursegroup already exist
		{
			return existNode;
		} else {
			// this means this node doesn't exist
			TreeNode cgNode = new TreeNode(cg);
			cgNode.setValue(cg);
			cgNode.setEnabled(activeTF);
			map.put(cg.getId(), cgNode);
			TreeNode parentNode = null;
			if(cg.getParentCourseGroup()==null){
				//this cg itself a root course group
				rootNodes.add(cgNode);
				return cgNode;
			}else{
				parentNode = map.get(cg.getParentCourseGroup().getId());
				if (parentNode == null) {
					parentNode = createParentNodes(cg
						.getParentCourseGroup(), cgNode);
				}else{
					parentNode = map.get(cg.getParentCourseGroup().getId());
					parentNode.addChild(cgNode);
					cgNode.setParent(parentNode);
				}
				return cgNode;
			}
		}
	}

	
	public TreeNode addNode2(CourseGroup cg) {
		TreeNode existNode = map.get(cg.getId());
		if (existNode != null)// it means this coursegroup already exist
		{
			return existNode;
		} else {
			// this means this node doesn't exist
			TreeNode cgNode = new TreeNode(cg);
			cgNode.setValue(cg);
			cgNode.setEnabled(true);
			map.put(cg.getId(), cgNode);
			TreeNode parentNode = null;
			if(cg.getParentCourseGroup()==null){
				//this cg itself a root course group
				rootNodes.add(cgNode);
				return cgNode;
			}else{
				parentNode = map.get(cg.getParentCourseGroup().getId());
				if (parentNode == null) {
					parentNode = createParentNodes(cg
						.getParentCourseGroup(), cgNode);
				}else{
					parentNode = map.get(cg.getParentCourseGroup().getId());
					parentNode.addChild(cgNode);
					cgNode.setParent(parentNode);
				}
				return cgNode;
			}
		}
	}
	
	public List<TreeNode> getRootNodes() {
		return rootNodes;
	}
}