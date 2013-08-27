package com.gamesbykevin.puzzle.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.menu.Option;
import com.gamesbykevin.puzzle.main.Engine;
import com.gamesbykevin.puzzle.menu.CustomMenu;

public class ExitGameConfirm extends Layer implements LayerRules
{
    public ExitGameConfirm(final Engine engine) throws Exception
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        super.setTitle("Confirm Exit");
        super.setForce(false);
        super.setPause(true);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine) throws Exception
    {
        //setup options here
        Option tmp;
        
        tmp = new Option(CustomMenu.LayerKey.MainTitle);
        tmp.add("Yes", null);
        super.add(CustomMenu.OptionKey.ExitGameConfirm, tmp);
        
        tmp = new Option(CustomMenu.LayerKey.StartGame);
        tmp.add("No", null);
        super.add(CustomMenu.OptionKey.ExitGameDeny, tmp);
    }
}