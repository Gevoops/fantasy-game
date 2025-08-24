package util;

import engine.GameObject;
import org.joml.Vector2d;
import org.joml.Vector2d;

import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;

// tilecoord is the in game coordinates in tile number.
// screen is absolute screen x y


public class Tiles {


    public static double tileToWorldX(double tileX, double tileY){
        return TILE_WIDTH * 0.5f * (tileX + tileY);
    }
    public static double tileToWorldY(double tileX, double tileY) { return - TILE_HEIGHT * 0.5f * (tileX - tileY); }

    public static double worldToTileX(double screenX, double screenY){
        return (screenX ) / TILE_WIDTH - (screenY ) / TILE_HEIGHT;
    }
    public static double worldToTileY(double screenX, double screenY){
        return (screenY ) / TILE_HEIGHT + (screenX ) / TILE_WIDTH ;
    }

    public static Vector2d worldToTileCoords(Vector2d worldPos){
        return new Vector2d(worldToTileX(worldPos.x,worldPos.y), worldToTileY(worldPos.x,worldPos.y));
    }

    public static Vector2d tileAt(Vector2d worldPos){
        return worldToTileCoords(worldPos).floor();
    }

    public static Vector2d tileAt(double x, double y){
        return tileAt(new Vector2d(x,y));
    }

    public static Vector2d tileToWorld(double tileX, double tileY){
        return new Vector2d(tileToWorldX(tileX,tileY) , tileToWorldY(tileX,tileY) );
    }

    // snap methods return world pos of bottom left corner of the tile screenX and screenY are in

    public static Vector2d snapWorldToTile(Vector2d screenPos){
        Vector2d tile = tileAt(screenPos);
        return tileToWorld(tile.x ,tile.y);
    }

    public static Vector2d tileSnapToTile(double tileX, double tileY){
        return snapToTile(tileToWorldX(tileX,tileY), tileToWorldY(tileX,tileY));
    }

    public static Vector2d snapToTile(double screenX, double screenY){
        return snapWorldToTile(new Vector2d(screenX,screenY)).add(0 ,-TILE_HEIGHT/2);
    }

    public static long calcMapKey(GameObject go){
        int x = (int)go.getCurrentTile().x;
        int y = (int)go.getCurrentTile().y;
        return (long)x << 32 | (y & 0xFFFFFFFFL);
    }










}
