	package com.softech.vu360.lms.web.controller.instructor;


import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import com.softech.vu360.lms.model.DiscussionForumCourse;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.controller.model.MySessionDateTime;
import com.softech.vu360.lms.web.controller.model.MySynchronousSession;
import com.softech.vu360.lms.web.controller.model.instructor.ManageStudentsFallingBehind;
import com.softech.vu360.util.ScheduleSort;
import com.softech.vu360.util.VU360Properties;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * The controller for the instructor synchronous classes display page.
 *
 * @author Noman Ali
 *
 */

public class SynchronousClassesController extends MultiActionController implements
InitializingBean {

	private static final Logger log = Logger.getLogger(SynchronousClassesController.class.getName());
	/* added by Noman for synchronousClasses template */
	private String synchronousClassesTemplate = null;
	private SynchronousClassService synchronousClassService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private EnrollmentService enrollmentService = null;
	private ResourceService resourceService = null;
	String documentPath = VU360Properties.getVU360Property("document.ampie.saveLocation");
	String documentAmColumnPath = VU360Properties.getVU360Property("document.amcolumn.saveLocation");
	
	private static final String VCS_DFC_LAUNCH_URL = VU360Properties.getVU360Property("vcsDFC.launchURL");	
	private static final String DFC_USER_TYPE_INSTRUCTOR = "I";


	@SuppressWarnings("unchecked")
	public ModelAndView displaySynchronousClasses(HttpServletRequest request,
			HttpServletResponse response) {
		log.debug("totalMemory:"+Runtime.getRuntime().totalMemory());
		log.debug("1- displayMyCourses freeMemory:"+Runtime.getRuntime().freeMemory());
		Long instructorId;

		try {
			Map<Object, Object> context = new HashMap<Object, Object>();

			com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
			instructorId = user.getInstructor().getId();
			

			// [5/18/2010] VCS-284 :: Get DiscussionForumCourse List for current Instructor and put it in context along with other information to create launch DFC link
			List<DiscussionForumCourse> dfcCourseList = this.courseAndCourseGroupService.getDFCCourseByInstructorId(instructorId);			
			context.put("launchURL", VCS_DFC_LAUNCH_URL + "?un=" + user.getUsername() + "&pw=" + user.getPassword() + "&ug=" + user.getUserGUID().replace("-", "") + "&ut=" + DFC_USER_TYPE_INSTRUCTOR + "&fg=" );
			context.put("dfcCourseList", dfcCourseList);
			
			
			List<SynchronousClass> synchClassesList = synchronousClassService.getAllSynchronousClasses(instructorId);

			ScheduleSort sort = new ScheduleSort();
			Collections.sort( synchClassesList , sort );

			Map<Long, List<MySessionDateTime>> synchSessionMapByClassId = new HashMap<Long, List<MySessionDateTime>>();

			Map<Long, List<VU360User>> synchClassEnrolledLearnerMap = new HashMap<Long, List<VU360User>>();

			for (SynchronousClass synchClass: synchClassesList ) {
				Long synchClassId = synchClass.getId();
				List<SynchronousSession> synchSessionLst = synchronousClassService.getSynchronousSessionsByClassId(synchClassId);

				List<MySessionDateTime> mySessionDateTime =  getMySynchronousSession(synchSessionLst);
				synchSessionMapByClassId.put(synchClassId, mySessionDateTime); 
				log.debug("SynchronousClass id : " + synchClass.getId());
				log.debug("section name : " + synchClass.getSectionName());

				List<VU360User> enrolledLearnerInfo = enrollmentService.getEnrolledLearnerInfoByClassId(synchClassId.longValue());
				synchClassEnrolledLearnerMap.put(synchClassId, enrolledLearnerInfo);
			}

			for (Iterator iter=synchClassEnrolledLearnerMap.keySet().iterator(); iter.hasNext();) {
				Long classId = (Long) iter.next();
				List<VU360User> listVU360 = synchClassEnrolledLearnerMap.get(classId);
				for (Iterator vuIter=listVU360.iterator(); vuIter.hasNext();) {
					VU360User vu360 = (VU360User) vuIter.next();
					log.debug("vu360user for : " + classId + " firstname :" + vu360.getFirstName());
				}
			}


			context.put("userName", "Noman" + " " + "Ali");
			context.put("mySynchClassList", synchClassesList);
			context.put("mySynchSessionMap", synchSessionMapByClassId);
			context.put("synchClassEnrolledLearnerMap", synchClassEnrolledLearnerMap);

			HashSet<ManageStudentsFallingBehind> studentsFallingBehindList = resourceService.getStudentsFallingBehindByInstructor("", "", "", instructorId);
			context.put("studentsFallingBehindList", studentsFallingBehindList);

			List<LearnerEnrollment> learnerEnrollments=resourceService.findLearnerEnrolmentsByInstructor(instructorId);

			Map<String,Integer> reportData= new HashMap<String,Integer>();
			String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",	"December"};
			
			FileOutputStream fos = new FileOutputStream(documentPath);
			// XERCES 1 or 2 additionnal classes.
			OutputFormat of = new OutputFormat("XML","UTF-8",true);
			of.setIndent(1);
			of.setIndenting(true);
			of.setDoctype(null,"users.dtd");
			XMLSerializer serializer = new XMLSerializer(fos,of);
			// SAX2.0 ContentHandler.
			ContentHandler hd = serializer.asContentHandler();
			hd.startDocument();
			
			// Processing instruction sample.
			//hd.processingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"users.xsl\"");
			// USER attributes.
			AttributesImpl atts = new AttributesImpl();
			// USERS tag.
			hd.startElement("","","pie",atts);
			// USER tags.
			
			
			if ( learnerEnrollments != null ) {
				for( LearnerEnrollment learnerEnrollment : learnerEnrollments ) {
					//Integer month=learnerEnrollment.getEnrollmentDate().getMonth();
					Calendar cal = Calendar.getInstance(); 
					// roll down the month
					cal.setTime(learnerEnrollment.getEnrollmentDate()); 
					String month	=monthName[cal.get(Calendar.MONTH)];
					
					if(!reportData.containsKey(month.toString())){
						reportData.put(month.toString(),1);
					}
					else{
						//Integer month=learnerEnrollment.getEnrollmentDate().getMonth();
						reportData.put(month.toString(), Integer.parseInt(reportData.get(month.toString()).toString()) + 1);
					}										
				}
				Iterator it = reportData.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry)it.next();

					atts.clear();
					atts.addAttribute("","","title","CDATA",pairs.getKey().toString());
					if(pairs.getKey().toString().equals("5"))// Need to change use for test purpose
						atts.addAttribute("","","pull_out","CDATA","true");
					else
						atts.addAttribute("","","pull_out","CDATA","false");
					hd.startElement("","","slice",atts);
					hd.characters(pairs.getValue().toString().toCharArray(),0,pairs.getValue().toString().length());
					hd.endElement("","","slice");
				}
			}
			hd.endElement("","","slice");	
			hd.endDocument();
			fos.close();

			//==================================================================================

			List<LearnerEnrollment> completedEnrollments=resourceService.findCompletedEnrolmentsByInstructor(instructorId);
			SortedMap<String,Integer> courseCompleteData = new TreeMap<String,Integer>();
			Date today = new Date();
			Date newDate = new Date();
			newDate.setMonth(today.getMonth()-1);
			SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy") ; 
			Calendar calendar = Calendar.getInstance(); 
			calendar.setTime(newDate);

			//int index = calendar.get(Calendar.DATE);

			for(int index = 0 ;index<=31;index++ ) {
				courseCompleteData.put(sdf.format(calendar.getTime()), 0);
				calendar.add(Calendar.DATE, +1);
			}
			
			FileOutputStream amColumnFOS = new FileOutputStream(documentAmColumnPath);
			OutputFormat amColumnOF = new OutputFormat("XML","UTF-8",true);
			
			amColumnOF.setIndent(1);
			amColumnOF.setIndenting(true);			
			XMLSerializer amColumnSerializer = new XMLSerializer(amColumnFOS,amColumnOF);
			ContentHandler amColumnCH = amColumnSerializer.asContentHandler();
			amColumnCH.startDocument();
			AttributesImpl amColumnAtts = new AttributesImpl();
			amColumnCH.startElement("","","chart",amColumnAtts);
			amColumnCH.startElement("","","series",amColumnAtts);
			
			Iterator amColumnIt = null;
			int y = 0;
			if ( completedEnrollments != null ) {
				for( LearnerEnrollment learnerEnrollment : completedEnrollments ) {
					Calendar cal = Calendar.getInstance(); 
					cal.setTime(learnerEnrollment.getCourseStatistics().getCompletionDate()); 
					//Integer day = Integer.parseInt(cal.get(Calendar.DATE) + "");
					String date = sdf.format(cal.getTime());
					if(!courseCompleteData.containsKey(date)){
						courseCompleteData.put(date,1);
					} else {
						courseCompleteData.put(date, courseCompleteData.get(date) + 1);
					}
				}				
				
				String[] days = {"1d", "2d", "3d", "4d", "5d", "6d", "1w", "8d", "9d", "10d", "11d", "12d", "13d", "1w", "15d", "16d", "17d"
						, "18d", "19d", "20d", "3w", "22d", "23d", "24d", "25d", "26d", "27d", "4w", "29d", "30d", "1m"};
				amColumnIt = courseCompleteData.entrySet().iterator();

				y = 100 ;
				for (int i=0;i<days.length;i++) {
					amColumnAtts.clear();
					String valId = y + "";
					amColumnAtts.addAttribute("","","xid","CDATA",valId);
					y++;
					amColumnCH.startElement("","","value",amColumnAtts);
					amColumnCH.characters(days[i].toCharArray(),0,days[i].length());
					amColumnCH.endElement("","","value");
				}
			}
			amColumnCH.endElement("","","series");			
			amColumnAtts.clear();
			amColumnCH.startElement("","","graphs",amColumnAtts);
			
			if ( completedEnrollments != null ) {				
				amColumnAtts.addAttribute("","","gid","CDATA","1");
				amColumnCH.startElement("","","graph",amColumnAtts);
				amColumnAtts.clear();
				y = 100 ;
				while (amColumnIt.hasNext()) {
					SortedMap.Entry pairs = (SortedMap.Entry)amColumnIt.next();
					amColumnAtts.clear();
					String valId = y + "";
					amColumnAtts.addAttribute("","","xid","CDATA",valId);
					y++;
					amColumnAtts.addAttribute("","","color","CDATA","#318DBD");
					amColumnCH.startElement("","","value",amColumnAtts);
					amColumnCH.characters(pairs.getValue().toString().toCharArray(),0,pairs.getValue().toString().length());
					amColumnCH.endElement("","","value");
				}
				amColumnCH.endElement("","","graph");
								
			}
			
			amColumnCH.endElement("","","graphs");
			amColumnCH.endElement("","","chart");
			amColumnCH.endDocument();
			amColumnFOS.close();
			
			//==================================================================================
			return new ModelAndView(synchronousClassesTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(synchronousClassesTemplate);
	}

	private List<MySessionDateTime> getMySynchronousSession(List<SynchronousSession> synchSession) {
		MySynchronousSession mySynchSessions = new MySynchronousSession();
		List<MySessionDateTime> mySessionDateTime = mySynchSessions.setupSynchSessions(synchSession);

		return mySessionDateTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	public String getSynchronousClassesTemplate() {
		return synchronousClassesTemplate;
	}

	public void setSynchronousClassesTemplate(String synchronousClassesTemplate) {
		this.synchronousClassesTemplate = synchronousClassesTemplate;
	}

	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}

	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	/**
	 * @return the resourceService
	 */
	public ResourceService getResourceService() {
		return resourceService;
	}

	/**
	 * @param resourceService the resourceService to set
	 */
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

}