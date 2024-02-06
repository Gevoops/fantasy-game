package engine;

import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.SpriteRenderer;
import renderer.Texture;
import util.AssetPool;

public class WorldEditorScene extends Scene {
    public WorldEditorScene() {

    }
    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        int xOffset = 0;
        int yOffset = -250;

        float totalWidth = (float)(100);
        float totalHeight = (float)(100);
        float sizeX = 50.0f;
        float sizeY =  50.0f;

        Texture tex = new Texture("assets/sprites/grass.png");
        for (int x = 0; x < 20; x ++) {
            for (int y = 0; y < 20; y ++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject ob = new GameObject("obj" + xPos + "," + yPos,
                        new SpriteRenderer(tex,
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
