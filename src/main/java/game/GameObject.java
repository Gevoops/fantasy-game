package game;

import engine.Component;
import renderer.Transform;
import imgui.ImGui;
import org.joml.Vector4f;
import renderer.Sprite;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    protected String name = "default";
    public Sprite sprite = null;
    public Transform transform = null;
    public Transform lastTransform = null;
    public boolean isDirty = true;
    private int zIndex = 0;
    private List<Component> components = new ArrayList<>();


    public GameObject(){

    }

    public GameObject(String name, Sprite sprite, Transform transform, int zIndex){
            this.name = name;
            this.sprite = sprite;
            this.transform = transform;
            this.lastTransform = transform.copy();
            this.zIndex = zIndex;
    }

    public void update(double dt) {
        if(!this.transform.equals(lastTransform)) {
            this.transform.copy(lastTransform);
            isDirty = true;
        }
        for (Component component : components) {
            component.update(dt);
        }
    }

    public void start(){
        for (int i = 0; i <  components.size(); i ++) {
            components.get(i).start();
        }
    }

    public boolean isDirty() {
        return isDirty;
    }



    public void imGui(){
        Vector4f color = sprite.getColor();
        float[] colors = {color.x,color.y,color.w,color.z};
        if(ImGui.colorPicker4("picker",colors)) {
            this.sprite.setColor(new Vector4f(colors[0],colors[1],colors[2],colors[3]));
            this.isDirty = true;
        }
    }

    public void addComponent(Component c){
        this.components.add(c);
        c.gameObject = this;
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for(Component c : components) {
            if(componentClass.isAssignableFrom(c.getClass())){
                return  componentClass.cast(c);
            }
        }
        return null;
    }

    public <T extends Component> void  removeComponent(Class<T> componentClass){
        for (int i = 0 ; i < components.size(); i ++) {
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }


    public void setClean() {
        this.isDirty = false;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setTransform(Transform transform) {
        this.transform = transform;
        lastTransform = transform.copy();

    }

    public void setLastTransform(Transform lastTransform) {
        this.lastTransform = lastTransform;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
    }

    public int getZIndex() {
        return this.zIndex;
    }


    public String getName() {
        return name;
    }
}
