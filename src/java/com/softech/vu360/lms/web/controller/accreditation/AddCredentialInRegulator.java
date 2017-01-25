package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Credential;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorCredential;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddCredentialInRegulatorValidator;
import com.softech.vu360.util.CredentialSort;
import com.softech.vu360.util.HtmlEncoder;

/**
 * @author Dyutiman
 * created on 23-june-2009
 *
 */
public class AddCredentialInRegulator extends AbstractWizardFormController{

	private static final Logger log = Logger.getLogger(AddCredentialInRegulator.class.getName());
	HttpSession session = null;

	private AccreditationService accreditationService = null;
	private String finishTemplate = null;

	public AddCredentialInRegulator() {
		super();
		setCommandName("regulatorForm");
		setCommandClass(RegulatorForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/regulator/editRegulator/edit_regulator_add_credentialStep1"
				, "accreditation/regulator/editRegulator/edit_regulator_add_credentialStep2"
		});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			RegulatorForm form = (RegulatorForm) command;
			String id = request.getParameter("id");
			Regulator regulator = accreditationService.loadForUpdateRegulator(Long.parseLong(id));
			form.setRegulator(regulator);
			com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
			getPrincipal();
			String credentialName = request.getParameter("credentialName");
			String credentialShortName = request.getParameter("credentialShortName");
			credentialName = StringUtils.isBlank(credentialName)? "" : credentialName.trim();
			credentialShortName = StringUtils.isBlank(credentialShortName)? "" : credentialShortName.trim();

			List<Credential> searchedCredentials = accreditationService.findCredential(credentialName, 
					credentialShortName, loggedInUser.getRegulatoryAnalyst());
			
			credentialName = HtmlEncoder.escapeHtmlFull(credentialName).toString();
			credentialShortName = HtmlEncoder.escapeHtmlFull(credentialShortName).toString();
			
			List<RegulatorCredential> regCredentials = new ArrayList <RegulatorCredential>();
			for( Credential cred : searchedCredentials ) {
				RegulatorCredential regCredential = new RegulatorCredential();
				regCredential.setCredential(cred);
				regCredential.setSelected(false);
				regCredentials.add(regCredential);
			}
			//form.setRegCredential(regCredentials);
		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		session = request.getSession();
		RegulatorForm form = (RegulatorForm) command;
		Map <Object, Object>model = new HashMap <Object, Object>();
		Map<String, String> PagerAttributeMap = new HashMap <String, String>();
		com.softech.vu360.lms.vo.VU360User loggedInUser = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().
		getPrincipal();
		String credentialName = request.getParameter("credentialName");
		String credentialShortName = request.getParameter("credentialShortName");
		credentialName = StringUtils.isBlank(credentialName)? "" : credentialName.trim();
		credentialShortName = StringUtils.isBlank(credentialShortName)? "" : credentialShortName.trim();

		switch(page){

		case 0:
			List<Credential> searchedCredentials = accreditationService.findCredential(credentialName, 
					credentialShortName, loggedInUser.getRegulatoryAnalyst());
			
			credentialName = HtmlEncoder.escapeHtmlFull(credentialName).toString();
			credentialShortName = HtmlEncoder.escapeHtmlFull(credentialShortName).toString();
			
			List<RegulatorCredential> regCredentials = new ArrayList <RegulatorCredential>();

			// for sorting...
			String sortColumnIndex = request.getParameter("sortColumnIndex");
			if( sortColumnIndex == null && session.getAttribute("sortColumnIndex") != null )
				sortColumnIndex = session.getAttribute("sortColumnIndex").toString();
			String sortDirection = request.getParameter("sortDirection");
			if( sortDirection == null && session.getAttribute("sortDirection") != null )
				sortDirection = session.getAttribute("sortDirection").toString();
			String showAll = request.getParameter("showAll");
			if ( showAll == null ) showAll = "false";
			if ( showAll.isEmpty() ) showAll = "true";
			model.put("showAll", showAll);
			String pageIndex = request.getParameter("pageCurrIndex");
			if( StringUtils.isBlank(pageIndex)) pageIndex = "0";
			PagerAttributeMap.put("pageIndex", pageIndex);

			if( sortColumnIndex != null && sortDirection != null ) {

				request.setAttribute("PagerAttributeMap", PagerAttributeMap);

				// sorting against officialLicenseName
				if( sortColumnIndex.equalsIgnoreCase("0") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CredentialSort credentialSort = new CredentialSort();
						credentialSort.setSortBy("officialLicenseName");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCredentials,credentialSort);
						model.put("sortDirection", 0);
						model.put("sortColumnIndex", 0);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 0);
					} else {
						CredentialSort credentialSort = new CredentialSort();
						credentialSort.setSortBy("officialLicenseName");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCredentials,credentialSort);
						model.put("sortDirection", 1);
						model.put("sortColumnIndex", 0);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 0);
					}
					// sorting against shortLicenseName
				} else if( sortColumnIndex.equalsIgnoreCase("1") ) {
					if( sortDirection.equalsIgnoreCase("0") ) {

						CredentialSort credentialSort = new CredentialSort();
						credentialSort.setSortBy("shortLicenseName");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCredentials,credentialSort);
						model.put("sortDirection", 0);
						model.put("sortColumnIndex", 1);
						session.setAttribute("sortDirection", 0);
						session.setAttribute("sortColumnIndex", 1);
					} else {
						CredentialSort credentialSort = new CredentialSort();
						credentialSort.setSortBy("shortLicenseName");
						credentialSort.setSortDirection(Integer.parseInt(sortDirection));
						Collections.sort(searchedCredentials,credentialSort);
						model.put("sortDirection", 1);
						model.put("sortColumnIndex", 1);
						session.setAttribute("sortDirection", 1);
						session.setAttribute("sortColumnIndex", 1);
					}
				}
			}
			List<RegulatorCredential> presentCredentials = form.getRegCredential();

			for( Credential cred : searchedCredentials ) {
				boolean sel = false;
				for( RegulatorCredential regCred : presentCredentials ) {
					if( cred.getId().compareTo(regCred.getCredential().getId()) == 0 ) {
						sel = regCred.isSelected();
					}
				}
				RegulatorCredential regCredential = new RegulatorCredential();
				regCredential.setCredential(cred);
				regCredential.setSelected(sel);
				regCredentials.add(regCredential);
			}
			if( form.getShowRecords().equalsIgnoreCase("true") ) {
				form.setRegCredential(regCredentials);
			}
			return model;

		case 1:
			break;
		default:
			break;
		}
		return super.referenceData(request, page);
	}

	protected ModelAndView processFinish(HttpServletRequest request,
			HttpServletResponse responce, Object command, BindException errors)
	throws Exception {

		RegulatorForm form = (RegulatorForm) command;
		Regulator regulator = accreditationService.loadForUpdateRegulator(form.getRegulator().getId());
		
		List<Credential> existingCredentials=null;
		if(regulator.getCredentials()!=null ) {
			existingCredentials=regulator.getCredentials();
		}
		List<Credential> credsToBeAdded = new ArrayList <Credential>();
		List<Credential> selectedCredentials = new ArrayList <Credential>();
		//credsToBeAdded.addAll(regulator.getCredentials());
		
		List<RegulatorCredential> regCredentials = form.getRegCredential();
		for( RegulatorCredential regCred : regCredentials ) {
			if( regCred.isSelected() ) {
				selectedCredentials.add(regCred.getCredential());
			}
		}
		if(existingCredentials!=null && existingCredentials.size()>0) {
			for(Credential credential:selectedCredentials) {
				boolean found=true;
				for(Credential existingCredential:existingCredentials) {

					if(credential.getId().compareTo(existingCredential.getId())== 0) {
						found=false;
						break;
					}

				}
				if(found) {
					credsToBeAdded.add(credential);
				}
			}
		}else {
			credsToBeAdded.addAll(selectedCredentials);

		}


		if(credsToBeAdded.size()>0) {
			
			List<Credential> lstNewCredential = regulator.getCredentials();
			
			for(Credential credel: credsToBeAdded){
				lstNewCredential.add(credel);
			}
			
			regulator.setCredentials(lstNewCredential);
			accreditationService.saveRegulator(regulator);
		}
		return new ModelAndView(finishTemplate);
	}

	protected void validatePage(Object command, Errors errors, int page) {

		AddCredentialInRegulatorValidator validator = (AddCredentialInRegulatorValidator)this.getValidator();
		RegulatorForm form = (RegulatorForm)command;
		switch(page) {
		case 0:
			if( form.getShowAllAction().equalsIgnoreCase("false")) {
				validator.validateFirstPage(form, errors);
			}
			break;
		case 1:
			break;
		default:
			break;
		}
		errors.setNestedPath("");
	}

	protected ModelAndView processCancel(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		return new ModelAndView(finishTemplate);
	}

	public AccreditationService getAccreditationService() {
		return accreditationService;
	}

	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public String getFinishTemplate() {
		return finishTemplate;
	}

	public void setFinishTemplate(String finishTemplate) {
		this.finishTemplate = finishTemplate;
	}
}