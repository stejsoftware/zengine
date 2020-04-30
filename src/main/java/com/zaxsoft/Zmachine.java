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

public class Zmachine {

    private final UI ui = new UI();

    private final ZCPU cpu = new ZCPU(ui);

    Zmachine(File story) {
        if (story.canRead()) {
            cpu.initialize(story.getPath());
        }
    }

    private void start() {
        System.out.println(">>>> Start <<<<");

        cpu.start();
        // cpu.run();

        System.out.println(">>>> Stop <<<<");
    }

    public static void main(String[] args) {
        try {
            Options opt = new Options(args, 1);

            opt.getSet().addOption("h", Multiplicity.ZERO_OR_ONE);

            if (!opt.check()) {
                System.err.println("Story Needed");
                System.exit(1);
            }

            String story_filename = opt.getSet().getData().get(0);
            File story = new File(story_filename);

            if (story.exists()) {
                new Zmachine(story).start();
            }
            else {
                System.err.printf("File not found: %s\n", story_filename);
                System.exit(1);
            }
        }
        catch (IllegalArgumentException ex) {
            System.err.printf("%s\n", ex.getMessage());
            System.err.println("Story Needed");
            System.exit(1);
        }
    }

    class UI implements ZUserInterface {
        Scanner scanner = new Scanner(System.in);

        @Override
        public void fatal(String error) {
            System.err.println(error);
            System.exit(-1);
        }

        @Override
        public void initialize(int ver) {
            System.out.printf("Ver: %s\n", ver);
        }

        @Override
        public void setTerminatingCharacters(Vector<Integer> chars) {

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
        public Dimension getWindowSize(int window) {
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
        public void showStatusBar(String s, int a, int b, boolean flag) {
            System.out.printf("[%s %d:%d]\n", s, a, b);
        }

        @Override
        public void splitScreen(int lines) {

        }

        @Override
        public void setCurrentWindow(int window) {

        }

        @Override
        public void setCursorPosition(int x, int y) {

        }

        @Override
        public void setColor(int fg, int bg) {

        }

        @Override
        public void setTextStyle(int style) {

        }

        @Override
        public void setFont(int font) {

        }

        @Override
        public int readLine(StringBuffer sb, int time) {
            sb.append(scanner.nextLine());
            scanner.delimiter();
            return 0;
        }

        @Override
        public int readChar(int time) {
            return scanner.nextByte();
        }

        @Override
        public void showString(String s) {
            System.out.print(s);
        }

        @Override
        public void scrollWindow(int lines) {

        }

        @Override
        public void eraseLine(int s) {

        }

        @Override
        public void eraseWindow(int window) {

        }

        @Override
        public String getFilename(String title, String suggested, boolean saveFlag) {
            return null;
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
