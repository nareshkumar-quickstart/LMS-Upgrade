package com.softech.vu360.lms.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerEnrollment;

/**
 * Value object for enrollment controller
 * @author joong
 * @since LMS-3212
 */
public class EnrollmentVO {
    private int enrollmentsUpdated;
    private int courseNumber;
    private int enrollmentsCreated;
    private List<LearnerEnrollment> learnerEnrollments = new ArrayList<LearnerEnrollment>();
    private List<LearnerEnrollment> enrollmentsToBeModified = new ArrayList<LearnerEnrollment>();
    private List<Learner> learnersFailedToEnroll = new ArrayList<Learner>();
    private HashSet<Learner> uniqueLearners = new HashSet<Learner>();
    private HashSet<Course> uniqueCourses = new HashSet<Course>();
    private HashMap<Long,CustomerEntitlement> customerContractMap = new HashMap<Long,CustomerEntitlement>();
    private Map<Long, Integer> orgGroupNumberOfseatUsed = new HashMap<Long, Integer>();  //  [10/13/2010] LMS-7196 :: Update Org. Group Contract Seats Used on Learner Enrollment
    
    public int getEnrollmentsUpdated() {
        return enrollmentsUpdated;
    }

    public void setEnrollmentsUpdated(int enrollmentsUpdated) {
        this.enrollmentsUpdated = enrollmentsUpdated;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public int getEnrollmentsCreated() {
        return enrollmentsCreated;
    }

    public void setEnrollmentsCreated(int enrollmentsCreated) {
        this.enrollmentsCreated = enrollmentsCreated;
    }

    public List<LearnerEnrollment> getLearnerEnrollments() {
        return learnerEnrollments;
    }

    public void setLearnerEnrollments(List<LearnerEnrollment> learnerEnrollments) {
        this.learnerEnrollments = learnerEnrollments;
    }

    public List<LearnerEnrollment> getEnrollmentsToBeModified() {
        return enrollmentsToBeModified;
    }

    public void setEnrollmentsToBeModified(List<LearnerEnrollment> enrollmentsToBeModified) {
        this.enrollmentsToBeModified = enrollmentsToBeModified;
    }

    public List<Learner> getLearnersFailedToEnroll() {
        return learnersFailedToEnroll;
    }

    public void setLearnersFailedToEnroll(List<Learner> learnersFailedToEnroll) {
        this.learnersFailedToEnroll = learnersFailedToEnroll;
    }

    public HashSet<Learner> getUniqueLearners() {
        return uniqueLearners;
    }

    public void setUniqueLearners(HashSet<Learner> uniqueLearners) {
        this.uniqueLearners = uniqueLearners;
    }

    public HashSet<Course> getUniqueCourses() {
        return uniqueCourses;
    }

    public void setUniqueCourses(HashSet<Course> uniqueCourses) {
        this.uniqueCourses = uniqueCourses;
    }

	public HashMap<Long, CustomerEntitlement> getCustomerContractMap() {
		return customerContractMap;
	}

	public void setCustomerContractMap(
			HashMap<Long, CustomerEntitlement> customerContractMap) {
		this.customerContractMap = customerContractMap;
	}

	/**
	 * //  [10/13/2010] LMS-7196 :: Update Org. Group Contract Seats Used on Learner Enrollment
	 * @param orgGroupNumberOfseatUsed the orgGroupNumberOfseatUsed to set
	 */
	public void setOrgGroupNumberOfseatUsed(Map<Long, Integer> orgGroupNumberOfseatUsed) {
		this.orgGroupNumberOfseatUsed = orgGroupNumberOfseatUsed;
	}

	/**
	 * //  [10/13/2010] LMS-7196 :: Update Org. Group Contract Seats Used on Learner Enrollment
	 * @return the orgGroupNumberOfseatUsed
	 */
	public Map<Long, Integer> getOrgGroupNumberOfseatUsed() {
		return orgGroupNumberOfseatUsed;
	}
}