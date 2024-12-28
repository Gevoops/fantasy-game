package components;

import engine.GameObject;
import engine.MouseListener;
import engine.Window;
import scenes.WorldEditorScene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseController extends Component{
    GameObject liftedObject = null;
    private int mouseCaptureMode = 0;
    private final int MAX_MODE = 2;
    private final int PLAYER_WALK = 0;
    private final int MENU = 1;
    private final int SELECT_OBJECT = 2;

    public void setMouseCaptureMode(int mode){
        if (mode < 0 || mode > MAX_MODE){
            return;
        }
        switch (mouseCaptureMode){
            case 0:
                break;
            case 1:
                System.out.println("case 1 mouse control");
                break;
        }
    }

    public void liftObject(GameObject ob){
        this.liftedObject = ob;
        Window.getScene().addGameObjectToScene(ob);
    }

    public void place(){
        liftedObject.setPosition(((WorldEditorScene)Window.getScene()).snapToGrid(MouseListener.getOrthoX(),MouseListener.getOrthoY()));
        this.liftedObject = null;
    }

    @Override
    public void update(double dt){
        if(liftedObject != null){
            liftedObject.setX(MouseListener.getOrthoX() - liftedObject.getTransform().scale.x /2);
            liftedObject.setY(MouseListener.getOrthoY() - liftedObject.getTransform().scale.y /2);


            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
        Window.getScene().getCamera().scaleUpdate(MouseListener.getScrollY());
    }
}
