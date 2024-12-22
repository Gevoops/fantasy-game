package engine;

import components.Component;
import renderer.Transform;
import imgui.ImGui;
import org.joml.Vector4f;
import renderer.Sprite;
import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = - 1;

    protected String name = "default";
    private Sprite sprite = null;
    private Transform transform = null;
    private Transform lastTransform = null;
    private boolean isDirty = true;
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

            this.uid = ID_COUNTER++;
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
        for (Component component : components) {
            component.start();
        }
    }

    public boolean isDirty() {
        return isDirty;
    }



    public void imGui(){
        for (Component c : components){
            c.imGui();
        }
    }

    public void addComponent(Component c){
        c.generateId();
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

    public Sprite getSprite() {
        return sprite;
    }

    public int getZIndex() {
        return this.zIndex;
    }


    public String getName() {
        return name;
    }

    public Transform getTransform() {
        return transform;
    }

    public Transform getLastTransform() {
        return lastTransform;
    }

    public void moveX(float offset){
        transform.position.x += offset;
    }
    public void moveY(float offset){
        transform.position.y += offset;
    }

    public float getX(){
        return transform.position.x;
    }
    public float getY(){
        return  transform.position.y;
    }

    public void setX(float x){
        this.transform.position.x = x;
    }
    public void setY(float y){
        this.transform.position.y = y;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }

    public int getUid(){
        return uid;
    }

    public List<Component> getComponents() {
        return components;
    }
}
