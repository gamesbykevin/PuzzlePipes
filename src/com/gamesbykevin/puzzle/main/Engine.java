package com.gamesbykevin.puzzle.main;

import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.framework.input.*;
import com.gamesbykevin.framework.input.Keyboard;

import com.gamesbykevin.puzzle.menu.GameMenu;
import com.gamesbykevin.puzzle.players.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Engine implements KeyListener, MouseMotionListener, MouseListener, EngineRules 
{
    //our Main class has important information in it so we need a reference here
    private Main main;
    
    //our human
    private Human human;
    
    //our cpu opponent
    private Agent agent;
    
    //access this menu here
    private GameMenu menu;
    
    //object that contains all image/audio resources in the game
    private ResourceManager resources;
    
    //mouse object that will be recording mouse input
    private Mouse mouse;
    
    //keyboard object that will be recording key input
    private Keyboard keyboard;
    
    //timer to countdown next game
    private TimerCollection timer;
    
    //width and height of puzzle
    private static final int PUZZLE_DIMENSION = 450;
    
    private enum TimerKeys
    {
        NextGame
    }
    
    /**
     * The Engine that contains the game/menu objects
     * 
     * @param main Main object that contains important information so we need a reference to it
     * @throws CustomException 
     */
    public Engine(final Main main) 
    {
        this.main = main;
        this.mouse = new Mouse();
        this.keyboard = new Keyboard();
        this.resources = new ResourceManager();
        this.timer = new TimerCollection(main.getTimeDeductionPerFrame());
        this.timer.add(TimerKeys.NextGame, TimerCollection.toNanoSeconds(3000L));
    }
    
    public void dispose()
    {
        try
        {
            resources.dispose();
            resources = null;
            
            menu.dispose();
            menu = null;

            super.finalize();
        }
        catch(Throwable t)
        {
            
        }
    }
    
    public void update(Main main)
    {
        try
        {
            //if resources are still loading
            if (resources.isLoading())
            {
                resources.update(main.getContainerClass());

                //resources are now loaded so create the menu
                if (!resources.isLoading())
                    menu = new GameMenu(resources, main.getScreen());
            }
            else
            {
                if (!menu.hasFocus())
                {
                    mouse.resetMouseEvents();
                    keyboard.resetAllKeyEvents();
                }

                menu.update(main, this, resources, keyboard, mouse);

                //if the menu is on the last layer and the window has focus
                if (menu.isMenuFinished() && menu.hasFocus())
                {
                    //MAIN GAME LOGIC RUN HERE
                    if (agent != null)
                    {
                        if (human != null && !agent.hasConnectedAll())
                            human.update(this);
                        if (!human.hasConnectedAll())
                            agent.update(this);
                    }
                    else
                    {
                        if (human != null)
                            human.update(this);
                    }
                    
                    if (human != null && agent != null)
                    {
                        if (human.getResult().trim().length() < 1)
                        {
                            if (human.hasConnectedAll() && !agent.hasConnectedAll())
                            {
                                human.setResult("WIN! (Next Game in ...)");
                                agent.setResult("LOSE!");
                            }
                            
                            if (!human.hasConnectedAll() && agent.hasConnectedAll())
                            {
                                human.setResult("LOSE! (Trying again in ...)");
                                agent.setResult("WIN!");
                            }
                        }
                        else
                        {
                            if (human.hasConnectedAll() || agent.hasConnectedAll())
                            {
                                timer.update(TimerKeys.NextGame);

                                if (timer.hasTimePassed(TimerKeys.NextGame))
                                    reset();
                            }
                        }
                    }
                    else
                    {
                        if (human != null)
                        {
                            if (human.hasConnectedAll())
                            {
                                human.setResult("Next puzzle starting ...");
                                timer.update(TimerKeys.NextGame);
                                
                                if (timer.hasTimePassed(TimerKeys.NextGame))
                                    reset();
                            }
                        }
                    }
                }

                //reset all mouse events
                mouse.resetMouseEvents();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void reset() throws Exception
    {
        int shape      = menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.Shape);
        int algorithm  = menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.Algorithm);
        int blocks     = menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.Blocks);
        int indexVs    = menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.VsMode);
        int difficulty = menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.Difficulty);
        
        //if 1 player make sure agent is null
        if (indexVs == 0)
            agent = null;
        
        if (human != null)
        {
            if (agent != null)
            {
                if (human.hasConnectedAll() && !agent.hasConnectedAll())
                {
                    blocks++;
                    menu.setOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.Blocks, blocks);
                }
            }
            else
            {
                if (human.hasConnectedAll())
                {
                    blocks++;
                    menu.setOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.Blocks, blocks);
                }
            }
        }
        
        timer.resetRemaining(TimerKeys.NextGame);
        
        human = new Human();
        human.setTimers(main.getTimeDeductionPerFrame());
        human.setShape(shape);
        human.setAlgorithm(algorithm);
        
        if (indexVs == 0)
        {
            Rectangle tmp1 = new Rectangle(
                    main.getScreen().x + (main.getScreen().width / 2) - (PUZZLE_DIMENSION / 2), 
                    75,
                    PUZZLE_DIMENSION,
                    PUZZLE_DIMENSION);
            
            human.generate(blocks + 2, tmp1);
        }
        else 
        {
            Rectangle tmp1 = new Rectangle(25,75,PUZZLE_DIMENSION,PUZZLE_DIMENSION);
            human.generate(blocks + 2, tmp1);
            human.setName("Hum");
            
            agent = new Agent(difficulty);
            agent.setTimers(main.getTimeDeductionPerFrame());
            agent.setShape(shape);
            agent.setAlgorithm(algorithm);
            
            Rectangle tmp2 = new Rectangle(tmp1.x + tmp1.width + 50,75,PUZZLE_DIMENSION,PUZZLE_DIMENSION);
            agent.generate(blocks + 2, tmp2);
            agent.setName("Cpu");
        }
        
        getResources().stopAllSound();
        getResources().playMusic(ResourceManager.GameAudioMusic.Theme, true);
    }
    
    public Graphics render(Graphics g) throws Exception
    {
        if (resources.isLoading())
        {
            //if we are loading resources draw loading screen
            resources.draw(g, main.getScreen());
        }
        else
        {
            //draw game elements
            renderGame((Graphics2D)g);
            
            //draw menu on top of the game if visible
            renderMenu(g);
        }
        
        //g.drawImage(resources.getGameImage(ResourceCollection.IMAGE_GAME_OVER), screen.x, screen.y, screen.width, screen.height, null);
        
        return g;
    }
    
    /**
     * Draw our game elements
     * @param g2d Graphics2D object that game elements will be written to
     * @return Graphics the Graphics object with the appropriate game elements written to it
     * @throws Exception 
     */
    private Graphics renderGame(Graphics2D g2d) throws Exception
    {
        Font f = g2d.getFont();
        Stroke stroke = g2d.getStroke();
        
        g2d.setFont(resources.getGameFont(ResourceManager.GameFont.Dialog));
        //g2d.setStroke(new BasicStroke(4));
        
        if (human != null)
            human.render(g2d, resources);
        
        if (agent != null)
            agent.render(g2d, resources);
        
        g2d.setStroke(stroke);
        g2d.setFont(f);
        
        f = null;
        stroke = null;
        
        return g2d;
    }
    
    /**
     * Draw the Game Menu
     * 
     * @param g Graphics object where Images/Objects will be drawn to
     * @return Graphics The applied Graphics drawn to parameter object
     * @throws Exception 
     */
    private Graphics renderMenu(Graphics g) throws Exception
    {
        //if menu is setup draw menu
        if (menu.isMenuSetup())
            menu.render(g);

        //is menu is finished and we dont want to hide mouse cursor then draw it, or if the menu is not finished show mouse
        if (menu.isMenuFinished() && !Main.hideMouse || !menu.isMenuFinished())
        {
            Point p = mouse.getMouseLocation();

            if (p != null && resources.getMenuImage(ResourceManager.MenuImage.Mouse) != null && resources.getMenuImage(ResourceManager.MenuImage.MouseDrag) != null)
            {
                if (mouse.isMouseDragged())
                    g.drawImage(resources.getMenuImage(ResourceManager.MenuImage.MouseDrag), p.x, p.y, null);
                else
                    g.drawImage(resources.getMenuImage(ResourceManager.MenuImage.Mouse), p.x, p.y, null);
            }
        }

        return g;
    }
    
    public ResourceManager getResources()
    {
        return resources;
    }
    
    public void keyReleased(KeyEvent e)
    {
        keyboard.setKeyReleased(e.getKeyCode());
    }
    
    public void keyPressed(KeyEvent e)
    {
        keyboard.setKeyPressed(e.getKeyCode());
    }
    
    public void keyTyped(KeyEvent e)
    {
        keyboard.setKeyTyped(e.getKeyChar());
    }
    
    public void mouseClicked(MouseEvent e)
    {
        mouse.setMouseClicked(e);
    }
    
    public void mousePressed(MouseEvent e)
    {
        mouse.setMousePressed(e);
    }
    
    public void mouseReleased(MouseEvent e)
    {
        mouse.setMouseReleased(e);
    }
    
    public void mouseEntered(MouseEvent e)
    {
        mouse.setMouseEntered(e.getPoint());
    }
    
    public void mouseExited(MouseEvent e)
    {
        mouse.setMouseExited(e.getPoint());
    }
    
    public void mouseMoved(MouseEvent e)
    {
        mouse.setMouseMoved(e.getPoint());
    }
    
    public void mouseDragged(MouseEvent e)
    {
        mouse.setMouseDragged(e.getPoint());
    }
    
    public Mouse getMouse()
    {
        return mouse;
    }
    
    public Keyboard getKeyboard()
    {
        return keyboard;
    }
}