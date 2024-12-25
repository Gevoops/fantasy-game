package scenes;


import components.MouseControls;
import components.RigidBody;
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


public class WorldEditorScene extends Scene {
    int frameCount = 0;
    int spriteIndex = 0;
    int spriteIndex2 = 0;
    float clickX = 0;
    float clickY = 0;
    float stepX =0;
    float stepY = 0;
    boolean start = true;
    private long windowPtr = Window.getWindow().getWindowPtr();

    private ImGuiLayer gui;
    GameObject ob1;
    MouseControls mouseControls = new MouseControls();






    public WorldEditorScene() {

    }
    @Override
    public void imGui() {
        ImGui.begin("editor gui");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        Sprite sprite;
        ArrayList<Sprite> sprites = AssetPool.getSpriteSheet("src/main/resources/sprites/Idle_KG_2_left.png").sprites;
        for (int i = 0; i < sprites.size(); i ++){
            sprite = sprites.get(i);
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            int id = sprite.getTexId();
            Vector2f[] coords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id,spriteWidth,spriteHeight,coords[2].x,coords[0].y,coords[0].x,coords[2].y)) {
                GameObject ob = Prefabs.generateSpriteObject(sprite,spriteWidth,spriteHeight);
                mouseControls.liftObject(ob);
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
            this.ob1 = new GameObject();
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
                        new Transform(worldToScreen(x,y).add(-TILE_WIDTH/2,0), new Vector2f(TILE_WIDTH, TILE_HEIGHT)),0);
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
        int lineNumX = 2*(int) ((camera.getProjectionSize().x / TILE_WIDTH) / zoom);
        int lineNumY = 2*(int) ((camera.getProjectionSize().y / TILE_HEIGHT) / zoom);
        for (int i = -lineNumX; i < lineNumX; i++) {
            DebugDraw.addLine2D(worldToScreen(-lineNumX, i), worldToScreen(lineNumX, i), new Vector3f(221f / 255, 221f / 255, 221f / 255), 1);
        }
        for (int i = -lineNumY; i < lineNumY; i++ ) {
            DebugDraw.addLine2D(worldToScreen(i,-lineNumY),worldToScreen(i,lineNumY),new Vector3f(221f/255,221f/255,221f/255),1);
        }
        Vector2f v1 = screenToWorld(MouseListener.getOrthoX(),MouseListener.getOrthoY());
        Vector2f snap = worldToScreen((float)Math.floor(v1.x) ,(float) Math.floor(v1.y));
        DebugDraw.addLine2D(new Vector2f(MouseListener.getOrthoX(),MouseListener.getOrthoY()), snap,new Vector3f(0,1,0),1);
    }

    public float screenToWorldX(float x, float y){
        return (x-X_OFFSET) / TILE_WIDTH +  (y -Y_OFFSET) / TILE_HEIGHT - 0.5f;
    }
    public float screenToWorldY(float x, float y){
        return (y-Y_OFFSET) / TILE_HEIGHT -  (x -X_OFFSET) / TILE_WIDTH + 0.5f;
    }
    public Vector2f screenToWorld(float x,float y){
        return new Vector2f(screenToWorldX(x,y),screenToWorldY(x,y));
    }



    public float worldToScreenX(float x,float y){
        return TILE_WIDTH * (x * 0.496f   - y  * 0.496f) + TILE_WIDTH/2;
    }
    public float worldToScreenY(float x,float y){
        return  TILE_HEIGHT * (x * 0.5f  + y  * 0.5f);
    }

    public Vector2f worldToScreen(float x,float y){

        return new Vector2f(worldToScreenX(x,y) + X_OFFSET, worldToScreenY(x,y) + Y_OFFSET);
    }


    @Override
    public void init() {
        loadResources();
        this.load();
        //imGui
        gui = new ImGuiLayer(windowPtr);
        gui.initImGui();

        this.camera = new Camera(new Vector2f(0,0));
        if(!gameObjects.isEmpty() && gameObjects.get(0).getName().equals("valerie") ){
            this.ob1 = gameObjects.get(0);
            activeGameObject = ob1;
        }
    }



    @Override
    public void update(double dt) {
        DebugDraw.beginFrame();
        drawGrid();
        mouseControls.update(dt);
        for (GameObject ob : gameObjects){
            ob.update(dt);
        }
        frameCount++;
        if(!gameObjects.isEmpty()){

            SpriteSheetList spriteSheets = ob1.getComponent(SpriteSheetList.class);
            if(Window.getWindow().leftClicked ) {
                clickX = Window.getWindow().clickX - 50;
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
                    ob1.moveX(stepX * 4);

                }
                if(Math.abs(ob1.getY() - clickY ) >= 2) {
                    ob1.moveY(stepY * 4);

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

        DebugDraw.draw();
        this.renderer.render();
        gui.drawGui(Window.getCurrentScene());
    }



    private void loadResources() {
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
}
