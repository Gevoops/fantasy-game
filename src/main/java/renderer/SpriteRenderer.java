package renderer;

import engine.Transform;
import org.joml.Vector4f;

public class SpriteRenderer {

    private Vector4f color;
    public Transform transform;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }



    public void start() {

    }


    public void update(double dt) {

    }


    public Vector4f getColor() {
        return this.color;
    }
}
