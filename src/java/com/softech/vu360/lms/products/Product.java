package com.softech.vu360.lms.products;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.products.handlers.Handler;


/**
 * @author ramiz.uddin
 * @since 0.1
 */
public class Product {
	
	Handler proHandler;
	Exception errors;
	private static final Logger LOGGER = Logger.getLogger( Product.class.getName());
	 
	public boolean callProductHandler(String productCode, Hashtable<String, Object> dataBundle) throws Exception{
		LOGGER.info("in Product.java callProductHandler() ...with product code .. " + productCode);
		
		Properties properties = new Properties();  
			
			final String PACKAGE_PATH="com.softech.vu360.lms.products.handlers.";
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("productHandlerConfig.properties");  
		
			properties.load(inputStream);
			String propHandler = properties.getProperty(productCode);
			
			if(propHandler!=null){
				Class objProduct = Class.forName(PACKAGE_PATH + propHandler);
				proHandler = (Handler) objProduct.newInstance();
				proHandler.exec(dataBundle, errors);
				return true;
			}else
				return false;
			
			
	}
    
}
