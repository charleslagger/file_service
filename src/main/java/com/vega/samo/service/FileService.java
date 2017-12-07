package com.vega.samo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.vega.samo.db.entities.FintechFile;
import com.vega.samo.repository.FileRepo;

@Service
public class FileService {	
	@Autowired
	private FileRepo fileRepo;
	
	public List<FintechFile> getAllFile(){
		return fileRepo.findAll();
	}
}
