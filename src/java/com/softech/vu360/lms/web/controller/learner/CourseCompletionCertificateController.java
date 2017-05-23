package com.softech.vu360.lms.web.controller.learner;
 
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.softech.vu360.lms.exception.NoCertificateNumberFoundException;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.PurchaseCertificateNumber;
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
		
		com.softech.vu360.lms.vo.VU360User user;
		String qsEnrollmentId;
		String qsLearningSessionGUID;
		Map<Object, Object> context;
		long enrollmentId;
		boolean isUserValid;
		boolean checkUser;
		boolean error;
		String certicationURL;
		boolean isCertificateAlreadyExists;
		Boolean hasToGenerateCertificate;
		Long courseApprovalId;

		LearnerEnrollment le;
		LearnerCourseStatistics lcs;
		CourseApproval courseApproval;

		ModelAndView view;

		le = null;
		lcs = null;
		courseApproval = null;

		user = null;
		enrollmentId = 0;

		isUserValid = Boolean.FALSE;
		checkUser = Boolean.FALSE;
		error = Boolean.FALSE;
		isCertificateAlreadyExists = Boolean.FALSE;
		hasToGenerateCertificate = Boolean.FALSE;

		certicationURL = "";

		view = null;

		final String QUERYSTRING_LEARNERENROLLMENTID = "learnerEnrollmentId";
		final String QUERYSTRING_LEARNINGSESSIONGUID = "SESSION";

		qsEnrollmentId = request.getParameter(QUERYSTRING_LEARNERENROLLMENTID) == null ? ""
				: request.getParameter(QUERYSTRING_LEARNERENROLLMENTID); // coming
																			// from
																			// /completionCertificate.pdf
		qsLearningSessionGUID = request.getParameter(QUERYSTRING_LEARNINGSESSIONGUID) == null ? ""
				: request.getParameter(QUERYSTRING_LEARNINGSESSIONGUID); // coming
																			// from
																			// /printCertificate.do

		context = new HashMap<Object, Object>();

		log.debug(user == null ? "User null" : " learnerId=" + user.getLearner().getId());

		if (qsEnrollmentId.trim().isEmpty()) {
			if (qsLearningSessionGUID.trim().isEmpty()) {
				LearningSession learningSession = statisticsService
						.getLearningSessionByLearningSessionId(qsLearningSessionGUID);
				if (learningSession == null) {
					context.put("error", "lms.learner.mycourses.printCertificate.learningSessionGUID.incorrect");
					error = Boolean.TRUE;
				}
				enrollmentId = learningSession.getEnrollmentId();
			}
		} else {
			enrollmentId = Long.parseLong(qsEnrollmentId);
			checkUser = Boolean.TRUE;
		}

		le = entitlementService.getLearnerEnrollmentById(enrollmentId);

		if (checkUser) { // skip user validation for LCMS (coming from
							// /printCertificate.do)
			user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			isUserValid = le.getLearner().getId().longValue() == user.getLearner().getId().longValue();
		}
		le.setCourseStatistics(getStatisticsService().getLearnerCourseStatisticsById(le.getCourseStatistics().getId()));
		lcs = le.getCourseStatistics();

		if (le != null && (!checkUser || isUserValid)) {
			if ( /* lcs.isCompleted() */ lcs.isCourseCompleted() || (lcs.getCompleted() && le.isEnableCertificate())) {
				// print certificate
				try {
					certicationURL = lcs.getCertificateURL();
					if (certicationURL != null && certicationURL.trim().length() > 0) {
						isCertificateAlreadyExists = Boolean.TRUE;
					} else {
						if (StringUtils.isBlank(lcs.getCertificateNumber()) && !le.getId().equals(0)) {
							courseApprovalId = statisticsService
									.getLearnerSelectedCourseApprovalByEnrollmentId(le.getId());

							courseApproval = null;

							if (courseApprovalId != null && courseApprovalId.longValue() > 0) {
								courseApproval = accreditationService.loadForUpdateCourseApproval(courseApprovalId);
							}

							if (courseApproval != null && courseApproval.getUsePurchasedCertificateNumbers()) {
								PurchaseCertificateNumber pcn = accreditationService
										.getUnusedPurchaseCertificateNumber(courseApproval.getId());
								if (pcn == null)
									throw new NoCertificateNumberFoundException("No Certificate numbers available");
							}
						}
						hasToGenerateCertificate = Boolean.TRUE;
					}
				} catch (NoCertificateNumberFoundException ncfe) {
					log.error(ncfe.getMessage(), ncfe);
					context.put("error", "lms.learner.mycourses.printCertificate.certificateNumberNotAvailable");
					error = Boolean.TRUE;
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
					context.put("error", "lms.learner.mycourses.printCertificate.certificateError");
					error = Boolean.TRUE;
				}
			} else if (lcs.getCompleted() && !le.isEnableCertificate()) {
				context.put("error", "lms.learner.mycourses.printCertificate.notallowed");
				error = Boolean.TRUE;
			} else {
				context.put("error", "lms.learner.mycourses.printCertificate.course.notcompleted");
				error = Boolean.TRUE;
			}
		}

		// redirect to error page letting the user know that we could not find
		// any matching records
		// protect against hacking the URL to print certificates that are not
		// theirs.
		// redirect user to page indicating that they have not completed the
		// course

		if (error) {
			view = new ModelAndView(templatePrefix + errorTemplate, "context", context);
		} else if (isCertificateAlreadyExists && !certicationURL.trim().isEmpty()) {
			view = new ModelAndView("redirect:" + certicationURL);
		} else if (hasToGenerateCertificate) {
			view = new ModelAndView("redirect:/rest/certificate/"+ le.getId() +"/"+ le.getCourse().getName().toLowerCase().replaceAll("[^\\w]", "_") +".pdf");
		}

		return view;
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
