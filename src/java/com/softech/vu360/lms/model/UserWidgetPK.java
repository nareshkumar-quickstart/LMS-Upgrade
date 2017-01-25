package com.softech.vu360.lms.model;

import java.io.Serializable;

public class UserWidgetPK  implements Serializable{

	private static final long serialVersionUID = 4689156822744722953L;
	private Widget widget;
	private VU360User user ;
	
	public Widget getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	public VU360User getUser() {
		return user;
	}
	public void setUser(VU360User user) {
		this.user = user;
	}

}
