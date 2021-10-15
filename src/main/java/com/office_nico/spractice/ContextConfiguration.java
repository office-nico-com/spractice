package com.office_nico.spractice;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component
public class ContextConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(ContextConfiguration.class);

	 @Autowired
   MessageSource messagesource;
	
	@PostConstruct
	public void startup() {
		logger.info("************************************************************");
		logger.info("******************** Application booted ********************");
		logger.info("************************************************************");
	}
	
	@PreDestroy
	public void cleanup() {
		logger.info("*************************************************************");
		logger.info("******************** Application stopped ********************");
		logger.info("*************************************************************");
	}
	
}
