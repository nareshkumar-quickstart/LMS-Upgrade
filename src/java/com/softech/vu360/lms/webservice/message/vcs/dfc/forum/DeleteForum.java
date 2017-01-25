
package com.softech.vu360.lms.webservice.message.vcs.dfc.forum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteForumRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteForumRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="forumGUID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteForumRequestType", propOrder = {

})
@XmlRootElement(name = "deleteForum")
public class DeleteForum {

    @XmlElement(required = true)
    protected String forumGUID;

    /**
     * Gets the value of the forumGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForumGUID() {
        return forumGUID;
    }

    /**
     * Sets the value of the forumGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForumGUID(String value) {
        this.forumGUID = value;
    }

}
