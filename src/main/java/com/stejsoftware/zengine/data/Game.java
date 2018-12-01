package com.stejsoftware.zengine.data;

import com.stejsoftware.zengine.processor.Memory;
import com.stejsoftware.zengine.processor.Move;
import com.stejsoftware.zengine.processor.ProgramCounter;
import com.stejsoftware.zengine.processor.Stack;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "Games")
public class Game {
    @Id
    private Integer id;
    private String name;
    private String storyFile;
    private List<Move> moveHistory;

    private Memory memory;
    private ProgramCounter pc;
    private Stack stack;

    public Game() {
        this.id = 0;
        this.name = "";
        this.storyFile = "";
        this.moveHistory = new ArrayList<>();

        this.memory = new Memory();
        this.pc = new ProgramCounter();
        this.stack = new Stack();
    }

    public static Game fromStoryFile(String storyFile) {
        Game game = new Game();
        game.loadStoryFile(storyFile);
        return game;
    }

    private void loadStoryFile(String storyFile) {
        this.storyFile = storyFile;

        List<Byte> data = new ArrayList<>();

        // Read in the story file
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(Files.readAllBytes(Paths.get(storyFile)));

            while (is.available() > 0) {
                data.add(Byte.valueOf((byte) is.read()));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.memory.init(data);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStoryFile() {
        return storyFile;
    }

    public List<Move> getMoveHistory() {
        return moveHistory;
    }

    public Memory getMemory() {
        return memory;
    }

    public ProgramCounter getPc() {
        return pc;
    }

    public Stack getStack() {
        return stack;
    }

    public Integer getGameScore() {
        return 0;
    }

    public String getStatusMessage() {
        return "Game Not Started";
    }

    public Integer getTurnsTaken() {
        return 0;
    }

    public Integer getVersion() {
        return memory.readByte(0x0).intValue();
    }

    public Integer getRevision() {
        return memory.readWord(0x2);
    }

    public String getSerialNumber() {
        return memory.readBytes(0x12, 6)
                .stream()
                .map(c -> Character.toString((char) c.byteValue()))
                .collect(Collectors.joining());
    }
}
