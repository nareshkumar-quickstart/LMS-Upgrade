package com.softech.vu360.lms.service;

import java.util.HashSet;
import java.util.List;

import com.softech.vu360.lms.model.Commission;
import com.softech.vu360.lms.model.CommissionAudit;
import com.softech.vu360.lms.model.CommissionParticipation;
import com.softech.vu360.lms.model.CommissionProduct;
import com.softech.vu360.lms.model.CommissionProductCategory;
import com.softech.vu360.lms.model.CommissionableParty;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.util.TreeNode;

/**
 * User: joong
 * Date: Nov 7, 2009
 */
public interface CommissionService {
    public Commission findCommissionById(long id);
    public Commission addCommission(Commission commission, Distributor distributor);
    public Commission addCommission(Commission commission, long distributorId);
    public HashSet<CommissionableParty> getCommissionableParties(Long distributorId);
    public HashSet<Commission> getCommissions(Long distributorId);
    public CommissionableParty loadForUpdateCommissionableParty(Long commPartyId);
    public boolean deleteCommissionableParties(Long[] deleteIds);
    public void saveCommissionableParty(CommissionableParty commissionableParty);
    
    public Commission loadForUpdateCommission(Long commId);
    public void saveCommission(Commission commission);
    
    public void logCommissionChangeForAudit (CommissionAudit commissionAudit);
        
    public List<CommissionProduct> getCommissionProduct(Long commissionId);   
    public void deleteCommissionProduct(Long[] deleteIds);
    //public CommissionProduct loadForUpdateCommissionProduct(Long commPartyId) ;
    
    
    public List<CommissionProductCategory> getCommissionProductCategory(Long commissionId);
    public void deleteCommissionProductCategory(Long[] deleteIds);
    public CommissionProductCategory loadForUpdateCommissionProductCategory(Long commPartyId) ;
	
    public List<CommissionParticipation> getAllCommissionParticipation(Long commissionId);
    public List<List<TreeNode>> getCommissionParticipationTreeList (List<CommissionParticipation> CommissionParticipationList, boolean enableNodes, long selectedNodeId);
    
    public void deleteCommissionParticipation(Long[] deleteIds);
    public CommissionParticipation loadForUpdateCommissionParticipation(Long commParticipationId);
	
	public List<CommissionParticipation>  getCommissionParticipationByIds(String CommParticipationIds[]);
	public List<TreeNode> searchCategoriesAndProducts(String storeId, String searchKeyword,int searchType);
	
	public CommissionParticipation saveCommissionParticipation(CommissionParticipation commissionParticipation);
	//public CommissionParticipation loadForDeleteCommissionParticipation(long commParticipationId);
	
	public List<List<TreeNode>> getCommissionProductTreeList (List<CommissionProductCategory> CommissionProduct, boolean enableNodes, long selectedNodeId);
	public List<CommissionProductCategory>  getCommissionProductCategoryByRootIds(Long[] CommProdCatIds);
	public CommissionProductCategory  getCommissionProductCategoryByCode(String catCode, String commId);
	
	public void saveCommProductCategory(CommissionProductCategory commProductCategory);
	public void saveCommProduct(CommissionProduct commProduct);
	
	public List<CommissionProduct> getCommissionProductByCategoryId(Long[] catId);
	
}
