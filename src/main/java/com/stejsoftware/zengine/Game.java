package com.stejsoftware.zengine;

import com.zaxsoft.zmachine.ZCPU;
import com.zaxsoft.zmachine.ZUserInterface;

import java.io.File;
import java.util.UUID;

public class Game {
    private final File story;
    private final String id;
    private final ZUserInterface ui;
    private final ZCPU cpu;

    public Game(File story) {
        this.story = story;
        this.id = UUID.randomUUID().toString();

        this.ui = new ZUserInterfaceImpl();
        this.cpu = new ZCPU(this.ui);
        this.cpu.initialize(this.story.getAbsolutePath());
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
}
