/*
 * author Faisal Ahmed Siddiqui
 */
package com.softech.vu360.lms.web.controller.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.util.TreeNodeComparator;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.TreeNode;

public class AvailableCoursesTree  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AvailableCoursesTree.class);
	
	private HashMap<Long, TreeNode> treeNodeMap = new HashMap<Long, TreeNode>();

	
	// this will contain list of all tree nodes, coursegroup, trainingplans and misc coursegroup
	private List<TreeNode> allRootNodes =  new ArrayList<TreeNode>();
	private JSONObject allRootNodesJSON =  new JSONObject();
	
	//this will contain only lit of tree node of coursegroup
	private List<TreeNode> courseGroupNodes = new ArrayList<TreeNode>();
	
	// this collection will hold list of training plans	
	private List<TreeNode> trainingPlans = new ArrayList<TreeNode>();

	// this will hold all customer courses (without course group)
	private TreeNode miscGroup; 

	Map<Long,CourseGroupView> courseGroupViewMap;
	
	public AvailableCoursesTree(Map<Long, CourseGroupView> courseGroupViewMap){
		this.courseGroupViewMap = courseGroupViewMap; 
	}
	
	public void build(){
		log.debug("build() starts..");
		for(CourseGroupView view:courseGroupViewMap.values()){
			//log.debug("INSDIE : " + view.getName() + " :::: " + view.getCourses().size());
			addNode(view);
		}
		log.debug("build ends... ");
	}
	
	
	/*
	 * This method will take course group as an argument and create hierarchies
	 * of all parents
	 */
	private TreeNode createParentNodes(Long parentCourseGroupId, TreeNode childNode) {
		TreeNode mapEntry = treeNodeMap.get(parentCourseGroupId);
		if (mapEntry == null) {
			//log.debug(" parent node("+parentCourseGroupId+") doesn't exist load one");
			CourseGroupView cgView = courseGroupViewMap.get(parentCourseGroupId);
			if(cgView==null)
				return null;
			//log.debug(cgView.getGroupName()+" has been loaded");

			TreeNode node = new TreeNode();
			node.setValue(cgView);
			node.addChild(childNode);
			childNode.setParent(node);
			node.setEnabled(Boolean.TRUE);
			treeNodeMap.put(cgView.getId(), node);

			if (cgView.getParentCourseGroupId() == null) {
				// it means this node is the root node
				//log.debug("there is no parent of this parent:"+cgView.getGroupName());
				updateInternalLists(node);
				return node;
			} else {
				//log.debug("prent node("+cgView.getParentCourseGroupId()+") for parent."+cgView.getGroupName()+" doesnt exist, create new");
				return createParentNodes(cgView.getParentCourseGroupId(), node);
			}
		} else {
			mapEntry.addChild(childNode);
			childNode.setParent(mapEntry);
			return mapEntry;
		}
	}



	public TreeNode addNode(CourseGroupView cgView) {
		//log.debug("node to add:"+cgView.getName());
		TreeNode existNode = treeNodeMap.get(cgView.getId());
		if (existNode != null)// it means this coursegroup already exist
		{
			//log.debug(cgView.getGroupName()+" already exist in map : " + ((CourseGroupView)existNode.getValue()).getCourses().size() + "------" + cgView.getCourses().size());
//			// this block of code will merge the courses in existing coursegroup view 
//			CourseGroupView existingCGView = (CourseGroupView)existNode.getValue();
//			List<CourseView> coursesList = cgView.getCourses();
//			for(CourseView cView:coursesList){
//				existingCGView.addCourse(cView);
//			}
			
			return existNode;
		} else {
			// this means this node doesn't exist
			//log.debug(cgView.getName() +" doesn't exist in map creating new");
			TreeNode cgNode = new TreeNode(cgView);
			cgNode.setValue(cgView);
			cgNode.setEnabled(Boolean.TRUE);
			treeNodeMap.put(cgView.getId(), cgNode);
			TreeNode parentNode = null;
			if(cgView.getParentCourseGroupId()==null){
				//this cg itself a root course group
				//log.debug("there is no parent for "+cgView.getName());
				updateInternalLists(cgNode);
				return cgNode;
			}else{
				//log.debug(cgView.getName()+" parent id("+cgView.getParentCourseGroupId()+")");
				parentNode = treeNodeMap.get(cgView.getParentCourseGroupId());
				if (parentNode == null) {
					//log.debug("prent node("+cgView.getParentCourseGroupId()+") for "+cgView.getName()+" doesnt exist, create new");
					parentNode = createParentNodes(cgView.getParentCourseGroupId(), cgNode);
				}else{
					//log.debug("prent node("+cgView.getParentCourseGroupId()+") for "+cgView.getName()+" exists");
					parentNode.addChild(cgNode);
					cgNode.setParent(parentNode);
				}
				return cgNode;
			}
		}
	}
	private void updateInternalLists(TreeNode cgNode){
		CourseGroupView cgView = (CourseGroupView)cgNode.getValue();
		if(cgView.isTrainingPlan()){
			//log.debug(cgView.getName()+" adding in trainingplan list");
			cgView.setGroupName(cgView.getName());
			trainingPlans.add(cgNode);
		}
		else if(cgView.isMisc()){
			//log.debug(cgView.getName()+" adding in MICS");
			this.miscGroup = cgNode;
		}else
		{
			//log.debug(cgView.getName()+" adding couresgroup list");
			courseGroupNodes.add(cgNode);
		}
		allRootNodes.add(cgNode);		
	}
	
	public JSONObject generateAllNodesJSON(){
		
		// Define JSON Objects
		JSONObject allNodesJSON = new JSONObject();
		ArrayList<JSONObject> rootNodesJSON = new ArrayList<JSONObject>();
		
		// Convert Course Group Nodes to JSON.
		List<TreeNode> courseGroupNodes = this.getCourseGroupRootNodes();

		// Sort Root Course Groups
		Collections.sort(courseGroupNodes, new TreeNodeComparator());

		ArrayList<JSONObject> courseGroupNodesJSON = this.convertCourseGroupNodesJSON(courseGroupNodes);
		rootNodesJSON.addAll(courseGroupNodesJSON);
		
		
		
		// Convert Training Plan Nodes to JSON.
		List<TreeNode> trainingPlanNodes = this.getTrainingPlanNodes();
		if(trainingPlanNodes.size()>0){
			JSONObject trainingPlanNodesJSON = this.convertTrainingPlansNodesJSON(trainingPlanNodes);
			rootNodesJSON.add(trainingPlanNodesJSON);
		}
		
		// Convert MISC Group Node to JSON.
		TreeNode miscGroupNodes = this.getMiscGroupNode();
		if(miscGroupNodes!=null){
			JSONObject miscGroupNodesJSON = this.convertMiscGroupNodesJSON(miscGroupNodes);
			rootNodesJSON.add(miscGroupNodesJSON);
		}
		
		
		allNodesJSON.put("rootNodes", rootNodesJSON);
		return allNodesJSON;
	}

	public JSONObject convertMiscGroupNodesJSON(TreeNode miscGroupNodes){
		JSONObject miscGroupJSON = new JSONObject();
		CourseGroupView miscCGView = (CourseGroupView) miscGroupNodes.getValue();
		miscGroupJSON.put("cgView", miscCGView);
		miscGroupJSON.put("children", "");
		miscGroupJSON.put("depth", 0);
		return miscGroupJSON;
	}
	
	public JSONObject convertTrainingPlansNodesJSON(List<TreeNode> trainingPlanNodes){
		
		// Create Training Plans JSONObject
		JSONObject trainingPlansJSON = new JSONObject();
		
		// Define Training Plans Course Group View
		CourseGroupView trainingPlansCGView = new CourseGroupView();
		trainingPlansCGView.setGroupName("Training Plans");
		trainingPlansCGView.setId(0);
		
		// Create Training Plan Views, and Sub-Views
		ArrayList<JSONObject> trainingPlanChildNodes = new ArrayList<JSONObject>();
		for(TreeNode node: trainingPlanNodes){
			JSONObject nodeJSON = new JSONObject();
			CourseGroupView trainingPlanCGView = (CourseGroupView) node.getValue();
			nodeJSON.put("cgView", trainingPlanCGView);
			nodeJSON.put("depth", 1);
			nodeJSON.put("children", "");
			nodeJSON.put("parentid", 0);
			trainingPlanChildNodes.add(nodeJSON);
		}
		
		// Add Training Plans Course Group View, and Sub-Views
		trainingPlansJSON.put("cgView", trainingPlansCGView);
		trainingPlansJSON.put("children", trainingPlanChildNodes);
		trainingPlansJSON.put("depth", 0);
		
		return trainingPlansJSON;
	}
	
	public ArrayList<JSONObject> convertCourseGroupNodesJSON(List<TreeNode> nodes){
		
		// Create Stack
		Stack<TreeNode> stack = new Stack<TreeNode>();
		// Adds Root Node
		stack.addAll(nodes);
		// Define Conversion Variables
		TreeNode node = null;
		ArrayList<JSONObject> rootNodeList = new ArrayList<JSONObject>();
		CourseGroupView view = null;
		
		// Map to Contain JSONObjects
		HashMap<Long, JSONObject> mapJSON = new HashMap<Long, JSONObject>();
		
		// Begin While
		int depth = 0;
		while(!stack.isEmpty()){
			
			// Gets the TOP of the stack.
			node = stack.pop();
			// View is the CourseGroupView from the popped TreeNode.
			view = (CourseGroupView) node.getValue();
			// The CourseGroupView Id.
			Long viewId = view.getId();
			// Define Parent JSONObject Variable
			JSONObject parentJSON = null;
			
			// If the node is not the root node. --------------------------------------------
			if(view.getParentCourseGroupId()!= null){
				
				// Retrieve JSONObject from mapJSON
				parentJSON = (JSONObject) mapJSON.get(view.getParentCourseGroupId());				
				
				// If the parentJSON does not exist in the mapJSON,
				if(parentJSON == null){
					
					// Create new JSONObject. NOTE: Children set to null initially.
					parentJSON = new JSONObject();	
					parentJSON.put(new Long(view.getParentCourseGroupId()), node.getParent().getValue());
					parentJSON.put("parentid", ((CourseGroupView) node.getParent().getValue()).getParentCourseGroupId());			
					parentJSON.put("children", new JSONObject());
					parentJSON.put("depth", node.getDepth());

					mapJSON.put(view.getParentCourseGroupId(), parentJSON);					
				}else{
					
					JSONObject childJSON = mapJSON.get(view.getId());
					if(childJSON == null){
						childJSON = new JSONObject();					
						//log.debug(view.getParentCourseGroupId());					
						childJSON.put("cgView", view);
						childJSON.put("parentid", view.getParentCourseGroupId());		
						childJSON.put("children", new JSONObject());
						childJSON.put("depth", node.getDepth());
						mapJSON.put(view.getId(), childJSON);		
					}else{						
						//log.debug("This is a childJSON that has just been created. " + childJSON);
					}
					
					//
					TreeNode decendentNode = node;
					TreeNode ancestorNode = null;
					CourseGroupView ancestorCGV = null;
					CourseGroupView decendentCGV = null;
					JSONObject ancestorJSON = new JSONObject();
					int innerDepth = depth;
					
					//
					while(innerDepth>=0){
						
						//
						if( decendentNode.getParent()!= null){
							// Parent of the current node.					
							ancestorNode = decendentNode.getParent();
						}else{
							//log.debug("I do not believe this should ever show.");
						}
						
						// CourseGroupView of the current nodes Parent.
						ancestorCGV = (CourseGroupView)ancestorNode.getValue();
						ancestorJSON = mapJSON.get(ancestorCGV.getId());

						// Assign current node and JSONObject.
						decendentCGV = (CourseGroupView)decendentNode.getValue();						
						JSONObject decendentJSON = mapJSON.get(decendentCGV.getId());;
						
						// Add decendent to ancestor
						if(decendentCGV.getParentCourseGroupId() != null){							
							
							//
							JSONObject childJ = null;
							//
							Long ancestorId = ancestorCGV.getId();
							ancestorJSON = mapJSON.get(ancestorId);
							
							//
							if(ancestorJSON.get("children")!=null){
								childJ = (JSONObject)ancestorJSON.get("children");
								childJ.put(decendentCGV.getId(), decendentJSON);
							}else{
								childJ = new JSONObject();
								childJ.put(decendentCGV.getId(), decendentJSON);
							}
							
							//
							if(childJ != null){
								ancestorJSON.put("children", childJ);
								ancestorJSON.put("depth", ancestorNode.getDepth());
							}
							// Add ancestor back to the map
							mapJSON.put(ancestorCGV.getId(), ancestorJSON );
							//
							decendentNode = ancestorNode;
							
						}else{
							
							// If Root Node
							mapJSON.put(ancestorCGV.getId(), ancestorJSON );
							
						}	
						innerDepth--;
					}					
				}
			// If the node has no parent course group id.
			}else if(view.getParentCourseGroupId()==null){
				
				//
				JSONObject rootJSON = new JSONObject();
				rootJSON.put("cgView", view);
				rootJSON.put("children", new JSONObject());	
				//
				mapJSON.put(viewId, rootJSON);
				// Add Root JSONObject
				rootNodeList.add(rootJSON);
				
			}
			
			// This is the recursive part.				
			if(node.getChildren()!=null && node.getChildren().size()>0){
				
				// Sort Children Course Groups 
				Collections.sort(node.getChildren(), new TreeNodeComparator());
				
				stack.addAll(stack.size(), node.getChildren());	
				depth++;					
			}
		}
		return rootNodeList;
	}


	
	private void printTree(List<TreeNode> nodes){
		Stack<TreeNode> stack = new Stack<TreeNode>();
		stack.addAll(nodes);
		TreeNode node = null;
		String str="";
		CourseGroupView view = null;
		while(!stack.isEmpty()){
			node = stack.pop();
			view = (CourseGroupView) node.getValue();
			if(view.getParentCourseGroupId()==null)
				str="";
			//System.out.print(str);
			//System.out.println(view.getName()+" contains ("+view.getCourses().size()+") courses");
			if(node.getChildren()!=null && node.getChildren().size()>0){
				str+="\t";
				for(TreeNode n:node.getChildren())
					stack.push(n);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	


	
	
	public static Map<Long, CourseGroupView> mockTest(){
		CourseGroupView cgv1 = new CourseGroupView();
		cgv1.setGroupName("Course Group View - 55");
		cgv1.setId(55);
		
			CourseGroupView cgv11 = new CourseGroupView();
			cgv11.setGroupName("Course Group View - 55.1");
			cgv11.setId(551);
			cgv11.setParentCourseGroupId(cgv1.getId());
		
			CourseGroupView cgv111 = new CourseGroupView();
			cgv111.setGroupName("Course Group View - 55.5.1");
			cgv111.setId(5552);
			cgv111.setParentCourseGroupId(cgv11.getId());
		
				// Root Child Child
				CourseGroupView cgv1111 = new CourseGroupView();
				cgv1111.setGroupName("Course Group View - 55.5.5.1");
				cgv1111.setId(55551);
				cgv1111.setParentCourseGroupId(cgv111.getId());
				
					// Root Child Child
					CourseGroupView cgv11111 = new CourseGroupView();
					cgv11111.setGroupName("Course Group View - 55.5.5.5.1");
					cgv11111.setId(555551);
					cgv11111.setParentCourseGroupId(cgv1111.getId());
					// Root Child Child
					CourseGroupView cgv11112 = new CourseGroupView();
					cgv11112.setGroupName("Course Group View - 55.5.5.5.2");
					cgv11112.setId(555552);
					cgv11112.setParentCourseGroupId(cgv1111.getId());
					// Root Child Child
					CourseGroupView cgv11113 = new CourseGroupView();
					cgv11113.setGroupName("Course Group View - 55.5.5.5.2");
					cgv11113.setId(555553);
					cgv11113.setParentCourseGroupId(cgv1111.getId());
						// Root Child Child
						CourseGroupView cgv11131 = new CourseGroupView();
						cgv11131.setGroupName("Course Group View - 55.5.5.5.2");
						cgv11131.setId(55333);
						cgv11131.setParentCourseGroupId(cgv11113.getId());
					
				// Root Child Child			
				CourseGroupView cgv1112 = new CourseGroupView();
				cgv1112.setGroupName("Course Group View - 55.5.5.2");
				cgv1112.setId(55552);
				cgv1112.setParentCourseGroupId(cgv111.getId());
				
					// Root Child Child
					CourseGroupView cgv11121 = new CourseGroupView();
					cgv11121.setGroupName("Course Group View - 55.5.5.5.1");
					cgv11121.setId(5555521);
					cgv11121.setParentCourseGroupId(cgv1112.getId());
					// Root Child Child
					CourseGroupView cgv11122 = new CourseGroupView();
					cgv11122.setGroupName("Course Group View - 55.5.5.5.2");
					cgv11122.setId(5555522);
					cgv11122.setParentCourseGroupId(cgv1112.getId());
					// Root Child Child
					CourseGroupView cgv11123 = new CourseGroupView();
					cgv11123.setGroupName("Course Group View - 55.5.5.5.2");
					cgv11123.setId(5555523);
					cgv11123.setParentCourseGroupId(cgv1112.getId());
				// Root Child Child
				CourseGroupView cgv1113 = new CourseGroupView();
				cgv1113.setGroupName("Course Group View - 55.5.5.3");
				cgv1113.setId(55553);
				cgv1113.setParentCourseGroupId(cgv111.getId());
		
				
		CourseGroupView cgv2 = new CourseGroupView();
		cgv2.setGroupName("Course Group View - 66");
		cgv2.setId(66);
		
		
			CourseGroupView cgv21 = new CourseGroupView();
			cgv21.setGroupName("Course Group View - 66.1");
			cgv21.setId(661);
			cgv21.setParentCourseGroupId(cgv2.getId());
			
			
		CourseGroupView tp1 = new CourseGroupView();
		tp1.setTrainingPlanName("Training Plan 1");
		tp1.setId(100);
		tp1.markTrainingPlan(Boolean.TRUE);
		CourseView tpcourse1 = new CourseView(mockCourseView(tp1.getGroupName(), tp1.getGroupName()+" course 1"));
		tp1.addCourse(tpcourse1);
		tpcourse1 = new CourseView(mockCourseView(tp1.getGroupName(), tp1.getGroupName()+" course 2"));
		tp1.addCourse(tpcourse1);
		tpcourse1 = new CourseView(mockCourseView(tp1.getGroupName(), tp1.getGroupName()+" course 3"));
		tp1.addCourse(tpcourse1);

		CourseGroupView tp2 = new CourseGroupView();
		tp2.setTrainingPlanName("Training Plan 2");
		tp2.setId(101);
		tp2.markTrainingPlan(Boolean.TRUE);
		CourseView tpcourse2 = new CourseView(mockCourseView(tp2.getGroupName(), tp2.getGroupName()+" course 1"));
		tp2.addCourse(tpcourse2);
		tpcourse1 = new CourseView(mockCourseView(tp2.getGroupName(), tp2.getGroupName()+" course 2"));
		tp2.addCourse(tpcourse2);

		
		CourseGroupView tp3 = new CourseGroupView();
		tp3.setTrainingPlanName("Training Plan 3");
		tp3.setId(102);
		tp3.markTrainingPlan(Boolean.TRUE);
		CourseView tpcourse3 = new CourseView(mockCourseView(tp3.getGroupName(), tp3.getGroupName()+" course 1"));
		tp3.addCourse(tpcourse3);
			
		Map<Long, CourseGroupView> cgvTestMap = new HashMap<Long, CourseGroupView>();
		cgvTestMap.put(cgv1.getId(),cgv1);
			cgvTestMap.put(cgv11.getId(),cgv11);
				cgvTestMap.put(cgv111.getId(),cgv111);				
					cgvTestMap.put(cgv1111.getId(),cgv1111);
						cgvTestMap.put(cgv11111.getId(),cgv11111);
						cgvTestMap.put(cgv11112.getId(),cgv11112);
						cgvTestMap.put(cgv11113.getId(),cgv11113);
							cgvTestMap.put(cgv11131.getId(),cgv11131);
					cgvTestMap.put(cgv1112.getId(),cgv1112);
						cgvTestMap.put(cgv11121.getId(),cgv11121);
						cgvTestMap.put(cgv11122.getId(),cgv11122);
						cgvTestMap.put(cgv11123.getId(),cgv11123);
					cgvTestMap.put(cgv1113.getId(),cgv1113);
						
		cgvTestMap.put(cgv2.getId(),cgv2);
			cgvTestMap.put(cgv21.getId(),cgv21);
	
			
		cgvTestMap.put(tp1.getId(), tp1);
		cgvTestMap.put(tp2.getId(), tp2);
		cgvTestMap.put(tp3.getId(), tp3);
			
		return cgvTestMap;
	}
	
	public static Map<Long, CourseGroupView> mockTest2(){
		
		CourseGroupView cgv1 = new CourseGroupView();
		cgv1.setGroupName("Course Group View - 1");
		cgv1.setId(1);
		
			CourseGroupView cgv11 = new CourseGroupView();
			cgv11.setGroupName("Course Group View - 1.1");
			cgv11.setId(11);
			cgv11.setParentCourseGroupId(cgv1.getId());
		
				CourseGroupView cgv111 = new CourseGroupView();
				cgv111.setGroupName("Course Group View - 1.1.1");
				cgv111.setId(111);
				cgv111.setParentCourseGroupId(cgv11.getId());
			
					// Root Child Child
					CourseGroupView cgv1111 = new CourseGroupView();
					cgv1111.setGroupName("Course Group View - 1.1.1.1");
					cgv1111.setId(1111);
					cgv1111.setParentCourseGroupId(cgv111.getId());
					// Root Child Child			
					CourseGroupView cgv1112 = new CourseGroupView();
					cgv1112.setGroupName("Course Group View - 1.1.1.2");
					cgv1112.setId(1112);
					cgv1112.setParentCourseGroupId(cgv111.getId());
					// Root Child Child
					CourseGroupView cgv1113 = new CourseGroupView();
					cgv1113.setGroupName("Course Group View - 1.1.1.3");
					cgv1113.setId(1113);
					cgv1113.setParentCourseGroupId(cgv111.getId());
					
				CourseGroupView cgv112 = new CourseGroupView();
				cgv112.setGroupName("Course Group View - 1.1.2");
				cgv112.setId(112);
				cgv112.setParentCourseGroupId(cgv11.getId());
				
					// Root Child Child
					CourseGroupView cgv1121 = new CourseGroupView();
					cgv1111.setGroupName("Course Group View - 1.1.2.1");
					cgv1111.setId(1121);
					cgv1111.setParentCourseGroupId(cgv112.getId());
					// Root Child Child			
					CourseGroupView cgv1122 = new CourseGroupView();
					cgv1122.setGroupName("Course Group View - 1.1.2.2");
					cgv1122.setId(1122);
					cgv1122.setParentCourseGroupId(cgv112.getId());
					// Root Child Child
					CourseGroupView cgv1123 = new CourseGroupView();
					cgv1123.setGroupName("Course Group View - 1.1.2.3");
					cgv1123.setId(1123);
					cgv1123.setParentCourseGroupId(cgv112.getId());
				
				CourseGroupView cgv113 = new CourseGroupView();
				cgv113.setGroupName("Course Group View - 1.1.3");
				cgv113.setId(113);
				cgv113.setParentCourseGroupId(cgv11.getId());
			
			
			
			
			
			CourseGroupView cgv12 = new CourseGroupView();
			cgv12.setGroupName("Course Group View - 1.2");
			cgv12.setId(12);
			cgv12.setParentCourseGroupId(cgv1.getId());
			
			
				CourseGroupView cgv121 = new CourseGroupView();
				cgv121.setGroupName("Course Group View - 1.2.1");
				cgv121.setId(121);
				cgv121.setParentCourseGroupId(cgv12.getId());
			
				CourseGroupView cgv122 = new CourseGroupView();
				cgv122.setGroupName("Course Group View - 1.2.2");
				cgv122.setId(122);
				cgv122.setParentCourseGroupId(cgv12.getId());
				
				CourseGroupView cgv123 = new CourseGroupView();
				cgv123.setGroupName("Course Group View - 1.2.3");
				cgv123.setId(123);
				cgv123.setParentCourseGroupId(cgv12.getId());
		
		CourseGroupView cgv2 = new CourseGroupView();
		cgv2.setGroupName("Course Group View - 2");
		cgv2.setId(2);
		
			CourseGroupView cgv21 = new CourseGroupView();
			cgv21.setGroupName("Course Group View - 2.1");
			cgv21.setId(21);
			cgv21.setParentCourseGroupId(cgv2.getId());
		
		Map<Long, CourseGroupView> cgvTestMap = new HashMap<Long, CourseGroupView>();
		cgvTestMap.put(cgv1.getId(),cgv1);
			cgvTestMap.put(cgv11.getId(),cgv11);
		
			cgvTestMap.put(cgv111.getId(),cgv111);
			
					cgvTestMap.put(cgv1111.getId(),cgv1111);
					cgvTestMap.put(cgv1112.getId(),cgv1112);
					cgvTestMap.put(cgv1113.getId(),cgv1113);
		
				cgvTestMap.put(cgv112.getId(),cgv112);
				
					cgvTestMap.put(cgv1121.getId(),cgv1121);
					cgvTestMap.put(cgv1122.getId(),cgv1122);
					cgvTestMap.put(cgv1123.getId(),cgv1123);
					
				cgvTestMap.put(cgv113.getId(),cgv113);
		
			cgvTestMap.put(cgv12.getId(),cgv12);
			
				cgvTestMap.put(cgv121.getId(),cgv121);
				cgvTestMap.put(cgv122.getId(),cgv122);
				cgvTestMap.put(cgv123.getId(),cgv123);
		
		cgvTestMap.put(cgv2.getId(),cgv2);
		
			cgvTestMap.put(cgv21.getId(),cgv21);
		
		return cgvTestMap;
	}
	
	
public static Map<Long,CourseGroupView> mockCourseGroupView(){
		
		CourseGroupView root1 = new CourseGroupView();
		root1.setGroupName("1");
		root1.setId(1);

		//root1.setCourses()
		
		CourseGroupView root11 = new CourseGroupView();
		root11.setGroupName("1.1");
		root11.setId(11);
		root11.setParentCourseGroupId(root1.getId());
		
		CourseView view11 = new CourseView(mockCourseView(root11.getGroupName(), root11.getGroupName()+" course 1"));
		root11.addCourse(view11);
		view11 = new CourseView(mockCourseView(root11.getGroupName(), root11.getGroupName()+" course 2"));
		root11.addCourse(view11);

		CourseGroupView root111 = new CourseGroupView();
		root111.setGroupName("1.1.1");
		root111.setId(111);
		root111.setParentCourseGroupId(root11.getId());
	
		CourseView view111 = new CourseView(mockCourseView(root111.getGroupName(), root111.getGroupName()+" course 1"));
		root111.addCourse(view111);
		view111 = new CourseView(mockCourseView(root111.getGroupName(), root111.getGroupName()+" course 2"));
		root111.addCourse(view111);
		view111 = new CourseView(mockCourseView(root111.getGroupName(), root111.getGroupName()+" course 3"));
		root111.addCourse(view111);

		
		
		
		CourseGroupView root2 = new CourseGroupView();
		root2.setGroupName("2");
		root2.setId(2);
		//root1.setCourses()

		CourseGroupView root21 = new CourseGroupView();
		root21.setGroupName("2.1");
		root21.setId(21);
		root21.setParentCourseGroupId(root2.getId());


		CourseGroupView root3 = new CourseGroupView();
		root3.setGroupName("3");
		root3.setId(3);
		CourseView view3 = new CourseView(mockCourseView(root3.getGroupName(), root3.getGroupName()+" course 1"));
		root3.addCourse(view3);
		view3 = new CourseView(mockCourseView(root3.getGroupName(), root3.getGroupName()+" course 2"));
		root3.addCourse(view3);
		view3 = new CourseView(mockCourseView(root3.getGroupName(), root3.getGroupName()+" course 3"));
		root3.addCourse(view3);

		
		CourseGroupView tp1 = new CourseGroupView();
		tp1.setTrainingPlanName("Training Plan 1");
		tp1.setId(100);
		tp1.markTrainingPlan(Boolean.TRUE);
		CourseView tpcourse1 = new CourseView(mockCourseView(tp1.getGroupName(), tp1.getGroupName()+" course 1"));
		tp1.addCourse(tpcourse1);
		tpcourse1 = new CourseView(mockCourseView(tp1.getGroupName(), tp1.getGroupName()+" course 2"));
		tp1.addCourse(tpcourse1);
		tpcourse1 = new CourseView(mockCourseView(tp1.getGroupName(), tp1.getGroupName()+" course 3"));
		tp1.addCourse(tpcourse1);

		CourseGroupView tp2 = new CourseGroupView();
		tp2.setTrainingPlanName("Training Plan 2");
		tp2.setId(101);
		tp2.markTrainingPlan(Boolean.TRUE);
		CourseView tpcourse2 = new CourseView(mockCourseView(tp2.getGroupName(), tp2.getGroupName()+" course 1"));
		tp2.addCourse(tpcourse2);
		tpcourse1 = new CourseView(mockCourseView(tp2.getGroupName(), tp2.getGroupName()+" course 2"));
		tp2.addCourse(tpcourse2);

		
		CourseGroupView tp3 = new CourseGroupView();
		tp3.setTrainingPlanName("Training Plan 3");
		tp3.setId(102);
		tp3.markTrainingPlan(Boolean.TRUE);
		CourseView tpcourse3 = new CourseView(mockCourseView(tp3.getGroupName(), tp3.getGroupName()+" course 1"));
		tp3.addCourse(tpcourse3);
		
		
		CourseGroupView misc = new CourseGroupView();
		misc.setGroupName("MISC Plan 1");
		misc.setId(222);
		misc.markMisc(Boolean.TRUE);
		CourseView miscCourse = new CourseView(mockCourseView(misc.getGroupName(), misc.getGroupName()+" course 1"));
		misc.addCourse(miscCourse);
		
		Map<Long,CourseGroupView> map = new HashMap<Long,CourseGroupView>();
		map.put(root1.getId(),root1);
		map.put(root11.getId(),root11);
		map.put(root111.getId(),root111);
		map.put(root2.getId(),root2);
		map.put(root21.getId(),root21);

		map.put(root3.getId(),root3);

		
		map.put(tp1.getId(), tp1);
		map.put(tp2.getId(), tp2);
		map.put(tp3.getId(), tp3);
		
		map.put(misc.getId(), misc);
		
		return map;
		
	}
	

	public static Map<String,Object> mockCourseView(String groupName,String courseName){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("COURSEGROUPNAME",groupName);
		map.put("COURSENAME",courseName);
		map.put("COURSE_ID", System.currentTimeMillis());
		return map;
	}
	
	
	public JSONObject getRootNodesJSON(){
		return allRootNodesJSON;
	}
	public List<TreeNode> getAllRootNodes() {
		return allRootNodes;
	}
	public List<TreeNode> getCourseGroupRootNodes(){
		return courseGroupNodes;
	}
	public List<TreeNode> getTrainingPlanNodes(){
		return trainingPlans;
	}
	public TreeNode getMiscGroupNode(){
		return miscGroup;
	}
}