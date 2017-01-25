package com.softech.vu360.lms.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.SortTool;
import org.springframework.beans.factory.annotation.Autowired;

import com.softech.vu360.lms.exception.ReportNotExecutableException;
import com.softech.vu360.lms.model.Customer;
import com.softech.vu360.lms.model.Distributor;
import com.softech.vu360.lms.model.DistributorGroup;
import com.softech.vu360.lms.model.OrganizationalGroup;
import com.softech.vu360.lms.model.TrainingAdministrator;
import com.softech.vu360.lms.model.VU360Report;
import com.softech.vu360.lms.model.VU360ReportExecutionSummary;
import com.softech.vu360.lms.model.VU360ReportField;
import com.softech.vu360.lms.model.VU360ReportFilter;
import com.softech.vu360.lms.model.VU360ReportFilterOperand;
import com.softech.vu360.lms.model.VU360ReportParameter;
import com.softech.vu360.lms.model.VU360User;
import com.softech.vu360.lms.repositories.VU360ReportRepository;
import com.softech.vu360.lms.service.DistributorService;
import com.softech.vu360.lms.service.ReportExecutionService;
import com.softech.vu360.lms.service.ReportingConfigurationService;
import com.softech.vu360.lms.service.VU360UserService;
import com.softech.vu360.lms.util.ReportFieldComparator;
import com.softech.vu360.util.FormUtil;
import com.softech.vu360.util.SecurityUtil;
import com.softech.vu360.util.VU360Properties;

import gnu.regexp.RE;
import gnu.regexp.REException;

/**
 * ReportExecutionServiceImpl - generic implementation class
 * for the reporting framework that will query the database
 * given a velocity template.
 *
 * @author jason
 *
 */
public class ReportExecutionServiceImpl implements ReportExecutionService {

	private static final String HTML_VELOCITY_TEMPLATE_LOC = VU360Properties.getVU360Property("reporting.htmlTemplateLocation");
	private static final String CSV_VELOCITY_TEMPLATE_LOC = VU360Properties.getVU360Property("reporting.csvTemplateLocation");
	private static final String HTML_OUTPUT_LOC = VU360Properties.getVU360Property("reporting.htmlOutputLocation");
	private static final String CSV_OUTPUT_LOC = VU360Properties.getVU360Property("reporting.csvOutputLocation");
	private static final String MS_SQL_SERVER = "mssql";
	private static final String DB2 = "db2";
	private static final String SQLTEMPLATE_FOR_PROCTOR = "vm/reportsql/accr_RegulatorReportForProctor.vm";
	private static final String SQLTEMPLATE_FOR_PROCTOR_CONCURRENT_LEARNER = "vm/reportsql/accr_RegulatorRptProctor_ConcurrentLnr.vm";
	private static final String SQLTEMPLATE_FOR_LEARNER_PERFORMANCE_BY_COURSE_COMPREHENSIVE = "vm/reportsql/mgr_PrfLearnerByCourseComp.vm";
	
	private Logger log = Logger.getLogger(ReportExecutionServiceImpl.class);
	
	@Inject
	VU360ReportRepository vu360ReportRepository = null;

	@PersistenceContext
	protected EntityManager entityManager;

	@Autowired
	private ReportingConfigurationService reportConfigService;
	@Autowired
	private DistributorService distributorService;
	@Autowired
	private VU360UserService vu360UserService;
	
	private Map<String, Properties> reportPropertiesFiles = new HashMap<String, Properties>();

	public ReportExecutionServiceImpl() {
		loadPropertiesFiles();
	}

	
	public void executeReport(VU360Report report, com.softech.vu360.lms.vo.VU360User user) throws ReportNotExecutableException {
		log.debug("-----------STARTING EXECUTE REPORT------------");
		List<VU360ReportParameter> reportParams = report.getParameter();
		
		//Long reportId= report.getId();
		report = vu360ReportRepository.findOne(report.getId());
		List<VU360ReportFilter> temp = report.getFilters();
		/*report.setFilters(new ArrayList());
		report.getFilters().addAll(temp);
		*/report.setParameter(reportParams);
		
		// first check to see if this report is owned by the executing user
		if ( report.getSystemOwned() || report.getOwner().getId().longValue()==user.getId().longValue() ) {
			String reportName = null;
			try {
				RE re1 = new RE( "[^a-zA-Z0-9]", RE.REG_ICASE );
				reportName = re1.substituteAll( report.getTitle(),"");
				reportName = reportName.trim().toLowerCase();
			}
			catch( REException ree ) {
				log.error( "Reg exp exception: " + ree.getMessage() );
			}

			log.debug("STARTING REPORT EXECUTION....");
			try {
				Date executionDate = new Date(System.currentTimeMillis());

				Map<Object, Object> context = new HashMap<Object, Object>();

				// TODO:  overwrite last execution files
				//cleanDirectories(report, user);
				List<Object>params = addReportFiltersToSqlString(context, report, user,temp);
				addSortingToSqlString(context, report, user);

				// columns name collection
				String strColumCollectionList="";
				List<VU360ReportField> fields = report.getFields();
				List<VU360ReportField> selectedFields = new ArrayList<VU360ReportField>();
				for (VU360ReportField field : fields)
				{
					/* 
					 * @Modifier: Wajahat
					 * The Learner Group is not showing due to the below commented line
					 * On comparing with the Toplink branch the code is changed.
					 */
					
					//if ( field.getVisible() && field.getEnabled() &&  (!field.getDbColumnName().equals("OrganizationalGroupName") && !field.getDbColumnName().equals("LearnerGroupName"))) {
					if ( field.getVisible() && field.getEnabled()) {
						strColumCollectionList+=field.getDbColumnName()+", ";
						selectedFields.add(field);
					}
				}
				context.put("selectedFields", selectedFields);
				strColumCollectionList=strColumCollectionList.substring(0,strColumCollectionList.lastIndexOf(","));

				List<Map<Object, Object>> results;
				if(report != null
						&& (report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR) || report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_PROCTOR_CONCURRENT_LEARNER)))
					 results = queryDatabase(report.getSqlTemplateUri(), context, params, strColumCollectionList, report.getParameter());
				else
					results = queryDatabase(report.getSqlTemplateUri(), context, params, strColumCollectionList);

				VU360ReportExecutionSummary executionSummary = new VU360ReportExecutionSummary();
				executionSummary.setExecutionDate(executionDate);
				executionSummary.setUser(vu360UserService.getUserById(user.getId()));// LMS-2496
				results = decryptData(results, "VALUE");


				exportToHtml(results, report, user.getId().toString(), reportName, executionDate, context, executionSummary);
 				exportToZippedCsv(results, report, user.getId().toString(), reportName, executionDate, context, executionSummary);
				reportConfigService.addReportExeuctionSummary(report, executionSummary);

			}
			catch (Throwable th) {
				log.error("could not execute report", th);
			}
			finally {

			}
			log.debug("-----------EXECUTE REPORT COMPLETED------------");
		}
		else {
			// error
			throw new ReportNotExecutableException("report:"+report.getKey()+" is not owned by:"+user.getId());
		}
	}

	/**
	 * Used to render results for those reports that has any encrypted field to decrypt.
	 * @param rObjresults
	 * @param fieldToDecrypt
	 * @return
	 */
	public List<Map<Object, Object>> decryptData(List<Map<Object, Object>> rObjresults, String fieldToDecrypt)
	{
		List<Map<Object, Object>> results = new ArrayList<Map<Object, Object>>();
		Map<Object,Object> oMap = null;
		try {
			SecurityUtil securityUtil = SecurityUtil.getInstance();
			if(rObjresults != null && rObjresults.size() > 0)
			{
				for(Map<Object, Object> map : rObjresults)
				{
					oMap = new HashMap<Object,Object>();
					for(Map.Entry<Object, Object> entry : map.entrySet())
					{
						if(entry.getKey().equals(fieldToDecrypt)) {

							if(map.get("FIELDTYPE")!=null && !map.get("FIELDTYPE").equals("SSNCREDITREPORTINGFIELD")) {
								if(entry.getValue()!=null)
									oMap.put(entry.getKey(), securityUtil.decrypt(entry.getValue().toString()));
								else
									oMap.put(entry.getKey(), entry.getValue());
							}
							else
								oMap.put(entry.getKey(), "xxx-xx-xxxx");



						} else {
							oMap.put(entry.getKey(), entry.getValue());
						}
					}
					results.add(oMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("could not Execute decrypt function", e);
		}
		return rObjresults;
	}

	// ------------ private helper methods for all of the logic -------------------------------------------//

	private List<Map<Object, Object>> queryDatabase(String templateUri, Map<Object, Object> contextMap, List<Object> params, String columnCollectionListParam)
	{

		try {
			TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
			String sqlString = null;
			HashMap<Object, Object> context = new HashMap<Object, Object>(contextMap);
			try {
				sqlString = tmpSvc.renderTemplate(templateUri, context);
				sqlString=sqlString.replace("*"," distinct "+columnCollectionListParam);
				log.info("SQL:"+sqlString);
			}
			catch(Exception e) {
				log.error("could not render velocity template", e);
				e.printStackTrace();
			}

			List<Map<Object, Object>> results;
			if ( params != null && params.size() > 0 ) {
				results = vu360ReportRepository.executeSqlString(sqlString, params);
			}
			else {
				results = vu360ReportRepository.executeSqlString(sqlString);
			}

			return results;
		}
		catch (Throwable th) {
			log.error("could not execute report", th);
		}
		return null;
	}


	/**
	 *  Add function for Proctor Report. It can be use for reports getting input(Parameters) from user end
	 *  Adding Parameters values in Reports object
	 * @param templateUri
	 * @param contextMap
	 * @param params
	 * @param columnCollectionListParam
	 * @param reportParam
	 * @return
	 */
	private List<Map<Object, Object>> queryDatabase(String templateUri, Map<Object, Object> contextMap, List<Object> params, String columnCollectionListParam, List<VU360ReportParameter> reportParam)
	{

		try {
			TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
			String sqlString = null;
			HashMap<Object, Object> context = new HashMap<Object, Object>(contextMap);


			sqlString = tmpSvc.renderTemplate(templateUri, context);
			sqlString=sqlString.replace("*"," distinct "+columnCollectionListParam);

			for (VU360ReportParameter rp : reportParam)
				sqlString=sqlString.replace(rp.getParamName(), "'" + rp.getParamValue() +"'");


			if(contextMap.containsKey("sorting")){
				sqlString = sqlString + " " + contextMap.get("sorting");
			}

			log.info("SQL:"+sqlString);



			List<Map<Object, Object>> results;
			if ( params != null && params.size() > 0 ) {
				results = vu360ReportRepository.executeSqlString(sqlString, params);
			}
			else {
				results = vu360ReportRepository.executeSqlString(sqlString);
			}

			return results;
		}
		catch (Exception th) {
			log.error("could not execute report", th);
		}
		return null;
	}

	

	

	private boolean exportToHtml(List<Map<Object, Object>> results, VU360Report report, String userId, String reportName, Date executionDate, Map<Object, Object> context, VU360ReportExecutionSummary executionSummary) {
		boolean result = true;
		List<VU360ReportField> reportFields = new ArrayList<VU360ReportField>();
		VU360ReportField field;
		for (Iterator<VU360ReportField> fieldsIter = report.getFields().iterator(); fieldsIter.hasNext(); ) {
			field = (VU360ReportField)fieldsIter.next();
			if ( field.getEnabled() && field.getVisible() ) {
				reportFields.add(field);
			}
		}

		Collections.sort(reportFields);
		// pass along to Velocity to render report HTML view
		TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
		// this has to be a HashMap in order for the template service to work
		// correctly.
		HashMap<Object, Object> attrs = new HashMap<Object, Object>(context);
		attrs.put("objects", results);
		attrs.put("columns", reportFields);
		attrs.put("lastExecutedDate", executionDate);
		attrs.put("report", report);
		attrs.put("formUtil", FormUtil.getInstance());
		attrs.put("sorter", new SortTool());
		StringBuffer sb = new StringBuffer();
		sb.append(HTML_OUTPUT_LOC);
		sb.append("/");
		sb.append(userId);
		sb.append("/");
		sb.append(report.getKey());
		sb.append("/");
		log.debug("filedir["+sb.toString()+"]");
		File f = new File(sb.toString());
		f.mkdirs();
		sb.append(reportName);
		sb.append(".html");
		executionSummary.setHtmlLocation(sb.toString());

		try {
			f = new File(sb.toString());
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(f));
			log.debug("HTML_VELOCITY_TEMPLATE_LOC["+HTML_VELOCITY_TEMPLATE_LOC+"]");
			tmpSvc.renderTemplateToWriter(HTML_VELOCITY_TEMPLATE_LOC, attrs, bufWriter);
			bufWriter.flush();
			bufWriter.close();
		}
		catch(Exception e) {
			log.error("could not create html file", e);
			result = false;
		}
		return result;
	}

	private boolean exportToZippedCsv(List<Map<Object, Object>> results, VU360Report report, String userId, String reportName, Date executionDate, Map<Object, Object> context, VU360ReportExecutionSummary executionSummary) {
		boolean result = true;
		List<VU360ReportField> reportFields = new ArrayList<VU360ReportField>();
		VU360ReportField field;
		for (Iterator<VU360ReportField> fieldsIter = report.getFields().iterator(); fieldsIter.hasNext(); ) {
			field = (VU360ReportField)fieldsIter.next();

			if ( field.getEnabled() && field.getVisible() ) {
				reportFields.add(field);
			}
		}
		Collections.sort(reportFields);
		// pass along to Velocity to render report CSV view
		StringBuffer csvFileName = new StringBuffer();
		csvFileName.append(CSV_OUTPUT_LOC);
		csvFileName.append("/");
		csvFileName.append(userId);
		csvFileName.append("/");
		File f = new File(csvFileName.toString());
		f.mkdirs();
		csvFileName.append(reportName);
		csvFileName.append(".csv");
		// Create the ZIP file
		StringBuffer outZipFileName = new StringBuffer();
		outZipFileName.append(CSV_OUTPUT_LOC);
		outZipFileName.append("/");
		outZipFileName.append(userId);
		outZipFileName.append("/");
		outZipFileName.append(report.getKey());
		outZipFileName.append("/");
		outZipFileName.append(reportName);
		outZipFileName.append(".zip");
		executionSummary.setCsvLocation(outZipFileName.toString());

		// pass along to Velocity to render report HTML view
		TemplateServiceImpl tmpSvc = TemplateServiceImpl.getInstance();
		HashMap<Object, Object> attrs = new HashMap<Object, Object>(context);
		attrs.put("objects", results);
		attrs.put("columns", reportFields);
		attrs.put("lastExecutedDate", executionDate);
		attrs.put("formUtil", FormUtil.getInstance());
		attrs.put("sorter", new SortTool());
		try {

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outZipFileName.toString()));
			// Compress the files
			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(reportName+".csv"));

			// write into the file
			OutputStreamWriter outWriter = new OutputStreamWriter(out);
			BufferedWriter bufWriter = new BufferedWriter(outWriter);
			tmpSvc.renderTemplateToWriter(CSV_VELOCITY_TEMPLATE_LOC,attrs,bufWriter);
			bufWriter.flush();

			// Complete the entry
			out.closeEntry();
			// Complete the ZIP file
			out.close();
		}
		catch(Exception e) {
			log.error("could not create csv zip archive", e);
			result = false;
		}
		return result;
	}

	private List<Object> addReportFiltersToSqlString(Map<Object, Object> context, VU360Report report, com.softech.vu360.lms.vo.VU360User user,List<VU360ReportFilter> filters) {
		StringBuffer sb = new StringBuffer();
		String dbSource = report.getDataSource();
		Properties propsFile = null;
		if ( dbSource == null || dbSource.trim().equalsIgnoreCase("mssql") ) {
			propsFile = (Properties)reportPropertiesFiles.get(MS_SQL_SERVER);
		}
		else {
			propsFile = (Properties)reportPropertiesFiles.get(DB2);
		}
		List<Object> params = new ArrayList<Object>();
		VU360ReportFilter filter;
		boolean first = true;
		int count = 0;
		Date startDate = new Date(); 
		
		for (Iterator<VU360ReportFilter> filtersIter = filters.iterator(); filtersIter.hasNext(); ++count) {
			filter = (VU360ReportFilter)filtersIter.next();
			if ( first ) {
				sb.append(" WHERE ");
				first = false;
			}
			else {
				sb.append(" AND ");
			}

			// figure out if we need to add any special characters and then use parameter
			// binding so that Toplink escapes any embedded charactes that need escaping
			if ( filter.getReportFilterType().equalsIgnoreCase(VU360ReportFilter.STRING_TYPE) ) {
				String filterOp = null;
				try {
					RE re1 = new RE( "[^a-zA-Z]", RE.REG_ICASE );
					filterOp = re1.substituteAll(filter.getOperand().getValue(),"");
					filterOp = filterOp.trim().toLowerCase();
				}
				catch( REException ree ) {
					log.error( "Reg exp exception: " + ree.getMessage() );
				}
				log.debug("Before:String OPERATOR IS:"+filterOp);
				sb.append("UPPER(");
				sb.append(filter.getField().getDbColumnName());
				sb.append(") ");
				sb.append(propsFile.getProperty(filterOp));
				log.debug("AFTER:String OPERATOR IS:"+propsFile.getProperty(filterOp));
				String value = filter.getValue().getStringValue().trim().toUpperCase();
				if (filter.getOperand().getValue().equalsIgnoreCase(VU360ReportFilterOperand.CONTAINS_OP) ) {
					value = "%"+value+"%";
				}
				else if (filter.getOperand().getValue().equalsIgnoreCase(VU360ReportFilterOperand.STARTSWITH_OP) ) {
					value = value+"%";
				}
				else if (filter.getOperand().getValue().equalsIgnoreCase(VU360ReportFilterOperand.ENDSWITH_OP) ) {
					value = "%"+value;
				}
				params.add(value);
				sb.append(" ? ");
			}
			else if ( filter.getReportFilterType().equalsIgnoreCase(VU360ReportFilter.DATE_TYPE) ) {

                String filterOp = filter.getOperand().getValue();
                filterOp = getDatabaseOperand(report, filter, filterOp);

                if ( report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_LEARNER_PERFORMANCE_BY_COURSE_COMPREHENSIVE) 
                		&& report.getMode().trim().equalsIgnoreCase(VU360Report.MANAGER_MODE) && count == filters.size() - 2 ){
                	sb.append(" ( ( ");
                	
                	startDate = filter.getValue().getDateValue();
                }
                
                sb.append(getColumnWithDateConversion(filter.getField().getDbColumnName(), dbSource));
                sb.append(" ");
                log.debug("Before:Date OPERATOR IS:"+filterOp);
                sb.append(propsFile.getProperty(filterOp));
                Date date = filter.getValue().getDateValue();
                log.debug("After:Date OPERATOR IS:"+propsFile.getProperty(filterOp));

                params.add(new java.sql.Timestamp(date.getTime()));//there is no date field in sqlserver but datetime. And javax.sql.Date doesn't contain time
                sb.append(" ? ");
                
                if ( report.getSqlTemplateUri().equalsIgnoreCase(SQLTEMPLATE_FOR_LEARNER_PERFORMANCE_BY_COURSE_COMPREHENSIVE) 
                		&& report.getMode().trim().equalsIgnoreCase(VU360Report.MANAGER_MODE) && count == filters.size() - 1 ){
                	sb.append(" ) OR ( CompletionDate >= '");
                	sb.append(new java.sql.Timestamp(startDate.getTime()));
                	sb.append("' AND CompletionDate <= '");
                	sb.append(new java.sql.Timestamp(date.getTime()));
                	sb.append("' ) OR ( FirstAccessDate >= '");
                	sb.append(new java.sql.Timestamp(startDate.getTime()));
                	sb.append("' AND FirstAccessDate <= '");
                	sb.append(new java.sql.Timestamp(date.getTime()));
                	sb.append("' ) )");
                }
			}
			else if ( filter.getReportFilterType().equalsIgnoreCase(VU360ReportFilter.BOOLEAN_TYPE) ) {

				String filterOp= filter.getOperand().getValue();
				sb.append("UPPER(");
				sb.append(filter.getField().getDbColumnName());
				sb.append(") ");

				log.debug("Before:Boolean OPERATOR IS:"+filterOp);
				sb.append(propsFile.getProperty(filterOp));
				log.debug("After:Boolean OPERATOR IS:"+propsFile.getProperty(filterOp));
				if ( filter.getValue().getStringValue().equalsIgnoreCase("true") || filter.getValue().getStringValue().equalsIgnoreCase("yes"))
				{
					params.add("1");//TODO need to check it, and might replace it by Boolean.TRUE
				}
				else {
					params.add("0"); //TODO need to check it, and might replace it by Boolean.FALSE
				}
				sb.append(" ? ");
			}
			else if ( filter.getReportFilterType().equalsIgnoreCase(VU360ReportFilter.NUMERIC_TYPE) ) {
				sb.append(filter.getField().getDbColumnName());
				String filterOp = null;
				try {
					RE re1 = new RE( "[^a-zA-Z]", RE.REG_ICASE );
					filterOp = re1.substituteAll(filter.getOperand().getValue(),"");
					filterOp = filterOp.trim().toLowerCase();
				}
				catch( REException ree ) {
					log.error( "Reg exp exception: " + ree.getMessage() );
				}
				sb.append(" ");
				log.debug("BEFORE:NUMERIC OPERATOR IS:"+filterOp);
				sb.append(propsFile.getProperty(filterOp));
				log.debug("AFTER:NUMERIC OPERATOR IS:"+propsFile.getProperty(filterOp));
				params.add(new BigDecimal(filter.getValue().getNumericValue().toString()));
				sb.append(" ? ");
			}
			else if ( filter.getReportFilterType().equalsIgnoreCase(VU360ReportFilter.OBJECT_TYPE) ) {
				// TODO: need to do groups, learners, etc and by default add in a group/customer filer
				//       if one was not provided to ensure data security integridy.
				List<Object> ids = filter.getValue().getCollectionsValue();

				// if filter type == org group
				//createCollectionFilterForIds(ids, OBJECT_TYPE_ORG_GROUP);

				// if filter type == learner group
				//createCollectionFilterForIds(ids, OBJECT_TYPE_LEARNER_GROUP);

				// if filter type == learner
				//createCollectionFilterForIds(ids, OBJECT_TYPE_LEARNER);

				// if filter type == customer
				//createCollectionFilterForIds(ids, OBJECT_TYPE_CUSTOMER);

				// if filter type == distributor
				//createCollectionFilterForIds(ids, OBJECT_TYPE_DISTRIBUTOR);


			}

		}

		// TODO:  ADD AUTOMATIC/SECURITY FILTERS FOR THIS REPORT/USER
		if ( report.getMode().trim().equalsIgnoreCase(VU360Report.LEARNER_MODE) ) {
			// add where learner ID = this user's learner id
			if ( first ) {
				sb.append(" WHERE ");
				first = false;
			}
			else {
				sb.append(" AND ");
			}
			sb.append("LEARNER_ID = ?");
			params.add(new BigDecimal(user.getLearner().getId()));
		}
		else if ( report.getMode().trim().equalsIgnoreCase(VU360Report.MANAGER_MODE) ) {
			// add where org group or customer ID = this training managers org groups or customer id
			log.debug("manageAll orgGroups:"+user.getTrainingAdministrator().isManagesAllOrganizationalGroups());
			if ( user.getTrainingAdministrator().isManagesAllOrganizationalGroups() ) {
				if ( first ) {
					sb.append(" WHERE ");
					first = false;
				}
				else {
					sb.append(" AND ");
				}
				sb.append("CUSTOMER_ID = ?");//by contract all manager level report should have a view with the column name CUSTOMER_ID
				//params.add(new BigDecimal(user.getLearner().getCustomer().getId()));
				
				//TODO set customer
				Customer customer = null; //((VU360UserAuthenticationDetails)SecurityContextHolder.
						//getContext().getAuthentication().getDetails()).getCurrentCustomer();

				if( customer == null ){
					params.add(new BigDecimal(user.getLearner().getCustomer().getId()));
				}else{
					params.add(new BigDecimal(customer.getId()));
				}

			}
			else {
				//find a list of all org group IDs and then use the OBJECT_TYPE filter to add this filter to the report
				// add an IN clause or insert into temp table for join
				// also remember you have to recursively go through these to ensure that all child org groups
				// are included
				TrainingAdministrator trainingAdministrator = vu360UserService.findTrainingAdminstratorById(user.getTrainingAdministrator().getId());
				List<OrganizationalGroup> managedOrgGroups = trainingAdministrator.getManagedGroups();
				Set<Long> ids = new HashSet<Long>();
				Stack<OrganizationalGroup> stack = new Stack<OrganizationalGroup>();
				stack.addAll(managedOrgGroups);
				OrganizationalGroup orgGroup = null;

				// recurrsively go through each org group and add it to our set
				// and then check the children groups to add to stack
				while ( !stack.isEmpty() ) {
					orgGroup = stack.pop();

					ids.add(orgGroup.getId());

					if ( orgGroup.getChildrenOrgGroups() != null ) {
						stack.addAll(orgGroup.getChildrenOrgGroups());
					}
				}
				// now call our object type filter method with these list of IDs
				createCollectionFilterForIds(ids, OBJECT_TYPE_ORG_GROUP,sb);
			}

		}
		else if ( report.getMode().trim().equalsIgnoreCase(VU360Report.ADMINISTRATOR_MODE) ) {
			// add distributor ID from group of distributor IDs or none if manage all distributors
			log.debug("isGlobalAdministrator="+user.getLmsAdministrator().isGlobalAdministrator());
			if(user.getLmsAdministrator().isGlobalAdministrator())
			{
				log.debug("user["+user.getUsername()+"] ");
				//getAll records no need to apply any filter
			}
			else
			{
				
				List<DistributorGroup> distgroups = distributorService
						.findDistributorGroupsByLMSAdministratorId(user.getLmsAdministrator().getId());
				Set<Long> ids = new HashSet<Long>();
				List<Distributor> distributors=new ArrayList<Distributor>();
				for(DistributorGroup distGroup:distgroups)
				{
					
					//LMS-14184
					List<Distributor> ls = distributorService.getAllowedDistributorByGroupId(String.valueOf(distGroup.getId()), String.valueOf(user.getLmsAdministrator().getId()));
					if(ls!=null && ls.size() > 0)
						distributors.addAll(ls);
					
				}	

				for(Distributor distributor:distributors)
				{
					ids.add(distributor.getId());
				}
				
				createCollectionFilterForIds(ids, OBJECT_TYPE_DISTRIBUTOR,sb);
			}
		}
		else if ( report.getMode().trim().equalsIgnoreCase(VU360Report.ACCREDITATION_MODE) ) {
			// TODO:  for future reports in accreditation module.

		}


		if ( !sb.toString().trim().equalsIgnoreCase("") ) {
			context.put("filters", sb.toString());
		}

		return params;
	}


	private void addSortingToSqlString(Map<Object, Object> context, VU360Report report, com.softech.vu360.lms.vo.VU360User user) {
		StringBuffer sb = new StringBuffer();
		VU360ReportField rpField;
		// find out which fields to sort by
		List<VU360ReportField> sortFields = new ArrayList<VU360ReportField>();
		for (Iterator<VU360ReportField> fieldsIter = report.getFields().iterator(); fieldsIter.hasNext(); ) {
			rpField = (VU360ReportField)fieldsIter.next();
			// for now all fields that are enabled and visible are sortable
			if ( rpField.getEnabled() && rpField.getVisible() && (!rpField.getDbColumnName().equals("OrganizationalGroupName") && !rpField.getDbColumnName().equals("LearnerGroupName")) ) {
				sortFields.add(rpField);
			}
		}
		// order the sort fields
		Collections.sort(sortFields, new ReportFieldComparator(ReportFieldComparator.SORTORDER));
		// now add the sorting to the sql string
		int count = 0;
		for (Iterator<VU360ReportField> sortFieldIter = sortFields.iterator(); sortFieldIter.hasNext(); ++count) {
			rpField = (VU360ReportField)sortFieldIter.next();
			if ( count == 0 )  {
				sb.append(" ORDER BY ");
			}
			else {
				sb.append(",");
			}
			if ( rpField.getDataType().equalsIgnoreCase(VU360ReportField.DT_STRING) ) {
				//sb.append("UPPER(");
				sb.append(rpField.getDbColumnName());
				//sb.append(")");
			}
			else {
				sb.append(rpField.getDbColumnName());
			}
			sb.append(" ");
			sb.append((rpField.getSortType()==VU360ReportField.SORT_DESSCENDING?"desc":"asc"));

		}
		// TODO this check is not required.
		if ( sb.toString().trim().length()>0 )
		{
			if(sb.charAt(sb.length()-1)==',')
			{
				sb.delete(sb.length()-1, sb.length());
			}

			context.put("sorting", sb.toString());
		}
	}


	private Map<String, Properties> loadPropertiesFiles() {
		this.reportPropertiesFiles = new HashMap<String, Properties>();
		try {
			Properties properties = new Properties();
			ClassLoader classLoader = VU360Properties.class
			.getClassLoader();
			InputStream resourceAsStream = classLoader.getResourceAsStream("report.mssql.properties");
			if (resourceAsStream != null) {
				properties.load(resourceAsStream);
				this.reportPropertiesFiles.put("default", properties);
				this.reportPropertiesFiles.put(MS_SQL_SERVER, properties);
			}
			resourceAsStream = classLoader.getResourceAsStream("report.db2.properties");
			if (resourceAsStream != null) {
				properties.load(resourceAsStream);
				this.reportPropertiesFiles.put(DB2, properties);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.reportPropertiesFiles;
	}
	private static final int OBJECT_TYPE_LEARNER=0;
	private static final int OBJECT_TYPE_LEARNER_GROUP=1;
	private static final int OBJECT_TYPE_ORG_GROUP=2;
	private static final int OBJECT_TYPE_CUSTOMER=3;
	private static final int OBJECT_TYPE_DISTRIBUTOR=4;
	private StringBuilder createCollectionFilterForIds(Set<Long> ids, int OBJECT_TYPE, StringBuffer sb)
	{


		StringBuilder sbValues=new StringBuilder();
		Iterator itr=ids.iterator();
		Long id=null;
		while(itr.hasNext())
		{
			id=(Long)itr.next();
			sbValues.append(id);
			sbValues.append(",");
		}

		//StringBuilder sb=new StringBuilder();
		if(OBJECT_TYPE==OBJECT_TYPE_ORG_GROUP)
		{
			if(sbValues.length()>0)
			{
				sbValues.delete(sbValues.length()-1,sbValues.length());
				if(sb.indexOf("WHERE")<0)
					sb.append(" WHERE ORGANIZATIONALGROUP_ID IN (");
				else
					sb.append(" AND ORGANIZATIONALGROUP_ID IN (");
				sb.append(sbValues);
				sb.append(") ");
			}
		}
		else
			if(OBJECT_TYPE==OBJECT_TYPE_DISTRIBUTOR)
			{
				if(sbValues.length()>0)
				{
					sbValues.delete(sbValues.length()-1,sbValues.length());
					if(sb.indexOf("WHERE")<0)
						sb.append(" WHERE DISTRIBUTOR_ID IN (");
					else
						sb.append(" AND DISTRIBUTOR_ID IN (");

					sb.append(sbValues);
					sb.append(") ");
				}

			}
		return null;
	}
	//TODO this is quick fix should be properly handled after this release for data comparison
	private String getColumnWithDateConversion(String column,String dbSource)
	{
		StringBuilder sb=new StringBuilder();
		if ( dbSource == null || dbSource.trim().equalsIgnoreCase("mssql") ) {

			sb.append(" convert(varchar, year(");
			sb.append(column);
			sb.append("))+'-'+case when len(convert(varchar, month(");
			sb.append(column);
			sb.append("))) = 1 then '0'+convert(varchar, month(");
			sb.append(column);
			sb.append(")) else convert(varchar, month(");
			sb.append(column);
			sb.append(")) end+'-'+convert(varchar, day(");
			sb.append(column);
			sb.append("))+' 00:00:00.0'");
		}
		else
		{


		}

		return sb.toString();
		//convert(Varchar,CREATEDDATE,105)+' 00:00:00.0'
	}

    /**
     * Get appropriate operand based on database engine
     *
     * @param report
     * @param filter
     * @param filterOp
     * @return
     */
    private String getDatabaseOperand(VU360Report report, VU360ReportFilter filter, String filterOp) {
        String dbSource = report.getDataSource();
        Properties propsFile = null;
        if ( dbSource == null || dbSource.trim().equalsIgnoreCase("mssql") ) {
            propsFile = (Properties)reportPropertiesFiles.get(MS_SQL_SERVER);
        }
        else {
            propsFile = (Properties)reportPropertiesFiles.get(DB2);
        }

        try {
            RE re1 = new RE( "[^a-zA-Z]", RE.REG_ICASE );
            filterOp = re1.substituteAll(filter.getOperand().getValue(),"");
            filterOp = filterOp.trim().toLowerCase();
        }
        catch( REException ree ) {
            log.error( "Reg exp exception: " + ree.getMessage() );
        }

        return filterOp;
    }

}