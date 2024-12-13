package engine;

import exceptions.NullSpriteException;
import game.GameObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import imgui.ImGui;
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
            FileWriter writer  = new FileWriter("world.txt");
            writer.write(gson.toJson(this.gameObjects,new TypeToken<ArrayList<GameObject>>(){}.getType()));
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(GameObject.class, new GameObjectSerializer())
                .registerTypeAdapter(Component.class, new ComponentSerializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("world.txt")));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!inFile.equals("")){
            ArrayList<GameObject> objects = gson.fromJson(inFile, new TypeToken<ArrayList<GameObject>>(){}.getType());
            for (GameObject go : objects) {
                addGameObjectToScene(go);
            }
        }
        this.levelLoaded = true;
    }



    public abstract void update(double dt);

    public Camera camera() {
        return this.camera;
    }
}
