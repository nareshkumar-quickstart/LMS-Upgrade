package com.softech.vu360.lms.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;

import com.softech.vu360.lms.util.XorEncoding;

/**
 * Filter checking for pre-authentication
 * 
 * @author muhammad.javed
 *
 */
public class VU360RequestPreAuthFilter extends AbstractPreAuthenticatedProcessingFilter {
	
	public VU360RequestPreAuthFilter() {
		super.setAuthenticationDetailsSource(new CustomWebAuthenticationDetailsSource());
	}

	protected static final Logger log = Logger.getLogger(VU360RequestPreAuthFilter.class);
	
	protected String preAuthEntryUrl;
	protected String listenFrom;
	protected String principalRequestHeader = "360_AUTH_USER_TOKEN"; 

	public String getPrincipalRequestHeader() {
		return principalRequestHeader;
	}

	public void setPrincipalRequestHeader(String principalRequestHeader) {
		this.principalRequestHeader = principalRequestHeader;
	}

	public String getListenFrom() {
		return listenFrom;
	}

	public void setListenFrom(String listenFrom) {
		this.listenFrom = listenFrom;
	}

	public String getPreAuthEntryUrl() {
		return preAuthEntryUrl;
	}

	public void setPreAuthEntryUrl(String preAuthEntryUrl) {
		this.preAuthEntryUrl = preAuthEntryUrl;
	}

	/**
	 * Read and returns the header named by principalRequestHeader from the request.
	 * @param {@link javax.servlet.http.HttpServletRequest}
	 * @throws {@link PreAuthenticatedCredentialsNotFoundException}
	 */
	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		String principal = request.getParameter(principalRequestHeader);
		if (principal == null) {
			throw new PreAuthenticatedCredentialsNotFoundException(principalRequestHeader + " parameter not found in request.");
		}
		String dencoded = new XorEncoding().decode(principal);
		return dencoded;
	}

	/*	@Override
	public void doFilterHttp(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
*/		
//Spring 3.0 change
@Override
public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
	
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
	
		String currentUri = request.getRequestURI();
		String expectedUri = request.getContextPath() + preAuthEntryUrl;
		String remoteAddr = request.getRemoteAddr();
		Boolean hasAuthPrincipal = hasPreAuthenticatedPrincipal(request);
		boolean doPreauth = false;
		
		//log.debug("remote address is " + remoteAddr + " and allowed address is " + listenFrom);
		//log.debug("preauth entry url is " + expectedUri + " and current url is " + currentUri);
		//log.debug("hasAuthPrincipal " + hasAuthPrincipal);
		
		if(currentUri.equalsIgnoreCase(expectedUri)) {
			log.debug("in an expected preauth uri");
			
			if(hasAuthPrincipal) {
				log.debug("has a principal in request");
				
				if((listenFrom.equalsIgnoreCase("*") || listenFrom.equalsIgnoreCase(remoteAddr))) {
					log.debug("is from authenticated ip " + remoteAddr);
					
					doPreauth = true;
				}
			}
		}
		
		if( doPreauth ) {
			log.debug("calling default/parent pre auth processing");
			super.doFilter(request, response, filterChain);
		} else {
			//log.debug("conditions not met skipping preauth processing");
			
			/**
			 * Flag for CAS Authenticated User
			 */			
			if(request.getParameter("isCASAuthenticated") != null) {
				request.getSession().setAttribute("isCASAuthenticated", true);
			}
			filterChain.doFilter(request, response);
		}
	}
	
	/**
	 * Checks if there exists a reference of the principal user
	 * @param {@link javax.servlet.http.HttpServletRequest}
	 * @return true when there exists the details of the user being pre authenticated referenced by <i>principalRequestHeader</i> false otherwise.
	 */
	protected boolean hasPreAuthenticatedPrincipal(HttpServletRequest request) {
		try {
			Object obj = getPreAuthenticatedPrincipal(request);
			if(obj==null) {
				return false;
			}
			return true;
		} catch(PreAuthenticatedCredentialsNotFoundException e) {
			return false;
		}
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest arg0) {
		return "";
	}
	
	
}
