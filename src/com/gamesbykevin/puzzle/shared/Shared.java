package com.gamesbykevin.puzzle.shared;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * This Shared class will have shared objects
 * 
 * @author GOD
 */
public class Shared 
{
    public static final int WINDOW_WIDTH  = 1000;
    public static final int WINDOW_HEIGHT = 600;
    
    public static final int DEFAULT_UPS = 60;
    
    public static final Cursor CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
}
