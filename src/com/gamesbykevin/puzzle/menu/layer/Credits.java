package com.gamesbykevin.puzzle.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.puzzle.main.Engine;
import com.gamesbykevin.puzzle.main.Resources;
import com.gamesbykevin.puzzle.menu.CustomMenu;

public class Credits extends Layer implements LayerRules
{
    public Credits(final Engine engine)
    {
        super(Layer.Type.SCROLL_VERTICAL_NORTH, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.Credits));
        setForce(false);
        setPause(false);
        setNextLayer(CustomMenu.LayerKey.MainTitle);
        setTimer(new Timer(TimerCollection.toNanoSeconds(7500L)));
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}