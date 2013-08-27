package com.gamesbykevin.puzzle.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.puzzle.main.Engine;

public class StartGame extends Layer implements LayerRules
{
    public StartGame(final Engine engine)
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }    
}