package com.gamesbykevin.puzzle.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.menu.Option;

import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.puzzle.main.Engine;
import com.gamesbykevin.puzzle.main.Resources;
import com.gamesbykevin.puzzle.menu.CustomMenu;

public class Options extends Layer implements LayerRules
{
    //maze will default to 5 row/col
    private static final int MAZE_DIMENSION_SIZE = 5;
    
    //maze limit will be 30 row/col
    private static final int MAZE_DIMENSION_LIMIT = 30;
    
    public Options(final Engine engine) throws Exception
    {
        super(Layer.Type.SCROLL_HORIZONTAL_WEST_REPEAT, engine.getMain().getScreen());
        
        super.setTitle("Options");
        super.setImage(engine.getResources().getMenuImage(Resources.MenuImage.TitleBackground));
        super.setTimer(new Timer(TimerCollection.toNanoSeconds(5000L)));
        super.setForce(false);
        super.setPause(true);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine) throws Exception
    {
        //setup options here
        Option tmp;
        
        tmp = new Option("Layers: ");
        tmp.add("2", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("3", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("4", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("5", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("6", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("7", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("8", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("9", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("10", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(CustomMenu.OptionKey.Blocks, tmp);
        
        tmp = new Option("Difficulty (2 Player): ");
        tmp.add("Medium", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("Hard", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("Easy", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(CustomMenu.OptionKey.Difficulty, tmp);
        
        tmp = new Option("Shape: ");
        tmp.add("Square", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("Hexagon", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(CustomMenu.OptionKey.Shape, tmp);
        
        tmp = new Option("Algorithm: ");
        tmp.add("Prim's",       engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("Depth-first",  engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(CustomMenu.OptionKey.Algorithm, tmp);
        

        tmp = new Option("# Player: ");
        tmp.add("1 Player",               engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("2 Player (Hum vs Cpu)",  engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(CustomMenu.OptionKey.VsMode, tmp);
        
        
        tmp = new Option("Sound: ");
        tmp.add("On", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("Off",engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(CustomMenu.OptionKey.Sound, tmp);
        
        tmp = new Option("FullScreen: ");
        tmp.add("Off",engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("On", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(CustomMenu.OptionKey.FullScreen, tmp);
        
        tmp = new Option(CustomMenu.LayerKey.MainTitle);
        tmp.add("Go Back", null);
        super.add(CustomMenu.OptionKey.GoBack, tmp);
    }
}