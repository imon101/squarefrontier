/*
 * Copyright 2009 all right reserved
 */

package squarefrontier;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

/**
 *
 * @author CY
 */
public class BeginCanvas extends GameCanvas implements Runnable{
    private sfMidlet _master;
    private boolean _living;
    private boolean _showtext;

    public BeginCanvas(sfMidlet master){
        super(true);
        _master = master;
        _living = true;
        _showtext = true;
    }

    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        Graphics g = getGraphics();

        while (_living) {
            long start = System.currentTimeMillis();

            //Check key pressing status
            int keyStates = getKeyStates();
            if (keyStates != 0) {
                _living = false;
            }

            //Render the screen
            clearScreen(g);
            showGamename(g);
            _showtext = !_showtext;
            if(_showtext)showText(g);
            flushGraphics();

            //next step timing
            long end = System.currentTimeMillis();
            int duration = (int)(end - start);
            if (duration < Constants.BEGIN_SCREEN_TEXT_FLASHING_SPEED) {
                try {
                    Thread.sleep(Constants.BEGIN_SCREEN_TEXT_FLASHING_SPEED - duration);
                }
                catch (InterruptedException ie) {
                    _living = false;
                }
            }
        }
        
        //Call back to master
        _master.startMainCanvas();
    }

    private void clearScreen(Graphics g){
        g.setColor(0x000000);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void showGamename(Graphics g){
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, 
                Font.SIZE_LARGE);
        g.setFont(font);
        int sw = font.stringWidth(Constants.GAME_NAME) + 2;
        int sh = font.getHeight();
        g.setColor(0xffffff);
        g.drawString(Constants.GAME_NAME,
                (getWidth() - sw)/2+1,
                (getHeight() - sh)/2,
                Graphics.LEFT | Graphics.TOP);
    }
    
    private void showText(Graphics g){
        Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN,
                Font.SIZE_SMALL);
        g.setFont(font);
        int sw = font.stringWidth(Constants.GAME_BEGIN_TEXT) + 2;
        int sh = font.getHeight();
        g.setColor(0xffffff);
        g.drawString(Constants.GAME_BEGIN_TEXT,
                (getWidth() - sw)/2+1,
                getHeight(),
                Graphics.LEFT | Graphics.BOTTOM);
    }
}
