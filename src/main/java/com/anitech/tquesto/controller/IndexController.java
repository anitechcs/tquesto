package com.anitech.tquesto.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This is the index controller required for SPA(Angular2) routing to work
 * 
 * @author Tapas
 * 
 */
@Controller
public class IndexController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	/**
	 * This method is to let spring play nice with Angular Routes
	 */
	@RequestMapping({"/home/**"})
	public String index() {
		logger.info("Inside IndexController->index(), Forwarding request to index.html!");
		return "forward:/index.html";
	}

}
