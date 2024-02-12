package engine;

import Game.RenderObject;
import renderer.Renderer;

import java.util.ArrayList;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    public ArrayList<RenderObject> RenderObjects = new ArrayList<>();



    public Scene() {

    }

    public void init() {

    }

    public void start(){
        for(RenderObject ob : RenderObjects) {
            this.renderer.addRenderOb(ob);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(RenderObject ob) {
        RenderObjects.add(ob);
        if(isRunning) {
            this.renderer.addRenderOb(ob);
        }
    }

    public abstract void update(double dt);

    public Camera camera() {
        return this.camera;
    }
}
