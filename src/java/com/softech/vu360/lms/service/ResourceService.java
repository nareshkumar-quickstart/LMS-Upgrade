package com.softech.vu360.lms.service;

import java.util.HashSet;
import java.util.List;

import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.Gradebook;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.model.ResourceType;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.web.controller.model.instructor.ManageActivity;
import com.softech.vu360.lms.web.controller.model.instructor.ManageEnrolledLearner;
import com.softech.vu360.lms.web.controller.model.instructor.ManageStudentsFallingBehind;

public interface ResourceService {
	
	public List<Resource> findResources(Long instructorContentOwnerId,String resourceName,String tagNumber,String resourceType);
	public List<Resource> findAllResources(Long instructorContentOwnerId);
	public Resource saveResource(Resource resource);
	public Resource getResourceById(long resourceId);
	public void deleteResource(long resourceIdArray[]);
	//public List<SynchronousSession> findResourceAssociatedSynchronousSessions( String resourceId );
	public List<SynchronousClass> findResourceAssociatedSynchronousClass( String resourceId );
	
	public ResourceType saveResourceType(ResourceType resourceType);
	public ResourceType getResourceTypeById(long resourceTypeId);
	public void deleteResourceType(long resourceTypeIdArray[]);
	public List<ResourceType> getAllResourceTypes(long instructorContentOwnerId);
	
	public List<Location> findLocations(Long instructorContentOwnerId,String locationName,String city,String state,String country,String zip);
	public Location saveLocation(Location location);
	public Location getLocationById(long locationId);
	public void deleteLocation(long locationIdArray[]);
	public List<SynchronousClass> findLocationAssociatedSynchronousClass(String locationId);
	
	public CourseActivity saveCourseActivity(CourseActivity courseActivity);
	public CourseActivity getCourseActivityById(long courseActivityId);
	public void deleteCourseActivity(long courseActivityIdArray[]);
	public List<CourseActivity> findCourseActivities(String activityName);

	
	public List<Gradebook>findGradeBooks(long instructorId, String classname,String startDate,String endDate);
	public Gradebook getGradeBookById(long gradebookId);
	public HashSet<VU360User> findLearnersByInstructor(String firstName,String lastName,String email,long instructorId);
	public HashSet<ManageStudentsFallingBehind> getStudentsFallingBehindByInstructor(String firstName,String lastName,String email,long instructorId);
	public List<LearnerCourseActivity> getLearnerCourseActivitiesForCourseActivity(long synchronousClassId);
	public List<CourseActivity> findCourseActivitiesByGradeBook(long gradebookId);
	public List<CourseActivity> findCourseActivitiesByGradeBookAndName(String name, long gradebookId);
	
	public List<ManageActivity>  getActivityListByGradeBookByInstructor(long synchronousClassId,long instructorId,List<CourseActivity> courseActivities);
	public List<LearnerCourseActivity> getLearnerCourseActivitiesByCourseActivity(CourseActivity courseActivity);
	public HashSet<ManageEnrolledLearner> findSynchronousClassesLearnersByInstructor( String firstName, String lastName, String email, long instructorId,long synchronousClassesId );
	public LearnerCourseActivity saveLearnerCourseActivity(LearnerCourseActivity learnerCourseActivity);
	public List<LearnerEnrollment> findLearnerEnrolmentsByInstructor(long instructorId);
	public List<LearnerEnrollment> findCompletedEnrolmentsByInstructor(long instructorId);
	
	public CourseActivity loadForUpdateCourseActivity(long id);
	public Gradebook loadForUpdateGradebook(long id);
	public Location loadForUpdateLocation(long id);
	public ResourceType loadForUpdateResourceType(long id);
	public Resource loadForUpdateResource(long id);
	
}