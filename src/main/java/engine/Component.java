package engine;

import game.GameObject;

public abstract class Component {

    public transient GameObject gameObject = null;

    public void update(double dt){

    }
    public void start(){

    }
}
