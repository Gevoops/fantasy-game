package engine;

import components.Component;
import org.joml.Vector2f;
import renderer.RenderBatch;
import renderer.Transform;
import renderer.Sprite;
import util.Tiles;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int ID = -1;
    protected String name = "default";
    private Sprite sprite = null;
    private Transform transform = null;
    private Transform lastTransform = null;
    private boolean isDirty = true;
    private int height;
    private int zIndex = 0;
    private List<Component> components = new ArrayList<>();
    private int type = DEFAULT;

    private transient RenderBatch batch;
    private transient int renderBufferIndex;

    public static final int DEFAULT = 0;
    public static final int TILE = 1;
    public static final int PLAYER = 2;


    public GameObject(){

    }

    public GameObject(String name, Sprite sprite, Transform transform, int zIndex){
            this.name = name;
            this.sprite = sprite;
            this.transform = transform;
            this.lastTransform = transform.copy();
            this.zIndex = zIndex;
            this.ID = ++ID_COUNTER;
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
    public void setPosition(Vector2f position){
        this.transform.setPosition(position);
    }
    public static void init(int maxId){
        ID_COUNTER = maxId;
    }
    public int getID(){
        return ID;
    }
    public List<Component> getComponents() {
        return components;
    }

    public RenderBatch getBatch() {
        return batch;
    }

    public void setBatch(RenderBatch batch) {
        this.batch = batch;
    }

    public int getRenderBufferIndex() {
        return renderBufferIndex;
    }

    public void setRenderBufferIndex(int renderBufferIndex) {
        this.renderBufferIndex = renderBufferIndex;
    }

    public float getWorldX(){
        return  Tiles.worldToTileX(getX(),getY());
    }

    public float getWorldY(){
        return Tiles.worldToTileY(getX(),getY());
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
