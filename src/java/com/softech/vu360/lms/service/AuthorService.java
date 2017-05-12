package com.softech.vu360.lms.service;

import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Author;
import com.softech.vu360.lms.model.AuthorGroup;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.model.UserItemForm;
/**
 * @author Faisal A. Siddiqui 
 */
public interface AuthorService {
	public Map<String, List<VU360User>> createAuthorForUsers(List<UserItemForm> userForm, VU360User loggedInUser);
    public Author createAuthorForUser(VU360User user, VU360User loggedInUser);
    public AuthorGroup addAuthorGroup(ContentOwner contentOwner,Long loggedInUserID, boolean isSelfServiceUser);
	public ContentOwner addContentOwnerIfNotExist(Customer customer, Long loggedInUserID);
	public ContentOwner addContentOwnerForSelfServiceUser(Customer customer, VU360User loggedInUser);
	public Map<Long, Author> getUsersAuthorMap(List<VU360User> vu360users);
	public ContentOwner readContentOwnerByGUID(String contentOwnerGUID);
	
	// [12/9/2010] LMS-7512 :: Update Content Owner for Self-Author Customer/Reseller
	public ContentOwner updateContentOwner(Customer customer);
	
	public Customer getCustomerByContentOwner(ContentOwner contentOwner);
	public Distributor getDistributorByContentOwner(ContentOwner contentOwner);
	public boolean isAuthor(long userId);
	public boolean isAuthor(VU360User VU360User);
	public Author getAuthorByUsername(String username);

	
}
