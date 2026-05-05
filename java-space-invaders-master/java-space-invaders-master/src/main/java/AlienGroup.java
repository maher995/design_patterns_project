import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;

public class AlienGroup implements AlienComponent {

    private ArrayList<Alien> members = new ArrayList<>();

    public void add(Alien a) {
        members.add(a);
    }

    public ArrayList<Alien> getMembers() {
        return members;
    }

    @Override
    public void draw(Graphics g, JPanel panel) {
        for (Alien a : members) {
            if (a.isVisible()) {
                g.drawImage(a.getImage(), a.getX(), a.getY(), panel);
            }
            if (a.isDying()) {
                a.die();
            }
        }
    }

    @Override
    public void act(int direction) {
        for (Alien a : members) {
            if (a.isVisible()) {
                a.act(direction);
            }
        }
    }

    @Override
    public boolean isVisible() {
        for (Alien a : members) {
            if (a.isVisible()) return true;
        }
        return false;
    }

    @Override
    public void die() {
        for (Alien a : members) {
            a.die();
        }
    }
}