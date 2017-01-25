/**
 * 
 */
package com.softech.vu360.lms.web.controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.VU360User;


/**
 * LoginHelper
 * 
 * @author Jason Burns
 * 
 */
public class LoginHelper {

		public final static String USER = "user";
		public final static String LOGON_COOKIE_NAME = "LS360P";
		public final static String LOGON_COOKIE_USERNAME_PART = "username";
		public final static String LOGON_COOKIE_MD5PASS_PART = "md5password";
		public final static String LOGON_COOKIE_BRAND_PART = "brand";
		public final static String LOGON_COOKIE_LANG_PART = "lang";

		private static int COOKIE_LIFE = 60 * 60 * 24 * 365 * 1; // 1 year
		
		private static Logger log = Logger.getLogger(LoginHelper.class.getName());
	
		/**
		 * Method getLogonCookieCrumbs.
		 * @param request
		 * @return Map
		 */
		public static Map<Object, Object> getLogonCookieCrumbs(HttpServletRequest request) {
			Cookie cookie = null;

			if (request != null) {
				cookie = findLogonCookie(request);
			}
		
			return makeCookieCrumbs(cookie);
		}

		/**
		 * Method getLogonCookieForUser.
		 * @param user
		 * @param onlyLang
		 * @return Cookie
		 */
		public static Cookie getLogonCookieForUser(VU360User user, String brandName, String lang) {
			Cookie cookie = null;
			StringBuffer buffer = new StringBuffer();
			String username = null;
			String password = null;

			if (user != null) {
				try {
					username = user.getUsername();
					password = user.getPassword();
					cookie = getLogonCookieForUser(username, password, brandName, lang);
				} catch (Exception md5prob) {
					log.error(
						"Could not create the logon cookie for user: " + username,
						md5prob);
				}
			}

			return cookie;
		}

		/**
		 * Method getLogonCookieForUser.
		 * @param username
		 * @param password
		 * @param lmslang
		 * @param onlyLang
		 * @return Cookie
		 */
		public static Cookie getLogonCookieForUser(String username, String password, String brandName, String lang) {
			Cookie cookie = null;
			StringBuffer buffer = new StringBuffer();
			String md5Pass = null;

			if (username != null && password != null) {
				try {
				
					md5Pass = getUserMD5Password(password);

					buffer.append(LOGON_COOKIE_USERNAME_PART);
					buffer.append("=");
					buffer.append(URLEncoder.encode(username));
					buffer.append("&");
					// for now do not set password 
/* 					buffer.append(LOGON_COOKIE_MD5PASS_PART);
 					buffer.append("=");
					buffer.append(URLEncoder.encode(md5Pass));
					buffer.append("&");
*/
					buffer.append(LOGON_COOKIE_BRAND_PART);
					buffer.append("=");
					buffer.append(URLEncoder.encode(brandName));
					buffer.append("&");
					buffer.append(LOGON_COOKIE_LANG_PART);
					buffer.append("=");
					buffer.append(URLEncoder.encode(lang));
					buffer.append("&");

					cookie = new Cookie(LOGON_COOKIE_NAME, buffer.toString());
					cookie.setMaxAge(COOKIE_LIFE);

				} catch (Exception md5prob) {
					log.error(
						"Could not create the logon cookie for user: " + username,
						md5prob);
				}
			}

			return cookie;
		}

	
		/**
		 * Method removeLogonCookie.
		 * @param response
		 * @return boolean
		 */
		public static boolean removeLogonCookie(HttpServletResponse response) {
			Cookie cookie = null;
			boolean result = false;

			if(response != null) {
				cookie = new Cookie(LOGON_COOKIE_NAME, "");
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				result = true;
			}
			return result;
		}
	
	
		/**
		 * Method validLogonCookieForUser.
		 * @param request
		 * @param user
		 * @param onlyLang
		 * @return boolean
		 */
		public static boolean validLogonCookieForUser(HttpServletRequest request, VU360User user) {
			Cookie comparecookie = null;
			Cookie cookie = null;
			boolean valid = false;
			Map crumbs = null;
			Map compareCrumbs = null;

			if (request != null) {
				cookie = findLogonCookie(request);
			}

			if (cookie != null && user != null) {
				comparecookie = getLogonCookieForUser(user, null, null);
				if(comparecookie != null) {
					log.debug("Cookie value: " + cookie.getValue());
					log.debug("Computed cookie value: " + comparecookie.getValue());

					crumbs = makeCookieCrumbs(cookie);
					compareCrumbs = makeCookieCrumbs(comparecookie);
					if(crumbs.containsKey(LOGON_COOKIE_USERNAME_PART) &&
						compareCrumbs.containsKey(LOGON_COOKIE_USERNAME_PART) &&
						crumbs.get(LOGON_COOKIE_USERNAME_PART).equals(compareCrumbs.get(LOGON_COOKIE_USERNAME_PART)) &&
						crumbs.containsKey(LOGON_COOKIE_MD5PASS_PART) &&
						compareCrumbs.containsKey(LOGON_COOKIE_MD5PASS_PART) &&
						crumbs.get(LOGON_COOKIE_MD5PASS_PART).equals(compareCrumbs.get(LOGON_COOKIE_MD5PASS_PART)) ) {
						valid = true;
					}
				}
			}

			return valid;
		}
	
		/**
		 * Method setLogonCookieForUser.
		 * @param response
		 * @param user
		 * @param onlyLang
		 * @return boolean
		 */
		public static boolean setLogonCookieForUser(HttpServletResponse response, VU360User user, String brandName, String lang) {
			Cookie cookie = null;
			boolean result = false;
		
			if(response != null) {
				cookie = getLogonCookieForUser(user, brandName, lang);
				if(cookie != null) {
					log.debug("setting cookie for user");
					response.addCookie(cookie);
					result = true;
				}
				else {
					log.debug("cookie was null!");
				}
			}
			else {
				log.debug("response is null!");
			}
		
			return result;
		}
		
		/**
		 * Method findLogonCookie.
		 * @param request
		 * @return Cookie
		 */
		public static Cookie findLogonCookie(HttpServletRequest request) {
			Cookie cookie = null;
			Cookie[] cookies = null;

			if (request != null) {
				cookies = request.getCookies();
			}

			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName() != null
						&& cookies[i].getName().equalsIgnoreCase(LOGON_COOKIE_NAME)) {
						cookie = cookies[i];
					}
				}
			}

			return cookie;
		}

		/**
		 * Method getUserMD5Password.
		 * @param password
		 * @return String
		 */
		public static String getUserMD5Password(String password) {
			MessageDigest md5 = null;
			String md5pass = "";

			if (password != null) {
				try {
					md5 = MessageDigest.getInstance("MD5");
					md5.update(password.getBytes());
					md5pass = new String(md5.digest());
				} catch (Exception badmd5) {
					log.error("Could not create MD5 password for user", badmd5);
				}
			}
			return md5pass;
		}
	
		/**
		 * Method hasLogonCookie.
		 * @param request
		 * @return boolean
		 */
		public static boolean hasLogonCookie(HttpServletRequest request) {
			Cookie cookie = null;
		
			if(request != null) {
				cookie = findLogonCookie(request);
			}
		
			return (cookie != null);
		}

		/**
		 * Method makeCookieCrumbs.
		 * @param cookie
		 * @return Map
		 */
		private static Map makeCookieCrumbs(Cookie cookie) {
			Map<Object, Object> crumbs = new HashMap<Object, Object>();
			String querystring = null;
			String pair = null;
			List<Object> namevaluepairs = new ArrayList<Object>();
			List<Object> nameandvalue = null;
		
			if (cookie != null) {
				try {
					querystring = cookie.getValue();
					split(namevaluepairs, "/[&]/", querystring);
					for (int i = 0; i < namevaluepairs.size(); i++) {
						pair = (String) namevaluepairs.get(i);
						nameandvalue = new ArrayList<Object>();
						split(nameandvalue, "/[=]/", pair);
						if (nameandvalue.size() == 1) {
							crumbs.put((String) nameandvalue.get(0), "");
						} else if (nameandvalue.size() == 2) {
							crumbs.put(
								(String) nameandvalue.get(0),
								URLDecoder.decode((String) nameandvalue.get(1)));
						}
					}
				} catch (Exception parseerror) {
					log.warn("Could not parse the cookie value", parseerror);
				}
			}

			return crumbs;

		}
	

		/**
		 *  This need some help with the boundry conditions.  Later.
		 */
		private static void split(Collection<Object> c, String delim, String source) {
			StringTokenizer st = new StringTokenizer(source, delim);
		
			while(st.hasMoreTokens()) {
				c.add(st.nextToken());
			}
		}
}
