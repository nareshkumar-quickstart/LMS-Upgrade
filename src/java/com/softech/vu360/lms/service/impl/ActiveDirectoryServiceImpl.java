package com.softech.vu360.lms.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.ActiveDirectoryUser;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.ActiveDirectoryService;
import com.softech.vu360.util.AuthConstant;
import com.softech.vu360.util.VU360Properties;

public class ActiveDirectoryServiceImpl implements ActiveDirectoryService{

	private static final Logger log = Logger.getLogger(ActiveDirectoryServiceImpl.class.getName());

	private static final String adminDN = VU360Properties.getVU360Property("ad.admin.fulldn");
	private static final String adminUserId = VU360Properties.getVU360Property("ad.admin.cn");
	private static final String adminPassword = VU360Properties.getVU360Property("ad.admin.password");
	private static final String ldapsURL = VU360Properties.getVU360Property("ad.ldap.url");
	private static final String ldapURL = VU360Properties.getVU360Property("ad.ldapa.url");
	private static final String keyStore = VU360Properties.getVU360Property("ad.certs.keystore");
	private static final String timeout = VU360Properties.getVU360Property("ad.connect.timeout");
	private static final boolean adEnabled = Boolean.valueOf(VU360Properties.getVU360Property("ad.integration.enabled"));
	private static final String baseDN = VU360Properties.getVU360Property("ad.user.basedn");
	private static final boolean adLoggingEnabled = Boolean.valueOf(VU360Properties.getVU360Property("ad.timelogging.enabled"));
	
	private boolean changePassword(String distinguishedName, byte[] newUnicodePassword, LdapContext ctx){
		try{
			ModificationItem[] modifiedEntries = new ModificationItem[1];
			modifiedEntries[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_PASSWORD, newUnicodePassword));

			ctx.modifyAttributes(distinguishedName, modifiedEntries);
			log.info("Password changed in AD Successfully");
		}
		catch (Exception ee ){
			log.error(ee.getMessage() + ee);
			return false;
		}	
		return true;
	}


	public boolean isADIntegrationEnabled() {
		return adEnabled;
	}

	/*
	 * This method will first find the user in AD and then updates.
	 * 
	 */
	@Override
	public ActiveDirectoryUser updateUser(VU360User userToUpdate){
		ActiveDirectoryUser updatedADUser = getADUserFromLMSUser(userToUpdate);
		LdapContext ctx = null;
		String distinguishedName = "";
		String userName = userToUpdate.getUsername();

		try{
			logForAD(String.format("Begin Updating user: %s", userName));
			log.info(String.format("Locating the user first in AD: %s", userName));			
			distinguishedName = getADUserDN(userToUpdate.getUsername());
			
			if (distinguishedName.length()==0){
				log.info(String.format("User not found in entire AD. Cannot Update user:%s in AD.", userName));
				return null;
			}
			
			try{
				ctx =  getLDAPContext(true, adminDN, adminPassword);

				ModificationItem[] modifiedEntries = userToUpdate.isPassWordChanged()? new ModificationItem[5]: new ModificationItem[4];

				modifiedEntries[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_FIRSTNAME, updatedADUser.getFirstName()));
				modifiedEntries[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_LASTNAME, updatedADUser.getLastName()));
				modifiedEntries[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_DISPLAYNAME, updatedADUser.getDisplayName()));
				modifiedEntries[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_EMAIL, updatedADUser.getEmail()!=null && updatedADUser.getEmail().length()>0 ?updatedADUser.getEmail(): updatedADUser.getUserName()));

				if (userToUpdate.isPassWordChanged()){				
					modifiedEntries[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_PASSWORD, updatedADUser.getADUnicodePassword()));

					changePassword(distinguishedName, updatedADUser.getADUnicodePassword(), ctx);
				}

				ctx.modifyAttributes(distinguishedName, modifiedEntries);
				log.info("User updated in AD Successfully");
			}
			catch (Exception ee ){
				log.error(ee.getMessage() + ee);
				return null;
			}		
			finally {
				if(ctx!=null) ctx.close();	
			}
		}
		catch (Exception ee){
			log.error(ee);
		}

		logForAD(String.format("Finished Updated user: %s" , userToUpdate.getUsername()));	
		return updatedADUser;
	}

	/*
	 * This method will first search the AD before creating the user. If found, it will not add the user to AD
	 * 
	 */
	@Override
	public ActiveDirectoryUser addUser(VU360User newUser){
		LdapContext ctx = null;
		ActiveDirectoryUser addedUser = null;

		try{
			logForAD(String.format("Begin Creating user: %s", newUser.getUsername()));

			String existingUser = getADUserDN(newUser.getUsername());			
			if (existingUser.length()>0) return null;//if user found anywhere in the AD dont do anything an return

			addedUser = getADUserFromLMSUser(newUser);

			try {
				ctx =  getLDAPContext(true, adminDN, adminPassword);

				Attributes attrs = new BasicAttributes(true); 
				attrs.put(AuthConstant.AD_FIELD_OBJECTCLASS,"user");
				attrs.put(AuthConstant.AD_FIELD_CN, addedUser.getUserName());
				attrs.put(AuthConstant.AD_FIELD_FIRSTNAME, addedUser.getFirstName());
				attrs.put(AuthConstant.AD_FIELD_LASTNAME, addedUser.getLastName());
				attrs.put(AuthConstant.AD_FIELD_DISPLAYNAME , addedUser.getDisplayName());
				attrs.put(AuthConstant.AD_FIELD_USERPRINCIPALNAME , addedUser.getUserName());
				if (addedUser.getEmail()!=null && addedUser.getEmail().length()>0 ) attrs.put(AuthConstant.AD_FIELD_EMAIL, addedUser.getEmail());
				attrs.put(AuthConstant.AD_FIELD_USERACCOUNTCONTROL, Integer.toString(AuthConstant.UF_NORMAL_ACCOUNT + AuthConstant.UF_PASSWD_NOTREQD + AuthConstant.UF_PASSWORD_EXPIRED + AuthConstant.UF_ACCOUNTDISABLE));
				//attrs.put(AuthConstant.AD_FIELD_USERACCOUNTCONTROL, Integer.toString(AuthConstant.UF_NORMAL_ACCOUNT + AuthConstant.UF_DONT_EXPIRE_PASSWD));
				attrs.put(AuthConstant.AD_FIELD_PASSWORD , addedUser.getADUnicodePassword());

				Context resultContext = ctx.createSubcontext(addedUser.getDistinguishedName(), attrs);
				//now that we've created the user object,set the password and change the userAccountControl enabling the acount and forcing the user to update ther password the first time they login
				ModificationItem[] modifiedEntries = new ModificationItem[2];

				modifiedEntries[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_PASSWORD, addedUser.getADUnicodePassword()));
				modifiedEntries[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_USERACCOUNTCONTROL, Integer.toString(AuthConstant.UF_NORMAL_ACCOUNT + AuthConstant.UF_DONT_EXPIRE_PASSWD)));

				// Perform the update
				ctx.modifyAttributes(addedUser.getDistinguishedName(), modifiedEntries);

				log.info("User created in AD Successfully");
			}
			catch (Exception ee){
				log.error(ee);
				return null;
			}
			finally {
				if(ctx!=null) ctx.close();
			}
		}
		catch (Exception ee){
			log.error(ee);
		}

		logForAD(String.format("Finished Creating user: %s" , newUser.getUsername()));		
		return addedUser;
	}

	/*
	 * This method will first find the user in AD and then updates.
	 * 
	 */
	@Override
	public ActiveDirectoryUser updateUser(com.softech.vu360.lms.vo.VU360User userToUpdate){
		ActiveDirectoryUser updatedADUser = getADUserFromLMSUser(userToUpdate);
		LdapContext ctx = null;
		String distinguishedName = "";
		String userName = userToUpdate.getUsername();

		try{
			logForAD(String.format("Begin Updating user: %s", userName));
			log.info(String.format("Locating the user first in AD: %s", userName));			
			distinguishedName = getADUserDN(userToUpdate.getUsername());
			
			if (distinguishedName.length()==0){
				log.info(String.format("User not found in entire AD. Cannot Update user:%s in AD.", userName));
				return null;
			}
			
			try{
				ctx =  getLDAPContext(true, adminDN, adminPassword);

				ModificationItem[] modifiedEntries = userToUpdate.isPassWordChanged()? new ModificationItem[5]: new ModificationItem[4];

				modifiedEntries[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_FIRSTNAME, updatedADUser.getFirstName()));
				modifiedEntries[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_LASTNAME, updatedADUser.getLastName()));
				modifiedEntries[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_DISPLAYNAME, updatedADUser.getDisplayName()));
				modifiedEntries[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_EMAIL, updatedADUser.getEmail()!=null && updatedADUser.getEmail().length()>0 ?updatedADUser.getEmail(): updatedADUser.getUserName()));

				if (userToUpdate.isPassWordChanged()){				
					modifiedEntries[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_PASSWORD, updatedADUser.getADUnicodePassword()));

					changePassword(distinguishedName, updatedADUser.getADUnicodePassword(), ctx);
				}

				ctx.modifyAttributes(distinguishedName, modifiedEntries);
				log.info("User updated in AD Successfully");
			}
			catch (Exception ee ){
				log.error(ee.getMessage() + ee);
				return null;
			}		
			finally {
				if(ctx!=null) ctx.close();	
			}
		}
		catch (Exception ee){
			log.error(ee);
		}

		logForAD(String.format("Finished Updated user: %s" , userToUpdate.getUsername()));	
		return updatedADUser;
	}

	/*
	 * This method will first search the AD before creating the user. If found, it will not add the user to AD
	 * 
	 */
	@Override
	public ActiveDirectoryUser addUser(com.softech.vu360.lms.vo.VU360User newUser){
		LdapContext ctx = null;
		ActiveDirectoryUser addedUser = null;

		try{
			logForAD(String.format("Begin Creating user: %s", newUser.getUsername()));

			String existingUser = getADUserDN(newUser.getUsername());			
			if (existingUser.length()>0) return null;//if user found anywhere in the AD dont do anything an return

			addedUser = getADUserFromLMSUser(newUser);

			try {
				ctx =  getLDAPContext(true, adminDN, adminPassword);

				Attributes attrs = new BasicAttributes(true); 
				attrs.put(AuthConstant.AD_FIELD_OBJECTCLASS,"user");
				attrs.put(AuthConstant.AD_FIELD_CN, addedUser.getUserName());
				attrs.put(AuthConstant.AD_FIELD_FIRSTNAME, addedUser.getFirstName());
				attrs.put(AuthConstant.AD_FIELD_LASTNAME, addedUser.getLastName());
				attrs.put(AuthConstant.AD_FIELD_DISPLAYNAME , addedUser.getDisplayName());
				attrs.put(AuthConstant.AD_FIELD_USERPRINCIPALNAME , addedUser.getUserName());
				if (addedUser.getEmail()!=null && addedUser.getEmail().length()>0 ) attrs.put(AuthConstant.AD_FIELD_EMAIL, addedUser.getEmail());
				attrs.put(AuthConstant.AD_FIELD_USERACCOUNTCONTROL, Integer.toString(AuthConstant.UF_NORMAL_ACCOUNT + AuthConstant.UF_PASSWD_NOTREQD + AuthConstant.UF_PASSWORD_EXPIRED + AuthConstant.UF_ACCOUNTDISABLE));
				//attrs.put(AuthConstant.AD_FIELD_USERACCOUNTCONTROL, Integer.toString(AuthConstant.UF_NORMAL_ACCOUNT + AuthConstant.UF_DONT_EXPIRE_PASSWD));
				attrs.put(AuthConstant.AD_FIELD_PASSWORD , addedUser.getADUnicodePassword());

				Context resultContext = ctx.createSubcontext(addedUser.getDistinguishedName(), attrs);
				//now that we've created the user object,set the password and change the userAccountControl enabling the acount and forcing the user to update ther password the first time they login
				ModificationItem[] modifiedEntries = new ModificationItem[2];

				modifiedEntries[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_PASSWORD, addedUser.getADUnicodePassword()));
				modifiedEntries[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(AuthConstant.AD_FIELD_USERACCOUNTCONTROL, Integer.toString(AuthConstant.UF_NORMAL_ACCOUNT + AuthConstant.UF_DONT_EXPIRE_PASSWD)));

				// Perform the update
				ctx.modifyAttributes(addedUser.getDistinguishedName(), modifiedEntries);

				log.info("User created in AD Successfully");
			}
			catch (Exception ee){
				log.error(ee);
				return null;
			}
			finally {
				if(ctx!=null) ctx.close();
			}
		}
		catch (Exception ee){
			log.error(ee);
		}

		logForAD(String.format("Finished Creating user: %s" , newUser.getUsername()));		
		return addedUser;
	}
	
	private LdapContext getLDAPContext(boolean useSSL, String distinguishedName, String password){
		Hashtable<String,String> prop = new Hashtable<String,String>();//to set the properties
		String usingSSL = useSSL?"using SSL (LDAPS)":"normally (LDAP)";

		if(useSSL){
			System.setProperty("javax.net.ssl.trustStore", keyStore);//access the truststore
			System.setProperty("javax.net.ssl.keyStorePassword","changeit");//as default password
			prop.put(Context.PROVIDER_URL, ldapsURL);//URL	
			prop.put(Context.SECURITY_PROTOCOL,"ssl");//specify the use of SSL	
			prop.put(Context.REFERRAL, "ignore");
		}
		else{
			prop.put(Context.PROVIDER_URL, ldapURL);//URL
			prop.put(Context.REFERRAL, "follow");
		}

		prop.put(Context.REFERRAL, "follow");
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		prop.put(Context.SECURITY_AUTHENTICATION, "simple");
		prop.put("com.sun.jndi.ldap.connect.pool", "false");
		prop.put("com.sun.jndi.ldap.connect.timeout", timeout);
		prop.put(Context.SECURITY_PRINCIPAL, distinguishedName);//User id to connect to Active directory
		prop.put(Context.SECURITY_CREDENTIALS, password);//User password 
		prop.put("java.naming.ldap.attributes.binary", "tokenGroups objectSid objectGUID");

		try {
			log.info(String.format("%s: AD-Logging - Begin: Trying to connect AD %s.",  new Timestamp(new Date().getTime()),  usingSSL ));
			LdapContext ctx = new InitialLdapContext(prop, null);
			log.info(String.format("%s: AD-Logging - Finished: Connected to AD %s.",  new Timestamp(new Date().getTime()),  usingSSL ));
			return ctx;
		} catch (NamingException e) {			
			log.error(e);
			return null;
		}		
	}	 

	private ActiveDirectoryUser getADUserFromLMSUser(VU360User lmsUser){
		ActiveDirectoryUser adUser = new ActiveDirectoryUser();
		adUser.setEmail(lmsUser.getEmailAddress());
		adUser.setFirstName(lmsUser.getFirstName());
		adUser.setLastName(lmsUser.getLastName());
		adUser.setMiddleName(lmsUser.getMiddleName());
		adUser.setPassword(lmsUser.getPassword());
		adUser.setUserName(lmsUser.getUsername());

		//create DisplayName
		StringBuffer sbDisplayName = new StringBuffer();
		sbDisplayName.append(lmsUser.getFirstName());
		if(lmsUser.getMiddleName()!=null && lmsUser.getMiddleName().trim().length() > 0) {
			sbDisplayName.append(" ");
			sbDisplayName.append(lmsUser.getMiddleName());
		}
		if(lmsUser.getLastName()!=null && lmsUser.getLastName().trim().length() > 0) {
			sbDisplayName.append(" ");
			sbDisplayName.append(lmsUser.getLastName());
		}

		adUser.setDisplayName(sbDisplayName.toString());
		return adUser;
	}

	private ActiveDirectoryUser getADUserFromLMSUser(com.softech.vu360.lms.vo.VU360User lmsUser){
		ActiveDirectoryUser adUser = new ActiveDirectoryUser();
		adUser.setEmail(lmsUser.getEmailAddress());
		adUser.setFirstName(lmsUser.getFirstName());
		adUser.setLastName(lmsUser.getLastName());
		adUser.setMiddleName(lmsUser.getMiddleName());
		adUser.setPassword(lmsUser.getPassword());
		adUser.setUserName(lmsUser.getUsername());

		//create DisplayName
		StringBuffer sbDisplayName = new StringBuffer();
		sbDisplayName.append(lmsUser.getFirstName());
		if(lmsUser.getMiddleName()!=null && lmsUser.getMiddleName().trim().length() > 0) {
			sbDisplayName.append(" ");
			sbDisplayName.append(lmsUser.getMiddleName());
		}
		if(lmsUser.getLastName()!=null && lmsUser.getLastName().trim().length() > 0) {
			sbDisplayName.append(" ");
			sbDisplayName.append(lmsUser.getLastName());
		}

		adUser.setDisplayName(sbDisplayName.toString());
		return adUser;
	}

	/**
	 * This method will first try to authenticate the user in the default organization. If not authenticated, it will search the user in entire AD and if found anywhere, it will authenticate it on the provided password.
	 * @param user
	 * @param password
	 * @return
	 */
	@Override
	public  boolean authenticateADUser(String userName, String password) {
		boolean isAuthenticated = false;
		String distinguishedName = "", distinguishedName2 = "";

		if(userName!=null && password!=null){
			try{
				logForAD(String.format("Begin authenticating user: %s", userName));
				LdapContext ctx = null;

				//First check the user in the default organization
				try{
					//Setting parameters for Context
					distinguishedName = "CN=" + userName + "," + baseDN;
					ctx = getLDAPContext(false, distinguishedName, password);
					isAuthenticated = ctx!=null? true:false;
				}
				catch(Exception e){
					isAuthenticated = false;
				}
				finally{
					if(ctx!=null) ctx.close();
				}

				if (!isAuthenticated){
					try{
						//Setting parameters for Context
						distinguishedName2 = getADUserDN(userName);
						
						if (distinguishedName2.length()>0 && !distinguishedName2.equalsIgnoreCase(distinguishedName)){
							ctx = getLDAPContext(false, distinguishedName2, password);							
							isAuthenticated = ctx!=null? true:false;
							logForAD(String.format("Finished Re-Authenticating user %s in other organization. distinguishedName: %s", userName, distinguishedName2));
						}					
					}
					catch(Exception e){
						isAuthenticated = false;
					}
					finally{
						if(ctx!=null) ctx.close();
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		String authenticationResult = isAuthenticated?"AD authenication PASSED for user:":"AD authenication FAILED for user:";
		logForAD(String.format("%s %s",authenticationResult, userName));
		logForAD(String.format("Finished authenticating user: %s" , userName));	
		return isAuthenticated;
	}

	/**
	 * This will first locate the user in default organization and then if not found, will locate the user in all organizations
	 * @param username to search in Active Directory
	 * @return
	 */
	@Override
	public boolean findADUser(String username) {
		String userDN = getADUserDN(username);
		return userDN.length()>0? true : false;
	}

	private String getADUserDN(String username){
		LdapContext ctx = null;

		try{
			logForAD(String.format("Begin Find user: %s" , username));
			try{
				ctx =  getLDAPContext(false, adminDN, adminPassword);

				if(ctx!=null){
					SearchControls constraints = new SearchControls();
					constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
					String[] attrIDs = {"distinguishedName"};
					constraints.setReturningAttributes(attrIDs);

					//first locate the user in default organization 
					NamingEnumeration answer = ctx.search(baseDN, "cn=" + username, constraints);
					if (answer.hasMore()) {
						Attributes attrs = ((SearchResult) answer.next()).getAttributes();
						log.info(String.format("Found this user in DEFAULT ORGANIZATION. %s" , attrs.get("distinguishedName")));
						return attrs.get("distinguishedName").get(0).toString(); //return true if found the user in default organization
					}

					//If not found in default, find this user in all organizations 
					String rootBaseDN = baseDN.toUpperCase().replace("O=DEFAULT ORGANIZATION,", "");
					answer = ctx.search(rootBaseDN, "cn=" + username, constraints);
					if (answer.hasMore()) {		        	
						Attributes attrs = ((SearchResult) answer.next()).getAttributes();		        	
						log.info(String.format("User not found in DEFAULT ORGANIZATION. Found this user in other organization. %s" , attrs.get("distinguishedName")));
						return attrs.get("distinguishedName").get(0).toString();
					}
					else{
						log.info(String.format("User not found anywhere in AD: %s" , username));
					}
				}
			}
			catch(Exception e){
				log.error(e);
			}
			finally {
				if(ctx!=null) ctx.close();
			}
		}
		catch(Exception e){
			log.error(e);
		}
		finally{
			logForAD(String.format("Finished Find user: %s" , username));
		}

		return "";
	}
	
	private static void logForAD(String message){
		if (adLoggingEnabled){
			log.info(String.format("%s: AD-Logging - %s", new Timestamp(new Date().getTime()), message));
		}
		else{
			log.info(String.format("%s", message));
		}
	}
}
