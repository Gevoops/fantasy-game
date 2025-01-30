package editor;

import engine.GameObject;
import engine.MouseControllerStrategy;
import engine.MouseListener;
import engine.Window;
import exceptions.GameObjectNotFoundException;
import org.joml.Vector2f;
import scenes.WorldEditorScene;
import util.Grid;
import util.Settings;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControllerEditor implements MouseControllerStrategy {
    private WorldEditorScene editorScene = WorldEditorScene.getInstance();
    private GameObject liftedObject;
    Vector2f clickOrigin = new Vector2f();
    private float cameraDragDebounce = 0.032f;
    private float cameraDragSensitivity = 2f;
    private float orthoX;
    private float orthoY;
    private float viewPortX;
    private float viewPortY;
    private boolean leftDown;
    private boolean lastFrameLeftDown;
    private boolean middleDown;


    public void update(float dt){
        getMouseState();
        cameraDrag(dt);
        liftedObject = editorScene.getLiftedObject();

        if(liftedObject == null && leftDown && !lastFrameLeftDown){
            liftObject();
        } else if (liftedObject != null && leftDown && !lastFrameLeftDown) {
            placeObject();
        } else if (liftedObject != null){
            dragObject();
        }

        Window.getInstance().getScene().getCamera().zoom(MouseListener.getScrollY());
    }

    private void cameraDrag(float dt){
        if (middleDown && cameraDragDebounce > 0) {
            clickOrigin.x = orthoX;
            clickOrigin.y = orthoY;
            cameraDragDebounce -= dt;
        } else if(middleDown){
            Vector2f mousePos = new Vector2f(orthoX,orthoY);
            Vector2f delta = new Vector2f(mousePos).sub(clickOrigin);
            editorScene.getCamera().viewPoint.sub(delta.mul(dt).mul(cameraDragSensitivity));
            this.clickOrigin.lerp(mousePos, dt);
        }
        if (cameraDragDebounce <= 0 && !middleDown) {
            cameraDragDebounce = 0.032f;
        }
    }

    private void liftObject(){
        System.out.println(Window.getInstance().getPickingTexture().readIDFromPixel((int)viewPortX,(int)viewPortY));
        int id = Window.getInstance().getPickingTexture().readIDFromPixel((int)viewPortX,(int)viewPortY);
        try {
            GameObject ob = editorScene.findGameObject(id);
            editorScene.setActiveGameObject(ob);
            editorScene.setLiftedObject(ob);
        } catch (GameObjectNotFoundException e){
            e.printStackTrace();
        }
    }
    private void placeObject(){
        System.out.println("place");
        editorScene.setLiftedObject(null);
    }

    private void dragObject(){
        GameObject ob = editorScene.getLiftedObject();
        ob.setPosition(Grid.snapToGrid(orthoX,orthoY).add(-(ob.getTransform().scale.x - Settings.TILE_WIDTH) / 2,0));
    }

    private void getMouseState(){
        orthoX = MouseListener.getOrthoX();
        orthoY = MouseListener.getOrthoY();
        viewPortX = MouseListener.getViewPortX();
        viewPortY = MouseListener.getViewPortY();
        lastFrameLeftDown = leftDown;
        leftDown = MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT);
        middleDown = MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE);

    }
}
