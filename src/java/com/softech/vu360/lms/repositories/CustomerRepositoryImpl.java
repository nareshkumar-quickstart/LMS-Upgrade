package com.softech.vu360.lms.repositories;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.LMSAdministratorAllowedDistributor;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.util.ResultSet;

/**
 * 
 * @author haider.ali
 *
 */
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

	
	@Inject
	LMSAdministratorAllowedDistributorRepository  lMSAdministratorAllowedDistributorRepository;
	
	@Inject
	LMSAdministratorRepository lMSAdministratorRepository;

	@Inject
	DistributorRepository distributorRepository;

	@PersistenceContext
	protected EntityManager entityManager;


	@Override
	public List<Customer> findCustomersByName(String name, String orderId, VU360User loggedInUser, boolean isExact) {

		StringBuilder queryString = new StringBuilder("SELECT c FROM Customer c   ");
		List<Distributor> list = getAdminRestrictionExpression(loggedInUser);

		if (list!=null && list.size() > 0){
			queryString.append(" JOIN c.distributor d2 ");
		}
		
		if (isExact) {
			queryString.append("WHERE c.name = :uname");
		}
		else{
			queryString.append("WHERE c.name like :uname ");
		}

		if (list!=null && list.size() > 0){
			queryString.append(" AND c.distributor IN :distId ");
		}

		Query query = entityManager.createQuery(queryString.toString());
		
		if (isExact) {
			query.setParameter("uname",  name );
		}
		else{
			query.setParameter("uname",  name +"%" );
		}
		
		
		if (list!=null && list.size() > 0){
			query.setParameter("distId", list);
		}
		List<Customer> customerList = query.getResultList(); 
		return customerList;
	}

	private List<Distributor> getAdminRestrictionExpression(VU360User loggedInUser){

		List<Distributor> distributorList = new ArrayList<Distributor>();

		//Highly optimized 
		if(loggedInUser.getLmsAdministrator() != null && !loggedInUser.getLmsAdministrator().isGlobalAdministrator()){
			
			List<DistributorGroup> ls = loggedInUser.getLmsAdministrator().getDistributorGroups();
			Set<DistributorGroup> uniqueDistributorGroups = new HashSet<DistributorGroup>(ls);
			
			//get id from collection.
			Collection<Long> dgIds = Collections2.transform(uniqueDistributorGroups, new Function<DistributorGroup, Long>() {
					public Long apply(DistributorGroup arg0) {
						return Long.parseLong(arg0.getId().toString());
					}
				}
			);

			
			List<LMSAdministratorAllowedDistributor> allowedDistributors = lMSAdministratorAllowedDistributorRepository.findByDistributorGroupIdInAndLmsAdministratorId(dgIds, loggedInUser.getLmsAdministrator().getId());

			//get id from collection.			
			Collection<Long> ids = Collections2.transform(allowedDistributors, new Function<LMSAdministratorAllowedDistributor, Long>() {
						public Long apply(LMSAdministratorAllowedDistributor arg0) {
							return Long.parseLong(arg0.getAllowedDistributorId().toString());
						}
					}
			);
			
			if(ids!=null && !ids.isEmpty()){
				Set<Long> uniqueIds = new HashSet<Long>(ids);
				distributorList =  (List<Distributor>) distributorRepository.findAll(uniqueIds);
			}
			
			
		}
		return distributorList;
	}
	
	public List<Map<Object, Object>> findCustomersSimpleSearch(String name,
			String orderId, String orderDate, String emailAddress,
			VU360User loggedInUser, boolean isExact, int startRowIndex,
			int noOfRecords, ResultSet resultSet,String sortBy,int sortDirection) {
		
		String orderBy;
		
		if(sortDirection==0){
			orderBy=" order by "+sortBy+" ASC";
		}
		else{
			orderBy=" order by "+sortBy+" DESC";
		}
		
		String distributorIds = getAdminRestrictionDistributorId(loggedInUser);
	
		String sql = "select * from fn_SearchCustomer('"+ name + "', '"+ orderId + "', '"+ orderDate + "', '"+ emailAddress + "', '"+ distributorIds +"')" + orderBy;
		String countSql="select count(1) from fn_SearchCustomer('"+ name + "', '"+ orderId + "', '"+ orderDate + "', '"+ emailAddress + "', '"+ distributorIds +"')";
		StringBuilder queryString = new StringBuilder(sql);
		StringBuilder countString = new StringBuilder(countSql);

		Query query = entityManager.createNativeQuery(queryString.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		//if paging is requested
		if(noOfRecords!=-1 && startRowIndex>=0){
			query.setFirstResult(startRowIndex);
			query.setMaxResults(noOfRecords);
		}
		
		List rows = query.getResultList();  
		List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
		mapList.addAll(rows);
		
		Query count = entityManager.createNativeQuery(countString.toString());
		resultSet.total = (int) count.getSingleResult();
 		return mapList;
	}

	private String getAdminRestrictionDistributorId(VU360User loggedInUser){
		
		StringBuffer comaSepratedIds = new StringBuffer();
		List<Distributor> disList = getAdminRestrictionExpression(loggedInUser);
		for (Iterator<Distributor> iterator = disList.iterator(); iterator.hasNext();) {
			Distributor distributor = (Distributor) iterator.next();
			comaSepratedIds.append(distributor.getId());
			comaSepratedIds.append(", ");
		}
		return comaSepratedIds.toString();
	}

	@Override
	public List<Map<Object, Object>> findCustomersAdvanceSearch(
			String name, String optrForCustName ,
			String orderId , String optrForOrderId,
			String orderDate, String operator3 ,
			String emailAddress ,VU360User loggedInUser   ,boolean isExact,  int startRowIndex,
			int noOfRecords, ResultSet resultSet, String sortBy,int sortDirection) {

		
		DateFormat dfChanged = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dfActual = new SimpleDateFormat("MM/dd/yyyy");
		String startDate="", endDate="";
		try {
			if(!orderDate.equalsIgnoreCase("")){
				startDate = dfChanged.format(dfActual.parse(orderDate)) + " 00:00:00";
				endDate = dfChanged.format(dfActual.parse(orderDate)) + " 23:59:59";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String sql="", sqlIncludeCustomerName="", sqlIncluseEmail="";
		
		//TODO UNIMPLEMENTED METHOD, DEPENDENT ON OTHER DAO TO IMPLEMENT
		String distributorIds = getAdminRestrictionDistributorId(loggedInUser);
		
		sqlIncludeCustomerName = " c.NAME like '%" + name + "%' " + optrForCustName;
		sqlIncluseEmail = " c.email like '%" + emailAddress + "%'";
		
		sql = "SELECT c.id as customerId ,C.NAME as customerName, C.EMAIL as customerEmail, C.ACTIVETF as customerStatus, c.CUSTOMERCODE as customerCode, d.ID as distributorId, min(CE.CONTRACTCREATIONDATE) as originalContractCreationDate, max(CE.CONTRACTCREATIONDATE) as recentContractCreationDate " +
		" FROM CUSTOMER C " +
		" left join CUSTOMERENTITLEMENT ce on ce.CUSTOMER_ID=c.ID " +
		" inner join DISTRIBUTOR d on d.id = c.DISTRIBUTOR_ID " +
		" where 1=1";

		if(distributorIds!=null && !distributorIds.equalsIgnoreCase(""))
			sql = sql + " and d.id in ("+distributorIds+") ";
		
		
		if(!orderId.equals("") && !orderDate.equals("")){
			sql = sql + " and ( " ;
				
				if(name!=null && !name.equals(""))          //---------------------------
					sql = sql + sqlIncludeCustomerName;  
				
				sql = sql + "  c.ID in (Select CUSTOMER_ID from ORDERINFO where OrderGUID =  '" + orderId + "') " +
				" " + optrForOrderId + "   c.ID in (Select CUSTOMER_ID from ORDERINFO where OrderDate between '"+ startDate +"' and '"+ endDate +"') " ;
				
				if(emailAddress!=null && !emailAddress.equals(""))
					sql = sql + operator3 + sqlIncluseEmail;
					
//				sql = sql + " ) order by c.ID ";
		
		}else if(!orderId.equals("") && orderDate.equals("")){
			sql = sql + " and ( " ;							 //---------------------------
			
				if(name!=null && !name.equals(""))
					sql = sql + sqlIncludeCustomerName; 
			
				sql = sql +  "  c.ID in (Select CUSTOMER_ID from ORDERINFO where OrderGUID =  '" + orderId + "') ";
				
				if(emailAddress!=null && !emailAddress.equals(""))
					sql = sql + optrForOrderId + sqlIncluseEmail;
				
//				sql = sql +" ) order by c.ID ";
			
		}else if(orderId.equals("") && !orderDate.equals("")){
			sql = sql + " and ( " ;
			if(name!=null && !name.equals(""))
				sql = sql + sqlIncludeCustomerName; 
			
				sql = sql + "   c.ID in (Select CUSTOMER_ID from ORDERINFO where OrderDate between '"+ startDate +"' and '"+ endDate +"') ";
				
				if(emailAddress!=null && !emailAddress.equals(""))
					sql = sql + operator3 + sqlIncluseEmail;
				
//				sql = sql + " ) order by c.ID ";

		} else if(orderId.equals("") && orderDate.equals("")){
			sql = sql + " and ( " ;
			
			if(name!=null && !name.equals(""))
				sql = sql + " c.NAME like '%" + name + "%' " ; 
			
			if(name!=null && !name.equals("") && emailAddress!=null && !emailAddress.equals(""))
				sql = sql + optrForCustName;
				
			if(emailAddress!=null && !emailAddress.equals(""))
				sql = sql + sqlIncluseEmail;
		}
		
		sql = sql + " ) group by c.id ,C.NAME, C.EMAIL, C.ACTIVETF, c.customerCode, d.ID"  ;
		
		//Setting count query before 'order by' clause (SQL limitation)
		String countSql="select count(1) from (" + sql + ") a";
		StringBuilder countString = new StringBuilder(countSql);
		Query count = entityManager.createNativeQuery(countString.toString());
		
		//handling for sortBya nd sortDirection arguments because of JPQL Query
		String direction="";
		if(sortDirection==0){
			direction="ASC";
		}
		else{
			direction="DESC";
		}
		if(sortBy.equalsIgnoreCase("customerName")){
			sql = sql + " order by c.NAME " + direction;
		}
		else if(sortBy.equalsIgnoreCase("customerEmail")){
			sql = sql + " order by c.EMAIL " + direction;
		}
		else if(sortBy.equalsIgnoreCase("customerStatus")){
			sql = sql + " order by c.ACTIVETF " + direction;
		}
		else if(sortBy.equalsIgnoreCase("originalContractCreationDate")){
			sql = sql + " order by originalContractCreationDate " + direction;
		}
		else if(sortBy.equalsIgnoreCase("recentContractCreationDate")){
			sql = sql + " order by recentContractCreationDate " + direction;
		}
		
		Query query = entityManager.createNativeQuery(sql);
		
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		//if paging is requested
		if(noOfRecords!=-1 && startRowIndex>=0){
			query.setFirstResult(startRowIndex);
			query.setMaxResults(noOfRecords);
		}
		List resultList = query.getResultList();
		List<Map<Object, Object>> mapList = new ArrayList<Map<Object,Object>>();
		mapList.addAll(resultList); 
		resultSet.total = (int) count.getSingleResult();
		return mapList;
	}

	


	/*@Override
	public Customer findDefaultCustomerByDistributor(Distributor dist) {
		// TODO Required RED -- 
		return null;
	}*/

/*	@Override
	public List<CustomerEntitlement> findCustomersWithEntitlementByDistributor(Long customerId) {
	
		
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			final CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(CustomerEntitlement.class);
			
			final Root customerRoot = criteriaQuery.from(Customer.class);
			final Root customerEntitlementRoot = criteriaQuery.from(CustomerEntitlement.class);
			List<Predicate> criteriaList = new ArrayList<Predicate>();

			Predicate predicate1 = criteriaBuilder.equal(customerRoot.get("Customer").get("id"), customerEntitlementRoot.get("customer_ID"));
	       criteriaList.add(predicate1);
			
	       
	       //cq.select(customer).where(predicates.toArray(new Predicate[]{}));
			//List<Customer> ls = entityManager.createQuery(cq).getResultList();
			//return ls;
			
	        criteriaQuery.select(customerEntitlementRoot).where(criteriaList.toArray(new Predicate[]{}));
			List<CustomerEntitlement> ls = entityManager.createQuery(criteriaQuery).getResultList();

			return ls;

	}
*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*@Override
	public List<Customer> findCustomersByDistributor(Long distributorId) {
		// TODO Auto-generated method stub
		return null;
	}*/

	


	@Override
	public Long getCustomersByCurrentDistributorRecordCount(
			String firstName, String lastName, String email, int pageIndex,
			int pageSize, String sortBy, int sortDirection) {
		
		CriteriaBuilder qb_ = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cq_ = qb_.createQuery(Long.class);
		Root<Customer> customer = cq_.from(Customer.class);
		
		//Constructing list of parameters
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotEmpty(firstName)){
			Expression<String> path = customer.get(COL_FIRSTNAME);
			predicates.add(qb_.like(path, PERCENT + firstName + PERCENT));
		}	

		if(StringUtils.isNotEmpty(lastName)){
			Expression<String> path1 = customer.get(COL_LASTNAME);
			predicates.add(qb_.like(path1, PERCENT + lastName + PERCENT));
		}
		if(StringUtils.isNotEmpty(email)){
			Expression<String> path2 = customer.get(COL_EMAIL);
			predicates.add(qb_.like(path2, PERCENT + email + PERCENT));
		}
		
		cq_.select(qb_.count(cq_.from(Customer.class)));
		cq_.where(predicates.toArray(new Predicate[]{}));
		return (Long) entityManager.createQuery(cq_).getSingleResult();

	}
	
	@Override
	public List<Customer> getCustomersByCurrentDistributor(
			String firstName, String lastName, String email, int pageIndex,
			int pageSize, String sortBy, int sortDirection) {
		
		CriteriaBuilder qb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = qb.createQuery(Customer.class);
		Root<Customer> customer = cq.from(Customer.class);
		
		//Constructing list of parameters
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (StringUtils.isNotEmpty(firstName)){
			Expression<String> path = customer.get(COL_FIRSTNAME);
			predicates.add(qb.like(path, PERCENT + firstName + PERCENT));
		}	

		if(StringUtils.isNotEmpty(lastName)){
			Expression<String> path1 = customer.get(COL_LASTNAME);
			predicates.add(qb.like(path1, PERCENT + lastName + PERCENT));
		}
		if(StringUtils.isNotEmpty(email)){
			Expression<String> path2 = customer.get(COL_EMAIL);
			predicates.add(qb.like(path2, PERCENT + email + PERCENT));
		}
		
		if(sortDirection==0)  cq.orderBy(qb.asc(customer.get(sortBy)));
		if(sortDirection==1)  cq.orderBy(qb.desc(customer.get(sortBy)));
		
		CriteriaQuery<Customer> select = cq.select(customer).where(predicates.toArray(new Predicate[]{}));
		
		TypedQuery<Customer> typedQuery = entityManager.createQuery(select);
		typedQuery.setFirstResult((pageIndex-1) * pageSize);
		typedQuery.setMaxResults(pageSize);
		List<Customer> ls = typedQuery.getResultList();
		return ls;
		
	}

	@Override
	public List<Customer> findCustomersWithCustomFields(VU360User loggedInUser) {
		
		StringBuilder queryString = new StringBuilder("SELECT c FROM Customer c  ");
		
		List<Distributor> list = getAdminRestrictionExpression(loggedInUser);

		if (list!=null && list.size() > 0){
			queryString.append(" WHERE c.distributor IN :distId ");
		}
		else{
			queryString.append("WHERE c.distributor.id = :distId");
		}
		

		Query query = entityManager.createQuery(queryString.toString());
		
		if (list!=null && list.size() > 0){
			query.setParameter("distId", list);
		}else{
			query.setParameter("distId", new Long(-1));
		}
		
		List<Customer> customerList = query.getResultList(); 
		return customerList;
		
		
		//TODO COMPLEX METHOD
		/*ReadAllQuery raq = new ReadAllQuery(Customer.class);
		ExpressionBuilder builder = raq.getExpressionBuilder();
		Expression adminExpr = getAdminRestrictionExpression(loggedInUser);
		Expression wherecust = builder.anyOf("customFields").get("id").greaterThan(0);			
		if(adminExpr != null){
			wherecust = wherecust.and(adminExpr);
		}		
		raq.setSelectionCriteria(wherecust);
		return (List<Customer>)getTopLinkTemplate().executeQuery(raq);*/
		
	}

	
	/*@Override
	public boolean updateDefaultCustomer(Distributor distributor) {
		
		entityManager.
		return false;
	}
	*/
	
	
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}




	


	
}
