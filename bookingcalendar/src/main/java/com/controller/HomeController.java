package com.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeController {
	
	// inject via application.properties
			@Value("${welcome.message:test}")
			private String message = "Booking Calendar";

			@RequestMapping("/")
			public ModelAndView welcome(Map<String, Object> model) {
				model.put("message", this.message);
				return new ModelAndView("welcome",model);
			}

}
