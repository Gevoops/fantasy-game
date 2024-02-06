package renderer;

import engine.Transform;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRenderer {

    private Vector4f color;
    public Transform transform;
    private Vector2f[] texCoords;
    private Texture texture;

    public SpriteRenderer(Texture texture, Transform transform) {
        this.texture = texture;
        this.color = new Vector4f(1,1,1,1);
        this.transform = transform;
    }

    public SpriteRenderer(Vector4f color, Transform transform) {
        this.color = color;
        this.transform = transform;
        this.texture = null;
    }



    public void start() {

    }


    public void update(double dt) {

    }


    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public Vector2f[] getTexCoords() {
        return new Vector2f[]{
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1),
                new Vector2f(0,0)
        };
    }
}
