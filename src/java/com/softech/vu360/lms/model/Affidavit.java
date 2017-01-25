package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Nationalized;


/**
 * 
 * @author marium.saud
 *
 */
@Entity
@DiscriminatorValue("Affidavit")
public class Affidavit extends Document {
	
	private static final long serialVersionUID = -3697835907257851984L;
	public static String AFFIDAVIT_TYPE_FILE = "file";
	public static String AFFIDAVIT_TYPE_TEMPLATE = "template";
	
	@Nationalized
	@Column(name = "DISPLAYTEXT1")
	private String content = null;
	
	@Nationalized
	@Column(name = "DISPLAYTEXT2")
	private String content2 = null;
	
	@Nationalized
	@Column(name = "DISPLAYTEXT3")
	private String content3 = null;
	
	@Column(name = "AFFIDAVITTEMPLATE_ID")
	private Long templateId;
	
	@Column(name = "AFFIDAVITTYPE")
	private String affidavitType=""; 

	public Affidavit(){
		super(ASSET_TYPE_AFFIDAVIT);
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getContent2() {
		return content2;
	}
	public void setContent2(String content2) {
		this.content2 = content2;
	}

	public String getContent3() {
		return content3;
	}
	public void setContent3(String content3) {
		this.content3 = content3;
	}
	
	public void setNoOfAffidavitsPerPage(Integer count){
		this.setNoOfDocumentsPerPage(count);
	}
	
	public Integer getNoOfAffidavitsPerPage(){
		return super.getNoOfDocumentsPerPage();
	}
	
	public Long getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getAffidavitType() {
		return affidavitType;
	}

	public void setAffidavitType(String affidavitType) {
		this.affidavitType = affidavitType;
	}
	

}