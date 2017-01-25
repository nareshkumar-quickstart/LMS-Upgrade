package com.softech.vu360.lms.vo;

import java.io.Serializable;

public class TimeZone implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String code = null;
	private String zone = null;
	private Integer hours;
	private Integer minutes;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getZone() {
		return zone;
	}

	public void setHours(Integer hours) {
		this.hours = hours;
	}

	public Integer getHours() {
		return hours;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public String getFormattedTimeZone() {

		if (this.hours == 0) {
			return "(" + code.toUpperCase() + ") " + zone;
		}

		return "(" + code.toUpperCase() + String.format("%+03d", hours) + ":" + String.format("%02d", minutes) + ") "
				+ zone;

	}

}
