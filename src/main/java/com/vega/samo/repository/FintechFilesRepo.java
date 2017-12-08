package com.vega.samo.repository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;

@Repository
public class FintechFilesRepo {

	@Autowired
	private MongoClient mongoClient;
	
	public FindIterable<Document> getAllFiles(String db, String files) {
		return mongoClient.getDatabase(db).getCollection(files).find();
	}
}
