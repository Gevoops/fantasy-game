package components;

import engine.Component;
import renderer.SpriteSheet;

import java.util.ArrayList;


public class SpriteSheetList extends Component {
    public ArrayList<SpriteSheet> spriteSheets = new ArrayList<>();

    public SpriteSheetList(){

    }

    public void addSpriteSheet(SpriteSheet spriteSheet){
        spriteSheets.add(spriteSheet);
    }

    public SpriteSheet get(int index){
        return spriteSheets.get(index);
    }

    public ArrayList<SpriteSheet> getSpriteSheets(){
        return spriteSheets;
    }
}
