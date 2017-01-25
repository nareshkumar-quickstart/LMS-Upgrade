package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.UserWidget;
import com.softech.vu360.lms.model.UserWidgetPK;
import com.softech.vu360.lms.model.VU360User;

public interface UserWidgetRepository extends CrudRepository<UserWidget, UserWidgetPK>, UserWidgetRepositoryCustom {

	UserWidget findByWidgetIdAndUserId(Long widgetId, Long userId);
	//List<UserWidget> findByUser(VU360User user);
	List<UserWidget> findByUserId(Long userId);

}
