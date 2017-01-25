package com.softech.vu360.lms.service.impl;

import java.math.BigInteger;
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

	@Override
	public List<ManageUserStatus> getUserStatusList(ManageUserStatus criteria) {
		List<Object[]> list =  authorRepository.getUserStatus(criteria);
		List<ManageUserStatus> listUserStatus = new ArrayList<ManageUserStatus>();
		if(list!=null && list.size()>0){
			listUserStatus = new ArrayList<ManageUserStatus>(list.size());
			for (int i = 0; i < list.size(); i++) {
				Object [] objArr=list.get(i);
				ManageUserStatus manageUserStatus = new ManageUserStatus();
				manageUserStatus.setFirstName((String) objArr[3]);//3
				manageUserStatus.setLastName((String) objArr[4]);//4
				manageUserStatus.setEmailAddress((String) objArr[5]);//5
				manageUserStatus.setPhoneNumber(nullConv((String) objArr[6]));//6
				manageUserStatus.setCourseName( (String)  objArr[7]);//7
				manageUserStatus.setCourseId( (String)  objArr[8]);//8
				manageUserStatus.setCourseStatus((String) objArr[16]);//16
				
				//see document 3.5.3 session
				Integer courseType1 = (Integer) objArr[9];//9
				Integer courseType2 = (Integer) objArr[10];//10
				Integer courseType3 = (Integer) objArr[11];//11

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
				
				manageUserStatus.setCompleteDate(DateUtil.getStringDate((Date) objArr[17] ));//17	
				manageUserStatus.setEnrollmentDate(DateUtil.getStringDate((Date) objArr[18]) );//18
				manageUserStatus.setFirstAccessDate(DateUtil.getStringDate((Date) objArr[19]) );//19
				manageUserStatus.setHoldingRegulatorName( (String) objArr[23]);//23
				manageUserStatus.setRegulatoryCategory((String) objArr[24]);//24
				
				manageUserStatus.setAffidavitType( nullConv( (String) objArr[26]) );//26
				
				manageUserStatus.setLastUserStatusChange(nullConv((String) objArr[29]));//29
				manageUserStatus.setLastUserStatusChangeDate( DateUtil.getStringDate((Date) objArr[20]) );//20
				manageUserStatus.setLastUserAffidavitUpload( nullConv( (String) objArr[27]));//27
				
				manageUserStatus.setLastUserAffidavitUploadDate( DateUtil.getStringDate((Date) objArr[28]) );//28
				manageUserStatus.setCourseSatisticsId( ((BigInteger)objArr[21]).longValueExact());//21
				manageUserStatus.setLearnerEnrollmentId( ((BigInteger) objArr[2]).longValueExact());//2
				manageUserStatus.setCourseApprovalId( ((BigInteger) objArr[22]).longValueExact());//22
				manageUserStatus.setAddress1(nullConv( (String) objArr[12]));//12
				manageUserStatus.setCity( nullConv((String) objArr[13]));//13
				manageUserStatus.setState( nullConv( (String) objArr[14]));//14
				manageUserStatus.setZipCode( nullConv((String) objArr[15]));//15
                manageUserStatus.setLearnerId( ((BigInteger) objArr[1]).longValueExact());//1
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
