
package com.softech.vu360.lms.webservice.message.lcms.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="assetType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="assetId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keywords" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="itemsPerPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contentOwnerId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="languageId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fileUploaded" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="loggedInUserID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="DisplayText1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DisplayText2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DisplayText3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AffidavitType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="affidavitTemplateID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "assetType",
    "assetId",
    "name",
    "description",
    "keywords",
    "fileName",
    "itemsPerPage",
    "content",
    "contentOwnerId",
    "languageId",
    "fileUploaded",
    "loggedInUserID",
    "displayText1",
    "displayText2",
    "displayText3",
    "affidavitType",
    "affidavitTemplateID"
})
@XmlRootElement(name = "SaveAsset")
public class SaveAsset {

    protected String assetType;
    protected long assetId;
    protected String name;
    protected String description;
    protected String keywords;
    protected String fileName;
    protected int itemsPerPage;
    protected String content;
    protected int contentOwnerId;
    protected int languageId;
    protected boolean fileUploaded;
    protected int loggedInUserID;
    @XmlElement(name = "DisplayText1")
    protected String displayText1;
    @XmlElement(name = "DisplayText2")
    protected String displayText2;
    @XmlElement(name = "DisplayText3")
    protected String displayText3;
    @XmlElement(name = "AffidavitType")
    protected String affidavitType;
    protected Long affidavitTemplateID;

    /**
     * Gets the value of the assetType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssetType() {
        return assetType;
    }

    /**
     * Sets the value of the assetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssetType(String value) {
        this.assetType = value;
    }

    /**
     * Gets the value of the assetId property.
     * 
     */
    public long getAssetId() {
        return assetId;
    }

    /**
     * Sets the value of the assetId property.
     * 
     */
    public void setAssetId(long value) {
        this.assetId = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the keywords property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * Sets the value of the keywords property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeywords(String value) {
        this.keywords = value;
    }

    /**
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
    }

    /**
     * Gets the value of the itemsPerPage property.
     * 
     */
    public int getItemsPerPage() {
        return itemsPerPage;
    }

    /**
     * Sets the value of the itemsPerPage property.
     * 
     */
    public void setItemsPerPage(int value) {
        this.itemsPerPage = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the contentOwnerId property.
     * 
     */
    public int getContentOwnerId() {
        return contentOwnerId;
    }

    /**
     * Sets the value of the contentOwnerId property.
     * 
     */
    public void setContentOwnerId(int value) {
        this.contentOwnerId = value;
    }

    /**
     * Gets the value of the languageId property.
     * 
     */
    public int getLanguageId() {
        return languageId;
    }

    /**
     * Sets the value of the languageId property.
     * 
     */
    public void setLanguageId(int value) {
        this.languageId = value;
    }

    /**
     * Gets the value of the fileUploaded property.
     * 
     */
    public boolean isFileUploaded() {
        return fileUploaded;
    }

    /**
     * Sets the value of the fileUploaded property.
     * 
     */
    public void setFileUploaded(boolean value) {
        this.fileUploaded = value;
    }

    /**
     * Gets the value of the loggedInUserID property.
     * 
     */
    public int getLoggedInUserID() {
        return loggedInUserID;
    }

    /**
     * Sets the value of the loggedInUserID property.
     * 
     */
    public void setLoggedInUserID(int value) {
        this.loggedInUserID = value;
    }

    /**
     * Gets the value of the displayText1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayText1() {
        return displayText1;
    }

    /**
     * Sets the value of the displayText1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayText1(String value) {
        this.displayText1 = value;
    }

    /**
     * Gets the value of the displayText2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayText2() {
        return displayText2;
    }

    /**
     * Sets the value of the displayText2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayText2(String value) {
        this.displayText2 = value;
    }

    /**
     * Gets the value of the displayText3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayText3() {
        return displayText3;
    }

    /**
     * Sets the value of the displayText3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayText3(String value) {
        this.displayText3 = value;
    }

    /**
     * Gets the value of the affidavitType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAffidavitType() {
        return affidavitType;
    }

    /**
     * Sets the value of the affidavitType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAffidavitType(String value) {
        this.affidavitType = value;
    }

    /**
     * Gets the value of the affidavitTemplateID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAffidavitTemplateID() {
        return affidavitTemplateID;
    }

    /**
     * Sets the value of the affidavitTemplateID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAffidavitTemplateID(Long value) {
        this.affidavitTemplateID = value;
    }

}
