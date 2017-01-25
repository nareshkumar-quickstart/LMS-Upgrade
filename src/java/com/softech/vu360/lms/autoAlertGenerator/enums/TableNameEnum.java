package com.softech.vu360.lms.autoAlertGenerator.enums;

/*
* When a queue creates,
* it has table name which
* attend to a queue details
*
* @author ramiz.uddin
* @version 0.1 Oct 15, 2012
* */
public enum TableNameEnum {

    AlertTriggerTable("AlertTriggerTable"),
    LearnerCourseStatistics("LEARNERENROLLMENT"),
    LearnerEnrollment("LEARNERENROLLMENT"),
    VU360User("VU360USER");

    public String tableName;

    // A Trick to add string with
    // each enum value
    private TableNameEnum(String tableName) {
        this.tableName = tableName;
    }

}
