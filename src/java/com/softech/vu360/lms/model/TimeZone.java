package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

@Entity
@Table(name = "TIMEZONE")
public class TimeZone implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	
	@Id
    private Integer id;
	
	@Column(name = "CODE")
	private String code = null;
	
	@Column(name = "ZONE")
	private String zone = null;
	
	@Column(name = "HOURS")
	private Integer hours;
	
	@Column(name = "MINUTES")
	private Integer minutes;
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param zone the zone to set
	 */
	public void setZone(String zone) {
		this.zone = zone;
	}
	/**
	 * @return the zone
	 */
	public String getZone() {
		return zone;
	}
	/**
	 * @param hours the hours to set
	 */
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	/**
	 * @return the hours
	 */
	public Integer getHours() {
		return hours;
	}
	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}
	/**
	 * @return the minutes
	 */
	public Integer getMinutes() {
		return minutes;
	}
	
	public String getFormattedTimeZone () {
		
		if ( this.hours == 0 ){
			return "("
				+ code.toUpperCase() + ") "
				+ zone;
		}
		
		return "("
			+ code.toUpperCase() 			
			+ String.format("%+03d", hours) + ":"
			+ String.format("%02d", minutes) + ") "
			+ zone;
		
	}
		
}
