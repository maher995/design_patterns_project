import java.awt.Graphics;
import java.awt.event.KeyEvent;

public interface GameState {
    void update();
    void draw(Graphics g);
    void handleInput(KeyEvent e);
}
