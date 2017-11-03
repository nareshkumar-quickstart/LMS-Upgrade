package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LabType")
public class LabType {
   
	private Long id;
    private String labName;
    private String labURL;
    private String instructions;

	@Column(name = "IsActive")
	private Boolean isActive;
	
	@Column(name = "IsThirdParty")
	private Boolean isThirdParty;
	
    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "LabName")
    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    @Column(name = "LabURL")
    public String getLabURL() {
        return labURL;
    }

    public void setLabURL(String labURL) {
        this.labURL = labURL;
    }
    
    @Column(name = "Instructions")
    public String getInstructions() {
        return instructions;
    }

    public void setinstructions(String instructions) {
        this.instructions = instructions;
    }

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsThirdParty() {
		return isThirdParty;
	}

	public void setIsThirdParty(Boolean isThirdParty) {
		this.isThirdParty = isThirdParty;
	}
    
    
}