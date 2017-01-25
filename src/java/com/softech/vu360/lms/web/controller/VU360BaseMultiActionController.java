/**
 * 
 */
package com.softech.vu360.lms.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * @author Somnath
 *
 */
public abstract class VU360BaseMultiActionController extends MultiActionController {

	private Object delegate;
	private boolean sessionForm = true;
	private String commandName = "command";
	private Class commandClass = null;
	private Validator validator;
	/**
	 * 
	 */
	public VU360BaseMultiActionController() {
		this.delegate = this;
		setDelegate(this);
	}

	/**
	 * @param delegate
	 */
	public VU360BaseMultiActionController(Object delegate) {
		this.delegate = delegate;
		setDelegate(this);
	}

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String targetMethodName = this.getMethodNameResolver().getHandlerMethodName(request);
			Method[] methods = this.delegate.getClass().getMethods();
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				String methodName = method.getName();
				if(methodName.equals(targetMethodName)){

					Class returnType = method.getReturnType();
					if (ModelAndView.class.equals(returnType) 
							|| Map.class.equals(returnType) 
							|| String.class.equals(returnType) 
							|| void.class.equals(returnType)) {

						Class[] paramTypes = method.getParameterTypes();
						List params = new ArrayList(4); 

						if (paramTypes.length == 4) {
							//Class commandClazz = paramTypes[paramTypes.length - 2];
							
							if(this.commandClass!=null){
								//Object command = newCommandObject(paramTypes[paramTypes.length - 2]);
								Object command = getCommand(request);

								//do the form binding
								BindingResult result = bindCommand(request, command, methodName);
								
								//invoke the validations
								BindException errors = new BindException(result);
								this.validate(request, command, errors, methodName);

								//now invoke the required handler
								params.add(request);
								params.add(response);
								params.add(command);
								params.add(errors);

								//need to be very particular about the controller action method signature
								//there were no prior checking on the signature...
								Object returnValue = method.invoke(this.delegate, params.toArray(new Object[params.size()]));
								return massageReturnValueIfNecessary(request, returnValue, errors);
							}
						}						
					}
				}
			}
		}catch (NoSuchRequestHandlingMethodException ex) {
			
			return handleNoSuchRequestHandlingMethod(ex, request, response);
		}catch (InvocationTargetException ex) {
			// The handler method threw an exception.
			return handleException(request, response, ex.getTargetException());
		}catch (Exception ex) {
			// The binding process threw an exception.
			return handleException(request, response, ex);
		}

		return super.handleRequestInternal(request, response);
	}

	protected final Object getCommand(HttpServletRequest request) throws Exception {
		// If not in session-form mode, create a new form object.
		if (!isSessionForm()) {
			return newCommandObject(this.commandClass);
		}

		// Session-form mode: retrieve form object from HTTP session attribute.
		HttpSession session = request.getSession();

		String formAttrName = getFormSessionAttributeName();
		Object sessionFormObject = session.getAttribute(formAttrName);
		if (sessionFormObject == null) {
			sessionFormObject = newCommandObject(this.commandClass);
		}else{

			// Remove form object from HTTP session
			// If it turns out that we need to show the form view again,
			// we'll re-bind the form object to the HTTP session.
			session.removeAttribute(formAttrName);
		}
		return sessionFormObject;
	}
	
	protected final BindingResult bindCommand(HttpServletRequest request, Object command, String methodName) throws Exception {
		logger.debug("Binding request parameters onto command");
		ServletRequestDataBinder binder = createBinder(request, command);
		binder.bind(request);
		onBind(request, command, methodName);
		BindingResult result = binder.getBindingResult();

		return result;
	}
	
	private ModelAndView massageReturnValueIfNecessary(HttpServletRequest request, Object returnValue, BindException errors) {
		
		//rebind the form to the session
		if (isSessionForm() && errors!=null) {
			String formAttrName = getFormSessionAttributeName();
			if (logger.isDebugEnabled()) {
				logger.debug("Setting form session attribute [" + formAttrName + "] to: " + errors.getTarget());
			}
			request.getSession().setAttribute(formAttrName, errors.getTarget());
		}
		
		if (returnValue instanceof ModelAndView) {
			ModelAndView tmpModelAndView = (ModelAndView) returnValue;
			if(errors!=null){
				Map bindmodel = errors.getModel();
				tmpModelAndView = tmpModelAndView.addAllObjects(bindmodel);
			}
			return tmpModelAndView;
		}
		else if (returnValue instanceof Map) {
			return new ModelAndView().addAllObjects((Map) returnValue);
		}
		else if (returnValue instanceof String) {
			return new ModelAndView((String) returnValue);
		}
		else {
			// Either returned null or was 'void' return.
			// We'll assume that the handle method already wrote the response.
			return null;
		}
	}
	
	//copied from the MultiActionController
	private ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex)
	throws Exception {

		Method handler = getExceptionHandler(ex);
		if (handler != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Invoking exception handler [" + handler + "] for exception: " + ex);
			}
			try {
				Object returnValue = handler.invoke(this.delegate, new Object[] {request, response, ex});
				return massageReturnValueIfNecessary(request, returnValue, null);
			}catch (InvocationTargetException ex2) {
				logger.error("Original exception overridden by exception handling failure", ex);
				ReflectionUtils.rethrowException(ex2.getTargetException());
			}catch (Exception ex2) {
				logger.error("Failed to invoke exception handler method", ex2);
			}
		}else {
			// If we get here, there was no custom handler or we couldn't invoke it.
			ReflectionUtils.rethrowException(ex);
		}
		throw new IllegalStateException("Should never get here");
	}
		
	
	protected String getFormSessionAttributeName() {
		return getClass().getName() + ".FORM." + getCommandName();
	}
	
	private boolean isSessionForm() {
		return sessionForm;
	}

	public void setSessionForm(boolean sessionForm) {
		this.sessionForm = sessionForm;
	}

	public Class getCommandClass() {
		return commandClass;
	}

	public void setCommandClass(Class commandClass) {
		this.commandClass = commandClass;
	}

	public String getCommandName() {
		return this.commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}


	protected String getCommandName(Object command) {
		if(StringUtils.isBlank(this.commandName))
			return super.getCommandName(command);
		return this.commandName;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}


	protected abstract void onBind(HttpServletRequest request, Object command, String methodName) throws Exception;
	protected abstract void validate(HttpServletRequest request, Object command, BindException errors, String methodName) throws Exception;
	
}