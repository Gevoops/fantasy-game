package engine;

import renderer.Sprite;

public class GameObject {
    protected String name;
    public Sprite sprite;
    public Transform transform;

    public GameObject(String name, Sprite sprite, Transform transform)
    {
        this.name = name;
        this.sprite = sprite;
        this.transform = transform;
    }

    public void update(double dt) {

    }
}
