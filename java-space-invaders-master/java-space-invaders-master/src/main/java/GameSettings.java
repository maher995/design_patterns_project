public class GameSettings {
    private static GameSettings instance;

    public final int BOARD_WIDTH = 640;
    public final int BOARD_HEIGHT = 480;
    public final int GROUND = 440;
    public final int BOMB_HEIGHT = 5;
    public final int ALIEN_HEIGHT = 25;
    public final int ALIEN_WIDTH = 25;
    public final int BORDER_RIGHT = 30;
    public final int BORDER_LEFT = 30;
    public final int GO_DOWN = 25;
    public final int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    public final int CHANCE = 5;
    public final int DELAY = 17;
    public final int PLAYER_WIDTH = 25;
    public final int PLAYER_HEIGHT = 25;


    private GameSettings() {}


    public static GameSettings getInstance() {
        if (instance == null) {
            instance = new GameSettings();
        }
        return instance;
    }
}