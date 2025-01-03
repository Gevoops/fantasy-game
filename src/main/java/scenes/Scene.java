package scenes;

import components.Component;
import components.ComponentSerializer;
import engine.Camera;
import engine.GameObjectSerializer;
import exceptions.NullSpriteException;
import engine.GameObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import imgui.ImGui;
import org.joml.Vector2f;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected GameObject activeGameObject = null;
    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected boolean levelLoaded = false;
    protected String savedWorldPath;
    protected final float TILE_WIDTH = 128f;
    protected final float TILE_HEIGHT = 64f;
    protected final float X_OFFSET = 0;
    protected final float Y_OFFSET = 0;





    public Scene() {

    }

    public void init() {

    }

    public void start(){
        for(GameObject ob : gameObjects) {
            ob.start();
            try{
                this.renderer.addGameObject(ob);
            } catch (NullSpriteException e) {
                e.printStackTrace();
            }

        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject ob) {
        gameObjects.add(ob);
        if(isRunning) {
            try {
                this.renderer.addGameObject(ob);
            }catch (NullSpriteException e){
                e.printStackTrace();
            }
            ob.start();
        }
    }

    public void sceneImGui(){
        if(activeGameObject != null) {
            ImGui.begin("inspector");
            activeGameObject.imGui();
            ImGui.end();
        }
        imGui();
    }

    public void imGui(){


    }

    public void saveExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(GameObject.class, new GameObjectSerializer())
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .create();
        try {
            FileWriter writer  = new FileWriter(savedWorldPath);
            writer.write(gson.toJson(this.gameObjects,new TypeToken<ArrayList<GameObject>>(){}.getType()));
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void load(){
        loadResources();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(GameObject.class, new GameObjectSerializer())
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get(savedWorldPath)));
        }catch (Exception e){
            System.err.println("cant load world file \"" + savedWorldPath  + "\" doesn't exist");
        }
        if(!inFile.equals("")){
            int maxObjId = -1;
            int maxCompId = -1;

            ArrayList<GameObject> objects = gson.fromJson(inFile, new TypeToken<ArrayList<GameObject>>(){}.getType());
            for (GameObject go : objects) {
                addGameObjectToScene(go);
                for (Component c : go.getComponents()){
                    if(c.getUid() > maxCompId){
                        maxCompId = c.getUid();
                    }
                }
                if (go.getUid() > maxObjId){
                    maxObjId = go.getUid();
                }
            }
            maxObjId++;
            maxCompId++;
            GameObject.init(maxObjId);
            Component.init(maxCompId);
        }
        this.levelLoaded = true;
    }



    public abstract void update(float dt);

    public abstract void render();

    protected abstract void loadResources();

    public Camera getCamera() {
        return this.camera;
    }

    public Vector2f screenToWorldCell(Vector2f screenPos){
        Vector2f worldPos = screenToWorld(screenPos);
        return worldToScreen((float)Math.floor(worldPos.x) ,(float) Math.floor(worldPos.y));
    }
    public Vector2f screenToWorldCell(float screenX, float screenY){
        return screenToWorldCell(new Vector2f(screenX,screenY));
    }


    public float screenToWorldX(float screenX, float screenY){
        return (screenX-X_OFFSET) / TILE_WIDTH +  (screenY -Y_OFFSET) / TILE_HEIGHT;
    }
    public float screenToWorldY(float screenX, float screenY){
        return (screenY-Y_OFFSET) / TILE_HEIGHT -  (screenX -X_OFFSET) / TILE_WIDTH ;
    }
    public Vector2f screenToWorld(float screenX,float screenY){
        return new Vector2f(screenToWorldX(screenX,screenY),screenToWorldY(screenX,screenY));
    }
    public Vector2f screenToWorld(Vector2f screenPos){
        return new Vector2f(screenToWorldX(screenPos.x,screenPos.y),screenToWorldY(screenPos.x,screenPos.y));
    }
    public float worldToScreenX(float cellX,float cellY){
        return TILE_WIDTH * (cellX * 0.5f - cellY  * 0.5f);
    }
    public float worldToScreenY(float cellX,float cellY){
        return  TILE_HEIGHT * (cellX * 0.5f  + cellY  * 0.5f);
    }

    public Vector2f worldToScreen(float cellX,float cellY){

        return new Vector2f(worldToScreenX(cellX,cellY) + X_OFFSET, worldToScreenY(cellX,cellY) + Y_OFFSET);
    }
    public Vector2f worldToScreen(Vector2f worldPos){

        return new Vector2f(worldToScreenX(worldPos.x,worldPos.y) + X_OFFSET, worldToScreenY(worldPos.x,worldPos.y) + Y_OFFSET);
    }
}
