package com.stejsoftware.zengine.controller;

import java.util.Arrays;
import java.util.Vector;

import com.zaxsoft.zmachine.Dimension;
import com.zaxsoft.zmachine.Point;
import com.zaxsoft.zmachine.ZUserInterface;

import io.socket.client.Ack;
import io.socket.spring.Socket;
import io.socket.spring.annotation.Of;
import io.socket.spring.annotation.OnConnect;
import io.socket.spring.annotation.OnEvent;

@Of("/zvm")
public class ZioController implements ZUserInterface {

    private final Socket socket;

    private ZioController(Socket socket) {
        this.socket = socket;
    }

    @OnConnect
    public static ZioController connect(Socket socket) {
        return new ZioController(socket);
    }

    @OnEvent("list")
    public void getStoryList(Ack ack) {
        if (ack != null) {
            ack.call(Arrays.asList("zork", "zorkII", "zorkIII"));
        }
    }

    @Override
    public void fatal(String errmsg) {
        socket.emit("fatal", errmsg);
    }

    @Override
    public void initialize(int ver) {
        socket.emit("initialize", ver);
    }

    @Override
    public void setTerminatingCharacters(Vector<Integer> chars) {
        socket.emit("setTerminatingCharacters", chars);
    }

    @Override
    public boolean hasStatusLine() {
        final boolean statusline = false;
        return statusline;
    }

    @Override
    public boolean hasUpperWindow() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean defaultFontProportional() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasColors() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasBoldface() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasItalic() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasFixedWidth() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasTimedInput() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Dimension getScreenCharacters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dimension getScreenUnits() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dimension getFontSize() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dimension getWindowSize(int window) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getDefaultForeground() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getDefaultBackground() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Point getCursorPosition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void showStatusBar(String s, int a, int b, boolean flag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void splitScreen(int lines) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCurrentWindow(int window) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCursorPosition(int x, int y) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setColor(int fg, int bg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTextStyle(int style) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFont(int font) {
        // TODO Auto-generated method stub

    }

    @Override
    public int readLine(StringBuffer sb, int time) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int readChar(int time) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void showString(String s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void scrollWindow(int lines) {
        // TODO Auto-generated method stub

    }

    @Override
    public void eraseLine(int s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void eraseWindow(int window) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getFilename(String title, String suggested, boolean saveFlag) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void quit() {
        // TODO Auto-generated method stub

    }

    @Override
    public void restart() {
        // TODO Auto-generated method stub

    }

}