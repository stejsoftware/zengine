package com.stejsoftware.zengine.controller;

import com.stejsoftware.zengine.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestRestController {
	private final ControllerConfig controllerConfig;

	public TestRestController(ControllerConfig controllerConfig) {
		this.controllerConfig = controllerConfig;
	}

	@GetMapping("/foo")
	public List<String> foo(RequestEntity<String> request) {
		String root = Utility.getRootURL(request);
		return List.of(root + "/foo", root + "/bar");
	}

	@GetMapping("/stories")
	public String stories() {
		return controllerConfig.getStoryFolder();
	}
}
