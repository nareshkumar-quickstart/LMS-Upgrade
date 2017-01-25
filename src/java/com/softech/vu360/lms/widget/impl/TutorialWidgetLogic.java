package com.softech.vu360.lms.widget.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.softech.vu360.lms.helpers.ProxyVOHelper;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.widget.WidgetLogic;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

public class TutorialWidgetLogic implements WidgetLogic {

	@Override
	public Collection<WidgetData> getWidgetDataList(VU360User vu360User,
			Map<String, Object> params, HttpServletRequest request) {
		 Brander brander = VU360Branding.getInstance().getBranderByUser(request, ProxyVOHelper.setUserProxy(vu360User));
		 List<WidgetData> datas = new ArrayList<WidgetData>();
		 
		 if(vu360User.isLearnerMode()) {
			 WidgetData widgetData = new WidgetData();
				Map<String, Object> data = widgetData.getDataMap();
				widgetData.setId((long)datas.size());
				ArrayList<HashMap<String, String>> videoUrls = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> videoUrlData = new HashMap<String, String>();
				videoUrlData.put("title", "Learner Mode Video Tutorial");
				videoUrlData.put("url", brander.getBrandElement("lms.login.guidedTourLearnerVideo"));
				videoUrlData.put("image", "brands/default/en/images/training_on.png");
				videoUrls.add(videoUrlData);
				
				data.put("videoUrls", videoUrls);
				
				
				ArrayList<HashMap<String, String>> pdfUrls = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> pdfUrlsData = new HashMap<String, String>();
				
				//Learner mode training manual
				pdfUrlsData.put("title", "Learner Mode Training Manual");
				pdfUrlsData.put("url", brander.getBrandElement("lms.learner.training.manual"));
				
				pdfUrls.add(pdfUrlsData);
		
				data.put("pdfUrls", pdfUrls);
				
				datas.add(widgetData); 
		 }
		 if(vu360User.isManagerMode()) {
			 WidgetData widgetData = new WidgetData();
				Map<String, Object> data = widgetData.getDataMap();
				widgetData.setId((long)datas.size());
				ArrayList<HashMap<String, String>> videoUrls = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> videoUrlData = new HashMap<String, String>();
				videoUrlData.put("title", "Manager Mode Video Tutorial");
				videoUrlData.put("url", brander.getBrandElement("lms.login.guidedTourManagerVideo"));
				videoUrlData.put("image", "brands/default/en/images/reports_on2.png");
				videoUrls.add(videoUrlData);
				data.put("videoUrls", videoUrls);
				
				ArrayList<HashMap<String, String>> pdfUrls = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> pdfUrlsData = new HashMap<String, String>();
				pdfUrlsData.put("title", "Manager Mode Training Manual");
				pdfUrlsData.put("url", brander.getBrandElement("lms.manager.training.manual"));
				pdfUrls.add(pdfUrlsData);
		
				data.put("pdfUrls", pdfUrls);
				datas.add(widgetData); 
		 }
//		 if(vu360User.isInstructorMode()) {
//			 WidgetData widgetData = new WidgetData();
//				Map<String, String> data = widgetData.getDataMap();
//				widgetData.setId((long)datas.size());
//				data.put("title", "Instructor Mode");
//				data.put("url", brander.getBrandElement("lms.login.guidedTourInstructorVideo"));
//				data.put("image", "brands/default/en/images/training_on2.png");
//				data.put("urlType", "video");
//				datas.add(widgetData); 
//		 }
//		 if(vu360User.isAccreditationMode()) {
//			 WidgetData widgetData = new WidgetData();
//				Map<String, String> data = widgetData.getDataMap();
//				widgetData.setId((long)datas.size());
//				data.put("title", "Accreditation Mode");
//				data.put("url", brander.getBrandElement("lms.login.guidedTourAccreditationVideo"));
//				data.put("image", "brands/default/en/images/accreditation_on.png");
//				data.put("urlType", "video");
//				datas.add(widgetData); 
//		 }
//		 if(vu360User.isAdminMode()) {
//			 WidgetData widgetData = new WidgetData();
//				Map<String, String> data = widgetData.getDataMap();
//				widgetData.setId((long)datas.size());
//				data.put("title", "Administrator Mode");
//				data.put("url", brander.getBrandElement("lms.login.guidedTourAdministratorVideo"));
//				data.put("image", "brands/default/en/images/distributors_on.png");
//				data.put("urlType", "video");	
//				datas.add(widgetData); 
//		 }
		
		return datas;
	}

}
