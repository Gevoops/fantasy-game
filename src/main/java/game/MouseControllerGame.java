package game;

import components.Player;
import components.SpriteSheetList;
import engine.*;
import org.joml.Vector2d;
import scenes.WorldEditorScene;

import static org.lwjgl.glfw.GLFW.*;


public class MouseControllerGame implements MouseControllerStrategy {

    private GameObject player;
    private static String direction = "right";

    public MouseControllerGame(){
        player = WorldEditorScene.getInstance().getPlayer();


    }
    public void update(float dt) {
        Window.getInstance().getScene().getCamera().zoom(MouseListener.getScrollY());
        movePlayer(dt);
    }
    @Override
    public void handleRightClick() {

    }

    public void movePlayer(float dt) {

        double moveX = 0;
        double moveY = 0;
        if (WorldEditorScene.getInstance().getPlayer() != null) {
            if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
                moveX = -1;
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_W)){
                moveY = 1;
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_D)){
                moveX = 1;
            }
            if(KeyListener.isKeyPressed(GLFW_KEY_S)){
                moveY = -1;
            }
            if(moveX != 0 && moveY != 0){
                moveX = moveX / Math.sqrt(2);
                moveY = moveY / Math.sqrt(2);
            }

            Vector2d movement = new Vector2d(moveX, moveY);
            player.getComponent(Player.class).move(movement);
        }
    }
}