package com.softech.vu360.lms.web.controller.learner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.vo.Language;
import com.softech.vu360.lms.vo.RecommendedCourseVO;
import com.softech.vu360.lms.webservice.message.SuggestedCourse.CourseInfo;
import com.softech.vu360.lms.webservice.message.SuggestedCourse.SuggestedCoursesList;
import com.softech.vu360.util.Brander;
import com.softech.vu360.util.VU360Branding;
//import com.softech.vu360.lms.model.CourseCompletionCriteria;
//import com.softech.vu360.lms.service.impl.SuggestedCoursesServiceImpl;

/**
 * The controller for the Recommended course display page.
 * @author abdullah masood
 * @modified rehan.rana
 */

public class MyRecommendedCourseController extends MultiActionController implements
InitializingBean {
	
	private static final Logger log = Logger.getLogger(MyCoursesController.class.getName());
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	
	@Autowired
	private VU360UserService vu360UserService;
	
	protected EnrollmentService enrollmentService;
	
	
	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}
	
	String filter = "all";

	@SuppressWarnings("unchecked")
    public ModelAndView displayMyCourses(HttpServletRequest request, HttpServletResponse response) {

        Long beforeALL = System.currentTimeMillis();
        log.debug("before ALL " + beforeALL);



        log.debug("totalMemory:"+Runtime.getRuntime().totalMemory());
        log.debug("1- displayMyCourses freeMemory:"+Runtime.getRuntime().freeMemory());


        List<String> recommendedCourses = new ArrayList<String>();
        List<SuggestedCoursesList> suggestedCoursesList = new ArrayList<SuggestedCoursesList>();
        Map<Object, Object> context = new HashMap<Object, Object>();

        int suggestedCoursesCount = 0;
        int suggestedCoursesCountLimit = 20;

        List<RecommendedCourseVO> maxRecommededCourses = new ArrayList<RecommendedCourseVO>();

        com.softech.vu360.lms.vo.VU360User user = (com.softech.vu360.lms.vo.VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Brander brand=VU360Branding.getInstance().getBrander((String)request.getSession().getAttribute(VU360Branding.BRAND), new Language());
        try {

            //context.put("COURSE", courseAndCourseGroupService.getRecommendedCoursesFromSF(brand, user));
            //for dummy data
            //List<RecommendedCourseVO> suggestedCourses =  dummyData();

            //for procduction
        	VU360User vu360UserModel = vu360UserService.getUserById(user.getId());
            List<RecommendedCourseVO> suggestedCourses =  courseAndCourseGroupService.getRecommendedCoursesFromSF(brand, vu360UserModel);

            //for null value check
            //List<RecommendedCourseVO> suggestedCourses =  null;

            if(suggestedCourses == null)
                suggestedCourses = new ArrayList<RecommendedCourseVO>();

            SuggestedCourseLoop:
            for (RecommendedCourseVO coursesVO : suggestedCourses) {
                if(suggestedCoursesCount == suggestedCoursesCountLimit)
                {
                    break SuggestedCourseLoop;

                }
                else
                {
                    RecommendedCourseVO recommededCourseVO = new RecommendedCourseVO();
                    recommededCourseVO = coursesVO;
                    maxRecommededCourses.add(recommededCourseVO);
                    suggestedCoursesCount = suggestedCoursesCount + 1;
                }
            }

            context.put("COURSE", maxRecommededCourses);
            context.put("FirstName", user.getFirstName());

            return new ModelAndView("learner/recommendedCoursesDetail", "context", context);
            //return new ModelAndView("learner/recommendedCoursesDetail", "context", context);


        } catch (Exception e) {
            log.debug("exception", e);
        }

        return new ModelAndView("learner/recommendedCoursesDetail", "context", recommendedCourses);

    }
    public  List<RecommendedCourseVO> dummyData()
    {
        List<RecommendedCourseVO> recommendedCourseVO = new ArrayList<RecommendedCourseVO>();

        RecommendedCourseVO rc1 = new RecommendedCourseVO();
        RecommendedCourseVO rc2 = new RecommendedCourseVO();
        RecommendedCourseVO rc3 = new RecommendedCourseVO();
        RecommendedCourseVO rc4 = new RecommendedCourseVO();
        RecommendedCourseVO rc5 = new RecommendedCourseVO();
        RecommendedCourseVO rc6 = new RecommendedCourseVO();
        RecommendedCourseVO rc7 = new RecommendedCourseVO();
        RecommendedCourseVO rc8 = new RecommendedCourseVO();
        RecommendedCourseVO rc9 = new RecommendedCourseVO();
        RecommendedCourseVO rc10 = new RecommendedCourseVO();
        RecommendedCourseVO rc11 = new RecommendedCourseVO();
        RecommendedCourseVO rc12 = new RecommendedCourseVO();
        RecommendedCourseVO rc13 = new RecommendedCourseVO();
        RecommendedCourseVO rc14 = new RecommendedCourseVO();
        RecommendedCourseVO rc15 = new RecommendedCourseVO();
      /*  RecommendedCourseVO rc16 = new RecommendedCourseVO();
        RecommendedCourseVO rc17 = new RecommendedCourseVO();
        RecommendedCourseVO rc18 = new RecommendedCourseVO();
        RecommendedCourseVO rc19 = new RecommendedCourseVO();
        RecommendedCourseVO rc20 = new RecommendedCourseVO();
        RecommendedCourseVO rc21 = new RecommendedCourseVO();
        RecommendedCourseVO rc22 = new RecommendedCourseVO();
     */
        rc1.setCourseGuidFrom("RC1");
        rc1.setCourseName("RC11");
        rc1.setCourseGuidTo("RC13");

        rc2.setCourseGuidFrom("RC1");
        rc2.setCourseName("RC21");
        rc2.setCourseGuidTo("RC22");
        rc2.setCourseGuidTo("RC23");

        rc3.setCourseGuidFrom("RC1");
        rc3.setCourseName("RC31");
        rc3.setCourseGuidTo("RC32");
        rc3.setCourseGuidTo("RC33");

        rc4.setCourseGuidFrom("RC41");
        rc4.setCourseName("RC41");
        rc4.setCourseGuidTo("RC42");
        rc4.setCourseGuidTo("RC43");

        rc5.setCourseGuidFrom("RC41");
        rc5.setCourseName("RC51");
        rc5.setCourseGuidTo("RC52");
        rc5.setCourseGuidTo("RC53");

        rc6.setCourseGuidFrom("RC6");
        rc6.setCourseName("RC61");
        rc6.setCourseGuidTo("RC62");
        rc6.setCourseGuidTo("RC63");

        rc7.setCourseGuidFrom("RC6");
        rc7.setCourseName("RC71");
        rc7.setCourseGuidTo("RC72");
        rc7.setCourseGuidTo("RC73");

        rc8.setCourseGuidFrom("RC6");
        rc8.setCourseName("RC81");
        rc8.setCourseGuidTo("RC82");
        rc8.setCourseGuidTo("RC83");

        rc9.setCourseGuidFrom("RC6");
        rc9.setCourseName("RC91");
        rc9.setCourseGuidTo("RC92");
        rc9.setCourseGuidTo("RC93");

        rc10.setCourseGuidFrom("RC10");
        rc10.setCourseName("RC101");
        rc10.setCourseGuidTo("RC102");
        rc10.setCourseGuidTo("RC103");

        rc11.setCourseGuidFrom("RC11");
        rc11.setCourseName("RC111");
        rc11.setCourseGuidTo("RC112");
        rc11.setCourseGuidTo("RC113");

        rc12.setCourseGuidFrom("RC12");
        rc12.setCourseName("RC121");
        rc12.setCourseGuidTo("RC122");
        rc12.setCourseGuidTo("RC123");

        rc13.setCourseGuidFrom("RC13");
        rc13.setCourseName("RC131");
        rc13.setCourseGuidTo("RC132");
        rc13.setCourseGuidTo("RC133");

        rc14.setCourseGuidFrom("RC14");
        rc14.setCourseName("RC141");
        rc14.setCourseGuidTo("RC142");
        rc14.setCourseGuidTo("RC143");

        rc15.setCourseGuidFrom("RC15");
        rc15.setCourseName("RC151");
        rc15.setCourseGuidTo("RC152");
        rc15.setCourseGuidTo("RC153");
/*
        rc16.setCourseGuidFrom("RC16");
        rc16.setCourseName("RC161");
        rc16.setCourseGuidTo("RC162");
        rc16.setCourseGuidTo("RC163");

        rc17.setCourseGuidFrom("RC17");
        rc17.setCourseName("RC171");
        rc17.setCourseGuidTo("RC172");
        rc17.setCourseGuidTo("RC173");

        rc18.setCourseGuidFrom("RC18");
        rc18.setCourseName("RC181");
        rc18.setCourseGuidTo("RC182");
        rc18.setCourseGuidTo("RC183");

        rc19.setCourseGuidFrom("RC19");
        rc19.setCourseName("RC191");
        rc19.setCourseGuidTo("RC192");
        rc19.setCourseGuidTo("RC193");

        rc20.setCourseGuidFrom("RC20");
        rc20.setCourseName("RC201");
        rc20.setCourseGuidTo("RC202");
        rc20.setCourseGuidTo("RC203");

        rc21.setCourseGuidFrom("RC21");
        rc21.setCourseName("RC211");
        rc21.setCourseGuidTo("RC212");
        rc21.setCourseGuidTo("RC213");
*/
        recommendedCourseVO.add(rc1);
        recommendedCourseVO.add(rc2);
        recommendedCourseVO.add(rc3);
        recommendedCourseVO.add(rc4);
        recommendedCourseVO.add(rc5);
        recommendedCourseVO.add(rc6);
        recommendedCourseVO.add(rc7);
        recommendedCourseVO.add(rc8);
        recommendedCourseVO.add(rc9);
        recommendedCourseVO.add(rc10);
        recommendedCourseVO.add(rc11);
        recommendedCourseVO.add(rc12);
        recommendedCourseVO.add(rc13);
        recommendedCourseVO.add(rc14);
        recommendedCourseVO.add(rc15);
   /*     recommendedCourseVO.add(rc16);
        recommendedCourseVO.add(rc17);
        recommendedCourseVO.add(rc18);
        recommendedCourseVO.add(rc19);
        recommendedCourseVO.add(rc20);
        recommendedCourseVO.add(rc21);
*/
        return recommendedCourseVO;
    }
	public void afterPropertiesSet() throws Exception {
		// Auto-generated method stub
	}
	
	
    public CourseAndCourseGroupService getCourseAndCourseGroupService() {
    	return courseAndCourseGroupService;
        }

        public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
    	this.courseAndCourseGroupService = courseAndCourseGroupService;
        }
	
}
