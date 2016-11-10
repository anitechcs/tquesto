package com.anitech.tquesto.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tapas
 *
 * This is the index controller required for SPA(Angular2) routing to work
 * 
 */
@RestController
public class IndexController {

	@RequestMapping("/")
    String home() {
        return "Hello Tapas!";
    }

}
