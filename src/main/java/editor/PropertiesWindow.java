package editor;

import engine.GameObject;
import engine.Window;
import imgui.ImGui;
import scenes.WorldEditorScene;

public class PropertiesWindow {
    private GameObject activeGameObject = WorldEditorScene.getInstance().getActiveGameObject();

    public void imGui(){
        if (WorldEditorScene.getInstance().getActiveGameObject() != null) {
            ImGui.begin("inspector");
            activeGameObject.imGui();
            ImGui.end();
        }
    }
}
