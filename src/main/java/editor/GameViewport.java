package editor;

import engine.Camera;
import engine.MouseListener;
import engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector4d;
import org.joml.Vector4f;
import renderer.Framebuffer;

import static org.lwjgl.opengl.GL11.glViewport;
import static util.Settings.SCREEN_HEIGHT;
import static util.Settings.SCREEN_WIDTH;

public class GameViewport {
    private float contentWidth, contentHeight, contentX, contentY;
    private Framebuffer framebuffer;

    public GameViewport(){
        int width = SCREEN_WIDTH ;
        int height = SCREEN_HEIGHT ;
        this.framebuffer = new Framebuffer(width ,height);
        glViewport(0,0,width, height);

    }

    public void imGui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse );
        ImVec2 frameSize = new ImVec2();
        frameSize.x = SCREEN_WIDTH;
        frameSize.y = SCREEN_HEIGHT;
        contentWidth = frameSize.x;
        contentHeight = frameSize.y;

        ImVec2 screenPos = new ImVec2();
        ImVec2 windowPos = new ImVec2();
        ImGui.getCursorScreenPos(screenPos);
        ImGui.getWindowPos(windowPos);

        contentX = screenPos.x + windowPos.x;
        contentY = screenPos.y + windowPos.y - 24;

        //TODO fix this disgusting 24 ^. when viewports are enabled some 24 pixel offset is happening and fucking up the screen pos



        int textureID = framebuffer.getTextureID();
        ImGui.image(textureID,contentWidth,contentHeight,0 , 1 , 1 ,0);

        ImGui.end();
    }

    private static ImVec2 getViewportWindowSize(){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        return windowSize;
    }

    public boolean wantCaptureMouse() {
        return  (MouseListener.getX() >= contentX / 2 && MouseListener.getX() <= contentX / 2 + contentWidth) &&
                (MouseListener.getY() >= contentY / 2 && MouseListener.getY() <= contentY / 2 + contentHeight);
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

    public float getContentX() {
        return contentX;
    }

    public float getContentY() {
        return contentY;
    }

    public float getViewPortX(float mouseX){
       return  mouseX - contentX / 2;

    }

    public float getViewPortY(float mouseY){
        float currentY = mouseY - contentY / 2;
        return SCREEN_HEIGHT - currentY;
    }


    public double getOrthoX(double mouseX) {
        double currentX = mouseX - contentX/2 ;
        currentX = (currentX/contentWidth) * 2 - 1;
        Vector4d tmp = new Vector4d(currentX,0,0,1);
        Camera camera = Window.getInstance().getScene().getCamera();
        tmp.mul(camera.getInvScaleMatrix()).mul(camera.getInvProjectionMatrix()).mul(camera.getInvViewMatrix());
        currentX = tmp.x;
        return currentX;
    }

    public double getOrthoY(double mouseY) {
        double currentY = mouseY - contentY/2;
        currentY = (currentY/contentHeight) * 2f - 1f;
        currentY *= -1; //something is flipped

        Vector4d tmp = new Vector4d(0,currentY,0,1);
        Camera camera = Window.getInstance().getScene().getCamera();
        tmp.mul(camera.getInvScaleMatrix()).mul(camera.getInvProjectionMatrix()).mul(camera.getInvViewMatrix());
        currentY = tmp.y;
        return currentY;
    }

}
