package engine;

import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.SpriteRenderer;
import util.AssetPool;

public class WorldEditorScene extends Scene {
    public WorldEditorScene() {

    }
    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x = 0; x < 130; x ++) {
            for (int y = 0; y < 130; y ++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject ob = new GameObject("obj" + xPos + "," + yPos,
                        new SpriteRenderer(new Vector4f(xPos / totalWidth,yPos / totalHeight,0.4f ,1),
                        new Transform(new Vector2f(xPos,yPos),new Vector2f(sizeX,sizeY))));

                this.addGameObjectToScene(ob);

            }
        }

       loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(double dt) {
        System.out.println(1.0f / dt);

        this.renderer.render();
    }
}
