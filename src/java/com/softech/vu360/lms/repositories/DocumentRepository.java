package com.softech.vu360.lms.repositories;

import org.springframework.data.repository.CrudRepository;

import com.softech.vu360.lms.model.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> {
	
	public Document findFirstByNameOrderByIdDesc(String documentName);
	
}
