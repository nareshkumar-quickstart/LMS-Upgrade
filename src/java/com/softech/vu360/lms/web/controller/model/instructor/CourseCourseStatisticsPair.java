package com.softech.vu360.lms.web.controller.model.instructor;

import java.util.Date;

import com.softech.vu360.lms.web.controller.ILMSBaseInterface;

/**
 * @author Dyutiman
 * created on 13 Apr 2010
 * Used as a wrapper class to show the status of courses in Instructor mode learner Transcript.
 * It stores a course's name and corresponding course statistics details.
 *
 */
public class CourseCourseStatisticsPair  implements ILMSBaseInterface{
	private static final long serialVersionUID = 1L;
	private String courseName = "";
	private boolean courseComplete = false;
	private Date complitionDate = null;
	private Date firstAccessDate = null;
	private Date lastAccessDate = null;
	private double percentComplete = 0;
	private double preTestScore = 0;
	private double averageQuizScore = 0;
	private String preTestPassed = "";
	private double higestPostScore = 0;
	private String postTestPassed = "";
	private double quizes = 0;
	private String courseStatus = "";
	private int numberPostTestsTaken = 0;
	private int totalTimeInSeconds = 0;
	private double numberQuizesTaken = 0;
	
	
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public boolean isCourseComplete() {
		return courseComplete;
	}
	public void setCourseComplete(boolean courseComplete) {
		this.courseComplete = courseComplete;
	}
	public Date getComplitionDate() {
		return complitionDate;
	}
	public void setComplitionDate(Date complitionDate) {
		this.complitionDate = complitionDate;
	}
	public double getPercentComplete() {
		return percentComplete;
	}
	public void setPercentComplete(double percentComplete) {
		this.percentComplete = percentComplete;
	}
	public double getPreTestScore() {
		return preTestScore;
	}
	public void setPreTestScore(double preTestScore) {
		this.preTestScore = preTestScore;
	}
	public String getPreTestPassed() {
		return preTestPassed;
	}
	public void setPreTestPassed(String preTestPassed) {
		this.preTestPassed = preTestPassed;
	}
	public double getHigestPostScore() {
		return higestPostScore;
	}
	public void setHigestPostScore(double higestPostScore) {
		this.higestPostScore = higestPostScore;
	}
	public String getPostTestPassed() {
		return postTestPassed;
	}
	public void setPostTestPassed(String postTestPassed) {
		this.postTestPassed = postTestPassed;
	}
	public double getQuizes() {
		return quizes;
	}
	public void setQuizes(double quizes) {
		this.quizes = quizes;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public Date getFirstAccessDate() {
		return firstAccessDate;
	}
	public void setFirstAccessDate(Date firstAccessDate) {
		this.firstAccessDate = firstAccessDate;
	}
	public Date getLastAccessDate() {
		return lastAccessDate;
	}
	public void setLastAccessDate(Date lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}
	public double getAverageQuizScore() {
		return averageQuizScore;
	}
	public void setAverageQuizScore(double averageQuizScore) {
		this.averageQuizScore = averageQuizScore;
	}
	public int getNumberPostTestsTaken() {
		return numberPostTestsTaken;
	}
	public void setNumberPostTestsTaken(int numberPostTestsTaken) {
		this.numberPostTestsTaken = numberPostTestsTaken;
	}
	public int getTotalTimeInSeconds() {
		return totalTimeInSeconds;
	}
	public void setTotalTimeInSeconds(int totalTimeInSeconds) {
		this.totalTimeInSeconds = totalTimeInSeconds;
	}
	public double getNumberQuizesTaken() {
		return numberQuizesTaken;
	}
	public void setNumberQuizesTaken(double numberQuizesTaken) {
		this.numberQuizesTaken = numberQuizesTaken;
	}
	
}