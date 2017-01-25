package com.softech.vu360.lms.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;

import com.softech.vu360.lms.model.Alert;
import com.softech.vu360.lms.model.AlertQueue;
import com.softech.vu360.lms.model.AlertRecipient;
import com.softech.vu360.lms.model.AlertTrigger;
import com.softech.vu360.lms.model.AvailableAlertEvent;
import com.softech.vu360.lms.model.EmailAddress;
import com.softech.vu360.lms.model.EmailAddressAlertRecipient;
import com.softech.vu360.lms.model.TestBaseDAO;
import com.softech.vu360.lms.model.VU360User;

/**
 * 
 * @author marium.saud This JUnit test class will test the Base class
 *         'AlertRecipient' and all its inherited classes.The base class uses
 *         Alert with one-one relation. The extended sub - classes are namely:
 *         EmailAddressAlertRecipient (Uses EmailAddress.java with @ManyToMany
 *         Relation) LearnerAlertRecipient (Uses Learner.java with @ManyToMany
 *         Relation) LearnerGroupAlertRecipient (Uses LearnerGroup.java with @ManyToMany
 *         Relation) OrgGroupAlertRecipient (Uses OrganizationalGroup.java with @ManyToMany
 *         Relation)
 *         It also includes Test case for Alert.java,AlertTrigger.java,LearnerGroupAlertRecipient.java and AlertQueue.java
 */
@Transactional
public class AlertTest extends TestBaseDAO<Alert> {


	//=====================================Test Cases for Alert Recepient
	
	//@Test
	public void AlertRecipient_should_Save() throws Exception {
		// Adding Alert Recipient
		 AlertRecipient alertRecipient=new AlertRecipient();
		 Alert alert=getById(new Long(14303),Alert.class);
		 alertRecipient.setAlert(alert);
		 alertRecipient.setAlertRecipientGroupName("Test_RecipientGroupName");
		 alertRecipient.setDelete(true);
		 try{
			 alertRecipient=(AlertRecipient)crudSave(AlertRecipient.class, alertRecipient);
		 }
		 catch(Exception ex){
			 System.out.println(alertRecipient.getId());
		 }
		
	}

//	@Test
	public void EmailAddressAlertRecipient_should_save_with_EmailAddress() throws Exception {

		EmailAddressAlertRecipient recipient = new EmailAddressAlertRecipient();

		Alert alert=getById(new Long(14303),Alert.class);
		recipient.setAlert(alert);
		// Setting List of Email Address
		EmailAddress email_1 = new EmailAddress();
		email_1.setEmail("lms@abc.com");
		EmailAddress email_2 = new EmailAddress();
		email_2.setEmail("myemail_lms@gmail.com");
		List<EmailAddress> list = new ArrayList<EmailAddress>();
		list.add(email_1);
		list.add(email_2);
	    recipient.setEmailAddress(list);

		recipient.setAlertRecipientGroupName("Test_AlertRecipientGroupName1");
		recipient.setDelete(false);
		try{
			recipient=(EmailAddressAlertRecipient)crudSave(AlertRecipient.class, recipient);
		 }
		 catch(Exception ex){
			 System.out.println(recipient.getId());
		 }

	}

	//@Test
	public void EmailAddressAlertRecipient_should_update() throws Exception {
		
		EmailAddressAlertRecipient updateObj=(EmailAddressAlertRecipient)crudFindById(AlertRecipient.class, new Long(9013));
		updateObj.setAlertRecipientGroupName("Test_AlertRecipientGroupName_Updated");
		updateObj.setDelete(true);
		updateObj.getEmailAddress().get(0).setEmail("sara@gmail.com");
		updateObj.getAlert().setAlertName("Test_Alert_Updated");
		updateObj.getAlert().setAlertMessageBody("Test_MessageBody_Updated");
		updateObj.getAlert().setAlertSubject("Test_Subject_Updated");
		
		VU360User vu360User = (VU360User)crudFindById(VU360User.class, new Long(3));
		updateObj.getAlert().setCreatedBy(vu360User);
		updateObj.getAlert().setLastUpdatedBy(vu360User);
		
		try{
			updateObj=(EmailAddressAlertRecipient)crudSave(AlertRecipient.class, updateObj);
		 }
		 catch(Exception ex){
			 System.out.println(updateObj.getId());
		 }
		
	} 
	
	@Test
	public void EmailAddressAlertRecipient_should_delete() throws Exception {

		EmailAddressAlertRecipient updateObj=(EmailAddressAlertRecipient)crudFindById(AlertRecipient.class, new Long(9010));
		EmailAddress email=new EmailAddress();
		EmailAddress email2=updateObj.getEmailAddress().get(1);
		
//		List<EmailAddress> recordToRemove=updateObj.getEmailAddress();
//		updateObj.getEmailAddress().removeAll(recordToRemove);
		
		updateObj.getEmailAddress().remove(0);
		updateObj.getEmailAddress().remove(0);
		
		email.setEmail("kaunain@zalim.net");
		email2.setEmail("marium@tention.net");
		List<EmailAddress> emailList=new ArrayList<EmailAddress>();
		
		
		emailList.add(email);
		emailList.add(email2);
		
		
		updateObj.setEmailAddress(emailList);	
		
		//EmailAddress email_1 = new EmailAddress();
		//email_1.setEmail("delete1_test1@gmail.com");
		//updateObj.getEmailAddress().add(email_1);
		try{
			updateObj=(EmailAddressAlertRecipient)crudSave(AlertRecipient.class, updateObj);
		 }
		 catch(Exception ex){
			 System.out.println(updateObj.getId());
		 }

	}
	
	//=========================================Test Case for Alert
	
//	@Test
	public void Alert_should_save(){
		Alert alert = new Alert();
		alert.setAlertMessageBody("Test_AlertMessage_New");
		alert.setAlertName("Test_AlertName_New");
		alert.setAlertSubject("Test_AlertSubject_New");
		alert.setCreatedDate(new Date());
		alert.setIsDefault(true);
		alert.setIsDelete(true);
		alert.setFromEmailAddress("test@360training.com");
		alert.setFromName("Test|_FromName_New");
		alert.setLastUpdatedDate(new Date());
		alert.setUseDefaultMessage(null);
		VU360User vu360User = (VU360User)crudFindById(VU360User.class, new Long(3));
		alert.setLastUpdatedBy(vu360User);
		alert.setCreatedBy(vu360User);
		try{
			alert=save(alert);
		}
		catch(Exception ex){
			System.out.println(alert.getId());
		}
	}
	
//	@Test
	public void Alert_should_update(){
		
		Alert alert=getById(new Long(15807), Alert.class);
		alert.setAlertMessageBody("Test_AlertMessage_Updated");
		save(alert);
	}
	
	//================================================Test case for AlertTrigger
	
//	@Test
	public void AlertTrigger_should_save(){
		
		AlertTrigger alertTrigger = new AlertTrigger();
		Alert alert = getById(new Long(15807), Alert.class);
		alertTrigger.setAlert(alert);
		alertTrigger.setTriggerName("Test_TriggerName");
		@SuppressWarnings("unchecked")
		List<AvailableAlertEvent> availavleAlert=(List<AvailableAlertEvent>)getAll("AvailableAlertEvent", AvailableAlertEvent.class);
		alertTrigger.setAvailableAlertEvents(availavleAlert);
		try{
			alertTrigger=(AlertTrigger)crudSave(AlertTrigger.class, alertTrigger);
		}
		catch(Exception ex){
			System.out.println(alertTrigger.getId());
		}
	}
	
//	@Test
	public void AlertTrigger_should_update(){
		
		AlertTrigger alertTrigger=(AlertTrigger)crudFindById(AlertTrigger.class, new Long(12353));
		alertTrigger.setAvailableAlertEvents(null);
		crudSave(AlertTrigger.class, alertTrigger);
		
	}
	
	//================================================Test case for AlertQueue
	
//	 @Test
		public void AlertQueue_should_save() throws Exception {
			AlertQueue alertQueue = new AlertQueue();
			
			VU360User vu360User = (VU360User)crudFindById(VU360User.class, new Long(3));
			alertQueue.setLearner(vu360User.getLearner());
			alertQueue.setLearnerFirstName("Test_First_Name");
			alertQueue.setLearnerLastName("Test_Last_Name");
			try{
				alertQueue=(AlertQueue)crudSave(AlertQueue.class, alertQueue);
			}
			catch(Exception ex){
				System.out.println(alertQueue.getId());
			}
		}

}
