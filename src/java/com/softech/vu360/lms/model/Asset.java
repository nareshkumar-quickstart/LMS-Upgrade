package com.softech.vu360.lms.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Parameter;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */

@Entity
@Table(name = "ASSET")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ASSETTYPE")
public class Asset implements SearchableKey {
	

	private static final long serialVersionUID = 1L;
	public static String ASSET_TYPE_AUDIOCLIP="Audio Clip";
	public static String ASSET_TYPE_CERTIFICATE="Certificate";
	public static String ASSET_TYPE_AFFIDAVIT="Affidavit";
	public static String ASSET_TYPE_DOCUMENT="Document";
	public static String ASSET_TYPE_FLASHOBJECT="Flash Object";
	public static String ASSET_TYPE_IMAGE="Image";
	public static String ASSET_TYPE_MOVIEOCLIP="Movie Clip";
	public static String ASSET_TYPE_TEXT="Text";
	public static String ASSET_TYPE_WEBLINK="Web Link";
	
	/*
	@Id
	@javax.persistence.TableGenerator(name = "ASSET_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "asset", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ASSET_ID")
	*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "seqAssetId")
	@GenericGenerator(name = "seqAssetId", strategy = "com.softech.vu360.lms.model.PrimaryKeyGenerator", parameters = {
	        @Parameter(name = "table_name", value = "VU360_SEQ"),
	        @Parameter(name = "value_column_name", value = "NEXT_ID"),
	        @Parameter(name = "segment_column_name", value = "TABLE_NAME"),
	        @Parameter(name = "segment_value", value = "ASSET") })
	private Long id;
	
	@Column(name = "CURRENT_ASSETVERSION_ID")
	private Long currentAssetVersionId;
	
	@Nationalized
	@Column(name = "NAME")
	private String name = null;
	
	
	@OneToOne (fetch=FetchType.LAZY)
	@JoinColumn(name = "CONTENTOWNER_ID")
	private ContentOwner contentowner = null;
	
	@Column(name = "KEYWORDS")
	private String keywords="";
	
	@Transient
	private String assetType="";
	
	public Asset(){
		
	}
	
	public Asset(String assetType){
		this.setAssetType(assetType);
	}
	
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return id.toString();
	}
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
	 * @return the currentAssetVersionId
	 */
	public Long getCurrentAssetVersionId() {
		return currentAssetVersionId;
	}
	/**
	 * @param currentAssetVersionId the currentAssetVersionId to set
	 */
	public void setCurrentAssetVersionId(Long currentAssetVersionId) {
		this.currentAssetVersionId = currentAssetVersionId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the contentowner
	 */
	public ContentOwner getContentowner() {
		return contentowner;
	}
	/**
	 * @param contentowner the contentowner to set
	 */
	public void setContentowner(ContentOwner contentowner) {
		this.contentowner = contentowner;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getAssetType() {
		return "";
	}
	
	
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	
}
