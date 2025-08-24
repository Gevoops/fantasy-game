package editor;

import engine.GameObject;
import engine.MouseControllerStrategy;
import engine.MouseListener;
import engine.Window;
import org.joml.Vector2f;
import scenes.WorldEditorScene;
import util.Tiles;
import util.Settings;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControllerEditor implements MouseControllerStrategy {
    private WorldEditorScene editorScene;
    Vector2f clickOrigin = new Vector2f();
    private float cameraDragDebounce = 0.032f;
    private double orthoX, orthoY, cursorViewPortX, cursorViewPortY, x, y;
    private boolean leftDown;
    private boolean lastFrameLeftDown;
    private boolean middleDown;
    private GameObject liftedObject;


    public MouseControllerEditor(WorldEditorScene scene){
        this.editorScene = scene;
    }

    public void update(float dt){
        getMouseState();

        cameraDrag(dt);
        if (!editorScene.getEditorWindow().wantCaptureMouse(x,y) ){
            editorScene.getCamera().zoom(MouseListener.getScrollY());
        }

        liftedObject = editorScene.getLiftedObject();
        if(liftedObject == null && leftDown && !lastFrameLeftDown){
            int id = Window.getInstance().getPickingTexture().readIDFromPixel((int)cursorViewPortX ,(int)cursorViewPortY);
            GameObject go = editorScene.findGameObject(id);
            editorScene.liftObject(go);
        } else if (liftedObject != null && leftDown && !lastFrameLeftDown) {
            editorScene.placeObject();
        } else if (liftedObject != null){
            editorScene.dragObject(orthoX,orthoY);
        }


    }

    @Override
    public void handleRightClick() {
        if(liftedObject != null){
           System.out.println( editorScene.deleteGameObj(liftedObject));
        }
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






    private void getMouseState(){
        x = MouseListener.getX();
        y = MouseListener.getY();
        orthoX = MouseListener.getOrthoX();
        orthoY = MouseListener.getOrthoY();
        cursorViewPortX = MouseListener.getViewPortX();
        cursorViewPortY = MouseListener.getViewPortY();
        lastFrameLeftDown = leftDown;
        leftDown = MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT);
        middleDown = MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE);
    }
}
