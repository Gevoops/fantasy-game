package engine;

import org.joml.Vector4f;
import renderer.Sprite;

public class GameObject {
    protected String name;
    public Sprite sprite;
    public Transform transform;
    public Transform lastTransform;
    private boolean isDirty;


    public GameObject(String name, Sprite sprite, Transform transform)
    {
        this.name = name;
        this.sprite = sprite;
        this.isDirty = true;
        this.transform = transform;
        lastTransform = this.transform.copy();
    }

    public void update(double dt) {
        if(!this.transform.equals(lastTransform)) {
            this.transform.copy(lastTransform);
            isDirty = true;
        }
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Vector4f color) {
        if(!this.sprite.getColor().equals(color)) {
            this.sprite.setColor(color);
            isDirty = true;
        }
    }

    public void setClean() {
        this.isDirty = false;
    }
}
