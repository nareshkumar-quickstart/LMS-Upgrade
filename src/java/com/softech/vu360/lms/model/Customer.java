package com.softech.vu360.lms.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * @author muhammad.saleem
 * @updated haider.ali
 *
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer extends Owner implements SearchableKey {

	
	private static final long serialVersionUID = -6595980022652321391L;
	public static final String B2B = "b2b";
    public static final String B2C = "b2c";
    public static final String CODE_PREFIX = "VUCUS";
    public static final String CUSTOMER= "CUSTOMER";
    
    @Id
    @javax.persistence.TableGenerator(name = "CUSTOMER_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "CUSTOMER", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "CUSTOMER_ID")
    private Long id;
    
    @Column(name="NAME")
    private String name;
    
    @Column(name="FIRSTNAME")
    private String firstName;
    
    @Column(name="LASTNAME")
    private String lastName;
    
    @Column(name="PHONENUMBER")
    private String phoneNumber;
    
    @Column(name="EXTENSION")
    private String phoneExtn;
    
    @Column(name="EMAIL")
    private String email;
    
    @Column(name="CUSTOMERTYPE")
    private String customerType;
    
    @Column(name="CUSTOMERCODE")
    private String customerCode;
    
    @Column(name="ORDERID")
    private String orderId;
    
    @Column(name="WEBSITEURL")
    private String websiteUrl;
    
    @Column(name="CUSTOMER_GUID")
    private String customerGUID;
    
    @Column(name="ACTIVETF")
    private Boolean active = Boolean.TRUE;
    
    @Column(name="BRANDNAME")
    private String brandName;
    
    @Column(name="AICCENABLEDTF")
    private Boolean aiccInterfaceEnabled = Boolean.FALSE;
    
    @Column(name="LmsAPI_Enabled_TF")
    private Boolean lmsApiEnabledTF= Boolean.FALSE;
    
    @Column(name="IS_DEFAULT")
    private Boolean isDefault;
    
    @Column(name="LMSPROVIDER")
    private Integer lmsProvider = LearningSession.LMS_LS360;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="BRAND_ID")
    private Brand brand;
    
    @OneToOne
    @JoinColumn(name="DISTRIBUTOR_ID")
    private Distributor distributor; 
    
	@OneToOne(fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="BILLINGADDRESS_ID")
    private Address billingAddress ;
    
    @OneToOne (fetch=FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="SHIPPINGADDRESS_ID")
    private Address shippingAddress ;
    
    @OneToOne(fetch=FetchType.LAZY, mappedBy = "customer",  cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private CustomerPreferences customerPreferences ;
    
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CONTENTOWNER_ID")
    private ContentOwner contentOwner ;
   
    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="CUSTOMER_CUSTOMFIELD", joinColumns = @JoinColumn(name="CUSTOMER_ID",referencedColumnName="ID"),inverseJoinColumns = @JoinColumn(name="CUSTOMFIELD_ID",referencedColumnName="ID"))
    private List<CustomField> customFields;
   
   
    @OneToMany(mappedBy = "customer" ,fetch=FetchType.LAZY)
    private List<CustomerEntitlement> customerEntitlement;
    
	public List<CustomerEntitlement> getCustomerEntitlement() {
		return customerEntitlement;
	}

	public void setCustomerEntitlement(List<CustomerEntitlement> customerEntitlement) {
		this.customerEntitlement = customerEntitlement;
	}
    
    public Customer() {}

    public Address getBillingAddress() {
        return this.billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getShippingAddress() {
        return this.shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneExtn() {
        return phoneExtn;
    }

    public void setPhoneExtn(String phoneExtn) {
        this.phoneExtn = phoneExtn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getKey() {
        return id.toString();
    }

   

    public CustomerPreferences getCustomerPreferences() {
        return this.customerPreferences;
    }

    public void setCustomerPreferences(CustomerPreferences customerPreferences) {
        this.customerPreferences = customerPreferences;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getOwnerType() {
        return CUSTOMER;
    }

    public String getCustomerCode() {
        return this.customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Customer clone() {
        Customer clone = new Customer();
        clone.setId(this.getId());
        clone.setActive(this.isActive());
        clone.setBillingAddress(this.getBillingAddress());
        clone.setBrand(this.getBrand());
        clone.setCustomerCode(this.getCustomerCode());
        clone.setCustomerGUID(this.getCustomerGUID());
        clone.setCustomerPreferences(this.getCustomerPreferences());
        clone.setCustomerType(this.getCustomerType());
        clone.setDistributor(this.getDistributor());
        clone.setEmail(this.getEmail());
        clone.setFirstName(this.getFirstName());
        clone.setLastName(this.getLastName());
        clone.setName(this.getName());
        clone.setOrderId(this.getOrderId());
        clone.setPhoneExtn(this.getPhoneExtn());
        clone.setPhoneNumber(this.getPhoneNumber());
        clone.setShippingAddress(this.getShippingAddress());
        clone.setWebsiteUrl(this.getWebsiteUrl());
        clone.setDefault(this.isDefault());
        return clone;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCustomerGUID() {
        return customerGUID;
    }

    public void setCustomerGUID(String customerGUID) {
        this.customerGUID = customerGUID;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    public Boolean getAiccInterfaceEnabled() {
    	if(aiccInterfaceEnabled==null){
    		aiccInterfaceEnabled=Boolean.FALSE;
    	}
        return aiccInterfaceEnabled;
    }

    public void setAiccInterfaceEnabled(Boolean aiccInterfaceEnabled) {
    	if(aiccInterfaceEnabled==null){
    		this.aiccInterfaceEnabled=Boolean.FALSE;
    	}else{
    		this.aiccInterfaceEnabled = aiccInterfaceEnabled;
    	}
	}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Name: ").append(getName());
        return sb.toString(); 
    }

	public Integer getLmsProvider() {
		return lmsProvider;
	}

	public void setLmsProvider(Integer lmsProvider) {
		this.lmsProvider = lmsProvider;
	}

	public ContentOwner getContentOwner() {
		return this.contentOwner;
	}

	public void setContentOwner(ContentOwner contentOwner) {
		this.contentOwner = contentOwner;
	}

	public Boolean isDistributorRepresentative() {
		
		if (distributor!=null &&
				(this.id!=null && distributor.getMyCustomer()!= null && distributor.getMyCustomer().getId()!=null && 
						distributor.getMyCustomer().getId().longValue() == this.id.longValue()))
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}	

	public Boolean isSelfAuthor(){
		if(contentOwner!=null)
			return Boolean.TRUE;
		else 
			return Boolean.FALSE;
	}

	public  Boolean getLmsApiEnabledTF() {
		if(lmsApiEnabledTF==null){
			lmsApiEnabledTF=Boolean.FALSE;
		}
		return lmsApiEnabledTF;
	}

	public void setLmsApiEnabledTF(Boolean lmsApiEnabledTF) {
		if(lmsApiEnabledTF==null){
			this.lmsApiEnabledTF=Boolean.FALSE;
		}else{
			this.lmsApiEnabledTF = lmsApiEnabledTF;
		}
	}

	public Boolean isDefault() {
		return isDefault;
	}

	public void setDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
    public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}
	
	public void initializeOwnerParams(){//This method will be used to set values for Owner while saving survey
		super.setId(this.id);
		super.setOwnerType(getOwnerType());
	}

}
