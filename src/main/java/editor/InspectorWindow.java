package editor;

import engine.GameObject;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import scenes.WorldEditorScene;

public class InspectorWindow {
    private GameObject activeGameObject;

    public void imGui() {
        ImGui.begin("inspector");
        if ((activeGameObject = WorldEditorScene.getInstance().getActiveGameObject()) != null) {
            activeGameObject.imGui();
        }
        ImGui.end();
    }
}
