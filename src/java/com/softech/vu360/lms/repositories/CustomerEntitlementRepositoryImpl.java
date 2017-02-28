package com.softech.vu360.lms.repositories;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import com.softech.vu360.lms.model.CourseCustomerEntitlement;
import com.softech.vu360.lms.model.CourseCustomerEntitlementItem;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlement;
import com.softech.vu360.lms.model.CourseGroupCustomerEntitlementItem;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.CustomerEntitlement;
import com.softech.vu360.lms.model.Learner;
import com.softech.vu360.lms.model.LearnerCourseStatistics;
import com.softech.vu360.lms.model.LearnerEnrollment;
import com.softech.vu360.lms.model.LearnerSCOStatistics;
import com.softech.vu360.lms.model.LearnerSelfStudyCourseActivity;
import com.softech.vu360.lms.model.OrgGroupEntitlement;
import com.softech.vu360.lms.model.TrainingPlanAssignment;
import com.softech.vu360.lms.model.VU360User;




/**
 * 
 * @author haider
 *
 */
public class CustomerEntitlementRepositoryImpl implements CustomerEntitlementRepositoryCustom{

	@Inject
	public CustomerEntitlementRepository customerEntitlementRespository;
	@Inject
	private OrgGroupEntitlementRepository orgGroupEntitlementRespository;
	@Inject
	private CourseCustomerEntitlementItemRepository courseCustomerEntitlementItemRespository;
	@Inject
	private CourseGroupCustomerEntitlementItemRepository courseGroupCustomerEntitlementItemRespository;
	@Inject
	private LearnerEnrollmentRepository learnerEnrollmentRespository;
	@Inject
	private TrainingPlanAssignmentRepository trainingPlanAssignmentRespository;
	@Inject
	private LearnerSCOStatisticsRepository learnerSCOStatisticsRespository;
	@Inject
	private LearnerSelfStudyCourseActivityRepository learnerSelfStudyCourseActivityRespository;
	@Inject
	private LearnerCourseStatisticsRepository learnerCourseStatisticsRespository;;
	@Inject
	private LearnerRepository learnerRespository;;
	@Inject
	public CustomerRepository customerRespository;

	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	public boolean isCustomerEntitlementExistByName(String customerEntitlementName) {
		
		return !customerEntitlementRespository.findByNameIgnoreCase(customerEntitlementName).isEmpty();
		
		
	}
	@Override
	public void deleteCustomerEntitlement(CustomerEntitlement objCustomerEntitlement) {
		
		List<OrgGroupEntitlement>  list = orgGroupEntitlementRespository.findByCustomerEntitlementId(objCustomerEntitlement.getId());
		if(list!=null && !list.isEmpty()){
			orgGroupEntitlementRespository.delete(list);
		}
		
    	// Delete Contract Items based on Contract Type
    	if(objCustomerEntitlement instanceof CourseCustomerEntitlement){
			CourseCustomerEntitlement cce= (CourseCustomerEntitlement)objCustomerEntitlement;
			List<CourseCustomerEntitlementItem> items = courseCustomerEntitlementItemRespository.findByCustomerEntitlement(cce);
			courseCustomerEntitlementItemRespository.delete(items);
		}
		else if (objCustomerEntitlement instanceof CourseGroupCustomerEntitlement) {
			CourseGroupCustomerEntitlement cgce = (CourseGroupCustomerEntitlement)objCustomerEntitlement;
			List<CourseGroupCustomerEntitlementItem> s = courseGroupCustomerEntitlementItemRespository.findByCustomerEntitlement(cgce);
			courseGroupCustomerEntitlementItemRespository.delete(s);
		}

    	// Delete Contract, finally	    	
    	customerEntitlementRespository.delete(objCustomerEntitlement);
		
	}
	
	
	@Override
	public List<CustomerEntitlement> findCustomerIdAndAllowSelfEnrollmentTrue(Learner learner) {

		Long cid = learner.getCustomer().getId();
		List<CustomerEntitlement> f  = customerEntitlementRespository.findByCustomerIdAndAllowSelfEnrollmentTrue(cid);
		return f;
		
	}
	@Override
	public List<CustomerEntitlement> getSimilarCustomerEntitlements(Long customerEntitlementId, Long totalEnrollments) {

		CustomerEntitlement ce = null;
		List<CustomerEntitlement> customerEntitlementList =null;
		List<CourseCustomerEntitlementItem> cceitemList = courseCustomerEntitlementItemRespository.findBycustomerEntitlementId(customerEntitlementId);
		Set<CourseCustomerEntitlementItem> courseItems = new HashSet<CourseCustomerEntitlementItem>(cceitemList);
		
		if(CollectionUtils.isNotEmpty(courseItems)){
			ce = courseItems.iterator().next().getCustomerEntitlement();
			Vector<Long> itemIds = new Vector<Long>();
			for (CourseCustomerEntitlementItem item : courseItems) {
				itemIds.add( item.getCourse().getId() );
			}
			customerEntitlementList = customerEntitlementRespository.findByCustomerEntitlement(ce.getCustomer().getId(), ce.getId(), new Long(0), itemIds );
			System.out.print(itemIds.size());
		}
		else{

			List<CourseGroupCustomerEntitlementItem> courseGroupItems = courseGroupCustomerEntitlementItemRespository.findByCustomerEntitlementId(customerEntitlementId);
			Set<CourseGroupCustomerEntitlementItem> courseGroupItems_ = new HashSet<CourseGroupCustomerEntitlementItem>(courseGroupItems);

			if(CollectionUtils.isNotEmpty(courseGroupItems_)){
				ce = courseGroupItems_.iterator().next().getCourseGroupCustomerEntitlement();
			}
			
			Vector<Long> itemIds = new Vector<Long>();
			for (CourseGroupCustomerEntitlementItem item : courseGroupItems) {
				itemIds.add( item.getCourseGroup().getId() );
			}
			customerEntitlementList = customerEntitlementRespository.findByCustomerEntitlement(ce.getCustomer().getId(), ce.getId(), new Long(0), itemIds );
			System.out.print(itemIds.size());

		}
		
		return customerEntitlementList;
	}
	
	
	@Override
	public void removeEntitlementsWithEnrollments(Customer customer, CustomerEntitlement objCustomerEntitlement) {

		List <LearnerEnrollment> leList = learnerEnrollmentRespository.findByCustomerEntitlementId(objCustomerEntitlement.getId());
		
		 @SuppressWarnings("unchecked")
			Collection<Long> ids = CollectionUtils.collect(leList, new Transformer() {
			      public Long transform(Object o) {
			          return ((LearnerEnrollment) o).getId();
			      }
			  });

		if(!leList.isEmpty()){
			List<TrainingPlanAssignment> d = trainingPlanAssignmentRespository.findByLearnerEnrollmentsIdIn(ids);
			 if(d!=null && !d.isEmpty()) {
				 trainingPlanAssignmentRespository.delete(d);
			 }

			 List<LearnerSCOStatistics> f = learnerSCOStatisticsRespository.findByLearnerEnrollmentIdIn(ids);
			 if (!f.isEmpty()) {
				 learnerSCOStatisticsRespository.delete(f);
			 }
			 
			 List<LearnerSelfStudyCourseActivity> x = learnerSelfStudyCourseActivityRespository.findByLearnerEnrollmentIdIn(ids);
			 if(x!=null && !x.isEmpty()) {
				 learnerSelfStudyCourseActivityRespository.delete(x);
			 }
			 
			 List<LearnerCourseStatistics> a = learnerCourseStatisticsRespository.findByLearnerEnrollmentIdIn(ids);
			 if(a!=null && !a.isEmpty()) {
				 learnerCourseStatisticsRespository.delete(a);
			 }

			 //Delete the Learner Enrollments associated with this contract
			 learnerEnrollmentRespository.delete(leList); 
		}
		
		List<OrgGroupEntitlement> lstOrgGroupEntitlement  = orgGroupEntitlementRespository.findByCustomerEntitlementId(objCustomerEntitlement.getId());
		if (! lstOrgGroupEntitlement.isEmpty() ) {
			orgGroupEntitlementRespository.delete(lstOrgGroupEntitlement);
		}
		
		if(objCustomerEntitlement instanceof CourseCustomerEntitlement){
			CourseCustomerEntitlement cce= (CourseCustomerEntitlement)objCustomerEntitlement;
			List<CourseCustomerEntitlementItem> l = courseCustomerEntitlementItemRespository.findByCustomerEntitlement(cce);
			courseCustomerEntitlementItemRespository.delete(l);
		}
		if (objCustomerEntitlement instanceof CourseGroupCustomerEntitlement) {
			CourseCustomerEntitlement cce= (CourseCustomerEntitlement)objCustomerEntitlement;
			List<CourseGroupCustomerEntitlementItem> l = courseGroupCustomerEntitlementItemRespository.findByCustomerEntitlement(cce);
			courseGroupCustomerEntitlementItemRespository.delete(l);
		}

		// Delete Contract, finally	   
		customerEntitlementRespository.delete(objCustomerEntitlement);		
	}


	@Override
	public List<CustomerEntitlement> getCustomerEntitlementsByCutomer(Customer customer) {
		StringBuilder queryString = new StringBuilder("SELECT c FROM CustomerEntitlement c WHERE c.customer.id =:customerId ");
		
		Query query = entityManager.createQuery(queryString.toString());
		query.setParameter("customerId", customer.getId());
		
		List<CustomerEntitlement> listCustomerEntitlement = query.getResultList(); 
		
		return listCustomerEntitlement;
	}
	
}
