package spaceinvaders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.sound.sampled.*;

import sun.audio.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.geom.Point2D;
import java.awt.PointerInfo;

public class Board extends JPanel implements Runnable, Commons, MouseListener, MouseMotionListener {

    private Dimension d;
    private ArrayList<Alien> aliens;
    private ArrayList<Mushroom> mushrooms;
    private Player player;
    private Shot shot;
    private Spider spider;

    private final int ALIEN_INIT_X = 150;
    private final int ALIEN_INIT_Y = 5;

    private int deaths = 0;
    private int lives = 4;
    private int score = 0;

    private boolean ingame = true;
    private final String explImg = "src/images/fire.png";
    private String message = "Game Over";

    private Thread animator;

    public Board() {

        initBoard();
    }

    private void initBoard() {

        //addKeyListener(new TAdapter());
        addMouseListener(this);
        addMouseMotionListener(this);
        		
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);

        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {

        super.addNotify();
        gameInit();
    }

    public void gameInit() {
    	JLabel scoreLabel = new JLabel("Score: " + score);
    	scoreLabel.setBounds(5, 5, 50, 10);
    	
        aliens = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_ALIENS_TO_DESTROY; i++) {
        	Alien alien = new Alien(ALIEN_INIT_X + ALIEN_SIZE * i, ALIEN_INIT_Y + ALIEN_SIZE);
        	aliens.add(alien);
        }
        
        mushrooms = new ArrayList<>();
        
        for (int i = 0; i < MUSHROOM_NUMBER; i++) {
        	Random generator = new Random();
        	int mushroomx = generator.nextInt(24);
        	int mushroomy = generator.nextInt(20);
        	Mushroom m = new Mushroom(20 + mushroomx*10, 15 + mushroomy*10);
        	mushrooms.add(m);
        }

        player = new Player();
        shot = new Shot();
        spider = new Spider(SPIDER_START_X, SPIDER_START_Y);

        if (animator == null || !ingame) {

            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) {

        Iterator it = aliens.iterator();
        for (Alien alien: aliens) {
            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }
            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {

        if (player.isVisible()) {
        	Point p = MouseInfo.getPointerInfo().getLocation();
        	SwingUtilities.convertPointFromScreen(p, this);
        	player.setX((int)p.getX());
        	player.setY((int)p.getY());
            g.drawImage(player.getImage(), (int)p.getX(), (int)p.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            ingame = false;
        }
    }

    public void drawShot(Graphics g) {
        
        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    public void drawMushrooms(Graphics g) {

        for (Mushroom m : mushrooms) {
            if (!m.isDestroyed()) {
                g.drawImage(m.getImage(), m.getX(), m.getY(), this);
            }
        }
    }
    
    public void drawSpider(Graphics g) {
    	if (spider.isVisible()) {
    		g.drawImage(spider.getImage(), spider.getX(), spider.getY(), this);
    	}
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {
            g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
            g.drawString("Score: " + score + " Lives: " + lives, 5, 15);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawMushrooms(g);
            drawSpider(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {

        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }

    public void animationCycle() {

        if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame = false;
            message = "Game won!";
        }

        // player
        player.act();

        // shot
        if (shot.isVisible()) {
        	//Pew.SHOOT.play();
            int shotX = shot.getX();
            int shotY = shot.getY();

            for (Alien alien: aliens) {

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + ALIEN_HEIGHT)) {
                    	alien.isShot();
                    	if(alien.getLives() == 0) {
                    		score += 5;
                    		alien.die();
                    	} else {
                    		score += 2;
                    	}
                    	alien.setDying(true);
                        shot.die();
                    }
                }
            }
            //Shoot a Mushroom
            for (Mushroom m: mushrooms) {

                int mushroomX = m.getX();
                int mushroomY = m.getY();

                if (m.isVisible() && shot.isVisible()) {
                    if (shotX >= (mushroomX)
                            && shotX <= (mushroomX + ALIEN_WIDTH)
                            && shotY >= (mushroomY)
                            && shotY <= (mushroomY + ALIEN_HEIGHT)) {
                    	m.isShot();
                    	if(m.getLives() == 0) {
                    		score += 5;
                    		m.die();
                    	} else {
                    		score++;
                    	}
                        m.setDying(true);
                        shot.die();
                    }
                }
            }
            //Shoot the Spider
            int spiderX = spider.getX();
            int spiderY = spider.getY();
            
            if(spider.isVisible() && shot.isVisible()) {
            	if (shotX >= (spiderX)
                        && shotX <= (spiderX + ALIEN_WIDTH)
                        && shotY >= (spiderY)
                        && shotY <= (spiderY + ALIEN_HEIGHT)) {
            		spider.isShot();
            		if(spider.getLives() == 0) {
            			score += 600;
            			spider.die();
            		} else {
            			score += 100;
            		}
            		spider.setDying(true);
            		shot.die();
            	}
            }

            int y = shot.getY();
            y -= 4;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // alien movement
        for (Alien alien: aliens) {
        	alien.act();
        	int alienx = alien.getX();
            int alieny = alien.getY();
            //5 points for going back and forth and not going down further when it enters the top of the player area.
            if(alieny >= GROUND - ALIEN_HEIGHT) {
            	if ((alienx >= BOARD_WIDTH - 2 * ALIEN_WIDTH) && alien.getDirection() != -1) {
            		alien.setDirection(-1);
            	}
            	if (alienx <= BORDER_LEFT && alien.getDirection() != 1) {
	            	alien.setDirection(1);
	            }
            } //move the alien down
            else {
	            if ((alienx >= BOARD_WIDTH - 2 * ALIEN_WIDTH) && alien.getDirection() != -1) {
	            	alien.collision();
	            }
	            if (alienx <= BORDER_LEFT && alien.getDirection() != 1) {
	            	alien.collision();
	            }
	            //Not that efficient but oh well
	            for (Mushroom m: mushrooms) {
	            	int mushroomx = m.getX();
	            	int mushroomy = m.getY();
	            	if (alien.isVisible() && m.isVisible()) {
	            		if (mushroomx >= (alienx) && mushroomx <= (alienx + ALIEN_WIDTH) && 
	            				mushroomy >= (alieny) && mushroomy <= (alieny + ALIEN_HEIGHT)) {
	            			alien.collision();
	            		}
	            	}
	            }
        }
            //end game
            int playerX = player.getX();
            int playerY = player.getY();
            if (alien.isVisible() && player.isVisible()) {
                if (playerX >= (alienx)
                        && playerX <= (alienx + ALIEN_WIDTH)
                        && playerY >= (alieny)
                        && playerY <= (alieny + ALIEN_HEIGHT)) {
                    shot.die();
                    player.die();
                    lives--;
                    if(lives == 0) {
                    	ingame = false;
                    }
                    restart();
                }
            }
            
        }
        //spider
        Random generator = new Random();
        boolean setNewDirection = false;

        int spiderX = spider.getX();
        int spiderY = spider.getY();
        int curDir = spider.getDirection();
        int movesSinceLastChange = spider.getMovesSinceLastChange();

        if(curDir == UP_RIGHT) {
        	spider.setX(spiderX + 10);
        	spider.setY(spiderY - 10);
        	System.out.println("upright");
        } else if (curDir == DOWN_RIGHT) {
        	spider.setX(spiderX + 1);
        	spider.setY(spiderY + 1);
        	System.out.println("downright");
        } else if (curDir == UP_LEFT) {
        	spider.setX(spiderX - 1);
        	spider.setY(spiderY - 1);
        	System.out.println("upleft");
        } else if (curDir == DOWN_LEFT) {
        	spider.setX(spiderX - 1);
        	spider.setY(spiderY + 1);
        	System.out.println("downleft");
        } else if (curDir == UP) {
        	spider.setY(spiderY - 1);
        	System.out.println("up");
        } else {
        	spider.setY(spiderY + 1);
        	System.out.println("down");
        }

        spider.setMovesSinceLastChange(movesSinceLastChange++);
        
        if ((spiderY >= GROUND - ALIEN_HEIGHT) || (spiderX >= BOARD_WIDTH - 2 * ALIEN_WIDTH) 
        		|| (spiderX <= BORDER_LEFT + ALIEN_WIDTH) || (spiderY <= BORDER_LEFT + ALIEN_SIZE)) {
        	setNewDirection = true;
        }
        // as the spider approaches 5 moves on its current path, increase odds of changing
        setNewDirection = (setNewDirection || generator.nextInt(6-movesSinceLastChange) == 0);

        if(setNewDirection) {
        	spider.setDirection(generator.nextInt(6)); 
        	spider.setMovesSinceLastChange(0);
        }
        
        //Player collision with spider
        int playerX = player.getX();
        int playerY = player.getY();
        if (spider.isVisible() && player.isVisible()) {
            if (playerX >= (spiderX)
                    && playerX <= (spiderX + ALIEN_WIDTH)
                    && playerY >= (spiderY)
                    && playerY <= (spiderY + ALIEN_HEIGHT)) {
                shot.die();
                player.die();
                lives--;
                //restart();
                if(lives == 0) {
                	ingame = false;
                }
            }
        }
        
        if(aliens.size() == 0) {
        	restart();
        	score += 600;
        }
        
//        for (Alien alien: aliens) {
//
//            int shot = generator.nextInt(15);
//
//            if (shot == CHANCE && alien.isVisible() && b.isDestroyed()) {
//
//                b.setDestroyed(false);
//                b.setX(alien.getX());
//                b.setY(alien.getY());
//            }
//
//            int bombX = b.getX();
//            int bombY = b.getY();
//            int playerX = player.getX();
//            int playerY = player.getY();
//
//            if (player.isVisible() && !b.isDestroyed()) {
//
//                if (bombX >= (playerX)
//                        && bombX <= (playerX + PLAYER_WIDTH)
//                        && bombY >= (playerY)
//                        && bombY <= (playerY + PLAYER_HEIGHT)) {
//                    ImageIcon ii
//                            = new ImageIcon(explImg);
//                    player.setImage(ii.getImage());
//                    player.setDying(true);
//                    b.setDestroyed(true);
//                }
//            }
//
//            if (!b.isDestroyed()) {
//                
//                b.setY(b.getY() + 1);
//                
//                if (b.getY() >= GROUND - BOMB_HEIGHT) {
//                    b.setDestroyed(true);
//                }
//            }
//        }
    }
    public void restart() {
    	
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {

            repaint();
            animationCycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            
            beforeTime = System.currentTimeMillis();
        }

        gameOver();
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = player.getX();
        int y = player.getY();
		if (ingame) {
            if (!shot.isVisible()) {
                shot = new Shot(x, y);
            }
        }
		System.out.println("Mouse Clicked");
		//Pew p = new Pew();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//eventOutput("Mouse pressed (# of clicks: " + e.getClickCount() + ")", e);
		System.out.println("Mouse Pressed");
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("Mouse Released");
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("Mouse Entered");
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//System.out.println("Mouse Exited");
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//System.out.println("why tho");
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}
}
