package com.softech.vu360.lms.service.impl;

import com.softech.aws.model.AffiliateResult;
import com.softech.aws.model.AmazonResult;
import com.softech.aws.service.AmazonResultConversion;
import com.softech.aws.service.AmazonService;
import com.softech.vu360.lms.service.AmazonIntegrationService;

public class AmazonIntegrationServiceImpl implements AmazonIntegrationService{
	
	public AffiliateResult affiliateSearchResults(String keyword){
		
		
		String[] keywordString = new String[]{keyword};

		// Define Amazon LMS Affiliate Service
		AmazonService amazonAffiliateService = new AmazonService();

		// Define LMS Conversion Service
		AmazonResultConversion amazonResultConversion = new AmazonResultConversion();
		
				
		// -------------------------------------------------------------------------------------------------------
		// AWS : Item Lookup
		// -------------------------------------------------------------------------------------------------------
		AmazonResult amazonSearchResults = amazonAffiliateService.getAmazonSearchResults(keywordString);
		

		// Convert Amazon Search Results
		AffiliateResult affiliateLookupResult =  amazonResultConversion.convertAmazonResult(amazonSearchResults);
		
		
		return affiliateLookupResult;
	}
	
}
