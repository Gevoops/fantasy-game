package editor;

import engine.GameObject;
import engine.MouseControllerStrategy;
import engine.MouseListener;
import engine.Window;
import exceptions.GameObjectNotFoundException;
import org.joml.Vector2f;
import scenes.WorldEditorScene;
import util.Tiles;
import util.Settings;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControllerEditor implements MouseControllerStrategy {
    private WorldEditorScene editorScene = WorldEditorScene.getInstance();
    Vector2f clickOrigin = new Vector2f();
    private float cameraDragDebounce = 0.032f;
    private double orthoX, orthoY, viewPortX, viewPortY, x, y;
    private boolean leftDown;
    private boolean lastFrameLeftDown;
    private boolean middleDown;


    public void update(float dt){
        getMouseState();
        cameraDrag(dt);
        GameObject liftedObject = editorScene.getLiftedObject();

        if(liftedObject == null && leftDown && !lastFrameLeftDown){
            System.out.println("try lift");
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
            clickOrigin.x = (float) orthoX;
            clickOrigin.y = (float) orthoY;
            cameraDragDebounce -= dt;
        } else if(middleDown){
            Vector2f mousePos = new Vector2f((float)orthoX,(float)orthoY);
            Vector2f delta = new Vector2f(mousePos).sub(clickOrigin);
            editorScene.getCamera().viewPoint.sub(delta.mul(dt).mul(Settings.CAMERA_DRAG));
            this.clickOrigin.lerp(mousePos, dt);
        }
        if (cameraDragDebounce <= 0 && !middleDown) {
            cameraDragDebounce = 0.032f;
        }
    }

    private void liftObject(){
        int id = Window.getInstance().getPickingTexture().readIDFromPixel((int)(viewPortX) ,(int) (viewPortY));
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
        editorScene.setActiveGameObject(editorScene.getLiftedObject());
        editorScene.setLiftedObject(null);
    }

    private void dragObject(){
        GameObject ob = editorScene.getLiftedObject();
        ob.setPosition(Tiles.snapToTile((float) orthoX, (float)orthoY).add(-(ob.getTransform().scale.x - Settings.TILE_WIDTH) / 2,0));
    }

    private void getMouseState(){
        x = MouseListener.getX();
        y = MouseListener.getY();
        orthoX = MouseListener.getOrthoX();
        orthoY = MouseListener.getOrthoY();
        viewPortX = MouseListener.getViewPortX();
        viewPortY = MouseListener.getViewPortY();
        lastFrameLeftDown = leftDown;
        leftDown = MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT);
        middleDown = MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE);
    }
}
