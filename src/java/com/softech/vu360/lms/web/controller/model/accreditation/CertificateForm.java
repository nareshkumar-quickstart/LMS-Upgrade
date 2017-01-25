package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.Certificate;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

public class CertificateForm implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String COURSE_APPROVAL = "Course";
	public static final String COURSE_CONFIG = "CourseConfiguration";
	
	private Certificate certificate;
	private MultipartFile file;
	
	private String entity = null;
	private CourseApproval courseApproval = null;
	private List<Certificate> certificates = new ArrayList<Certificate>();
	
	private String certificateName = null;
	
	private String selectedCertificateID = null;
        
        private String returnURL = null;
        private Long templateId = null;
	
	// for pagination
	private String pageIndex;
	private String showAll = "false";
	private String pageCurrIndex;
	
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	
	/**
	 * @return the pageIndex
	 */
	public String getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(String pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the showAll
	 */
	public String getShowAll() {
		return showAll;
	}

	/**
	 * @param showAll the showAll to set
	 */
	public void setShowAll(String showAll) {
		this.showAll = showAll;
	}

	/**
	 * @return the pageCurrIndex
	 */
	public String getPageCurrIndex() {
		return pageCurrIndex;
	}

	/**
	 * @param pageCurrIndex the pageCurrIndex to set
	 */
	public void setPageCurrIndex(String pageCurrIndex) {
		this.pageCurrIndex = pageCurrIndex;
	}

	/**
	 * @return the sortColumnIndex
	 */
	public String getSortColumnIndex() {
		return sortColumnIndex;
	}

	/**
	 * @param sortColumnIndex the sortColumnIndex to set
	 */
	public void setSortColumnIndex(String sortColumnIndex) {
		this.sortColumnIndex = sortColumnIndex;
	}

	/**
	 * @return the sortDirection
	 */
	public String getSortDirection() {
		return sortDirection;
	}

	/**
	 * @param sortDirection the sortDirection to set
	 */
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	/**
	 * @return the entity
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * @param entity the entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the courseApproval
	 */
	public CourseApproval getCourseApproval() {
		return courseApproval;
	}

	/**
	 * @param courseApproval the courseApproval to set
	 */
	public void setCourseApproval(CourseApproval courseApproval) {
		this.courseApproval = courseApproval;
	}

	/**
	 * @return the certificates
	 */
	public List<Certificate> getCertificates() {
		return certificates;
	}

	/**
	 * @param certificates the certificates to set
	 */
	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}

	/**
	 * @return the certificateName
	 */
	public String getCertificateName() {
		return certificateName;
	}

	/**
	 * @param certificateName the certificateName to set
	 */
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	/**
	 * @return the selectedCertificateID
	 */
	public String getSelectedCertificateID() {
		return selectedCertificateID;
	}

	/**
	 * @param selectedCertificateID the selectedCertificateID to set
	 */
	public void setSelectedCertificateID(String selectedCertificateID) {
		this.selectedCertificateID = selectedCertificateID;
	}

    /**
     * @return the returnURL
     */
    public String getReturnURL() {
        return returnURL;
    }

    /**
     * @param returnURL the returnURL to set
     */
    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    /**
     * @return the templateId
     */
    public Long getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId the templateId to set
     */
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
	
}