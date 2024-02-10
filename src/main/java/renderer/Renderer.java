package renderer;

import engine.GameObject;
import engine.Transform;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void addSprite(GameObject ob) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom()) {
                Texture tex = ob.sprite.getTexture();
                if(tex != null && (batch.hasTexture(tex) || batch.hasTextureRoom())) {
                    batch.addSprite(ob);
                    added = true;
                    break;
                }
            }
        }
        if(!added && ob.sprite.getTexture() != null) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(ob);
        }
    }
    public void render() {
        for(RenderBatch batch : batches) {
            batch.render();
        }
    }
}
