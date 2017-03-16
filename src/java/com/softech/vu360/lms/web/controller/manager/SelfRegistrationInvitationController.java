package com.softech.vu360.lms.web.controller.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.RegistrationInvitation;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.model.ManageOrganizationalGroups;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.RedirectUtils;
import com.softech.vu360.util.SendMailService;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Saptarshi
 *
 */
public class SelfRegistrationInvitationController extends MultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(SelfRegistrationInvitationController.class.getName());
	private LearnerService learnerService = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService = null;
	private VU360UserService vu360UserService;
	
	private JavaMailSenderImpl mailSender;
	private SimpleMailMessage templateMessage;

	private static final String MANAGE_USER_SEARCH_ACTION = "search";
	private static final String MANAGE_USER_DELETE_LEARNER_ACTION = "delete";

	private String registrationInvitationTemplate = null;
	private String addNewInvitationTemplate = null;
	private String invitationMessageTemplate = null;
	private String reviewTemplate = null;
	private String saveRegInvitationTemplate = null;
	private String regInvitationPreviewTemplate = null;
	private String redirectTemplate =null;
	
	HttpSession session = null;
	private VelocityEngine velocityEngine;
	
	/**
	 * @author Saptarshi
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView registrationInvitation(HttpServletRequest request,HttpServletResponse response) {
		try {
			session = request.getSession();
			String sortDirection = request.getParameter("sortDirection");
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			String pageIndex = request.getParameter("pageCurrIndex");
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";

			if(pageIndex==null){
				pageIndex="0";
			}
				
			log.debug("pageIndex = " + pageIndex);
			Map PagerAttributeMap = new HashMap();
			PagerAttributeMap.put("pageIndex",pageIndex);
			VU360User user = VU360UserAuthenticationDetails.getCurrentUser();

			String action = request.getParameter("action");

			String invitationName = request.getParameter("invitationName");
			invitationName = (invitationName == null)? "" : invitationName;
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("invitationName", invitationName);
			context.put("showAll", showAll);
			List<RegistrationInvitation> listOfInvitations = null;
			Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer();
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			if ( StringUtils.isNotBlank(action) ) {
				if( action.equalsIgnoreCase(MANAGE_USER_SEARCH_ACTION) ) {
					if( !vu360UserService.hasAdministratorRole(user) &&  !user.getTrainingAdministrator().isManagesAllOrganizationalGroups()) {
						if (user.getTrainingAdministrator().getManagedGroups().size()==0 ) {
							return new ModelAndView(redirectTemplate);
						}
					}
					listOfInvitations = learnerService.getRegistrationInvitationByName(customer, invitationName,user);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);

					/**
					 *  added by Dyutiman for sorting purpose
					 */
					if( StringUtils.isNotBlank(sortDirection) && StringUtils.isNotBlank(sortColumnIndex)) {
						// sorting against invitation name
						
						if( sortColumnIndex.equalsIgnoreCase("0") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < listOfInvitations.size() ; i ++ ) {
									for( int j = 0 ; j < listOfInvitations.size() ; j ++ ) {
										if( i != j ) {
											RegistrationInvitation r1 =  listOfInvitations.get(i);
											RegistrationInvitation r2 =  listOfInvitations.get(j);
											if( r1.getInvitationName().trim().toUpperCase().compareTo(r2.getInvitationName().trim().toUpperCase()) > 0 ) {
												RegistrationInvitation tempMap = listOfInvitations.get(i);
												listOfInvitations.set(i, listOfInvitations.get(j));
												listOfInvitations.set(j, tempMap);
											}
										}
									}
								}
								context.put("showAll", showAll);
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 0);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 0);
							} else {
								for( int i = 0 ; i < listOfInvitations.size() ; i ++ ) {
									for( int j = 0 ; j < listOfInvitations.size() ; j ++ ) {
										if( i != j ) {
											RegistrationInvitation r1 =  listOfInvitations.get(i);
											RegistrationInvitation r2 =  listOfInvitations.get(j);
											if( r1.getInvitationName().toUpperCase().compareTo(r2.getInvitationName().toUpperCase()) < 0 ) {
												RegistrationInvitation tempMap = listOfInvitations.get(i);
												listOfInvitations.set(i, listOfInvitations.get(j));
												listOfInvitations.set(j, tempMap);
											}
										}
									}
								}
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 0);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 0);
							}
							// sorting against times used
						} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < listOfInvitations.size() ; i ++ ) {
									for( int j = 0 ; j < listOfInvitations.size() ; j ++ ) {
										if( i != j ) {
											int count1 =  listOfInvitations.get(i).getRegistrationUtilized();
											int count2 =  listOfInvitations.get(j).getRegistrationUtilized();
											if( count1 > count2 ) {
												RegistrationInvitation tempMap = listOfInvitations.get(i);
												listOfInvitations.set(i, listOfInvitations.get(j));
												listOfInvitations.set(j, tempMap);
											}
										}
									}
								}
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 1);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 1);
							} else {
								for( int i = 0 ; i < listOfInvitations.size() ; i ++ ) {
									for( int j = 0 ; j < listOfInvitations.size() ; j ++ ) {
										if( i != j ) {
											int count1 =  listOfInvitations.get(i).getRegistrationUtilized();
											int count2 =  listOfInvitations.get(j).getRegistrationUtilized();
											if( count1 < count2 ) {
												RegistrationInvitation tempMap = listOfInvitations.get(i);
												listOfInvitations.set(i, listOfInvitations.get(j));
												listOfInvitations.set(j, tempMap);
											}
										}
									}
								}
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 1);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 1);
							}
							// sorting against maximum uses	
						} else if( sortColumnIndex.equalsIgnoreCase("2") ) {
							if( sortDirection.equalsIgnoreCase("0") ) {
								for( int i = 0 ; i < listOfInvitations.size() ; i ++ ) {
									for( int j = 0 ; j < listOfInvitations.size() ; j ++ ) {
										if( i != j ) {
											RegistrationInvitation r1 =  listOfInvitations.get(i);
											RegistrationInvitation r2 =  listOfInvitations.get(j);
											if( r1.getMaximumRegistration() < r2.getMaximumRegistration() ) {
												RegistrationInvitation tempMap = listOfInvitations.get(i);
												listOfInvitations.set(i, listOfInvitations.get(j));
												listOfInvitations.set(j, tempMap);
											}
										}
									}
								}
								// if invitation has unlimited uses
								/*for( int i = 0 ; i < listOfInvitations.size() ; i ++ ) {
									if( listOfInvitations.get(i).hasUnutilizedInvitation() ) {
										RegistrationInvitation tempInvitation = listOfInvitations.get(i);
										listOfInvitations.remove(i);
										listOfInvitations.add(tempInvitation);
									}
								}*/
								context.put("sortDirection", 0);
								context.put("sortColumnIndex", 2);
//								session.setAttribute("sortDirection", 0);
//								session.setAttribute("sortColumnIndex", 2);
							} else {
								for( int i = 0 ; i < listOfInvitations.size() ; i ++ ) {
									for( int j = 0 ; j < listOfInvitations.size() ; j ++ ) {
										if( i != j ) {
											RegistrationInvitation r1 =  listOfInvitations.get(i);
											RegistrationInvitation r2 =  listOfInvitations.get(j);
											if( r1.getMaximumRegistration() > r2.getMaximumRegistration() ) {
												RegistrationInvitation tempMap = listOfInvitations.get(i);
												listOfInvitations.set(i, listOfInvitations.get(j));
												listOfInvitations.set(j, tempMap);
											}
										}
									}
								}
								// if any invitation has unlimited uses
								/*for( int i = 0 ; i < listOfInvitations.size() ; i ++ ) {
									if( listOfInvitations.get(i).hasUnutilizedInvitation() ) {
										RegistrationInvitation tempInvitation = listOfInvitations.get(i);
										listOfInvitations.remove(i);
										listOfInvitations.add(0, tempInvitation);
									}
								}*/
								context.put("sortDirection", 1);
								context.put("sortColumnIndex", 2);
//								session.setAttribute("sortDirection", 1);
//								session.setAttribute("sortColumnIndex", 2);
							}
						}
					}
					context.put("regInvitation", listOfInvitations);

				} else if(action.equalsIgnoreCase(MANAGE_USER_DELETE_LEARNER_ACTION)) {

					String[] selectedRegisTrationValues = request.getParameterValues("regName");
					if( selectedRegisTrationValues != null ){
						long regIdArray[] = new long[selectedRegisTrationValues.length];

						for( int i=0; i<regIdArray.length; i++ ) {
							String regId = selectedRegisTrationValues[i];
							if( StringUtils.isNotBlank(regId)) {
								regIdArray[i] = Long.parseLong(selectedRegisTrationValues[i]);
							}	
						}		
						learnerService.deleteRegistrationInvitations(regIdArray);
						/*modifiedOrgGroups=orgGroupLearnerGroupService.getAllOrganizationalGroups(customer);
					 	rootOrgGroup=modifiedOrgGroups.get(0).getRootOrgGroup();*/
					}
					return new ModelAndView(redirectTemplate);
				}
			}else{
				request.setAttribute("newPage","true");
			}
			context.put("userData", user);
			return new ModelAndView(registrationInvitationTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(registrationInvitationTemplate);
	}

	/**
	 * @author Saptarshi
	 */
	public ModelAndView registrationInvitationAddNew(HttpServletRequest request,
			HttpServletResponse response) {
		try {

			RegistrationInvitation regInvitation = null;
			HttpSession session = request.getSession();
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext()
			.getAuthentication().getPrincipal();
			Map<Object, Object> context = new HashMap<Object, Object>();

			String actionType = request.getParameter("action");

			OrganizationalGroup rootOrgGroup = null;
			Long customerId= null;
			if (loggedInUser.isLMSAdministrator()){
				customerId = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();
			}
			else{
				customerId = loggedInUser.getLearner().getCustomer().getId();
			}

			List<OrganizationalGroup> organizationalGroups = orgGroupLearnerGroupService.getAllOrganizationalGroups(customerId);
			rootOrgGroup=organizationalGroups.get(0).getRootOrgGroup();

			//TODO will implement after new DAO written comment on 02/04/09.
			//Create OrgGroups list
			ManageOrganizationalGroups arrangedOrgGroup = new ManageOrganizationalGroups();
			List<Map>OrgGroupView = arrangedOrgGroup.createOrgGroupView(rootOrgGroup,null,null);

			TreeNode orgGroupRoot  = getOrgGroupTree(null, rootOrgGroup, null, null);
			List<TreeNode> treeAsList = orgGroupRoot.bfs();
			for(TreeNode node:treeAsList )
				log.debug(node.toString());
			context.put("orgGroupTreeAsList", treeAsList);

			log.debug((OrgGroupView==null)?"org group gets null":"org group size="+OrgGroupView.size());
			if ( actionType == null ) {
				if ( regInvitation == null ) {
					session = request.getSession();
					regInvitation = new RegistrationInvitation();
					context.put("OrgGroupView",OrgGroupView);
					context.put("regInvitationSession", regInvitation);
					session.setAttribute("regInvitationSession", regInvitation);
				}
			} 
			regInvitation = (RegistrationInvitation)session.getAttribute("regInvitationSession");
			context.put("regInvitationSession", regInvitation);
			session.setAttribute("regInvitationSession", regInvitation);
			return new ModelAndView(addNewInvitationTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(addNewInvitationTemplate);
	}

	/**
	 * @author Saptarshi
	 */
	public ModelAndView registrationInvitationMessage(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
			Map<Object, Object> context = new HashMap<Object, Object>();
			RegistrationInvitation regInvitation = (RegistrationInvitation)session.getAttribute("regInvitationSession");
			regInvitation.setInvitationName(request.getParameter("invName"));
			regInvitation.setPasscode(request.getParameter("passCode"));
			String checked = request.getParameter("regnNo");

			if (checked.equalsIgnoreCase("unlimited")) {
				regInvitation.setMaximumRegistration(9999);
				regInvitation.setIsUnlimited(true);
			} else {
				regInvitation.setIsUnlimited(false);
				regInvitation.setMaximumRegistration(Integer.parseInt(request.getParameter("maximumRegistration")));
			}

			String[] selectedOrgGroupValues = request.getParameterValues("groups");

			List<OrganizationalGroup> selectedOrgGroup = new ArrayList<OrganizationalGroup>();
			if( selectedOrgGroupValues != null ){
				for( int i=0; i<selectedOrgGroupValues.length; i++ ) {
					String orgGroupID = selectedOrgGroupValues[i];
					if( StringUtils.isNotBlank(orgGroupID)) {
						OrganizationalGroup orgGroup = learnerService.getOrganizationalGroupById(Long.valueOf(orgGroupID));
						selectedOrgGroup.add(orgGroup);
					}
				}
			}
			regInvitation.setOrgGroups(selectedOrgGroup);

			List<LearnerGroup> selectedLearnerGroups = new ArrayList<LearnerGroup>();
			LearnerGroup learnerGroup = null;
			String[] selectedGroups = request.getParameterValues("selectedLearnerGroups");

			if( selectedGroups!=null ){
				for( int i=0; i<selectedGroups.length; i++ ) {
					learnerGroup = learnerService.getLearnerGroupById(Long.valueOf(selectedGroups[i]));
					selectedLearnerGroups.add(learnerGroup);
				}
			}
			regInvitation.setLearnerGroups(selectedLearnerGroups);

			VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
			session.setAttribute("regInvitationSession",regInvitation);
			context.put("regInvitationSession", regInvitation);
			context.put("userData", user);
			return new ModelAndView(invitationMessageTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(invitationMessageTemplate);
	}

	/**
	 * @author Saptarshi
	 */
	public ModelAndView registrationInvitationReview(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			RegistrationInvitation regInvitation = null;
			HttpSession session = request.getSession();
			regInvitation = (RegistrationInvitation)session.getAttribute("regInvitationSession");
			regInvitation.setInvitationMessage(request.getParameter("freeRTE_content").trim());
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put("regnInv", regInvitation);
			session.setAttribute("regInvitationSession",regInvitation);
			return new ModelAndView(reviewTemplate, "context", context);

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(reviewTemplate);
	}

	/**
	 * @author Saptarshi
	 */
	public ModelAndView registrationInvitationSave(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			RegistrationInvitation regInvitation = null;
			HttpSession session = request.getSession();
			VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
			regInvitation = (RegistrationInvitation)session.getAttribute("regInvitationSession");

			if (vu360UserService.hasAdministratorRole(user))
				regInvitation.setCustomer(((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomer());
			else
				regInvitation.setCustomer(user.getLearner().getCustomer());

			learnerService.saveRegistrationInvitation(regInvitation);

		} catch (Exception e) {
			log.debug("exception", e);
		}

		return new ModelAndView(saveRegInvitationTemplate);
	}


	/**
	 * @author Saptarshi
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView sendRegistrationInvitationEmail(HttpServletRequest request, HttpServletResponse response) throws Exception{

		String toEmailAddress = request.getParameter("emailaddress");
		if (toEmailAddress.isEmpty()) {
			Map<Object, Object> context = new HashMap<Object, Object>();
			RegistrationInvitation regInvitation = (RegistrationInvitation)session.getAttribute("regnInv");
			context.put("regnInv", regInvitation);
			context.put("errMsg", "Please provide an email address.");
			session.setAttribute("regnInv", regInvitation);
			return new ModelAndView(regInvitationPreviewTemplate, "context", context);
		}
		String strRegistrationInvtId = request.getParameter("regInvitationId");
		Long regInvtId = Long.parseLong(strRegistrationInvtId);
		String mailMessage = request.getParameter("mailMessage");
		RegistrationInvitation regInvReturn =  learnerService.getRegistrationInvitationByID(regInvtId);
		Customer customer = regInvReturn.getCustomer();
		VU360User user = VU360UserAuthenticationDetails.getCurrentUser();
		
		Map model = new HashMap();		
		model.put("user", user);
		model.put("customer", customer);
		model.put("passcode", regInvReturn.getPasscode());
		
		String serverURL = request.getRequestURL().toString();
		serverURL = serverURL.substring(0, serverURL.indexOf( request.getContextPath() ) ) + request.getContextPath();	
		model.put("serverURL", serverURL);

		String patternStr = "\\\n";
		String replacementStr = "<br/>";
		// Compile regular expression
		Pattern pattern = Pattern.compile(patternStr);
		// Replace all occurrences of pattern in input
		Matcher matcher = pattern.matcher(mailMessage);
		mailMessage = matcher.replaceAll(replacementStr);                 

		model.put("invMessage", mailMessage);
		model.put("url", request.getScheme()+"://"+request.getServerName()
				+":"+request.getServerPort()+request.getContextPath());
		
		model.put("registrationId", regInvtId);
		
		//commented by Faisal A. Siddiqui July 21 09 for new branding
		//Brander brander= VU360Branding.getInstance().getBrander("default", new Language());
		Brander brander=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
		model.put("brander", brander);
		String regInvTemplatePath =  brander.getBrandElement("lms.email.selfRegistration.body");
		String fromAddress =  brander.getBrandElement("lms.email.selfRegistration.fromAddress");
		String fromCommonName =  brander.getBrandElement("lms.email.selfRegistration.fromCommonName");
		String subject =  brander.getBrandElement("lms.email.selfRegistration.subject");// + customer.getName();
		String support =  brander.getBrandElement("lms.email.resetPassWord.fromCommonName");
		model.put("support", support);

		String lmsDomain="";
		lmsDomain=FormUtil.getInstance().getLMSDomain(brander);
		model.put("lmsDomain",lmsDomain);
		
		String text = VelocityEngineUtils.mergeTemplateIntoString( velocityEngine, regInvTemplatePath, model);

		SendMailService.sendSMTPMessage(toEmailAddress, fromAddress, fromCommonName, subject, text); 


		String url = "/mgr_regInvitation-1.do";
		RedirectUtils.sendRedirect(request, response, url, false);

		return null;

	}

	public ModelAndView registrationInvitationPreview(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String id = request.getParameter("ID");
			Map<Object, Object> context = new HashMap<Object, Object>();
			RegistrationInvitation regInvitation = (RegistrationInvitation)learnerService.loadForPreviewRegistrationInvitation(Long.valueOf(id));
			regInvitation.setInvitationMessage(regInvitation.getInvitationMessage().trim());
			context.put("regnInv", regInvitation);
			session.setAttribute("regnInv", regInvitation);
			return new ModelAndView(regInvitationPreviewTemplate, "context", context);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView(regInvitationPreviewTemplate);
	}


	public void sendMail(String from,String to,String subject,String msgText){
		log.debug("send mail");
		SimpleMailMessage message = new SimpleMailMessage() ;
		message.setTo(to);
		message.setText(msgText) ;
		message.setFrom(from) ;
		message.setSubject(subject) ;
		try {
			mailSender.send(message) ;
		} catch ( MailException m ) {
			log.debug("COUGHT EXCEPTION WHILE SENDING MAIL in SelfRegistrationInvitation : " + m.toString());
		}
	}

	private TreeNode getOrgGroupTree( TreeNode parentNode, OrganizationalGroup orgGroup, 
			List<OrganizationalGroup> selectedLearnerOrgGroups, List<OrganizationalGroup> managableOrgGroups){

		if(orgGroup!=null){

			TreeNode node = new TreeNode(orgGroup);

			if( selectedLearnerOrgGroups != null ) {
				for(int orgGroupNum = 0; orgGroupNum < selectedLearnerOrgGroups.size(); orgGroupNum ++ ) {
					if(selectedLearnerOrgGroups.get(orgGroupNum).getId().equals(orgGroup.getId())) {
						node.setSelected(true);
					}
				}
			}
			if( managableOrgGroups != null ) {
				for(int orgGroupNum = 0; orgGroupNum < managableOrgGroups.size(); orgGroupNum ++ ) {
					if(managableOrgGroups.get(orgGroupNum).getId() == orgGroup.getId()) {
						node.setEnabled(true);
					}
				}
			} else {
				node.setEnabled(true);
			}
			List<OrganizationalGroup> childGroups = orgGroup.getChildrenOrgGroups();
			for(int i=0; i<childGroups.size(); i++){
				node = getOrgGroupTree(node, childGroups.get(i), selectedLearnerOrgGroups, managableOrgGroups);
			}

			if(parentNode!=null){
				parentNode.addChild(node);
				return parentNode;
			}else
				return node;
		}
		return null;
	}

	/*
	 *  getter & setters.
	 */
	public SimpleMailMessage getTemplateMessage() {
		return templateMessage;
	}

	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public String getRegistrationInvitationTemplate() {
		return registrationInvitationTemplate;
	}

	public void setRegistrationInvitationTemplate(
			String registrationInvitationTemplate) {
		this.registrationInvitationTemplate = registrationInvitationTemplate;
	}

	public String getAddNewInvitationTemplate() {
		return addNewInvitationTemplate;
	}

	public void setAddNewInvitationTemplate(String addNewInvitationTemplate) {
		this.addNewInvitationTemplate = addNewInvitationTemplate;
	}

	public String getInvitationMessageTemplate() {
		return invitationMessageTemplate;
	}

	public void setInvitationMessageTemplate(String invitationMessageTemplate) {
		this.invitationMessageTemplate = invitationMessageTemplate;
	}

	public String getReviewTemplate() {
		return reviewTemplate;
	}

	public void setReviewTemplate(String reviewTemplate) {
		this.reviewTemplate = reviewTemplate;
	}

	public String getSaveRegInvitationTemplate() {
		return saveRegInvitationTemplate;
	}

	public void setSaveRegInvitationTemplate(String saveRegInvitationTemplate) {
		this.saveRegInvitationTemplate = saveRegInvitationTemplate;
	}

	public String getRegInvitationPreviewTemplate() {
		return regInvitationPreviewTemplate;
	}

	public void setRegInvitationPreviewTemplate(String regInvitationPreviewTemplate) {
		this.regInvitationPreviewTemplate = regInvitationPreviewTemplate;
	}

	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	public void setOrgGroupLearnerGroupService(
			OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
}