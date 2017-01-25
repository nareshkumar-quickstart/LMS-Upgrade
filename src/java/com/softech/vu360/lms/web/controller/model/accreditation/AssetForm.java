package com.softech.vu360.lms.web.controller.model.accreditation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.softech.vu360.lms.model.AffidavitTemplate;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 * created on 26-june-2009
 *
 */
public class AssetForm implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	public static final String REFERENCE_TYPE_COURSE_APPROVAL = "CourseApproval";
	
	private Asset asset = null;
	private boolean selected = false;
	private MultipartFile file;
	private byte[] fileData;
	private long referenceId;
	private String referenceType;
	private Object referenceObject;
	private String assetName;
	private List<Asset> assets;
	
	// for pagination
	private String pageIndex;
	private String showAll = "false";
	private String pageCurrIndex;
	
	// for sorting
	private String sortColumnIndex;
	private String sortDirection;
	
	private String selectedAssetID;
	private String noOfDocumentsPerPage;
	
	private ArrayList<AffidavitTemplate> templatesList;
	private String templateGUID;
	private String templateName;
	
	
	
	/**
	 * @return the selectedAssetID
	 */
	public String getSelectedAssetID() {
		return selectedAssetID;
	}
	/**
	 * @param selectedAssetID the selectedAssetID to set
	 */
	public void setSelectedAssetID(String selectedAssetID) {
		this.selectedAssetID = selectedAssetID;
	}
	public Asset getAsset() {
		return asset;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	/**
	 * @return the referenceId
	 */
	public long getReferenceId() {
		return referenceId;
	}
	/**
	 * @param referenceId the referenceId to set
	 */
	public void setReferenceId(long referenceId) {
		this.referenceId = referenceId;
	}
	/**
	 * @return the referenceType
	 */
	public String getReferenceType() {
		return referenceType;
	}
	/**
	 * @param referenceType the referenceType to set
	 */
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	/**
	 * @return the referenceObject
	 */
	public Object getReferenceObject() {
		return referenceObject;
	}
	/**
	 * @param referenceObject the referenceObject to set
	 */
	public void setReferenceObject(Object referenceObject) {
		this.referenceObject = referenceObject;
	}
	/**
	 * @return the assetName
	 */
	public String getAssetName() {
		return assetName;
	}
	/**
	 * @param assetName the assetName to set
	 */
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
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
	/**
	 * @return the assets
	 */
	public List<Asset> getAssets() {
		return assets;
	}
	/**
	 * @param assets the assets to set
	 */
	public void setAssets(List<Asset> assets) {
		this.assets = assets;
	}
	/**
	 * @return the noOfDocumentsPerPage
	 */
	public String getNoOfDocumentsPerPage() {
		return noOfDocumentsPerPage;
	}
	/**
	 * @param noOfDocumentsPerPage the noOfDocumentsPerPage to set
	 */
	
	
	public void setNoOfDocumentsPerPage(String noOfDocumentsPerPage) {
		this.noOfDocumentsPerPage = noOfDocumentsPerPage;
	}
	
	public ArrayList<AffidavitTemplate> getTemplatesList() {
		return templatesList;
	}
	public void setTemplatesList(ArrayList<AffidavitTemplate> templatesList) {
		this.templatesList = templatesList;
	}
	/**
	 * @return the templateGUID
	 */
	public String getTemplateGUID() {
		return templateGUID;
	}
	/**
	 * @param templateGUID the templateGUID to set
	 */
	public void setTemplateGUID(String templateGUID) {
		this.templateGUID = templateGUID;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}	
	
	
}