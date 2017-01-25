/**
 * 
 */
package com.softech.vu360.lms.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.softech.vu360.util.VU360Branding;

/**
 * @author jason
 *
 */
public class BrandsResetServlet extends HttpServlet {

	private static final long serialVersionUID = -8400762627019410976L;

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		boolean reload = VU360Branding.getInstance().reset();
		if ( reload ) {
			res.getOutputStream().print("<html><body><h2>Brands reloaded successfully.</h2></body></html>");
		}
		else {
			res.getOutputStream().print("<html><body><h2>Brand reload failed.</h2></body></html>");
		}
	}

	/**
	 * Constructor for BrandsReset.
	 */
	public BrandsResetServlet() {
		super();
	}

}
