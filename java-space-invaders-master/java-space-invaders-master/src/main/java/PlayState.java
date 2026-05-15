import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayState implements GameState {
    private Board board;

    public PlayState(Board board) {
        this.board = board;
    }

    @Override
    public void update() {
        board.player.act();
        board.updateAliens();
        board.checkCollisions();
    }

    @Override
    public void draw(Graphics g) {
        board.drawAliens(g);
        board.drawPlayer(g);
        board.drawShot(g);
    }

    @Override
    public void handleInput(KeyEvent e) {
        board.player.keyPressed(e);
    }
}