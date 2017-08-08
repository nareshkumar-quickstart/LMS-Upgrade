package com.softech.vu360.lms.rest;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.impl.VU360UserServiceImpl;
import com.softech.vu360.lms.web.restful.request.EnrollmentRequest;



@Controller
@RequestMapping(value = "/subscriptionEnrollment")
public class SubscriptionEnrollmentService {

	@Inject
	VU360UserServiceImpl vU360UserServiceImp;

	@Inject
	private EnrollmentService enrollmentService;

	@RequestMapping(value = "enroll", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> subscriptionCancelQuery(@RequestBody EnrollmentRequest enrollReq) {

		String crsId = enrollReq.getCourseId();
		String subCode = enrollReq.getSubscriptionId();
		String uName = enrollReq.getUserName();
		String courseGroupGUID = enrollReq.getCourseGroupGUID();

		if (crsId.isEmpty() || subCode.isEmpty() || uName.isEmpty()){
			String json= new String("{\"message\":\"courseid, username, subscriptionid cannot be null\"}");
			return new ResponseEntity<String>(json,HttpStatus.BAD_REQUEST);
		}

		List<VU360User> user = vU360UserServiceImp.getActiveUserByUsername(uName);
		Iterator<VU360User> iterator = user.iterator();
		VU360User userItr = null;
		while (iterator.hasNext()) {
			userItr= (VU360User)iterator.next();
		}

		if (userItr == null){
			String userMsg = "user does not exist";
			String json= new String("{\"message\":\""+userMsg+"\"}");
			return new ResponseEntity<String>(json,HttpStatus.NOT_FOUND);
		}
		LearnerEnrollment le = null;
		try{
			le = enrollmentService.addSubscriptionEnrollments(userItr.getLearner(),subCode , crsId);//.addSelfEnrollmentsForSubscription(userItr.getLearner(),subCode , crsId);
		}catch(Exception e){
			e.printStackTrace();
			String userMsg = "Either courseid or subscriptionid does not exist";
			String json= new String("{\"message\":\""+userMsg+"\"}");
			return new ResponseEntity<String>(json,HttpStatus.NOT_FOUND);
		}
		//context.put("enrollment",le);
		Long id=le.getId();
		String msg= new String("{\"enrollmentId\":\""+id+"\"}");
		return new ResponseEntity<String>(msg,HttpStatus.CREATED);

	}

}
