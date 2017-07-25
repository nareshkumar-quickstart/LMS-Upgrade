package com.softech.vu360.lms.web.controller.model;

import java.util.ArrayList;
import java.util.List;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;
import com.softech.vu360.util.Brander;

/**
 * @author Dyutiman
 *
 */
public class EditCustomerForm  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	
	public EditCustomerForm() {
	
	}
	
	private Customer customer = null;
	private long id = 0;
	private String name = null;
	private String firstName = null;
	private String lastName = null;
	private String websiteUrl = null;
	private String emailAddress = null;
	private String phone = null;
	private String extension = null;
	private String type = null;
	private boolean status = false;
	private String billingAddress1 = null;
	private String billingAddress2 = null;
	private String billingCity = null;
	private String billingState = null;
	private String billingZip = null;
	private String billingCountry = null;
	private String shippingAddress1 = null;
	private String shippingAddress2 = null;
	private String shippingCity = null;
	private String shippingState = null;
	private String shippingZip = null;
	private String shippingCountry = null;
	private boolean audio = false;
    private boolean blanksearch = false;
	private boolean audioLocked = false;
	private boolean vedio = false;
	private boolean vedioLocked = false;
	private boolean captioning = false;
	private boolean captioningLocked = false;
	private boolean bandwidth = false;
	private boolean bandwidthLocked = false;
	private int volume = 0;
	private boolean volumeLocked = false;
	private boolean regEmail = false;
	private boolean regEmailLocked = false;
	private boolean enrollEmail = false;
	private boolean enrollEmailLocked = false;
	private boolean multiLearner = false;
	private List<EditDistributor> distributors = new ArrayList <EditDistributor>();
	private boolean selfAuthor = false;
	private boolean distributorRepresentative = false;
	private boolean courseCompCertificateEmails = true;
	private boolean courseCompCertificateEmailsLocked = false;
	private Brander brander = null ;
	private String brandName = null;
	private long brandId;
	private boolean aiccInterfaceEnabled = Boolean.FALSE;

	String eventSource = null ;
	
	
	public void reset(){
		
		customer = null;
		name = null;
		firstName = null;
		lastName = null;
		websiteUrl = null;
		emailAddress = null;
		phone = null;
		extension = null;
		type = null;
		status = false;
		billingAddress1 = null;
		billingAddress2 = null;
		billingCity = null;
		billingState = null;
		billingZip = null;
		billingCountry = null;
		shippingAddress1 = null;
		shippingAddress2 = null;
		shippingCity = null;
		shippingState = null;
		shippingZip = null;
		shippingCountry = null;
		audio = false;
        blanksearch = false;
		audioLocked  = false;
		vedio = false;
		vedioLocked = false;
		captioning = false;
		captioningLocked = false;
		bandwidth = false;
		bandwidthLocked = false;
		//private String volume = null;
		volumeLocked = false;
		regEmail = false;
		regEmailLocked = false;
		enrollEmail = false;
		enrollEmailLocked = false;
		distributors = null;
	}
	
	/*
	 * getters and setters
	 */
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getWebsiteUrl() {
		return websiteUrl;
	}
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public String getBillingZip() {
		return billingZip;
	}

	public void setBillingZip(String billingZip) {
		this.billingZip = billingZip;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getShippingAddress1() {
		return shippingAddress1;
	}

	public void setShippingAddress1(String shippingAddress1) {
		this.shippingAddress1 = shippingAddress1;
	}

	public String getShippingAddress2() {
		return shippingAddress2;
	}

	public void setShippingAddress2(String shippingAddress2) {
		this.shippingAddress2 = shippingAddress2;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}

	public String getShippingState() {
		return shippingState;
	}

	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}

	public String getShippingZip() {
		return shippingZip;
	}

	public void setShippingZip(String shippingZip) {
		this.shippingZip = shippingZip;
	}

	public String getShippingCountry() {
		return shippingCountry;
	}

	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}

	public boolean getAudio() {
		return audio;
	}

	public void setAudio(boolean audio) {
		this.audio = audio;
	}

    public boolean getBlanksearch() {
        return blanksearch;
    }

    public void setBlanksearch(boolean blanksearch) {
        this.blanksearch = blanksearch;
    }

	public boolean getAudioLocked() {
		return audioLocked;
	}

	public void setAudioLocked(boolean audioLocked) {
		this.audioLocked = audioLocked;
	}

	public boolean getVedio() {
		return vedio;
	}

	public void setVedio(boolean vedio) {
		this.vedio = vedio;
	}

	public boolean getVedioLocked() {
		return vedioLocked;
	}

	public void setVedioLocked(boolean vedioLocked) {
		this.vedioLocked = vedioLocked;
	}

	public boolean getCaptioning() {
		return captioning;
	}

	public void setCaptioning(boolean captioning) {
		this.captioning = captioning;
	}

	public boolean getCaptioningLocked() {
		return captioningLocked;
	}

	public void setCaptioningLocked(boolean captioningLocked) {
		this.captioningLocked = captioningLocked;
	}

	public boolean getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(boolean bandwidth) {
		this.bandwidth = bandwidth;
	}

	public boolean getBandwidthLocked() {
		return bandwidthLocked;
	}

	public void setBandwidthLocked(boolean bandwidthLocked) {
		this.bandwidthLocked = bandwidthLocked;
	}

	public boolean getVolumeLocked() {
		return volumeLocked;
	}

	public void setVolumeLocked(boolean volumeLocked) {
		this.volumeLocked = volumeLocked;
	}

	public boolean getRegEmail() {
		return regEmail;
	}

	public void setRegEmail(boolean regEmail) {
		this.regEmail = regEmail;
	}

	public boolean getRegEmailLocked() {
		return regEmailLocked;
	}

	public void setRegEmailLocked(boolean regEmailLocked) {
		this.regEmailLocked = regEmailLocked;
	}

	public boolean getEnrollEmail() {
		return enrollEmail;
	}

	public void setEnrollEmail(boolean enrollEmail) {
		this.enrollEmail = enrollEmail;
	}

	public boolean getEnrollEmailLocked() {
		return enrollEmailLocked;
	}

	public void setEnrollEmailLocked(boolean enrollEmailLocked) {
		this.enrollEmailLocked = enrollEmailLocked;
	}

	public List<EditDistributor> getDistributors() {
		return distributors;
	}

	public void setDistributors(List<EditDistributor> distributors) {
		this.distributors = distributors;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
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
		return brandName;
	}

	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the brand
	 */
	public Brander getBrander() {
		return brander;
	}

	/**
	 * @param brand the brand to set
	 */
	public void setBrander(Brander brand) {
		this.brander = brand;
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

	public boolean isMultiLearner() {
		return multiLearner;
	}

	public void setMultiLearner(boolean multiLearner) {
		this.multiLearner = multiLearner;
	}

	public boolean isSelfAuthor() {
		return selfAuthor;
	}

	public void setSelfAuthor(boolean selfAuthor) {
		this.selfAuthor = selfAuthor;
	}

	public boolean isDistributorRepresentative() {
		return distributorRepresentative;
	}

	public void setDistributorRepresentative(boolean distributorRepresentative) {
		this.distributorRepresentative = distributorRepresentative;
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
	 * @param courseCompCertificateEmailsLocked the courseCompCertificateEmailsLocked to set
	 * Sending emails may be locked for some reason despite being allowed
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

	public boolean isAiccInterfaceEnabled() {
		return aiccInterfaceEnabled;
	}

	public void setAiccInterfaceEnabled(boolean aiccInterfaceEnabled) {
		this.aiccInterfaceEnabled = aiccInterfaceEnabled;
	}
	
	
}