import javax.swing.*;
import java.awt.*;

public class Alien extends Sprite implements AlienComponent {

    private Bomb bomb;
    private final String alien = "/img/alien.png";
    // ADDED: Observer reference
    private SpriteObserver observer;

    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(this.getClass().getResource(alien));
        setImage(ii.getImage());
    }

    // ADDED: Method to register the observer (usually the Board)
    public void setObserver(SpriteObserver observer) {
        this.observer = observer;
    }

    @Override
    public void setDying(boolean dying) {
        super.setDying(dying);
        // NOTIFY: When the alien dies, tell the observer
        if (dying && observer != null) {
            observer.onSpriteDie(this);
        }
    }

    @Override
    public void draw(Graphics g, JPanel panel) {}

    public void act(int direction) {
        this.x += direction;
    }
    
    public Bomb getBomb() {
        return bomb;
    }
}