package engine;

import components.Component;
import org.joml.Vector2d;
import org.joml.Vector4f;
import renderer.RenderBatch;
import renderer.Transform;
import renderer.Sprite;
import tiles.TileGrid;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int ID = -1;
    protected String name = "default";
    private Sprite sprite = null;
    private Transform transform = null;
    private Transform lastTransform = null;
    private float height;
    private float elevation;
    private List<Component> components = new ArrayList<>();
    private int type = DEFAULT;
    private boolean dynamic;
    private boolean isDirty = true;
    private boolean deathMark = false;


    private transient RenderBatch batch;
    private transient Long tileMapKey = null;



    public static final int DEFAULT = 0;
    public static final int TILE = 1;
    public static final int PLAYER = 2;


    public GameObject(){

    }

    public GameObject(String name, Sprite sprite, Transform transform, int type){
            this.name = name;
            this.sprite = sprite;
            this.transform = transform;
            this.lastTransform = transform.copy();
            this.ID = ++ID_COUNTER;
            this.type = type;
    }

    public GameObject(GameObject go) {
        this.name = go.name;
        this.sprite = new Sprite(go.sprite);
        this.transform = go.transform.copy();
        this.lastTransform = go.lastTransform.copy();
        this.ID = ++ID_COUNTER;
        this.type = go.type;

    }

    public void update(float dt) {
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



    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
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
    public void moveX(double offset){
        transform.position.x += offset;
    }
    public void moveY(double offset){
        transform.position.y += offset;
    }
    public double getX(){
        return transform.position.x;
    }
    public double getY(){
        return  transform.position.y;
    }
    public void setX(double x){
        this.transform.position.x = x;
    }
    public void setY(double y){
        this.transform.position.y = y;
    }
    public void setPosition(Vector2d position){
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

    public RenderBatch getRenderBatch() {
        return batch;
    }

    public void setBatch(RenderBatch batch) {
        this.batch = batch;
    }

    public double getTileCoordsY(){
        return  TileGrid.worldToTileY(getX(),getY());
    }

    public double getTileCoordsX(){
        return TileGrid.worldToTileX(getX(),getY());
    }

    public Vector2d getCurrentTile(){


        return TileGrid.tileAt(getX() + transform.scale.x/2 ,getY() + 1);

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAlpha(float alpha) {
        this.sprite.color.set(new Vector4f(1,1,1,alpha));
    }

    public Long getTileMapKey() {
        return tileMapKey;
    }

    public void setTileMapKey(Long tileMapKey) {
        this.tileMapKey = tileMapKey;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public boolean isDeathMarked() {
        return deathMark;
    }

    public void setDeathMark(boolean deathMark) {
        this.deathMark = deathMark;
    }
}
