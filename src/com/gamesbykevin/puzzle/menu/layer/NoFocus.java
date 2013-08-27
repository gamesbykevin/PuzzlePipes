package com.gamesbykevin.puzzle.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.puzzle.main.Engine;
import com.gamesbykevin.puzzle.main.Resources;
import com.gamesbykevin.puzzle.menu.CustomMenu;

public class NoFocus extends Layer implements LayerRules
{
    public NoFocus(final Engine engine)
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.AppletFocus));
        setForce(false);
        setPause(true);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}