package components;

import engine.GameObject;
import engine.MouseListener;
import engine.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component{
    GameObject liftedObject = null;


    public void liftObject(GameObject ob){
        this.liftedObject = ob;
        Window.getScene().addGameObjectToScene(ob);
    }

    public void place(){
        this.liftedObject = null;
    }

    @Override
    public void update(double dt){
        if(liftedObject != null){
            liftedObject.setX(MouseListener.getOrthoX() - 16);
            liftedObject.setY(MouseListener.getOrthoY() - 16);

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                place();
            }
        }
    }
}
