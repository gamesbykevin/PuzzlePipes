package com.gamesbykevin.puzzle.players;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.puzzle.board.Board;
import com.gamesbykevin.puzzle.board.Piece;
import com.gamesbykevin.puzzle.main.Engine;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Agent extends Board implements Player
{
    //current piece the cpu is working on
    private Piece current;
    
    //different time for different difficulty {EASY, MEDIUM, HARD}
    private static final long[] CPU_DELAY_DIFFICULTY = new long[]{1000L, 500L, 1500L};
    
    private final long cpuDelay;
    
    //cells to check that need to be solved
    private List<Cell> checklist = new ArrayList<Cell>();
    
    public Agent(final int difficulty)
    {
        this.cpuDelay = CPU_DELAY_DIFFICULTY[difficulty];
    }
    
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
        
        getTimers().add(TimerKey.CPU_DELAY, TimerCollection.toNanoSeconds(cpuDelay));
    }
    
    public void generate(final int layers, final Rectangle boundary)
    {
        super.generate(layers, boundary);
    }
    
    public void update(Engine engine)
    {
        if (hasConnectedAll())
            return;
        
        getTimers().update(TimerKey.CPU_DELAY);
        
        super.update(engine);
        
        //set anchor piece to corret rotation
        getPiece(getAnchor()).reset();
        
        //if we aren't spinning and past the intro and we have awaited the delay for cpu
        if (!hasSpin() && !hasIntro() && getTimers().hasTimePassed(TimerKey.CPU_DELAY))
        {
            getTimers().resetRemaining(TimerKey.CPU_DELAY);
            
            //has the current piece finished rotating
            if (hasRotationFinished())
            {
                if (current == null)
                {
                    current = getPiece(getAnchor());
                }
                
                //has the current piece finished rotation
                if (current.getCurrentRotation() != 0)
                {
                    addClick();
                    current.startRotate();
                }
                else
                {
                   //the current piece has rotated to its appropriate place now find next piece
                   List<Cell> list = current.getNeighbors();

                   for (Cell c : list)
                   {
                       if (getPiece(c).getCurrentRotation() != 0 && checklist.indexOf(c) < 0)
                           checklist.add(c);
                   }

                   final int randomIndex = (int)(Math.random() * checklist.size());
                   current = getPiece(checklist.get(randomIndex));
                   checklist.remove(randomIndex);
                }
            }
        }
    }
}