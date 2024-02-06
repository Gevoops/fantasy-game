package renderer;

import engine.GameObject;
import engine.Scene;
import engine.WorldEditorScene;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void add(GameObject ob) {
        SpriteRenderer spr = ob.sprite;
        if(spr != null) {
            this.add(spr);
        }
    }
    public void add(SpriteRenderer spr) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom()) {
                batch.addSprite(spr);
                added = true;
                break;
            }
        }
        if(!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spr);
        }
    }
    public void render() {
        for(RenderBatch batch : batches) {
            batch.render();
        }
    }
}
