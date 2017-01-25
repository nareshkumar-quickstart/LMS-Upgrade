
package com.softech.vu360.lms.webservice.message.vcs.dfc.forum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createForumRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createForumRequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="category" type="{http://www.threesixtytraining.com/phpbbIntForumWS}Category"/>
 *         &lt;element name="forum" type="{http://www.threesixtytraining.com/phpbbIntForumWS}Forum"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createForumRequestType", propOrder = {

})
@XmlRootElement(name = "createForum")
public class CreateForum {

    @XmlElement(required = true)
    protected Category category;
    @XmlElement(required = true)
    protected Forum forum;

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link Category }
     *     
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link Category }
     *     
     */
    public void setCategory(Category value) {
        this.category = value;
    }

    /**
     * Gets the value of the forum property.
     * 
     * @return
     *     possible object is
     *     {@link Forum }
     *     
     */
    public Forum getForum() {
        return forum;
    }

    /**
     * Sets the value of the forum property.
     * 
     * @param value
     *     allowed object is
     *     {@link Forum }
     *     
     */
    public void setForum(Forum value) {
        this.forum = value;
    }

}
