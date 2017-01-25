package com.softech.vu360.lms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.SurveyAnswerItem;

public interface SurveyAnswerItemRepository extends CrudRepository<SurveyAnswerItem, Long> {
	List<SurveyAnswerItem> findByIdIn(List<Long> surveyAnswerItemArray);
}
