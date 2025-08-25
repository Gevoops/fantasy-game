package engine;

import org.joml.Vector2d;
import renderer.Sprite;
import renderer.Transform;

public class Prefabs {

    public static GameObject generateObject(Sprite sprite, double sizeX, double sizeY){
        return new Tile("Sprite_Object_Gen",new Sprite(sprite),
                new Transform(new Vector2d(0,0),new Vector2d(sizeX,sizeY)),0);
    }
}
