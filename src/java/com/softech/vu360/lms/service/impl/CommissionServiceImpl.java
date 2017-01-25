package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.softech.vu360.lms.model.Commission;
import com.softech.vu360.lms.model.CommissionAudit;
import com.softech.vu360.lms.model.CommissionParticipation;
import com.softech.vu360.lms.model.CommissionProduct;
import com.softech.vu360.lms.model.CommissionProductCategory;
import com.softech.vu360.lms.model.CommissionableParty;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.repositories.CommissionAuditRepository;
import com.softech.vu360.lms.repositories.CommissionParticipationRepository;
import com.softech.vu360.lms.repositories.CommissionProductCategoryRepository;
import com.softech.vu360.lms.repositories.CommissionProductRepository;
import com.softech.vu360.lms.repositories.CommissionRepository;
import com.softech.vu360.lms.repositories.CommissionablePartyRepository;
import com.softech.vu360.lms.service.CommissionService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.webservice.client.StorefrontClientWS;
import com.softech.vu360.util.CommissionParticipationTree;
import com.softech.vu360.util.CommissionProductTree;
import com.softech.vu360.util.TreeNode;
/**
 * User: joong
 * Date: Nov 7, 2009
 */
public class CommissionServiceImpl implements CommissionService {
	private static final Logger log = Logger.getLogger(CommissionServiceImpl.class.getName());
	
    @Inject
	private CommissionablePartyRepository commissionablePartyRepository;
    @Inject
	private CommissionAuditRepository commissionAuditRepository;
    @Inject
	private CommissionProductRepository commissionProductRepository;
    @Inject
	private CommissionProductCategoryRepository commissionProductCategoryRepository;
    @Inject
	private CommissionParticipationRepository commissionParticipationRepository;
    @Inject
	private CommissionRepository commissionRepository;
    
    private DistributorService distributorService = null;
    private StorefrontClientWS storefrontClientWS=null;

    public Commission findCommissionById(long id) {
        Preconditions.checkArgument(0 != id, "Commission ID must be non-zero");
        return commissionRepository.findOne(id);
    }

    @Transactional
    public Commission addCommission(Commission commission, Distributor distributor) {
        Preconditions.checkNotNull(distributor, "Distributor is null");
        commission.setDistributor(distributor);
        return commissionRepository.save(commission);
    }

    public Commission addCommission(Commission commission, long distributorId) {
        Preconditions.checkArgument(distributorId != 0, "Distributor ID is zero");
        return this.addCommission(commission, distributorService.getDistributorById(distributorId));
    }

    public void setDistributorService(DistributorService distributorService) {
        this.distributorService = distributorService;
    }
    
    public HashSet<CommissionableParty> getCommissionableParties(Long distributorId){    	
    	return commissionablePartyRepository.findByDistributorId(distributorId);    	    	
    }
	
    public HashSet<Commission> getCommissions(Long distributorId){    	
    	return commissionRepository.getCommissions(distributorId);    	    	
    }

    @Override
    @Transactional
	public boolean deleteCommissionableParties(Long[] commPartyIds)
    {
    	if(commissionParticipationRepository.countByCommissionablePartyIdIn(commPartyIds)>0){
    		return false;
    	}
    	
/*		for(int i=0;i<commPartyIds.length;i++){			
			commissionablePartyRepository.delete(commissionablePartyRepository.findOne(commPartyIds[i]));	
		}
		return true;
*/	
    	List<CommissionableParty> aListCommissionableParty= commissionablePartyRepository.findByIdIn(commPartyIds);
		commissionablePartyRepository.delete(aListCommissionableParty);	
		return true;
    	
    }
    
	@Override
	public CommissionableParty loadForUpdateCommissionableParty(Long commPartyId){
		return commissionablePartyRepository.findOne(commPartyId) ;	
		
	}
	
	public List<TreeNode> searchCategoriesAndProducts(String storeId, String searchKeyword,int searchType){
		return storefrontClientWS.searchProductOrProductCategories(storeId, searchKeyword, searchType);
	}
	
	@Override
	public Commission loadForUpdateCommission(Long commId){
		return commissionRepository.findOne(commId) ;	
		
	}
	
	@Override
	@Transactional
	public void logCommissionChangeForAudit (CommissionAudit commissionAudit){
		commissionAuditRepository.save(commissionAudit);		
	}
	
	@Override
	@Transactional
	public void saveCommissionableParty(CommissionableParty commissionableParty) {
		commissionablePartyRepository.save(commissionableParty); 		
	}
	
	@Transactional
	public void saveCommission(Commission commission) {
		commissionRepository.save(commission); 		
	}
	
	
	public List<CommissionProduct> getCommissionProduct(Long commissionId)  { 	
		return commissionProductRepository.findByCommissionId(commissionId);    	    	
	}
	
	@Transactional
	public void deleteCommissionProduct(Long[] commPartyIds){
		commissionProductRepository.delete(commissionProductRepository.findByIdIn(commPartyIds));	
	}

	public List<CommissionProductCategory> getCommissionProductCategory(Long commissionId)  { 	
    	return commissionProductCategoryRepository.findByCommissionId(commissionId);    	    	
	}
	
	@Transactional
	public void deleteCommissionProductCategory(Long[] commIds){
		commissionProductCategoryRepository.delete(commissionProductCategoryRepository.findByIdIn(commIds));	
	}
 
 	@Override
	public CommissionProductCategory loadForUpdateCommissionProductCategory(Long commPartyId) {
		return commissionProductCategoryRepository.findOne(commPartyId) ;
	}
	 
 	public List<CommissionParticipation> getAllCommissionParticipation(Long commissionId){
		List<CommissionParticipation> courseGroupList = commissionParticipationRepository.findByCommissionId(commissionId);
		return courseGroupList ;
	}
	 
 	@Override
	public List<List<TreeNode>> getCommissionParticipationTreeList(List<CommissionParticipation> courseGroupList, boolean enableNodes,  long selectedNodeId) {
		
		List<List<TreeNode>> treeNodeList = new ArrayList<List<TreeNode>>();
		
		if ( courseGroupList.size() > 0 ){
			CommissionParticipationTree cgTree = new CommissionParticipationTree(enableNodes);
			
			for ( CommissionParticipation courseGroup : courseGroupList ) {
				cgTree.addNode( courseGroup );
			}
			
			List<TreeNode> rootNodeList = cgTree.getRootNodes();
			for ( TreeNode rootNode : rootNodeList ) {
				treeNodeList.add( rootNode.bfs() );
			}
		}
		return treeNodeList;
	}
	 
 	@Override
	public List<List<TreeNode>> getCommissionProductTreeList(List<CommissionProductCategory> courseGroupList, boolean enableNodes,  long selectedNodeId) {
		
		List<List<TreeNode>> treeNodeList = new ArrayList<List<TreeNode>>();
		
		if ( courseGroupList.size() > 0 ){
			CommissionProductTree cgTree = new CommissionProductTree(enableNodes);
			
			for ( CommissionProductCategory cpc : courseGroupList ) {
				cgTree.addNode( cpc );
			}
			
			List<TreeNode> rootNodeList = cgTree.getRootNodes();
			for ( TreeNode rootNode : rootNodeList ) {
				treeNodeList.add( rootNode.bfs() );
			}
		}
		return treeNodeList;
	}
	 
	 	
 	@Override
 	@Transactional
 	public void deleteCommissionParticipation(Long[] commParticipationIds){
		commissionParticipationRepository.delete(commissionParticipationRepository.findByIdIn(commParticipationIds));
	}
 
 	@Override
	public CommissionParticipation loadForUpdateCommissionParticipation(Long commParticipationId) {
		return commissionParticipationRepository.findOne(commParticipationId);
	}
 
 	@Override
 	public List<CommissionParticipation>  getCommissionParticipationByIds(String CommissionParticipationIds[]){
 		if(CommissionParticipationIds==null) return null;
 		
 		Long[] ids=new Long[CommissionParticipationIds.length];
 		for (int i = 0; i < ids.length; i++) {
			ids[i]=Long.valueOf(CommissionParticipationIds[i]);
		}
 		
		return commissionParticipationRepository.findByIdIn(ids);
	}
 	
 	
 	public StorefrontClientWS getStorefrontClientWS() {
		return storefrontClientWS;
	}

	public void setStorefrontClientWS(StorefrontClientWS storefrontClientWS) {
		this.storefrontClientWS = storefrontClientWS;
	}
	
	
	
	@Override
	@Transactional
	public CommissionParticipation saveCommissionParticipation(CommissionParticipation commissionParticipation) {
		 return commissionParticipationRepository.save(commissionParticipation); 		
	}

	@Override
 	public List<CommissionProductCategory>  getCommissionProductCategoryByRootIds(Long[] CommProdCatIds){
 		return commissionProductCategoryRepository.findByParentIdIn(Arrays.asList(CommProdCatIds));
 	}
	
	@Override
	public CommissionProductCategory  getCommissionProductCategoryByCode(String catCode, String commId){
		CommissionProductCategory entity= commissionProductCategoryRepository.findByProductCategoryCodeAndCommissionId(catCode, Long.valueOf(commId));
		if(entity==null){
			log.debug("CommissionProductCategory with productCategoryCode: " + catCode + "is not found");
		}
		return entity;
 	}
	
	@Override
	@Transactional
	public void saveCommProductCategory(CommissionProductCategory commProductCategory){
		commissionProductCategoryRepository.save(commProductCategory); 		
	}
	
	@Override
	@Transactional
	public void saveCommProduct(CommissionProduct commProduct){
		commissionProductRepository.save(commProduct); 		
	}
	
	@Override
	public List<CommissionProduct> getCommissionProductByCategoryId(Long[] catId)  { 	
    	return commissionProductRepository.findByCommissionProductCategoryIdIn(catId);    	    	
	}
		
}