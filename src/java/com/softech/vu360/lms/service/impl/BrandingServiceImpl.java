package com.softech.vu360.lms.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.Mapping;
import com.softech.vu360.lms.service.BrandingService;
import com.softech.vu360.util.VU360Branding;
import com.softech.vu360.util.VU360Properties;

public class BrandingServiceImpl implements BrandingService{
private  Properties brands=null;
private final String SYSTEMCODE= "lms.userCreated.";
private static final Object lock = new Object();
private static final Logger log = Logger.getLogger(BrandingServiceImpl.class.getName());

public Properties getBrandProperties(String brandName){
	if(brandName==null || brandName.equalsIgnoreCase("default"))return null;
		
	Properties brands= this.readProperties(brandName,false);
										
	return brands;	
    }	    																
    
    private Properties readProperties(String brandName,boolean printErrorTrace){
    	synchronized (lock){
    	Properties brands = null;
    	FileInputStream fis=null;
	     try{ 	
	    	String filePath=VU360Properties.getVU360Property("brands.basefolder")+brandName+"/brand.en.properties";
	    	fis= new FileInputStream(filePath);
	    	brands= new Properties();
	    	brands.load(fis);		    		    	
	    }catch(Exception e){
	    	brands=null;
	    	if(printErrorTrace)
	    	e.printStackTrace();
	    }
	    finally{
	    	try {
				if(fis!=null)fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block				
				if(printErrorTrace)
				e.printStackTrace();
			}
	    }
	    return brands;
    	}	      	        	
    }
    
    
    public boolean isValidBrandName(String brandName){
    	return readProperties(brandName,false)!=null?true:false;
    	
    }
	/*public  String getProperty(String key){
		return brands.getProperty(key);
	}*/
	
	/*public  void setProperty(String key,String value){
	   brands.setProperty(key, value);
	}*/
	
	/**
	 * This method changes the old keyName with a new one
	 * change keyName
	 */
	public  void changeKeyName(String oldKeyName,String newKeyName,String brandName,Properties brands){
		if(oldKeyName.equals(newKeyName))return;
		String value=  brands.getProperty(oldKeyName);
		brands.remove(oldKeyName);
		brands.setProperty(newKeyName, value);
		saveBrands(brandName,brands);
	}
	
	public  void removeProperty(String key){		
		brands.remove(key);				
	}
	/**
	 * Gets all SYSTEM/USER GENERATED keys like the one passed as parameter.
	 * @param searchKey
	 * @return ArrayList<BrandProperty>
	 */
	public ArrayList<Mapping> searchKeys(String searchKey,Properties brands){		
		
		if(brands==null)return null;		
		ArrayList<Mapping> searchList= new ArrayList<Mapping>();
		for (Object key:brands.keySet()){
			if(((String)key).startsWith(SYSTEMCODE) && ((String)key).contains(searchKey)){
				Mapping tempProp =new Mapping();
				String tempKey= (String)key;
				tempProp.setKey(tempKey);
				tempProp.setValue(brands.getProperty(tempKey));
				tempProp.setSystem(true);
				tempProp.setUniqueKeyId(Math.random());
				tempProp.setUniqueValueId(Math.random());
				searchList.add(tempProp);
			}			
		}
		return searchList;		
	}
	
	/**
	 * This is an expensive procedure please use wisely.
	 * @return
	 */
	public  boolean saveBrands(String brandName,Properties brands){
		synchronized (lock){
		boolean isSuccess=false;	  		
			FileOutputStream out=null;
			try {
				String filePath=VU360Properties.getVU360Property("brands.basefolder")+brandName+"/brand.en.properties";			
				out = new FileOutputStream(filePath);			
				brands.store(out, null);
				isSuccess=true;
				VU360Branding.getInstance().reset();    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(out!=null)
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   }
			return isSuccess;
		}	  
	}

	@Override
	public String getProperty(Properties brands,String brandName, String key){
		if(brands.getProperty(key)==null){
			Properties defaultBrand=readProperties("default",true);
			brands.setProperty(key, defaultBrand.getProperty(key));
			saveBrands(brandName, brands);
		}
		return brands.getProperty(key);
	}
	
}
