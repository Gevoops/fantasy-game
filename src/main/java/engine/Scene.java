package engine;

import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;


    public Scene() {

    }

    public void init() {

    }

    public void start(){

            isRunning = true;
    }



    public abstract void update(double dt);

    public Camera camera() {
        return this.camera;
    }
}
