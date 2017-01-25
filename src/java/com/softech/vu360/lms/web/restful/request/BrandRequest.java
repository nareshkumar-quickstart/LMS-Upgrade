package com.softech.vu360.lms.web.restful.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BrandRequest{
	private List<String> brandKeyValue = new ArrayList();

	public List<String> getBrandKeyValue() {
		return brandKeyValue;
	}

	public void setBrandKeyValue(List<String> brandKeyValue) {
		this.brandKeyValue = brandKeyValue;
	}
	
	
	
}
