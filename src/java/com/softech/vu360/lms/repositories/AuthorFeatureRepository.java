/**
 * 
 */
package com.softech.vu360.lms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.softech.vu360.lms.model.AuthorFeature;

/**
 * @author muhammad.junaid
 *
 */
public interface AuthorFeatureRepository extends CrudRepository<AuthorFeature, Long> {
	@Query("SELECT af FROM  #{#entityName} af WHERE af.displayName IN (:lstDisplayName) Order By af.id ASC")
	List<AuthorFeature> getAuthorFeaturesForSelfServiceUsers(@Param("lstDisplayName") List<String> lstDisplayName);
}
