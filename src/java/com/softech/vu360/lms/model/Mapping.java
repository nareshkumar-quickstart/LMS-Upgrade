package com.softech.vu360.lms.model;

public class Mapping implements SearchableKey {
    private long id;
	private String key;
    private String value;
    private  Boolean isSystem;
    private double uniqueKeyId;
    private double uniqueValueId;
    private String valueSelectionCode;
    
	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public  Boolean isSystem() {
		return isSystem;
	}


	public void setSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}


	public void setKey(String key) {
		this.key = key;
	}


	
	public String getKey() {
		
		return key;
	}


	public double getUniqueKeyId() {
		return uniqueKeyId;
	}


	public void setUniqueKeyId(double uniqueKeyId) {
		this.uniqueKeyId = uniqueKeyId;
	}


	public double getUniqueValueId() {
		return uniqueValueId;
	}


	public void setUniqueValueId(double uniqueValueId) {
		this.uniqueValueId = uniqueValueId;
	}

  
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getValueSelectionCode() {
		return valueSelectionCode;
	}


	public void setValueSelectionCode(String valueSelectionCode) {
		this.valueSelectionCode = valueSelectionCode;
	}


	@Override
    public String toString() {
        return "Mapping{" + "key=" + key + 
                ", value=" + value + 
                ", isSystem=" + isSystem + 
                ", uniqueKeyId=" + uniqueKeyId + 
                ", uniqueValueId=" + uniqueValueId + '}';
    }
	
}
