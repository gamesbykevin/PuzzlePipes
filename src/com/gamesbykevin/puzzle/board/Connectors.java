package com.gamesbykevin.puzzle.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class Connectors 
{
    //lines drawn on shape so we can tell if it is connected
    private List<Rectangle> connectors = new ArrayList<Rectangle>();
    
    //how much to rotate each connection
    private List<Double> rotations = new ArrayList<Double>();
    
    //extra rotation(if needed)
    private double extraRotation = 0;
    
    private static final int THICKNESS = 4;
    
    /**
     * Add a connector based on the starting point, radius 
     * and additional rotation so it appears in the correct spot.
     * 
     * @param centerX X Coordinate
     * @param centerY Y Coordinate
     * @param width The width of the connector
     * @param rotation Rotation in degrees
     */
    public void add(final int centerX, final int centerY, final int width, final double rotation)
    {
        Rectangle r = new Rectangle(centerX, centerY - width, THICKNESS, width);
        r.x = centerX - (THICKNESS / 2);
        
        connectors.add(r);
        rotations.add(rotation);
    }
    
    /**
     * Set the extra rotation if needed if the first connector is not vertical north
     * 
     * @param extraRotation 
     */
    public void setExtraRotation(final double extraRotation)
    {
        this.extraRotation = extraRotation;
    }
    
    /**
     * Draw the connectors to our piece here. 
     * We will start at startingRotation and 
     * then apply the additional rotation for 
     * each object afterwards.
     * 
     * @param g2d Graphics object to draw connectors
     * @param startingRotation Default rotation to start at
     * @param centerX X coordinate
     * @param centerY X coordinate
     * @return g2d Graphics object with the connectors drawn
     */
    public Graphics2D render(Graphics2D g2d, final double startingRotation, final int centerX, final int centerY)
    {
        g2d.setColor(Color.BLUE);
        
        for (int i=0; i < connectors.size(); i++)
        {
            Rectangle r = connectors.get(i);

            //set rotation of new transform with anchor point x, y
            AffineTransform a = new AffineTransform();
            a.rotate(Math.toRadians(startingRotation + extraRotation + rotations.get(i)), centerX, centerY);
            
            g2d.setTransform(a);
            g2d.fillRect(r.x, r.y, r.width, r.height);
            
            a.setToIdentity();
            g2d.setTransform(a);
        }
        
        return g2d;
    }
}