package com.softech.vu360.lms.web.controller.instructor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.meetingservice.dimdim.DimDimMeetingServiceImpl;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

public class LaunchClassController implements Controller{

	private String launchClassError;
	SynchronousClassService synchronousClassService;
	DimDimMeetingServiceImpl dimdimMeetingService;

	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strClassId = request.getParameter("classId");
		Long classId = Long.valueOf(strClassId);

		//SynchronousClass synchClass = synchronousClassService.getSynchronousClassById(Long.valueOf(classId));
		List<InstructorSynchronousClass> leadInstructors = synchronousClassService.getLeadInstructorsOfSynchClass(classId);
		InstructorSynchronousClass classInstructor = leadInstructors.get(0);
		Brander brander = VU360Branding.getInstance().getBrander(
				(String) request.getSession().getAttribute(VU360Branding.BRAND),
				new com.softech.vu360.lms.vo.Language());

		String startMeetingURL = dimdimMeetingService.startMeeting(classInstructor,brander);
		response.sendRedirect(startMeetingURL);
		return null;
	}
	
	public DimDimMeetingServiceImpl getDimdimMeetingService() {
		return dimdimMeetingService;
	}


	public void setDimdimMeetingService(
			DimDimMeetingServiceImpl dimdimMeetingService) {
		this.dimdimMeetingService = dimdimMeetingService;
	}


	public String getLaunchClassError() {
		return launchClassError;
	}


	public void setLaunchClassError(String launchClassError) {
		this.launchClassError = launchClassError;
	}
	
	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}
	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}	

}
