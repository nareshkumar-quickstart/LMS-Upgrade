package com.softech.vu360.lms.meetingservice.dimdim;

public class DimDimUpdateUserRequest extends DimDimCreateUserRequest{
	public DimDimUpdateUserRequest() {
		setURL(DimDimConfiguration.getInstance().getUpdateUserURL());
	}
	
}
