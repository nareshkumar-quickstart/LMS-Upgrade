package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.Instructor;
import com.softech.vu360.lms.model.InstructorApproval;
import com.softech.vu360.lms.model.Provider;
import com.softech.vu360.lms.model.ProviderApproval;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.RegulatoryAnalystService;
import com.softech.vu360.lms.util.UserPermissionChecker;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.Menu;
import com.softech.vu360.lms.web.controller.model.accreditation.SearchMemberForm;
import com.softech.vu360.lms.web.controller.model.accreditation.SearchMemberItem;
import com.softech.vu360.util.HtmlEncoder;
import com.softech.vu360.util.RecordSort;

/**
 * @author Dyutiman
 * created on 17-july-2009
 *
 */
public class SearchMemberController extends VU360BaseMultiActionController {

	//private static final Logger log = Logger.getLogger(SearchMemberController.class.getName());
	@Inject
	private AccreditationService accreditationService = null;
	@Inject
	private RegulatoryAnalystService regulatoryAnalystService;
	private String searchMemberTemplate = null;
	private String redirectTemplate = null;
	private String redirectToOtherPageIfPermisionRevokedTemplate = null;
//	HttpSession session = null;

	public SearchMemberController() {
		super();
	}

	public SearchMemberController(Object delegate) {
		super(delegate);
	}

	//default method
	protected ModelAndView handleNoSuchRequestHandlingMethod( NoSuchRequestHandlingMethodException ex, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute("newPage","true");
		Map<String, Object> context = new HashMap <String, Object>();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if( !UserPermissionChecker.hasAccessToFeature("LMS-ACC-0001", loggedInUser, request.getSession(true)) ) { 
				HttpSession session = request.getSession();
				session.setAttribute("isAdminSwitch", true);
				Menu menu = new Menu(loggedInUser,request);
				context.put("menu", menu);
				return new ModelAndView(redirectToOtherPageIfPermisionRevokedTemplate,"context",context);
			}
		context.put("target", "displaySearchMemberView");
		return new ModelAndView(searchMemberTemplate, "context", context);
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		if(command instanceof SearchMemberForm){
			//SearchMemberForm form = (SearchMemberForm)command;
			if(methodName.equals("searchMembers")) {
				// do nothing
			}
		}	
	}

	public ModelAndView displaySearchMemberView(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {
		return new ModelAndView(searchMemberTemplate);
	}
	
	public ModelAndView searchMembers(HttpServletRequest request, HttpServletResponse response, 
			Object command,	BindException errors) throws Exception{
		boolean isSearch=false;
		SearchMemberForm form = (SearchMemberForm)command;
		// @MariumSaud
//		VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser(); //PRINCIPAL: 07-09-2016
		
		// parameter type of findCredential() is changed so creating second user object of type VO.
		// first user object is also necessary for further operation
		com.softech.vu360.lms.vo.VU360User loggedInUserVO = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		HttpSession session = request.getSession();
		Map<Object, Object> context = new HashMap<Object, Object>();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();
		String searchAll = form.getAllSearch();
		String allName = "";
		if( searchAll.equalsIgnoreCase("true") ) {
			allName = (request.getParameter("allName") == null) ? "" : 
				request.getParameter("allName");
		}
		List<SearchMemberItem> items = new ArrayList<SearchMemberItem>();

		if( form.getSearchType().equalsIgnoreCase("Credential") || searchAll.equalsIgnoreCase("true") ) {
			String credName = "";
			if( searchAll.equalsIgnoreCase("false") ) {
				credName = (request.getParameter("credName") == null) ? "" : 
					request.getParameter("credName");
			} else {
				credName = allName;
			}
			String credType = (request.getParameter("credType") == null) ? "" : 
				request.getParameter("credType");
			List<Credential> creds = accreditationService.findCredential(credName, credType, 
					loggedInUserVO.getRegulatoryAnalyst());
			for( Credential cred : creds ) {
				SearchMemberItem newItem = new SearchMemberItem();
				newItem.setName(cred.getOfficialLicenseName());
				newItem.setType("CREDENTIAL");
				newItem.setId(cred.getId().toString());
				items.add(newItem);
			}
			isSearch=true;
			credName = HtmlEncoder.escapeHtmlFull(credName).toString();
			credType = HtmlEncoder.escapeHtmlFull(credType).toString();
			context.put("credName", credName);
			context.put("credType", credType);
		}
		if ( form.getSearchType().equalsIgnoreCase("Regulator") || searchAll.equalsIgnoreCase("true") ) {
			String regName = "";
			if( searchAll.equalsIgnoreCase("false") ) {
				regName = (request.getParameter("regName") == null) ? "" : 
					request.getParameter("regName");
			} else {
				regName = allName;
			}
			List<Regulator> regs = accreditationService.searchRegulator(regName, regulatoryAnalystService.getRegulatoryAnalystById(loggedInUserVO.getRegulatoryAnalyst().getId()));
			for( Regulator reg : regs ) {
				SearchMemberItem newItem = new SearchMemberItem();
				newItem.setName(reg.getName());
				newItem.setType("REGULATOR");
				newItem.setId(reg.getId().toString());
				items.add(newItem);
			}
			isSearch=true;
			regName = HtmlEncoder.escapeHtmlFull(regName).toString();
			context.put("regName", regName);
		}
		if ( form.getSearchType().equalsIgnoreCase("Provider") || searchAll.equalsIgnoreCase("true") ) {
			String provName = "";
			if( searchAll.equalsIgnoreCase("false") ) {
				provName = (request.getParameter("provName") == null) ? "" : 
					request.getParameter("provName");
			} else {
				provName = allName;
			}
			List<Provider> provs = accreditationService.searchProviders(provName, regulatoryAnalystService.getRegulatoryAnalystById(loggedInUserVO.getRegulatoryAnalyst().getId()));
			for( Provider prov : provs ) {
				SearchMemberItem newItem = new SearchMemberItem();
				newItem.setName(prov.getName());
				newItem.setType("PROVIDER");
				newItem.setId(prov.getId().toString());
				items.add(newItem);
			}
			isSearch=true;
			provName = HtmlEncoder.escapeHtmlFull(provName).toString();
			context.put("provName", provName);
		}
		if ( form.getSearchType().equalsIgnoreCase("Approval") || searchAll.equalsIgnoreCase("true") ) {
			String apprName = "";
			if( searchAll.equalsIgnoreCase("false") ) {
				apprName = (request.getParameter("apprName") == null) ? "" : 
					request.getParameter("apprName");
			} else {
				apprName = allName;
			}
			List<CourseApproval> courseApprovals = accreditationService.findCourseApproval(apprName, "", "",
					"", -1);
			for( CourseApproval c : courseApprovals ) {
				SearchMemberItem newItem = new SearchMemberItem();
				newItem.setName(c.getApprovedCourseName());
				newItem.setType("COURSE-APPROVAL");
				newItem.setId(c.getId().toString());
				items.add(newItem);
			}

			List<ProviderApproval> providerApprovals = accreditationService.findProviderApproval(apprName,
					"", "", "", -1);
			for( ProviderApproval p : providerApprovals ) {
				SearchMemberItem newItem = new SearchMemberItem();
				newItem.setName(p.getApprovedProviderName());
				newItem.setType("PROVIDER-APPROVAL");
				newItem.setId(p.getId().toString());
				items.add(newItem);
			}

			List<InstructorApproval> instructorApprovals = accreditationService.findInstructorApproval(
					apprName, "", "", "", -1);
			for( InstructorApproval i : instructorApprovals ) {
				SearchMemberItem newItem = new SearchMemberItem();
				newItem.setName(i.getApprovedInstructorName());
				newItem.setType("INSTRUCTOR-APPROVAL");
				newItem.setId(i.getId().toString());
				items.add(newItem);
			}
			isSearch=true;
			apprName = HtmlEncoder.escapeHtmlFull(apprName).toString();
			context.put("apprName", apprName);

		}
		if ( form.getSearchType().equalsIgnoreCase("Instructor") || searchAll.equalsIgnoreCase("true") ) {
			String instName = "";
			if( searchAll.equalsIgnoreCase("false") ) {
				instName = (request.getParameter("instName") == null) ? "" : 
					request.getParameter("instName");
			} else {
				instName = allName;
			}
			List<Instructor> ins = accreditationService.searchInstructor(instName);
			for( Instructor in : ins ) {
				SearchMemberItem newItem = new SearchMemberItem();
				newItem.setName(in.getUserName());
				newItem.setType("INSTRUCTOR");
				newItem.setId(in.getId().toString());
				items.add(newItem);
			}
			isSearch=true;
			instName = HtmlEncoder.escapeHtmlFull(instName).toString();
			context.put("instName", instName);
		}
		// for sorting...
		String sortColumnIndex = request.getParameter("sortColumnIndex");
//		if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
//			sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
		String sortDirection = request.getParameter("sortDirection");
//		if( sortDirection == null && session.getAttribute("sortDirection") != null )
//			sortDirection = session.getAttribute("sortDirection").toString();
		String showAll = request.getParameter("showAll");
		if ( showAll == null ) showAll = "false";
		//if ( showAll == "" ) showAll = "true";
		context.put("showAll", showAll);
		context.put("searchType", form.getSearchType());
		
		if( sortColumnIndex != null && sortDirection != null ) {

			if(form.getItems().size()>0 && items.size()<=0 && isSearch==false){
				items=form.getItems();
				}
			request.setAttribute("PagerAttributeMap", PagerAttributeMap);

			// sorting against regulator name
			if( sortColumnIndex.equalsIgnoreCase("0") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					RecordSort sort = new RecordSort();
					sort.setSortBy("name");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(items, sort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 0);
				} else {
					RecordSort sort = new RecordSort();
					sort.setSortBy("name");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(items, sort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 0);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 0);
				}
				// sorting against alias
			} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
				if( sortDirection.equalsIgnoreCase("0") ) {

					RecordSort sort = new RecordSort();
					sort.setSortBy("type");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(items, sort);
					context.put("sortDirection", 0);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 0);
//					session.setAttribute("sortColumnIndex", 1);
				} else {
					RecordSort sort = new RecordSort();
					sort.setSortBy("type");
					sort.setSortDirection(Integer.parseInt(sortDirection));
					Collections.sort(items, sort);
					context.put("sortDirection", 1);
					context.put("sortColumnIndex", 1);
//					session.setAttribute("sortDirection", 1);
//					session.setAttribute("sortColumnIndex", 1);
				}
			}
		}
		form.setItems(items);
		return new ModelAndView(searchMemberTemplate, "context", context);
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// do nothing
	}

	public String getSearchMemberTemplate() {
		return searchMemberTemplate;
	}

	public void setSearchMemberTemplate(String searchMemberTemplate) {
		this.searchMemberTemplate = searchMemberTemplate;
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public RegulatoryAnalystService getRegulatoryAnalystService() {
		return regulatoryAnalystService;
	}

	public void setRegulatoryAnalystService(RegulatoryAnalystService regulatoryAnalystService) {
		this.regulatoryAnalystService = regulatoryAnalystService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	public String getRedirectToOtherPageIfPermisionRevokedTemplate() {
		return redirectToOtherPageIfPermisionRevokedTemplate;
	}

	public void setRedirectToOtherPageIfPermisionRevokedTemplate(
			String redirectToOtherPageIfPermisionRevokedTemplate) {
		this.redirectToOtherPageIfPermisionRevokedTemplate = redirectToOtherPageIfPermisionRevokedTemplate;
	}
	
}