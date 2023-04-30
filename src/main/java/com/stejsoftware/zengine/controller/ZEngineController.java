package com.stejsoftware.zengine.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ZEngineController {
	@GetMapping(value = "/")
	public String index() {
		return "index";
	}
}