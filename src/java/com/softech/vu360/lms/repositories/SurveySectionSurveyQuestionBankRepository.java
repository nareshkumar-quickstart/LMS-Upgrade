/**
 * 
 */
package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.SurveySectionSurveyQuestionBank;
import com.softech.vu360.lms.model.SurveySectionSurveyQuestionBankPK;

/**
 * @author muhammad.junaid
 *
 */
public interface SurveySectionSurveyQuestionBankRepository extends CrudRepository<SurveySectionSurveyQuestionBank, SurveySectionSurveyQuestionBankPK>,SurveySectionSurveyQuestionBankCustom {

}
