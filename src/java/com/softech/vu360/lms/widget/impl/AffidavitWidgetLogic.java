package com.softech.vu360.lms.widget.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.softech.vu360.lms.model.Affidavit;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.CourseApproval;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearningSession;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.StatisticsService;
import com.softech.vu360.lms.vo.AffidavitVO;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.widget.WidgetLogic;

public class AffidavitWidgetLogic implements WidgetLogic {
	private EntitlementService entitlementService = null;
	private AccreditationService accreditationService = null;
	private StatisticsService statisticsService = null;
	private static String affidavitPreviewLink = "acc_manageAffidavit.do?method=preview&id=";
	
	
	public EntitlementService getEntitlementService() {
		return entitlementService;
	}


	public void setEntitlementService(EntitlementService entitlementService) {
		this.entitlementService = entitlementService;
	}


	public AccreditationService getAccreditationService() {
		return accreditationService;
	}


	public void setAccreditationService(AccreditationService accreditationService) {
		this.accreditationService = accreditationService;
	}
	

	public StatisticsService getStatisticsService() {
		return statisticsService;
	}


	public void setStatisticsService(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}


	@Override
	public Collection<WidgetData> getWidgetDataList(VU360User vu360User,
			Map<String, Object> params, HttpServletRequest request) {
		List<AffidavitVO> affidavits = new ArrayList<AffidavitVO>();
		
		Learner learner = vu360User.getLearner();
		
//		List<LearnerEnrollment> emrollments = this.entitlementService.getActiveLearnerEnrollmentsByLearner(learner, "");
		List<LearnerEnrollment> emrollments = this.entitlementService.getLearnerEnrollmentsByLearner(learner, "");
		List<String> coursesList = new ArrayList<String>();

		for(LearnerEnrollment learnerenrollemnt : emrollments){
			if(!coursesList.contains(learnerenrollemnt.getCourse().getCourseGUID()))
				coursesList.add(learnerenrollemnt.getCourse().getCourseGUID());
		}
		
		List<LearningSession> lstLearningSession = accreditationService.getLearningSessionForCourseApproval(coursesList, learner.getId()) ;
		
		for(LearningSession ls : lstLearningSession){
			CourseApproval ca = accreditationService.getCourseApprovalById(ls.getCourseApprovalId());
			AffidavitVO vo = null;
			Affidavit a = null;
			LearnerCourseStatistics lcs = null;
			if(ca.getAffidavit()!=null && ca.getAffidavit().getAssetType().equalsIgnoreCase(Asset.ASSET_TYPE_AFFIDAVIT)) {
				vo = new AffidavitVO();
				a = ca.getAffidavit(); 
				vo.id = a.getId();
				vo.name = a.getName();
				lcs = statisticsService.getLearnerCourseStatistics(learner, ca.getCourse());
				if(lcs !=null) vo.status = lcs.getStatusDisplayText();
				affidavits.add(vo);
			}
		
		}
		List<WidgetData> datas = new ArrayList<WidgetData>();
		for(AffidavitVO a:affidavits) {
		
			WidgetData widgetData = new WidgetData();
			Map<String, Object> data = widgetData.getDataMap();
			widgetData.setId((long)datas.size());
			
			data.put("affidavitId", a.getId().toString());
			data.put("affidavitName", a.getName());
			data.put("status", a.getStatus());
//			data.put("link", affidavitPreviewLink + a.getId());
			datas.add(widgetData);
		
			
		}
		
		return datas;
	}

}
