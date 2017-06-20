package com.softech.vu360.lms.repositories;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

import com.softech.vu360.lms.model.Course;
import com.softech.vu360.lms.model.CourseGroup;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorEntitlement;
import com.softech.vu360.util.FormUtil;

public class CourseGroupRepositoryImpl implements CourseGroupRepositoryCustom{
	
	private static final Logger log = Logger.getLogger(CourseGroupRepositoryImpl.class.getName());
	
	@PersistenceContext
	protected EntityManager entityManager;

		@Override
		public List<Map<Object, Object>> findByAvailableCourseGroups(Distributor distributor, String courseName, String entityId,	String keywords) {
			
			StringBuilder strSQL =new StringBuilder("Select * from LMS_CONTRACTSEARCH_VIEW where Distributor_ID=" + distributor.getId());
			
			String strSQLSubQuery=" AND (";
						
			if(StringUtils.isNotEmpty(courseName)) 	{
				String []courseNameList = courseName.split(",");
				
				for(int i=0;i<courseNameList.length;i++){
					if(i==0) //1st iteration
						strSQLSubQuery+="(COURSEGROUPNAME LIKE '%" + courseNameList[i].trim().replace("'", "''") +"%'";
					else
					{
						strSQLSubQuery+=" OR COURSEGROUPNAME LIKE '%" + courseNameList[i].trim().replace("'", "''") +"%'";
					}
						
							
				}
					
				
					strSQLSubQuery+=")";
			}
			
			if(StringUtils.isNotEmpty(entityId)) 	{
				String []courseIDList = entityId.split(",");
				
				if(strSQLSubQuery.length()>10)
					strSQLSubQuery+="OR(";
				else
					strSQLSubQuery+="(";
				
			
				for(int i=0;i<courseIDList.length;i++){
					if(i==0) //1st iteration
						strSQLSubQuery+=" BUSINESSKEY LIKE '%" + courseIDList[i].trim().replace("'", "''") +"%'";
					else
					{
						strSQLSubQuery+=" OR BUSINESSKEY LIKE '%" + courseIDList[i].trim().replace("'", "''") +"%'";
					}
						
							
				}
				strSQLSubQuery+=")";
			}
			
			if(StringUtils.isNotEmpty(keywords)) 	{
				if(strSQLSubQuery.length()>10)
					strSQLSubQuery+="OR(";
				else
					strSQLSubQuery+="(";
				strSQLSubQuery+=" KEYWORDS LIKE '%" + keywords.trim().replace("'","''")+ "%'";
				strSQLSubQuery+=")";
			}
			strSQLSubQuery+=")"; //ANDS ending )
				
			if(strSQLSubQuery.length()>10)
				strSQL.append(strSQLSubQuery);
			
			
			Query query = entityManager.createNativeQuery(strSQL.toString());
			
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List rows = query.getResultList();  
			List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
			mapList.addAll(rows);
			return mapList;
			
		}
	
		public List<Map<Object, Object>> findByAvailableCourseGroups(Distributor distributor, List<Long> courseGroudIdList) {
			
			StringBuilder strSQL = new StringBuilder("Select distinct * from LMS_CONTRACTSEARCH_VIEW where Distributor_ID=" + distributor.getId());
			
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append(strSQL);
			
			if (!org.springframework.util.CollectionUtils.isEmpty(courseGroudIdList)) {
				
				StringBuilder subQueryBuilder = new StringBuilder();
				subQueryBuilder.append(" AND COURSEGROUP_ID IN (");
				
				for (Long courseGroupdId : courseGroudIdList) {
					subQueryBuilder.append(courseGroupdId).append(",");
				}
				
				subQueryBuilder.deleteCharAt(subQueryBuilder.length() - 1);
				subQueryBuilder.append(")");
				queryBuilder.append(subQueryBuilder);
				
			}
			
			
			Query query = entityManager.createNativeQuery(queryBuilder.toString());
			
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List rows = query.getResultList();  
			List<Map<Object, Object>> mapList =new   ArrayList<Map<Object,Object>>();
			mapList.addAll(rows);
			return mapList;

			
			
		}
	
		@SuppressWarnings("unchecked")
		@Override
		public List<CourseGroup> findCoursesAndCourseGroupsByDistributor(
				Distributor distributor, String courseName, String entityId,
				String keywords, String contractType) {

			boolean courseNameFlag = false;
			boolean courseFlag = false;
			boolean courseGroupFlag = false;
			boolean courseFlag2=false;
			boolean courseGroupFlag2=false;
			boolean entityIdFlag = false;
			boolean keywordsFlag=false;
			StringBuilder subQueryBuilder = null;
			StringBuilder subQueryBuilder2 = null;
			List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
			
			StringBuilder builder = new StringBuilder(
					"Select p from DistributorEntitlement p");
			
			builder.append(" JOIN p.courseGroups c JOIN c.courses d WHERE p.distributor.id=:distributorId");// ).equal(distributor.getId()));
			builder.append(" AND d.courseStatus=:coursePublished");// Course.PUBLISHED));

			if (StringUtils.isNotEmpty(courseName)) {
				String[] courseNameList = courseName.split(",");
				courseNameFlag = true;

				
				if (contractType.equalsIgnoreCase("Course")) {

					subQueryBuilder = new StringBuilder("Select s.id from Course s");

					for (int i = 0; i < courseNameList.length; i++) {
						if (i == 0) {
							subQueryBuilder.append(" WHERE s.courseTitle like ")
							.append(courseNameList[0].trim()).append(":per");
						} else {
							subQueryBuilder.append(" or s.courseTitle like ")
							.append(courseNameList[i].trim()).append(":per");
						}
					}

					builder.append(" AND d.id IN (:idList1)");
					courseFlag = true;
				} else { // for Course Group Type Contract

					subQueryBuilder = new StringBuilder(
							"Select s.id from CourseGroup s");

					for (int i = 0; i < courseNameList.length; i++) {
						if (i == 0) {
							subQueryBuilder.append(" WHERE s.name like ")
							.append(":per").append(courseNameList[0].trim()).append(":per");
						} else {
							subQueryBuilder.append(" or s.name like ").append(":per")
							.append(courseNameList[i].trim()).append(":per");
						}
					}

					builder.append(" AND c.id IN (:idList2)");
					courseGroupFlag=true;
				}

			}

			if (StringUtils.isNotEmpty(entityId)) {
				String[] courseIDList = entityId.split(",");
				entityIdFlag=true;
				
				if (contractType.equalsIgnoreCase("Course")) {

					subQueryBuilder2 = new StringBuilder(
							"Select s.id from Course s");

					for (int i = 0; i < courseIDList.length; i++) {
						if (i == 0) {
							subQueryBuilder2.append(" WHERE s.bussinesskey like ").append(":per").append(courseIDList[0].trim()).append(":per");
						} else {
							subQueryBuilder2.append(" or s.bussinesskey like ").append(":per").append( courseIDList[i].trim()).append(":per");
						}
					}
					builder.append(" AND d.id IN (:idList3)");
					courseFlag2=true;

				} else { // for Course Group Type Contract
					

						subQueryBuilder2 = new StringBuilder(
								"Select s.id from CourseGroup s");

						for (int i = 0; i < courseIDList.length; i++) {
							if (i == 0) {
								subQueryBuilder2
										.append(" WHERE s.courseGroupID like ")
										.append(":per").append(courseIDList[0].trim()).append(":per");
							} else {
								subQueryBuilder2.append(" or s.courseGroupID like ")
								.append(":per").append(courseIDList[i].trim()).append(":per");
							}
						}
						builder.append(" AND c.id IN (:idList4)");
						courseGroupFlag2=false;
				
				}

				if (StringUtils.isNotEmpty(keywords)) {
					keywordsFlag=true;
					if (contractType.equalsIgnoreCase("Course")){
						builder.append(" AND d.keywords like :keyWords");
						
						}
					else{
						builder.append(" AND c.keywords like :keyWords");
						
					}

				}
				Query subQuery = null;
				Query subQuery2 = null;
				System.out.println(builder.toString());
				Query topQuery = entityManager.createQuery(builder.toString());
				List<Course> courseList = null;
				List<CourseGroup> courseGroupList = null;
				
				
				if(courseNameFlag){
					System.out.println(subQueryBuilder2.toString());
					subQuery = entityManager.createQuery(subQueryBuilder.toString());
					subQuery.setParameter(":per", "%");
					if(courseFlag){
						courseList=subQuery.getResultList();
						topQuery.setParameter("idList1", courseList);
					}
					if(courseGroupFlag){
						courseGroupList=subQuery.getResultList();
						topQuery.setParameter("idList2", courseGroupList);
					}
					
				}
				if(entityIdFlag){
					System.out.println(subQueryBuilder2.toString());
					subQuery2 = entityManager.createQuery(subQueryBuilder2.toString());
					subQuery2.setParameter(":per", "%");
					if(courseFlag2){
						courseList=subQuery2.getResultList();
						topQuery.setParameter("idList3", courseList);
					}
									
					if(courseGroupFlag2){
						courseList=subQuery2.getResultList();
						topQuery.setParameter("idList4", courseGroupList);
					}
						
					
				}
				if(keywordsFlag){
						topQuery.setParameter("keyWords", "%"+keywords+"%");
					}
				
				topQuery.setParameter("distributorId", distributor.getId());
				topQuery.setParameter("coursePublished", Course.PUBLISHED);

				List<DistributorEntitlement> result = topQuery.getResultList();
				

				for (DistributorEntitlement de : result) {
					courseGroups.addAll(de.getCourseGroups());
				}
				
				
			}
			return courseGroups;
		}

		
		/**
		 * 
		 */
		@SuppressWarnings("unchecked")
		@Override
		public List<CourseGroup> findCoursesAndCourseGroupsByDistributor_Adv(
				Distributor distributor, String courseName, String entityId,
				String keywords, String contractType) {

			boolean courseGroupFlag = false;
			boolean courseFlag =false;
			boolean courseFlag2=false;
			boolean entityIdFlag = false;
			boolean keywordsFlag=false;
			StringBuilder subQueryBuilder = null;
			StringBuilder subQueryBuilder2 = null;
			List<CourseGroup> courseGroups = new ArrayList<CourseGroup>();
			
			StringBuilder builder = new StringBuilder("Select p from DistributorEntitlement p");
			builder.append(" JOIN p.courseGroups c JOIN c.courses d WHERE p.distributor.id=:distributorId");// ).equal(distributor.getId()));
			builder.append(" AND d.courseStatus=:coursePublished");// Course.PUBLISHED));

			String[] courseNameList=null;
			List<CourseGroup> courseGroupList = null;
			List<Course> courseList = null;
			
			if (StringUtils.isNotEmpty(courseName)) {
				
				courseNameList = courseName.split(",");
				
				if (contractType.equalsIgnoreCase("Course")) {
					
					/*subQueryBuilder = new StringBuilder("Select s.id from Course s where c.status='active");
					for (int i = 0; i < courseNameList.length; i++) {
						if (i == 0) {
							subQueryBuilder.append(" WHERE s.courseTitle like :per ");
						} else {
							subQueryBuilder.append(" or s.courseTitle like ")
							.append(courseNameList[i].trim()).append(":per");
						}
					}
					
					String courseTitle = "%";
					if(courseNameList[0]!=null){
						courseTitle= courseNameList[0].trim()+"%";
					}
					System.out.println(subQueryBuilder.toString());
					Query subQuery = entityManager.createQuery(subQueryBuilder.toString());
					subQuery.setParameter("per", courseTitle);
					courseList = subQuery.getResultList();
					
					if(courseList.size()>0)
						builder.append(" AND d.id IN (:idList1)");
					
					courseFlag = true;
					*/
					
				} else { // for Course Group Type Contract
	
					subQueryBuilder = new StringBuilder("Select s.id from CourseGroup s");
						for (int i = 0; i < courseNameList.length; i++) {
							if (i == 0) {
								subQueryBuilder.append(" WHERE s.name like :per");
							} else {
								subQueryBuilder.append(" or s.name like ").append(":per").append(courseNameList[i].trim())
										.append(":per");
							}
						} 
						
						String parm = "%";
						if(courseNameList[0]!=null){
							parm= "%"+courseNameList[0].trim()+"%";
						}

						System.out.println(subQueryBuilder.toString());
						Query subQuery = entityManager.createQuery(subQueryBuilder.toString());
						subQuery.setParameter("per", parm);
						courseGroupList = subQuery.getResultList();
						

					if(courseGroupList.size()>0)
						builder.append(" AND c.id IN (:idList2)");
					
					courseGroupFlag=true;
				}

			}
			
			System.out.println(builder.toString());
			Query topQuery = entityManager.createQuery(builder.toString());
			
			if(courseGroupList!=null && courseGroupList.size()>0)
				topQuery.setParameter("idList2", courseGroupList);

			//if(courseList!=null && courseList.size()>0)
				//topQuery.setParameter("idList1", courseList);
			
			topQuery.setParameter("distributorId", distributor.getId());
			topQuery.setParameter("coursePublished", Course.PUBLISHED);

			List<DistributorEntitlement> result = topQuery.getResultList();
			
			for (DistributorEntitlement de : result) {
				courseGroups.addAll(de.getCourseGroups());
			}
				
			return courseGroups;
		}
		
		@Override
		public List<CourseGroup> getCourseGroupsByDistributor(Long distributorId) {
			List<CourseGroup> courseGroups=new ArrayList<CourseGroup>();
			StringBuilder builder=new StringBuilder("Select p from DistributorEntitlement p where p.distributor.id=:distributorId and p.startDate <= :now");
			Date now = new Date(System.currentTimeMillis());
			now = DateUtils.truncate(now, Calendar.DATE);
			Query query=entityManager.createQuery(builder.toString());
			query.setParameter("distributorId", distributorId);
			query.setParameter("now", now);
			@SuppressWarnings("unchecked")
			List<DistributorEntitlement> cGList=query.getResultList();
			if(cGList!=null){
				for(DistributorEntitlement de:cGList){
					courseGroups.addAll(de.getCourseGroups());
				}
			}
			return courseGroups;
		}

		@Override
		public List<CourseGroup> getAllChildCourseGroupsForCourseGroups(
				List<CourseGroup> courseGroups) {
			List<CourseGroup> completeCourseGroups = new ArrayList<CourseGroup>(courseGroups);
			
			if (courseGroups != null && courseGroups.size() > 0) {
				Object []idList=FormUtil.getPropertyArrayFromList(courseGroups);
				List<Object> passList=Arrays.asList(idList);
				StringBuilder builder=new StringBuilder("Select p from CourseGroup p where p.parentCourseGroup.id IN (:idList)");
				Query query=entityManager.createQuery(builder.toString());
				query.setParameter("idList", passList);
				@SuppressWarnings("unchecked")
				List<CourseGroup> resultCourseGroups = query.getResultList();
				if (resultCourseGroups != null && resultCourseGroups.size() > 0) {
					for(CourseGroup cg : resultCourseGroups) {
						List<CourseGroup> newCGs = new ArrayList<CourseGroup>();
						if (!completeCourseGroups.contains(cg)){
							completeCourseGroups.add(cg);
							newCGs.add(cg);
						}
						List<CourseGroup> childCGs = getAllChildCourseGroupsForCourseGroups(newCGs);
						if (childCGs.size() > 0) {
							completeCourseGroups.addAll(childCGs);
						}
					}
				}
			}
			
			return completeCourseGroups;
		}
		
		@Override
        public List<Map<Object, Object>> findCourseGroups(String courseName, String entityId, String keywords) {
            StringBuilder strSQL =new StringBuilder("Select * from LMS_SEARCH_ALL_COURSEGROUPS_VIEW ");
            StringBuilder strSQLSubQuery = new StringBuilder(" WHERE ");
						
            if(StringUtils.isNotEmpty(courseName))
            {
                String []courseNameList = courseName.split(",");
                for(int i=0;i<courseNameList.length;i++){
                    if(i==0) //1st iteration
                        strSQLSubQuery.append("(COURSEGROUPNAME LIKE '%" + courseNameList[i].trim().replace("'", "''") +"%'");
                    else
                    {
                    	strSQLSubQuery.append(" OR COURSEGROUPNAME LIKE '%" + courseNameList[i].trim().replace("'", "''") +"%'");
                    }
                }
                strSQLSubQuery.append(")");
            }
		
            if(StringUtils.isNotEmpty(entityId)) {
                if(StringUtils.isNotEmpty(courseName)){
                    strSQLSubQuery.append(" OR ");
                }
                
                String []courseIDList = entityId.split(",");
                for(int i=0;i<courseIDList.length;i++){
                    if(i==0) //1st iteration
                            strSQLSubQuery.append(" (BUSINESSKEY LIKE '%" + courseIDList[i].trim().replace("'", "''") +"%'");
                    else
                    {
                            strSQLSubQuery.append(" OR BUSINESSKEY LIKE '%" + courseIDList[i].trim().replace("'", "''") +"%'");
                    }
                }
                strSQLSubQuery.append(" )");
            }
		
            if(StringUtils.isNotEmpty(keywords)) {
                if(StringUtils.isNotEmpty(courseName) || StringUtils.isNotEmpty(entityId)){
                    strSQLSubQuery.append(" OR ");
                }
                
                String []keywordsList = keywords.split(",");
                for(int i=0; i<keywordsList.length; i++){
                    if( i==0 ) //1st iteration
                            strSQLSubQuery.append(" (KEYWORDS LIKE '%" + keywordsList[i].trim().replace("'", "''") +"%'");
                    else
                    {
                            strSQLSubQuery.append(" OR KEYWORDS LIKE '%" + keywordsList[i].trim().replace("'", "''") +"%'");
                    }
                }
                strSQLSubQuery.append(" )");
            }

            if(StringUtils.isNotEmpty(courseName) || StringUtils.isNotEmpty(entityId) || StringUtils.isNotEmpty(keywords))
                strSQL.append(strSQLSubQuery);
		
            log.debug("Query: " + strSQL);
            System.out.println(strSQL.toString());
            Query query = entityManager.createNativeQuery(strSQL.toString());
			
			query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			List rows = query.getResultList();  
			List<Map<Object, Object>> results =new   ArrayList<Map<Object,Object>>();
			results.addAll(rows);
            return results;
        }

}
