package com.softech.vu360.lms.meetingservice.dimdim;

import net.sf.json.JSONObject;

public class DimDimCreateUserRequest extends DimDimRequest {

	public DimDimCreateUserRequest(){
		setURL(DimDimConfiguration.getInstance().getCreateUserURL());
	}
	public Boolean getNotify() {
		return notify;
	}


	public void setNotify(Boolean notify) {
		this.notify = notify;
	}


	public String getEnterpriseName() {
		return enterpriseName;
	}


	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}


	public String getNewUserName() {
		return newUserName;
	}


	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
	}


	public String getUserPassword() {
		return userPassword;
	}


	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}


	public String getGroupName() {
		return groupName;
	}


	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getAlternateEmail() {
		return alternateEmail;
	}


	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}


	public String getTimeZone() {
		return timeZone;
	}


	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getLocale() {
		return locale;
	}


	public void setLocale(String locale) {
		this.locale = locale;
	}


	private String newUserName;
	private String userPassword;
	private String groupName;
	private String displayName;
	private String firstName;
	private String lastName;
	private String email;
	private String alternateEmail;
	private String timeZone;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String orgName;
	private String street;
	private String phoneNumber;
	private String locale;
	private String enterpriseName;
	private Boolean notify;
	
	//dimdimUserName:"admin",userPassword:"","object":"account","dimdimEnterpriseName":"vcs1",
	//"dimdimGroupName":"all"
	@Override
	public JSONObject toJSON() {
		JSONObject request = new JSONObject();
		setParameter(request,"dimdimUserName", this.getNewUserName());
		setParameter(request,"userPassword", this.getPassword());
		setParameter(request,"displayName", this.getDisplayName());
		setParameter(request,"dimdimFirstName", this.getFirstName());
		setParameter(request,"dimdimLastName", this.getLastName());
		setParameter(request,"dimdimEmail", this.getEmail());
		setParameter(request,"dimdimAlternateEmail", this.getAlternateEmail());
		setParameter(request,"dimdimTimezone", this.getTimeZone());
		setParameter(request,"dimdimCity", this.getCity());
		setParameter(request,"dimdimState", this.getState());
		setParameter(request,"dimdimZipCode", this.getZipCode());
		setParameter(request,"dimdimCountry", this.getCountry());
		setParameter(request,"dimdimOrgName", this.getOrgName());
		setParameter(request,"dimdimStreet", this.getStreet());
		setParameter(request,"dimdimPhoneNumber", this.getPhoneNumber());
		setParameter(request,"dimdimLocale", this.getLocale());
		setParameter(request,"notify", this.getNotify().toString());
		//setParameter(request,"sessionKey", this.getAuthToken());
		setParameter(request,"object", "account");
		setParameter(request, "dimdimEnterpriseName",enterpriseName);
		setParameter(request,"dimdimGroupName", this.getGroupName()==null?super.getDefaultGroupName():this.getGroupName());
		JSONObject obj = new JSONObject();
		obj.put("request", request);
		return obj;
	}
}
