package com.softech.vu360.lms.aspects.audit;

/**
 * 
 * @author haider.ali
 *
 */
public interface IAuditLogAspect {
	public static final String PointCutProvider = "execution(public * com.softech.vu360.lms.repositories.ProviderRepository.save(..))";
	public static final String PointCutCourseApproval = "execution(public * com.softech.vu360.lms.repositories.CourseApprovalRepository.save(..))";
	public static final String PointCutRegulatorCategory = "execution(public * com.softech.vu360.lms.repositories.RegulatorCategoryRepository.save(..))";
	public static final String PointCutAssignCreditReportingFields = "execution(public *  com.softech.vu360.lms.service.impl.AccreditationServiceImpl.saveRegulatorCategoryCreditReporting(..))";
	public static final String PointCutUnAssignCreditReportingFields = "execution(public *  com.softech.vu360.lms.service.impl.AccreditationServiceImpl.dropRegulatorCategoryCreditReporting(..))";
	public static final String PointCutDeleteRegulatorCategories = "execution(public *  com.softech.vu360.lms.service.impl.AccreditationServiceImpl.deleteRegulatorCategories(..))";
	public static final String PointCutReportingFields = "execution(public *  com.softech.vu360.lms.service.impl.AccreditationServiceImpl.saveCreditReportingField(..))";
	
}
