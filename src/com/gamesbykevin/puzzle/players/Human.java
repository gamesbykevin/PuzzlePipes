package com.gamesbykevin.puzzle.players;

import com.gamesbykevin.puzzle.board.Board;
import com.gamesbykevin.puzzle.board.Piece;
import com.gamesbykevin.puzzle.main.Engine;
import java.awt.Rectangle;

public class Human extends Board implements Player
{
    public void setShape(final int shape)
    {
        super.setShape(shape);
    }
    
    public void setAlgorithm(final int algorithm)
    {
        super.setAlgorithm(algorithm);
    }
            
    public void setTimers(final long timeDeduction)
    {
        super.setTimers(timeDeduction);
    }
    
    public void generate(final int layers, final Rectangle boundary)
    {
        super.generate(layers, boundary);
    }
    
    public void update(Engine engine)
    {
        if (hasConnectedAll())
            return;
        
        super.update(engine);
        
        //if we aren't spinning and the game isn't ready
        if (!hasSpin() && !hasIntro())
        {
            //check each piece for mouse input
            for (Piece p : getPieces())
            {
                final boolean hasBounds = p.getShape().contains(engine.getMouse().getMouseLocation());
                
                if (engine.getMouse().isMousePressed() && hasBounds && !p.hasRotate())
                {
                    addClick();
                    p.startRotate();
                }
            }
        }
    }
}