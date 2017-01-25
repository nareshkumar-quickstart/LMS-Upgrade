package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @Date 14-09-2015
 * @author haider.ali
 * modified by marium.saud
 *
 */

@Entity
@Table(name = "REGULATORYANALYST")
public class RegulatoryAnalyst implements Serializable {
	
	private static final long serialVersionUID = 6029188228657344907L;

	@Id
	@javax.persistence.TableGenerator(name = "REGULATORYANALYST_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "REGULATORYANALYST", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "REGULATORYANALYST_ID")
	private Long id;
	
	@Column(name = "GLOBALANALYSTTF")
	private Boolean isForAllContentOwner = true;

	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="CONTENTOWNER_REGULATORYANALYST", joinColumns = @JoinColumn(name="REGULATORYANALYST_ID"),inverseJoinColumns = @JoinColumn(name="CONTENTOWNER_ID"))
	private List<ContentOwner> contentOwners = new ArrayList<ContentOwner>();
	
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "VU360USER_ID")
	private VU360User user ; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public  Boolean isForAllContentOwner() {
		return isForAllContentOwner;
	}

	public void setForAllContentOwner(Boolean isForAllContentOwner) {
		this.isForAllContentOwner = isForAllContentOwner;
	}

	public List<ContentOwner> getContentOwners() {
		return contentOwners;
	}

	public void setContentOwners(List<ContentOwner> contentOwners) {
		this.contentOwners = contentOwners;
	}

	public VU360User getUser() {
		return user;
	}

	public void setUser(VU360User user) {
		this.user = user;
	}

}
