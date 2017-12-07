package com.vega.samo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vega.samo.db.entities.FintechFile;

@Repository
public interface FileRepo extends MongoRepository<FintechFile, String>{
	
}
