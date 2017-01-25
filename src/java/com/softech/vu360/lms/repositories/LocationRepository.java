package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.Location;

public interface LocationRepository extends CrudRepository<Location, Long>{

	public List<Location> findByIdIn(long[] ids);
	@Query("SELECT a FROM  Location a WHERE a.contentOwner.id=:coid and a.name LIKE %:locationName% AND a.address.city like %:city% AND  a.address.state like %:state% and address.country like %:country% and address.zipCode like %:zip%   and a.isActive =:isActive")
	public List<Location> findContentOwnerIdAndNameLikeAndAddressCityLikeAndAddressStateLikeAndAddressCountryLikeAndAddressZipcodeLikeAndIsActive(@Param("coid") Long contentOwnerId,@Param("locationName") String locationName,@Param("city") String city,@Param("state") String state,@Param("country") String country,@Param("zip") String zip,@Param("isActive") boolean isActive);
	
}
