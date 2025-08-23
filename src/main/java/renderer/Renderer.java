package renderer;

import exceptions.NullSpriteException;
import engine.GameObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 2500;
    public List<RenderBatch> batches;
    public static Shader currentShader;

    public Renderer() {
        this.batches = new ArrayList<>();
    }
    public void render() {
        currentShader.use();
        batches.sort(Comparator.comparing(RenderBatch::getZIndex));
        for(RenderBatch batch : batches) {
            batch.render();
        }
    }

    public void addGameObject(GameObject ob) throws Exception {
        if(ob.getSprite() == null){
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


    public void clear(){

    }


    public static Shader getCurrentShader(){
        return currentShader;
    }
    public static void setCurrentShader(Shader shader){
        currentShader = shader;
    }
}
