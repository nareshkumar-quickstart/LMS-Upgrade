package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;


/**
 * 
 * @author Saptarshi
 *
 */
public class AddDistributorForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public AddDistributorForm() {
	}

	// For page-1 (addNewDistributorProfile.vm)
	private String distributorName = null;
	private String distributorCode = null;
	private String firstName = null;
	private String lastName = null;
	private String wesiteURL = null;
	private String emailAdd = null;
	private String phone = null;
	private String ext = null;
	private String address1Line1 = null;
	private String address1Line2 = null;
	private String city1 = null;
	private String state1 = null;
	private String zip1 = null;
	private String country1 = null;
	private String address2Line1 = null;
	private String address2Line2 = null;
	private String city2 = null;
	private String state2 = null;
	private String zip2 = null;
	private String country2= null;

	private String brandName= null;
	private Brander brander = null;
	private String eventSource = null;
	private String type = null;

	private boolean accountStatus = true;
	private boolean selfReporting = true;
	private boolean markedPrivate=false;
	
	// For page-2 (addNewDistributorPreferences.vm)
	private boolean audio = false;
	private boolean audioLocked = false;
	private int volume = 0;
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
	private boolean courseCompCertificateEmails = true;
	private boolean courseCompCertificateEmailsLocked = false;
	private boolean selfAuthor = false;
	private String loginEmailID = null;
	private String password = null;
	private String confirmPassword = null;
	private boolean locked = false;
	private boolean expired = false;
	private String expirationDate = null;
	private boolean disabled = false;
	private boolean report = true;
	private boolean changePassword = false;
	private long brandId;
	private ArrayList<Brand> brandsList; 
	private boolean isCorporateAuthorVar;
	private Boolean lmsApiEnabledTF = new Boolean(false);
	// For page-3 (addNewDistributorGroups.vm)

	private String distName = null;
	private List<AddDistributorGroups> distributors = new ArrayList<AddDistributorGroups>();
	
	/**
	 * Whether call loggin is enabled or not
	 * @since 4.13 {LMS-8108}
	 * @author sm.humayun
	 */
	private Boolean callLoggingEnabled;
	
	/**
	 * Return <code>callLoggingEnabled</code>
	 * @return <code>callLoggingEnabled</code>
	 * @since 4.13 {LMS-8108}
	 * @author sm.humayun
	 */
	public Boolean getCallLoggingEnabled() {
		return callLoggingEnabled;
	}
	
	/**
	 * Set <code>callLoggingEnabled</code>
	 * @param callLoggingEnabled
	 * @since 4.13 {LMS-8108}
	 * @author sm.humayun
	 */
	public void setCallLoggingEnabled(Boolean callLoggingEnabled) {
		this.callLoggingEnabled = callLoggingEnabled;
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
	/**
	 * @return the distributorName
	 */
	public String getDistributorName() {
		return distributorName;
	}
	/**
	 * @param distributorName the distributorName to set
	 */
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the wesiteURL
	 */
	public String getWesiteURL() {
		return wesiteURL;
	}
	/**
	 * @param wesiteURL the wesiteURL to set
	 */
	public void setWesiteURL(String wesiteURL) {
		this.wesiteURL = wesiteURL;
	}
	/**
	 * @return the emailAdd
	 */
	public String getEmailAdd() {
		return emailAdd;
	}
	/**
	 * @param emailAdd the emailAdd to set
	 */
	public void setEmailAdd(String emailAdd) {
		this.emailAdd = emailAdd;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the ext
	 */
	public String getExt() {
		return ext;
	}
	/**
	 * @param ext the ext to set
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}
	/**
	 * @return the address1Line1
	 */
	public String getAddress1Line1() {
		return address1Line1;
	}
	/**
	 * @param address1Line1 the address1Line1 to set
	 */
	public void setAddress1Line1(String address1Line1) {
		this.address1Line1 = address1Line1;
	}
	/**
	 * @return the address1Line2
	 */
	public String getAddress1Line2() {
		return address1Line2;
	}
	/**
	 * @param address1Line2 the address1Line2 to set
	 */
	public void setAddress1Line2(String address1Line2) {
		this.address1Line2 = address1Line2;
	}
	/**
	 * @return the city1
	 */
	public String getCity1() {
		return city1;
	}
	/**
	 * @param city1 the city1 to set
	 */
	public void setCity1(String city1) {
		this.city1 = city1;
	}
	/**
	 * @return the state1
	 */
	public String getState1() {
		return state1;
	}
	/**
	 * @param state1 the state1 to set
	 */
	public void setState1(String state1) {
		this.state1 = state1;
	}
	/**
	 * @return the zip1
	 */
	public String getZip1() {
		return zip1;
	}
	/**
	 * @param zip1 the zip1 to set
	 */
	public void setZip1(String zip1) {
		this.zip1 = zip1;
	}
	/**
	 * @return the country1
	 */
	public String getCountry1() {
		return country1;
	}
	/**
	 * @param country1 the country1 to set
	 */
	public void setCountry1(String country1) {
		this.country1 = country1;
	}
	/**
	 * @return the address2Line1
	 */
	public String getAddress2Line1() {
		return address2Line1;
	}
	/**
	 * @param address2Line1 the address2Line1 to set
	 */
	public void setAddress2Line1(String address2Line1) {
		this.address2Line1 = address2Line1;
	}
	/**
	 * @return the address2Line2
	 */
	public String getAddress2Line2() {
		return address2Line2;
	}
	/**
	 * @param address2Line2 the address2Line2 to set
	 */
	public void setAddress2Line2(String address2Line2) {
		this.address2Line2 = address2Line2;
	}
	/**
	 * @return the city2
	 */
	public String getCity2() {
		return city2;
	}
	/**
	 * @param city2 the city2 to set
	 */
	public void setCity2(String city2) {
		this.city2 = city2;
	}
	/**
	 * @return the state2
	 */
	public String getState2() {
		return state2;
	}
	/**
	 * @param state2 the state2 to set
	 */
	public void setState2(String state2) {
		this.state2 = state2;
	}
	/**
	 * @return the zip2
	 */
	public String getZip2() {
		return zip2;
	}
	/**
	 * @param zip2 the zip2 to set
	 */
	public void setZip2(String zip2) {
		this.zip2 = zip2;
	}
	/**
	 * @return the country2
	 */
	public String getCountry2() {
		return country2;
	}
	/**
	 * @param country2 the country2 to set
	 */
	public void setCountry2(String country2) {
		this.country2 = country2;
	}
	/**
	 * @return the accountStatus
	 */
	public boolean isAccountStatus() {
		return accountStatus;
	}
	/**
	 * @param accountStatus the accountStatus to set
	 */
	public void setAccountStatus(boolean accountStatus) {
		this.accountStatus = accountStatus;
	}
	/**
	 * @return the audio
	 */
	public boolean isAudio() {
		return audio;
	}
	/**
	 * @param audio the audio to set
	 */
	public void setAudio(boolean audio) {
		this.audio = audio;
	}
	/**
	 * @return the audioLocked
	 */
	public boolean isAudioLocked() {
		return audioLocked;
	}
	/**
	 * @param audioLocked the audioLocked to set
	 */
	public void setAudioLocked(boolean audioLocked) {
		this.audioLocked = audioLocked;
	}
	/**
	 * @return the volumeLocked
	 */
	public boolean isVolumeLocked() {
		return volumeLocked;
	}
	/**
	 * @param volumeLocked the volumeLocked to set
	 */
	public void setVolumeLocked(boolean volumeLocked) {
		this.volumeLocked = volumeLocked;
	}
	/**
	 * @return the captioning
	 */
	public boolean isCaptioning() {
		return captioning;
	}
	/**
	 * @param captioning the captioning to set
	 */
	public void setCaptioning(boolean captioning) {
		this.captioning = captioning;
	}
	/**
	 * @return the captioningLocked
	 */
	public boolean isCaptioningLocked() {
		return captioningLocked;
	}
	/**
	 * @param captioningLocked the captioningLocked to set
	 */
	public void setCaptioningLocked(boolean captioningLocked) {
		this.captioningLocked = captioningLocked;
	}
	/**
	 * @return the bandwidth
	 */
	public boolean isBandwidth() {
		return bandwidth;
	}
	/**
	 * @param bandwidth the bandwidth to set
	 */
	public void setBandwidth(boolean bandwidth) {
		this.bandwidth = bandwidth;
	}
	/**
	 * @return the bandwidthLocked
	 */
	public boolean isBandwidthLocked() {
		return bandwidthLocked;
	}
	/**
	 * @param bandwidthLocked the bandwidthLocked to set
	 */
	public void setBandwidthLocked(boolean bandwidthLocked) {
		this.bandwidthLocked = bandwidthLocked;
	}
	/**
	 * @return the video
	 */
	public boolean isVideo() {
		return video;
	}
	/**
	 * @param video the video to set
	 */
	public void setVideo(boolean video) {
		this.video = video;
	}
	/**
	 * @return the videoLocked
	 */
	public boolean isVideoLocked() {
		return videoLocked;
	}
	/**
	 * @param videoLocked the videoLocked to set
	 */
	public void setVideoLocked(boolean videoLocked) {
		this.videoLocked = videoLocked;
	}
	/**
	 * @return the registrationEmails
	 */
	public boolean isRegistrationEmails() {
		return registrationEmails;
	}
	/**
	 * @param registrationEmails the registrationEmails to set
	 */
	public void setRegistrationEmails(boolean registrationEmails) {
		this.registrationEmails = registrationEmails;
	}
	/**
	 * @return the enrollmentEmails
	 */
	public boolean isEnrollmentEmails() {
		return enrollmentEmails;
	}
	/**
	 * @param enrollmentEmails the enrollmentEmails to set
	 */
	public void setEnrollmentEmails(boolean enrollmentEmails) {
		this.enrollmentEmails = enrollmentEmails;
	}
	/**
	 * @return the distName
	 */
	public String getDistName() {
		return distName;
	}
	/**
	 * @param distName the distName to set
	 */
	public void setDistName(String distName) {
		this.distName = distName;
	}
	/**
	 * @return the distributors
	 */
	public List<AddDistributorGroups> getDistributors() {
		return distributors;
	}
	/**
	 * @param distributors the distributors to set
	 */
	public void setDistributors(List<AddDistributorGroups> distributors) {
		this.distributors = distributors;
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
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName ;
	}
	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public boolean isSelfReporting() {
		return selfReporting;
	}
	public void setSelfReporting(boolean selfReporting) {
		this.selfReporting = selfReporting;
	}
	public boolean isMarkedPrivate() {
		return markedPrivate;
	}
	public void setMarkedPrivate(boolean markedPrivate) {
		this.markedPrivate = markedPrivate;
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
	

	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
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
	public boolean isSelfAuthor() {
		return selfAuthor;
	}
	public void setSelfAuthor(boolean selfAuthor) {
		this.selfAuthor = selfAuthor;
	}
	/**
	 * @param courseCompCertificateEmails the courseCompCertificateEmails to set
	 */
	public void setCourseCompCertificateEmails(boolean courseCompCertificateEmails) {
		this.courseCompCertificateEmails = courseCompCertificateEmails;
	}
	/**
	 * @return the courseCompCertificateEmails
	 */
	public boolean isCourseCompCertificateEmails() {
		return courseCompCertificateEmails;
	}
	/**
	 * @param courseCompCertificateEmailsLocked the courseCompCertificateEmailsLocked to set
	 */
	public void setCourseCompCertificateEmailsLocked(
			boolean courseCompCertificateEmailsLocked) {
		this.courseCompCertificateEmailsLocked = courseCompCertificateEmailsLocked;
	}
	/**
	 * @return the courseCompCertificateEmailsLocked
	 */
	public boolean isCourseCompCertificateEmailsLocked() {
		return courseCompCertificateEmailsLocked;
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
	/**
	 * @return the distributorCode
	 */
	public String getDistributorCode() {
		return distributorCode;
	}
	/**
	 * @param distributorCode the distributorCode to set
	 */
	public void setDistributorCode(String distributorCode) {
		this.distributorCode = distributorCode;
	} 

	public boolean getIsCorporateAuthorVar() {
		return isCorporateAuthorVar;
	}

	public Boolean getLmsApiEnabledTF() {
		return lmsApiEnabledTF;
	}

	public void setLmsApiEnabledTF(Boolean lmsApiEnabledTF) {
		this.lmsApiEnabledTF = lmsApiEnabledTF;
	}

	public void setIsCorporateAuthorVar(boolean isCorporateAuthorVar) {
		this.isCorporateAuthorVar = isCorporateAuthorVar;
	}
}
