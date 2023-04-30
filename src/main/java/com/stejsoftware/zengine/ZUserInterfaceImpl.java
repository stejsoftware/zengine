/**
 *
 */
package com.stejsoftware.zengine;

import com.zaxsoft.zmachine.Dimension;
import com.zaxsoft.zmachine.Point;
import com.zaxsoft.zmachine.ZUserInterface;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.Vector;

/**
 * @author jon
 */
@Slf4j
public class ZUserInterfaceImpl implements ZUserInterface {
	private static final Scanner scanner = new Scanner(System.in);
	private final Point cursorPosition = new Point(0, 0);
	private int currentWindow;
	private int fgColor;
	private int bgColor;
	private int textStyle;
	private int font;

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#fatal(java.lang.String)
	 */
	@Override
	public void fatal(String errmsg) {
		log.error(errmsg);
		System.exit(-1);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#initialize(int)
	 */
	@Override
	public void initialize(int ver) {
		log.info("Z Story Version {}", ver);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#setTerminatingCharacters(java.util.Vector)
	 */
	@Override
	public void setTerminatingCharacters(Vector<Integer> chars) {
		log.info("setTerminatingCharacters {}", chars);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#hasStatusLine()
	 */
	@Override
	public boolean hasStatusLine() {
		return false;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#hasUpperWindow()
	 */
	@Override
	public boolean hasUpperWindow() {
		return false;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#defaultFontProportional()
	 */
	@Override
	public boolean defaultFontProportional() {
		return false;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#hasColors()
	 */
	@Override
	public boolean hasColors() {
		return true;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#hasBoldface()
	 */
	@Override
	public boolean hasBoldface() {
		return true;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#hasItalic()
	 */
	@Override
	public boolean hasItalic() {
		return true;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#hasFixedWidth()
	 */
	@Override
	public boolean hasFixedWidth() {
		return true;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#hasTimedInput()
	 */
	@Override
	public boolean hasTimedInput() {
		return false;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#getScreenCharacters()
	 */
	@Override
	public Dimension getScreenCharacters() {
		return new Dimension(80, 25);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#getScreenUnits()
	 */
	@Override
	public Dimension getScreenUnits() {
		return new Dimension(1, 1);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#getFontSize()
	 */
	@Override
	public Dimension getFontSize() {
		return new Dimension(5, 7);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#getWindowSize(int)
	 */
	@Override
	public Dimension getWindowSize(int window) {
		return new Dimension(80, 25);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#getDefaultForeground()
	 */
	@Override
	public int getDefaultForeground() {
		return bgColor;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#getDefaultBackground()
	 */
	@Override
	public int getDefaultBackground() {
		return fgColor;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#getCursorPosition()
	 */
	@Override
	public Point getCursorPosition() {
		return cursorPosition;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#showStatusBar(java.lang.String, int,
	 * int, boolean)
	 */
	@Override
	public void showStatusBar(String s, int a, int b, boolean flag) {
		log.info("{} {} {} {}", s, a, b, flag);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#splitScreen(int)
	 */
	@Override
	public void splitScreen(int lines) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#setCurrentWindow(int)
	 */
	@Override
	public void setCurrentWindow(int window) {
		currentWindow = window;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#setCursorPosition(int, int)
	 */
	@Override
	public void setCursorPosition(int x, int y) {
		cursorPosition.x = x;
		cursorPosition.y = y;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#setColor(int, int)
	 */
	@Override
	public void setColor(int fg, int bg) {
		fgColor = fg;
		bgColor = bg;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#setTextStyle(int)
	 */
	@Override
	public void setTextStyle(int style) {
		textStyle = style;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#setFont(int)
	 */
	@Override
	public void setFont(int font) {
		this.font = font;
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#readLine(java.lang.StringBuffer,
	 * int)
	 */
	@Override
	public int readLine(StringBuffer sb, int time) {
		log.info("readLine(): Timout:{}", time);

		sb.append(scanner.nextLine());

		return '\r';
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#readChar(int)
	 */
	@Override
	public int readChar(int time) {
		log.info("readChar(): Timout:{}", time);

		return scanner.nextByte();
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#showString(java.lang.String)
	 */
	@Override
	public void showString(String s) {
		System.out.print(s);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#scrollWindow(int)
	 */
	@Override
	public void scrollWindow(int lines) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#eraseLine(int)
	 */
	@Override
	public void eraseLine(int s) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#eraseWindow(int)
	 */
	@Override
	public void eraseWindow(int window) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#getFilename(java.lang.String,
	 * java.lang.String, boolean)
	 */
	@Override
	public String getFilename(String title, String suggested, boolean saveFlag) {
		log.info("getFilename: [{}] [{}] [save:{}]", title, suggested, saveFlag);

		return "save.dat";
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#quit()
	 */
	@Override
	public void quit() {
		log.info("Quiting...");
		System.exit(0);
	}

	/**
	 * @see com.zaxsoft.zmachine.ZUserInterface#restart()
	 */
	@Override
	public void restart() {
		log.info("Restarting...");
	}
}
