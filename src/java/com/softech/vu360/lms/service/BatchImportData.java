package com.softech.vu360.lms.service;

import java.util.List;

import org.apache.velocity.app.VelocityEngine;

import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.vo.BatchImportParam;
import com.softech.vu360.util.Brander;

/**
 * Contains all the input parameters for the batch import run.
 * @author abdul.aziz
 */
public class BatchImportData {

    protected Customer customer = null;
    protected String file = null;
    protected String delimiter = null;
    protected String actionOnDuplicateRecords = null;
    protected boolean accVisible = true;
    protected boolean accLocked = false;
    protected boolean isFirstRowHeader = true;
    protected boolean notifyLearnerOnRegistration = true;
    protected String loginURL = null;
    protected VU360User loggedInUser = null;
    protected VelocityEngine velocityEngine = null;
    protected List<CustomField> allCustomFields = null;
    protected Brander brander = null;
    protected boolean changePasswordOnLogin = false;  // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login

    /**
     * Initializes the batch import input with input parameters and custom fields.
     * @param batchImportParam
     * @param customFields 
     */
    public BatchImportData(BatchImportParam batchImportParam, List<CustomField> customFields){
        this(
            batchImportParam.getCustomer(), 
            batchImportParam.getFile(), 
            batchImportParam.getDelimiter(), 
            batchImportParam.getActionOnDuplicateRecords(), 
            batchImportParam.isAccVisibleOnReport(), 
            batchImportParam.isAccLocked(), 
            batchImportParam.isFirstRowHeader(), 
            batchImportParam.isNotifyLearnerOnRegistration(), 
            batchImportParam.getLoginURL(), 
            batchImportParam.getLoggedInUser(), 
            batchImportParam.getVelocityEngine(), 
            customFields, 
            batchImportParam.getBrander(), 
            batchImportParam.isChangePasswordOnLogin()
        );
    }
    
    public BatchImportData(Customer customer,
            String file,
            String delimiter,
            String actionOnDuplicateRecords,
            boolean accVisible,
            boolean accLocked,
            boolean isFirstRowHeader,
            boolean notifyLearnerOnRegistration,
            String loginURL,
            VU360User loggedInUser,
            VelocityEngine velocityEngine,
            List<CustomField> allCustomFields,
            Brander brander,
            boolean changePasswordOnLogin) {
        this.customer = customer;
        this.file = file;
        this.delimiter = delimiter;
        this.actionOnDuplicateRecords = actionOnDuplicateRecords;
        this.accVisible = accVisible;
        this.accLocked = accLocked;
        this.isFirstRowHeader = isFirstRowHeader;
        this.notifyLearnerOnRegistration = notifyLearnerOnRegistration;
        this.loginURL = loginURL;
        this.loggedInUser = loggedInUser;
        this.velocityEngine = velocityEngine;
        this.allCustomFields = allCustomFields;
        this.brander = brander;
        this.changePasswordOnLogin = changePasswordOnLogin;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * @return the delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * @param delimiter the delimiter to set
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * @return the actionOnDuplicateRecords
     */
    public String getActionOnDuplicateRecords() {
        return actionOnDuplicateRecords;
    }

    /**
     * @param actionOnDuplicateRecords the actionOnDuplicateRecords to set
     */
    public void setActionOnDuplicateRecords(String actionOnDuplicateRecords) {
        this.actionOnDuplicateRecords = actionOnDuplicateRecords;
    }

    /**
     * @return the accVisible
     */
    public boolean isAccVisible() {
        return accVisible;
    }

    /**
     * @param accVisible the accVisible to set
     */
    public void setAccVisible(boolean accVisible) {
        this.accVisible = accVisible;
    }

    /**
     * @return the accLocked
     */
    public boolean isAccLocked() {
        return accLocked;
    }

    /**
     * @param accLocked the accLocked to set
     */
    public void setAccLocked(boolean accLocked) {
        this.accLocked = accLocked;
    }

    /**
     * @return the isFirstRowHeader
     */
    public boolean isFirstRowHeader() {
        return isFirstRowHeader;
    }

    /**
     * @param isFirstRowHeader the isFirstRowHeader to set
     */
    public void setIsFirstRowHeader(boolean isFirstRowHeader) {
        this.isFirstRowHeader = isFirstRowHeader;
    }

    /**
     * @return the notifyLearnerOnRegistration
     */
    public boolean notifyLearnerOnRegistration() {
        return notifyLearnerOnRegistration;
    }

    /**
     * @param notifyLearnerOnRegistration the notifyLearnerOnRegistration to set
     */
    public void setNotifyLearnerOnRegistration(boolean notifyLearnerOnRegistration) {
        this.notifyLearnerOnRegistration = notifyLearnerOnRegistration;
    }

    /**
     * @return the loginURL
     */
    public String getLoginURL() {
        return loginURL;
    }

    /**
     * @param loginURL the loginURL to set
     */
    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }

    /**
     * @return the loggedInUser
     */
    public VU360User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * @param loggedInUser the loggedInUser to set
     */
    public void setLoggedInUser(VU360User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * @return the velocityEngine
     */
    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    /**
     * @param velocityEngine the velocityEngine to set
     */
    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    /**
     * @return the allCustomFields
     */
    public List<CustomField> getAllCustomFields() {
        return allCustomFields;
    }

    /**
     * @param allCustomFields the allCustomFields to set
     */
    public void setAllCustomFields(List<CustomField> allCustomFields) {
        this.allCustomFields = allCustomFields;
    }

    /**
     * @return the brander
     */
    public Brander getBrander() {
        return brander;
    }

    /**
     * @param brander the brander to set
     */
    public void setBrander(Brander brander) {
        this.brander = brander;
    }

    /**
     * @return the changePasswordOnLogin
     */
    public boolean changePasswordOnLogin() {
        return changePasswordOnLogin;
    }

    /**
     * @param changePasswordOnLogin the changePasswordOnLogin to set
     */
    public void setChangePasswordOnLogin(boolean changePasswordOnLogin) {
        this.changePasswordOnLogin = changePasswordOnLogin;
    }
}
