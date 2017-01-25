package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Author;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AuthorService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.VelocityPagerTool;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.SearchUsersForm;
import com.softech.vu360.lms.web.controller.model.UserItemForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author muzammil.shaikh
 *
 */
public class AssignUsersAuthorController extends VU360BaseMultiActionController implements InitializingBean {

	private static final Logger log = Logger.getLogger(AssignUsersAuthorController.class.getName());
	private static final String SEARCH_ACTION = "search";
	private static final String CANCEL_ACTION = "cancel";
	private static final String ASSIGNAUTHOR_ACTION = "assignAuthors";
	
	private static final String MANAGE_USER_SORT_FIRSTNAME = "firstName";
	private static final String MANAGE_USER_SORT_LASTNAME = "lastName";
	private static final String MANAGE_USER_SORT_USERNAME = "emailAddress";
	private static final String[] SORTABLE_COLUMNS = {MANAGE_USER_SORT_FIRSTNAME,MANAGE_USER_SORT_LASTNAME,MANAGE_USER_SORT_USERNAME};
	
	private static final String REDIRECT_FROM_EDIT_CUSTOMER_PROFILE = "EditCustomerProfile";
	private static final String REDIRECT_FROM_EDIT_DISTRIBUTOR_PROFILE = "EditDistributorProfile";
	private static final String SEARCHTYPE_CUSTOMER_USERS = "searchCustomerUsers";
	private static final String SEARCHTYPE_REPRESENTATIVECUSTOMER_USERS = "searchRepresentativeCustomerUsers";
	private static final String RESULT_KEY_USERS_LIST = "UsersList";
	
	private String selectLearnersTemplate = null;
	private String assignAuthorToUsersResultTemplate = null;
	private VU360UserService vu360UserService;
	private CustomerService customerService;
	private DistributorService distributorService;
	private AuthorService authorService;
	private String cancelTemplate = null;

	//private String errorMessage = null;
	
	public AssignUsersAuthorController() {
		super();
	}

	public AssignUsersAuthorController(Object delegate) {
		super(delegate);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView searchUsers(HttpServletRequest request,
			HttpServletResponse response,Object command, BindException errors )throws Exception {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("pageNo", "1");
		
		try {			
			SearchUsersForm form = (SearchUsersForm)command;
			
			if (!StringUtils.isBlank(form.getRedirectingFrom().trim())) {
				if (form.getRedirectingFrom().trim().equals(REDIRECT_FROM_EDIT_DISTRIBUTOR_PROFILE)) 
					form.setCustomerId(null);
				else 
					form.setDistributorId(null);
				form.reset();				
			}
			
			if (form.getPerformAction() != null) {
				if (form.getPerformAction().trim().equals(SEARCH_ACTION) && form.getSearchType() != null) {
					Map<Object,Object> resultMap = null;
					List<VU360User> users = new ArrayList<VU360User>();
					List<UserItemForm> userItems = new ArrayList<UserItemForm>();
					Customer customer = null;
					
					Integer totalResults = 0;
					int pageNo = form.getPageIndex()<0 ? 0 : form.getPageIndex()/VelocityPagerTool.DEFAULT_PAGE_SIZE;
					int sortColumnIndex = form.getSortColumnIndex();
					String sortBy = (sortColumnIndex>=0 && sortColumnIndex<SORTABLE_COLUMNS.length) ? SORTABLE_COLUMNS[sortColumnIndex]: MANAGE_USER_SORT_FIRSTNAME ;
					int sortDirection = form.getSortDirection()<0 ? 0:(form.getSortDirection()>1?1:form.getSortDirection());
					if( form.getSearchType().trim().equals(SEARCHTYPE_CUSTOMER_USERS)) {
						customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext()
								.getAuthentication().getDetails()).getCurrentCustomer();						
					}
					else if(form.getSearchType().trim().equals(SEARCHTYPE_REPRESENTATIVECUSTOMER_USERS)) {
						Distributor currentDistributor = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext()
								.getAuthentication().getDetails()).getCurrentDistributor();	
						
						if (currentDistributor != null)
							customer = currentDistributor.getMyCustomer();
					}
					
					if (customer != null) {
						if (form.isShowAll()) {
							users = vu360UserService .searchCustomerUsers(customer, request.getParameter("searchFirstName").trim(), 
									request.getParameter("searchLastName").trim(), request.getParameter("searchEmailAddress").trim(), sortBy, sortDirection);
							totalResults = users.size();
						}
						else {
							resultMap = vu360UserService.searchCustomerUsers(customer, request.getParameter("searchFirstName").trim(), 
									request.getParameter("searchLastName").trim(), request.getParameter("searchEmailAddress").trim(), pageNo, 
									VelocityPagerTool.DEFAULT_PAGE_SIZE, sortBy, sortDirection);
							
							if (resultMap != null) {
								users = (List<VU360User>)resultMap.get(RESULT_KEY_USERS_LIST);
								totalResults = (Integer)resultMap.get("recordSize");
							}
						}							
					}
					
					//by ows
					for(UserItemForm usr : form.getUsers()){
						if(usr.isSelected()){
							form.getSelectedUsers().add(usr);
						}
					}
					
					
					if (!users.isEmpty()) {
						Map<Long, Author> usersAuthorsMap = authorService.getUsersAuthorMap(users);
						for (VU360User user : users) {
							UserItemForm userItemForm = new UserItemForm();
							userItemForm.setSelected(Boolean.FALSE);
							userItemForm.setUser(user);
							
							if(usersAuthorsMap.get(user.getId()) != null) {
								userItemForm.setAuthorAssigned(Boolean.TRUE);
							}
							userItems.add(userItemForm);
						}
					}
					
					form.setUsers(userItems);
					//for pagination to work for a paged list
					Map<String,String> pagerAttributeMap = new HashMap<String,String>();
					pagerAttributeMap.put("pageIndex", Integer.toString(form.getPageIndex()));
					pagerAttributeMap.put("totalCount", totalResults.toString());
					request.setAttribute("PagerAttributeMap", pagerAttributeMap);
				}
			}			
		} catch (Exception e) {
			log.debug("exception", e);
		}
		
		return new ModelAndView(selectLearnersTemplate, "context", context);
	}

	
	public ModelAndView assignAuthorToUsers(HttpServletRequest request,
			HttpServletResponse response,Object command, BindException errors )throws Exception {
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		SearchUsersForm form = (SearchUsersForm)command;
		String[] userIds = form.getUserId();
		
		if (form.getUserId() != null && form.getUserId().length > 0) {
			context.put("pageNo", "2");
			VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
			
			
			System.out.println("No of Users selected: " + form.getSelectedUsers().size());
			
			//by ows
			for(UserItemForm usr : form.getUsers()){
				if(usr.isSelected()){
					form.getSelectedUsers().add(usr);
				}
			}
			
			context.put("selectedUsersCount", form.getSelectedUsers().size());
			
			Map<String, List<VU360User>> result = authorService.createAuthorForUsers(form.getSelectedUsers(), loggedInUser);
			
			List<VU360User> authorNotCreatedUsers = result.get("failedAuthorUsers");
			List<VU360User> authorCreatedUsers = result.get("successfulAuthorUsers");
			
			context.put("authorNotCreatedUsers", authorNotCreatedUsers);
			context.put("authorCreatedUsers", authorCreatedUsers);
			//int created = userIds.length - authorNotCreatedUsers.size();
			int created=form.getSelectedUsers().size()-authorNotCreatedUsers.size();
			System.out.println("No of Users successfully assigned authors: " + created);
			System.out.println("No of Users not assigned authors: " + authorNotCreatedUsers.size());
			
			return new ModelAndView(assignAuthorToUsersResultTemplate, "context", context);
		}
		else {
			context.put("pageNo", "1");
			context.put("errorMessageKey", "lms.assignAuthorToLearners.usernotselected.error");
			//Put some validation logic here
		}
		
		return new ModelAndView(selectLearnersTemplate, "context", context);
	}
	
	public ModelAndView cancelAction(HttpServletRequest request,
			HttpServletResponse response,Object command, BindException errors )throws Exception {
		SearchUsersForm form = (SearchUsersForm)command;
		
		Map<Object, Object> context = new HashMap<Object, Object>();
		if (!StringUtils.isBlank(form.getCustomerId())){
			context.put("redirectToCustomerEditProfile", Boolean.TRUE);
		}
		else if (!StringUtils.isBlank(form.getDistributorId())) {
			context.put("redirectToDistributorEditProfile", Boolean.TRUE);
		}
		form.reset();
		
		return new ModelAndView(cancelTemplate, "context", context);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public VU360UserService getVu360UserService() {
		return vu360UserService;
	}

	public void setVu360UserService(VU360UserService vu360UserService) {
		this.vu360UserService = vu360UserService;
	}
	
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}

		
	public AuthorService getAuthorService() {
		return authorService;
	}

	public void setAuthorService(AuthorService authorService) {
		this.authorService = authorService;
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		
		SearchUsersForm form = (SearchUsersForm)command;
		/*form.setSearchType("");
		form.setAction("");*/
		if (!StringUtils.isBlank(request.getParameter("customerId"))){
			form.setCustomerId(request.getParameter("customerId").trim());
		}
		else if (!StringUtils.isBlank(request.getParameter("distributorId"))) {
			form.setDistributorId(request.getParameter("distributorId").trim());
		}		
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public String getSelectLearnersTemplate() {
		return selectLearnersTemplate;
	}

	public void setSelectLearnersTemplate(String selectLearnersTemplate) {
		this.selectLearnersTemplate = selectLearnersTemplate;
	}
	
	
	public String getAssignAuthorToUsersResultTemplate() {
		return assignAuthorToUsersResultTemplate;
	}

	public void setAssignAuthorToUsersResultTemplate(
			String assignAuthorToUsersResultTemplate) {
		this.assignAuthorToUsersResultTemplate = assignAuthorToUsersResultTemplate;
	}

	public String getCancelTemplate() {
		return cancelTemplate;
	}

	public void setCancelTemplate(String cancelTemplate) {
		this.cancelTemplate = cancelTemplate;
	}

	
	
}
