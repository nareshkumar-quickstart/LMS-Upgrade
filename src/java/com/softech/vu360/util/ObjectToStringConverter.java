package com.softech.vu360.util;

import javax.persistence.AttributeConverter;
/**
 * 
 * @author marium.saud
 *
 */

public class ObjectToStringConverter implements AttributeConverter<Object, String>{

	@Override
	public String convertToDatabaseColumn(Object entityValue) {
		if(entityValue!=null){
			return String.valueOf(entityValue); 
		}
		else{
			return null;
		}
	}

	@Override
	public Object convertToEntityAttribute(String dataBaseValue) {
		Object obj=dataBaseValue;
		return obj;
	}

	
}
