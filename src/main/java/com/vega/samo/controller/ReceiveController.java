package com.vega.samo.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vega.samo.config.FileConfig;
import com.vega.samo.service.FileService;
import com.vega.samo.transform.CoreResponse;

@RestController
public class ReceiveController {
	private static Logger log = Logger.getLogger(ReceiveController.class);
	
	AbstractApplicationContext context = new AnnotationConfigApplicationContext(FileConfig.class);
	FileService fileService = (FileService) context.getBean("fileService");
	
//	public static void main(String[] args) {
//		AbstractApplicationContext context = new AnnotationConfigApplicationContext(FileConfig.class);
//        FileService fileService = (FileService) context.getBean("fileService");
//		
//        for (FintechFile file: fileService.getAllFile()) {
//        	log.info(JsonMapper.writeValueAsString(file) );
//        }
//        context.close();
//	}
	
	@GetMapping("/private/file/get_all_file")
	public CoreResponse getAllFile() {
		log.info("In here");
		return new CoreResponse(fileService.getAllFile());
	}
	
}
