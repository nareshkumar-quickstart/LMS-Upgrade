/**
 * 
 */
package com.softech.vu360.lms.web.controller.proctor;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Proctor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CertificateService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.ProctorService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.ProctorCertificateCompletionForm;
import com.softech.vu360.util.ProctorCertificateCompletionSort;
import com.softech.vu360.util.VU360Properties;

/**
 * @author syed.mahmood
 *
 */
public class ProctorCertificateCompletionController extends VU360BaseMultiActionController{
	
	private static final Logger log = Logger.getLogger(ProctorCertificateCompletionController.class.getName());
	private String redirectTemplate;
	private String searchCompletionCertificateTemplate;
	private String errorTemplate;
	private String templatePrefix;
	private EntitlementService entitlementService;
	private AccreditationService accreditationService;
	private StatisticsService statisticsService;
	private CertificateService certificateService;
	private ProctorService proctorService;
	
	private HttpSession session = null;

	public ProctorCertificateCompletionController() {
		super();
	}

	public ProctorCertificateCompletionController(Object delegate) {
		super(delegate);
	}

	protected void onBind(HttpServletRequest request, Object command, String methodName) throws Exception {

	}

	protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response) throws Exception {

		Map<Object,Object> context = new HashMap<Object,Object>();
		context.put("target", "displayMagAlert");

		return new ModelAndView(searchCompletionCertificateTemplate,"context",context);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		
			
		// TODO Auto-generated method stub
	}

	public ModelAndView displayProctorLearners( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors )
	throws Exception {

		session = request.getSession(true);
		Map<Object, Object> context = new HashMap<Object, Object>();
		List<LearnerEnrollment> proctorEnrollmentList = null;
		ProctorCertificateCompletionForm form = (ProctorCertificateCompletionForm)command;
		
		try{
			//get LoggedIn User from session
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			//Date handling
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date startDate = null;
			Date endDate = null;
			
			if(!StringUtils.isEmpty(form.getStartDate())) 
				startDate = formatter.parse(form.getStartDate());
			
			if(!StringUtils.isEmpty(form.getEndDate())) 
				endDate = formatter.parse(form.getEndDate());
			
			//Get proctor
			Proctor proctor = proctorService.getProctorByUserId(loggedInUser.getId());
			if(proctor != null){
				//Getting records 
				proctorEnrollmentList = proctorService.getLearnersByProctor(proctor, form.getFirstName(),form.getLastName(),form.getEmailAddress(),startDate,endDate, form.getCourseTitle(), new String[]{LearnerCourseStatistics.COMPLETED, LearnerCourseStatistics.REPORTED});
				if(proctorEnrollmentList != null && proctorEnrollmentList.size() > 0){
					//============================For Sorting============================//
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					String sortColumnIndex = request.getParameter("sortColumnIndex");
					if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
						sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
					String sortDirection = request.getParameter("sortDirection");
					if( sortDirection == null && session.getAttribute("sortDirection") != null )
						sortDirection = session.getAttribute("sortDirection").toString();
					String pageIndex = request.getParameter("pageCurrIndex");
					if( pageIndex == null ) {
						if(session.getAttribute("pageCurrIndex")==null)pageIndex="0";
						else pageIndex = session.getAttribute("pageCurrIndex").toString();
					}
		
					if( sortColumnIndex != null && sortDirection != null ) {
		
						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								ProctorCertificateCompletionSort sort = new ProctorCertificateCompletionSort();
								sort.setSortBy("firstName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(proctorEnrollmentList,sort);
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 0);
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 0);
							} else {
								ProctorCertificateCompletionSort sort = new ProctorCertificateCompletionSort();
								sort.setSortBy("firstName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(proctorEnrollmentList,sort);
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 0);
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 0);
							}
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								ProctorCertificateCompletionSort sort = new ProctorCertificateCompletionSort();
								sort.setSortBy("lastName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(proctorEnrollmentList,sort);
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 1);
							} else {
								ProctorCertificateCompletionSort sort = new ProctorCertificateCompletionSort();
								sort.setSortBy("lastName");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(proctorEnrollmentList,sort);
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 1);
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 1);
							}
						} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								ProctorCertificateCompletionSort sort = new ProctorCertificateCompletionSort();
								sort.setSortBy("emailAddress");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(proctorEnrollmentList,sort);
								session.setAttribute("sortDirection", 0);
								session.setAttribute("sortColumnIndex", 1);
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 1);
							} else {
								ProctorCertificateCompletionSort sort = new ProctorCertificateCompletionSort();
								sort.setSortBy("emailAddress");
								sort.setSortDirection(Integer.parseInt(sortDirection));
								Collections.sort(proctorEnrollmentList,sort);
								session.setAttribute("sortDirection", 1);
								session.setAttribute("sortColumnIndex", 2);
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 2);
							}
						} else if( sortColumnIndex.equalsIgnoreCase("3") ) {
							ProctorCertificateCompletionSort sort = new ProctorCertificateCompletionSort();
							Integer sortDirInt = 0; 
							try {
								sortDirInt = Integer.parseInt(sortDirection);// .parseInt();
							} catch (Exception e) {
								log.error("Error parsing sort direction ");
							}
							sort.setSortBy("courseTitle");
							sort.setSortDirection(sortDirInt);
							Collections.sort(proctorEnrollmentList,sort);
							session.setAttribute("sortDirection", sortDirInt);
							session.setAttribute("sortColumnIndex", 3);
							context.put("sortDirection", sortDirInt);
							context.put("sortColumnIndex", 3);
						} else if( sortColumnIndex.equalsIgnoreCase("4") ) {
								ProctorCertificateCompletionSort sort = new ProctorCertificateCompletionSort();
								Integer sortDirInt = 0; 
								try {
									sortDirInt = Integer.parseInt(sortDirection);// .parseInt();
								} catch (Exception e) {
									log.error("Error parsing sort direction ");
								}
								sort.setSortBy("completionDate");
								sort.setSortDirection(sortDirInt);
								Collections.sort(proctorEnrollmentList,sort);
								session.setAttribute("sortDirection", sortDirInt);
								session.setAttribute("sortColumnIndex", 4);
								context.put("sortDirection", sortDirInt);
								context.put("sortColumnIndex", 4);
						}
					}
					
					
					context.put("proctorEnrollmentList", proctorEnrollmentList);
					context.put("sortDirection", sortDirection);
					context.put("sortColumnIndex", sortColumnIndex);
					context.put("showAll", request.getParameter("showAll"));
		//			session.setAttribute("pageCurrIndex", pageIndex);
					pagerAttributeMap.put("showAll", request.getParameter("showAll"));
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
					form.setLearnerEnrollmentList(proctorEnrollmentList);
					return new ModelAndView(searchCompletionCertificateTemplate , "context" , context);
				}
			}
		}
		catch(Exception ex){
			log.debug(" EXCEPTION  "+ex.getMessage());
		}
		form.setLearnerEnrollmentList(new ArrayList<LearnerEnrollment>());
		return new ModelAndView(searchCompletionCertificateTemplate);
	}

	public ModelAndView printCompletionCertificates(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		List<LearnerEnrollment> learnerEnrollmentList = null;
		Long[] proctorEnrollmentIds = null;
		ByteArrayOutputStream baos = null;
		PdfReader reader = null;
		int contentLenght=0;
		String password=VU360Properties.getVU360Property("lms.server.certificate.SecurityKey");
		try{
			//Get all selected Ids from request
			String[] selectedProctorEnrollmentIds = request.getParameterValues("proctorEnrollmentId");
			if(selectedProctorEnrollmentIds != null && selectedProctorEnrollmentIds.length > 0)
			 {
				//Convert string array to long array to be passed to DAO layer
				proctorEnrollmentIds = new Long[selectedProctorEnrollmentIds.length];
				 for(int i =0; i < selectedProctorEnrollmentIds.length;i++)
				 {
					 proctorEnrollmentIds[i] = Long.valueOf(selectedProctorEnrollmentIds[i]);
				 }
			 
				//Get List of all selected Objects
				 learnerEnrollmentList = proctorService.getProctorEnrollmentsByIds(proctorEnrollmentIds);
				if(learnerEnrollmentList != null && learnerEnrollmentList.size() > 0)
				{
					//Write ByteArrayOutputStream to the ServletOutputStream
					ServletOutputStream out = response.getOutputStream();
					PdfCopyFields copy = new PdfCopyFields(out);
					copy.setEncryption(null, password.getBytes(), PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
					for(LearnerEnrollment learnerEnrollment : learnerEnrollmentList){
						baos = certificateService.generateCertificate(learnerEnrollment);
						if(baos != null)
						{
							contentLenght = contentLenght + baos.size();
							reader = new PdfReader(baos.toByteArray(), password.getBytes());
							copy.addDocument(reader);
							reader.close();
						}
					}
					copy.close();
					response.setHeader("Expires", "0");
					response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
					response.setHeader("Pragma", "public");
					// setting the content type
					response.setContentType("application/pdf");
					// the content length is needed for MSIE!!!
					response.setContentLength(contentLenght);                                                                                              
					out.flush();
					out.close();
				}
			 }
			 return null;
		}
		catch (Exception ex) 
		{
				log.error(ex.getMessage(), ex);
				//throw new RuntimeException(ex);
				Map<Object, Object> context = new HashMap<Object, Object>();
				context.put("error","lms.learner.mycourses.printCertificate.certificateError" );
				//return new ModelAndView(templatePrefix+errorTemplate, "context", context);
		}
		Map<Object, Object> context = new HashMap<Object, Object>();
		return new ModelAndView(templatePrefix+errorTemplate,"context",context);
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	/**
	 * @return the searchCompletionCertificateTemplate
	 */
	public String getSearchCompletionCertificateTemplate() {
		return searchCompletionCertificateTemplate;
	}

	/**
	 * @param searchCompletionCertificateTemplate the searchCompletionCertificateTemplate to set
	 */
	public void setSearchCompletionCertificateTemplate(
			String searchCompletionCertificateTemplate) {
		this.searchCompletionCertificateTemplate = searchCompletionCertificateTemplate;
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
	 * @return the accreditationService
	 */
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	/**
	 * @param accreditationService the accreditationService to set
	 */
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	/**
	 * @return the statisticsService
	 */
	public StatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param statisticsService the statisticsService to set
	 */
	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	/**
	 * @return the certificateService
	 */
	public CertificateService getCertificateService() {
		return certificateService;
	}

	/**
	 * @param certificateService the certificateService to set
	 */
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	/**
	 * @return the proctorService
	 */
	public ProctorService getProctorService() {
		return proctorService;
	}

	/**
	 * @param proctorService the proctorService to set
	 */
	public void setProctorService(ProctorService proctorService) {
		this.proctorService = proctorService;
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
	
	

}
