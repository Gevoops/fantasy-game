package renderer;

import engine.GameObject;

import java.util.*;

public class Renderer {
    private final int MAX_BATCH_SIZE = 2500;
    public List<RenderBatch> batches;
    public static Shader currentShader;
    public static Comparator<GameObject> byDepth = Comparator.comparingDouble(o -> o.getTileCoordsX() - o.getTileCoordsY() + o.getHeight() * 2.1);
    public List<GameObject> dirtyList = new ArrayList<>();

    public Renderer() {
        this.batches = new ArrayList<>();
    }

    public void render() {
        currentShader.use();
        for (GameObject go : dirtyList){
            go.setDirty(false);
            go.getRenderBatch().deleteGameObj(go);
            addGameObject(go);
        }
        dirtyList.clear();

        batches.sort(Comparator.comparing(batch -> batch.isEmpty() ? null : batch.get(batch.size() -1 ), Comparator.nullsFirst(byDepth)));
        for(RenderBatch batch : batches) {
            batch.render();
        }
    }

    public void addGameObject(GameObject go)  {

        batches.removeIf(Objects::isNull);
        RenderBatch candidateBatch = findInsertionBatch(go);

        if (candidateBatch == null){
            candidateBatch = new RenderBatch(MAX_BATCH_SIZE);
            candidateBatch.start();
            candidateBatch.addGameObject(go);
            batches.add(candidateBatch);
            return;
        }

        if (candidateBatch.hasRoom() && candidateBatch.hasTextureRoom()) {
            candidateBatch.addGameObject(go);
            return;
        }

        split(candidateBatch,go);
    }


    private RenderBatch findInsertionBatch(GameObject go){
        for (int i = 0; i < batches.size(); i++) {
            GameObject prevLastObject = i == 0 || batches.get(i - 1).isEmpty() ? null : batches.get(i - 1).get(batches.get(i - 1).size() -1);
            GameObject nextFirstObject = i == batches.size() - 1 || batches.get(i + 1).isEmpty() ? null : batches.get(i + 1).get(0);

            boolean biggerThanLast = Comparator.nullsFirst(byDepth).compare(prevLastObject,go) <= 0;
            boolean smallerThanNext = Comparator.nullsLast(byDepth).compare(go,nextFirstObject) <= 0;
            if(biggerThanLast && smallerThanNext ) {
                return batches.get(i);

            }
        }
        return null;
    }

    private void split(RenderBatch candidateBatch, GameObject go){
        RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
        newBatch.start();
        ArrayList<GameObject> batchGoList = candidateBatch.getGameObjects();
        int index = Collections.binarySearch(batchGoList, go, Renderer.byDepth);
        index = index < 0 ? -index - 1 : index;
        List<GameObject> tail = new ArrayList<>(batchGoList.subList(index,batchGoList.size()));
        for (GameObject tailGo : tail){
            newBatch.addGameObject(tailGo);
        }
        batchGoList.subList(index,batchGoList.size()).clear();
        newBatch.addGameObject(go);
        batches.add(batches.indexOf(candidateBatch) + 1, newBatch);
        System.out.println("=======================================================split");
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
