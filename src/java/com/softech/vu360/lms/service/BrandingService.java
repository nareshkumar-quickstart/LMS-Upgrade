package com.softech.vu360.lms.service;
import java.util.ArrayList;
import java.util.Properties;

import com.softech.vu360.lms.model.Mapping;

public interface BrandingService{
public Properties getBrandProperties(String brandName);
public  void changeKeyName(String oldKeyName,String newKeyName,String brandName,Properties brands);
public ArrayList<Mapping> searchKeys(String searchKey,Properties brands);	
public  boolean saveBrands(String brandName,Properties brands);
public boolean isValidBrandName(String brandName);
public String getProperty(Properties brands,String brandName, String key);
}
