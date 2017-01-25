package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.persistence.criteria.Order;


public class SearchCriteria {

	private String key;
    private String operation;
    private Object value;
    private Object[] array; 
    private String joinType;
    private Object between_value1;
    private Object between_value2;
    
	public SearchCriteria(String key,String operation,Object value,String joinType) {
		this.setKey(key);
		this.setOperation(operation);
		this.setValue(value);
		this.setJoinType(joinType);
		
	}
	public SearchCriteria(String key,String operation,Object[] objectArray,String joinType) {
		this.setKey(key);
		this.setOperation(operation);
		this.setArray(objectArray);
		this.setJoinType(joinType);
	}
	public SearchCriteria(String key,String operation,Object between_value1, Object between_value2, String joinType) {
		this.setKey(key);
		this.setOperation(operation);
		this.setBetween_value1(between_value1);
		this.setBetween_value2(between_value2);
		this.setJoinType(joinType);
		
	}
	
	/**
	 * @return the joinType
	 */
	public String getJoinType() {
		return joinType;
	}
	/**
	 * @param joinType the joinType to set
	 */
	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
	

	/**
	 * @return the array
	 */
	public Object[] getArray() {
		return array;
	}
	/**
	 * @param array the array to set
	 */
	public void setArray(Object[] array) {
		this.array = array;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return the between_value1
	 */
	public Object getBetween_value1() {
		return between_value1;
	}
	/**
	 * @param between_value1 the between_value1 to set
	 */
	public void setBetween_value1(Object between_value1) {
		this.between_value1 = between_value1;
	}
	/**
	 * @return the between_value2
	 */
	public Object getBetween_value2() {
		return between_value2;
	}
	/**
	 * @param between_value2 the between_value2 to set
	 */
	public void setBetween_value2(Object between_value2) {
		this.between_value2 = between_value2;
	}
	

}
