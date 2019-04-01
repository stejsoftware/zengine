package com.stejsoftware.zengine.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {

	@Value("${zengine.story.folder}")
	public String storyFolder;
}
