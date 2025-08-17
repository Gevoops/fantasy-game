package util;

import org.joml.Vector2f;

import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;


public class Tiles {
    private static final float X_OFFSET = 0;
    private static final float Y_OFFSET = 0;


    public static float tileToScreenX(float tileX, float tileY){
        return TILE_WIDTH * (tileX * 0.5f - tileY  * 0.5f);
    }
    public static float tileToScreenY(float tileX, float tileY){
        return  TILE_HEIGHT * (tileX * 0.5f  + tileY  * 0.5f);
    }

    public static Vector2f tileToScreen(float tileX, float tileY){
        return new Vector2f(tileToScreenX(tileX,tileY) + X_OFFSET, tileToScreenY(tileX,tileY) + Y_OFFSET);
    }
    public static Vector2f tileToScreen(Vector2f worldPos){
        return new Vector2f(tileToScreenX(worldPos.x,worldPos.y) + X_OFFSET, tileToScreenY(worldPos.x,worldPos.y) + Y_OFFSET);
    }


    // returns screen coords of the tile you are in
    public static Vector2f snapScreenToTile(Vector2f screenPos){
    Vector2f worldPos = screenToTile(screenPos);
    return tileToScreen((float)Math.floor(worldPos.x) ,(float) Math.floor(worldPos.y));
    }
    public static Vector2f snapScreenToTile(float screenX, float screenY){
        return snapScreenToTile(new Vector2f(screenX,screenY));
    }


    public static float screenToTileX(float screenX, float screenY){
        return (screenX- X_OFFSET) / TILE_WIDTH +  (screenY - Y_OFFSET) / TILE_HEIGHT;
    }
    public static float screenToTileY(float screenX, float screenY){
        return (screenY-Y_OFFSET) / TILE_HEIGHT -  (screenX -X_OFFSET) / TILE_WIDTH ;
    }

    public static Vector2f screenToTile(float screenX, float screenY){
        return new Vector2f(screenToTileX(screenX,screenY), screenToTileY(screenX,screenY));
    }
    public static Vector2f screenToTile(Vector2f screenPos){
        return new Vector2f(screenToTileX(screenPos.x,screenPos.y), screenToTileY(screenPos.x,screenPos.y));
    }


    public static Vector2f tileSnapToTile(float tileX, float tileY){
        return snapToTile(tileToScreenX(tileX,tileY), tileToScreenY(tileX,tileY));
    }
    public static Vector2f snapToTile(float screenX, float screenY){
        return snapScreenToTile(screenX,screenY).add(-TILE_WIDTH /2 ,0);
    }

    public static Vector2f snapToTile(Vector2f screenPos){
        return snapToTile(screenPos.x,screenPos.y) ;
    }

}
