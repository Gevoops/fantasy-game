package util;

import org.joml.Vector2f;

import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;

// tilecoord is the in game coordinates in tile number.
// screen is absolute screen x y


public class Tiles {
    private static final float X_OFFSET = 0;
    private static final float Y_OFFSET = 0;


    public static float tileToWorldX(float tileX, float tileY){
        return TILE_WIDTH * (tileX * 0.5f - tileY  * 0.5f);
    }
    public static float tileToWorldY(float tileX, float tileY) { return TILE_HEIGHT * (tileX * 0.5f  + tileY  * 0.5f); }

    public static float worldToTileX(float screenX, float screenY){
        return (screenX - X_OFFSET) / TILE_WIDTH +  (screenY - Y_OFFSET) / TILE_HEIGHT;
    }
    public static float worldToTileY(float screenX, float screenY){
        return (screenY - Y_OFFSET) / TILE_HEIGHT -  (screenX - X_OFFSET) / TILE_WIDTH ;
    }

    public static Vector2f worldToTile(Vector2f worldPos){
        return new Vector2f(worldToTileX(worldPos.x,worldPos.y), worldToTileY(worldPos.x,worldPos.y));
    }

    public static Vector2f tileToWorld(float tileX, float tileY){
        return new Vector2f(tileToWorldX(tileX,tileY) + X_OFFSET, tileToWorldY(tileX,tileY) + Y_OFFSET);
    }



    // returns screen pos of bottom left corner of the tile screenX and screenY are in
    public static Vector2f snapToTile(float screenX, float screenY){
        return snapScreenToTile(new Vector2f(screenX,screenY)).add(-TILE_WIDTH / 2 ,0);
    }

    public static Vector2f snapScreenToTile(Vector2f screenPos){
    Vector2f worldPos = worldToTile(screenPos);
    return tileToWorld((float)Math.floor(worldPos.x) ,(float) Math.floor(worldPos.y));
    }

    public static Vector2f tileSnapToTile(float tileX, float tileY){
        return snapToTile(tileToWorldX(tileX,tileY), tileToWorldY(tileX,tileY));
    }









}
