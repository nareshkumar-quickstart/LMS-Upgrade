package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Test;

import com.softech.vu360.lms.SpringJUnitConfigAbstractTest;
import com.softech.vu360.lms.model.Language;

@Transactional
public class LanguageRepositoryTest extends SpringJUnitConfigAbstractTest {

	@Inject
	private LanguageRepository languageRepository;

	@Test
		public void Language_List_Should() {
			try{
				
				
				List<Language> lng = (List<Language>) languageRepository.findAll();
			//	Language lng1 = this.languageRepository.findOne(1L);
				for(Language language:lng)
					System.out.println(language.getDisplayName());
			//	System.out.println(lng1.getDisplayName());
				
			}catch(Exception ex){
				System.out.println(ex.getMessage());
			}
			
		}
}
