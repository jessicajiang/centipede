package spaceinvaders;

import javax.swing.ImageIcon;

public class Spider extends Sprite implements Commons{

	private final String SpiderImg = "src/images/fullheart.png";
	private final String halfHeart = "src/images/halfheart.png";
	private boolean destroyed;
	private int lives = 2;
	private int direction = UP_RIGHT;
	private int moves = 0;

    public Spider(int x, int y) {
        initSpider(x, y);
    }

    private void initSpider(int x, int y) {

        this.x = x;
        this.y = y;

        ImageIcon ii = new ImageIcon(SpiderImg);
        setImage(ii.getImage());
    }
    
    public void setDestroyed(boolean destroyed) {
        
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
    
        return destroyed;
    }
    
    public void act() {
    	this.x += this.direction;
    }
    
    public void setDirection(int direction) {
    	this.direction = direction;
    }
    
    public int getDirection() {
    	return this.direction;
    }
    
    public int getLives() {
    	return this.lives;
    }
    
    public void setMovesSinceLastChange(int moves) {
    	this.moves = moves;
    }
    
    public int getMovesSinceLastChange() {
    	return this.moves;
    }
    
    public void isShot() {
    	this.lives--;
    	if (this.lives == 0) {
    		setDestroyed(true);
    	} else if (this.lives == 1) {
    		ImageIcon ii = new ImageIcon(halfHeart);
    		setImage(ii.getImage());
    	} else {
    		System.out.println("Error");
    	}
    }
}

