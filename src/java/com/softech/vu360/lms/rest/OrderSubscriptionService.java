package com.softech.vu360.lms.rest;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Subscription;
import com.softech.vu360.lms.rest.transformers.Response;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.service.SubscriptionService;

@Controller
@RequestMapping(value = "/subscription")
public class OrderSubscriptionService {
	private static final Logger log = Logger.getLogger(OrderSubscriptionService.class);
	
	@Inject
	SubscriptionService subscriptionService;
	@Inject
	EnrollmentService enrollmentService;
	@Inject
	StatisticsService statisticsService;

	@RequestMapping(value = "/testExpose", method = RequestMethod.GET, produces = "application/json")
	public String processQuery(@RequestParam("status") String status,
			@RequestParam("userName") String userName,
			@RequestParam("statusCode") String statusCode) {
		String jsonString = null;
		Response response = new Response();
		jsonString = transformResponse(response);
		return jsonString;
	}

	@RequestMapping(value = "updateStatus", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String subscriptionCancelQuery(
			@RequestParam("status") String status,
			@RequestParam("subscriptioncode") String subscriptioncode,
			@RequestParam("username") String username) {

		String jsonString = null;
		Response response = new Response();
		String stat = status;
		String subCode = subscriptioncode;
		String uName = username;
		response.setStatus(status);
		response.setSubscriptionCode(subCode);
		response.setUserName(uName);

		if (stat.equals("CANCEL")) {
			boolean subscriptionCancelled = cancelSubscription(subCode);
			if (subscriptionCancelled) {
				// response.setResponseCode("CANCEL_OK");
				jsonString = "OK";
			} else {
				response.setResponseCode("CANCEL_FAILED");
				jsonString = "FAILED";
			}
			// jsonString = transformResponse(response);
			return jsonString;

		}

		if (!stat.equals("CANCEL")) {

			boolean subscriptionStatus = updateSubscription(subCode, status);
			if (subscriptionStatus) {
				// response.setResponseCode("UPDATE_OK");
				jsonString = "OK";
			} else {
				// response.setResponseCode("UPDATE_FAILED");
				jsonString = "FAILED";
			}
			// jsonString = transformResponse(response);
			return jsonString;
		}

		// response.setResponseCode("QUERY_FAILED");
		// jsonString = transformResponse(response);
		jsonString = "QUERY_FAILED";
		return jsonString;

	}

	private boolean cancelSubscription(String subCode) {
		boolean complete = false;
		Subscription subscription;
		subscription = subscriptionService
				.getSubscriptionBySubscriptionCode(subCode);
		subscription.setSubscriptionStatus("CANCEL");
		subscription.getCustomerEntitlement().setEndDate(new Date());
		subscriptionService.saveSubscription(subscription);
		complete = updateLearnerStatus(subscription.getId());

		return complete;
	}

	private boolean updateLearnerStatus(Long subscriptionId) {
		boolean complete = false;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 00);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.set(Calendar.MILLISECOND, 00);
		Date currentDate = cal.getTime();

		List<LearnerEnrollment> leList = enrollmentService
				.getLearnerEnrollmentsBySubscription(subscriptionId);
		if (leList != null) {
			for (LearnerEnrollment le : leList) {
				Long enrollmentId = le.getId();
				List<LearnerCourseStatistics> courseStats = statisticsService
						.getLearnerCoursesbyEnrolmentId(enrollmentId);
				if (courseStats != null && courseStats.size() > 0) {
					le.setEnrollmentStatus(LearnerEnrollment.EXPIRED);
					le.setEnrollmentEndDate(currentDate);
					enrollmentService.updateEnrollment(le);
					complete = true;
				}

			}// update status

		}
		return complete;
	}

	private boolean updateSubscription(String subCode, String status) {
		boolean complete = false;
		Subscription subscription= subscriptionService.getSubscriptionBySubscriptionCode(subCode);
		if(subscription==null){
			log.debug("No Subscription found for SubscriptionCode:"+subCode);
			return false;
		}
		subscription.setSubscriptionStatus(status);
		Subscription updatedSubs = subscriptionService.saveSubscription(subscription);
		if (updatedSubs != null) {
			if (updatedSubs.getSubscriptionStatus().equalsIgnoreCase(status)) {
				complete = true;
			}
		}
		return complete;
	}

	private String transformResponse(Response response) {
		String json = null;

		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		try {
			json = ow.writeValueAsString(response);
		} catch (JsonGenerationException e) {
			//
			e.printStackTrace();
		} catch (JsonMappingException e) {
			//
			e.printStackTrace();
		} catch (IOException e) {
			//
			e.printStackTrace();
		}

		return json;

	}

}
