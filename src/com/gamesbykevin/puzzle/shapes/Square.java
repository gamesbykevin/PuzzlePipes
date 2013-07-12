package com.gamesbykevin.puzzle.shapes;

import com.gamesbykevin.puzzle.board.Piece;
import com.gamesbykevin.puzzle.board.PuzzlePiece;
import java.awt.Polygon;
import java.awt.Rectangle;

public final class Square extends Piece implements PuzzlePiece
{
    private static final int EACH_ROTATION = 90;
    private static final int EXTRA_ROTATION = 0;
    
    public Square()
    {
        super(EACH_ROTATION, EXTRA_ROTATION);
    }
    
    public Polygon getPolygon(final int centerX, final int centerY, final double radius)
    {
        int[] xpoints = new int[4];
        int[] ypoints = new int[4];
        
        xpoints[0] = (int)(centerX - radius);
        ypoints[0] = (int)(centerY - radius);
        
        xpoints[1] = (int)(centerX + radius);
        ypoints[1] = (int)(centerY - radius);
        
        xpoints[2] = (int)(centerX + radius);
        ypoints[2] = (int)(centerY + radius);
        
        xpoints[3] = (int)(centerX - radius);
        ypoints[3] = (int)(centerY + radius);
     
        Polygon p = new Polygon(xpoints, ypoints, xpoints.length);
        
        Rectangle r = p.getBounds();
        this.setCenterX(r.x + (r.width  / 2));
        this.setCenterY(r.y + (r.height / 2));
        
        return p;
    }
}