package com.softech.vu360.lms.util;

import java.util.Comparator;

import com.softech.vu360.lms.web.controller.model.CourseGroupView;
import com.softech.vu360.util.TreeNode;


public class TreeNodeComparator implements Comparator<TreeNode> {

	public int compare(TreeNode arg0, TreeNode arg1) {		
		
		CourseGroupView arg0_cgv = (CourseGroupView)arg0.getValue();
		CourseGroupView arg1_cgv = (CourseGroupView)arg1.getValue();
		
		return arg1_cgv.getGroupName().trim().compareTo(arg0_cgv.getGroupName().trim());
	}
}