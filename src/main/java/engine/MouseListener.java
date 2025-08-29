package engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import imgui.ImGui;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double x, y, lastX, lastY;
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;
    private final static int windowWidth = Window.getWidth();
    private final static int windowHeight = Window.getHeight();


    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.x = 0.0;
        this.y = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double x, double y) {
        get().lastX = get().x;
        get().lastY = get().y;
        get().x = x;
        get().y = y;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mod) {
        if (button < get().mouseButtonPressed.length && (!ImGui.getIO().getWantCaptureMouse() || Window.getInstance().getScene().getGameViewport().wantCaptureMouse())) {
            if (action == GLFW_PRESS) {
                get().mouseButtonPressed[button] = true;
            }
            if(button == 1){
                Window.getInstance().getScene().getMouseController().handleRightClick();
            }
        }
        if (action == GLFW_RELEASE) {
            get().mouseButtonPressed[button] = false;
            get().isDragging = false;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        if(!ImGui.getIO().getWantCaptureMouse() || Window.getInstance().getScene().getGameViewport().wantCaptureMouse()){
            get().scrollX = xOffset;
            get().scrollY = yOffset;
        }

    }
    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().x;
        get().lastY = get().y;
    }

    public static double getX(){
        return get().x;
    }

    public static double getY(){
        return get().y;
    }


    public static float getViewPortX(){
        return Window.getInstance().getCurrentScene().getGameViewport().getViewPortX((float)get().x);
    }
    public static float getViewPortY(){
        return Window.getInstance().getCurrentScene().getGameViewport().getViewPortY((float)get().y);
    }

    public static double getOrthoX() {
        return Window.getInstance().getCurrentScene().getGameViewport().getOrthoX(get().x);
    }

    public static double getOrthoY() {
        return Window.getInstance().getCurrentScene().getGameViewport().getOrthoY(get().y);
    }

    public static float getDx() {
        return (float) (get().x- get().lastX);
    }

    public static float getDy() {
        return (float) (get().y- get().lastY);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean getIsDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if(button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        }
        else { return false;}
    }
}

