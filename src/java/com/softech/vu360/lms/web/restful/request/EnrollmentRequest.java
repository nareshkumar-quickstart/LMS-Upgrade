package com.softech.vu360.lms.web.restful.request;

public class EnrollmentRequest {

    private String userName;
    private String courseId;
    private String subscriptionId;
    private String courseGroupGUID;

    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getUserName(){
        return this.userName;
    }
    public void setCourseId(String courseId){
        this.courseId = courseId;
    }
    public String getCourseId(){
        return this.courseId;
    }
    public void setSubscriptionId(String subscriptionId){
        this.subscriptionId = subscriptionId;
    }
    public String getSubscriptionId(){
        return this.subscriptionId;
    }
    public String getCourseGroupGUID() {
        return courseGroupGUID;
    }
    public void setCourseGroupGUID(String courseGroupGUID) {
        this.courseGroupGUID = courseGroupGUID;
    }
}
