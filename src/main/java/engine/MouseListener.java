package engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static util.Settings.SCREEN_HEIGHT;
import static util.Settings.SCREEN_WIDTH;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double x, y, lastX, lastY;
    private boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;
    private final static int windowWidth = Window.getWidth();
    private final static int windowHeight = Window.getHeight();

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

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

    public static float getX(){
        return (float)get().x;
    }

    public static float getY(){
        return (float)get().y;
    }


    public static float getViewPortX(){
        float currentX = getX() - get().gameViewportPos.x ;
        currentX = (currentX/get().gameViewportSize.x) * SCREEN_WIDTH;
        return currentX;
    }
    public static float getViewPortY(){
        float currentY = getY() - get().gameViewportPos.y ;
        currentY = SCREEN_HEIGHT - (currentY/get().gameViewportSize.y) * SCREEN_HEIGHT;
        return  currentY;
    }

    public static float getOrthoX() {
        float currentX = getX() - get().gameViewportPos.x ;
        currentX = (currentX/get().gameViewportSize.x) * 2f - 1f;
        Vector4f tmp = new Vector4f(currentX,0,0,1);
        Camera camera = Window.getInstance().getScene().getCamera();
        tmp.mul(camera.getInvScaleMatrix()).mul(camera.getInvProjectionMatrix()).mul(camera.getInvViewMatrix());
        currentX = tmp.x;
        return currentX;
    }

    public static float getOrthoY() {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = (currentY/get().gameViewportSize.y) * 2f - 1f;
        currentY *= -1; //open gl flips images, so flip back

        Vector4f tmp = new Vector4f(0,currentY,0,1);
        Camera camera = Window.getInstance().getScene().getCamera();
        tmp.mul(camera.getInvScaleMatrix()).mul(camera.getInvProjectionMatrix()).mul(camera.getInvViewMatrix());
        currentY = tmp.y;
        return currentY;
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

    public void setGameViewportPos(Vector2f gameViewportPos) {
        this.gameViewportPos.set( gameViewportPos);
    }

    public void setGameViewportSize(Vector2f gameViewportSize) {
        this.gameViewportSize.set(gameViewportSize);
    }
}

