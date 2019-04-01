package com.stejsoftware.zengine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestRestController {
	private static Logger LOG = LoggerFactory.getLogger(TestRestController.class);

	@Autowired
	private ControllerConfig config;

	@GetMapping("/foo")
	public List<String> foo(RequestEntity<String> request) {
		String root = Utility.getRootURL(request);

		return Arrays.asList(root + "/foo", root + "/bar");
	}

	@GetMapping("/stories")
	public String stories() throws IOException {

		return "error";
	}
}
