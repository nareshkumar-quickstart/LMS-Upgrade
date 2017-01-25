package com.softech.vu360.lms.webservice.client.impl;

//application specific imports
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.softech.vu360.lms.model.CommissionProduct;
import com.softech.vu360.lms.model.CommissionProductCategory;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.ScormCourse;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebLinkCourse;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.webservice.client.StorefrontClientWS;
import com.softech.vu360.lms.webservice.commission.CommissionServicesRequest;
import com.softech.vu360.lms.webservice.commission.CommissionServicesResponse;
import com.softech.vu360.lms.webservice.commission.GetProductInformationType;
import com.softech.vu360.lms.webservice.commission.NodeInfo;
import com.softech.vu360.lms.webservice.commission.ProductAndProductGroupList;
import com.softech.vu360.lms.webservice.message.storefront.client.CustomerData;
import com.softech.vu360.lms.webservice.message.storefront.client.GetUpdateUserProfileRequest;
import com.softech.vu360.lms.webservice.message.storefront.client.ShowUpdateUserProfileResponse;
import com.softech.vu360.lms.webservice.message.storefront.client.UpdateUserAuthenticationCredential;
import com.softech.vu360.lms.webservice.message.storefront.coursecatalog.CatalogCoursePublishRequest;
import com.softech.vu360.lms.webservice.message.storefront.coursecatalog.CatalogCoursePublishResponse;
import com.softech.vu360.lms.webservice.message.storefront.coursecatalog.ClassRoomWebinarType;
import com.softech.vu360.lms.webservice.message.storefront.coursecatalog.ContentOwnerType;
import com.softech.vu360.lms.webservice.message.storefront.coursecatalog.CourseAuthorType;
import com.softech.vu360.lms.webservice.message.storefront.coursecatalog.CourseType;
import com.softech.vu360.lms.webservice.message.storefront.coursecatalog.GroupInfoType;
import com.softech.vu360.lms.webservice.message.storefront.coursecatalog.GroupType;
import com.softech.vu360.lms.webservice.message.storefront.synccustomer.GetSyncCustomerRequest;
import com.softech.vu360.lms.webservice.message.storefront.synccustomer.ShowSyncCustomerResponse;
import com.softech.vu360.lms.webservice.message.storefront.tester.GetUserProfileDisplayRequest;
import com.softech.vu360.lms.webservice.message.storefront.tester.ShowUserProfileDisplayResponse;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Properties;

/**
 * This class implements all the interface methods for example,
 * updateProfile which will call stroefront web service to update the details
 * about a user. Little more work still needed.
 * 
 * @author faisal khwaja
**/


public class StorefrontClientWSImpl implements StorefrontClientWS{
	
	private static final Logger log = Logger.getLogger(StorefrontClientWSImpl.class.getName());
	public static final String STOREFRONT_ENDPOINT = VU360Properties.getVU360Property("storeFrontEndPoint"); //look in the vu360-lms.properties file
	public static final String STOREFRONT_USER_ACCOUNT_ENDPOINT = VU360Properties.getVU360Property("storeFrontUserAccountEndPoint"); //look in the vu360-lms.properties file
	public static final String STOREFRONT_CATALOG_SERVICE_ENDPOINT = VU360Properties.getVU360Property("storeFrontCatalogServiceEndPoint");
	public static final String STOREFRONT_CUSTOMER_SERVICE_ENDPOINT = VU360Properties.getVU360Property("storeFrontCustomerServiceEndPoint");
	public static final String STOREFRONT_COMMISSION_ENDPOINT = VU360Properties.getVU360Property("storeFrontCommissionEndPoint"); //look in the vu360-lms.properties file
	
	private static ApplicationContext applicationContext;
	
	
// storeFrontUserAccountEndPoint

	
	/**
	 * updateProfile
	 */
	public boolean updateProfileEvent(Customer customer) {
		if (customer == null )
			return false;
		//From this point on we need to call Stroefront Webservice to update the profile
		CustomerData msgCustomer = transformVOtoRequestObject( customer );
		//call Web service passing in the UpdateProfileRequest object
		boolean ret = updateProfileOnStorefront( msgCustomer, null);
		return ret;
	}
	
	/** 
	 * transformVOtoRequestObject
	 * This method will transform the value object, Customer, into a Request object which will 
	 * be passed to the SF WS layer for processing
	 * @return 
	 */
	private CustomerData transformVOtoRequestObject( com.softech.vu360.lms.model.Customer customer ) {
		log.info( "Transforming Value Object to a message customer object. Last call before lift off!" );
		//Need to transform the customer object to com.softech.vu360.lms.webservice.message.storefront.Customer object
		return getCustomer( customer );
	}


	public static com.softech.vu360.lms.webservice.message.storefront.synccustomer.CustomerData transformCustomerToCustomerData( Customer customer ) {
			
			com.softech.vu360.lms.webservice.message.storefront.synccustomer.CustomerData customerData = new com.softech.vu360.lms.webservice.message.storefront.synccustomer.CustomerData();
	    	customerData.setCustomerGUID(customer.getCustomerGUID());
	    	customerData.setCompanyName(customer.getName());
	    	customerData.setBillingAddress( getAddressForSF(customer, customer.getBillingAddress() ));
	    	customerData.setShippingAddress( getAddressForSF(customer, customer.getShippingAddress() ));
	    	return customerData;
	}

    public static com.softech.vu360.lms.webservice.message.storefront.synccustomer.Address getAddressForSF( Customer customer, com.softech.vu360.lms.model.Address address ) {
    	com.softech.vu360.lms.webservice.message.storefront.synccustomer.Address msgAddress = new com.softech.vu360.lms.webservice.message.storefront.synccustomer.Address();
    	msgAddress.setStreetAddress1( customer.getBillingAddress().getStreetAddress() );
    	msgAddress.setStreetAddress2( customer.getBillingAddress().getStreetAddress2() );
    	msgAddress.setCity( customer.getBillingAddress().getCity() );
    	msgAddress.setCountry( customer.getBillingAddress().getCountry());
    	msgAddress.setState( customer.getBillingAddress().getState() );
    	msgAddress.setZipCode( customer.getBillingAddress().getZipcode() );
    	msgAddress.setFirstName( customer.getFirstName() );
    	msgAddress.setLastName( customer.getLastName() );
    	msgAddress.setPhone( customer.getPhoneNumber() );
    	return msgAddress;
    }  
	/**
	 * Setting the Customer info
	 * @param customer
	 * @return
	 */
    public static CustomerData getCustomer( Customer customer ) {
    	CustomerData customerData = new CustomerData();
    	customerData.setCustomerID(customer.getCustomerGUID());
    	customerData.setCompanyName(customer.getName());
    	customerData.setBillingAddress( getAddress(customer, customer.getBillingAddress() ));
    	customerData.setShippingAddress( getAddress(customer, customer.getShippingAddress() ));
    	return customerData;
    }
    
    /**
     * Setting the Address info
     * @param customer
     * @param address
     * @return
     */
    public static com.softech.vu360.lms.webservice.message.storefront.client.Address getAddress( Customer customer, com.softech.vu360.lms.model.Address address ) {
    	com.softech.vu360.lms.webservice.message.storefront.client.Address msgAddress = new com.softech.vu360.lms.webservice.message.storefront.client.Address();
    	msgAddress.setStreetAddress1( customer.getBillingAddress().getStreetAddress() );
    	msgAddress.setStreetAddress2( customer.getBillingAddress().getStreetAddress2() );
    	msgAddress.setCity( customer.getBillingAddress().getCity() );
    	msgAddress.setCountry( customer.getBillingAddress().getCountry());
    	msgAddress.setState( customer.getBillingAddress().getState() );
    	msgAddress.setZipCode( customer.getBillingAddress().getZipcode() );
    	msgAddress.setFirstName( customer.getFirstName() );
    	msgAddress.setLastName( customer.getLastName() );
    	msgAddress.setPhone( customer.getPhoneNumber() );
    	return msgAddress;
    }  

    /**
     * Setting the Authenteication Details
     * @return
     */
    public static UpdateUserAuthenticationCredential getAuthenticateDetail() {
    	UpdateUserAuthenticationCredential auth = new UpdateUserAuthenticationCredential();
    	auth.setUserPassword("cyber2006");
    	auth.setUserLogonID("abdul.hai@360training.com");
    	return auth;
    }
	
   //@TODO for noman, add publish course method implementation here same like given below method
    
	/**
	 * updateProfileOnStorefront
	 * This is the method which will call the SF layer
	 * @return
	 */
	public boolean updateProfileOnStorefront( CustomerData msgCustomer, UpdateUserAuthenticationCredential authCredentials ) {
    	WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        //webServiceTemplate.setDefaultUri("http://10.0.100.120:81/webapp/wcs/services/UpdateUserProfileServices" );
    	webServiceTemplate.setDefaultUri(STOREFRONT_ENDPOINT);
        GetUpdateUserProfileRequest updateProfileRequest = new GetUpdateUserProfileRequest();
        
        try {
        	//Set the data in the request object
        	updateProfileRequest.setCustomer( msgCustomer );
        	if(authCredentials!=null)
        		updateProfileRequest.setAuthenticationCredential( authCredentials );
        	updateProfileRequest.setTransactionID(GUIDGeneratorUtil.generateGUID());
        	
	    	//set the marshallers/unmarshallars
	    	org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
	    	org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
	    	webServiceTemplate.setMarshaller( marshaller );
	    	webServiceTemplate.setUnmarshaller( unmarshaller );
	    	
	    	//Need to set the contextpath which is just the complete package name used from ObjectFactory class
	    	marshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.client");
	    	unmarshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.client");
	    	
	    	//Need to call following methods to make sure the properties are properly set
	    	marshaller.afterPropertiesSet();
	    	unmarshaller.afterPropertiesSet();
	    	
	    	//Now make the call and pray that everything will work :)
	    	ShowUpdateUserProfileResponse object = (ShowUpdateUserProfileResponse) webServiceTemplate.marshalSendAndReceive(updateProfileRequest);
	    	
        } catch( Exception e ) {
        	e.printStackTrace();
        	log.error("Exception Occured while transofrming & updating learner profile: " + e.getMessage());
        }
        return true;
	}
	
	
	public void updatePassword(VU360User user, String strNewPassword){
		
		try{
			
			UpdateUserAuthenticationCredential authCredentials=new UpdateUserAuthenticationCredential();
			authCredentials.setUserPassword(strNewPassword);
			authCredentials.setUserLogonID(user.getUsername());
			
			
			this.updateProfileOnStorefront(getCustomer(user.getLearner().getCustomer()), authCredentials);
			
        } catch( Exception e ) {
        	e.printStackTrace();
        	log.error("Exception Occured while transofrming & updating learner profile: " + e.getMessage());
        }
        
	}

	public ShowUserProfileDisplayResponse getUserAccountInfo(String userGUID, String transactionGUID,
			String resellerCode) {
		
			//Request object used for communication between client and webservice
			WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
			webServiceTemplate.setDefaultUri(STOREFRONT_USER_ACCOUNT_ENDPOINT);
			//"http://10.0.100.120:81/webapp/wcs/services/UserProfileDisplayServices" );
			  // /UserProfileDisplayServices
			GetUserProfileDisplayRequest lsRequest = new GetUserProfileDisplayRequest();
			ShowUserProfileDisplayResponse object = null;
			try {
				//Set the dates in the request object
				lsRequest.setResellerCode(resellerCode);
				lsRequest.setTransactionGUID(transactionGUID);
				lsRequest.setUserGUID(userGUID );
				
				
				//set the marshallers/unmarshallars
				org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
				org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
				webServiceTemplate.setMarshaller( marshaller );
				webServiceTemplate.setUnmarshaller( unmarshaller );
				
				//Need to set the contextpath which is just the complete package name used from ObjectFactory class
				marshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.tester");
				unmarshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.tester");
				
				//Need to call following methods to make sure the properties are properly set
				marshaller.afterPropertiesSet();
				unmarshaller.afterPropertiesSet();
				
				 
				
				//Now make the call and pray that everything will work :)
				object = (ShowUserProfileDisplayResponse)webServiceTemplate.marshalSendAndReceive(lsRequest);
				

	       } catch( Exception e ) {
	      	e.printStackTrace();
	      }

		return object;
	}

	/**
	 * 
	 * 360training would like to use learner referrals (via e-mail, social media, and similar channels) 
	 * to promote the 360training web stores and their product, in order to increase sales. 
	 * 
	 * http://jira.360training.com/browse/LMS-14265
	 */
	public ShowUserProfileDisplayResponse getUserRewardsInfo(String userGUID, String transactionGUID,String resellerCode) {
		
			//Request object used for communication between client and webservice
			WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
			webServiceTemplate.setDefaultUri(STOREFRONT_USER_ACCOUNT_ENDPOINT);
			//"http://10.0.100.120:81/webapp/wcs/services/UserProfileDisplayServices" );
			  // /UserProfileDisplayServices
			GetUserProfileDisplayRequest lsRequest = new GetUserProfileDisplayRequest();
			ShowUserProfileDisplayResponse object = null;
			try {
				
				  //<TransactionGUID>15186e1d-1a13-11e3-8240-edfe09a0174e</TransactionGUID>
				  //<UserGUID>c2a49bf2-782e-4610-8763-0dca26841c9b</UserGUID>
				  //<ResellerCode>21701</ResellerCode>
				  
				  //Set the dates in the request object
				
				/*			
  					lsRequest.setResellerCode("21701");
					lsRequest.setTransactionGUID("15186e1d-1a13-11e3-8240-edfe09a0174e");
					lsRequest.setUserGUID("c2a49bf2-782e-4610-8763-0dca26841c9b");
				 */
				
				//Set the dates in the request object
				lsRequest.setResellerCode(resellerCode);
				lsRequest.setTransactionGUID(transactionGUID);
				lsRequest.setUserGUID(userGUID );
				
				
				//set the marshallers/unmarshallars
				org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
				org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
				webServiceTemplate.setMarshaller( marshaller );
				webServiceTemplate.setUnmarshaller( unmarshaller );
				
				//Need to set the contextpath which is just the complete package name used from ObjectFactory class
				marshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.tester");
				unmarshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.tester");
				
				//Need to call following methods to make sure the properties are properly set
				marshaller.afterPropertiesSet();
				unmarshaller.afterPropertiesSet();
				
				 
				
				//Now make the call and pray that everything will work :)
				object = (ShowUserProfileDisplayResponse)webServiceTemplate.marshalSendAndReceive(lsRequest);
				

	       } catch( Exception e ) {
	      	e.printStackTrace();
	      }

		return object;
	}
	
	/**
	 * If <code>searchType</code> is:
	 *  0 - Return Products in the form of hierarchical tree, matched against <code>searchKeyword</code> in store represented by <code>storeId</code>
	 *  1 - Return ProductCategories in the form of hierarchical tree, matched against <code>searchKeyword</code> in store represented by <code>storeId</code>
	 * @param storeId
	 * @param searchKeyword
	 * @param searchType
	 * @return List<TreeNode>
	 * @author sm.humayun
	 * @since 4.16 {LMS-3350}
	 */
	@Override
	public List<TreeNode> searchProductOrProductCategories (String storeId, String searchKeyword, int searchType)
	{
		List<TreeNode> rootNodes = new ArrayList<TreeNode>();
		try
		{
			//FileUtils.readLines(new File(VU360Properties.getVU360Property("commissionService.Dummy")));
			rootNodes = searchProductOrProductCategoriesOriginal ( storeId,  searchKeyword,  searchType);
		}
		catch(Exception e)
		{
			log.error(e, e);
		}
		return rootNodes;
	}

	public List<TreeNode> searchProductOrProductCategoriesOriginal (String storeId, String searchKeyword, int searchType)
	{
		log.info("\\n\\n\\n ---------- START : searchProductOrProductCategories() : " + this.getClass().getName() + " ---------- ");
		List<TreeNode> rootNodes = new ArrayList<TreeNode>();
		try
		{
			//Request object used for communication between client and webservice
			WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
			webServiceTemplate.setDefaultUri(STOREFRONT_COMMISSION_ENDPOINT);
			
			CommissionServicesRequest lsRequest = new CommissionServicesRequest();
			CommissionServicesResponse commissionResponse = null;			

			//Set the input parameters in the request object
			GetProductInformationType productInformationType= new GetProductInformationType();
			productInformationType.setStoreID(storeId);
			productInformationType.setSearchKeyword(searchKeyword);
			productInformationType.setSearchType(searchType+"");
			lsRequest.setSearchInfo(productInformationType);								
			
			//set the marshallers/unmarshallars
			org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
			org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
			webServiceTemplate.setMarshaller( marshaller );
			webServiceTemplate.setUnmarshaller( unmarshaller );
			
			//Need to set the contextpath which is just the complete package name used from ObjectFactory class
			marshaller.setContextPath("com.softech.vu360.lms.webservice.commission");
			unmarshaller.setContextPath("com.softech.vu360.lms.webservice.commission");
			
			//Need to call following methods to make sure the properties are properly set
			marshaller.afterPropertiesSet();
			unmarshaller.afterPropertiesSet();
			
			log.info("sending WS request...");
			commissionResponse = (CommissionServicesResponse) webServiceTemplate.marshalSendAndReceive(lsRequest);
			log.info("got response from WS !");
			ProductAndProductGroupList ppgl = commissionResponse.getSearchDataObjectInfo();
			
			List<NodeInfo> nodeInfoList = ppgl.getNodeList();
			if(nodeInfoList != null && nodeInfoList.size() > 0)
			{
				//this loop is just for logging in comming data
				for(NodeInfo nodeInfo : nodeInfoList)
					log.info("GUID=" + nodeInfo.getGUID() + ", Name=" + nodeInfo.getName() 
							+ ", IsProduct=" + nodeInfo.getIsProduct() + ", ParentGUID=" + nodeInfo.getParentGUID());
				
				log.info("transforming root nodes...");
				for(NodeInfo nodeInfo : nodeInfoList)
				{
					if(StringUtils.isEmpty(nodeInfo.getParentGUID()))
					{
						log.info("<GUID=" + nodeInfo.getParentGUID() + ", Name=" + nodeInfo.getName() +">");
						CommissionProductCategory cpg = new CommissionProductCategory();
						cpg.setName(nodeInfo.getName());
						cpg.setProductCategoryCode(nodeInfo.getGUID());
						rootNodes.add(new TreeNode(cpg));
					}
				}

				log.info("transforming root node hierarchies...");
				for(TreeNode rootNode : rootNodes)
					this.transformNodeHierarchy(rootNode, nodeInfoList);
			}
			else
				log.info("nodeList is null or its size is 0");
			
		} 
		catch( Exception e ) 
		{
			log.error(" ########## >>> ERROR <<< ########## ");
			log.error(e, e);
		}
		finally
		{
			log.info(" ---------- END : searchProductOrProductCategories() : " + this.getClass().getName() + " ---------- \\n\\n\\n");
		}
		return rootNodes;
	}
	
	private void transformNodeHierarchy (TreeNode treeNode, List<NodeInfo> nodeInfoList)
	{
		CommissionProductCategory cpc = (CommissionProductCategory) treeNode.getValue();
		CommissionProductCategory childCpc;
		CommissionProduct childCp;
		boolean isProduct;
		for(NodeInfo nodeInfo : nodeInfoList)
		{
			if(StringUtils.isNotEmpty(nodeInfo.getParentGUID()) && StringUtils.isNotEmpty(cpc.getProductCategoryCode()) 
					&& nodeInfo.getParentGUID().equals(cpc.getProductCategoryCode()))
			{
				isProduct = Boolean.parseBoolean(nodeInfo.getIsProduct());
				log.info(StringUtils.left("    ", treeNode.getDepth()) 
						+ "<GUID=" + nodeInfo.getParentGUID() + ", " + (isProduct ? "P" : "PC") + ", Name=" + nodeInfo.getName() 
						+ ", Parent=" + nodeInfo.getParentGUID() + ">");
				if(isProduct)
				{
					childCp = new CommissionProduct();
					childCp.setName(nodeInfo.getName());
					childCp.setProductCode(nodeInfo.getGUID());
					childCp.setCommissionProductCategory(cpc);
					treeNode.addChild(new TreeNode(childCp));
				}
				else
				{
					childCpc = new CommissionProductCategory();
					childCpc.setName(nodeInfo.getName());
					childCpc.setProductCategoryCode(nodeInfo.getGUID());
					childCpc.setParentCommissionProductCategory(cpc);
					treeNode.addChild(new TreeNode(childCpc));
				}
			}
		}
		if(treeNode.getChildCount() > 0)
			for(TreeNode childNode : treeNode.getChildren()){
				
				if(childNode.getValue().getClass().getName().equals("com.softech.vu360.lms.model.CommissionProduct")){
					//CommissionProduct cp = (CommissionProduct) childNode.getValue();
					//childCp = new CommissionProduct();
					//childCp.setName(cp.getName());
					//childCp.setProductCode(nodeInfo.getGUID());
					//childCp.setCommissionProductCategory(cpc);
					//treeNode.addChild(new TreeNode(cp));
				}
				
				else
					transformNodeHierarchy(childNode, nodeInfoList);
			}
				
	}
	
	public boolean publishCourseEvent(Course course, List<CourseGroup> courseGroups, String distributorCode) {
			
			WebServiceTemplate webServiceTemplate = (WebServiceTemplate) applicationContext.getBean("webServiceTemplateCatalogServiceWS");
			webServiceTemplate.setDefaultUri(STOREFRONT_CATALOG_SERVICE_ENDPOINT);
			
			if(course.getBussinesskey()==null || course.getBussinesskey().equals("") && course.getId()!=null)
				course.setBussinesskey(course.getId().toString());
			
			CatalogCoursePublishRequest lsRequest = getCoursePublishDetails(course, courseGroups, distributorCode);
			
			org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
	    	org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
	    	webServiceTemplate.setMarshaller( marshaller );
	    	webServiceTemplate.setUnmarshaller( unmarshaller );
	    	
	    	
	    	//Need to set the contextpath which is just the complete package name used from ObjectFactory class
	    	marshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.coursecatalog");
	    	unmarshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.coursecatalog");
	    	try {
	    	//Need to call following methods to make sure the properties are properly set
	    	marshaller.afterPropertiesSet();
	    	unmarshaller.afterPropertiesSet();
	    	
	    	
				log.debug( "Calling the PUBLISHING method to SF... with DUMMY " + lsRequest );
				//Now make the call and pray that everything will work :)
				CatalogCoursePublishResponse object = (CatalogCoursePublishResponse) webServiceTemplate.marshalSendAndReceive(lsRequest);
				log.debug( "object recvd from publishing...." + object.getTransactionGUID() );
				log.debug("other info: "  + object.getEventDate() );
				log.debug("success: " + object.getSuccess() ); 
				return true;
			} catch (Exception e) {
				log.error("ERROR in PUBLISHING: " + e.getMessage());
				return false;
			}
			
		}
	
	
	private CatalogCoursePublishRequest getCoursePublishDetails(Course course, List<CourseGroup> courseGroups, String distributorCode) {
	       CatalogCoursePublishRequest lsRequest = new CatalogCoursePublishRequest();
	       lsRequest.setGUID(course.getCourseGUID().replace("-", ""));
	       lsRequest.setSTOREID(distributorCode);
	       lsRequest.setCourse( getCourseType(course, courseGroups) );
	       
	       return lsRequest;
	}
	private CourseType getCourseType(Course course, List<CourseGroup> courseGroups) {
		
		
			//SynchronousClassService synchronousClassService  = (SynchronousClassService) applicationContext.getBean("synchronousClassService");
		
			
	       CourseType cType = new CourseType();
	       ContentOwnerType contentType = new ContentOwnerType();
	       
	       if(course.getContentOwner() == null) {
	    	   log.error("Content Owner is not available");
	       } else {
	    	   contentType.setGUID(course.getContentOwner().getGuid());
		       contentType.setName(course.getContentOwner().getName());
	       }
	       cType.setAdditionalMaterials( course.getAdditionalMaterials()==null?"":course.getAdditionalMaterials());
	       cType.setApprovalNumber(new BigInteger(course.getApprovalNumber()==null || StringUtils.isBlank(course.getApprovalNumber()) ?"0":course.getApprovalNumber()));
	       cType.setApprovedCourseHours( course.getApprovedcoursehours()) ;
	       cType.setCEUs(course.getCeus()==0?"0": course.getCeus()+"");
	       cType.setCourseDescription(course.getDescription() );
	       cType.setCourseGroups(getCourseGroupType(course, courseGroups) );
	       cType.setCourseID(course.getCourseGUID().toString());
	       cType.setCourseName(course.getCourseTitle());
	       cType.setCoursePreReq(course.getCoursePrereq()==null?"":course.getCoursePrereq());
	       cType.setCourseTypeCategory(course.getCourseCategory()==null?"":course.getCourseCategory());
	       cType.setCurrency(course.getCurrency()==null || course.getCurrency().equals("$") ?"USD":course.getCurrency());
	       //TODO: This needs to be corrected once we decide what should be the delivery type	       
	       cType.setDeliveryMethod( this.getCoursePublishingType(course) );//(course.getDeliveryMethod()==null?"":course.getDeliveryMethod());
	       cType.setEndofCourseInstructions(course.getEndofCourseInstructions()==null?"":course.getEndofCourseInstructions());
	       cType.setFinalExamInfo(course.getFinalExamInfo()==null?"":course.getFinalExamInfo());
	       cType.setImageofCourse(course.getImageofCourse()==null?"":course.getImageofCourse());
	       cType.setIsDemo(new BigInteger("0") );
	       cType.setLearningObjectives(course.getLearningObjectives()==null?"":course.getLearningObjectives());
	       cType.setProductPrice(new BigDecimal(course.getProductprice()==0?1:course.getProductprice()));
	       cType.setQuizInfo(course.getQuizInfo()==null?"":course.getQuizInfo());
	       cType.setStateRegReq(course.getStateRegistartionRequired()==null?"":course.getStateRegistartionRequired());
	       cType.setSubjectMatterExpertInfo(course.getSubjectMatterExpertInfo()==null?"":course.getSubjectMatterExpertInfo());
	       cType.setTopicsCovered(course.getTopicsCovered()==null?"":course.getTopicsCovered());
	       cType.setTOS(course.getTos()==0?"0": course.getTos()+"");
	       cType.setCourseContentOwner(contentType);
	       cType.setCourseBusinessKey(course.getBussinesskey()==null?"":course.getBussinesskey());
	       cType.setDemoVideoURL(course.getDemoVideoURL() == null ? StringUtils.EMPTY : course.getDemoVideoURL());
	       
	       //LMS-16915
	       cType.setProductOfferPrice(new BigDecimal(0));
   		   cType.setAudience("");
   		   CourseAuthorType cat = new CourseAuthorType();
   		   cType.getCourseAuthor().add(cat);
	       		
       	   ClassRoomWebinarType crw = new ClassRoomWebinarType();
       	   cType.setCourseClassRoomWebinar(crw);       	   
       		
	       return cType;
	}
	
	/**
	 * [09/21/2010] LMS-7113 :: Course publishing option for all type of courses, Instructor Mode only.
	 */
	private String getCoursePublishingType ( Course course ) {
		String courseTypeClassroom = VU360Properties.getVU360Property("course.SynchronousCourseTypes.Classroom");
		String courseTypeWebinar = VU360Properties.getVU360Property("course.SynchronousCourseTypes.Webinar");
		String courseType = "SYNCHRONOUSCOURSEONLINE"; // set as default value
		
		if ( course instanceof DiscussionForumCourse ) {
			courseType = "DFC";
		}
		else if ( course instanceof ScormCourse ) {
			courseType = "SCORM";
		}
		else if ( course instanceof WebLinkCourse ) {
			courseType = "WEBLINK";
		}
		else if ( course instanceof SynchronousCourse ) {
			//LMS-15724
			//Synchronous Course can publish course if his delivery method set Only Classroom or Webinar.
			//if(course.getDeliveryMethodId()!=null && course.getDeliveryMethodId()==7){
				courseType = courseTypeClassroom;
			//}else if(course.getDeliveryMethodId()!=null && course.getDeliveryMethodId()==8){
			//	courseType = courseTypeWebinar;
			//}
		}
		else if ( course instanceof WebinarCourse ) {
			//LMS-15724
			//Synchronous Course can publish course if his delivery method set Only Classroom or Webinar.
			//if(course.getDeliveryMethodId()!=null && course.getDeliveryMethodId()==7){
				//courseType = courseTypeClassroom;
			//}else if(course.getDeliveryMethodId()!=null && course.getDeliveryMethodId()==8){
				courseType = courseTypeWebinar;
			//}
		}
		
		return courseType;
	}
	
	private GroupType getCourseGroupType(Course course, List<CourseGroup> courseGroups) {
		 GroupType gType = new GroupType();
		 GroupInfoType giType = null;
		 
		 if(courseGroups==null || courseGroups.size()==0){//i.e. this course has no coursegroups associated so create a misc. one
			 giType=new GroupInfoType();
			 giType.setGroupID(course.getId().toString());
			 giType.setGroupName("Miscellaneous");
			 giType.setParentGroupID("");
			 giType.setContainsCourse(true);
			 
			 gType.getGroup().add( giType );
		 }
		 else{
			 for(CourseGroup courseGroup : courseGroups){
				 giType=new GroupInfoType();
				 giType.setGroupID(courseGroup.getGuid());
				 giType.setGroupName(courseGroup.getName());
				 giType.setParentGroupID((courseGroup.getParentCourseGroup()!=null)? courseGroup.getParentCourseGroup().getGuid().toString(): "");
				 giType.setContainsCourse(true);
				 
				 gType.getGroup().add( giType );
			 }
		 }
	   	 return gType;
	}
	
/*
	public static CatalogCoursePublishRequest getCoursePublishDetails() {
	       CatalogCoursePublishRequest lsRequest = new CatalogCoursePublishRequest();
	       lsRequest.setGUID("b5c9627949174d66b1ce05274c33bd63");
	       lsRequest.setCourse( getCourseType() );
	       return lsRequest;
	}

	public static CourseType getCourseType() {
	       CourseType cType = new CourseType();
	       cType.setAdditionalMaterials( "" );
	       cType.setApprovalNumber( new BigInteger("2") );
	       cType.setApprovedCourseHours( new BigInteger("3") );
	       cType.setCEUs("CEUS" );
	       cType.setCourseDescription("courseDescrfiption" );
	       cType.setCourseGroups(getCourseGroupType() );
	       cType.setCourseID("courseID" );
	       cType.setCourseName("courseName");
	       cType.setCoursePreReq("coursePreReq");
	       cType.setCourseTypeCategory("courseCat");
	       cType.setCurrency("USD");
	       cType.setDeliveryMethod("deliverymethod");
	       cType.setEndofCourseInstructions("endofcourseinsturctuions");
	       cType.setFinalExamInfo("finalExaminfo");
	       cType.setImageofCourse("imageofCourse");
	       cType.setIsDemo(new BigInteger("1") );
	       cType.setLearningObjectives("learnignobjective");
	       cType.setProductPrice(new BigDecimal("1.0") );
	       cType.setQuizInfo("quizinfo");
	       cType.setStateRegReq("statereg");
	       cType.setSubjectMatterExpertInfo("subjectmatterexprtinf");
	       cType.setTopicsCovered("topicscovered");
	       cType.setTOS("100");
	       return cType;
	}

	public static GroupType getCourseGroupType() {
		GroupType gType = new GroupType();
		 GroupInfoType giType = new GroupInfoType();
		 giType.setGroupID("1111");
		 giType.setGroupName("groupname");
		 giType.setParentGroupID( "" );
		 giType.setContainsCourse(true);
		gType.getGroup().add( giType );
		return gType;
	}*/


	public ShowSyncCustomerResponse createCustomerOnStoreFront(Customer customer, String userName, String password){
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        //webServiceTemplate.setDefaultUri("http://10.0.100.120:81/webapp/wcs/services/UpdateUserProfileServices" );
    	webServiceTemplate.setDefaultUri(STOREFRONT_CUSTOMER_SERVICE_ENDPOINT);
        GetSyncCustomerRequest createCustomerRequest = new GetSyncCustomerRequest();
        com.softech.vu360.lms.webservice.message.storefront.synccustomer.UpdateUserAuthenticationCredential credentials = new com.softech.vu360.lms.webservice.message.storefront.synccustomer.UpdateUserAuthenticationCredential();
        
        com.softech.vu360.lms.webservice.message.storefront.synccustomer.CustomerData msgCustomer = transformCustomerToCustomerData(customer);
        
        
        try {
        	//Set the data in the request object
        	createCustomerRequest.setCustomer( msgCustomer );

        	createCustomerRequest.setTransactionID(GUIDGeneratorUtil.generateGUID());
        	createCustomerRequest.setStoreID(customer.getDistributor().getDistributorCode());
        	
        	credentials.setUserLogonID(userName);
        	credentials.setUserPassword(password);
        	
        	createCustomerRequest.setAuthenticationCredential(credentials);
        	
	    	//set the marshallers/unmarshallars
	    	org.springframework.oxm.jaxb.Jaxb2Marshaller marshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
	    	org.springframework.oxm.jaxb.Jaxb2Marshaller unmarshaller = new org.springframework.oxm.jaxb.Jaxb2Marshaller();
	    	webServiceTemplate.setMarshaller( marshaller );
	    	webServiceTemplate.setUnmarshaller( unmarshaller );
	    	
	    	//Need to set the contextpath which is just the complete package name used from ObjectFactory class
	    	marshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.synccustomer");
	    	unmarshaller.setContextPath("com.softech.vu360.lms.webservice.message.storefront.synccustomer");
	    	
	    	//Need to call following methods to make sure the properties are properly set
	    	marshaller.afterPropertiesSet();
	    	unmarshaller.afterPropertiesSet();
	    	
	    	//Now make the call and pray that everything will work :)
	    	ShowSyncCustomerResponse object = (ShowSyncCustomerResponse) webServiceTemplate.marshalSendAndReceive(createCustomerRequest);
	    	return object;
	    	
        } catch( Exception e ) {
        	e.printStackTrace();
        	log.error("Exception Occured while Creating Customer on SF : " + e.getMessage());
        	return null;
        }
        
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		StorefrontClientWSImpl.applicationContext = applicationContext;
	}
	
}
