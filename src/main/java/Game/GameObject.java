package Game;

import engine.Transform;
import imgui.ImGui;
import org.joml.Vector4f;
import renderer.Sprite;
import renderer.SpriteSheet;


import java.util.ArrayList;

public class GameObject {
    protected String name = "default";
    public Sprite sprite = null;
    public ArrayList<SpriteSheet> spriteSheets = new ArrayList<>();
    public Transform transform = null;
    public Transform lastTransform = null;
    private boolean isDirty = true;
    private int zIndex = 0;

    public GameObject(){

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



    public void addSpriteSheet(SpriteSheet s) {
        this.spriteSheets.add(s);
    }

    public void imGui(){
        Vector4f color = sprite.getColor();
        float[] colors = {color.x,color.y,color.w,color.z};
        if(ImGui.colorPicker4("picker",colors)) {
            this.sprite.setColor(new Vector4f(colors[0],colors[1],colors[2],colors[3]));
            this.isDirty = true;
        }
    }

    public void setClean() {
        this.isDirty = false;
    }

    public int getZIndex() {
        return this.zIndex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpriteSheets(ArrayList<SpriteSheet> spriteSheets) {
        this.spriteSheets = spriteSheets;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
        lastTransform = transform.copy();

    }

    public void setLastTransform(Transform lastTransform) {
        this.lastTransform = lastTransform;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
    }
}
