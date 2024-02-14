package renderer;

import Game.RenderObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void addRenderOb(RenderObject ob) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.getZIndex() == ob.getZIndex()) {
                Texture tex = ob.sprite.getTexture();
                if(tex != null && batch.hasTextureRoom()){
                    batch.addRenderOb(ob);
                    added = true;
                    break;
                }
            }
        }
        if(!added && ob.sprite.getTexture() != null) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE,ob.getZIndex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addRenderOb(ob);
        }
    }
    public void render() {
        batches.sort(Comparator.comparing(RenderBatch::getZIndex));
        for(RenderBatch batch : batches) {
            batch.render();
        }
    }
}
