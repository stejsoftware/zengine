package com.stejsoftware.zengine.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stejsoftware.zengine.zmachine.Memory;
import com.stejsoftware.zengine.zmachine.ProgramCounter;
import com.stejsoftware.zengine.zmachine.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {
	@JsonIgnore
	private static final Logger LOG = LoggerFactory.getLogger(Game.class);

	private String id;
	private String name;
	private String storyFile;

	private List<Move> moveHistory;

	private Memory memory;
	private ProgramCounter pc;
	private Stack stack;

	public void init(File storyFile) {
		this.id = UUID.randomUUID().toString();
		this.name = storyFile.getName();
		this.storyFile = storyFile.getAbsolutePath();
		this.moveHistory = new ArrayList<>();
		this.memory = new Memory();
		this.pc = new ProgramCounter();
		this.stack = new Stack();

		List<Byte> data = new ArrayList<>();

		// Read in the story file
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(Files.readAllBytes(storyFile.toPath()));

			while (is.available() > 0) {
				data.add((byte) is.read());
			}
		}
		catch (IOException ex) {
			LOG.error("error", ex);
		}

		this.memory.init(data);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getStoryFile() {
		return storyFile;
	}

	public String getStatusMessage() {
		return "Game Not Started";
	}

	public Integer getGameScore() {
		return 0;
	}

	public Integer getTurnsTaken() {
		return 0;
	}

	@JsonIgnore
	public List<Move> getMoveHistory() {
		return moveHistory;
	}

	@JsonIgnore
	public Memory getMemory() {
		return memory;
	}

	@JsonIgnore
	public ProgramCounter getPc() {
		return pc;
	}

	@JsonIgnore
	public Stack getStack() {
		return stack;
	}

	@JsonIgnore
	public Integer getVersion() {
		return memory.readByte(0x0).intValue();
	}

	@JsonIgnore
	public Integer getRevision() {
		return memory.readWord(0x2);
	}

	@JsonIgnore
	public String getSerialNumber() {
		return memory.readBytes(0x12, 6)
				.stream()
				.map(c -> Character.toString((char) c.byteValue()))
				.collect(Collectors.joining());
	}
}
