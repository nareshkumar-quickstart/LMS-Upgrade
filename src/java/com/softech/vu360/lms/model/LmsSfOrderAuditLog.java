package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Nationalized;
/**
 * 
 * @author muhammad.rehan
 *
 */
@Entity
@Table(name = "ORDERAUDITLOG")
public class LmsSfOrderAuditLog implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@javax.persistence.TableGenerator(name = "ORDERAUDITLOG_ID", table = "VU360_SEQ", pkColumnName = "TABLE_NAME", valueColumnName = "NEXT_ID", pkColumnValue = "ORDERAUDITLOGSEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ORDERAUDITLOG_ID")
	private Long id;

    @Column(name = "ORDERID")
	private String orderId;
	
    @Nationalized
    @Column(name = "COURSEGUID")
	private String courseGUID;
	
    @Column(name = "COURSEGROUPGUID")
	private String courseGroupGUID;
	
    @Column(name = "STATUS")
	private String status;
	
    @Column(name = "MESSAGE")
	private String message;
	
    @Column(name = "ACTIONTYPE")
	private String actionType;
	
    @Column(name = "VALIDCOURSEGUID")
	private Boolean validCourseGUID;
	
	public LmsSfOrderAuditLog(){
		
	}
	
	public LmsSfOrderAuditLog(String orderId, String courseGUID,
			String courseGroupGUID, String status, String message,
			String actionType, Boolean validCourseGUID) {

		this.orderId = orderId;
		this.courseGUID = courseGUID;
		this.courseGroupGUID = courseGroupGUID;
		this.status = status;
		this.message = message;
		this.actionType = actionType;
		this.validCourseGUID = validCourseGUID;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}
	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * @return the courseGUID
	 */
	public String getCourseGUID() {
		return courseGUID;
	}
	/**
	 * @param courseGUID the courseGUID to set
	 */
	public void setCourseGUID(String courseGUID) {
		this.courseGUID = courseGUID;
	}
	/**
	 * @return the courseGroupGUID
	 */
	public String getCourseGroupGUID() {
		return courseGroupGUID;
	}
	/**
	 * @param courseGroupGUID the courseGroupGUID to set
	 */
	public void setCourseGroupGUID(String courseGroupGUID) {
		this.courseGroupGUID = courseGroupGUID;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}
	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	/**
	 * @return the validCourseGUID
	 */
	public Boolean isValidCourseGUID() {
		return validCourseGUID;
	}
	/**
	 * @param validCourseGUID the validCourseGUID to set
	 */
	public void setValidCourseGUID(Boolean validCourseGUID) {
		this.validCourseGUID = validCourseGUID;
	}
	
	
	
}
