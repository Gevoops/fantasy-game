package game;

import engine.GameObject;
import engine.MouseControllerStrategy;
import engine.MouseListener;
import engine.Window;
import scenes.WorldEditorScene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControllerGame implements MouseControllerStrategy {
        public void update(float dt){

            Window.getScene().getCamera().zoom(MouseListener.getScrollY());
        }
}
