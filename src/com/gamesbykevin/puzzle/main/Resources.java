package com.gamesbykevin.puzzle.main;

import com.gamesbykevin.framework.resources.Manager;
import com.gamesbykevin.framework.resources.Audio;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import javax.swing.JApplet;
import javax.swing.JPanel;

/**
 * this class will contain all resources and methods to load/access all resources
 * @author GOD
 */
public class Resources 
{
    private LinkedHashMap<Object, Manager> everyResource = new LinkedHashMap<>();
    
    /**
     * Type of resource
     */
    private enum Type
    {
        MenuImage, MenuAudio, GameImage, GameFont, GameAudioEffects, GameAudioMusic
    }
    
    //root directory of all resources
    public static final String RESOURCE_DIR = "resources/"; 
    
    public enum MenuAudio
    {
        MenuChange
    }
    
    public enum MenuImage
    {
        TitleScreen, Credits, AppletFocus, TitleBackground, Mouse, MouseDrag, Instructions1, Instructions2, Controls
    }
    
    public enum GameFont
    {
        Dialog
    }
    
    public enum GameAudioEffects
    {
        Connection, NoConnection, Spinning
    }
    
    public enum GameAudioMusic
    {
        Theme
    }
    
    private boolean loading = true;
    
    public Resources() throws Exception
    {
        //load all menu images
        add(Type.MenuImage, (Object[])MenuImage.values(), RESOURCE_DIR + "images/menu/{0}.gif", "Loading Menu Image Resources", Manager.Type.Image);
        
        //load all game fonts
        add(Type.GameFont, (Object[])GameFont.values(), RESOURCE_DIR + "font/{0}.ttf", "Loading Game Font Resources", Manager.Type.Font);
        
        //load all menu audio
        add(Type.MenuAudio, (Object[])MenuAudio.values(), RESOURCE_DIR + "audio/menu/{0}.wav", "Loading Menu Audio Resources", Manager.Type.Audio);
        
        //load all game audio
        add(Type.GameAudioEffects, (Object[])GameAudioEffects.values(), RESOURCE_DIR + "audio/game_effects/{0}.wav", "Loading Game Audio Resources", Manager.Type.Audio);

        //load all game audio
        add(Type.GameAudioMusic,  (Object[])GameAudioMusic.values(),   RESOURCE_DIR + "audio/game_music/{0}.mp3", "Loading Game Audio Resources", Manager.Type.Audio);
    }
    
    /**
     * add a collection of resources audio/image/font
     * @param key
     * @param eachResourceKey
     * @param directory
     * @param loadDesc
     * @param resourceType 
     */
    private void add(Object key, Object[] eachResourceKey, String directory, String loadDesc, Manager.Type resourceType) throws Exception
    {
        String[] locations = new String[eachResourceKey.length];
        for (int i=0; i < locations.length; i++)
        {
            locations[i] = MessageFormat.format(directory, i);
        }

        Manager resources = new Manager(Manager.LoadMethod.OnePerFrame, locations, eachResourceKey, resourceType);
        resources.setDescription(loadDesc);
        
        everyResource.put(key, resources);
    }
    
    public boolean isLoading()
    {
        return loading;
    }
    
    private Manager getResources(Object key)
    {
        return everyResource.get(key);
    }
    
    public Font getGameFont(Object key)
    {
        return getResources(Type.GameFont).getFont(key);
    }
    
    public Image getGameImage(Object key)
    {
        return getResources(Type.GameImage).getImage(key);
    }
    
    public Image getMenuImage(Object key)
    {
        return getResources(Type.MenuImage).getImage(key);
    }
    
    public Audio getMenuAudio(Object key)
    {
        return getResources(Type.MenuAudio).getAudio(key);
    }
    
    public void playMusic(Object key, boolean loop)
    {
        getResources(Type.GameAudioMusic).playAudio(key, loop);
    }
    
    public void playSound(Object key, boolean loop)
    {
        getResources(Type.GameAudioEffects).playAudio(key, loop);
    }
    
    public void stopSound(Object key)
    {
        getResources(Type.GameAudioEffects).getAudio(key).stopSound();
    }
    
    public void stopAllSound()
    {
        getResources(Type.GameAudioEffects).stopAllAudio();
        getResources(Type.GameAudioMusic).stopAllAudio();
    }
    
    public void update(final JApplet applet)
    {
        this.update(applet.getClass());
    }
    
    public void update(final JPanel panel)
    {
        this.update(panel.getClass());
    }
    
    public void update(final Class source) 
    {
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Manager resources = getResources(key);
            
            if (!resources.isComplete())
            {
                resources.update(source);
                return;
            }
        }

        //if this line is reached we are done loading every resource
        loading = false;
    }
    
    public boolean isAudioEnabled()
    {
        return getResources(Type.GameAudioEffects).isAudioEnabled();
    }
    
    public void setAudioEnabled(boolean soundEnabled)
    {
        Manager r1 = getResources(Type.GameAudioEffects);
        Manager r2 = getResources(Type.GameAudioMusic);
        
        if ((r1.isAudioEnabled() && soundEnabled) || (!r1.isAudioEnabled() && !soundEnabled))
            return;
        
        r1.setAudioEnabled(soundEnabled);
        r2.setAudioEnabled(soundEnabled);
        
        if (!soundEnabled)
        {
            r1.stopAllAudio();
            r2.stopAllAudio();
        }
    }
    
    public void dispose()
    {
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Manager manager = getResources(key);
            
            if (manager != null)
                manager.dispose();
            
            manager = null;
            
            everyResource.put(key, manager);
        }
        
        keys = null;
        
        everyResource.clear();
        everyResource = null;
    }
    
    public Graphics draw(Graphics graphics, final Rectangle screen)
    {
        if (!loading)
            return graphics;
        
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Manager manager = getResources(key);
            
            if (!manager.isComplete())
            {
                manager.render(graphics, screen);
                return graphics;
            }
        }
        
        return graphics;
    }
}