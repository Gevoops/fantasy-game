package engine;

import renderer.Renderer;

import java.util.ArrayList;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();


    public Scene() {

    }

    public void init() {

    }

    public void start(){
            for(GameObject ob : gameObjects) {
                this.renderer.add(ob);
            }
            isRunning = true;
    }

    public void addGameObjectToScene(GameObject ob) {
        gameObjects.add(ob);
        if(isRunning) {
            this.renderer.add(ob);
        }
    }

    public abstract void update(double dt);

    public Camera camera() {
        return this.camera;
    }
}
