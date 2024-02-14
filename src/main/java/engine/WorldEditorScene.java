package engine;


import Game.RenderObject;
import org.joml.Matrix2f;
import org.joml.Vector2f;
import renderer.Sprite;
import renderer.SpriteSheet;
import renderer.Texture;
import util.AssetPool;


public class WorldEditorScene extends Scene {
    public static Matrix2f isoMatrix = new Matrix2f(0.5f, -0.25f,
                                                     1f, 0.49f );

    //m11 = space between rows

    //for thick tiles
    //0.4f, -0.15f,
    //0.9f, 0.34f );
    //
    int frameCount = 0;
    int spriteIndex = 0;
    int spriteIndex2 = 0;
    float clickX = 0;
    float clickY = 0;
    float stepX =0;
    float stepY = 0;
    RenderObject ob1;
    boolean start = true;

    public WorldEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(0,0));


       // this.spriteSheet3 = AssetPool.getSpriteSheet("assets/sprites/Walking_KG_2_left.png");




        int xOffset = -1950;
        int yOffset = -200;

        float sizeX = 128.0f;
        float sizeY = 64f;
        Texture tex = new Texture("assets/sprites/ground1.png");
        Sprite sp = new Sprite(AssetPool.getSpriteSheet("assets/sprites/forest_sheet.png").getSprite(0));




        for (int y = 40; y > 0; y--) {
            for (int x = 0; x < 40; x++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                RenderObject ob = new RenderObject("obj" + xPos + "," + yPos,
                        new Sprite(tex),
                        new Transform(new Vector2f(xPos, yPos).mul(isoMatrix), new Vector2f(sizeX, sizeY)),0);

                this.addGameObjectToScene(ob);


            }
        }

        this.ob1 = new RenderObject("obj" + ",",AssetPool.getSpriteSheet("assets/sprites/Idle_KG_2.png").getSprite(0),
                new Transform(new Vector2f(300, 300), new Vector2f(100, 64)),1);

        ob1.addSpriteSheet(AssetPool.getSpriteSheet( "assets/sprites/Idle_KG_2.png"));
        ob1.addSpriteSheet(AssetPool.getSpriteSheet("assets/sprites/Walking_KG_2.png"));
        ob1.addSpriteSheet(AssetPool.getSpriteSheet("assets/sprites/Idle_KG_2_left.png"));
        ob1.addSpriteSheet(AssetPool.getSpriteSheet("assets/sprites/Walking_KG_2_left.png"));
        this.addGameObjectToScene(this.ob1);


    }



    @Override
    public void update(double dt) {
        //System.out.println(1.0f / dt);
        frameCount++;
        if(Window.get().leftClicked) {
            clickX = Window.get().clickX - 50;
            clickY = Window.get().clickY;
            float distance = (float) Math.sqrt(Math.pow(ob1.transform.position.x - clickX,2)
            + Math.pow(ob1.transform.position.y - clickY,2));

            stepX = (clickX - ob1.transform.position.x) / distance;
            stepY = (clickY - ob1.transform.position.y) / distance;
            Window.get().leftClicked = false;
            start = false;
        }

        if(((Math.abs(ob1.transform.position.x - clickX) >= 2) || (Math.abs(ob1.transform.position.y - clickY) >= 2)) && !start) {
            if(frameCount > 4) {
                frameCount =0;
                if(stepX >= 0) {
                    ob1.setSprite(ob1.spriteSheets.get(1).getSprite(spriteIndex));
                    spriteIndex = spriteIndex == 6 ? 0 : spriteIndex + 1;
                } else {
                    ob1.setSprite(ob1.spriteSheets.get(3).getSprite(spriteIndex));
                    spriteIndex = spriteIndex == 0 ? 6 : spriteIndex - 1;
                }


                spriteIndex2 = 0;
            }
            if(Math.abs(ob1.transform.position.x - clickX) >= 2) {
                ob1.transform.position.x += stepX * 4;
                System.out.println(stepX);
            }
            if(Math.abs(ob1.transform.position.y - clickY ) >= 2) {
                ob1.transform.position.y += stepY * 4;
                System.out.println(stepY);
            }
        } else {
            if(frameCount > 9) {
                frameCount = 0;
                if(stepX >= 0) {
                    ob1.setSprite(ob1.spriteSheets.get(0).getSprite(spriteIndex2));
                    spriteIndex2 = spriteIndex2 == 3 ? 0 : spriteIndex2 + 1;
                } else {
                    ob1.setSprite(ob1.spriteSheets.get(2).getSprite(spriteIndex2));
                    spriteIndex2 = spriteIndex2 == 0 ? 3 : spriteIndex2 - 1;
                }

                spriteIndex = 6;
            }
        }

        ob1.update(dt);
        this.renderer.render();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/sprites/Walking_KG_2_left.png",
                new SpriteSheet(AssetPool.getTexture("assets/sprites/Walking_KG_2_left.png"),
                        100, 64, 7, 0));


        AssetPool.addSpriteSheet("assets/sprites/Idle_KG_2.png",
                new SpriteSheet(AssetPool.getTexture("assets/sprites/Idle_KG_2.png"),
                        100, 64, 4, 0));

        AssetPool.addSpriteSheet("assets/sprites/Idle_KG_2_left.png",
                new SpriteSheet(AssetPool.getTexture("assets/sprites/Idle_KG_2_left.png"),
                        100, 64, 4, 0));

        AssetPool.addSpriteSheet("assets/sprites/Walking_KG_2.png",
                new SpriteSheet(AssetPool.getTexture("assets/sprites/Walking_KG_2.png"),
                        100, 64, 7, 0));

        AssetPool.addSpriteSheet("assets/sprites/forest_sheet.png",
                new SpriteSheet(AssetPool.getTexture("assets/sprites/forest_sheet.png"),
                        128, 64, 18, 0));

    }
}
