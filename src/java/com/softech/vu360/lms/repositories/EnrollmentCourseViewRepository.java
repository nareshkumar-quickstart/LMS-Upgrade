package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;


import com.softech.vu360.lms.model.EnrollmentCourseView;

public interface EnrollmentCourseViewRepository extends CrudRepository<EnrollmentCourseView, Long>, EnrollmentCourseViewRepositoryCustom  {

}
