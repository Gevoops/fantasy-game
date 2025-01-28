package editor;

import engine.GameObject;
import engine.MouseControllerStrategy;
import engine.MouseListener;
import engine.Window;
import exceptions.GameObjectNotFoundException;
import scenes.WorldEditorScene;
import util.Grid;
import util.Settings;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControllerEditor implements MouseControllerStrategy {
    private WorldEditorScene editorScene = WorldEditorScene.getInstance();
    private float orthoX;
    private float orthoY;
    private float viewPortX;
    private float viewPortY;
    private boolean leftDown;
    private boolean lastFrameLeftDown;
    private boolean wantCaptureMouse;


    public void update(float dt){
        getMouseState();
        dragObject();
        Window.getInstance().getScene().getCamera().zoom(MouseListener.getScrollY());
        if (leftDown && !lastFrameLeftDown){
            System.out.println(Window.getInstance().getPickingTexture().readIDFromPixel((int)viewPortX,(int)viewPortY));
            int id = Window.getInstance().getPickingTexture().readIDFromPixel((int)viewPortX,(int)viewPortY);
            try {
                editorScene.setActiveGameObject(editorScene.findGameObject(id));
            } catch (GameObjectNotFoundException e){
                e.printStackTrace();
            }

        }
    }

    private void place(){
        editorScene.setLiftedObject(null);
    }

    private void dragObject(){
        if(editorScene.getLiftedObject() != null){
            GameObject ob = editorScene.getLiftedObject();
            ob.setPosition(Grid.snapToGrid(orthoX,orthoY).add(-(ob.getTransform().scale.x - Settings.TILE_WIDTH) / 2,0));
            if(leftDown){
                place();
            }
        }
    }

    private void getMouseState(){
        orthoX = MouseListener.getOrthoX();
        orthoY = MouseListener.getOrthoY();
        viewPortX = MouseListener.getViewPortX();
        viewPortY = MouseListener.getViewPortY();
        lastFrameLeftDown = leftDown;
        leftDown = MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT);

    }
}
