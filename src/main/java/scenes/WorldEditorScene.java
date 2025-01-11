package scenes;


import com.google.gson.internal.NumberLimits;
import components.RigidBody;
import editor.GameViewWindow;
import engine.*;


import components.SpriteSheetList;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import renderer.Sprite;
import renderer.SpriteSheet;
import renderer.Transform;
import util.AssetPool;

import java.util.ArrayList;

import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;


public class WorldEditorScene extends Scene {
    double frameCount = 0;
    int spriteIndex = 0;
    int spriteIndex2 = 0;
    float clickX = 0;
    float clickY = 0;
    float stepX = 0;
    float stepY = 0;
    boolean start = true;
    private long windowPtr = Window.getWindow().getWindowPtr();
    private GameViewWindow gameViewWindow;
    private ImGuiLayer gui;
    GameObject ob1;
    private ArrayList<Sprite> guiSprites;

    public WorldEditorScene() {

    }
    @Override
    public void imGui() {
        gameViewWindow.imGui();
        ImGui.begin("editor gui");
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        Sprite sprite;
        ArrayList<Sprite> sprites = guiSprites;
        for (int i = 0; i < sprites.size(); i ++){
            sprite = sprites.get(i);
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            if(sprite.getSpriteSheetName().equals("src/main/resources/sprites/ground1.png")){
                spriteWidth = TILE_WIDTH;
                spriteHeight = TILE_HEIGHT;
            }
            int id = sprite.getTexId();
            Vector2f[] coords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id,spriteWidth,spriteHeight,coords[2].x,coords[0].y,coords[0].x,coords[2].y)) {
                GameObject ob = Prefabs.generateSpriteObject(sprite,spriteWidth,spriteHeight);
                Window.mouseController.liftObject(ob);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < sprites.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        if (ImGui.button("create")) {
            this.ob1 = new GameObject("valerie", null,null,0);
            ob1.setName("valerie");
            ob1.setSprite(AssetPool.getSpriteSheet("src/main/resources/sprites/Idle_KG_2.png").getSprite(0));
            ob1.setTransform(new Transform(new Vector2f(300, 300), new Vector2f(100, 64)));
            ob1.setZIndex(2);
            SpriteSheetList s;
            ob1.addComponent(s = new SpriteSheetList());
            ob1.addComponent(new RigidBody());

            s.addSpriteSheet(AssetPool.getSpriteSheet("src/main/resources/sprites/Idle_KG_2.png"));
            s.addSpriteSheet(AssetPool.getSpriteSheet("src/main/resources/sprites/Walking_KG_2.png"));
            s.addSpriteSheet(AssetPool.getSpriteSheet("src/main/resources/sprites/Idle_KG_2_left.png"));
            s.addSpriteSheet(AssetPool.getSpriteSheet("src/main/resources/sprites/Walking_KG_2_left.png"));
            this.addGameObjectToScene(this.ob1);



        for (int y = 39; y >= 0; y--) {
            for (int x = 0; x < 40; x++) {
                GameObject ob = new GameObject("ground: " + x + "," + y,null,
                        new Transform(cellSnapToGrid(x,y), new Vector2f(TILE_WIDTH, TILE_HEIGHT)),0);
                ob.setName("ground: " + x + "," + y);
                SpriteSheetList list = new SpriteSheetList();
                list.addSpriteSheet(AssetPool.getSpriteSheet("src/main/resources/sprites/ground1.png"));
                ob.addComponent(list);
                ob.setSprite(list.getSpriteSheets().get(0).getSprite(0));
                this.addGameObjectToScene(ob);
            }
        }


        }
        ImGui.end();
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
        DebugDraw.addCircle2D(worldToScreen(0,0), 100, new Vector3f(0,1,0),1);
        Window.mouseController.update(dt);
        camera.moveCamera(dt);
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
        this.load();
        this.gameViewWindow = new GameViewWindow();
        this.camera = new Camera(new Vector2f(0,0));
        guiSprites = AssetPool.getSpriteSheet("src/main/resources/sprites/Idle_KG_2.png").getSprites();
        guiSprites.addAll(AssetPool.getSpriteSheet("src/main/resources/sprites/Walking_KG_2_left.png").getSprites());
        guiSprites.addAll(AssetPool.getSpriteSheet("src/main/resources/sprites/ground1.png").getSprites());


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
}
