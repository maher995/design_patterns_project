import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    // Pattern implementations
    private GameSettings settings = GameSettings.getInstance();
    private SpriteFactory spriteFactory = new SpriteFactory();
    private GameFacade gameFacade = new GameFacade(this);

    private Dimension d;
    private ArrayList aliens;
    private Player player;
    private Shot shot;
    private GameOver gameend;
    private Won vunnet;

    private int alienX = 150;
    private int alienY = 25;
    private int direction = -1;
    private int deaths = 0;

    private boolean ingame = true;
    private boolean havewon = true;
    private final String expl = "/img/explosion.png";
    private String message = "Seu planeta nos pertence agora...";

    private Thread animator;

    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true);

        d = new Dimension(settings.BOARD_WIDTH, settings.BOARD_HEIGHT);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {

        aliens = new ArrayList();

        // Refactored: Using Factory to create Aliens
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = (Alien) spriteFactory.getSprite("ALIEN",
                        alienX + 18 * j, alienY + 18 * i);
                aliens.add(alien);
            }
        }

        // Refactored: Using Factory to create Player and initial Shot
        player = (Player) spriteFactory.getSprite("PLAYER", 0, 0);
        shot = (Shot) spriteFactory.getSprite("SHOT", 0, 0);

        if (animator == null || !ingame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) {

        Iterator it = aliens.iterator();

        while (it.hasNext()) {

            Alien alien = (Alien) it.next();

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
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            player.die();
            havewon = false;
            ingame = false;
        }
    }

    public void drawShot(Graphics g) {

        if (shot.isVisible()) {
            g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
        }
    }

    public void drawBombing(Graphics g) {

        Iterator i3 = aliens.iterator();

        while (i3.hasNext()) {

            Alien a = (Alien) i3.next();
            Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this);
            }
        }
    }

    public void drawGround(Graphics g) {
       g.drawLine(0, settings.GROUND, settings.BOARD_WIDTH, settings.GROUND);
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);
        g.setColor(Color.green);

        if (ingame) {
            // Facade Pattern: draw all game elements through GameFacade
            gameFacade.drawGame(g);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void gameOver() {

        Graphics g = this.getGraphics();

        gameend = new GameOver();
        vunnet = new Won();

        g.fillRect(0, 0, settings.BOARD_WIDTH, settings.BOARD_HEIGHT);

        if (havewon) {
            g.drawImage(vunnet.getImage(), 0, 0, this);
        } else {
            g.drawImage(gameend.getImage(), 0, 0, this);
        }

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, settings.BOARD_WIDTH / 2 - 30,
                settings.BOARD_WIDTH - 100, 50);

        g.setColor(Color.white);
        g.drawRect(50, settings.BOARD_WIDTH / 2 - 30,
                settings.BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message,
                (settings.BOARD_WIDTH - metr.stringWidth(message)) / 2,
                settings.BOARD_WIDTH / 2);
    }

    public void animationCycle() {

        if (deaths == settings.NUMBER_OF_ALIENS_TO_DESTROY) {
            ingame = false;
            message = "Parabéns! Você salvou a galáxia!";
        }

        player.act();

        if (shot.isVisible()) {

            Iterator it = aliens.iterator();

            int shotX = shot.getX();
            int shotY = shot.getY();

            while (it.hasNext()) {

                Alien alien = (Alien) it.next();

                int alienX = alien.getX();
                int alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {

                    if (shotX >= alienX
                            && shotX <= alienX + settings.ALIEN_WIDTH
                            && shotY >= alienY
                            && shotY <= alienY + settings.ALIEN_HEIGHT) {

                        ImageIcon ii = new ImageIcon(getClass().getResource(expl));
                        alien.setImage(ii.getImage());
                        alien.setDying(true);
                        deaths++;
                        shot.die();
                    }
                }
            }

            int y = shot.getY();
            y -= 8;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // Aliens Movement
        Iterator it1 = aliens.iterator();

        while (it1.hasNext()) {

            Alien a1 = (Alien) it1.next();
            int x = a1.getX();

            if (x >= settings.BOARD_WIDTH - settings.BORDER_RIGHT && direction != -1) {

                direction = -1;

                Iterator i1 = aliens.iterator();

                while (i1.hasNext()) {
                    Alien a2 = (Alien) i1.next();
                    a2.setY(a2.getY() + settings.GO_DOWN);
                }
            }

            if (x <= settings.BORDER_LEFT && direction != 1) {

                direction = 1;

                Iterator i2 = aliens.iterator();

                while (i2.hasNext()) {
                    Alien a = (Alien) i2.next();
                    a.setY(a.getY() + settings.GO_DOWN);
                }
            }
        }

        Iterator it = aliens.iterator();

        while (it.hasNext()) {

            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > settings.GROUND - settings.ALIEN_HEIGHT) {
                    havewon = false;
                    ingame = false;
                    message = "Aliens estão invadindo a galáxia!";
                }

                alien.act(direction);
            }
        }

        // Bombs
        Iterator i3 = aliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {

            int chance = generator.nextInt(15);

            Alien a = (Alien) i3.next();
            Bomb b = a.getBomb();

            if (chance == settings.CHANCE && a.isVisible() && b.isDestroyed()) {
                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());
            }

            if (player.isVisible() && !b.isDestroyed()) {

                if (b.getX() >= player.getX()
                        && b.getX() <= player.getX() + settings.PLAYER_WIDTH
                        && b.getY() >= player.getY()
                        && b.getY() <= player.getY() + settings.PLAYER_HEIGHT) {

                    ImageIcon ii = new ImageIcon(getClass().getResource(expl));
                    player.setImage(ii.getImage());
                    player.setDying(true);
                    b.setDestroyed(true);
                }
            }

            if (!b.isDestroyed()) {

                b.setY(b.getY() + 1);

                if (b.getY() >= settings.GROUND - settings.BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    public void fireShot() {

        if (!shot.isVisible()) {
            // Refactored: Using Factory for creating shots
            shot = (Shot) spriteFactory.getSprite("SHOT", player.getX(), player.getY());
        }
    }

    @Override
    public void run() {

        long beforeTime;
        long timeDiff;
        long sleep;

        beforeTime = System.currentTimeMillis();

        while (ingame) {

            repaint();

            // Facade Pattern: update game through GameFacade
            gameFacade.updateGame();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = settings.DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 1;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            beforeTime = System.currentTimeMillis();
        }

        // Facade Pattern: show final game result through GameFacade
        gameFacade.showGameResult();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            player.keyPressed(e);

            if (ingame && e.getKeyCode() == KeyEvent.VK_SPACE) {
                // Facade Pattern: fire shot through GameFacade
                gameFacade.fireShot();
            }
        }
    }
}