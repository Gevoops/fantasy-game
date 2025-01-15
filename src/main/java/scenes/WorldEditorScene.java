package scenes;

import editor.EditorWindow;
import editor.GameViewWindow;
import editor.MouseControllerEditor;
import editor.PropertiesWindow;
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
    private PropertiesWindow propertiesWindow;
    private MouseControllerStrategy mouseController;
    private GameObject liftedObject;
    private GameObject activeGameObject;

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
        propertiesWindow.imGui();

    }

    public void addGrid() {

        float zoom = camera.getZoom();
        Vector3f color = new Vector3f(75f / 255, 75f / 255, 75f / 255);
        int lineNumX = 2*(int) ((camera.getProjectionSize().x / TILE_WIDTH) / zoom) + 15;
        int lineNumY = 2*(int) ((camera.getProjectionSize().y / TILE_HEIGHT) / zoom) + 15;
        Vector2f snappedCamera = snapScreenToGrid(camera.getViewPoint());
        Vector2f offset = new Vector2f(0,0);
        offset.add(snappedCamera);
        for (int i = -lineNumX; i < lineNumX; i++) {
            DebugDraw.addLine2D(gridToScreen(-lineNumX, i).add(offset), gridToScreen(lineNumX, i).add(offset),color, 1,false);
        }
        for (int i = -lineNumY; i < lineNumY; i++ ) {
            DebugDraw.addLine2D(gridToScreen(i,-lineNumY).add(offset), gridToScreen(i,lineNumY).add(offset),color,1,false);
        }
        DebugDraw.addLine2D(gridToScreen(0,0), gridToScreen(-2,-2));

    }
    public void addMouseSnapLines(){
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY());
        DebugDraw.addLine2D(new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY()), snapScreenToGrid(mousePos));
        DebugDraw.addLine2D(new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY()), snapScreenToGrid(mousePos).sub(gridToScreen(1,0)), new Vector3f(1,0,1), 1,false);
        DebugDraw.addLine2D(new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY()), snapScreenToGrid(mousePos).sub(gridToScreen(1,1)), new Vector3f(1,0,1), 1,false);
        DebugDraw.addLine2D(new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY()), snapScreenToGrid(mousePos).sub(gridToScreen(0,1)), new Vector3f(1,0,1), 1,false);
        DebugDraw.addLine2D(new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY()), snapScreenToGrid(mousePos).sub(gridToScreen(1,1)), new Vector3f(1,0,1), 1,false);
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
        addMouseSnapLines();
        DebugDraw.drawUnder();
        renderer.render();
        DebugDraw.drawOver();
    }

    @Override
    public void init() {
        load();
        mouseController = new MouseControllerEditor();
        gameViewWindow = new GameViewWindow();
        editorWindow = new EditorWindow(instance);
        propertiesWindow = new PropertiesWindow();
        camera = new Camera(new Vector2f(0,0));
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
        return snapToGrid(gridToScreenX(cellX,cellY), gridToScreenY(cellX,cellY));
    }
    public Vector2f snapToGrid(float screenX, float screenY){
        return snapScreenToGrid(screenX,screenY).add(-TILE_WIDTH /2 ,0);
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

    public GameObject getActiveGameObject() {
        return activeGameObject;
    }

    public void setMouseController(MouseControllerStrategy mouseController) {
        this.mouseController = mouseController;
    }

    public void setLiftedObject(GameObject liftedObject) {
        this.liftedObject = liftedObject;
    }


}
