package com.softech.vu360.lms.service.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import com.softech.vu360.lms.model.ManageUserStatus;
import com.softech.vu360.lms.repositories.AuthorRepository;
import com.softech.vu360.lms.service.ManageUserStatusService;
import com.softech.vu360.util.DateUtil;

public class ManageUserStatusServiceImpl implements ManageUserStatusService {

	@Inject
	private AuthorRepository authorRepository;

	// MariumSaud : Please note in order to retrieve any newly added column please add the setter of that field in sequence to avoid Indexing Problem.
	@Override
	public List<ManageUserStatus> getUserStatusList(ManageUserStatus criteria) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		List<Object[]> list =  authorRepository.getUserStatus(criteria);
		List<ManageUserStatus> listUserStatus = new ArrayList<ManageUserStatus>();
		if(list!=null && list.size()>0){
			listUserStatus = new ArrayList<ManageUserStatus>(list.size());
			for (int i = 0; i < list.size(); i++) {
				Object [] objArr=list.get(i);
				Integer k=0;
				ManageUserStatus manageUserStatus = new ManageUserStatus();
				manageUserStatus.setLearnerId( ((BigInteger) objArr[k++]).longValueExact());//1
				manageUserStatus.setCardMailingStatus(StringUtils.upperCase((String) objArr[k++])); //2
				if(objArr[3] != null){
					String dtCardMailingDate = dateFormat.format((Date) objArr[3]); //3
					manageUserStatus.setCardMailingDate(dtCardMailingDate) ;        //3
					
				}
				else {
					manageUserStatus.setCardMailingDate(null);
				}
				manageUserStatus.setReportingStatus(StringUtils.upperCase((String) objArr[k++])); //4
				if(objArr[k++] != null){
					String dtReportingDate = dateFormat.format((Date) objArr[k++]); //5
					manageUserStatus.setReportingDate(dtReportingDate) ;
					
				}
				else {
					manageUserStatus.setReportingDate(null);
				}
				manageUserStatus.setLearnerEnrollmentId( ((BigInteger) objArr[k++]).longValueExact());//6
				manageUserStatus.setFirstName((String) objArr[k++]);//7
				manageUserStatus.setLastName((String) objArr[k++]);//8
				manageUserStatus.setEmailAddress((String) objArr[k++]);//9
				manageUserStatus.setPhoneNumber(nullConv((String) objArr[k++]));//10
				manageUserStatus.setCourseName( (String)  objArr[k++]);//11
				manageUserStatus.setCourseId( (String)  objArr[k++]);//12
				//manageUserStatus.setAffidavitLink((String) map.get(  StringUtils.upperCase("affidavitLink")));
				
				//see document 3.5.3 session
				Integer courseType1 = (Integer) objArr[k++];//13
				Integer courseType2 = (Integer) objArr[k++];//14
				Integer courseType3 = (Integer) objArr[k++];//15

				if(  (courseType1!=null) && courseType1.intValue()==1){
					manageUserStatus.setCourseType(ManageUserStatus.COURSE_TYPE_AFFIDAVIT_WITH_REPORTING);
					manageUserStatus.setCourseTypeId(new Long(1));
				}
				else if(  (courseType2!=null) && courseType2.intValue()==2)	{			
					manageUserStatus.setCourseType(ManageUserStatus.COURSE_TYPE_AFFIDAVIT_WITHOUT_REPORTING);
					manageUserStatus.setCourseTypeId(new Long(2));
				}
				else if( (courseType3!=null) && courseType3.intValue()==3){
					manageUserStatus.setCourseType(ManageUserStatus.COURSE_TYPE_REPORTING_WITH_NO_AFFIDAVIT);
					manageUserStatus.setCourseTypeId(new Long(3));
				}
				manageUserStatus.setCourseType (nullConv(manageUserStatus.getCourseType()));
				
				manageUserStatus.setAddress1(nullConv( (String) objArr[k++]));//16
				manageUserStatus.setCity( nullConv((String) objArr[k++]));//17
				manageUserStatus.setState( nullConv( (String) objArr[k++]));//18
				manageUserStatus.setZipCode( nullConv((String) objArr[k++]));//19
				manageUserStatus.setCourseStatus(StringUtils.upperCase((String) objArr[k++])); //20
				manageUserStatus.setCompleteDate(DateUtil.getStringDate((Date) objArr[k++] ));//21	
				manageUserStatus.setEnrollmentDate(DateUtil.getStringDate((Date) objArr[k++]) );//22
				manageUserStatus.setFirstAccessDate(DateUtil.getStringDate((Date) objArr[k++]) );//23
				manageUserStatus.setLastUserStatusChangeDate( DateUtil.getStringDate((Date) objArr[k++]) );//24
				manageUserStatus.setCourseSatisticsId( ((BigInteger)objArr[k++]).longValueExact());//25
				manageUserStatus.setCourseApprovalId( ((BigInteger) objArr[k++]).longValueExact());//26
				manageUserStatus.setHoldingRegulatorName( (String) objArr[k++]);//27
				manageUserStatus.setRegulatoryCategory((String) objArr[k++]);//28
				k++;// Index 29 is missing i-e:- AFFADAVIT_ID 
				manageUserStatus.setAffidavitType( nullConv( (String) objArr[k++]) );//30
				manageUserStatus.setLastUserAffidavitUpload( nullConv( (String) objArr[k++]));//31
				manageUserStatus.setLastUserAffidavitUploadDate( DateUtil.getStringDate((Date) objArr[k++]) );//32
				manageUserStatus.setLastUserStatusChange(nullConv((String) objArr[k++]));//33
				listUserStatus.add(manageUserStatus);
			}
		}
		return listUserStatus;
	}
	
	public static String nullConv(String str)
	{    
	    if(str==null)
	        str="";
	    else if(str.equals("null"))
	        str=StringUtils.EMPTY;
	    else if((str.trim()).equals(StringUtils.EMPTY))
	        str=StringUtils.EMPTY;
	    else if(str.equals(null))
	        str=StringUtils.EMPTY;
	    else
	        str=str.trim();
	    return str;
	}


	
	
	

}
