package com.softech.vu360.lms.web.controller.accreditation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.softech.vu360.lms.model.CertificateBookmarkAssociation;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CustomField;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.Mapping;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.web.controller.VU360BaseMultiActionController;
import com.softech.vu360.lms.web.controller.model.accreditation.CertificateBookmarkAssociationForm;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.MappingSort;

public class CertificateBookmarkAssociationController extends VU360BaseMultiActionController {
	
	private String associateBookmarksTemplate;
	private String associateBookmarksAddTemplate;
	private String associateBookmarksUpdateTemplate;
	private String failureTemplate= null;	
	private CustomerService customerService;
	private DistributorService distributorService;
	private AccreditationService accreditationService = null;	
	private static final Logger log = Logger.getLogger(CertificateBookmarkAssociationController.class.getName());		
	
	public CertificateBookmarkAssociationController() {
		super();
	}
	
	protected void onBind(HttpServletRequest request, Object command,String methodName) throws Exception {        
		
	}
	
	public Mapping createNewMapping(CertificateBookmarkAssociation cba){
		Mapping tempMapping = new Mapping();				
		tempMapping.setId(cba.getId());
		tempMapping.setKey(cba.getBookmarkLabel());
		tempMapping.setValue(cba.getValue());				
		tempMapping.setUniqueKeyId(Math.random());
		tempMapping.setUniqueValueId(Math.random());
		tempMapping.setValueSelectionCode(cba.getValueSelectionCode());
		return tempMapping;
	}
		
	public ArrayList<String> getLMSFieldNameList(){
		ArrayList<String> list=  new ArrayList<String>();		
				list.add("First Name");
				list.add("Middle Initial");
				list.add("Last Name");
				list.add("Full Name");
				list.add("Address");
				list.add("City");
				list.add("State");
				list.add("Zip Code");
				list.add("Phone Number");
				list.add("Certificate Number");
				list.add("Course Registration Date");
				list.add("Course Completion Date");
				list.add("Course Expiration Date");
				list.add("Certificate Expiration Date");
				list.add("Certificate Expiration Date - 3 years");
				list.add("School Name");
				list.add("Provider Name");
				list.add("Course Approval Number");
				list.add("Course Name");
				list.add("Approved Course Name");
				list.add("Approved Credit Hours");
				list.add("Final Exam Score");
				list.add("Credit Hours");
				list.add("Course State");
				list.add("Course Status");
				list.add("Instructor Name");
				list.add("Instructor Number");
				list.add("Course Start Date");
				list.add("Course Start Time");
				list.add("Course End Time");
				list.add("Course Approval Type");
				list.add("Certificate Validation");
				list.add("SID");//LMS-20652
//				list.add("SSN last 4 digits");
				return list;
	}
	
	public ModelAndView showAssociationList(HttpServletRequest request,HttpServletResponse response,Object command,BindException errors) throws Exception {
        List<Mapping> searchKeys = new ArrayList<Mapping>();                
        String searchText= request.getParameter("name");
        List <CertificateBookmarkAssociation> al= accreditationService.getAllCertificateBookmarkAssociation(searchText!=null?searchText:"");
        for(CertificateBookmarkAssociation cba: al){
        	searchKeys.add(createNewMapping(cba));     
        }                
        
        Map<Object, Object> context = new HashMap<Object, Object>();
        Map<String, String> pagerAttributes = new HashMap <String, String>();
		context.put("name", searchText);
        //For sorting branding list based on keys.
        String sortColumnIndex = request.getParameter("sortColumnIndex");
        String sortDirection = request.getParameter("sortDirection");
        
        String pageIndex = request.getParameter("pageIndex");
        if( pageIndex == null ) pageIndex = "0";

        pagerAttributes.put("pageIndex", pageIndex);
        if( searchKeys.size() <= 10 ) {
            pagerAttributes.put("pageIndex", pageIndex);
        }
        
        String showAll = request.getParameter("showAll");
        if ( showAll == null ) 
            showAll = "false";
        if ( showAll.isEmpty() ) 
            showAll = "true";
        context.put("showAll", showAll);
        
        log.debug("sortColumnIndex: " + sortColumnIndex);
        log.debug("sortDirection: " + sortDirection);
        log.debug("pageIndex: " + pageIndex);
        
        if( sortColumnIndex != null && (sortDirection != null && sortDirection.isEmpty() == false) ) {
            request.setAttribute("PagerAttributeMap", pagerAttributes);
            Integer order = Integer.valueOf(sortDirection);
            MappingSort sort = new MappingSort();
            sort.setSortDirection(order);                                        
            Collections.sort(searchKeys, sort);
            context.put("sortDirection", sortDirection);
            context.put("sortColumnIndex", sortColumnIndex);
        }
                
		context.put("systemBrandList", searchKeys);
        context.put("candidateFieldsMap", getCandidateFieldsMap());
        log.debug("context: " + context);
		return new ModelAndView(associateBookmarksTemplate, "context", context);
	}
	
	public ModelAndView saveAssociation(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{            		
		CertificateBookmarkAssociationForm form = (CertificateBookmarkAssociationForm)command;
		log.debug("In saveAssociation");
		if(StringUtils.isEmpty(form.getBookmarkLabel()) ||form.getBookmarkLabel().equals("") )
			errors.rejectValue("bookmarkLabel", "lms.accraditation.editApproval.caption.CertBookmarkAssociation.warningMessageSelectLabel");
		if(StringUtils.isEmpty(form.getAssociatedField()) || (form.getAssociatedField().contains("-") && !form.getAssociatedField().contains("Certificate Expiration Date - 3 years")) || form.getAssociatedField().equals(""))
			errors.rejectValue("associatedField", "lms.accraditation.editApproval.caption.CertBookmarkAssociation.warningMessageSelectValue");
		
		if( errors.hasErrors())
			return gotoEditMode(request,  response, command, errors);
		
		
		String id= request.getParameter("id");
		
		CertificateBookmarkAssociation cba= accreditationService.loadForUpdateCertificateBookmarkAssociation(Long.parseLong(id));
		cba.setBookmarkLabel(form.getBookmarkLabel());			
	
		StringTokenizer st= new StringTokenizer(form.getAssociatedField(),":"); 
		String fieldType= st.nextToken().trim(); 		
		if(fieldType.equalsIgnoreCase("L")){	
			cba.setLMSFieldName(st.nextToken().trim());
			cba.setReportingField(null);
			cba.setCustomField(null);
		}else if(fieldType.equalsIgnoreCase("R")){					      
		      cba.setReportingField(accreditationService.getCreditReportingFieldById(Long.parseLong(st.nextToken())));
		      cba.setCustomField(null);
		      cba.setLMSFieldName(null);
		}else if(fieldType.equalsIgnoreCase("C")){			
			cba.setCustomField(accreditationService.getCustomFieldById(Long.parseLong(st.nextToken())));
			cba.setReportingField(null);
			cba.setLMSFieldName(null);
		}
		accreditationService.saveCertificateBookmarkAssociation(cba);
		form.reset();
		return showAssociationList(request,  response, command, errors);
	}
	
	public ModelAndView deleteAssociation(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{
		log.debug("In deleteAssociation");
		if  ( request.getParameterValues("brand") != null ) {
			long[] id = new long[request.getParameterValues("brand").length];
			String []idArray = request.getParameterValues("brand");
			
			ArrayList<CertificateBookmarkAssociation> deleteList= new ArrayList<CertificateBookmarkAssociation>();			
			for (int i=0;i<id.length;i++) {								
				deleteList.add(accreditationService.loadForUpdateCertificateBookmarkAssociation(Long.parseLong(idArray[i])));				
			}			
			accreditationService.deleteCertificateBookmarkAssociation(deleteList);
		}		
		return showAssociationList(request, response, command, errors);									  		 
	}
	
	public ModelAndView gotoEditMode(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{
		String bookmarkId="";
		String bookmarkLabel= "";
		String associatedField= "";
		
		CertificateBookmarkAssociationForm certBookMarkAssoForm = (CertificateBookmarkAssociationForm)command;
		if(request.getParameter("baid")!=null && !request.getParameter("baid").equals(""))
		{
			 bookmarkId= request.getParameter("baid");
			 bookmarkLabel= request.getParameter("bookmarkLabel");
			 associatedField= request.getParameter("associatedField");
			 
			 certBookMarkAssoForm.setId(bookmarkId);
			 certBookMarkAssoForm.setBookmarkLabel(bookmarkLabel);
			 certBookMarkAssoForm.setAssociatedField(associatedField);
		}
		Map<Object, Object> context = new HashMap<Object, Object>();
	    context.put("candidateFieldsMap", getCandidateFieldsMap());
		return new ModelAndView(associateBookmarksUpdateTemplate, "context", context);
	}
	
	public ModelAndView newAssociation(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{
		CertificateBookmarkAssociationForm form = (CertificateBookmarkAssociationForm)command;
		if(!errors.hasErrors())
			form.reset();	
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put("candidateFieldsMap", getCandidateFieldsMap());
		return new ModelAndView(associateBookmarksAddTemplate, "context", context);
	}
	
	public ModelAndView saveNewAssociation(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)throws Exception{
		CertificateBookmarkAssociationForm form = (CertificateBookmarkAssociationForm)command;
		String bookmarkLabel= request.getParameter("bookmarkLabel");
		String associatedField= request.getParameter("associatedField");
		
		if(StringUtils.isEmpty(bookmarkLabel) || bookmarkLabel.equals("") )
			errors.rejectValue("bookmarkLabel", "lms.accraditation.editApproval.caption.CertBookmarkAssociation.warningMessageSelectLabel");
		if(StringUtils.isEmpty(associatedField) || (associatedField.contains("-") && !associatedField.contains("Certificate Expiration Date - 3 years")) || associatedField.equals(""))
			errors.rejectValue("associatedField", "lms.accraditation.editApproval.caption.CertBookmarkAssociation.warningMessageSelectValue");
		
		if( errors.hasErrors())
			return newAssociation(request,  response, command, errors);
		
		CertificateBookmarkAssociation cba= new CertificateBookmarkAssociation();
		cba.setBookmarkLabel(bookmarkLabel);			
	
		StringTokenizer st= new StringTokenizer(associatedField,":"); 
		String fieldType= st.nextToken().trim(); 		
		if(fieldType.equalsIgnoreCase("L")){	
			cba.setLMSFieldName(st.nextToken().trim());
			cba.setReportingField(null);
			cba.setCustomField(null);
		}else if(fieldType.equalsIgnoreCase("R")){					      
		      cba.setReportingField(accreditationService.getCreditReportingFieldById(Long.parseLong(st.nextToken())));
		      cba.setCustomField(null);
		      cba.setLMSFieldName(null);
		}else if(fieldType.equalsIgnoreCase("C")){			
			cba.setCustomField(accreditationService.getCustomFieldById(Long.parseLong(st.nextToken())));
			cba.setReportingField(null);
			cba.setLMSFieldName(null);
		}
		accreditationService.saveCertificateBookmarkAssociation(cba);
		form.reset();
		return showAssociationList(request,  response, command, errors);
	}
	
	@Override
	protected void validate(HttpServletRequest request, Object command,
		BindException errors, String methodName) throws Exception {		 		
	}

	public String getAssociateBookmarksTemplate() {
		return associateBookmarksTemplate;
	}

	public void setAssociateBookmarksTemplate(String associateBookmarksTemplate) {
		this.associateBookmarksTemplate = associateBookmarksTemplate;
	}
	
	public String getFailureTemplate() {
		return failureTemplate;
	}
	
	public void setFailureTemplate(String failureTemplate) {
		this.failureTemplate = failureTemplate;
	}	
	
	public CustomerService getCustomerService() {
		return customerService;
	}
	
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
        
	public AccreditationService getAccreditationService() {
		return accreditationService;
	}
	
	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}

	public DistributorService getDistributorService() {
		return distributorService;
	}

	public void setDistributorService(DistributorService distributorService) {
		this.distributorService = distributorService;
	}	
	
	public String getAssociateBookmarksAddTemplate() {
		return associateBookmarksAddTemplate;
	}

	public void setAssociateBookmarksAddTemplate(String associateBookmarksAddTemplate) {
		this.associateBookmarksAddTemplate = associateBookmarksAddTemplate;
	}

	public String getAssociateBookmarksUpdateTemplate() {
		return associateBookmarksUpdateTemplate;
	}

	public void setAssociateBookmarksUpdateTemplate(
			String associateBookmarksUpdateTemplate) {
		this.associateBookmarksUpdateTemplate = associateBookmarksUpdateTemplate;
	}

	public class ReportingFieldComparable implements Comparator<CreditReportingField>{
	    @Override
	    public int compare(CreditReportingField o1, CreditReportingField o2) {
	    	 String s1 = o1.getFieldLabel().trim().toUpperCase();
	    	    String s2 = o2.getFieldLabel().trim().toUpperCase();
	    	    return s1.compareTo(s2);
	    }
	}
	
	public class CustomFieldComparable implements Comparator<CustomField>{
	    @Override
	    public int compare(CustomField o1, CustomField o2) {
	    	 String s1 = o1.getFieldLabel().trim().toUpperCase();
	    	    String s2 = o2.getFieldLabel().trim().toUpperCase();
	    	    return s1.compareTo(s2);
	    }
	}
	
	public LinkedHashMap <String,String> getCandidateFieldsMap() {
		LinkedHashMap <String,String> candidateFieldsMap= new LinkedHashMap<String,String>();
        List<String> lmsFieldNameList= this.getLMSFieldNameList();
        Collections.sort(lmsFieldNameList);
        candidateFieldsMap.put("-1","---LMS FIELDS---");
        for(String fieldName:lmsFieldNameList){
        	candidateFieldsMap.put(new StringBuilder().append("L:").append(fieldName).toString(), fieldName);
        }
    
	    VU360User loggedInUser = VU360UserAuthenticationDetails.getCurrentUser();
	    //Commented by Marium Saud : ContentOwner argument is not used by the method accreditationService.getAllCreditReportingFields implementation
	    //ContentOwner contentOwner = accreditationService.findContentOwnerByRegulatoryAnalyst(loggedInUser.getRegulatoryAnalyst());                
	    List<CreditReportingField>tempList= accreditationService.getAllCreditReportingFields("","");
	    Collections.sort(tempList,new ReportingFieldComparable());
	    candidateFieldsMap.put("-1.5","---REPORTING FIELDS---");
	    for(CreditReportingField crf :tempList){
	    	candidateFieldsMap.put(new StringBuilder().append("R:").append(crf.getId()).toString(), crf.getFieldLabel());
	    }                
	    candidateFieldsMap.put("-2","---CUSTOM FIELDS---");
	    /*custom fields*/
	    for(Distributor dist: distributorService.findResellersWithCustomFields(loggedInUser)){
	    	candidateFieldsMap.put("-3".concat(dist.getId().toString()),"RESELLER:".concat(dist.getName()));
	    	Collections.sort(dist.getCustomFields(),new CustomFieldComparable());
	    	for(CustomField cf:dist.getCustomFields()){
	    		candidateFieldsMap.put(new StringBuilder().append("C:").append(cf.getId()).toString(), cf.getFieldLabel());
	    	}
	    }                                
	    for(Customer cust: customerService.findCustomersWithCustomFields(loggedInUser) ){
	    	candidateFieldsMap.put("-4".concat(cust.getId().toString()),"CUSTOMER:".concat(cust.getName()));
	    	Collections.sort(cust.getCustomFields(),new CustomFieldComparable());
	    	for(CustomField cf:cust.getCustomFields()){
	    		candidateFieldsMap.put(new StringBuilder().append("C:").append(cf.getId()).toString(), cf.getFieldLabel());
	    	}
	    }  
	    return candidateFieldsMap;
	}
}
