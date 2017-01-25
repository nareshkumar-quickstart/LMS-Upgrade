package com.softech.vu360.lms.repositories;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.softech.vu360.lms.LmsTestConfig;
import com.softech.vu360.lms.model.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=LmsTestConfig.class)
public class DocumentRepositoryTest {

	@Inject
	private DocumentRepository documentRepository;
	
	//@Test
	public void credentialRepository_should_save() {
		Document saveDocument = documentRepository.findOne(new Long(2105));
		saveDocument.setId(null);
		
		Document savedDocument = documentRepository.save(saveDocument);
		
		System.out.println("..........");
	}
	
	//@Test
	public void purchaseCertificateNumber_should_find() {
		Document saveDocument = documentRepository.findOne(new Long(2));
		
		System.out.println("..........");
	}
	
	//@Test
	public void purchaseCertificateNumber_should_deleteAll(List<Document> objDocuments) {
		documentRepository.delete(objDocuments);
		
		System.out.println("..........");
	}

	//Added By Marium Saud
	@Test
	public void document_should_findByName(){
		Document doc=documentRepository.findFirstByNameOrderByIdDesc("360 Training");
		if(doc!=null){
			System.out.println("Doc Id " + doc.getId() + "and name is " +doc.getName());
		}
		else {
			System.out.println("No Doc Found ");
		}
	}
	
}
