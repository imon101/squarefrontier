/*
 * Copyright 2009 all right reserved
 */

package squarefrontier;

/**
 *
 * @author CY
 */
public class Constants {
    public final static String GAME_NAME = "SquareFrontier";
    public final static String GAME_BEGIN_TEXT = "Press any key to start";
    
    //public static String FLY_IMAGE_PATH = "/starcraft.PNG";
    //public static String FLY_IMAGE_PATH = "/Old Plane.PNG";
    public final static String FLY_IMAGE_PATH = "/SmallFish.PNG";
    public final static int FLY_IMAGE_SIZE = 20;

    public final static String BULLET_IMAGE_PATH = "/SmallYellowBall.PNG";
    public final static int BULLET_IMAGE_SIZE = 5;

    public final static String BACKGROUND_IMAGE_PATH = "/background.PNG";
    public final static int BACKGROUND_IMAGE_SIZE = 20; // Image size is 20 * 20
    public final static int BACKGROUND_MAP_SIZE = 12; // Map size is 12 * 12
    public final static int BACKGROUP_MAP[] = {
            0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  3,  0,
            0,  0,  2,  0,  0,  0,  1,  0,  0,  0,  0,  2,
            0,  4,  0,  0,  5,  0,  0,  0,  0,  2,  0,  0,
            0,  0,  0,  0,  4,  0,  0,  3,  0,  0,  0,  0,
            0,  0,  0,  2,  0,  0,  1,  0,  3,  2,  0,  0,
            1,  1,  3,  0,  3,  0,  0,  0,  0,  0,  4,  0,
            0,  0,  0,  0,  0,  0,  4,  0,  4,  0,  0,  0,
            0,  0,  3,  1,  0,  0,  0,  2,  0,  5,  0,  0,
            0,  2,  0,  0,  0,  0,  5,  0,  0,  0,  0,  0,
            0,  0,  0,  4,  0,  0,  3,  2,  0,  0,  0,  4,
            0,  0,  0,  0,  0,  0,  5,  0,  0,  0,  0,  0
        };

    //Text flashing speed is milisecond. The smaller, the quicker.
    public final static int BEGIN_SCREEN_TEXT_FLASHING_SPEED = 500;
    //Game Speed unit is milisecond. The smaller, the game quicker.
    public final static int GAME_SPEED = 50;
    //Bullet moving speed unit is pixel.
    public final static int BULLET_MOVING_SPEED = 2;
    //Bullet batch speed unit is running times.
    public final static int BULLET_BATCH_SPEED = 200;
    //Bullet counts in a batch.
    public final static int BULLET_COUNTS_IN_A_BATCH = 20;
    //Flyer moving speed unit is pixel.
    public final static int FLYER_MOVING_SPEED = 2;

    public final static int SCREEN_WIDTH = 240;
    public final static int SCREEN_HEIGHT = 240;
    public final static int SCREEN_SHADOW = 50;

    private Constants(){
        //Nothing to do
    }
}
