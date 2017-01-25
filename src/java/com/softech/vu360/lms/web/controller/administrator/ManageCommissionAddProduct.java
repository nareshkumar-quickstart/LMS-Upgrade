/**
 * 
 */
package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Commission;
import com.softech.vu360.lms.model.CommissionProduct;
import com.softech.vu360.lms.model.CommissionProductCategory;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.service.CommissionService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;

/**
 * @author muhammad.Rehan
 *
 */
public class ManageCommissionAddProduct extends VU360BaseMultiActionController {

	private String addProductTemplate = null;
	private String addProductConfirmation=null;
	private CommissionService commissionService;
    private EntitlementService entitlementService;
	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());
	

	public ModelAndView ManageProduct( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		if(request.getParameter("fromSummaryPage") != null ){
			String []strCategoryIds = request.getParameterValues("chkCategory");
			request.getSession(true).setAttribute("chkCategory", strCategoryIds);
			String []strProductIds = request.getParameterValues("chkProduct");
			request.getSession(true).setAttribute("chkProduct", strProductIds);
            if(strProductIds == null || strProductIds.length == 0) {
                Map<String, Object> context = new HashMap<String, Object>();
                Map<Object, Object> status = new HashMap<Object, Object>();
                String[] errorCodes = new String[]{ "lms.administrator.resellerCommission.SelectProduct"};
                status.put("error", true);
                status.put("errorCodes", errorCodes);
                context.put("status", status);
                return new ModelAndView(addProductTemplate, context);
            }
			return new ModelAndView(addProductConfirmation);
		}
		else if(request.getParameter("prevtoSummaryPage") != null )
			return new ModelAndView(addProductTemplate);
		else if(request.getParameter("fromConfirmationPage") != null )
			return saveCategory(request,response,command,errors);
		else{
			


			
			Map<Object, Object> context = new HashMap<Object, Object>();
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
			
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
				if( vu360AuthDetails.getCurrentDistributor() != null ){ 											
					
					//List<TreeNode> catProd = commissionService.searchCategoriesAndProducts(vu360AuthDetails.getCurrentDistributor().getId()+"", "test", 1);
					//System.out.println("-----------------------------------------------------------------------");
					//List<TreeNode> catProd = commissionService.searchCategoriesAndProducts("10701", "Osha", 0);
					//context.put("treeList", convertTreeToList(catProd, new ArrayList<ProductOrProductCategoryNodeInfo>(), 0, ""));
							
					}
				return new ModelAndView(addProductTemplate, "context", context);
				} else
					throw new IllegalArgumentException("Distributor not found");
			}
	}
	

	
	public ModelAndView searchStoreFrontProduct(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {	

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
			if( vu360AuthDetails.getCurrentDistributor() != null ){ 			
				
				Map<Object, Object> context = new HashMap<Object, Object>();
				String varSearchProduct = request.getParameter("txtCommissionProduct");

                List<TreeNode> treeAsList = new ArrayList<TreeNode>();
                Set<CourseGroup> contractCourseGroups = new HashSet<CourseGroup>();
                treeAsList.addAll(
                        entitlementService.searchCourseGroupsByDistributor(
                                vu360AuthDetails.getCurrentDistributor(), contractCourseGroups, varSearchProduct,
                                "", "", "Course")
                );
                Long[] resellerCourseGroupIDs = entitlementService.getCourseGroupIDArrayForDistributor(
                        vu360AuthDetails.getCurrentDistributor());

                List<Long> resellerCourseGroupIds = Arrays.asList(resellerCourseGroupIDs);

                context.put("resellerCourseGroupIds", resellerCourseGroupIds);
                context.put("contractCourseGroups", contractCourseGroups);
                context.put("coursesTreeAsList", treeAsList);
                context.put("contractType", "Course");
                context.put("callMacroForChildren", "true");

				return new ModelAndView(addProductTemplate, "context", context);	
			} else
				throw new IllegalArgumentException("Distributor not found");
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
	}
	
	
	
	
	/*private List<ProductOrProductCategoryNodeInfo> convertTreeToList (List<TreeNode> treeNodeList, List<ProductOrProductCategoryNodeInfo> nodeInfoList, int level, String indexPrefix)
	{
		ProductOrProductCategoryNodeInfo nodeInfo;
		CommissionProduct commissionProduct;
		CommissionProductCategory commissionProductCategory;
		int x = 0;
		for(TreeNode node : treeNodeList)
		{
			if(node.getValue() != null)
			{
				nodeInfo = new ProductOrProductCategoryNodeInfo();
				if(node.getValue() instanceof CommissionProduct)
				{
					commissionProduct = (CommissionProduct) node.getValue();
					nodeInfo.setId(commissionProduct.getId());
					nodeInfo.setGuid(commissionProduct.getProductCode());
					nodeInfo.setLeaf(true);
					nodeInfo.setName(commissionProduct.getName());
					nodeInfo.setProduct(true);
					nodeInfo.setLevel(level);
					nodeInfo.setIndex(indexPrefix + ++x);
					nodeInfo.setParentId(commissionProduct.getCommissionProductCategory().getId()+"");
					log.info(StringUtils.leftPad("   ", level) + "commission product - " + nodeInfo.getIndex());
				}
				else
				{
					commissionProductCategory = (CommissionProductCategory) node.getValue();
					nodeInfo.setId(commissionProductCategory.getId());
					nodeInfo.setGuid(commissionProductCategory.getProductCategoryCode());
					nodeInfo.setLeaf(node.isLeaf());
					nodeInfo.setName(commissionProductCategory.getName());
					nodeInfo.setProduct(false);
					nodeInfo.setLevel(level);
					nodeInfo.setIndex(indexPrefix + ++x);
					if(commissionProductCategory.getParentCommissionProductCategory()!=null)
						nodeInfo.setParentId(commissionProductCategory.getParentCommissionProductCategory().getId()+"");
					else
						nodeInfo.setParentId("0");
					log.info(StringUtils.leftPad("   ", level) + "commission product category - " + nodeInfo.getIndex());
				}
				nodeInfoList.add(nodeInfo);
				if(node.getChildCount() > 0)
					convertTreeToList(node.getChildren(), nodeInfoList, level + 1, indexPrefix + x);
			}
			else
				log.info(StringUtils.leftPad("   ", level) + "node value is null");
		}
		return nodeInfoList;
	}
	*/
	
	
	public ModelAndView saveCategory( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		//Array element info of chkProduct =  CategoryId:ProductId:ProductName
		//Array element info of chkCategory =  ParentCategoryId:CategoryId:ProductName
		
		String commissionId =request.getSession().getAttribute("commissionId").toString();
		
		if(request.getSession().getAttribute("chkCategory")!=null && request.getSession().getAttribute("commissionId")!=null)
		{
			String []strCategoryIds = (String [])request.getSession().getAttribute("chkCategory");
			
			//Save all Categories first
			for(int i=0;i<strCategoryIds.length;i++){

                String []strArray = strCategoryIds[i].split(":");
                //CourseGroup courseGroup = courseAndCourseGroupService.getCourseGroupById(Long.parseLong(strArray[0]));
				CommissionProductCategory cpcop = commissionService.getCommissionProductCategoryByCode(strArray[1], commissionId);
				if(cpcop==null){
					CommissionProductCategory cpc = new CommissionProductCategory();
					Commission comm  = new Commission(); comm.setId(Long.valueOf(commissionId));
					cpc.setCommission(comm);
					cpc.setProductCategoryCode(strArray[1]);
					cpc.setName(strArray[2]);
					commissionService.saveCommProductCategory(cpc);
				}
			}
			
			//Setting Parent_Id if exists
			for(int i=0;i<strCategoryIds.length;i++){
				
				String []strArray = strCategoryIds[i].split(":");
				CommissionProductCategory cpcUsingCode = commissionService.getCommissionProductCategoryByCode(strArray[0], commissionId);
				if(cpcUsingCode!=null)
				{
					CommissionProductCategory cpcop = commissionService.getCommissionProductCategoryByCode(strArray[1], commissionId);
					CommissionProductCategory cpc = commissionService.loadForUpdateCommissionProductCategory(cpcop.getId());

					CommissionProductCategory cpcForRootId = new CommissionProductCategory();
					cpcForRootId.setId(cpcUsingCode.getId());
					cpc.setParentCommissionProductCategory(cpcForRootId);
					//cpcForRootId = null;
					commissionService.saveCommProductCategory(cpc);
				}
			}
		}
		
		
		//Add All selected Products
		if(request.getSession().getAttribute("chkProduct")!=null && request.getSession().getAttribute("commissionId")!=null)
		{
            List<CommissionProduct> commissionProducts = commissionService.getCommissionProduct(Long.parseLong(commissionId));
			String []strProductIds = (String [])request.getSession().getAttribute("chkProduct");
			for(int i=0;i<strProductIds.length;i++){
				
				String []strArray = strProductIds[i].split(":");
				CommissionProductCategory cpcop = commissionService.getCommissionProductCategoryByCode(strArray[0], commissionId);
				if(cpcop!=null){
                    boolean found = false;
					CommissionProduct cp = new CommissionProduct();
					cp.setProductCode(strArray[1]);
					cp.setCommissionProductCategory(cpcop);

                    for(CommissionProduct commissionProduct : commissionProducts) {
                        if((commissionProduct.getProductCode() != null && cp.getProductCode() != null) && (commissionProduct.getCommissionProductCategory() != null && cp.getProductCode() != null) && commissionProduct.getProductCode().equals(cp.getProductCode()) && commissionProduct.getCommissionProductCategory().equals(cp.getCommissionProductCategory())) {
                            found = true;
                        }
                    }

                    if(!found) {
                        Commission comm = new Commission();
                        comm.setId(Long.valueOf(commissionId));
                        cp.setCommission(comm);
                        cp.setName(strArray[2]);
                        commissionService.saveCommProduct(cp);
                    }

                }
			}
		}
		return new ModelAndView("redirect:/adm_manageCommissionProduct.do?method=searchCommissionProduct&entity=reseller");
	}

	
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
	}
	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
	}


	public String getAddProductTemplate() {
		return addProductTemplate;
	}



	public void setAddProductTemplate(String addProductTemplate) {
		this.addProductTemplate = addProductTemplate;
	}



	public CommissionService getCommissionService() {
		return commissionService;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}

    public EntitlementService getEntitlementService() {
        return entitlementService;
    }

    public void setEntitlementService(EntitlementService entitlementService) {
        this.entitlementService = entitlementService;
    }

	public String getAddProductConfirmation() {
		return addProductConfirmation;
	}



	public void setAddProductConfirmation(String addProductConfirmation) {
		this.addProductConfirmation = addProductConfirmation;
	}
	
	
	
	
}
