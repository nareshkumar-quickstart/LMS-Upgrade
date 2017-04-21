package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.softech.vu360.lms.model.Author;
import com.softech.vu360.lms.model.AuthorFeature;
import com.softech.vu360.lms.model.AuthorGroup;
import com.softech.vu360.lms.model.AuthorPermission;
import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.AuthorFeatureRepository;
import com.softech.vu360.lms.repositories.AuthorGroupRepository;
import com.softech.vu360.lms.repositories.AuthorRepository;
import com.softech.vu360.lms.repositories.ContentOwnerRepository;
import com.softech.vu360.lms.repositories.CustomerRepository;
import com.softech.vu360.lms.repositories.DistributorRepository;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.web.controller.model.UserItemForm;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.LabelBean;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Faisal A. Siddiqui 
 */
public class AuthorServiceImpl implements AuthorService{
	@Inject
	private AuthorRepository authorRepository;
	@Inject
	private AuthorGroupRepository authorGroupRepository;
	@Inject
	private CustomerRepository customerRepository;
	@Inject
	private ContentOwnerRepository contentOwnerRespository;
	@Inject
	private AuthorFeatureRepository authorFeatureRepository;
	@Inject
	private DistributorRepository distributorRepository;
    
    private static final Logger log = Logger.getLogger(AuthorServiceImpl.class.getName());
    
    @Transactional
    public Map<String, List<VU360User>> createAuthorForUsers(List<UserItemForm> userForm, VU360User loggedInUser) {
    	
    	Map<String, List<VU360User>> result = new HashMap<String, List<VU360User>>();
    	List<VU360User> failedAuthorUsers = new ArrayList<VU360User>();
    	List<VU360User> successfulAuthorUsers = new ArrayList<VU360User>();
    	
    	
    			for(UserItemForm usr : userForm){
    				VU360User currentUser = null;
    				try {
    					
	    				currentUser = usr.getUser();
	    				Author author = createAuthorForUser(currentUser, loggedInUser);
	    				
	    				if(author == null) {
	    					failedAuthorUsers.add(currentUser);
	    				}
	    				else {
	    					successfulAuthorUsers.add(currentUser);
	    				}
	    			}
    			
    			catch (Exception e) {
    				failedAuthorUsers.add(currentUser);
    				e.printStackTrace();
    				log.error(e.getMessage(), e);
    			}
    		}
    	
    	
    	result.put("failedAuthorUsers", failedAuthorUsers);
    	result.put("successfulAuthorUsers", successfulAuthorUsers);
    	
    	return result;
    }
    
	/*
	 * This method requires a content owner creation and association with customer or Distributor before execution
	 * 
	 */
    @Transactional
	public Author createAuthorForUser(VU360User user, VU360User loggedInUser){
    	Author author = new Author();
		ContentOwner contentOwner = user.getLearner().getCustomer().getContentOwner();
		if(contentOwner == null){
			contentOwner = user.getLearner().getCustomer().getDistributor().getContentOwner();
		}
		author.getContentOwners().add(contentOwner);
		author.setCreatedDate(new Date());
		author.setCreatedUserId(loggedInUser.getId());
		List<AuthorGroup> authorGroups = authorGroupRepository.findByContentOwner(contentOwner);
		if(!CollectionUtils.isEmpty(authorGroups))
			author.getGroups().add(authorGroups.get(0));
		author.setVu360User(user);
		return authorRepository.save(author);
    }
	@Transactional
	public ContentOwner addContentOwner(Customer customer, Long loggedInUserID){
		ContentOwner contentOwner = new ContentOwner();
		
		contentOwner.setGuid(GUIDGeneratorUtil.generateGUID());
		if(customer.isDistributorRepresentative()){
			contentOwner.setName(customer.getDistributor().getName());
		}
		else{
			contentOwner.setName(customer.getName());
		}
		contentOwner = contentOwnerRespository.save(contentOwner);
		//customer.setContentOwner(contentOwner);
		addAuthorGroup(contentOwner, loggedInUserID, false);
		return contentOwner;
	}
	@Transactional
	public ContentOwner addContentOwnerIfNotExist(Customer customer, Long loggedInUserID){
		ContentOwner contentOwner = null;
		if(customer.isDistributorRepresentative()){
			contentOwner = customer.getDistributor().getContentOwner();
			if(contentOwner == null){
				contentOwner = addContentOwner(customer, loggedInUserID);
			}
		}
		else{
			contentOwner = customer.getContentOwner();
			if(contentOwner == null){
				contentOwner = addContentOwner(customer, loggedInUserID);
			}
		}
		return contentOwner;
	}

	@Transactional
	public ContentOwner addContentOwnerForSelfServiceUser(Customer customer, VU360User loggedInUser){
		ContentOwner contentOwner = null;
		if(customer.isDistributorRepresentative()){
			contentOwner = customer.getDistributor().getContentOwner();
			if(contentOwner == null){
				
				
				//contentOwner = addContentOwner(customer, loggedInUser);
				contentOwner = new ContentOwner();
				contentOwner.setGuid(GUIDGeneratorUtil.generateGUID());
				
				// LCMS team introduce following attributes. 
				// Like, As per LCMS team instruction we are creating contentowner with PlanType_Id=2, this mean it can use some extra scene templete and course to create and publish in LMS.
				contentOwner.setPlanTypeId(new Long(2));
				contentOwner.setMaxAuthorCount(new Long(1));
				contentOwner.setMaxCourseCount(new Long(1));
				contentOwner.setCurrentAuthorCount(new Long(1));
				contentOwner.setCurrentCourseCount(new Long(0));
				
				if(customer.isDistributorRepresentative()){
					contentOwner.setName(customer.getDistributor().getName());
				}
				else{
					contentOwner.setName(customer.getName());
				}
				contentOwner = contentOwnerRespository.save(contentOwner);
				addAuthorGroup(contentOwner, loggedInUser.getId(), true);
				
				
			}
		}
		else{
			contentOwner = customer.getContentOwner();
			if(contentOwner == null){
				
				//contentOwner = addContentOwner(customer, loggedInUser);
				contentOwner = new ContentOwner();
				contentOwner.setGuid(GUIDGeneratorUtil.generateGUID());
				contentOwner.setPlanTypeId(new Long(2));
				contentOwner.setMaxAuthorCount(new Long(1));
				contentOwner.setMaxCourseCount(new Long(1));
				contentOwner.setCurrentAuthorCount(new Long(1));
				contentOwner.setCurrentCourseCount(new Long(0));
				
				if(customer.isDistributorRepresentative()){
					contentOwner.setName(customer.getDistributor().getName());
				}
				else{
					contentOwner.setName(customer.getName());
				}
				contentOwner = contentOwnerRespository.save(contentOwner);
				addAuthorGroup(contentOwner, loggedInUser.getId(), true);
				
				
			}
		}
		return contentOwner;
	}
	
	@Transactional
	public AuthorGroup addAuthorGroup(ContentOwner contentOwner,Long loggedInUserID, boolean isSelfServiceUser){
    	AuthorGroup authorGroup = new AuthorGroup();
    	List<AuthorFeature> features = null;
    	List<AuthorPermission> permissions = null;
    	
    	authorGroup.setName(contentOwner.getName()+" Admin");
    	authorGroup.setCreatedDate(new Date());
    	authorGroup.setCreatedUserId(loggedInUserID);
    	authorGroup.setGuid(GUIDGeneratorUtil.generateGUID());
    	authorGroup.setDescription(authorGroup.getDescription());
    	
    	if(isSelfServiceUser){
    		com.softech.vu360.lms.vo.Language lang = new com.softech.vu360.lms.vo.Language();
    		lang.setLanguage(Language.DEFAULT_LANG);
    		Brander brand = VU360Branding.getInstance().getBrander(VU360Branding.DEFAULT_BRAND, lang);
    		
    		List<LabelBean> lcmsFeatures = null;
    		List<String> lstDisplayName = new ArrayList<String>();
    		lcmsFeatures = brand.getBrandMapElements("lms.lcms.Author.Features");
    		if(lcmsFeatures!=null){
    			for( LabelBean bean : lcmsFeatures ) {
    				lstDisplayName.add(bean.getLabel());
    			}
    			
    			features = authorFeatureRepository.getAuthorFeaturesForSelfServiceUsers(lstDisplayName);
    		}
    		permissions = createPermissionsForSelfServiceUsers(authorGroup, features, loggedInUserID);
    	}else{
    		features = (List<AuthorFeature>) authorFeatureRepository.findAll();
    		permissions = createPermissions(authorGroup, features, loggedInUserID);
    	}
    	
    	authorGroup.setPermissions(permissions);
    	authorGroup.setContentOwner(contentOwner);
    	return authorGroupRepository.save(authorGroup);
    }

	private List<AuthorPermission> createPermissions(AuthorGroup authorGroup,List<AuthorFeature> features,Long loggedInUserID){
		List<AuthorPermission> permissionList = new ArrayList<AuthorPermission>();
		for(AuthorFeature feature:features){
			AuthorPermission permission = new AuthorPermission();
			permission.setAuthorFeature(feature);
			permission.setAuthorGroup(authorGroup);
			permission.setCreatedDate(new Date());
			permission.setCreatedUserId(loggedInUserID);
			// This hard coding is replicated from LCMS StoredProcedure (INSERT_CONTENTOWNER)
			if(feature.getDisplayName().trim().equalsIgnoreCase("Read Only Mode") || 
					feature.getDisplayName().trim().equalsIgnoreCase("Publishing to Master Catalog") ||
					feature.getDisplayName().trim().equalsIgnoreCase("Publishing to Legacy Systems") ||
					feature.getDisplayName().trim().equalsIgnoreCase("Allow Copy/Move Content") ||
					feature.getDisplayName().trim().equalsIgnoreCase("User Management") ||
					feature.getDisplayName().trim().equalsIgnoreCase("Accept Offers") 

			){
				permission.setFeatureEnabled(Integer.valueOf(2));	
			}else{
				permission.setFeatureEnabled(Integer.valueOf(1));
			}
			permissionList.add(permission);
		}
		return permissionList;
	}

	private List<AuthorPermission> createPermissionsForSelfServiceUsers(AuthorGroup authorGroup,List<AuthorFeature> features,Long loggedInUserID){
		List<AuthorPermission> permissionList = new ArrayList<AuthorPermission>();
		for(AuthorFeature feature:features){
			AuthorPermission permission = new AuthorPermission();
			permission.setAuthorFeature(feature);
			permission.setAuthorGroup(authorGroup);
			permission.setCreatedDate(new Date());
			permission.setCreatedUserId(loggedInUserID);
			
			permission.setFeatureEnabled(Integer.valueOf(1));
			permissionList.add(permission);
		}
		return permissionList;
	}
	
	public Map<Long, Author> getUsersAuthorMap(List<VU360User> vu360users) {
		Map<Long, Author> usersAuthorMap = new HashMap<Long, Author>();
		List<Long> lstUserId = new ArrayList<Long>(vu360users.size());
		for (VU360User user : vu360users) {
			lstUserId.add(user.getId());
		} 
		List<Author> authors = (List<Author>)authorRepository.findByVu360UserIdIn(lstUserId);
		
		for (Author author : authors) {
			usersAuthorMap.put(author.getVu360User().getId(), author);
		}
		
		return usersAuthorMap;
	}
	public ContentOwner readContentOwnerByGUID(String contentOwnerGUID){
		return contentOwnerRespository.findFirstByGuidOrderByIdAsc(contentOwnerGUID);
	}

	/**
	 * // [12/9/2010] LMS-7512 :: Update Content Owner for Self-Author Customer/Reseller
	 */
	@Transactional
	@Override
	public ContentOwner updateContentOwner(Customer customer) {
		
		ContentOwner contentOwner = customer.getContentOwner();
		if (customer.isSelfAuthor()) {
			if ( customer.isDistributorRepresentative() ) {
				contentOwner.setName(customer.getDistributor().getName());
			}
			else {
				contentOwner.setName(customer.getName());
			}
			contentOwner = contentOwnerRespository.save(contentOwner);
		}		
		return contentOwner;
	}
	
	public Customer getCustomerByContentOwner(ContentOwner contentOwner){
		return customerRepository.findByContentOwner(contentOwner);
	}
	
	public Distributor getDistributorByContentOwner(ContentOwner contentOwner){
		return distributorRepository.findFirstByContentOwner(contentOwner);
	}

	@Override
	@Deprecated
	public boolean isAuthor(long userId){
		return authorRepository.isAuthor(userId);
	}
	/**
	 * 
	 */
	public boolean isAuthor(VU360User user){
		return authorRepository.isThisAuthor(user.getUsername());
	}
	
}
