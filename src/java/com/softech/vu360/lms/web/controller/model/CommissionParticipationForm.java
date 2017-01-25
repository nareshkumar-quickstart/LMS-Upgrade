package com.softech.vu360.lms.web.controller.model;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * User: Rehan
 * Date: May 26, 2011
 */
public class CommissionParticipationForm  implements ILMSBaseInterface{

	private static final long serialVersionUID = 1L;
	private long id;
	private String rootCommissionParticipation = "";
    private String parentCommissionParticipation = "";
    private String parentCommissionParticipationName = "";
    private String commission = "";
    private String flat = "";
    private String flatFeeAmount = "";    
    private String percentage = "";
    private String commissionablePartyId = "";
    private String commissionablePartyName = "";
    private String childrenCommissionParticipation = "";
    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRootCommissionParticipation() {
		return rootCommissionParticipation;
	}
	public void setRootCommissionParticipation(String rootCommissionParticipation) {
		this.rootCommissionParticipation = rootCommissionParticipation;
	}
	public String getParentCommissionParticipation() {
		return parentCommissionParticipation;
	}
	public void setParentCommissionParticipation(
			String parentCommissionParticipation) {
		this.parentCommissionParticipation = parentCommissionParticipation;
	}
	public String getCommission() {
		return commission;
	}
	public void setCommission(String commission) {
		this.commission = commission;
	}
	public String getFlat() {
		return flat;
	}
	public void setFlat(String flat) {
		this.flat = flat;
	}
	public String getFlatFeeAmount() {
		return flatFeeAmount;
	}
	public void setFlatFeeAmount(String flatFeeAmount) {
		this.flatFeeAmount = flatFeeAmount;
	}
	public String getPercentage() {
		return percentage;
	}
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}


	
	
	public String getCommissionablePartyId() {
		return commissionablePartyId;
	}
	public void setCommissionablePartyId(String commissionablePartyId) {
		this.commissionablePartyId = commissionablePartyId;
	}
	public String getCommissionablePartyName() {
		return commissionablePartyName;
	}
	
	public void setCommissionablePartyName(String commissionablePartyName) {
		this.commissionablePartyName = commissionablePartyName;
	}
	public String getChildrenCommissionParticipation() {
		return childrenCommissionParticipation;
	}
	public void setChildrenCommissionParticipation(
			String childrenCommissionParticipation) {
		this.childrenCommissionParticipation = childrenCommissionParticipation;
	}
   
	
    
	 public String getParentCommissionParticipationName() {
		return parentCommissionParticipationName;
	}
	public void setParentCommissionParticipationName(
			String parentCommissionParticipationName) {
		this.parentCommissionParticipationName = parentCommissionParticipationName;
	}
	public void reset ()
	    {
			 this.setId(0);
			 this.setRootCommissionParticipation(null);
			 this.setParentCommissionParticipation(null);
			 this.setCommission(null);
			 this.setFlat(null);
			 this.setFlatFeeAmount(null);    
			 this.setPercentage(null);
			 this.setCommissionablePartyId(null);
			 this.setCommissionablePartyName(null);
			 this.setChildrenCommissionParticipation(null);
	    }
}
