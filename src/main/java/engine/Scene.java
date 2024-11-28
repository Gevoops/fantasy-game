package engine;

import Game.GameObject;
import imgui.ImGui;
import renderer.Renderer;

import java.util.ArrayList;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected GameObject activeGameObject = null;
    public ArrayList<GameObject> gameObjects = new ArrayList<>();



    public Scene() {

    }

    public void init() {

    }

    public void start(){
        for(GameObject ob : gameObjects) {
            this.renderer.addGameObject(ob);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject ob) {
        gameObjects.add(ob);
        if(isRunning) {
            this.renderer.addGameObject(ob);
        }
    }

    public void sceneImgui(){
        if(activeGameObject != null) {
            ImGui.begin("inspector");
            activeGameObject.imGui();
            ImGui.end();
        }
        imGui();
    }

    public void imGui(){


    }



    public abstract void update(double dt);

    public Camera camera() {
        return this.camera;
    }
}
