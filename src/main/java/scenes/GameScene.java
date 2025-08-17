package scenes;

import editor.GameViewport;
import engine.GameObject;

public class GameScene extends Scene {
    private static GameScene instance;
    private GameObject player;
    private GameScene() {

    }




    @Override
    public GameViewport getGameViewport() {
        return null;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

    }

    @Override
    protected void loadResources() {

    }

    public static GameScene getInstance() {
        if (instance == null){
            instance = new GameScene();
        }
        return instance;
    }

    public void setPlayer(GameObject player){
        this.player = player;
    }
}
