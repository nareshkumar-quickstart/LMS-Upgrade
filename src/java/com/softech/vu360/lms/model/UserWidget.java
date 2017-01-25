package com.softech.vu360.lms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author haider.ali
 * @dated 11/11/2015
 */
@Entity
@Table(name = "USERWIDGET")
@IdClass(UserWidgetPK.class)
public class UserWidget implements Serializable {

	private static final long serialVersionUID = -6418650355292416289L;
	
	@Column(name = "position")
	private Long position;
	
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "widgetId")
	private Widget widget;

	@Id
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "userId")
	private VU360User user ; 

	
	public UserWidget() {
		this.user = new VU360User();
		this.widget = new Widget();
	}

	public Long getPosition() {
		return this.position;
	}

	public VU360User getUser() {
		return this.user;
	}

	public Widget getWidget() {
		return this.widget;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	public void setUser(VU360User user) {
		this.user = user;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	@Override
	public String toString() {
		return "UserWidget [position=" + position + ", widget=" + widget
				+ ", user=" + user + "]";
	}

}
