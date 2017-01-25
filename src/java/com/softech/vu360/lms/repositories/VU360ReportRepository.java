package com.softech.vu360.lms.repositories;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.softech.vu360.lms.model.VU360Report;




import org.springframework.data.repository.query.Param;
/**
 * Repository interface for {@code VU360Report}s.
 * 
 * @author muhammad.rehan
 * 
 */
public interface VU360ReportRepository extends CrudRepository<VU360Report, Long>,VU360ReportRepositoryCustom {

	//public List<VU360Report> findSystemReportsByMode(String mode);
	List<VU360Report> findBySystemOwnedOrderByTitleAsc(Boolean systemOwned);
	List<VU360Report> findByModeAndSystemOwnedOrderByTitleAsc(String mode, Boolean systemOwned);

	
	//public List<VU360Report> findReportsByOwner(VU360User owner, String mode);
	@Query("SELECT r FROM  #{#entityName} r join fetch r.owner u WHERE u.id =:ID")
	public List<VU360Report> findByOwnerId(@Param("ID") Long Id);
	
	@Query("SELECT r FROM  #{#entityName} r join fetch r.owner u WHERE u.id =:ID and r.mode=:MODE")
	public List<VU360Report> findByOwnerIdAndMode(@Param("ID") Long Id, @Param("MODE") String mode);
}
