package util;

import org.joml.Vector2f;

import static util.Settings.TILE_HEIGHT;
import static util.Settings.TILE_WIDTH;


public class Grid {
    private static final float X_OFFSET = 0;
    private static final float Y_OFFSET = 0;


    public static Vector2f snapScreenToGrid(Vector2f screenPos){
    Vector2f worldPos = screenToGrid(screenPos);
    return gridToScreen((float)Math.floor(worldPos.x) ,(float) Math.floor(worldPos.y));
    }
    public static Vector2f snapScreenToGrid(float screenX, float screenY){
        return snapScreenToGrid(new Vector2f(screenX,screenY));
    }

    public static float screenToGridX(float screenX, float screenY){
        return (screenX- X_OFFSET) / TILE_WIDTH +  (screenY - Y_OFFSET) / TILE_HEIGHT;
    }
    public static float screenToGridY(float screenX, float screenY){
        return (screenY-Y_OFFSET) / TILE_HEIGHT -  (screenX -X_OFFSET) / TILE_WIDTH ;
    }
    public static Vector2f screenToGrid(float screenX, float screenY){
        return new Vector2f(screenToGridX(screenX,screenY), screenToGridY(screenX,screenY));
    }
    public static Vector2f screenToGrid(Vector2f screenPos){
        return new Vector2f(screenToGridX(screenPos.x,screenPos.y), screenToGridY(screenPos.x,screenPos.y));
    }
    public static float gridToScreenX(float cellX, float cellY){
        return TILE_WIDTH * (cellX * 0.5f - cellY  * 0.5f);
    }
    public static float gridToScreenY(float cellX, float cellY){
        return  TILE_HEIGHT * (cellX * 0.5f  + cellY  * 0.5f);
    }

    public static Vector2f gridToScreen(float cellX, float cellY){

        return new Vector2f(gridToScreenX(cellX,cellY) + X_OFFSET, gridToScreenY(cellX,cellY) + Y_OFFSET);
    }
    public static Vector2f gridToScreen(Vector2f worldPos){

        return new Vector2f(gridToScreenX(worldPos.x,worldPos.y) + X_OFFSET, gridToScreenY(worldPos.x,worldPos.y) + Y_OFFSET);
    }
    public static Vector2f cellSnapToGrid(float cellX, float cellY){
        return snapToGrid(gridToScreenX(cellX,cellY), gridToScreenY(cellX,cellY));
    }
    public static Vector2f snapToGrid(float screenX, float screenY){
        return snapScreenToGrid(screenX,screenY).add(-TILE_WIDTH /2 ,0);
    }

    public static Vector2f snapToGrid(Vector2f screenPos){
        return snapToGrid(screenPos.x,screenPos.y) ;
    }

}
