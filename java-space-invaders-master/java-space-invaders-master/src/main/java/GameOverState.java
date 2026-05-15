import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOverState implements GameState {
    private Board board;

    public GameOverState(Board board) {
        this.board = board;
    }

    @Override
    public void update() {  }

    @Override
    public void draw(Graphics g) {
        board.drawGameEnd(g);
    }

    @Override
    public void handleInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            board.gameInit();
        }
    }
}