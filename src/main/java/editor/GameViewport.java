package editor;

import engine.Camera;
import engine.MouseListener;
import engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector4d;
import renderer.Framebuffer;

import static org.lwjgl.opengl.GL11.glViewport;
import static util.Settings.SCREEN_HEIGHT;
import static util.Settings.SCREEN_WIDTH;

public class GameViewport {
    private float contentWidth, contentHeight, contentX, contentY;
    private Framebuffer framebuffer;
    private ImVec2 viewPortSize;
    private Camera camera;

    public GameViewport(Camera camera){
        int width = SCREEN_WIDTH ;
        int height = SCREEN_HEIGHT ;
        this.framebuffer = new Framebuffer(width ,height);
        this.camera = camera;
        glViewport(0,0,width, height);

    }

    public void imGui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse );
        viewPortSize = getViewportSizePvt();
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



    public boolean wantCaptureMouse() {
        return  (MouseListener.getX() >= contentX / 2 && MouseListener.getX() <= contentX / 2 + contentWidth) &&
                (MouseListener.getY() >= contentY / 2 && MouseListener.getY() <= contentY / 2 + contentHeight);
    }

    public Framebuffer getFramebuffer() {
        return framebuffer;
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

    public double getViewPortX(double mouseX){
       return  mouseX - contentX / 2;

    }

    public double getViewPortY(double mouseY){
        double currentY = mouseY - contentY / 2;
        return SCREEN_HEIGHT - currentY;
    }


    public double getOrthoX(double mouseX) {
        double currentX = mouseX - contentX/2 ;
        currentX = (currentX/contentWidth) * 2 - 1;
        Vector4d tmp = new Vector4d(currentX,0,0,1);
        tmp.mul(camera.getInvScaleMatrix()).mul(camera.getInvProjectionMatrix()).mul(camera.getInvViewMatrix());
        currentX = tmp.x;
        return currentX;
    }


    public double getAbsPosX(double orthoPositionX){
        Vector4d tmp = new Vector4d(orthoPositionX, 0 ,0 ,1);
        tmp.mul(camera.getViewMatrix()).mul(camera.getProjectionMatrix()).mul(camera.getScaleMatrix());
        return  ((tmp.x + 1) * contentWidth / 2.0 ) + contentX/2;
    }

    public double getOrthoY(double mouseY) {
        double currentY = mouseY - contentY/2;
        currentY = (currentY/contentHeight) * 2f - 1f;
        currentY *= -1; //something is flipped

        Vector4d tmp = new Vector4d(0,currentY,0,1);
        tmp.mul(camera.getInvScaleMatrix()).mul(camera.getInvProjectionMatrix()).mul(camera.getInvViewMatrix());
        currentY = tmp.y;
        return currentY;
    }

    public double getAbsPosY(double orthoPositionY){
        Vector4d tmp = new Vector4d(0, orthoPositionY,0 ,1);
        tmp.mul(camera.getViewMatrix()).mul(camera.getProjectionMatrix()).mul(camera.getScaleMatrix());
        tmp.y *= -1;
        return  ((tmp.y + 1) * contentHeight / 2.0 ) + contentY/2;
    }

    private ImVec2 getViewportSizePvt(){
        ImVec2 size = new ImVec2();
        ImGui.getContentRegionAvail(size);
        return size;
    }
    public ImVec2 getViewportSize(){
        return viewPortSize;
    }



}
