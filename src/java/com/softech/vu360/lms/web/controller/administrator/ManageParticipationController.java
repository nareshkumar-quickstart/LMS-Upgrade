/**
 * 
 */
package com.softech.vu360.lms.web.controller.administrator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Commission;
import com.softech.vu360.lms.model.CommissionParticipation;
import com.softech.vu360.lms.model.CommissionableParty;
import com.softech.vu360.lms.service.CommissionService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.CommissionParticipationForm;
import com.softech.vu360.lms.web.controller.validator.CommissionParticipationValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;

/**
 * @author muhammad.Rehan.Rana
 *
 */
public class ManageParticipationController extends VU360BaseMultiActionController {

	private String addParticipationTemplate = null;
	private String addParticipationConfirmationTemplate = null;
	private String editParticipationTemplate = null;
	private CommissionService commissionService;
	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());
	

	public void displaySummeryPage( HttpServletRequest request, Map<Object, Object> context, long  parentCommissionParticipation) throws Exception {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
		{
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
			if(vu360AuthDetails != null && vu360AuthDetails.getOriginalPrincipal() != null )
			{
				try {
					HttpSession session = request.getSession(true);
		
					if(request.getParameter("commissionId")!=null)
						session.setAttribute("commissionId", request.getParameter("commissionId"));
					
					List<CommissionParticipation> CommParticList = commissionService.getAllCommissionParticipation(Long.valueOf(request.getSession(true).getAttribute("commissionId").toString()));
					// Create Tree for display
					List<List<TreeNode>> CommParticTreeList = this.commissionService.getCommissionParticipationTreeList( CommParticList, true, parentCommissionParticipation );				
					context.put("CommParticList", CommParticTreeList);
					
					// Pulling COMMISSIONABLEPARTY
					ArrayList <CommissionableParty> commissionablePartyList= new ArrayList<CommissionableParty>(commissionService.getCommissionableParties(vu360AuthDetails.getCurrentDistributor().getId()));
					context.put("commissionablePartyList", commissionablePartyList);
				} catch (Exception e) {
					log.debug("exception", e);
				}
				
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
	}


	public ModelAndView addParticipation( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		Map<Object, Object> context = new HashMap<Object, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		CommissionParticipationForm commPartiesForm = (CommissionParticipationForm)command;
		HttpSession session = request.getSession(true);
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
		{
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
			if(vu360AuthDetails != null && vu360AuthDetails.getOriginalPrincipal() != null )
			{
			
				
				if(request.getParameter("fromSummaryPage")!=null )
				{			
					CommissionParticipationValidator validator = (CommissionParticipationValidator) this.getValidator();
					validator.validate(commPartiesForm, errors);
					if( errors.hasErrors() ) 
					{
						if(commPartiesForm.getParentCommissionParticipation()!=null && !commPartiesForm.getParentCommissionParticipation().equals("") )
							displaySummeryPage( request, context, Long.valueOf(commPartiesForm.getParentCommissionParticipation()));
						else
							displaySummeryPage( request, context, 0);
						
						return new ModelAndView(addParticipationTemplate, "context", context);
					}
					else
						return new ModelAndView(addParticipationConfirmationTemplate);
				} 
				else if(request.getParameter("prevtoSummaryPage") != null )
				{
					if(commPartiesForm.getParentCommissionParticipation()!=null && !commPartiesForm.getParentCommissionParticipation().equals("") )
						displaySummeryPage( request, context, Long.valueOf(commPartiesForm.getParentCommissionParticipation()));
					else
						displaySummeryPage( request, context, 0);
					
					return new ModelAndView(addParticipationTemplate, "context", context);
				}
					 
				else if(request.getParameter("fromConfirmationPage")!=null )
				{
					
					CommissionParticipation commParticipation = new CommissionParticipation();
					formToModel(commParticipation, commPartiesForm);

					Commission comm = new Commission();
					
					if(request.getSession(true).getAttribute("commissionId")!=null)
						comm.setId(Long.valueOf(session.getAttribute("commissionId").toString()));
					else
						return new ModelAndView("redirect:/adm_manageCommission.do?method=editResellerCommission&entity=reseller");
					
					commParticipation.setCommission(comm);
					
					if(commPartiesForm.getParentCommissionParticipation()!=null && !commPartiesForm.getParentCommissionParticipation().equals(""))
					{
						CommissionParticipation commissionPartyForRootId = null;
						commissionPartyForRootId = commissionService.loadForUpdateCommissionParticipation(commParticipation.getParentCommissionParticipation().getId());
						
						if(commissionPartyForRootId!=null && commissionPartyForRootId.getRootCommissionParticipation()!=null)
							// putting same root id as of its parent's root id because root id will be same in one tree [Hierarchy] 
							commParticipation.setRootCommissionParticipation(commissionPartyForRootId.getRootCommissionParticipation());
						else if(commissionPartyForRootId!=null && commissionPartyForRootId.getId() > 0)
						{
							// Root id and parent id will be same here 
							// because this is second level node and its parent has null in root and parent node
							CommissionParticipation commPartyForRootIdIfParentIsnull = new CommissionParticipation();
							commPartyForRootIdIfParentIsnull.setId(commissionPartyForRootId.getId());
							commParticipation.setRootCommissionParticipation(commPartyForRootIdIfParentIsnull);
						}	
						commParticipation = commissionService.saveCommissionParticipation(commParticipation);
						commissionPartyForRootId=null;
					}
					else{ // Root id and parent id will be null here because this is top level node 
						commParticipation = commissionService.saveCommissionParticipation(commParticipation);
					}
					
					commParticipation=null;
					
					commPartiesForm.reset();
					return new ModelAndView("redirect:/adm_manageCommission.do?method=editResellerCommission&entity=reseller");
				}
				
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
		// Dispaly 1st page When User click on 'Add Participation' button from 'Edit Commission' page.
		commPartiesForm.reset();
		displaySummeryPage( request, context,0);
		return new ModelAndView(addParticipationTemplate, "context", context);
	}
	
	
	public ModelAndView editParticipation( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		Map<Object, Object> context = new HashMap<Object, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		CommissionParticipationForm commPartiesForm = (CommissionParticipationForm)command;
		HttpSession session = request.getSession(true);
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
		{
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
			if(vu360AuthDetails != null && vu360AuthDetails.getOriginalPrincipal() != null )
			{
				if(request.getParameter("commparttyId")!=null)
					session.setAttribute("commparttyId", request.getParameter("commparttyId"));
				
				CommissionParticipation commissionParticipation = commissionService.loadForUpdateCommissionParticipation(Long.valueOf(session.getAttribute("commparttyId").toString()));
				displaySummeryPage( request, context, 0);
				populateForm(commissionParticipation, commPartiesForm);
				
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
		return new ModelAndView(editParticipationTemplate, "context", context);
	}
	
	
	public ModelAndView saveParticipation( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		CommissionParticipationForm commPartiesForm = (CommissionParticipationForm)command;
		HttpSession session = request.getSession(true);
		Map<Object, Object> context = new HashMap<Object, Object>();
		
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
		{
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
			if(vu360AuthDetails != null && vu360AuthDetails.getOriginalPrincipal() != null )
			{
				//if(session.getAttribute("commparttyId")!=null){
					CommissionParticipationValidator validator = (CommissionParticipationValidator) this.getValidator();
					validator.validate(commPartiesForm, errors);
					if( errors.hasErrors() ) 
					{
						displaySummeryPage( request, context, 0);
						return new ModelAndView(editParticipationTemplate, "context", context);
						
					}
					else{
						CommissionParticipation commissionParticipation = commissionService.loadForUpdateCommissionParticipation(Long.valueOf(session.getAttribute("commparttyId").toString()));
						formToModel(commissionParticipation,commPartiesForm);
						commissionParticipation = commissionService.saveCommissionParticipation(commissionParticipation);
						commissionParticipation=null;
					}
					
				//}else{	return new ModelAndView("redirect:/adm_manageCommission.do?method=showCommissions&entity=reseller"); }
				
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		
		return new ModelAndView("redirect:/adm_manageCommission.do?method=editResellerCommission&entity=reseller");
	}
	
	
	
	
	
	
	private void populateForm (CommissionParticipation commParty, CommissionParticipationForm commissionForm)
	{
		commissionForm.setFlat(commParty.getFlat().toString());
		if(commParty.getFlat())
			commissionForm.setFlatFeeAmount(commParty.getFlatFeeAmount() + "");
		else
			commissionForm.setPercentage(commParty.getPercentage() + "");
	
		commissionForm.setCommissionablePartyId(commParty.getCommissionableParty().getId() + "");
	}
	
	
	private void formToModel (CommissionParticipation commParty, CommissionParticipationForm commissionForm)
	{
		commParty.setFlat(Boolean.parseBoolean(commissionForm.getFlat()));
		if(commParty.getFlat()){
			commParty.setFlatFeeAmount(Double.parseDouble(commissionForm.getFlatFeeAmount()));
			commParty.setPercentage(0);
		}
		else{
			commParty.setPercentage(Double.parseDouble(commissionForm.getPercentage()));
			commParty.setFlatFeeAmount(0);
		}
		

		CommissionParticipation parent = new CommissionParticipation();
		if(commissionForm.getParentCommissionParticipation()!=null && !commissionForm.getParentCommissionParticipation().equals(""))
		{
			parent.setId(Long.valueOf(commissionForm.getParentCommissionParticipation()));
			commParty.setParentCommissionParticipation(parent);
		}
		

		CommissionableParty commparty = new CommissionableParty();
		commparty.setId(Long.valueOf(commissionForm.getCommissionablePartyId()));
		commParty.setCommissionableParty(commparty);
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
	}
	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
	}
	public CommissionService getCommissionService() {
		return commissionService;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}

	public String getAddParticipationTemplate() {
		return addParticipationTemplate;
	}

	public void setAddParticipationTemplate(String addParticipationTemplate) {
		this.addParticipationTemplate = addParticipationTemplate;
	}
	
	
	public String getAddParticipationConfirmationTemplate() {
		return addParticipationConfirmationTemplate;
	}


	public void setAddParticipationConfirmationTemplate(
			String addParticipationConfirmationTemplate) {
		this.addParticipationConfirmationTemplate = addParticipationConfirmationTemplate;
	}


	public String getEditParticipationTemplate() {
		return editParticipationTemplate;
	}


	public void setEditParticipationTemplate(String editParticipationTemplate) {
		this.editParticipationTemplate = editParticipationTemplate;
	}
	
}
