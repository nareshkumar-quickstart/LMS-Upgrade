/**
 * 
 */
package com.softech.vu360.lms.model;

import java.io.Serializable;

/**
 * The purpose of this interface is to define method that may be used on the
 * User Interface to always return a unique string version of a key for any
 * domain object. This can be used as the values in check boxes, links, etc
 * within the UI.
 * 
 * @author jason
 * 
 */
public interface SearchableKey extends Serializable {

	public String getKey();
}
