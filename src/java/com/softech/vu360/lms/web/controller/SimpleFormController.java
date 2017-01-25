package com.softech.vu360.lms.web.controller;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class SimpleFormController extends AbstractFormController {

	private String formView;

	private String successView;


	/**
	 * Create a new SimpleFormController.
	 * <p>Subclasses should set the following properties, either in the constructor
	 * or via a BeanFactory: commandName, commandClass, sessionForm, formView,
	 * successView. Note that commandClass doesn't need to be set when overriding
	 * {@code formBackingObject}, as this determines the class anyway.
	 * @see #setCommandClass
	 * @see #setCommandName
	 * @see #setSessionForm
	 * @see #setFormView
	 * @see #setSuccessView
	 * @see #formBackingObject
	 */
	public SimpleFormController() {
		// AbstractFormController sets default cache seconds to 0.
		super();
	}

	/**
	 * Set the name of the view that should be used for form display.
	 */
	public final void setFormView(String formView) {
		this.formView = formView;
	}

	/**
	 * Return the name of the view that should be used for form display.
	 */
	public final String getFormView() {
		return this.formView;
	}

	/**
	 * Set the name of the view that should be shown on successful submit.
	 */
	public final void setSuccessView(String successView) {
		this.successView = successView;
	}

	/**
	 * Return the name of the view that should be shown on successful submit.
	 */
	public final String getSuccessView() {
		return this.successView;
	}


	/**
	 * This implementation shows the configured form view, delegating to the analogous
	 * {@link #showForm(HttpServletRequest, HttpServletResponse, BindException, Map)}
	 * variant with a "controlModel" argument.
	 * <p>Can be called within
	 * {@link #onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)}
	 * implementations, to redirect back to the form in case of custom validation errors
	 * (errors not determined by the validator).
	 * <p>Can be overridden in subclasses to show a custom view, writing directly
	 * to the response or preparing the response before rendering a view.
	 * <p>If calling showForm with a custom control model in subclasses, it's preferable
	 * to override the analogous showForm version with a controlModel argument
	 * (which will handle both standard form showing and custom form showing then).
	 * @see #setFormView
	 * @see #showForm(HttpServletRequest, HttpServletResponse, BindException, Map)
	 */
	@Override
	protected ModelAndView showForm(
			HttpServletRequest request, HttpServletResponse response, BindException errors)
			throws Exception {

		return showForm(request, response, errors, null);
	}

	/**
	 * This implementation shows the configured form view.
	 * <p>Can be called within
	 * {@link #onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)}
	 * implementations, to redirect back to the form in case of custom validation errors
	 * (errors not determined by the validator).
	 * <p>Can be overridden in subclasses to show a custom view, writing directly
	 * to the response or preparing the response before rendering a view.
	 * @param request current HTTP request
	 * @param errors validation errors holder
	 * @param controlModel model map containing controller-specific control data
	 * (e.g. current page in wizard-style controllers or special error message)
	 * @return the prepared form view
	 * @throws Exception in case of invalid state or arguments
	 * @see #setFormView
	 */
	protected ModelAndView showForm(
			HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel)
			throws Exception {

		return showForm(request, errors, getFormView(), controlModel);
	}

	/**
	 * Create a reference data map for the given request and command,
	 * consisting of bean name/bean instance pairs as expected by ModelAndView.
	 * <p>The default implementation delegates to {@link #referenceData(HttpServletRequest)}.
	 * Subclasses can override this to set reference data used in the view.
	 * @param request current HTTP request
	 * @param command form object with request parameters bound onto it
	 * @param errors validation errors holder
	 * @return a Map with reference data entries, or {@code null} if none
	 * @throws Exception in case of invalid state or arguments
	 * @see ModelAndView
	 */
	@Override
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		return referenceData(request);
	}

	/**
	 * Create a reference data map for the given request.
	 * Called by the {@link #referenceData(HttpServletRequest, Object, Errors)}
	 * variant with all parameters.
	 * <p>The default implementation returns {@code null}.
	 * Subclasses can override this to set reference data used in the view.
	 * @param request current HTTP request
	 * @return a Map with reference data entries, or {@code null} if none
	 * @throws Exception in case of invalid state or arguments
	 * @see #referenceData(HttpServletRequest, Object, Errors)
	 * @see ModelAndView
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		return null;
	}


	/**
	 * This implementation calls
	 * {@link #showForm(HttpServletRequest, HttpServletResponse, BindException)}
	 * in case of errors, and delegates to the full
	 * {@link #onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)}'s
	 * variant else.
	 * <p>This can only be overridden to check for an action that should be executed
	 * without respect to binding errors, like a cancel action. To just handle successful
	 * submissions without binding errors, override one of the {@code onSubmit}
	 * methods or {@link #doSubmitAction}.
	 * @see #showForm(HttpServletRequest, HttpServletResponse, BindException)
	 * @see #onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)
	 * @see #onSubmit(Object, BindException)
	 * @see #onSubmit(Object)
	 * @see #doSubmitAction(Object)
	 */
	@Override
	protected ModelAndView processFormSubmission(
			HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		if (errors.hasErrors()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Data binding errors: " + errors.getErrorCount());
			}
			return showForm(request, response, errors);
		}
		else if (isFormChangeRequest(request, command)) {
			logger.debug("Detected form change request -> routing request to onFormChange");
			onFormChange(request, response, command, errors);
			return showForm(request, response, errors);
		}
		else {
			logger.debug("No errors -> processing submit");
			return onSubmit(request, response, command, errors);
		}
	}

	/**
	 * This implementation delegates to {@link #isFormChangeRequest(HttpServletRequest, Object)}:
	 * A form change request changes the appearance of the form and should not get
	 * validated but just show the new form.
	 * @see #isFormChangeRequest
	 */
	@Override
	protected boolean suppressValidation(HttpServletRequest request, Object command) {
		return isFormChangeRequest(request, command);
	}

	/**
	 * Determine whether the given request is a form change request.
	 * A form change request changes the appearance of the form
	 * and should always show the new form, without validation.
	 * <p>Gets called by {@link #suppressValidation} and {@link #processFormSubmission}.
	 * Consequently, this single method determines to suppress validation
	 * <i>and</i> to show the form view in any case.
	 * <p>The default implementation delegates to
	 * {@link #isFormChangeRequest(javax.servlet.http.HttpServletRequest)}.
	 * @param request current HTTP request
	 * @param command form object with request parameters bound onto it
	 * @return whether the given request is a form change request
	 * @see #suppressValidation
	 * @see #processFormSubmission
	 */
	protected boolean isFormChangeRequest(HttpServletRequest request, Object command) {
		return isFormChangeRequest(request);
	}

	/**
	 * Simpler {@code isFormChangeRequest} variant, called by the full
	 * variant {@link #isFormChangeRequest(HttpServletRequest, Object)}.
	 * <p>The default implementation returns {@code false}.
	 * @param request current HTTP request
	 * @return whether the given request is a form change request
	 * @see #suppressValidation
	 * @see #processFormSubmission
	 */
	protected boolean isFormChangeRequest(HttpServletRequest request) {
		return false;
	}

	/**
	 * Called during form submission if
	 * {@link #isFormChangeRequest(javax.servlet.http.HttpServletRequest)}
	 * returns {@code true}. Allows subclasses to implement custom logic
	 * to modify the command object to directly modify data in the form.
	 * <p>The default implementation delegates to
	 * {@link #onFormChange(HttpServletRequest, HttpServletResponse, Object, BindException)}.
	 * @param request current servlet request
	 * @param response current servlet response
	 * @param command form object with request parameters bound onto it
	 * @param errors validation errors holder, allowing for additional
	 * custom validation
	 * @throws Exception in case of errors
	 * @see #isFormChangeRequest(HttpServletRequest)
	 * @see #onFormChange(HttpServletRequest, HttpServletResponse, Object)
	 */
	protected void onFormChange(
			HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		onFormChange(request, response, command);
	}

	/**
	 * Simpler {@code onFormChange} variant, called by the full variant
	 * {@link #onFormChange(HttpServletRequest, HttpServletResponse, Object, BindException)}.
	 * <p>The default implementation is empty.
	 * @param request current servlet request
	 * @param response current servlet response
	 * @param command form object with request parameters bound onto it
	 * @throws Exception in case of errors
	 * @see #onFormChange(HttpServletRequest, HttpServletResponse, Object, BindException)
	 */
	protected void onFormChange(HttpServletRequest request, HttpServletResponse response, Object command)
			throws Exception {
	}


	/**
	 * Submit callback with all parameters. Called in case of submit without errors
	 * reported by the registered validator, or on every submit if no validator.
	 * <p>The default implementation delegates to {@link #onSubmit(Object, BindException)}.
	 * For simply performing a submit action and rendering the specified success
	 * view, consider implementing {@link #doSubmitAction} rather than an
	 * {@code onSubmit} variant.
	 * <p>Subclasses can override this to provide custom submission handling like storing
	 * the object to the database. Implementations can also perform custom validation and
	 * call showForm to return to the form. Do <i>not</i> implement multiple onSubmit
	 * methods: In that case, just this method will be called by the controller.
	 * <p>Call {@code errors.getModel()} to populate the ModelAndView model
	 * with the command and the Errors instance, under the specified command name,
	 * as expected by the "spring:bind" tag.
	 * @param request current servlet request
	 * @param response current servlet response
	 * @param command form object with request parameters bound onto it
	 * @param errors Errors instance without errors (subclass can add errors if it wants to)
	 * @return the prepared model and view, or {@code null}
	 * @throws Exception in case of errors
	 * @see #onSubmit(Object, BindException)
	 * @see #doSubmitAction
	 * @see #showForm
	 * @see org.springframework.validation.Errors
	 * @see org.springframework.validation.BindException#getModel
	 */
	protected ModelAndView onSubmit(
			HttpServletRequest request,	HttpServletResponse response, Object command,	BindException errors)
			throws Exception {

		return onSubmit(command, errors);
	}

	/**
	 * Simpler {@code onSubmit} variant.
	 * Called by the default implementation of the
	 * {@link #onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)}
	 * variant with all parameters.
	 * <p>The default implementation calls {@link #onSubmit(Object)}, using the
	 * returned ModelAndView if actually implemented in a subclass. Else, the
	 * default behavior will apply: rendering the success view with the command
	 * and Errors instance as model.
	 * <p>Subclasses can override this to provide custom submission handling that
	 * does not need request and response.
	 * <p>Call {@code errors.getModel()} to populate the ModelAndView model
	 * with the command and the Errors instance, under the specified command name,
	 * as expected by the "spring:bind" tag.
	 * @param command form object with request parameters bound onto it
	 * @param errors Errors instance without errors
	 * @return the prepared model and view
	 * @throws Exception in case of errors
	 * @see #onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)
	 * @see #onSubmit(Object)
	 * @see #setSuccessView
	 * @see org.springframework.validation.Errors
	 * @see org.springframework.validation.BindException#getModel
	 */
	protected ModelAndView onSubmit(Object command, BindException errors) throws Exception {
		ModelAndView mv = onSubmit(command);
		if (mv != null) {
			// simplest onSubmit variant implemented in custom subclass
			return mv;
		}
		else {
			// default behavior: render success view
			if (getSuccessView() == null) {
				throw new ServletException("successView isn't set");
			}
			return new ModelAndView(getSuccessView(), errors.getModel());
		}
	}

	/**
	 * Simplest {@code onSubmit} variant. Called by the default implementation
	 * of the {@link #onSubmit(Object, BindException)} variant.
	 * <p>This implementation calls {@link #doSubmitAction(Object)} and returns
	 * {@code null} as ModelAndView, making the calling {@code onSubmit}
	 * method perform its default rendering of the success view.
	 * <p>Subclasses can override this to provide custom submission handling
	 * that just depends on the command object. It's preferable to use either
	 * {@link #onSubmit(Object, BindException)} or {@link #doSubmitAction(Object)},
	 * though: Use the former when you want to build your own ModelAndView; use the
	 * latter when you want to perform an action and forward to the successView.
	 * @param command form object with request parameters bound onto it
	 * @return the prepared model and view, or {@code null} for default
	 * (that is, rendering the configured "successView")
	 * @throws Exception in case of errors
	 * @see #onSubmit(Object, BindException)
	 * @see #doSubmitAction
	 * @see #setSuccessView
	 */
	protected ModelAndView onSubmit(Object command) throws Exception {
		doSubmitAction(command);
		return null;
	}

	/**
	 * Template method for submit actions. Called by the default implementation
	 * of the simplest {@link #onSubmit(Object)} variant.
	 * <p><b>This is the preferred submit callback to implement if you want to
	 * perform an action (like storing changes to the database) and then render
	 * the success view with the command and Errors instance as model.</b>
	 * You don't need to care about the success ModelAndView here.
	 * @param command form object with request parameters bound onto it
	 * @throws Exception in case of errors
	 * @see #onSubmit(Object)
	 * @see #setSuccessView
	 */
	protected void doSubmitAction(Object command) throws Exception {
	}

}
