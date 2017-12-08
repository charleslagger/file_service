package com.vega.samo.service;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.mongodb.client.FindIterable;
import com.vega.samo.repository.FintechFilesRepo;

@Component
public class FintechFilesService {
	@Autowired
	private FintechFilesRepo fintechFileRepo;
	
	private String DB;
	private String DOC_FINTECH_FILES;
	private String DOC_PARTNERS;
	
	@Autowired
	public FintechFilesService(Environment env) {
		DB = env.getProperty("mongo.db");
		DOC_FINTECH_FILES = env.getProperty("mongo.doc_files");
		DOC_PARTNERS = env.getProperty("mongo.doc_partners");
	}
	
	public FindIterable<Document> getAll(){
		
		return fintechFileRepo.getAllFiles(DB, DOC_FINTECH_FILES);
	}

}
