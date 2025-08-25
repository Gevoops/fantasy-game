package game;

import components.SpriteSheetList;
import engine.GameObject;
import engine.MouseControllerStrategy;
import engine.MouseListener;
import engine.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import scenes.WorldEditorScene;
import util.Tiles;


public class MouseControllerGame implements MouseControllerStrategy {
        private float animationCounter;
        private double clickX;
        private double clickY;
        private GameObject player;
        private double stepX;
        private double stepY;
        private int spriteIndex = 0, spriteIndex2 = 0;
        private boolean start;
        private boolean leftClicked = false;


        public MouseControllerGame(){
            player = WorldEditorScene.getInstance().getPlayer();
            start = false;
            if(player != null) {
                clickX = player.getX();
                clickY = player.getY();
            }
        }
        public void update(float dt) {
            if(MouseListener.mouseButtonDown(0)) {
                this.clickX = MouseListener.getOrthoX();
                this.clickY = MouseListener.getOrthoY();
                this.leftClicked = true;
            }
            Window.getInstance().getScene().getCamera().zoom(MouseListener.getScrollY());
            if (player!= null){
                movePlayer(dt);
                player.update(dt);
            }
            leftClicked = false;
        }

    @Override
    public void handleRightClick() {

    }

    public void movePlayer(float dt) {
            animationCounter += dt;
            if (WorldEditorScene.getInstance().getPlayer() != null) {

                SpriteSheetList spriteSheets = player.getComponent(SpriteSheetList.class);
                if (leftClicked) {
                    clickX = clickX - 50;
                    double distance =  Math.sqrt(Math.pow(player.getX() - clickX, 2)
                            + Math.pow(player.getY() - clickY, 2));

                    stepX = ( clickX - player.getX()) / distance;
                    stepY = ( clickY - player.getY()) / distance;

                    start = true;
                }

                if ((Math.abs(player.getX() - clickX) >= 2) || (Math.abs(player.getY() - clickY) >= 2) && start) {
                    if (animationCounter > 4) {
                        animationCounter = 0;
                        if (stepX > 0) {
                            player.setSprite(spriteSheets.get(1).getSprite(spriteIndex));
                            spriteIndex = spriteIndex == 6 ? 0 : spriteIndex + 1;
                        } else {
                            player.setSprite(spriteSheets.get(3).getSprite(spriteIndex));
                            spriteIndex = spriteIndex == 0 ? 6 : spriteIndex - 1;
                        }

                        spriteIndex2 = 0;
                    }
                    if (Math.abs(player.getX() - clickX) >= 2) {
                        player.moveX(stepX * 4 * dt);

                    }
                    if (Math.abs(player.getY() - clickY) >= 2) {
                        player.moveY(stepY * 4 * dt);

                    }
                } else {
                    if (animationCounter > 12) {
                        animationCounter = 0;
                        if (stepX >= 0) {
                            player.setSprite(spriteSheets.get(0).getSprite(spriteIndex2));
                            spriteIndex2 = spriteIndex2 == 3 ? 0 : spriteIndex2 + 1;
                        } else {
                            player.setSprite(spriteSheets.get(2).getSprite(spriteIndex2));
                            spriteIndex2 = spriteIndex2 == 0 ? 3 : spriteIndex2 - 1;
                        }

                        spriteIndex = 6;
                    }
                }
            }
        }
}