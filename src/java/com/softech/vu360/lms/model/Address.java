package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;

/**
 * 
 * @author muhammad.saleem
 * modified by marium.saud
 *
 */
@Entity
@Table(name = "ADDRESS")
public class Address implements SearchableKey {

	private static final long serialVersionUID = 2627011619016900781L;

	@Id
    @javax.persistence.TableGenerator(name = "ADDRESS_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ADDRESS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ADDRESS_ID")
	private Long id;
    
	@Nationalized
    @Column(name="STREETADDRESS")
	private String streetAddress = null;
    
	@Nationalized
    @Column(name="STREETADDRESS2")
	private String streetAddress2 = null;
    
	@Nationalized
    @Column(name="STREETADDRESS3")
	private String streetAddress3 = null;
    
    @Column(name="CITY")
	private String city = null;
    
    @Column(name="STATE")
	private String state = null;
    
    @Column(name="ZIPCODE")
	private String zipCode = null;
    
    @Column(name="COUNTRY")
	private String country = null;
    
    @Column(name="PROVINCE")
	private String province = null;

	/**
	 * @return the streetAddress
	 */
	public String getStreetAddress() {
		return streetAddress;
	}

	/**
	 * @param streetAddress
	 *            the streetAddress to set
	 */
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipcode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipCode = zipcode;
	}

	public String getKey() {
		return id.toString();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the streetAddress2
	 */
	public String getStreetAddress2() {
		return streetAddress2;
	}

	/**
	 * @param streetAddress2
	 *            the streetAddress2 to set
	 */
	public void setStreetAddress2(String streetAddress2) {
		this.streetAddress2 = streetAddress2;
	}

	/**
	 * @return the streetAddress3
	 */
	public String getStreetAddress3() {
		return streetAddress3;
	}

	/**
	 * @param streetAddress3
	 *            the streetAddress3 to set
	 */
	public void setStreetAddress3(String streetAddress3) {
		this.streetAddress3 = streetAddress3;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

}
