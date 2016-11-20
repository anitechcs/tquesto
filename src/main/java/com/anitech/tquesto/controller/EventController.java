package com.anitech.tquesto.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tapas
 *
 */
@RestController
@RequestMapping("/api")
public class EventController {

	@RequestMapping("/event/{eventId}")
    String home(@PathVariable Long eventId) {
        return "Hello Event! " + eventId;
    }
	
}
