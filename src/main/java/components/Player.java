package components;

import engine.Camera;
import engine.Window;
import org.joml.Vector2f;
import scenes.Scene;

public class Player extends Component{

    @Override
    public void start(){
        Scene scene = Window.getScene();
        Camera camera = scene.getCamera();
        scene.setPlayer(this.gameObject);
        camera.setViewPoint(new Vector2f(this.gameObject.getTransform().position.x - camera.getProjectionSize().x  / 2,
                this.gameObject.getTransform().position.y - camera.getProjectionSize().y / 2));
    }
}
