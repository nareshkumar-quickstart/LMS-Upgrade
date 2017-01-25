package com.softech.vu360.util;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.web.controller.model.SurveyCourse;

public class TreeNodeCourseGroup {

	private CourseGroup value = null;
	private List<TreeNodeCourseGroup> children = null;
	private List<SurveyCourse> courses = null;
	private TreeNodeCourseGroup parent = null;
	private boolean selected = false;
	private boolean enabled = false;

	public TreeNodeCourseGroup() {
		super();
	}

	public TreeNodeCourseGroup(CourseGroup value) {
		super();
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public CourseGroup getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(CourseGroup value) {
		this.value = value;
	}

	/**
	 * @return the children
	 */
	public List<TreeNodeCourseGroup> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<TreeNodeCourseGroup> children) {
		this.children = children;
	}

	/**
	 * @return the courses
	 */
	public List<SurveyCourse> getCourses() {
		return courses;
	}

	/**
	 * @param courses the courses to set
	 */
	public void setCourses(List<SurveyCourse> courses) {
		this.courses = courses;
	}

	public void addChild(TreeNodeCourseGroup child){
		if(this.children == null)
			this.children = new ArrayList<TreeNodeCourseGroup>();
		this.children.add(child);
		child.setParent(this);
	}
	
	public TreeNodeCourseGroup getParent() {
		return parent;
	}

	public void setParent(TreeNodeCourseGroup parent) {
		this.parent = parent;
	}

	
	public int getChildCount(){
		if(this.children == null)
			return 0;
		return this.children.size();
	}
	
	public TreeNodeCourseGroup getChildAt(int childIndex){
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
	
	public List<TreeNodeCourseGroup> bfs(){
		List<TreeNodeCourseGroup> nodeSeq = new ArrayList<TreeNodeCourseGroup>();
		nodeSeq.add(this);
		if(!this.isLeaf()){
			for(TreeNodeCourseGroup child:children)
			nodeSeq.addAll(child.bfs());
		}
		return nodeSeq;
	}
	
	public String toString(){
		String str = "";
		str += this.value.toString(); 
		str += " Depth::"+this.getDepth();
		return str;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
