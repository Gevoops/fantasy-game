package components;

import engine.GameObject;
import engine.Window;
import org.joml.Vector2d;
import tiles.TileGrid;

import java.util.ArrayList;

public class Collider extends Component{
    private Vector2d position;
    private double radius = 1;



    @Override
    public void update(float dt){
        if(!this.gameObject.isDynamic()) return;


    }

    private Vector2d nextTile(){
        Vector2d movement = this.gameObject.getComponent(Player.class).getMovement();
        Vector2d currentTile = this.gameObject.getCurrentTile();
        return currentTile;
    }
}
