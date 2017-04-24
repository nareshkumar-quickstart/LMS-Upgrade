package com.softech.vu360.lms.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.CustomerLMSFeature;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.lms.model.DistributorLMSFeature;
import com.softech.vu360.lms.model.EnrollmentCourseView;
import com.softech.vu360.lms.model.InstructorConnectCourse;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerGroup;
import com.softech.vu360.lms.model.LearnerSCOStatistics;
import com.softech.vu360.lms.model.LearnerSelfStudyCourseActivity;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.SubscriptionCourse;
import com.softech.vu360.lms.model.SubscriptionCustomerEntitlement;
import com.softech.vu360.lms.model.SynchronousClass;
import com.softech.vu360.lms.model.SynchronousCourse;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.TrainingPlanCourse;
import com.softech.vu360.lms.model.TrainingPlanCourseView;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.model.WebinarCourse;
import com.softech.vu360.lms.repositories.CourseCustomerEntitlementItemRepository;
import com.softech.vu360.lms.repositories.CourseCustomerEntitlementRepository;
import com.softech.vu360.lms.repositories.CourseGroupCustomerEntitlementItemRepository;
import com.softech.vu360.lms.repositories.CourseGroupCustomerEntitlementRepository;
import com.softech.vu360.lms.repositories.CourseGroupRepository;
import com.softech.vu360.lms.repositories.CourseRepository;
import com.softech.vu360.lms.repositories.CustomerEntitlementRepository;
import com.softech.vu360.lms.repositories.DistributorEntitlementRepository;
import com.softech.vu360.lms.repositories.EnrollmentCourseViewRepository;
import com.softech.vu360.lms.repositories.LearnerCourseStatisticsRepository;
import com.softech.vu360.lms.repositories.LearnerEnrollmentRepository;
import com.softech.vu360.lms.repositories.LearnerSCOStatisticsRepository;
import com.softech.vu360.lms.repositories.LearnerSelfStudyCourseActivityRepository;
import com.softech.vu360.lms.repositories.OrgGroupEntitlementRepository;
import com.softech.vu360.lms.repositories.OrganizationalGroupMemberRepository;
import com.softech.vu360.lms.repositories.SubscriptionCourseRepository;
import com.softech.vu360.lms.repositories.TrainingPlanAssignmentRepository;
import com.softech.vu360.lms.service.CourseAndCourseGroupService;
import com.softech.vu360.lms.service.CustomerService;
import com.softech.vu360.lms.service.EnrollmentService;
import com.softech.vu360.lms.service.EntitlementService;
import com.softech.vu360.lms.service.OrgGroupLearnerGroupService;
import com.softech.vu360.lms.service.SecurityAndRolesService;
import com.softech.vu360.lms.service.SubscriptionService;
import com.softech.vu360.lms.service.SynchronousClassService;
import com.softech.vu360.lms.web.filter.VU360UserAuthenticationDetails;
import com.softech.vu360.util.ArrangeCourseGroupTree;
import com.softech.vu360.util.CourseSort;
import com.softech.vu360.util.TreeNode;
import com.softech.vu360.util.VU360Properties;
/**
 * @author jason
 * @modified by Faisal A. Siddiqui
 */
public class EntitlementServiceImpl implements EntitlementService {
	@Inject
	private LearnerEnrollmentRepository learnerEnrollmentRepository = null;
	@Inject
	private LearnerCourseStatisticsRepository learnerCourseStatisticsRepository = null;
	@Inject
	private SubscriptionCourseRepository subscriptionCourseRepository;
	@Inject
	private CustomerEntitlementRepository customerEntitlementRepository=null;
	@Inject
	private CourseCustomerEntitlementItemRepository courseCustomerEntitlementItemRepository=null;
	@Inject
	private CourseCustomerEntitlementRepository courseCustomerEntitlementRepository=null;
	@Inject
	private CourseGroupCustomerEntitlementRepository courseGroupCustomerEntitlementRepository=null;
	@Inject
	private CourseGroupRepository courseGroupRepository=null;
	@Inject
	private CourseRepository courseRepository;
	@Inject
	private DistributorEntitlementRepository distributorEntitlementRepository;
	@Inject
	private OrgGroupEntitlementRepository orgGroupEntitlementRepository;
	@Inject
	private CourseGroupCustomerEntitlementItemRepository courseGroupCustomerEntitlementItemRepository;
	@Inject
	private EnrollmentCourseViewRepository enrollmentCourseViewRepository;
	@Inject
	private TrainingPlanAssignmentRepository trainingPlanAssignmentRepository = null;
	@Inject
	private LearnerSCOStatisticsRepository learnerSCOStatisticsRepository = null;
	@Inject
	private LearnerSelfStudyCourseActivityRepository learnerSelfStudyCourseActivityRepository = null;
	@Inject
	private OrganizationalGroupMemberRepository organizationalGroupMemberRepository = null;


	//private static final int DEFAULT_TOS_IN_DAYS = (Integer.parseInt(VU360Properties.getVU360Property("defaultTOS")));
	private static final String ENFORCE_ORG_GROUP_ENROLLMENT_RESTRICTION_FEATURE_CODE = "lms.permissions.manager.planAndEnroll.enforceOrgGroupEnrollmentRestriction.featureCode";
	private static final Logger log = Logger.getLogger(EntitlementServiceImpl.class.getName());
	//private EntitlementDAO entitlementDAO = null;
	private CustomerService customerService = null;
	//private SubscriptionDAO subscriptionDAO = null;
	private OrgGroupLearnerGroupService orgGroupLearnerGroupService = null;
	private CourseAndCourseGroupService courseAndCourseGroupService = null;
	private SynchronousClassService synchronousClassService=null;
	private EnrollmentService enrollmentService = null;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	private SubscriptionService subscriptionService;
	private SecurityAndRolesService securityAndRolesService = null;


	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
	public SynchronousClassService getSynchronousClassService() {
		return synchronousClassService;
	}
	public void setSynchronousClassService(
			SynchronousClassService synchronousClassService) {
		this.synchronousClassService = synchronousClassService;
	}

	public List<OrgGroupEntitlement> getOrgGroupsEntilementsForCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		List<OrgGroupEntitlement> orggroupEntitlements = orgGroupEntitlementRepository.findByCustomerEntitlementId(customerEntitlement.getId()); //Wajahat Ali
		List<OrgGroupEntitlement> nonZeroOrgGroupEntitlements = new ArrayList<OrgGroupEntitlement>();
		for (OrgGroupEntitlement oge : orggroupEntitlements) {
			if (oge.getMaxNumberSeats() >= 0) {
				nonZeroOrgGroupEntitlements.add(oge);
			}
		}
		return nonZeroOrgGroupEntitlements;
	}

	public List<CustomerEntitlement> getActiveCustomerEntitlementsForCustomer(Customer customer) {
		return customerEntitlementRepository.getActiveCustomerEntitlementsForCustomer(customer.getId()); // Wajahat Ali
	}

	public CustomerEntitlement customerHasActiveEntitlementFor(Customer customer, Course course) {
//        List<CustomerEntitlement> entls = getActiveCustomerEntitlementsForCustomer(customer);
//        return (CustomerEntitlement)CollectionUtils.find(entls, new CoursePredicate(course.getBussinesskey()));

		List<CourseCustomerEntitlementItem> courseItems = courseCustomerEntitlementItemRepository.getActiveCourseContractsFor(customer.getId(),new Date(),new Date(),true,course.getId());
		HashMap<Long,CourseCustomerEntitlement> map = new HashMap<Long, CourseCustomerEntitlement>();
		for(CourseCustomerEntitlementItem item: courseItems){
			if(map.get(item.getCustomerEntitlement().getId())==null){
				map.put(item.getCustomerEntitlement().getId(), (CourseCustomerEntitlement)item.getCustomerEntitlement());
			}
		}
		Collection<CourseCustomerEntitlement> courseContracts = map.values();
		if(CollectionUtils.isNotEmpty(courseContracts)){
			return (CustomerEntitlement)courseContracts.toArray()[0];
		}else{
			try {
				Date curDate = new Date( System.currentTimeMillis() );
				List<CourseGroupCustomerEntitlementItem> courseGroupItems = courseGroupCustomerEntitlementItemRepository.getActiveCourseGroupContractsFor(customer.getId(), curDate, course.getId());
				Collection<CourseGroupCustomerEntitlement> groupContracts = null;//entitlementDAO.getActiveCourseGroupContractsFor(customer, course.getId());
				HashMap<Long,CourseGroupCustomerEntitlement> map2 = new HashMap<Long, CourseGroupCustomerEntitlement>();
				for(CourseGroupCustomerEntitlementItem item: courseGroupItems){
					if(map2.get(item.getCourseGroupCustomerEntitlement().getId())==null){
						map2.put(item.getCourseGroupCustomerEntitlement().getId(), item.getCourseGroupCustomerEntitlement());
					}
				}
				groupContracts = map2.values();
				if(CollectionUtils.isNotEmpty(groupContracts)){
					return (CustomerEntitlement)groupContracts.toArray()[0];
				}
			} catch (Exception e) {
				log.debug("Error occurred while fetching Active Contracts for Customer:"+ customer.getId());
			}
		}
		return null;
	}

	public LearnerEnrollment getLearnerEnrollmentById(Long id) {
		return learnerEnrollmentRepository.findOne(id);
	}

	public List<CustomerEntitlement> getCustomerEntitlementsForLearner(Learner l) {
		log.debug("looking for CustomerEntitlments");
		List<CustomerEntitlement> results = customerEntitlementRepository.findCustomerIdAndAllowSelfEnrollmentTrue(l);//this was returing customer entitlements Wajahat Ali
		log.debug("customer entitlements=" + results.size());
		List<CustomerEntitlement> availableCustomerentitlements = new ArrayList<CustomerEntitlement>();

        /*
        * LMS-2851 :
        *  If org group selected is blank then system should allow any user to access all the courses
        *  present in entitlement in course catalog. However if the entitlement is created for a
        *  specific org group then system must display those courses in course catalog for the
        *  learner in which entitlement is created for that org group.
        * but the given below code somehow filtering the all customer entitlements.. so thats the reason why it wasn't
        * showing the courses in course catalog page.
        */
		List<OrganizationalGroup> learnerOrgGroups = orgGroupLearnerGroupService.getOrgGroupsByLearner(l);
		List<OrgGroupEntitlement> orgGroupEntitlements = orgGroupEntitlementRepository.getAvailableOrgGroupEntitlementsOfLearner(l,learnerOrgGroups);
		if (orgGroupEntitlements != null && orgGroupEntitlements.size() > 0) {
			for (OrgGroupEntitlement orgGroupentitlement : orgGroupEntitlements) {
				if (orgGroupentitlement.getMaxNumberSeats() > orgGroupentitlement.getNumberSeatsUsed()) {
					availableCustomerentitlements.add(orgGroupentitlement.getCustomerEntitlement());
				}
			}
		}
		for (CustomerEntitlement customerEntitlement : results) {
			List<OrgGroupEntitlement> orgGroupEntitlementsForCustomer = this.getOrgGroupsEntilementsForCustomerEntitlement(customerEntitlement);
			if (orgGroupEntitlementsForCustomer != null && orgGroupEntitlementsForCustomer.size() > 0) {
				//this will be handled by orggroupentitlement
			} else {
				availableCustomerentitlements.add(customerEntitlement);
			}
		}

		log.debug("Entitlements of learner=" + availableCustomerentitlements.size());
		return availableCustomerentitlements;
	}

	public List<LearnerEnrollment> getActiveLearnerEnrollmentsByLearner(Learner l, String search){
		List<String> enrollmentStatus = new ArrayList<String>();
		enrollmentStatus.add(LearnerEnrollment.EXPIRED);
		enrollmentStatus.add(LearnerEnrollment.ACTIVE);
		java.util.Date enrollmentEndDate =new Date();
		List<LearnerEnrollment> enrollments= new ArrayList<LearnerEnrollment>();
		if(!StringUtils.isBlank(search) && !search.equals("Search")){
			enrollments = learnerEnrollmentRepository.findbyLearnerIdandEnrollmentStatusandEnrollmentEndDateandCourseTitleorDescorKeywords(l.getId(), enrollmentStatus, enrollmentEndDate, search);
		}else
			enrollments = learnerEnrollmentRepository.findbyLearnerIdandEnrollmentStatusandEnrollmentEndDate(l.getId(), enrollmentStatus, enrollmentEndDate);

		//List<LearnerEnrollment> enrollments = entitlementDAO.getActiveLearnerEnrollmentsByLearner(l,search);
		return filterEnrollmentsByDate(enrollments);
	}

	public List<LearnerEnrollment> getLearnerEnrollmentsByLearner(Learner l, String search) {
//    	List<String> enrollmentStatus = new ArrayList();
//		enrollmentStatus.add(LearnerEnrollment.EXPIRED);
//		enrollmentStatus.add(LearnerEnrollment.ACTIVE);
		List<LearnerEnrollment> enrollments= new ArrayList<LearnerEnrollment>();
		if(!StringUtils.isBlank(search) && !search.equals("Search")){
			enrollments = learnerEnrollmentRepository.findbyLearnerIdandEnrollmentStatusandCourseTitleorDescorKeywords(l.getId(), LearnerEnrollment.EXPIRED,LearnerEnrollment.ACTIVE, search);
		}else
		/**
		 * Modified bY Marium Saud : Enrollment Status is passed separately to the repository to replace 'IN' clause with 'OR' inorder to make use of 'NamedEntityGraph'
		 */
			enrollments = learnerEnrollmentRepository.findbyLearnerIdandEnrollmentStatus(l.getId(), LearnerEnrollment.EXPIRED,LearnerEnrollment.ACTIVE );

		//List<LearnerEnrollment> enrollments = entitlementDAO.getLearnerEnrollmentsByLearner(l, search);
		return filterEnrollmentsByDate(enrollments);
	}


	/* LMS-13793 */
	public List<LearnerEnrollment> getLearnerEnrollmentsForReportingfields(Learner l){
		/**
		 * Method modified By Marium Saud : Instead of getting LearnerEnrollment From LearnerCourseStatistics , it was fetched from LearnerEnrollment base table by joining LearnerCourseStatistics
		 * Inorder to avoid bulk queries generation (LMS-21562)
		 */
		List<String> status = new ArrayList<String>();
		List<LearnerEnrollment> enrollments = new ArrayList<LearnerEnrollment>();
		status.add(LearnerEnrollment.DROPPED);
		status.add(LearnerEnrollment.SWAPPED);

		enrollments = learnerEnrollmentRepository.findByLearnerIdAndEnrollmentStatusNotIn(l.getId(), status);

		return filterEnrollmentsByDate(enrollments);
	}

	public List<LearnerEnrollment> getExpiredLearnerEnrollmentsByLearner(Learner l, String search){
//    	List<String> enrollmentStatus = new ArrayList();
//		enrollmentStatus.add(LearnerEnrollment.EXPIRED);
//		enrollmentStatus.add(LearnerEnrollment.ACTIVE);
//		String statsStatus = LearnerCourseStatistics.REPORTING_PENDING;

		List<LearnerEnrollment> enrollments= new ArrayList<LearnerEnrollment>();
		if(!StringUtils.isBlank(search) && !search.equals("Search")){
			enrollments = learnerEnrollmentRepository.findbyLearnerIdandCourseStatisticsStatusandEnrollmentStatusandEnrollmentEndDateandCourseTitleorDescorKeywords(l.getId(),LearnerCourseStatistics.REPORTING_PENDING, LearnerEnrollment.EXPIRED, LearnerEnrollment.ACTIVE, new Date(), search);
		}else
			enrollments = learnerEnrollmentRepository.findbyLearnerIdandCourseStatisticsStatusandEnrollmentStatusandEnrollmentEndDate(l.getId(), LearnerCourseStatistics.REPORTING_PENDING,LearnerEnrollment.EXPIRED, LearnerEnrollment.ACTIVE, new Date());

		//enrollments = entitlementDAO.getExpiredLearnerEnrollmentsByLearner(l, search);
		return filterEnrollmentsByDate(enrollments);
	}

	public List<LearnerEnrollment> getLearnerEnrollmentsForLearner(Learner l) {
		log.debug("looking for learnerEnrollments");
		List<LearnerEnrollment> enrollments = learnerEnrollmentRepository.findByLearnerId(l.getId());
		return filterEnrollmentsByDate(enrollments);
	}

	/* LMS-14020 */
	public List<LearnerEnrollment> getFreshLearnerEnrollmentsForLearner(Learner l) {
		log.debug("looking for learnerEnrollments");
		//Modified By Marium Saud : Method 'learnerEnrollmentRepository.findByLearnerId(l.getId())' has been replaced by new method inorder to avoid n+1 issue for LearnerEnrollment queries
		List<LearnerEnrollment> enrollments = learnerEnrollmentRepository.getFreshLearnerEnrollmentsForLearner(l);
		return filterEnrollmentsByDate(enrollments);
	}

	private List<LearnerEnrollment> filterEnrollmentsByDate(List<LearnerEnrollment> enrollments){
		List<LearnerEnrollment> results = new ArrayList<LearnerEnrollment>();
		Date now = new Date();
		now = DateUtils.truncate(now, Calendar.DATE);
		Date enrollmentStartDate = null;
		for (LearnerEnrollment le : enrollments) {
			enrollmentStartDate = le.getEnrollmentStartDate();
			// enrollment start date shouldn't be null, but a safety check
			if (enrollmentStartDate != null) {
				enrollmentStartDate = DateUtils.truncate(enrollmentStartDate, Calendar.DATE);
				if (now.after(enrollmentStartDate) || now.equals(enrollmentStartDate)) {
					results.add(le);
				}
			} else {
				results.add(le);
			}
		}
		return results;
	}

	public LearnerEnrollment getLearnerEnrollmentsForLearner(Learner learner, long courseId) {
		Course course = courseAndCourseGroupService.getCourseById(courseId);
		return getLearnerEnrollmentsForLearner(learner, course);
	}

	public LearnerEnrollment getLearnerEnrollmentsForLearner(Learner learner, Course course) {
		List<LearnerEnrollment> results = new ArrayList<LearnerEnrollment>();
		List<LearnerEnrollment> enrollments = learnerEnrollmentRepository.findByLearnerIdAndCourseId(learner.getId(), course.getId());
		if (enrollments != null) {
			for (LearnerEnrollment learnerEnrollment : enrollments) {
				if (!learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.DROPPED)
						&& !learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.EXPIRED)
						&& !learnerEnrollment.getEnrollmentStatus().equalsIgnoreCase(LearnerEnrollment.SWAPPED)) {
					results.add(learnerEnrollment);
				}
			}
		}
		// this should not happen (have more than one active enrollment at a time...
		if (results.size() > 1) {
			log.warn("LEARNER:" + learner.getVu360User().getId() + " has more than one ACTIVE enrollment for course:" + course.getKey());
			return results.get(0);
		} else if (results.size() == 1) {
			return results.get(0);
		}
		return null;
	}

	public OrgGroupEntitlement   loadForUpdateOrgGroupEntitlement(long id){
		return orgGroupEntitlementRepository.findById(id);
	}

	public  CustomerEntitlement  loadForUpdateCustomerEntitlement(long id){
		return customerEntitlementRepository.findById(id);
	}

	@Deprecated
	public List<OrgGroupEntitlement> getOrgGroupEntitlementsForLearner(Learner l) {
		log.debug("looking for orgGroupEntitlements");
		//List<OrgGroupEntitlement> results = entitlementDAO.getOrgGroupEntitlemnetsForLearner(l);
		List<OrgGroupEntitlement> results = orgGroupEntitlementRepository.getOrgGroupEntitlemnetsForLearner(l.getId());

		return results;
	}
	public  DistributorEntitlement  loadForUpdateDistributorEntitlement(long id)
	{
		//return entitlementDAO.loadForUpdateDistributorEntitlement(id);
		return distributorEntitlementRepository.findOne(id);
	}
	public DistributorEntitlement getDistributorEntitlementByName(String name, long distributorId) {
		//return entitlementDAO.getDistributorEntitlementByName(name, distributorId);
		List<DistributorEntitlement> listDistributorEntitlement = distributorEntitlementRepository.findByNameAndDistributorId(name, distributorId);
		if(listDistributorEntitlement!=null && listDistributorEntitlement.size()>0){
			return (DistributorEntitlement)listDistributorEntitlement.get(0);
		}

		return null;
	}

	public OrgGroupEntitlement getOrgGroupEntitlementById(long id) {
		//return entitlementDAO.getOrgGroupEntitlementById(id);
		return orgGroupEntitlementRepository.findOne(id);
	}

	public OrgGroupEntitlement saveOrgGroupEntitlement(OrgGroupEntitlement orgGroupEntitlement) {
		//return entitlementDAO.saveOrgGroupEntitlement(orgGroupEntitlement);
		return orgGroupEntitlementRepository.save(orgGroupEntitlement);
	}

	public List<CustomerEntitlement> getAllCustomerEntitlements(Customer customer) {
		List<CustomerEntitlement> results = customerEntitlementRepository.findByCustomer(customer);  //Wajahat Ali
		return results;
	}

	public List<CustomerEntitlement> getAllCustomerEntitlementsForGrid(Customer customer){
		return customerEntitlementRepository.findByCustomer(customer);
	}

	@Override
	/**
	 * Return a <CODE>CustomerEntitlement</CODE> with no limit if one exists or
	 * create a new if doesn't exist and the <CODE>createIfNotExists</CODE> is set to true and return that.
	 * @author muzammil.shaikh
	 */
	public CustomerEntitlement addUnlimitedCustomerEntitlementIfNotExist(
			Customer customer, List<Course> courses) {

		CourseCustomerEntitlement unlimitedCustomerEntitlement = null;
		List<CustomerEntitlement> customerEntitlements = customerEntitlementRepository.findByCustomer(customer); //Wajahat Ali
		boolean exists = false;
		if (!customerEntitlements.isEmpty()) {
			for(CustomerEntitlement customerEntitlement : customerEntitlements) {
				if (customerEntitlement instanceof CourseCustomerEntitlement) {
					unlimitedCustomerEntitlement = (CourseCustomerEntitlement)customerEntitlement;
					log.debug("Existing Unlimited Customer Entitlement Id: " + unlimitedCustomerEntitlement.getId() + " will be updated.");
					unlimitedCustomerEntitlement = addCourseCustomerEntitlementItemsIfNotExist(
							unlimitedCustomerEntitlement, courses);
					exists = true;
					break;
				}
			}

		}
		if (!exists) {
			unlimitedCustomerEntitlement = (CourseCustomerEntitlement)createUnlimitedCustomerEntitlement(customer, courses);
			log.debug("New Unlimited Customer Entitlement created with Id: " + unlimitedCustomerEntitlement.getId());
		}
		return unlimitedCustomerEntitlement;
	}

	public DistributorEntitlement getUnlimitedDistributorEntitlement(Distributor distributor, List<Course> courses, boolean createIfNotExists) {
		DistributorEntitlement unlimitedDistributorEntitlement = null;
		//List<DistributorEntitlement> distributorEntitlements = entitlementDAO.getAllDistributorEntitlements(distributor);
		List<DistributorEntitlement> distributorEntitlements = distributorEntitlementRepository.findByDistributor(distributor);

		boolean exists = false;
		if(!distributorEntitlements.isEmpty()) {
			for (DistributorEntitlement distributorEntitlement : distributorEntitlements) {
				Date startDate = distributorEntitlement.getStartDate();
				Date endDate = distributorEntitlement.getEndDate();

				if( isUnlimitedEntitlement(startDate, endDate)) {
					unlimitedDistributorEntitlement = distributorEntitlement;
					log.debug("returning existing Unlimited Distributor Entitlement for Distributor: " + distributor.getName()
							+ " with ID: " + unlimitedDistributorEntitlement.getId());
					unlimitedDistributorEntitlement = addDistributorEntitlementCourseGroupForCoursesIfNotExist(unlimitedDistributorEntitlement, courses);
					exists = true;
					break;
				}
			}
		}
		if (!exists && createIfNotExists) {
			unlimitedDistributorEntitlement = createUnlimitedDistributorEntitlement(distributor, courses);
			log.debug("returning new Unlimited Distributor Entitlement for Distributor: " + distributor.getName()
					+ " with ID: " + unlimitedDistributorEntitlement.getId());
		}
		return unlimitedDistributorEntitlement;
	}

	private boolean isUnlimitedEntitlement(Date date1, Date date2) {
		if (date1 != null && date2 != null) {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date1);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(date2);
			int yearDiff = calendar2.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);

			if (yearDiff >= 1000) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}

	public CourseCustomerEntitlement addCourseCustomerEntitlementItemsIfNotExist(CourseCustomerEntitlement courseCustomerEntitlement,
																				 List<Course> courses) {
		try {
			List<CourseCustomerEntitlementItem> existingEntitlementItems = this.getItemsByEntitlement(courseCustomerEntitlement);//courseCustomerEntitlement.getEntitlementItems();
			//List<CourseGroup> existingEntitlementCourseGroups = new ArrayList<CourseGroup>();
			boolean itemFound = false;
			for (Course course : courses) {
				course = courseAndCourseGroupService.getCourseById(course.getId());
				List<CourseGroup> courseGroups = courseAndCourseGroupService.getCourseGroupsForCourse(course);
				if (CollectionUtils.isNotEmpty(courseGroups)) {
					for (CourseGroup cg : courseGroups) {
						itemFound = false;
						for(CourseCustomerEntitlementItem item:existingEntitlementItems){
							if(course.getId().longValue() == item.getCourse().getId().longValue() &&
									(item.getCourseGroup()!=null && item.getCourseGroup().getId().longValue()==cg.getId().longValue())
									){
								itemFound = true;
								break;
							}
						}
						if(!itemFound){
							this.addEntitlementItem(courseCustomerEntitlement, course, cg);
						}
					}
				}
				else {
					itemFound = false;
					for(CourseCustomerEntitlementItem item:existingEntitlementItems){
						if(course.getId().longValue() == item.getCourse().getId().longValue() && item.getCourseGroup()==null){
							itemFound = true;
							break;
						}
					}
					if(!itemFound){
						this.addEntitlementItem(courseCustomerEntitlement, course, null);
					}
				}
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return courseCustomerEntitlement;
	}


	public DistributorEntitlement addDistributorEntitlementCourseGroupForCoursesIfNotExist(DistributorEntitlement distributorEntitlement,
																						   List<Course> courses) {

		try {
			List<CourseGroup> distributorEntitlementCourseGroups = distributorEntitlement.getCourseGroups();
			boolean newEntitlementAdded = false;
			if(distributorEntitlementCourseGroups == null) {
				distributorEntitlementCourseGroups = new ArrayList<CourseGroup>();
			}

			for (Course course : courses) {
				//Get all CourseGroups for this course
				List<CourseGroup> courseGroups = courseAndCourseGroupService.getCourseGroupsForCourse(course);
				//If no course group of this course exists or none of the existing course group are part of this entitlement then.
				if (courseGroups != null && !courseGroups.isEmpty()) {
					log.debug("Course Groups Exist for course: " + course.getId() + ", with title: " + course.getCourseTitle());
					//Check if there is any course group of this course that is already a part of this distributor entitlement.
					//If yes then do nothing.
					for (CourseGroup cg:courseGroups) {
						if(distributorEntitlementCourseGroups.isEmpty() || !distributorEntitlementCourseGroups.contains(cg)) {
							newEntitlementAdded = true;
							distributorEntitlementCourseGroups.add(cg);
						}
					}
				}
			}

			if (newEntitlementAdded && !distributorEntitlementCourseGroups.isEmpty()) {
				distributorEntitlement.getCourseGroups().addAll(distributorEntitlementCourseGroups);
				distributorEntitlement = saveDistributorEntitlement(distributorEntitlement);
			}
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return distributorEntitlement;
	}

	public List<CustomerEntitlement> getAllCustomerEntitlementsExcept(Long[] entitlmentIdsNotToGet) {
		List<CustomerEntitlement> entitlements = new ArrayList<CustomerEntitlement>();
		Customer customer = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails())
				.getCurrentCustomer();
		if (customer != null && entitlmentIdsNotToGet != null && entitlmentIdsNotToGet.length > 0) {
			entitlements = customerEntitlementRepository.findByCustomerAndIdNotIn(customer, Arrays.asList(entitlmentIdsNotToGet)); //Wajahat Ali
		}
		return entitlements;
	}

	public CustomerEntitlement addCustomerEntitlement(Customer customer, CustomerEntitlement customerEntitlement) {
		Customer cust = customerService.getCustomerById(customer.getId());
		customerEntitlement.setCustomer(cust);
		//return entitlementDAO.saveCustomerEntitlement(customerEntitlement);
		return customerEntitlementRepository.save(customerEntitlement);
	}

	public DistributorEntitlement createUnlimitedDistributorEntitlement(Distributor distributor, List<Course> courses) {

		DistributorEntitlement distributorEntitlement = null;
		try {
			distributorEntitlement = new DistributorEntitlement();
			StringBuilder nameBuilder = new StringBuilder("");
			nameBuilder.append(distributor.getName() + "'s" + " ");
			nameBuilder.append("DistributorEntitlement created at: " +  dateFormat.format(new Date()));
			distributorEntitlement.setName(nameBuilder.toString());
			distributorEntitlement.setDistributor(distributor);
			distributorEntitlement.setAllowUnlimitedEnrollments(true);
			distributorEntitlement.setAllowSelfEnrollment(true);
			Calendar calendar = Calendar.getInstance();
			Date startDate = calendar.getTime();
			distributorEntitlement.setStartDate(startDate);
			log.debug("Start Date for this entitlement: " + startDate);
			calendar.add(Calendar.YEAR, 1000);
			Date endDate = calendar.getTime();
			distributorEntitlement.setEndDate(endDate);
			log.debug("End Date for this entitlement: " + endDate);
			List<CourseGroup> courseGroupsForEntitlements = new Vector<CourseGroup>();

			for (Course course : courses) {
				//Get all CourseGroups for this course
				List<CourseGroup> courseGroups = courseAndCourseGroupService.getCourseGroupsForCourse(course);

				for (CourseGroup cg : courseGroups) {
					if (!courseGroupsForEntitlements.contains(cg)){
						courseGroupsForEntitlements.add(cg);
					}
				}
			}
			distributorEntitlement.setCourseGroups(courseGroupsForEntitlements);
			distributorEntitlement = saveDistributorEntitlement(distributorEntitlement);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return distributorEntitlement;
	}

	/*
     * TODO: system managed should be updated here..
     */
	public CustomerEntitlement createUnlimitedCustomerEntitlement(Customer customer, List<Course> courses) {

		CourseCustomerEntitlement customerEntitlement = null;
		try {
			Date maxEndDate = getMaxDistributorEntitlementEndDate(customer.getDistributor());
			customerEntitlement = new CourseCustomerEntitlement();
			StringBuilder nameBuilder = new StringBuilder("");
			nameBuilder.append(customer.getName() + "'s" + " ");
			nameBuilder.append("Contract created at: " +  dateFormat.format(new Date()));
			customerEntitlement.setName(nameBuilder.toString());
			customerEntitlement.setCustomer(customer);
			customerEntitlement.setAllowUnlimitedEnrollments(true);
			customerEntitlement.setAllowSelfEnrollment(true);
			Calendar calendar = Calendar.getInstance();
			Date startDate = calendar.getTime();
			customerEntitlement.setStartDate(startDate);
			calendar.add(Calendar.YEAR, 1000);
			Date endDate = calendar.getTime();
			if(endDate.after(maxEndDate)){
				customerEntitlement.setEndDate(maxEndDate);
			}else{
				customerEntitlement.setEndDate(endDate);
			}
			customerEntitlement.setEnrollmentType(CustomerEntitlement.COURSE_ENROLLMENTTYPE);
			customerEntitlement.setContractCreationDate(new Date());
			customerEntitlement = (CourseCustomerEntitlement)addCustomerEntitlement(customerEntitlement);

			ArrayList<CourseCustomerEntitlementItem> courseCustomerEntitlementItems = new ArrayList<CourseCustomerEntitlementItem>(courses.size());
			CourseCustomerEntitlementItem item = null;
			for (Course course : courses) {
				log.debug("Course Id: " + course.getId() + ", Course Name: " + course.getCourseTitle());
				log.debug("Creating new Course Customer Entitlement Item for course: " + course.getId());

				List<CourseGroup> courseGroups = courseAndCourseGroupService.getCourseGroupsForCourse(course);
				if(!courseGroups.isEmpty()) {
					for(CourseGroup courseGroup:courseGroups) {
						item = addEntitlementItem(customerEntitlement, course, courseGroup);
						courseCustomerEntitlementItems.add(item);
					}
				}
				else {
					item = addEntitlementItem(customerEntitlement, course, null);
					courseCustomerEntitlementItems.add(item);
				}
			}
			//customerEntitlement.setEntitlementItems(courseCustomerEntitlementItems);
			log.debug("New Customer Entitlement Id: " + customerEntitlement.getId() + ", Name: " + customerEntitlement.getName());
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return customerEntitlement;
	}

	public CustomerEntitlement addCustomerEntitlement(CustomerEntitlement customerEntitlement) {
		if (customerEntitlement != null) {
			//return entitlementDAO.saveCustomerEntitlement(customerEntitlement);
			return customerEntitlementRepository.save(customerEntitlement);
		}
		return null;
	}

	public List<DistributorEntitlement> getAllDistributorEntitlements(Distributor distributor) {
		//return entitlementDAO.getAllDistributorEntitlements(distributor);
		return distributorEntitlementRepository.findByDistributor(distributor);
	}

	public List<Course> findCoursesByDistributor(long courseGroupIdArray[], String courseName, String courseGUID, String keywords, String searchCriteria) {
		List<Long> idsArr = Arrays.asList(ArrayUtils.toObject(courseGroupIdArray));
		return courseRepository.findBycourseTitle(idsArr, courseName, courseGUID, keywords, searchCriteria);
	}

	public CustomerEntitlement readCustomerEntitlementById(Long id){
		return customerEntitlementRepository.findById(id);
	}
	public CustomerEntitlement getCustomerEntitlementById(Long id) {
		return customerEntitlementRepository.findById(id);
	}

	public DistributorEntitlement getDistributorEntitlementById(Long id) {
		//return entitlementDAO.getDistributorEntitlementById(id);
		return distributorEntitlementRepository.findOne(id);
	}

	public void addOrgGroupEntitlementInCustomerEntitlement(CustomerEntitlement customerEntitlement, List<OrgGroupEntitlement> orgGroupEntitlements) {
		//entitlementDAO.addOrgGroupEntitlementInCustomerEntitlement(customerEntitlement, orgGroupEntitlements);
		orgGroupEntitlements = orgGroupEntitlementRepository.findByCustomerEntitlementIn(orgGroupEntitlements);
		for(int i=0;i<orgGroupEntitlements.size();i++){
			orgGroupEntitlements.get(i).setMaxNumberSeats(orgGroupEntitlements.get(i).getMaxNumberSeats());
		}

		if(orgGroupEntitlements!=null  && orgGroupEntitlements.size()>0){
			orgGroupEntitlementRepository.save(orgGroupEntitlements);
		}

	}

	public List<Course> getAllCoursesByEntitlement(Long customerID) {
		//start:Ahsun(RR): ENGSUP-6409: Corp: LMS: Survey Tool Broken
		Long[] lstcourseId = courseRepository.getPublishedEntitledCourses(customerID);
		return courseRepository.findCoursesByCustomer(lstcourseId,"","","","");
		// return entitlementDAO.getPublishedEntitledCourses(customer);
		//end:Ahsun(RR): ENGSUP-6409: Corp: LMS: Survey Tool Broken
	}

	/**
	 * LMS-14319
	 * Overloading method getAllCoursesByEntitlement() with two additional parameter
	 */
	public List<Course> getAllCoursesByEntitlement(Long customerId,String courseName, String courseType) {
		Long[] lstcourseId = courseRepository.getPublishedEntitledCourses(customerId);
		return courseRepository.findCoursesByCustomer(lstcourseId, courseName, courseType);
	}

	public List<Course> getCoursesByEntitlement(Customer customer, String courseName, String courseGUID, String keywords) {
		List<CustomerEntitlement> entitlements = customerEntitlementRepository.getCustomerEntitlementsByCutomer(customer);
		List<Course> selectedCourseListById = new ArrayList<Course>();
		List<Course> selectedCourseListByTitle = new ArrayList<Course>();
		List<Course> selectedCourseListByKeyWords = new ArrayList<Course>();
		List<Course> selectedCourseListToReturn = new ArrayList<Course>();
		List<Course> allCourseList = this.getUniqueCoursesByEnts(entitlements);
		if(courseGUID== "" && StringUtils.isBlank(courseName) ){
			selectedCourseListToReturn = allCourseList;
		}
		if(courseGUID != ""){
			for(Course selectedCourse : allCourseList){
				if(selectedCourse != null){
					if(selectedCourse.getId()== Long.parseLong(courseGUID)){
						selectedCourseListById.add(selectedCourse);
					}

				}
			}
		}else{
			selectedCourseListById = allCourseList;
		}

		if(courseName != ""){
			for(Course selectedCourse : selectedCourseListById){
				if(selectedCourse != null){
					if(StringUtils.isNotBlank(selectedCourse.getCourseTitle())&& selectedCourse.getCourseTitle().matches("(?i).*" + courseName + ".*")){
						selectedCourseListByTitle.add(selectedCourse);
					}

				}
			}
		}else{
			selectedCourseListByTitle = selectedCourseListById;
		}

		if(keywords != ""){
			for(Course selectedCourse : selectedCourseListByTitle){
				if(selectedCourse != null){
					if(StringUtils.isNotBlank(selectedCourse.getKeywords())&& selectedCourse.getKeywords().matches("(?i).*" + keywords + ".*")){
						selectedCourseListByKeyWords.add(selectedCourse);
					}
				}
			}

			selectedCourseListToReturn = selectedCourseListByKeyWords;
		}else{
			selectedCourseListToReturn = selectedCourseListByTitle;
		}
		return selectedCourseListToReturn;
	}

	/*
     * @author Waqas Baig for LMS-14294
     * @param customer courses available for given customer as per the contracts/entitlements
     * @param searches on course name and course type.
     */
	public List<Course> getCoursesByNameAndCourseType(Customer customer, String courseName, String courseType) {
		List<CourseCustomerEntitlementItem> entitlements = courseCustomerEntitlementItemRepository.getCoursesByNameAndCourseType(customer, courseName, courseType);
		List<Course> selectedCourseListToReturn = new ArrayList<Course>();

		for(CourseCustomerEntitlementItem courseEnt : entitlements) {
			selectedCourseListToReturn.add(courseEnt.getCourse());
		}

		return selectedCourseListToReturn;
	}

	/*
     * @author Faisal A. Siddiqui
     * @param customer courses available for given customer as per the contracts/entitlements
     * @param searchCriteria this course be value of Course Title, GUID or keywords
     * @return List of Unique courses without duplication.
     * @description This method fetches all entitlements/contracts of given customer with
     */
	public List<Course> getCoursesByEntitlement(Long customerID, String searchCriteria) {
		List<CourseCustomerEntitlement> cEnts= courseCustomerEntitlementRepository.getCourseCustomerEntitlementsByCourse(customerID, searchCriteria);
		//List<CourseGroupCustomerEntitlement> cgEnts= entitlementDAO.getCourseGroupCustomerEntitlementsByCourse(customer, searchCriteria);
        /*
         * The above line breaks in run, the property is missing and is not mapped in JPA hibernate
         */
		List<CustomerEntitlement> allEnts = new ArrayList<CustomerEntitlement>();
		allEnts.addAll(cEnts);
		//allEnts.addAll(cgEnts);
		//return this.filterCourses(allEnts);
		return this.getUniqueCoursesByEnts(allEnts);
	}
	public List<Course> getUniqueCoursesByEnts(List<CustomerEntitlement> custEnts){
		Set<Course> setOfCourses = new HashSet<Course>();
		List<Course> listOfCourses = new ArrayList<Course>();
		for(CustomerEntitlement ce:custEnts){
			setOfCourses.addAll(this.getUniqueCourses(ce));
		}
		listOfCourses.addAll(setOfCourses);
		return listOfCourses;
	}
	//re-factored by Faisal A. Siddiqui for LMS-4445
	public List<CustomerEntitlement> getCustomerEntitlementsByCourse(Customer customer, String searchCriteria) {
		List<CourseCustomerEntitlement> courseEnts = courseCustomerEntitlementRepository.getCourseCustomerEntitlementsByCourse(customer.getId(), searchCriteria);
		//List<CourseGroupCustomerEntitlement> courseGroupEnts = entitlementDAO.getCourseGroupCustomerEntitlementsByCourse(customer, searchCriteria);
        /*
         * The above line breaks as property mappen in toplink cannot be mapped the same way in JPA
         */
		List<CustomerEntitlement> availableCustomerEntitlements = new ArrayList<CustomerEntitlement>();
		availableCustomerEntitlements.addAll(courseEnts);
		//availableCustomerEntitlements.addAll(courseGroupEnts);
		return availableCustomerEntitlements;
	}

	public List<CustomerEntitlement> getActiveCustomerContractByCourseId(Customer customer, Long courseId){
    	/*
    	 * 		Toplink Code:
    	 * 		*************
		    	Collection<CourseCustomerEntitlement> courseEnts = entitlementDAO.getActiveCourseContractsFor(customer, courseId);//entitlementDAO.getCourseCustomerEntitlementItemByCourseId(customer, courseId);
		        Collection<CourseGroupCustomerEntitlement> courseGroupEnts = entitlementDAO.getActiveCourseGroupContractsFor(customer, courseId);//entitlementDAO.getCourseGroupCustomerEntitlementItemByCourseId(customer, courseId);
		        List<CustomerEntitlement> availableCustomerEntitlements = new ArrayList<CustomerEntitlement>();
		        if(CollectionUtils.isNotEmpty(courseEnts)){
		        	availableCustomerEntitlements.addAll(courseEnts);
		        }
		        if(CollectionUtils.isNotEmpty(courseGroupEnts)){
		        	availableCustomerEntitlements.addAll(courseGroupEnts);
		        }
		        return availableCustomerEntitlements;

    	 */

		List<CustomerEntitlement> availableCustomerEntitlements = new ArrayList<CustomerEntitlement>();
		Collection<CourseCustomerEntitlement> courseEnts = courseCustomerEntitlementRepository.getActiveCourseCustomerEntitlement(customer, courseId);//entitlementDAO.getCourseCustomerEntitlementItemByCourseId(customer, courseId);
		if(CollectionUtils.isNotEmpty(courseEnts)){
			availableCustomerEntitlements.addAll(courseEnts);
		}
		Collection<CourseGroupCustomerEntitlement> courseGroupEnts = courseGroupCustomerEntitlementRepository.getActiveCourseGroupCustomerEntitlement(customer, courseId);//entitlementDAO.getCourseGroupCustomerEntitlementItemByCourseId(customer, courseId);
		if(CollectionUtils.isNotEmpty(courseGroupEnts)){
			//Wajahat: Extra check to filter expired Contracts (This can also be done in the Sql)
			for(CourseGroupCustomerEntitlement cge : courseGroupEnts){
				if(cge.getEndDate()==null){
					Date expireDate = new Date(cge.getStartDate().getTime() + TimeUnit.DAYS.toMillis(cge.getDefaultTermOfServiceInDays()));
					if(expireDate.compareTo(new Date(System.currentTimeMillis()))>=0){
						//System.out.println("Contract should be added: "+cge.getId());
						availableCustomerEntitlements.add(cge);
					}
				}
				else{
					availableCustomerEntitlements.add(cge);
				}
			}
			//availableCustomerEntitlements.addAll(courseGroupEnts);
		}
		return availableCustomerEntitlements;
	}

	public List<CustomerEntitlement> getCustomerEntitlementsByCourseId(Customer customer, Long courseId) {
		//entitlementDAO.getCourseCustomerEntitlementItemByCourseId(customer, courseId);
		List<CourseCustomerEntitlementItem> courseEnts = courseCustomerEntitlementItemRepository.findByCustomerEntitlement_CustomerAndCourseId(customer, courseId);
		List<CourseGroupCustomerEntitlementItem> courseGroupEnts = courseGroupCustomerEntitlementItemRepository.findByCourseGroupCoursesIdAndCustomerEntitlementCustomerId(courseId, customer.getId());
		List<CustomerEntitlement> availableCustomerEntitlements = new ArrayList<CustomerEntitlement>();

		for(CourseCustomerEntitlementItem item : courseEnts){
			availableCustomerEntitlements.add(item.getCustomerEntitlement());
		}

		for(CourseGroupCustomerEntitlementItem item : courseGroupEnts){
			availableCustomerEntitlements.add(item.getCourseGroupCustomerEntitlement());
		}
		return availableCustomerEntitlements;
	}

	public List<DistributorEntitlement> findAllDistributorEntitlementsByDistributor(long distributorId) {
		//return entitlementDAO.findAllDistributorEntitlementsByDistributor(distributorId);
		return distributorEntitlementRepository.findByDistributorId(distributorId);
	}

	@Transactional
	public CustomerEntitlement saveCustomerEntitlement(CustomerEntitlement customerEntitlement, List<OrgGroupEntitlement> orgGroupEntitlements) {
		customerEntitlement = customerEntitlementRepository.save(customerEntitlement);

		if (orgGroupEntitlements != null) {
			orgGroupEntitlementRepository.save(orgGroupEntitlements);
		}

		return customerEntitlement;
	}

	public List<Course> findCoursesByCustomer(long courseIdArray[], String courseName, String courseGUID, String keywords, String searchCriteria) {
		return courseRepository.findCoursesByCustomer(ArrayUtils.toObject(courseIdArray), courseName, courseGUID, keywords, searchCriteria); //Basit
	}

	public DistributorEntitlement saveDistributorEntitlement(DistributorEntitlement distributorEntitlement) {
		//return entitlementDAO.saveDistributorEntitlement(distributorEntitlement);
		return distributorEntitlementRepository.save(distributorEntitlement);

	}

	public boolean isCustomerEntitlementExistByName(String customerEntitlementName) {
		return customerEntitlementRepository.isCustomerEntitlementExistByName(customerEntitlementName);  //Wajahat Ali
	}

	public List<CourseGroup> findCourseGroupsByDistributor(long courseGroupIdArray[], String courseGroupName) {
		return courseGroupRepository.findByIdInAndNameContaining(courseGroupIdArray, courseGroupName);
	}

	public List<CourseGroup> findCourseGroups(String courseGroupName) {

		return courseGroupRepository.findByNameContaining(courseGroupName);
	}

	public OrgGroupEntitlement getOrgGroupEntitlementByOrgGroupId(Long id) {
		return orgGroupEntitlementRepository.findFirstByOrganizationalGroupId(id);
	}

	@SuppressWarnings("null")
	public OrgGroupEntitlement getMaxAvaiableOrgGroupEntitlementByLearner(Learner learner, long entitlementId) {

		List<OrgGroupEntitlement> orgGroupEntitlementsForCustomerEntitlement = orgGroupEntitlementRepository.findByCustomerEntitlementIdAndMaxNumberSeatsGreaterThanEqual(entitlementId,0);//Wajahat Ali // this.getOrgGroupsEntilementsForCustomerEntitlement(customerEntitlement);
		//Modified By Marium Saud: Replace the method orgGroupLearnerGroupService.getOrgGroupsByLearner(learner) because for each learner enrollment Organizational Group
		// will be fetched along with its all children (as List<OrganizationalGroup> childrenOrgGroups) in OrganizationalGroup.java is marked as Eager (LMS-21545,LMS-21541)
		//List<OrganizationalGroup> orgGroupsForLearner = orgGroupLearnerGroupService.getOrgGroupsByLearner(learner);

		List<OrganizationalGroup> orgGroupsForLearner = orgGroupLearnerGroupService.getOrgGrpsForLearner(learner);

		if (orgGroupEntitlementsForCustomerEntitlement != null || orgGroupEntitlementsForCustomerEntitlement.size() > 0) {
			for (OrgGroupEntitlement orgGroupentitlement : orgGroupEntitlementsForCustomerEntitlement) {
				for (OrganizationalGroup orgGroup : orgGroupsForLearner) {
					// @MariumSaud : LMS-21702 : Removing Enrollment dependency from OrgGrpEntitlement Available Seats as MaxSeat column has been removed from Contract Creation
					//  if (orgGroupentitlement.getOrganizationalGroup().getId().equals(orgGroup.getId()) && orgGroupentitlement.hasAvailableSeats(1)) {
					if (orgGroupentitlement.getOrganizationalGroup().getId().equals(orgGroup.getId())) {
						return orgGroupentitlement;
					}
				}
			}
		}
		return null;
	}

	public long[] getCourseIDArrayForDistributor(Distributor distributor) {
		//Start:Ahsun(RR): ENGSUP-6409: Corp: LMS: Survey Tool Broken
		Long[] courseIds = courseRepository.getPublishedEntitledCourseIdArray(distributor.getId());
		return ArrayUtils.toPrimitive(courseIds);
		//End:Ahsun(RR): ENGSUP-6409: Corp: LMS: Survey Tool Broken
	}

	public Long[] getCourseGroupIDArrayForDistributor(Distributor distributor) {

		List<Map<Object,Object>> lst  = courseGroupCustomerEntitlementItemRepository.getCourseGroupIdsForDistributor(distributor);
		int index=0;
		Long[] courseGroupIDArray= new Long[lst.size()];
		for(Map<Object, Object> row : lst){
			courseGroupIDArray[index++]= Long.valueOf(String.valueOf(row.get("COURSEGROUP_ID")));
		}

		return courseGroupIDArray;//entitlementDAO.getCourseGroupIdsForDistributor(distributor);
	}


	private Hashtable arrangecourseObj(CourseGroup courseGroup, Hashtable coursesObjs) {

		List<Course> courses = courseGroup.getCourses();

		for (Course course : courses) {
            /*
                code added by Faisal
               courseIdArray[count] = course.getId().longValue();
               comment out the above line because it was overriding the courses as logic not incrementing the count in course loop
               idea behind using hashset and course id as key is to ensures the uniqueness of courses if courses are shared or duplicated in course group
                */
			coursesObjs.put(new Long(course.getId().longValue()), course);
		}
		if (courseGroup.getChildrenCourseGroups() != null && courseGroup.getChildrenCourseGroups().size() > 0) {
			List<CourseGroup> childCourseGroups = courseGroup.getChildrenCourseGroups();
			for (int childCount = 0; childCount < childCourseGroups.size(); childCount++) {
				coursesObjs = this.arrangecourseObj(childCourseGroups.get(childCount), coursesObjs);
			}
		}
		return coursesObjs;
	}

	public void deleteOrgGroupEntitlementInCustomerEntitlement(List<OrgGroupEntitlement> orgGroups) {
		orgGroupEntitlementRepository.delete(orgGroups);
	}

	public List<CustomerEntitlement> getCustomerEntitlementsByCourseId(Long courseId) {
		//List<CustomerEntitlement> lst = entitlementDAO.getCustomerEntitlementsByCourseId(courseId);
		List<CustomerEntitlement> lst = customerEntitlementRepository.getCustomerEntitlementsByCourseId(courseId);

		return lst;
	}

	/**
	 * @return the orgGroupLearnerGroupService
	 */
	public OrgGroupLearnerGroupService getOrgGroupLearnerGroupService() {
		return orgGroupLearnerGroupService;
	}

	/**
	 * @param orgGroupLearnerGroupService the orgGroupLearnerGroupService to set
	 */
	public void setOrgGroupLearnerGroupService(OrgGroupLearnerGroupService orgGroupLearnerGroupService) {
		this.orgGroupLearnerGroupService = orgGroupLearnerGroupService;
	}

	public List<LearnerEnrollment> getLearnerEnrollmentsAgainstCustomerEntitlement(CustomerEntitlement cE) {

		return learnerEnrollmentRepository.findByCustomerEntitlementId(cE.getId());
	}

	/**
	 * @return the courseAndCourseGroupService
	 */
	public CourseAndCourseGroupService getCourseAndCourseGroupService() {
		return courseAndCourseGroupService;
	}

	/**
	 * @param courseAndCourseGroupService the courseAndCourseGroupService to set
	 */
	public void setCourseAndCourseGroupService(CourseAndCourseGroupService courseAndCourseGroupService) {
		this.courseAndCourseGroupService = courseAndCourseGroupService;
	}

	// [08/05/2010] LMS-5544 :: Remove Contract
	public void removeEntitlementsWithEnrollments(Customer customer, CustomerEntitlement customerEntitlement) {


		List<LearnerEnrollment> learnerEnrollments = learnerEnrollmentRepository.findByCustomerEntitlementId(customerEntitlement.getId());
		Collection<Long> learnerEnrollmentsIds = null;

		if(learnerEnrollments!=null && learnerEnrollments.size()>0){
			learnerEnrollmentsIds = Collections2.transform(learnerEnrollments, new Function<LearnerEnrollment, Long>() {
						public Long apply(LearnerEnrollment arg0) {
							return Long.parseLong(arg0.getId().toString());
						}
					}
			);
		}


		if(learnerEnrollmentsIds!=null && learnerEnrollmentsIds.size()>0){

			List<LearnerSCOStatistics> lsco = learnerSCOStatisticsRepository.findByLearnerEnrollmentIdIn(learnerEnrollmentsIds);
			learnerSCOStatisticsRepository.delete(lsco);
			List<LearnerSelfStudyCourseActivity> lssca = learnerSelfStudyCourseActivityRepository.findByLearnerEnrollmentIdIn(learnerEnrollmentsIds);
			learnerSelfStudyCourseActivityRepository.delete(lssca);
			List<LearnerCourseStatistics> lcs = learnerCourseStatisticsRepository.findByLearnerEnrollmentIdIn(learnerEnrollmentsIds);
			learnerCourseStatisticsRepository.delete(lcs);

			//finally
			learnerEnrollmentRepository.delete(learnerEnrollments);

			customerEntitlementRepository.deleteCustomerEntitlement(customerEntitlement);

			List<TrainingPlanAssignment> ls = trainingPlanAssignmentRepository.findByLearnerEnrollmentsIdIn(learnerEnrollmentsIds);
			trainingPlanAssignmentRepository.delete(ls);

		}


	}

	public void deleteCustomerEntitlement(CustomerEntitlement objCustomerEntitlement) {
		customerEntitlementRepository.deleteCustomerEntitlement(objCustomerEntitlement);
	}
	public void deleteCourseEntitlementItems(List<CourseCustomerEntitlementItem> entitlementItems){
		deleteCourseCustomerEntitlementItems(entitlementItems);  //Kaunain Wajeeh
//    	List<LearnerEnrollment> learnerEnrollments = getLearnerEnrollmentsAgainstCustomerEntitlement(ce);
//    	int seatsRemoved = 0;
//		for(LearnerEnrollment learnerEnrollment : learnerEnrollments){
//			for(CourseCustomerEntitlementItem item : entitlementItems){
//					if(item.getCourse().getId().longValue() == learnerEnrollment.getCourse().getId().longValue()){
//						if(ce.getNumberSeatsUsed()>0)
//							seatsRemoved++;
//					}
//				}
//			}
//		if(seatsRemoved>0){
//			CustomerEntitlement updateableVersion = loadForUpdateCustomerEntitlement(ce.getId());
//			updateableVersion.setNumberSeatsUsed(updateableVersion.getNumberSeatsUsed()-seatsRemoved);
//			entitlementDAO.saveCustomerEntitlement(updateableVersion);
//		}
//		enrollmentService.dropEnrollments(learnerEnrollments);
	}
	public void deleteEntitlementItems(CourseGroupCustomerEntitlement cgce, long[] courseGroupIds){
		List<CourseGroupCustomerEntitlementItem> items = courseGroupCustomerEntitlementItemRepository.findByCustomerEntitlementIdAndCourseGroupIdIn(cgce.getId(), courseGroupIds);  //Kaunain Wajeeh
		deleteCourseGroupCEI(items); //Kaunain Wajeeh
//    	List<LearnerEnrollment> learnerEnrollments = getLearnerEnrollmentsAgainstCustomerEntitlement(cgce);
//    	List<Course> courses = null;
//    	int seatsRemoved = 0;
//		for(LearnerEnrollment learnerEnrollment : learnerEnrollments){
//			for(CourseGroupCustomerEntitlementItem item : items){
//				courses = item.getCourseGroup().getCourses();
//					if(courses.contains(learnerEnrollment.getCourse())){
//						//learnerEnrollment.setEnrollmentEndDate(new java.util.Date());
//						learnerEnrollment.setEnrollmentStatus(LearnerEnrollment.DROPPED);
//						if(cgce.getNumberSeatsUsed()>0)
//							seatsRemoved++;
//					}
//				}
//			}
//		if(seatsRemoved>0){
//			CustomerEntitlement updateableVersion = loadForUpdateCustomerEntitlement(cgce.getId());
//			updateableVersion.setNumberSeatsUsed(updateableVersion.getNumberSeatsUsed()-seatsRemoved);
//			entitlementDAO.saveCustomerEntitlement(updateableVersion);
//		}
//		enrollmentService.dropEnrollments(learnerEnrollments);
	}

	@Deprecated
	public List<CustomerEntitlement> searchCoursesForEnrollment(Customer customer, String courseName, String courseId, String entitlementName, String date, int resultSetLimit) {
		List<CustomerEntitlement> entitlements = null ; // entitlementDAO.searchCoursesForEnrollment(customer, courseName, courseId, entitlementName, date, resultSetLimit);
		return entitlements;
	}

	public List<EnrollmentCourseView> getCoursesForEnrollmentByCustomer(Customer customer,
																		String courseName, String courseCode, String entitlementName, Date date, Long[] customerEntitlementIds, int resultSetLimit) {
		/**Modified By Marium Saud LMS-20163 : TSM: Manager Mode > Plan & Enroll: System is showing Webinar course while its enrollment end date has been closed.
		 * The added List enrollmentCourseViewList holds the filtered Course records.
		 *
		 */
		List<EnrollmentCourseView> enrollmentCourseViewList = new ArrayList<EnrollmentCourseView>(500);//setting initial capacity reasonable as dynamic increase is expensive operation
		Long customerId = customer.getId();
		List<EnrollmentCourseView> enrollmentCourseViews = enrollmentCourseViewRepository.getCoursesForEnrollment(customerId, courseName, courseCode, entitlementName, date, customerEntitlementIds);
		if (!org.springframework.util.CollectionUtils.isEmpty(enrollmentCourseViews)) {
			for (EnrollmentCourseView enrollmentCourseView : enrollmentCourseViews) {
				long courseId = enrollmentCourseView.getCourseId();
				String courseType = enrollmentCourseView.getCourseType();
				if (StringUtils.isNotBlank(courseType)) {
					if (courseType.equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || courseType.equalsIgnoreCase(WebinarCourse.COURSE_TYPE) || courseType.equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
						List<SynchronousClass> synchronousClasses = getSynchronousClasses(courseType, courseId);
						if (!org.springframework.util.CollectionUtils.isEmpty(synchronousClasses)) {
							enrollmentCourseView.setSyncClasses(synchronousClasses);
						}
						else{
							/**Modified By Marium Saud LMS-20163 : TSM: Manager Mode > Plan & Enroll: System is showing Webinar course while its enrollment end date has been closed.
							 * Code Missing as compare with TopLink branch due to which the Courses are not being filtered depending on specific course Type
							 */
							continue;
						}
					}
				}
				enrollmentCourseView.setAlphaNumeicCourseId(Long.toString(courseId));
				enrollmentCourseView.setBusinessKey(enrollmentCourseView.getCourseCode());
				enrollmentCourseViewList.add(enrollmentCourseView);
			}
		}
		return enrollmentCourseViewList;
	}

	private List<SynchronousClass> getSynchronousClasses(String courseType, long courseId) {

		List<SynchronousClass> synchronousClasses = null;
		if(courseType.equalsIgnoreCase(InstructorConnectCourse.COURSE_TYPE)) {
			Course course = synchronousClassService.getInstructorCourse(courseId);
			if (course != null) {
				if((course instanceof InstructorConnectCourse)) {
					InstructorConnectCourse dbcourse = (InstructorConnectCourse) course;
					String instructorType = dbcourse.getInstructorType();
					if (StringUtils.isNotBlank(instructorType)) {
						if(!instructorType.equals("Email Online")) {
							if(synchronousClassService.isSyncCourseAssignable(courseId)) {
								synchronousClasses = synchronousClassService.getAllSynchClassesOfCourse(courseId);
							}
						}
					}
				}
			}
		} else {
			if(synchronousClassService.isSyncCourseAssignable(courseId)){
				synchronousClasses = synchronousClassService.getAllSynchClassesOfCourse(courseId);
			}
		}
		return synchronousClasses;
	}

	public List<EnrollmentCourseView> getEnrollmentCourseViewsByCustomerAndCourseIds(List<CustomerEntitlement> customerEntitlementList, Long[] courseIds) {

		//List<Map<Object, Object>> resultSet = entitlementDAO.getCoursesForEnrollmentByCourseIds(customerEntitlementList, courseIds, 0);
		List<Map<Object, Object>> resultSet = courseRepository.findByCustomerEntitlementsByCourseIds(customerEntitlementList, courseIds);
		List<EnrollmentCourseView> enrollmentCourseViewList = new ArrayList<EnrollmentCourseView>(500);//setting initial capacity reasonable as dynamic increase is expensive operation
		EnrollmentCourseView enrollmentCourseView = null;
		List<String> uniqueCourseIds = new ArrayList<String>();
		Calendar calendar1=Calendar.getInstance();
		Calendar calendar2=Calendar.getInstance();
		Date endDate;
		for (Map<Object, Object> row : resultSet) {
			String courseId = row.get("COURSE_ID") + "";
			if ( StringUtils.isNotBlank(courseId) && !uniqueCourseIds.contains(courseId)) {
				enrollmentCourseView = new EnrollmentCourseView();
				enrollmentCourseView.setCourseId(Long.parseLong((row.get("COURSE_ID") + "")));
				enrollmentCourseView.setCourseName((String) row.get("COURSENAME"));
				enrollmentCourseView.setCourseType((String) row.get("COURSETYPE"));
				enrollmentCourseView.setBusinessKey((String) row.get("COURSECODE"));
				enrollmentCourseView.setTotalSeats( Long.parseLong(row.get("TOTAL_SEATS").toString() ));
				enrollmentCourseView.setSeatsUsed((Integer) row.get("NUMBERSEATSUSED"));
				enrollmentCourseView.setEntitlementId( Long.parseLong(row.get("CUSTOMERENTITLEMENT_ID").toString()));
				enrollmentCourseView.setTermsOfService((Integer) row.get("TOS"));
				enrollmentCourseView.setUnlimitedEnrollments((Integer) row.get("ALLOWUNLIMITEDENROLLMENTS"));

				Date entitlementExpirationDate = (Date) row.get("EXPIRATION_DATE");
				enrollmentCourseView.setExpirationDate(entitlementExpirationDate);

				Date entitlementStartDate = (Date) row.get("STARTDATE");
				enrollmentCourseView.setEntitlementStartDate(entitlementStartDate);

				Date entitlementEndDate = (Date) row.get("ENDDATE");
				enrollmentCourseView.setEntitlementEndDate(entitlementEndDate);

				if(row.get("COURSEGROUP_ID") !=null)
					enrollmentCourseView.setCourseGroupId(Long.parseLong(row.get("COURSEGROUP_ID").toString()));

				//Added for Synchronous classess
				if( enrollmentCourseView.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE)
						|| enrollmentCourseView.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
					//enrollmentCourseView.sets
					List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassByCourseId(Long.parseLong((row.get("COURSE_ID") + "")));

					if( syncClasses != null && syncClasses.size() > 0 ) {
						enrollmentCourseView.setSyncClasses(syncClasses);
						for( SynchronousClass syncClass : syncClasses ) {
							endDate = syncClass.getClassEndDate();
							calendar2.setTime(endDate);
							if(calendar2.after(calendar1)){
								syncClass.setSelected(true);
								//syncClass.setSynchronousSessions(synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId()));
								//todo
								uniqueCourseIds.add(courseId);
								enrollmentCourseViewList.add(enrollmentCourseView);
								break;
							}
						}
					}
				}else {

					uniqueCourseIds.add(courseId);
					enrollmentCourseViewList.add(enrollmentCourseView);
				}

			}
		}
		return enrollmentCourseViewList;
	}


	public List<EnrollmentCourseView> getEnrollmentCourseViewsByCustomerCourseAndCourseGroupIds(List<CustomerEntitlement> customerEntitlementList, Long courseId, Long courseGroupId) {

		//List<Map<Object, Object>> resultSet = entitlementDAO.getCoursesForEnrollmentByCourseIds(customerEntitlementList, courseIds, 0);
		//List<Map<Object, Object>> resultSet = entitlementDAO.getCoursesForEnrollmentByCourseAndCourseGroupId(customerEntitlementList, courseId,courseGroupId, 0);
		List<Map<Object, Object>> resultSet = courseRepository.getByCourseAndCourseGroupId(customerEntitlementList, courseId,courseGroupId);
		List<EnrollmentCourseView> enrollmentCourseViewList = new ArrayList<EnrollmentCourseView>(500);//setting initial capacity reasonable as dynamic increase is expensive operation
		EnrollmentCourseView enrollmentCourseView = null;
		List<String> uniqueCourseIds = new ArrayList<String>();
		Calendar calendar1=Calendar.getInstance();
		Calendar calendar2=Calendar.getInstance();
		Date endDate;
		for (Map<Object, Object> row : resultSet) {
			String courseIdString = row.get("COURSE_ID") + "";
			if ( StringUtils.isNotBlank(courseIdString) && !uniqueCourseIds.contains(courseIdString)) {
				enrollmentCourseView = new EnrollmentCourseView();
				enrollmentCourseView.setCourseId(Long.parseLong((row.get("COURSE_ID") + "")));
				enrollmentCourseView.setCourseName((String) row.get("COURSENAME"));
				enrollmentCourseView.setCourseType((String) row.get("COURSETYPE"));
				enrollmentCourseView.setBusinessKey((String) row.get("COURSECODE"));
				enrollmentCourseView.setTotalSeats( Long.parseLong(row.get("TOTAL_SEATS").toString()));
				enrollmentCourseView.setSeatsUsed((Integer) row.get("NUMBERSEATSUSED"));
				enrollmentCourseView.setEntitlementId(Long.parseLong(row.get("CUSTOMERENTITLEMENT_ID").toString()));
				enrollmentCourseView.setTermsOfService((Integer) row.get("TOS"));
				enrollmentCourseView.setUnlimitedEnrollments((Integer) row.get("ALLOWUNLIMITEDENROLLMENTS"));

				Date entitlementExpirationDate = (Date) row.get("EXPIRATION_DATE");
				enrollmentCourseView.setExpirationDate(entitlementExpirationDate);

				Date entitlementStartDate = (Date) row.get("STARTDATE");
				enrollmentCourseView.setEntitlementStartDate(entitlementStartDate);

				Date entitlementEndDate = (Date) row.get("ENDDATE");
				enrollmentCourseView.setEntitlementEndDate(entitlementEndDate);

				if(row.get("COURSEGROUP_ID") !=null)
					enrollmentCourseView.setCourseGroupId(Long.parseLong(row.get("COURSEGROUP_ID").toString()));

				//Added for Synchronous classess
				if( enrollmentCourseView.getCourseType().equalsIgnoreCase(SynchronousCourse.COURSE_TYPE)
						|| enrollmentCourseView.getCourseType().equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
					//enrollmentCourseView.sets
					List<SynchronousClass> syncClasses = synchronousClassService.getSynchronousClassByCourseId(Long.parseLong((row.get("COURSE_ID") + "")));

					if( syncClasses != null && syncClasses.size() > 0 ) {
						enrollmentCourseView.setSyncClasses(syncClasses);
						for( SynchronousClass syncClass : syncClasses ) {
							endDate = syncClass.getClassEndDate();
							calendar2.setTime(endDate);
							if(calendar2.after(calendar1)){
								syncClass.setSelected(true);
								//syncClass.setSynchronousSessions(synchronousClassService.getSynchronousSessionsByClassId(syncClass.getId()));
								//todo
								uniqueCourseIds.add(courseIdString);
								enrollmentCourseViewList.add(enrollmentCourseView);
								break;
							}
						}
					}
				}else {

					uniqueCourseIds.add(courseIdString);
					enrollmentCourseViewList.add(enrollmentCourseView);
				}

			}
		}
		return enrollmentCourseViewList;
	}


	/**
	 * ahsun
	 * when course id is null in case of Tp whisch do not have courses associated
	 * This method returns list of training plan courses that are also in customer entitlement.
	 */
	public List<TrainingPlanCourseView> getEntitledCoursesFromTrainingPlanCourses(List<TrainingPlanCourse> trainingPlanCourseList, int resultSetLimit) {

		Long customerId = ((VU360UserAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getCurrentCustomerId();

		Long[] trainingPlanCourseIds = new Long[trainingPlanCourseList.size()];
		for (int i=0;i<trainingPlanCourseIds.length;i++) {
			//if course in TP are null
			if(trainingPlanCourseList.get(i).getCourse()==null)
				return Collections.EMPTY_LIST;
			trainingPlanCourseIds[i] =  trainingPlanCourseList.get(i).getCourse().getId();
		}

		List<Map<Object, Object>> resultSet = courseRepository.getCoursesForEnrollmentByCourseIdsAndCustomer(customerId, Arrays.asList(trainingPlanCourseIds));
		List<TrainingPlanCourseView> enrollmentCourseViewList = new ArrayList<TrainingPlanCourseView>(500);//setting initial capacity reasonable as dynamic increase is expensive operation
		TrainingPlanCourseView enrollmentCourseView = null;
		List<String> uniqueCourseIds = new ArrayList<String>();
		for (Map<Object, Object> row : resultSet) {
			String courseId = row.get("COURSE_ID") + "";
			if ( StringUtils.isNotBlank(courseId) && !uniqueCourseIds.contains(courseId)) {
				enrollmentCourseView = new TrainingPlanCourseView();
				enrollmentCourseView.setCourseId(courseId);

				if(row.get("COURSEGROUP_ID") != null)
					enrollmentCourseView.setCourseGroupId(Long.parseLong(row.get("COURSEGROUP_ID").toString()));

				enrollmentCourseView.setCourseName((String) row.get("COURSENAME"));
				enrollmentCourseView.setCourseType((String) row.get("COURSETYPE"));
				enrollmentCourseView.setBusinessKey((String) row.get("COURSECODE"));
				enrollmentCourseView.setTotalSeats(Long.parseLong(row.get("TOTAL_SEATS").toString()));
				enrollmentCourseView.setSeatsUsed((Integer) row.get("NUMBERSEATSUSED"));
				enrollmentCourseView.setEntitlementId(Long.parseLong( row.get("CUSTOMERENTITLEMENT_ID").toString()));
				enrollmentCourseView.setEntitlementName((String) row.get("ENTITLEMENT_NAME"));
				enrollmentCourseView.setTermsOfService((Integer) row.get("TOS"));
				enrollmentCourseView.setUnlimitedEnrollments((Integer) row.get("ALLOWUNLIMITEDENROLLMENTS"));

				Date entitlementExpirationDate = (Date) row.get("EXPIRATION_DATE");
				enrollmentCourseView.setExpirationDate(entitlementExpirationDate);

				Date entitlementStartDate = (Date) row.get("STARTDATE");
				enrollmentCourseView.setEntitlementStartDate(entitlementStartDate);

				Date entitlementEndDate = (Date) row.get("ENDDATE");
				enrollmentCourseView.setEntitlementEndDate(entitlementEndDate);
				uniqueCourseIds.add(courseId);
				enrollmentCourseViewList.add(enrollmentCourseView);
			}
		}
		return enrollmentCourseViewList;
	}

	public List<EnrollmentCourseView> getCoursesForTrainingPlanByCustomer(Customer customer, String courseName, String courseId, String entitlementName, Date date, Long[] customerEntitlementIds, int resultSetLimit) {

		//List<Map<Object, Object>> resultSet = entitlementDAO.getCoursesForEnrollment(customer, courseName, courseId, entitlementName, date, resultSetLimit);
		List<Map<Object, Object>> resultSet = courseRepository.findByCustomerIdBycourseNameByCourseIdByEntitlementIdByExpiryDate(customer.getId(), courseName, courseId, entitlementName, date, customerEntitlementIds);

		List<EnrollmentCourseView> trainingPlanCourseViewList = new ArrayList<EnrollmentCourseView>(500);//setting initial capacity reasonable as dynamic increase is expensive operation
		EnrollmentCourseView trainingPlanCourseView = null;
		//int courseIdIterator=0;
		long[] courseIds = new long[resultSet.size()];
		for (Map<Object, Object> row : resultSet) {
			trainingPlanCourseView = new EnrollmentCourseView();

			trainingPlanCourseView.setCourseName((String) row.get("COURSENAME"));
			trainingPlanCourseView.setCourseId(Long.parseLong(row.get("COURSE_ID").toString()));
			trainingPlanCourseView.setCourseType((String) row.get("COURSETYPE"));
			trainingPlanCourseView.setBusinessKey((String) row.get("COURSECODE"));
			trainingPlanCourseView.setTotalSeats(Long.parseLong(row.get("TOTAL_SEATS").toString()));
			trainingPlanCourseView.setSeatsUsed((Integer) row.get("NUMBERSEATSUSED"));
			trainingPlanCourseView.setEntitlementId(Long.parseLong(row.get("CUSTOMERENTITLEMENT_ID").toString()));
			trainingPlanCourseView.setTermsOfService((Integer) row.get("TOS"));
			trainingPlanCourseView.setUnlimitedEnrollments((Integer) row.get("ALLOWUNLIMITEDENROLLMENTS"));

			Date entitlementExpirationDate = (Date) row.get("EXPIRATION_DATE");
			trainingPlanCourseView.setExpirationDate(entitlementExpirationDate);

			Date entitlementStartDate = (Date) row.get("STARTDATE");
			trainingPlanCourseView.setEntitlementStartDate(entitlementStartDate);

			Date entitlementEndDate = (Date) row.get("ENDDATE");
			trainingPlanCourseView.setEntitlementEndDate(entitlementEndDate);

			trainingPlanCourseViewList.add(trainingPlanCourseView);
		}

		return trainingPlanCourseViewList;
	}

	/**
	 * //[1/18/2011] LMS-8661 :: Search Courses and Course Groups By Reseller based on Search Parameters
	 * @param distributor
	 * @param courseName
	 * @param courseGUID
	 * @param keywords
	 * @param contractType
	 * @return HashMap<CourseGroup, List<Course>>
	 */
	public HashMap<CourseGroup, List<Course>> findCoursesAndCourseGroupsByDistributor(Distributor distributor,String courseName, String entityId, String keywords, String contractType){

		Set<CourseGroup> courseGroups = new HashSet<CourseGroup>();
		Set<CourseGroup> finalCourseGroups = new HashSet<CourseGroup>();
		HashMap<CourseGroup, List<Course>> finalList =  new HashMap<CourseGroup, List<Course>>();

		List<CourseGroup> courseGroupList = courseGroupRepository.findCoursesAndCourseGroupsByDistributor(distributor, courseName, entityId, keywords,contractType);  //Kaunain Wajeeh

		// Iterate through all course groups and get all children
		for(CourseGroup cg:courseGroupList){
			courseGroups.add(cg);
			for(CourseGroup childCG : cg.getAllChildrenInHierarchy()){
				courseGroups.add(childCG);
			}
		}

		boolean isBlankSearch = (StringUtils.isBlank(courseName) && StringUtils.isBlank(entityId) && StringUtils.isBlank(keywords));
		for(CourseGroup courseGroup : courseGroups){

			if(isBlankSearch){
				finalCourseGroups.add(courseGroup);
				finalList.put(courseGroup, courseGroup.getActiveCourses());
				for(CourseGroup childCG : courseGroup.getAllChildrenInHierarchy()){
					finalCourseGroups.add(childCG);
					if(!finalList.containsKey(childCG)){
						finalList.put(childCG, childCG.getActiveCourses());
					}
				}
			}
			else {
				if(contractType.equals("CourseGroup")){
					String []courseNamesList=courseName.split(",");
					for(int i=0;i<courseNamesList.length;i++){
						if(StringUtils.isNotEmpty(courseNamesList[i])) 	{
							if(courseGroup.getName().equalsIgnoreCase(courseNamesList[i].trim()) || courseGroup.getName().toUpperCase().contains(courseNamesList[i].trim().toUpperCase())){
								finalCourseGroups.add(courseGroup);
								finalList.put(courseGroup, null);

								for(CourseGroup childCG : courseGroup.getAllChildrenInHierarchy()){
									finalCourseGroups.add(childCG);
									if(!finalList.containsKey(childCG))
										finalList.put(childCG, null);
								}
							}
						}
					}
					String []courseGroupIdList= entityId.split(",");
					for(int i=0;i<courseGroupIdList.length;i++){
						if(StringUtils.isNotEmpty(courseGroupIdList[i])) 	{
							if(courseGroup.getCourseGroupID()!=null && (courseGroup.getCourseGroupID().equalsIgnoreCase(courseGroupIdList[i].trim()) || courseGroup.getCourseGroupID().toUpperCase().contains(courseGroupIdList[i].trim().toUpperCase()))){
								finalCourseGroups.add(courseGroup);
								finalList.put(courseGroup, null);

								for(CourseGroup childCG : courseGroup.getAllChildrenInHierarchy()){
									finalCourseGroups.add(childCG);
									if(!finalList.containsKey(childCG))
										finalList.put(childCG, null);
								}
							}
						}
					}
					if(StringUtils.isNotEmpty(keywords)) {
						if(courseGroup.getKeywords()!=null){
							if(courseGroup.getKeywords().equalsIgnoreCase(keywords) || courseGroup.getKeywords().toUpperCase().contains(keywords.toUpperCase())){
								finalCourseGroups.add(courseGroup);
								finalList.put(courseGroup, null);
							}
						}
					}

				}

				else { //i.e its a Course type contract
					List<Course> courseList = new ArrayList<Course>();
					for(Course course : courseGroup.getActiveCourses()){
						if(StringUtils.isNotEmpty(courseName)){
							String []courseNamesList=courseName.split(",");
							for(int i=0;i<courseNamesList.length;i++){
								if(course.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED) &&( course.getCourseTitle().trim().equalsIgnoreCase(courseNamesList[i].trim()) || course.getCourseTitle().trim().toUpperCase().contains(courseNamesList[i].trim().toUpperCase()))){
									courseList.add(course);
								}
							}
						}

						if(StringUtils.isNotEmpty(entityId)){
							String []courseIDList=entityId.split(",");
							for(int i=0;i<courseIDList.length;i++){
								if(course.getBussinesskey()!=null){
									if(course.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED) &&( course.getBussinesskey().equalsIgnoreCase(courseIDList[i].trim()) || course.getBussinesskey().toUpperCase().contains(courseIDList[i].trim().toUpperCase()))){
										courseList.add(course);
									}
								}
							}
						}

						if(StringUtils.isNotEmpty(keywords)){
							if(course.getKeywords()!=null){
								if(course.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED) &&( course.getKeywords().equalsIgnoreCase(keywords) || course.getKeywords().toUpperCase().contains(keywords.toUpperCase()))){
									courseList.add(course);
								}
							}
						}
					}

					if (courseList.size() > 0) {
						finalCourseGroups.add(courseGroup);
						finalList.put(courseGroup, courseList);
					}
				}
			}
		}

		return finalList;
	}


	public HashMap<CourseGroup, List<Course>> findCourseInCustomerEntitlementsBySearchCriteria(Customer customer, String courseName, String courseGUID, String keywords)
	{
		HashMap<CourseGroup, List<Course>> resultMap =  new HashMap<CourseGroup, List<Course>>();


		boolean isCourseName=false,isCourseGUID=false,isKeywords=false;
		List<CourseCustomerEntitlementItem> lst = new ArrayList<CourseCustomerEntitlementItem>();
		if(StringUtils.isNotEmpty(courseName))
		{
			isCourseName=true;

		}

		if(StringUtils.isNotEmpty(courseGUID))
		{
			isCourseGUID=true;

		}

		if(StringUtils.isNotEmpty(keywords))
		{
			isKeywords=true;

		}

		if(isCourseName && !isCourseGUID && !isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWith(customer.getId(),Course.PUBLISHED,courseName);
		}else if(!isCourseName && isCourseGUID && !isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseBussinesskeyStartingWith(customer.getId(),Course.PUBLISHED,courseGUID);
		}else if(!isCourseName && !isCourseGUID && isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseKeywordsStartingWith(customer.getId(),Course.PUBLISHED,keywords);
		}else if(isCourseName && isCourseGUID && !isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseBussinesskeyStartingWith(customer.getId(),Course.PUBLISHED,courseName,courseGUID);
		}else if(isCourseName && !isCourseGUID && isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseKeywordsStartingWith(customer.getId(),Course.PUBLISHED,courseName,keywords);
		}else if(!isCourseName && isCourseGUID && isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseBussinessKeyStartingWithAndCourseKeywordsStartingWith(customer.getId(),Course.PUBLISHED,courseGUID,keywords);
		}else if(isCourseName && isCourseGUID && isKeywords){
			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatusAndCourseCourseTitleStartingWithAndCourseBussinesskeyStartingWithAndCourseKeywordsStartingWith(customer.getId(),Course.PUBLISHED,courseName,courseGUID,keywords);
		}else{

			lst =courseCustomerEntitlementItemRepository.findByCustomerEntitlementCustomerIdAndCourseCourseStatus(customer.getId(),Course.PUBLISHED);

		}


		List<CourseCustomerEntitlementItem> courseEntitlements = lst;
		isCourseName=false;
		isCourseGUID=false;
		isKeywords=false;


		List<CourseGroupCustomerEntitlementItem> lst2 = new ArrayList<CourseGroupCustomerEntitlementItem>();
		if(StringUtils.isNotEmpty(courseName))
		{
			isCourseName=true;

		}

		if(StringUtils.isNotEmpty(courseGUID))
		{
			isCourseGUID=true;

		}

		if(StringUtils.isNotEmpty(keywords))
		{
			isKeywords=true;

		}

		if(isCourseName && !isCourseGUID && !isKeywords){
			lst2 =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseTitle(customer.getId(),Course.PUBLISHED,courseName);
		}else if(!isCourseName && isCourseGUID && !isKeywords){
			lst2 =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseBussinessKey(customer.getId(),Course.PUBLISHED,courseGUID);
		}else if(!isCourseName && !isCourseGUID && isKeywords){
			lst2 =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseKeywords(customer.getId(),Course.PUBLISHED,keywords);
		}else if(isCourseName && isCourseGUID && !isKeywords){
			lst2 =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseTitleAndCourseBussinessKey(customer.getId(),Course.PUBLISHED,courseName,courseGUID);
		}else if(isCourseName && !isCourseGUID && isKeywords){
			lst2 =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseTitleAndCourseKeywords(customer.getId(),Course.PUBLISHED,courseName,keywords);
		}else if(!isCourseName && isCourseGUID && isKeywords){
			lst2 =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseBussinessKeyAndCourseKeywords(customer.getId(),Course.PUBLISHED,courseGUID,keywords);
		}else if(isCourseName && isCourseGUID && isKeywords){
			lst2 =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatusAndCourseTitleAndCourseBussinessKeyAndKeywords(customer.getId(),Course.PUBLISHED,courseName,courseGUID,keywords);
		}else{
			lst2 =courseGroupCustomerEntitlementItemRepository.findByCustomerIdAndStatus(customer.getId(),Course.PUBLISHED);
		}
		List<CourseGroupCustomerEntitlementItem> courseGroupEntitlements = lst2;

		CourseGroup miscCourseGroup = new CourseGroup();
		java.util.Date date = new java.util.Date();
		miscCourseGroup.setId(new Long((date.getHours()+ date.getMinutes() + date.getSeconds())*100));
		miscCourseGroup.setName("Miscellaneous");

		Calendar todaysCalendar = Calendar.getInstance();
		Calendar entitlementExpiryDateCalendar = Calendar.getInstance();
		for (CourseCustomerEntitlementItem item : courseEntitlements) {
			if((item.getCustomerEntitlement().isAllowUnlimitedEnrollments()
					&& item.getCustomerEntitlement().getRemainingSeats() <= 0)
					|| (!item.getCustomerEntitlement().isAllowUnlimitedEnrollments()
					&& item.getCustomerEntitlement().getRemainingSeats() > 0)) {

				Date endDate = item.getCustomerEntitlement().getEndDate();
				if (endDate != null) {
					entitlementExpiryDateCalendar.setTime(endDate);
				}
				else {
					int noOfTermOfServiceDays = item.getCustomerEntitlement().getDefaultTermOfServiceInDays();
					Date startDate = item.getCustomerEntitlement().getStartDate();
					entitlementExpiryDateCalendar.setTime(startDate);
					entitlementExpiryDateCalendar.add(Calendar.DAY_OF_YEAR, noOfTermOfServiceDays);
				}

				//Filter out Expired Contracts
				if (entitlementExpiryDateCalendar.after(todaysCalendar) || entitlementExpiryDateCalendar.equals(todaysCalendar)) {

					Course c = item.getCourse();
					if(c.isActive()) { // [1/27/2011] LMS-7183 :: Retired Course Functionality II
						CourseGroup cg = item.getCourseGroup();

						List<Course> courses = null;
						if(cg!=null){
							courses = resultMap.get(cg);
						}
						else {
							cg = miscCourseGroup;
							courses = resultMap.get(cg);
						}

						boolean isBlankSearch = true;
						boolean courseMatchesCriteria = true;
						if((StringUtils.isNotEmpty(courseName))) {
							isBlankSearch = false;
							if(c.getCourseTitle().equalsIgnoreCase(courseName)
									|| c.getCourseTitle().toUpperCase().contains(courseName.toUpperCase()))
							{
								courseMatchesCriteria = true;
							}
							else {
								courseMatchesCriteria = false;
							}
						}
						if(courseMatchesCriteria && StringUtils.isNotEmpty(courseGUID)) {
							isBlankSearch = false;
							if(c.getBussinesskey().equalsIgnoreCase(courseGUID)
									|| c.getBussinesskey().toUpperCase().contains(courseGUID.toUpperCase()))
							{
								courseMatchesCriteria = true;
							}
							else {
								courseMatchesCriteria = false;
							}
						}
						if(courseMatchesCriteria && StringUtils.isNotEmpty(keywords)) {
							isBlankSearch = false;
							if(c.getKeywords().equalsIgnoreCase(keywords)
									|| c.getKeywords().toUpperCase().contains(keywords.toUpperCase()))
							{
								courseMatchesCriteria = true;
							}
							else {
								courseMatchesCriteria = false;
							}
						}
						if (courseMatchesCriteria || isBlankSearch){
							if(courses == null || courses.isEmpty()) {
								courses = new ArrayList<Course>();
								courses.add(c);
							}
							else if (!courses.contains(c)){
								courses.add(c);
							}
						}
						resultMap.put(cg, courses);
					}
				}
			}
		}

		for (CourseGroupCustomerEntitlementItem item : courseGroupEntitlements) {

			if((item.getCourseGroupCustomerEntitlement().isAllowUnlimitedEnrollments()
					&& item.getCourseGroupCustomerEntitlement().getRemainingSeats() <= 0)
					|| (!item.getCourseGroupCustomerEntitlement().isAllowUnlimitedEnrollments()
					&& item.getCourseGroupCustomerEntitlement().getRemainingSeats() > 0)) {

				Date endDate = item.getCourseGroupCustomerEntitlement().getEndDate();
				if (endDate != null) {
					entitlementExpiryDateCalendar.setTime(endDate);
				}
				else {
					int noOfTermOfServiceDays = item.getCourseGroupCustomerEntitlement().getDefaultTermOfServiceInDays();
					Date startDate = item.getCourseGroupCustomerEntitlement().getStartDate();
					entitlementExpiryDateCalendar.setTime(startDate);
					entitlementExpiryDateCalendar.add(Calendar.DAY_OF_YEAR, noOfTermOfServiceDays);
				}

				//Filter out Expired Contracts
				if (entitlementExpiryDateCalendar.after(todaysCalendar) || entitlementExpiryDateCalendar.equals(todaysCalendar)) {
					//List<CourseGroup> courseGroups = entitlement.getCourseGroups();

					CourseGroup cg = null;

					cg = item.getCourseGroup();
					List<Course> cgCourses = cg.getActiveCourses();
					for (Course c : cgCourses) {
						List<Course> courses = null;
						if(cg!=null){
							courses = resultMap.get(cg);
						}
						else {
							cg = miscCourseGroup;
							courses = resultMap.get(cg);
						}

						boolean isBlankSearch = true;
						boolean courseMatchesCriteria = true;
						if((StringUtils.isNotEmpty(courseName))) {
							isBlankSearch = false;
							if(c.getCourseTitle().equalsIgnoreCase(courseName)
									|| c.getCourseTitle().toUpperCase().contains(courseName.toUpperCase()))
							{
								courseMatchesCriteria = true;
							}
							else {
								courseMatchesCriteria = false;
							}
						}
						if(courseMatchesCriteria && StringUtils.isNotEmpty(courseGUID)) {
							isBlankSearch = false;
							if(c.getBussinesskey().equalsIgnoreCase(courseGUID)
									|| c.getBussinesskey().toUpperCase().contains(courseGUID.toUpperCase()))
							{
								courseMatchesCriteria = true;
							}
							else {
								courseMatchesCriteria = false;
							}
						}
						if(courseMatchesCriteria && StringUtils.isNotEmpty(keywords)) {
							isBlankSearch = false;
							if(c.getKeywords().equalsIgnoreCase(keywords)
									|| c.getKeywords().toUpperCase().contains(keywords.toUpperCase()))
							{
								courseMatchesCriteria = true;
							}
							else {
								courseMatchesCriteria = false;
							}
						}
						if (courseMatchesCriteria || isBlankSearch){
							if(courses == null || courses.isEmpty()) {
								courses = new ArrayList<Course>();
								courses.add(c);
							}
							else if (!courses.contains(c)){
								courses.add(c);
							}
						}
						resultMap.put(cg, courses);
					}

				}
			}
		}

		return resultMap;
	}

	/**
	 * [08/03/2010] LMS-5544 :: Remove Contract
	 */
	@Override
	public List<CustomerEntitlement> getSimilarCustomerEntitlements(Long customerEntitlementId, Long totalEnrollments) {

		return customerEntitlementRepository.getSimilarCustomerEntitlements(customerEntitlementId, totalEnrollments);
	}

	/**
	 * // [08/03/2010] LMS-5544 :: Remove Contract
	 */
	@Override
	public void moveCustomerEnrollments(Customer customer, Long sourceContractId, Long destinationContractId) {

		CustomerEntitlement sourceContract = this.loadForUpdateCustomerEntitlement(sourceContractId);
		CustomerEntitlement destinationContract = this.loadForUpdateCustomerEntitlement(destinationContractId);

		if (sourceContract != null && destinationContract != null) {
			List<LearnerEnrollment> learnerEnrollmentList = this.getLearnerEnrollmentsAgainstCustomerEntitlement(sourceContract);

			if (learnerEnrollmentList.size() > 0) {
				// Move Enrollments

				List<LearnerEnrollment> lst = learnerEnrollmentRepository.findByCustomerEntitlementId(sourceContractId);
				CustomerEntitlement customerEntitlement = new CustomerEntitlement();
				customerEntitlement = customerEntitlementRepository.findOne(destinationContractId);
				for(LearnerEnrollment obj : lst){

					obj.setCustomerEntitlement(customerEntitlement);
					obj.setOrgGroupEntitlement( null );
				}

				learnerEnrollmentRepository.save(lst);
				//int enrollmentsMoved = this.entitlementDAO.moveCustomerEnrollments(customer, sourceContract, destinationContract);
				int enrollmentsMoved =lst.size();
				// Update Number Of Seats used for Destination Contract
				if (enrollmentsMoved > 0) {
					destinationContract.setNumberSeatsUsed( destinationContract.getNumberSeatsUsed() + enrollmentsMoved );
					//this.entitlementDAO.saveCustomerEntitlement(destinationContract);
					customerEntitlementRepository.save(destinationContract);
				}
			}
		}
	}

	/**
	 * // [09/06/2010] LMS-6997 :: Org. Group Entitlement: update seatsUsed after selfEnrollment
	 */
	@Override
	public void updateSeatsUsed(CustomerEntitlement cE, OrgGroupEntitlement orgGroupEntitlement, int seatCountToAdd){
		try{
			Long orgGroupID = (orgGroupEntitlement != null) ? orgGroupEntitlement.getId() : -1L;
			//this.entitlementDAO.updateSeatsUsed(cE.getId(), orgGroupID, seatCountToAdd);

			try {
				CustomerEntitlement customerContract = customerEntitlementRepository.findOne( cE.getId() );
				int seatCount = customerContract.getNumberSeatsUsed() +  seatCountToAdd;
				customerContract.setNumberSeatsUsed( seatCount <= 0 ? 0 : seatCount );
				customerEntitlementRepository.save( customerContract );

				if (orgGroupID > 0  ) {
					OrgGroupEntitlement orgGroupContract = orgGroupEntitlementRepository.findOne( orgGroupID );
					if (orgGroupContract != null) {
						seatCount = orgGroupContract.getNumberSeatsUsed() + seatCountToAdd;
						orgGroupContract.setNumberSeatsUsed( seatCount <= 0 ? 0 : seatCount );
						orgGroupEntitlementRepository.save( orgGroupContract );
					}
				}
			}
			catch (Exception e) {
				log.debug(e);
			}

		}catch(Exception e){
			log.error("Error Occured while upating customer entitlement["+cE.getId()+"]");
			log.error(e);
		}
		//entitlementDAO.refreshContractInCache(cE, orgGroupEntitlement);
	}
	public CustomerEntitlement getSystemManagedContract(Customer customer) {

		List<CustomerEntitlement>cEList= customerEntitlementRepository.findByCourseTypeByCustomerAndIsSystemManagedTrue(customer.getId());
		if(cEList!=null && cEList.size()>0)
			return cEList.get(0); //because ideally there should only be 1 system managed contract for a customer
		else
			return null;
	}

	/*
	 * CourseCustomerEntitlement -> List<Course> getCourses()
	 */
	public List<Course> getCoursesByCustomerEntitlement(CustomerEntitlement ce){
		return null;
	}
	/*
	 * reference
	 * CustomerEntitlement -> getListOfCourseGroups()
	 * CourseGroupCustomerEntitlement ->getCourseGroups()
	 */
	public Set<CourseGroup> getCourseGroupsByEntitlement(CustomerEntitlement ce){
		return null;
	}
	public List<CourseGroupCustomerEntitlementItem> getItemsByEntitlement(CourseGroupCustomerEntitlement cgce){
		return courseGroupCustomerEntitlementItemRepository.findByCustomerEntitlementId(cgce.getId());
	}
	/*
	 * CourseCustomerEntitlement -> getEntitlementItems()
	 */
	public List<CourseCustomerEntitlementItem> getItemsByEntitlement(CourseCustomerEntitlement cce){
		return courseCustomerEntitlementItemRepository.findByCustomerEntitlementId(cce.getId());
	}

	//LMS-15124 - Created overload function getItemsByEntitlement() that only pull active courses if pass it to 'true'
	public List<CourseCustomerEntitlementItem> getItemsByEntitlement(CourseCustomerEntitlement cce, boolean removeRetiredCourse){
		return courseCustomerEntitlementItemRepository.findByCustomerEntitlementIdAndCourseRetiredFalse(cce.getId());
	}
	/*
	 * replacement of CourseCustomerEntitlement -> getCourses()
	 */
	public Set<Course> getCoursesByCourseCustomerEntitlement(CourseCustomerEntitlement cce){
		List<CourseCustomerEntitlementItem> items = this.getItemsByEntitlement(cce);
		Set<Course> uniqueCourses = new HashSet<Course>(items.size());
		for(CourseCustomerEntitlementItem item:items){
			uniqueCourses.add(item.getCourse());
		}
		return uniqueCourses;
	}
	/*
	 * replacement of CourseCustomerEntitlementItem adddCustomerEntitlementItem(Course course,CourseGroup courseGroup)
	 */
	public CourseCustomerEntitlementItem addEntitlementItem(CourseCustomerEntitlement ce, Course course, CourseGroup courseGroup){
		CourseCustomerEntitlementItem item = new CourseCustomerEntitlementItem();
		item.setCustomerEntitlement(ce);
		item.setCourse(course);
		item.setCourseGroup(courseGroup);
		return courseCustomerEntitlementItemRepository.save(item);
	}

	@Override
	public List<CourseCustomerEntitlementItem> addCourseEntitlementItems(List<CourseCustomerEntitlementItem> courseCustomerEntitlementItem){
		return (List<CourseCustomerEntitlementItem>) courseCustomerEntitlementItemRepository.save(courseCustomerEntitlementItem);
	}

	public List<CourseGroupCustomerEntitlementItem> addCourseGroupEntitlementItem(List<CourseGroupCustomerEntitlementItem> item){
		//Iterable<CourseGroupCustomerEntitlementItem> items=item;
		return (List<CourseGroupCustomerEntitlementItem>) courseGroupCustomerEntitlementItemRepository.bulkSave(item);
	}


	public List<CourseGroupCustomerEntitlementItem> addEntitlementItems(CourseGroupCustomerEntitlement cgce, List<CourseGroup> courseGroups){
		List<CourseGroupCustomerEntitlementItem> items = new ArrayList<CourseGroupCustomerEntitlementItem>();
		for(CourseGroup cg:courseGroups){
			CourseGroupCustomerEntitlementItem item = new CourseGroupCustomerEntitlementItem();
			item.setCourseGroup(cg);
			item.setCourseGroupCustomerEntitlement(cgce);
			item = courseGroupCustomerEntitlementItemRepository.saveCGCEI(item);
			items.add(item);
		}
		return items ;
	}

	/*
     * reference # CustomerEntitlement -> public Set<Course> getUniqueCourses()
     */
	public Set<Course> getUniqueCourses(CustomerEntitlement ce){
		Set<Course> courses = new HashSet<Course>();
		if(ce instanceof CourseCustomerEntitlement){
			List<CourseCustomerEntitlementItem> items = this.getItemsByEntitlement((CourseCustomerEntitlement)ce);
			for(CourseCustomerEntitlementItem item: items){
				courses.add(item.getCourse());
			}
		}else if (ce instanceof CourseGroupCustomerEntitlement){
			List<CourseGroupCustomerEntitlementItem> items = courseGroupCustomerEntitlementItemRepository.findByCustomerEntitlementId(ce.getId());
			Stack<CourseGroup> cgStack = new Stack<CourseGroup>();
			for(CourseGroupCustomerEntitlementItem item: items){
				if(item.getCourseGroup()!=null)
					cgStack.add(item.getCourseGroup());
			}
			List<Course> cgCourses = null;
			while(!cgStack.isEmpty()){
				CourseGroup cg = cgStack.pop();
				cgCourses = cg.getCourses();
				for(Course course:cgCourses){
					if(course.getCourseStatus().equalsIgnoreCase(Course.PUBLISHED)){
						courses.add(course);
					}
				}
				cgStack.addAll(cg.getChildrenCourseGroups());
			}
		}
		return courses;
	}
	public List<CourseCustomerEntitlementItem> findEntitlementItemsByCourse(CourseCustomerEntitlement cce, Long courseId){
		return courseCustomerEntitlementItemRepository.findByCustomerEntitlementIdAndCourseId(cce.getId(), courseId);
	}

	/*
     * reference # CourseCustomerEntitlement -> findCourseCustomerEntitlementItem
     */
	public CourseCustomerEntitlementItem findEntitlementItem(CourseCustomerEntitlement cce, Long courseGroupId,Long courseId){
		return courseCustomerEntitlementItemRepository.findByCustomerEntitlementIdAndCourseGroupIdAndCourseId(cce.getId(), courseGroupId, courseId);
	}

	public EnrollmentService getEnrollmentService() {
		return enrollmentService;
	}

	public void setEnrollmentService(EnrollmentService enrollmentService) {
		this.enrollmentService = enrollmentService;
	}

	/**
	 * [8/27/2010] LMS-6751 :: Use of aggregate function to get the Enrollment count By Customer Entitlement ID instead of loading all objects
	 */
	@Override
	public Long getEnrollmentCountByCustomerEntitlementId(Long customerEntitlementId) {

		return learnerEnrollmentRepository.countByCustomerEntitlementId(customerEntitlementId);
	}

	/**
	 * //[1/18/2011] LMS-8661 :: Search Courses and Course Groups By Reseller based on Search Parameters
	 */
	public List<TreeNode> getEntitlementCourseGroupTreeNode(Object object, String title, String entityId, String keywords, String contractType, Map context, Customer customer) {

		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();

		if (customer != null && customer.getDistributor() != null) {

			HashMap<CourseGroup, List<Course>> coursesList = null;//findCoursesAndCourseGroupsByDistributor(customer.getDistributor(), title, entityId, keywords, contractType);
			if(contractType.equals("Course")) {
				coursesList = findAvailableCourses(customer.getDistributor(), title, entityId, keywords);
			}
			else{
				coursesList = findAvailableCourseGroups(customer.getDistributor(), title, entityId, keywords);
			}


			if(contractType.equals("Course")) {
				List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
				TreeNode courseGroupCourseTreeNode = null;
				for (CourseGroup courseGroup : coursesList.keySet()) {
					if(coursesList.get(courseGroup)!=null) { //i.e. its not a blank search

						List<Course> courseList = coursesList.get(courseGroup);
						for (Course course : courseList) {
							if(courseGroup==null){
								courseGroup = new CourseGroup();
								java.util.Date date = new java.util.Date();
								courseGroup.setId(new Long((date.getHours()+ date.getMinutes() + date.getSeconds())*100));
								courseGroup.setName("Miscellaneous");
							}

							boolean courseAdded = false;
							for (TreeNode rootTreeNode : rootNodesReferences) {
								List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
								if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, courseGroup, course, childCourseGroups)) {
									courseAdded = true;
									break;
								}

								CourseGroup currentCourseGroup = courseGroup;
								while (!courseAdded && currentCourseGroup.getParentCourseGroup() != null) {
									CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
									childCourseGroups.add(currentCourseGroup);
									if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, parentCourseGroup, course, childCourseGroups)) {
										courseAdded = true;
										break;
									}
									currentCourseGroup = parentCourseGroup;
								}

								if (courseAdded)
									break;
							}

							if (!courseAdded) {
								courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, course);
								rootNodesReferences.add(courseGroupCourseTreeNode);
							}
						}

						context.put("callMacroForChildren", "false");
					}
					else {//i.e. its a blank search

						// [1/27/2011] LMS-7183 :: Retired Course Functionality II
						CourseSort courseSort = new CourseSort();
						List<Course> cgCourses = courseGroup.getActiveCourses();
						if (cgCourses != null && cgCourses.size() > 0) {
							Collections.sort(cgCourses, courseSort);
							courseGroup.setCourses(cgCourses);
						}

						boolean courseGroupAdded = false;
						for (TreeNode rootTreeNode : rootNodesReferences) {
							List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
							if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, courseGroup, childCourseGroups)) {
								courseGroupAdded = true;
								break;
							}

							CourseGroup currentCourseGroup = courseGroup;
							while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
								CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
								childCourseGroups.add(currentCourseGroup);
								if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
									courseGroupAdded = true;
									break;
								}
								currentCourseGroup = parentCourseGroup;
							}

							if (courseGroupAdded)
								break;
						}
						/*if (!courseGroupAdded) {
							//if(isCourseGroupBelongToDistributorEntitlement(courseGroup,allCourseGroups)){
							//courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourses(courseGroup, courseGroup.getCourses());
							//}
							//else{
								//courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup,null);
							//}

							for(Course c : courseGroup.getActiveCourses()){


								courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup,c);
								rootNodesReferences.add(courseGroupCourseTreeNode);
							}
						}*/
						if (!courseGroupAdded) {
							courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, null);
							rootNodesReferences.add(courseGroupCourseTreeNode);
						}
						context.put("callMacroForChildren", "false");
					}
				}
				for (TreeNode rootTreeNode : rootNodesReferences) {
					treeNodesList.addAll(rootTreeNode.bfs());
				}
			}
			else{//i.e. Contract type is CourseGroups

				List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
				TreeNode courseGroupCourseTreeNode = null;
				List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
				courseGroups.addAll(coursesList.keySet());
				for (CourseGroup courseGroup : courseGroups) {
					CourseSort courseSort = new CourseSort();
					List<Course> cgCourses = courseGroup.getActiveCourses(); // [1/27/2011] LMS-7183 :: Retired Course Functionality II

					if (cgCourses != null && cgCourses.size() > 0) {
						Collections.sort(cgCourses, courseSort);
						courseGroup.setCourses(cgCourses);
					}

					boolean courseGroupAdded = false;
					for (TreeNode rootTreeNode : rootNodesReferences) {
						List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
						if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, courseGroup, childCourseGroups)) {
							courseGroupAdded = true;
							break;
						}

						CourseGroup currentCourseGroup = courseGroup;
						while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
							CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
							childCourseGroups.add(currentCourseGroup);
							if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
								courseGroupAdded = true;
								break;
							}
							currentCourseGroup = parentCourseGroup;
						}

						if (courseGroupAdded)
							break;
					}
					if (!courseGroupAdded) {
						courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, null, courseAndCourseGroupService);
						rootNodesReferences.add(courseGroupCourseTreeNode);
					}
				}

				for (TreeNode rootTreeNode : rootNodesReferences) {
					treeNodesList.addAll(rootTreeNode.bfs());
				}

				context.put("callMacroForChildren", "true");
			}
		}

		return treeNodesList;

	}

	public List<TreeNode> getEntitlementItemsTreeStack(CustomerEntitlement cE,Map context ) {
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		List<TreeNode> treeNodesListToReturn = new ArrayList<TreeNode>();
		CourseGroup courseGroup = null;
		CourseGroup currentCG=null;
		if(cE instanceof CourseGroupCustomerEntitlement){
			try{
				List<CourseGroupCustomerEntitlementItem> items = this.getItemsByEntitlement((CourseGroupCustomerEntitlement)cE);
				for (CourseGroupCustomerEntitlementItem item : items) {
					CourseSort courseSort = new CourseSort();
					currentCG=item.getCourseGroup();
					List<Course> cgCourses = currentCG.getActiveCourses(); // [07/22/2010] LMS-6388:: Hide retired courses from all queries and views
					if (cgCourses != null && cgCourses.size() > 0) {
						Collections.sort(cgCourses, courseSort);
						item.getCourseGroup().setCourses(cgCourses);
					}

					TreeNode node=new TreeNode();
					node.setValue(currentCG);
					node.setEnabled(true);
					treeNodesList.add(node);
				}

				Stack stack=new Stack();
				stack.addAll(treeNodesList);
				boolean isNodeRemoved=false;
				while(!stack.isEmpty()){
					isNodeRemoved=false;
					TreeNode treeNode=new TreeNode();
					treeNode=(TreeNode)stack.pop();
					treeNode.setEnabled(true);
					courseGroup=(CourseGroup)treeNode.getValue();

					if(courseGroup.getParentCourseGroup()!=null){
						TreeNode parentCGNode=checkAndGetIfAlreadyInMainTree(treeNodesList,courseGroup.getParentCourseGroup());
						if(parentCGNode!=null ){
							parentCGNode.addChild(treeNode);
							treeNodesList.remove(treeNode);
						}else{
							parentCGNode=new TreeNode();
							parentCGNode.setEnabled(true);
							parentCGNode.setValue(courseGroup.getParentCourseGroup());
							parentCGNode.addChild(treeNode);
							treeNodesList.remove(treeNode);
							treeNodesList.add(parentCGNode);
						}
						isNodeRemoved=true;
					}

					if(!isNodeRemoved){
						if(treeNode.getChildren()!=null && treeNode.getChildren().size()>0){
							stack.addAll(treeNode.getChildren());
						}
					}
				}

			}
			catch(Exception ex){
				System.out.println("EXCEPTION: " + ex);
			}

		}
		else{ //i.e. its Course Type
			try{
				List<CourseCustomerEntitlementItem> items = this.getItemsByEntitlement((CourseCustomerEntitlement)cE);
				Course currentCourse=null;
				for (CourseCustomerEntitlementItem item : items) {
					currentCG=item.getCourseGroup();
					currentCourse=item.getCourse();
					if(currentCG==null){
						currentCG = new CourseGroup();
						currentCG.setId(new Long(-100));
						currentCG.setName("Miscellaneous");

					}

					TreeNode cgNode=checkAndGetIfAlreadyInMainTree(treeNodesList,currentCG);
					if(cgNode==null){
						cgNode=new TreeNode();
						cgNode.setValue(currentCG);
						cgNode.setEnabled(true);
						treeNodesList.add(cgNode);
					}

					if(!isCoursePresentInTree(treeNodesList, currentCourse)){
						TreeNode courseNode = new TreeNode();
						courseNode.setValue(currentCourse);
						courseNode.setEnabled(true);
						cgNode.addChild(courseNode);
					}
				}

				Stack stack=new Stack();
				stack.addAll(treeNodesList);
				while(!stack.isEmpty()){
					TreeNode treeNode=new TreeNode();
					treeNode=(TreeNode)stack.pop();
					treeNode.setEnabled(true);
					if(treeNode.getValue() instanceof CourseGroup){
						courseGroup=(CourseGroup)treeNode.getValue();

						if(courseGroup.getParentCourseGroup()!=null){

							TreeNode parentCGNode=checkAndGetIfAlreadyInMainTree(treeNodesList,courseGroup.getParentCourseGroup());
							if(parentCGNode!=null ){
								parentCGNode.addChild(treeNode);
								treeNodesList.remove(treeNode);
							}
							else{
								parentCGNode=new TreeNode();
								parentCGNode.setEnabled(true);
								parentCGNode.setValue(courseGroup.getParentCourseGroup());
								parentCGNode.addChild(treeNode);

								treeNodesList.remove(treeNode);
								treeNodesList.add(parentCGNode);
							}

						}
					}
					if(treeNode.getChildren()!=null && treeNode.getChildren().size()>0){
						stack.addAll(treeNode.getChildren());
					}
				}
			}
			catch(Exception ex){
				System.out.println("EXCEPTION: " + ex);
			}
		}

		for (TreeNode rootTreeNode : treeNodesList) {
			treeNodesListToReturn.addAll(rootTreeNode.bfs());
		}
		return treeNodesListToReturn;
	}

	private TreeNode checkAndGetIfAlreadyInMainTree(List<TreeNode> treeNodesList, CourseGroup courseGroup) {
		Stack stack=new Stack();
		stack.addAll(treeNodesList);
		TreeNode nodeToReturn=null;
		while(!stack.isEmpty()){
			TreeNode node=(TreeNode) stack.pop();
			if(node.getValue() instanceof CourseGroup){
				if(courseGroup.getId().equals(((CourseGroup)node.getValue()).getId())){
					nodeToReturn=node;
					break;
				}
			}
			if(node.getChildren()!=null && node.getChildren().size()>0){
				stack.addAll(node.getChildren());
			}
		}
		return nodeToReturn;
	}

	public List<TreeNode> getTreeForContract(CustomerEntitlement entitlement, Set<CourseGroup> contractCourseGroups) {
		return getTreeForContract(entitlement, contractCourseGroups, "", "");
	}

	public List<TreeNode> getTreeForContract(CustomerEntitlement entitlement, Set<CourseGroup> contractCourseGroups, String searchBy, String keyword) {
		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		if(entitlement instanceof CourseGroupCustomerEntitlement){
			List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
			TreeNode courseGroupCourseTreeNode = null;
			List<CourseGroupCustomerEntitlementItem> items = this.getItemsByEntitlement((CourseGroupCustomerEntitlement)entitlement);
			if (contractCourseGroups == null) {
				contractCourseGroups = new HashSet<CourseGroup>();
			}

			for (CourseGroupCustomerEntitlementItem item : items) {
				boolean courseGroupAdded = false;
				CourseSort courseSort = new CourseSort();
				CourseGroup contractCourseGroup = item.getCourseGroup();
				contractCourseGroups.add(contractCourseGroup);
				if (contractCourseGroup.getChildrenCourseGroups() != null
						&& !contractCourseGroup.getChildrenCourseGroups().isEmpty()) {
					contractCourseGroups.addAll(contractCourseGroup.getChildrenCourseGroups());
				}
				List<Course> cgCourses = contractCourseGroup.getActiveCourses(); // [07/22/2010] LMS-6388:: Hide retired courses from all queries and views
				if (cgCourses != null && cgCourses.size() > 0) {
					Collections.sort(cgCourses, courseSort);
					contractCourseGroup.setCourses(cgCourses);
				}
				for (TreeNode rootTreeNode : rootNodesReferences) {
					List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
					if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, contractCourseGroup, childCourseGroups)) {
						courseGroupAdded = true;
						break;
					}

					CourseGroup currentCourseGroup = contractCourseGroup;
					while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
						CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
						childCourseGroups.add(currentCourseGroup);
						if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
							courseGroupAdded = true;
							break;
						}
						currentCourseGroup = parentCourseGroup;
					}

					if (courseGroupAdded)
						break;
				}
				if (!courseGroupAdded) {
					courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(contractCourseGroup, null, courseAndCourseGroupService);
					rootNodesReferences.add(courseGroupCourseTreeNode);
				}
			}

			for (TreeNode rootTreeNode : rootNodesReferences) {
				treeNodesList.addAll(rootTreeNode.bfs());
			}
		}
		else if(entitlement instanceof SubscriptionCustomerEntitlement){
			SubscriptionCustomerEntitlement courseCustomerEntitlement = (SubscriptionCustomerEntitlement)entitlement;
			List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
			TreeNode courseGroupCourseTreeNode = null;
			//Created overload function getItemsByEntitlement() that only pull active courses if pass it to 'true'
			//List<CourseCustomerEntitlementItem> items = this.getItemsByEntitlement(courseCustomerEntitlement, true);
			List<SubscriptionCourse> items = subscriptionCourseRepository.
					findBySubscriptionCustomerEntitlementId(courseCustomerEntitlement.getId());
			for(SubscriptionCourse subscriptionCourseItem : items) {
				Course course = subscriptionCourseItem.getCourse();
				// No need to check isActive because created overload function getItemsByEntitlement() that only pull active courses
				//if (course.isActive()) {  // [1/27/2011] LMS-7183 :: Retired Course Functionality II
				CourseGroup courseGroup = subscriptionCourseItem.getCourseGroup();
				if(courseGroup==null){
					courseGroup = new CourseGroup();
					java.util.Date date = new java.util.Date();
					courseGroup.setId(new Long((date.getHours()+ date.getMinutes() + date.getSeconds())*100));
					courseGroup.setName("Miscellaneous");
				}

				boolean courseAdded = false;
				for (TreeNode rootTreeNode : rootNodesReferences) {
					List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
					if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, courseGroup, course, childCourseGroups)) {
						courseAdded = true;
						break;
					}

					CourseGroup currentCourseGroup = courseGroup;
					while (!courseAdded && currentCourseGroup.getParentCourseGroup() != null) {
						CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
						childCourseGroups.add(currentCourseGroup);
						if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, parentCourseGroup, course, childCourseGroups)) {
							courseAdded = true;
							break;
						}
						currentCourseGroup = parentCourseGroup;
					}

					if (courseAdded)
						break;
				}
				if (!courseAdded) {
					courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, course);
					rootNodesReferences.add(courseGroupCourseTreeNode);
				}
			}

			for (TreeNode rootTreeNode : rootNodesReferences) {
				treeNodesList.addAll(rootTreeNode.bfs());
			}

		}
		else {
			CourseCustomerEntitlement courseCustomerEntitlement = (CourseCustomerEntitlement)entitlement;
			List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
			TreeNode courseGroupCourseTreeNode = null;
			//Created overload function getItemsByEntitlement() that only pull active courses if pass it to 'true'
			List<CourseCustomerEntitlementItem> items = this.getItemsByEntitlement(courseCustomerEntitlement, true);
			for(CourseCustomerEntitlementItem courseCustomerEntitlementItem : items) {
				Course course = courseCustomerEntitlementItem.getCourse();
				// No need to check isActive because created overload function getItemsByEntitlement() that only pull active courses
				//if (course.isActive()) {  // [1/27/2011] LMS-7183 :: Retired Course Functionality II
				CourseGroup courseGroup = courseCustomerEntitlementItem.getCourseGroup();
				if(courseGroup==null){
					courseGroup = new CourseGroup();
					java.util.Date date = new java.util.Date();
					courseGroup.setId(new Long((date.getHours()+ date.getMinutes() + date.getSeconds())*100));
					courseGroup.setName("Miscellaneous");
				}

				boolean courseAdded = false;
				for (TreeNode rootTreeNode : rootNodesReferences) {
					List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
					if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, courseGroup, course, childCourseGroups)) {
						courseAdded = true;
						break;
					}

					CourseGroup currentCourseGroup = courseGroup;
					while (!courseAdded && currentCourseGroup.getParentCourseGroup() != null) {
						CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
						childCourseGroups.add(currentCourseGroup);
						if (ArrangeCourseGroupTree.traverseTreeToAddCourse(rootTreeNode, parentCourseGroup, course, childCourseGroups)) {
							courseAdded = true;
							break;
						}
						currentCourseGroup = parentCourseGroup;
					}

					if (courseAdded)
						break;
				}
				if (!courseAdded) {
					courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, course);
					rootNodesReferences.add(courseGroupCourseTreeNode);
				}
			}

			for (TreeNode rootTreeNode : rootNodesReferences) {
				treeNodesList.addAll(rootTreeNode.bfs());
			}
		}

		return treeNodesList;
	}

	private boolean isCoursePresentInTree(List<TreeNode> treeNodesList, Course course) {
		Stack stack=new Stack();
		stack.addAll(treeNodesList);
		boolean returnVal= false;
		while(!stack.isEmpty()){
			TreeNode node=(TreeNode) stack.pop();
			if(node.getValue() instanceof Course){
				if(course.getId().equals(((Course)node.getValue()).getId())){
					returnVal=true;
					break;
				}
			}
			if(node.getChildren()!=null && node.getChildren().size()>0)
				stack.addAll(node.getChildren());
		}
		return returnVal;
	}

	/**
	 * [8/31/2010] LMS-6931 :: Code refactored to get Course Group for Enrolled Courses
	 */
	@Override
	public List<CourseGroupCustomerEntitlementItem> findCourseGroupEntitlementItems(CourseGroupCustomerEntitlement cgce, Long courseId) {

		return courseGroupCustomerEntitlementItemRepository.findByCustomerEntitlementIdAndCourseGroupCoursesId(cgce.getId(), courseId);
	}

	public CustomerEntitlement assignCourseIntoSystemManagedContract(Course dbCourse, Customer customer) {

		CustomerEntitlement cE=null;
		CourseCustomerEntitlement systemCE=(CourseCustomerEntitlement) this.getSystemManagedContract(customer);
		if(systemCE==null){ //create a new customer contract
			Date maxEndDate = getMaxDistributorEntitlementEndDate(customer.getDistributor());
			systemCE=new CourseCustomerEntitlement();
			systemCE.setName(dbCourse.getCourseTitle());
			systemCE.setAllowUnlimitedEnrollments(true);
			systemCE.setAllowSelfEnrollment(true);
			//LMS-4445 TODO what coursegroup should be created for WebLinkCourse

			systemCE.setCustomer(customer);

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());
			String TodaysDate=formatter.format(new Date());
			Date startDate=null;
			try {
				startDate=formatter.parse(TodaysDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			systemCE.setStartDate(startDate);
			now.add(Calendar.YEAR , 500);
			/* check distributor contract date. for more LMS-13438 */
			if(startDate.after(maxEndDate)){
				systemCE.setEndDate(maxEndDate);
			}else{
				systemCE.setEndDate(now.getTime());
			}

			systemCE.setContractCreationDate(new Date());
			systemCE.setSystemManaged(true);//Very Imp. so that it is marked as system managed and doesnt show up in listing
			cE = this.saveCustomerEntitlement(systemCE,null);
			this.addEntitlementItem((CourseCustomerEntitlement)cE, dbCourse, null);
		}
		else{ //i.e. its an existing contract
			this.addEntitlementItem((CourseCustomerEntitlement)systemCE, dbCourse, null);
			cE=systemCE;
		}
		return cE;
	}

	/**
	 * // [12/27/2010] LMS-7021 :: Admin Mode > Swap Enrollment - Showing courses irrespective of contract and enrollments availability
	 * @param customerId
	 * @param courseName
	 * @param courseCode
	 * @param contractName
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public Map<Object, Object> getCoursesForEnrollment (Long customerId, String courseName, String courseCode, String contractName, int pageIndex, int pageSize) {

		int startIndex = (pageSize < 0) ? -1 : (pageIndex * pageSize) + 1;
		int endIndex = (pageSize < 0) ? -1 : (pageIndex * pageSize) + pageSize;
		int totalRecords = -1;

		List<EnrollmentCourseView> enrollmentCourseViews = enrollmentCourseViewRepository.getCoursesForEnrollment(customerId, courseName, courseCode, contractName, startIndex, endIndex);
		if (!org.springframework.util.CollectionUtils.isEmpty(enrollmentCourseViews)) {
			for (EnrollmentCourseView enrollmentCourseView : enrollmentCourseViews) {
				if (totalRecords < 0) {
					totalRecords = enrollmentCourseView.getTotalRecords();
				}
				String courseType = enrollmentCourseView.getCourseType();
				if (StringUtils.isNotBlank(courseType)) {
					if (courseType.equalsIgnoreCase(SynchronousCourse.COURSE_TYPE) || courseType.equalsIgnoreCase(WebinarCourse.COURSE_TYPE)) {
						long courseId = enrollmentCourseView.getCourseId();
						List<SynchronousClass> synchronousClasses = this.synchronousClassService.getSynchronousClassesForEnrollment(courseId, 1, false);
						if (!org.springframework.util.CollectionUtils.isEmpty(synchronousClasses)) {
							enrollmentCourseView.setSyncClasses(synchronousClasses);
						}
					}
				}
			}
		}

		Map<Object, Object> serachResult = new HashMap<Object, Object>();
		serachResult.put("totalRecords", totalRecords);
		serachResult.put("enrollmentList", enrollmentCourseViews);
		return serachResult;
	}

	// issue LMS 8256
	@Deprecated
	public EnrollmentCourseView getEnrollmentCourseViewByCourseId(long courseId){
		return null; //entitlementDAO.getEnrollmentCourseViewByCourseId(courseId);
	}

	public String getCoursePathToCourseGroup(int courseGroupId){
		String coursePath = null;
		try {
			//coursePath = entitlementDAO.getCoursePathToCourseGroup(courseGroupId);
			coursePath = this.courseGroupRepository.getCourseGroupPath(new Long(courseGroupId));
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
		}
		return coursePath;
	}



	public HashMap<CourseGroup, List<Course>> findAvailableCourseGroups(Distributor distributor,String courseName, String entityId, String keywords){

		List<Map<Object, Object>> results = courseGroupRepository.findByAvailableCourseGroups(distributor, courseName, entityId, keywords);//entitlementDAO.findAvailableCourseGroups(distributor, courseName, entityId, keywords);
		Course course=null;
		CourseGroup courseGroup = null;
		HashMap<Long,CourseGroup>cgIDs = new HashMap<Long, CourseGroup>();
		HashMap<CourseGroup,List<Course>> courseGroups=new HashMap<CourseGroup, List<Course>>();

		for(Map<Object, Object> record : results){

			courseGroup = getCourseGroupHierarchy(record);
			courseGroup.setCourses(courseGroup.getActiveCourses());

			if(cgIDs.containsKey(courseGroup.getId())){//i.e. this CG is already in the list
				courseGroup=cgIDs.get(courseGroup.getId());
				courseGroup.getCourses().add(course);
			} else{//i.e. 1st time adding this CG
				cgIDs.put(courseGroup.getId(), courseGroup);
			}
		}

		for(CourseGroup cG : cgIDs.values()){

			cG = courseAndCourseGroupService.getCourseGroupById(cG.getId());
			/** Commented By MairumSaud: This piece of code is of no uise and causing number of queries to be executing when search for CourseGroup is performed.
			 * Which is affecting the search performance
			 *
			 for (Iterator iterator = cG.getCourses().iterator(); iterator.hasNext();) {
			 Course cc = (Course) iterator.next();

			 if(cc.getId()== 56530 || cc.getId()==56531){
			 System.out.println(cc.getName()+"|"+cc.isRetired()+"|"+cc.isActive()+"|"+cc.getCourseStatus()+"|"+cc.getId());
			 }
			 }
			 */
			/**
			 * Modified By MariumSaud : LMS-21943 : getActiveCourses() is hitting performance and throwing 'OutOfMemory Exception'
			 * replaced cG.getActiveCourses from new method that will return all active courses against given Course Group Id.
			 *
			 * cG.setCourses(cG.getActiveCourses());
			 */

			List<Course> activeCourses = this.getActiveCourses(cG.getId());
			cG.setCourses(activeCourses);


			courseGroups.put(cG, cG.getActiveCourses());
			for(CourseGroup childCG : cG.getAllChildrenInHierarchy()){
				// childCG.setCourses(childCG.getActiveCourses()); Commented inn reference to the above mentioned reason
				childCG.setCourses(this.getActiveCourses(childCG.getId()));
				courseGroups.put(childCG, childCG.getActiveCourses());
			}
		}

		log.debug("-- final courseList.size()=>"+courseGroups.size());
		return courseGroups;

	}

	/**
	 * @author 		marium.saud
	 * @param		id	CourseGroup Id
	 * @return		List of Courses
	 */
	public List<Course> getActiveCourses(Long id) {
		return courseAndCourseGroupService.getActiveCourses(id);
	}

	public HashMap<CourseGroup, List<Course>> findAvailableCourseGroups(Distributor distributor, List<Long> courseGroudIdList){

		List<Map<Object, Object>> results = courseGroupRepository.findByAvailableCourseGroups(distributor,courseGroudIdList);
		Course course=null;
		CourseGroup courseGroup = null;
		HashMap<Long,CourseGroup>cgIDs = new HashMap<Long, CourseGroup>();
		HashMap<CourseGroup,List<Course>> courseGroups=new HashMap<CourseGroup, List<Course>>();

		for(Map<Object, Object> record : results){

			courseGroup = getCourseGroupHierarchy(record);
			courseGroup.setCourses(courseGroup.getActiveCourses());

			if(cgIDs.containsKey(courseGroup.getId())){//i.e. this CG is already in the list
				courseGroup=cgIDs.get(courseGroup.getId());
				courseGroup.getCourses().add(course);
			} else{//i.e. 1st time adding this CG
				cgIDs.put(courseGroup.getId(), courseGroup);
			}
		}

		for(CourseGroup cG : cgIDs.values()){

			cG = courseAndCourseGroupService.getCourseGroupById(cG.getId());
			for (Iterator iterator = cG.getCourses().iterator(); iterator.hasNext();) {
				Course cc = (Course) iterator.next();

				if(cc.getId()== 56530 || cc.getId()==56531){
					System.out.println(cc.getName()+"|"+cc.isRetired()+"|"+cc.isActive()+"|"+cc.getCourseStatus()+"|"+cc.getId());
				}
			}
			cG.setCourses(cG.getActiveCourses());


			courseGroups.put(cG, cG.getActiveCourses());
			for(CourseGroup childCG : cG.getAllChildrenInHierarchy()){
				childCG.setCourses(childCG.getActiveCourses());
				courseGroups.put(childCG, childCG.getActiveCourses());
			}
		}

		log.debug("-- final courseList.size()=>"+courseGroups.size());
		return courseGroups;

	}


	public HashMap<CourseGroup, List<Course>> findAvailableCourses(Distributor distributor,String courseName, String entityId, String keywords){

		List<Map<Object, Object>> results = courseRepository.findAvailableCourses(distributor, courseName,entityId,keywords);

		Course course=null;
		CourseGroup courseGroup = null;
		HashMap<Long,CourseGroup>cgIDs = new HashMap<Long, CourseGroup>();
		HashMap<CourseGroup,List<Course>> courseGroups=new HashMap<CourseGroup, List<Course>>();

		for(Map<Object, Object> record : results){

			if(record.get("COURSE_ID")!=null && StringUtils.isNotBlank(record.get("COURSE_ID").toString())){
				course=new Course();
				course.setId(Long.parseLong(record.get("COURSE_ID").toString()));
				course.setName(record.get("COURSENAME").toString());
				course.setCourseStatus(Course.PUBLISHED);
				course.setCourseTitle(course.getName());
				course.setBussinesskey(record.get("COURSEBUSINESSKEY")==null?"":record.get("COURSEBUSINESSKEY").toString());

				courseGroup = getCourseGroupHierarchy(record);

				if(cgIDs.containsKey(courseGroup.getId())){//i.e. this CG is already in the list
					courseGroup=cgIDs.get(courseGroup.getId());
					courseGroup.getCourses().add(course);
				} else{//i.e. 1st time adding this CG
					courseGroup.getCourses().add(course);
					cgIDs.put(courseGroup.getId(), courseGroup);

				}
			}

		}

		for(CourseGroup cG : cgIDs.values()){
			courseGroups.put(cG, cG.getActiveCourses());
		}


		log.debug("-- final courseList.size()=>"+courseGroups.size());
		return courseGroups;


	}

	public HashMap<CourseGroup, List<Course>> findAvailableCourses(Distributor distributor, List<Long> courseIdList){

		List<Map<Object, Object>> results = courseRepository.findAvailableCourses(distributor.getId(), courseIdList);

		Course course=null;
		CourseGroup courseGroup = null;
		HashMap<Long,CourseGroup>cgIDs = new HashMap<Long, CourseGroup>();
		HashMap<CourseGroup,List<Course>> courseGroups=new HashMap<CourseGroup, List<Course>>();

		for(Map<Object, Object> record : results){

			if(record.get("COURSE_ID")!=null && StringUtils.isNotBlank(record.get("COURSE_ID").toString())){
				course=new Course();
				course.setId(Long.parseLong(record.get("COURSE_ID").toString()));
				course.setName(record.get("COURSENAME").toString());
				course.setCourseStatus(Course.PUBLISHED);
				course.setCourseTitle(course.getName());
				course.setBussinesskey(record.get("COURSEBUSINESSKEY")==null?"":record.get("COURSEBUSINESSKEY").toString());

				courseGroup = getCourseGroupHierarchy(record);

				if(cgIDs.containsKey(courseGroup.getId())) {//i.e. this CG is already in the list
					courseGroup=cgIDs.get(courseGroup.getId());
					courseGroup.getCourses().add(course);
				} else{//i.e. 1st time adding this CG
					courseGroup.getCourses().add(course);
					cgIDs.put(courseGroup.getId(), courseGroup);

				}
			}

		}

		for(CourseGroup cG : cgIDs.values()){
			courseGroups.put(cG, cG.getActiveCourses());
		}


		log.debug("-- final courseList.size()=>"+courseGroups.size());
		return courseGroups;


	}

	private CourseGroup getCourseGroupHierarchy(Map<Object, Object> record){

		CourseGroup currentCG = new CourseGroup();
		CourseGroup parentCourseGroup=null;
		CourseGroup relationalCG=null;

		currentCG.setId(Long.parseLong(record.get("COURSEGROUP_ID").toString()));
		currentCG.setName(record.get("COURSEGROUPNAME").toString());

		Stack<CourseGroup> stack = new Stack<CourseGroup>();
		stack.push(currentCG);

		for(int i=1;i<=10;i++){
			if(record.get("PARENTCOURSEGROUP_ID"+i)!=null){
				parentCourseGroup=new CourseGroup();
				parentCourseGroup.setId(Long.parseLong(record.get("PARENTCOURSEGROUP_ID"+i).toString()));
				parentCourseGroup.setName(record.get("PARENTCOURSEGROUPNAME"+i).toString());

				relationalCG=stack.pop();

				relationalCG.setParentCourseGroup(parentCourseGroup);
				parentCourseGroup.getChildrenCourseGroups().add(relationalCG);

				stack.push(parentCourseGroup);

			}
		}
		stack.removeAllElements();

		return currentCG;
	}

	/**
	 * ${@inheritDoc }
	 */
	public void removeDistributorEntitlement(DistributorEntitlement distributorEntitlement) {
		if( null == distributorEntitlement) {
			throw new IllegalArgumentException("Please provide distributor entitlement.");
		}

		//entitlementDAO.deleteDistributorEntitlement(distributorEntitlement);
		distributorEntitlementRepository.delete(distributorEntitlement);
	}

	/**
	 * ${@inheritDoc }
	 */
	@Override
	public List<TreeNode> searchCourseGroups(String title,
											 String entityId, String keywords) {

		Map<CourseGroup, List<Course>> coursesList = findCourseGroups(title, entityId, keywords);

		List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
		TreeNode courseGroupCourseTreeNode = null;
		List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
		courseGroups.addAll(coursesList.keySet());

		for (CourseGroup courseGroup : courseGroups) {
			boolean courseGroupAdded = false;
			for (TreeNode rootTreeNode : rootNodesReferences) {
				List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
				if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, courseGroup, childCourseGroups)) {
					courseGroupAdded = true;
					break;
				}

				CourseGroup currentCourseGroup = courseGroup;
				while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
					CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
					childCourseGroups.add(currentCourseGroup);
					if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
						courseGroupAdded = true;
						break;
					}
					currentCourseGroup = parentCourseGroup;
				}

				if (courseGroupAdded)
					break;
			}
			if (!courseGroupAdded) {
				courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, null);
				rootNodesReferences.add(courseGroupCourseTreeNode);
			}
		}

		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		for (TreeNode rootTreeNode : rootNodesReferences) {
			treeNodesList.addAll(rootTreeNode.bfs());
		}


		return treeNodesList;
	}

	@Override
	public List<TreeNode> searchCourseGroupsByDistributor(Distributor distributor, Set<CourseGroup> courseGroupSet, String title,
														  String entityId, String keywords, String contractType) {

		Map<CourseGroup, List<Course>> coursesList = findCoursesAndCourseGroupsByDistributor(distributor, title, entityId, keywords, contractType);

		List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
		TreeNode courseGroupCourseTreeNode = null;
		Set<CourseGroup> courseGroups = courseGroupSet;//new ArrayList<CourseGroup>();
		courseGroups.addAll(coursesList.keySet());

		for (CourseGroup courseGroup : courseGroups) {
			boolean courseGroupAdded = false;
			for (TreeNode rootTreeNode : rootNodesReferences) {
				List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
				if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, courseGroup, childCourseGroups)) {
					courseGroupAdded = true;
					break;
				}

				CourseGroup currentCourseGroup = courseGroup;
				while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
					CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
					childCourseGroups.add(currentCourseGroup);
					if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
						courseGroupAdded = true;
						break;
					}
					currentCourseGroup = parentCourseGroup;
				}

				if (courseGroupAdded)
					break;
			}
			if (!courseGroupAdded) {
				courseGroupCourseTreeNode = ArrangeCourseGroupTree.getCourseGroupTreeNodeForCourse(courseGroup, null);
				rootNodesReferences.add(courseGroupCourseTreeNode);
			}
		}

		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		for (TreeNode rootTreeNode : rootNodesReferences) {
			treeNodesList.addAll(rootTreeNode.bfs());
		}


		return treeNodesList;
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public  Map<CourseGroup, List<Course>> findCourseGroups(String courseGroupName, String entityId, String keywords){
		List<Map<Object, Object>> results = courseGroupRepository.findCourseGroups(courseGroupName, entityId, keywords);

		//Course course=null;
		CourseGroup courseGroup = null;
		HashMap<Long,CourseGroup>cgIDs = new HashMap<Long, CourseGroup>();
		HashMap<CourseGroup,List<Course>> courseGroups=new HashMap<CourseGroup, List<Course>>();

		for(Map<Object, Object> record : results){
			courseGroup = getCourseGroupHierarchy(record);
			if(!cgIDs.containsKey(courseGroup.getId())){//i.e. this CG is already in the list
				cgIDs.put(courseGroup.getId(), courseGroup);

			}
		}
		for(CourseGroup cG : cgIDs.values()){
			cG = courseAndCourseGroupService.getCourseGroupById(cG.getId());
			courseGroups.put(cG, new ArrayList<Course>());
			for(CourseGroup childCG : cG.getAllChildrenInHierarchy()){
				courseGroups.put(childCG,new ArrayList<Course>());
			}
		}
		return courseGroups;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TreeNode> getCourseGroupTreeForDistributorEntitlement(DistributorEntitlement distributorEntitlement,
																	  Set<CourseGroup> contractCourseGroups) {

		if( null == distributorEntitlement )
			throw new IllegalArgumentException("Please provide distributor entitlement.");

		List<TreeNode> treeNodesList = new ArrayList<TreeNode>();
		List<TreeNode> rootNodesReferences = new ArrayList<TreeNode>();
		TreeNode courseGroupCourseTreeNode = null;

		if (contractCourseGroups == null) {
			contractCourseGroups = new HashSet<CourseGroup>();
		}

		List<CourseGroup> courseGroups = distributorEntitlement.getCourseGroups();
		for (CourseGroup contractCourseGroup: courseGroups) {
			boolean courseGroupAdded = false;
			contractCourseGroups.add(contractCourseGroup);

			if (contractCourseGroup.getChildrenCourseGroups() != null
					&& !contractCourseGroup.getChildrenCourseGroups().isEmpty()) {
				contractCourseGroups.addAll(contractCourseGroup.getChildrenCourseGroups());
			}

			for (TreeNode rootTreeNode : rootNodesReferences) {
				List<CourseGroup> childCourseGroups = new ArrayList<CourseGroup>();
				if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, contractCourseGroup, childCourseGroups)) {
					courseGroupAdded = true;
					break;
				}

				CourseGroup currentCourseGroup = contractCourseGroup;
				while (!courseGroupAdded && currentCourseGroup.getParentCourseGroup() != null) {
					CourseGroup parentCourseGroup = currentCourseGroup.getParentCourseGroup();
					childCourseGroups.add(currentCourseGroup);
					if (ArrangeCourseGroupTree.traverseTreeToAddCourseGroup(rootTreeNode, parentCourseGroup, childCourseGroups)) {
						courseGroupAdded = true;
						break;
					}
					currentCourseGroup = parentCourseGroup;
				}

				if (courseGroupAdded)
					break;
			}
			if (!courseGroupAdded) {
				courseGroupCourseTreeNode = ArrangeCourseGroupTree.
						getCourseGroupTreeNodeForCourse(contractCourseGroup, null);

				rootNodesReferences.add(courseGroupCourseTreeNode);
			}
		}

		for (TreeNode rootTreeNode : rootNodesReferences) {
			treeNodesList.addAll(rootTreeNode.bfs());
		}

		return treeNodesList;
	}

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	public SecurityAndRolesService getSecurityAndRolesService() {
		return securityAndRolesService;
	}

	public void setSecurityAndRolesService(
			SecurityAndRolesService securityAndRolesService) {
		this.securityAndRolesService = securityAndRolesService;
	}


	public Date getMaxDistributorEntitlementEndDate(Distributor distributor){
		List<DistributorEntitlement> listDistributorEntitlements =this.getAllDistributorEntitlements(distributor);
		Date maxExpirationDate=new Date();
		for(DistributorEntitlement de:listDistributorEntitlements){
			Date expirationDate= de.getEndDate();
			if(expirationDate==null){
				Calendar cal = Calendar.getInstance();
				cal.setTime(de.getStartDate());
				cal.add(Calendar.DATE, de.getDefaultTermOfServiceInDays());
				expirationDate = cal.getTime();
			}
			if(expirationDate.after(maxExpirationDate)){
				maxExpirationDate= expirationDate;
			}
		}
		return maxExpirationDate;
	}

	// @MariumSaud : LMS-21702 -- The method will return all customerEntitlementIds bound with selected organizationGroups
	@Override
	public List<Long> getCustomerEntitlementForOrgGroupEntitlementsByOrgGrpIds(List<Long> orgGrpIds) {
		Long[] orgGrpIdArray = new Long[orgGrpIds.size()];
		orgGrpIdArray = orgGrpIds.toArray(orgGrpIdArray);

		List<OrgGroupEntitlement> organizationEntitlement = new ArrayList<OrgGroupEntitlement>();
		organizationEntitlement = orgGroupEntitlementRepository.findDistinctByOrganizationalGroupIdInAndMaxNumberSeatsGreaterThanEqual(orgGrpIdArray,0L);

		Set<Long> customerEntitlementIds = new LinkedHashSet<Long>();
		for(OrgGroupEntitlement orgGrpEntitlement : organizationEntitlement){
			customerEntitlementIds.add(orgGrpEntitlement.getCustomerEntitlement().getId());
		}

		if(!customerEntitlementIds.isEmpty()){
			return new ArrayList<Long>(customerEntitlementIds);
		}
		return null;
	}

	// @MariumSaud : LMS-21702 -- The method will return all customerEntitlementIds bound with organizationGroups of Learners
	@Override
	public List<Long> getCustomerEntitlementForOrgGroupEntitlementsByLearnerIds(Long learnerIds[]) {
		log.debug("looking for orgGroupEntitlements for Learners");

		List<OrganizationalGroup> orgGroups = orgGroupLearnerGroupService.getOrgGroupsByLearners(learnerIds);

		if ( orgGroups == null || orgGroups.isEmpty() ) {
			// return empty resut set.
			return new ArrayList<Long>();
		}
		Long[] orgGroupIdArray = new Long[orgGroups.size()];
		int count=0;
		for (OrganizationalGroup orgGroup:orgGroups){
			orgGroupIdArray[count]=orgGroup.getId();
			count++;

		}
		List<OrgGroupEntitlement> results = orgGroupEntitlementRepository.findDistinctByOrganizationalGroupIdInAndMaxNumberSeatsGreaterThanEqual(orgGroupIdArray,0L);

		Set<Long> customerEntitlementIds = new LinkedHashSet<Long>();
		for(OrgGroupEntitlement oge :results){
			customerEntitlementIds.add(oge.getCustomerEntitlement().getId());
		}
		if(!customerEntitlementIds.isEmpty()){
			return new ArrayList<Long>(customerEntitlementIds);
		}
		return null;
	}

	// @MariumSaud : LMS-21702 -- The method will return all customerEntitlementIds bound with organizationGroups of LearnerGroup
	@Override
	public List<Long> getCustomerEntitlementForOrgGroupEntitlementsByLearnerGroupIds(List<Long> learnerGrpIds) {
		Long[] learnerGrpIdsArray = new Long[learnerGrpIds.size()];
		learnerGrpIdsArray = learnerGrpIds.toArray(learnerGrpIdsArray);

		List<LearnerGroup> learnerGroups = orgGroupLearnerGroupService.getLearnerGroupsByLearnerGroupIDs(learnerGrpIdsArray);

		if ( learnerGroups == null || learnerGroups.isEmpty() ) {
			// return empty resut set.
			return new ArrayList<Long>();
		}

		Long[] orgGroupIdArray = new Long[learnerGroups.size()];
		int count=0;
		for (LearnerGroup lg : learnerGroups){
			orgGroupIdArray[count] = lg.getOrganizationalGroup().getId();
			count++;

		}

		List<OrgGroupEntitlement> organizationEntitlement = new ArrayList<OrgGroupEntitlement>();
		organizationEntitlement = orgGroupEntitlementRepository.findDistinctByOrganizationalGroupIdInAndMaxNumberSeatsGreaterThanEqual(orgGroupIdArray,0L);

		Set<Long> customerEntitlementIds = new LinkedHashSet<Long>();
		for(OrgGroupEntitlement orgGrpEntitlement : organizationEntitlement){
			customerEntitlementIds.add(orgGrpEntitlement.getCustomerEntitlement().getId());
		}
		if(!customerEntitlementIds.isEmpty()){
			return new ArrayList<Long>(customerEntitlementIds);
		}
		return null;
	}

	// @MariumSaud : LMS-22023 -- The method will return 'true' if manager Permission for 'Enforce Org. Group Enrollment Restriction' is Enabled for Customer else return 'false'
	@Override
	public boolean isEnforceOrgGroupEnrollmentRestrictionEnable(Customer customer){

		String enforceOrgGroupEnrollmentRestrictionFeatureCode = "";

		if(VU360Properties.getVU360Property(ENFORCE_ORG_GROUP_ENROLLMENT_RESTRICTION_FEATURE_CODE) != null) {
			enforceOrgGroupEnrollmentRestrictionFeatureCode = VU360Properties.getVU360Property(ENFORCE_ORG_GROUP_ENROLLMENT_RESTRICTION_FEATURE_CODE).toString();
		}

		if(!StringUtils.isEmpty(enforceOrgGroupEnrollmentRestrictionFeatureCode)) {

			DistributorLMSFeature distributorLMSFeature = securityAndRolesService.getDistributorLMSFeatureByFeatureCode(customer.getDistributor().getId(), enforceOrgGroupEnrollmentRestrictionFeatureCode);
			CustomerLMSFeature customerLMSFeatures = securityAndRolesService.getCustomerLMSFeatureByFeatureCode(customer.getId(), enforceOrgGroupEnrollmentRestrictionFeatureCode);

			if(distributorLMSFeature!=null && customerLMSFeatures != null) {
				boolean enforceOrgGroupEnrollmentRestrictionAtResellerLevel = distributorLMSFeature.getEnabled();
				boolean enforceOrgGroupEnrollmentRestrictionAtCustomerLevel = customerLMSFeatures.getEnabled();
				if(enforceOrgGroupEnrollmentRestrictionAtResellerLevel&&enforceOrgGroupEnrollmentRestrictionAtCustomerLevel){
					return true;
				}
			}
		}
		return false;
	}

	private void deleteCourseCustomerEntitlementItems(List<CourseCustomerEntitlementItem> entitlementItems){

		if(entitlementItems!=null){
			for(CourseCustomerEntitlementItem item:entitlementItems){
				courseCustomerEntitlementItemRepository.delete(item);
			}
		}

	}

	private void deleteCourseGroupCEI(List<CourseGroupCustomerEntitlementItem> entitlementItems){
		if(entitlementItems!=null){
			for(CourseGroupCustomerEntitlementItem item:entitlementItems){
				courseGroupCustomerEntitlementItemRepository.delete(item);
			}
		}
	}

}