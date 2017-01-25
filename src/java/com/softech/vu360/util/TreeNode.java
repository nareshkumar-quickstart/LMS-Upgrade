package com.softech.vu360.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode implements Serializable{
	private Object value = null;
	private List<TreeNode> children = null;
	private TreeNode parent = null;

	public TreeNode() {
		super();
	}

	public TreeNode(Object value) {
		super();
		this.value = value;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public void addChild(TreeNode child){
		if(this.children == null)
			this.children = new ArrayList<TreeNode>();
		this.children.add(child);
		child.setParent(this);
	}
	
	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public int getChildCount(){
		if(this.children == null)
			return 0;
		return this.children.size();
	}
	
	public TreeNode getChildAt(int childIndex){
		if(this.children == null)
			return null;
		return this.children.get(childIndex);
	}
	
	public boolean isLeaf(){
		boolean hasChild = (this.children == null || this.children.size()<=0)? false:true;
		return !hasChild;
	}
	
	public int getDepth(){
		int depth = 0;
		if(this.parent==null)
			return 0;
		else
			depth = this.parent.getDepth()+1;
		return depth;
	}
	
	public List<TreeNode> bfs(){
		List<TreeNode> nodeSeq = new ArrayList<TreeNode>();
		nodeSeq.add(this);
		if(!this.isLeaf()){
			for(TreeNode child:children)
			nodeSeq.addAll(child.bfs());
		}
		return nodeSeq;
	}
	
	
	private boolean selected = false;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
    	TreeNode other = (TreeNode)obj;
    	if (this.value.equals(other.value))
    		return true;
        return false;
	}
    public String toString(){
    	StringBuilder builder = new StringBuilder();
    	TreeNode parent = this.getParent();
    	while(parent!=null){
    		builder.append("\t");
    		parent = parent.getParent();
    	}
    	builder.append("height:"+this.getDepth());
    	builder.append("->");
    	builder.append(getValue().toString());
    	
    	return builder.toString();
    }
}
