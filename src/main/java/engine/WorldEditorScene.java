package engine;


import Game.GameObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.joml.Matrix2f;
import org.joml.Vector2f;

import org.joml.Vector4f;
import renderer.Sprite;
import renderer.SpriteSheet;
import renderer.Texture;
import renderer.Transform;
import util.AssetPool;


public class WorldEditorScene extends Scene {
    public static Matrix2f isoMatrix = new Matrix2f(0.5f, -0.25f,
                                                     1f, 0.49f );

    //m11 = space between rows

    //for thick tiles
    //0.4f, -0.15f,
    //0.9f, 0.34f );
    //
    int frameCount = 0;
    int spriteIndex = 0;
    int spriteIndex2 = 0;
    float clickX = 0;
    float clickY = 0;
    float stepX =0;
    float stepY = 0;
    GameObject ob1;
    boolean start = true;



    private long windowPtr = Window.get().getWindowPtr();
    private ImGuiLayer gui;

    public WorldEditorScene() {

    }
    @Override
    public void imGui(){
        ImGui.showDemoWindow();
    }

    @Override
    public void init() {
        //imGui
        gui = new ImGuiLayer(windowPtr);
        gui.initImGui();



        loadResources();

        this.camera = new Camera(new Vector2f(0,0));


       // this.spriteSheet3 = AssetPool.getSpriteSheet("src/main/resources/sprites/Walking_KG_2_left.png");


        int xOffset = -1950;
        int yOffset = -200;

        float sizeX = 128.0f;
        float sizeY = 64f;
        Texture tex = new Texture();
        tex.init("src/main/resources/sprites/ground1.png");
        Sprite sprt2 = AssetPool.getSpriteSheet( "src/main/resources/sprites/Overworld - Forest - Flat 128x64.png").getSprite(3);
        GameObject imguitestobject = new GameObject();
        imguitestobject.setName("object");
        imguitestobject.setSprite(new Sprite());
        imguitestobject.sprite.setColor(new Vector4f(1,0,0,1));
        imguitestobject.setTransform(new Transform(new Vector2f(300,300),new Vector2f(256,258)));
        imguitestobject.setZIndex(1);
        activeGameObject = imguitestobject;
        this.addGameObjectToScene(imguitestobject);

        for (int y = 40; y > 0; y--) {
            for (int x = 0; x < 40; x++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject ob = new GameObject();
                ob.setName("obj" + xPos + "," + yPos);
                ob.setSprite(new Sprite());
                ob.sprite.setTexture(tex);
                ob.setTransform( new Transform(new Vector2f(xPos, yPos).mul(isoMatrix), new Vector2f(sizeX, sizeY)));
                ob.setZIndex(0);
                this.addGameObjectToScene(ob);
            }
        }
        this.ob1 = new GameObject();
        ob1.setName("valerie");
        ob1.setSprite(AssetPool.getSpriteSheet("src/main/resources/sprites/Idle_KG_2.png").getSprite(0));
        ob1.setTransform( new Transform(new Vector2f(300, 300), new Vector2f(100, 64)));
        ob1.setZIndex(2);

        ob1.addSpriteSheet(AssetPool.getSpriteSheet( "src/main/resources/sprites/Idle_KG_2.png"));
        ob1.addSpriteSheet(AssetPool.getSpriteSheet("src/main/resources/sprites/Walking_KG_2.png"));
        ob1.addSpriteSheet(AssetPool.getSpriteSheet("src/main/resources/sprites/Idle_KG_2_left.png"));
        ob1.addSpriteSheet(AssetPool.getSpriteSheet("src/main/resources/sprites/Walking_KG_2_left.png"));
        this.addGameObjectToScene(this.ob1);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(ob1.sprite));

    }



    @Override
    public void update(double dt) {
        frameCount++;
        if(Window.get().leftClicked ) {
            clickX = Window.get().clickX - 50;
            clickY = Window.get().clickY;
            float distance = (float) Math.sqrt(Math.pow(ob1.transform.position.x - clickX,2)
            + Math.pow(ob1.transform.position.y - clickY,2));

            stepX = (clickX - ob1.transform.position.x) / distance;
            stepY = (clickY - ob1.transform.position.y) / distance;

            start = false;
        }

        if(((Math.abs(ob1.transform.position.x - clickX) >= 2) || (Math.abs(ob1.transform.position.y - clickY) >= 2)) && !start) {
            if(frameCount > 4) {
                frameCount =0;
                if(stepX > 0) {
                    ob1.setSprite(ob1.spriteSheets.get(1).getSprite(spriteIndex));
                    spriteIndex = spriteIndex == 6 ? 0 : spriteIndex + 1;
                } else {
                    ob1.setSprite(ob1.spriteSheets.get(3).getSprite(spriteIndex));
                    spriteIndex = spriteIndex == 0 ? 6 : spriteIndex - 1;
                }

                spriteIndex2 = 0;
            }
            if(Math.abs(ob1.transform.position.x - clickX) >= 2) {
                ob1.transform.position.x += stepX * 4;

            }
            if(Math.abs(ob1.transform.position.y - clickY ) >= 2) {
                ob1.transform.position.y += stepY * 4;

            }
        } else {
            if(frameCount > 12) {
                frameCount = 0;
                if(stepX >= 0) {
                    ob1.setSprite(ob1.spriteSheets.get(0).getSprite(spriteIndex2));
                    spriteIndex2 = spriteIndex2 == 3 ? 0 : spriteIndex2 + 1;
                } else {
                    ob1.setSprite(ob1.spriteSheets.get(2).getSprite(spriteIndex2));
                    spriteIndex2 = spriteIndex2 == 0 ? 3 : spriteIndex2 - 1;
                }

                spriteIndex = 6;
            }
        }

        ob1.update(dt);
        this.renderer.render();

        gui.drawGui(Window.getCurrentScene());
    }



    private void loadResources() {
        AssetPool.getShader("src/main/resources/shaders/default.glsl");

        AssetPool.addSpriteSheet("src/main/resources/sprites/Walking_KG_2_left.png",
                new SpriteSheet(AssetPool.getTexture("src/main/resources/sprites/Walking_KG_2_left.png"),
                        100, 64, 7, 0));


        AssetPool.addSpriteSheet("src/main/resources/sprites/Idle_KG_2.png",
                new SpriteSheet(AssetPool.getTexture("src/main/resources/sprites/Idle_KG_2.png"),
                        100, 64, 4, 0));

        AssetPool.addSpriteSheet("src/main/resources/sprites/Idle_KG_2_left.png",
                new SpriteSheet(AssetPool.getTexture("src/main/resources/sprites/Idle_KG_2_left.png"),
                        100, 64, 4, 0));

        AssetPool.addSpriteSheet("src/main/resources/sprites/Walking_KG_2.png",
                new SpriteSheet(AssetPool.getTexture("src/main/resources/sprites/Walking_KG_2.png"),
                        100, 64, 7, 0));

        AssetPool.addSpriteSheet("src/main/resources/sprites/forest_sheet.png",
                new SpriteSheet(AssetPool.getTexture("src/main/resources/sprites/forest_sheet.png"),
                        128, 64, 18, 0));

        AssetPool.addSpriteSheet("src/main/resources/sprites/Overworld - Forest - Flat 128x64.png",
                new SpriteSheet(AssetPool.getTexture("src/main/resources/sprites/Overworld - Forest - Flat 128x64.png"),
                        128, 64, 18, 0));

    }
}
