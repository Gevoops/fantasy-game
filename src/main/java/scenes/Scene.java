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



    public abstract void update(double dt);

    public Camera getCamera() {
        return this.camera;
    }
}
