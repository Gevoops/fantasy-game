package scenes;

import components.Component;
import components.ComponentSerializer;
import editor.GameViewport;
import engine.Camera;
import engine.GameObjectSerializer;
import exceptions.GameObjectNotFoundException;
import engine.GameObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
    public ArrayList<GameObject> gameObjects = new ArrayList<>();
    protected boolean levelLoaded = false;
    protected String savedWorldPath;
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
            } catch (Exception e) {
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
            }catch (Exception e){
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
                addGameObjectToScene(go);
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
    public GameObject findGameObject(int gameObjectID) throws GameObjectNotFoundException {
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
    }

    public boolean deleteGameObj(GameObject obj){
        obj.getBatch().deleteGameObj(obj);
        gameObjects.remove(obj);
        return true;
    }

    // grid methods =============================================================================================================================================



}
