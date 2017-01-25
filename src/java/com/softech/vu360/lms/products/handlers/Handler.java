package com.softech.vu360.lms.products.handlers;

/**
 * @author ramiz.uddin
 * @since 0.1
 */
public interface Handler {
	
	public boolean init()throws Exception;
	public boolean exec(Object dataBundle, Exception errors)throws Exception;
	public boolean exit(Object dataBundle, Exception errors)throws Exception;
	
}
