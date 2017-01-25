package com.softech.vu360.lms.web.controller.learner;
 
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.exception.NoCertificateNumberFoundException;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.BusinessObjectSequenceService;
import com.softech.vu360.lms.service.CertificateService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.StatisticsService;

public class CourseCompletionCertificateController implements Controller{

	private static final Logger log = Logger.getLogger(CourseCompletionCertificateController.class.getName());
	private EntitlementService entitlementService;
	private String errorTemplate = null;
	private String errorTemplateNoCertNumberfound = null;
	private String notCompletedTemplate = null;
	private String templatePrefix = null;
	private AccreditationService accreditationService = null;
	private StatisticsService statisticsService = null;
	private BusinessObjectSequenceService businessObjectSequenceService = null;
	private CertificateService certificateService= null;
	
	public ModelAndView handleRequest(HttpServletRequest request,HttpServletResponse response) {
		
		com.softech.vu360.lms.vo.VU360User user = null;
		log.debug(user == null ? "User null" : " learnerId="+ user.getLearner().getId());
		
		String learnerEnrollmentIDStr = request.getParameter("learnerEnrollmentId"); //coming from /completionCertificate.pdf
		String learningSessionGUIDStr = request.getParameter("SESSION"); // coming from /printCertificate.do
		
		Map<Object, Object> context = new HashMap<Object, Object>();

		long leanerEnrollmentID = 0;
		boolean isUserValid = false;
		boolean checkUser = false;
		
		if(StringUtils.isBlank(learnerEnrollmentIDStr)){
			if(StringUtils.isNotBlank(learningSessionGUIDStr)){
				LearningSession learningSession = statisticsService.getLearningSessionByLearningSessionId(learningSessionGUIDStr);
				if(learningSession == null){
					context.put("error","lms.learner.mycourses.printCertificate.learningSessionGUID.incorrect" );
					return new ModelAndView(templatePrefix+errorTemplate, "context", context);
				}
				leanerEnrollmentID = learningSession.getEnrollmentId();
			}
		}else{
			leanerEnrollmentID = Long.parseLong(learnerEnrollmentIDStr);
			checkUser = true;
		}
		
//		Date date = new Date(System.currentTimeMillis());
		LearnerEnrollment le = entitlementService.getLearnerEnrollmentById(leanerEnrollmentID);
		
		if(checkUser){ // skip user validation for LCMS (coming from /printCertificate.do)
			user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			isUserValid = le.getLearner().getId().longValue() == user.getLearner().getId().longValue();
		}
		le.setCourseStatistics(getStatisticsService().getLearnerCourseStatisticsById(le.getCourseStatistics().getId()));
		LearnerCourseStatistics lcs=le.getCourseStatistics();
						
		if ( le != null && (!checkUser || isUserValid) ) {
			if ( /*lcs.isCompleted()*/ lcs.isCourseCompleted() || (lcs.getCompleted() && le.isEnableCertificate())) {
				// print certificate
				try {
 					String certURL=lcs.getCertificateURL();
 					if(certURL!=null && certURL.trim().length()>0)
					{
						response.sendRedirect(certURL);
					}
					else
					{   			        	
 			            ByteArrayOutputStream baos = certificateService.generateCertificate(le);
						// setting some response headers
						response.setHeader("Expires", "0");
						response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
						response.setHeader("Pragma", "public");
						// setting the content type
						response.setContentType("application/pdf");
						// the content length is needed for MSIE!!!
						response.setContentLength(baos.size());
						// write ByteArrayOutputStream to the ServletOutputStream
						ServletOutputStream out = response.getOutputStream();
						baos.writeTo(out);
						out.flush();
						out.close();
					}
					return null;
				}
				catch(NoCertificateNumberFoundException ncfe)
				{
					log.error(ncfe.getMessage(), ncfe);
					context.put("error","lms.learner.mycourses.printCertificate.certificateNumberNotAvailable" );
//					return new ModelAndView(templatePrefix+errorTemplateNoCertNumberfound, "context", context);
				}
				catch (Exception ex) {
					log.error(ex.getMessage(), ex);
					//throw new RuntimeException(ex);
					context.put("error","lms.learner.mycourses.printCertificate.certificateError" );
//					return new ModelAndView(templatePrefix+errorTemplate, "context", context);
				}
			}else if( lcs.getCompleted() && !le.isEnableCertificate()){
				context.put("error","lms.learner.mycourses.printCertificate.notallowed" );
			}
			else{
				context.put("error","lms.learner.mycourses.printCertificate.course.notcompleted" );
			}
			// redirect user to page indicating that they have not completed the course
			return new ModelAndView(templatePrefix+errorTemplate, "context", context);
		}
		
		// redirect to error page letting the user know that we could not find any matching records
		// protect against hacking the URL to print certificates that are not theirs.
		// redirect user to page indicating that they have not completed the course
		return new ModelAndView(templatePrefix+errorTemplate, "context", context);
	}
		
	/**
	 * @return the entitlementService
	 */
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}

	/**
	 * @param entitlementService the entitlementService to set
	 */
	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}
	/**
	 * @return the errorTemplate
	 */
	public String getErrorTemplate() {
		return errorTemplate;
	}
	/**
	 * @param errorTemplate the errorTemplate to set
	 */
	public void setErrorTemplate(String errorTemplate) {
		this.errorTemplate = errorTemplate;
	}
	/**
	 * @return the notCompletedTemplate
	 */
	public String getNotCompletedTemplate() {
		return notCompletedTemplate;
	}
	/**
	 * @param notCompletedTemplate the notCompletedTemplate to set
	 */
	public void setNotCompletedTemplate(String notCompletedTemplate) {
		this.notCompletedTemplate = notCompletedTemplate;
	}
	/**
	 * @return the templatePrefix
	 */
	public String getTemplatePrefix() {
		return templatePrefix;
	}
	/**
	 * @param templatePrefix the templatePrefix to set
	 */
	public void setTemplatePrefix(String templatePrefix) {
		this.templatePrefix = templatePrefix;
	}
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	
	/**
	 * @param statisticsService the statisticsService to set
	 * // [10/27/2010] LMS-6389 :: Certificate Number and Issue Date Fields
	 */
	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	/**
	 * @return the statisticsService
	 * // [10/27/2010] LMS-6389 :: Certificate Number and Issue Date Fields
	 */
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param businessObjectSequenceService the businessObjectSequenceService to set
	 * // [10/27/2010] LMS-6389 :: Certificate Number and Issue Date Fields
	 */
	public void setBusinessObjectSequenceService(
			BusinessObjectSequenceService businessObjectSequenceService) {
		this.businessObjectSequenceService = businessObjectSequenceService;
	}

	/**
	 * @return the businessObjectSequenceService
	 * // [10/27/2010] LMS-6389 :: Certificate Number and Issue Date Fields
	 */
	public BusinessObjectSequenceService getBusinessObjectSequenceService() {
		return businessObjectSequenceService;
	}

	public CertificateService getCertificateService() {
		return certificateService;
	}

	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	/**
	 * @return the errorTemplateNoCertNumberfound
	 */
	public String getErrorTemplateNoCertNumberfound() {
		return errorTemplateNoCertNumberfound;
	}

	/**
	 * @param errorTemplateNoCertNumberfound the errorTemplateNoCertNumberfound to set
	 */
	public void setErrorTemplateNoCertNumberfound(
			String errorTemplateNoCertNumberfound) {
		this.errorTemplateNoCertNumberfound = errorTemplateNoCertNumberfound;
	}
	
	
	
}
