package com.softech.vu360.lms.web.controller.accreditation;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.Address;
import com.softech.vu360.lms.model.Contact;
import com.softech.vu360.lms.model.Regulator;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.web.controller.AbstractWizardFormController;
import com.softech.vu360.lms.web.controller.model.accreditation.RegulatorForm;
import com.softech.vu360.lms.web.controller.validator.Accreditation.AddContactInRegulatorValidator;
import com.softech.vu360.util.VU360Branding;

/**
 * @author Dyutiman
 * created on 24-june-2009
 *
 */
public class AddContactInRegulator extends AbstractWizardFormController {

	private static final Logger log = Logger.getLogger(AddContactInRegulator.class.getName());

	private AccreditationService accreditationService = null;
	private String finishTemplate = null;

	public AddContactInRegulator() {
		super();
		setCommandName("regulatorForm");
		setCommandClass(RegulatorForm.class);
		setSessionForm(true);
		this.setBindOnNewForm(true);
		setPages(new String[] {
				"accreditation/regulator/editRegulator/edit_regulator_add_contactStep1"
				, "accreditation/regulator/editRegulator/edit_regulator_add_contactStep2"
		});
	}

	protected Object formBackingObject( HttpServletRequest request ) throws Exception {

		Object command = super.formBackingObject(request);
		try {
			RegulatorForm form = (RegulatorForm) command;
			String id = request.getParameter("id");
			Regulator regulator = accreditationService.loadForUpdateRegulator(Long.parseLong(id));
			form.setRegulator(regulator);
			Contact contact = new Contact();
			Address address= new Address();
			Address address2= new Address();
			contact.setAddress(address);
			contact.setAddress2(address2);
			/*List<Contact> contacts = new ArrayList <Contact>();
			contacts.add(contact);*/
			form.setContact(contact);
			form.setBrander(VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language()));

		} catch (Exception e) {
			log.debug("exception", e);
		}
		return command;
	}

	@SuppressWarnings("unchecked")
	protected Map<Object, Object> referenceData(HttpServletRequest request, Object command, Errors errors,
			int page) throws Exception {

		switch( page ) {

		case 0:
			break;
		case 1:
			break;
		default:
			break;
		}
		return super.referenceData(request, page);
	}

	protected ModelAndView processFinish( HttpServletRequest request,
			HttpServletResponse responce, Object command, BindException errors)	throws Exception {
		try {
			log.debug("IN processFinish");
			RegulatorForm form = (RegulatorForm) command;
			Regulator regulator = form.getRegulator();
			Contact newContact = form.getContact();
			newContact.setFirstName(newContact.getFirstName().trim());
			newContact.setLastName(newContact.getLastName().trim());
			newContact.setPhone(newContact.getPhone().trim());
			newContact.setTitle(newContact.getTitle().trim());
			newContact.getAddress().setStreetAddress(newContact.getAddress().getStreetAddress().trim());
			newContact.getAddress().setStreetAddress2(newContact.getAddress().getStreetAddress2().trim());
			newContact.getAddress().setCity(newContact.getAddress().getCity().trim());
			newContact.getAddress().setZipcode(newContact.getAddress().getZipcode().trim());
			newContact.setRegulator(regulator);
			accreditationService.saveContact(newContact);
		} catch (Exception e) {
			log.debug(e);
		}

		log.debug("IN processFinish");
		RegulatorForm form = (RegulatorForm) command;
		Regulator regulator = form.getRegulator();
		Contact newContact = form.getContact();
		newContact.setRegulator(regulator);
		accreditationService.saveContact(newContact);


		return new ModelAndView(finishTemplate);
	}

	protected void validatePage(Object command, Errors errors, int page) {

		AddContactInRegulatorValidator validator = (AddContactInRegulatorValidator)this.getValidator();
		RegulatorForm form = (RegulatorForm)command;
		switch(page) {
		case 0:
			if( form.getEventSource().equalsIgnoreCase("true") ) {
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
			HttpServletResponse response, Object command, BindException errors)	throws Exception {

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