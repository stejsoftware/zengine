package com.zaxsoft;

import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import com.zaxsoft.zmachine.Dimension;
import com.zaxsoft.zmachine.Point;
import com.zaxsoft.zmachine.ZCPU;
import com.zaxsoft.zmachine.ZUserInterface;

import ml.options.Options;
import ml.options.Options.Multiplicity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Zmachine {

    private final UI ui = new UI();

    private final ZCPU cpu = new ZCPU(ui);

    private static final Logger log = LoggerFactory.getLogger(Zmachine.class);

    Zmachine(final File story) {
        if (story.canRead()) {
            cpu.initialize(story.getPath());
        }
    }

    private void start() {
        log.debug(">>>> Start <<<<");

        cpu.start();
        // cpu.run();
    }

    public static void main(final String[] args) {
        try {
            final Options opt = new Options(args, 1);

            opt.getSet().addOption("h", Multiplicity.ZERO_OR_ONE);

            if (!opt.check()) {
                System.err.println("Story Needed");
                System.exit(1);
            }

            final String story_filename = opt.getSet().getData().get(0);
            final File story = new File(story_filename);

            if (story.exists()) {
                new Zmachine(story).start();
            }
            else {
                System.err.printf("File not found: %s\n", story_filename);
                System.exit(1);
            }
        }
        catch (final IllegalArgumentException ex) {
            System.err.printf("%s\n", ex.getMessage());
            System.err.println("Story Needed");
            System.exit(1);
        }
    }

    class UI implements ZUserInterface {
        Scanner scanner = new Scanner(System.in);

        @Override
        public void fatal(final String error) {
            log.error(error);
            System.exit(1);
        }

        @Override
        public void initialize(final int ver) {
            log.info("Ver: {}", ver);
        }

        @Override
        public void setTerminatingCharacters(final Vector<Integer> chars) {

        }

        @Override
        public boolean hasStatusLine() {
            return false;
        }

        @Override
        public boolean hasUpperWindow() {
            return false;
        }

        @Override
        public boolean defaultFontProportional() {
            return false;
        }

        @Override
        public boolean hasColors() {
            return false;
        }

        @Override
        public boolean hasBoldface() {
            return false;
        }

        @Override
        public boolean hasItalic() {
            return false;
        }

        @Override
        public boolean hasFixedWidth() {
            return false;
        }

        @Override
        public boolean hasTimedInput() {
            return false;
        }

        @Override
        public Dimension getScreenCharacters() {
            return new Dimension(80, 25);
        }

        @Override
        public Dimension getScreenUnits() {
            return new Dimension(640, 480);
        }

        @Override
        public Dimension getFontSize() {
            return new Dimension(5, 7);
        }

        @Override
        public Dimension getWindowSize(final int window) {
            return new Dimension(80, 25);
        }

        @Override
        public int getDefaultForeground() {
            return 0;
        }

        @Override
        public int getDefaultBackground() {
            return 0;
        }

        @Override
        public Point getCursorPosition() {
            return new Point(1, 1);
        }

        @Override
        public void showStatusBar(final String s, final int a, final int b, final boolean flag) {
            System.out.printf("[%s %d:%d]\n", s, a, b);
        }

        @Override
        public void splitScreen(final int lines) {

        }

        @Override
        public void setCurrentWindow(final int window) {

        }

        @Override
        public void setCursorPosition(final int x, final int y) {

        }

        @Override
        public void setColor(final int fg, final int bg) {

        }

        @Override
        public void setTextStyle(final int style) {

        }

        @Override
        public void setFont(final int font) {

        }

        @Override
        public int readLine(final StringBuffer sb, final int time) {
            sb.append(scanner.nextLine());
            scanner.delimiter();
            return 0;
        }

        @Override
        public int readChar(final int time) {
            return scanner.nextByte();
        }

        @Override
        public void showString(final String s) {
            System.out.print(s);
        }

        @Override
        public void scrollWindow(final int lines) {

        }

        @Override
        public void eraseLine(final int s) {

        }

        @Override
        public void eraseWindow(final int window) {

        }

        @Override
        public String getFilename(final String title, final String suggested, final boolean saveFlag) {
            log.debug("getFilename('{}' '{}' '{}')", title, suggested, saveFlag);

            return "file.dat";
        }

        @Override
        public void quit() {
            System.out.printf("[done]\n");
            System.exit(0);
        }

        @Override
        public void restart() {

        }

    }
}
