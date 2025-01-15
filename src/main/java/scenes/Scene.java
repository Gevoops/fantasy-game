package scenes;

import components.Component;
import components.ComponentSerializer;
import editor.GameViewWindow;
import engine.Camera;
import engine.GameObjectSerializer;
import exceptions.NullSpriteException;
import engine.GameObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.joml.Vector2f;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected boolean levelLoaded = false;
    protected String savedWorldPath;
    protected final float X_OFFSET = 0;
    protected final float Y_OFFSET = 0;
    protected GameObject player;




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
        imGui();
    }

    public void imGui(){


    }

    public abstract GameViewWindow getGameViewport();

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
                    if(c.getCompID() > maxCompId){
                        maxCompId = c.getCompID();
                    }
                }
                if (go.getObjID() > maxObjId){
                    maxObjId = go.getObjID();
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

    public Vector2f snapScreenToGrid(Vector2f screenPos){
        Vector2f worldPos = screenToGrid(screenPos);
        return gridToScreen((float)Math.floor(worldPos.x) ,(float) Math.floor(worldPos.y));
    }
    public Vector2f snapScreenToGrid(float screenX, float screenY){
        return snapScreenToGrid(new Vector2f(screenX,screenY));
    }


    public float screenToGridX(float screenX, float screenY){
        return (screenX-X_OFFSET) / TILE_WIDTH +  (screenY -Y_OFFSET) / TILE_HEIGHT;
    }
    public float screenToGridY(float screenX, float screenY){
        return (screenY-Y_OFFSET) / TILE_HEIGHT -  (screenX -X_OFFSET) / TILE_WIDTH ;
    }
    public Vector2f screenToGrid(float screenX, float screenY){
        return new Vector2f(screenToGridX(screenX,screenY), screenToGridY(screenX,screenY));
    }
    public Vector2f screenToGrid(Vector2f screenPos){
        return new Vector2f(screenToGridX(screenPos.x,screenPos.y), screenToGridY(screenPos.x,screenPos.y));
    }
    public float gridToScreenX(float cellX, float cellY){
        return TILE_WIDTH * (cellX * 0.5f - cellY  * 0.5f);
    }
    public float gridToScreenY(float cellX, float cellY){
        return  TILE_HEIGHT * (cellX * 0.5f  + cellY  * 0.5f);
    }

    public Vector2f gridToScreen(float cellX, float cellY){

        return new Vector2f(gridToScreenX(cellX,cellY) + X_OFFSET, gridToScreenY(cellX,cellY) + Y_OFFSET);
    }
    public Vector2f gridToScreen(Vector2f worldPos){

        return new Vector2f(gridToScreenX(worldPos.x,worldPos.y) + X_OFFSET, gridToScreenY(worldPos.x,worldPos.y) + Y_OFFSET);
    }

    public Renderer getRenderer() {
        return renderer;
    }
    public void setPlayer(GameObject player) {
        this.player = player;
    }

}
