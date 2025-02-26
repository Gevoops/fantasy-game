package engine;

import imgui.ImGui;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance; //singleton
    private boolean[] keyPressed = new boolean[350];

    private KeyListener() {


    }

    public static KeyListener get() {
        if(KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }
    public static void keyCallback(long window, int key, int scanCode, int action, int mods) {
        if (!ImGui.getIO().getWantCaptureKeyboard()){
            if(action == GLFW_PRESS) {
                get().keyPressed[key] = true;
            }
        }
        if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {

            return get().keyPressed[keyCode];
    }

 }
