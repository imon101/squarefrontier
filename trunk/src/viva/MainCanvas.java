/*
 * Copyright 2009 all right reserved
 */

package viva;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import java.util.Enumeration;

import common.NokiaS40;

/**
 *
 * @author CY
 */
public class MainCanvas extends GameCanvas implements Runnable {
    private VivaMidlet _master;
    private Command _replayCommand;
    private boolean _living;
    private FlySprite _fly;
    private TiledLayer _backgroud;
    private LayerManager _layerManager;
    private Vector _bullets; // bullets collection
    private Image _bulletImage;
    private Random _random;
    private int _bulletBatches; // bullets batches internal counter
    private int _runTimes; // time recorder

    MainCanvas(VivaMidlet master) throws IOException {
        super(true);
        _master=master;
        _living=false;
        _fly = createFly();
        _backgroud = createBoard();
        _bulletImage = Image.createImage(Constants.BULLET_IMAGE_PATH);
    }

    public void start() {
        removeCommand(_replayCommand);
        _layerManager = new LayerManager();
        _layerManager.append(_backgroud);
        _layerManager.insert(_fly,0);
        _layerManager.setViewWindow(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        _living = true;
        _bulletBatches = 0;
        _bullets = new Vector();
        _random = new Random();
        _fly.reset();
        _fly.setPosition((Constants.SCREEN_WIDTH-Constants.FLY_IMAGE_SIZE)/2,
            (Constants.SCREEN_HEIGHT-Constants.FLY_IMAGE_SIZE)/2);
        Thread t = new Thread(this);
        t.start();
    }
    
    public void stop() {
        _living = false;
    }
    
    public void resume(){
        _living = true;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        Graphics g = getGraphics();
        _runTimes =0;

        while (_living) {
            long start = System.currentTimeMillis();
       
            //Check if bullet hit the flyer
            checkBulletHitFlyer();

            if(_fly.isAlive()){
                _runTimes++;
                //Manage the bullets actions
                tickBullets();
                //check user's inputing
                checkInput();
                //To set the backgrond to lowest Z-Order
                _layerManager.append(_backgroud);
                //render the graphic
                render(g);
            }else{
                _living =false;
                showTextInMiddle(g);
                showExitCommand(g);
                showReplayCommand(g);
                //_replayCommand = new Command("Replay", Command.OK, 0);
                //addCommand(_replayCommand);
                flushGraphics();
            }
           
            //next step timing
            long end = System.currentTimeMillis();
            int duration = (int)(end - start);
            if (duration < Constants.GAME_SPEED) {
                try {
                    Thread.sleep(Constants.GAME_SPEED - duration);
                }
                catch (InterruptedException ie) {
                    stop();
                }
            }
        }
    }

    //Only handle the non-j2me pre-defined key-pressed
    protected void keyPressed(int keyCode){
        if(!_living&&keyCode==NokiaS40.RIGHT_SOFT_KEY_CODE){
            _master.exitApp();
        }else if(!_living&&keyCode==NokiaS40.LEFT_SOFT_KEY_CODE){
            _master.replayMainCanvas();
        }
    }

    private void checkBulletHitFlyer(){
        for(Enumeration en=_bullets.elements();en.hasMoreElements();){
            BulletSprite bs = (BulletSprite)en.nextElement();
            if(bs.collidesWith(_fly, false)){
                if(bs.collidesWith(_fly, true))_fly.beKilled();
            }
        }
    }

    private void tickBullets(){
        //Check the bullet if need to be deleted
        for(Enumeration en=_bullets.elements();en.hasMoreElements();){
            BulletSprite bs = (BulletSprite)en.nextElement();
            if(bs.Move()==false){
                _layerManager.remove(bs);
                _bullets.removeElement(bs);
            }
        }

        //Create batch bullets
        if(0==_bulletBatches){
            createBatchBullets();
        }
        
        _bulletBatches++;
        if(_bulletBatches>Constants.BULLET_BATCH_SPEED)_bulletBatches=0;
    }

    private void checkInput(){
        int offset = Constants.FLY_IMAGE_SIZE>>1;
        int keyStates = getKeyStates();
        if ((keyStates & LEFT_PRESSED) != 0) {
            if(_fly.getRefPixelX()>offset)
                _fly.moveLeft();
        }
        else if ((keyStates & RIGHT_PRESSED) != 0) {
            if(_fly.getRefPixelX()<Constants.SCREEN_WIDTH-offset)
                _fly.moveRight();
        }
        else if ((keyStates & UP_PRESSED) != 0) {
            if(_fly.getRefPixelY()>offset)
                _fly.moveUp();
        }
        else if ((keyStates & DOWN_PRESSED) != 0) {
            if(_fly.getRefPixelY()<Constants.SCREEN_HEIGHT-offset)
                _fly.moveDown();
        }
    }

    private void render(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        
        //full screen fill in black color
        g.setColor(0x000000);
        g.fillRect(0, 0, w, h);

        //The physical screen may be bigger than the game screen size.
        //the layers postion (0,0) are set starting from physical screen (x,y)
        //int x = (w - Constants.SCREEN_WIDTH) / 2;
        int y = (h - Constants.SCREEN_HEIGHT) / 2;
        _layerManager.paint(g, 0, y);

        g.setColor(0x202040);
        g.drawLine(0, y,
                Constants.SCREEN_WIDTH, y);
        g.drawLine(0, Constants.SCREEN_HEIGHT + y,
                Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT + y);
        
        //DEBUG
        //showDebugMsg(g);
                
        flushGraphics();
        //flushGraphics(x, y, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    }

    private Position newShadowPosition(int paramA){
        int x; int y;
        x=0; y=0;
        
        if(1==paramA){
            x = _random.nextInt(Constants.SCREEN_WIDTH);
            y = 0 - _random.nextInt(Constants.SCREEN_SHADOW);
        }
        if(2==paramA){
            x = 0 - _random.nextInt(Constants.SCREEN_SHADOW);
            y = _random.nextInt(Constants.SCREEN_HEIGHT);
        }
        if(3==paramA){
            x = Constants.SCREEN_WIDTH + _random.nextInt(Constants.SCREEN_SHADOW);
            y = _random.nextInt(Constants.SCREEN_HEIGHT);
        }
        if(4==paramA){
            x = _random.nextInt(Constants.SCREEN_WIDTH);
            y = Constants.SCREEN_HEIGHT + _random.nextInt(Constants.SCREEN_SHADOW);
        }
        return new Position(x,y);
    }

    private void createBatchBullets(){
        for(int i=1;i<5;i++){
            for(int ii=1;ii<=Constants.BULLET_COUNTS_IN_A_BATCH;ii++){
                BulletSprite bs = new BulletSprite(
                    _bulletImage,
                    Constants.BULLET_IMAGE_SIZE,
                    Constants.BULLET_IMAGE_SIZE
                    );
                bs.init(
                    Constants.BULLET_MOVING_SPEED,
                    newShadowPosition(i),
                    newShadowPosition(5-i)
                    );
                _bullets.addElement(bs);
                _layerManager.append(bs);
            }
        }
    }

    private TiledLayer createBoard() throws IOException {
        Image image = Image.createImage(Constants.BACKGROUND_IMAGE_PATH);
        TiledLayer tiledLayer = new TiledLayer(
                Constants.BACKGROUND_MAP_SIZE,
                Constants.BACKGROUND_MAP_SIZE,
                image,
                Constants.BACKGROUND_IMAGE_SIZE,
                Constants.BACKGROUND_IMAGE_SIZE
                );
    
        for (int i = 0; i < Constants.BACKGROUP_MAP.length; i++) {
            int column = i % Constants.BACKGROUND_MAP_SIZE;
            int row = (i - column) / Constants.BACKGROUND_MAP_SIZE;
            tiledLayer.setCell(column, row, Constants.BACKGROUP_MAP[i]);
        }

        return tiledLayer;
    }

    private FlySprite createFly() throws IOException{
        Image image = Image.createImage(Constants.FLY_IMAGE_PATH);
        return new FlySprite(
                image,
                Constants.FLY_IMAGE_SIZE,
                Constants.FLY_IMAGE_SIZE
                );
    }

    private void showDebugMsg(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        StringBuffer sb = new StringBuffer();
        sb.append(Integer.toString(w));
        sb.append(':');
        sb.append(Integer.toString(h));
        sb.append("  ");
        sb.append(Integer.toString(_layerManager.getSize()));
        String s = sb.toString();

        Font font = g.getFont();
        int sw = font.stringWidth(s) + 2;
        int sh = font.getHeight() + 2;

        // Draw the render capacity.
        //g.setColor(0x000000);
        //g.fillRect(0, Constants.SCREEN_HEIGHT, sw, sh);
        g.setColor(0xffffff);
        g.drawRect(0, Constants.SCREEN_HEIGHT+2, sw, sh);
        g.drawString(s, 0, Constants.SCREEN_HEIGHT+3, Graphics.LEFT | Graphics.TOP);
    }

    private void showTextInMiddle(Graphics g) {
        int w = getWidth();
        int h = getHeight();

        int i = _runTimes * Constants.GAME_SPEED / 1000;
        StringBuffer sb = new StringBuffer();
        sb.append("Scores: ");
        sb.append(Integer.toString(i));
        sb.append(" seconds");
        String s = sb.toString();
        
        Font font = g.getFont();
        int sw = font.stringWidth(s) + 2;
        int sh = font.getHeight();

        g.setColor(0x000000);
        g.fillRect((w - sw)/2, (h - sh)/2, sw, sh);
        g.setColor(0xffffff);
        g.drawRect((w - sw)/2, (h - sh)/2, sw, sh);
        g.drawString(s, (w - sw)/2+1, (h - sh)/2, Graphics.LEFT | Graphics.TOP);
    }

    private void showExitCommand(Graphics g){
        int w = getWidth();
        int h = getHeight();

        String s = "Exit";

        //Font font = g.getFont();
        //int sw = font.stringWidth(s) + 2;
        //int sh = font.getHeight();

        g.setColor(0xffffff);
        g.drawString(s, w, h, Graphics.RIGHT | Graphics.BOTTOM);
    }

    private void showReplayCommand(Graphics g){
        int w = getWidth();
        int h = getHeight();

        String s = "Replay";

        //Font font = g.getFont();
        //int sw = font.stringWidth(s) + 2;
        //int sh = font.getHeight();

        g.setColor(0xffffff);
        g.drawString(s, 0, h, Graphics.LEFT | Graphics.BOTTOM);
    }
}
