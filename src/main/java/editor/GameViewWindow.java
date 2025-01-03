package editor;

import engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class GameViewWindow {

    public static void imGui(){
        ImGui.begin("Game Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
        ImVec2 windowSize = getLargestSizeForViewport();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x,windowPos.y);

        int textureID = Window.getFramebuffer().getTextureID();
        ImGui.image(textureID,windowSize.x,windowSize.y,0 , 1 , 1 ,0);

        ImGui.end();
    }
    private static ImVec2 getLargestSizeForViewport(){
        ImVec2 windowSize = getFittedWindowSize();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth / Window.getTargetAspectRatio();
        if (aspectHeight > windowSize.y){
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }
        return new ImVec2(aspectWidth,aspectHeight);
    }

    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize){
        ImVec2 windowSize = getFittedWindowSize();

        float viewPortX = windowSize.x / 2f - aspectSize.x / 2f + ImGui.getCursorPosX();
        float viewPortY = windowSize.y / 2f - aspectSize.y / 2f + ImGui.getCursorPosY();

        return new ImVec2(viewPortX , viewPortY);
    }

    private static ImVec2 getFittedWindowSize(){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        return windowSize;
    }
}
