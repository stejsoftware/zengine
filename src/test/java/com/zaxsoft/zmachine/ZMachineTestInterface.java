package com.zaxsoft.zmachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

public abstract class ZMachineTestInterface implements ZUserInterface {
    private static final Logger LOG = LoggerFactory.getLogger(ZMachineTestInterface.class);

    @Override
    public void fatal(String errorMessage) {
        throw new ZException(errorMessage);
    }

    @Override
    public void initialize(int ver) {

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
        return null;
    }

    @Override
    public Dimension getScreenUnits() {
        return null;
    }

    @Override
    public Dimension getFontSize() {
        return null;
    }

    @Override
    public Dimension getWindowSize(int window) {
        return null;
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
        return null;
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
    public void restart() {

    }
}
