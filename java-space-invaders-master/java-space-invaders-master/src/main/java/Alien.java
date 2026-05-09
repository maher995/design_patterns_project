import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author 
 */
public class Alien extends Sprite implements AlienComponent {

    private Bomb bomb;
    private final String alien = "/img/alien.png";

    /*
     * Constructor
     */
    public Alien(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(this.getClass().getResource(alien));
        setImage(ii.getImage());

    }

    @Override
    public void draw(Graphics g, JPanel panel) {

    }

    public void act(int direction) {
        this.x += direction;
    }

    /*
     * Getters & Setters
     */
    
	public Bomb getBomb() {
		return bomb;
	}

}