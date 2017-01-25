package com.softech.vu360.lms.repositories;

import java.util.List;
import java.util.Vector;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.SynchronousClass;

public interface SynchronousClassRepository extends
		CrudRepository<SynchronousClass, Long>,
		SynchronousClassRepositoryCustom {

	public List<SynchronousClass> findByCourse_Id(Long id);

	public List<SynchronousClass> findByCourse_IdIn(Vector<Long> ids);

	public SynchronousClass findByGuid(String guid);
	
	//public void deleteByIdIn(List<Long> ids ); Committed to be removed as its not working, changed to JPA customized Method LMS-19387/ Please note that this is working while run via Junit
	
	
	@Query(value = "SELECT DISTINCT t1.ID, t1.MEETING_ID, t1.AUTOMATIC, t1.MEETINGPASSCODE, t1.lOCATION_ID, t1.CLASSSTARTDATE, t1.MEETINGTYPE, t1.CLASSSTATUS, t1.CLASSNAME, t1.ENROLLMENTCLOSEDATE, t1.MINIMUMCLASSSIZE, t1.CLASSENDDATE, t1.PRESENTER_EMAIL_ADDRESS, t1.MAXIMUMCLASSSIZE, t1.PRESENTER_FIRST_NAME, t1.GUID, t1.PRESENTER_LAST_NAME, t1.COURSE_ID, t1.TIMEZONE_ID FROM LMSRESOURCE t0, SYNCHRONOUSCLASS_LMSRESOURCE t2, SYNCHRONOUSCLASS t1 WHERE ((t0.ID = ?) AND ((t2.SYNCHRONOUSCLASS_ID = t1.ID) AND (t0.ID = t2.LMSRESOURCE_ID)))",nativeQuery = true)
	public List<SynchronousClass> findResourceAssociatedSynchronousClass(Long resourceId);
	
}
