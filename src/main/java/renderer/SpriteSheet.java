package renderer;


import components.Component;
import org.joml.Vector2f;

import java.util.ArrayList;


public class SpriteSheet extends Component {
    public String name = "";
    public Texture texture = null;
    public ArrayList<Sprite> sprites = null;

    public SpriteSheet(){

    }
    public SpriteSheet(String name, Texture texture, int spriteWidth, int spriteHeight,
                       int numSprites, int spacing){
        this.sprites = new ArrayList<>();
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
            sprite.setSpriteSheet(name);
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

}
