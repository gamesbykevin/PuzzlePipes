package com.gamesbykevin.puzzle.players;

import com.gamesbykevin.puzzle.main.Engine;

import java.awt.Rectangle;

public interface Player 
{
    /**
     * Sets the shape that we will be working with
     * @param shape int Representing the shape
     */
    public void setShape(final int shape);
    
    /**
     * Which algorithm do we use to generate the puzzle
     * 
     * @param algorithm int Representing the algorithm used to generate the puzzle
     */
    public void setAlgorithm(final int algorithm);
    
    /**
     * Setup game timers
     * 
     * @param timeDeduction amount of time in nano seconds per each frame
     */
    public void setTimers(final long timeDeduction);
    
    /**
     * 
     * @param layers number of layers for this puzzle
     * @param boundary the dimensions the puzzle will fit inside
     */
    public void generate(final int layers, final Rectangle boundary);
    
    /**
     * We will need different logic to update the different types of players
     * 
     * @param engine Main game engine
     */
    public void update(Engine engine);
}
