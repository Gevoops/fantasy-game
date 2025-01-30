package scenes;

import editor.EditorWindow;
import editor.GameViewWindow;
import editor.MouseControllerEditor;
import editor.InspectorWindow;
import engine.*;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;

import renderer.SpriteSheet;

import util.AssetPool;
import util.Grid;


import static util.Grid.gridToScreen;
import static util.Grid.snapScreenToGrid;
import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;


public class WorldEditorScene extends Scene {
    private static WorldEditorScene instance;
    private GameViewWindow gameViewWindow;
    private EditorWindow editorWindow;
    private InspectorWindow inspectorWindow;
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
        inspectorWindow.imGui();


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
        inspectorWindow = new InspectorWindow();
        camera = new Camera(new Vector2f(0,0));
        Window.getInstance().setFramebuffer(gameViewWindow.getFramebuffer());
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

    public void setActiveGameObject(GameObject activeGameObject) {
        this.activeGameObject = activeGameObject;
    }
}
