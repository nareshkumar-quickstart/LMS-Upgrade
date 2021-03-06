//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.09 at 03:40:50 PM PKT 
//


package com.softech.vu360.lms.webservice.message.lmsapi.types.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegisterUsers complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisterUsers"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RegisterUser" type="{http://user.types.lmsapi.message.webservice.lms.vu360.softech.com}RegisterUser" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterUsers", propOrder = {
    "registerUser"
})
public class RegisterUsers
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "RegisterUser", required = true, nillable = true)
    protected List<RegisterUser> registerUser;

    /**
     * Gets the value of the registerUser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the registerUser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegisterUser().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RegisterUser }
     * 
     * 
     */
    public List<RegisterUser> getRegisterUser() {
        if (registerUser == null) {
            registerUser = new ArrayList<RegisterUser>();
        }
        return this.registerUser;
    }

	public void setRegisterUser(List<RegisterUser> registerUser) {
		this.registerUser = registerUser;
	}
    
}
