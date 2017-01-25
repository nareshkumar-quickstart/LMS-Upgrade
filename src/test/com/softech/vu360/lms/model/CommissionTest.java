package com.softech.vu360.lms.model;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;

/**
 * 
 * @author marium.saud
 */
@Transactional
public class CommissionTest extends TestBaseDAO<Commission> {


	//@Test
	public void Commission_should_save() throws Exception {

		Commission commission = new Commission();
		commission.setApplyToAllProducts(true);
		commission.setName("Test_Commission");
		commission.setType(1);
		//Get distributor for commission
		Distributor distributor=(Distributor)crudFindById(Distributor.class, new Long(153));
		commission.setDistributor(distributor);
				
		save(commission);

	}
	
	//@Test
	public void CommissionableParty_should_save() throws Exception {

		CommissionableParty commissionableparty = new CommissionableParty();
		commissionableparty.setName("Test_Commissionable_Party");
		//Get distributor for commissionable Party
		Distributor distributor=(Distributor)crudFindById(Distributor.class, new Long(153));
		commissionableparty.setDistributor(distributor);
				
		crudSave(CommissionableParty.class, commissionableparty);

	}

	//@Test
	public void CommissionAudit_should_save() throws Exception {

		CommissionAudit commissionAudit = new CommissionAudit();
		commissionAudit.setChangedOn(new Date());
		commissionAudit.setComments("CREATED");
		
		Commission commission=getById(new Long(3705), Commission.class);
		commissionAudit.setCommission(commission);
		commissionAudit.setNewValue("New");
		commissionAudit.setOldValue("N/A");
		VU360User user=(VU360User)crudFindById(VU360User.class, new Long(3));
		commissionAudit.setVu360User(user);
		crudSave(CommissionAudit.class, commissionAudit);

	}

	//@Test
	public void CommissionParticipation_should_save_with_root_parent_childrenCommissionParticipation() throws Exception {

		CommissionParticipation commissionParticipation = new CommissionParticipation();
		commissionParticipation.setFlat(true);
		commissionParticipation.setFlatFeeAmount(new Double(98.00));
		commissionParticipation.setPercentage(new Double(0));
		Commission commission=getById(new Long(3705), Commission.class);
		commissionParticipation.setCommission(commission);
		CommissionableParty party= (CommissionableParty)crudFindById(CommissionableParty.class, new Long(1805));
		commissionParticipation.setCommissionableParty(party);
		//Setting root id 
		CommissionParticipation mainrecord= (CommissionParticipation)crudFindById(CommissionParticipation.class, new Long(3555));
		CommissionParticipation root=new CommissionParticipation();
		root.setId(mainrecord.getId());
		commissionParticipation.setRootCommissionParticipation(root);
		CommissionParticipation parent=new CommissionParticipation();
		parent.setId(mainrecord.getId());
		commissionParticipation.setParentCommissionParticipation(parent);
		crudSave(CommissionParticipation.class, commissionParticipation);
	}
	
	@Test
	public void CommissionProduct_should_save() throws Exception {

		CommissionProduct commissionProduct = new CommissionProduct();
		commissionProduct.setName("Test_CommissionProduct_1");
		commissionProduct.setProductCode("TT_Product_01");
		//Setting commission
		Commission commission=getById(new Long(3705), Commission.class);
		commissionProduct.setCommission(commission);
	    //get product category 
		CommissionProductCategory cpc = (CommissionProductCategory) crudFindById(CommissionProductCategory.class, new Long(3260));
		commissionProduct.setCommissionProductCategory(cpc);
		crudSave(CommissionProduct.class, commissionProduct);
	}
	
	//@Test
	public void CommissionProductCategory_should_save_with_parentCommissionProductCategory() throws Exception {

		CommissionProductCategory commissionProductCat = new CommissionProductCategory();
		commissionProductCat.setName("Test_CommissionProduct");
		commissionProductCat.setProductCategoryCode("TT_PC_01");
		//Setting commission
		Commission commission=getById(new Long(3705), Commission.class);
		commissionProductCat.setCommission(commission);
		//will be run after one time insert
		//step 2. setting parent id
		CommissionProductCategory parent=new CommissionProductCategory();
		//get 
		CommissionProductCategory cpcUsingCode = (CommissionProductCategory)crudFindById(CommissionProductCategory.class, new Long(3260));
		parent.setId(cpcUsingCode.getId());
		commissionProductCat.setParentCommissionProductCategory(parent);
		crudSave(CommissionProductCategory.class, commissionProductCat);
	}


}
