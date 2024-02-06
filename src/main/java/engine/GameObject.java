package engine;

import renderer.SpriteRenderer;

public class GameObject {
    private String name;
    public SpriteRenderer sprite;

    public GameObject(String name, SpriteRenderer sprite)
    {
        this.name = name;
        this.sprite = sprite;
    }

    public void update(double dt) {

    }
}
