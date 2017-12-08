package com.vega.samo.controller;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.client.FindIterable;
import com.vega.samo.service.FintechFilesService;

@RestController
public class ReceiveController {
	private static Logger log = Logger.getLogger(ReceiveController.class);
	
	@Autowired
	private FintechFilesService fintechFilesService;
	
	@GetMapping("/private/file/get_all_file")
	public FindIterable<Document> getAllFile() {
		log.info("Get all document in db");
		return fintechFilesService.getAll();
	}
	
}
