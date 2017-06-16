package com.softech.vu360.lms.web.controller.learner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.softech.vu360.lms.model.LearnerProfileStaticField;
import com.softech.vu360.lms.vo.VU360User;


/**
 * 
 * @author haider.ali
 * @version 1.0
 */
public class ControllerUtils {

	
	 private ControllerUtils() {
		 
	 }

	public static Boolean isValidUserOrSession(HttpServletRequest request, HttpServletResponse response){
     	VU360User vu360User = (VU360User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 	    String lguid = request.getParameter("lguid");
 	    return (!StringUtils.isEmpty(lguid) && !(vu360User.getUserGUID().equalsIgnoreCase(lguid))) ? true : false;
     }
	
	/**
	 * //LMS-22392
	 * @param f
	 */
	public static void assignWeightToField(com.softech.vu360.lms.model.CreditReportingField f){
		
		if(f.getFieldLabel().equals(LearnerProfileStaticField.FIRST_NAME.toString())){ f.setWeight(1);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.MIDDLE_NAME.toString())){	f.setWeight(2);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.EMAIL.toString())){	f.setWeight(3);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.PHONE.toString())){	f.setWeight(4);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.OFFICE_PHONE.toString())){ f.setWeight(5);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.OFFICE_EXTN.toString())){	f.setWeight(6);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_1_LINE_1.toString())){f.setWeight(7);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_1_LINE_2.toString())){	f.setWeight(8);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.CITY.toString())){f.setWeight(9);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.STATE.toString())){f.setWeight(10);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.ZIP_CODE.toString())){f.setWeight(11);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.COUNTRY.toString())){ f.setWeight(12);
		}else if(f.getFieldLabel().equals(LearnerProfileStaticField.ADDRESS_2_LINE_1.toString())){f.setWeight(13);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.ADDRESS_2_LINE_2.toString())){f.setWeight(14);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.CITY_2.toString())){f.setWeight(15);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.STATE_2.toString())){	f.setWeight(16);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.ZIP_CODE_2.toString())){f.setWeight(17);
		}else if(f.getFieldLabel().equalsIgnoreCase(LearnerProfileStaticField.COUNTRY_2.toString())){f.setWeight(18);
		}
		}

	 
}
