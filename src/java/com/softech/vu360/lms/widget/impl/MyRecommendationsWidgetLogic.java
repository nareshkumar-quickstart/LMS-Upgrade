package com.softech.vu360.lms.widget.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.RecommendedCourseVO;
import com.softech.vu360.lms.vo.WidgetData;
import com.softech.vu360.lms.webservice.client.impl.StorefrontClientWSImpl;
import com.softech.vu360.lms.widget.WidgetLogic;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;

public class MyRecommendationsWidgetLogic implements WidgetLogic {

    protected static final Logger log = Logger.getLogger(MyRecommendationsWidgetLogic.class);

    private StorefrontClientWSImpl storefrontClientWS;
    private CourseAndCourseGroupService  courseAndCourseGroupService;

    @Override
    public Collection<WidgetData> getWidgetDataList(VU360User vu360User, Map<String, Object> params, HttpServletRequest request) {
        log.debug("in getWidgetDataList vu360User.id=" + vu360User.getId() + " vu360User.username=" + vu360User.getUsername() + " params=" + params);
        List<WidgetData> widgetDataList = new ArrayList<WidgetData>();

        int suggestedCoursesCount = 0;
        int suggestedCoursesCountLimit = 5;

        Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
        List<RecommendedCourseVO> lstOfObjCoursesVO = courseAndCourseGroupService.getRecommendedCoursesFromSF(brand, vu360User);

        if(lstOfObjCoursesVO == null)
            lstOfObjCoursesVO = new ArrayList<RecommendedCourseVO>();

        Map<String, Object> dataMap;

        SuggestedCourseLoop:
        for (RecommendedCourseVO coursesVO : lstOfObjCoursesVO) {
            if(suggestedCoursesCount == suggestedCoursesCountLimit)
            {
                break SuggestedCourseLoop;

            }
            else
            {
                WidgetData sampleData = new WidgetData();
                sampleData.setId(coursesVO.getId());
                dataMap = sampleData.getDataMap();
                dataMap.put("name", coursesVO.getCourseName() );
                dataMap.put("url", coursesVO.getOrderItemURL());
                widgetDataList.add(sampleData);
                suggestedCoursesCount = suggestedCoursesCount + 1;
            }
        }

        return widgetDataList;
    }


    public void setStorefrontClientWS(StorefrontClientWSImpl storefrontClientWS) {
        this.storefrontClientWS = storefrontClientWS;
    }

    public StorefrontClientWSImpl getStorefrontClientWS() {
        return storefrontClientWS;
    }


	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}


	public void setCourseAndCourseGroupService(
			CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}


	
    
}
