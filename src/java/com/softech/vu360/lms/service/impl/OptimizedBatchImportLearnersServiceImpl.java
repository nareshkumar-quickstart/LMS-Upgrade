package com.softech.vu360.lms.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.TelephoneNumberCreditReportingField;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.lms.service.BatchImportData;
import com.softech.vu360.lms.service.BatchImportLearnersService;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.util.AsyncTaskExecutorWrapper;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.LearnersToBeMailedService;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Properties;


/**
 * @author S M Humayun
 */
public class OptimizedBatchImportLearnersServiceImpl implements BatchImportLearnersService, Runnable {

    private static final Logger log = Logger.getLogger(OptimizedBatchImportLearnersServiceImpl.class.getName());
    public static String ERROR_MESSAGE_TEXT = "Incorrect display sequence";
    public static String REQUIRED_FIELDS_MISSING_TEXT = " required fields missing in your batch file";
    public static final int FIX_FIELD_SIZE = 17;
    public static final int NUMBER_OF_ALLOWED_RECORDS = 10000;
    private static final String GROUP_SPLITTER = ">";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    private OrgGroupLearnerGroupService orgGroupService;
    private VU360UserService vu360UserService;
    private LearnerService learnerService;
    private CustomFieldService customFieldService;
    private EntitlementService entitlementService;
	private EnrollmentService enrollmentService;
	private AsyncTaskExecutorWrapper asyncTaskExecutorWrapper;
	private LearnersToBeMailedService learnersToBeMailedService;
    private AccreditationService accreditationService = null;
    private ActiveDirectoryService activeDirectoryService = null;

    private static final int COL_FIRST_NAME = 0;
    private static final int COL_MIDDLE_NAME = 1;
    private static final int COL_LAST_NAME = 2;
    private static final int COL_OFFICE_PHONE = 3;
    private static final int COL_OFFICE_PHONE_EXT = 4;
    private static final int COL_MOBILE_PHONE = 5;
    private static final int COL_STREET_ADDRESS = 6;
    private static final int COL_STREET_ADDRESS2 = 7;
    private static final int COL_CITY = 8;
    private static final int COL_STATE = 9;
    private static final int COL_ZIPCODE = 10;
    private static final int COL_COUNTRY = 11;
    private static final int COL_EMAIL = 12;
    private static final int COL_PASSWORD = 13;
    private static final int COL_LEARNER_GRPS = 14;
    private static final int COL_ORG_GRPS = 15;
    private static final int COL_USERNAME = 16;
    private static final int COL_MANAGER_ORG_GRP = 17;
    private static final int COL_ACCOUNT_LOCKED = 18;
    private static final int COL_ACCOUNT_VISIBLE_ON_REPORTS = 19;
    private static final int COL_SECURITY_ROLE = -1;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
    private static Map<String, Long> timerMap = new HashMap<String, Long>();
    private static long startTime = System.currentTimeMillis();
    private String lastStart;
    private static final int numOfProcessingThreads = 1;

    public static void logMethod (String methodName, boolean start)
    {
        logDebug(getTimerMessage(methodName, start));
    }

    public static void logDebug (String str)
    {
        log.info(getTimeDifference(startTime, System.currentTimeMillis()) + " | " +
                StringUtils.leftPad(Thread.currentThread().getName(), 5) + " > " + str);
    }

    public static void timer (String message, boolean start)
    {
        logDebug(getTimerMessage(message, start));
    }

    public static String getTimerMessage (String message, boolean start)
    {
        String msg;
        String name = Thread.currentThread().getName();
        if(start)
        {
            timerMap.put(name + message, System.currentTimeMillis());
            msg = "- - - - - " + message + " - START - - - - - ";
        }
        else
        {
            Long time = timerMap.remove(name + message);
            if(time != null)
            {
                msg = "- - - - - " + message + " - END - " + getTimeDifference(time, System.currentTimeMillis()) +
                        " - - - - - ";
            }
            else
                msg = name + message + "' is null.";
        }
        return msg;
    }

    private static String getTimeDifference (long t1, long t2)
    {
        long diff = t2 - t1;
        int h = 0, m = 0, s = 0, ms;
        if(diff >= 3600000)
        {
            h = (int) (diff/3600000);
            diff -= h * 3600000;
        }
        if(diff >= 60000)
        {
            m = (int) (diff/60000);
            diff -= m * 60000;
        }
        if(diff >= 1000)
        {
            s = (int) (diff/1000);
            diff -= s * 1000;
        }
        ms = (int) diff;
        return StringUtils.leftPad("" + h, 2, '0') + ":" + StringUtils.leftPad("" + m, 2, '0') + ":" +
                StringUtils.leftPad("" + s, 2, '0') + "." + StringUtils.leftPad("" + ms, 3, '0');
    }

    public Map<String, String[]> validateCSVData(
            String csvFileData,
            boolean firstRowHeader,
            String delimiter,
            Brander brander,
            List<CustomField> allCustomFields
    ) throws Exception
    {
        try
        {
            logMethod("validateCSVData", true);
            delimiter = delimiterCheck(delimiter);
            Map<String, String[]> errors = null;
            if(csvFileData != null && csvFileData.length() > 0 && !csvFileData.trim().equals(""))
            {
                errors = validateDataSize(csvFileData, brander,firstRowHeader);
                if(errors != null && errors.size() > 0)
                    return errors;

                if(firstRowHeader)
                {
                    BufferedReader csvFileDataReader = null;
                    String header = null;
                    try
                    {
                        csvFileDataReader = new BufferedReader(new StringReader(csvFileData));
                        header = csvFileDataReader.readLine();
                    }
                    finally
                    {
                        if(csvFileDataReader != null)
                            csvFileDataReader.close();
                    }
                    String headerColumns[] = null;
                    if(header != null)
                    {
                        errors = validateDelimiters(header, brander, delimiter);
                        if(errors != null && errors.size() > 0)
                            return errors;
                        headerColumns=this.splitStringWithQuotes(header, delimiter);
                        //headerColumns = header.split(delimiter);
                    }

                    errors = validateCustomFields(headerColumns, allCustomFields);
                    if(errors != null)
                        return errors;
                }

            }
            return errors;
        }
        finally
        {
            logMethod("validateCSVData", false);
        }
    }

    public String delimiterCheck (String delimiter)
    {
        return delimiter.equalsIgnoreCase("|") ? "\\|" : delimiter;
    }

    public long getMaxUploadSizeInBytes (Brander brander)
    {
        long maxUploadSizeInBytes;
        try
        {
            maxUploadSizeInBytes = Long.parseLong(brander.getBrandElement("lms.batchUpload.maximumFileSize"));
        }
        catch(Exception e)
        {
            log.error(e, e);
            maxUploadSizeInBytes = -1;
        }
        //logDebug("maxUploadSizeInBytes = " + maxUploadSizeInBytes);
        return maxUploadSizeInBytes;
    }

    public boolean validDataSize (long dataSize, Brander brander)
    {
        logDebug("dataSize = " + dataSize);
        return dataSize > 0 && dataSize <= getMaxUploadSizeInBytes(brander);
    }

    public Map<String, String[]> validateDataSize (String data, Brander brander,boolean firstRowHeader)
    {

        Map<String, String[]> errors = null;
        try {
        	long timeStart = System.currentTimeMillis();
			long numberOfRecords = countLines(data,firstRowHeader);
			long timeEnd = System.currentTimeMillis();
			log.debug("TOTAL TIME TAKEN IN Checking Number OF LINES  :: "+getTimeDifference(timeStart, timeEnd));

			if(numberOfRecords<1 || numberOfRecords > NUMBER_OF_ALLOWED_RECORDS )
			{
				log.error("ERROR : data size is greater than max size allowed");
	            errors = new HashMap<String, String[]>();
	            errors.put("file", new String[] {
	                    brander.getBrandElement("lms.batchUpload.fileSizeNotice"),
	                    ERROR_MESSAGE_TEXT});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

        return errors;
    }

    public static long countLines(String file,boolean firstRowHeader) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(file.getBytes())));

        long lineCount = 0;
        char[] buffer = new char[4096];
        while(reader.readLine()!=null)
        {
        	 lineCount++;

        }
        reader.close();

//        if header is no then should not subtract -1 becasue it is subtracting one for header
        if(firstRowHeader)
        	--lineCount;

        return lineCount;
    }

    public Map<String, String[]> validateDelimiters (
            String header,
            Brander brander,
            String delimiter
    ) throws IOException
    {
        Map<String, String[]> errors = null;
        if (!validDelimiters(header, delimiter))
        {
            log.error("error !");
            errors = new HashMap<String, String[]>();
            errors.put("file", new String[] {
                    brander.getBrandElement("lms.batchImportUsersResult.InvalidDelimiter"),
                    ERROR_MESSAGE_TEXT});
        }
        return errors;
    }

    public boolean validDelimiters (String header, String delimiter)
    {
    	// Added by Dyutiman :: header.contains(delimiter) is not working in case of pipe(|)
    	// and tab. So, delimiter is manually assigned for these 2 cases.
    	if( delimiter.equals("\\|") ) {
    		delimiter = "|";
    	}
    	if( delimiter.equals("\\t") ) {
    		delimiter = "\t";
    	}
        boolean contains = header.contains(delimiter);
        logDebug("header " + (contains ? "" : "does not") + " contains delimiter");
        return contains;
    }

    public Map<String, String[]> validateCustomFields (
            String[] headerColumns,
            List<CustomField> allCustomFields
    ) throws IOException
    {
        try
        {
            logMethod("validateCustomFields", true);
            Map<String, String[]> errors = null;
            if(allCustomFields != null && allCustomFields.size() > 0)
            {
                logDebug("allFields size = " + allCustomFields.size());
                List<String> missingFields = this.getMissingCustomFields(headerColumns, allCustomFields);
                logDebug("missing fields = " + missingFields);
                if(missingFields != null && missingFields.size() > 0)
                {
                    log.error("error !");
                    StringBuilder errorMessage = new StringBuilder();
                    for(String field:missingFields)
                    {
                        errorMessage.append(field);
                        errorMessage.append(",");
                    }
                    errorMessage.delete(errorMessage.length()-1, errorMessage.length());
                    errorMessage.append(REQUIRED_FIELDS_MISSING_TEXT);
                    errors = new HashMap<String, String[]>();
                    errors.put("file", new String[]{errorMessage.toString(), ERROR_MESSAGE_TEXT});
                }
            }
            else
                logDebug("allFields is null or zero length");
            return errors;
        }
        finally
        {
            logMethod("validateCustomFields", false);
        }
    }

    public List<String> getMissingCustomFields (
            String[] headerColumns,
            List<CustomField> allCustomFields
    ) throws IOException
    {
        try
        {
            logMethod("getMissingCustomFields", true);
            boolean applyValidation = false;
            List<String> requiredFieldsNotFoundInHeader = new ArrayList<String>();
            boolean fieldFound;
            //logDebug("allCustomFields.size = " + allCustomFields.size());
            //logDebug("headerColumns = " + Arrays.toString(headerColumns));
            for(CustomField field:allCustomFields)
            {
                fieldFound = false;
                //logDebug("field '" + field.getFieldLabel() + "' is " + (field.isFieldRequired() ? "" : "not") + " required");
                if(field.getFieldRequired())
                {
                    applyValidation = true;
                    if(headerColumns != null && headerColumns.length > FIX_FIELD_SIZE)
                    {
                        //logDebug("\tsearching in header columns...");
                        for(int i = (FIX_FIELD_SIZE - 1); i < headerColumns.length; i++)
                        {
                            if(field.getFieldLabel().trim().equalsIgnoreCase(headerColumns[i]))
                            {
                                //logDebug("\tfound !");
                                fieldFound = true;
                                break;
                            }
                        }
                    }
                    else
                        fieldFound = false;
                    //logDebug("\t" + (fieldFound ? "" : "not") + " found.");
                    if(!fieldFound)
                        requiredFieldsNotFoundInHeader.add(field.getFieldLabel());
                }
            }
            if(applyValidation && requiredFieldsNotFoundInHeader.size() > 0)
                return requiredFieldsNotFoundInHeader;
            return null;
        }
        finally
        {
            logMethod("getMissingCustomFields", false);
        }
    }

    public Map<Object, Object> importUsersFromBatchFile(
            Customer currentCustomer,
            String csvFileData,
            String delimiter,
            String actionOnDuplicateRecords,
            boolean accountVisible,
            boolean accountLocked,
            boolean firstRowHeader,
            boolean notifyLearnerOnRegistration,
            String loginURL,
            VU360User loggedInUser,
            VelocityEngine velocityEngine,
            List<CustomField> allCustomFields,
            Brander brander,
            boolean changePasswordOnLogin  // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
    ) throws Exception
    {
        try
        {
            logMethod("importUsersFromBatchFile", true);
            loggedInUser = vu360UserService.findByIdForBatchImport(loggedInUser.getId());
            Map<Object, Object> context = new HashMap<Object, Object>();
            Map<Object, Object>  errorMsg = new HashMap<Object, Object> ();
            OptimizedBatchImportLearnersSummary batchImportResultSummary = new OptimizedBatchImportLearnersSummary();
            batchImportResultSummary.setBrander(brander);
            if (actionOnDuplicateRecords != null && actionOnDuplicateRecords.trim().equals("update"))
            	batchImportResultSummary.setUpdateExistingUser(true);
            else
            	batchImportResultSummary.setUpdateExistingUser(false);

            //Pre-processing the data import to determine if NERC field import is required.
            //http://jira.360training.com/browse/LMS-10568
            CreditReportingField NERCField = NERCBatchImportUtil.getNERCFieldIfExist(accreditationService);
            //boolean NERCFieldIsReuired = (NERCField != null && NERCField.isActive() && NERCField.isFieldRequired());

            //http://jira.360training.com/browse/LMS-12908
            // Objective of this field was to check if field is exists and 'Required' but
            // for ticket LMS-12908 restriction of Required is removed and now this field check existence
            // field name is still same 'NERCFieldIsReuired' but its purpose is changed from Required to Existence
            boolean NERCFieldIsReuired = isNERCFieldRequired(NERCField);
            int NERCFieldColumnIndex = -1;

            List<OptimizedBatchImportLearnersErrors> batchImportErrors = new ArrayList<OptimizedBatchImportLearnersErrors>();
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext = new
            OptimizedBatchImportLearnersProcessorThreadSharedContext();
            if ( csvFileData == null || csvFileData.isEmpty() || csvFileData.length() == 0 )
            {
                //logDebug(" file is null ");
                errorMsg.put("nullFile", true);
            }
            else
            {
                delimiter = delimiterCheck(delimiter);

                //logDebug("loading data for shared context...");

                Map<CustomField, List<CustomFieldValueChoice>> customFieldValueChoices;
                List<VU360User> existingUserList;
                BufferedReader csvFileDataReader = null;
                Set<Integer> invalidRecordLineNumbers = new HashSet<Integer>();
                Map<Integer, CustomField> customFieldsWithIndicesInFileMap;
                int totalRecordCount[] = new int[]{0};
                String headerColumns[] = null;
                try
                {
                    csvFileDataReader = new BufferedReader(new StringReader(csvFileData));
                    String header = csvFileDataReader.readLine();
                    if(header != null)
                    		headerColumns=this.splitStringWithQuotes(header, delimiter);

//                    if( NERCFieldIsReuired ) {
                    	NERCFieldColumnIndex = NERCBatchImportUtil.deteremineNERCColumnIndex(headerColumns, NERCField);
//                    }


                    customFieldsWithIndicesInFileMap = getCustomFieldsWithIndicesInFile(headerColumns , allCustomFields);
                    context.put("customFieldHash", customFieldsWithIndicesInFileMap);

                    if(firstRowHeader)
                        customFieldValueChoices = getCustomFieldChoices(allCustomFields);
                    else
                        customFieldValueChoices = new HashMap<CustomField, List<CustomFieldValueChoice>>(0);
                    existingUserList = getExistingUserList(csvFileDataReader, delimiter,
                            customFieldsWithIndicesInFileMap, customFieldValueChoices, allCustomFields, brander,
                            invalidRecordLineNumbers, totalRecordCount, batchImportErrors, currentCustomer,loggedInUser,NERCField,NERCFieldColumnIndex);
                }
                finally
                {
                    csvFileDataReader.close();
                }

                timer("getRootOrgGroupForCustomer", true);
                OrganizationalGroup rootOrgGroup = orgGroupService.getRootOrgGroupForCustomer(currentCustomer.getId());
//              rootOrgGroup = orgGroupService.loadForUpdateOrganizationalGroup(rootOrgGroup.getId());

                //@MariumSaud : New Method was added to load Organization Group inorder to avoid 'Could not find Session - Lazy Initialization Exception'
        		rootOrgGroup = orgGroupService.getOrgGroupById(rootOrgGroup.getId());
                Map<String, OrganizationalGroup> allOrganizationalGroups = new HashMap<String, OrganizationalGroup>();
                allOrganizationalGroups = this.getOrganizationalGroupMap(allOrganizationalGroups, rootOrgGroup);
                Map<String, LearnerGroup> allLearnerGroups = new HashMap<String, LearnerGroup>();
                allLearnerGroups = this.getLearnerGroupsMap(allLearnerGroups, rootOrgGroup);
                timer("getRootOrgGroupForCustomer", false);

                //logDebug("data loaded, creating shared context...");
                sharedContext.setNERCFieldRequired(NERCFieldIsReuired);
                sharedContext.setNERCFieldIndex(NERCFieldColumnIndex);
                sharedContext.setNERCField(NERCField);

                sharedContext.setChangePasswordOnLogin( changePasswordOnLogin );  // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
                sharedContext.setAccountLocked(accountLocked);
                sharedContext.setAccountVisible(accountVisible);
                sharedContext.setActionOnDuplicateRecords(actionOnDuplicateRecords);
                sharedContext.setBrander(brander);
                sharedContext.setAllCustomFields(allCustomFields);
                sharedContext.setDelimiter(delimiter);
                sharedContext.setFirstRowHeader(firstRowHeader);
                sharedContext.setLoggedInUser(loggedInUser);
                sharedContext.setCustomFieldValueChoices(customFieldValueChoices);
                sharedContext.setCurrentCustomer(currentCustomer);
                sharedContext.setAllOrganizationalGroups(allOrganizationalGroups);
                sharedContext.setAllLearnerGroups(allLearnerGroups);
                sharedContext.setRootOrganizationalGroup(rootOrgGroup);
                sharedContext.setExistingUserList(existingUserList);
                sharedContext.setBatchImportErrors(batchImportErrors);
                sharedContext.setRecordNumber(0);
                sharedContext.setUpdatedLearners(0);
                sharedContext.setNumOfupdatedLearners(new ArrayList<Map<Object, Object>>());
                sharedContext.setNumberOfImportedUsers(0);
                sharedContext.setAddedLearners(new ArrayList<Learner>());
                sharedContext.setCustomFieldsWithIndicesInFileMap(customFieldsWithIndicesInFileMap);
                sharedContext.setPasswords(new ArrayList<String>());
                sharedContext.setOrgGroupsToSave(new HashSet<OrganizationalGroup>());
                sharedContext.setVelocityEngine(velocityEngine);
                // LMS-6548
                sharedContext.setHeaderColumns(headerColumns);
                sharedContext.setAccountLockedIndex();
                sharedContext.setAccountVisibleOnReportIndex();
                sharedContext.setManageOrgGroupIndex();
                //logDebug("shared context created.");

                LMSRole defaultLearnerRole = this.getLearnerRole(currentCustomer); // fetch it once and assign to all new learner
                sharedContext.setDefaultLearnerRole(defaultLearnerRole);
                LMSRole defaultManagerRole = vu360UserService.getTop1RoleByName("MANAGER",currentCustomer);
                sharedContext.setDefaultManagerRole(defaultManagerRole);

                int securityRoleColumnIndex = getColumnNameIndex(headerColumns, "Security Role");
                sharedContext.setSecurityRoleColumnIndex(securityRoleColumnIndex);

                try
                {
                    csvFileDataReader = new BufferedReader(new StringReader(csvFileData));
                    if(firstRowHeader)
                    {
                        //logDebug("first row is header, skipping it...");
                        csvFileDataReader.readLine();
                    }
                    processUsingMultipleThreads(csvFileDataReader, sharedContext, this, invalidRecordLineNumbers,
                            totalRecordCount[0]);
                }
                finally
                {
                    csvFileDataReader.close();
                }

                timer("saveRootOrgGroup", true);
                orgGroupService.saveOrganizationalGroup(sharedContext.getRootOrganizationalGroup());
                timer("saveRootOrgGroup", false);

                batchImportResultSummary.setTotalRecordCount(totalRecordCount[0]);
                batchImportResultSummary.setRecordNumber(sharedContext.getRecordNumber());
                batchImportResultSummary.setTotalImportedUsers(sharedContext.getNumberOfImportedUsers());
                batchImportResultSummary.setTotalUpdatedUsers(sharedContext.getUpdatedLearners());
                batchImportResultSummary.setBatchImportErrorsList(sharedContext.getBatchImportErrors());
                batchImportResultSummary.setAddedLearners(sharedContext.getAddedLearners());
                batchImportResultSummary.setNullFile(false);
                batchImportResultSummary.setPasswords(sharedContext.getPasswords());
            }
            logDebug("notifyLearnerOnRegistration = " + notifyLearnerOnRegistration);

            /**
             * LMS-7920
             * @author sultan.mubasher
             */

            if(currentCustomer.getDistributor().getDistributorPreferences().isEnableRegistrationEmailsForNewCustomers()
            		&& currentCustomer.getCustomerPreferences().isEnableRegistrationEmailsForNewCustomers())
            	sendEmail( loggedInUser, brander, loginURL, velocityEngine, batchImportResultSummary, currentCustomer,notifyLearnerOnRegistration);

            context.put("errorMsg", errorMsg);
            context.put("customFieldHeaders", allCustomFields);
            context.put("batchImportResultSummary", batchImportResultSummary);

            // code to launch enrollment process of all learners specified in batch import file along leanrergroup
            timer("enrollLearnersToLearnerGroup", true);
            enrollLearnersToLearnerGroupCourses(sharedContext);
            timer("enrollLearnersToLearnerGroup", false);
            return context;
        }
        catch(Exception e){
        	e.printStackTrace();
        	throw new Exception(e);
        }
        finally
        {
            logMethod("importUsersFromBatchFile", false);
        }
    }

	/**
	 * @param NERCField
	 * @return
	 */
	private boolean isNERCFieldRequired(CreditReportingField NERCField) {
		return (NERCField != null && NERCField.isActive() && NERCField.isFieldRequired());
	}

    public void processUsingMultipleThreads (
            BufferedReader csvFileDataReader,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
            Runnable runnable,
            Set<Integer> invalidRecordLineNumbers,
            int totalRecordCount
    ) throws IOException, InterruptedException
    {
        try
        {
            logMethod("processUsingMultipleThreads", true);
            int netRecordCount = totalRecordCount - invalidRecordLineNumbers.size();
            logDebug("netRecordCount (" + netRecordCount + ") = totalRecordCount (" + totalRecordCount +
                    ") - invalidRecordLineNumbers.size (" + invalidRecordLineNumbers.size() + ")");
            int threadCount = numOfProcessingThreads;
            int csvFileDataChunkLineSize = 1;
            int remainderLineCount = 0;
            if(totalRecordCount < numOfProcessingThreads)
                threadCount = totalRecordCount;
            else
            {
                csvFileDataChunkLineSize = Math.round(totalRecordCount / threadCount);
                remainderLineCount = totalRecordCount % threadCount;
            }
            logDebug("csvFileDataChunkLineSize = " + csvFileDataChunkLineSize +
                    ", remainderLineCount = " + remainderLineCount +
                    ", numOfProcessingThreads = " + numOfProcessingThreads +
                    ", threadCount = " + threadCount);
            String line;
            int lineCount = 0;

            ArrayList<OptimizedBatchImportLearnersProcessorThread> processorThreads =
                    new ArrayList<OptimizedBatchImportLearnersProcessorThread>(threadCount);
            OptimizedBatchImportLearnersProcessorThread processorThread;
            StringBuilder csvFileDataChunk = new StringBuilder();
            String lineSeparator = System.getProperty("line.separator");
            int csvFileDataChunkLineSizeWithRemainder = csvFileDataChunkLineSize;
            if(remainderLineCount > 0)
            {
                csvFileDataChunkLineSizeWithRemainder++;
                remainderLineCount--;
                logDebug("csvFileDataChunkLineSizeWithRemainder = " + csvFileDataChunkLineSizeWithRemainder +
                        ", remainderLineCount = " + remainderLineCount);
            }
            logDebug("invalidRecordLineNumbers.size = " + invalidRecordLineNumbers.size());
            while((line = csvFileDataReader.readLine()) != null)
            {
                if(!invalidRecordLineNumbers.contains(++lineCount))
                {
                    /**
                     * Appending an empty first line due to lineSeparator, processing thread must ignore this line.
                     * Instead of inserting a check like (lineCount > 0 ? lineSeparator : "") for 10000 max iterations,
                     * the approach of adding an empty line with out any check seems to be lesser over head
                     */
                    csvFileDataChunk.append(lineSeparator).append(line);
                    if(lineCount == csvFileDataChunkLineSizeWithRemainder)
                    {
                        createAndStartProcessorThread(runnable, processorThreads, csvFileDataChunk.toString(),
                                lineCount, sharedContext);
                        csvFileDataChunk.delete(0, csvFileDataChunk.length());
                        lineCount = 0;
                        csvFileDataChunkLineSizeWithRemainder = csvFileDataChunkLineSize;
                        if(remainderLineCount > 0)
                        {
                            csvFileDataChunkLineSizeWithRemainder++;
                            remainderLineCount--;
                            logDebug("csvFileDataChunkLineSizeWithRemainder = " + csvFileDataChunkLineSizeWithRemainder +
                                    ", remainderLineCount = " + remainderLineCount);
                        }
                    }
                }
                else
                    logDebug("INVALID RECORD - SKIP IT");
            }
            if(lineCount > 0)
                createAndStartProcessorThread(runnable, processorThreads, csvFileDataChunk.toString(), lineCount,
                        sharedContext);
            csvFileDataChunk.delete(0, csvFileDataChunk.length());
            csvFileDataChunk.trimToSize();

            for (int i = 0; i < processorThreads.size(); i++)
            {
                processorThread = processorThreads.get(i);
                logDebug("waiting for thread '" + processorThread.getName() + "' to finish processing and join us...");
                processorThread.join();
                logDebug("thread '" + processorThread.getName() + "' finished processing and has joined");
            }
        }
        finally
        {
            logMethod("processUsingMultipleThreads", false);
        }
    }

    public void createAndStartProcessorThread (
            Runnable runnable,
            ArrayList<OptimizedBatchImportLearnersProcessorThread> processorThreads,
            String csvFileDataChunk,
            int lineCount,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext
    )
    {
        //logDebug("creating processor thread...");
        OptimizedBatchImportLearnersProcessorThread processorThread = new
                OptimizedBatchImportLearnersProcessorThread(runnable, "pt#" + (processorThreads.size() + 1),
                csvFileDataChunk, lineCount, sharedContext);
        processorThread.start();
        //logDebug("thread '" + processorThread.getName() + "' started");
        processorThreads.add(processorThread);
        logDebug("total processor thread count is now " + processorThreads.size());
    }

    @Override
    @Transactional
    public void run()
    {
        try
        {
            logMethod("run (" + Thread.currentThread().getName() + ")", true);
            OptimizedBatchImportLearnersProcessorThread thisThread = (OptimizedBatchImportLearnersProcessorThread)
                    Thread.currentThread();
            BufferedReader csvDataChunkReader = new BufferedReader(new StringReader(thisThread.getLocalContext().
                    getCsvFileDataChunk()));

            String rowLine, rowColumns[];
            int recordNumber = thisThread.getLocalContext().getRecordNumberOffset();

            //ignoring first line, because its just an additional new line, added while spliting data chunk
            csvDataChunkReader.readLine();

            VU360User newUser;
            Learner learner;
            VU360User existingUser = null;
            Set<OrganizationalGroup> setOfOrgGroups = null;
            Set<LearnerGroup> setOfLearnerGroups;

            Learner updateableLearner = null;

            while((rowLine = csvDataChunkReader.readLine()) != null)
            {
            	Map <Object, Object> errorMsg = new HashMap<Object, Object>();
            	OptimizedBatchImportLearnersErrors errorsInRecord = new OptimizedBatchImportLearnersErrors();

                recordNumber++;
                rowColumns = this.splitStringWithQuotes(rowLine, thisThread.getSharedContext().getDelimiter());
                for (int i = 0; i < rowColumns.length; i++)
                    rowColumns[i] = rowColumns[i].trim();


                initializeErrorMsgFlags(errorMsg, recordNumber, rowColumns);

                // populate custom field values in errorMsg
                getCustomFieldRowValues(rowColumns, thisThread.getSharedContext().getAllCustomFields(), errorMsg,
                        thisThread.getSharedContext().getCustomFieldsWithIndicesInFileMap());

                //checkMissingUserName(rowColumns, errorMsg);
                setOfOrgGroups=new HashSet<OrganizationalGroup>();
                /**
					  LMS-6774
		              existing user was previously updated correctly but when one record is inserted if that record is
		              duplicate with any other record the data will be inserted twice because getExistingUserList is not updated
		              as the first record is inserted
                **/

                existingUser = getExistingUser(thisThread.getSharedContext().getExistingUserList(), rowColumns);
                VU360User loggedInUser = vu360UserService.findByIdForBatchImport(thisThread.getSharedContext().getLoggedInUser().getId());

                if (isValidRecord(thisThread.getSharedContext(), rowColumns, errorsInRecord, setOfOrgGroups, loggedInUser))
                {
                    setOfLearnerGroups = checkMissingLearnerGroupsAndReturnLearnerGroups(rowColumns, thisThread.getSharedContext());

                    //Looking up NERC field to determine if NERC field needs to be handeled.
                    CreditReportingField NERCField = thisThread.getSharedContext().getNERCField();

//                    boolean courseWithNERCFieldExists = NERCBatchImportUtil.courseWithNERCFieldExists(
//                        setOfLearnerGroups, NERCField, accreditationService);
                    boolean NERCFieldRequired = thisThread.getSharedContext().isNERCFieldRequired();
                    int NERCFieldIndex = thisThread.getSharedContext().getNERCFieldIndex();
                    boolean isSourceNERCCreditReportingValid = false;

//                    if(NERCFieldRequired && NERCFieldIndex > 0 && courseWithNERCFieldExists) {
                    if(NERCFieldRequired && NERCFieldIndex !=-1 ) {
                        //Validate the NERC field value from the imported source data.
                        isSourceNERCCreditReportingValid = NERCBatchImportUtil.isSourceNERCCreditReportingFieldValueValid(
                            NERCField, rowColumns, NERCFieldIndex);
                        log.debug("is NERC source data value valid? " + isSourceNERCCreditReportingValid);
                    }

	                logDebug("existingUser = " + existingUser + ", actionOnDuplicateRecords = " +
	                        thisThread.getSharedContext().getActionOnDuplicateRecords());

	                if(existingUser != null)
	                {
                		// load update-able learner from org group
                    	VU360User updateableUser = vu360UserService.loadUserForBatchImport(existingUser.getId());
                    	updateableLearner = updateableUser.getLearner();//orgGroupService.findLearnerInOrgGroupHierarchy(thisThread.getSharedContext().getRootOrganizationalGroup(), existingUser.getLearner());
                        learner = updateableLearner;

                        if( isSourceNERCCreditReportingValid ){
                            log.debug("Importing data row with NERC credit reporting field.");
                            updateUser(updateableUser,
                                errorMsg,
                                thisThread.getSharedContext(),
                                setOfOrgGroups,
                                setOfLearnerGroups,
                                NERCField,
                                NERCFieldIndex,
                                rowColumns);
                        }else{
                            log.debug("Importing data row without NERC credit reporting field.");
                            updateUser(updateableUser, errorMsg, thisThread.getSharedContext(), setOfOrgGroups, setOfLearnerGroups, rowColumns);
                        }

                        synchronized (thisThread.getSharedContext().getUpdatedLearners())
                        {
                            thisThread.getSharedContext().setUpdatedLearners(
                                    thisThread.getSharedContext().getUpdatedLearners() + 1);
                        }
                        thisThread.getSharedContext().getNumOfupdatedLearners().add(errorMsg);
                        logDebug(" ##### UPDATED => " + updateableLearner.getVu360User().getFirstName());
	                }
	                else
	                {
                            if( isSourceNERCCreditReportingValid ){
                                log.debug("Importing data row with NERC credit reporting field.");
                                newUser = addUser(rowColumns,
                                    setOfOrgGroups,
                                    setOfLearnerGroups,
                                    thisThread.getSharedContext(),
                                    NERCField,
                                    NERCFieldIndex,
                                    errorMsg);
                            }else{
                                log.debug("Importing data row without NERC credit reporting field.");
                                newUser = addUser(rowColumns, setOfOrgGroups, setOfLearnerGroups,thisThread.getSharedContext(),errorMsg);
                            }

	                    thisThread.getSharedContext().getExistingUserList().add(newUser);
	                    //synchronized (thisThread.getSharedContext().getNumberOfImportedUsers())
	                    //{
	                        thisThread.getSharedContext().setNumberOfImportedUsers(
	                                thisThread.getSharedContext().getNumberOfImportedUsers() + 1);
	                    //}
	                    thisThread.getSharedContext().getAddedLearners().add(newUser.getLearner());
	                    logDebug(" ##### ADDED => " + newUser.getFirstName());
	                }
                }
                else {
                	synchronized (thisThread.getSharedContext().getBatchImportErrors()) {
                		errorsInRecord.setRecordNumber(recordNumber);
                    	thisThread.getSharedContext().getBatchImportErrors().add(errorsInRecord);
                    }
                }

                logDebug("Added : " + thisThread.getSharedContext().getNumberOfImportedUsers() + ", " +
                        "Updated : " + thisThread.getSharedContext().getUpdatedLearners());

                //logDebug(">>> ERROR MESSAGE = " + errorMsg);
                //thisThread.getSharedContext().getErrorMessages().add(errorMsg);
            }
        }
        catch(Exception e)
        {
            log.error(e, e);
            throw new OptimizedBatchImportLearnersException(e.toString(), e);
        }
        finally
        {
            logMethod("run (" + Thread.currentThread().getName() + ")", false);
        }
    }

    private boolean isValidRecord(OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
    		String[] rowColumns, OptimizedBatchImportLearnersErrors errorsInRecord,
    		Set<OrganizationalGroup> setOfOrgGroups, VU360User existingUser){
    	//existingUser = getExistingUser(sharedContext.getExistingUserList(), rowColumns);
    	if (existingUser != null) {
	    	/*LMS-8875: can't batch import learners when login in via admin mode.*/
    		boolean currentCustomerUser = isCurrentCustomerUser(existingUser,rowColumns,sharedContext);
	    	errorsInRecord.setCurrentCustomerUser(currentCustomerUser);
	    	errorsInRecord.setCountry(rowColumns[COL_COUNTRY]);
	    	errorsInRecord.setZip(rowColumns[COL_ZIPCODE]);
	    	errorsInRecord.setState(rowColumns[COL_STATE]);
	    	errorsInRecord.setFirstName(rowColumns[COL_FIRST_NAME]);
	    	errorsInRecord.setLastName(rowColumns[COL_LAST_NAME]);
	    	errorsInRecord.setEmailAddress(rowColumns[COL_EMAIL]);
	    	errorsInRecord.setUserName(rowColumns[COL_USERNAME]);

	    	if(((sharedContext.getActionOnDuplicateRecords() != null &&
	                !sharedContext.getActionOnDuplicateRecords().equalsIgnoreCase("update"))
	                || sharedContext.getActionOnDuplicateRecords() == null)){
	    		errorsInRecord.setUserAlreadyExists(true);
	    		return false;
	    	}

	    	else if (sharedContext.getActionOnDuplicateRecords() != null &&
	                sharedContext.getActionOnDuplicateRecords().equalsIgnoreCase("update")) {
	    		if(!currentCustomerUser) {
	    			errorsInRecord.setUserAlreadyExists(true);
	    			return false;
	    		}
	    	}
    	}

    	Map <Object, Object> errorMsg = new HashMap<Object, Object>();
    	validateAddress(rowColumns, sharedContext.getBrander(), errorMsg);
		if (errorMsg.get("invalidZip") != null) {
			String invalid = (String)errorMsg.get("invalidZip");

			if (invalid.equals("invalidZip")){
				errorsInRecord.setInvalidZip(true);
			}
			else if (invalid.equals("invalidCountry")){
				errorsInRecord.setInvalidCountry(true);
			}
			else if (invalid.equals("invalidState")){
				errorsInRecord.setInvalidState(true);
			}
			return false;
		}

        setOfOrgGroups.addAll(checkMissingOrgGroupsAndReturnOrgGroups(errorMsg, rowColumns,sharedContext,existingUser));

        if(setOfOrgGroups.isEmpty()) {
        	errorsInRecord.setOrgGroupMissing(true);
        	return false;
        }

        if (activeDirectoryService.findADUser(rowColumns[COL_USERNAME]) && vu360UserService.findUserByUserName(rowColumns[COL_USERNAME]) == null){
    		errorsInRecord.setUserAlreadyExistsInAD(true);
    		return false;
    	}

        return true;
    }

    private boolean isCurrentCustomerUser(VU360User existingUser, String[] rowColumns,
    		OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext){

        //long fileCustomerId = existingUser.getLearner().getCustomer().getId();
       //long currentCustomerId = sharedContext.getCurrentCustomer().getId();

    	//Added by Faisal.Pathan for ticket ENGSUP-32354
    	if( rowColumns!=null && rowColumns[COL_USERNAME]!=null && !rowColumns[COL_USERNAME].isEmpty()){
	    	List<VU360User> existingUserList =sharedContext.getExistingUserList();
			if (existingUserList != null && !existingUserList.isEmpty()) {
				for (VU360User user : existingUserList) {
				    user = vu360UserService.findByIdForBatchImport(user.getId());
					if (user!=null && user.getUsername()!=null && user.getUsername().equals(rowColumns[COL_USERNAME])) {
						if (!isCustomersSame(existingUser, user)) {
							logDebug("USERNAME ALREADY EXISTS ON ANOTHER CUSTOMER");
							return false;
						}
					}
				}
			}
			return true;
    	}
    	logDebug("ERROR: NO DATA PRESENT ON CURRENT USER");
        return false;
    }



    /**
     * <p>To validate if learner's customer is the same as the customer of the logged in user during Batch import.</p>
     * @author faisal.pathan
     * @param existingUser
     * @param sharedContext
     * @return boolean
     * @RelatedTickets ENGSUP-32354
     */
	private boolean isCustomersSame(VU360User existingUser,VU360User user) {

		if (existingUser != null && user!=null) {

			/**
			 *

			Customer loggedInUserCustomer = null;
			Customer loggedInUserLearnerCustomer = null;
			Customer userCustomer = null;

			LMSRole loggedInUserLmsRole = existingUser.getLogInAsManagerRole();
			Learner loggedInUserLearner = existingUser.getLearner();
			Learner userLearner = user.getLearner();

			if (loggedInUserLmsRole != null) {
				loggedInUserCustomer = loggedInUserLmsRole.getOwner();
			}

			if (loggedInUserLearner != null) {
				loggedInUserLearnerCustomer = loggedInUserLearner.getCustomer();
			}

			if (userLearner != null) {
				userCustomer = userLearner.getCustomer();
			}

			if (userCustomer != null) {
				//If an Administrator logged in to customer's Manager mode uses the batch import
				if (loggedInUserCustomer != null) {
					if (!(loggedInUserCustomer.getId().equals(userCustomer.getId()))) {
						return false;
					}

				//If customer is using batch import through its own Manager mode
				}else if (loggedInUserLearnerCustomer !=null){
					if (!(loggedInUserLearnerCustomer.getId().equals(userCustomer.getId())))
						return false;
				}
			}

			 */

			//If an Administrator logged in to customer's Manager mode uses the batch import
			if (existingUser.getLogInAsManagerRole() != null && existingUser.getLogInAsManagerRole().getOwner() != null) {

				// Fix by technical services
				if (!(existingUser.getLogInAsManagerRole().getOwner().getId().equals(user.getLearner().getCustomer().getId())))

				//if (existingUser.getLogInAsManagerRole().getOwner().getId() != user.getLearner().getCustomer().getId())
					return false;
			//If customer is using batch import through its own Manager mode
			}else if (existingUser.getLearner()!=null && existingUser.getLearner().getCustomer()!=null){
				 // Fix by technical services
				if (!(existingUser.getLearner().getCustomer().getId().equals(user.getLearner().getCustomer().getId())))
					//if (existingUser.getLearner().getCustomer().getId() != user.getLearner().getCustomer().getId())
					return false;
			}

		}
		return true;
	}

    private void assignTrainingManager(VU360User updateableUser,String[] rowColumns,
    	    OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
    		LMSRole managerRole
    ){

        StringTokenizer managedOrgGroupTokenizer = new StringTokenizer(rowColumns[sharedContext.getManagerOrgGroupIndex()],
                ":");
        String orgGroupHierarchy;
        List<OrganizationalGroup> orgGroups2Manage = new ArrayList<OrganizationalGroup>();
        Map<String,OrganizationalGroup> allOrgGroups = sharedContext.getAllOrganizationalGroups();
        while( managedOrgGroupTokenizer.hasMoreTokens() )
        {
            // getting the organizational group path
            orgGroupHierarchy = managedOrgGroupTokenizer.nextToken().trim();
            OrganizationalGroup organizationalGroup = allOrgGroups.get(orgGroupHierarchy.toUpperCase());
            orgGroups2Manage.add(organizationalGroup);
        }
//        if(!updateableUser.getLmsRoles().contains(managerRole))//safety check
        	updateableUser.addLmsRole(managerRole);

        TrainingAdministrator trainingAdministrator = updateableUser.getTrainingAdministrator();
        if(trainingAdministrator == null)
        {
			trainingAdministrator = new TrainingAdministrator();
			trainingAdministrator.setCustomer(updateableUser.getLearner().getCustomer());
			trainingAdministrator.setManagesAllOrganizationalGroups(false);//through batch import we only allow management on certain groups not all
			trainingAdministrator.setVu360User(updateableUser);
			updateableUser.setTrainingAdministrator(trainingAdministrator);
		}
		trainingAdministrator.setManagedGroups(orgGroups2Manage);
    }
    private void removeTrainingManager(VU360User user){
		TrainingAdministrator trainingAdministrator = user.getTrainingAdministrator();
		trainingAdministrator.setVu360User(null);
		trainingAdministrator.setCustomer(null);
		vu360UserService.deleteLMSTrainingAdministrator(trainingAdministrator);
		// set training manager null
		user.setTrainingAdministrator(null);

		//delete manager role from this user

		Set<LMSRole> roles = user.getLmsRoles();
    	Set<LMSRole> roles2Remove = new HashSet<LMSRole>();
    	for(LMSRole role : roles){
    		if(role.getRoleType().equals(LMSRole.ROLE_TRAININGMANAGER)){
    			roles2Remove.add(role);
    		}
    	}
    	if(CollectionUtils.isNotEmpty(roles2Remove)){
    		roles.removeAll(roles2Remove);
    	}
    	user.setLmsRoles(roles);
//    	TrainingAdministrator trainingAdministrator = vu360UserService.getUserById(learner.getVu360User().getId()).getTrainingAdministrator();
//        if(trainingAdministrator!=null){
//       	 learner.getVu360User().setTrainingAdministrator(trainingAdministrator);
//       	 learnerService.deleteLearnerFromRole(learner);
//        }

    }
    private void setUserProperties (
            VU360User newUser,
            String[] rowColumns,
            boolean accountLocked,
            boolean accountVisible,
            int accountLockedIndex,
            int accountVisibleOnReportIndex,
            boolean changePasswordOnLogin  // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
    )
    {
        newUser.setFirstName(rowColumns[COL_FIRST_NAME]);
        newUser.setMiddleName(rowColumns[COL_MIDDLE_NAME]);
        newUser.setLastName(rowColumns[COL_LAST_NAME]);
        newUser.setLmsAdministrator(null);
        newUser.setTrainingAdministrator(null);
        newUser.setExpirationDate(null);
        newUser.setEmailAddress(rowColumns[COL_EMAIL]);
        newUser.setPassword(rowColumns[COL_PASSWORD]);
        newUser.setUsername(rowColumns[COL_USERNAME]);
        // checking if user account is locked & provided in batch import file
        setAccountLocked(newUser, accountLockedIndex, rowColumns, accountLocked);
        // checking if user account is visible on reports
        setAccountVisibleOnReport(newUser, accountVisibleOnReportIndex, rowColumns, accountVisible);

        newUser.setAcceptedEULA(false);
        newUser.setNewUser(true);
        newUser.setChangePasswordOnLogin( changePasswordOnLogin );  // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
        newUser.setCredentialsNonExpired(true);
        newUser.setEnabled(true);
    }
    private void setUserPropertiesForExistingRecord (
            VU360User user,
            String[] rowColumns,
            boolean accountLocked,
            boolean accountVisible,
            int accountLockedIndex,
            int accountVisibleOnReportIndex,
            boolean changePasswordOnLogin  	// [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
    )
    {
    	/*
    	 *  Update vu360user properties by the values of batch file
    	 */
    	user.setFirstName(rowColumns[COL_FIRST_NAME]);
    	user.setMiddleName(rowColumns[COL_MIDDLE_NAME]);
    	user.setLastName(rowColumns[COL_LAST_NAME]);
    	user.setEmailAddress(rowColumns[COL_EMAIL]);
    	user.setPassword(rowColumns[COL_PASSWORD]);
    	user.setUsername(rowColumns[COL_USERNAME]);
        // checking if user account is locked & provided in batch import file
        setAccountLocked(user, accountLockedIndex, rowColumns, accountLocked);
        // checking if user account is visible on reports
        setAccountVisibleOnReport(user, accountVisibleOnReportIndex, rowColumns, accountVisible);

        /*
         * Update LearnerProfile properties by the values of batch file
         */
        LearnerProfile profile = user.getLearner().getLearnerProfile();
        profile=learnerService.loadForUpdateLearnerProfile(profile.getId());
        profile.setOfficePhone(rowColumns[COL_OFFICE_PHONE]);
        profile.setOfficePhoneExtn(rowColumns[COL_OFFICE_PHONE_EXT]);
        profile.setMobilePhone(rowColumns[COL_MOBILE_PHONE]);
        Address a1 = profile.getLearnerAddress();
        //this logic is for fail safe
        if(a1==null){
        	a1 = new Address();
        	profile.setLearnerAddress(a1);
        }
        a1.setStreetAddress(rowColumns[COL_STREET_ADDRESS]);
        a1.setStreetAddress2(rowColumns[COL_STREET_ADDRESS2]);
        a1.setCity(rowColumns[COL_CITY]);
        a1.setState(rowColumns[COL_STATE]);
        a1.setZipcode(rowColumns[COL_ZIPCODE]);
        a1.setCountry(rowColumns[COL_COUNTRY]);

        user.getLearner().setLearnerProfile(profile);

    }
    private void setAccountLocked(VU360User user, int accountLockedIndex, String[] rowColumns,boolean accountLocked){
        if(accountLockedIndex!=-1 && rowColumns.length > accountLockedIndex && StringUtils.isNotBlank(rowColumns[accountLockedIndex]))
        {
            logDebug("Account Locked = " + rowColumns[accountLockedIndex]);
            if(rowColumns[accountLockedIndex].trim().equalsIgnoreCase("true") || rowColumns[accountLockedIndex].trim().equalsIgnoreCase("t") || rowColumns[accountLockedIndex].trim().equalsIgnoreCase("yes") || rowColumns[accountLockedIndex].trim().equalsIgnoreCase("y") || rowColumns[accountLockedIndex].trim().equalsIgnoreCase("1")){
            	accountLocked = true;//override value passed in argument
            }else if(rowColumns[accountLockedIndex].trim().equalsIgnoreCase("false") || rowColumns[accountLockedIndex].trim().equalsIgnoreCase("f")  || rowColumns[accountLockedIndex].trim().equalsIgnoreCase("no") || rowColumns[accountLockedIndex].trim().equalsIgnoreCase("n") || rowColumns[accountLockedIndex].trim().equalsIgnoreCase("0")){
            	accountLocked = false;//override value passed in argument
            }else{
            	log.error("Invalid value specified for Account Lockeed = "+ rowColumns[accountLockedIndex]);
            }
            logDebug("Account Locked = " + accountLocked);
        }
        user.setAccountNonLocked(!accountLocked);
    }
    private void setAccountVisibleOnReport(VU360User user, int accountVisibleOnReportIndex, String[] rowColumns,boolean accountVisible){
        if(accountVisibleOnReportIndex!=-1 && rowColumns.length > accountVisibleOnReportIndex && StringUtils.isNotBlank(rowColumns[accountVisibleOnReportIndex]))
        {
            logDebug("Account Visible = " + rowColumns[COL_ACCOUNT_VISIBLE_ON_REPORTS]);
            if(rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("true") || rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("t") || rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("yes") || rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("y") || rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("1")){
            	accountVisible = true;//override value pass in argument
            }else if(rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("false") || rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("f") || rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("no") || rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("n") || rowColumns[accountVisibleOnReportIndex].trim().equalsIgnoreCase("0")){
            	accountVisible = false;//override value pass in argument
            }
            else{
            	log.error("Invalid value specified for Account Visible = "+ rowColumns[accountVisibleOnReportIndex]);
            }
            logDebug("Account Visible = " + accountVisible);
        }
        user.setVissibleOnReport(accountVisible);
    }
    private void checkInvalidRecordAndFlagErrorMessages (
            Map<String, Boolean> isValidRecord,
            Map<Object, Object> errorMsg,
            int recordNumber,
            String[] rowColumns,
            List<CustomField> allCustomFields,
            Brander brander,
            OptimizedBatchImportLearnersErrors batchImportError,
            CreditReportingField nercField,
            int nercFieldColumnIndex
    )
    {
    	batchImportError.setRecordNumber(recordNumber);
        if(isValidRecord.get("invalidRowNumber"))
        {
            batchImportError.setInvalidFormat(true);
        }
        else
        {
            final String blank = "blank";
            if(rowColumns.length < (FIX_FIELD_SIZE - 1)) {
                batchImportError.setOrgGroupMissing(true);
            }
            if( isValidRecord.get("firstNameBlank") ) {
                batchImportError.setFirstNameBlank(true);
            }
            if( isValidRecord.get("lastNameBlank") ) {
                batchImportError.setLastNameBlank(true);
            }
            if( isValidRecord.get("emailAddressBlank") ) {
                batchImportError.setEmailAddressBlank(true);
            }
            if( isValidRecord.get("passwordBlank") ) {
                errorMsg.put("password", blank);
                batchImportError.setPasswordBlank(true);
            }
            if( isValidRecord.get("shortPassword") ) {
                batchImportError.setShortPassword(true);
            }
            if( isValidRecord.get("missingCountry") ) {
                errorMsg.put("missingCountry", blank);
                batchImportError.setCountryBlank(true);
            }
            if( isValidRecord.get("missingOrgGroup") ) {
                batchImportError.setOrgGroupMissing(true);
            }
            /*if( isValidRecord.get("missingLearnerGroup") ) {
                errorMsg.put("missingLearnerGroup", blank);
            }*/
            if( isValidRecord.get("userNameBlank") ) {
                batchImportError.setUserNameBlank(true);
            }
            if( isValidRecord.get("invalidEmailAddress") ) {
                batchImportError.setInvalidEmailAddress(true);
            }

            /*Setting up values */
            	batchImportError.setFirstName(errorMsg.get("firstName").toString());
            	batchImportError.setLastName(errorMsg.get("lastName").toString());
            	batchImportError.setUserName(errorMsg.get("userName").toString());
            	batchImportError.setEmailAddress(errorMsg.get("emailAddress").toString());
            	batchImportError.setCountry(errorMsg.get("country").toString());
            	batchImportError.setState(errorMsg.get("state").toString());
            	batchImportError.setZip(errorMsg.get("zip").toString());


            /* End setting up values */

            if(allCustomFields != null)
                for(CustomField cf : allCustomFields )
                {
                    if( isValidRecord.get( cf.getFieldLabel()+"-required") != null &&
                            isValidRecord.get( cf.getFieldLabel()) )
                    	batchImportError.getCustomFields().put(cf.getFieldLabel(),
                    			cf.getFieldLabel() + " : " +  brander.getBrandElement("lms.batchImportUsers.ValueRequired"));
                    else if(cf instanceof SSNCustomFiled && isValidRecord.get(cf.getFieldLabel()+"-ssnError") != null)
                    	batchImportError.getCustomFields().put(cf.getFieldLabel(),
                    			cf.getFieldLabel() + " : " +brander.getBrandElement("lms.batchImportUsers.SSNRequired"));
                    else if(cf instanceof DateTimeCustomField &&
                            isValidRecord.get(cf.getFieldLabel()+"-dateError") != null)
                    	batchImportError.getCustomFields().put(cf.getFieldLabel(),
                    			cf.getFieldLabel() + " : " +brander.getBrandElement("lms.batchImportUsers.DateRequired") );
                    else if(cf instanceof NumericCusomField &&
                            isValidRecord.get(  cf.getFieldLabel()+"-numericError") != null )
                    	batchImportError.getCustomFields().put(cf.getFieldLabel(),
                    			cf.getFieldLabel() + " : " +brander.getBrandElement("lms.batchImportUsers.NumericRequired"));
                    else if(cf instanceof SingleSelectCustomField &&
                            isValidRecord.get(  cf.getFieldLabel()+"-textError")  != null )
                    	batchImportError.getCustomFields().put(cf.getFieldLabel(),
                    			cf.getFieldLabel() + " : " +brander.getBrandElement("lms.batchImportUsers.SingleSelectRequired"));
                    else if(cf instanceof MultiSelectCustomField &&
                            isValidRecord.get( cf.getFieldLabel()+"-multiSelectError")  != null )
                    	batchImportError.getCustomFields().put(cf.getFieldLabel(),
                    			cf.getFieldLabel() + " : " +brander.getBrandElement("lms.batchImportUsers.MultiSelectRequired"));
                }

            if(nercFieldColumnIndex>0 && nercField !=null){
            	if(nercField instanceof TelephoneNumberCreditReportingField){
            		if(isValidRecord.get(nercField.getFieldLabel()+"-telephoneNumberRequiredError") !=null){
            			batchImportError.getReportingFields().put(nercField.getFieldLabel(),
                    			nercField.getFieldLabel() + " : " +brander.getBrandElement("lms.batchImportUsers.TelephoneNumberRequired"));
            		}
            		else if(isValidRecord.get(nercField.getFieldLabel()+"-telephoneNumberValidationError") !=null){
            			batchImportError.getReportingFields().put(nercField.getFieldLabel(),
                    			nercField.getFieldLabel() + " : " +brander.getBrandElement("lms.batchImportUsers.TelephoneNumberInvalid"));
            		}
            	}
            }
        }
    }

    private VU360User addUser (
            String[] rowColumns,
            Set<OrganizationalGroup> setOfOrgGroups,
            Set<LearnerGroup> setOfLearnerGroups,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
            Map<Object, Object> errorMsg
    )
    {
        try
        {
            VU360User newUser = new VU360User();
            //setUserLmsRoles(newUser, thisThread.getSharedContext().getCurrentCustomer());// no need to fetch learner role for individual learners
            newUser.setUserGUID(GUIDGeneratorUtil.generateGUID());
            Learner learner = new Learner();
            learner.setCustomer(sharedContext.getCurrentCustomer());
            learner.setVu360User(newUser);
            newUser.setLearner(learner);

            // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
            setUserProperties(newUser, rowColumns, sharedContext.isAccountLocked(),
            		sharedContext.isAccountVisible(),sharedContext.getAccountLockedIndex(),sharedContext.getAccountVisibleOnReportIndex(), sharedContext.isChangePasswordOnLogin());

            LearnerProfile learnerProfile = new LearnerProfile();
            learnerProfile.setLearner(learner);
            learner.setLearnerProfile(learnerProfile);
            Address[] addresses = validateAndReturnAddresses(rowColumns, sharedContext.getBrander(),errorMsg);
            setLearnerAndLearnerProfileProperties(learner, learnerProfile, rowColumns,
            		sharedContext.getCustomFieldsWithIndicesInFileMap(), addresses,
            		sharedContext.getCurrentCustomer());

            /**
             *
             INT-3460
            newUser.addLmsRole(sharedContext.getDefaultLearnerRole());
            // Manage Org Group Functionality
            if(sharedContext.getManagerOrgGroupIndex()!=-1 && rowColumns.length > sharedContext.getManagerOrgGroupIndex())
            {
                if(StringUtils.isNotBlank(rowColumns[sharedContext.getManagerOrgGroupIndex()]))
                {
                	assignTrainingManager(newUser, rowColumns, sharedContext, sharedContext.getDefaultManagerRole());
                }
            }
            */

            int securityRoleColumnIndex = sharedContext.getSecurityRoleColumnIndex();
            if(securityRoleColumnIndex > 0 ) {   // Security Role column exist
            	  try {
                  	boolean securityRoleResult =  processSecurityRoles(newUser, sharedContext, rowColumns);
                      logDebug("Security Role Result: " + securityRoleResult);
                  } catch (Exception e) {
                  	String errorMessage = e.getMessage();
                  	logDebug("Error in assigning security Role: " + errorMessage);
                  }
            }

            /**
             * If no role is present in Security Role column while creating new user then default learner role is
             * assigned to user. If no role is present in Security Role column then we get lmsRoles set null or empty.
             */
            Set<LMSRole> lmsRoles = newUser.getLmsRoles();
            if (CollectionUtils.isEmpty(lmsRoles) || !isRoleTypeExist(lmsRoles, LMSRole.ROLE_LEARNER)) {
            	newUser.addLmsRole(sharedContext.getDefaultLearnerRole());
            }

            //add this password to list. so that it can be used later for sending email
            sharedContext.getPasswords().add(newUser.getPassword());
            logDebug("adding learner...");
            learner = learnerService.addLearner(learner);
            logDebug("learner added.");

            this.updateAssociationOfOrgGroupsAndLearnerGroups(learner, setOfOrgGroups, setOfLearnerGroups,sharedContext, true);
            logMethod("addUser", true);

            // For Enrolling Learner into Courses
            // List<Learner> learners = new ArrayList<Learner>();
            // learners.add(learner);
            // enrollLearnersToLearnerGroupCourses(learners,setOfLearnerGroups, sharedContext.getBrander(), sharedContext.getLoggedInUser(),sharedContext.getCurrentCustomer(),sharedContext.getVelocityEngine());
			sharedContext.updateLearnerGroupMap(learner, setOfLearnerGroups);
			return learner.getVu360User();
        }
        finally
        {
            logMethod("addUser", false);
        }
    }

    private VU360User addUser (
            String[] rowColumns,
            Set<OrganizationalGroup> setOfOrgGroups,
            Set<LearnerGroup> setOfLearnerGroups,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
            CreditReportingField NERCField,
            int NERCFieldIndex,
            Map<Object, Object> errorMsg
    )
    {
        try
        {
            VU360User newUser = new VU360User();
            //setUserLmsRoles(newUser, thisThread.getSharedContext().getCurrentCustomer());// no need to fetch learner role for individual learners
            newUser.setUserGUID(GUIDGeneratorUtil.generateGUID());
            Learner learner = new Learner();
            learner.setCustomer(sharedContext.getCurrentCustomer());
            learner.setVu360User(newUser);
            newUser.setLearner(learner);

            // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
            setUserProperties(newUser, rowColumns, sharedContext.isAccountLocked(),
            		sharedContext.isAccountVisible(),sharedContext.getAccountLockedIndex(),sharedContext.getAccountVisibleOnReportIndex(), sharedContext.isChangePasswordOnLogin());

            LearnerProfile learnerProfile = new LearnerProfile();
            learnerProfile.setLearner(learner);
            learner.setLearnerProfile(learnerProfile);
            Address[] addresses = validateAndReturnAddresses(rowColumns, sharedContext.getBrander(),errorMsg);
            setLearnerAndLearnerProfileProperties(learner, learnerProfile, rowColumns,
            		sharedContext.getCustomFieldsWithIndicesInFileMap(), addresses,
            		sharedContext.getCurrentCustomer());

            /**
             *
             INT-3460
            newUser.addLmsRole(sharedContext.getDefaultLearnerRole());
            // Manage Org Group Functionality
            if(sharedContext.getManagerOrgGroupIndex()!=-1 && rowColumns.length > sharedContext.getManagerOrgGroupIndex())
            {
                if(StringUtils.isNotBlank(rowColumns[sharedContext.getManagerOrgGroupIndex()]))
                {
                	assignTrainingManager(newUser, rowColumns, sharedContext, sharedContext.getDefaultManagerRole());
                }
            }
            */

            int securityRoleColumnIndex = sharedContext.getSecurityRoleColumnIndex();
            if(securityRoleColumnIndex > 0 ) {   // Security Role column exist
            	  try {
                  	boolean securityRoleResult =  processSecurityRoles(newUser, sharedContext, rowColumns);
                      logDebug("Security Role Result: " + securityRoleResult);
                  } catch (Exception e) {
                  	String errorMessage = e.getMessage();
                  	logDebug("Error in assigning security Role: " + errorMessage);
                  }
            }

            Set<LMSRole> lmsRoles = newUser.getLmsRoles();
            if (CollectionUtils.isEmpty(lmsRoles) || !isRoleTypeExist(lmsRoles, LMSRole.ROLE_LEARNER)) {
            	newUser.addLmsRole(sharedContext.getDefaultLearnerRole());
            }

            //add this password to list. so that it can be used later for sending email
            sharedContext.getPasswords().add(newUser.getPassword());
            logDebug("adding learner...");
            learner = learnerService.addLearner(learner);
            logDebug("learner added.");

            this.updateAssociationOfOrgGroupsAndLearnerGroups(learner, setOfOrgGroups, setOfLearnerGroups,sharedContext, true);
            logMethod("addUser", true);

            if(NERCFieldIndex > -1) {
            	String NERCValue = rowColumns[NERCFieldIndex];

                NERCBatchImportUtil.saveNERCFieldValue(learnerProfile, NERCField, NERCValue, learnerService);
            }

            sharedContext.updateLearnerGroupMap(learner, setOfLearnerGroups);

            // For Enrolling Learner into Courses
            // List<Learner> learners = new ArrayList<Learner>();
            // learners.add(learner);
            // enrollLearnersToLearnerGroupCourses(learners,setOfLearnerGroups, sharedContext.getBrander(), sharedContext.getLoggedInUser(),sharedContext.getCurrentCustomer(),sharedContext.getVelocityEngine());

			return learner.getVu360User();
        }
        finally
        {
            logMethod("addUser", false);
        }
    }

    private void updateUser (
            VU360User updateableUser,
            Map<Object, Object> errorMsg,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
            Set<OrganizationalGroup> setOfOrgGroups,
            Set<LearnerGroup> setOfLearnerGroups,
            String[] rowColumns
    )
    {
        try
        {
            logMethod("updateUser", true);
            errorMsg.put("updateLearner", "true");

            // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
        	// update user, learner profile, address properties from batch file values
        	setUserPropertiesForExistingRecord(updateableUser, rowColumns, sharedContext.isAccountLocked(),
        			sharedContext.isAccountVisible(),sharedContext.getAccountLockedIndex(),sharedContext.getAccountVisibleOnReportIndex(), sharedContext.isChangePasswordOnLogin());
        	// update custom field values in learner profile
        	setCustomFieldValuesInLearnerProfile(rowColumns, sharedContext.getCustomFieldsWithIndicesInFileMap(), updateableUser.getLearner().getLearnerProfile());

            this.setAccountLocked(updateableUser, sharedContext.getAccountLockedIndex(), rowColumns, sharedContext.isAccountLocked());
            this.setAccountVisibleOnReport(updateableUser, sharedContext.getAccountVisibleOnReportIndex(), rowColumns, sharedContext.isAccountVisible());
            updateableUser.setPassWordChanged(true);

            // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
            updateableUser.setChangePasswordOnLogin( sharedContext.isChangePasswordOnLogin() );

            //Update Profile before Remove Training Manger since it generate exception after removal.
            learnerService.updateLearnerProfile(updateableUser.getLearner().getLearnerProfile());

            /**
             *
             INT-3460
            if(sharedContext.getManagerOrgGroupIndex()!=-1 && rowColumns.length > sharedContext.getManagerOrgGroupIndex())
            {
                if(StringUtils.isNotBlank(rowColumns[sharedContext.getManagerOrgGroupIndex()]))
                {
                	assignTrainingManager(updateableUser, rowColumns, sharedContext, sharedContext.getDefaultManagerRole());
                }else{ //i.e. Manage Org Group Cell value is blank. Demote previous managers
                	if(updateableUser.getTrainingAdministrator()!=null){
                		// this means.. this is existing learner and has got administrator .. really need demotion :)
                		removeTrainingManager(updateableUser);
                	}
                }
            }
            */

            int securityRoleColumnIndex = sharedContext.getSecurityRoleColumnIndex();
            if(securityRoleColumnIndex > 0 ) {   // Security Role column exist
            	try {
                  	boolean securityRoleResult =  processSecurityRoles(updateableUser, sharedContext, rowColumns);
                      logDebug("Security Role Result: " + securityRoleResult);
                  } catch (Exception e) {
                  	String errorMessage = e.getMessage();
                  	logDebug("Error in assigning security Role: " + errorMessage);
                  }
            }

            logDebug("updating learner having id = "+updateableUser.getLearner().getId()+" profile id = "+
            		updateableUser.getLearner().getLearnerProfile().getId() +" address id = "+
            		updateableUser.getLearner().getLearnerProfile().getLearnerAddress().getId()+" user id = "+updateableUser.getId());

            VU360User updatedUser = learnerService.updateUserFromBatchFile(updateableUser);


            //learnerService.updateLearnerProfile(updateableUser.getLearner().getLearnerProfile());
            Learner updatedLearner = updatedUser.getLearner();
            logDebug("learner updated.");

            /*this.updateAssociationOfOrgGroupsAndLearnerGroups(updateableUser.getLearner(), setOfOrgGroups, setOfLearnerGroups,
                    sharedContext, false);*/
            this.updateAssociationOfOrgGroupsAndLearnerGroups(updatedLearner, setOfOrgGroups, setOfLearnerGroups,
                    sharedContext, false);



            //For Enrolling Learner into Courses
//            List<Learner> learners = new ArrayList<Learner>();
//			learners.add(updateableUser.getLearner());
//			enrollLearnersToLearnerGroupCourses(learners,setOfLearnerGroups, sharedContext.getBrander(), sharedContext.getLoggedInUser(),sharedContext.getCurrentCustomer(),sharedContext.getVelocityEngine());
			sharedContext.updateLearnerGroupMap(updatedLearner, setOfLearnerGroups);
        }
        finally
        {
            logMethod("updateUser", false);
        }
    }

    private void updateUser (
            VU360User updateableUser,
            Map<Object, Object> errorMsg,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
            Set<OrganizationalGroup> setOfOrgGroups,
            Set<LearnerGroup> setOfLearnerGroups,
            CreditReportingField NERCCreditReportingField,
            int NERCColumnIndex,
            String[] rowColumns
    )
    {
        try
        {
            logMethod("updateUser", true);
            errorMsg.put("updateLearner", "true");

            // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
        	// update user, learner profile, address properties from batch file values
        	setUserPropertiesForExistingRecord(updateableUser, rowColumns, sharedContext.isAccountLocked(),
        			sharedContext.isAccountVisible(),sharedContext.getAccountLockedIndex(),sharedContext.getAccountVisibleOnReportIndex(), sharedContext.isChangePasswordOnLogin());
        	// update custom field values in learner profile
        	setCustomFieldValuesInLearnerProfile(rowColumns, sharedContext.getCustomFieldsWithIndicesInFileMap(), updateableUser.getLearner().getLearnerProfile());

            this.setAccountLocked(updateableUser, sharedContext.getAccountLockedIndex(), rowColumns, sharedContext.isAccountLocked());
            this.setAccountVisibleOnReport(updateableUser, sharedContext.getAccountVisibleOnReportIndex(), rowColumns, sharedContext.isAccountVisible());
            updateableUser.setPassWordChanged(true);

            // [9/23/2010] LMS-4958 :: Manager Mode > Batch Import: Option to Change Password on Next Login
            updateableUser.setChangePasswordOnLogin( sharedContext.isChangePasswordOnLogin() );


            //Update Profile before Remove Training Manger since it generate exception after removal.
            learnerService.updateLearnerProfile(updateableUser.getLearner().getLearnerProfile());

            /**
             *
             INT-3460
            if(sharedContext.getManagerOrgGroupIndex()!=-1 && rowColumns.length > sharedContext.getManagerOrgGroupIndex())
            {
                if(StringUtils.isNotBlank(rowColumns[sharedContext.getManagerOrgGroupIndex()]))
                {
                	assignTrainingManager(updateableUser, rowColumns, sharedContext, sharedContext.getDefaultManagerRole());
                }else{ //i.e. Manage Org Group Cell value is blank. Demote previous managers
                	if(updateableUser.getTrainingAdministrator()!=null){
                		// this means.. this is existing learner and has got administrator .. really need demotion :)
                		removeTrainingManager(updateableUser);
                	}
                }
            }
            */

            int securityRoleColumnIndex = sharedContext.getSecurityRoleColumnIndex();
            if(securityRoleColumnIndex > 0 ) {   // Security Role column exist
            	try {
                  	boolean securityRoleResult =  processSecurityRoles(updateableUser, sharedContext, rowColumns);
                      logDebug("Security Role Result: " + securityRoleResult);
                  } catch (Exception e) {
                  	String errorMessage = e.getMessage();
                  	logDebug("Error in assigning security Role: " + errorMessage);
                  }
            }

            logDebug("updating learner having id = "+updateableUser.getLearner().getId()+" profile id = "+
            		updateableUser.getLearner().getLearnerProfile().getId() +" address id = "+
            		updateableUser.getLearner().getLearnerProfile().getLearnerAddress().getId()+" user id = "+updateableUser.getId());

            VU360User updatedUser = learnerService.updateUserFromBatchFile(updateableUser);


            //learnerService.updateLearnerProfile(updateableUser.getLearner().getLearnerProfile());
            Learner updatedLearner = updatedUser.getLearner();
            logDebug("learner updated.");

            this.updateAssociationOfOrgGroupsAndLearnerGroups(updateableUser.getLearner(), setOfOrgGroups, setOfLearnerGroups,
                    sharedContext, false);

            NERCBatchImportUtil.updateNERCCreditFieldValue(
                setOfLearnerGroups,
                NERCCreditReportingField,
                rowColumns[NERCColumnIndex],
                updatedLearner.getLearnerProfile(),
                accreditationService,
                learnerService);

            //For Enrolling Learner into Courses
//            List<Learner> learners = new ArrayList<Learner>();
//			learners.add(updateableUser.getLearner());
//			enrollLearnersToLearnerGroupCourses(learners,setOfLearnerGroups, sharedContext.getBrander(), sharedContext.getLoggedInUser(),sharedContext.getCurrentCustomer(),sharedContext.getVelocityEngine());
			sharedContext.updateLearnerGroupMap(updatedLearner, setOfLearnerGroups);
        }
        finally
        {
            logMethod("updateUser", false);
        }
    }

    private VU360User getExistingUser(
            List<VU360User> existingUserList,
            String[] rowColumns)
    {
        try
        {
            logMethod("getExistingUser", true);
            VU360User duplicateUser = null;
            logDebug("existingUserList size = " + existingUserList.size());
            for( VU360User user : existingUserList ) // loop through all system learners
            {
                if( user.getUsername().equalsIgnoreCase(rowColumns[COL_USERNAME]) ) // now match with specific email
                {
                    logDebug("USER ALREADY EXISTS");
                    duplicateUser = user;
                    break; // break the loop ,as we are done for this row
                }
            }
            return duplicateUser;
        }
        finally
        {
            logMethod("getExistingUser", false);
        }
    }

    private void setLearnerAndLearnerProfileProperties (
            Learner learner,
            LearnerProfile learnerProfile,
            String[] rowColumns,
            Map<Integer, CustomField> customFieldsWithIndicesInFileMap,
            Address[] addresses,
            Customer currentCustomer
    )
    {
        setCustomFieldValuesInLearnerProfile(rowColumns, customFieldsWithIndicesInFileMap, learnerProfile);
        learnerProfile.setOfficePhone(rowColumns[COL_OFFICE_PHONE]);
        learnerProfile.setOfficePhoneExtn(rowColumns[COL_OFFICE_PHONE_EXT]);
        learnerProfile.setMobilePhone(rowColumns[COL_MOBILE_PHONE]);
        learnerProfile.setLearnerAddress(addresses[0]);
        learnerProfile.setLearnerAddress2(addresses[1]);
        learnerProfile.setLearner(learner);
        learner.setLearnerProfile(learnerProfile);
        learner.setCustomer(currentCustomer);
    }

    private Set<LearnerGroup> checkMissingLearnerGroupsAndReturnLearnerGroups(
            String[] rowColumns,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext)
    {
        try
        {
            logMethod("checkMissingLearnerGroupsAndReturnLearnerGroups", true);
            Set<LearnerGroup> setOfLearnerGroups = new HashSet<LearnerGroup>();
            if(rowColumns[COL_LEARNER_GRPS] == null || rowColumns[COL_LEARNER_GRPS].isEmpty())
            {
                logDebug("learner group is null or empty");
            }
            else
            {
                logDebug("rowColumns[COL_LEARNER_GRPS] = " + rowColumns[COL_LEARNER_GRPS]);
                StringTokenizer learnerGroupTokenizer = new StringTokenizer(rowColumns[COL_LEARNER_GRPS], ":");
                String learnerGroupName;
                while(learnerGroupTokenizer.hasMoreTokens())
                {
                    learnerGroupName = learnerGroupTokenizer.nextToken().trim();
                    logDebug("learnerGroupName = " + learnerGroupName);

                    synchronized (sharedContext.getAllLearnerGroups())
                    {
                        LearnerGroup learnerGroup = sharedContext.getAllLearnerGroups().
                                get(learnerGroupName.trim().toUpperCase());
                        if( learnerGroup == null )
                        {
                            logDebug("creating learner group...="+learnerGroupName.trim().toUpperCase());
                            learnerGroup = new LearnerGroup();
                            learnerGroup.setName(learnerGroupName);
                            learnerGroup.setOrganizationalGroup(sharedContext.getRootOrganizationalGroup());// TODO its not correct.. i guess
                            learnerGroup.setCustomer(sharedContext.getCurrentCustomer());
                            //learnerGroup = learnerService.saveLearnerGroup(learnerGroup);
                            learnerGroup = orgGroupService.addLearnerGroup(learnerGroup);
                            //LMS-6744
                            //sharedContext.getRootOrganizationalGroup().getLearnerGroups().add(learnerGroup);
                            sharedContext.getAllLearnerGroups().
                                    put(learnerGroup.getName().trim().toUpperCase(), learnerGroup);
                            logDebug("learner group created.");
                        }
                        else
                            logDebug("learner group found !");
                        setOfLearnerGroups.add(learnerGroup);
                    }//synchronized
                }
            }
            return setOfLearnerGroups;
        }
        finally
        {
            logMethod("checkMissingLearnerGroupsAndReturnLearnerGroups", false);
        }
    }

    private Set<OrganizationalGroup> checkMissingOrgGroupsAndReturnOrgGroups(
            Map<Object, Object> errorMsg,
            String[] rowColumns,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
            VU360User loggedInUser)
    {
        try
        {
            logMethod("checkMissingOrgGroupsAndReturnOrgGroups", true);
            Set<OrganizationalGroup> setOfOrgGroups = new HashSet<OrganizationalGroup>();
            if(rowColumns[COL_ORG_GRPS] == null || rowColumns[COL_ORG_GRPS].isEmpty())
            {
                errorMsg.put("orgGroupMissing", "true");
                logDebug("ERROR org group is null or empty");
            }
            else
            {
                logDebug("rowColumns[COL_ORG_GRPS] = " + rowColumns[COL_ORG_GRPS]);
                StringTokenizer orgGroupTokenizer = new StringTokenizer(rowColumns[COL_ORG_GRPS], ":");
                String orgGroupHierarchy;
                while( orgGroupTokenizer.hasMoreTokens() )
                {
                    orgGroupHierarchy = orgGroupTokenizer.nextToken().trim();
                    logDebug("orgGroupHierarchy = " + orgGroupHierarchy);
                 // remove white spaces before and after >
                    String hierarchy = "";
                    String[] groupNames = orgGroupHierarchy.split(GROUP_SPLITTER);
                    for(String orgGroupName : groupNames)
                    {
                    	if(hierarchy=="")
                    		hierarchy=orgGroupName.trim();
                    	else
                    		hierarchy=hierarchy+">"+orgGroupName.trim();
                    }
                    orgGroupHierarchy=hierarchy;
                    //True if Organization exists
                    Boolean isOrgExists=false;
            		Map<String,OrganizationalGroup> managedGroupsMap=new HashMap<String, OrganizationalGroup>();

            		if(loggedInUser.getTrainingAdministrator()!=null && !loggedInUser.getTrainingAdministrator().getManagedGroups().isEmpty()){
            			for(OrganizationalGroup managedGroups : loggedInUser.getTrainingAdministrator().getManagedGroups()){
            				managedGroupsMap.put(managedGroups.getName().toUpperCase(), managedGroups);
            			}
            		}

            		OrganizationalGroup organizationalGroup = null;
            		String[] orgInBatchFile=orgGroupHierarchy.split(">");

            		for(int i=0;i <orgInBatchFile.length ; i++){
            				if(managedGroupsMap.containsKey(orgInBatchFile[i].toUpperCase())){
            					isOrgExists=true;
            					organizationalGroup=managedGroupsMap.get(orgInBatchFile[i].toUpperCase());
            				}
            		}

//                  if organization is not available then create it
                    if(!isOrgExists)
                    {
                        logDebug("org group not found in shared context");
                        synchronized (sharedContext.getAllOrganizationalGroups())
                        {
                            organizationalGroup = this.createOrgGroupsIfNotExist(GROUP_SPLITTER, orgGroupHierarchy,
                                    sharedContext);
                        }
                        if (organizationalGroup != null)
                        {
                            setOfOrgGroups.add(organizationalGroup);
                            logDebug("org group(s) created !");
                        }
                        else
                        {
                            errorMsg.put("orgGroupMissing", "true");
                            log.error("ERROR >> invalid group path or not permitted");
                        }
                    }
                    else
                    {
	                        setOfOrgGroups.add(organizationalGroup);
	                        logDebug("orgGroup found in shared context");

                    }
                }
            }
            return setOfOrgGroups;
        }
        finally
        {
            logMethod("checkMissingOrgGroupsAndReturnOrgGroups", false);
        }
    }

    public OrganizationalGroup createOrgGroupsIfNotExist (
            String spliter, String orgGroupHierarchy,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext)
    {
        try
        {
            logMethod("createOrgGroupsIfNotExist", true);
            log.info("orgGroupHierarchy = " + orgGroupHierarchy);
            log.info("rootOrgGroup.getName() = " + sharedContext.getRootOrganizationalGroup().getName());
            log.info("sharedContext.getAllOrganizationalGroups() = " + sharedContext.getAllOrganizationalGroups());

            //check root group first if it matches then continue else return null
            String rootGroup;
            if(orgGroupHierarchy.indexOf(spliter) > 0)
                rootGroup = orgGroupHierarchy.substring(0, orgGroupHierarchy.indexOf(spliter)).trim();
            else
                rootGroup = orgGroupHierarchy.trim();
            log.info("rootGroup = " + rootGroup);
            if(!sharedContext.getRootOrganizationalGroup().getName().equalsIgnoreCase(rootGroup))
            {
                log.error("ERROR >> rootGroup does not exists");
                return null;
            }

            OrganizationalGroup organizationalGroup;
            String hierarchy = orgGroupHierarchy;
            do
            {
                log.info("lookup group '" + hierarchy + "'...");
                organizationalGroup = sharedContext.getAllOrganizationalGroups().get(hierarchy.trim().toUpperCase());
                if(organizationalGroup == null)
                {
                    log.info("not found");
                    if(hierarchy.indexOf(spliter) > 0)
                        hierarchy = hierarchy.substring(0, hierarchy.lastIndexOf(spliter));
                    else
                        hierarchy = "";
                    log.info("hierarchy = " + hierarchy);
                }
                else
                    log.info("found");
                log.info("hierarchy.length() = "  + hierarchy.length());
                log.info("organizationalGroup = " + organizationalGroup);
            }
            while(organizationalGroup == null && hierarchy.length() > 0);
            if(organizationalGroup != null && !organizationalGroup.toString().equalsIgnoreCase(orgGroupHierarchy))
            {
                log.info("creating missing groups...");
                orgGroupHierarchy = orgGroupHierarchy.substring(organizationalGroup.toString().length() +
                        spliter.length());
                log.info("missing orgGroupHierarchy = " + orgGroupHierarchy);
                String[] orgGroupNames = orgGroupHierarchy.split(spliter);
                OrganizationalGroup newOrgGroup;
                for(String orgGroupName : orgGroupNames)
                {
                    if(vu360UserService.hasAdministratorRole(sharedContext.getLoggedInUser()) ||
                            sharedContext.getLoggedInUser().getTrainingAdministrator().
                                    isManagesAllOrganizationalGroups())
                    {
                        log.info("creating org group '" + orgGroupName + "'...");
                        newOrgGroup = orgGroupService.createOrgGroup2(sharedContext.getCurrentCustomer(),
                                orgGroupName, sharedContext.getRootOrganizationalGroup(), organizationalGroup);
                        log.info("new org group created");
/*
                        boolean childFound = false;
                        for(OrganizationalGroup childOrgGroup : organizationalGroup.getChildrenOrgGroups())
                            if(childOrgGroup.toString().equalsIgnoreCase(newOrgGroup.toString()))
                            {
                                newOrgGroup = childOrgGroup;
                                childFound = true;
                                break;
                            }
                        if(!childFound)
*/
                            organizationalGroup.getChildrenOrgGroups().add(newOrgGroup);
                        newOrgGroup.setRootOrgGroup(sharedContext.getRootOrganizationalGroup());
                        newOrgGroup.setParentOrgGroup(organizationalGroup);
                        sharedContext.getAllOrganizationalGroups().
                                put(newOrgGroup.toString().trim().toUpperCase(), newOrgGroup);
                        log.info("new org group added to shared context");
                        organizationalGroup = newOrgGroup;
                    }
                    else
                    {
                        log.info("checking permission...");
                        boolean hasPermission = false;
                        List<OrganizationalGroup> managedGroups = sharedContext.getLoggedInUser().
                                getTrainingAdministrator().getManagedGroups();
                        log.info("managedGroups = " + managedGroups);
                        for(OrganizationalGroup permitedOrgGroup :
                                sharedContext.getLoggedInUser().getTrainingAdministrator().getManagedGroups())
                        {
                            log.info("\tchecking group '" + permitedOrgGroup.getName() + "'...");
                            if(permitedOrgGroup.getName().equals(organizationalGroup.getName()))
                            {
                                hasPermission = true;
                                break;
                            }
                        }
                        log.info("hasPermission = " + hasPermission);
                        if(hasPermission)
                        {
                            log.info("creating org group '" + orgGroupName + "'...");
                            newOrgGroup = orgGroupService.createOrgGroup2(sharedContext.getCurrentCustomer(),
                                    orgGroupName, sharedContext.getRootOrganizationalGroup(), organizationalGroup);
                            log.info("new org group created");
/*
                            boolean childFound = false;
                            for(OrganizationalGroup childOrgGroup : organizationalGroup.getChildrenOrgGroups())
                                if(childOrgGroup.toString().equalsIgnoreCase(newOrgGroup.toString()))
                                {
                                    newOrgGroup = childOrgGroup;
                                    childFound = true;
                                    break;
                                }
                            if(!childFound)
*/
                                organizationalGroup.getChildrenOrgGroups().add(newOrgGroup);
                            newOrgGroup.setRootOrgGroup(sharedContext.getRootOrganizationalGroup());
                            newOrgGroup.setParentOrgGroup(organizationalGroup);
                            sharedContext.getAllOrganizationalGroups().
                                    put(newOrgGroup.toString().trim().toUpperCase(), newOrgGroup);
                            log.info("new org group added to shared context");
                            organizationalGroup = newOrgGroup;
                        }
                        else
                        {
                            log.error("ERROR >> No permissions to create org group.");
                            organizationalGroup = null;
                            break;
                        }
                    }
                }
            }
            else
                log.info("No new groups/subgroups to create group already exists");
            return organizationalGroup;
        }
        finally
        {
            logMethod("createOrgGroupsIfNotExist", false);
        }
    }

    private void initializeErrorMsgFlags(Map<Object, Object> errorMsg, int recordNumber, String[] rowColumns)
    {
        errorMsg.put("userNameMissing", "false");
        errorMsg.put("orgGroupMissing", "false");
        errorMsg.put("learnerGroupMissing", "false");
        errorMsg.put("recordNumber", recordNumber);
        errorMsg.put("firstName", rowColumns[COL_FIRST_NAME]);
        errorMsg.put("lastName", rowColumns[COL_LAST_NAME]);
        errorMsg.put("emailAddress", rowColumns[COL_EMAIL]);
        errorMsg.put("userName", rowColumns[COL_USERNAME]);
        errorMsg.put("country", rowColumns[COL_COUNTRY]);
        errorMsg.put("state", rowColumns[COL_STATE]);
        errorMsg.put("zip", rowColumns[COL_ZIPCODE]);



        String shownPassword = "";
        for( int passwordLength = 0; passwordLength < rowColumns[COL_PASSWORD].length(); passwordLength ++ )
            shownPassword = shownPassword + "*";
        errorMsg.put("password", shownPassword);
        errorMsg.put("updateLearner", "false");
        errorMsg.put("invalidFormat", "false");
        errorMsg.put("invalidZip", "false");
        errorMsg.put("orgGroups", rowColumns[COL_ORG_GRPS]);
        errorMsg.put("userGroups", rowColumns[COL_LEARNER_GRPS]);

    }

    private void checkMissingUserName (String[] rowColumns, Map<Object, Object> errorMsg)
    {
        if(rowColumns[COL_USERNAME] == null || rowColumns[COL_USERNAME].isEmpty())
            errorMsg.put("userNameMissing", "true");

    }
    /*
     * this method will be used to validate Address values specified in batch import file.
     * this method can be used for both new & existing learners
     */
    private void validateAddress(String[] rowColumns, Brander brander, Map<Object, Object> errorMsg){
    	// for now rely on already created method of validateAndReturnAddresses. Once we will deprecate it we will write our own logic
    	validateAndReturnAddresses(rowColumns, brander, errorMsg);
    }
    private Address[] validateAndReturnAddresses (String[] rowColumns, Brander brander, Map<Object, Object> errorMsg)
    {
        try
        {
            logMethod("validateAndReturnAddresses", true);
            Address address = new Address();
            address.setStreetAddress(rowColumns[COL_STREET_ADDRESS]);
            address.setStreetAddress2(rowColumns[COL_STREET_ADDRESS2]);
            address.setCity(rowColumns[COL_CITY]);
            address.setState(rowColumns[COL_STATE]);
            address.setZipcode(rowColumns[COL_ZIPCODE]);
            address.setCountry(rowColumns[COL_COUNTRY]);

            Address address2 = new Address();
            address2.setStreetAddress("");
            address2.setStreetAddress2("");
            address2.setCity("");
            address2.setState("");
            address2.setZipcode("");
            address2.setCountry("");

            errorMsg.put("zipcode", address.getStreetAddress() + " " + address.getStreetAddress2() + " " +
                    address.getCity() + " " + address.getState());

            boolean validCountry = false;
            boolean validState = false;
            String stateBean = "";

            List<LabelBean> countries = brander.getBrandMapElements("lms.manageUser.AddLearner.Country");
            for( LabelBean bean : countries )
            {
                if( rowColumns[COL_COUNTRY].equalsIgnoreCase(bean.getValue()) ||
                        rowColumns[COL_COUNTRY].equalsIgnoreCase(bean.getLabel()) )
                {
                    stateBean = bean.getValue();
                    address.setCountry(stateBean);
                    validCountry = true;
                    break;
                }
            }

            List<LabelBean> states;
            if( stateBean.isEmpty() || stateBean.equalsIgnoreCase("US") || stateBean.equalsIgnoreCase("United States") )
                states = brander.getBrandMapElements("lms.manageUser.AddLearner.State");
            else
                states = brander.getBrandMapElements("lms.manageUser.AddLearner."+stateBean+".State");
            if(StringUtils.isEmpty(rowColumns[COL_STATE]))//Since State is an optional field
        		validState=true;
            else
            {
	            for( LabelBean bean : states )
	            {
	                if( rowColumns[COL_STATE].equalsIgnoreCase(bean.getValue()) ||
	                        rowColumns[COL_STATE].equalsIgnoreCase(bean.getLabel()) ) {
	                    address.setState(bean.getValue());
	                    validState = true;
	                    break;
	                }
	            }
            }

            if( !validCountry || !validState
                    || ! ZipCodeValidator.isZipCodeValid( stateBean, rowColumns[COL_ZIPCODE], brander, log) )
            {
                String invalid = "invalidZip";
                if( !validCountry )
                    invalid = "invalidCountry";
                if( !validState )
                    invalid = "invalidState";
                errorMsg.put("invalidZip", invalid);
            }

            return new Address[] {address, address2};
        }
        finally
        {
            logMethod("validateAndReturnAddresses", false);
        }
    }

    private LMSRole getLearnerRole (Customer currentCustomer)
    {
        LMSRole lmsRole = vu360UserService.getOptimizedBatchImportLearnerDefaultRole(currentCustomer);
        if (lmsRole == null){
            lmsRole = vu360UserService.getRoleByName("LEARNER", currentCustomer);
        }
        return lmsRole;
    }

    /*
     * This method being used in iteration and fetch default role for every single learner.
     * default role is by customer and enough to be fetched at once before Iteration of records in batch file.
     * should remove it.
     */
    private void setUserLmsRoles (VU360User newUser, Customer currentCustomer)
    {
        Set<LMSRole> userRoles = new HashSet<LMSRole>();
        LMSRole lmsRole = vu360UserService.getDefaultRole(currentCustomer);
        if (lmsRole == null)
            lmsRole = vu360UserService.getRoleByName("LEARNER", currentCustomer);
        userRoles.add(lmsRole);
        newUser.setLmsRoles(userRoles);
    }

	private void updateCustomFieldValuesOfExistingLearner(LearnerProfile profile,List<CustomFieldValue> customFieldValues){
		try{
			List<CustomFieldValue> profileValues = profile.getCustomFieldValues();
			boolean found = false;
			List<CustomFieldValue> list2Remove = new ArrayList<CustomFieldValue>();
			for(CustomFieldValue value:customFieldValues){
				found = false;
				for(CustomFieldValue existingValue:profileValues){
					if(value.getCustomField().getFieldLabel().equals(existingValue.getCustomField().getFieldLabel())){
						list2Remove.add(existingValue);
						found = true;
						break;
					}
				}
				if(!found){
					profileValues.add(value);
				}
			}
			profileValues.removeAll(list2Remove);
			profileValues.addAll(customFieldValues);
		}
		catch(Exception e){
			log.error(e);
		}
	}

	private void setCustomFieldValuesInLearnerProfile (
            String[] rowColumns,
            Map<Integer, CustomField> customFieldsWithIndicesInFileMap,
            LearnerProfile learnerProfile)
    {
        List<CustomFieldValue> customFieldValueList = new ArrayList<CustomFieldValue>();
        if( rowColumns.length > FIX_FIELD_SIZE  )
        {
            for( int i = FIX_FIELD_SIZE-1; i < rowColumns.length ; i++ )
            {
                CustomField cf = customFieldsWithIndicesInFileMap.get(i);
                if( cf != null )
                {
                    CustomFieldValue cfv = this.getCustomFieldValueByCustomField(cf, rowColumns[i]);
                    if(cfv != null)
                        customFieldValueList.add(cfv);
                }
            }
        }
        if( learnerProfile.getCustomFieldValues() == null )
            learnerProfile.setCustomFieldValues(customFieldValueList);
        else
            updateCustomFieldValuesOfExistingLearner(learnerProfile, customFieldValueList);
        	//learnerProfile.getCustomFieldValues().addAll(customFieldValueList);
    }

    private List<VU360User> getExistingUserList (
            BufferedReader csvFileDataReader,
            String delimiter,
            Map<Integer, CustomField> customFieldsWithIndicesInFileMap,
            Map<CustomField, List<CustomFieldValueChoice>> customFieldValueChoices,
            List<CustomField> allCustomFields,
            Brander brander,
            Set<Integer> invalidRecordLineNumbers,
            int[] totalRecordCount,
            List<OptimizedBatchImportLearnersErrors> batchImportErrors,
            Customer currentCustomer,
            VU360User loggedInUser,
            CreditReportingField nercField,
            int nercFieldColumnIndex
    ) throws IOException
    {
        try
        {
            logMethod("getExistingUserList", true);
            List<VU360User> existingUserList = new ArrayList<VU360User>();
            List<String> userNameList = new ArrayList<String>();
            Set<String> masterUserNameSet = new HashSet<String>();
            Set<String> userNameSet = new HashSet<String>();
            String rowLine, rowColumns[];
            Map<String, Boolean> isValidRecord;
            String userName;
            Map <Object, Object> errorMsg;
            int recordNumber = 0;
            String rootOrgGroupName = orgGroupService.getRootOrgGroupForCustomer(currentCustomer.getId()).getName();
            while((rowLine = csvFileDataReader.readLine()) != null)
            {
                recordNumber++;
                rowColumns = this.splitStringWithQuotes(rowLine, delimiter);

                isValidRecord = isValidRow(rowColumns, customFieldsWithIndicesInFileMap, customFieldValueChoices, rootOrgGroupName,loggedInUser,nercField, nercFieldColumnIndex);
                if(!isValidRecord.get("invalidRecord"))
                {
                    userName = rowColumns[COL_USERNAME].trim();
                    if(rowColumns.length >= FIX_FIELD_SIZE && !masterUserNameSet.contains(userName))
                    {
                        userNameSet.add(userName);

                        /**
                         * Because of following Error
                         * Internal Exception: com.microsoft.sqlserver.jdbc.SQLServerException:
                         * The incoming tabular data stream (TDS) remote procedure call (RPC)
                         * protocol stream is incorrect. Too many parameters were provided in this
                         * RPC request. The maximum is 2100.Error Code: 8003
                         */
                        if(userNameSet.size() >= 500)
                            fetchUserFromDB(userNameList, userNameSet, existingUserList, masterUserNameSet);
                    }
                }
                else
                {
                    errorMsg = new HashMap<Object, Object>();
                    initializeErrorMsgFlags(errorMsg,recordNumber, rowColumns );
                    invalidRecordLineNumbers.add(recordNumber);
                    OptimizedBatchImportLearnersErrors batchImportError = new OptimizedBatchImportLearnersErrors();
                    checkInvalidRecordAndFlagErrorMessages(isValidRecord, errorMsg, recordNumber, rowColumns,
                            allCustomFields, brander, batchImportError,nercField,nercFieldColumnIndex );
                    batchImportErrors.add(batchImportError);
                    logDebug(">>> ERROR MESSAGE = " + errorMsg);
                }
            }
            totalRecordCount[0] = recordNumber;
            if(userNameSet.size() > 0)
            {
                fetchUserFromDB(userNameList, userNameSet, existingUserList, masterUserNameSet);
                masterUserNameSet.clear();
            }
            return existingUserList;
        }
        finally
        {
            logMethod("getExistingUserList", false);
        }
    }

    private void fetchUserFromDB (
            List<String> userNameList,
            Set<String> userNameSet,
            List<VU360User> existingUserList,
            Set<String> masterUserNameSet
    )
    {
        try
        {
            logMethod("fetchUserFromDB", true);
            userNameList.addAll(userNameSet);
            logDebug("finding all system learners with given user names...");
            existingUserList.addAll(learnerService.findAllSystemLearnersForBatchImport(userNameList));
            logDebug("done. existingUserList size is now " + existingUserList.size());
            userNameList.clear();
            masterUserNameSet.addAll(userNameSet);
            logDebug("masterUserNameSet size is now " + masterUserNameSet.size());
            userNameSet.clear();
        }
        finally
        {
            logMethod("fetchUserFromDB", false);
        }
    }

    private CustomFieldValue getCustomFieldValueByCustomField(CustomField customField, String value)
    {
        try
        {
            logMethod("getCustomFieldValueByCustomField", true);
            CustomFieldValue valueObj = new CustomFieldValue();
            if(customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField)
            {
                List<CustomFieldValueChoice> customFieldValueChoices = customFieldService.
                        getOptionsByCustomField(customField);
                List<CustomFieldValueChoice> customFieldValueSelectedChoices = new ArrayList<CustomFieldValueChoice>();
                boolean choicesAreGood = false;
                if(customField instanceof SingleSelectCustomField)
                {
                    choicesAreGood = false ;
                    for(CustomFieldValueChoice customFieldValueChoice : customFieldValueChoices)
                    {
                        if(customFieldValueChoice.getLabel().trim().equalsIgnoreCase(value.trim()))
                        {
                            choicesAreGood = true;
                            customFieldValueSelectedChoices.add(customFieldValueChoice);
                            break; //because it is single select
                        }
                    }
                    if( choicesAreGood )
                    {
                        valueObj.getValueItems().addAll(customFieldValueSelectedChoices );
                        valueObj.setValue( value );
                        valueObj.setCustomField(customField);
                    }
                }
                if (customField instanceof MultiSelectCustomField )
                {
                    String [] valueList = value.split(">");
                    for( String str : valueList )
                    {
                        choicesAreGood = false ;
                        for( CustomFieldValueChoice customFieldValueChoice : customFieldValueChoices )
                        {
                            if( customFieldValueChoice.getLabel().trim().equalsIgnoreCase(str.trim()))
                            {
                                choicesAreGood = true ;
                                customFieldValueSelectedChoices.add(customFieldValueChoice);
                            }
                        }
                        if(!choicesAreGood)
                            break;
                    }
                    if(choicesAreGood)
                    {
                        valueObj.setValueItems(customFieldValueSelectedChoices );
                        //valueObj.setValue( value );
                        valueObj.setCustomField(customField);
                    }
                }
            }
            else
            {
                valueObj.setValue(value);
                valueObj.setCustomField(customField);
            }
            return valueObj;
        }
        finally
        {
            logMethod("getCustomFieldValueByCustomField", false);
        }
    }

    private HashMap<Integer,CustomField> getCustomFieldsWithIndicesInFile(String[] headerColumns,
                                                                          List<CustomField> allCustomFields)
    {
        HashMap<Integer, CustomField> customFieldsWithIndicesMap = new HashMap<Integer, CustomField>();
        if(allCustomFields != null && allCustomFields.size() > 0)
        {
            int index;
            for(CustomField customField : allCustomFields)
            {
                for(index = 0; index < headerColumns.length; index++)
                {
                    if(headerColumns[index].trim().equalsIgnoreCase(customField.getFieldLabel().trim()))
                    {
                        customFieldsWithIndicesMap.put(index, customField);
                        break;
                    }
                }
            }
        }
        return customFieldsWithIndicesMap;
    }

    private Map<CustomField,List<CustomFieldValueChoice>> getCustomFieldChoices(List<CustomField> allCustomFields)
    {
        Map<CustomField,List<CustomFieldValueChoice>> customFieldChoiceMap=new HashMap<CustomField,
                List<CustomFieldValueChoice>>();
        if(allCustomFields != null && allCustomFields.size() > 0)
        {
            for(CustomField field : allCustomFields)
            {
                if(field instanceof SingleSelectCustomField || field instanceof MultiSelectCustomField)
                {
                    List<CustomFieldValueChoice> choices = customFieldService.getOptionsByCustomField(field);
                    customFieldChoiceMap.put(field, choices);
                }
            }
        }
        return customFieldChoiceMap;
    }

    private void getCustomFieldRowValues(
            String[] record,
            List<CustomField> allCustomFields,
            Map<Object,Object> errorMsg,
            Map<Integer, CustomField> customFieldsWithIndicesInFileMap
    )
    {
        try
        {
            logMethod("getCustomFieldRowValues", true);
            if(allCustomFields != null && allCustomFields.size() > 0)
            {
                int rowArrayCount = 0;
                for(CustomField cf : allCustomFields)
                {
                    int customFieldArrayIndex = (FIX_FIELD_SIZE + rowArrayCount);
                    //errorMsg.put(cfHash.get(new Integer(customFieldArrayIndex)).getFieldLabel(), "");
                    if(record.length > customFieldArrayIndex)
                    {
                        if(customFieldsWithIndicesInFileMap.get(customFieldArrayIndex) != null)
                            errorMsg.put(customFieldsWithIndicesInFileMap.get(customFieldArrayIndex).getFieldLabel(),
                                    record[customFieldArrayIndex]);
                        errorMsg.put(cf.getId(), cf);
                    }
                    else
                        break;
                    rowArrayCount++ ;
                }
            }
        }
        finally
        {
            logMethod("getCustomFieldRowValues", false);
        }
    }

    private String[] splitStringWithQuotes (String inputString, String delimiter)
    {
        List <String> retVal = new ArrayList<String>();
        StringBuilder currentPart = new StringBuilder();
        boolean withinQuotes = false;
        String localDelimiter=delimiter;
        if(localDelimiter.equals("\\|"))
            localDelimiter="|";
        for ( int i = 0; i < inputString.length(); i++ ){
            char c = inputString.charAt(i);
            if( withinQuotes ) {
                if(c == '"'){
                    withinQuotes = false;
                } else {
                    currentPart.append(c);
                }
            } else {
                if(c == localDelimiter.charAt(0)){
                    retVal.add( currentPart.toString().trim() );
                    currentPart.setLength(0);
                }else if( c == '"' ){
                    withinQuotes = true;
                } else {
                    currentPart.append(c);
                }
            }
        }
        retVal.add( currentPart.toString().trim() );
        return retVal.toArray(new String[retVal.size()]);
    }

    private Map<String, Boolean> isValidRow(
            String[] record,
            Map<Integer, CustomField> customFieldsWithIndicesInFileMap,
            Map<CustomField, List<CustomFieldValueChoice>> customFieldChoicesMap, String rootOrgGroupName,VU360User loggedInUser,
            CreditReportingField nercField,
            int nercFieldColumnIndex
    )
    {
            int column_count = 0;

            Map<String, Boolean> validRecord = new HashMap<String, Boolean>();
            validRecord.put("firstNameBlank", false);
            validRecord.put("lastNameBlank", false);
            validRecord.put("emailAddressBlank", false);
            validRecord.put("invalidEmailAddress", false);
            validRecord.put("userNameBlank", false);
            validRecord.put("passwordBlank", false);
            validRecord.put("confirmPasswordBlankl", false);
            validRecord.put("invalidRowNumber", false);
            validRecord.put("invalidRecord", false);
            validRecord.put("shortPassword", false);
            validRecord.put("missingUserGroup", false);
            validRecord.put("missingOrgGroup", false);
            validRecord.put("missingCountry", false);

            for (String rec : record)
            {
                column_count++;
                if (column_count == 1 && rec.equalsIgnoreCase("")) {
                    validRecord.put("firstNameBlank", true);
                    validRecord.put("invalidRecord", true);
                }
                if (column_count == 3 && rec.equalsIgnoreCase("")) {
                    validRecord.put("lastNameBlank", true);
                    validRecord.put("invalidRecord", true);
                }
                if(column_count == 12 && rec.equalsIgnoreCase("")){
                	validRecord.put("missingCountry", true);
                    validRecord.put("invalidRecord", true);
                }
                if (column_count == 13 && rec.equalsIgnoreCase("")) {
                    validRecord.put("emailAddressBlank", true);
                    validRecord.put("invalidRecord", true);
                }
                else if(column_count==13 && !isEmailValid(rec)){
                	 validRecord.put("invalidEmailAddress", true);
                     validRecord.put("invalidRecord", true);
                }
                if (column_count == 14 && rec.equalsIgnoreCase("")) {
                    validRecord.put("passwordBlank", true);
                    validRecord.put("invalidRecord", true);
                }
                if (column_count == 14 && rec.equalsIgnoreCase("")) {
                    validRecord.put("confirmPasswordBlankl", true);
                    validRecord.put("invalidRecord", true);
                } else if (column_count == 14 && !FieldsValidation.isPasswordCorrect(rec))
                {
                        validRecord.put("shortPassword", true);
                        validRecord.put("invalidRecord", true);
                }
                //TODO Learner group is not required field.. need to double check with new required
//                if(column_count == 15 && rec.equalsIgnoreCase("")){
//                	validRecord.put("missingLearnerGroup", true);
//                    validRecord.put("invalidRecord", true);
//                }

                if(column_count == 16 && rec.equalsIgnoreCase("")){
                	validRecord.put("missingOrgGroup", true);
                    validRecord.put("invalidRecord", true);
                }
//                 any restriction on user that the org group which he is entering should
                if(column_count == 16 && !rec.equalsIgnoreCase("")){
                	String rootOrgInFile = splitStringWithQuotes(rec, ":")[0];
                	if(hasManagerSecurityRightsForOrganization(loggedInUser,rootOrgInFile)){
    		    		validRecord.put("missingOrgGroup", true);
    		        	validRecord.put("invalidRecord", true);
                	}
                }
                if (column_count == 17 && rec.equalsIgnoreCase("")) {
                    validRecord.put("userNameBlank", true);
                    validRecord.put("invalidRecord", true);
                }
            }
            boolean invalidRecord;
            boolean choicesAreGood = false;
            for (int i = FIX_FIELD_SIZE; i < record.length; i++)
            {
                if(i == COL_MANAGER_ORG_GRP)
                    continue;
                CustomField field = customFieldsWithIndicesInFileMap.get(i);
                if (field != null) {
                    invalidRecord = false;
                    if (field.getFieldRequired() && StringUtils.isBlank(record[i])) {
                        validRecord.put(field.getFieldLabel(), true);
                        validRecord.put("invalidRecord", true);
                        validRecord.put(field.getFieldLabel() + "-required", true);
                        invalidRecord = true;
                    }
                    if (!invalidRecord && !StringUtils.isBlank(record[i])) {
                        if (field instanceof SSNCustomFiled) {
                            if (!FieldsValidation.isSSNValid(record[i])) {
                                validRecord.put(field.getFieldLabel(), true);
                                validRecord.put("invalidRecord", true);
                                validRecord.put(field.getFieldLabel() + "-ssnError", true);
                            }
                        } else if (field instanceof DateTimeCustomField) {
                            if (!FieldsValidation.isValidDate(record[i])) {
                                validRecord.put(field.getFieldLabel(), true);
                                validRecord.put("invalidRecord", true);
                                validRecord.put(field.getFieldLabel() + "-dateError", true);
                            }
                        } else if (field instanceof NumericCusomField) {
                            if (!FieldsValidation.isNumeric(record[i])) {
                                validRecord.put(field.getFieldLabel(), true);
                                validRecord.put("invalidRecord", true);
                                validRecord.put(field.getFieldLabel() + "-numericError", true);
                            }
                        } else if (field instanceof SingleSelectCustomField) {
                            List<CustomFieldValueChoice> choices = customFieldChoicesMap.get(field);
                            boolean choiceFound = false;
                            for (CustomFieldValueChoice choice : choices) {
                                if (choice.getLabel().trim().equalsIgnoreCase(record[i].trim())) {
                                    choiceFound = true;
                                    break;
                                }
                            }
                            if (!choiceFound) {
                                validRecord.put("invalidRecord", true);
                                validRecord.put(field.getFieldLabel() + "-textError", true);
                            }
                        } else if (field instanceof MultiSelectCustomField) {
                            List<CustomFieldValueChoice> choices = customFieldChoicesMap.get(field);
                            String[] valueList = record[i].split(">");
                            String someValue = "";
                            for (String str : valueList) {
                                choicesAreGood = false;
                                for (CustomFieldValueChoice customFieldValueChoice : choices) {
                                    if (customFieldValueChoice.getLabel().trim().equalsIgnoreCase(str.trim())) {
                                        choicesAreGood = true;
                                    }
                                }
                                if (!choicesAreGood)
                                    break;
                            }
                            if (!choicesAreGood) {
                                validRecord.put("invalidRecord", true);
                                validRecord.put(field.getFieldLabel() + "-multiSelectError", true);
                            }
                        }
                    }
                }
            }

            if(nercFieldColumnIndex>0 && nercField !=null){
            	String nercData = record[nercFieldColumnIndex].trim();
            	if(nercField instanceof TelephoneNumberCreditReportingField){
            		if((nercData==null || nercData.length()==0) && isNERCFieldRequired(nercField)){
            			validRecord.put("invalidRecord", true);
            			validRecord.put(nercField.getFieldLabel() + "-telephoneNumberRequiredError", true);
            		}
            		else if(!(nercData.length()==10 && FieldsValidation.isNumeric(nercData))){
            			validRecord.put("invalidRecord", true);
            			validRecord.put(nercField.getFieldLabel() + "-telephoneNumberValidationError", true);
            		}
            	}
            }

            return validRecord;
    }

    @Transactional
	private Boolean hasManagerSecurityRightsForOrganization(
			VU360User loggedInUser,	String rootOrgInFile) {

		Map<String,String> managedGroupsMap=new HashMap<String, String>();

		List tempManagedGroups = vu360UserService.findAllManagedGroupsByTrainingAdministratorId(loggedInUser.getTrainingAdministrator().getId());
		loggedInUser.getTrainingAdministrator().setManagedGroups(tempManagedGroups);
		if(loggedInUser.getTrainingAdministrator()!=null && !loggedInUser.getTrainingAdministrator().getManagedGroups().isEmpty()){
			for(OrganizationalGroup managedGroups : loggedInUser.getTrainingAdministrator().getManagedGroups()){
				managedGroupsMap.put(managedGroups.getName().toUpperCase(), managedGroups.getName().toUpperCase());
			}
			String[] orgInBatchFile=rootOrgInFile.split(">");
			for(int i=0;i <orgInBatchFile.length ; i++){
				if(!managedGroupsMap.containsKey(orgInBatchFile[i].toUpperCase())){
					return true;
				}
			}
		}
		return false;
	}

    public OrgGroupLearnerGroupService getOrgGroupService() {
        return orgGroupService;
    }

    public void setOrgGroupService(OrgGroupLearnerGroupService orgGroupService) {
        this.orgGroupService = orgGroupService;
    }

    public VU360UserService getVu360UserService() {
        return vu360UserService;
    }

    public void setVu360UserService(VU360UserService vu360UserService) {
        this.vu360UserService = vu360UserService;
    }

    public LearnerService getLearnerService() {
        return learnerService;
    }

    public void setLearnerService(LearnerService learnerService) {
        this.learnerService = learnerService;
    }

    public CustomFieldService getCustomFieldService() {
        return customFieldService;
    }

    public void setCustomFieldService(CustomFieldService customFieldService) {
        this.customFieldService = customFieldService;
    }

    private void sendEmail(
            VU360User loggedInUser,
            Brander brander,
            String loginURL,
            VelocityEngine velocityEngine,
            OptimizedBatchImportLearnersSummary batchImportResultSummary,
            Customer currentCustomer,
            boolean notifyLearnerOnRegistration)
    {

        try
        {
            Map<String, Object> model = new HashMap<String, Object>();
            String batchImportTemplatePath;
            String fromAddress;
            String fromCommonName;
            String subject;
            String support;
            String text;
            model.put("customerName", currentCustomer.getName());
            model.put("loggedInUser", loggedInUser);
            model.put("url", loginURL);
            model.put("brander", brander);
            String lmsDomain=VU360Properties.getVU360Property("lms.domain");
            model.put("lmsDomain",lmsDomain);
            if (notifyLearnerOnRegistration)
            {
            	List<String> passwords =(List<String>)batchImportResultSummary.getPasswords();
                batchImportTemplatePath = brander.getBrandElement("lms.email.batchUpload.body");
                fromAddress = brander.getBrandElement("lms.email.batchUpload.fromAddress");
                fromCommonName = brander.getBrandElement("lms.email.batchUpload.fromCommonName");
                subject = brander.getBrandElement("lms.email.batchUpload.subject");
                support = brander.getBrandElement("lms.email.batchUpload.fromCommonName");
                model.put("support", support);
                int mailCounter=0;
                if(batchImportResultSummary.getAddedLearners()!=null && !batchImportResultSummary.getAddedLearners().isEmpty())
                for (Learner learnerToBeMailed : batchImportResultSummary.getAddedLearners())
                {
                	VU360User user = learnerToBeMailed.getVu360User();
                	user.setPassword(passwords.get(mailCounter));
                	log.debug("CURRENT USER PASSWORD :: "+user.getPassword());
                    model.put("user", user);
                    /*START-BRANDING EMAILTEMPLATE WORK*/
        			String templateText=brander.getBrandElement("lms.branding.email.accountDetails.templateText");
        			String loginUrl=lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());

        			templateText=templateText.replaceAll("&lt;firstname&gt;",user.getFirstName());
                    templateText=templateText.replaceAll("&lt;lastname&gt;",user.getLastName());
                    templateText=templateText.replaceAll("&lt;username&gt;",user.getUsername());
                    templateText=templateText.replaceAll("&lt;password&gt;",user.getPassword());
                    templateText=templateText.replaceAll("&lt;loginurl&gt;", loginUrl);

                    model.put("templateText", templateText);
        			/*END BRANDING EMAIL TEMPLATE WORK*/
                    mailCounter++;
                    text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, batchImportTemplatePath, model);
                    logDebug("sending email to " + learnerToBeMailed.getVu360User().getEmailAddress() + "...");
                    try
                    {
                        SendMailService.sendSMTPMessage(
                                learnerToBeMailed.getVu360User().getEmailAddress()
                                , fromAddress, fromCommonName, subject, text);
                    }catch(Exception e)
                    {
                    	log.error(e);
                    }
                }
                model.clear();
            }
            model.put("customerName", currentCustomer.getName());
            model.put("loggedInUser", loggedInUser);
            model.put("url", loginURL);
            model.put("brander", brander);

            lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
            model.put("lmsDomain",lmsDomain);
            model.put("batchImportResultSummary", batchImportResultSummary);
            model.put("totalerrors", batchImportResultSummary.getBatchImportErrorsList().size());
            //model.put("totalerrors", ((Collection)errorMsg.get("errorMessages")).size());
            String toDate = formatter.format(Calendar.getInstance().getTime());
            batchImportTemplatePath =  brander.getBrandElement("lms.email.batchUpload.managerMail.body");
            fromAddress =  brander.getBrandElement("lms.email.batchUpload.managerMail.fromAddress");
            fromCommonName =  brander.getBrandElement("lms.email.batchUpload.managerMail.fromCommonName");
            subject =  brander.getBrandElement("lms.emil.batchUpload.managerMail.subject")+ toDate;
            support =  brander.getBrandElement("lms.email.batchUpload.managerMail.fromCommonName");
            model.put("support", support);
            logDebug("velocityEngine = " + velocityEngine);
            logDebug("batchImportTemplatePath = " + batchImportTemplatePath);
            text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, batchImportTemplatePath, model);

            logDebug("sending summary email to " + loggedInUser.getEmailAddress() + "...");
            if(loggedInUser.getEmailAddress()!=null && loggedInUser.getEmailAddress().length()>0)
	            SendMailService.sendSMTPMessage(loggedInUser.getEmailAddress()
	                    , fromAddress, fromCommonName, subject, text);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }

    // deprecated
    private List<OrganizationalGroup> getOrgGroupToManage(String orgGroupColumnVal, Customer customer)
    {
        List<OrganizationalGroup> orgGroup;
        String[] orgGroupNames = new String[1];
        orgGroupNames[0] = orgGroupColumnVal.substring(orgGroupColumnVal.lastIndexOf(">") + 1,
                orgGroupColumnVal.length());
        orgGroup = orgGroupService.getOrgGroupByNames(orgGroupNames, customer);
        return orgGroup;
    }

    @Transactional
    private Map<String, OrganizationalGroup> getOrganizationalGroupMap (Map<String, OrganizationalGroup> orgGroupMap, OrganizationalGroup orgGroup)
    {
    	//orgGroup = orgGroupService.loadForUpdateOrganizationalGroup(orgGroup.getId());

    	//@MariumSaud : New Method was added to load Organization Group inorder to avoid 'Could not find Session - Lazy Initialization Exception'
    	// occured when orgGroup.getChildrenOrgGroups() is called.
    	orgGroup = orgGroupService.getOrgGroupById(orgGroup.getId());
    	orgGroupMap.put(orgGroup.toString().toUpperCase(), orgGroup);
        List<OrganizationalGroup> childrenOrgGroups = orgGroup.getChildrenOrgGroups();
        //List<OrganizationalGroup> childrenOrgGroups = orgGroupService.getChildrenOrgGroupByParentOrgGroupId(orgGroup.getParentOrgGroup().getId());
        if(childrenOrgGroups != null && childrenOrgGroups.size() > 0)
            for (OrganizationalGroup childOrgGroup : childrenOrgGroups)
                orgGroupMap = this.getOrganizationalGroupMap(orgGroupMap, childOrgGroup);
        return orgGroupMap;
    }

    synchronized private Map<String, LearnerGroup> getLearnerGroupsMap (Map<String, LearnerGroup> learnerGroupMap, OrganizationalGroup rootOrgGroup)
    {

    	Stack<OrganizationalGroup> ogStack = new Stack<OrganizationalGroup>();
    	ogStack.add(rootOrgGroup);
    	List<OrganizationalGroup> childrenOrgGroup = rootOrgGroup.getChildrenOrgGroups();
    	if(CollectionUtils.isNotEmpty(childrenOrgGroup))
    		ogStack.addAll(childrenOrgGroup);

    	OrganizationalGroup og = null;
    	while(!ogStack.isEmpty()){
    		og = ogStack.pop();
    		//@MariumSaud : Method was added to load 'og' Organization Group again inorder to avoid 'Could not find Session - Lazy Initialization Exception'
    		// occured when og.getChildrenOrgGroups() is called.
    		og= orgGroupService.getOrgGroupById(og.getId());
    		//Set<LearnerGroup> learnerGroups = og.getLearnerGroups();
    		List<LearnerGroup> learnerGroups = orgGroupService.getLearnerGroupsByOrgGroup(og.getId());
    		if(CollectionUtils.isNotEmpty(learnerGroups)){
    			for(LearnerGroup lg:learnerGroups){
    				if(learnerGroupMap.get(lg.getName().trim().toUpperCase())== null){
        				logDebug("setting LearnerGroup :"+lg.getName().trim().toUpperCase());
        				learnerGroupMap.put(lg.getName().trim().toUpperCase(), lg);
    				}
    			}
    		}
			childrenOrgGroup = og.getChildrenOrgGroups();
			if(CollectionUtils.isNotEmpty(childrenOrgGroup)){
				ogStack.addAll(childrenOrgGroup);
			}
    	}
    	return learnerGroupMap;
    }



    private Map<String, LearnerGroup> getLearnerGroupMap (Map<String, LearnerGroup> learnerGroupMap, OrganizationalGroup orgGroup)
    {
        if(orgGroup != null)
        {
            List<LearnerGroup> learnerGroups = orgGroupService.getLearnerGroupsByOrgGroup(orgGroup.getId());
            if(learnerGroups != null && learnerGroups.size() > 0)
                for (LearnerGroup learnerGroup : learnerGroups)
                    learnerGroupMap.put(learnerGroup.getName().toUpperCase(), learnerGroup);
            List<OrganizationalGroup> childrenOrgGroups = orgGroup.getChildrenOrgGroups();
            if(childrenOrgGroups != null && childrenOrgGroups.size() > 0)
                for (OrganizationalGroup childOrgGroup : childrenOrgGroups)
                    learnerGroupMap = this.getLearnerGroupMap(learnerGroupMap, childOrgGroup);
        }
        return learnerGroupMap;
    }

    //TODO take better decisions on existing and new memeberships..
    @Transactional
    private void updateAssociationOfOrgGroupsAndLearnerGroups (
            Learner learner, Set<OrganizationalGroup> setOfOrgGroups, Set<LearnerGroup> setOfLearnerGroups,
            OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext, boolean add)
    {

        logDebug("updating associations of learner groups...");
        orgGroupService.addRemoveLearnerGroupsForLearner(learner, setOfLearnerGroups);
        orgGroupService.addRemoveOrgGroupsForLearner(learner, setOfOrgGroups);

    }

//    public OrganizationalGroup addLearnerInOrgGroups(OrganizationalGroup root,List<OrganizationalGroup> list, Learner learner){
//    	Stack<OrganizationalGroup> orgStack = new Stack<OrganizationalGroup>();
//    	orgStack.add(root);
//
////    	if(root.getChildrenOrgGroups()!=null || root.getChildrenOrgGroups().size()>0)
////    		orgStack.addAll(root.getChildrenOrgGroups());
//
//    	OrganizationalGroup temp = null;
//    	while(!orgStack.isEmpty()){
//    		temp = orgStack.pop();
//    		if(temp.getChildrenOrgGroups()!=null || temp.getChildrenOrgGroups().size()>0)
//        		orgStack.addAll(temp.getChildrenOrgGroups());
//
//    		for(OrganizationalGroup og:list){
//    			if(og.getId().longValue()== temp.getId().longValue()){
//    				if(!temp.getMembers().contains(learner))
//    					temp.addMember(learner);
//    			}
//    		}
//       	}
//    	return root;
//    }
//
    private void printOrgGroup(OrganizationalGroup og){
    	logDebug("\n" + og.toString());
    	for(OrganizationalGroup orgGroup : og.getChildrenOrgGroups())
    		printOrgGroup (orgGroup);
    }


    public static boolean isEmailValid(String email){
    	boolean isValid = false;

    	//	Initialize reg ex for email.
    	String expression = "^[\'\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    	CharSequence inputStr = email;
    //	Make the comparison case-insensitive.
    	Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
    	Matcher matcher = pattern.matcher(inputStr);
    	if(matcher.matches()){
    		isValid = true;
    	}
    	return isValid;
    }

	/**
	 * Enroll learners for batch import
	 * @param sharedContext
	 */
    private void enrollLearnersToLearnerGroupCourses(
    		OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext)
    {
		List<LearnerGroupItem> learnerGroupCourses = null;
		Map<Long,LearnerGroup> uniqueLearnersLearnerGroups=new HashMap<Long,LearnerGroup>();
		List<Learner> learnersToEnroll=new ArrayList<Learner>();
		List<LearnerGroup> listLearnerGroup=new ArrayList<LearnerGroup>();//list of LearnerGroups in which learners are to be enrolled
		Long[] learnerGroupCourseIds = null;
		int counter = 0;

		//Creating list of LearnerGroups in which learners are to be enrolled not learnerGroups in which are learner are not enrolled
		List<Long> learnerGroupId=sharedContext.getLearnerGroupIdsInWhichToEnrollLearner();
		for(Long idlearnerGroup : learnerGroupId){
			listLearnerGroup.add(learnerService.loadForUpdateLearnerGroup(idlearnerGroup));
		}

		enrollLearnersWhichAreDuplicateInLearnerGroup(sharedContext,uniqueLearnersLearnerGroups, learnersToEnroll, listLearnerGroup);

		// Job for those learners which are not duplicate in other Learner groups and only enrolled in one learner group
		for(LearnerGroup lg:listLearnerGroup){
			//learnerGroupCourses = lg.getLearnerGroupItems();
			learnerGroupCourses = learnerService.getLearnerGroupItemsByLearnerGroupId(lg.getId());
			if(learnerGroupCourses.size()>0){
				List<Learner> learners = new ArrayList<Learner>(sharedContext.getLearnersByLearnerGroup(lg));
				if(CollectionUtils.isEmpty(learners))
				continue;
				/*learnerGroupCourseIds = new Long[learnerGroupCourses.size()];
				counter = 0;
				for(LearnerGroupItem item:learnerGroupCourses){
					learnerGroupCourseIds[counter++]=item.getCourse().getId();
				}*/

				//enrollmentService.enrollLearnersInCourses(learners, learnerGroupCourseIds, sharedContext.getLoggedInUser(), sharedContext.getCurrentCustomer(), sharedContext.getBrander());
				enrollmentService.enrollLearnersInCoursesViaUserGroup(learners, lg.getLearnerGroupItems(), sharedContext.getLoggedInUser(), sharedContext.getCurrentCustomer(), sharedContext.getBrander());
			}
		}

    }

	private void enrollLearnersWhichAreDuplicateInLearnerGroup(
			OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext,
			Map<Long, LearnerGroup> uniqueLearnersLearnerGroups,
			List<Learner> learnersToEnroll, List<LearnerGroup> listLearnerGroup) {

		Iterator<Learner> iterLearner=null;
		Learner learnerOne=null;
		for(LearnerGroup learnerGroup:listLearnerGroup){
			iterLearner=sharedContext.getLearnersByLearnerGroup(learnerGroup).iterator();
			while(iterLearner.hasNext()){
				learnerOne=iterLearner.next();

				//[LMS-14829] - comments out incorrect loop
				//for(LearnerGroup learnerGroupInner:listLearnerGroup){//have to check this learnerOne in other LearnerGroups for duplicate
				//	if(learnerGroupInner.getId().equals(learnerGroup.getId())){// have to check for other learner groups that it exists in other or not
				//		continue;
				//	}
						List<Learner> learnerList = orgGroupService.getLearnersByLearnerGroupId(learnerGroup.getId());
						for(Learner learnerInner:learnerList){
							if(learnerInner.getId().equals(learnerOne.getId())){
								//if learner exists in other group add to map and only will be added if previously not added
								if(!uniqueLearnersLearnerGroups.containsKey(learnerOne.getId())) {
									uniqueLearnersLearnerGroups.put(Long.valueOf(learnerOne.getId()+""+learnerGroup.getId()),learnerGroup);
									if(!checkIflearnerExistInlearnersToEnrolllist(learnersToEnroll,learnerOne)){
										learnersToEnroll.add(learnerOne); // Check if learner is already in collection
									}
								}
								iterLearner.remove();
							}
						}
				//}
			}
		}

		// Job For those records which where duplicate in other LearnerGroups and will be enrolled separately
		for(Learner learnersToEnrollInlearneGroups:learnersToEnroll) {
			List<LearnerGroup> learnerGroupForLearner=new ArrayList<LearnerGroup>();
			for(LearnerGroup learnerGroup:listLearnerGroup) {
				if(learnerGroup != null){
					//[LMS-14829] - added check to restrict null addition in collection
					if(uniqueLearnersLearnerGroups.get(Long.valueOf(learnersToEnrollInlearneGroups.getId()+""+learnerGroup.getId()))!=null)
						learnerGroupForLearner.add((uniqueLearnersLearnerGroups.get(Long.valueOf(learnersToEnrollInlearneGroups.getId()+""+learnerGroup.getId()))));
				}

			}
			try{
				enrollmentService.enrollLearnerInLearnerGroupsCourses(sharedContext.getLoggedInUser(), sharedContext.getCurrentCustomer(), learnersToEnrollInlearneGroups,learnerGroupForLearner , sharedContext.getBrander());
			} catch (Exception e ) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}


	private boolean checkIflearnerExistInlearnersToEnrolllist(List<Learner> learnersToEnrollList, Learner learnerToAdd){
		boolean alreadyExist = false;
		if(learnerToAdd != null && learnersToEnrollList != null && learnersToEnrollList.size() > 0)	{
			for(Learner learner:learnersToEnrollList) {
				if(learner != null && learner.getId() == learnerToAdd.getId()){
					alreadyExist = true;
				}
			}
		}
		return alreadyExist;
	}

    public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}


	public ActiveDirectoryService getActiveDirectoryService() {
		return activeDirectoryService;
	}

	public void setActiveDirectoryService(
			ActiveDirectoryService activeDirectoryService) {
		this.activeDirectoryService = activeDirectoryService;
	}

	public AsyncTaskExecutorWrapper getAsyncTaskExecutorWrapper() {
		return asyncTaskExecutorWrapper;
	}
	public void setAsyncTaskExecutorWrapper(
			AsyncTaskExecutorWrapper asyncTaskExecutorWrapper) {
		this.asyncTaskExecutorWrapper = asyncTaskExecutorWrapper;
	}

	public LearnersToBeMailedService getLearnersToBeMailedService() {
		return learnersToBeMailedService;
	}

	public void setLearnersToBeMailedService(
			LearnersToBeMailedService learnersToBeMailedService) {
		this.learnersToBeMailedService = learnersToBeMailedService;
	}

    /**
     * @param accreditationService the accreditationService to set
     */
    public void setAccreditationService(AccreditationService accreditationService) {
        this.accreditationService = accreditationService;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<Object, Object> importUsersFromBatchFile(BatchImportData batchImportData) throws Exception {
        return importUsersFromBatchFile(
                batchImportData.getCustomer(),
                batchImportData.getFile(),
                batchImportData.getDelimiter(),
                batchImportData.getActionOnDuplicateRecords(),
                batchImportData.isAccVisible(),
                batchImportData.isAccLocked(),
                batchImportData.isFirstRowHeader(),
                batchImportData.notifyLearnerOnRegistration(),
                batchImportData.getLoginURL(),
                batchImportData.getLoggedInUser(),
                batchImportData.getVelocityEngine(),
                batchImportData.getAllCustomFields(),
                batchImportData.getBrander(),
                batchImportData.changePasswordOnLogin());
    }

    private int getColumnNameIndex(String[] headerColumns, String columnName) {

        for(int i=0; i<headerColumns.length; i++) {
        	String column = headerColumns[i].trim();
        	if (column.equals(columnName.trim())) {
        		return i;
        	}
        }
        return -1;
    } //end of getColumnNameIndex()

    private LMSRole getLmsRoleByName(String securityRoleName, Customer customer) {
        LMSRole lmsRole = null;
        lmsRole = vu360UserService.getTop1RoleByName(securityRoleName, customer);
        return lmsRole;
    }

    private boolean isRoleTypeManager(LMSRole lmsRole) {

        if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)) {
        	return true;
        }
        return false;
    } //end of isRoleTypeManager()

    private boolean isRoleTypeLearner(LMSRole lmsRole) {

        if (lmsRole.getRoleType().equalsIgnoreCase(LMSRole.ROLE_LEARNER)) {
            return true;
        }
        return false;
    } //end of isRoleTypeManager()

    private boolean processSecurityRoles(VU360User user,
    		OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext, String[] rowColumns) throws Exception {

        try {

        	// Check Security Role Column Exist
            int securityRoleColumnIndex = sharedContext.getSecurityRoleColumnIndex();
            if(securityRoleColumnIndex > 0 ) {

                // find security role column value
                String securityRoleColumnValue = rowColumns[securityRoleColumnIndex];
                if (StringUtils.isNotEmpty(securityRoleColumnValue) && StringUtils.isNotBlank(securityRoleColumnValue)) {

                	Customer currentCustomer = sharedContext.getCurrentCustomer();
                	Map<String, List<?>> securityRoleTypeMap = getSecurityRoleTypeMap(currentCustomer, securityRoleColumnValue);
                	boolean result = processSecurityRoleTypeMap(user, sharedContext, rowColumns, securityRoleTypeMap);
                	logDebug("Process Security Roles: " + result);
                } else {
                	logDebug("Security Role is empty or blanck. ");
                }
            } else {  //end of if(securityRoleColumnIndex > 0 then keep role as it is)
                logDebug("Security Role column is not present. ");
            }
            logDebug("Returning successfully.");
        } catch (Exception e) {
        	String errorMessage = e.getMessage();
        	throwException(errorMessage);
        }
        return true;
    }

    private Map<String, List<?>> getSecurityRoleTypeMap(Customer customer, String securityRoleColumnValue) throws Exception {

        if (StringUtils.isEmpty(securityRoleColumnValue) || StringUtils.isBlank(securityRoleColumnValue)) {
        	String errorMessage = "Security Role column value is null or blank";
        	throwException(errorMessage);
        }

        if (customer == null) {
        	String errorMessage = "Customer is mandotory to find LMSRole";
        	throwException(errorMessage);
        }

        Map<String, List<?>> securityRoleTypeMap = new HashMap<String, List<?>>();
        List<LMSRole> learnerRoleTypeList = new ArrayList<LMSRole>();
        List<LMSRole> managerRoleTypeList = new ArrayList<LMSRole>();

        try {
        	// multiple security role can provided with colon
            String[] securityRoleNames = securityRoleColumnValue.split(":");
            if (securityRoleNames.length > 0) {   // more than one record
            	for (String securityRoleName : securityRoleNames) {
                	if (StringUtils.isNotEmpty(securityRoleName) && StringUtils.isNotBlank(securityRoleName)) {
                		String securityRole = securityRoleName.trim();

                	    // get Lms Role
                		LMSRole lmsRole = getLmsRoleByName(securityRole, customer);
                		if (lmsRole != null) {
                			if (isRoleTypeManager(lmsRole)) {
                				managerRoleTypeList.add(lmsRole);
                			} else if (isRoleTypeLearner(lmsRole)) {
                				learnerRoleTypeList.add(lmsRole);
                			}
                		}
                	}
                } //end of for
            }
            securityRoleTypeMap.put("learnerRoleTypeList", learnerRoleTypeList);
            securityRoleTypeMap.put("managerRoleTypeList", managerRoleTypeList);
            return securityRoleTypeMap;
        } catch (Exception e) {
        	String errorMessage = e.getMessage();
        	throwException(errorMessage);
        }
        return null;
    }

    private boolean processSecurityRoleTypeMap(VU360User user,
    		OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext, String[] rowColumns,

    	Map<String, List<?>> securityRoleTypeMap) throws Exception {

        if (securityRoleTypeMap == null || securityRoleTypeMap.isEmpty()) {
        	String errorMessage = "Security Role Type Map is empty or null";
        	throwException(errorMessage);
        }

        try {

        	List<LMSRole> learnerRoleTypeList = (List<LMSRole>)securityRoleTypeMap.get("learnerRoleTypeList");
            List<LMSRole> managerRoleTypeList = (List<LMSRole>)securityRoleTypeMap.get("managerRoleTypeList");

            if (!learnerRoleTypeList.isEmpty()) {
            	unassignLmsRolesAndAddLmsRolesToUser(user, learnerRoleTypeList);
            }

            if ( !managerRoleTypeList.isEmpty()) {
            	processManagerRoleTypeList(user, sharedContext, rowColumns, managerRoleTypeList);
            } else {
            	if (user.getId() != null) {  // If this is new user then we get id = null
            		List<LMSRole> demotedManagerRoleTypeList = new ArrayList<LMSRole>();
            		List<LMSRole> userLmsRoles = vu360UserService.getLMSRolesByUserById(user.getId());  // find all lms manager roles of user
            		for (LMSRole lmsRole : userLmsRoles) {
            			if (lmsRole != null) {
            				if (isRoleTypeManager(lmsRole)) {
            					demotedManagerRoleTypeList.add(lmsRole);
            				}
            			}
            		}
            		Set<String> roleTypeSet = getLmsRolesTypeSet(demotedManagerRoleTypeList);
            		unAssignUserFromAllLmsRolesOfTypeInSet(user, roleTypeSet);
            		if(user.getTrainingAdministrator()!=null) {
                        // this means.. this is existing learner and has got administrator .. really need demotion :)
                        removeTrainingManager(user);
            		}
            	}
			}
        } catch (Exception e) {
        	String errorMessage = e.getMessage();
        	throwException(errorMessage);
        }
        return true;
    }

    private boolean processManagerRoleTypeList(VU360User user,
        	OptimizedBatchImportLearnersProcessorThreadSharedContext sharedContext, String[] rowColumns,
        	List<LMSRole> lmsRoleTypeList) throws Exception {

        try {
        	int managerOrgGroupIndex = sharedContext.getManagerOrgGroupIndex();
        	String managerOrgGroup = rowColumns[managerOrgGroupIndex];
        	if(managerOrgGroupIndex != -1 && rowColumns.length > managerOrgGroupIndex) {
				Map<String,OrganizationalGroup> allOrgGroups = sharedContext.getAllOrganizationalGroups();
				unassignLmsRolesAndAddLmsRolesToUser(user, lmsRoleTypeList);
        		if(StringUtils.isNotBlank(managerOrgGroup)) {
        			assignTrainingManagerToUser(user, managerOrgGroup, allOrgGroups);
        		} else {

        			// If Manger Org group value is empty then it should associate to Root group. Tyler suggestion
        			Customer currentCustomer = sharedContext.getCurrentCustomer();
					OrganizationalGroup rootOrgGroup = orgGroupService.getRootOrgGroupForCustomer(currentCustomer.getId());
					String rootOrgGroupName = rootOrgGroup.getName();
					assignTrainingManagerToUser(user, rootOrgGroupName, allOrgGroups);
        		}
        	}
        } catch (Exception e) {
        	String errorMessage = e.getMessage();
        	throwException(errorMessage);
        }
        return true;
    }

    private boolean assignTrainingManagerToUser(VU360User user, String managerOrgGroup,
    		Map<String,OrganizationalGroup> allOrgGroups) throws Exception {

        try {

            List<OrganizationalGroup> orgGroups2Manage = getOrganizationalGroupListFromManagerOrgGroup(managerOrgGroup, allOrgGroups);
        	TrainingAdministrator trainingAdministrator = getUserTrainingAdministrator(user);
        	trainingAdministrator.setManagedGroups(orgGroups2Manage);
        } catch (Exception e) {
        	String errorMessage = e.getMessage();
        	throwException(errorMessage);
        }
        return true;
    }

    private boolean unassignLmsRolesAndAddLmsRolesToUser(VU360User user, List<LMSRole> lmsRolesList) throws Exception {

        try {
        	if (user != null && lmsRolesList != null && !lmsRolesList.isEmpty()) {
        		Set<String> roleTypeSet = getLmsRolesTypeSet(lmsRolesList);
        		unAssignUserFromAllLmsRolesOfTypeInSet(user, roleTypeSet);
            	addLmsRolesToUser(user, lmsRolesList);
        	} else {
        		logDebug("User is null or LMS roles list is empty or null. Returning without unassigning and assigning lms roles.");
        	}
        } catch (Exception e) {
        	String errorMessage = e.getMessage();
        	throwException(errorMessage);
        }
        return true;
    }

    private Set<String> getLmsRolesTypeSet(List<LMSRole> roleList) {

        if (roleList == null || roleList.isEmpty()) {
        	return null;
        }

        Set<String> roleTypeSet = new HashSet<String>();
        for (LMSRole lmsRole : roleList) {
        	if (lmsRole != null) {
        		String roleType = lmsRole.getRoleType();
        		if (StringUtils.isNotEmpty(roleType) && StringUtils.isNotBlank(roleType)) {
        			roleTypeSet.add(roleType);
        		}
        	}
        }
        return roleTypeSet;
    }

    private boolean unAssignUserFromAllLmsRolesOfTypeInSet(VU360User user, Set<String> roleTypeSet) throws Exception {

        if (roleTypeSet == null || roleTypeSet.isEmpty()) {
        	return false;
        }
        if (user == null) {
        	return false;
        }

        if (user.getId() != null) {    // If this is new user then we get id = null

        	Set<LMSRole> roles = user.getLmsRoles();
            Set<LMSRole> roles2Remove = new HashSet<LMSRole>();
        	try {
                for (String roleType: roleTypeSet) {
            		if (StringUtils.isNotEmpty(roleType) && StringUtils.isNotBlank(roleType)) {
            			for(LMSRole role : roles){
            	        	if(role.getRoleType().equals(roleType)){
            	        		roles2Remove.add(role);
            	        	}
            	        }
            		}
            	}

                if(CollectionUtils.isNotEmpty(roles2Remove)){
                	roles.removeAll(roles2Remove);
                }
                user.setLmsRoles(roles);
            } catch (Exception e) {
            	String errorMessage = e.getMessage();
            	throwException(errorMessage);
            }
        }
        return true;
    }

    private boolean addLmsRolesToUser(VU360User user, List<LMSRole> lmsRolesList) throws Exception {

        if (user == null) {
        	String errorMessage = "user is null. Unable to add lms roles to null user";
        	throwException(errorMessage);
        }

        if (lmsRolesList == null || lmsRolesList.isEmpty()) {
        	String errorMessage = "Lms roles list is empty or null. No lms roles are present to add in user.";
        	throwException(errorMessage);
        }

        /**
         * we can assign only one role to learner. Suppose user give three Security Roles like
         * LearnerRoleTypeTest1:LearnerRoleTypeTest2:LearnerRoleTypeTest3. We assign only first role to learner
         * because in our LMS system, if we assign LearnerRoleTypeTest1 to learner and then assign
         * LearnerRoleTypeTest2 to same learner then learner remove from the first role i.e., LearnerRoleTypeTest1.
         * If we do not put break then all LearnerRoleTypeTest1:LearnerRoleTypeTest2:LearnerRoleTypeTest3 assign
         * to learner which is not right.
         */
        for (LMSRole lmsRole : lmsRolesList) {
        	try {
        		addLmsRoleToUser(user, lmsRole);
        		break;
        	} catch (Exception e) {
        		String errorMessage = e.getMessage();
            	throwException(errorMessage);
        	}
        }
        return true;
    }

    private boolean addLmsRoleToUser(VU360User user, LMSRole lmsRole) throws Exception {

        if (user == null) {
        	String errorMessage = "user is null. Unable to add lms role to null user";
        	throwException(errorMessage);
        }

        if (lmsRole == null) {
        	String errorMessage = "LMS role is null. Unable to add null role to user";
        	throwException(errorMessage);
        }
        user.addLmsRole(lmsRole);
        return true;
    }

    private TrainingAdministrator getUserTrainingAdministrator(VU360User user) throws Exception {

        if (user == null) {
        	String errorMessage = "user is null while getting user training administrator";
        	throwException(errorMessage);
        }

        try {
        	TrainingAdministrator trainingAdministrator = user.getTrainingAdministrator();
            if(trainingAdministrator == null) {
        		trainingAdministrator = new TrainingAdministrator();
        		trainingAdministrator.setCustomer(user.getLearner().getCustomer());
        		trainingAdministrator.setManagesAllOrganizationalGroups(false);//through batch import we only allow management on certain groups not all
        		trainingAdministrator.setVu360User(user);
        		user.setTrainingAdministrator(trainingAdministrator);
        	}
            return trainingAdministrator;
        } catch (Exception e) {
        	String errorMessage = e.getMessage();
        	throwException(errorMessage);
        }
        return null;
    }

    private List<OrganizationalGroup> getOrganizationalGroupListFromManagerOrgGroup(String managerOrgGroup,
        	Map<String,OrganizationalGroup> allOrgGroups) throws Exception {

        try {
        	StringTokenizer managedOrgGroupTokenizer = new StringTokenizer(managerOrgGroup, ":");

            String orgGroupHierarchy;
            List<OrganizationalGroup> orgGroups2Manage = new ArrayList<OrganizationalGroup>();

            while( managedOrgGroupTokenizer.hasMoreTokens()) {
                // getting the organizational group path
                orgGroupHierarchy = managedOrgGroupTokenizer.nextToken().trim();
                OrganizationalGroup organizationalGroup = allOrgGroups.get(orgGroupHierarchy.toUpperCase());
                if(organizationalGroup !=null){
                    orgGroups2Manage.add(organizationalGroup);
                }
            }
            return orgGroups2Manage;
        } catch (Exception e) {
        	String errorMessage = e.getMessage();
        	throwException(errorMessage);
        }
        return null;
    }

    private boolean isRoleTypeExist(Set<LMSRole> lmsRoles, String roleType) {

        if (CollectionUtils.isEmpty(lmsRoles) || StringUtils.isBlank(roleType)) {
        	return false;
        }

        for (LMSRole lmsRole : lmsRoles) {
        	if (lmsRole != null) {
        		if (roleType.equalsIgnoreCase(LMSRole.ROLE_LEARNER)) {
        			if (isRoleTypeLearner(lmsRole)) {
        				return true;
        			}
        		} else if (roleType.equalsIgnoreCase(LMSRole.ROLE_TRAININGMANAGER)) {
        			if (isRoleTypeManager(lmsRole)) {
        				return true;
        			}
        		}
    		}
        }
        return false;
    }

    private void throwException(String error) throws Exception {
    	logDebug(error);
    	throw new Exception(error);
    }
}
