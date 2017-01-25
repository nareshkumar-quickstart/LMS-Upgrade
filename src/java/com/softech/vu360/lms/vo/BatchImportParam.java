package com.softech.vu360.lms.vo;
/**
 * User: Faisal A. Siddiqui
 * Date: May 27, 2010
 * @since LMS-5781
 */


import org.apache.velocity.app.VelocityEngine;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.Brander;


public class BatchImportParam {
    
    
    private final Customer customer;
    private final boolean accLocked;
    private final boolean accVisibleOnReport;
    private final boolean isFirstRowHeader;
    private final boolean notifyLearnerOnRegistration;
    private final boolean changePasswordOnLogin;	// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
    
    private final String delimiter;
    private String file;
    private final String loginURL;
    private final String actionOnDuplicateRecords;
    private final Brander brander;
    

    private final VelocityEngine velocityEngine;
    private final VU360User loggedInUser;
    

	public BatchImportParam(Customer customer,String file, String delimiter, String actionOnDuplicateRecords,boolean accVisible,boolean accLocked,boolean isFirstRowHeader,boolean notifyLearnerOnRegistration, String loginURL,VU360User loggedInUser, VelocityEngine velocityEngine,Brander brander, boolean changePasswordOnLogin)
	{
		this.customer=customer;
		this.file=file;
		this.delimiter=delimiter;
		this.actionOnDuplicateRecords=actionOnDuplicateRecords;
		this.accVisibleOnReport=accVisible;
		this.accLocked=accLocked;
		this.isFirstRowHeader=isFirstRowHeader;
		this.notifyLearnerOnRegistration=notifyLearnerOnRegistration;
		this.loginURL=loginURL;
		this.loggedInUser=loggedInUser;
		this.velocityEngine=velocityEngine;
		this.brander=brander;
		this.changePasswordOnLogin = changePasswordOnLogin;		// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
	}


	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	public Customer getCustomer() {
		return customer;
	}


	public boolean isAccLocked() {
		return accLocked;
	}


	public boolean isAccVisibleOnReport() {
		return accVisibleOnReport;
	}


	public boolean isFirstRowHeader() {
		return isFirstRowHeader;
	}


	public boolean isNotifyLearnerOnRegistration() {
		return notifyLearnerOnRegistration;
	}


	public String getDelimiter() {
		return delimiter;
	}


	public String getLoginURL() {
		return loginURL;
	}


	public String getActionOnDuplicateRecords() {
		return actionOnDuplicateRecords;
	}


	public Brander getBrander() {
		return brander;
	}


	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}


	public VU360User getLoggedInUser() {
		return loggedInUser;
	}


	/**
	 * // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
	 * @return the changePasswordOnLogin
	 */
	public boolean isChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}
}
