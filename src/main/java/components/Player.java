package components;

import engine.Camera;
import engine.Window;
import org.joml.Vector2f;
import scenes.Scene;

public class Player extends Component{

    @Override
    public void start(){
        Scene scene = Window.getInstance().getScene();
        Camera camera = scene.getCamera();
        scene.setPlayer(this.gameObject);
        camera.setViewPoint(new Vector2f(this.gameObject.getX() - camera.getProjectionSize().x  / 2,
                this.gameObject.getY() - camera.getProjectionSize().y / 2));
    }
}
