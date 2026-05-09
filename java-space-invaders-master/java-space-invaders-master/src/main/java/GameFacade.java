import java.awt.Graphics;

public class GameFacade {

    private Board board;

    public GameFacade(Board board) {
        this.board = board;
    }

    public void drawGame(Graphics g) {
        board.drawGround(g);
        board.drawAliens(g);
        board.drawPlayer(g);
        board.drawShot(g);
        board.drawBombing(g);
    }

    public void updateGame() {
        board.animationCycle();
    }

    public void fireShot() {
        board.fireShot();
    }

    public void showGameResult() {
        board.gameOver();
    }
}