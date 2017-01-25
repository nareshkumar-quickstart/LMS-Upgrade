package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Widget;

public interface WidgetRepository extends CrudRepository<Widget, Long> {

	Widget findByTitle(String title);

}
