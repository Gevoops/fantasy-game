package scenes;

import editor.EditorWindow;
import editor.GameViewport;
import editor.MouseControllerEditor;
import editor.InspectorWindow;
import engine.*;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;

import renderer.SpriteSheet;

import util.AssetPool;
import util.Settings;
import util.Tiles;

import java.util.HashMap;
import java.util.Map;

import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;


public class WorldEditorScene extends Scene {
    private static WorldEditorScene instance;
    private GameViewport gameViewport;
    private EditorWindow editorWindow;
    private InspectorWindow inspectorWindow;
    private GameObject liftedObject;
    private GameObject activeGameObject;
    private Map<Long, GameObject> tileMap = new HashMap<>();


    private WorldEditorScene() {}

    public static WorldEditorScene getInstance(){
        if (instance == null){
            instance = new WorldEditorScene();
        }
        return instance;
    }

    @Override
    public void imGui() {
        gameViewport.imGui();
        editorWindow.imGui();
        inspectorWindow.imGui();
    }

    public void addGrid() {
        double zoom = camera.getZoom();
        Vector3f color = new Vector3f(75f / 255, 75f / 255, 75f / 255);
        int lineNumX = 2*(int) ((camera.getProjectionSize().x / TILE_WIDTH) / zoom) + 15;
        int lineNumY = 2*(int) ((camera.getProjectionSize().y / TILE_HEIGHT) / zoom) + 15;
        Vector2d snappedCamera = Tiles.snapWorldToTile(camera.getViewPoint());
        Vector2d offset = new Vector2d(0,0);
        offset.add(snappedCamera);
        for (int i = -lineNumX; i < lineNumX; i++) {
            DebugDraw.addLine2D(Tiles.tileToWorld(-lineNumX, i).add(offset), Tiles.tileToWorld(lineNumX, i).add(offset),color, 1,false);
        }
        for (int i = -lineNumY; i < lineNumY; i++ ) {
            DebugDraw.addLine2D(Tiles.tileToWorld(i,-lineNumY).add(offset), Tiles.tileToWorld(i,lineNumY).add(offset),color,1,false);
        }
    }

    public void addMouseSnapLines(){
        Vector2d mousePos = new Vector2d( MouseListener.getOrthoX(), MouseListener.getOrthoY());
        DebugDraw.addLine2D(new Vector2d( MouseListener.getOrthoX(), MouseListener.getOrthoY()), Tiles.snapWorldToTile(mousePos));
    }

    @Override
    public void update(float dt) {
        mouseController.update(dt);
        camera.update(dt);
        for (GameObject ob : gameObjects){
            ob.update(dt);
        }
    }

    @Override
    public void render(){
        DebugDraw.beginFrame();
        addGrid();
        DebugDraw.drawUnder();
        renderer.render();
        DebugDraw.drawOver();
    }

    @Override
    public void init() {
        load();
        mouseController = new MouseControllerEditor(this);
        gameViewport = new GameViewport();
        editorWindow = new EditorWindow(instance);
        inspectorWindow = new InspectorWindow(instance);
        camera = new Camera(new Vector2d(Tiles.tileToWorld(0,0)));
        Window.getInstance().setFramebuffer(gameViewport.getFramebuffer());
    }

    protected void loadResources() {
        savedWorldPath = "world.txt";

        AssetPool.getShader("src/main/resources/shaders/default.glsl");
        String name;
        String path;

        /////// player sprites
        String playerPath = "src/main/resources/sprites/player sprites/";
        String png = ".png";

        AssetPool.addSpriteSheet(name = "Walking_KG_2_left",
                new SpriteSheet(path = playerPath + name + png, name, AssetPool.getTexture(path),
                        100, 64, 7, 0));

        AssetPool.addSpriteSheet(name ="Idle_KG_2",
                new SpriteSheet(path = playerPath + name + png ,name, AssetPool.getTexture(path),
                        100, 64, 4, 0));

        AssetPool.addSpriteSheet(name = "Idle_KG_2_left",
                new SpriteSheet(path = playerPath + name + png ,name ,AssetPool.getTexture(path),
                        100, 64, 4, 0));

        AssetPool.addSpriteSheet(name = "Walking_KG_2",
                new SpriteSheet(path = playerPath + name + png ,name ,AssetPool.getTexture(path),
                        100, 64, 7, 0));


        // tile sprites

        AssetPool.addSpriteSheet(name = "ground1",
                new SpriteSheet(path = "src/main/resources/sprites/ground1.png",name,AssetPool.getTexture(path),
                        960, 480, 1, 0,2));

        AssetPool.addSpriteSheet(name = "isoTiles",
                new SpriteSheet(path = "src/main/resources/sprites/isoTiles.png",name,AssetPool.getTexture(path),
                        32, 32, 16*10, 0,1));

    }




    public GameViewport getGameViewport() {
        return gameViewport;
    }

    public MouseControllerStrategy getMouseControllerStrategy() {
        return mouseController;
    }

    public GameObject getLiftedObject() {
        return liftedObject;
    }

    public GameObject getPlayer() {
        return player;
    }

    public GameObject getActiveGameObject() {
        return activeGameObject;
    }

    public void setMouseController(MouseControllerStrategy mouseController) {
        this.mouseController = mouseController;
    }

    public void setLiftedObject(GameObject liftedObject) {
        this.liftedObject = liftedObject;
    }

    public void setActiveGameObject(GameObject activeGameObject) {
        this.activeGameObject = activeGameObject;
    }

    public EditorWindow getEditorWindow() {
        return editorWindow;
    }

    public void addToTileMap(GameObject go) {
        long key = Tiles.calcMapKey(go);
        GameObject go1 = tileMap.get(key);
        if (!(go == go1 ) && go1 != null ) {
            deleteGameObj(go1);
            System.out.println("updatetilemap delete");
        }
        tileMap.put(key,go);
    }

    public void removeFromTileMap(GameObject go){
        long key = Tiles.calcMapKey(go);
        tileMap.remove(key);
    }

    public void placeObject(){
        addToTileMap(liftedObject);
        setActiveGameObject(liftedObject);
        setLiftedObject(null);
    }

    public void liftObject(GameObject go){
        if (go == null) return;
        setActiveGameObject(go);
        setLiftedObject(go);
        removeFromTileMap(go);
    }

    public void dragObject(double mousePosX, double mousePosY){
        liftedObject.setPosition(Tiles.snapToTile( mousePosX, mousePosY)
                .add(-(liftedObject.getTransform().scale.x - Settings.TILE_WIDTH) / 2,0));
    }
}
