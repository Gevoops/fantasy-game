package tiles;

import engine.GameObject;
import scenes.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TileObjectMap {
    Scene scene;
    Map<Long, List<GameObject>> goMap = new HashMap<>();

    public TileObjectMap(Scene currentScene){
        this.scene = currentScene;
    }


    public void remove(GameObject go){
        Long key = go.getTileMapKey();
        if (key  == null) return;
        goMap.get(key).remove(go);
    }
    public void add(GameObject go) {
        long tileCoords = calcMapKey(go);
        List<GameObject> objectsOnTile = goMap.get(tileCoords);
        if(objectsOnTile == null){
            objectsOnTile = new ArrayList<>();
            goMap.put(tileCoords,objectsOnTile);
        } else {
            for(GameObject go1 : objectsOnTile) {
                if (go != go1 && go.getType() ==  GameObject.TILE && go1.getType() == GameObject.TILE) {
                    objectsOnTile.remove(go1);
                    go1.setDeathMark(true);
                    break;
                }
            }
        }
        go.setTileMapKey(tileCoords);
        objectsOnTile.add(go);
    }

    public static long calcMapKey(GameObject go){
        int x = (int)go.getCurrentTile().x;
        int y = (int)go.getCurrentTile().y;
        return (long)x << 32 | (y & 0xFFFFFFFFL);
    }
    public static long calcMapKey(int TileX, int TileY){
        return (long)TileX << 32 | (TileY & 0xFFFFFFFFL);
    }

    public List<GameObject> getObjectsAt(int TileX, int TileY){
        return goMap.get(calcMapKey(TileX,TileY));
    }
}
