import com.gamesbykevin.puzzle.main.Main;
import com.gamesbykevin.puzzle.shared.Shared;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This file will run the game as a desktop application
 * @author GOD
 */
public class StartDesktop 
{
    public static void main(String[] args)
    {
        try
        {
            JFrame window = new JFrame("Puzzle Pipes");

            window.setCursor(Shared.CURSOR);
            window.add(new GamePanel());
            window.setResizable(false);
            window.pack();

            window.setLocationRelativeTo(null);
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

class GamePanel extends JPanel
{
    private Main main;
	
    public GamePanel() 
    {
        setCursor(Shared.CURSOR);
        setPreferredSize(new Dimension(Shared.WINDOW_WIDTH, Shared.WINDOW_HEIGHT));
        setFocusable(true);
        requestFocus();
        
        try
        {
            main = new Main(Shared.DEFAULT_UPS, false);
            main.setPanel(this);
            main.createGameEngine();
            main.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}