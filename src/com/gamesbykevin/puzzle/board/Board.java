package com.gamesbykevin.puzzle.board;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.puzzle.main.Engine;
import com.gamesbykevin.puzzle.main.ResourceManager;
import com.gamesbykevin.puzzle.shapes.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board 
{
    //different shapes that can be used
    private static final int SQUARE   = 0;
    private static final int HEXAGON  = 1;
    
    private static final int PRIMS_ALGORITHM = 0;
    private static final int DEPTH_FIRST_SEARCH_ALGORITHM = 1;
    
    //array containing all our game pieces
    private ArrayList<Piece> pieces;
    
    //what is the current shape that will be used
    private int shape;
    
    //parts of the game
    private boolean intro = true;
    private boolean spin  = false;
    
    //track all time here
    private TimerCollection timers;
    
    //this is the Cell we know is correct and is typically the center
    private Cell anchor;
    
    private int miscX = 0, miscY = 0;
    
    public enum TimerKey
    {
        START, SPIN, GAME, CPU_DELAY
    }
    
    private static final long START_DURATION = TimerCollection.toNanoSeconds((long)1000);
    private static final long  SPIN_DURATION = TimerCollection.toNanoSeconds((long)1000);
    
    //which algorithm are we using to generate puzzle
    private int algorithm;
    
    //how many clicks have there been
    private int clicks = 0;
    
    //the name of the board to display to differentiate the players
    private String name = "";
    
    //this will display winner or loser
    private String result = "";
    
    public Board()
    {
    }
    
    protected TimerCollection getTimers()
    {
        return timers;
    }
    
    public void setResult(final String result)
    {
        this.result = result;
    }
    
    public String getResult()
    {
        return this.result;
    }
    
    public void setName(final String name)
    {
        this.name = name;
    }
    
    protected void addClick()
    {
        clicks++;
    }
    
    protected void resetClick()
    {
        clicks = 0;
    }
    
    protected void setShape(final int shape)
    {
        this.shape = shape;
    }
    
    protected void setAlgorithm(final int algorithm)
    {
        this.algorithm = algorithm;
    }
            
    protected void setTimers(final long timeDeduction)
    {
        this.timers = new TimerCollection(timeDeduction);
        this.timers.add(TimerKey.START, START_DURATION);
        this.timers.add(TimerKey.SPIN,  SPIN_DURATION);
        this.timers.add(TimerKey.GAME);
    }
    
    /**
     *  The purpose is the generate a new board and generate a path
     * 
     * @param layers The number of shapes that surround the center shape
     * @param boundary The container for the shape pieces
     */
    protected void generate(final int layers, final Rectangle boundary)
    {
        miscX = boundary.x;
        miscY = boundary.y - 50;
        
        final int totalRows = ((layers - 1) * 2) + 1;
        final int totalCols = ((layers - 1) * 2) + 1;
        
        //basically the middle piece
        this.anchor = new Cell(totalCols / 2, totalRows / 2);
                
        //the width-height of each shape
        double eachWidth  = (boundary.width  / totalCols);
        double eachHeight = (boundary.height / totalRows);
        
        //start x,y
        int startX = boundary.x;
        int startY = boundary.y;
        
        pieces = new ArrayList<Piece>();
        
        switch (shape)
        {
            case SQUARE:

                for (int row=0; row < totalRows; row++)
                {
                    for (int col=0; col < totalCols; col++)
                    {
                        Square square = new Square();
                        square.setCol(col);
                        square.setRow(row);
                        square.setWidth(eachWidth);
                        square.setHeight(eachHeight);
                        square.setX(startX + (col * square.getWidth())  + (square.getWidth() /2));
                        square.setY(startY + (row * square.getHeight()) + (square.getHeight()/2));
                        
                        square.setPolygon(square.getPolygon(square.getX(), square.getY(), (square.getWidth() /2)));
                        
                        //add all the neighbors to this piece regardless if connected or not
                        //NOTE add neighbors in clockwise fashion around current piece
                        square.addNeighbor(col, row - 1);   //N
                        square.addNeighbor(col + 1, row);   //E
                        square.addNeighbor(col, row + 1);   //S
                        square.addNeighbor(col - 1, row);   //W

                        pieces.add(square);
                    }
                }

                break;

            case HEXAGON:
                
                //eachHeight = (eachHeight * .75);
                eachWidth = (Math.sqrt(3) / 2) * eachHeight;
                
                startY += (eachHeight * .5);
                startX += (eachWidth / 2);
                
                for (int row=0; row < totalRows; row++)
                {
                    for (int col=0; col < totalCols; col++)
                    {
                        Hexagon hexagon = new Hexagon();
                        hexagon.setCol(col);
                        hexagon.setRow(row);
                        hexagon.setWidth(eachWidth);
                        hexagon.setHeight(eachHeight);
                        
                        hexagon.setX( startX + (col * hexagon.getWidth()) );
                        
                        if (row % 2 == 0)
                            hexagon.setX(hexagon.getX() + (hexagon.getWidth() / 2));
                        
                        hexagon.setY( startY + (row * (hexagon.getHeight() * .75) ) );
                        
                        hexagon.setPolygon( hexagon.getPolygon(hexagon.getX(), hexagon.getY(), (hexagon.getWidth() /2)) );
                        
                        //add all the neighbors to this piece regardless if connected or not
                        //NOTE add neighbors in clockwise fashion around current piece
                        if (row % 2 == 0)
                        {
                            //order added here NE, E, SE, SW, W, NW
                            hexagon.addNeighbor(col + 1, row - 1);
                            hexagon.addNeighbor(col + 1, row);
                            hexagon.addNeighbor(col + 1, row + 1);
                            hexagon.addNeighbor(col, row + 1);
                            hexagon.addNeighbor(col - 1, row);
                            hexagon.addNeighbor(col, row - 1);
                        }
                        else
                        {
                            //order added here NE, E, SE, SW, W, NW
                            hexagon.addNeighbor(col, row - 1);
                            hexagon.addNeighbor(col + 1, row);
                            hexagon.addNeighbor(col, row + 1);
                            hexagon.addNeighbor(col - 1, row + 1);
                            hexagon.addNeighbor(col - 1, row);
                            hexagon.addNeighbor(col - 1, row - 1);
                        }
                        
                        pieces.add(hexagon);
                    }
                }
                
                break;
        }

        createMaze();
        
        //set connectors for each piece now that the maze is created
        for (Piece p : pieces)
        {   
            p.setConnectors();
        }
    }
    
    protected Cell getAnchor()
    {
        return this.anchor;
    }
    
    /**
     * Creates the maze using the given algorithm
     */
    private void createMaze()
    {
        //start at our anchor
        int currentCol = getAnchor().getCol();
        int currentRow = getAnchor().getRow();

        //this is so we can back track if at a dead end
        List<Cell> tracker = new ArrayList<Cell>();

        while(true)
        {
            if (hasVisitedAll())
                break;

            Cell current = new Cell(currentCol, currentRow);

            List<Cell> possible = getPossibilities(current);

            switch(algorithm)
            {
                case PRIMS_ALGORITHM:
                    //prism's algorithm
                    if (possible.size() > 0)
                    {
                        //out of the possible directions get a random one
                        Cell random = possible.get((int)(Math.random() * possible.size()));

                        //let the current piece know it is connected to the random piece
                        getPiece(current).addConnectedNeighbor(random);

                        //let the random piece know it is connected to the current piece
                        getPiece(random).addConnectedNeighbor(current);

                        tracker.add(random);
                    }
                    else
                    {
                        final int randomIndex = (int)(Math.random() * tracker.size());

                        //get random cell and remove from tracker
                        Cell random = tracker.get(randomIndex);
                        tracker.remove(randomIndex);

                        currentCol = random.getCol();
                        currentRow = random.getRow();
                    }
                    break;
                    
                case DEPTH_FIRST_SEARCH_ALGORITHM:
                    //Depth-first search algorithm
                    //if at least 1 new direction was found we can continue
                    if (possible.size() > 0)
                    {
                        //if there is more than 1 direction we can come back to this location
                        tracker.add(current);

                        //out of the possible directions get a random one
                        Cell random = possible.get((int)(Math.random() * possible.size()));

                        //let the current piece know it is connected to the random piece
                        getPiece(current).addConnectedNeighbor(random);

                        //let the random piece know it is connected to the current piece
                        getPiece(random).addConnectedNeighbor(current);

                        //the random cell is now the current one
                        currentCol = random.getCol();
                        currentRow = random.getRow();
                    }
                    else
                    {
                        //no more possible moves so back track to the last spot that had more possibilities
                        final int lastIndex = tracker.size() - 1;
                        Cell tmp = tracker.get(lastIndex);
                        tracker.remove(lastIndex);

                        currentCol = tmp.getCol();
                        currentRow = tmp.getRow();
                    }
                    break;
            }
        }
    }
    
    /**
     * This function will take a Cell parameter
     * 
     * @param current The current cell you would like to check
     * @return List<Cell> A list containing possible Cell locations
     */
    private List<Cell> getPossibilities(Cell current)
    {
        List<Cell> possible = new ArrayList<Cell>();
        
        List<Cell> cells = getPiece(current).getAllNeighbors();
        
        for (Cell cell : cells)
        {
            Piece tmp = getPiece(cell);
            
            if (tmp != null && !tmp.hasVisited())
                possible.add(cell);
        }
        
        return possible;
    }
    
    /**
     * This function will check every col,row 
     * in the puzzle piece array 
     * and verify all pieces have been visited.
     * 
     * @return boolean True if all non-null pieces have been visited 
     */
    private boolean hasVisitedAll()
    {
        for (Piece p : pieces)
        {
            if (!p.hasVisited())
                return false;
        }
        
        return true;
    }
    
    /**
     * Get the puzzle Piece at the given Cell
     * 
     * @param cell The current Cell we want to get from our array
     * @return Piece, if piece does not exist null is returned
     */
    protected Piece getPiece(Cell cell)
    {
        for (Piece p : pieces)
        {
            if (p.getCell().equals(cell))
                return p;
        }
        
        return null;
    }
    
    protected boolean hasIntro()
    {
        return intro;
    }
    
    protected boolean hasSpin()
    {
        return spin;
    }
    
    public void update(Engine engine)
    {
        updateBehavior(engine);
        
        for (Piece p : pieces)
        {
            final boolean finished = hasRotationFinished();
            
            p.update();
            
            //if the rotation hasn't finished and is now after update, check who is connected
            if (!finished && hasRotationFinished())
            {
                final int oldCount = this.getConnectedCount();
                checkConnected();
                final int newCount = this.getConnectedCount();
                
                //if old count is less than new count another piece has been connected, play sound effect
                if (oldCount < newCount)
                {
                    engine.getResources().playSound(ResourceManager.GameAudioEffects.Connection, false);
                }
                else
                {
                    engine.getResources().playSound(ResourceManager.GameAudioEffects.NoConnection, false);
                }
            }
        }
    }
    
    protected boolean hasRotationFinished()
    {
        for (Piece p : pieces)
        {
            if (p.hasRotate())
                return false;
        }
        
        return true;
    }
    
    protected ArrayList<Piece> getPieces()
    {
        return this.pieces;
    }
    
    protected int getConnectedCount()
    {
        int count = 0;
        
        for (Piece p : pieces)
        {
            if (p.isConnected())
                count++;
        }
        
        return count;
    }
    
    /**
     * WILL NEED ADDDITIONAL LOGIC HERE FOR DIFFERENT SHAPES
     * Checks all pieces to see/set the ones connected to the center piece
     */
    private void checkConnected()
    {
        //reset all pieces connected to false
        for (Piece p : pieces)
        {
            p.setConnected(false);
        }
        
        //the center cell is always connected and that is where we will start
        Cell currentCell = new Cell(getAnchor());
        Piece current = getPiece(currentCell);
        current.setConnected(true);
        
        //list of new places to check
        List<Cell> list    = new ArrayList<Cell>();
        
        //list of places we have already visited
        List<Cell> visited = new ArrayList<Cell>();
        
        while(true)
        {
            //get current piece
            current = getPiece(currentCell);
            
            //mark current piece as visited
            visited.add(currentCell);
            
            //get current piece neighbors
            List<Cell> neighbors = current.getNeighbors();
            
            for (Cell c : neighbors)
            {
                //make sure the neighbor hasn't already been visited and isn't on our future list
                if (list.indexOf(c) == -1 && visited.indexOf(c) == -1)
                {
                    Piece tmp = getPiece(c);
                    
                    if (tmp != null)
                    {
                        //checks if this neighbor is connected to the current piece
                        if (tmp.hasNeighbor(currentCell))
                        {
                            list.add(c);
                            tmp.setConnected(true);
                        }
                    }
                }
            }
            
            if (list.size() > 0)
            {
                final int randomIndex = (int)(Math.random() * list.size());
                currentCell = list.get(randomIndex);
                list.remove(randomIndex);
            }
            else
            {
                break;
            }
        }
    }
    
    /**
     * Checks all pieces to see if they are connected
     * 
     * @return true if all pieces are connected
     */
    public boolean hasConnectedAll()
    {
        for (Piece p : pieces)
        {
            if (!p.isConnected())
                return false;
        }
        
        for (Piece p : pieces)
        {
            p.setFinished(true);
        }
        
        return true;
    }
    
    /**
     * Checks the timers and updates the game accordingly.
     * Mostly spins the pieces and changes the spin speed 
     * and if the actual game can begin
     */
    private void updateBehavior(Engine engine)
    {
        if (hasIntro())
        {
            timers.update(TimerKey.START);

            if (timers.hasTimePassed(TimerKey.START))
            {
                intro = false;
                spin = true;
                engine.getResources().playSound(ResourceManager.GameAudioEffects.Spinning, false);
            }
        } 
        else 
        {
            if (spin)
            {
                timers.update(TimerKey.SPIN);
                
                for (Piece p : pieces)
                {
                    if (!p.hasRotate())
                    {
                        p.setRotationRateFast();
                        p.startRotate();
                    }
                }
                
                if (timers.hasTimePassed(TimerKey.SPIN))
                {
                    spin = false;
                    
                    for (Piece p : pieces)
                    {
                        p.setRotationRateNormal();
                        p.stopRotate();
                    }
                    
                    checkConnected();
                }
            }
            else
            {
                //keeps track of game time
                timers.update(TimerKey.GAME);
            }
        }
    }
    
    /**
     * Draw our game elements
     * 
     * @param g2d Graphics2d object to write game objects
     * @param resources ResourceCollection that contains sound/images etc....
     * @return Graphics2d object to write game objects
     */
    public Graphics2D render(Graphics2D g2d, final ResourceManager resources)
    {
        for (Piece p : pieces)
        {
            p.render(g2d);
        }
        
        renderMisc(g2d);
        
        return g2d;
    }
    
    private Graphics2D renderMisc(Graphics2D g2d)
    {
        final String timeDesc = timers.getDescPassed(TimerKey.GAME, TimerCollection.FORMAT_4);
        g2d.setFont(g2d.getFont().deriveFont((float)14));
        g2d.setColor(Color.WHITE);
        g2d.drawString(timeDesc, miscX, miscY);
        g2d.drawString("Clicks: " + this.clicks + " " + name, miscX, miscY + g2d.getFontMetrics().getHeight());
        g2d.drawString(result, miscX, miscY + (g2d.getFontMetrics().getHeight() * 2));
        return g2d;
    }
}