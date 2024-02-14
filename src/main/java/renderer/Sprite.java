package renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Sprite{
    private Vector4f color;
    private Texture texture;
    private Vector2f[] texCoords;

    public Sprite(Texture texture, Vector2f[] texCoords) {
        this.texture = texture;
        this.texCoords = texCoords;
        this.color = new Vector4f(1,1,1,1);
    }

    public Sprite(Texture texture) {
        this.texture = texture;

        this.texCoords =  new Vector2f[] {
                new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1),
                new Vector2f(0,0)
        };
        this.color = new Vector4f(1,1,1,1);
    }

    public Sprite(Sprite sprite) {
        this.color = sprite.getColor();
        this.texture = sprite.getTexture();
        this.texCoords = sprite.getTexCoords();
    }

    public Vector2f[] getTexCoords() {
        return this.texCoords;
    }

    public Vector4f getColor() {
        return this.color;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setColor(Vector4f color) {
        this.color.set(color);
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }
}
