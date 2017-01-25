package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CreditReportingField;
import com.softech.vu360.lms.model.CreditReportingFieldValue;
import com.softech.vu360.lms.model.CustomReportingField;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerGroupItem;
import com.softech.vu360.lms.model.LearnerProfile;
import com.softech.vu360.lms.service.AccreditationService;
import com.softech.vu360.lms.service.LearnerService;
import com.softech.vu360.util.VU360Properties;

/**
 * Provides various helper methods for NERC related activities for 
 * batch import process.
 * 
 * @author abdul.aziz
 */
public class NERCBatchImportUtil {

    private static final Log logger = LogFactory.getLog(NERCBatchImportUtil.class);
    
    //This property must be defined in vu360-lms.properties file if required.
    private static final String NERC_FIELD_PROPERTY_NAME = "learner.batchimport.nerc.creditReportingFieldId";
    
    /**
     * Checks the NERC {@link CustomReportingField}. If the {@link CustomReportingField} whose id
     * is represented by the system property <strong>learner.batchimport.nerc.creditReportingFieldId</strong>.
     * 
     * @param accreditationService used to look up {@link CustomReportingField}.
     * 
     * @return an instance of {@link CustomReportingField} if id represented by system property 
     *  learner.batchimport.nerc.creditReportingFieldId is found else null.
     */
    public static CreditReportingField getNERCFieldIfExist(AccreditationService accreditationService) {
        CreditReportingField NERCField = null;

        try {
            OptimizedBatchImportLearnersServiceImpl.logMethod("checkNercFieldValue", true);
            String nercId = VU360Properties.getVU360Property(NERC_FIELD_PROPERTY_NAME);
            logger.debug("nercId = " + nercId);

            if (StringUtils.isNumeric(nercId) && !StringUtils.isEmpty(nercId)) {
                NERCField = accreditationService.getCreditReportingFieldById(Integer.valueOf(nercId));
                if (NERCField != null) {
                    logger.debug("nercField.id = " + NERCField.getId());
                    logger.debug("nercField.label = " + NERCField.getFieldLabel());
                    logger.debug("nercField.required = " + NERCField.isFieldRequired());
                    logger.debug("nercField.active = " + NERCField.isActive());
                }
                return NERCField;
            }
        } finally {
            OptimizedBatchImportLearnersServiceImpl.logMethod("checkNercFieldValue", false);
        }
        return null;
    }

    /**
     * Iterates over the headers to determine NERC column index.
     * If not found returns -1.
     * 
     * @param columnLabels column labels found in the CSV file.
     * @param NERCField used to match column header label with NERC field label.
     * @return -1 if the column is not found else the index of the column.
     */
    public static int deteremineNERCColumnIndex(String[] columnLabels, CreditReportingField NERCField) {
        int index = -1;
        for (int i = 0; i < columnLabels.length; i++) {
            String label = columnLabels[i];

            if (NERCField!=null && label!=null && label.equalsIgnoreCase(NERCField.getFieldLabel())) {
                index = i;
                break;
            }
        }

        return index;
    }    

    /**
     * Tries to find a course with the given NERC field in the learner groups.
     * @param learnerGroups
     * @param NERCField
     * @param accreditationService
     * @return true if it finds the learner group with a course associated with 
     * the NERC field else false.
     */
    public static boolean courseWithNERCFieldExists(Set<LearnerGroup> learnerGroups,
            CreditReportingField NERCField,
            AccreditationService accreditationService) {
        logger.debug("Looking up a NERC field in the learner groups.");
        
        boolean exists = false;
        
        if(CollectionUtils.isEmpty(learnerGroups) == false){
            for (LearnerGroup learnerGroup : learnerGroups) {
                Course course = getCourseWithNERCField(
                    learnerGroup, NERCField, accreditationService);
                if( null != course){
                    exists = true;
                    break;
                }
            }
        }
        
        logger.debug("NERC field found in the learner groups? " + exists);
        return exists;
    }
    
    /**
     * Recursive method that iterates over the courses and course groups in the 
     * learner group to look for any associated NERC reporting field.
     * 
     * @param learnerGroup 
     * @param NERCField 
     * @param accreditationService 
     * 
     * @return course if found else null
     */
    public static Course getCourseWithNERCField(LearnerGroup learnerGroup,
            CreditReportingField NERCField,
            AccreditationService accreditationService) {
        if (null == learnerGroup) {
            throw new IllegalArgumentException("Please provide valid learner group.");
        }
        if (null == NERCField) {
            throw new IllegalArgumentException("Please provide valid NERC field.");
        }
        if (null == accreditationService) {
            throw new IllegalArgumentException("Please provide valid accreditation service.");
        }

        logger.debug("Looking up a course with NERC field in learner group: " + 
            learnerGroup.getName());
        
        Course courseWithNERCField = null;
        List<LearnerGroupItem> learnerGroupItems = learnerGroup.getLearnerGroupItems();
        
        for (LearnerGroupItem learnerGroupItem : learnerGroupItems) {
            Course course = learnerGroupItem.getCourse();
            courseWithNERCField = getCourseWithNERCField(
                course, NERCField, accreditationService);
            
            //NERC field was not found on the learner group course, we will
            //have to look for it in the associated course group(s).
            if (null == courseWithNERCField && learnerGroupItem.getCourseGroup()!=null) {
                CourseGroup courseGroup = learnerGroupItem.getCourseGroup();
                courseWithNERCField = getCourseWithNERCFieldFromCourseGroup(
                    courseGroup, NERCField, accreditationService);
            }else{
                break;
            }
        }

        return courseWithNERCField;
    }

    /**
     * Method will recursively look for, in the courseGroup or its children,
     * a course with given NERC field.
     * 
     * @param courseGroup 
     * @param NERCField 
     * @param accreditationService 
     * @return valid course if a NERC credit reporting is found to be associated
     * it else null.
     */
    private static Course getCourseWithNERCFieldFromCourseGroup(CourseGroup courseGroup,
            CreditReportingField NERCField,
            AccreditationService accreditationService) {
        
        Course courseWithNERCField = null;

        List<Course> courses = courseGroup.getCourses();
        for (Course course : courses) {
            courseWithNERCField = getCourseWithNERCField(
                course, NERCField, accreditationService);
            
            //Course is found break it up and return.
            if( null != courseWithNERCField){
                break;
            }
        }
        
        //NERC field was not found in courses within the given course group, 
        //lets recursively search course groups childrens.
        if( courseWithNERCField == null ){
            List<CourseGroup> childrenCourseGroups = courseGroup.getChildrenCourseGroups();
            for (CourseGroup childCourseGroup : childrenCourseGroups) {
                courseWithNERCField = getCourseWithNERCFieldFromCourseGroup(
                    childCourseGroup, NERCField, accreditationService);
                if( courseWithNERCField != null)
                    break;
            }
        }

        return courseWithNERCField;
    }

    /**
     * Searches the credit reporting fields for the given NERC 
     * field for given course.
     * 
     * @param course
     * @param NERCField
     * @param accreditationService
     * @return 
     */
    private static Course getCourseWithNERCField(Course course, 
        CreditReportingField NERCField, 
        AccreditationService accreditationService) {
        
        Course courseWithNERCField = null;
        List<CreditReportingField> creditReportingFields = new ArrayList<CreditReportingField>(accreditationService.getCreditReportingFieldsByCourse(course));
        
        for (CreditReportingField field : creditReportingFields) {
            if (field.getFieldLabel().equalsIgnoreCase(NERCField.getFieldLabel())) {
                courseWithNERCField = course;
                break;
            }
        }

        return courseWithNERCField;
    }
    
    /**
     * Validates the NERC credit reporting field data coming from the source file.
     * <p>
     * Right now there is only one validation rule that checks whether the source
     * file contain data or not, if NERC credit reporting field is active 
     * and required.
     * <p>
     * 
     * @param NERCField 
     * @param sourceData
     * @param columnIndex
     * @return 
     */
    public static boolean isSourceNERCCreditReportingFieldValueValid(
        CreditReportingField NERCField, String[] sourceData, int columnIndex){
        if( NERCField == null){
            throw new IllegalArgumentException("Please provide NERC credit reporting field.");
        }
        if( ArrayUtils.isEmpty(sourceData)){
            throw new IllegalArgumentException("Please provide source data.");
        }
        /*if( columnIndex < 0){
            throw new IllegalArgumentException("Please provide NERC column index " + 
                "in the soruce file.");
        }*/
        
        boolean isValid = true;
        if(columnIndex > -1) {
        	String NERCData = sourceData[columnIndex];
            logger.debug("NERC data: " + NERCData);
            if( NERCField.isFieldRequired() && NERCField.isActive()){
                if( StringUtils.isEmpty(NERCData)){
                    isValid = false;
                }
            }
        } else if(columnIndex == -1)
        	isValid = false;
        
        return isValid;
    }
    
    /**
     * Iterates over the learner groups recursively, look for any course with associated NERC 
     * Credit Reporting Field, if found, updates its value with newValue.
     * 
     * @param learnerGroups 
     * @param NERCReportingField 
     * @param learnerProfile 
     * @param learnerGroups 
     */
    public static void updateNERCCreditFieldValue(Collection<LearnerGroup> learnerGroups, 
        CreditReportingField NERCReportingField, 
        String newValue, 
        LearnerProfile learnerProfile,
        AccreditationService accreditationService,
        LearnerService learnerService){
        
        updateNERCCreditReportingValue( 
            NERCReportingField, 
            learnerProfile,
            newValue,
            learnerService);
    }

    /**
     * Updates the credit reporting field value with newValue.
     * @param NERCReportingField
     * @param learnerProfile
     * @param newValue
     * @param learnerService 
     */
    private static void updateNERCCreditReportingValue(CreditReportingField NERCReportingField, 
            LearnerProfile learnerProfile,
            String newValue,
            LearnerService learnerService) {
        logger.debug("Updating NERC field: " + NERCReportingField);
        List<CreditReportingFieldValue> customFieldValueList = learnerService.getCreditReportingFieldValues(
            learnerProfile.getLearner());
        
        logger.debug("Looking up NERC field value.");
        CreditReportingFieldValue NERCFieldValue = null;
        for (CreditReportingFieldValue value : customFieldValueList) {
            if (value.getReportingCustomField().getId().longValue() == NERCReportingField.getId().longValue()) {
                NERCFieldValue = value;
            }
        }
        
        if( null != NERCFieldValue){
            logger.debug("NERC field value found");
            NERCFieldValue.setValue(newValue);
            learnerService.saveCreditReportingfieldValue(NERCFieldValue);
        }else{
            logger.debug("NERC field value not found, insert a new one.");
            saveNERCFieldValue(learnerProfile, NERCReportingField, newValue, learnerService);
        }
    }
    
    /**
     * Saves the new NERC value with the learner profile.
     * 
     * @param learnerProfile
     * @param NERCField
     * @param NERCFieldValue
     * @param learnerService 
     */
    public static void saveNERCFieldValue(LearnerProfile learnerProfile, 
        CreditReportingField NERCField, 
        String NERCFieldValue, 
        LearnerService learnerService){
        
        logger.debug("Saving new value for NERC field.");
        CreditReportingFieldValue value = new CreditReportingFieldValue();
        value.setLearnerprofile(learnerProfile);
        value.setReportingCustomField(NERCField);
        value.setValue(NERCFieldValue);
        
        learnerService.saveCreditReportingfieldValue(value);
        logger.debug("NERC field value saved.");
    }
}
