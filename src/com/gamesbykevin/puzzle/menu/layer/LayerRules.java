package com.gamesbykevin.puzzle.menu.layer;

import com.gamesbykevin.puzzle.main.Engine;

public interface LayerRules 
{
    /**
     * Setup Layer including options if they exist
     * 
     * @param engine 
     */
    public void setup(final Engine engine) throws Exception;
}