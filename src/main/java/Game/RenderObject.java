package Game;

import engine.Transform;
import org.joml.Vector4f;
import renderer.Sprite;
import renderer.SpriteSheet;


import java.util.ArrayList;

public class RenderObject {
    protected String name;
    public Sprite sprite;
    public ArrayList<SpriteSheet> spriteSheets = new ArrayList<>();
    public Transform transform;
    public Transform lastTransform;
    private boolean isDirty;
    private int zIndex;



    public RenderObject(String name, Sprite sprite, Transform transform, int zIndex)
    {
        this.name = name;
        this.sprite = sprite;
        this.isDirty = true;
        this.transform = transform;
        this.zIndex = zIndex;
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

    public void addSpriteSheet(SpriteSheet s) {
        this.spriteSheets.add(s);
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

    public int getZIndex() {
        return this.zIndex;
    }
}
