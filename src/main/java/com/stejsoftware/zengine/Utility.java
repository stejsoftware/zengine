package com.stejsoftware.zengine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Utility {
	private Utility() {
	}

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
			Collections.addAll(stories, Objects.requireNonNull(folder.listFiles()));
		}
		catch (Exception ex) {
			log.error("error:", ex);
		}

		return stories;
	}
}