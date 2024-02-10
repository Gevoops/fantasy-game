package engine;


import org.joml.Matrix2f;
import org.joml.Vector2f;
import renderer.Sprite;
import renderer.SpriteSheet;
import renderer.Texture;
import util.AssetPool;

public class WorldEditorScene extends Scene {
    public static Matrix2f isoMatrix = new Matrix2f(0.5f, -0.25f * 0.975f,
                                                     1f, 0.5f * 0.99f );
    int frameCount = 0;
    int spriteIndex;
    float move = 0;
    GameObject ob1;
    SpriteSheet spriteSheet = null;

    public WorldEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());

        this.spriteSheet = AssetPool.getSpriteSheet("assets/sprites/Walking_KG_2.png");


        int xOffset = -1000;
        int yOffset = -100;

        float sizeX = 64.0f;
        float sizeY = 32.0f;
        Texture tex = new Texture("assets/sprites/isoGrass1.png");


        for (int y = 45; y > 0; y--) {
            for (int x = 45; x > 0; x--) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject ob = new GameObject("obj" + xPos + "," + yPos,
                        new Sprite(tex),
                        new Transform(new Vector2f(xPos, yPos).mul(isoMatrix), new Vector2f(sizeX, sizeY)));

                this.addGameObjectToScene(ob);


            }
        }

        this.ob1 = new GameObject("obj" + ",", spriteSheet.getSprite(6), new Transform(new Vector2f(300, 300), new Vector2f(50, 32)));
        this.addGameObjectToScene(this.ob1);


    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/sprites/Walking_KG_2.png",
                new SpriteSheet(AssetPool.getTexture("assets/sprites/Walking_KG_2.png"),
                        100, 64, 7, 0));
    }

    @Override
    public void update(double dt) {
        System.out.println(1.0f / dt);
        frameCount++;
        if(frameCount > 1) {
            frameCount =0;
            ob1.sprite = spriteSheet.getSprite(spriteIndex);
            spriteIndex = spriteIndex >= 6 ? 0 : spriteIndex + 1;
            ob1.transform.position.x += 4;
            ob1.update(dt);
        }
        this.renderer.render();
    }
}
