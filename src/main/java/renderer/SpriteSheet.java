package renderer;


import org.joml.Vector2f;

import java.util.ArrayList;


public class SpriteSheet {
    private String name;
    private String path;
    private Texture texture = null;
    private ArrayList<Sprite> sprites = null;
    private float tileSizeRatio = 0;

    public SpriteSheet(){

    }
    public SpriteSheet(String path,String name,Texture texture, int spriteWidth, int spriteHeight,
                       int numSprites, int spacing){
        this(path ,name,texture,spriteWidth,spriteHeight,numSprites,spacing,0);
    }
    public SpriteSheet(String path, String name, Texture texture, int spriteWidth, int spriteHeight,
                       int numSprites, int spacing, float tileSizeRatio){
        this.tileSizeRatio = tileSizeRatio;
        this.sprites = new ArrayList<>();
        this.path = path;
        this.name = name;
        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;
        for (int i = 0; i < numSprites; i++) {
            float topY = (currentY + spriteHeight) / (float) texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float) texture.getWidth();
            float leftX = currentX / (float) texture.getWidth(); //normalising coords to texture size
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, bottomY),
                    new Vector2f(rightX, topY),
                    new Vector2f(leftX, topY),
                    new Vector2f(leftX, bottomY)
            };
            Sprite sprite = new Sprite(texture, texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            sprite.setSpriteSheetName(name);
            sprite.setSpriteSheetIndex(i);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth()) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index) {
        return this.sprites.get(index);
    }

    public Texture getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }

    public float getTileSizeRatio() {
        return tileSizeRatio;
    }
}
