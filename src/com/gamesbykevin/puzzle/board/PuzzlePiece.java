package com.gamesbykevin.puzzle.board;

import java.awt.Polygon;

public interface PuzzlePiece 
{
    /**
     * Every puzzle Piece is a polygon 
     * so every piece used needs a way 
     * to create the polygon for the
     * specified piece
     * 
     * @param centerX The X coordinate of our polygon, self explanatory
     * @param centerY The Y coordinate of our polygon, self explanatory
     * @param radius The radius of our polygon, self explanatory
     * @return Polygon created with the given x,y and radius
     */
    public Polygon getPolygon(final int centerX, final int centerY, final double radius);
}
