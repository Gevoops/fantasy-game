package components;

import engine.Camera;
import engine.Window;
import org.joml.Vector2d;
import scenes.Scene;

public class Player extends Component{
    boolean moveDirection = false;
    boolean wantsToMove = false;
    double speed = 4;
    Vector2d movement = new Vector2d();
    private float animationCounter = 0;
    private int spriteIndex = 0, spriteIndex2 = 0;
    private boolean animate;

    public Player(){
        super();
    }

    @Override
    public void start(){
        Scene scene = Window.getInstance().getCurrentScene();
        Camera camera = scene.getCamera();
        scene.setPlayer(gameObject);
        camera.setViewPoint(new Vector2d(this.gameObject.getX() - camera.getProjectionSize().x  / 2,
                this.gameObject.getY() - camera.getProjectionSize().y / 2));
        gameObject.setDynamic(true);

    }

    public void update(float dt){
        if (animate){
            this.gameObject.moveX(movement.x * speed * dt);
            this.gameObject.moveY(movement.y * speed * dt);
            animate(dt);
        }
        wantsToMove = false;
    }

    public void move(Vector2d movement){
        if (movement.x != 0 || movement.y != 0) {
            wantsToMove = true;
            moveDirection = movement.x != 0 ? movement.x > 0 : moveDirection;
        }
        this.movement = movement;
        Window.getInstance().getCurrentScene().getCamera().setViewPoint(
                new Vector2d(this.gameObject.getTransform().position.x - 600,
                        this.gameObject.getTransform().position.y - 700));
    }

    public void animate(float dt){
        animationCounter += dt;
        SpriteSheetList spriteSheets = this.gameObject.getComponent(SpriteSheetList.class);
        if (wantsToMove && animationCounter > 4) {
            animationCounter = 0;
            if (moveDirection) {
                this.gameObject.setSprite(spriteSheets.get(1).getSprite(spriteIndex));
                spriteIndex = spriteIndex == 6 ? 0 : spriteIndex + 1;
            } else {
                this.gameObject.setSprite(spriteSheets.get(3).getSprite(spriteIndex));
                spriteIndex = spriteIndex == 0 ? 6 : spriteIndex - 1;
            }
            spriteIndex2 = 0;
        }
        if (animationCounter > 12 & !wantsToMove) {
            animationCounter = 0;
            if (moveDirection) {
                this.gameObject.setSprite(spriteSheets.get(0).getSprite(spriteIndex2));
                spriteIndex2 = spriteIndex2 == 3 ? 0 : spriteIndex2 + 1;
            } else {
                this.gameObject.setSprite(spriteSheets.get(2).getSprite(spriteIndex2));
                spriteIndex2 = spriteIndex2 == 0 ? 3 : spriteIndex2 - 1;
            }
            spriteIndex = 6;
        }
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public Vector2d getMovement() {
        return movement;
    }

    public void setMovement(Vector2d movement) {
        this.movement = movement;
    }
}
