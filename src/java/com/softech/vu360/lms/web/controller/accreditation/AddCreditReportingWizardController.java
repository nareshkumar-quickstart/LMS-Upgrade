package com.softech.vu360.lms.web.controller.accreditation;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSmartCopy;
import com.lowagie.text.pdf.PdfStamper;
import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.BusinessObjectSequenceService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.CourseItem;
import com.softech.vu360.lms.web.controller.model.LearnerItemForm;
import com.softech.vu360.lms.web.controller.model.accreditation.CreditReportForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddCreditReportValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.CourseSort;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.UserSort;
import com.softech.vu360.util.VU360Properties;

/**
 * @author Dyutiman
 * created on 23-july-2009
 */
public class AddCreditReportingWizardController extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddCreditReportingWizardController.class.getName());
//	HttpSession session = null;

	private AccreditationService accreditationService;
	private LearnerService learnerService;
	private StatisticsService statService;
	private BusinessObjectSequenceService businessObjectSequenceService;
	
	private static final String BOSEQ_OSHA_CERTIFICATE = "OSHACertificate";

	private String cancelTemplate = null;
	private String finishTemplate = null;

	public AddCreditReportingWizardController() {
		super();
		setCommandName("creditReportForm");
		setCommandClass(CreditReportForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/approvals/CreditReporting/report_creditCourses"
				, "accreditation/approvals/CreditReporting/report_creditTimeframe"
				, "accreditation/approvals/CreditReporting/report_creditSelection"
				, "accreditation/approvals/CreditReporting/report_creditStudent"
				, "accreditation/approvals/CreditReporting/report_creditOptions"
				, "accreditation/approvals/CreditReporting/report_creditSetting"
				, "accreditation/approvals/CreditReporting/report_creditSuccess"});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			// do nothing
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		Map <Object, Object>model = new HashMap <Object, Object>();
		CreditReportForm form = (CreditReportForm) command;

		switch(page){

		case 0:
			break;
		case 1:
			break;
		case 2:
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			List<CourseItem> items = form.getCourses();
			int i = 0;
			for( CourseItem item : items ) {
				if( item.getSelected() )
					i ++;
			}
			Long[] courseIdArray = new Long[i];
			i = 0;
			for( CourseItem item : items ) {
				if( item.getSelected() ) {
					courseIdArray[i] = item.getCourse().getId();
					i ++;
				}	
			}
			List<Learner> searchedLearners = new ArrayList <Learner>();
			searchedLearners = accreditationService.SearchLearnerByCourseCompletion(courseIdArray, formatter.parse(form.getFromDateWithTime()), 
					formatter.parse(form.getToDateWithTime()), "", "", "");
			form.setSelectedLearners(searchedLearners.size());
			model.put("numberOfLearners", searchedLearners.size());
			return model;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			int selectedLearners = 0;
			List<LearnerItemForm> formLearners = form.getLearners();
			for( LearnerItemForm l : formLearners ) {
				if( l.isSelected() )
					selectedLearners ++;
			}
			model.put("learnersSelected", selectedLearners);
			return model;
		default:
			break;
		}
		return super.referenceData(request, page);
	}

	@SuppressWarnings("deprecation")
	protected void postProcessPage(HttpServletRequest request, Object command,
			Errors errors, int page) throws Exception {

		CreditReportForm form = (CreditReportForm)command;
		
		/* @Wajahat
		 * Principal: 03-09-2016
		 * Please don't change the below code of getting VU360User object because it is being used to check contentowners set in this object
		 */
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		Map<String,String> pagerAttributeMap = new HashMap<String,String>();
//		session = request.getSession();
		SimpleDateFormat formatter = null;
		
		if(((page == 0)||(page == 3))&& (!form.getAction().equalsIgnoreCase("search"))){
			request.setAttribute("newPage","true");
		}
		
		if( page == 0 && form.getAction().equalsIgnoreCase("search") ) {

			String courseName = form.getCourseName();
			String courseId = form.getBusinessKey();

			List<Course> courses = accreditationService.findCoursesByRegulatoryAnalyst(courseName, courseId, loggedInUser.getRegulatoryAnalyst());
			
			courseName = HtmlEncoder.escapeHtmlFull(courseName).toString();
			courseId = HtmlEncoder.escapeHtmlFull(courseId).toString();
			
			// for sorting
			String sortColumnIndex = form.getSortColumnIndex();
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = form.getSortDirection();
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			if( sortColumnIndex != null && sortDirection != null ) {

				// sorting against course title
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CourseSort sort = new CourseSort();
						sort.setSortBy("courseTitle");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(courses,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						CourseSort sort = new CourseSort();
						sort.setSortBy("courseTitle");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(courses,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against course id
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CourseSort sort = new CourseSort();
						sort.setSortBy("courseId");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(courses,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						CourseSort sort = new CourseSort();
						sort.setSortBy("courseId");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(courses,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
				} 
			}
			List<CourseItem> items = new ArrayList <CourseItem>();
			for( Course c : courses ) {
				CourseItem item = new CourseItem();
				item.setCourse(c);
				item.setSelected(false);
				items.add(item);
			}
			form.setCourses(items);
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}
		if( page == 1 ) {

			Date startDate = null;
			Date endDate = null;
			int minutes = 0;
			int hours = 0;

			if( !form.getFromDate().isEmpty() ) {
				formatter = new SimpleDateFormat("MM/dd/yyyy");
				startDate = formatter.parse(form.getFromDate());
				if( form.getFromTimeHour().isEmpty() ) {
					//form.setFromTimeHour("00");
					hours = Integer.parseInt("00");
				}
				if( form.getFromTimeMinute().isEmpty() ) {
					//form.setFromTimeMinute("00");
					minutes = Integer.parseInt("00");
				}
				if( form.getFromTimePhase().equalsIgnoreCase("pm") )
					hours = hours + 12;
				startDate.setHours(hours);
				startDate.setMinutes(minutes);
				form.setFromDate(formatter.format(startDate));
				formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
				form.setFromDateWithTime(formatter.format(startDate));
			}
			if( !form.getToDate().isEmpty() ) {
				formatter = new SimpleDateFormat("MM/dd/yyyy");
				endDate = formatter.parse(form.getToDate());
				if( form.getToTimeHour().isEmpty() ) {
					//form.setToTimeHour("11");
					hours = Integer.parseInt("11");
				}
				if( form.getToTimeMinute().isEmpty() ) {
					//form.setToTimeMinute("59");
					minutes = Integer.parseInt("59");
				}
				if( form.getToTimePhase().equalsIgnoreCase("pm") )
					hours = hours + 12;
				endDate.setHours(hours);
				endDate.setMinutes(minutes);
				form.setToDate(formatter.format(endDate));
				formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
				form.setToDateWithTime(formatter.format(endDate));
			}
		}
		if( page == 2 ) {

			// if all learners are to be selected...
			if( form.getSelectAllLearner().equalsIgnoreCase("true") ) {

				formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
				List<CourseItem> items = form.getCourses();
				int i = 0;
				for( CourseItem item : items ) {
					if( item.getSelected() )
						i ++;
				}
				Long[] courseIdArray = new Long[i];
				i = 0;
				for( CourseItem item : items ) {
					if( item.getSelected() ) {
						courseIdArray[i] = item.getCourse().getId();
						i ++;
					}	
				}
				List<Learner> searchedLearners = new ArrayList <Learner>();
				searchedLearners = accreditationService.SearchLearnerByCourseCompletion(courseIdArray, formatter.parse(form.getFromDateWithTime()), 
						formatter.parse(form.getToDateWithTime()), "", "", "");

				List<LearnerItemForm> formLearners = new ArrayList <LearnerItemForm>();
				for( Learner l : searchedLearners ) {
					LearnerItemForm item = new LearnerItemForm();
					item.setUser(l.getVu360User());
					item.setSelected(true);
					formLearners.add(item);
				}
				form.setLearners(formLearners);
			}
		}
		if( page == 3 && form.getAction().equalsIgnoreCase("search") ) {

			formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
			String fname = StringUtils.isBlank(request.getParameter("fname"))? "" : request.getParameter("fname");
			String lname = StringUtils.isBlank(request.getParameter("lname")) ? "" : request.getParameter("lname");
			String studentId = StringUtils.isBlank(request.getParameter("studentId")) ? "" : request.getParameter("studentId");
			List<CourseItem> items = form.getCourses();
			int i = 0;
			for( CourseItem item : items ) {
				if( item.getSelected() )
					i ++;
			}
			Long[] courseIdArray = new Long[i];
			i = 0;
			for( CourseItem item : items ) {
				if( item.getSelected() ) {
					courseIdArray[i] = item.getCourse().getId();
					i ++;
				}	
			}
			List<Learner> searchedLearners = new ArrayList <Learner>();
			searchedLearners = accreditationService.SearchLearnerByCourseCompletion(courseIdArray, formatter.parse(form.getFromDateWithTime()), 
					formatter.parse(form.getToDateWithTime()), fname, lname, studentId);
			
			fname = HtmlEncoder.escapeHtmlFull(fname).toString();
			lname = HtmlEncoder.escapeHtmlFull(lname).toString();
			studentId = HtmlEncoder.escapeHtmlFull(studentId).toString();
			
			// for sorting
			String sortColumnIndex = form.getSortColumnIndex();
//			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = form.getSortDirection();
//			if( sortDirection == null && session.getAttribute("sortDirection") != null )
//				sortDirection = session.getAttribute("sortDirection").toString();
			String pageIndex = form.getPageCurrIndex();
			if( pageIndex == null ) pageIndex = form.getPageIndex();

			if( sortColumnIndex != null && sortDirection != null ) {

				// sorting against first name
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						UserSort sort = new UserSort();
						sort.setSortBy("firstName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedLearners,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 0);
					} else {
						UserSort sort = new UserSort();
						sort.setSortBy("firstName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedLearners,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("0");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against last name
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						UserSort sort = new UserSort();
						sort.setSortBy("lastName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedLearners,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 1);
					} else {
						UserSort sort = new UserSort();
						sort.setSortBy("lastName");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedLearners,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("1");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 1);
					}
					// sorting against e-mail address
				} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						UserSort sort = new UserSort();
						sort.setSortBy("emailAddress");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedLearners,sort);
						form.setSortDirection("0");
						form.setSortColumnIndex("2");
//						session.setAttribute("sortDirection", 0);
//						session.setAttribute("sortColumnIndex", 2);
					} else {
						UserSort sort = new UserSort();
						sort.setSortBy("emailAddress");
						sort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedLearners,sort);
						form.setSortDirection("1");
						form.setSortColumnIndex("2");
//						session.setAttribute("sortDirection", 1);
//						session.setAttribute("sortColumnIndex", 2);
					}
				}
			}			
			List<LearnerItemForm> formLearners = new ArrayList <LearnerItemForm>();
			for( Learner l : searchedLearners ) {
				LearnerItemForm item = new LearnerItemForm();
				item.setUser(l.getVu360User());
				item.setSelected(false);
				formLearners.add(item);
			}
			form.setLearners(formLearners);
			pagerAttributeMap.put("pageIndex", pageIndex);
			pagerAttributeMap.put("showAll", form.getShowAll());
			request.setAttribute("PagerAttributeMap", pagerAttributeMap);
		}
		if( page == 5 ) {

			log.debug("LAST -- "+form.getLast());
			if( form.getLast().equalsIgnoreCase("true")) {
				if( form.getExportCsv().equalsIgnoreCase("true") ) {

					String delimiter = form.getCsvDel();
					formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
					List<CreditReportingField> fields = new ArrayList <CreditReportingField>();
					FileOutputStream csvwriter;
					String documentPath = VU360Properties.getVU360Property("document.saveLocation");
					csvwriter = new FileOutputStream(documentPath + "CreditReport.csv");
					PrintWriter pw = new PrintWriter(csvwriter, true);

					List<CourseItem> items = form.getCourses();
					List<Course> selectedCourses = new ArrayList <Course>();
					for( CourseItem item : items ) {
						if( item.getSelected() )
							selectedCourses.add(item.getCourse());
					}
					List<CreditReportingField> reportfields = new ArrayList <CreditReportingField>();
					// header line - credit reporting field names
					List<String> secondLine = new ArrayList <String>();
					for( Course c : selectedCourses ) {
						fields = new ArrayList<CreditReportingField>(this.accreditationService.getCreditReportingFieldsByCourse(c));
						for( CreditReportingField f : fields ) {
							secondLine.add(f.getFieldLabel());
							reportfields.add(f);
						}
					}
					if( form.getFirstRowHeader().equalsIgnoreCase("yes") ) {
						pw.print("FIRST NAME "+delimiter+"LAST NAME "+delimiter+"USER NAME "+delimiter+
								"STREET ADDRESS1 LINE1 "+delimiter+"STREET ADDRESS1 LINE2 "+delimiter+
								"CITY1 "+delimiter+"STATE1 "+delimiter+"ZIP1 "+delimiter+"COUNTRY1 "+
								delimiter+"PROVINCE1 "+delimiter+"STREET ADDRESS2 LINE1 "+delimiter+
								"STREET ADDRESS2 LINE2 "+delimiter+"CITY2 "+delimiter+"STATE2 "+delimiter+
								"ZIP2 "+delimiter+"COUNTRY2 "+delimiter+"PROVINCE2"+delimiter+" OFFICE-PHONE"+
								delimiter+" PHONE EXTENSION"+delimiter+" MOBILE"+delimiter+"");
						for( String s : secondLine )
							pw.printf("%s"+delimiter+" ", s);
						pw.println();
					}
					// end of header line.

					List<String> line = null;
					for( LearnerItemForm l : form.getLearners() ) {
						if( l.isSelected() ) {
							line = new ArrayList <String>();
							for( int i = 0 ; i < secondLine.size() ; i ++ )
								line.add("");
							Learner learner = l.getUser().getLearner();
							LearnerProfile p = learner.getLearnerProfile();
							// null handling for province -
							String province1 = "";
							String province2 = "";
							if( p.getLearnerAddress().getProvince() != null ) {
								province1 = p.getLearnerAddress().getProvince();
							} 
							if( p.getLearnerAddress2().getProvince() != null ) {
								province2 = p.getLearnerAddress2().getProvince();
							}
							List<CreditReportingFieldValue> values = learnerService.getCreditReportingFieldValues(learner);
							// printing course title & corresponding student name
							pw.printf("%s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" %s"+delimiter+" " +
									"%s"+delimiter+" %s"+delimiter+"", l.getUser().getFirstName(), l.getUser().getLastName(), l.getUser().getUsername()
									, p.getLearnerAddress().getStreetAddress(), p.getLearnerAddress().getStreetAddress2()
									, p.getLearnerAddress().getCity(), p.getLearnerAddress().getState()
									, p.getLearnerAddress().getZipcode(), p.getLearnerAddress().getCountry()
									, province1, p.getLearnerAddress2().getStreetAddress()
									, p.getLearnerAddress2().getStreetAddress2(), p.getLearnerAddress2().getCity()
									, p.getLearnerAddress2().getState(), p.getLearnerAddress2().getZipcode()
									, p.getLearnerAddress2().getCountry(), province2
									, p.getOfficePhone(), p.getOfficePhoneExtn(), p.getMobilePhone());

							for( CreditReportingFieldValue v : values ) {
								for( int i = 0 ; i < secondLine.size() ; i ++ ) {
									if( v.getReportingCustomField().getId().compareTo(reportfields.get(i).getId()) == 0 ) {
										String value = accreditationService.getValueByReportingType(v);
										line.set(i, value);
									}
								}
							}
							for( String s : line )
								pw.printf("%s"+delimiter+" ", s);
							pw.println();
						}
					}
				}
				if( form.getGenCertificate().equalsIgnoreCase("true") ) {

					String documentPath = VU360Properties.getVU360Property("document.saveLocation");
					formatter = new SimpleDateFormat("MM/dd/yyyy");
					List<CourseItem> items = form.getCourses();
					List<Course> selectedCourses = new ArrayList <Course>();
					for( CourseItem item : items ) {
						if( item.getSelected() )
							selectedCourses.add(item.getCourse());
					}
					List<LearnerItemForm> formLearners = form.getLearners();
					List<VU360User> selectedUsers = new ArrayList <VU360User>();
					for( LearnerItemForm l : formLearners ) {
						if( l.isSelected() )
							selectedUsers.add(l.getUser());
					}

					Date d = new Date();
					Document document = new Document();
					PdfSmartCopy copy = new PdfSmartCopy(document, new FileOutputStream(documentPath + "Certificates.pdf"));
					document.open();
					PdfReader reader;
					PdfStamper stamper;
					AcroFields field;
					ByteArrayOutputStream baos;

					try {
						// this loop will work Course vice
						for( Course c : selectedCourses ) {

							List<CreditReportingField> reportfields = new ArrayList <CreditReportingField>();
							reportfields = new ArrayList<CreditReportingField>(this.accreditationService.getCreditReportingFieldsByCourse(c));
							CourseApproval app = accreditationService.getCourseApprovalByCourse(c);
							int userNumber = 0;
							if( app != null ) {
								Certificate cert = app.getCertificate();
								if( cert != null ) {

									// this loop will work PAGE vice
									for( int pageNumber = 0 ; pageNumber < selectedUsers.size()/cert.getNoOfCertificatePerPage() ; pageNumber ++ ) {

										// this will work LEARNER vice -
										for( int j = 0 ; j < cert.getNoOfCertificatePerPage() ; j ++ ) {
											if( j < selectedUsers.size() ) {
												VU360User user = selectedUsers.get(userNumber);
												LearnerProfile p = user.getLearner().getLearnerProfile();
												List<CreditReportingFieldValue> values = learnerService.getCreditReportingFieldValues(user.getLearner());

												reader = new PdfReader(documentPath + cert.getFileName());
												baos = new ByteArrayOutputStream();
												stamper = new PdfStamper(reader, baos);
												field = stamper.getAcroFields();
												/*
												 * Acro-field's names are decided as per convention
												 *  depending upon number of certificates per page :-
												 */
												if( cert.getNoOfCertificatePerPage() == 1 ) {
													// setting the fields... 
													field.setField("name", user.getName());
													field.setField("course_name", c.getCourseTitle());
													field.setField("comp_date", formatter.format(d));
													field.setField("email_address", user.getEmailAddress());
													field.setField("username", user.getUsername());
													// address1
													field.setField("learnerAddress_streetAddress", p.getLearnerAddress().getStreetAddress());
													field.setField("learnerAddress_streetAddress2", p.getLearnerAddress().getStreetAddress2());
													field.setField("learnerAddress_streetAddress3", p.getLearnerAddress().getStreetAddress3());
													field.setField("learnerAddress_city", p.getLearnerAddress().getCity());
													field.setField("learnerAddress_state", p.getLearnerAddress().getState());
													field.setField("learnerAddress_zipcode", p.getLearnerAddress().getZipcode());
													field.setField("learnerAddress_country", p.getLearnerAddress().getCountry());
													field.setField("learnerAddress_province", p.getLearnerAddress().getProvince());
													// address2
													field.setField("learnerAddress2_streetAddress", p.getLearnerAddress2().getStreetAddress());
													field.setField("learnerAddress2_streetAddress2", p.getLearnerAddress2().getStreetAddress2());
													field.setField("learnerAddress2_streetAddress3", p.getLearnerAddress2().getStreetAddress3());
													field.setField("learnerAddress2_city", p.getLearnerAddress2().getCity());
													field.setField("learnerAddress2_state", p.getLearnerAddress2().getState());
													field.setField("learnerAddress2_zipcode", p.getLearnerAddress2().getZipcode());
													field.setField("learnerAddress2_country", p.getLearnerAddress2().getCountry());
													field.setField("learnerAddress2_province", p.getLearnerAddress2().getProvince());
													// profile details -
													field.setField("officePhone", p.getOfficePhone());
													field.setField("officePhoneExtn", p.getOfficePhoneExtn());
													field.setField("mobilePhone", p.getMobilePhone());
												} else {
													// setting the fields... 
													field.setField("name_"+j, user.getName());
													field.setField("course_name_"+j, c.getCourseTitle());
													field.setField("comp_date_"+j, formatter.format(d));
													field.setField("email_address_"+j, user.getEmailAddress());
													field.setField("username_"+j, user.getUsername());													
													// address1
													field.setField("learnerAddress_streetAddress_"+j, p.getLearnerAddress().getStreetAddress());
													field.setField("learnerAddress_streetAddress2_"+j, p.getLearnerAddress().getStreetAddress2());
													field.setField("learnerAddress_streetAddress3_"+j, p.getLearnerAddress().getStreetAddress3());
													field.setField("learnerAddress_city_"+j, p.getLearnerAddress().getCity());
													field.setField("learnerAddress_state_"+j, p.getLearnerAddress().getState());
													field.setField("learnerAddress_zipcode_"+j, p.getLearnerAddress().getZipcode());
													field.setField("learnerAddress_country_"+j, p.getLearnerAddress().getCountry());
													field.setField("learnerAddress_province_"+j, p.getLearnerAddress().getProvince());
													// address2
													field.setField("learnerAddress2_streetAddress_"+j, p.getLearnerAddress2().getStreetAddress());
													field.setField("learnerAddress2_streetAddress2_"+j, p.getLearnerAddress2().getStreetAddress2());
													field.setField("learnerAddress2_streetAddress3_"+j, p.getLearnerAddress2().getStreetAddress3());
													field.setField("learnerAddress2_city_"+j, p.getLearnerAddress2().getCity());
													field.setField("learnerAddress2_state_"+j, p.getLearnerAddress2().getState());
													field.setField("learnerAddress2_zipcode_"+j, p.getLearnerAddress2().getZipcode());
													field.setField("learnerAddress2_country_"+j, p.getLearnerAddress2().getCountry());
													field.setField("learnerAddress2_province_"+j, p.getLearnerAddress2().getProvince());
													// profile details -
													field.setField("officePhone_"+j, p.getOfficePhone());
													field.setField("officePhoneExtn_"+j, p.getOfficePhoneExtn());
													field.setField("mobilePhone_"+j, p.getMobilePhone());
												}
												// custom fields
												for( CreditReportingFieldValue v : values ) {
													for( int i = 0 ; i < reportfields.size() ; i ++ ) {
														if( v.getReportingCustomField().getId().compareTo(reportfields.get(i).getId()) == 0 ) {
															String value = accreditationService.getValueByReportingType(v);
															String tag = reportfields.get(i).getFieldLabel().replaceAll(" ", "");
															field.setField(tag, value);
														}
													}
												}
												stamper.setFormFlattening(true);
												stamper.close();

												reader = new PdfReader(baos.toByteArray());
												copy.addPage(copy.getImportedPage(reader, 1));
											}
											userNumber ++;
										}
									}
								}
							}
						}
						document.close();
					} catch( Exception e ) {
						log.debug("COUGHT EXCEPTION - "+e);
					}
				}
				if( form.getMarkComplete().equalsIgnoreCase("true") ) {

					List<CourseItem> items = form.getCourses();
					List<Course> selectedCourses = new ArrayList <Course>();
					for( CourseItem item : items ) {
						if( item.getSelected() )
							selectedCourses.add(item.getCourse());
					}
					List<LearnerItemForm> formLearners = form.getLearners();
					List<VU360User> selectedUsers = new ArrayList <VU360User>();
					for( LearnerItemForm l : formLearners ) {
						if( l.isSelected() )
							selectedUsers.add(l.getUser());
					}
					for( Course c : selectedCourses ) {
						for( VU360User u : selectedUsers ) {
							LearnerCourseStatistics stat = statService.getLearnerCourseStatistics(u.getLearner(), c);
							if( stat != null ) {
								stat.setReported(true);
								stat.setReportedDate(new Date());
								
								// [10/27/2010] LMS-6389 :: Certificate Number and Issue Date Fields
								String certificateNumber = this.businessObjectSequenceService.getNextBusinessObjectSequence( BOSEQ_OSHA_CERTIFICATE );
								if ( StringUtils.isNotBlank(certificateNumber) ) {
									//certificateNumber = "N/A";
									stat.setCertificateNumber( certificateNumber );
									stat.setCertificateIssueDate( new Date() );
								}							
																
								statService.saveLearnerCourseStatistics(stat);
							}
						}
					}
				}
			}
		}
	}

	protected int getTargetPage(HttpServletRequest request, Object command,
			Errors errors, int currentPage) {
		CreditReportForm form = (CreditReportForm) command;
		if( currentPage == 2 && this.getTargetPage(request, currentPage) != 1 ) {
			if( form.getSelectAllLearner().equalsIgnoreCase("true") ) {
				return 4;
			} else {
				return 3;
			}
		}
		return super.getTargetPage(request, command, errors, currentPage);
	}

	protected void validatePage( Object command, Errors errors, int page, boolean finish ) {

		AddCreditReportValidator validator = (AddCreditReportValidator)this.getValidator();
		CreditReportForm form = (CreditReportForm)command;
		log.debug("Page num IN VALIDATOR --- "+page);
		// validation will not be done for process finish
		if( !finish ) {
			switch(page) {

			case 0:
				if( !form.getAction().equalsIgnoreCase("search") )
					validator.validateCoursePage(form, errors);
				break;
			case 1:
				validator.validateTimeFramePage(form, errors);
				break;
			case 2:
				validator.validateSelectionPage(form, errors);
				break;
			case 3:
				if( !form.getAction().equalsIgnoreCase("search") )
					validator.validateStudentPage(form, errors);
				break;
			case 4:
				validator.validateOptionPage(form, errors);
				break;
			case 5:
				break;
			case 6:
				break;
			default:
				break;
			}
		}
		super.validatePage(command, errors, page, finish);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		// getting out of the wizard -
		return new ModelAndView(finishTemplate);
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors) throws Exception {
		log.debug("IN processCancel");
		return new ModelAndView(cancelTemplate);
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public StatisticsService getStatService() {
		return statService;
	}

	public void setStatService(StatisticsService statService) {
		this.statService = statService;
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

}