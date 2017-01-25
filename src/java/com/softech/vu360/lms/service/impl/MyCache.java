package com.softech.vu360.lms.service.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class MyCache {
	
    private static ConcurrentMap<String, Object> Cache = new ConcurrentHashMap<String, Object>();
	public static String getValue(String value){ return (String) Cache.get(value);}
	public static  void setValue(String key, String value){	Cache.put(key, value);}
}


