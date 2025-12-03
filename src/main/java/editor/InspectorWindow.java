package editor;

import engine.GameObject;
import engine.KeyListener;
import imgui.ImGui;
import imgui.type.ImFloat;
import scenes.WorldEditorScene;

import static org.lwjgl.glfw.GLFW.*;

public class InspectorWindow {
    private WorldEditorScene scene;


    public InspectorWindow(WorldEditorScene scene) {
        this.scene = scene;
    }
    public void imGui() {
        GameObject activeGo = scene.getActiveGameObject();
        ImGui.begin("inspector");
        if (activeGo == null) { ImGui.end(); return;}

        activeGo.imGui();
        ImGui.text("active object: " + activeGo.getID() );
        ImGui.text("type: " + activeGo.getType());
        if (ImGui.button("delete") || KeyListener.isKeyPressed(GLFW_KEY_DELETE)) {
            activeGo.setDeathMark(true);
            scene.setLiftedObject(null);
            scene.setActiveGameObject(null);
        }


        ImFloat height = new ImFloat(activeGo.getHeight());
        if(ImGui.inputFloat(" height", height)){
            activeGo.setHeight(height.get());
        }
        ImGui.end();
    }
}
