package com.softech.vu360.util;

import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author jason
 * 
 */
public class Brander extends ResourceBundleWrapper  implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	private String language;
	Brander defaultBrander=null;
	Brander parent;

	public Brander(InputStream in,String name,String lang) throws Exception {
		super(in);
		this.language=lang;
		this.name=name;
	}
	public String getBrandElement(String brandElement) {
		String value=null;
		if (this.containsKey(brandElement)) 
		{
			value=this.getString(brandElement);
		}else{
			if(parent!=null){
				value = parent.getBrandElement(brandElement);
			}
		}
		
		return value;
	}

	public String getBrandElement(String brandElement, String... customValues) {
		return getBrandElement(brandElement, (Object[])customValues);
	}

	public String getBrandElement(String brandElement, Object[] customValues) {
		String value=null;
		if (this.containsKey(brandElement)) 
		{
			value=this.getString(brandElement);
			
			if(customValues != null){
				value = MessageFormat.format(value, customValues);
			}
		}else{
			if(parent!=null){
				value = parent.getBrandElement(brandElement, customValues);
			}
		}
		
		return value;
	}

	/**
	 * If you want to parse the brandElement e.g. lms.manageUser.AddLearner.State=[{'al','Alabama'},{'in','Indiana'}]
     * then use the following "getBrandMapElements" function. This function will return a List<LabelBean>
	 * */
	public List<LabelBean> getBrandMapElements(String brandElement) {
		List<LabelBean> result=null;
		if (this.containsKey(brandElement)) {
			if(brandElement.equals("lms.manageUser.AddLearner.Country")) {
				result=(List<LabelBean>)getCountryList(brandElement);
			} else {
				result=(List<LabelBean>)this.getObject(brandElement);
			}
		}
		else
			if(defaultBrander!=null)
			{
				result=defaultBrander.getBrandMapElements(brandElement);
			}
		return result;
	}

	public String[] getBrandElements(String brandElement) {
		String[] result=null;
		if (this.containsKey(brandElement)) {
			result= this.getStringArray(brandElement);
		}
		else
			if(defaultBrander!=null)
			{
				result=defaultBrander.getBrandElements(brandElement);
			}
		return result;
	}

	public Object handleGetObject(String key)
	{
		Object objValue = super.handleGetObject(key);
		if(objValue!=null){
			String valueStr = (String)objValue;
			valueStr = valueStr.trim();
			
			String regex = "\\[(.*)\\]";
			String regexMap = "\\[(\\{.*)\\]";
			
			Pattern p = Pattern.compile(regex);
			Pattern pMap = Pattern.compile(regexMap);
			
			Matcher m = p.matcher(valueStr);
			Matcher mMap = pMap.matcher(valueStr);
			
			//if input matches with regular exp then and only then the Parse method is called.
			
			if(mMap.find()){
				valueStr = mMap.group(1);
				List<LabelBean> mapEntries = parseMapEntry(valueStr);
				return mapEntries;
			}else if(m.find()){
				valueStr = m.group(1);
				List<String> parts = parse(valueStr);
				String[] valueArray = new String[parts.size()];
				parts.toArray(valueArray);
				return valueArray;
				
			}
		}
		return objValue;
	}
	
	public Object getCountryList(String key) {
		Object objValue = super.handleGetObject(key);
		if(objValue!=null){
			String valueStr = (String)objValue;
			valueStr = valueStr.trim();
			
			String regex = "\\[(.*)\\]";
			String regexMap = "\\[(\\{.*)\\]";
			
			Pattern p = Pattern.compile(regex);
			Pattern pMap = Pattern.compile(regexMap);
			
			Matcher m = p.matcher(valueStr);
			Matcher mMap = pMap.matcher(valueStr);
			
			if(mMap.find()) {
				valueStr = mMap.group(1);
				List<LabelBean> mapEntries = parseCountryList(valueStr);
				
				return mapEntries;
			}
		}
		return objValue;
	}
	
	public List<LabelBean> parseCountryList(String line) {
		List<LabelBean> list = new ArrayList<LabelBean>();

		/*String regex ="\\{\\'([\\w \\s]+)\\'\\,\\'([\\w \\s]+)\\'\\}\\,?";
        Pattern mapentryPattern = Pattern.compile(regex);
        Matcher mapentryMatcher = mapentryPattern.matcher(line);*/
        
		/**
		 * Added by Waqas Baig 11 July, 2011 5:10PM (PST)
		 * All Country List with braces, special characters & digits.
		 */
		StringTokenizer token = new StringTokenizer(line, "[{',}");
        while(token.hasMoreTokens()) {
        	String key = token.nextToken();
        	String value = token.nextToken();
        	LabelBean mapEntry = new LabelBean(value, key);
        	list.add(mapEntry);
        }
        /*
        while (mapentryMatcher.find()) {
        	LabelBean mapEntry = new LabelBean(mapentryMatcher.group(2),mapentryMatcher.group(1));
        	
        	list.add(mapEntry);
        	i++;
        }*/
        
        return list;
	}
	
	

//  /** Parse one line.
 //  * @return List of Strings, minus their double quotes
//   */
	public List<String> parse(String line) 
	{
		List<String> list = new ArrayList<String>();
		String regex ="\"([^\"]+?)\",?|([^,]+),?|,";
		Pattern csvRE = Pattern.compile(regex);
		Matcher m = csvRE.matcher(line);
		// For each field
		while (m.find()) {
			String match = m.group();
			if (match == null)	break;
			if (match.endsWith(",")) {  // trim trailing ,
				match = match.substring(0, match.length() - 1);
			}
			if (match.startsWith("\"")) { // assume also ends with
				match = match.substring(1, match.length() - 1);
			}
			if (match.length() == 0)
				match = null;
			else
				list.add(match);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<LabelBean> parseMapEntry(String line) {
		
		List<LabelBean> list = new ArrayList<LabelBean>();
		
		String regex ="\\{\\'([\\w \\s]+)\\'\\,\\'([\\w \\s]+)\\'\\}\\,?";
        Pattern mapentryPattern = Pattern.compile(regex);
        Matcher mapentryMatcher = mapentryPattern.matcher(line);
        while (mapentryMatcher.find()) {
        	LabelBean mapEntry = new LabelBean(mapentryMatcher.group(2),mapentryMatcher.group(1));
        	list.add(mapEntry);
        }
        return list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	public Brander getDefaultBrander() {
		return defaultBrander;
	}
	public void setDefaultBrander(Brander defaultBrander) {
		this.defaultBrander = defaultBrander;
	}
	public String toString()
	{
		return new StringBuilder(getName()).append(".").append(getLanguage()).toString();
	}
	/**
	 * @return the parent
	 */
	public Brander getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(Brander parent) {
		this.parent = parent;
	}
}
