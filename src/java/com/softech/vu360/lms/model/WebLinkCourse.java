/**
 * 
 */
package com.softech.vu360.lms.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * 
 * @author muhammad.saleem
 *
 */
@Entity
@DiscriminatorValue("Weblink")
public class WebLinkCourse extends Course {

    private static final long serialVersionUID = 1L;
    public static final String COURSE_TYPE="Weblink";
    
    @Column(name = "LINK")
	private String link = null;
	
	
	public WebLinkCourse() {
		super();
        setCourseType(COURSE_TYPE);
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 *            the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	public String getCourseType() {
		// TODO Auto-generated method stub
		return COURSE_TYPE;
	}
}
