package com.softech.vu360.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.Language;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;

/**
 * VU360Branding is the singleton class that is used for the branding and
 * localization framework within the VU360 Platform.
 * 
 * @author jason
 * 
 */
public class VU360Branding {

	private static final Logger log = Logger.getLogger(VU360Branding.class.getName());

	private static VU360Branding vu360Branding = null;
	private Map<String, Brander> brands = new HashMap<String, Brander>();
	private static final Object lock = new Object();
	public static final String BRAND = "brand";
	public static final String DEFAULT_BRAND = "default";

	/*
	 * TODO it should iterate all folders and load branding
	 */
	private VU360Branding() {
		loadDefaultBrander();
	}

	/*
	 * 
	 * load default brander
	 */
	private Brander loadDefaultBrander() {
		return loadBrander(DEFAULT_BRAND, new com.softech.vu360.lms.vo.Language());
	}

	/**
	 * Get the instance of the VU360Branding
	 * 
	 * @return VU360Branding
	 */
	public static VU360Branding getInstance() {
		if (vu360Branding == null) {
			vu360Branding = new VU360Branding();
		}
		return vu360Branding;
	}
	
	/**
	 * Used to reset/clear the brands so they are reloaded from disk
	 * especially helpful for hot deployment of brands
	 * @return
	 */
	public boolean reset() {
		boolean success = true;
		try {
			synchronized (lock) {
				this.brands.clear();
				loadDefaultBrander();
			}
		}
		catch(Exception ex) {
			log.error("could not reset the brands", ex);
			success = false;
		}
		
		return success;
	}

	/*
	 * this method will return the appropriate Brander based on given brand & language.
	 * if provided brand is null or empty then default brand name would be considered 
	 * if provided lang is null or empty  then default language would be considered  
	 * After if no brander is identified then system will try to reload
	 * it from file system. If still doesn't find it then it will try to get the default brand with given lang.
	 * If still doens'find any then as fall back strategy it will get the default 360 brander  
	 */
	public Brander getBrander(String brand, com.softech.vu360.lms.vo.Language lang) {

		// get the default brand of same language, if not found then get the brand of default lang
		StringBuilder defaultBrand=new StringBuilder();
		defaultBrand.append(DEFAULT_BRAND);
		defaultBrand.append(".");
		defaultBrand.append(lang.toString());
		// trying to get the brander for chaining/inheritance feature
		// this(default) brander will respond if user brander didn't define the property.
		Brander defaultBrander = (Brander) brands.get(defaultBrand.toString());
		if(defaultBrander==null){
			// it seems no default brander defined for user specified language, now get the default lang brander
			defaultBrand.delete(defaultBrand.lastIndexOf(".")+1,defaultBrand.length());
			defaultBrand.append(Language.DEFAULT_LANG);
			defaultBrander = (Brander) brands.get(defaultBrand.toString());
		}
		
		// get the brand user asked for, if brandName is null or empty then select default
		StringBuffer givenBrand = new StringBuffer();
		if (brand == null || brand.trim().length() == 0) {
			givenBrand.append(DEFAULT_BRAND);
		}else {
			givenBrand.append(brand);
		}
		givenBrand.append(".");
		givenBrand.append(lang.toString());

		Brander brander = (Brander) brands.get(givenBrand.toString());
		
		if (brander == null) {
			synchronized (lock) {

				// ok try to reload the property file may be its newly added.
				brander = (Brander) loadBrander(brand, lang);
				if (brander == null) {
					// ok try with default language
					brander = brands.get(brand + "." + lang.toString());
					if (brander == null) {
						// try to reload from file system
						brander = loadBrander(brand, lang);
						if (brander == null) {
							// ok dude you are coming with wrong brand, let me
							// try to give you default brand with your language
							brander = (Brander) brands.get(DEFAULT_BRAND
									+ lang.toString());
							if (brander == null) {
								// As fall back.. i'll give you default brand
								// with default language.
								brander = (Brander) brands.get(DEFAULT_BRAND+"."+Language.DEFAULT_LANG);
							}
						}
					}
				}
			}
		}
		log.debug("brander="+brander+" .....  givenBrand ["+givenBrand+"]- defaultBrand["+defaultBrand+"]");
		if(brander!=null && !brander.toString().equals(defaultBrander.toString()))
			brander.setDefaultBrander(defaultBrander);
		return brander;
	}

	private Brander loadBrander(String brand, com.softech.vu360.lms.vo.Language lang) {
		// load brand property file based on given parameter
			InputStream in = null;
			Brander brander = null;
			StringBuffer sb = new StringBuffer();
			try {
				sb
						.append(VU360Properties
								.getVU360Property("brands.basefolder"));
				sb.append(brand);
				sb.append("/brand");
				sb.append(".");
				sb.append(lang.toString());
				sb.append(".properties");
				log.debug("path=" + sb.toString());
				in = new FileInputStream(sb.toString());
				brander = new Brander(in, brand, lang.getLanguage());
				brands.put(brand + "." + lang.toString(), brander);
				// new code started
				String parentBrand = brander.getBrandElement("brand.parent");
				if(!StringUtils.isBlank(parentBrand) && !parentBrand.equalsIgnoreCase("null")){
					Brander parent = brands.get(parentBrand+"."+lang.toString());
					//Check for parent was incorrect. Should have been parent == null
					if(parent==null){
						parent = loadBrander(parentBrand, lang);
					}
					brander.setParent(parent);
				} else if ( parentBrand != null && parentBrand.equalsIgnoreCase("null") )  {
					brander.setParent( null );
				} else {
					//If parent property was not found then let's set the parent as DEFAULT
					Brander defaultBrand = brands.get( DEFAULT_BRAND +"."+ lang.toString());
					//Check for parent was incorrect. Should have been parent == null
					if(defaultBrand != null && !brand.equalsIgnoreCase( DEFAULT_BRAND )){
						brander.setParent(defaultBrand);
						//parent = loadBrander(parentBrand, lang);
					}
				}
			} catch (FileNotFoundException fnf) {
				log.error("File[" + sb.toString() + "] not Found[brand="
						+ brand + ", language=" + lang.toString() + "].."
						+ fnf.getMessage());
			} catch (Exception e) {
				log.error("error in loading brand[" + e.getMessage() + "]");
			}
			return brander;
	}
	/*
	 * this method will decide the precedence b/w customer brand name and distributor brand name
	 * 
	 */
	public Brander getBranderByUser(HttpServletRequest request, com.softech.vu360.lms.vo.VU360User user)
	{
		Brander brander=null;
		if(user!=null)
		{			
			String brandName=user.getLearner().getCustomer().getBrandName();			
			
			HttpSession session = request!=null?request.getSession(true):null;
			
			//if admin is logged in as manager
			if(session!=null && session.getAttribute("loggedInAsManager")!=null){
				Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
				Object objDetails = currentAuth.getDetails();
				VU360UserAuthenticationDetails userDetails = (VU360UserAuthenticationDetails)objDetails;
				brandName=userDetails.getProxyCustomer().getBrandName();
			}
				
			//if brand is not defined at customer level then apply distributor level brand
			brandName=(brandName==null || brandName.trim().length()==0)?user.getLearner().getCustomer().getDistributor().getBrandName():brandName;
			brander=this.getBrander(brandName, user.getLanguage());
		}
		return brander;
	}
	
	public Brander getBranderByUser(HttpServletRequest request, VU360User user)
	{
		Brander brander=null;
		if(user!=null)
		{			
			String brandName=user.getLearner().getCustomer().getBrandName();			
			
			HttpSession session = request!=null?request.getSession(true):null;
			//if admin is logged in as manager
			if(session!=null && session.getAttribute("loggedInAsManager")!=null){
				Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
				Object objDetails = currentAuth.getDetails();
				VU360UserAuthenticationDetails userDetails = (VU360UserAuthenticationDetails)objDetails;
				brandName=userDetails.getCurrentCustomer().getBrandName();
			}
				
			//if brand is not defined at customer level then apply distributor level brand
			brandName=(brandName==null || brandName.trim().length()==0)?user.getLearner().getCustomer().getDistributor().getBrandName():brandName;
			brander=this.getBrander(brandName, ProxyVOHelper.createLanguageVO(user.getLanguage()));
		}
		return brander;
	}	
	
	public  int constructBrandDirectoryStructure(String brandName) throws Exception{
		try {
			
			String scriptName="lms_create_brand_directory_structure";
			String rootFolder=VU360Properties.getVU360Property("brands.basefolder"); 
			String scriptPathName = VU360Properties.getVU360Property("brands.basefolder")+scriptName+".sh";
			String logfileName = VU360Properties.getVU360Property("brands.basefolder")+"scriptErrroLog.log";
			String parameters= brandName + " " + logfileName;		
			Process process = Runtime.getRuntime().exec(scriptPathName+" "+parameters+" "+rootFolder);
			process.waitFor();
			return process.exitValue();
			
		} catch (IOException e) {
			e.printStackTrace();
			log.info(e);
			throw e;
		}
	}
}