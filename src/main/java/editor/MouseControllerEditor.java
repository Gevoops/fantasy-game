package editor;

import engine.GameObject;
import engine.MouseControllerStrategy;
import engine.MouseListener;
import engine.Window;
import scenes.WorldEditorScene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControllerEditor implements MouseControllerStrategy {
    private WorldEditorScene editorScene = WorldEditorScene.getInstance();

    public void update(float dt){
        if(editorScene.getLiftedObject() != null){
            GameObject ob = editorScene.getLiftedObject();
            ob.setX(MouseListener.getOrthoX() - ob.getTransform().scale.x /2);
            ob.setY(MouseListener.getOrthoY() - ob.getTransform().scale.y /2);
            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
        Window.getScene().getCamera().zoom(MouseListener.getScrollY());
    }

    private void place(){
        editorScene.getLiftedObject().setPosition(editorScene.snapToGrid(MouseListener.getOrthoX(),MouseListener.getOrthoY()));
        editorScene.setLiftedObject(null);
    }

}
