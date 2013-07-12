package com.gamesbykevin.puzzle.shapes;

import com.gamesbykevin.puzzle.board.Piece;
import com.gamesbykevin.puzzle.board.PuzzlePiece;
import java.awt.Polygon;
import java.awt.Rectangle;

public final class Hexagon extends Piece implements PuzzlePiece
{
    private static final int EACH_ROTATION = 60;
    private static final int EXTRA_ROTATION = 30;
    
    public Hexagon()
    {
        super(EACH_ROTATION, EXTRA_ROTATION);
    }
    
    public Polygon getPolygon(final int x, final int y, final double radius)
    {
        int[] xpoints = new int[6];
        int[] ypoints = new int[6];
        
        xpoints[0] = getX();
        ypoints[0] = getY() - (getHeight() / 2);
        
        xpoints[1] = (int)(getX() + radius);
        ypoints[1] = getY() - (getHeight() / 4);
        
        xpoints[2] = (int)(getX() + radius);
        ypoints[2] = getY() + (getHeight() / 4);
        
        xpoints[3] = getX();
        ypoints[3] = getY() + (getHeight() / 2);
        
        xpoints[4] = (int)(getX() - radius);
        ypoints[4] = getY() + (getHeight() / 4);
        
        xpoints[5] = (int)(getX() - radius);
        ypoints[5] = getY() - (getHeight() / 4);
        
        Polygon p = new Polygon(xpoints, ypoints, xpoints.length);;
        
        Rectangle r = p.getBounds();
        this.setCenterX(r.x + (r.width  / 2));
        this.setCenterY(r.y + (r.height / 2));
        
        return p;
    }
}