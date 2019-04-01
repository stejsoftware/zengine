package com.stejsoftware.zengine.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Utility {
	private static final Logger LOG = LoggerFactory.getLogger(Utility.class);

	public static String getRootURL(RequestEntity<String> request) {
		URI url = request.getUrl();

		String scheme = url.getScheme();
		String host = url.getHost();
		Integer port = url.getPort();

		return String.format("%s://%s:%d", scheme, host, port);
	}

	public static List<File> getStories(String storyFolder) {
		List<File> stories = new ArrayList<>();

		try {
			File folder = new ClassPathResource(storyFolder).getFile();

			if (folder != null) {
				for (File file : folder.listFiles()) {
					stories.add(file);
				}
			}
		}
		catch (Exception ex) {
			LOG.error("error:", ex);
		}

		return stories;
	}
}