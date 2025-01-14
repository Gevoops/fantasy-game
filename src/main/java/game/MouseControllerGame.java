package game;

import components.SpriteSheetList;
import engine.GameObject;
import engine.MouseControllerStrategy;
import engine.MouseListener;
import engine.Window;
import scenes.WorldEditorScene;


public class MouseControllerGame implements MouseControllerStrategy {
        private float animationCounter;
        private float clickX;
        private float clickY;
        private GameObject player;
        private float stepX;
        private float stepY;
        private int spriteIndex = 0, spriteIndex2 = 0;
        private boolean start;


        public MouseControllerGame(){
            player = WorldEditorScene.getInstance().getPlayer();
            start = false;
            clickX = player.getX();
            clickY = player.getY();
        }
        public void update(float dt) {
            Window.getScene().getCamera().zoom(MouseListener.getScrollY());
            movePlayer(dt);
            if (player!= null){
                player.update(dt);
            }
        }

        public void movePlayer(float dt) {
            animationCounter += dt;
            if (WorldEditorScene.getInstance().getPlayer() != null) {

                SpriteSheetList spriteSheets = player.getComponent(SpriteSheetList.class);
                if (Window.getWindow().leftClicked) {
                    clickX = Window.getWindow().clickX - 50;
                    clickY = Window.getWindow().clickY;
                    float distance = (float) Math.sqrt(Math.pow(player.getX() - clickX, 2)
                            + Math.pow(player.getY() - clickY, 2));

                    stepX = (clickX - player.getX()) / distance;
                    stepY = (clickY - player.getY()) / distance;

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