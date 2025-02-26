package editor;

import components.Player;
import components.RigidBody;
import components.SpriteSheetList;
import engine.GameObject;
import engine.KeyListener;
import engine.Prefabs;
import game.MouseControllerGame;
import imgui.ImGui;
import imgui.ImGuiInputTextCallbackData;
import imgui.ImVec2;
import imgui.callback.ImGuiInputTextCallback;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.joml.Vector2f;
import renderer.Sprite;
import renderer.Transform;
import scenes.WorldEditorScene;
import util.AssetPool;
import util.Grid;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;

public class EditorWindow {
    private ArrayList<Sprite> editorSprites;
    private WorldEditorScene editorScene;
    private boolean editMode = true;
    int[] cameraPos = new int[2];



    public EditorWindow(WorldEditorScene editorScene){
        this.editorScene = editorScene;
        initEditorSprites();

    }

    public void imGui(){
        ImGui.begin("editor gui");

        // buttons

        if (ImGui.button("play")) {
            editorScene.setMouseController(new MouseControllerGame());
            editMode = false;
        }
        ImGui.sameLine();
        if (ImGui.button("edit")) {
            editorScene.setMouseController(new MouseControllerEditor());
            editMode = true;
        }
        ImGui.setNextItemWidth(200);

        ImGuiInputTextCallback callback = new ImGuiInputTextCallback() {
            @Override
            public void accept(ImGuiInputTextCallbackData imGuiInputTextCallbackData) {

            }
        };

        ImString cameraInput = new ImString(256);
        if (ImGui.inputText("goto position",cameraInput,ImGuiInputTextFlags.EnterReturnsTrue)){
            String[] res = cameraInput.get().split(" ");
            if (res.length == 2) {
                try {
                    int x = Integer.parseInt(res[0]);
                    int y = Integer.parseInt(res[1]);
                    editorScene.getCamera().viewPoint.set(Grid.gridToScreen(x, y));

                } catch (Exception e) {
                    System.out.println("invalid x and y");
                }
            }
        }
        ImGui.sameLine();

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        Sprite sprite;
        ArrayList<Sprite> sprites = editorSprites;
        for (int i = 0; i < sprites.size(); i ++){
            sprite = sprites.get(i);
            float spriteWidth = sprite.getWidth();
            float spriteHeight = sprite.getHeight();
            float ratio;
            if((ratio  = AssetPool.getSpriteSheet(sprite.getSpriteSheetName()).getTileSizeRatio()) != 0){
                float normalize = TILE_WIDTH / spriteWidth;
                spriteWidth *= ratio  * normalize;
                spriteHeight *= ratio * normalize;
            }

            int id = sprite.getTexId();
            Vector2f[] coords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id,spriteWidth,spriteHeight,coords[2].x,coords[0].y,coords[0].x,coords[2].y) && editMode ){
                GameObject liftedObject = Prefabs.generateSpriteObject(sprite,spriteWidth,spriteHeight);
                editorScene.addGameObjectToScene(liftedObject);
                editorScene.setLiftedObject(liftedObject);
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
            GameObject player = new GameObject(
                    "valerie",
                    AssetPool.getSpriteSheet("Idle_KG_2").getSprite(0),
                    new Transform(new Vector2f(300, 300), new Vector2f(100, 64)),
                    2);
            SpriteSheetList spriteSheetList = new SpriteSheetList();
            spriteSheetList.addSpriteSheet(AssetPool.getSpriteSheet("Idle_KG_2"));
            spriteSheetList.addSpriteSheet(AssetPool.getSpriteSheet("Walking_KG_2"));
            spriteSheetList.addSpriteSheet(AssetPool.getSpriteSheet("Idle_KG_2_left"));
            spriteSheetList.addSpriteSheet(AssetPool.getSpriteSheet("Walking_KG_2_left"));
            player.addComponent(spriteSheetList);

            player.addComponent(new RigidBody());
            player.addComponent(new Player());
            editorScene.setPlayer(player);
            editorScene.addGameObjectToScene(player);



            for (int y = 39; y >= 0; y--) {
                for (int x = 0; x < 40; x++) {
                    GameObject ob = new GameObject("ground: " + x + "," + y,null,
                            new Transform(Grid.cellSnapToGrid(x,y), new Vector2f(TILE_WIDTH, TILE_HEIGHT)),0);
                    SpriteSheetList list = new SpriteSheetList();
                    list.addSpriteSheet(AssetPool.getSpriteSheet("ground1"));
                    ob.addComponent(list);
                    ob.setSprite(list.getSpriteSheets().get(0).getSprite(0));
                    editorScene.addGameObjectToScene(ob);
                }
            }
        }
        ImGui.end();
    }

    private void initEditorSprites(){
        editorSprites = new ArrayList<>();
        editorSprites.addAll(AssetPool.getSpriteSheet("ground1").getSprites());
        editorSprites.addAll(AssetPool.getSpriteSheet("isoTiles").getSprites());
    }
}
