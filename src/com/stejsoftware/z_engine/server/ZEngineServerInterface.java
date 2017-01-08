/**
 * 
 */
package com.stejsoftware.z_engine.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stejsoftware.z_engine.server.zmachine.Dimension;
import com.stejsoftware.z_engine.server.zmachine.Point;
import com.stejsoftware.z_engine.server.zmachine.ZUserInterface;
import com.stejsoftware.z_engine.shared.msg.*;

/**
 * @author jon
 * 
 */
public class ZEngineServerInterface implements ZUserInterface
{
    private final Logger   log             = Logger.getLogger(ZEngineServiceImpl.class.getName());

    private Queue<Message> to_client       = new LinkedList<Message>();
    private Queue<Message> from_server     = new LinkedList<Message>();
    private Point          cursor_position = new Point(0, 0);
    private int            text_fg         = 0;
    private int            text_bg         = 0;
    private int            text_style      = 0;
    private int            text_font       = 0;

    /**
     * puts a message on the client message queue; messages from the client
     * 
     * @param from_client
     */
    public void AddMessage(Message from_client)
    {
        to_client.offer(from_client);
    }

    /**
     * Get the messages to be sent to the client
     * 
     * @return
     */
    public List<Message> GetMessages()
    {
        List<Message> messages = new ArrayList<Message>();
        Message message = null;

        while( (message = from_server.poll()) != null )
        {
            messages.add(message);
        }

        return messages;
    }

    @Override
    public void fatal(String errmsg)
    {
        log.log(Level.SEVERE, errmsg);
        System.exit(-1);
    }

    @Override
    public void initialize(int ver)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void setTerminatingCharacters(Vector<Integer> chars)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean hasStatusLine()
    {
        return true;
    }

    @Override
    public boolean hasUpperWindow()
    {
        return false;
    }

    @Override
    public boolean defaultFontProportional()
    {
        return false;
    }

    @Override
    public boolean hasColors()
    {
        return false;
    }

    @Override
    public boolean hasBoldface()
    {
        return false;
    }

    @Override
    public boolean hasItalic()
    {
        return false;
    }

    @Override
    public boolean hasFixedWidth()
    {
        return true;
    }

    @Override
    public boolean hasTimedInput()
    {
        return false;
    }

    @Override
    public Dimension getScreenCharacters()
    {
        return new Dimension(80, 25);
    }

    @Override
    public Dimension getScreenUnits()
    {
        return new Dimension(320, 240);
    }

    @Override
    public Dimension getFontSize()
    {
        return new Dimension(1, 2);
    }

    @Override
    public Dimension getWindowSize(int window)
    {
        return new Dimension(0, 0);
    }

    @Override
    public int getDefaultForeground()
    {
        return text_fg;
    }

    @Override
    public int getDefaultBackground()
    {
        return text_bg;
    }

    @Override
    public Point getCursorPosition()
    {
        return cursor_position;
    }

    @Override
    public void showStatusBar(String location, int a, int b, boolean flag)
    {
        to_client.add(new Status(location, a, b));
    }

    @Override
    public void splitScreen(int lines)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void setCurrentWindow(int window)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void setCursorPosition(int x, int y)
    {
        cursor_position = new Point(x, y);
    }

    @Override
    public void setColor(int fg, int bg)
    {
        text_fg = fg;
        text_bg = bg;
    }

    @Override
    public void setTextStyle(int style)
    {
        text_style = style;
    }

    @Override
    public void setFont(int font)
    {
        text_font = font;
    }

    @Override
    public int readLine(StringBuffer sb, int time)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int readChar(int time)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void showString(String text)
    {
        to_client.add(new Text(text, text_fg, text_bg, text_style, text_font));
    }

    @Override
    public void scrollWindow(int lines)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void eraseLine(int line)
    {
        to_client.add(Erase.Line(line));
    }

    @Override
    public void eraseWindow(int window)
    {
        to_client.add(Erase.Window(window));
    }

    @Override
    public String getFilename(String title, String suggested, boolean saveFlag)
    {
        // TODO Auto-generated method stub
        return suggested;
    }

    @Override
    public void quit()
    {
        to_client.add(new Quit());
    }

    @Override
    public void restart()
    {
        // TODO Auto-generated method stub
    }
}
