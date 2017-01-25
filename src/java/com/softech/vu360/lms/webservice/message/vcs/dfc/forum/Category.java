
package com.softech.vu360.lms.webservice.message.vcs.dfc.forum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Category complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Category">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="categoryTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="categoryGUID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Category", propOrder = {

})
public class Category {

    @XmlElement(required = true)
    protected String categoryTitle;
    @XmlElement(required = true)
    protected String categoryGUID;

    /**
     * Gets the value of the categoryTitle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryTitle() {
        return categoryTitle;
    }

    /**
     * Sets the value of the categoryTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryTitle(String value) {
        this.categoryTitle = value;
    }

    /**
     * Gets the value of the categoryGUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategoryGUID() {
        return categoryGUID;
    }

    /**
     * Sets the value of the categoryGUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategoryGUID(String value) {
        this.categoryGUID = value;
    }

}
