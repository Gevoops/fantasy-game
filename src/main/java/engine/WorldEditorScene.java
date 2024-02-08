package engine;


import org.joml.Matrix2f;
import org.joml.Vector2f;
import renderer.SpriteRenderer;
import renderer.Texture;
import util.AssetPool;

public class WorldEditorScene extends Scene {
    public static Matrix2f isoMatrix = new Matrix2f( +0.5f * Camera.spriteWidth,  -0.5f * Camera.spriteHeight,
                                                       0.99f * Camera.spriteWidth , 0.99f*  Camera.spriteHeight);
    public WorldEditorScene() {

    }
    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        int xOffset = -1200;
        int yOffset = -200;

        float sizeX = 100.0f;
        float sizeY =  50.0f;
        Texture tex = new Texture("assets/sprites/isoGrass.png");
        Texture tex1 = new Texture("assets/sprites/Walking_KG_1.png");
        Texture tex3 = new Texture("assets/sprites/orc_ranger.png");


        for (int y = 0; y < 45; y ++) {
            for (int x = 0; x < 45; x ++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject ob = new GameObject("obj" + xPos + "," + yPos,
                        new SpriteRenderer(tex,
                        new Transform(new Vector2f(xPos,yPos).mul(isoMatrix),new Vector2f(sizeX,sizeY))));

                this.addGameObjectToScene(ob);


            }
        }

        GameObject ob1 = new GameObject("obj" +",",
                new SpriteRenderer(tex1,
                        new Transform(new Vector2f(300,300),new Vector2f(300,32))));
        this.addGameObjectToScene(ob1);


        GameObject ob2 = new GameObject("obj" +",",
                new SpriteRenderer(tex3,
                        new Transform(new Vector2f(0,0),new Vector2f(sizeX,sizeY))));
        this.addGameObjectToScene(ob2);
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
