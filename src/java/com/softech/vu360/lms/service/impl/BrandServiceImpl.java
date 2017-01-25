package com.softech.vu360.lms.service.impl;

import java.util.ArrayList;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.softech.vu360.lms.model.Brand;
import com.softech.vu360.lms.repositories.BrandRepository;
import com.softech.vu360.lms.service.BrandService;



public class BrandServiceImpl implements BrandService 
{
	public static final int DEFAULT_BRAND_ID=1;
	 
	@Inject
	private BrandRepository brandRepository;

	public Brand getBrandById(long id) 
	{
			return brandRepository.findOne(id);
	}
	
	public ArrayList<Brand> getAllBrands(){
		return (ArrayList<Brand>) brandRepository.findAll();
	}
	/*
	 * All service will use BrandService to retrieve Default Brand
	 * BrandService is responsible to decide the default Brand
	 */
	public Brand getDefaultBrand()
	{
		return this.getBrandById(DEFAULT_BRAND_ID);
	}

	@Transactional
	@Override
	public void save(Brand brand) throws Exception {

		try{
			brandRepository.save(brand.getBrandName(), brand.getBrandKey());
		} catch (Exception exception) {
			// sometime procedure did not return any result due to jdbc dirver, then its throws particular Exception.
			if(!exception.getMessage().contains("The statement did not return a result set")) throw exception;
		}

	}

	@Transactional
	public void delete(Brand brand) {
		brandRepository.deleteByBrandNameIgnoreCase(brand.getBrandName());
	}

	@Override
	public Brand getBrandByName(String brandName) {
		return brandRepository.findByBrandNameIgnoreCase(brandName);
	}

}
