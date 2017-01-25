/**
 * 
 */
package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CommissionableParty;
import com.softech.vu360.lms.service.CommissionService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.comparator.CommissionablePartyComparator;
import com.softech.vu360.lms.web.controller.model.CommissionablePartyForm;
import com.softech.vu360.lms.web.controller.validator.CommissionablePartyValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author muhammad.taqveem
 *
 */
public class CommissionablePartyController extends VU360BaseMultiActionController {

	private String commissionablePartiesListTemplate= null;
	private String commissionablePartiesEditTemplate= null;	
	private String addCommissionablePartyTemplate= null;
	private String addCommissionablePartyFinishTemplate= null;	
	private String addRedirectTemplate= null;
	private String failureTemplate = null;	
	private CommissionService commissionService;
	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {		
		if( command instanceof CommissionablePartyForm ){
			CommissionablePartyForm form = (CommissionablePartyForm)command;
			if( methodName.equals("updateCommissionablePartyDetails")){				
				if (request.getParameter("name") != null){
					form.setName(request.getParameter("name"));
				}				
			}
		}
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {	 			
	}

	public ModelAndView showCommissionableParties( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
	    request.getSession(true).setAttribute("feature", "LMS-ADM-0024");
		Map<Object, Object> context = new HashMap<Object, Object>();		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		context.put("title", "Administration Mode");		
		
		String showAll =(request.getParameter("showAll")==null)?"false":"true";
		int sortDirection =Integer.parseInt((request.getParameter("sortDirection")==null)?"0":request.getParameter("sortDirection"));		
		int sortColumnIndex =Integer.parseInt((request.getParameter("sortColumnIndex")==null)?"0":request.getParameter("sortColumnIndex"));
		
		context.put("showAll", showAll);
		context.put("sortDirection", sortDirection);
		context.put("sortColumnIndex", sortColumnIndex);
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();	
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
			String type = details.getCurrentSearchType().toString();					
				String entity = "";				
				if (request.getParameter("entity") != null)
					entity = request.getParameter("entity");				
				if(entity == null){
					entity = "";
				}				
				if(type.equalsIgnoreCase("distributor") ) 
				{
				 if( vu360AuthDetails.getCurrentDistributor() != null ){ 
					 ArrayList <CommissionableParty> commissionablePartyList= new ArrayList<CommissionableParty>(commissionService.getCommissionableParties(details.getCurrentDistributor().getId()));										 
					 Collections.sort(commissionablePartyList, new CommissionablePartyComparator(sortDirection,sortColumnIndex));
					 context.put("commissionablePartyList", commissionablePartyList);
				 } else
					throw new IllegalArgumentException("Distributor not found");
				}
				else{					
					return new ModelAndView(failureTemplate, "isRedirect", "d");					
				}
				return new ModelAndView(commissionablePartiesListTemplate, "context", context);			
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
	}
	
	public ModelAndView deleteCommissionableParty(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {	
		request.getSession(true).setAttribute("feature", "LMS-ADM-0024");
		Map<Object, Object> context = new HashMap<Object, Object>();
		String[] fieldsToDelete=request.getParameterValues("commissionablePartyCheck");
		if  ( fieldsToDelete!=null && fieldsToDelete.length>0) {
			Long[] ids = new Long[fieldsToDelete.length];
			for ( int i = 0 ; i < fieldsToDelete.length ; i++ ) {
				ids[i] = Long.valueOf(fieldsToDelete[i]);				 
			}
			HttpSession session = request.getSession(true);
			if(commissionService.deleteCommissionableParties(ids))
				session.setAttribute("deleteCommissionablePartyError", null);
			else
				session.setAttribute("deleteCommissionablePartyError", "Unable to delete because, one or more parties are participating in one or more commissions");
		}
		
		request.getSession(true).setAttribute("feature", "LMS-ADM-0024");			
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();	
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
			if( vu360AuthDetails.getCurrentDistributor() != null ){ 											
				 return showCommissionableParties(request, response, command, errors) ;//context.put("commissionablePartyList", new ArrayList(commissionService.getCommissionableParties(details.getCurrentDistributor().getId())));
			} else
				throw new IllegalArgumentException("Distributor not found");
								
				//return new ModelAndView(commissionablePartiesListTemplate, "context", context);			
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
	}
	
	public ModelAndView editCommissionableParty(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {	
		Map<Object, Object> context = new HashMap<Object, Object>();
		String commPartyId= request.getParameter("commissionablePartyId");
		
		request.getSession(true).setAttribute("feature", "LMS-ADM-0024");			
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){			
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
			if( vu360AuthDetails.getCurrentDistributor() != null ){ 
				CommissionableParty commParty= commissionService.loadForUpdateCommissionableParty(Long.valueOf(commPartyId));
				CommissionablePartyForm form = (CommissionablePartyForm)command;
				form.setId(commParty.getId());
				form.setName(commParty.getName());
				context.put("commissionableParty", commissionService.loadForUpdateCommissionableParty(Long.valueOf(commPartyId)));
			} else
				throw new IllegalArgumentException("Distributor not found");
								
				return new ModelAndView(commissionablePartiesEditTemplate, "context", context);			
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
	}
	
	public ModelAndView updateCommissionablePartyDetails(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {	
		Map<Object, Object> context = new HashMap<Object, Object>();
		CommissionablePartyForm form = (CommissionablePartyForm)command;
		CommissionablePartyValidator validator=(CommissionablePartyValidator)this.getValidator();
		validator.validateFirstPage(form, errors);
		if( errors.hasErrors() ) {
			context.put("commissionableParty", commissionService.loadForUpdateCommissionableParty(Long.valueOf(form.getId())));
			return new ModelAndView(commissionablePartiesEditTemplate, "context", context);			
		}

		CommissionableParty cParty= commissionService.loadForUpdateCommissionableParty(Long.valueOf(form.getId()));
		cParty.setName(form.getName());		
		commissionService.saveCommissionableParty(cParty);		
		
		request.getSession(true).setAttribute("feature", "LMS-ADM-0024");			
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
			VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();	
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
			if( vu360AuthDetails.getCurrentDistributor() != null ){ 											
				return showCommissionableParties(request, response, command, errors) ;
				//context.put("commissionablePartyList", new ArrayList(commissionService.getCommissionableParties(details.getCurrentDistributor().getId())));
			} else
				throw new IllegalArgumentException("Distributor not found");
								
				//return new ModelAndView(commissionablePartiesListTemplate, "context", context);			
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView addCommissionableParty(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {
		Map<Object, Object> context = new HashMap<Object, Object>();
		CommissionablePartyForm form = (CommissionablePartyForm)command;
		String action= request.getParameter("action");
		if(request.getParameter("fromSummaryPage")!=null ){			
			form.setName(request.getParameter("name"));									
			CommissionablePartyValidator validator=(CommissionablePartyValidator)this.getValidator();
			validator.validateFirstPage(form, errors);
			if( errors.hasErrors() ) {				
				return new ModelAndView(addCommissionablePartyTemplate);			
			}
			return new ModelAndView(addCommissionablePartyFinishTemplate);
		} else if(request.getParameter("prevtoSummaryPage")!=null ){
			return new ModelAndView(addCommissionablePartyTemplate); 
		} else if(request.getParameter("fromConfirmationPage")!=null ){
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
			
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();	
				VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
				if( vu360AuthDetails.getCurrentDistributor() != null ){ 
					CommissionableParty comParty= new CommissionableParty();
					comParty.setName(form.getName());
					comParty.setDistributor(details.getCurrentDistributor());
					commissionService.saveCommissionableParty(comParty);
					return showCommissionableParties(request, response, command, errors) ;
					//context.put("commissionablePartyList", new ArrayList(commissionService.getCommissionableParties(details.getCurrentDistributor().getId())));
					//return new ModelAndView(commissionablePartiesListTemplate, "context", context);
				 }
			}
			
		}
		form.setId(0);
		form.setName(null);
		return new ModelAndView(addCommissionablePartyTemplate);
		
	}

	public String getFailureTemplate() {
		return failureTemplate;
	}

	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}

	public String getCommissionablePartiesListTemplate() {
		return commissionablePartiesListTemplate;
	}

	public void setCommissionablePartiesListTemplate(
			String commissionablePartiesListTemplate) {
		this.commissionablePartiesListTemplate = commissionablePartiesListTemplate;
	}
	
	public CommissionService getCommissionService() {
		return commissionService;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}

	public String getCommissionablePartiesEditTemplate() {
		return commissionablePartiesEditTemplate;
	}

	public void setCommissionablePartiesEditTemplate(
			String commissionablePartiesEditTemplate) {
		this.commissionablePartiesEditTemplate = commissionablePartiesEditTemplate;
	}

	public String getAddCommissionablePartyTemplate() {
		return addCommissionablePartyTemplate;
	}

	public void setAddCommissionablePartyTemplate(
			String addCommissionablePartyTemplate) {
		this.addCommissionablePartyTemplate = addCommissionablePartyTemplate;
	}

	public String getAddCommissionablePartyFinishTemplate() {
		return addCommissionablePartyFinishTemplate;
	}

	public void setAddCommissionablePartyFinishTemplate(
			String addCommissionablePartyFinishTemplate) {
		this.addCommissionablePartyFinishTemplate = addCommissionablePartyFinishTemplate;
	}

	public String getAddRedirectTemplate() {
		return addRedirectTemplate;
	}

	public void setAddRedirectTemplate(String addRedirectTemplate) {
		this.addRedirectTemplate = addRedirectTemplate;
	}

}
