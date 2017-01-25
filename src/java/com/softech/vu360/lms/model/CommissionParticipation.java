package com.softech.vu360.lms.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COMMISSIONPARTICIPATION")
public class CommissionParticipation implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqCommissionParticipationId")
	@GenericGenerator(name = "seqCommissionParticipationId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "CommissionParticipation") })
	private Long id;
	
	@Column(name = "FLATTF")
	private Boolean flat;
	
	@Column(name = "FLATFEEAMOUNT")
    private Double flatFeeAmount;
	
	@Column(name = "PERCENTAGE")
    private Double percentage;
	
	@OneToOne
    @JoinColumn(name="ROOT_ID")
	private CommissionParticipation rootCommissionParticipation ;
	
	// there should be @ManyToOne(fetch = FetchType.LAZY)
	@OneToOne
    @JoinColumn(name="PARENT_ID")
    private CommissionParticipation parentCommissionParticipation ;
    
    @OneToOne
    @JoinColumn(name="COMMISSION_ID")
    private Commission commission ;
    
    @OneToOne
    @JoinColumn(name="COMMISSIONABLEPARTY_ID")
    private CommissionableParty commissionableParty ;  
    
    @OneToMany(cascade={CascadeType.REMOVE})
    @JoinColumn(name="PARENT_ID")
    private List<CommissionParticipation> childrenCommissionParticipation = new ArrayList<CommissionParticipation>();
	
    public Long getId() {
        return this.id;
    }

    public double getPercentage() {
        return this.percentage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
    public CommissionParticipation getRootCommissionParticipation() {
		return this.rootCommissionParticipation;
	}

	public void setRootCommissionParticipation(
			CommissionParticipation rootCommissionParticipation) {
		this.rootCommissionParticipation = rootCommissionParticipation;
	}

	public CommissionParticipation getParentCommissionParticipation() {
		return this.parentCommissionParticipation;
	}

	public void setParentCommissionParticipation(
			CommissionParticipation parentCommissionParticipation) {
		this.parentCommissionParticipation = parentCommissionParticipation;
	}

	public Commission getCommission() {
		return this.commission;
	}

	public void setCommission(Commission commission) {
		this.commission = commission;
	}

	 public Boolean getFlat() {
			return flat;
	}
    
    public void setFlat(Boolean flat) {
		this.flat = flat;
	}
	    

	public double getFlatFeeAmount() {
		return flatFeeAmount;
	}

	public void setFlatFeeAmount(double flatFeeAmount) {
		this.flatFeeAmount = flatFeeAmount;
	}

	public CommissionableParty getCommissionableParty() {
		return this.commissionableParty;
	}

	public void setCommissionableParty(CommissionableParty commissionableParty) {
		this.commissionableParty = commissionableParty;
	}

	public String getKey() {
        return String.valueOf(id);
    }

    public List<CommissionParticipation> getChildrenCommissionParticipation() {
        return childrenCommissionParticipation;
    }

    public void setChildrenCommissionParticipation(List<CommissionParticipation> childrenCommissionParticipation) {
        this.childrenCommissionParticipation = childrenCommissionParticipation;
    }
    
}
