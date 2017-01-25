package com.softech.vu360.util;

public class LabelBean  implements java.io.Serializable{
	private String mLabel = "";
	private String mValue = "";

	public LabelBean(String label, String value){
		mLabel = label;
		mValue = value;
	}

	public String getLabel() {
		return mLabel;
	}

	public void setLabel(String pLabel) {
		mLabel = pLabel;
	}

	public String getValue() {
		return mValue;
	}

	public void setValue(String pValue) {
		mValue = pValue;
	}
	
	public String toString(){
		return "Name::"+mLabel+" Value::"+mValue;
	}
	
	
}
