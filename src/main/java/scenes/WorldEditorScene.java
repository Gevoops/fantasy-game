package scenes;

import editor.EditorWindow;
import editor.GameViewWindow;
import editor.MouseControllerEditor;
import engine.*;
import components.SpriteSheetList;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;

import renderer.SpriteSheet;

import util.AssetPool;



import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;


public class WorldEditorScene extends Scene {
    private static WorldEditorScene instance;
    double frameCount = 0;
    int spriteIndex = 0;
    int spriteIndex2 = 0;
    float clickX = 0;
    float clickY = 0;
    float stepX = 0;
    float stepY = 0;
    boolean start = true;
    private GameViewWindow gameViewWindow;
    private EditorWindow editorWindow;
    GameObject ob1;
    private MouseControllerEditor mouseController;
    private GameObject liftedObject;

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
        frameCount += dt;
        if(!gameObjects.isEmpty()){

            SpriteSheetList spriteSheets = ob1.getComponent(SpriteSheetList.class);
            if(Window.getWindow().leftClicked ) {
                clickX  = Window.getWindow().clickX - 50;
                clickY = Window.getWindow().clickY;
                float distance = (float) Math.sqrt(Math.pow(ob1.getX() - clickX,2)
                        + Math.pow(ob1.getY() - clickY,2));

                stepX = (clickX - ob1.getX()) / distance;
                stepY = (clickY - ob1.getY()) / distance;

                start = false;
            }

            if(((Math.abs(ob1.getX() - clickX) >= 2 ) || (Math.abs(ob1.getY() - clickY) >= 2)) && !start) {
                if(frameCount > 4) {
                    frameCount = 0;
                    if(stepX > 0) {
                       ob1.setSprite(spriteSheets.get(1).getSprite(spriteIndex));
                        spriteIndex = spriteIndex == 6 ? 0 : spriteIndex + 1;
                    } else {
                        ob1.setSprite(spriteSheets.get(3).getSprite(spriteIndex));
                        spriteIndex = spriteIndex == 0 ? 6 : spriteIndex - 1;
                    }

                    spriteIndex2 = 0;
                }
                if(Math.abs(ob1.getX() - clickX) >= 2) {
                    ob1.moveX(stepX * 4 * dt);

                }
                if(Math.abs(ob1.getY() - clickY ) >= 2) {
                    ob1.moveY(stepY * 4 * dt );

                }
            } else {
                if(frameCount > 12) {
                    frameCount = 0;
                    if(stepX >= 0) {
                        ob1.setSprite(spriteSheets.get(0).getSprite(spriteIndex2));
                        spriteIndex2 = spriteIndex2 == 3 ? 0 : spriteIndex2 + 1;
                    } else {
                        ob1.setSprite(spriteSheets.get(2).getSprite(spriteIndex2));
                        spriteIndex2 = spriteIndex2 == 0 ? 3 : spriteIndex2 - 1;
                    }

                    spriteIndex = 6;
                }
            }
            ob1.update(dt);

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

    public MouseControllerEditor getMouseController() {
        return mouseController;
    }

    public GameObject getLiftedObject() {
        return liftedObject;
    }

    public void setOb1(GameObject ob1) {
        this.ob1 = ob1;
    }
    public void setMouseController(MouseControllerEditor mouseController) {
        this.mouseController = mouseController;
    }

    public void setLiftedObject(GameObject liftedObject) {
        this.liftedObject = liftedObject;
    }
}
