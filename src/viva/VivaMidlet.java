/*
 * Copyright 2009 all right reserved
 */

package viva;

import java.io.IOException;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/**
 * @author CY
 */
public class VivaMidlet extends MIDlet implements CommandListener{
    private MainCanvas _mainCanvas;

    public void startApp() {
        if (_mainCanvas == null) {
            BeginCanvas beginCanvas = new BeginCanvas(this);
            beginCanvas.setFullScreenMode(true);
            beginCanvas.start();
            Display.getDisplay(this).setCurrent(beginCanvas);
        }else {
            _mainCanvas.resume();
            Display.getDisplay(this).setCurrent(_mainCanvas);
        }
    }

    public void pauseApp() {
        if (_mainCanvas != null)_mainCanvas.stop();
    }

    public void destroyApp(boolean unconditional) {
        if (_mainCanvas != null) _mainCanvas.stop();
    }
    
    public void commandAction(Command c, Displayable s) {
        if (c.getCommandType() == Command.EXIT) {
            exitApp();
        }
        if (c.getCommandType() == Command.OK) {
            replayMainCanvas();
        }
    }

    //A callback method for start the MainCanvas
    public void startMainCanvas(){
        try {
            _mainCanvas = new MainCanvas(this);
            _mainCanvas.setFullScreenMode(true);
            _mainCanvas.start();
            //Command exitCommand = new Command("Exit", Command.EXIT, 0);
            //_mainCanvas.addCommand(exitCommand);
            //_mainCanvas.setCommandListener(this);
        }catch (IOException ioe) {
            System.out.println(ioe);
        }
        Display.getDisplay(this).setCurrent(_mainCanvas);
    }
    
    public void exitApp(){
        destroyApp(true);
        notifyDestroyed();
    }

    public void replayMainCanvas(){
        _mainCanvas.stop();
        _mainCanvas.start();
    }
}
