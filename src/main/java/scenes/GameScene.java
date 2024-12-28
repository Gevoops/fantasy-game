package scenes;

import engine.Window;

public class GameScene extends Scene {
    public GameScene() {
        System.out.println("inside level Scene");
        Window.getWindow().r = 1;
        Window.getWindow().g = 1;
        Window.getWindow().b = 1;
    }

    @Override
    public void update(float dt) {

    }
}
