package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;

public class AddCustomerForm  implements ILMSBaseInterface{
	
	private static final long serialVersionUID = 1L;
	
	public AddCustomerForm() {

	}
	
	private String customerName = null;
	private String firstName = null;
	private String lastName = null;
	private String brandName = null;
	private String customerType = null;
	private String wesiteURL = null;
	private String emailAdd = null;
	private String phone = null;
	private String ext = null;
	private boolean lmsApiEnabledTF=false;
	
	private String address1 = null;
	private String address1a = null;
	private String city1 = null;
	private String state1 = null;
	private String zip1 = null;
	private String country1 = null;
	
	private String address2 = null;
	private String address2a = null;
	private String city2 = null;
	private String state2 = null;
	private String zip2 = null;
	private String country2 = null;

	private String countryLabel1 = null;
	private String countryLabel2 = null;
	
	private boolean status = true;
	
	private boolean audio = false;
	private boolean audioLocked = false;
	private boolean volumeLocked = false;
	private boolean captioning = false;
	private boolean captioningLocked = false;
	private boolean bandwidth = true;
	private boolean bandwidthLocked = true;
	private boolean video = false;
	private boolean videoLocked = false;
	private boolean registrationEmails = true;
	private boolean registrationEmailsLocked = false;
	private boolean enrollmentEmails = true;
	private boolean enrollmentEmailsLocked = false;
	private int volume = 0;
	
	private String distName = null;
	private List<AddDistributors> distributors = new ArrayList<AddDistributors>();
	
	private String loginEmailID = null;
	private String password = null;
	private String confirmPassword = null;
	private String matchPassword = null;
	private boolean locked = false;
	private boolean expired = false;
	private String expirationDate = null;
	private boolean disabled = false;
	private boolean report = true;
	private boolean changePassword = false;

	private String eventSource = null;
	private Brander brander = null ;
	private boolean selfAuthor = false;
	
	private boolean courseCompCertificateEmails = true;
	private boolean courseCompCertificateEmailsLocked = false;
	
	private long brandId;
	private ArrayList<Brand>brandsList;
	
	private boolean isDefault;
	private boolean aiccInterfaceEnabled = Boolean.FALSE;
		
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	public String getWesiteURL() {
		return wesiteURL;
	}
	public void setWesiteURL(String wesiteURL) {
		this.wesiteURL = wesiteURL;
	}
	public String getEmailAdd() {
		return emailAdd;
	}
	public void setEmailAdd(String emailAdd) {
		this.emailAdd = emailAdd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress1a() {
		return address1a;
	}
	public void setAddress1a(String address1a) {
		this.address1a = address1a;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress2a() {
		return address2a;
	}
	public void setAddress2a(String address2a) {
		this.address2a = address2a;
	}
	public String getCity1() {
		return city1;
	}
	public void setCity1(String city1) {
		this.city1 = city1;
	}
	public String getState1() {
		return state1;
	}
	public void setState1(String state1) {
		this.state1 = state1;
	}
	public String getZip1() {
		return zip1;
	}
	public void setZip1(String zip1) {
		this.zip1 = zip1;
	}
	public String getCountry1() {
		return country1;
	}
	public void setCountry1(String country1) {
		this.country1 = country1;
	}
	public String getCity2() {
		return city2;
	}
	public void setCity2(String city2) {
		this.city2 = city2;
	}
	public String getState2() {
		return state2;
	}
	public void setState2(String state2) {
		this.state2 = state2;
	}
	public String getZip2() {
		return zip2;
	}
	public void setZip2(String zip2) {
		this.zip2 = zip2;
	}
	public String getCountry2() {
		return country2;
	}
	public void setCountry2(String country2) {
		this.country2 = country2;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public boolean isAudio() {
		return audio;
	}
	public void setAudio(boolean audio) {
		this.audio = audio;
	}
	public boolean isCaptioning() {
		return captioning;
	}
	public void setCaptioning(boolean captioning) {
		this.captioning = captioning;
	}
	public boolean isBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(boolean bandwidth) {
		this.bandwidth = bandwidth;
	}
	public boolean isVideo() {
		return video;
	}
	public void setVideo(boolean video) {
		this.video = video;
	}
	public String getDistName() {
		return distName;
	}
	public void setDistName(String distName) {
		this.distName = distName;
	}
	public List<AddDistributors> getDistributors() {
		return distributors;
	}
	public void setDistributors(List<AddDistributors> distributors) {
		this.distributors = distributors;
	}
	public boolean isAudioLocked() {
		return audioLocked;
	}
	public void setAudioLocked(boolean audioLocked) {
		this.audioLocked = audioLocked;
	}
	public boolean isVolumeLocked() {
		return volumeLocked;
	}
	public void setVolumeLocked(boolean volumeLocked) {
		this.volumeLocked = volumeLocked;
	}
	public boolean isCaptioningLocked() {
		return captioningLocked;
	}
	public void setCaptioningLocked(boolean captioningLocked) {
		this.captioningLocked = captioningLocked;
	}
	public boolean isBandwidthLocked() {
		return bandwidthLocked;
	}
	public void setBandwidthLocked(boolean bandwidthLocked) {
		this.bandwidthLocked = bandwidthLocked;
	}
	public boolean isVideoLocked() {
		return videoLocked;
	}
	public void setVideoLocked(boolean videoLocked) {
		this.videoLocked = videoLocked;
	}
	public boolean isRegistrationEmails() {
		return registrationEmails;
	}
	public void setRegistrationEmails(boolean registrationEmails) {
		this.registrationEmails = registrationEmails;
	}
	public boolean isEnrollmentEmails() {
		return enrollmentEmails;
	}
	public void setEnrollmentEmails(boolean enrollmentEmails) {
		this.enrollmentEmails = enrollmentEmails;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	/**
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}
	/**
	 * @return the registrationEmailsLocked
	 */
	public boolean isRegistrationEmailsLocked() {
		return registrationEmailsLocked;
	}
	/**
	 * @param registrationEmailsLocked the registrationEmailsLocked to set
	 */
	public void setRegistrationEmailsLocked(boolean registrationEmailsLocked) {
		this.registrationEmailsLocked = registrationEmailsLocked;
	}
	/**
	 * @return the enrollmentEmailsLocked
	 */
	public boolean isEnrollmentEmailsLocked() {
		return enrollmentEmailsLocked;
	}
	/**
	 * @param enrollmentEmailsLocked the enrollmentEmailsLocked to set
	 */
	public void setEnrollmentEmailsLocked(boolean enrollmentEmailsLocked) {
		this.enrollmentEmailsLocked = enrollmentEmailsLocked;
	}
	public String getLoginEmailID() {
		return loginEmailID;
	}
	public void setLoginEmailID(String loginEmailID) {
		this.loginEmailID = loginEmailID;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public boolean isExpired() {
		return expired;
	}
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public boolean isReport() {
		return report;
	}
	public void setReport(boolean report) {
		this.report = report;
	}
	public boolean isChangePassword() {
		return changePassword;
	}
	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}
	public String getMatchPassword() {
		return matchPassword;
	}
	public void setMatchPassword(String matchPassword) {
		this.matchPassword = matchPassword;
	}
	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}
	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	/**
	 * @return the eventSource
	 */
	public String getEventSource() {
		return eventSource;
	}
	/**
	 * @param eventSource the eventSource to set
	 */
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
	/**
	 * @return the brander
	 */
	public Brander getBrander() {
		return brander;
	}
	/**
	 * @param brander the brander to set
	 */
	public void setBrander(Brander brander) {
		this.brander = brander;
	}
	public void setCountryLabel1(String countryLabel1) {
		this.countryLabel1 = countryLabel1;
	}
	public String getCountryLabel1() {
		return countryLabel1;
	}
	public void setCountryLabel2(String countryLabel2) {
		this.countryLabel2 = countryLabel2;
	}
	public String getCountryLabel2() {
		return countryLabel2;
	}
	
	public boolean isSelfAuthor() {
		return selfAuthor;
	}
	public void setSelfAuthor(boolean selfAuthor) {
		this.selfAuthor = selfAuthor;
	}
	/**
	 * @return the courseCompCertificateEmails
	 */
	public boolean isCourseCompCertificateEmails() {
		return courseCompCertificateEmails;
	}
	/**
	 * @param courseCompCertificateEmails the courseCompCertificateEmails to set
	 */
	public void setCourseCompCertificateEmails(boolean courseCompCertificateEmails) {
		this.courseCompCertificateEmails = courseCompCertificateEmails;
	}
	/**
	 * @return the courseCompCertificateEmailsLocked
	 */
	public boolean isCourseCompCertificateEmailsLocked() {
		return courseCompCertificateEmailsLocked;
	}
	/**
	 * @param courseCompCertificateEmailsLocked the courseCompCertificateEmailsLocked to set
	 */
	public void setCourseCompCertificateEmailsLocked(
			boolean courseCompCertificateEmailsLocked) {
		this.courseCompCertificateEmailsLocked = courseCompCertificateEmailsLocked;
	}
	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	public ArrayList<Brand> getBrandsList() {
		return brandsList;
	}
	public void setBrandsList(ArrayList<Brand> brandsList) {
		this.brandsList = brandsList;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public boolean isLmsApiEnabledTF() {
		return lmsApiEnabledTF;
	}
	public void setLmsApiEnabledTF(boolean lmsApiEnabledTF) {
		this.lmsApiEnabledTF = lmsApiEnabledTF;
	}
	public boolean isAiccInterfaceEnabled() {
		return aiccInterfaceEnabled;
	}
	public void setAiccInterfaceEnabled(boolean aiccInterfaceEnabled) {
		this.aiccInterfaceEnabled = aiccInterfaceEnabled;
	}
	
}