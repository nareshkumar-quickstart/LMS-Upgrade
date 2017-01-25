package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class EasySignupPageForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	
	// Variables for New User
	private String nFirstName = null;
	private String nLastName = null;
	private String nEmailAddress = null;
	private String nEmailAddressConfirm = null;
	private String nUserNameOption = null;
	private String nUserName = null;
	private String nPassword = null;
	private String nPasswordConfirm = null;

	// Variables for Returning User
	private String rUserName = null;
	private String rPassword = null;
	
	private List<String> courseList = new ArrayList<String>();
	
	public String getnFirstName() {
		return nFirstName;
	}
	public void setnFirstName(String nFirstName) {
		this.nFirstName = nFirstName;
	}
	
	public String getnLastName() {
		return nLastName;
	}
	public void setnLastName(String nLastName) {
		this.nLastName = nLastName;
	}
	
	public String getnEmailAddress() {
		return nEmailAddress;
	}
	public void setnEmailAddress(String nEmailAddress) {
		this.nEmailAddress = nEmailAddress;
	}
	
	public String getnEmailAddressConfirm() {
		return nEmailAddressConfirm;
	}
	public void setnEmailAddressConfirm(String nEmailAddressConfirm) {
		this.nEmailAddressConfirm = nEmailAddressConfirm;
	}
	
	public String getnUserNameOption() {
		return nUserNameOption;
	}
	public void setnUserNameOption(String nUserNameOption) {
		this.nUserNameOption = nUserNameOption;
	}
	
	public String getnUserName() {
		return nUserName;
	}
	public void setnUserName(String nUserName) {
		this.nUserName = nUserName;
	}
	
	public String getnPassword() {
		return nPassword;
	}
	public void setnPassword(String nPassword) {
		this.nPassword = nPassword;
	}
	
	public String getnPasswordConfirm() {
		return nPasswordConfirm;
	}
	public void setnPasswordConfirm(String nPasswordConfirm) {
		this.nPasswordConfirm = nPasswordConfirm;
	}
	
	public String getrUserName() {
		return rUserName;
	}
	public void setrUserName(String rUserName) {
		this.rUserName = rUserName;
	}
	
	public String getrPassword() {
		return rPassword;
	}
	public void setrPassword(String rPassword) {
		this.rPassword = rPassword;
	}
	public List<String> getCourseList() {
		return courseList;
	}
	public void setCourseList(List<String> courseList) {
		this.courseList = courseList;
	}
	


}