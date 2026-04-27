import javax.swing.ImageIcon;

public class SpriteFactory {

  // Shared image icon for aliens to avoid reloading it multiple times
    private final ImageIcon alienIcon = new ImageIcon(getClass().getResource("/img/alien.png"));

    public Object getSprite(String type, int x, int y) {
        if (type == null) return null;

        if (type.equalsIgnoreCase("ALIEN")) {
            Alien alien = new Alien(x, y);
            alien.setImage(alienIcon.getImage());
            return alien;
        } else if (type.equalsIgnoreCase("PLAYER")) {
            return new Player();
        } else if (type.equalsIgnoreCase("SHOT")) {
            return new Shot(x, y);
        }

        return null;
    }
}

