package com.softech.vu360.lms.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.CustomFieldValue;
import com.softech.vu360.lms.model.CustomFieldValueChoice;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DateTimeCustomField;
import com.softech.vu360.lms.model.LMSRole;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.MultiSelectCustomField;
import com.softech.vu360.lms.model.NumericCusomField;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SSNCustomFiled;
import com.softech.vu360.lms.model.SingleSelectCustomField;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomFieldService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.web.controller.manager.FileUploadBean;
import com.softech.vu360.lms.web.controller.model.FileRecordValidator;
import com.softech.vu360.lms.web.controller.validator.ZipCodeValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class BatchImportLearnersServiceImpl //implements BatchImportLearnersService 
{

	private static final Logger log = Logger.getLogger(BatchImportLearnersServiceImpl.class.getName());
	
	public static final int FIX_FIELD_SIZE = 17;
	
	private static String ERROR_MESSAGE_TEXT = "Incorrect display sequence";
	private static String DATE_FORMAT = "MM/dd/yyyy";
	public static final String SPLITTER = ">";
	
	private OrgGroupLearnerGroupService orgGroupService;
	private VU360UserService vu360UserService;
	private LearnerService learnerService;
	private CustomFieldService customFieldService;	
	private VelocityEngine velocityEngine;
	
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
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

	/**
	 * 
	 */
	public Map<String, String[]> validateCSVData(String batchImportLearnersCSVData, FileUploadBean bean, Brander brander, String firstRowHeader, String delimiter, Customer customer) throws Exception {
		
		Map<String, String[]> errors = new HashMap<String, String[]>();
		
		if ( batchImportLearnersCSVData != null && !batchImportLearnersCSVData.trim().equals("")) {
			
			boolean isValid = isValidFile(batchImportLearnersCSVData, firstRowHeader, brander);
			if( !isValid ) {
				String[] errorStringParts = new String[]{brander.getBrandElement("lms.batchUpload.fileSizeNotice"), 
						ERROR_MESSAGE_TEXT};
				errors.put("file", errorStringParts);
			}
			
			isValid=isValidDelimiter(batchImportLearnersCSVData,firstRowHeader,delimiter);
			if( !isValid ) {
				String[] errorStringParts = new String[]{brander.getBrandElement("lms.batchImportUsersResult.InvalidDelimiter"), 
						ERROR_MESSAGE_TEXT};
				errors.put("file", errorStringParts);
			}
			
			if (StringUtils.isBlank(firstRowHeader) || firstRowHeader.equalsIgnoreCase("yes"))
			{
				List<String> missingFields=this.validateFileForCustomFields(delimiter, batchImportLearnersCSVData, bean, customer);
				StringBuilder errorMessage=new StringBuilder();
				if(missingFields!=null && missingFields.size()>0)
				{
					for(String field:missingFields){
						errorMessage.append(field);
						errorMessage.append(",");
					}
					errorMessage.delete(errorMessage.length()-1, errorMessage.length());
					errorMessage.append(" required fields missing in your batch file");
					
					String[] errorStringParts = new String[]{errorMessage.toString(), ERROR_MESSAGE_TEXT};
					errors.put("file", errorStringParts);
				}
			}
		}
		
		return errors;
	}
	/*
	 * 
	 */
	public Map<Object, Object> importUsersFromBatchFile(Customer customer,String file, String delimiter, String actionOnDuplicateRecords,boolean accVisible,boolean accLocked,boolean isFirstRowHeader,boolean notifyLearnerOnRegistration, String loginURL,VU360User loggedInUser)	throws Exception
	{
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<Object, Object>  errorMsg = new HashMap<Object, Object> ();
		Map<Integer, CustomField> customFieldHash = new HashMap<Integer, CustomField>();
		//VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
		//get customFields from customer.getCustomFields() and customer.getDistributor().getCustomFields();
		//VelocityEngine inject through spring
		//get brander w.r.t to brand specified in customer
		//if not specified then take VU360Branding.DEFAULT_BRAND with Language.DEFAULT_LANG
		//VU360Branding.getInstance().getBrander("brandName", "en");
		com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
		lang.setLanguage(Language.DEFAULT_LANG);
		Brander brander = VU360Branding.getInstance().getBrander(customer.getBrandName(), lang);
		List<CustomField> customFields = new ArrayList<CustomField>(); 
		customFields.addAll(customer.getCustomFields());
		customFields.addAll(customer.getDistributor().getCustomFields());
		if ( file == null || file.isEmpty() || file.length() == 0 ) {
			log.debug(" file is null ");
			errorMsg.put("nullFile", true);
		}
		else {
			errorMsg = readUploadedFile( file, delimiter, isFirstRowHeader, accLocked,
					accVisible, actionOnDuplicateRecords, loggedInUser, brander, customFields, customFieldHash,customer );
			errorMsg.put("nullFile", false);
		}
		List<Learner> learnersToBeMailed = (List<Learner>) errorMsg.get("addedLearners");
		
		if (notifyLearnerOnRegistration) {
			sendEmail(learnersToBeMailed, loggedInUser, brander, loginURL, velocityEngine, errorMsg, customer);
		}
		
		context.put("errorMsg", errorMsg);
		context.put("customFieldHeaders", customFields);
		context.put("customFieldHash", customFieldHash);
		
		return context;
	}	
	
	
	private Map<Object, Object> readUploadedFile(String file, String delimiter, boolean isFirstRowHeader, boolean accLocked,
			boolean accVisible, String actionOnDuplicateRecords, VU360User loggedInUser, Brander b , List<CustomField> customFields, Map<Integer, CustomField> customFieldHash,Customer customer) throws IOException {

		FileRecordValidator validator = new FileRecordValidator();
		Map <Object, Object> totalMap = new HashMap<Object, Object>();
		Map <Object, Object> errorMsg = new HashMap<Object, Object>();
		List <Map<Object, Object>> errorMessages = new ArrayList<Map<Object, Object>>();
		List <Map<Object, Object>> numOfupdatedLearners = new ArrayList<Map<Object, Object>>();
		List<com.softech.vu360.lms.model.CustomField> allCustomFieldsList = this.getAllCustomFields(customer);

		String header = null  ;
		String headerRow[] ={};
		String userAdd = "";
		BufferedReader is = new BufferedReader(new StringReader(file));
		Boolean hasManagerColumn=false;
		int managerColumnIndex=0;


		Map<CustomField,List<CustomFieldValueChoice>> customFieldValueChoices=new HashMap<CustomField,List<CustomFieldValueChoice>>();
		if( isFirstRowHeader ){
			header = is.readLine();
			
			if( delimiter.equalsIgnoreCase("|") ) 
				delimiter = "\\|";

//			headerRow = header.split(delimiter,1000);
			headerRow = splitStringWithQuotes(header, delimiter);
			if(headerRow.length>FIX_FIELD_SIZE){
			
				for(int i=FIX_FIELD_SIZE;i<headerRow.length;i++){
					if(headerRow[i].toString().equalsIgnoreCase("Manager Org Group")){
						hasManagerColumn=true;
						managerColumnIndex=i;
						break;
					}
				}
			}
			
			
			customFieldHash = getCustomFieldsInFile(headerRow , allCustomFieldsList);
			//fileBean.setCfHash( customFieldHash );
			customFieldValueChoices=getCustomFieldChoices(allCustomFieldsList);
		}

		int recordNumber = 0;
		int number_of_imported_users = 0;
		int updatedLearners = 0;
		String nextLine;
		Map<String, Boolean> isValidRecord;
		List<VU360User> userList = new ArrayList<VU360User>();
		List<VU360User> useralreadyExistingUserList = new ArrayList<VU360User>();
		List<String> existingUserNameList = new ArrayList <String>();

		// names of all org & learner groups are stored here.
		List <LearnerGroup>allLearnerGroups = orgGroupService.getAllLearnerGroups(customer.getId(),loggedInUser);

		Set<LMSRole> userRoles = new HashSet<LMSRole>();
		//LMSRole lmsRole =securityAndRolesService.getLearnerRoleTemplateByCustomer(customer); //vu360UserService.getDefaultRole(customer);
		LMSRole lmsRole =vu360UserService.getDefaultRole(customer);//by OWS for LMS-3577

		if ( lmsRole == null ) {
			lmsRole = vu360UserService.getRoleByName("LEARNER", customer);
		}

		/* ******************************************************************** */
		/* ******************************************************************** */
		/* ******************************************************************** */
		/* to pass email batch upload file email addresses to IN Query*/

		List<String> userNameList = new ArrayList<String>();

		String[] strRow;
		while((nextLine=is.readLine())!=null)
		{
			strRow=this.splitStringWithQuotes(nextLine, delimiter);
			recordNumber++;
			isValidRecord = validator.isValidRow(strRow, customFieldHash,customFieldValueChoices,hasManagerColumn,managerColumnIndex);
			log.debug("line record is valid - "+isValidRecord);

			if( isValidRecord.get("invalidRecord") == false ) 
			{
				if( delimiter.equalsIgnoreCase("|") ) 
					delimiter = "\\|";
				//String[] record2 = nextLine.split(delimiter,1000);
				String[] record2 =  strRow;
				String[] record = new String[record2.length];
				if( record2.length == 14 && nextLine.charAt(nextLine.length()-1) == delimiter.charAt(0) ) {
					for( int i=0 ; i<14 ; i++ ) {
						record[i] = record2[i];
					}
					record[14] = "";
					record[15] = "";
				} else {
					record = record2;
				}

				if(record.length >= FIX_FIELD_SIZE)
					userNameList.add(record[16].trim());
			}
		}
		useralreadyExistingUserList = learnerService.findAllSystemLearners(userNameList);

		is = new BufferedReader(new StringReader(file));
		if( isFirstRowHeader ){
			is.readLine();
		} 
		recordNumber = 0 ;

		/* ******************************************************************** */
		/* ******************************************************************** */
		/* ******************************************************************** */

		List <Learner>addedLearners = new ArrayList <Learner>();

		while((nextLine=is.readLine())!=null) {
			strRow=this.splitStringWithQuotes(nextLine,delimiter);
			errorMsg = new HashMap<Object, Object>();
			recordNumber++;
			isValidRecord = validator.isValidRow(strRow, customFieldHash,customFieldValueChoices,hasManagerColumn,managerColumnIndex);
			log.debug("line record is valid - "+isValidRecord);
			if( delimiter.equalsIgnoreCase("|") ) 
				delimiter = "\\|";
			// populate custom field values 
			//getCustomFieldRowValues( nextLine.split(delimiter,1000) ,  fileBean.getFileHeader(),  errorMsg ,customFieldHash );
			getCustomFieldRowValues( strRow ,  customFields,  errorMsg ,customFieldHash );
			if( isValidRecord.get("invalidRecord") == false ) {

				VU360User vu360User = new VU360User();
				Learner learner = new Learner();
				LearnerProfile learnerProfile = new LearnerProfile();
				Address address = new Address();
				Address address2 = new Address();
				boolean update = false;
				boolean valid = true;
				boolean validGroups = true;
				long userId = 0;

				if( delimiter.equalsIgnoreCase("|") ) 
					delimiter = "\\|";
				//String[] record2 = nextLine.split(delimiter,1000);
				String[] record2 =  strRow;
				String[] record = new String[record2.length];
				if( record2.length == 14 && nextLine.charAt(nextLine.length()-1) == delimiter.charAt(0) ) {
					for( int i=0 ; i<14 ; i++ ) {
						record[i] = record2[i];
					}
					record[14] = "";
					record[15] = "";
				} else {
					record = record2;
				}

				/* *******************custom field work ******************************* */

				List<CustomFieldValue> CustomFieldValueList = new ArrayList<CustomFieldValue>(); 
				if( record.length > FIX_FIELD_SIZE  ) {
					for( int i= FIX_FIELD_SIZE ; i < record.length ; i++ ){
			
						CustomField cf = customFieldHash.get(new Integer(i));
						if( cf != null ){
							
							CustomFieldValue cfv = this.getCustomFieldValueByCustomField(cf, record[i].toString());
							if(cfv!=null)
								CustomFieldValueList.add(cfv);
						}
					}
					
				}
				if( learnerProfile.getCustomFieldValues() == null )
					learnerProfile.setCustomFieldValues(CustomFieldValueList);
				else
					learnerProfile.getCustomFieldValues().addAll(CustomFieldValueList);

				/* *******************custom field work ******************************* */


				String firstName = record[0].trim();
				vu360User.setFirstName(firstName);
				vu360User.setMiddleName(record[1].trim());
				String lastName = record[2].trim();
				vu360User.setLastName(lastName);
				userRoles.add(lmsRole);
				vu360User.setLmsRoles(userRoles);
				vu360User.setLmsAdministrator(null);
				//vu360User.setTrainingAdministrator(null);
				vu360User.setExpirationDate(null);
				learnerProfile.setOfficePhone(record[3].trim());
				learnerProfile.setOfficePhoneExtn(record[4].trim());
				learnerProfile.setMobilePhone(record[5].trim());

				// These variables are used for validation
				String state = record[9].trim();
				String zip = record[10].trim();
				String country = record[11].trim();

				address.setStreetAddress(record[6].trim());
				address.setStreetAddress2(record[7].trim());
				address.setCity(record[8].trim());
				address.setState(record[9].trim());
				address.setZipcode(record[10].trim());
				address.setCountry(record[11].trim());

				address2.setStreetAddress("");
				address2.setStreetAddress2("");
				address2.setCity("");
				address2.setState("");
				address2.setZipcode("");
				address2.setCountry("");

				userAdd = address.getStreetAddress()+" "+address.getStreetAddress2()+" "+address.getCity()+" "+address.getState();
				String emailAddress = record[12].trim();
				String userName = "";

				if(record.length > 16 )
					userName = record[ ( FIX_FIELD_SIZE - 1 )].trim();
				else{
					valid = false;
					errorMsg.put("userNameMissing", "true" );
				}
				
				String password = record[13].trim();

				String learnerGroups = record[14].trim();

				String orgGroups = "";
				if(record.length>15)
					orgGroups = record[15].trim();

				boolean duplicate = false;
				//				for( String userNameString : existingUserNameList) {
				//					if( userName.equalsIgnoreCase(userNameString) ) {
				//						duplicate = true;
				//						break;
				//					}
				//				}
				VU360User usr=vu360UserService.findUserByUserName(userName);
				if(usr!=null) duplicate = true;

				if( !duplicate ) {
					update = false;
				} else {
					if( actionOnDuplicateRecords != null && actionOnDuplicateRecords.equalsIgnoreCase("update") ) {

						String orgGroupMissing = "false";
						String learnerGroupMissing = "false";
						String isUpdate = "true";
						String shownPassword = "";
						for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
							shownPassword = shownPassword + "*";
						
						List<VU360User> vU360User = vu360UserService.getUsersByEmailAddress(emailAddress);
						if( vU360User != null && !vU360User.isEmpty() ) {
							VU360User presentVu360User = vU360User.get(0);
							userId = presentVu360User.getId();
							vu360User.setId(userId);
							learner.setId(presentVu360User.getLearner().getId());
							address.setId(presentVu360User.getLearner().getLearnerProfile().getLearnerAddress().getId());
							address2.setId(presentVu360User.getLearner().getLearnerProfile().getLearnerAddress2().getId());
							learnerProfile.setId(presentVu360User.getLearner().getLearnerProfile().getId());
						}
						errorMsg.put("orgGroupMissing", orgGroupMissing);
						errorMsg.put("learnerGroupMissing", learnerGroupMissing);
						errorMsg.put("recordNumber", recordNumber);
						errorMsg.put("firstName", firstName);
						errorMsg.put("lastName", lastName);
						errorMsg.put("emailAddress", emailAddress);
						errorMsg.put("userName", userName);
						errorMsg.put("password", shownPassword);
						errorMsg.put("updateLearner", isUpdate);
						errorMsg.put("invalidFormat", "false");
						errorMsg.put("invalidZip", "false");
						errorMsg.put("orgGroups", orgGroups);
						errorMsg.put("learnerGroups", learnerGroups);
						errorMsg.put("zipcode", userAdd);
						update = true;
					} else {
						valid = false;
					}
				}
				/*
				 *  ################################
				 *  Validation of State, Country and 
				 *  Zip code/postal code is done here...
				 */
				boolean validCountry = false;
				boolean validState = false;
				String stateBean = "";
				List<LabelBean> countries = b.getBrandMapElements("lms.manageUser.AddLearner.Country");

				for( LabelBean bean : countries ) {
					if( country.equalsIgnoreCase(bean.getValue()) || country.equalsIgnoreCase(bean.getLabel()) ) {
						stateBean = bean.getValue();
						address.setCountry(stateBean);
						validCountry = true;
						break;
					}
				}
				List<LabelBean> states = null;
				if( stateBean.isEmpty() || stateBean.equalsIgnoreCase("US") || stateBean.equalsIgnoreCase("United States") ) {
					states = b.getBrandMapElements("lms.manageUser.AddLearner.State");
				} else {
					states = b.getBrandMapElements("lms.manageUser.AddLearner."+stateBean+".State");
				}

				for( LabelBean bean : states ) {
					if( state.equalsIgnoreCase(bean.getValue()) || state.equalsIgnoreCase(bean.getLabel()) ) {
						address.setState(bean.getValue());
						validState = true;
						break;
					}
				} 
				if( !validCountry || !validState || ! ZipCodeValidator.isZipCodeValid( stateBean, zip, b, log) ) {

					String invalid = "invalidZip";
					if( !validCountry ) {
						invalid = "invalidCountry";
					}
					if( !validState ) {
						invalid = "invalidState";
					}
					String isUpdate = "false";
					String orgGroupMissing = "false";
					String shownPassword = "";
					for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
						shownPassword = shownPassword + "*";
					errorMsg.put("orgGroupMissing", orgGroupMissing);
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("firstName", firstName);
					errorMsg.put("lastName", lastName);
					errorMsg.put("emailAddress", emailAddress);
					errorMsg.put("userName", userName);
					errorMsg.put("password", shownPassword);
					errorMsg.put("updateLearner", isUpdate);
					errorMsg.put("invalidFormat", "false");
					errorMsg.put("invalidZip", invalid);
					errorMsg.put("orgGroups", orgGroups);
					errorMsg.put("learnerGroups", learnerGroups);

					valid = false;
					validGroups = false;
				}
				// ############  end of validation  ###########

				learnerProfile.setLearnerAddress(address);
				learnerProfile.setLearnerAddress2(address2);
				learner.setLearnerProfile(learnerProfile);
				learnerProfile.setLearner(learner);

				vu360User.setEmailAddress(emailAddress);
				vu360User.setPassword(password);
				vu360User.setUsername(userName);

				if( valid == false && ( userName.isEmpty()  ) ) {

					String isUpdate = "false";
					String orgGroupMissing = "false";
					String userNameMissing = "true";
					String shownPassword = "";
					for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
						shownPassword = shownPassword + "*";
					errorMsg.put("userNameMissing", userNameMissing);
					errorMsg.put("orgGroupMissing", orgGroupMissing);
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("firstName", firstName);
					errorMsg.put("lastName", lastName);
					errorMsg.put("emailAddress", emailAddress);
					errorMsg.put("userName", userName);
					errorMsg.put("password", shownPassword);
					errorMsg.put("updateLearner", isUpdate);
					errorMsg.put("invalidFormat", "false");
					errorMsg.put("invalidZip", "false");
					errorMsg.put("zipcode", userAdd);
					errorMsg.put("orgGroups", orgGroups);
					errorMsg.put("learnerGroups", learnerGroups);

					valid = false;
					validGroups = false;
				}				

				if( orgGroups == null || orgGroups.isEmpty() ) {

					String isUpdate = "false";
					String orgGroupMissing = "true";
					String shownPassword = "";
					for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
						shownPassword = shownPassword + "*";
					errorMsg.put("orgGroupMissing", orgGroupMissing);
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("firstName", firstName);
					errorMsg.put("lastName", lastName);
					errorMsg.put("emailAddress", emailAddress);
					errorMsg.put("userName", userName);
					errorMsg.put("password", shownPassword);
					errorMsg.put("updateLearner", isUpdate);
					errorMsg.put("invalidFormat", "false");
					errorMsg.put("invalidZip", "false");
					errorMsg.put("zipcode", userAdd);
					errorMsg.put("learnerGroups", learnerGroups);

					valid = false;
					validGroups = false;
				}
				log.debug("org group :: "+orgGroups);

				//let me collect the org groups with path
				StringTokenizer orgGroupTokenizer = new StringTokenizer(orgGroups, ":");
				List <OrganizationalGroup>listOfOrgGroups = new ArrayList <OrganizationalGroup>();

				/*
				 * let me check whether this is valid
				 * GetRootOrg
				 * check existance
				 * check manager's permission on parent org group
				 * if yes 
				 * if exists assign unless create then assign
				 * else show error
				 */
				while( orgGroupTokenizer.hasMoreTokens() ) {
					// getting the org group path -
					String orgGroupList = orgGroupTokenizer.nextToken().trim();
					OrganizationalGroup createdOrgGroup = orgGroupService.getOrgGroupsByHiererchyString(SPLITTER, orgGroupList, customer,loggedInUser);
					if ( createdOrgGroup != null ) {
						listOfOrgGroups.add(createdOrgGroup);
						log.debug("CREATED ORG GRP -- "+createdOrgGroup.getName());
					}
					else {
						String isUpdate = "false";
						String orgGroupMissing = "true";
						String shownPassword = "";
						for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
							shownPassword = shownPassword + "*";
						errorMsg.put("orgGroupMissing", orgGroupMissing);
						errorMsg.put("recordNumber", recordNumber);
						errorMsg.put("firstName", firstName);
						errorMsg.put("lastName", lastName);
						errorMsg.put("emailAddress", emailAddress);
						errorMsg.put("userName", userName);
						errorMsg.put("password", shownPassword);
						errorMsg.put("updateLearner", isUpdate);
						errorMsg.put("invalidFormat", "false");
						errorMsg.put("invalidZip", "false");
						errorMsg.put("orgGroups", orgGroups);
						errorMsg.put("learnerGroups", learnerGroups);
						errorMsg.put("zipcode", userAdd);
						valid = false;
						validGroups = false;
					}
				}

				log.debug("lear group :: "+learnerGroups);
				StringTokenizer learnerGroupTokenizer = new StringTokenizer(learnerGroups, ":");
				List <LearnerGroup>listOfLearnerGroups = new ArrayList <LearnerGroup>();
				while(learnerGroupTokenizer.hasMoreTokens()) {
					String learnerGroupName = learnerGroupTokenizer.nextToken().trim();
					int learnerGroupPresent = 0;
					int index = 0;
					for( LearnerGroup lGroup : allLearnerGroups ) {
						index++;
						if( lGroup.getName().equalsIgnoreCase(learnerGroupName) ) {
							learnerGroupPresent = index;
						}
					}
					if( learnerGroupPresent == 0 ) {

						String isUpdate = "false";
						String learnerGroupMissing = "true";
						String shownPassword = "";
						for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
							shownPassword = shownPassword + "*";
						errorMsg.put("learnerGroupMissing", learnerGroupMissing);
						errorMsg.put("recordNumber", recordNumber);
						errorMsg.put("firstName", firstName);
						errorMsg.put("lastName", lastName);
						errorMsg.put("emailAddress", emailAddress);
						errorMsg.put("userName", userName);
						errorMsg.put("password", shownPassword);
						errorMsg.put("updateLearner", isUpdate);
						errorMsg.put("invalidFormat", "false");
						errorMsg.put("invalidZip", "false");
						errorMsg.put("zipcode", userAdd);
						errorMsg.put("learnerGroups", learnerGroups);

						valid = false;
						validGroups = false;
					} else {
						LearnerGroup learnerGroup = allLearnerGroups.get(learnerGroupPresent - 1);
						listOfLearnerGroups.add(learnerGroup);
					}
				}
				learner.setCustomer(customer);

				// checking if user account is locked
				if( !accLocked ) {
					vu360User.setAccountNonLocked(true);
				} else {
					vu360User.setAccountNonLocked(false);
				}
				// checking if user account is visible on reports
				if( !accVisible ) {
					vu360User.setVissibleOnReport(false);
				} else {
					vu360User.setVissibleOnReport(true);
				}
				vu360User.setAcceptedEULA(false);
				vu360User.setNewUser(true);
				vu360User.setChangePasswordOnLogin(false);
				vu360User.setCredentialsNonExpired(true);
				vu360User.setEnabled(true);
				vu360User.setLearner(learner);

				/* ******************************************************************** */
				/* ******************************************************************** */
				/* ******************************************************************** */
				/* its time to see if the new users already exists or not               */

				if( valid ) // is it still valid
				{
					if( update == false &&  useralreadyExistingUserList != null ) // is it a new user  
					{
						for( VU360User user : useralreadyExistingUserList ) // loop through all system learners
						{
							if( user.getUsername().equalsIgnoreCase(userName) ) // now match with specific email
							{// if matches then  
								valid = false; // it is no more a valid Learner for adding into the system 

								String isUpdate = "false";
								String learnerAlreadyExists = "true";
								String shownPassword = "";
								for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
									shownPassword = shownPassword + "*";
								errorMsg.put("learnerAlreadyExists", learnerAlreadyExists);
								errorMsg.put("recordNumber", recordNumber);
								errorMsg.put("firstName", firstName);
								errorMsg.put("lastName", lastName);
								errorMsg.put("emailAddress", emailAddress);
								errorMsg.put("userName", userName);
								errorMsg.put("password", shownPassword);
								errorMsg.put("updateLearner", isUpdate);
								errorMsg.put("invalidFormat", "false");
								errorMsg.put("invalidZip", "false");
								errorMsg.put("zipcode", userAdd);
								errorMsg.put("orgGroups", orgGroups);
								errorMsg.put("learnerGroups", learnerGroups);

								errorMessages.add(errorMsg);
								break; // break the loop ,as we are done for this row 
							}
						}
					}
					
					
					
					if( update == true ) {
						VU360User fileUser=vu360UserService.getUserByUsernameAndDomain(userName, null);
						vu360User.setUserGUID(fileUser.getUserGUID());
						long fileCustomerId=fileUser.getLearner().getCustomer().getId();
						long currentCustomerId=customer.getId();
						if(fileCustomerId!=currentCustomerId){
							valid=false;
							errorMsg.put("notCurrentCustomer", true);
							if( validGroups == true  ) {
								errorMessages.add(errorMsg);
							}
						}
					}
				}
				/* ******************************************************************** */
				/* ******************************************************************** */
				/* ******************************************************************** */
				
				if( valid ) {
					if( update == true ) {
						updatedLearners++;
						vu360User.setPassWordChanged(true);
						learner.setVu360User(vu360User);
						learnerService.saveLearner(learner);
						log.debug("updating learner having id = "+learner.getId()+" profile id = "+learner.getLearnerProfile().getId()
								+" address id = "+learner.getLearnerProfile().getLearnerAddress().getId()+" user id = "+vu360User.getId());
						//learnerService.saveLearner(learner);
						
						
						vu360User.setTrainingAdministrator(vu360UserService.getUserById(vu360User.getId()).getTrainingAdministrator());												
						//Since password has been saved above, we need to change the flag to false
						vu360User.setPassWordChanged(false);
						learnerService.saveUser(vu360User);
						
						
						
						
						numOfupdatedLearners.add(errorMsg);
					} else {
						number_of_imported_users++;
						//String passWord = vu360User.getPassword();

						
						vu360User.setUserGUID(GUIDGeneratorUtil.generateGUID());
						learner.setVu360User(vu360User);
						learnerService.addLearner(null, listOfOrgGroups, listOfLearnerGroups, learner);
						existingUserNameList.add(userName);
						learner.getVu360User().setPassword(password);
						//String passWord = vu360User.getPassword();
						addedLearners.add(learner);
						
						
					}
					
					//by OWS for LMS-5182
					if(hasManagerColumn){
						
						
						if(StringUtils.isNotBlank(record[managerColumnIndex])){
							LMSRole managerRole = vu360UserService.getRoleByName("MANAGER", customer);
							StringTokenizer managedOrgGroupTokenizer = new StringTokenizer(record[managerColumnIndex], ":");
							long userIdArray[] = new long[1];
							userIdArray[0]=learner.getVu360User().getId();
							String orgGroupsHierarchy="";
							List<OrganizationalGroup> orgGroupsList=new ArrayList<OrganizationalGroup>();
							
							while( managedOrgGroupTokenizer.hasMoreTokens() ) {
								// getting the org group path -
								orgGroupsHierarchy = managedOrgGroupTokenizer.nextToken().trim();
								orgGroupsList.addAll(this.getOrgGroupToManage(orgGroupsHierarchy,customer));
							
							
							}
							
							
						
							if(orgGroupsList.size()>0)
								learnerService.assignUserToRole(userIdArray,managerRole, "false", orgGroupsList);
						}
					}
					
					
					
				} else if( validGroups == false  ) {
					errorMessages.add(errorMsg);
				}

			} else if( isValidRecord.get("invalidRecord") == true ){

				if( isValidRecord.get("invalidRowNumber") == true ) {
					// invalid line
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("invalidFormat", "true");
					errorMsg.put("updateLearner", "false");
					errorMsg.put("invalidZip", "false");
					errorMessages.add(errorMsg);

				} else {
					if( delimiter.equalsIgnoreCase("|") ) 
						delimiter = "\\|";
					//String[] record = nextLine.split(delimiter,1000);
					String[] record = splitStringWithQuotes(nextLine, delimiter);
					String firstName = record[0];
					String lastName = record[2];
					String zip = record[10];
					String emailAddress = record[12];
					String userName = record[ ( FIX_FIELD_SIZE - 1 )].trim();					
					String orgGroupMissing = "false";
					String orgGroups = "";
					if( record.length < ( FIX_FIELD_SIZE - 1) ) {
						orgGroupMissing = "true";
					} else {
						orgGroups = record[15].trim();
					}
					String learnerGroupMissing = "false";
					String learnerGroups = record[14].trim();
					//String userName = "";
					String isUpdate = "false";
					String password = record[13];
					String shownPassword = "";
					for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
						shownPassword = shownPassword + "*";

					if( isValidRecord.get("firstNameBlank") == true ) {
						firstName = "blank";
					} 
					if( isValidRecord.get("lastNameBlank") == true ) {
						lastName = "blank";
					}
					if( isValidRecord.get("emailAddressBlank") == true ) {
						emailAddress = "blank";
					}
					if( isValidRecord.get("passwordBlank") == true ) {
						password = "blank";
						shownPassword=password;
					}
					if( isValidRecord.get("shortPassword") == true ) {
						password = "shortPassword";
						shownPassword=password;
					}
					// commented on 3 dec , 2009 , to provide different usename and email address
					/*if( !emailAddress.equalsIgnoreCase("blank") ) {
						userName = emailAddress;
					}*/
					if( isValidRecord.get("userNameBlank") == true ) {
						userName = "blank";
					}

					/* #################################### Custom Field ############################################### */
					noteCustomFieldErrors(customFields , errorMsg , isValidRecord, b) ;

					/* ################################################################################################# */

					errorMsg.put("invalidFormat", "false");
					errorMsg.put("invalidZip", "false");
					errorMsg.put("orgGroupMissing", orgGroupMissing);
					errorMsg.put("learnerGroupMissing", learnerGroupMissing);
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("firstName", firstName);
					errorMsg.put("lastName", lastName);
					errorMsg.put("emailAddress", emailAddress);
					errorMsg.put("userName", userName);
					errorMsg.put("password", shownPassword);
					errorMsg.put("updateLearner", isUpdate);
					errorMsg.put("orgGroups", orgGroups);
					errorMsg.put("learnerGroups", learnerGroups);
					errorMsg.put("zipcode", userAdd);

					errorMessages.add(errorMsg);
				}
			}
			else{
				
				log.debug("THIS Record Not Entered: " + recordNumber);
				for(Map <Object, Object> error : errorMessages){
					for(Object key : error.keySet()){
						log.debug("Error List: " + key.toString() + " -----" + error.get(key).toString());
					}
					
					
				}
				
			}
		}
		totalMap.put("recordNumber", recordNumber);
		totalMap.put("totalImportedUsers", number_of_imported_users);
		totalMap.put("totalUpdatedUsers", updatedLearners);
		totalMap.put("numOfupdatedLearners",numOfupdatedLearners);
		totalMap.put("errorMessages", errorMessages);
		totalMap.put("addedLearners", addedLearners);

		return totalMap;
	}	
//	/**
//	 * 
//	 */
//	public Map<Object, Object> batchImportUsers(Map<String, String> requestParametersMap, FileUploadBean bean, Brander brander) throws Exception {
//	
//		Map<Object, Object> context = new HashMap<Object, Object>();
//		Map<Object, Object>  errorMsg = new HashMap<Object, Object> ();
//		VU360User loggedInUser = (VU360User) SecurityContextHolder.getContext() .getAuthentication().getPrincipal();
//		
//		// Read configuration
//		String firstRowHeader = requestParametersMap.get("header");
//		String delimiter = requestParametersMap.get("delimiter");
//		String accLocked = requestParametersMap.get("accLocked");
//		String accVisible = requestParametersMap.get("accVisible");
//		String dupRecords = requestParametersMap.get("dupRecords");
//		log.debug("first Row is Header :: "+firstRowHeader);
//
//		// let's see if there's content there
//		String batchImportLearnersCSVData = bean.getFile();
//		if ( batchImportLearnersCSVData == null || batchImportLearnersCSVData.isEmpty() || batchImportLearnersCSVData.length() == 0 ) {
//			log.debug(" file is null ");
//			errorMsg.put("nullFile", true);
//		} else {
//			errorMsg = readUploadedFile( batchImportLearnersCSVData, delimiter, firstRowHeader, accLocked,
//					accVisible, dupRecords, loggedInUser, brander, bean );
//			errorMsg.put("nullFile", false);
//		}
//		List<Learner> learnersToBeMailed = (List<Learner>) errorMsg.get("addedLearners");
//		
//		if (requestParametersMap.get("registration") != null && requestParametersMap.get("registration") == "yes") {
//			sendEmail(learnersToBeMailed, loggedInUser, brander, requestParametersMap.get("loginURL"), velocityEngine, errorMsg);
//		}
//		
//		context.put("errorMsg", errorMsg);
//		context.put("customFieldHeaders", bean.getFileHeader());
//		context.put("customFieldHash", bean.getCfHash());
//		
//		return context;
//	}
	
	private void sendEmail(List <Learner>learnersToBeMailed, VU360User loggedInUser, Brander brander, String loginURL, 
			VelocityEngine velocityEngine, Map<Object, Object>  errorMsg, Customer customer) {
		
		try {
//			Customer customer = null;
//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
//				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
//				if( details.getCurrentCustomer() != null ){
//					customer = details.getCurrentCustomer();
//				}
//			}
			
			for( int i=0 ; i<learnersToBeMailed.size(); i++ ) {
				Map<String, Object> model = new HashMap <String, Object>();
				model.put("customerName", customer.getName());
				model.put("loggedInUser", loggedInUser);
				VU360User user= learnersToBeMailed.get(i).getVu360User();
				model.put("user", user);
				model.put("url", loginURL);
				model.put("brander", brander);

				String batchImportTemplatePath =  brander.getBrandElement("lms.email.batchUpload.body");
				String fromAddress =  brander.getBrandElement("lms.email.batchUpload.fromAddress");
				String fromCommonName =  brander.getBrandElement("lms.email.batchUpload.fromCommonName");
				String subject =  brander.getBrandElement("lms.email.batchUpload.subject");
				String support =  brander.getBrandElement("lms.email.batchUpload.fromCommonName");
				model.put("support", support);
				/*START-BRANDING EMAILTEMPLATE WORK*/
    			String templateText=brander.getBrandElement("lms.branding.email.accountDetails.templateText");
    			String lmsDomain=VU360Properties.getVU360Property("lms.domain");
    			String loginUrl=lmsDomain.concat("/lms/login.do?brand=").concat(brander.getName());
    			
    			templateText=templateText.replaceAll("&lt;firstname&gt;",user.getFirstName());			                
                templateText=templateText.replaceAll("&lt;lastname&gt;",user.getLastName());
                templateText=templateText.replaceAll("&lt;username&gt;",user.getUsername());
                templateText=templateText.replaceAll("&lt;password&gt;",user.getPassword());            			                            
                templateText=templateText.replaceAll("&lt;loginurl&gt;", loginUrl);                                                
                
                model.put("templateText", templateText);
    			/*END BRANDING EMAIL TEMPLATE WORK*/
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, batchImportTemplatePath, model);
				SendMailService.sendSMTPMessage(learnersToBeMailed.get(i).
						getVu360User().getEmailAddress(), fromAddress, fromCommonName, 
						subject, text);      
			}
			
			Map<String, Object> model = new HashMap <String, Object>();
			model.put("errorMsg", errorMsg);

			Calendar calender=Calendar.getInstance();
			Date importDate=calender.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			String toDate = formatter.format(importDate);
			//commented by Faisal A. Siddiqui July 21 09 for new branding
			//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());

			String batchImportTemplatePath =  brander.getBrandElement("lms.email.batchUpload.managerMail.body");
			String fromAddress =  brander.getBrandElement("lms.email.batchUpload.managerMail.fromAddress");
			String fromCommonName =  brander.getBrandElement("lms.email.batchUpload.managerMail.fromCommonName");
			String subject =  brander.getBrandElement("lms.emil.batchUpload.managerMail.subject")+ toDate;
			String support =  brander.getBrandElement("lms.email.batchUpload.managerMail.fromCommonName");
			
			String lmsDomain="";
			lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
			model.put("lmsDomain",lmsDomain);
			model.put("support", support);
			String text = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, batchImportTemplatePath, model);
			SendMailService.sendSMTPMessage(loggedInUser.getEmailAddress(), fromAddress, 
					fromCommonName, subject, text);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}
	
	private Map<Object, Object> readUploadedFile(String file, String delimiter, String firstRowHeader, String accLocked,
			String accVisible, String dupRecords, VU360User loggedInUser, Brander b , FileUploadBean fileBean) throws IOException {

		FileRecordValidator validator = new FileRecordValidator();
		Map <Object, Object> totalMap = new HashMap<Object, Object>();
		Map <Object, Object> errorMsg = new HashMap<Object, Object>();
		List <Map<Object, Object>> errorMessages = new ArrayList<Map<Object, Object>>();
		List <Map<Object, Object>> numOfupdatedLearners = new ArrayList<Map<Object, Object>>();
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext()
				.getAuthentication().getDetails()).getCurrentCustomer();

		List<com.softech.vu360.lms.model.CustomField> allCustomFieldsList = this.getAllCustomFields(customer);
		Map<Integer, CustomField> customFieldHash = new HashMap<Integer, CustomField>(); 

		String header = null  ;
		String headerRow[] ={};
		String userAdd = "";
		boolean hasManagerColumn = false;
		int managerColumnIndex = 0;
		
		BufferedReader is = new BufferedReader(new StringReader(file));


		Map<CustomField,List<CustomFieldValueChoice>> customFieldValueChoices=new HashMap<CustomField,List<CustomFieldValueChoice>>();
		if( StringUtils.isBlank(firstRowHeader) || firstRowHeader.equalsIgnoreCase("yes") ){
			header = is.readLine();
			
			if( delimiter.equalsIgnoreCase("|") ) 
				delimiter = "\\|";

			//headerRow = header.split(delimiter,1000);
			headerRow = splitStringWithQuotes(header, delimiter);
			
			if(headerRow.length>FIX_FIELD_SIZE){
				
				for(int i=FIX_FIELD_SIZE;i<headerRow.length;i++){
					if(headerRow[i].toString().equalsIgnoreCase("Manager Org Group")){
						hasManagerColumn=true;
						managerColumnIndex=i;
						break;
					}
				}
			}
			customFieldHash = getCustomFieldsInFile(headerRow , allCustomFieldsList);
			fileBean.setCfHash( customFieldHash );
			customFieldValueChoices=getCustomFieldChoices(allCustomFieldsList);
		}

		int recordNumber = 0;
		int number_of_imported_users = 0;
		int updatedLearners = 0;
		String nextLine;
		Map<String, Boolean> isValidRecord;
		List<VU360User> userList = new ArrayList<VU360User>();
		List<VU360User> useralreadyExistingUserList = new ArrayList<VU360User>();
		List<String> existingUserNameList = new ArrayList <String>();

		// names of all org & learner groups are stored here.
		List <LearnerGroup>allLearnerGroups = orgGroupService.getAllLearnerGroups(customer.getId(),loggedInUser);

		Set<LMSRole> userRoles = new HashSet<LMSRole>();
		//LMSRole lmsRole =securityAndRolesService.getLearnerRoleTemplateByCustomer(customer); //vu360UserService.getDefaultRole(customer);
		LMSRole lmsRole =vu360UserService.getDefaultRole(customer);//by OWS for LMS-3577

		if ( lmsRole == null ) {
			lmsRole = vu360UserService.getRoleByName("LEARNER", customer);
		}

		/* ******************************************************************** */
		/* ******************************************************************** */
		/* ******************************************************************** */
		/* to pass email batch upload file email addresses to IN Query*/

		List<String> userNameList = new ArrayList<String>();

		String[] strRow;
		while((nextLine=is.readLine())!=null)
		{
			strRow=this.splitStringWithQuotes(nextLine, delimiter);
			recordNumber++;
			isValidRecord = validator.isValidRow(strRow, customFieldHash,customFieldValueChoices,hasManagerColumn,managerColumnIndex);
			log.debug("line record is valid - "+isValidRecord);

			if( isValidRecord.get("invalidRecord") == false ) 
			{
				if( delimiter.equalsIgnoreCase("|") ) 
					delimiter = "\\|";
				//String[] record2 = nextLine.split(delimiter,1000);
				String[] record2 =  strRow;
				String[] record = new String[record2.length];
				if( record2.length == 14 && nextLine.charAt(nextLine.length()-1) == delimiter.charAt(0) ) {
					for( int i=0 ; i<14 ; i++ ) {
						record[i] = record2[i];
					}
					record[14] = "";
					record[15] = "";
				} else {
					record = record2;
				}

				if(record.length >= FIX_FIELD_SIZE)
					userNameList.add(record[16].trim());
			}
		}
		useralreadyExistingUserList = learnerService.findAllSystemLearners(userNameList);

		is = new BufferedReader(new StringReader(file));
		if( StringUtils.isBlank(firstRowHeader) || firstRowHeader.equalsIgnoreCase("yes") ){
			is.readLine();
		} 
		recordNumber = 0 ;

		/* ******************************************************************** */
		/* ******************************************************************** */
		/* ******************************************************************** */

		List <Learner>addedLearners = new ArrayList <Learner>();

		while((nextLine=is.readLine())!=null) {
			strRow=this.splitStringWithQuotes(nextLine,delimiter);
			errorMsg = new HashMap<Object, Object>();
			recordNumber++;
			isValidRecord = validator.isValidRow(strRow, customFieldHash,customFieldValueChoices,hasManagerColumn,managerColumnIndex);
			log.debug("line record is valid - "+isValidRecord);
			if( delimiter.equalsIgnoreCase("|") ) 
				delimiter = "\\|";
			// populate custom field values 
			//getCustomFieldRowValues( nextLine.split(delimiter,1000) ,  fileBean.getFileHeader(),  errorMsg ,customFieldHash );
			getCustomFieldRowValues( strRow ,  fileBean.getFileHeader(),  errorMsg ,customFieldHash );
			if( isValidRecord.get("invalidRecord") == false ) {

				VU360User vu360User = new VU360User();
				Learner learner = new Learner();
				LearnerProfile learnerProfile = new LearnerProfile();
				Address address = new Address();
				Address address2 = new Address();
				boolean update = false;
				boolean valid = true;
				boolean validGroups = true;
				long userId = 0;

				if( delimiter.equalsIgnoreCase("|") ) 
					delimiter = "\\|";
				//String[] record2 = nextLine.split(delimiter,1000);
				String[] record2 =  strRow;
				String[] record = new String[record2.length];
				if( record2.length == 14 && nextLine.charAt(nextLine.length()-1) == delimiter.charAt(0) ) {
					for( int i=0 ; i<14 ; i++ ) {
						record[i] = record2[i];
					}
					record[14] = "";
					record[15] = "";
				} else {
					record = record2;
				}

				/* *******************custom field work ******************************* */

				List<CustomFieldValue> CustomFieldValueList = new ArrayList<CustomFieldValue>(); 
				if( record.length > FIX_FIELD_SIZE  ) {
					
					for( int i= FIX_FIELD_SIZE ; i < record.length ; i++ ){
						CustomField cf = customFieldHash.get(new Integer(i));
						if( cf != null ){
							CustomFieldValue cfv = this.getCustomFieldValueByCustomField(cf, record[i].toString());
							if(cfv!=null)
								CustomFieldValueList.add(cfv);
						}
	
					}
				
				}
				if( learnerProfile.getCustomFieldValues() == null )
					learnerProfile.setCustomFieldValues(CustomFieldValueList);
				else
					learnerProfile.getCustomFieldValues().addAll(CustomFieldValueList);

				/* *******************custom field work ******************************* */


				String firstName = record[0].trim();
				vu360User.setFirstName(firstName);
				vu360User.setMiddleName(record[1].trim());
				String lastName = record[2].trim();
				vu360User.setLastName(lastName);
				userRoles.add(lmsRole);
				vu360User.setLmsRoles(userRoles);
				vu360User.setLmsAdministrator(null);
				//vu360User.setTrainingAdministrator(null);
				vu360User.setExpirationDate(null);
				learnerProfile.setOfficePhone(record[3].trim());
				learnerProfile.setOfficePhoneExtn(record[4].trim());
				learnerProfile.setMobilePhone(record[5].trim());

				// These variables are used for validation
				String state = record[9].trim();
				String zip = record[10].trim();
				String country = record[11].trim();

				address.setStreetAddress(record[6].trim());
				address.setStreetAddress2(record[7].trim());
				address.setCity(record[8].trim());
				address.setState(record[9].trim());
				address.setZipcode(record[10].trim());
				address.setCountry(record[11].trim());

				address2.setStreetAddress("");
				address2.setStreetAddress2("");
				address2.setCity("");
				address2.setState("");
				address2.setZipcode("");
				address2.setCountry("");

				userAdd = address.getStreetAddress()+" "+address.getStreetAddress2()+" "+address.getCity()+" "+address.getState();
				String emailAddress = record[12].trim();
				String userName = "";

				if(record.length > 16 )
					userName = record[ ( FIX_FIELD_SIZE - 1 )].trim();
				else{
					valid = false;
					errorMsg.put("userNameMissing", "true" );
				}


				String password = record[13].trim();

				String learnerGroups = record[14].trim();

				String orgGroups = "";
				if(record.length>15)
					orgGroups = record[15].trim();

				boolean duplicate = false;
				//				for( String userNameString : existingUserNameList) {
				//					if( userName.equalsIgnoreCase(userNameString) ) {
				//						duplicate = true;
				//						break;
				//					}
				//				}
				VU360User usr=vu360UserService.findUserByUserName(userName);
				if(usr!=null) duplicate = true;

				if( !duplicate ) {
					update = false;
				} else {
					if( dupRecords != null && dupRecords.equalsIgnoreCase("update") ) {

						String orgGroupMissing = "false";
						String learnerGroupMissing = "false";
						String isUpdate = "true";
						String shownPassword = "";
						for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
							shownPassword = shownPassword + "*";
						
						List<VU360User> vU360User = vu360UserService.getUsersByEmailAddress(emailAddress);
						if( vU360User != null && !vU360User.isEmpty() ) {
							VU360User presentVu360User = vU360User.get(0);
							userId = presentVu360User.getId();
							vu360User.setId(userId);
							learner.setId(presentVu360User.getLearner().getId());
							address.setId(presentVu360User.getLearner().getLearnerProfile().getLearnerAddress().getId());
							address2.setId(presentVu360User.getLearner().getLearnerProfile().getLearnerAddress2().getId());
							learnerProfile.setId(presentVu360User.getLearner().getLearnerProfile().getId());
						}
						errorMsg.put("orgGroupMissing", orgGroupMissing);
						errorMsg.put("learnerGroupMissing", learnerGroupMissing);
						errorMsg.put("recordNumber", recordNumber);
						errorMsg.put("firstName", firstName);
						errorMsg.put("lastName", lastName);
						errorMsg.put("emailAddress", emailAddress);
						errorMsg.put("userName", userName);
						errorMsg.put("password", shownPassword);
						errorMsg.put("updateLearner", isUpdate);
						errorMsg.put("invalidFormat", "false");
						errorMsg.put("invalidZip", "false");
						errorMsg.put("orgGroups", orgGroups);
						errorMsg.put("learnerGroups", learnerGroups);
						errorMsg.put("zipcode", userAdd);
						update = true;
					} else {
						valid = false;
					}
				}
				/*
				 *  ################################
				 *  Validation of State, Country and 
				 *  Zip code/postal code is done here...
				 */
				boolean validCountry = false;
				boolean validState = false;
				String stateBean = "";
				List<LabelBean> countries = b.getBrandMapElements("lms.manageUser.AddLearner.Country");

				for( LabelBean bean : countries ) {
					if( country.equalsIgnoreCase(bean.getValue()) || country.equalsIgnoreCase(bean.getLabel()) ) {
						stateBean = bean.getValue();
						address.setCountry(stateBean);
						validCountry = true;
						break;
					}
				}
				List<LabelBean> states = null;
				if( stateBean.isEmpty() || stateBean.equalsIgnoreCase("US") || stateBean.equalsIgnoreCase("United States") ) {
					states = b.getBrandMapElements("lms.manageUser.AddLearner.State");
				} else {
					states = b.getBrandMapElements("lms.manageUser.AddLearner."+stateBean+".State");
				}

				for( LabelBean bean : states ) {
					if( state.equalsIgnoreCase(bean.getValue()) || state.equalsIgnoreCase(bean.getLabel()) ) {
						address.setState(bean.getValue());
						validState = true;
						break;
					}
				} 
				if( !validCountry || !validState || ! ZipCodeValidator.isZipCodeValid( stateBean, zip, b, log) ) {

					String invalid = "invalidZip";
					if( !validCountry ) {
						invalid = "invalidCountry";
					}
					if( !validState ) {
						invalid = "invalidState";
					}
					String isUpdate = "false";
					String orgGroupMissing = "false";
					String shownPassword = "";
					for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
						shownPassword = shownPassword + "*";
					errorMsg.put("orgGroupMissing", orgGroupMissing);
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("firstName", firstName);
					errorMsg.put("lastName", lastName);
					errorMsg.put("emailAddress", emailAddress);
					errorMsg.put("userName", userName);
					errorMsg.put("password", shownPassword);
					errorMsg.put("updateLearner", isUpdate);
					errorMsg.put("invalidFormat", "false");
					errorMsg.put("invalidZip", invalid);
					errorMsg.put("orgGroups", orgGroups);
					errorMsg.put("learnerGroups", learnerGroups);

					valid = false;
					validGroups = false;
				}
				// ############  end of validation  ###########

				learnerProfile.setLearnerAddress(address);
				learnerProfile.setLearnerAddress2(address2);
				learner.setLearnerProfile(learnerProfile);
				learnerProfile.setLearner(learner);

				vu360User.setEmailAddress(emailAddress);
				vu360User.setPassword(password);
				vu360User.setUsername(userName);

				if( valid == false && ( userName.isEmpty()  ) ) {

					String isUpdate = "false";
					String orgGroupMissing = "false";
					String userNameMissing = "true";
					String shownPassword = "";
					for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
						shownPassword = shownPassword + "*";
					errorMsg.put("userNameMissing", userNameMissing);
					errorMsg.put("orgGroupMissing", orgGroupMissing);
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("firstName", firstName);
					errorMsg.put("lastName", lastName);
					errorMsg.put("emailAddress", emailAddress);
					errorMsg.put("userName", userName);
					errorMsg.put("password", shownPassword);
					errorMsg.put("updateLearner", isUpdate);
					errorMsg.put("invalidFormat", "false");
					errorMsg.put("invalidZip", "false");
					errorMsg.put("zipcode", userAdd);
					errorMsg.put("orgGroups", orgGroups);
					errorMsg.put("learnerGroups", learnerGroups);

					valid = false;
					validGroups = false;
				}				

				if( orgGroups == null || orgGroups.isEmpty() ) {

					String isUpdate = "false";
					String orgGroupMissing = "true";
					String shownPassword = "";
					for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
						shownPassword = shownPassword + "*";
					errorMsg.put("orgGroupMissing", orgGroupMissing);
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("firstName", firstName);
					errorMsg.put("lastName", lastName);
					errorMsg.put("emailAddress", emailAddress);
					errorMsg.put("userName", userName);
					errorMsg.put("password", shownPassword);
					errorMsg.put("updateLearner", isUpdate);
					errorMsg.put("invalidFormat", "false");
					errorMsg.put("invalidZip", "false");
					errorMsg.put("zipcode", userAdd);
					errorMsg.put("learnerGroups", learnerGroups);

					valid = false;
					validGroups = false;
				}
				log.debug("org group :: "+orgGroups);

				//let me collect the org groups with path
				StringTokenizer orgGroupTokenizer = new StringTokenizer(orgGroups, ":");
				List <OrganizationalGroup>listOfOrgGroups = new ArrayList <OrganizationalGroup>();

				/*
				 * let me check whether this is valid
				 * GetRootOrg
				 * check existance
				 * check manager's permission on parent org group
				 * if yes 
				 * if exists assign unless create then assign
				 * else show error
				 */
				while( orgGroupTokenizer.hasMoreTokens() ) {
					// getting the org group path -
					String orgGroupList = orgGroupTokenizer.nextToken().trim();
					OrganizationalGroup createdOrgGroup = orgGroupService.getOrgGroupsByHiererchyString(SPLITTER, orgGroupList, customer,loggedInUser);
					if ( createdOrgGroup != null ) {
						listOfOrgGroups.add(createdOrgGroup);
						log.debug("CREATED ORG GRP -- "+createdOrgGroup.getName());
					}
					else {
						String isUpdate = "false";
						String orgGroupMissing = "true";
						String shownPassword = "";
						for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
							shownPassword = shownPassword + "*";
						errorMsg.put("orgGroupMissing", orgGroupMissing);
						errorMsg.put("recordNumber", recordNumber);
						errorMsg.put("firstName", firstName);
						errorMsg.put("lastName", lastName);
						errorMsg.put("emailAddress", emailAddress);
						errorMsg.put("userName", userName);
						errorMsg.put("password", shownPassword);
						errorMsg.put("updateLearner", isUpdate);
						errorMsg.put("invalidFormat", "false");
						errorMsg.put("invalidZip", "false");
						errorMsg.put("orgGroups", orgGroups);
						errorMsg.put("learnerGroups", learnerGroups);
						errorMsg.put("zipcode", userAdd);
						valid = false;
						validGroups = false;
					}
				}

				log.debug("lear group :: "+learnerGroups);
				StringTokenizer learnerGroupTokenizer = new StringTokenizer(learnerGroups, ":");
				List <LearnerGroup>listOfLearnerGroups = new ArrayList <LearnerGroup>();
				while(learnerGroupTokenizer.hasMoreTokens()) {
					String learnerGroupName = learnerGroupTokenizer.nextToken().trim();
					int learnerGroupPresent = 0;
					int index = 0;
					for( LearnerGroup lGroup : allLearnerGroups ) {
						index++;
						if( lGroup.getName().equalsIgnoreCase(learnerGroupName) ) {
							learnerGroupPresent = index;
						}
					}
					if( learnerGroupPresent == 0 ) {

						String isUpdate = "false";
						String learnerGroupMissing = "true";
						String shownPassword = "";
						for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
							shownPassword = shownPassword + "*";
						errorMsg.put("learnerGroupMissing", learnerGroupMissing);
						errorMsg.put("recordNumber", recordNumber);
						errorMsg.put("firstName", firstName);
						errorMsg.put("lastName", lastName);
						errorMsg.put("emailAddress", emailAddress);
						errorMsg.put("userName", userName);
						errorMsg.put("password", shownPassword);
						errorMsg.put("updateLearner", isUpdate);
						errorMsg.put("invalidFormat", "false");
						errorMsg.put("invalidZip", "false");
						errorMsg.put("zipcode", userAdd);
						errorMsg.put("learnerGroups", learnerGroups);

						valid = false;
						validGroups = false;
					} else {
						LearnerGroup learnerGroup = allLearnerGroups.get(learnerGroupPresent - 1);
						listOfLearnerGroups.add(learnerGroup);
					}
				}
				learner.setCustomer(customer);

				// checking if user account is locked
				if( StringUtils.isBlank(accLocked) || accLocked.equalsIgnoreCase("no") ) {
					vu360User.setAccountNonLocked(true);
				} else {
					vu360User.setAccountNonLocked(false);
				}
				// checking if user account is visible on reports
				if( StringUtils.isBlank(accVisible) || accVisible.equalsIgnoreCase("no") ) {
					vu360User.setVissibleOnReport(false);
				} else {
					vu360User.setVissibleOnReport(true);
				}
				vu360User.setAcceptedEULA(false);
				vu360User.setNewUser(true);
				vu360User.setChangePasswordOnLogin(false);
				vu360User.setCredentialsNonExpired(true);
				vu360User.setEnabled(true);
				vu360User.setLearner(learner);

				/* ******************************************************************** */
				/* ******************************************************************** */
				/* ******************************************************************** */
				/* its time to see if the new users already exists or not               */

				if( valid ) // is it still valid
				{
					if( update == false &&  useralreadyExistingUserList != null ) // is it a new user  
					{
						for( VU360User user : useralreadyExistingUserList ) // loop through all system learners
						{
							if( user.getUsername().equalsIgnoreCase(userName) ) // now match with specific email
							{// if matches then  
								valid = false; // it is no more a valid Learner for adding into the system 

								String isUpdate = "false";
								String learnerAlreadyExists = "true";
								String shownPassword = "";
								for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
									shownPassword = shownPassword + "*";
								errorMsg.put("learnerAlreadyExists", learnerAlreadyExists);
								errorMsg.put("recordNumber", recordNumber);
								errorMsg.put("firstName", firstName);
								errorMsg.put("lastName", lastName);
								errorMsg.put("emailAddress", emailAddress);
								errorMsg.put("userName", userName);
								errorMsg.put("password", shownPassword);
								errorMsg.put("updateLearner", isUpdate);
								errorMsg.put("invalidFormat", "false");
								errorMsg.put("invalidZip", "false");
								errorMsg.put("zipcode", userAdd);
								errorMsg.put("orgGroups", orgGroups);
								errorMsg.put("learnerGroups", learnerGroups);

								errorMessages.add(errorMsg);
								break; // break the loop ,as we are done for this row 
							}
						}
					}
				}
				/* ******************************************************************** */
				/* ******************************************************************** */
				/* ******************************************************************** */
				if( update == true ) {
					VU360User fileUser=vu360UserService.getUserByUsernameAndDomain(userName, null);
					vu360User.setUserGUID(fileUser.getUserGUID());
					long fileCustomerId=fileUser.getLearner().getCustomer().getId();
					long currentCustomerId=customer.getId();
					if(fileCustomerId!=currentCustomerId){
						valid=false;
						errorMsg.put("notCurrentCustomer", true);
						if( validGroups == true  ) {
							errorMessages.add(errorMsg);
						}
					}
				}
				if( valid ) {
					if( update == true ) {
						updatedLearners++;
						vu360User.setPassWordChanged(true);
						learner.setVu360User(vu360User);
						learnerService.saveLearner(learner);
						log.debug("updating learner having id = "+learner.getId()+" profile id = "+learner.getLearnerProfile().getId()
								+" address id = "+learner.getLearnerProfile().getLearnerAddress().getId()+" user id = "+vu360User.getId());
						//learnerService.saveLearner(learner);
						learnerService.saveUser(vu360User);
						numOfupdatedLearners.add(errorMsg);
					} else {
						number_of_imported_users++;
						//String passWord = vu360User.getPassword();
						vu360User.setUserGUID(GUIDGeneratorUtil.generateGUID());
						learner.setVu360User(vu360User);
						learnerService.addLearner(null, listOfOrgGroups, listOfLearnerGroups, learner);
						existingUserNameList.add(userName);
						learner.getVu360User().setPassword(password);
						//String passWord = vu360User.getPassword();
						addedLearners.add(learner);
					}
					
					//by OWS for LMS-5182
					if(hasManagerColumn){
						
						
						if(StringUtils.isNotBlank(record[managerColumnIndex])){
							LMSRole managerRole = vu360UserService.getRoleByName("MANAGER", customer);
							StringTokenizer managedOrgGroupTokenizer = new StringTokenizer(record[managerColumnIndex], ":");
							long userIdArray[] = new long[1];
							userIdArray[0]=learner.getVu360User().getId();
							String orgGroupsHierarchy="";
							List<OrganizationalGroup> orgGroupsList=new ArrayList<OrganizationalGroup>();
							
							while( managedOrgGroupTokenizer.hasMoreTokens() ) {
								// getting the org group path -
								orgGroupsHierarchy = managedOrgGroupTokenizer.nextToken().trim();
								orgGroupsList.addAll(this.getOrgGroupToManage(orgGroupsHierarchy,customer));
							
							
							}
							
							
							if(orgGroupsList.size()>0)					
								learnerService.assignUserToRole(userIdArray,managerRole, "false", orgGroupsList);
						}
					}
					
					
					
					
				} else if( validGroups == false  ) {
					errorMessages.add(errorMsg);
				}

			} else if( isValidRecord.get("invalidRecord") == true ){

				if( isValidRecord.get("invalidRowNumber") == true ) {
					// invalid line
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("invalidFormat", "true");
					errorMsg.put("updateLearner", "false");
					errorMsg.put("invalidZip", "false");
					errorMessages.add(errorMsg);

				} else {
					if( delimiter.equalsIgnoreCase("|") ) 
						delimiter = "\\|";
					//String[] record = nextLine.split(delimiter,1000);
					String[] record = splitStringWithQuotes(nextLine, delimiter);
					String firstName = record[0];
					String lastName = record[2];
					String zip = record[10];
					String emailAddress = record[12];
					String userName = record[ ( FIX_FIELD_SIZE - 1 )].trim();					
					String orgGroupMissing = "false";
					String orgGroups = "";
					if( record.length < ( FIX_FIELD_SIZE - 1) ) {
						orgGroupMissing = "true";
					} else {
						orgGroups = record[15].trim();
					}
					String learnerGroupMissing = "false";
					String learnerGroups = record[14].trim();
					//String userName = "";
					String isUpdate = "false";
					String password = record[13];
					String shownPassword = "";
					for( int passwordLength = 0; passwordLength<password.length(); passwordLength ++ )
						shownPassword = shownPassword + "*";

					if( isValidRecord.get("firstNameBlank") == true ) {
						firstName = "blank";
					} 
					if( isValidRecord.get("lastNameBlank") == true ) {
						lastName = "blank";
					}
					if( isValidRecord.get("emailAddressBlank") == true ) {
						emailAddress = "blank";
					}
					if( isValidRecord.get("passwordBlank") == true ) {
						password = "blank";
						shownPassword=password;
					}
					if( isValidRecord.get("shortPassword") == true ) {
						password = "shortPassword";
						shownPassword=password;
					}
					// commented on 3 dec , 2009 , to provide different usename and email address
					/*if( !emailAddress.equalsIgnoreCase("blank") ) {
						userName = emailAddress;
					}*/
					if( isValidRecord.get("userNameBlank") == true ) {
						userName = "blank";
					}

					/* #################################### Custom Field ############################################### */
					noteCustomFieldErrors(fileBean.getFileHeader() , errorMsg , isValidRecord, b) ;

					/* ################################################################################################# */

					errorMsg.put("invalidFormat", "false");
					errorMsg.put("invalidZip", "false");
					errorMsg.put("orgGroupMissing", orgGroupMissing);
					errorMsg.put("learnerGroupMissing", learnerGroupMissing);
					errorMsg.put("recordNumber", recordNumber);
					errorMsg.put("firstName", firstName);
					errorMsg.put("lastName", lastName);
					errorMsg.put("emailAddress", emailAddress);
					errorMsg.put("userName", userName);
					errorMsg.put("password", shownPassword);
					errorMsg.put("updateLearner", isUpdate);
					errorMsg.put("orgGroups", orgGroups);
					errorMsg.put("learnerGroups", learnerGroups);
					errorMsg.put("zipcode", userAdd);

					errorMessages.add(errorMsg);
				}
			}
		}
		totalMap.put("recordNumber", recordNumber);
		totalMap.put("totalImportedUsers", number_of_imported_users);
		totalMap.put("totalUpdatedUsers", updatedLearners);
		totalMap.put("numOfupdatedLearners",numOfupdatedLearners);
		totalMap.put("errorMessages", errorMessages);
		totalMap.put("addedLearners", addedLearners);

		return totalMap;
	}
	
	

	/**
	 * checking if the file has more than 500 records
	 * @param csvData
	 * @param firstRowHeader
	 */
	private boolean isValidFile( String csvData, String firstRowHeader,Brander brander) throws IOException {
		boolean isValid;
		FileRecordValidator validator = new FileRecordValidator();
		isValid = validator.isValidFile(csvData, firstRowHeader,brander);
		return isValid;
	}
	
	private boolean isValidDelimiter(String csvData, String firstRowHeader,String delimiter)throws IOException {
		boolean isValid;
		FileRecordValidator validator = new FileRecordValidator();
		isValid = validator.isValidDelimiter(csvData, firstRowHeader,delimiter);
		return isValid;
	}
	
	private List<String> validateFileForCustomFields(String delimiter, String csvData , FileUploadBean bean, Customer customer)throws IOException{
		BufferedReader is = new BufferedReader(new StringReader(csvData));
		String headerRow=is.readLine();
		String[] headerCols=null;
		if( delimiter.equalsIgnoreCase("|") ) 
			delimiter = "\\|";

		boolean hasManagerColumn = false;
		int managerColumnIndex=0;
		//headerCols=headerRow.split(delimiter,1000);
		headerCols=splitStringWithQuotes(headerRow, delimiter);
		
		if(headerCols.length>FIX_FIELD_SIZE){
			
			for(int i=FIX_FIELD_SIZE;i<headerCols.length;i++){
				if(headerCols[i].toString().equalsIgnoreCase("Manager Org Group")){
					hasManagerColumn=true;
					managerColumnIndex=i;
					break;
				}
			}
		}
		
		
		List<String> requiredFieldsNotFoundInHeader=new ArrayList<String>();
		boolean applyValidation=false;
		List<CustomField> allFields=this.getAllCustomFields(customer);
		if(allFields.size()>0)
		{
			bean.setFileHeader(allFields);
			boolean fieldFound=false;

			for(CustomField field:allFields)
			{
				fieldFound=false;
				if(field.getFieldRequired())
				{
					applyValidation=true;
					
					if(hasManagerColumn){
						for(int i= (FIX_FIELD_SIZE );i<headerCols.length;i++)
						{
							if(field.getFieldLabel().trim().equalsIgnoreCase(headerCols[i]))
							{
								fieldFound=true;
								break;
							}
						}
					}
					else{
						for(int i= (FIX_FIELD_SIZE - 1 );i<headerCols.length;i++)
						{
							if(field.getFieldLabel().trim().equalsIgnoreCase(headerCols[i]))
							{
								fieldFound=true;
								break;
							}
						}
						
					}
					if(!fieldFound)
					{
						requiredFieldsNotFoundInHeader.add(field.getFieldLabel());
					}
				}
			}
		}
		if(applyValidation && requiredFieldsNotFoundInHeader.size()>0)
		{
			return requiredFieldsNotFoundInHeader;	
		}
		else
			return null;
	}
	

	/**
	 * @author Faisal A. Siddiqui
		this method iterate the custom field list and matches the custom field label with colunms in header of uploaded file	 
	 * @param headerRows, Array that contain column name specified in batch import file
	 * @param customFields, List of Custom Field
	 * @return HashMap<Index No. in File, CustomField Object>
	 * @see LMS-3479
	 */
	private HashMap<Integer,CustomField> getCustomFieldsInFile(String[] headerRows,List<com.softech.vu360.lms.model.CustomField> customFields)
	{
		HashMap<Integer,CustomField> customFieldMap=new HashMap<Integer,CustomField>();
		int loop=0;
		for(com.softech.vu360.lms.model.CustomField customField:customFields)
		{
			for(loop=0;loop<headerRows.length;loop++)
			{
				if(headerRows[loop].trim().equalsIgnoreCase(customField.getFieldLabel().trim()))
				{
					customFieldMap.put(Integer.valueOf(loop), customField);
					break;				
				}
			}
		}
		return customFieldMap;
	}

	public  String[] splitStringWithQuotes (String inputString, String delimiter )	{
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


	private void getCustomFieldRowValues(String [] record , List<CustomField> cfList, Map<Object,Object> errorMsg , Map<Integer, CustomField> cfHash){
		
		int rowArrayCount = 0;
		for( com.softech.vu360.lms.model.CustomField cf :cfList){
			int customFieldArrayIndex = ( FIX_FIELD_SIZE + rowArrayCount ); 
			//errorMsg.put(cfHash.get(new Integer(customFieldArrayIndex)).getFieldLabel(), "");
			if(record.length > customFieldArrayIndex ){
				if(cfHash.get(new Integer(customFieldArrayIndex)) !=null )
					errorMsg.put(cfHash.get(new Integer(customFieldArrayIndex)).getFieldLabel(), record[customFieldArrayIndex]);

				errorMsg.put(cf.getId(), cf);
			}
			else
				break;
			rowArrayCount++ ;
		}
	}


	/**
	 * @author Adeel Hussain
	 * @param Customg Field, this is a custom field specified by Customer or Distributor
	 * @param value, specified in uploaded batch import file
	 * @return CustomgFieldValue, this is desirable object that would be set in LearnerProfile
	 */
	private CustomFieldValue getCustomFieldValueByCustomField(CustomField customField,String value)
	{
		CustomFieldValue valueObj=new CustomFieldValue();

		if (customField instanceof SingleSelectCustomField || customField instanceof MultiSelectCustomField ) {

			List<CustomFieldValueChoice> customFieldValueChoices = customFieldService.getOptionsByCustomField(customField);
			List<CustomFieldValueChoice> customFieldValueSelectedChoices = new ArrayList<CustomFieldValueChoice>();

			boolean choicesAreGood = false;
			if (customField instanceof SingleSelectCustomField ){

				choicesAreGood = false ;
				for( CustomFieldValueChoice customFieldValueChoice : customFieldValueChoices ){
					if( customFieldValueChoice.getLabel().trim().equalsIgnoreCase(value.trim())) {
						choicesAreGood = true ;
						customFieldValueSelectedChoices.add(customFieldValueChoice);
						break ; //because it is single select
					}
				}
				if( choicesAreGood ){
					valueObj.getValueItems().addAll(customFieldValueSelectedChoices );
					valueObj.setValue( value );
					valueObj.setCustomField(customField);
				}
			}
			if (customField instanceof MultiSelectCustomField ){
				String [] valueList = value.split(">");
				String someValue = "";

				for( String str : valueList ){
					choicesAreGood = false ;
					for( CustomFieldValueChoice customFieldValueChoice : customFieldValueChoices ){
						if( customFieldValueChoice.getLabel().trim().equalsIgnoreCase(str.trim())) {
							choicesAreGood = true ;
							customFieldValueSelectedChoices.add(customFieldValueChoice);
							someValue = str ;
						}
					}
					if( ! choicesAreGood )
						break;
				}
				if( choicesAreGood ){
					valueObj.setValueItems(customFieldValueSelectedChoices );
					//valueObj.setValue( value );
					valueObj.setCustomField(customField);
				}
			}	
		} else {
			//if() custom field type is date then check format
			//if() customfield encrypted is true then encrypt it
			if(customField instanceof DateTimeCustomField){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

				try {
					value = FormUtil.getInstance().formatDate(sdf.parse(value),"MM/dd/yyyy");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			valueObj.setCustomField(customField);
			valueObj.setValue(value);
		}
		return valueObj;
	}
	
	private void noteCustomFieldErrors(List<CustomField> cfList, Map<Object,Object> errorMsg , Map<String,Boolean>  isValidRecord , Brander b) {


		for( com.softech.vu360.lms.model.CustomField cf : cfList ){

			if( isValidRecord.get( cf.getFieldLabel()+"-required") != null) {
				if( isValidRecord.get( cf.getFieldLabel()) == true ) {
					errorMsg.put(cf.getFieldLabel()+"-error", b.getBrandElement("lms.batchImportUsers.ValueRequired"));
					continue;
				}}
			else  
			{
				if(cf instanceof SSNCustomFiled && isValidRecord.get(  cf.getFieldLabel()+"-ssnError")  != null ){
					errorMsg.put(cf.getFieldLabel()+"-error", b.getBrandElement("lms.batchImportUsers.SSNRequired"));
					continue;
				}
				else if(cf instanceof DateTimeCustomField && isValidRecord.get(  cf.getFieldLabel()+"-dateError")  != null ){
					errorMsg.put(cf.getFieldLabel()+"-error", b.getBrandElement("lms.batchImportUsers.DateRequired") );
					continue;
				}
				else if(cf instanceof NumericCusomField && isValidRecord.get(  cf.getFieldLabel()+"-numericError") != null ){
					errorMsg.put(cf.getFieldLabel()+"-error", b.getBrandElement("lms.batchImportUsers.NumericRequired"));
					continue;
				}
				else if(cf instanceof SingleSelectCustomField && isValidRecord.get(  cf.getFieldLabel()+"-textError")  != null ){
					errorMsg.put(cf.getFieldLabel()+"-error", b.getBrandElement("lms.batchImportUsers.SingleSelectRequired"));
					continue;
				}
				else if(cf instanceof MultiSelectCustomField && isValidRecord.get( cf.getFieldLabel()+"-multiSelectError")  != null ){
					errorMsg.put(cf.getFieldLabel()+"-error", b.getBrandElement("lms.batchImportUsers.MultiSelectRequired"));
					continue;
				}							
			}							

		}
	}
	
	private Map<CustomField,List<CustomFieldValueChoice>> getCustomFieldChoices(List<CustomField> fields)
	{
		Map<CustomField,List<CustomFieldValueChoice>> customFieldChoiceMap=new HashMap<CustomField,List<CustomFieldValueChoice>>();
		for(CustomField field:fields)
		{
			if(field instanceof SingleSelectCustomField || field instanceof MultiSelectCustomField)
			{
				List<CustomFieldValueChoice> choices = customFieldService.getOptionsByCustomField(field);
				customFieldChoiceMap.put(field, choices);
			}
		}
		return customFieldChoiceMap;
	}
	private List<CustomField> getAllCustomFields(Customer customer){
		List<com.softech.vu360.lms.model.CustomField> customerFields=customer.getCustomFields();
		List<com.softech.vu360.lms.model.CustomField> distributorFields=customer.getDistributor().getCustomFields();
		List<com.softech.vu360.lms.model.CustomField> allFields=new ArrayList<com.softech.vu360.lms.model.CustomField>();
		allFields.addAll(customerFields);
		allFields.addAll(distributorFields);
		return allFields;
		
	}
	
	private List<OrganizationalGroup> getOrgGroupToManage(String orgGroupColumnVal, Customer customer) {

		List<OrganizationalGroup> orgGroup=null;
		String []orgGroupNames=new String[1];
		orgGroupNames[0]=orgGroupColumnVal.substring(orgGroupColumnVal.lastIndexOf(">")+1, orgGroupColumnVal.length());
		orgGroup = orgGroupService.getOrgGroupByNames(orgGroupNames, customer);
		
		return orgGroup;
	}
	

}
