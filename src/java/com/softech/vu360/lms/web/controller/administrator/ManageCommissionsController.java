/**
 * 
 */
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

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Commission;
import com.softech.vu360.lms.model.CommissionAudit;
import com.softech.vu360.lms.model.CommissionParticipation;
import com.softech.vu360.lms.model.CommissionProduct;
import com.softech.vu360.lms.model.CommissionProductCategory;
import com.softech.vu360.lms.service.CommissionService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.comparator.CommissionComparator;
import com.softech.vu360.lms.web.controller.model.CommissionForm;
import com.softech.vu360.lms.web.controller.validator.CommissionValidator;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.TreeNode;

/**
 * @author muhammad.Rehan
 *
 */
public class ManageCommissionsController extends VU360BaseMultiActionController {

	private String editResellerCommissionsTemplate= null;
	private String showCommissionsFailureView = null;	
	private String showCommissionsSuccessView = null;	
	private String addCommissionWizardSummaryView = null;	
	private String addCommissionWizardConfirmView = null;	
	private CommissionService commissionService;
	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());
	public static final int COMMISSION_TYPE_PRODUCT = 0;
	public static final int COMMISSION_TYPE_CATEGORY = 1; 
	
	public ModelAndView showCommissions(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		try
		{
			//log.info(" ---------- START - showCommissions() :: " + this.getClass().getName() + " ---------- ");
		    request.getSession(true).setAttribute("feature", "LMS-ADM-0023");
		    
		    String showAll = request.getParameter("showAll");
			//log.info("showAll = " + showAll);
			showAll = (showAll == null || !showAll.equals("true")) ? "false" : "true";
			//log.info("showAll = " + showAll);
			int sortDirection = Integer.parseInt((request.getParameter("sortDirection") == null) ? "0" : request.getParameter("sortDirection"));
			//log.info("sortDirection = " + sortDirection);
			int sortColumnIndex = Integer.parseInt((request.getParameter("sortColumnIndex") == null) ? "0" : request.getParameter("sortColumnIndex"));
			//log.info("sortColumnIndex = " + sortColumnIndex);
			
			Map<Object, Object> context = new HashMap<Object, Object>();		
			context.put("title", "Administration Mode");				
			context.put("showAll", showAll);
			context.put("sortDirection", sortDirection);
			context.put("sortColumnIndex", sortColumnIndex);
	
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
			{
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();
				String currentSearchType = details.getCurrentSearchType().toString();
				if(currentSearchType.equalsIgnoreCase("distributor") ) 
				{
					VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
					if( vu360AuthDetails.getCurrentDistributor() != null )
					{ 
						ArrayList <Commission> commissionList = new ArrayList<Commission>(commissionService.getCommissions(details.getCurrentDistributor().getId()));
						//log.info("commissionList.size = " + commissionList.size());
						Collections.sort(commissionList, new CommissionComparator(sortDirection, sortColumnIndex));
						context.put("commissionList", commissionList);
						return new ModelAndView(this.getShowCommissionsSuccessView(), "context", context);			
					} 
					else
						throw new IllegalArgumentException("Distributor not found");
				}
				else
					log.error("currentSearchType = " + currentSearchType);
			}
			else
				log.error("auth.getDetails = " + auth.getDetails());
			return new ModelAndView(this.getShowCommissionsFailureView(), "isRedirect", "d");					
		}
		finally
		{
			log.info(" ---------- END - showCommissions() :: " + this.getClass().getName() + " ---------- ");
		}
	}	
	
	public ModelAndView addCommission (HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception 
	{
		try
		{
			//log.info(" ---------- START - addCommission() :: " + this.getClass().getName() + " ---------- ");
			request.getSession(true).setAttribute("feature", "LMS-ADM-0023");
			CommissionForm commissionForm = (CommissionForm)command;
			//log.info("commissionForm = " + commissionForm);
			if(request.getParameter("fromSummaryPage")!=null )
			{			
				commissionForm.setName(request.getParameter("name"));									
				CommissionValidator validator = (CommissionValidator) this.getValidator();
				validator.validate(commissionForm, errors);
				if( errors.hasErrors() ) 
					return new ModelAndView(this.getAddCommissionWizardSummaryView());			
				return new ModelAndView(this.getAddCommissionWizardConfirmView());
			} 
			else if(request.getParameter("prevtoSummaryPage") != null )
				return new ModelAndView(this.getAddCommissionWizardSummaryView()); 
			else if(request.getParameter("fromConfirmationPage")!=null )
			{
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
				{
					VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
					if( vu360AuthDetails.getCurrentDistributor() != null )
					{ 
						Commission commission = new Commission();
						formToModel(commission, commissionForm);
						//log.info("commission = " + commission);
						commission.setDistributor(vu360AuthDetails.getCurrentDistributor());
						commissionService.saveCommission(commission);
						CommissionAudit commissionAudit = new CommissionAudit();
						commissionAudit.setChangedOn(new Date(System.currentTimeMillis()));
						commissionAudit.setComments("CREATED");
						commissionAudit.setCommission(commission);
						commissionAudit.setNewValue(this.getValueForCommissionAudit(commission));
						commissionAudit.setOldValue("N/A");
						commissionAudit.setVu360User(VU360UserAuthenticationDetails.getCurrentUser());//TO DO: NEED TO CALL FIND BY ID FOR VU360USER 2016-09-04
						log.info("commissionAudit = " + commissionAudit);
						commissionService.logCommissionChangeForAudit(commissionAudit);
						return new ModelAndView("redirect:/adm_manageCommission.do?method=showCommissions&entity=reseller");
					 }
				}
			}
			commissionForm.reset();
			return new ModelAndView(this.getAddCommissionWizardSummaryView());
		}
		finally
		{
			log.info(" ---------- END - addCommission() :: " + this.getClass().getName() + " ---------- ");
		}
	}
	
	public ModelAndView deleteCommission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception 
	{	
		try
		{
			log.info(" ---------- START - deleteCommission() :: " + this.getClass().getName() + " ---------- ");
			request.getSession(true).setAttribute("feature", "LMS-ADM-0023");
			String[] fieldsToDelete = request.getParameterValues("commissionCheck");
			if(fieldsToDelete != null && fieldsToDelete.length > 0) 
			{
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
				if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
				{
					VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
					if(vu360AuthDetails != null && vu360AuthDetails.getOriginalPrincipal() != null)
					{
						Commission commission;
						CommissionAudit commissionAudit;
						for ( int i = 0 ; i < fieldsToDelete.length ; i++ )
						{
							commission = commissionService.loadForUpdateCommission(Long.valueOf(fieldsToDelete[i]));
							commission.setDeleted(true);
							commissionService.saveCommission(commission);
							commissionAudit = new CommissionAudit();
							commissionAudit.setChangedOn(new Date(System.currentTimeMillis()));
							commissionAudit.setComments("DELETED");
							commissionAudit.setCommission(commission);
							commissionAudit.setNewValue("N/A");
							commissionAudit.setOldValue("N/A");
							commissionAudit.setVu360User(VU360UserAuthenticationDetails.getCurrentUser());//TO DO: NEED TO CALL FIND BY ID FOR VU360USER 2016-09-04
							log.info("commissionAudit = " + commissionAudit);
							commissionService.logCommissionChangeForAudit(commissionAudit);
						}
					}
				}
			}
			return new ModelAndView("redirect:/adm_manageCommission.do?method=showCommissions&entity=reseller");
		}
		finally
		{
			log.info(" ---------- END - deleteCommission() :: " + this.getClass().getName() + " ---------- ");
		}
	}

	
	private String getValueForCommissionAudit (Commission commission)
	{
		return (commission.getFlat() ? "$" : "") 
			+ (commission.getFlat() ? commission.getFlatFeeAmount() : commission.getPercentage()) 
			+ (commission.getFlat() ? "" : "%") 
			+ " of " + (commission.getType() == 0 ? "Commission Model" : "Wholesale Model");
	}
	
	private void formToModel (Commission commission, CommissionForm commissionForm)
	{
		commission.setApplyToAllProducts(Boolean.parseBoolean(commissionForm.getApplyToAllProducts()));
		commission.setDeleted(false);
		commission.setDefaults(false);
		commission.setFlat(Boolean.parseBoolean(commissionForm.getFlat()));
		if(commission.getFlat())
			commission.setFlatFeeAmount(Double.parseDouble(commissionForm.getFlatFeeAmount()));
		else
			commission.setPercentage(Double.parseDouble(commissionForm.getPercentage()));
		commission.setName(commissionForm.getName());
		commission.setPayOnNetIncome(Boolean.parseBoolean(commissionForm.getPayOnNetIncome()));
		commission.setType(Integer.parseInt(commissionForm.getType()));
		commission.setCommissionType(Integer.parseInt(commissionForm.getCommissionType()));
		if(commissionForm.getPaymentMode()!=null)
			commission.setPaymentMode(Boolean.parseBoolean(commissionForm.getPaymentMode()));
	}

	public ModelAndView editResellerCommission( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		Map<Object, Object> context = new HashMap<Object, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
		
		if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
		{
			VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();
			if(vu360AuthDetails != null && vu360AuthDetails.getOriginalPrincipal() != null)
			{
			
				try {
					HttpSession session = request.getSession(true);
		
					if(request.getParameter("commissionId")!=null)
						session.setAttribute("commissionId", request.getParameter("commissionId"));
					
					populateForm(command, (String)session.getAttribute("commissionId"));
					
					
					CommissionForm formbean = (CommissionForm)command;
					if(formbean !=null && formbean.getCommissionType() != null && !formbean.getCommissionType().equals("") && Integer.parseInt(formbean.getCommissionType())==COMMISSION_TYPE_PRODUCT)
						session.setAttribute("commissionType", "product");
					else
						session.setAttribute("commissionType", "category");
					
					
					List<CommissionParticipation> CommParticList = commissionService.getAllCommissionParticipation(Long.valueOf(request.getSession(true).getAttribute("commissionId").toString()));
					// Create Tree for display
					List<List<TreeNode>> CommParticTreeList = this.commissionService.getCommissionParticipationTreeList( CommParticList, true,0 );				
					context.put("CommParticList", CommParticTreeList);
				} catch (Exception e) {
					log.debug("exception", e);
				}
				
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		}else{
			throw new IllegalArgumentException("Distributor not found");
		}
		return new ModelAndView(editResellerCommissionsTemplate, "context", context);	
	}
	
	
	public ModelAndView updateResellerCommission( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		try {
			CommissionForm form = (CommissionForm)command;
			CommissionValidator validator =(CommissionValidator)this.getValidator();
			validator.validate(form, errors);
			if( errors.hasErrors()) 
				return new ModelAndView(editResellerCommissionsTemplate);	
			
			Long varCommissionId = Long.valueOf(request.getSession(true).getAttribute("commissionId").toString());
			Commission formDB = commissionService.loadForUpdateCommission(varCommissionId);
			String oldValue = this.getValueForCommissionAudit(formDB);

			// Delete Product and Product Category if 'Apply to all Product'=yes   ----          Start
			if(Boolean.parseBoolean(form.getApplyToAllProducts()))
			{
				List<CommissionProduct> lstCP  = commissionService.getCommissionProduct(varCommissionId);
				Long[] deleteIds  = new Long[lstCP.size()];
				
				if(lstCP != null && lstCP.size()>0)
				{
					//System.out.println("=============================="+ lstCP.size() +"=============================");
					
					for(int i=0;i<lstCP.size();i++){
						CommissionProduct cp = (CommissionProduct)lstCP.get(i);
						deleteIds[i]=cp.getId();
					}
					
				}
				
				List<CommissionProductCategory> lstCPC  = commissionService.getCommissionProductCategory(varCommissionId);
				Long[] deleteCPCIds  = new Long[lstCPC.size()];
				
				if(lstCPC != null && lstCPC.size()>0)
				{
					for(int i=0;i<lstCPC.size();i++){
						CommissionProductCategory cp = (CommissionProductCategory)lstCPC.get(i);
						deleteCPCIds[i]=cp.getId();
					}
					
				}
				
				commissionService.deleteCommissionProduct(deleteIds); 
				commissionService.deleteCommissionProductCategory(deleteCPCIds); 
			}
			// Delete Product and Product Category if 'Apply to all Product'=yes   ----    End
			
			formToModel(formDB, form);
			commissionService.saveCommission(formDB);
			
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails )
			{
				VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
				if( vu360AuthDetails.getOriginalPrincipal() != null )
				{ 
					CommissionAudit commissionAudit = new CommissionAudit();
					commissionAudit.setChangedOn(new Date(System.currentTimeMillis()));
					commissionAudit.setComments("UPDATED");
					commissionAudit.setCommission(formDB);
					commissionAudit.setNewValue(this.getValueForCommissionAudit(formDB));
					commissionAudit.setOldValue(oldValue);
					commissionAudit.setVu360User(VU360UserAuthenticationDetails.getCurrentUser());//TO DO: NEED TO CALL FIND BY ID FOR VU360USER 2016-09-04
					log.info("commissionAudit = " + commissionAudit);
					commissionService.logCommissionChangeForAudit(commissionAudit);
				}
			}
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return new ModelAndView("redirect:/adm_manageCommission.do?method=showCommissions&entity=reseller");

	}

	public String getEditResellerCommissionsTemplate() {
		return editResellerCommissionsTemplate;
	}

	public void setEditResellerCommissionsTemplate(
			String editResellerCommissionsTemplate) {
		this.editResellerCommissionsTemplate = editResellerCommissionsTemplate;
	}

	public CommissionService getCommissionService() {
		return commissionService;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}
	
	public void populateForm(Object command, String commissionId)
	{
		Commission formDB = commissionService.loadForUpdateCommission(Long.valueOf(commissionId));
		CommissionForm formbean = (CommissionForm)command;
		formbean.setId("" + formDB.getId());
		formbean.setName(formDB.getName());
		formbean.setApplyToAllProducts(formDB.getApplyToAllProducts().toString());
		formbean.setPayOnNetIncome(formDB.getPayOnNetIncome().toString());
		
		formbean.setFlat(formDB.getFlat().toString());
		if(formDB.getFlat())
			formbean.setFlatFeeAmount(formDB.getFlatFeeAmount().toString());
		else
			formbean.setPercentage(formDB.getPercentage().toString());
		
		formbean.setType(formDB.getType().toString());
		formbean.setCommissionType(formDB.getCommissionType().toString());
		if(formDB.getPaymentMode()!=null )
			formbean.setPaymentMode(formDB.getPaymentMode().toString());
	}

	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public String getShowCommissionsFailureView() {
		return showCommissionsFailureView;
	}
	
	public void setShowCommissionsFailureView(String showCommissionsFailureView) {
		this.showCommissionsFailureView = showCommissionsFailureView;
	}
	
	public String getShowCommissionsSuccessView() {
		return showCommissionsSuccessView;
	}
	
	public void setShowCommissionsSuccessView(String showCommissionsSuccessView) {
		this.showCommissionsSuccessView = showCommissionsSuccessView;
	}
	
	public String getAddCommissionWizardSummaryView() {
		return addCommissionWizardSummaryView;
	}
	
	public void setAddCommissionWizardSummaryView(
			String addCommissionWizardSummaryView) {
		this.addCommissionWizardSummaryView = addCommissionWizardSummaryView;
	}
	
	public String getAddCommissionWizardConfirmView() {
		return addCommissionWizardConfirmView;
	}
	
	public void setAddCommissionWizardConfirmView(
			String addCommissionWizardConfirmView) {
		this.addCommissionWizardConfirmView = addCommissionWizardConfirmView;
	}
}
