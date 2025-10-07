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
        GameObject o = scene.getActiveGameObject();
        ImGui.begin("inspector");
        if (o == null) { ImGui.end(); return;}

        o.imGui();
        ImGui.text("active object: " + o.getID() );
        if (ImGui.button("delete") || KeyListener.isKeyPressed(GLFW_KEY_DELETE)) {
            scene.deleteGameObj(o);
            scene.setLiftedObject(null);
            scene.setActiveGameObject(null);
        }


        ImFloat height = new ImFloat(o.getHeight());
        if(ImGui.inputFloat(" height", height)){
            o.setHeight(height.get());
        }
        ImGui.end();
    }
}
