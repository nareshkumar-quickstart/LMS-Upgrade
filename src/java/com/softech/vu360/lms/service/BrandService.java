package com.softech.vu360.lms.service;

import java.util.ArrayList;

import com.softech.vu360.lms.model.Brand;

public interface BrandService {

	
	public Brand getBrandById(long id) ;
	public ArrayList<Brand> getAllBrands();
	public Brand getDefaultBrand();
	public void save(Brand brand) throws Exception ;
	public void delete(Brand brand) ;
	public Brand getBrandByName(String brandName);
	
}
