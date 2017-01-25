package com.softech.vu360.lms.webservice;

import com.softech.vu360.lms.webservice.message.storefront.CourseDetailsRequest;
import com.softech.vu360.lms.webservice.message.storefront.CourseDetailsResponse;
import com.softech.vu360.lms.webservice.message.storefront.OrderCreatedRequest;
import com.softech.vu360.lms.webservice.message.storefront.OrderCreatedResponse;
import com.softech.vu360.lms.webservice.message.storefront.RefundRequest;
import com.softech.vu360.lms.webservice.message.storefront.RefundResponse;
import com.softech.vu360.lms.webservice.message.storefront.UpdateProfileRequest;
import com.softech.vu360.lms.webservice.message.storefront.UpdateProfileResponse;
import com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApprovalRequest;
import com.softech.vu360.lms.webservice.message.storefront.courseapproval.CourseApprovalResponse;


/**
 * StorefrontWS defines the set of interfaces 
 * to control the interactions and business logic
 * of the integration between Storefront & LMS.
 * 
 * @author jason
 *
 */
public interface StorefrontWS extends AbstractWS {
	
	public OrderCreatedResponse orderCreatedEvent(OrderCreatedRequest orderCreatedEvent);
	public UpdateProfileResponse updateProfileEvent(UpdateProfileRequest updateProfileEvent);
	public RefundResponse refundEvent(RefundRequest refundEvent);
	public CourseDetailsResponse getCourseDetailsEvent(CourseDetailsRequest	 orderCreatedEvent);
	
	public CourseApprovalResponse getCourseApproval(CourseApprovalRequest courseApprovalReq);
}
