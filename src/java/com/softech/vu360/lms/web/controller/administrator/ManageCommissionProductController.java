/**
 * 
 */
package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CommissionProduct;
import com.softech.vu360.lms.model.CommissionProductCategory;
import com.softech.vu360.lms.service.CommissionService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.CommissionUtil;
import com.softech.vu360.util.ProductOrProductCategoryNodeInfo;
import com.softech.vu360.util.TreeNode;

/**
 * @author muhammad.Rehan
 *
 */
public class ManageCommissionProductController extends VU360BaseMultiActionController {

	private String commissionProductListTemplate= null;
	private CommissionService commissionService;

	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {		
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {	 	
	}
	
	public ModelAndView testTreeView ( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception 
	{
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("treeList",  new CommissionUtil().convertTreeToList(getDummyTreeNodeList(), new ArrayList<ProductOrProductCategoryNodeInfo>(), 0, ""));
		return new ModelAndView("administrator/commission/TreeView", "context", context);	
	}
	
	private List<TreeNode> getDummyTreeNodeList ()
	{
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		TreeNode n, cn, gcn;
		CommissionProduct commissionProduct = null;
		CommissionProductCategory commissionProductCategory;
		
		for(int i = 1; i <= 3; i++)
		{
			commissionProductCategory = new CommissionProductCategory();
			commissionProductCategory.setId(i);
			commissionProductCategory.setName("PC " + i);
			commissionProductCategory.setProductCategoryCode("PC-" + i);
			n = new TreeNode(commissionProductCategory);
			treeNodeList.add(n);

			for(int j = 1; j <= 3; j++)
			{
				commissionProduct = new CommissionProduct();
				commissionProduct.setId((10 * i) + j);
				commissionProduct.setName("P " + ((10 * i) + j));
				commissionProduct.setProductCode("P-" + ((10 * i) + j));
				cn = new TreeNode(commissionProduct);
				n.addChild(cn);
				
				commissionProductCategory = new CommissionProductCategory();
				commissionProductCategory.setId((10 * i) + j);
				commissionProductCategory.setName("PC " + ((10 * i) + j));
				commissionProductCategory.setProductCategoryCode("PC-" + ((10 * i) + j));
				cn = new TreeNode(commissionProductCategory);
				n.addChild(cn);
				
				for(int k = 1; k <= 3; k++)
				{
					commissionProduct = new CommissionProduct();
					commissionProduct.setId((100 * i) + (10 * j) + k);
					commissionProduct.setName("P " + ((100 * i) + (10 * j) + k));
					commissionProduct.setProductCode("P-" + ((100 * i) + (10 * j) + k));
					gcn = new TreeNode(commissionProduct);
					cn.addChild(gcn);
				}
			}
		}
		return treeNodeList;
	}
	
	
	
	public ModelAndView searchCommissionProduct( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
	    //request.getSession(true).setAttribute("feature", "ResellersCommissionableParties");
		Map<Object, Object> context = new HashMap<Object, Object>();
		//List<CommissionProduct> lstCP = null;
		try {
			Long commId=0L;
			if(request.getSession(true).getAttribute("commissionId")!=null)
				commId = Long.valueOf(request.getSession(true).getAttribute("commissionId").toString());
			
			String showAll= "false";
			if(request.getParameter("showAll")!=null)
				showAll = request.getParameter("showAll");
			else
				showAll= "false";
					
			int sortDirection = Integer.parseInt((request.getParameter("sortDirection")==null)?"0":request.getParameter("sortDirection"));		
			int sortColumnIndex = Integer.parseInt((request.getParameter("sortColumnIndex")==null)?"0":request.getParameter("sortColumnIndex"));
			
			context.put("showAll", showAll);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
			
			
			
			//creating  tree ---- START
			List<CommissionProduct> lstCP = commissionService.getCommissionProduct(commId);
			List<CommissionProductCategory> lstCPC = commissionService.getCommissionProductCategory(commId);
			//List<List<TreeNode>> CommPCTreeList = this.commissionService.getCommissionProductTreeList( lstCPC, true, 0 );				
			
			
			/*for(List lstrootLst : CommPCTreeList){
				
				for(int i=0;i<lstrootLst.size();i++){
					TreeNode currntTN = (TreeNode)lstrootLst.get(i);
					populateCoursesToRelatedCourseGroup (currntTN, lstCP);

				}
				
			}*/
			// ---------------------------------------------------------
			// Tree Start ----------------------------------------------
			// ---------------------------------------------------------
			List<TreeNode> rootTreeNodes = new ArrayList<TreeNode>();
			TreeNode rootNode;

			List<CommissionProductCategory> sourceProductCategoryList;
			List<CommissionProductCategory> remainingProductCategoryList;

			sourceProductCategoryList = lstCPC;
			remainingProductCategoryList = new ArrayList<CommissionProductCategory>();

			for(CommissionProductCategory PC : sourceProductCategoryList){
				if(PC.getParentCommissionProductCategory() == null){
					rootNode = new TreeNode();
					rootNode.setValue(PC);
					rootTreeNodes.add(rootNode);
				}else{
					remainingProductCategoryList.add(PC);
				}
			}

			for (TreeNode rootTreeNode : rootTreeNodes)
				populateChildCommPartNodes (rootTreeNode, remainingProductCategoryList);
			
			
			//Adding Course into related course group 
			// --------------------------------------------------------------------------
			

			for (TreeNode tn : rootTreeNodes)
				populateCoursesToRelatedCourseGroup (tn, lstCP);
			
//			context.put("CommPCTreeList", rootTreeNodes);
			
			// ---------------------------------------------------------
			// Tree End ----------------------------------------------
			// ---------------------------------------------------------
			
/*			context.put("products", lstCP);
			//context.put("CommPCTreeList", CommPCTreeList);
*/			
			 
			//context.put("treeList", convertTreeToList(getDummyTreeNodeList(), new ArrayList<ProductOrProductCategoryNodeInfo>(), 0, ""));
			context.put("treeList",  new CommissionUtil().convertTreeToList(rootTreeNodes, new ArrayList<ProductOrProductCategoryNodeInfo>(), 0, ""));
			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(commissionProductListTemplate, "context", context);	
	}
	
	
	public void populateChildCommPartNodes (TreeNode treeNode, List<CommissionProductCategory> sourceProductCategoryList)
	{
		TreeNode node;
		if (sourceProductCategoryList.size()>0){ 
			for (CommissionProductCategory pc : sourceProductCategoryList){
				if(pc.getParentCommissionProductCategory().getId() == ((CommissionProductCategory)treeNode.getValue()).getId()){
					node = new TreeNode();
					node.setValue(pc);
					treeNode.addChild(node);
					
				}
			}
			
			if(treeNode!=null && treeNode.getChildren()!=null)
				for (TreeNode childTreeNode : treeNode.getChildren())
					populateChildCommPartNodes (childTreeNode, sourceProductCategoryList);
			
		}
	}
	
	
	public void populateCoursesToRelatedCourseGroup (TreeNode treeNode, List<CommissionProduct> productList)
	{
		TreeNode node;
		if (productList.size()>0){ 
			for (CommissionProduct p : productList){
				Class cls = treeNode.getValue().getClass(); 
				if( !cls.getName().equals("com.softech.vu360.lms.model.CommissionProduct"))
				{	
					if(p.getCommissionProductCategory().getId() == ((CommissionProductCategory)treeNode.getValue()).getId()){
						node = new TreeNode();
						node.setEnabled(false);
						node.setValue(p);
						treeNode.addChild(node);
						
					}
				}
			}
			
			if(treeNode!=null && treeNode.getChildren()!=null)
				for (TreeNode childTreeNode : treeNode.getChildren())
					populateCoursesToRelatedCourseGroup (childTreeNode, productList);
			
		}
	}
	

	
	
	
	public ModelAndView deleteCommissionableProduct(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {	

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
			if( vu360AuthDetails.getCurrentDistributor() != null ){ 											
				
				String[] fieldsToDelete=request.getParameterValues("chkProduct");
				
				if(fieldsToDelete!=null && fieldsToDelete.length>0){
					Long[] ids = new Long[fieldsToDelete.length];
					
					for(int i=0;i<fieldsToDelete.length;i++){
						String []strArray = fieldsToDelete[i].split(":");
						if(strArray.length>=2)
							ids[i] = Long.valueOf(strArray[1]);
						else
							log.debug("Incorrect Product delete operation");
					}
					
					if  (ids!=null && ids.length>0) 
						commissionService.deleteCommissionProduct(ids); 
						
				}
				
				
				 HttpSession session = request.getSession(true);
				fieldsToDelete=null;
				fieldsToDelete=request.getParameterValues("chkCategory");
				if(fieldsToDelete!=null && fieldsToDelete.length>0){
					Long[] ids = new Long[fieldsToDelete.length];
					//String[] parentCatIds = new String[fieldsToDelete.length];
					
					for(int i=0;i<fieldsToDelete.length;i++){
						String []strArray = fieldsToDelete[i].split(":");
						if(strArray.length>=2){
							ids[i] = Long.valueOf(strArray[1]);
							//parentCatIds[i] = strArray[1];
						}else
							log.debug("Incorrect Category delete operation");
					}
					
					if  (ids!=null && ids.length>0) 
					{
						List<CommissionProduct> lstCat = commissionService.getCommissionProductByCategoryId(ids);
						List<CommissionProductCategory> lstParentCat = commissionService.getCommissionProductCategoryByRootIds(ids);
						
						if((lstCat==null || lstCat.size()==0) && (lstParentCat==null || lstParentCat.size()==0))
						{
							commissionService.deleteCommissionProductCategory(ids);
						    session.setAttribute("validateCommParCatErr",null);
						}else
						     session.setAttribute("validateCommParCatErr", "Children Product(s) or Category(s) exist for the selected Category.");
						
					}
						
				}
				return searchCommissionProduct(request, response, command, errors) ;//context.put("commissionablePartyList", new ArrayList(commissionService.getCommissionableParties(details.getCurrentDistributor().getId())));
			} else
				throw new IllegalArgumentException("Distributor not found");
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
	}
	

	
	public String getCommissionProductListTemplate() {
		return commissionProductListTemplate;
	}

	public void setCommissionProductListTemplate(
			String commissionProductListTemplate) {
		this.commissionProductListTemplate = commissionProductListTemplate;
	}

	public CommissionService getCommissionService() {
		return commissionService;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}

	
	
}
