package centipede;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class GamePlay extends JPanel {
	private static final Color BG_COLOR = new Color( 0x6B5A62 );

    // background color of playing screen
    private static final String FONT_NAME = "Questrial";

    // font of the numbers of the tiles
    private static final int TILE_SIZE = 64;

    // size of each tile
    private static final int TILES_MARGIN = 16;

    // margin between the tiles
    private Board grid;

    // creating an object of the GameGrid class

    protected boolean myWin = false;

    protected boolean myLose = false;

    protected int myScore = 0;
    
    public GamePlay()
    {
        setFocusable( true ); // focusable flag indicates whether a component
        // can gain the focus if it is requested to do so

        grid = new Board( this );

        // deals with all the keystroke registering
        // keyadapter allows the keys pressed to work
        addKeyListener( new KeyAdapter()
        {
            // keypressing
            public void keyPressed( KeyEvent e )
            {
                if ( e.getKeyCode() == KeyEvent.VK_SPACE )
                {
                    System.out.println("Hello");
                }

            }
        });

    }
    
    /**
     * Adds the score for every move that is made.
     * 
     * @param score
     */
    public void incrementScore( int score )
    {
        myScore += score;
    }
    
    
}
