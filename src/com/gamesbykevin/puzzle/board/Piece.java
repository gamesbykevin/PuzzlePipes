package com.gamesbykevin.puzzle.board;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.base.Cell;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class Piece extends Sprite
{
    //were our we currently
    private double currentRotation = 0;
    
    //each time we rotate, how much
    private final int eachRotation;
    
    //when to stop rotating
    private double finalRotation   = 0;
    
    //should we rotate
    private boolean rotate = false;
    
    //the normal rotation
    private static final int NORMAL_ROTATION_RATE = 5;
    private final int FAST_ROTATION_RATE = (Math.random() > .5) ? 15 : -15;
    private int currentRotationRate = FAST_ROTATION_RATE;
    
    //is the current piece connected in any way
    private boolean connected = false;
    
    //indicates all pieces are done
    private boolean finished = false;
    
    //our shape
    private Polygon shape;
    
    //this will have every neighbor cell, even if it isn't connected to this one
    private List<Cell> allNeighbors;
    
    //which elements in List<Cell> allNeighbors are connected
    private List<Integer> connectedNeighbors;
    
    //this will store the parent piece to help the cpu solve the puzzle
    private Cell parent;
    
    //visible connectors for the user to see
    private Connectors connectors;
    
    //rotate around this x,y
    private int centerX, centerY;
    
    public Piece(final int eachRotation, final int extraRotation)
    {
        super();
        
        this.eachRotation = eachRotation;
        
        allNeighbors = new ArrayList<Cell>();
        connectedNeighbors = new ArrayList<Integer>();
        connectors = new Connectors();
        connectors.setExtraRotation(extraRotation);
    }
    
    private boolean isFinished()
    {
        return finished;
    }
    
    public void setFinished(final boolean finished)
    {
        this.finished = finished;
    }
    
    public void setConnectors()
    {
        for (int i : connectedNeighbors)
        {
            connectors.add((int)getX(), (int)getY(), (int)(getWidth() / 2), eachRotation * i);
        }
    }
    
    public boolean isConnected()
    {
        return connected;
    }
    
    public void setConnected(final boolean connected)
    {
        this.connected = connected;
    }
    
    public void setParent(final Cell parent)
    {
        this.parent = parent;
    }
    
    public Cell getParent()
    {
        return parent;
    }
    
    public boolean hasRotate()
    {
        return rotate;
    }
    
    public void reset()
    {
        //continue to rotate until at correct spot
        while (finalRotation != 0)
        {
            startRotate();
        }
    }
    
    protected void setCenterX(final int centerX)
    {
        this.centerX = centerX;
    }
    
    protected void setCenterY(final int centerY)
    {
        this.centerY = centerY;
    }
    
    public void startRotate()
    {
        this.rotate = true;
        
        //each rotation is x degrees
        finalRotation += eachRotation;
        
        if (finalRotation > 359)
            finalRotation = 0;
        
        //apply rotation to neighbors as well
        rotateNeighbors();
    }
    
    private void rotateNeighbors()
    {
        for (int i=0; i < connectedNeighbors.size(); i++)
        {
            Integer index = connectedNeighbors.get(i);
            index++;
            
            if (index > allNeighbors.size() - 1)
                index = 0;
            
            connectedNeighbors.set(i, index);
        }
    }
    
    public void stopRotate()
    {
        this.rotate = false;
        this.currentRotation = this.finalRotation;
    }
    
    public void addNeighbor(final int col, final int row)
    {
        allNeighbors.add(new Cell(col, row));
    }
    
    public void addConnectedNeighbor(Cell cell)
    {
        for (int i=0; i < allNeighbors.size(); i++)
        {
            //we found our neighbor now store the index it was located in
            if (allNeighbors.get(i).equals(cell))
            {
                connectedNeighbors.add(i);
            }
        }
    }
    
    public double getCurrentRotation()
    {
        return this.currentRotation;
    }
    
    /**
     * Gets all neighbors connected or not
     * 
     * @return List<Cell> all Cells
     */
    public List<Cell> getAllNeighbors()
    {
        return this.allNeighbors;
    }
    
    /**
     * Gets all neighbors connected to this piece 
     * @return List<Cell> all Cells connected
     */
    public List<Cell> getNeighbors()
    {
        List<Cell> list = new ArrayList<Cell>();
        
        for (Integer i : connectedNeighbors)
        {
            list.add(allNeighbors.get(i));
        }
        
        return list;
    }
    
    public boolean hasVisited()
    {
        return (getNeighbors().size() > 0);
    }
    
    public boolean hasNeighbor(Cell cell)
    {
        for (Cell c : getNeighbors())
        {
            if (c.equals(cell))
                return true;
        }
        
        return false;
    }
    
    public void setPolygon(Polygon p)
    {
        this.shape = p;
    }
    
    public Polygon getShape()
    {
        return shape;
    }
    
    public void setRotationRateFast()
    {
        this.currentRotationRate = FAST_ROTATION_RATE;        
    }
    
    public void setRotationRateNormal()
    {
        this.currentRotationRate = NORMAL_ROTATION_RATE;
    }
    
    /**
     * Checks to see if we are finished rotating and rotate if not to the next rotation
     */
    public void update()
    {
        if (rotate)
        {
            currentRotation += currentRotationRate;
            
            if (currentRotation > 359)
                currentRotation = 0;
            
            if (currentRotation == finalRotation)
                rotate = false;
        }
    }
    
    public Graphics2D render(Graphics2D g2d)
    {
        //set rotation of new transform with anchor point x, y
        AffineTransform a = new AffineTransform();
        a.rotate(Math.toRadians(currentRotation), centerX, centerY);
        
        //set the transform, so the shape we draw after this will have the rotation
        g2d.setTransform(a);
        
        //draw stuff here
        if (shape != null)
        {
            if (isFinished())
            {
                g2d.setColor(Color.GREEN);
                g2d.fillPolygon(shape);
            }
            else
            {
                if (isConnected())
                {
                    g2d.setColor(Color.GREEN);
                    g2d.fillPolygon(shape);
                    g2d.setColor(Color.RED);
                    g2d.drawPolygon(shape);
                }
                else
                {
                    g2d.setColor(Color.WHITE);
                    g2d.fillPolygon(shape);
                    g2d.setColor(Color.RED);
                    g2d.drawPolygon(shape);
                }
            }
        }
        
        //reset transform back
        a.setToIdentity();
        g2d.setTransform(a);
        
        //draw connectors
        connectors.render(g2d, currentRotation, centerX, centerY);
        
        return g2d;
    }
}