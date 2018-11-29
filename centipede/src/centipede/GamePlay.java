package centipede;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
//        addKeyListener( new KeyAdapter()
//        {
            // keypressing
//            public void keyPressed( KeyEvent e )
//            {
//                // when the escape button is pressed (VK_SPACE), call reset
//                // game
//                if ( e.getKeyCode() == KeyEvent.VK_SPACE )
//                {
//                    resetGame();
//                }
//
//                if ( !grid.canMove() )
//                {
//                    myLose = true;
//                }
//
//                // connects the arrow keys to their movement functions in the
//                // GameGrid class; respectively, left, right, down, and up
//                if ( !myWin && !myLose )
//                {
//                    switch ( e.getKeyCode() )
//                    {
//                        case KeyEvent.VK_LEFT:
//                            grid.left();
//                            break;
//                        case KeyEvent.VK_RIGHT:
//                            grid.right();
//                            break;
//                        case KeyEvent.VK_DOWN:
//                            grid.down();
//                            break;
//                        case KeyEvent.VK_UP:
//                            grid.up();
//                            break;
//                    }
//                }
//
//                if ( !myWin && !grid.canMove() )
//                {
//                    myLose = true;
//                }
//
//                repaint();
//            }
//        } );
//
//        myScore = 0;
//        myWin = false;
//        myLose = false;
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
