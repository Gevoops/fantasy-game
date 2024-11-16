package engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import org.joml.Vector4f;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double x, y, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[3];
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
        if (button < get().mouseButtonPressed.length) {
            if (action == GLFW_PRESS) {
                get().mouseButtonPressed[button] = true;
            } else if (action == GLFW_RELEASE) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
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

    public static float getOrthoX() {
        float currentX = getX()/(float)windowWidth * 2f - 1f;

        Vector4f tmp = new Vector4f(currentX,0,0,1);
        tmp.mul(Window.getScene().camera.invScaleMatrix).mul(Window.getScene().camera.getInvProjectionMatrix()).mul(Window.getScene().camera.getInvViewMatrix());
        currentX = tmp.x;
        return currentX;
    }

    public static float getOrthoY() {
        float currentY = getY()/(float)windowHeight * 2f - 1f;
        currentY += 2 -  2 *Window.getHeight() /(float)windowHeight;
        currentY *= -1; //open gl flips images, so flip back

        Vector4f tmp = new Vector4f(0,currentY,0,1);
        tmp.mul(Window.getScene().camera.invScaleMatrix).mul(Window.getScene().camera.getInvProjectionMatrix()).mul(Window.getScene().camera.getInvViewMatrix());
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
}

