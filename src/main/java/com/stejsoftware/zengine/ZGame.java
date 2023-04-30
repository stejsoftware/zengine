package com.stejsoftware.zengine;

import com.stejsoftware.zengine.data.Game;
import com.stejsoftware.zengine.model.Move;
import com.stejsoftware.zengine.zmachine.Memory;
import com.stejsoftware.zengine.zmachine.ProgramCounter;
import com.stejsoftware.zengine.zmachine.Stack;
import com.zaxsoft.zmachine.ZCPU;
import com.zaxsoft.zmachine.ZUserInterface;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class ZGame {
    private final String id;
    private final ZUserInterface ui;
    private final ZCPU cpu;
    private final List<Move> moveHistory;
    private final Memory memory;
    private final ProgramCounter pc;
    private final Stack stack;

    private File story;

    private ZGame() {
        this.id = UUID.randomUUID().toString();

        this.ui = new ZUserInterfaceImpl();
        this.cpu = new ZCPU(this.ui);

        this.moveHistory = new ArrayList<>();
        this.memory = new Memory();
        this.pc = new ProgramCounter();
        this.stack = new Stack();
    }

    public static ZGame init(File storyFile) {
        ZGame game = new ZGame();

        game.story = storyFile;
        game.cpu.initialize(game.story.getAbsolutePath());

        return game;
    }

    public Game toGame() {
        return Game.createGame(this.id, this.story);
    }

    public Move start() {
        if (!cpu.is_running()) {
            cpu.start();
        }

        return Move.builder().build();
    }

    public String getId() {
        return id;
    }

    public File getStory() {
        return story;
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

    public Move addMove(String input) {
        return Move.builder().input(input).build();
    }

    public Move getMove(String moveId) {
        return Move.builder().build();
    }
}
