package editor;

import engine.GameObject;
import imgui.ImGui;
import scenes.WorldEditorScene;

public class InspectorWindow {
    private WorldEditorScene scene;


    public InspectorWindow(WorldEditorScene scene) {
        this.scene = scene;
    }
    public void imGui() {
        GameObject o = scene.getActiveGameObject();
        ImGui.begin("inspector");
        if (o != null) {
            o.imGui();
            ImGui.text("active object: " + o.getID() );
        }
        if (ImGui.button("delete")) {
            scene.deleteGameObj(o);
            scene.setActiveGameObject(null);
            scene.setLiftedObject(null);
        }
        ImGui.sameLine();
        ImGui.end();
    }
}
