package com.vega.samo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.mongodb.MongoClient;

@Configuration
@ComponentScan(basePackages = "com.vega.samo.*")
@PropertySource("classpath:config.properties")
public class AppConfig {
	@Bean
	public MongoClient mongoClient() {
		return new MongoClient();
	}
}
