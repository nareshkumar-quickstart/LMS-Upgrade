package com.softech.vu360.lms.util;

import java.util.Set;

import org.apache.commons.collections.Predicate;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CustomerEntitlement;

/**
 * User: joong
 * Date: Feb 22, 2010
 */
public class CoursePredicate implements Predicate {
    private String businessKey;

    public CoursePredicate(String key) {
        businessKey = key;
    }

    public boolean evaluate(Object o) {
        boolean result = false;
        if (o instanceof CustomerEntitlement) {
            CustomerEntitlement ce = (CustomerEntitlement) o;
            result = doesBusinessKeyMatch(ce.getUniqueCourses());
        }
        return result;
    }

    private boolean doesBusinessKeyMatch(Set<Course> courses) {
        boolean result = false;
        for (Course course : courses) {
            if (course.getBussinesskey() != null) {
                if (course.getBussinesskey().equalsIgnoreCase(businessKey)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}