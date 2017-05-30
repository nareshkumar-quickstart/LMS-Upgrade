package com.softech.vu360.lms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.AssignmentCourseActivity;
import com.softech.vu360.lms.model.CourseActivity;
import com.softech.vu360.lms.model.FinalScoreCourseActivity;
import com.softech.vu360.lms.model.Gradebook;
import com.softech.vu360.lms.model.InstructorSynchronousClass;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerAssignmentActivity;
import com.softech.vu360.lms.model.LearnerCourseActivity;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerFinalCourseActivity;
import com.softech.vu360.lms.model.LearnerLectureCourseActivity;
import com.softech.vu360.lms.model.LearnerSelfStudyCourseActivity;
import com.softech.vu360.lms.model.LectureCourseActivity;
import com.softech.vu360.lms.model.Location;
import com.softech.vu360.lms.model.Resource;
import com.softech.vu360.lms.model.ResourceType;
import com.softech.vu360.lms.model.SelfStudyCourseActivity;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousSession;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.CourseActivityRepository;
import com.softech.vu360.lms.repositories.CourseRepository;
import com.softech.vu360.lms.repositories.GradeBookRepository;
import com.softech.vu360.lms.repositories.InstructorSynchronousClassRepository;
import com.softech.vu360.lms.repositories.LearnerCourseActivityRepository;
import com.softech.vu360.lms.repositories.LearnerEnrollmentRepository;
import com.softech.vu360.lms.repositories.LocationRepository;
import com.softech.vu360.lms.repositories.ResourceRepository;
import com.softech.vu360.lms.repositories.ResourceTypeRepository;
import com.softech.vu360.lms.repositories.SynchronousClassRepository;
import com.softech.vu360.lms.service.ResourceService;
import com.softech.vu360.lms.web.controller.model.instructor.GradeBookForm;
import com.softech.vu360.lms.web.controller.model.instructor.ManageActivity;
import com.softech.vu360.lms.web.controller.model.instructor.ManageEnrolledLearner;
import com.softech.vu360.lms.web.controller.model.instructor.ManageStudentsFallingBehind;

public class ResourceServiceImpl implements ResourceService{
	
	private static final Logger log = Logger.getLogger(ResourceServiceImpl.class.getName());
	
	@Inject
	private ResourceRepository resourceRepository = null;
	
	@Inject
	private ResourceTypeRepository resourceTypeRepository = null;
	
	@Inject
	private LocationRepository locationRepository = null;
	
	@Inject
	private CourseActivityRepository courseActivityRepository = null;
	
	@Inject
	private GradeBookRepository gradeBookRepository = null;
	
	@Inject
	private LearnerCourseActivityRepository learnerCourseActivityRepository = null;
	
	@Inject
	private InstructorSynchronousClassRepository instructorSynchronousClassRepository = null;
	
	@Inject
	private LearnerEnrollmentRepository leanrEnrollmentRepository = null;
	
	
	@Inject
	private SynchronousClassRepository synchronousClassRepository = null;
	
	//private InstructorDAO instructorDAO = null;

	public List<Resource> findResources(Long instructorContentOwnerId, String resourceName, String tagNumber, String resourceType) {
		return resourceRepository.findContentOwnerIdandNameAndAssetTagNumberandResourceTypeIdandIsActive(instructorContentOwnerId,resourceName,tagNumber,Long.valueOf(resourceType),true);
	}

	public List<Resource> findAllResources(Long instructorContentOwnerId) {
		return resourceRepository.findByContentOwnerIdAndIsActive(instructorContentOwnerId,true);
	}

	public Resource saveResource(Resource resource){
		return resourceRepository.save(resource);
	}

	public Resource getResourceById(long resourceId){

		return resourceRepository.findOne(resourceId);
	}
	public void deleteResource(long resourceIdArray[]){
		List<Resource> resources = resourceRepository.findByIdIn(resourceIdArray);
		for(Resource resource : resources){
			resource.setActive(false);
			resourceRepository.save(resource);
		}
		

	}

	
	public List<SynchronousClass> findResourceAssociatedSynchronousClass( String resourceId ) {
		return synchronousClassRepository.findResourceAssociatedSynchronousClass(Long.valueOf(resourceId));
	}
	
	public ResourceType saveResourceType(ResourceType resourceType){

		return resourceTypeRepository.save(resourceType);
	}
	public ResourceType getResourceTypeById(long resourceTypeId){

		return resourceTypeRepository.findOne(resourceTypeId);
	}
	public void deleteResourceType(long resourceTypeIdArray[]){
		List<ResourceType> resouresourceTypes =   resourceTypeRepository.findByIdIn(resourceTypeIdArray);
		for(ResourceType obj : resouresourceTypes){
			
			obj.setActive(true);
			
		}
		
		
		resourceTypeRepository.save(resouresourceTypes);
	}
	public List<ResourceType> getAllResourceTypes(long instructorContentOwnerId){

		return resourceTypeRepository.findByContentOwnerIdAndIsActive(instructorContentOwnerId,true);
	}

	public List<Location> findLocations(Long instructorContentOwnerId,String locationName,String city,String state,String country,String zip){

		return locationRepository.findContentOwnerIdAndNameLikeAndAddressCityLikeAndAddressStateLikeAndAddressCountryLikeAndAddressZipcodeLikeAndIsActive(instructorContentOwnerId,locationName,city,state,country,zip,true);
	}
	public Location saveLocation(Location location){

		return locationRepository.save(location);

	}
	public Location getLocationById(long locationId){

		return locationRepository.findOne(locationId);
	}
	public void deleteLocation(long locationIdArray[]){

		List<Location> locations = locationRepository.findByIdIn(locationIdArray);
		for(Location obj : locations){
			obj.setActive(false);
		}
		
		locationRepository.save(locations);
	}
	
	public List<SynchronousClass> findLocationAssociatedSynchronousClass(String locationId){
		return synchronousClassRepository.findResourceAssociatedSynchronousClass(Long.valueOf(locationId));//instructorDAO.findLocationAssociatedSynchronousClass(locationId);
	}

	public CourseActivity saveCourseActivity(CourseActivity courseActivity){

		return courseActivityRepository.save(courseActivity);
	}
	public CourseActivity getCourseActivityById(long courseActivityId){

		return courseActivityRepository.findOne(courseActivityId);
	}
	@Transactional
	public void deleteCourseActivity(long courseActivityIdArray[]){

		courseActivityRepository.deleteByIdIn(courseActivityIdArray);
	}
	public List<CourseActivity> findCourseActivities(String activityName){

		return courseActivityRepository.findActivityNameLike(activityName);
	}

	public List<Gradebook> findGradeBooks(long instructorId, String classname,String startDate,String endDate){

		List<InstructorSynchronousClass> objs = instructorSynchronousClassRepository.findByInstructorId(instructorId);
		List<Long> synchronousClasses = new ArrayList();
		for(InstructorSynchronousClass obj : objs){
			synchronousClasses.add(obj.getSynchronousClass().getId());
			
		}
		List<Gradebook> gradebooks = new ArrayList();
		
		try{
		
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
	
			java.util.Date startRange =null; 
			java.util.Date endRange = null;
			startRange=format.parse(startDate);
			endRange = format.parse(endDate);
			
			gradebooks = gradeBookRepository.findsynchronousClassandsectionNameandstartDateandendDate(synchronousClasses,classname,startRange,endRange);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return gradebooks; 
		//return instructorDAO.findGradeBooks(instructorId, classname, startDate, endDate);
	}
	public Gradebook getGradeBookById(long gradebookId){

		return gradeBookRepository.findOne(gradebookId);
	}

	public HashSet<VU360User> findLearnersByInstructor( String firstName, String lastName, String email, long instructorId )
	{
		
		List<InstructorSynchronousClass> objs = instructorSynchronousClassRepository.findByInstructorId(instructorId);
		
		List<Long> synchronousClasses = null;
		if(objs != null && !objs.isEmpty()) {
			synchronousClasses = new ArrayList<>();
			for (InstructorSynchronousClass obj : objs) {
				synchronousClasses.add(obj.getSynchronousClass().getId());
			}
		}

		List<LearnerEnrollment> enrollments = leanrEnrollmentRepository.findSynchronousClassIdsInandfirstNameandLastNameandEmailAddress(synchronousClasses,firstName, lastName, email);//instructorDAO.findLearnersByInstructor(firstName, lastName, email, instructorId);
		HashSet<VU360User> searchedUsers = new HashSet<VU360User>();
		if( enrollments != null ) {
			for( LearnerEnrollment le : enrollments ) {
				searchedUsers.add(le.getLearner().getVu360User());
			}
		}
		return searchedUsers;
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ResourceService#getStudentsFallingBehind(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	public HashSet<ManageStudentsFallingBehind> getStudentsFallingBehindByInstructor(String firstName,String lastName, String email, long instructorId) {
		HashSet<ManageStudentsFallingBehind> students = new HashSet<ManageStudentsFallingBehind>();
		try {
			
			List<InstructorSynchronousClass> objs = instructorSynchronousClassRepository.findByInstructorId(instructorId);
			
			List<Long> synchronousClasses = new ArrayList();
			for(InstructorSynchronousClass obj : objs){
				synchronousClasses.add(obj.getSynchronousClass().getId());
				
			}
			
			if(!synchronousClasses.isEmpty()){
				List<LearnerEnrollment> enrollments = leanrEnrollmentRepository.findSynchronousClassIdsInandfirstNameandLastNameandEmailAddress(synchronousClasses,firstName, lastName, email);
				Date today = new Date();
				if( enrollments != null ) {
					for( LearnerEnrollment le : enrollments ) {
						Date lastAccessDate = null;
						lastAccessDate = le.getCourseStatistics().getLastAccessDate();
						long diff = today.getTime() - lastAccessDate.getTime();
						long totalDaysBehind = diff / (1000 * 60 * 60 * 24);
						if (totalDaysBehind > 0){
							ManageStudentsFallingBehind studentsFallingBehind = new ManageStudentsFallingBehind();
							studentsFallingBehind.setUser(le.getLearner().getVu360User());
							String days = (totalDaysBehind/7) + "w  " + (totalDaysBehind%7) + "d";
							studentsFallingBehind.setDays(days);
							students.add(studentsFallingBehind);
						}
					}
				}
			}

		} catch(Exception e) {
			log.debug(e);
		}
		return students;
	}

	public List<LearnerCourseActivity> getLearnerCourseActivitiesForCourseActivity(long synchronousClassId)
	{
		return learnerCourseActivityRepository.findByCourseActivityGradeBookSynchronousClassId(synchronousClassId);
	}
	
	public List<CourseActivity> findCourseActivitiesByGradeBook(long gradebookId) {
		return courseActivityRepository.findByGradeBookId(gradebookId);
	}

	public List<CourseActivity> findCourseActivitiesByGradeBookAndName(String name, long gradebookId)
	{
		List<CourseActivity> activityList =  this.findCourseActivitiesByGradeBook(gradebookId);
		List<CourseActivity> newActivityList =  new ArrayList<CourseActivity>();
		if( activityList != null ) {
			for( CourseActivity act : activityList ) {
				//if( act!=null && act.getActivityName() != null && act.getActivityName().contains(name) ) {
					newActivityList.add(act);
				//}
			}
		}
		return newActivityList;
	}

	@SuppressWarnings("unchecked")
	public List<ManageActivity>  getActivityListByGradeBookByInstructor(long synchronousClassId,long instructorId,List<CourseActivity> courseActivities)
	{
		List<String> objectTypes = new ArrayList<String>();
		for (int i=0;i<courseActivities.size();i++ )
		{
			if (courseActivities.get(i) instanceof SelfStudyCourseActivity) {
				objectTypes.add(GradeBookForm.SELF_STUDY_COURSE);

			}else if (courseActivities.get(i) instanceof FinalScoreCourseActivity) {

				objectTypes.add(GradeBookForm.FINAL_SCORE_COURSE);

			} else if (courseActivities.get(i) instanceof  AssignmentCourseActivity){
				objectTypes.add(GradeBookForm.ASSIGNMENT_COURSE);

			} 
			else if (courseActivities.get(i) instanceof  LectureCourseActivity){
				objectTypes.add(GradeBookForm.LECTURE_COURSE);


			}

		}
		List<LearnerCourseActivity> learnerCourseActivities=this.getLearnerCourseActivitiesForCourseActivity(synchronousClassId);
		Map <Learner, List<LearnerCourseActivity>> mapLearnerCourseActivity= new HashMap<Learner, List<LearnerCourseActivity>>();
		// implementing map for learners and  List<LearnerCourseActivity>
		for( LearnerCourseActivity learnerCourseActivity : learnerCourseActivities ) {

			if(!mapLearnerCourseActivity.containsKey(learnerCourseActivity.getLearnerCourseStatistics().getLearnerEnrollment().getLearner())){
				List<LearnerCourseActivity> learnerCourseActivityList=new ArrayList<LearnerCourseActivity>();
				learnerCourseActivityList.add(learnerCourseActivity);
				mapLearnerCourseActivity.put(learnerCourseActivity.getLearnerCourseStatistics().getLearnerEnrollment().getLearner(),learnerCourseActivityList);
			}
			else
			{
				mapLearnerCourseActivity.get(learnerCourseActivity.getLearnerCourseStatistics().getLearnerEnrollment().getLearner()).add(learnerCourseActivity);
			}
		}
		List<ManageActivity> manageLearnerActivities = new ArrayList<ManageActivity>();
		Iterator it = mapLearnerCourseActivity.entrySet().iterator();
		//int maxNoActivity = 0;
		while (it.hasNext()) {
			ManageActivity manageActivity= new ManageActivity();
			Map.Entry pairs = (Map.Entry)it.next();
			manageActivity.setLearner((Learner)pairs.getKey());
			List<LearnerCourseActivity> learnerCourseActivities1=(List<LearnerCourseActivity>)pairs.getValue();


			List<LearnerCourseActivity> finalLearnerCourseActivities = new ArrayList<LearnerCourseActivity>();

			for (int i=0;i<courseActivities.size();i++ )
			{
				finalLearnerCourseActivities.add(new LearnerCourseActivity());
			}
			int index=0;
			for (LearnerCourseActivity learnerCourseActivity: learnerCourseActivities1)
			{
				for(int i=0;i<courseActivities.size(); i++ )
				{
					if( learnerCourseActivity.getCourseActivity().getId().longValue() ==  courseActivities.get(i).getId().longValue())
					{
						index=i;
						break;
					}

				}
				if (learnerCourseActivity instanceof LearnerSelfStudyCourseActivity) {
					//manageActivity.addType(objectTypes.get(index));

					finalLearnerCourseActivities.set(index, learnerCourseActivity);
				}else if (learnerCourseActivity instanceof LearnerFinalCourseActivity) {

					//	manageActivity.addType(objectTypes.get(index));
					finalLearnerCourseActivities.set(index, learnerCourseActivity);
				} else if (learnerCourseActivity instanceof LearnerAssignmentActivity){
					//manageActivity.addType(GradeBookForm.ASSIGNMENT_COURSE);
					finalLearnerCourseActivities.set(index, learnerCourseActivity);
				} 
				else if (learnerCourseActivity instanceof LearnerLectureCourseActivity){
					//manageActivity.addType(GradeBookForm.LECTURE_COURSE);

					finalLearnerCourseActivities.set(index, learnerCourseActivity);
				}else
				{

					finalLearnerCourseActivities.set(index, null);
				}
				manageActivity.setTypes(objectTypes);
				//index++;
				//	 manageActivity.addLearnerCourseActivity(learnerCourseActivity);
			}
			manageActivity.setLearnerCourseActivities(finalLearnerCourseActivities);
			manageLearnerActivities.add(manageActivity);
		}
		// adding learners those do not have any activity
		List<LearnerEnrollment> enrollments =leanrEnrollmentRepository.findSynchronousClassIdInandfirstNameandLastNameandEmailAddress(synchronousClassId,"","","");// instructorDAO.findSynchronousClassesLearnersByInstructor("", "", "", instructorId, synchronousClassId);
	//	HashSet<VU360User> users= this.findLearnersByInstructor("", "","", instructorId );
		HashSet<VU360User> users = new HashSet<VU360User>();
		if( enrollments != null ) {
			for( LearnerEnrollment le : enrollments ) {
				users.add(le.getLearner().getVu360User());
			}
		}
		List<ManageActivity> manageLearnerActivities1 = new ArrayList<ManageActivity>();
		for( VU360User vu360User : users ) {
			Boolean isInList=false;  

			for( ManageActivity manageActivity3:manageLearnerActivities)
			{
				if(manageActivity3.getLearner().getId().longValue()== vu360User.getLearner().getId().longValue())
				{
					isInList=true;
					break;

				}
			}
			if(!isInList)
			{
				ManageActivity manageActivity2= new ManageActivity();
				manageActivity2.setLearner(vu360User.getLearner());
				List<LearnerCourseActivity> finalLearnerCourseActivities = new ArrayList<LearnerCourseActivity>();


				for (int i=0;i<courseActivities.size();i++ )
				{
					new LearnerCourseActivity();
					finalLearnerCourseActivities.add(new LearnerCourseActivity());
					String type="";
					manageActivity2.addType(type);

				}
				manageActivity2.setLearnerCourseActivities(finalLearnerCourseActivities);

				manageLearnerActivities1.add(manageActivity2);
			}

		}
		for( ManageActivity manageActivity3:manageLearnerActivities1)
		{
			manageLearnerActivities.add(manageActivity3);
		}
		return manageLearnerActivities;

	}
	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ResourceService#getLearnerCourseActivitiesByCourseActivity(com.softech.vu360.lms.model.CourseActivity)
	 */
	public List<LearnerCourseActivity> getLearnerCourseActivitiesByCourseActivity(CourseActivity courseActivity) {
		return learnerCourseActivityRepository.findByCourseActivityId(courseActivity.getId());
	}
	
	public HashSet<ManageEnrolledLearner> findSynchronousClassesLearnersByInstructor( String firstName, String lastName, String email, long instructorId,long synchronousClassesId )
	{
		List<LearnerEnrollment> enrollments = leanrEnrollmentRepository.findSynchronousClassIdInandfirstNameandLastNameandEmailAddress(synchronousClassesId,firstName, lastName, email);//instructorDAO.findSynchronousClassesLearnersByInstructor(firstName, lastName, email, instructorId, synchronousClassesId);
		HashSet<ManageEnrolledLearner> enrolledLearner = new HashSet<ManageEnrolledLearner>();
		if( enrollments != null ) {
			for( LearnerEnrollment le : enrollments ) {
				ManageEnrolledLearner mngEnrolledLearner = new ManageEnrolledLearner();
				mngEnrolledLearner.setUser(le.getLearner().getVu360User());
				mngEnrolledLearner.setLearnerEnrollment(le);
				enrolledLearner.add(mngEnrolledLearner);
			}
		}
		return enrolledLearner;
	}
	
	
	/*public InstructorDAO getInstructorDAO() {
		return instructorDAO;
	}
	public void setInstructorDAO(InstructorDAO instructorDAO) {
		this.instructorDAO = instructorDAO;
	}*/

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ResourceService#saveLearnerCourseActivity(com.softech.vu360.lms.model.LearnerCourseActivity)
	 */
	public LearnerCourseActivity saveLearnerCourseActivity(LearnerCourseActivity learnerCourseActivity) {
		
		return learnerCourseActivityRepository.save(learnerCourseActivity);
	}
	

	public List<LearnerEnrollment> findLearnerEnrolmentsByInstructor(long instructorId)
	{
		
		List<InstructorSynchronousClass> synchronousClasses = instructorSynchronousClassRepository.findByInstructorIdAndSynchronousClassCourseRetired(new Long(instructorId), false);
		List<Long> synchIds = new ArrayList();
		for(InstructorSynchronousClass obj : synchronousClasses){
			//System.out.println("*********"+obj.getId());
			synchIds.add(obj.getSynchronousClass().getId());
		}
		
		
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); 
		c1.setTime(new Date());  // 1999 jan 20
		c1.add(Calendar.DATE, +1);
		Calendar c2 = Calendar.getInstance(); 
		//c2.add(Calendar.DATE, -(c2.get(Calendar.DATE)-1)); 
		c2.add(Calendar.MONTH, -1); 
		
		//System.out.println("************"+.get(0).getId());
		
		
		return leanrEnrollmentRepository.findBySynchronousClassIdInAndEnrollmentDateBetween(synchIds, c2.getTime(), c1.getTime());
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ResourceService#findCompletedEnrolmentsByInstructor(long)
	 */
	public List<LearnerEnrollment> findCompletedEnrolmentsByInstructor(long instructorId) {
		List<InstructorSynchronousClass> synchronousClasses = instructorSynchronousClassRepository.findByInstructorIdAndSynchronousClassCourseRetired(new Long(instructorId), false);
		List<Long> synchIds = new ArrayList();
		for(InstructorSynchronousClass obj : synchronousClasses){
			//System.out.println("*********"+obj.getId());
			synchIds.add(obj.getSynchronousClass().getId());
		}
		
		
		String DATE_FORMAT = "yyyy-MM-dd";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
		Calendar c1 = Calendar.getInstance(); 
		c1.setTime(new Date());  // 1999 jan 20
		c1.add(Calendar.DATE, +1);
		Calendar c2 = Calendar.getInstance(); 
		//c2.add(Calendar.DATE, -(c2.get(Calendar.DATE)-1)); 
		c2.add(Calendar.MONTH, -1); 
		
		//System.out.println("************"+.get(0).getId());
		
		
		return leanrEnrollmentRepository.findBySynchronousClassIdInAndCourseStatisticsCompletionDateBetween(synchIds, c2.getTime(), c1.getTime());
	}
	
	public CourseActivity loadForUpdateCourseActivity(long id){
		return courseActivityRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ResourceService#loadForUpdateGradebook(long)
	 */
	@Override
	public Gradebook loadForUpdateGradebook(long id) {
		return gradeBookRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ResourceService#loadForUpdateLocation(long)
	 */
	@Override
	public Location loadForUpdateLocation(long id) {
		return locationRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ResourceService#loadForUpdateResource(long)
	 */
	@Override
	public Resource loadForUpdateResource(long id) {
		return resourceRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see com.softech.vu360.lms.service.ResourceService#loadForUpdateResourceType(long)
	 */
	@Override
	public ResourceType loadForUpdateResourceType(long id) {
		return resourceTypeRepository.findOne(id);
	}

}