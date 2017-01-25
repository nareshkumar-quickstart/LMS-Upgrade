/**
 * 
 */
package com.softech.vu360.lms.repositories;


import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Brand;

/**
 * @author marium.saud
 *
 */
public interface BrandRepository extends CrudRepository<Brand, Long> {
	
	public Brand findByBrandNameIgnoreCase(@Param("brandName") String brandName);
	
	public void deleteByBrandNameIgnoreCase(@Param("brandName") String brandName) ;
	
	@Procedure(name = "Brand.addNewBrand")
	public void save(@Param("BRAND_NAME") String brandName,@Param("BRAND_KEY") String brandKey);

}
