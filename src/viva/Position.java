/*
 * Copyright 2009 all right reserved
 */

package viva;

/**
 *
 * @author CY
 */
public class Position {
    public int X;
    public int Y;
    
    public Position(int x, int y){
        X=x;
        Y=y;
    }

    public int getDistance(Position target){
        return (int)sqrt((target.X - X)*(target.X - X) + (target.Y -Y)*(target.Y -Y));
    }
    
    private long sqrt(long x){
        long y = 0;
        long b = (~Long.MAX_VALUE) >>> 1;
        while (b > 0) {
            if (x >= y + b) {
                x -= y + b;
                y >>= 1;
                y += b;
            } else {
                y >>= 1;
            }
            b >>= 2;
        }
        return y;
    }
}
