package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * 
 * @author marium.saud
 *
 */
@Entity
@Table(name = "COMMISSIONABLEPARTY")
public class CommissionableParty implements SearchableKey {

	private static final long serialVersionUID = 1L;
	
	@Id
    @javax.persistence.TableGenerator(name = "COMMISSIONABLEPARTY_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "COMMISSIONABLEPARTY", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "COMMISSIONABLEPARTY_ID")
	private Long id;
	
	@Column(name = "NAME")
	private String name;
	
	@OneToOne
    @JoinColumn(name="DISTRIBUTOR_ID")
    private Distributor distributor = null;
	

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
        return String.valueOf(id);
    }
}
