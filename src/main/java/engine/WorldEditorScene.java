package engine;

import org.joml.Vector2f;

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

        for (int x = 0; x < 100; x ++) {
            for (int y = 0; y < 100; y ++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);


            }
        }
    }

    float zoom = 0.9965f;
    @Override
    public void update(double dt) {
        System.out.println(1.0f / dt);

        this.renderer.render();
    }
}
