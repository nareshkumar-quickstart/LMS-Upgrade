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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Commission;
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
public class ManageCommissionProdCatController extends VU360BaseMultiActionController {

	private String commissionProductCategoryListTemplate= null;
	private CommissionService commissionService;
	private String addCategoryTemplate = null;
	private String addCategoryConfirmation = null;
	 
	
	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());
    private EntitlementService entitlementService;

    @Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {		
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {	 	
	}
	
	
	public ModelAndView searchCategory( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
	    //request.getSession(true).setAttribute("feature", "ResellersCommissionableParties");
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<CommissionProductCategory> lstCP = null;
		try {
			Long commId=0L;
			if(request.getSession().getAttribute("commissionId")!=null)
				commId = Long.valueOf(request.getSession().getAttribute("commissionId").toString());

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


			//lstCP = commissionService.getCommissionProductCategory(commId);

			//CommissionProductCategorySort sort = new CommissionProductCategorySort();
			//sort.setSortDirection(sortDirection);
			//Collections.sort(lstCP, sort);
			List<CommissionProductCategory> lstCPC = commissionService.getCommissionProductCategory(commId);
			List<List<TreeNode>> CommPCTreeList = this.commissionService.getCommissionProductTreeList( lstCPC, true, 0 );


			context.put("commissionProductList", CommPCTreeList);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(commissionProductCategoryListTemplate, "context", context);
	}
	
	
	public ModelAndView searchStoreFrontCategory(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
				if( vu360AuthDetails.getCurrentDistributor() != null ){ 	
					
					Map<Object, Object> context = new HashMap<Object, Object>();
					String varSearchProduct = request.getParameter("txtCommissionCategory");

                    List<TreeNode> treeAsList = new ArrayList<TreeNode>();
                    Set<CourseGroup> contractCourseGroups = new HashSet<CourseGroup>();
                    treeAsList.addAll(
                        entitlementService.searchCourseGroupsByDistributor(
                                vu360AuthDetails.getCurrentDistributor(), contractCourseGroups, varSearchProduct,
                                "", "", "CourseGroup")
                    );
                    Long[] resellerCourseGroupIDs = entitlementService.getCourseGroupIDArrayForDistributor(
                            vu360AuthDetails.getCurrentDistributor());

                    List<Long> resellerCourseGroupIds = Arrays.asList(resellerCourseGroupIDs);

                    context.put("resellerCourseGroupIds", resellerCourseGroupIds);
                    context.put("contractCourseGroups", contractCourseGroups);
                    context.put("coursesTreeAsList", treeAsList);
                    context.put("contractType", "CourseGroup");
                    context.put("callMacroForChildren", "false");
					return new ModelAndView(addCategoryTemplate, "context", context);
				
			} else
				throw new IllegalArgumentException("Distributor not found");
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
	}
	
	public ModelAndView deleteCommissionableProduct(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
			
				
				if( vu360AuthDetails.getCurrentDistributor() != null ){ 	
					 HttpSession session = request.getSession(true);
					 String[] fieldsToDelete=request.getParameterValues("commissionProductCheck");
					
					if  ( fieldsToDelete!=null && fieldsToDelete.length>0) {
						Long[] ids = new Long[fieldsToDelete.length];
						for ( int i = 0 ; i < fieldsToDelete.length ; i++ ) {
							ids[i] = Long.valueOf(fieldsToDelete[i]);				 
						}			
						if(validateDeleteData(fieldsToDelete, request))
						{
							commissionService.deleteCommissionProductCategory(ids);
							session.setAttribute("validateCommParCat",null);
						}
						else
							session.setAttribute("validateCommParCat", "error.resellerCommission.childCommissionProductCategory.exists");
					}

				return searchCategory(request, response, command, errors) ;

				
			} else
				throw new IllegalArgumentException("Distributor not found");
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
	}
	
	
	
	private boolean validateDeleteData(String prodCategoryArray[], HttpServletRequest request){
		boolean bRet=true;
		Long[] pIds = new Long[prodCategoryArray.length];
		for (int i = 0; i < pIds.length; i++) {
			pIds[i]=Long.valueOf(prodCategoryArray[i]);
		}

		List<CommissionProductCategory> commProductCats =	commissionService.getCommissionProductCategoryByRootIds(pIds);
			if(commProductCats!=null && commProductCats.size()>0){
				log.info("Children of Commission Participation size  = " + commProductCats.size());
				bRet= false;
			}
			
		return bRet;
	}
	
	public ModelAndView PageNav( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		if(request.getParameter("prevtoSummaryPage") != null )
			return new ModelAndView(addCategoryTemplate);
		else if(request.getParameter("fromSummaryPage") != null )
		{
			String []strCategoryIds = request.getParameterValues("chkCategory");
			request.getSession(true).setAttribute("chkCategory", strCategoryIds);

            if(strCategoryIds == null || strCategoryIds.length == 0) {
                Map<String, Object> context = new HashMap<String, Object>();
                Map<Object, Object> status = new HashMap<Object, Object>();
                String[] errorCodes = new String[]{ "lms.administrator.resellerCommission.SelectCategory"};
                status.put("error", true);
                status.put("errorCodes", errorCodes);
                context.put("status", status);
                return new ModelAndView(addCategoryTemplate, context);
            }

			return new ModelAndView(addCategoryConfirmation);
		}
			
		else
			return new ModelAndView(addCategoryTemplate);
	}
	
	public ModelAndView saveCategory( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		//Array element info of chkCategory =  ParentCategoryId:CategoryId:ProductName
		
		if(request.getSession().getAttribute("chkCategory")!=null && request.getSession().getAttribute("commissionId")!=null)
		{
			String commissionId = request.getSession().getAttribute("commissionId").toString();
			String []strCategoryIds = (String [])request.getSession().getAttribute("chkCategory");
			
			// following loop use to Save all selected category first
			for(int i=0;i<strCategoryIds.length;i++){

                boolean found = false;
				String []strArray = strCategoryIds[i].split(":");

                CommissionProductCategory cpc = new CommissionProductCategory();
                Commission comm  = new Commission(); comm.setId(Long.valueOf(commissionId));
                cpc.setCommission(comm);
                cpc.setProductCategoryCode(strArray[1]);
                cpc.setName(strArray[2]);

                CommissionProductCategory commissionProductCategory = commissionService.getCommissionProductCategoryByCode(cpc.getProductCategoryCode(), commissionId);

                if(commissionProductCategory == null || !commissionProductCategory.getProductCategoryCode().equals(cpc.getProductCategoryCode()))
                {
				    commissionService.saveCommProductCategory(cpc);
                }
			}
			
			// Following code use to set the Parent Category Id of the selected categories 
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

                    CommissionProductCategory commissionProductCategory = commissionService.getCommissionProductCategoryByCode(cpc.getProductCategoryCode(), commissionId);

                    if(commissionProductCategory == null || ((commissionProductCategory.getProductCategoryCode() != null && cpc.getProductCategoryCode() != null) && (commissionProductCategory.getParentCommissionProductCategory() != null) &&  !(commissionProductCategory.getProductCategoryCode().equals(cpc.getProductCategoryCode()) && commissionProductCategory.getParentCommissionProductCategory().equals(cpcForRootId))))
                    {
                        commissionService.saveCommProductCategory(cpc);
                    }
				}
			}
		}
		return searchCategory(request, response, command, errors) ;
	}
	
	
	public String getCommissionProductCategoryListTemplate() {
		return commissionProductCategoryListTemplate;
	}

	public void setCommissionProductCategoryListTemplate(
			String commissionProductCategoryListTemplate) {
		this.commissionProductCategoryListTemplate = commissionProductCategoryListTemplate;
	}

	public CommissionService getCommissionService() {
		return commissionService;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}

	public String getAddCategoryTemplate() {
		return addCategoryTemplate;
	}

	public void setAddCategoryTemplate(String addCategoryTemplate) {
		this.addCategoryTemplate = addCategoryTemplate;
	}

	public String getAddCategoryConfirmation() {
		return addCategoryConfirmation;
	}

	public void setAddCategoryConfirmation(String addCategoryConfirmation) {
		this.addCategoryConfirmation = addCategoryConfirmation;
	}


    public void setEntitlementService(EntitlementService entitlementService) {
        this.entitlementService = entitlementService;
    }

}
