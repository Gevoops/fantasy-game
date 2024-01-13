package engine;

public class GameScene extends Scene{
    public GameScene() {
        System.out.println("inside level Scene");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }

    @Override
    public void update(double dt) {

    }
}
