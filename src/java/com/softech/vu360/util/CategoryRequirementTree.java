/**
 * 
 */
package com.softech.vu360.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.softech.vu360.lms.model.CredentialCategory;
import com.softech.vu360.lms.model.CredentialCategoryRequirement;


/**
 * @author sana.majeed
 * // [1/6/2011] LMS-8314 :: Regulatory Module Phase II - Credential > Category > Requirement
 */
public class CategoryRequirementTree {
	
	private HashMap<Long, TreeNode> treeMap = new HashMap<Long, TreeNode>();	
	private boolean isEnabled;
	private List<TreeNode> rootNodes = new ArrayList<TreeNode>();
	
		

	public CategoryRequirementTree(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	/*public TreeNode addNode (CredentialCategoryRequirement requirement) {
		
		TreeNode categoryNode = this.treeMap.get( requirement.getCredentialCategory().getId() );
		
		// CategoryNode does not exist; create a node first
		if (categoryNode == null) { 
			categoryNode = new TreeNode();
			categoryNode.setValue( requirement.getCredentialCategory() );
			categoryNode.setEnabled( this.isEnabled );
			
			this.treeMap.put( requirement.getCredentialCategory().getId() , categoryNode );
			this.rootNodes.add( categoryNode );
		}
		
		// CategoryNode now exists; add requirement under this category
		TreeNode reqNode = new TreeNode();
		reqNode.setValue( requirement );
		reqNode.setEnabled( this.isEnabled );
		reqNode.setParent( categoryNode );
		
		categoryNode.addChild(reqNode);
		
		return categoryNode;		
	}*/
	
	public TreeNode addRootNode (CredentialCategory category) {
		
		TreeNode categoryNode = this.treeMap.get( category.getId() );
		
		// CategoryNode does not exist; create a node first
		if (categoryNode == null) { 
			categoryNode = new TreeNode();
			categoryNode.setValue( category );
			categoryNode.setEnabled( this.isEnabled );
			
			this.treeMap.put( category.getId() , categoryNode );
			this.rootNodes.add( categoryNode );
		}
			
		return categoryNode;		
	}
	
	public TreeNode addChildNode (CredentialCategoryRequirement requirement) {
		
		TreeNode categoryNode = this.addRootNode( requirement.getCredentialCategory() );
		
		// Add requirement under this category
		TreeNode reqNode = new TreeNode();
		reqNode.setValue( requirement );
		reqNode.setEnabled( this.isEnabled );
		reqNode.setParent( categoryNode );
		
		categoryNode.addChild(reqNode);
		
		return categoryNode;
	}
	
	/*public void addAllNodes ( List<CredentialCategoryRequirement> requirementList ) {
		
		for (CredentialCategoryRequirement requirement : requirementList ) {
			this.addNode(requirement);
		}
	}*/
	
	public List<List<TreeNode>> getTreeList() {
		
		List<List<TreeNode>> treeNodeList = new ArrayList<List<TreeNode>>();
		
		for (TreeNode rootNode : this.rootNodes) {
			treeNodeList.add( rootNode.bfs() );
		}
		
		return treeNodeList;
	}
	
	/**
	 * @return the rootNodes
	 */
	public List<TreeNode> getRootNodes() {
		return rootNodes;
	}
}
