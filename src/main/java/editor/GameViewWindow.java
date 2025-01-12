package editor;

import engine.MouseListener;
import engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import renderer.Framebuffer;

import static org.lwjgl.opengl.GL11.glViewport;
import static util.Settings.SCREEN_HEIGHT;
import static util.Settings.SCREEN_WIDTH;

public class GameViewWindow {
    private float contentWidth, contentHeight, contentX, contentY;
    private Framebuffer framebuffer;

    public GameViewWindow(){
        int width = SCREEN_WIDTH ;
        int height = SCREEN_HEIGHT ;
        this.framebuffer = new Framebuffer(width   ,height );
        glViewport(0,0,width, height);

    }

    public void imGui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse );
        ImVec2 frameSize1 = getLargestSizeForViewport();
        frameSize1.x = 2560;
        frameSize1.y = 1440;
        contentWidth = frameSize1.x;
        contentHeight = frameSize1.y;

        ImVec2 screenPos = new ImVec2();
        ImGui.getCursorScreenPos(screenPos);

        contentX = screenPos.x;
        contentY = screenPos.y;


        int textureID = framebuffer.getTextureID();
        ImGui.image(textureID,contentWidth,contentHeight,0 , 1 , 1 ,0);

        MouseListener.get().setGameViewportPos(new Vector2f(screenPos.x,screenPos.y));
        MouseListener.get().setGameViewportSize(new Vector2f(contentWidth,contentHeight));

        ImGui.end();
    }
    private static ImVec2 getLargestSizeForViewport(){
        ImVec2 windowSize = getViewportWindowSize();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y){
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }
        return new ImVec2(aspectWidth,aspectHeight);
    }

    private static ImVec2 getViewportWindowSize(){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        return windowSize;
    }

    public  boolean wantCaptureMouse() {
        return  (MouseListener.getX() >= contentX && MouseListener.getX() <= contentX + contentWidth) &&
                (MouseListener.getY() >= contentY && MouseListener.getY() <= contentY + contentHeight);
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
    }

    public float getContentWidth() {
        return contentWidth;
    }

    public float getContentHeight() {
        return contentHeight;
    }
}
