package com.softech.vu360.lms.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Asset;
import com.softech.vu360.lms.model.ContentOwner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class AssetRepositoryTest {

	@Inject
	private AssetRepository assetRepository;
	
	
	@Test
	public void assets_should_find_by_co_and_name_and_keywords() {
		ContentOwner co = new ContentOwner();
		co.setId(new Long(53));
		//List<Asset> listRegulators = (ArrayList<Asset>)assetRepository.findAssetByContentownerAndNameAndKeywords(co.getId(),"043009-Document", "");
		List<Asset> listRegulators2 = (ArrayList<Asset>)assetRepository.findAssetByContentownerAndNameAndKeywords(co.getId(),"image", "image");
		
		System.out.println("..........");
	}

}
