
package com.softech.vu360.lms.webservice.message.vcs.dfc.forum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Forum complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Forum">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="forumName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="forumDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
@XmlType(name = "Forum", propOrder = {

})
public class Forum {

    @XmlElement(required = true)
    protected String forumName;
    @XmlElement(required = true)
    protected String forumDesc;
    @XmlElement(required = true)
    protected String forumGUID;

    /**
     * Gets the value of the forumName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForumName() {
        return forumName;
    }

    /**
     * Sets the value of the forumName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForumName(String value) {
        this.forumName = value;
    }

    /**
     * Gets the value of the forumDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForumDesc() {
        return forumDesc;
    }

    /**
     * Sets the value of the forumDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForumDesc(String value) {
        this.forumDesc = value;
    }

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
