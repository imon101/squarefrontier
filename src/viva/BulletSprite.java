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
public class BulletSprite extends Sprite{
    private int _StepDistance;
    private Position _start;
    private Position _target;
    private int _movementOffsetX;
    private int _movementOffsetY;

    public BulletSprite(Image image, int frameWidth, int frameHeight) {
        super(image, frameWidth, frameHeight);
        defineReferencePixel(frameWidth / 2, frameHeight / 2);
    }
    
    public void init(int stepDistance, Position start, Position target){
        _StepDistance = stepDistance;
        _start=start;
        _target=target;
        setPosition(start.X, start.Y);
        calculateMovementOffset();
    }

    public boolean Move(){
        if(isFinish())return false;
        setPosition(getX()+_movementOffsetX, getY()+_movementOffsetY);
        return true;
    }

    private boolean isFinish(){
        boolean resultX;
        boolean resultY;
        
        resultX= false;
        int x = getX();
        if(_movementOffsetX > 0){
            if(x >= _target.X) resultX=true;
        }
        if(_movementOffsetX < 0){
            if(x <= _target.X) resultX=true;
        }
        if(_movementOffsetX == 0){
            resultX=true;
        }

        resultY= false;
        int y = getY();
        if(_movementOffsetY >= 0){
            if(y >= _target.Y) resultY=true;
        }
        if(_movementOffsetY < 0){
            if(y <= _target.Y) resultY=true;
        }
        if(_movementOffsetY == 0){
            resultY=true;
        }

        return resultX && resultY;
    }

    private void calculateMovementOffset(){
        int targetDistance;
        targetDistance = _start.getDistance(_target);
        _movementOffsetX = (int)((_target.X - _start.X) * _StepDistance / targetDistance);
        _movementOffsetY = (int)((_target.Y - _start.Y) * _StepDistance / targetDistance);
    }
}
