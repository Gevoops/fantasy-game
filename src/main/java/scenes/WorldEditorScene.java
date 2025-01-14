package scenes;

import editor.EditorWindow;
import editor.GameViewWindow;
import editor.MouseControllerEditor;
import engine.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;

import renderer.SpriteSheet;

import util.AssetPool;



import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;


public class WorldEditorScene extends Scene {
    private static WorldEditorScene instance;
    private GameViewWindow gameViewWindow;
    private EditorWindow editorWindow;
    GameObject ob1;
    private MouseControllerStrategy mouseController;
    private GameObject liftedObject;
    private GameObject player;

    private WorldEditorScene() {}

    public static WorldEditorScene getInstance(){
        if (instance == null){
            instance = new WorldEditorScene();
        }
        return instance;
    }

    @Override
    public void imGui() {
        gameViewWindow.imGui();
        editorWindow.imGui();

    }

    public void drawGrid() {

        float zoom = camera.getZoom();
        Vector3f color = new Vector3f(75f / 255, 75f / 255, 75f / 255);
        int lineNumX = 2*(int) ((camera.getProjectionSize().x / TILE_WIDTH) / zoom) + 15;
        int lineNumY = 2*(int) ((camera.getProjectionSize().y / TILE_HEIGHT) / zoom) + 15;
        Vector2f snappedCamera = screenToWorldCell(camera.getViewPoint());
        Vector2f offset = new Vector2f(0,0);
        offset.add(snappedCamera);
        for (int i = -lineNumX; i < lineNumX; i++) {
            DebugDraw.addLine2D(worldToScreen(-lineNumX, i).add(offset), worldToScreen(lineNumX, i).add(offset),color, 1);
        }
        for (int i = -lineNumY; i < lineNumY; i++ ) {
            DebugDraw.addLine2D(worldToScreen(i,-lineNumY).add(offset),worldToScreen(i,lineNumY).add(offset),color,1);
        }
        DebugDraw.addLine2D(worldToScreen(0,0),worldToScreen(-2,-2));

    }
    public void drawMouseSnap(){
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY());
        DebugDraw.addLine2D(new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY()), screenToWorldCell(mousePos));
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
        drawGrid();
        DebugDraw.draw();
        DebugDraw.beginFrame();
        renderer.render();
        drawMouseSnap();
        DebugDraw.draw();

    }

    @Override
    public void init() {
        load();
        mouseController = new MouseControllerEditor();
        gameViewWindow = new GameViewWindow();
        editorWindow = new EditorWindow(instance);
        camera = new Camera(new Vector2f(0,0));

        if(!gameObjects.isEmpty()){
            activeGameObject = gameObjects.get(0);
            ob1 = activeGameObject;
            camera.setViewPoint(new Vector2f(ob1.getTransform().position).sub(camera.getProjectionSize().x /2f, camera.getProjectionSize().y /2f ));
        }

        Window.getWindow().setFramebuffer(gameViewWindow.getFramebuffer());

    }

    protected void loadResources() {
        savedWorldPath = "world.txt";

        AssetPool.getShader("src/main/resources/shaders/default.glsl");
        String name;

        AssetPool.addSpriteSheet(name = "src/main/resources/sprites/Walking_KG_2_left.png",
                new SpriteSheet(name, AssetPool.getTexture(name),
                        100, 64, 7, 0));

        AssetPool.addSpriteSheet(name = "src/main/resources/sprites/Idle_KG_2.png",
                new SpriteSheet(name, AssetPool.getTexture(name),
                        100, 64, 4, 0));

        AssetPool.addSpriteSheet(name = "src/main/resources/sprites/Idle_KG_2_left.png",
                new SpriteSheet(name ,AssetPool.getTexture(name),
                        100, 64, 4, 0));

        AssetPool.addSpriteSheet(name = "src/main/resources/sprites/Walking_KG_2.png",
                new SpriteSheet(name ,AssetPool.getTexture(name),
                        100, 64, 7, 0));

        AssetPool.addSpriteSheet(name = "src/main/resources/sprites/ground1.png",
                new SpriteSheet(name ,AssetPool.getTexture(name),
                        960, 480, 1, 0));
    }

    public Vector2f cellSnapToGrid(float cellX, float cellY){
        return snapToGrid(worldToScreenX(cellX,cellY),worldToScreenY(cellX,cellY));
    }
    public Vector2f snapToGrid(float screenX, float screenY){
        return screenToWorldCell(screenX,screenY).add(-TILE_WIDTH /2 ,0);
    }

    public Vector2f snapToGrid(Vector2f screenPos){
        return snapToGrid(screenPos.x,screenPos.y) ;
    }

    public GameViewWindow getGameViewport() {
        return gameViewWindow;
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

    public void setMouseController(MouseControllerStrategy mouseController) {
        this.mouseController = mouseController;
    }

    public void setLiftedObject(GameObject liftedObject) {
        this.liftedObject = liftedObject;
    }

    public void setPlayer(GameObject player) {
        this.player = player;
    }
}
