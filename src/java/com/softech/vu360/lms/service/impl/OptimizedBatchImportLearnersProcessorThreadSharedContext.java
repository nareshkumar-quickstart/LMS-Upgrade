package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;

import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.util.Brander;

/**
 * @author S M Humayun
 */
public class OptimizedBatchImportLearnersProcessorThreadSharedContext {

    private String delimiter;
    private boolean isFirstRowHeader;
    private boolean accountLocked;
    private boolean accountVisible;
    private String actionOnDuplicateRecords;
    private VU360User loggedInUser;
    private Brander brander;
    private List<CustomField> allCustomFields;
    private Map<Integer, CustomField> customFieldsWithIndicesInFileMap;
    private Map<CustomField,List<CustomFieldValueChoice>> customFieldValueChoices;
    private Customer currentCustomer;
    private List<VU360User> existingUserList;
    private List<Map<Object, Object>> errorMessages;
    private Integer updatedLearners;
    private List<Map<Object, Object>> numOfupdatedLearners;
    private Integer numberOfImportedUsers;
    private List<Learner> addedLearners;
    private int recordNumber;
    //LMS-5973 : To send the original password to newly added learner without decoding it.
    private List<String> passwords;
    private Map<String, OrganizationalGroup> allOrganizationalGroups;
    private Map<String, LearnerGroup> allLearnerGroups;
    private OrganizationalGroup rootOrganizationalGroup;
    private Set<OrganizationalGroup> orgGroupsToSave;
    private VelocityEngine velocityEngine;
    private String headerColumns[];
    private int accountLockedIndex = -1;
    private int accountVisibleOnReportIndex = -1;
    private int managerOrgGroupIndex = -1;
    private static final Logger log = Logger.getLogger(OptimizedBatchImportLearnersProcessorThreadSharedContext.class.getName()); 
    private List<OptimizedBatchImportLearnersErrors> batchImportErrors;
	private LMSRole defaultLearnerRole = null;
	private LMSRole defaultManagerRole = null;
	private boolean changePasswordOnLogin;  // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
	
	//http://jira.360training.com/browse/LMS-12908
    // Objective of this field was to check if field is exists and 'Required' but
    // for ticket LMS-12908 restriction of Required is removed and now this field check existence
    // field name is still same 'NERCFieldRequired' but its purpose is changed from Required to Existence 
    protected boolean NERCFieldRequired = false;
    protected int NERCFieldIndex = -1;
    protected CreditReportingField NERCField = null;
    private int securityRoleColumnIndex = -1;

    public List<String> getPasswords() {
		return passwords;
	}

	public void setPasswords(List<String> passwords) {
		this.passwords = passwords != null ? Collections.synchronizedList(passwords) : null;
	}

	public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isFirstRowHeader() {
        return isFirstRowHeader;
    }

    public void setFirstRowHeader(boolean firstRowHeader) {
        isFirstRowHeader = firstRowHeader;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
    }

    public String getActionOnDuplicateRecords() {
        return actionOnDuplicateRecords;
    }

    public void setActionOnDuplicateRecords(String actionOnDuplicateRecords) {
        this.actionOnDuplicateRecords = actionOnDuplicateRecords;
    }

    public VU360User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(VU360User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public Brander getBrander() {
        return brander;
    }

    public void setBrander(Brander brander) {
        this.brander = brander;
    }

    public List<CustomField> getAllCustomFields() {
        return allCustomFields;
    }

    public void setAllCustomFields(List<CustomField> allCustomFields) {
        this.allCustomFields = allCustomFields != null ? Collections.synchronizedList(allCustomFields) : null;
    }

    public Map<Integer, CustomField> getCustomFieldsWithIndicesInFileMap() {
        return customFieldsWithIndicesInFileMap;
    }

    public void setCustomFieldsWithIndicesInFileMap(Map<Integer, CustomField> customFieldsWithIndicesInFileMap) {
        this.customFieldsWithIndicesInFileMap = customFieldsWithIndicesInFileMap != null ?
                Collections.synchronizedMap(customFieldsWithIndicesInFileMap) : null;
    }

    public Map<CustomField, List<CustomFieldValueChoice>> getCustomFieldValueChoices() {
        return customFieldValueChoices;
    }

    public void setCustomFieldValueChoices(Map<CustomField, List<CustomFieldValueChoice>> customFieldValueChoices) {
        this.customFieldValueChoices = customFieldValueChoices != null ?
                Collections.synchronizedMap(customFieldValueChoices) : null;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public List<VU360User> getExistingUserList() {
        return existingUserList;
    }

    public void setExistingUserList(List<VU360User> existingUserList) {
        this.existingUserList = existingUserList != null ? Collections.synchronizedList(existingUserList) : null;
    }

    public List<Map<Object, Object>> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<Map<Object, Object>> errorMessages) {
        this.errorMessages = errorMessages != null ? Collections.synchronizedList(errorMessages) : null;
    }

    public Integer getUpdatedLearners() {
        return updatedLearners;
    }

    public void setUpdatedLearners(Integer updatedLearners) {
        this.updatedLearners = updatedLearners;
    }

    public List<Map<Object, Object>> getNumOfupdatedLearners() {
        return numOfupdatedLearners;
    }

    public void setNumOfupdatedLearners(List<Map<Object, Object>> numOfupdatedLearners) {
        //this.numOfupdatedLearners = Collections.synchronizedList(numOfupdatedLearners);
        this.numOfupdatedLearners = numOfupdatedLearners;
    }

    public List<Learner> getAddedLearners() {
        return addedLearners;
    }

    public void setAddedLearners(List<Learner> addedLearners) {
        //this.addedLearners = Collections.synchronizedList(addedLearners);
        this.addedLearners = addedLearners;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public Integer getNumberOfImportedUsers() {
        return numberOfImportedUsers;
    }

    public void setNumberOfImportedUsers(Integer numberOfImportedUsers) {
        this.numberOfImportedUsers = numberOfImportedUsers;
    }

    public Map<String, OrganizationalGroup> getAllOrganizationalGroups() {
        return allOrganizationalGroups;
    }

    public void setAllOrganizationalGroups(Map<String, OrganizationalGroup> allOrganizationalGroups) {
        this.allOrganizationalGroups = allOrganizationalGroups != null ?
                Collections.synchronizedMap(allOrganizationalGroups) : null;
    }

    public OrganizationalGroup getRootOrganizationalGroup() {
        return rootOrganizationalGroup;
    }

    public void setRootOrganizationalGroup(OrganizationalGroup rootOrganizationalGroup) {
        this.rootOrganizationalGroup = rootOrganizationalGroup;
    }

    public Set<OrganizationalGroup> getOrgGroupsToSave() {
        return orgGroupsToSave;
    }

    public void setOrgGroupsToSave(Set<OrganizationalGroup> orgGroupsToSave) {
        this.orgGroupsToSave = orgGroupsToSave != null ? Collections.synchronizedSet(orgGroupsToSave) : null;
    }

    public Map<String, LearnerGroup> getAllLearnerGroups() {
        return allLearnerGroups;
    }

    public void setAllLearnerGroups(Map<String, LearnerGroup> allLearnerGroups) {
        this.allLearnerGroups = allLearnerGroups != null ?
                Collections.synchronizedMap(allLearnerGroups) : null;
    }

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * @return the headerRow
	 */
	public String[] getHeaderColumns() {
		return headerColumns;
	}

	/**
	 * @param headerRow the headerRow to set
	 */
	public void setHeaderColumns(String[] headerColumns) {
		this.headerColumns = headerColumns;
	}

	/**
	 * @return the accountLockedIndex
	 */
	public int getAccountLockedIndex() {
		return accountLockedIndex;
	}

	/**
	 * @param accountLockedIndex the accountLockedIndex to set
	 */
	public void setAccountLockedIndex() {
		String accountLockedColumnName = brander.getBrandElement("lms.batchImportUsers.accountLockedColumn.name").trim();
		int index = -1;
		boolean indexFound = false;
		for(String column:headerColumns){
			index++;
			if(column.trim().equalsIgnoreCase(accountLockedColumnName)){
				indexFound = true;
				break;
			}
		}
		if(indexFound){
			accountLockedIndex = index;
		}
	}

	/**
	 * @return the accountVisibleOnReportIndex
	 */
	public int getAccountVisibleOnReportIndex() {
		return accountVisibleOnReportIndex;
	}

	/**
	 * @param accountVisibleOnReportIndex the accountVisibleOnReportIndex to set
	 */
	public void setAccountVisibleOnReportIndex() {
		String accountVisibleOnReportColumnName = brander.getBrandElement("lms.batchImportUsers.accountVisibleOnReportColumn.name").trim();
		int index = -1;
		boolean indexFound = false;
		for(String column:headerColumns){
			index++;
			if(column.trim().equalsIgnoreCase(accountVisibleOnReportColumnName)){
				indexFound = true;
				break;
			}
		}
		if(indexFound){
			accountVisibleOnReportIndex = index;
		}
	}

	/**
	 * @return the manageOrgGroupIndex
	 */
	public int getManagerOrgGroupIndex() {
		return managerOrgGroupIndex;
	}

	/**
	 * @param manageOrgGroupIndex the manageOrgGroupIndex to set
	 */
	public void setManageOrgGroupIndex() {
		
		String managerOrgGroupColumnName = brander.getBrandElement("lms.batchImportUsers.managerOrgGroupColumn.name").trim();
		int index = -1;
		boolean indexFound = false;
		for(String column:headerColumns){
			index++;
			if(column.trim().equalsIgnoreCase(managerOrgGroupColumnName)){
				indexFound = true;
				break;
			}
		}
		if(indexFound){
			managerOrgGroupIndex = index;
		}

	}

	/**
	 * @return the defaultLearnerRole
	 */
	public LMSRole getDefaultLearnerRole() {
		return defaultLearnerRole;
	}

	/**
	 * @param defaultLearnerRole the defaultLearnerRole to set
	 */
	public void setDefaultLearnerRole(LMSRole defaultLearnerRole) {
		this.defaultLearnerRole = defaultLearnerRole;
	}

	/**
	 * @return the defaultManagerRole
	 */
	public LMSRole getDefaultManagerRole() {
		return defaultManagerRole;
	}

	/**
	 * @param defaultManagerRole the defaultManagerRole to set
	 */
	public void setDefaultManagerRole(LMSRole defaultManagerRole) {
		this.defaultManagerRole = defaultManagerRole;
	}
    private HashMap<Long,Set<Learner>> learnerGroupAndLearnerMap = new HashMap<Long,Set<Learner>>();
	synchronized public void updateLearnerGroupMap(Learner learner, Set<LearnerGroup> learnerGroups){
		for(LearnerGroup lg: learnerGroups){
			log.debug("Learner Group:"+lg.getName()+" id:"+lg.getId());
			Set<Learner> learners = learnerGroupAndLearnerMap.get(lg.getId());
			if(learners == null){
				log.debug("Adding Learner Group in Map");
				learners = new HashSet<Learner>();
				learnerGroupAndLearnerMap.put(lg.getId(), learners);
			}
			log.debug("Adding Learner :"+learner.getVu360User().getUsername()+" in Learner Group:"+lg.getName());
			learners.add(learner);
		}	
	}
	public Set<Learner> getLearnersByLearnerGroup(LearnerGroup learnerGroup){
		Set<Learner> learners = new HashSet<Learner>();
		if(learnerGroup!=null && learnerGroupAndLearnerMap.containsKey(learnerGroup.getId()))
			learners = learnerGroupAndLearnerMap.get(learnerGroup.getId());
		return learners;
	}
	
	/**
	 * List of learner groups for which enrollment is to be made
	 * Returns list that in how many learner groups learners are to be enrolled
	 * @return
	 */
	public List<Long> getLearnerGroupIdsInWhichToEnrollLearner(){
		return new ArrayList<Long>(learnerGroupAndLearnerMap.keySet());
	}
	
	public List<OptimizedBatchImportLearnersErrors> getBatchImportErrors() {
		return batchImportErrors;
	}

	public void setBatchImportErrors(
			List<OptimizedBatchImportLearnersErrors> batchImportErrors) {
		this.batchImportErrors = batchImportErrors;
	}

	/**
	 * // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
	 * @param changePasswordOnLogin the changePasswordOnLogin to set
	 */
	public void setChangePasswordOnLogin(boolean changePasswordOnLogin) {
		this.changePasswordOnLogin = changePasswordOnLogin;
	}

	/**
	 * // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
	 * @return the changePasswordOnLogin
	 */
	public boolean isChangePasswordOnLogin() {
		return changePasswordOnLogin;
	}
	
        /**
         * @return the NERCFieldRequired
         */
        public boolean isNERCFieldRequired() {
            return NERCFieldRequired;
        }
	
        /**
         * @param NERCFieldRequired the NERCFieldRequired to set
         */
        public void setNERCFieldRequired(boolean NERCFieldRequired) {
            this.NERCFieldRequired = NERCFieldRequired;
}

    /**
     * @return the NERCFieldIndex
     */
    public int getNERCFieldIndex() {
        return NERCFieldIndex;
    }

    /**
     * @param NERCFieldIndex the NERCFieldIndex to set
     */
    public void setNERCFieldIndex(int NERCFieldIndex) {
        this.NERCFieldIndex = NERCFieldIndex;
    }

    /**
     * @return the NERCField
     */
    public CreditReportingField getNERCField() {
        return NERCField;
    }

    /**
     * @param NERCField the NERCField to set
     */
    public void setNERCField(CreditReportingField NERCField) {
        this.NERCField = NERCField;
    }
    
    public int getSecurityRoleColumnIndex() {
		return securityRoleColumnIndex;
	}

	public void setSecurityRoleColumnIndex(int securityRoleColumnIndex) {
		this.securityRoleColumnIndex = securityRoleColumnIndex;
	}
}
