/*
 * Copyright 2009 all right reserved
 */

package viva;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

/**
 *
 * @author CY
 */
public class FlySprite extends Sprite{
    private boolean _Dead;
    private int _MoveStep;

    public FlySprite(Image image, int frameWidth, int frameHeight) {
        super(image, frameWidth, frameHeight);
        defineReferencePixel(frameWidth >> 1, frameHeight >> 1);
        _MoveStep = Constants.FLYER_MOVING_SPEED;
        _Dead = false;
    }

    public void moveUp(){
        moveOffset(0, -_MoveStep);
    }

    public void moveDown(){
        moveOffset(0, _MoveStep);
    }

    public void moveLeft(){
        setFrame(1);
        moveOffset(-_MoveStep, 0);
    }

    public void moveRight(){
        setFrame(0);
        moveOffset(_MoveStep, 0);
    }
    
    public void beKilled(){
        _Dead = true;
    }
    
    public boolean isAlive(){
        return !_Dead;
    }

    public void reset(){
        _Dead = false;
    }
    
    private void moveOffset(int offsetX, int offsetY){
        int x = getX();
        int y = getY();
        setPosition(x + offsetX, y + offsetY);
    }
}
