import java.awt.Graphics;
import javax.swing.JPanel;

public interface AlienComponent {
    default void draw(Graphics g, JPanel panel) {}
    void act(int direction);
    boolean isVisible();
    void die();
}