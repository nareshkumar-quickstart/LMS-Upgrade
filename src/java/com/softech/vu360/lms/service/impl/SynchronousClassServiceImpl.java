package com.softech.vu360.lms.service.impl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar; // Sana Majeed | LMS-4132
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.inject.Inject;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.ContentOwner;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.FinalScoreCourseActivity;
import com.softech.vu360.lms.model.Gradebook;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.MyWebinarPlace;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.TimeZone;// Sana Majeed | LMS-4132
import com.softech.vu360.lms.repositories.CourseRepository;
import com.softech.vu360.lms.repositories.GradeBookRepository;
import com.softech.vu360.lms.repositories.InstructorRepository;
import com.softech.vu360.lms.repositories.InstructorSynchronousClassRepository;
import com.softech.vu360.lms.repositories.LearnerEnrollmentRepository;
import com.softech.vu360.lms.repositories.SynchronousClassRepository;
import com.softech.vu360.lms.repositories.SynchronousSessionRepository;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.service.TimeZoneService;
import com.softech.vu360.lms.vo.SynchrounousSessionVO;
import com.softech.vu360.lms.web.controller.model.AssignInstructorForm;
import com.softech.vu360.lms.web.controller.model.ClassForm;
import com.softech.vu360.lms.web.controller.model.SessionForm;
import com.softech.vu360.lms.webservice.client.impl.RestClient;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.DateUtil;
import com.softech.vu360.util.GUIDGeneratorUtil;
import com.softech.vu360.util.VU360Properties;

/**
 * 
 * @author noman
 * 
 */
public class SynchronousClassServiceImpl implements SynchronousClassService {

	@Inject
	private SynchronousSessionRepository synchronousSessionRepository;
	private ResourceService resourceService;
	TimeZoneService timeZoneService = null;

	@Inject
	private CourseRepository courseRepository =  null;
	
	@Inject
	private InstructorRepository instructorRepository=null;
	
	@Inject
	private InstructorSynchronousClassRepository instructorSynchronousClassRepository=null;
	
	@Inject
	private SynchronousClassRepository synchronousClassRepository;
	
	@Inject
	GradeBookRepository gradebookRepository;
	
	@Inject
	LearnerEnrollmentRepository learnerEnrollmentRepository;
	
	/**
	 * addSynchronousSession is used for adding the single or multiple session
	 * in existing synchronousclass or brand new synchronousclass (within a
	 * single screen, instructor or admin wants to create the single or multiple
	 * session of a class, all session belongs to only one instructor, therefore
	 * we have a list of synchronousSession and an object of
	 * instructorSynchronousSession for creating a single or bulk session.
	 * 
	 * @return
	 */
	// public String addSynchronousSession(List<SynchronousSession>
	// lstSynchronousSession, InstructorSynchronousClass instSynchClass) {
	// String temp = "testing1";
	// return temp;
	// }

	public SynchronousSession loadForUpdateSynchronousSession(long id) {
		SynchronousSession synchSession = synchronousSessionRepository
				.findOne(id);
		return synchSession;
	}

	public SynchronousClass loadForUpdateSynchronousClass(long id) {
		SynchronousClass synchronousClass = synchronousClassRepository
				.findOne(id);
		return synchronousClass;
	}

	public SynchronousSession findSynchClassSessionBySessionId(Long id) {
		SynchronousSession synchSession = synchronousSessionRepository
				.findOne(id);
		return synchSession;
	}

	public InstructorSynchronousClass findInstructorSynchClassById(Long id) {
		InstructorSynchronousClass instructorSynchronousClass = instructorSynchronousClassRepository
				.findOne(id);
		return instructorSynchronousClass;
	}

	public InstructorSynchronousClass loadForUpdateInstructorSynchClassById(
			Long classId) {
		InstructorSynchronousClass instructorSynchronousClass = instructorSynchronousClassRepository
				.findOne(classId);
		return instructorSynchronousClass;
	}

	public InstructorSynchronousClass findInstructorSynchClassByClassId(
			Long classId, Long instructorId) {
		InstructorSynchronousClass instructorSynchronousClass = instructorSynchronousClassRepository
				.findBySynchronousClassIdAndInstructorId(classId, instructorId);
		return instructorSynchronousClass;
	}

	public List<InstructorSynchronousClass> findInstructorsByClassId(
			Long synchClassId, String firstName, String lastName,
			String instType) {
		List<Instructor> instructors = null;
		List<InstructorSynchronousClass> synchClassInstructors = null;
		synchClassInstructors = instructorSynchronousClassRepository.
				findSynchronousClassInstructors(synchClassId,
						firstName, lastName, instType);

		/*
		 * Vector<Long> instructorIds =
		 * instructorDAO.getInstructorIdsByClassId(synchClassId, instType);
		 * 
		 * if (instructorIds.size() != 0) instructors =
		 * instructorDAO.findSynchClassInstructorsByInstIds(instructorIds,
		 * firstName, lastName);
		 * 
		 * return instructors;
		 */
		return synchClassInstructors;
	}

	public List<Instructor> getInstructors2AssignSynchClass(Long classId,
			String firstName, String lastName, com.softech.vu360.lms.vo.ContentOwner contentOwner) {
		List<Instructor> instructors = null;

		String instType=null;
		List<InstructorSynchronousClass> objs = new ArrayList();
		Vector<Long> instructorIds = new Vector();
		
		if ( StringUtils.isNotBlank(instType)) {
			objs = instructorSynchronousClassRepository.findBySynchronousClassIdAndInstructorType(classId,instType);
		}else{
			objs = instructorSynchronousClassRepository.findBySynchronousClassId(classId);
		}
		
		
		for(InstructorSynchronousClass obj : objs){
			instructorIds.add(obj.getInstructor().getId());
			
		}
		
		/*Vector<Long> instructorIds = instructorDAO.getInstructorIdsByClassId(classId, null);*/
		
		instructors = instructorRepository.findByIdNotInAndFirstNameAndLastNameAndContentOwnerId(
				instructorIds, firstName, lastName, contentOwner.getId());

		return instructors;

	}
	
	public Course getInstructorCourse(Long courseId)
	{
		return courseRepository.findOne(courseId);
	}

	/**
	 * it returns all the active Synchronousclasses
	 * 
	 * @return
	 */
	public List<SynchronousClass> getAllSynchronousClasses(Long instructorId) {
		
		List<InstructorSynchronousClass> instructorSynchronousClasses = instructorSynchronousClassRepository.findByInstructorIdAndSynchronousClassCourseRetired(instructorId, false);
		List<SynchronousClass> synchronousClasses = new ArrayList<SynchronousClass>();
		if( instructorSynchronousClasses != null && instructorSynchronousClasses.size() > 0){
			for (InstructorSynchronousClass insSynClass : instructorSynchronousClasses) {
				synchronousClasses.add( insSynClass.getSynchronousClass() );
			}
		}
			
	 
		
		return synchronousClasses;
	}

	public List<SynchronousSession> getSynchronousSessionsByClassId(
			Long synchClassId) {
		List<SynchronousSession> synchSessionList = null;
		try {
			synchSessionList = synchronousSessionRepository
					.getSynchronousSessionsByClassId(synchClassId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return synchSessionList;
	}

	public SynchronousClass getSynchronousClassById(Long id) {
		SynchronousClass synchClass = null;
		try {
			synchClass = synchronousClassRepository.findOne(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return synchClass;
	}

	public SynchronousClass getSynchronousClassByGUID(String guid) {
		SynchronousClass synchClass = null;
		try {
			synchClass = synchronousClassRepository.findByGuid(guid);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return synchClass;
	}

	/*
	 * It will return all the synchronousClasses available on the basis of
	 * course id
	 */
	public Map<Object, Object> findClassesByCourseId(Long courseId,
			String className, String startDate, String endDate, int pageIndex,
			int pageSize, String sortBy, int sortDirection, int maxLimit) {

		Map<Object, Object> results = synchronousClassRepository
				.findSynchClassesByCourseId(courseId, className, startDate,
						endDate, pageIndex, pageSize, sortBy, sortDirection,
						maxLimit);

		return results;
	}

	
	/*
	 * It will return all the synchronousClasses available on the basis of
	 * course id
	 */
	public Map<Object, Object> findAllClassesByCourseId(Long courseId,
			String className, String startDate, String endDate, String sortBy,
			int sortDirection, int maxLimit) {

		Map<Object, Object> results = synchronousClassRepository
				.findAllSynchClassesByCourseId(courseId, className, startDate,
						endDate, sortBy, sortDirection, maxLimit);

		return results;
	}

	/* deleting classes and related class sessions */
	public void deleteSynchronousClass(Long instructorIdArray[]) {
		List<Long> instructorIdArrays=Arrays.asList(instructorIdArray);
		synchronousClassRepository.deleteByIdIn(instructorIdArrays);
	}

	@Transactional
	public void deleteSynchronousClassSession(Long sessionIdArray[]) {
		synchronousSessionRepository.deleteSynchronousSessions(sessionIdArray);
	}

	/*
	 * deleting only the mapping entries of instructor from
	 * instructor_synchronousclass synchronousClassInstructorIdArray is the PK
	 * id of Instructor_SynchronousClass
	 */
	public void deleteSynchronousClassInstructor(
			long[] synchronousClassInstructorIdArray) {
		List<long[]> idList=Arrays.asList(synchronousClassInstructorIdArray);
		instructorSynchronousClassRepository.deleteByIdIn(idList);
	}

	public void saveSynchronousClassInstructorRole(
			InstructorSynchronousClass instructorSynchClass) {
		instructorSynchronousClassRepository
				.save(instructorSynchClass);
	}

	public void saveSynchronousClass(SynchronousClass synchClass) {
		synchronousClassRepository.save(synchClass);
	}

	public List<SynchronousSession> findSynchronousSessionByClassId(
			Long synchClassId, SessionForm sessionForm) {
		Date startDateTime = sessionForm.getStartDateTime();
		Date endDateTime = sessionForm.getEndDateTime();
		List<SynchronousSession> synchSession = synchronousSessionRepository
				.findSynchronousSessionByClassId(synchClassId, startDateTime,
						endDateTime);
		return synchSession;
	}

	public void addSynchronousSession(SessionForm sessionForm) {
		SynchronousSession synchSession = new SynchronousSession();
		SynchronousClass synchronousClass = synchronousClassRepository
				.findOne(sessionForm.getId());

		synchSession.setSynchronousClass(synchronousClass);
		synchSession.setStartDateTime(sessionForm.getStartDateTime());
		synchSession.setEndDateTime(sessionForm.getEndDateTime());

		synchronousSessionRepository.save(synchSession);

		// Now update SyncClass's End Time; LMS-8489
		if (synchronousClass.getClassEndDate().before(
				synchSession.getEndDateTime())) {
			synchronousClass = synchronousClassRepository
					.findOne(sessionForm.getId());
			synchronousClass.setClassEndDate(synchSession.getEndDateTime());
			synchronousClassRepository.save(synchronousClass);

		}

	}

	public void saveSynchronousSession(SynchronousSession synchronousSession) {
		synchronousSessionRepository.save(synchronousSession);

		// Now update SyncClass's End Time; LMS-8489
		SynchronousClass synchronousClass = synchronousSession
				.getSynchronousClass();
		if (synchronousClass.getClassEndDate().before(
				synchronousSession.getEndDateTime())) {
			synchronousClass = synchronousClassRepository
					.findOne(synchronousClass.getId());
			synchronousClass.setClassEndDate(synchronousSession
					.getEndDateTime());
			synchronousClassRepository.save(synchronousClass);

		}
	}

	public InstructorSynchronousClass assignInstructors2SynchClass(
			AssignInstructorForm assignInstructorForm) {
		Long classId = assignInstructorForm.getId();
		List<Instructor> instructors = assignInstructorForm
				.getSelectedInstructors();
		String instType = assignInstructorForm.getInstType();

		SynchronousClass synchronousClass = synchronousClassRepository
				.findOne(classId);
		InstructorSynchronousClass instructorSynchronousClass =
				assignInstructors2SynchronousClass(synchronousClass,
						instructors, instType);

		return instructorSynchronousClass;
	}
	
	public InstructorSynchronousClass assignInstructors2SynchronousClass(SynchronousClass synchronousClass,
			List<Instructor> instructors, String instType){
		InstructorSynchronousClass clone=null;
		if(instructors!=null && instructors.size()>0){
			for(Instructor instructor:instructors){
				InstructorSynchronousClass instSynchClass=new InstructorSynchronousClass();
				instSynchClass.setInstructor(instructor);
				instSynchClass.setInstructorType(instType);
				instSynchClass.setSynchronousClass(synchronousClass);
				instructorSynchronousClassRepository.save(instSynchClass);
				clone=instSynchClass;
			}
		}
		
		return clone;
	}

	@Override
	public List<SynchronousClass> getSynchronousClassByCourseId(Long courseId) {
		return synchronousClassRepository.findByCourse_Id(courseId);
	}

	/**
	 * it returns all the active Synchronoussessions in a particular class
	 * 
	 * @return
	 */
	// public List<SynchronousSession>
	// getAllSynchronousSessionBySychronousClassID(Long id) {
	// return new ArrayList();
	// }

	/**
	 * this will add new class and their sessions and also update course name
	 */
	public void addSchedule(ClassForm classForm) {
		
		//Make a call to to third party WS to get webinar meeting ID.
		if(classForm.getMeetingType()!=null && classForm.getMeetingType().equals("Webinar")){

			String meetingID = getMeetingInfoForSessions(populateMyWebinarPlaceBean(classForm));
			classForm.setMeetingId(meetingID);
			
		}
		
		
		SynchronousClass synchronousClass =	addSynchronousSchedule(classForm);
		Gradebook gradebook = new Gradebook();
		gradebook.setSynchronousClass(synchronousClass);
		gradebook = gradebookRepository.save(gradebook);

		// Note Added as per LMS-7971
		CourseActivity activity = null;
		activity = new FinalScoreCourseActivity();
		activity.setGradeBook(gradebook);
		activity.setActivityName("FinalScoreCourseActivity");
		resourceService.saveCourseActivity(activity);
	}
	
/*@Kaunain - added to support SynchronousClass addSchedule earlier written on Dao Layer*/
	
	public SynchronousClass addSynchronousSchedule(ClassForm classForm){

				Course synchronousCourse = courseRepository.findOne(classForm.getId());
				synchronousCourse.setName(classForm.getCourseName());

				SynchronousClass synchronousClass = new SynchronousClass();
				synchronousClass.setSectionName(classForm.getClassName());
				synchronousClass.setClassStartDate(classForm.getStartDateDTF());
				synchronousClass.setClassEndDate(classForm.getEndDateDTF());
				synchronousClass.setClassStatus(classForm.getStatus());
				synchronousClass.setEnrollmentCloseDate(classForm
						.getEnrollmentCloseDate());
				synchronousClass.setGuid(GUIDGeneratorUtil.generateGUID().replace("-",
						""));

				if (classForm.getClassSize() != null
						&& classForm.getClassSize().length() != 0)
					synchronousClass
							.setMaxClassSize(new Long(classForm.getClassSize()));

				// Sana Majeed | LMS-4180
				if (classForm.isOnlineMeetingTF()) { // save only IF meeting type is
														// 'online' else null
					synchronousClass.setMeetingType(classForm.getMeetingType());
						synchronousClass.setMeetingID(classForm.getMeetingId());
						synchronousClass.setMeetingPassCode(classForm
								.getMeetingPasscode());
						synchronousClass.setMeetingURL(classForm.getMeetingURL());
						
						synchronousClass.setPresenterFirstName(classForm.getPresenterFirstName());
						synchronousClass.setPresenterLastName(classForm.getPresenterLastName());
						synchronousClass.setPresenterEmailAddress(classForm.getPresenterEmailAddress());
						
				}
				synchronousClass.setTimeZone(classForm.getTimeZone());
				//added by muhammad.akif 
				//--LMS-16163 for course completion mode
				synchronousClass.setAutomatic(classForm.isAutomatic());
				
				// synchronousClass.setSynchronousCourse(synchronousCourse);
				synchronousClass.setCourse(synchronousCourse);

				for (SessionForm sessionForm : classForm.getClassSessionList()) {
					SynchronousSession synchronousSession = new SynchronousSession();
					synchronousSession.setSynchronousClass(synchronousClass);
					synchronousSession.setStartDateTime(sessionForm.getStartDateTime());
					synchronousSession.setEndDateTime(sessionForm.getEndDateTime());
					synchronousSessionRepository.save(synchronousSession);
				}
		return synchronousClass;
	}

	public String getMeetingInfoForSessions(MyWebinarPlace myWebinarPlace){
		
		String path = VU360Properties.getVU360Property("lms.instructor.mywebinarplace.api").concat(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.api.key"));
        int timeout = 30*60*1000;
        String meetingGUID=null;
        try{
        	String callResponse = new RestClient().postForObject(myWebinarPlace, path, timeout);

			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(callResponse);

			if(jsonObject.get("mt_number")!=null ) {
				meetingGUID = (String)jsonObject.get("mt_number");
			}
        }
        catch (Exception ex) {
        	ex.printStackTrace();
		}
        return meetingGUID;
	}
	
	public boolean deleteMyWebinarPlaceMeeting(String meetingID) {
		StringBuilder restURL = new StringBuilder(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.api"));
        try {
        	restURL.append("/").append(meetingID).append(VU360Properties.getVU360Property("lms.instructor.mywebinarplace.api.key"));        	
        	URL url = new URL(restURL.toString());
        	HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        	httpCon.setDoOutput(true);
        	httpCon.setRequestProperty(
        	    "Content-Type", "application/x-www-form-urlencoded" );
        	httpCon.setRequestMethod("DELETE");
        	if(httpCon.getResponseCode() == 200) return true;
        }
        catch (Exception ex) {
        	ex.printStackTrace();
		}
        return false;
	}
	
	private MyWebinarPlace populateMyWebinarPlaceBean(ClassForm classForm) {
		
		MyWebinarPlace webinar = new MyWebinarPlace();
		webinar.setTopic(classForm.getClassName());
		webinar.setAgenda(classForm.getCourseName());
		webinar.setEvent_reference(classForm.getClassName());
		
		//Time calculation
		
		Date startDate = DateUtil.getDateObject(classForm.getStartDate());
		Date endDate = DateUtil.getDateObject(classForm.getEndDate());
		
		Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		if(classForm.getStartDateTime()!=null)
			calendar.setTime(classForm.getStartDateTime());   // assigns calendar to given date
		else
			calendar.setTime(classForm.getStartDateDTF());   // assigns calendar to given date
		calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
		calendar.get(Calendar.HOUR);        // gets hour in 12h format
		calendar.get(Calendar.MONTH);       // gets month number, NOTE this is zero based!
		calendar.get(Calendar.AM_PM);		// Gets AM/PM
		
		Calendar endCalendar = GregorianCalendar.getInstance(); // creates a new calendar instance
		if(classForm.getEndDateTime()!=null)
			endCalendar.setTime(classForm.getEndDateTime());   // assigns calendar to given date
		else
			endCalendar.setTime(classForm.getEndDateDTF());   // assigns calendar to given date
		
		long dminutes = (calendar.getTimeInMillis() - endCalendar.getTimeInMillis()) / (60 * 1000);
		
		
		webinar.setDate(classForm.getStartDate());//"2001-11-12"
		webinar.setTime_hrs(String.valueOf(calendar.get(Calendar.HOUR)));
		webinar.setTime_mins(String.valueOf(dminutes));
		webinar.setTime_type(String.valueOf(calendar.get(Calendar.AM_PM)));
		
		
		return webinar;
	}
	
	

	public List<InstructorSynchronousClass> getLeadInstructorsOfSynchClass(
			Long classId) {
		return instructorSynchronousClassRepository.getLeadInstructorsOfSynchClass(classId);
	}

	public List<InstructorSynchronousClass> getAllInstructorsOfSynchClass(
			Long classId) {
		List<InstructorSynchronousClass> listInstructor = null;
		listInstructor = instructorSynchronousClassRepository.findBySynchronousClassId(classId);
		return listInstructor;
	}

	public List<SynchronousSession> getAllClassSessionsOfSynchClass(Long classId) {
		List<SynchronousSession> synchronousSessionList = null;
		synchronousSessionList = synchronousSessionRepository.getSynchronousSessionsByClassId(classId);
		return synchronousSessionList;
	}

	public List<SynchronousClass> getAllSynchClassesOfCourse(Long courseId) {
		List<SynchronousClass> synchClass = null;
		synchClass = synchronousClassRepository.findByCourse_Id(courseId);
		return synchClass;
	}

	public List<Course> getCourseByGUID(Vector<String> guid) {
		List<Course> courseList = null;
		
		//courseList = courseAndCourseGroupDAO.getCourseByGUID(guid);
		courseList = courseRepository.findByCourseGUIDIn(guid);
		return courseList;
	}

	public List<SynchronousClass> getSynchronousClassOfCourses(
			Vector<Long> courseIds) {
		return synchronousClassRepository.findByCourse_IdIn(courseIds);
	}

	// Sana Majeed | LMS-4132
	@Override
	public boolean checkIfSessionInProgress(
			List<SynchronousSession> syncSessions, TimeZone classTimeZone,
			TimeZone learnerTimeZone, Brander brand) {
		if (syncSessions != null
				&& syncSessions.size() > 0
				&& syncSessions.get(0).getSynchronousClass().getMeetingType() == null)
			return false;
		if (learnerTimeZone == null) {
			learnerTimeZone = timeZoneService.getDefaultLernerTimeZone(brand);
		}
		boolean isInSession = false;

		// calculate difference between learner and class time zones
		int hrDiff = learnerTimeZone.getHours() - classTimeZone.getHours();
		int minDiff = (learnerTimeZone.getMinutes() + classTimeZone
				.getMinutes()) % 60;

		if (hrDiff < 0) {
			hrDiff -= ((learnerTimeZone.getMinutes() + classTimeZone
					.getMinutes()) / 60);
			minDiff *= (-1);
		} else {
			hrDiff += ((learnerTimeZone.getMinutes() + classTimeZone
					.getMinutes()) / 60);
		}

		for (SynchronousSession syncSession : syncSessions) {

			if (syncSession.getStatus().equals("In Progress")) {
				isInSession = true;
			}
			// adjust class start time according to learner time zone
			Calendar startTime = Calendar.getInstance();
			startTime.setTime(syncSession.getStartDateTime());
			syncSession.setStartDateTimeZ(syncSession.getStartDateTime());
			startTime.add(Calendar.HOUR_OF_DAY, hrDiff);
			startTime.add(Calendar.MINUTE, minDiff);
			syncSession.setStartDateTimeZ(startTime.getTime());

			// adjust class end time according to learner time zone
			Calendar endTime = Calendar.getInstance();
			endTime.setTime(syncSession.getEndDateTime());
			syncSession.setEndDateTimeZ(syncSession.getEndDateTime());
			endTime.add(Calendar.HOUR_OF_DAY, hrDiff);
			endTime.add(Calendar.MINUTE, minDiff);
			syncSession.setEndDateTimeZ(endTime.getTime());

		} // end-for(SynchronousSession syncSession : syncSessions)

		return isInSession;
	}

	public TimeZoneService getTimeZoneService() {
		return timeZoneService;
	}

	public void setTimeZoneService(TimeZoneService timeZoneService) {
		this.timeZoneService = timeZoneService;
	}

	/**
	 * LMS-4189 | Pagination on View Session
	 */
	@Override
	public Map<Object, Object> getSynchronousSessionsByClassId(
			Long synchClassId, int pageIndex, int pageSize, String sortBy,
			int sortDirection) {

		return synchronousSessionRepository.getSynchronousSessionsByClassId(
				synchClassId, pageIndex, pageSize, sortBy, sortDirection);

	}

	public SynchrounousSessionVO getMinMaxScheduleDateForSynchronousClass(
			Long classId) {

		return synchronousSessionRepository
				.getMinMaxScheduleDateForSynchronousClass(classId);
	}

	/**
	 * [8/10/2010] LMS-6657 :: Use of aggregate function to get the Enrollment
	 * count By Class ID instead of loading all objects
	 */
	@Override
	public Long getEnrollmentCountBySynchronousClassId(Long synchronousClassId) {
		return learnerEnrollmentRepository.getEnrollmentCountBySynchronousClassId(synchronousClassId);
	}

	/**
	 * //[8/24/2010] LMS-6857 :: Get Synchronous Class for Enrollment. Set
	 * newEnrollmentCount to -1 if want to by pass it
	 */
	@Override
	public List<SynchronousClass> getSynchronousClassesForEnrollment(
			Long courseId, int newEnrollmentCount) {

		List<SynchronousClass> synchronousClassList = synchronousClassRepository.findByCourse_Id(courseId);
		for (int index = 0; index < synchronousClassList.size(); index++) {

			SynchronousClass synchronousClass = synchronousClassList.get(index);
			boolean removeItem = false;

			// Validate Enrollment if allowed based on Enrollment Close
			// Date/Last Session Start Date
			Date enrollmentCloseDate = synchronousClass
					.getEnrollmentCloseDate();
			if (enrollmentCloseDate == null) {
				enrollmentCloseDate = this.synchronousSessionRepository
						.getLastSessionStartDate(synchronousClass.getId());
			}

			if (enrollmentCloseDate != null
					&& enrollmentCloseDate.before(new Date())) {
				removeItem = true;
			}

			if (!removeItem) {
				// Validate Class size for Offline Synchronous Courses only.
				if (synchronousClass.getMeetingType() == null) {
					Long remainingSeats = synchronousClass.getMaxClassSize()
							- learnerEnrollmentRepository.getEnrollmentCountBySynchronousClassId(synchronousClass
											.getId());

					if ((remainingSeats <= 0)
							|| ((newEnrollmentCount != -1) && (remainingSeats < newEnrollmentCount))) {
						removeItem = true;
					}
				}
			}

			if (removeItem) {
				synchronousClassList.remove(index);
				index--;

				if (synchronousClassList.isEmpty()) {
					return null;
				}
			}
		}

		return synchronousClassList;
	}

	/**
	 * // [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing
	 * courses irrespective of contract and enrollments availability
	 */
	@Override
	public List<SynchronousClass> getSynchronousClassesForEnrollment(
			Long courseId, int newEnrollmentCount, boolean includeSessions) {

		List<SynchronousClass> synchronousClassList = this
				.getSynchronousClassesForEnrollment(courseId,
						newEnrollmentCount);
		if (synchronousClassList == null) {
			return null;
		}

		if (includeSessions) {
			for (SynchronousClass synClass : synchronousClassList) {
				List<SynchronousSession> synchronousSessions = this.synchronousSessionRepository
						.getSynchronousSessionsByClassId(synClass.getId());
				if (synchronousSessions == null) {
					// Exclude Sync. Class from list if Sessions do not exist
					synchronousClassList.remove(synClass);
					if (synchronousClassList.isEmpty()) {
						return null;
					}
				} else {
					synClass.setSynchronousSessions(synchronousSessions);
				}
			}
		}
		return synchronousClassList;

	}

	public ResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@Override
	public boolean isSyncCourseAssignable(Long syncCourseId) {

		boolean isAssignable = false;
		List<SynchronousClass> syncClasses = this
				.getAllSynchClassesOfCourse(syncCourseId);
		for (SynchronousClass syncClass : syncClasses) {
			isAssignable = this.isSyncClassAssignable(syncClass, -1);
			if (isAssignable) {
				break;
			}
		}

		return isAssignable;
	}

	@Override
	public boolean isSyncClassAssignable(SynchronousClass synchronousClass,
			int newEnrollmentCount) {

		if (synchronousClass == null) {
			return false;
		}

		boolean isAssignable = false;

		// Validate Enrollment if allowed based on Enrollment Close Date/Last
		// Session Start Date
		Date classEnrollmentDate = synchronousClass.getEnrollmentCloseDate();
		if (classEnrollmentDate == null) {
			classEnrollmentDate = this.synchronousSessionRepository
					.getLastSessionStartDate(synchronousClass.getId());
		}

		if (classEnrollmentDate != null
				&& classEnrollmentDate.after(new Date())) {
			isAssignable = true;
		}

		if (isAssignable) {
			// Validate Class size for Offline Synchronous Courses only.
			if (synchronousClass.getMeetingType() == null) {

				isAssignable = false;
				Long remainingSeats = synchronousClass.getMaxClassSize()
						- learnerEnrollmentRepository.getEnrollmentCountBySynchronousClassId(synchronousClass
										.getId());

				if (remainingSeats > 0) {
					isAssignable = true;
					if ((newEnrollmentCount != -1)
							&& (remainingSeats < newEnrollmentCount)) {
						isAssignable = false;
					}
				}
			}
		}
		return isAssignable;
	}
	
	
}