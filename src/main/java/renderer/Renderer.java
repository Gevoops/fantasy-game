package renderer;

import exceptions.NullSpriteException;
import game.GameObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void addGameObject(GameObject ob) throws NullSpriteException {
        if(ob.sprite == null){
            throw new NullSpriteException("renderer.addGameObject, " + ob.getName() + "sprite is null");
        }
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.getZIndex() == ob.getZIndex()) {
                if(batch.hasTextureRoom()){
                    batch.addGameObject(ob);
                    added = true;
                    break;
                }
            }
        }
        if(!added ) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE,ob.getZIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addGameObject(ob);
        }
    }
    public void render() {
        batches.sort(Comparator.comparing(RenderBatch::getZIndex));
        for(RenderBatch batch : batches) {
            batch.render();
        }
    }
}
