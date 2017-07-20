package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.ResultSet;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.helper.AdminSearchMemberEnum;
import com.softech.vu360.lms.web.controller.model.AdminSearchForm;
import com.softech.vu360.lms.web.controller.model.AdminSearchMember;
import com.softech.vu360.lms.web.filter.AdminSearchType;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.AdminSort;
import com.softech.vu360.util.FieldsValidation;
import com.softech.vu360.util.FormUtil;

public class SearchMemberController extends VU360BaseMultiActionController {

	private static final Logger log = Logger
			.getLogger(SearchMemberController.class.getName());
	private String searchMemberTemplate = null;
	private String selectMemberTemplate = null;
	private String redirectTemplate = null;
	private VU360UserService vu360UserService;
	private CustomerService customerService;
	private DistributorService distributorService;
	private LearnerService learnerService;
	
	HttpSession session = null;

	public SearchMemberController() {
		super();
	}

	public SearchMemberController(Object delegate) {
		super(delegate);
	}

	/**
	 * Set current distributor in session as an attribute to be later used by
	 * the velocity file for enabling or disabling call log columns
	 * 
	 * @param request
	 * @since 4.13 {LMS-8108}
	 * @author sm.humayun
	 */
	private void setCurrentDistributorInSession(HttpServletRequest request) {
		Distributor objDistributor = ((VU360UserAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentDistributor();
		if(objDistributor!=null)
			request.getSession(true).setAttribute("DISTRIBUTOR-FOR-CALL-LOGGING", objDistributor.getCallLoggingEnabled()?"Yes":"No");
	}

	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {

		/**
		 * Call Logging | LMS-8108 | S M Humayun | 27 April 2011
		 */
		this.setCurrentDistributorInSession(request);
		session = request.getSession(false);

		if (methodName.equalsIgnoreCase("displaySearchMemberView")) {
			// form.reset();
		}
	}

	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {

		AdminSearchForm form = (AdminSearchForm) command;
		if (methodName.equals("searchMembers")
				&& !form.isConstrainedCustomerSearch()) {

			if (form.getSearchType() != null &&
				(form.getSearchType().equalsIgnoreCase(AdminSearchForm.CUSTOMER_SEARCH_TYPE) ||
				 form.getSearchType().equalsIgnoreCase(AdminSearchForm.CUSTOMER_ADVANCE_SEARCH_TYPE)) ) {

				if (form.getSearchOrderDate() != null
						&& !form.getSearchOrderDate().equals("")) {
					// SimpleDateFormat sdf = new
					// SimpleDateFormat("MM/dd/yyyy");
					try {
						// sdf.parse(form.getSearchOrderDate());
						if (FieldsValidation.isValidDate(form
								.getSearchOrderDate()) == false)
							errors.rejectValue("searchOrderDate",
									"error.adminSearch.isCorrectOrderDateFormat.Message");
					} catch (Exception e) {
						errors.rejectValue("searchOrderDate",
								"error.adminSearch.isCorrectOrderDateFormat.Message");
					}
				}

				// --Start- [LMS-13588]
				// Validate Apostrophe (') that is not allowed in CustomerName,
				// OrderId, EmailAddress
				boolean isSingleQuotExists = false;
				if (form.getSearchCustomerName() != null
						&& !form.getSearchCustomerName().equals("")) {
					if (form.getSearchCustomerName().contains("'"))
						isSingleQuotExists = true;
				}

				if (form.getSearchOrderId() != null
						&& !form.getSearchOrderId().equals("")) {
					if (form.getSearchOrderId().contains("'"))
						isSingleQuotExists = true;
				}
				if (form.getSearchCusEmailAddress() != null
						&& !form.getSearchCusEmailAddress().equals("")) {
					if (form.getSearchCusEmailAddress().contains("'"))
						isSingleQuotExists = true;
				}

				if (isSingleQuotExists)
					errors.rejectValue("searchCustomerName",
							"error.adminSearch.singleQuotNotAlloed.Message");
				// -- End ----------
			}

		}

	}

	// default method
	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, String> context = new HashMap<String, String>();
		context.put("target", "displaySearchMemberView");
		request.setAttribute("newPage", "true");
		return new ModelAndView(redirectTemplate, "context", context);
	}

	// handler methods
	public ModelAndView displaySearchMemberView(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		// AdminSearchForm form = (AdminSearchForm)command;
		// form.resetAdminSearchForm();

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("featureGroup", "Search");

		return new ModelAndView(searchMemberTemplate);
	}

	public ModelAndView searchMembers(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		session.setAttribute("featureGroup", "Search");

		final AdminSearchForm form;
		final VU360User loggedInUser;
		final boolean constrainedCustomerSearch;
		final boolean constrainedLearnerSearch;
		final boolean hasValidationErrors;
		final String searchedCustomerName, searchedOrderId, searchedOrderDate, searchedCustomerEmailAddress;
		final String searchedDistributorName;
		final String searchedLearnerFirstName, searchedLearnerLastName, searchedLearnerEmailAddress;
		final ResultSet resultset;

		List<AdminSearchMember> adminSearchMemberList = new ArrayList<AdminSearchMember>();

		String sortDirection, sortColumnIndex;
		String showAll;
		int pageIndex;
		int pageSize;

		final Map<String, String> PagerAttributeMap;
		final Map<Object, Object> context;

		sortDirection = request.getParameter("sortDirection");
		sortColumnIndex = request.getParameter("sortColumnIndex");

		if (sortDirection == null)
			sortDirection = "0";
		if (sortColumnIndex == null)
			sortColumnIndex = "0";

		showAll = request.getParameter("showAll");

		if (StringUtils.isBlank(showAll)) {
			showAll = "false";
		}
		
		String currentPageIndex = request.getParameter("pageCurrIndex");
		if (StringUtils.isNumeric(currentPageIndex)) {
			pageIndex = Integer.parseInt(currentPageIndex);
		} else {
			pageIndex = 0;
		}

		// Added By MariumSaud LMS-22148: Attribute that will hold the value of selected Page Size
		String selectedResultSize = request.getParameter("selectedResultSize")==""?"10":request.getParameter("selectedResultSize");
		if (StringUtils.isNumeric(selectedResultSize)) {
			pageSize = Integer.parseInt(selectedResultSize);
		} else {
			pageSize = 10;
		}
		
		log.debug("pageIndex = " + pageIndex);

		PagerAttributeMap = new HashMap<String, String>();
		PagerAttributeMap.put("pageIndex", String.valueOf(pageIndex));
	
		context = new HashMap<Object, Object>();

		form = (AdminSearchForm) command;

		resultset = new ResultSet();

		loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();

		if (form.getSearchType() != null) {
			if (!form.getSearchType().equalsIgnoreCase(
					(String) session.getAttribute("searchType"))) {
				sortDirection = "0";
				sortColumnIndex = "0";
			}
		}

		if (loggedInUser.getLmsAdministrator().isGlobalAdministrator() == false
				&& (loggedInUser.getLmsAdministrator().getDistributorGroups() == null
				|| loggedInUser.getLmsAdministrator().getDistributorGroups().size() == 0)) {
			return new ModelAndView(searchMemberTemplate);
		}

		searchedCustomerName = StringUtils
				.isBlank(form.getSearchCustomerName()) ? StringUtils.EMPTY
				: form.getSearchCustomerName();
		searchedOrderId = StringUtils.isBlank(form.getSearchOrderId()) ? StringUtils.EMPTY
				: form.getSearchOrderId();
		searchedOrderDate = StringUtils.isBlank(form.getSearchOrderDate()) ? StringUtils.EMPTY
				: form.getSearchOrderDate();
		searchedCustomerEmailAddress = StringUtils.isBlank(form
				.getSearchCusEmailAddress()) ? StringUtils.EMPTY : form
				.getSearchCusEmailAddress();

		searchedDistributorName = StringUtils.isBlank(form
				.getSearchDistributorName()) ? StringUtils.EMPTY : form
				.getSearchDistributorName();

		searchedLearnerFirstName = StringUtils.isBlank(form
				.getSearchFirstName()) ? StringUtils.EMPTY : form
				.getSearchFirstName();
		searchedLearnerLastName = StringUtils.isBlank(form.getSearchLastName()) ? StringUtils.EMPTY
				: form.getSearchLastName();
		searchedLearnerEmailAddress = StringUtils.isBlank(form
				.getSearchEmailAddress()) ? StringUtils.EMPTY : form
				.getSearchEmailAddress();

		// If a distributor or reseller is selected
		// On search box, following options starts to appear:
		// (x) Limit search to selected reseller
		// to limit search
		
		//Added By MariumSaud : LMS-22376 : Keeping the track of 'Limit Customer Search' checkbox with PageCount dropdown 
		if(request.getParameter("isConstrainedCustomerSearch")!=null){
			constrainedCustomerSearch = request.getParameter("isConstrainedCustomerSearch")==""?false:Boolean.parseBoolean(request.getParameter("isConstrainedCustomerSearch").toString());
		}
		else{
			constrainedCustomerSearch = form.isConstrainedCustomerSearch();
		}

		// If a distributor or reseller or customer is selected
		// On search box, following options starts to appear:
		// (x) Limit search to selected reseller
		// or ...
		// (x) Limit search to selected customer
		// to limit search
		
		//Added By MariumSaud : LMS-22376 : Keeping the track of 'Limit Learner Search' checkbox with PageCount dropdown 
		if(request.getParameter("isConstrainedLearnerSearch")!=null){
			constrainedLearnerSearch = request.getParameter("isConstrainedLearnerSearch")==""?false:Boolean.parseBoolean(request.getParameter("isConstrainedLearnerSearch").toString());
		}
		else{
			constrainedLearnerSearch = form.isConstrainedLearnerSearch();
		}
		
		// Validation status?
		hasValidationErrors = errors.hasErrors();

		// Selected Distributor
		Distributor distributor = ((VU360UserAuthenticationDetails) SecurityContextHolder
				.getContext().getAuthentication().getDetails())
				.getCurrentDistributor();

		searchMembers(request, form, loggedInUser, distributor,
				constrainedCustomerSearch, constrainedLearnerSearch,
				hasValidationErrors, searchedCustomerName, searchedOrderId,
				searchedOrderDate, searchedCustomerEmailAddress,
				searchedDistributorName, searchedLearnerFirstName,
				searchedLearnerLastName, searchedLearnerEmailAddress,
				adminSearchMemberList, resultset, sortDirection,
				sortColumnIndex, (showAll.equals("false") ? pageIndex : 0),
				PagerAttributeMap, context,
				pageSize);

		form.setCurrentPageIndex((showAll.equals("false") ? pageIndex : 0));
		form.setTotalRecordCount(resultset.total);
		
		//Added By Marium Saud : Two variables 'sortDirection' and 'sortColumnIndex' has been added in AdminSearchForm 
		//inorder to keep track of Column Sort Index and sort Direction.
		form.setSortColumnIndex(sortColumnIndex);
		form.setSortDirection(sortDirection);
		
		// Added By MariumSaud LMS-22148: Form Attribute that will hold the value of selected Page Size and is used when Next and Prev Button is clicked
		form.setSelectedResultSize(Integer.parseInt(selectedResultSize));
		
		//Added By MariumSaud : LMS-22376- Holding constrain Search value for Customer and Learner to implement it with Page Size count
		form.setConstrainedCustomerSearch(constrainedCustomerSearch);
		form.setConstrainedLearnerSearch(constrainedLearnerSearch);
		
		context.put("selectedResultSize", selectedResultSize);
		context.put("resultSize", selectedResultSize);
		
		context.put("sortDirection", sortDirection);
		context.put("sortColumnIndex", sortColumnIndex);
		
		context.put("sortColumnIndex", sortColumnIndex);

		
	
		return new ModelAndView(searchMemberTemplate, "context", context);

	}

	/**
	 * @param request
	 * @param form
	 * @param loggedInUser
	 * @param distributor
	 * @param constrainedCustomerSearch
	 * @param constrainedLearnerSearch
	 * @param hasValidationErrors
	 * @param searchedCustomerName
	 * @param searchedOrderId
	 * @param searchedOrderDate
	 * @param searchedCustomerEmailAddress
	 * @param searchedDistributorName
	 * @param searchedLearnerFirstName
	 * @param searchedLearnerLastName
	 * @param searchedLearnerEmailAddress
	 * @param adminSearchMemberList
	 * @param resultset
	 * @param sortDirection
	 * @param sortColumnIndex
	 * @param pageIndex
	 * @param PagerAttributeMap
	 * @param context
	 * @param retrieveRowCount
	 */
	private void searchMembers(HttpServletRequest request,
			final AdminSearchForm form, final VU360User loggedInUser,
			Distributor distributor, final boolean constrainedCustomerSearch,
			final boolean constrainedLearnerSearch,
			final boolean hasValidationErrors,
			final String searchedCustomerName, final String searchedOrderId,
			final String searchedOrderDate,
			final String searchedCustomerEmailAddress,
			final String searchedDistributorName,
			final String searchedLearnerFirstName,
			final String searchedLearnerLastName,
			final String searchedLearnerEmailAddress,
			List<AdminSearchMember> adminSearchMemberList,
			final ResultSet resultset, String sortDirection,
			String sortColumnIndex, int pageIndex,
			final Map<String, String> PagerAttributeMap,
			final Map<Object, Object> context, int retrieveRowCount) {

		// ascertain, search type is defined
		if (!StringUtils.isBlank(form.getSearchType())) {

			// customer search, it is
			if (form.getSearchType().equalsIgnoreCase(
					AdminSearchForm.CUSTOMER_SEARCH_TYPE)) {

				// do the search
				customerSearch(form, adminSearchMemberList, loggedInUser,
						constrainedCustomerSearch, distributor,
						searchedCustomerName, searchedOrderId,
						searchedOrderDate, searchedCustomerEmailAddress,
						hasValidationErrors, resultset, pageIndex,
						retrieveRowCount, sortColumnIndex,
						Integer.parseInt(sortDirection));
			}
			// searching customer with advanced search options
			else if (form.getSearchType().equalsIgnoreCase(
					AdminSearchForm.CUSTOMER_ADVANCE_SEARCH_TYPE)) {

				// do the search
				advanceSearchCustomer(form, loggedInUser,
						constrainedCustomerSearch, hasValidationErrors,
						adminSearchMemberList, distributor,
						searchedCustomerName, searchedOrderId,
						searchedOrderDate, searchedCustomerEmailAddress,
						form.getSearchOperatorForCustName(),
						form.getSearchOperatorForOrderId(),
						form.getSearchOperatorForOrderDate(), retrieveRowCount,
						pageIndex, resultset, sortColumnIndex,
						Integer.parseInt(sortDirection));

			} else if (form.getSearchType().equalsIgnoreCase(
					AdminSearchForm.DISTRIBUTOR_SEARCH_TYPE)) {

				searchDistributor(form, loggedInUser, searchedDistributorName,
						adminSearchMemberList, resultset, pageIndex,
						retrieveRowCount, sortColumnIndex,
						Integer.parseInt(sortDirection));

			} else if (form.getSearchType().equalsIgnoreCase(
					AdminSearchForm.LEARNER_SEARCH_TYPE)) {

				searchLearner(form, loggedInUser, constrainedLearnerSearch,
						searchedLearnerFirstName, searchedLearnerLastName,
						searchedLearnerEmailAddress, adminSearchMemberList,
						distributor, resultset, pageIndex, retrieveRowCount,
						sortColumnIndex, Integer.parseInt(sortDirection));

			}

			PagerAttributeMap
					.put("totalCount", String.valueOf(resultset.total));

			// TODO:
			// ramiz.uddin
			// 4/23/2015
			// THE LINE BELOW IS BEING COMMENTED
			// FOR TESTING PURPOSES
			// context.put("showAll", showAll);

			// reset form
			// TODO:
			// ramiz.uddin
			// 4/23/2015
			// THE LINE BELOW IS BEING COMMENTED
			// FOR TESTING PURPOSES
			// form.resetAdminSearchForm();

			request.setAttribute("PagerAttributeMap", PagerAttributeMap);
			
			//Added By MariumSaud : setting the pageSize as request Attribute to be used as DEFAULT_PAGE_SIZE for Admin search and will effect any other search via VelocityPagerTool.java
			request.setAttribute("selectedResultSize", retrieveRowCount);
			
			 setupPageSorting(context, form, adminSearchMemberList,
			 sortDirection,
			 sortColumnIndex);
			
			form.setAdminSearchMemberList(adminSearchMemberList);

		}
	}

	/**
	 * @param context
	 * @param form
	 * @param sortAppliedTo
	 * @param sortDirection
	 * @param sortColumnIndex
	 * Method has been commented-out by Marium Saud : JPA Spring Data Sorting has been implemented , AdminSort.java(Comparator Class) is no longer used for Sort purpose.
	 * Only 'Type' column of Search Result Grid will be sorted using Comparator Class.
	 */
	// private void setupPageSorting(Map<Object, Object> context,
	// AdminSearchForm form, List<AdminSearchMember> sortAppliedTo,
	// String sortDirection,
	// String sortColumnIndex) {
	//
	// if (StringUtils.isNotBlank(sortDirection)
	// && StringUtils.isNotBlank(sortColumnIndex)) {
	//
	// if (sortColumnIndex.equalsIgnoreCase("0")) {
	//
	// if (sortDirection.equalsIgnoreCase("0")) {
	//
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("name");
	//
	// adminSort.setSortDirection(Integer
	// .parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", 0);
	// context.put("sortColumnIndex", 0);
	// }
	// else {
	//
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("name");
	//
	// adminSort.setSortDirection(Integer
	// .parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", 1);
	// context.put("sortColumnIndex", 0);
	// }
	//
	// }
	// // sort by e-mail address
	// else if (sortColumnIndex.equalsIgnoreCase("1")) {
	//
	// if (sortDirection.equalsIgnoreCase("0")) {
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("currentSearchType");
	//
	// adminSort.setSortDirection(Integer
	// .parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", 0);
	// context.put("sortColumnIndex", 1);
	// } else {
	//
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("currentSearchType");
	//
	// adminSort.setSortDirection(Integer
	// .parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", 1);
	// context.put("sortColumnIndex", 1);
	//
	// }
	//
	// }
	// // sort by status
	// else if (sortColumnIndex.equalsIgnoreCase("2")) {
	//
	// // list of in-active members
	// if (sortDirection.equalsIgnoreCase("0")) {
	//
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("email");
	//
	// adminSort.setSortDirection(Integer
	// .parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", 0);
	// context.put("sortColumnIndex", 2);
	//
	// } else {
	//
	// // list of active members
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("email");
	//
	// adminSort.setSortDirection(Integer
	// .parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", 1);
	// context.put("sortColumnIndex", 2);
	// }
	// } else if (sortColumnIndex.equalsIgnoreCase("3")) {
	//
	// if (sortDirection.equalsIgnoreCase("0")) {
	//
	// // list of in-active members
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("status");
	//
	// adminSort.setSortDirection(Integer
	// .parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", 0);
	// context.put("sortColumnIndex", 3);
	//
	// } else {
	//
	// // list of active members
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("status");
	//
	// adminSort.setSortDirection(Integer
	// .parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", 1);
	// context.put("sortColumnIndex", 3);
	// }
	// } else if (sortColumnIndex.equalsIgnoreCase("4")
	// && (form.getSearchType().equalsIgnoreCase(
	// AdminSearchForm.CUSTOMER_ADVANCE_SEARCH_TYPE) || form
	// .getSearchType().equalsIgnoreCase(
	// AdminSearchForm.CUSTOMER_SEARCH_TYPE))) {
	//
	// // list of in-active members
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("originalContractCreationDate");
	//
	// adminSort.setSortDirection(Integer.parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", sortDirection);
	// context.put("sortColumnIndex", 4);
	//
	// } else if (sortColumnIndex.equalsIgnoreCase("5")
	// && (form.getSearchType().equalsIgnoreCase(
	// AdminSearchForm.CUSTOMER_ADVANCE_SEARCH_TYPE) || form
	// .getSearchType().equalsIgnoreCase(
	// AdminSearchForm.CUSTOMER_SEARCH_TYPE))) {
	//
	// // list of in-active members
	// AdminSort adminSort = new AdminSort();
	// adminSort.setSortBy("recentContractCreationDate");
	//
	// adminSort.setSortDirection(Integer.parseInt(sortDirection));
	// Collections.sort(sortAppliedTo, adminSort);
	//
	// context.put("sortDirection", sortDirection);
	// context.put("sortColumnIndex", 5);
	//
	// }
	// }
	// }

	/**
	 * @param context
	 * @param form
	 * @param sortAppliedTo
	 * @param sortDirection
	 * @param sortColumnIndex
	 */
	private void setupPageSorting(Map<Object, Object> context,
			AdminSearchForm form, List<AdminSearchMember> sortAppliedTo,
			String sortDirection, String sortColumnIndex) {

			if (StringUtils.isNotBlank(sortDirection)
				&& StringUtils.isNotBlank(sortColumnIndex)) {
				if (sortColumnIndex.equalsIgnoreCase("1")) {
					
					 if (sortDirection.equalsIgnoreCase("0")) {
					 AdminSort adminSort = new AdminSort();
					 adminSort.setSortBy("currentSearchType");
					
					 adminSort.setSortDirection(Integer
					 .parseInt(sortDirection));
					 Collections.sort(sortAppliedTo, adminSort);
					 } 
					 
					 else {
					
					 AdminSort adminSort = new AdminSort();
					 adminSort.setSortBy("currentSearchType");
					
					 adminSort.setSortDirection(Integer
					 .parseInt(sortDirection));
					 Collections.sort(sortAppliedTo, adminSort);
					
					 }

			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
		}
	}
	}

	/**
	 * @param form
	 * @param loggedInUser
	 * @param constrainedLearnerSearch
	 * @param searchedLearnerFirstName
	 * @param searchedLearnerLastName
	 * @param searchedLearnerEmailAddress
	 * @param adminSearchMemberList
	 * @param distributor
	 * @param resultset
	 * @param pageIndex
	 * @param retrieveRowCount
	 */
	private void searchLearner(AdminSearchForm form, VU360User loggedInUser,
			final boolean constrainedLearnerSearch,
			final String searchedLearnerFirstName,
			final String searchedLearnerLastName,
			final String searchedLearnerEmailAddress,
			List<AdminSearchMember> adminSearchMemberList,
			Distributor distributor, ResultSet resultset, int pageIndex,
			int retrieveRowCount, String sortColumnIndex, int sortDirection) {

		Customer selectedCustomer;
		Distributor selectedDistributor;

		// selected Customer
		selectedCustomer = ((VU360UserAuthenticationDetails) SecurityContextHolder
				.getContext().getAuthentication().getDetails())
				.getCurrentCustomer();

		// selected Distributor
		selectedDistributor = ((VU360UserAuthenticationDetails) SecurityContextHolder
				.getContext().getAuthentication().getDetails())
				.getCurrentDistributor();

		// i.e. its a default customer
		if (selectedCustomer.getId().longValue() == loggedInUser.getLearner().getCustomer().getId().longValue()) {
			selectedCustomer = null;
		}

		List<VU360User> searchedLearners = new ArrayList<VU360User>();

		// If a distributor or reseller is selected
		// On search box, following options starts to appear:
		// (x) Limit search to selected reseller
		if (constrainedLearnerSearch) {

			// SortBy is different for same ColumnIndex , if
			// constrainedLearnerSearch is 'true'
			AdminSearchMemberEnum.LEARNER
					.setCONSTRAINT_LEARNER_SEARCH(constrainedLearnerSearch);

			// if distributor is selected,
			if (selectedDistributor != null) {

				// search learners by distributor
				searchedLearners = distributorService.getLearnersByDistributor(
						searchedLearnerFirstName, searchedLearnerLastName,
						searchedLearnerEmailAddress, StringUtils.EMPTY,
						distributor.getId(), Integer.valueOf(pageIndex),
						retrieveRowCount, resultset,
						AdminSearchMemberEnum.LEARNER
								.getSortBy(sortColumnIndex), sortDirection);
			}
			// if customer is selected
			else if (selectedCustomer != null) {

				// search learners by customer
				searchedLearners = distributorService.getLearnersByCustomer(
						searchedLearnerFirstName, searchedLearnerLastName,
						searchedLearnerEmailAddress, StringUtils.EMPTY,
						selectedCustomer.getId(), Integer.valueOf(pageIndex),
						retrieveRowCount, resultset,
						AdminSearchMemberEnum.LEARNER
								.getSortBy(sortColumnIndex), sortDirection);
			}

		} else {

			// if none is selected; nor distributor
			// neither customer then do open search
			searchedLearners = distributorService.getAllLearners(
					searchedLearnerFirstName, searchedLearnerLastName,
					searchedLearnerEmailAddress, StringUtils.EMPTY,
					loggedInUser, Integer.valueOf(pageIndex), retrieveRowCount,
					resultset,
					AdminSearchMemberEnum.LEARNER.getSortBy(sortColumnIndex),
					sortDirection);
		}

		for (VU360User vu360User : searchedLearners) {
			AdminSearchMember adminSearchMember = convertToAdminSearchMember(vu360User);
			adminSearchMemberList.add(adminSearchMember);
		}
		form.setSearchType(AdminSearchForm.LEARNER_SEARCH_TYPE);
		form.setAdminSearchMemberList(adminSearchMemberList);

		session.setAttribute("searchType", AdminSearchForm.LEARNER_SEARCH_TYPE);
	}

	/**
	 * @param vu360User
	 * @return
	 */
	private AdminSearchMember convertToAdminSearchMember(VU360User vu360User) {
		AdminSearchMember adminSearchMember = new AdminSearchMember();
		adminSearchMember.setId(vu360User.getLearner().getId());
		adminSearchMember.setName(vu360User.getName());
		adminSearchMember.setEMail(vu360User.getEmailAddress());
		adminSearchMember.setStatus(vu360User.getAccountNonLocked());
		adminSearchMember.setCustomer(vu360User.getLearner().getCustomer());
		adminSearchMember.setCurrentSearchType(AdminSearchType.LEARNER);
		
		// new fields added for JIRA Integration
		adminSearchMember.setCustomerCode(vu360User.getLearner().getCustomer()
				.getCustomerCode());
		adminSearchMember.setResellerCode(vu360User.getLearner().getCustomer()
				.getDistributor().getDistributorCodeUI());
		return adminSearchMember;
	}

	/**
	 * @param form
	 * @param loggedInUser
	 * @param searchedDistributorName
	 * @param adminSearchMemberList
	 * @param resultSet
	 * @param pageIndex
	 * @param retrieveRowCount
	 */
	private void searchDistributor(AdminSearchForm form,
			VU360User loggedInUser, final String searchedDistributorName,
			List<AdminSearchMember> adminSearchMemberList, ResultSet resultSet,
			int pageIndex, int retrieveRowCount, String sortColumnIndex,
			int sortDirection) {
		List<Distributor> searchedDistributors = distributorService
				.findDistributorsByName(searchedDistributorName, loggedInUser,
						false, pageIndex, retrieveRowCount, resultSet,
						AdminSearchMemberEnum.RESELLER
								.getSortBy(sortColumnIndex), sortDirection);

		// Add searchedDistributors to adminSearchMemberList
		for (Distributor dist : searchedDistributors) {
			AdminSearchMember adminSearchMember = convertToAdminSearchMember(dist);
			adminSearchMemberList.add(adminSearchMember);
		}
		form.setAdminSearchMemberList(adminSearchMemberList);
		form.setSearchType(AdminSearchForm.DISTRIBUTOR_SEARCH_TYPE);
		session.setAttribute("searchType",
				AdminSearchForm.DISTRIBUTOR_SEARCH_TYPE);
	}

	/**
	 * @param distributor
	 * @return
	 */
	private AdminSearchMember convertToAdminSearchMember(Distributor distributor) {
		AdminSearchMember adminSearchMember = new AdminSearchMember();
		adminSearchMember.setId(distributor.getId());
		adminSearchMember.setName(distributor.getName());
		adminSearchMember.setEMail(distributor.getDistributorEmail());
		adminSearchMember.setStatus(distributor.getActive());
		adminSearchMember.setCurrentSearchType(AdminSearchType.DISTRIBUTOR);
		adminSearchMember.setResellerCode(distributor.getDistributorCodeUI());
		return adminSearchMember;
	}

	/**
	 * @param form
	 * @param loggedInUser
	 * @param constrainedCustomerSearch
	 * @param hasValidationErrors
	 * @param adminSearchMemberList
	 * @param distributor
	 * @param searchedCustomerName
	 * @param searchedOrderId
	 * @param searchedOrderDate
	 * @param searchedCustomerEmailAddress
	 * @param customerNameSearchOperator
	 * @param orderIdSearchOperator
	 * @param orderDateSearchOperator
	 * @param retrieveRowCount
	 * @param pageIndex
	 * @param resultSet
	 */
	private void advanceSearchCustomer(AdminSearchForm form,
			VU360User loggedInUser, final boolean constrainedCustomerSearch,
			final boolean hasValidationErrors,
			List<AdminSearchMember> adminSearchMemberList,
			final Distributor distributor, final String searchedCustomerName,
			final String searchedOrderId, final String searchedOrderDate,
			final String searchedCustomerEmailAddress,
			final String customerNameSearchOperator,
			final String orderIdSearchOperator,
			final String orderDateSearchOperator, int retrieveRowCount,
			int pageIndex, ResultSet resultSet, String sortColumnIndex,
			int sortDirection) {

		List<Map<Object, Object>> objCol = new ArrayList<Map<Object, Object>>();
		// Check for Distributor Constrained Search
		if (constrainedCustomerSearch) {
			List<Customer> customers = customerService
					.findCustomersWithEntitlementByDistributor(distributor, searchedCustomerName, searchedCustomerEmailAddress,
							pageIndex, retrieveRowCount, resultSet,
							AdminSearchMemberEnum.CUSTOMER
									.getSortBy(sortColumnIndex), sortDirection);
			
			// Add Customer to adminSearchMemberList
						for (Customer customer : customers) {
							AdminSearchMember adminSearchMember = convertToAdminSearchMember(customer);
							adminSearchMemberList.add(adminSearchMember);
						}
		} else {

			if (!hasValidationErrors) {
				objCol = customerService.findCustomersAdvanceSearch(
						searchedCustomerName, customerNameSearchOperator,
						searchedOrderId, orderIdSearchOperator,
						searchedOrderDate, orderDateSearchOperator,
						searchedCustomerEmailAddress, loggedInUser, false,
						Integer.valueOf(pageIndex), retrieveRowCount,
						resultSet, AdminSearchMemberEnum.CUSTOMER
								.getSortBy(sortColumnIndex), sortDirection);

				for (Map map : objCol) {
					AdminSearchMember adminSearchMember = convertToAdminSearchModel(map);

					adminSearchMemberList.add(adminSearchMember);
				}

			}

		}

		form.setAdminSearchMemberList(adminSearchMemberList);
		form.setSearchType(AdminSearchForm.CUSTOMER_ADVANCE_SEARCH_TYPE);
		session.setAttribute("searchType",
				AdminSearchForm.CUSTOMER_ADVANCE_SEARCH_TYPE);
	}

	/**
	 * @param form
	 * @param adminSearchMemberList
	 * @param searchbyUser
	 * @param constrainedCustomerSearch
	 * @param distributor
	 * @param searchedCustomerName
	 * @param searchedOrderId
	 * @param searchedOrderDate
	 * @param searchedCustomerEmailAddress
	 * @param hasValidationErrors
	 * @param resultSet
	 * @param pageIndex
	 * @param retrieveRowCount
	 * @param customerName
	 * @param orderId
	 * @param orderDate
	 * @param customerEmailAddress
	 */
	private void customerSearch(AdminSearchForm form,
			List<AdminSearchMember> adminSearchMemberList,
			final VU360User searchbyUser,
			final boolean constrainedCustomerSearch,
			final Distributor distributor, final String searchedCustomerName,
			final String searchedOrderId, final String searchedOrderDate,
			final String searchedCustomerEmailAddress,
			boolean hasValidationErrors, ResultSet resultSet, int pageIndex,
			int retrieveRowCount, String sortColumnIndex, int sortDirection) {

		List<Map<Object, Object>> customerList = new ArrayList<Map<Object, Object>>();

		// If, search is constrained
		// to distributor
		if (constrainedCustomerSearch) {
			List<Customer> customers = customerService
					.findCustomersWithEntitlementByDistributor(distributor, searchedCustomerName, searchedCustomerEmailAddress,
							pageIndex, retrieveRowCount, resultSet,
							AdminSearchMemberEnum.CUSTOMER
									.getSortBy(sortColumnIndex), sortDirection);
			
			// Add Customer to adminSearchMemberList
			for (Customer customer : customers) {
				AdminSearchMember adminSearchMember = convertToAdminSearchMember(customer);
				adminSearchMemberList.add(adminSearchMember);
			}

		} else {
			customerList = customerService.findCustomersSimpleSearch(
					searchedCustomerName, searchedOrderId, searchedOrderDate,
					searchedCustomerEmailAddress, searchbyUser, false,
					Integer.valueOf(pageIndex), retrieveRowCount, resultSet,
					AdminSearchMemberEnum.CUSTOMER.getSortBy(sortColumnIndex),
					sortDirection);

			for (Map map : customerList) {

				adminSearchMemberList.add(convertToAdminSearchModel(map));

			}

		}

		form.setAdminSearchMemberList(adminSearchMemberList);
		form.setSearchType(AdminSearchForm.CUSTOMER_SEARCH_TYPE);
		session.setAttribute("searchType", AdminSearchForm.CUSTOMER_SEARCH_TYPE);
	}
	
	/**
	 * Added by Marium Saud
	 * 'adminSearchMemberList' obj gets reset when it is manipulated at service level and is being returned from there.
	 *  Moved code from Service to controller inorder to retain the obtained search result set.
	 */
	private AdminSearchMember convertToAdminSearchMember(Customer customer){
		AdminSearchMember adminSearchMember = new AdminSearchMember();
        adminSearchMember.setId(customer.getId());
        adminSearchMember.setName(customer.getName());
        adminSearchMember.setEMail(customer.getEmail());
        adminSearchMember.setStatus(customer.isActive());
        adminSearchMember.setCurrentSearchType(AdminSearchType.CUSTOMER);
        adminSearchMember.setCustomerCode(customer.getCustomerCode()); 
        adminSearchMember.setResellerCode(customer.getDistributor().getDistributorCodeUI());
        
		List<CustomerEntitlement> lstCustomerEnt  = customerService.findCustomerEntitlementByCustomerId(customer.getId());
		if(lstCustomerEnt!=null && lstCustomerEnt.size()>0){
			adminSearchMember.setOriginalContractCreationDate(FormUtil.getInstance().formatDate( lstCustomerEnt.get(0).getContractCreationDate(), "MM/dd/yyyy"));
			adminSearchMember.setRecentContractCreationDate(FormUtil.getInstance().formatDate( lstCustomerEnt.get(lstCustomerEnt.size()-1).getContractCreationDate(), "MM/dd/yyyy"));
			adminSearchMember.originalContractCreationDatetime = lstCustomerEnt.get(0).getContractCreationDate();
			adminSearchMember.recentContractCreationDatetime = lstCustomerEnt.get(lstCustomerEnt.size()-1).getContractCreationDate();
		}
		return adminSearchMember;
		}

	/**
	 * @param Map
	 * @return AdminSearchMember
	 */
	private AdminSearchMember convertToAdminSearchModel(Map map) {
		AdminSearchMember adminSearchMember = new AdminSearchMember();

		adminSearchMember.setId(Long.valueOf(map.get("customerId").toString()));
		adminSearchMember.setName(map.get("customerName").toString());
		
		if(map.get("customerEmail")!=null){
			adminSearchMember.setEMail(map.get("customerEmail").toString());	
		}
		else{
			adminSearchMember.setEMail(null);
		}
		
		if (map.get("customerStatus") != null
				&& map.get("customerStatus").toString().equals("1"))
			adminSearchMember.setStatus(true);
		else
			adminSearchMember.setStatus(false);

		adminSearchMember.setCurrentSearchType(AdminSearchType.CUSTOMER);
		adminSearchMember.setCustomerCode(map.get("customerCode").toString());

		adminSearchMember.setResellerCode("VUCUS" + "-"
				+ map.get("distributorId").toString());
		if (map.get("originalContractCreationDate") != null) {
			adminSearchMember.setOriginalContractCreationDate(FormUtil
					.getInstance().formatDate(
							(Date) map.get("originalContractCreationDate"),
							"MM/dd/yyyy"));
			adminSearchMember.originalContractCreationDatetime = (Date) map
					.get("originalContractCreationDate");
		}

		if (map.get("recentContractCreationDate") != null) {
			adminSearchMember.setRecentContractCreationDate(FormUtil
					.getInstance().formatDate(
							(Date) map.get("recentContractCreationDate"),
							"MM/dd/yyyy"));
			adminSearchMember.recentContractCreationDatetime = (Date) map
					.get("recentContractCreationDate");
		}
		return adminSearchMember;
	}

	private List<AdminSearchMember> simpleSearchMembers(
			List<Customer> searchedCustomers,
			List<Distributor> searchedDistributors,
			List<VU360User> searchedUsers) {
		List<AdminSearchMember> adminSearchMemberList = new ArrayList<AdminSearchMember>();
		for (Customer customer : searchedCustomers) {
			AdminSearchMember adminSearchMember = new AdminSearchMember();
			adminSearchMember.setId(customer.getId());
			adminSearchMember.setName(customer.getName());
			adminSearchMember.setEMail(customer.getEmail());
			adminSearchMember.setStatus(customer.isActive());
			adminSearchMember.setCurrentSearchType(AdminSearchType.CUSTOMER);
			adminSearchMember.setCustomerCode(customer.getCustomerCode());// jira
																			// link
																			// integration
			adminSearchMemberList.add(adminSearchMember);
		}

		for (Distributor distributor : searchedDistributors) {
			AdminSearchMember adminSearchMember = convertToAdminSearchMember(distributor);
			adminSearchMemberList.add(adminSearchMember);
		}

		for (VU360User vu360User : searchedUsers) {
			AdminSearchMember adminSearchMember = convertToAdminSearchMember(vu360User);
			adminSearchMemberList.add(adminSearchMember);
		}
		// form.setAdminSearchMemberList(adminSearchMemberList);
		return adminSearchMemberList;
	}

	public ModelAndView refreshQuestions(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		request.getSession(true).setAttribute("featureGroup", "Search");

		String showAll = request.getParameter("showAll");
		if (showAll == null) {
			showAll = "false";
		}
		

		final AdminSearchForm form;
		final VU360User loggedInUser;
		final boolean constrainedCustomerSearch;
		final boolean constrainedLearnerSearch;
		final boolean hasValidationErrors;
		final String searchedCustomerName, searchedOrderId, searchedOrderDate, searchedCustomerEmailAddress;
		final String searchedDistributorName;
		final String searchedLearnerFirstName, searchedLearnerLastName, searchedLearnerEmailAddress;
		final ResultSet resultset;

		List<AdminSearchMember> adminSearchMemberList = new ArrayList<AdminSearchMember>();

		int sortDirection, sortColumnIndex;
		int pageIndex = 0;
		int pageSize;

		final Map<String, String> PagerAttributeMap;
		final Map<Object, Object> context;

		String currentPageIndex = request.getParameter("pageCurrIndex");
		if (StringUtils.isNumeric(currentPageIndex)) {
			pageIndex = Integer.parseInt(currentPageIndex);
		}
		
		// Added By MariumSaud LMS-22148: Attribute that will hold the value of selected Page Size
		String selectedResultSize = request.getParameter("selectedResultSize")==""?"10":request.getParameter("selectedResultSize");
		if (StringUtils.isNumeric(selectedResultSize)) {
			pageSize = Integer.parseInt(selectedResultSize);
		} else {
			pageSize = 10;
		}
		
		PagerAttributeMap = new HashMap<String, String>();
		PagerAttributeMap.put("pageIndex", String.valueOf(pageIndex));

		context = new HashMap<Object, Object>();

		form = (AdminSearchForm) command;

		resultset = new ResultSet();

		loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();

		searchedCustomerName = StringUtils
				.isBlank(form.getSearchCustomerName()) ? StringUtils.EMPTY
				: form.getSearchCustomerName();
		searchedOrderId = StringUtils.isBlank(form.getSearchOrderId()) ? StringUtils.EMPTY
				: form.getSearchOrderId();
		searchedOrderDate = StringUtils.isBlank(form.getSearchOrderDate()) ? StringUtils.EMPTY
				: form.getSearchOrderDate();
		searchedCustomerEmailAddress = StringUtils.isBlank(form
				.getSearchCusEmailAddress()) ? StringUtils.EMPTY : form
				.getSearchCusEmailAddress();

		searchedDistributorName = StringUtils.isBlank(form
				.getSearchDistributorName()) ? StringUtils.EMPTY : form
				.getSearchDistributorName();

		searchedLearnerFirstName = StringUtils.isBlank(form
				.getSearchFirstName()) ? StringUtils.EMPTY : form
				.getSearchFirstName();
		searchedLearnerLastName = StringUtils.isBlank(form.getSearchLastName()) ? StringUtils.EMPTY
				: form.getSearchLastName();
		searchedLearnerEmailAddress = StringUtils.isBlank(form
				.getSearchEmailAddress()) ? StringUtils.EMPTY : form
				.getSearchEmailAddress();

		// If a distributor or reseller is selected
		// On search box, following options starts to appear:
		// (x) Limit search to selected reseller
		// to limit search
		
		//Added By MariumSaud : LMS-22376 : Keeping the track of 'Limit Customer Search' checkbox with PageCount dropdown 
		if(request.getParameter("isConstrainedCustomerSearch")!=null){
			constrainedCustomerSearch = request.getParameter("isConstrainedCustomerSearch")==""?false:Boolean.parseBoolean(request.getParameter("isConstrainedCustomerSearch").toString());
		}
		else{
			constrainedCustomerSearch = form.isConstrainedCustomerSearch();
		}

		// If a distributor or reseller or customer is selected
		// On search box, following options starts to appear:
		// (x) Limit search to selected reseller
		// or ...
		// (x) Limit search to selected customer
		// to limit search
		
		//Added By MariumSaud : LMS-22376 : Keeping the track of 'Limit Learner Search' checkbox with PageCount dropdown 
		if(request.getParameter("isConstrainedLearnerSearch")!=null){
			constrainedLearnerSearch = request.getParameter("isConstrainedLearnerSearch")==""?false:Boolean.parseBoolean(request.getParameter("isConstrainedLearnerSearch").toString());
		}
		else{
			constrainedLearnerSearch = form.isConstrainedLearnerSearch();
		}

		// Validation status?
		hasValidationErrors = errors.hasErrors();

		// Selected Distributor
		Distributor distributor = ((VU360UserAuthenticationDetails) SecurityContextHolder
				.getContext().getAuthentication().getDetails())
				.getCurrentDistributor();
		
		//Added By Marium Saud : Inorder to have complete Column wise Sorting these request Params are being set on 'Next' and 'Prev' actions
		sortDirection = Integer.parseInt(request.getParameter("direction"));
		sortColumnIndex = Integer.parseInt(request.getParameter("sortIndex"));
		
		/**Commented By Marium Saud : No session attribute are being set ; lines of code are of no use. 

		sortDirection = ((session.getAttribute("sortDirection") != null) && (StringUtils
				.isBlank(session.getAttribute("sortDirection").toString()))) ? Integer
				.valueOf(session.getAttribute("sortDirection").toString()) : 0;
		
		sortColumnIndex = ((session.getAttribute("sortColumnIndex") != null) && (StringUtils
				.isBlank(session.getAttribute("sortColumnIndex").toString()))) ? Integer
				.valueOf(session.getAttribute("sortColumnIndex").toString())
				: 0;
				**/

		context.put("sortDirection", sortDirection);
		context.put("sortColumnIndex", sortColumnIndex);

		context.put("showAll", showAll);
		
		searchMembers(request, form, loggedInUser, distributor,
				constrainedCustomerSearch, constrainedLearnerSearch,
				hasValidationErrors, searchedCustomerName, searchedOrderId,
				searchedOrderDate, searchedCustomerEmailAddress,
				searchedDistributorName, searchedLearnerFirstName,
				searchedLearnerLastName, searchedLearnerEmailAddress,
				adminSearchMemberList, resultset,
				String.valueOf(sortDirection), String.valueOf(sortColumnIndex),
				(showAll.equals("false") ? pageIndex : 0), PagerAttributeMap,
				context,pageSize);

		form.setCurrentPageIndex((showAll.equals("false") ? pageIndex : 0));
		form.setTotalRecordCount(resultset.total);
		
		// Added By MariumSaud LMS-22148: Form Attribute that will hold the value of selected Page Size and is used when Next and Prev Button is clicked
		form.setSelectedResultSize(Integer.parseInt(selectedResultSize));
		
		//Added By MariumSaud : LMS-22376- Holding constrain Search value for Customer and Learner to implement it with Page Size count
		form.setConstrainedCustomerSearch(constrainedCustomerSearch);
		form.setConstrainedLearnerSearch(constrainedLearnerSearch);
		
		context.put("selectedResultSize", selectedResultSize);
		context.put("resultSize", selectedResultSize);

		return new ModelAndView(searchMemberTemplate, "context", context);
	}

	public ModelAndView selectMember(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("featureGroup", "Search");
		VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails) SecurityContextHolder
				.getContext().getAuthentication().getDetails();

		AdminSearchForm form = (AdminSearchForm) command;
		if (form.getAdminSearchMemberList() != null
				&& form.getAdminSearchMemberList().size() > 0) {
			if (form.getAdminSearchMemberList().get(0).getCurrentSearchType()
					.equals(AdminSearchType.LEARNER)
					&& details.getProxyLearner() != null) {
				return new ModelAndView(
						"redirect:adm_learnerProfile.do?method=displayLearnerProfile");
			} else
				return new ModelAndView(selectMemberTemplate);
		} else
			return new ModelAndView(selectMemberTemplate);
	}

	public ModelAndView resetSearchMembers(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		/**
		 * LMS-9512 | S M Humayun | 28 Mar 2011
		 */
		request.getSession(true).setAttribute("featureGroup", "Search");

		AdminSearchForm form = new AdminSearchForm();
		form = (AdminSearchForm) command;
		form.resetAdminSearchForm();
		form.setAdminSearchMemberList(null);
		return new ModelAndView(searchMemberTemplate);
	}

	/**
	 * @return the searchMemberTemplate
	 */
	public String getSearchMemberTemplate() {
		return searchMemberTemplate;
	}

	/**
	 * @param searchMemberTemplate
	 *            the searchMemberTemplate to set
	 */
	public void setSearchMemberTemplate(String searchMemberTemplate) {
		this.searchMemberTemplate = searchMemberTemplate;
	}

	/**
	 * @return the vu360UserService
	 */
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	/**
	 * @param vu360UserService
	 *            the vu360UserService to set
	 */
	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}

	/**
	 * @return the customerService
	 */
	public CustomerService getCustomerService() {
		return customerService;
	}

	/**
	 * @param customerService
	 *            the customerService to set
	 */
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * @return the redirectTemplate
	 */
	public String getRedirectTemplate() {
		return redirectTemplate;
	}

	/**
	 * @param redirectTemplate
	 *            the redirectTemplate to set
	 */
	public void setRedirectTemplate(String redirectTemplate) {
		this.redirectTemplate = redirectTemplate;
	}

	/**
	 * @return the distributorService
	 */
	public DistributorService getDistributorService() {
		return distributorService;
	}

	/**
	 * @param distributorService
	 *            the distributorService to set
	 */
	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

	/**
	 * @return the selectMemberTemplate
	 */
	public String getSelectMemberTemplate() {
		return selectMemberTemplate;
	}

	/**
	 * @param selectMemberTemplate
	 *            the selectMemberTemplate to set
	 */
	public void setSelectMemberTemplate(String selectMemberTemplate) {
		this.selectMemberTemplate = selectMemberTemplate;
	}

	public LearnerService getLearnerService() {
		return learnerService;
	}

	public void setLearnerService(LearnerService learnerService) {
		this.learnerService = learnerService;
	}

}