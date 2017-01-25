package com.softech.vu360.lms.model;

import java.io.Serializable;
import java.util.Date;

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
 * @author raja.ali
 * 2015/11/19
 *
 */
@Entity
@Table(name = "ADLSTORE")
public class ADLStore implements SearchableKey, Serializable {

	private static final long serialVersionUID = -6324945516629476891L;
	
	@Id
	@javax.persistence.TableGenerator(name = "ADLSTORE_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ADLSTORE_ID")
    private Long id;
	
	@Column(name = "ADLSTOREID")
	private String adlStoreId = null;
	
	@Column(name = "STORE")
	private String store = null;
	
	@Column(name = "CREATEDDATE")
	private Date createdDate = null;
	
	@Column(name = "LASTUPDATEDDATE")
	private Date lastUpatedDate = null;
	
	@OneToOne
    @JoinColumn(name="LEARNERSCOSTATISTIC_ID")
	private LearnerSCOStatistics learnerScoStatistics;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the searchableKey
	 */
	public String getKey() {
		return id.toString();
	}
	/**
	 * @return the adlStoreId
	 */
	public String getAdlStoreId() {
		return adlStoreId;
	}
	/**
	 * @param adlStoreId the adlStoreId to set
	 */
	public void setAdlStoreId(String adlStoreId) {
		this.adlStoreId = adlStoreId;
	}
	/**
	 * @return the store
	 */
	public String getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(String store) {
		this.store = store;
	}
	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return the lastUpatedDate
	 */
	public Date getLastUpatedDate() {
		return lastUpatedDate;
	}
	/**
	 * @param lastUpatedDate the lastUpatedDate to set
	 */
	public void setLastUpatedDate(Date lastUpatedDate) {
		this.lastUpatedDate = lastUpatedDate;
	}
	/**
	 * @return the learnerScoStatistics
	 */
	public LearnerSCOStatistics getLearnerScoStatistics() {
		return this.learnerScoStatistics;
	}
	/**
	 * @param learnerScoStatistics the learnerScoStatistics to set
	 */
	public void setLearnerScoStatistics(LearnerSCOStatistics learnerScoStatistics) {
		this.learnerScoStatistics = learnerScoStatistics;
	}
}
