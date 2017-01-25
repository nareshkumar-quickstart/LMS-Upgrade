package com.softech.vu360.lms.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingErrorProcessor;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.AbstractController;


public abstract class BaseCommandController extends AbstractController {

	/** Default command name used for binding command objects: "command" */
	public static final String DEFAULT_COMMAND_NAME = "command";


	private String commandName = DEFAULT_COMMAND_NAME;

	private Class commandClass;

	private Validator[] validators;

	private boolean validateOnBinding = true;

	private MessageCodesResolver messageCodesResolver;

	private BindingErrorProcessor bindingErrorProcessor;

	private PropertyEditorRegistrar[] propertyEditorRegistrars;

	private WebBindingInitializer webBindingInitializer;


	/**
	 * Set the name of the command in the model.
	 * The command object will be included in the model under this name.
	 */
	public final void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	/**
	 * Return the name of the command in the model.
	 */
	public final String getCommandName() {
		return this.commandName;
	}

	/**
	 * Set the command class for this controller.
	 * An instance of this class gets populated and validated on each request.
	 */
	public final void setCommandClass(Class commandClass) {
		this.commandClass = commandClass;
	}

	/**
	 * Return the command class for this controller.
	 */
	public final Class getCommandClass() {
		return this.commandClass;
	}

	/**
	 * Set the primary Validator for this controller. The Validator
	 * must support the specified command class. If there are one
	 * or more existing validators set already when this method is
	 * called, only the specified validator will be kept. Use
	 * {@link #setValidators(Validator[])} to set multiple validators.
	 */
	public final void setValidator(Validator validator) {
		this.validators = new Validator[] {validator};
	}

	/**
	 * Return the primary Validator for this controller.
	 */
	public final Validator getValidator() {
		return (this.validators != null && this.validators.length > 0 ? this.validators[0] : null);
	}

	/**
	 * Set the Validators for this controller.
	 * The Validator must support the specified command class.
	 */
	public final void setValidators(Validator[] validators) {
		this.validators = validators;
	}

	/**
	 * Return the Validators for this controller.
	 */
	public final Validator[] getValidators() {
		return this.validators;
	}

	/**
	 * Set if the Validator should get applied when binding.
	 */
	public final void setValidateOnBinding(boolean validateOnBinding) {
		this.validateOnBinding = validateOnBinding;
	}

	/**
	 * Return if the Validator should get applied when binding.
	 */
	public final boolean isValidateOnBinding() {
		return this.validateOnBinding;
	}

	/**
	 * Set the strategy to use for resolving errors into message codes.
	 * Applies the given strategy to all data binders used by this controller.
	 * <p>Default is {@code null}, i.e. using the default strategy of
	 * the data binder.
	 * @see #createBinder
	 * @see org.springframework.validation.DataBinder#setMessageCodesResolver
	 */
	public final void setMessageCodesResolver(MessageCodesResolver messageCodesResolver) {
		this.messageCodesResolver = messageCodesResolver;
	}

	/**
	 * Return the strategy to use for resolving errors into message codes (if any).
	 */
	public final MessageCodesResolver getMessageCodesResolver() {
		return this.messageCodesResolver;
	}

	/**
	 * Set the strategy to use for processing binding errors, that is,
	 * required field errors and {@code PropertyAccessException}s.
	 * <p>Default is {@code null}, that is, using the default strategy
	 * of the data binder.
	 * @see #createBinder
	 * @see org.springframework.validation.DataBinder#setBindingErrorProcessor
	 */
	public final void setBindingErrorProcessor(BindingErrorProcessor bindingErrorProcessor) {
		this.bindingErrorProcessor = bindingErrorProcessor;
	}

	/**
	 * Return the strategy to use for processing binding errors (if any).
	 */
	public final BindingErrorProcessor getBindingErrorProcessor() {
		return this.bindingErrorProcessor;
	}

	/**
	 * Specify a single PropertyEditorRegistrar to be applied
	 * to every DataBinder that this controller uses.
	 * <p>Allows for factoring out the registration of PropertyEditors
	 * to separate objects, as an alternative to {@link #initBinder}.
	 * @see #initBinder
	 */
	public final void setPropertyEditorRegistrar(PropertyEditorRegistrar propertyEditorRegistrar) {
		this.propertyEditorRegistrars = new PropertyEditorRegistrar[] {propertyEditorRegistrar};
	}

	/**
	 * Specify multiple PropertyEditorRegistrars to be applied
	 * to every DataBinder that this controller uses.
	 * <p>Allows for factoring out the registration of PropertyEditors
	 * to separate objects, as an alternative to {@link #initBinder}.
	 * @see #initBinder
	 */
	public final void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars) {
		this.propertyEditorRegistrars = propertyEditorRegistrars;
	}

	/**
	 * Return the PropertyEditorRegistrars (if any) to be applied
	 * to every DataBinder that this controller uses.
	 */
	public final PropertyEditorRegistrar[] getPropertyEditorRegistrars() {
		return this.propertyEditorRegistrars;
	}

	/**
	 * Specify a WebBindingInitializer which will apply pre-configured
	 * configuration to every DataBinder that this controller uses.
	 * <p>Allows for factoring out the entire binder configuration
	 * to separate objects, as an alternative to {@link #initBinder}.
	 */
	public final void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
		this.webBindingInitializer = webBindingInitializer;
	}

	/**
	 * Return the WebBindingInitializer (if any) which will apply pre-configured
	 * configuration to every DataBinder that this controller uses.
	 */
	public final WebBindingInitializer getWebBindingInitializer() {
		return this.webBindingInitializer;
	}


	@Override
	protected void initApplicationContext() {
		if (this.validators != null) {
			for (int i = 0; i < this.validators.length; i++) {
				if (this.commandClass != null && !this.validators[i].supports(this.commandClass))
					throw new IllegalArgumentException("Validator [" + this.validators[i] +
							"] does not support command class [" +
							this.commandClass.getName() + "]");
			}
		}
	}


	/**
	 * Retrieve a command object for the given request.
	 * <p>The default implementation calls {@link #createCommand}.
	 * Subclasses can override this.
	 * @param request current HTTP request
	 * @return object command to bind onto
	 * @throws Exception if the command object could not be obtained
	 * @see #createCommand
	 */
	protected Object getCommand(HttpServletRequest request) throws Exception {
		return createCommand();
	}

	/**
	 * Create a new command instance for the command class of this controller.
	 * <p>This implementation uses {@code BeanUtils.instantiateClass},
	 * so the command needs to have a no-arg constructor (supposed to be
	 * public, but not required to).
	 * @return the new command instance
	 * @throws Exception if the command object could not be instantiated
	 * @see org.springframework.beans.BeanUtils#instantiateClass(Class)
	 */
	protected final Object createCommand() throws Exception {
		if (this.commandClass == null) {
			throw new IllegalStateException("Cannot create command without commandClass being set - " +
					"either set commandClass or (in a form controller) override formBackingObject");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Creating new command of class [" + this.commandClass.getName() + "]");
		}
		return BeanUtils.instantiateClass(this.commandClass);
	}

	/**
	 * Check if the given command object is a valid for this controller,
	 * i.e. its command class.
	 * @param command the command object to check
	 * @return if the command object is valid for this controller
	 */
	protected final boolean checkCommand(Object command) {
		return (this.commandClass == null || this.commandClass.isInstance(command));
	}


	/**
	 * Bind the parameters of the given request to the given command object.
	 * @param request current HTTP request
	 * @param command the command to bind onto
	 * @return the ServletRequestDataBinder instance for additional custom validation
	 * @throws Exception in case of invalid state or arguments
	 */
	protected final ServletRequestDataBinder bindAndValidate(HttpServletRequest request, Object command)
			throws Exception {

		ServletRequestDataBinder binder = createBinder(request, command);
		BindException errors = new BindException(binder.getBindingResult());
		if (!suppressBinding(request)) {
			binder.bind(request);
			onBind(request, command, errors);
			if (this.validators != null && isValidateOnBinding() && !suppressValidation(request, command, errors)) {
				for (int i = 0; i < this.validators.length; i++) {
					ValidationUtils.invokeValidator(this.validators[i], command, errors);
				}
			}
			onBindAndValidate(request, command, errors);
		}
		return binder;
	}

	/**
	 * Return whether to suppress binding for the given request.
	 * <p>The default implementation always returns "false". Can be overridden
	 * in subclasses to suppress validation, for example, if a special
	 * request parameter is set.
	 * @param request current HTTP request
	 * @return whether to suppress binding for the given request
	 * @see #suppressValidation
	 */
	protected boolean suppressBinding(HttpServletRequest request) {
		return false;
	}

	/**
	 * Create a new binder instance for the given command and request.
	 * <p>Called by {@link #bindAndValidate}. Can be overridden to plug in
	 * custom ServletRequestDataBinder instances.
	 * <p>The default implementation creates a standard ServletRequestDataBinder
	 * and invokes {@link #prepareBinder} and {@link #initBinder}.
	 * <p>Note that neither {@link #prepareBinder} nor {@link #initBinder} will
	 * be invoked automatically if you override this method! Call those methods
	 * at appropriate points of your overridden method.
	 * @param request current HTTP request
	 * @param command the command to bind onto
	 * @return the new binder instance
	 * @throws Exception in case of invalid state or arguments
	 * @see #bindAndValidate
	 * @see #prepareBinder
	 * @see #initBinder
	 */
	protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command)
		throws Exception {

		ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName());
		prepareBinder(binder);
		initBinder(request, binder);
		return binder;
	}

	/**
	 * Prepare the given binder, applying the specified MessageCodesResolver,
	 * BindingErrorProcessor and PropertyEditorRegistrars (if any).
	 * Called by {@link #createBinder}.
	 * @param binder the new binder instance
	 * @see #createBinder
	 * @see #setMessageCodesResolver
	 * @see #setBindingErrorProcessor
	 */
	protected final void prepareBinder(ServletRequestDataBinder binder) {
		if (useDirectFieldAccess()) {
			binder.initDirectFieldAccess();
		}
		if (this.messageCodesResolver != null) {
			binder.setMessageCodesResolver(this.messageCodesResolver);
		}
		if (this.bindingErrorProcessor != null) {
			binder.setBindingErrorProcessor(this.bindingErrorProcessor);
		}
		if (this.propertyEditorRegistrars != null) {
			for (int i = 0; i < this.propertyEditorRegistrars.length; i++) {
				this.propertyEditorRegistrars[i].registerCustomEditors(binder);
			}
		}
	}

	/**
	 * Determine whether to use direct field access instead of bean property access.
	 * Applied by {@link #prepareBinder}.
	 * <p>Default is "false". Can be overridden in subclasses.
	 * @return whether to use direct field access ({@code true})
	 * or bean property access ({@code false})
	 * @see #prepareBinder
	 * @see org.springframework.validation.DataBinder#initDirectFieldAccess()
	 */
	protected boolean useDirectFieldAccess() {
		return false;
	}

	/**
	 * Initialize the given binder instance, for example with custom editors.
	 * Called by {@link #createBinder}.
	 * <p>This method allows you to register custom editors for certain fields of your
	 * command class. For instance, you will be able to transform Date objects into a
	 * String pattern and back, in order to allow your JavaBeans to have Date properties
	 * and still be able to set and display them in an HTML interface.
	 * <p>The default implementation is empty.
	 * @param request current HTTP request
	 * @param binder the new binder instance
	 * @throws Exception in case of invalid state or arguments
	 * @see #createBinder
	 * @see org.springframework.validation.DataBinder#registerCustomEditor
	 * @see org.springframework.beans.propertyeditors.CustomDateEditor
	 */
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		if (this.webBindingInitializer != null) {
			this.webBindingInitializer.initBinder(binder, new ServletWebRequest(request));
		}
	}

	/**
	 * Callback for custom post-processing in terms of binding.
	 * Called on each submit, after standard binding but before validation.
	 * <p>The default implementation delegates to {@link #onBind(HttpServletRequest, Object)}.
	 * @param request current HTTP request
	 * @param command the command object to perform further binding on
	 * @param errors validation errors holder, allowing for additional
	 * custom registration of binding errors
	 * @throws Exception in case of invalid state or arguments
	 * @see #bindAndValidate
	 * @see #onBind(HttpServletRequest, Object)
	 */
	protected void onBind(HttpServletRequest request, Object command, BindException errors) throws Exception {
		onBind(request, command);
	}

	/**
	 * Callback for custom post-processing in terms of binding.
	 * <p>Called by the default implementation of the
	 * {@link #onBind(HttpServletRequest, Object, BindException)} variant
	 * with all parameters, after standard binding but before validation.
	 * <p>The default implementation is empty.
	 * @param request current HTTP request
	 * @param command the command object to perform further binding on
	 * @throws Exception in case of invalid state or arguments
	 * @see #onBind(HttpServletRequest, Object, BindException)
	 */
	protected void onBind(HttpServletRequest request, Object command) throws Exception {
	}

	/**
	 * Return whether to suppress validation for the given request.
	 * <p>The default implementation delegates to {@link #suppressValidation(HttpServletRequest, Object)}.
	 * @param request current HTTP request
	 * @param command the command object to validate
	 * @param errors validation errors holder, allowing for additional
	 * custom registration of binding errors
	 * @return whether to suppress validation for the given request
	 */
	protected boolean suppressValidation(HttpServletRequest request, Object command, BindException errors) {
		return suppressValidation(request, command);
	}

	/**
	 * Return whether to suppress validation for the given request.
	 * <p>Called by the default implementation of the
	 * {@link #suppressValidation(HttpServletRequest, Object, BindException)} variant
	 * with all parameters.
	 * <p>The default implementation delegates to {@link #suppressValidation(HttpServletRequest)}.
	 * @param request current HTTP request
	 * @param command the command object to validate
	 * @return whether to suppress validation for the given request
	 */
	protected boolean suppressValidation(HttpServletRequest request, Object command) {
		return suppressValidation(request);
	}

	/**
	 * Return whether to suppress validation for the given request.
	 * <p>Called by the default implementation of the
	 * {@link #suppressValidation(HttpServletRequest, Object)} variant
	 * with all parameters.
	 * <p>The default implementation is empty.
	 * @param request current HTTP request
	 * @return whether to suppress validation for the given request
	 * @deprecated as of Spring 2.0.4, in favor of the
	 * {@link #suppressValidation(HttpServletRequest, Object)} variant
	 */
	@Deprecated
	protected boolean suppressValidation(HttpServletRequest request) {
		return false;
	}

	/**
	 * Callback for custom post-processing in terms of binding and validation.
	 * Called on each submit, after standard binding and validation,
	 * but before error evaluation.
	 * <p>The default implementation is empty.
	 * @param request current HTTP request
	 * @param command the command object, still allowing for further binding
	 * @param errors validation errors holder, allowing for additional
	 * custom validation
	 * @throws Exception in case of invalid state or arguments
	 * @see #bindAndValidate
	 * @see org.springframework.validation.Errors
	 */
	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors)
			throws Exception {
	}

}
