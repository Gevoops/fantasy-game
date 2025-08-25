package renderer;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Sprite{
    public Vector4f color = new Vector4f(1,1,1,1); // important for textures
    private Texture texture = null;
    public Vector2f[] texCoords = new Vector2f[] {
        new Vector2f(1,0),
                new Vector2f(1,1),
                new Vector2f(0,1),
                new Vector2f(0,0)};

    float width,height;
    String spriteSheetName = "";
    int spriteSheetIndex = 0;

    public Sprite() {
        color = new Vector4f(0,0,0,0);
    }

    public Sprite(Texture texture, Vector2f[] texCoords){
        this.texture = texture;
        this.texCoords = texCoords;
    }
    public Sprite(Sprite sprite) {
        this.color = new Vector4f(sprite.color);
        this.texture = sprite.texture;
        this.texCoords = sprite.texCoords;
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


    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexId(){
        return texture == null ? -1 : texture.getID();
    }

    public String getSpriteSheetName() {
        return spriteSheetName;
    }

    public void setSpriteSheetName(String spriteSheetName) {
        this.spriteSheetName = spriteSheetName;
    }

    public int getSpriteSheetIndex() {
        return spriteSheetIndex;
    }

    public void setSpriteSheetIndex(int spriteSheetIndex) {
        this.spriteSheetIndex = spriteSheetIndex;
    }
}
