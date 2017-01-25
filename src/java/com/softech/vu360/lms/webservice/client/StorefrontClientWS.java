/**
 * 
 */
package com.softech.vu360.lms.webservice.client;

//application specific imports
import java.util.List;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.webservice.message.storefront.client.CustomerData;
import com.softech.vu360.lms.webservice.message.storefront.client.UpdateUserAuthenticationCredential;
import com.softech.vu360.lms.webservice.message.storefront.synccustomer.ShowSyncCustomerResponse;
import com.softech.vu360.lms.webservice.message.storefront.tester.ShowUserProfileDisplayResponse;
import com.softech.vu360.util.TreeNode;



/**
 * @author faisalkhwaja
 *
 */
public interface StorefrontClientWS extends ClientAbstractWS {

	public boolean updateProfileEvent( Customer customer);
	public boolean updateProfileOnStorefront( CustomerData msgCustomer, UpdateUserAuthenticationCredential authCredentials );
	public ShowUserProfileDisplayResponse getUserAccountInfo(String userGUID , String transactionGUID, String resellerCode );
	public ShowUserProfileDisplayResponse getUserRewardsInfo(String userGUID , String transactionGUID, String resellerCode );
	//@TODO for noman, add publish course method here with request response parameter 
	public boolean publishCourseEvent(Course course, List<CourseGroup> courseGroups, String distributorCode);
	public ShowSyncCustomerResponse createCustomerOnStoreFront(Customer msgCustomer, String userName, String password);

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
	public List<TreeNode> searchProductOrProductCategories (String storeId, String searchKeyword, int searchType);
}
