 /**
 * 
 */
package com.softech.vu360.lms.web.controller.administrator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CommissionParticipation;
import com.softech.vu360.lms.service.CommissionService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * @author muhammad.Rehan
 *
 */
public class ManageCommissionParticipationController extends VU360BaseMultiActionController {

	private String commissionProductListTemplate= null;
	private CommissionService commissionService;
	private static final Logger log = Logger.getLogger(ManageCustomFieldsController.class.getName());
	

	public ModelAndView deleteCommissionParticipation( HttpServletRequest request, HttpServletResponse response, Object command, BindException errors ) throws Exception {		
		try {
			
			HttpSession session = request.getSession(true);
			String[] fieldsToDelete=request.getParameterValues("commParticipation");
			
			if  ( fieldsToDelete!=null && fieldsToDelete.length>0) {
			
				if(validateDeleteData(fieldsToDelete, request))
				{
					Long[] ids = new Long[fieldsToDelete.length];
					for ( int i = 0 ; i < fieldsToDelete.length ; i++ ) {
						ids[i] = Long.valueOf(fieldsToDelete[i]);				 
					}			
					commissionService.deleteCommissionParticipation(ids);
					session.setAttribute("validateCommPar",null);
				}
				else
				{
					session.setAttribute("validateCommPar", "error.resellerCommission.childParticipation.exists");
				}
			}
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();				
			
			if( auth.getDetails()!= null && auth.getDetails() instanceof VU360UserAuthenticationDetails ){
				VU360UserAuthenticationDetails details = (VU360UserAuthenticationDetails)auth.getDetails();	
				VU360UserAuthenticationDetails vu360AuthDetails = (VU360UserAuthenticationDetails)auth.getDetails();											
				if( vu360AuthDetails.getCurrentDistributor() == null ) 											
					throw new IllegalArgumentException("Distributor not found");
			}else{
				throw new IllegalArgumentException("Distributor not found");
			}
		
		}catch(Exception e){
			log.debug("exception", e);
		}
		
		return new ModelAndView("redirect:adm_manageCommission.do?method=editResellerCommission&entity=reseller");
	}



	@Override
	protected void onBind(HttpServletRequest request, Object command,
			String methodName) throws Exception {
	}
	@Override
	protected void validate(HttpServletRequest request, Object command,
			BindException errors, String methodName) throws Exception {
	}



	public String getCommissionProductListTemplate() {
		return commissionProductListTemplate;
	}

	public void setCommissionProductListTemplate(
			String commissionProductListTemplate) {
		this.commissionProductListTemplate = commissionProductListTemplate;
	}

	public CommissionService getCommissionService() {
		return commissionService;
	}

	public void setCommissionService(CommissionService commissionService) {
		this.commissionService = commissionService;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private boolean validateDeleteData(String orgGroupIdArray[], HttpServletRequest request){

		boolean bRet=true;

		List<CommissionParticipation> commissionParticipations =	commissionService.getCommissionParticipationByIds(orgGroupIdArray);
		for(CommissionParticipation commissionParticipation: commissionParticipations){

			if(commissionParticipation.getChildrenCommissionParticipation() != null && commissionParticipation.getChildrenCommissionParticipation().size()>0){
				log.info("Children of Commission Participation size  = " + commissionParticipation.getChildrenCommissionParticipation().size());
				bRet= false;
				break;
			}
			
		}
		return bRet;
	}
}
