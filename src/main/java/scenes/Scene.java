package scenes;

import components.Component;
import components.ComponentSerializer;
import editor.GameViewport;
import engine.Camera;
import engine.GameObjectSerializer;
import engine.MouseControllerStrategy;
import engine.GameObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import renderer.Renderer;
import tiles.TileObjectMap;


import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;


public abstract class Scene {
    protected TileObjectMap objectMap;
    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected HashSet<GameObject> gameObjectsForDeath = new HashSet<>();
    protected boolean levelLoaded = false;
    protected String savedWorldPath;
    protected GameObject player;
    protected MouseControllerStrategy mouseController;


    public Scene() {

    }

    public void init() {

    }

    public void start(){
        for(GameObject go : gameObjects) {
            go.start();
        }
        isRunning = true;
    }

    public void addGameObject(GameObject go) {
        gameObjects.add(go);
        if(isRunning) {
            try {
                this.renderer.addGameObject(go);
            }catch (Exception e){
                e.printStackTrace();
            }
            go.start();
        }
    }

    public void sceneImGui(){
        imGui();
    }

    public void imGui(){

    }

    public abstract GameViewport getGameViewport();

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
                addGameObject(go);
                for (Component c : go.getComponents()){
                    if(c.getCompID() > maxCompId){
                        maxCompId = c.getCompID();
                    }
                }
                if (go.getID() > maxObjId){
                    maxObjId = go.getID();
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
    public GameObject findGameObject(int gameObjectID) {
        if (gameObjectID <= 0) return null;
        for (GameObject ob : gameObjects){
            if (ob.getID() == gameObjectID){
                return ob;
            }
        }
        return null;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public Renderer getRenderer() {
        return renderer;
    }
    public void setPlayer(GameObject player) {
        this.player = player;
        player.setType(GameObject.PLAYER);
    }

    public MouseControllerStrategy getMouseController() {
        return mouseController;
    }

    public void setMouseController(MouseControllerStrategy mouseController) {
        this.mouseController = mouseController;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public TileObjectMap getObjectMap() {
        return objectMap;
    }

    public boolean imGuiWantsMouse() {
        return false;
    }
}
