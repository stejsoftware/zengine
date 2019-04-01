package com.stejsoftware.zengine;

import com.stejsoftware.zengine.data.Game;
import com.zaxsoft.zmachine.ZCPU;
import com.zaxsoft.zmachine.ZUserInterface;

import java.io.File;
import java.util.UUID;

public class ZGame {
	private final File story;
	private final String id;
	private final ZUserInterface ui;
	private final ZCPU cpu;

	public ZGame(File story) {
		this.story = story;
		this.id = UUID.randomUUID().toString();

		this.ui = new ZUserInterfaceImpl();
		this.cpu = new ZCPU(this.ui);
		this.cpu.initialize(this.story.getAbsolutePath());
	}

	public static Game fromStoryFile(String storyFile) {
		Game game = new Game();
		//	game.loadStoryFile(storyFile);
		return game;
	}

	private void loadStoryFile(String storyFile) {
//		this.storyFile = storyFile;
//
//		List<Byte> data = new ArrayList<>();
//
//		/*
//				try {
//			File story = new ClassPathResource(config.storyFolder + "/" + game.getStoryFile()).getFile();
//		}
//		catch (IOException e) {
//			LOG.error("error", e);
//		}
//
//		*/
//
//		// Read in the story file
//		try {
//			ByteArrayInputStream is = new ByteArrayInputStream(Files.readAllBytes(Paths.get(storyFile)));
//
//			while (is.available() > 0) {
//				data.add(Byte.valueOf((byte) is.read()));
//			}
//		}
//		catch (IOException ex) {
//			ex.printStackTrace();
//		}
//
//		this.memory.init(data);
	}

	public void start() {
		if (!cpu.is_running()) {
			cpu.start();
		}
	}

	public String getId() {
		return id;
	}

	public File getStory() {
		return story;
	}

	public Game toGame() {
		Game game = new Game();
		return game;
	}
}
