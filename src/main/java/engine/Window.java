package engine;

import com.sun.marlin.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import renderer.DebugDraw;
import scenes.GameScene;
import scenes.Scene;
import scenes.WorldEditorScene;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private final int width;
    private final int height;
    private final String title;
    private long glfwWindow;
    public boolean leftClicked = false;
    public float clickX;
    public float clickY;

    public float r = 0.4f,g = 0.5f,b = 0.7f,a = 1.0f;

    private static Window window = null;

    private static Scene currentScene ;
    public static final double FPS = 80;


    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new WorldEditorScene();
                break;
            case 1:
                currentScene = new GameScene();
                break;
            default:
                assert false: "unknown scene" + newScene;
                break;
        }

        currentScene.init();
        currentScene.start();
    }

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "desktop rpg";
    }

    public static Window getWindow() {
        if(Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }

    public static Scene getScene() {
        return currentScene;
    }

    public void run() {
        System.out.println("hello LWJGL " + Version.getVersion() + "!");
        init();
        gameLoop();

        // free memory and error callback
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        // setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize glfw
        if(!glfwInit()) {
            throw new IllegalStateException("unable to init GLFW");
        }

        // configure glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // create window
        glfwWindow = glfwCreateWindow(this.width, this.height , this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        //make the openGL context current
        glfwMakeContextCurrent(glfwWindow);
        //enable v-sync
        glfwSwapInterval(0); //sets screen update time to match screen actual refresh rate
        //show window
        glfwShowWindow(glfwWindow);

        //very important or something
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0);

    }

    public void gameLoop() {
        float beginTime = (float) Time.getTime();
        float endTime;
        float dt = 0;
        float accumulateTime = 0;




        while (!glfwWindowShouldClose(glfwWindow)){

            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);


            if(MouseListener.mouseButtonDown(0)) {
                this.clickX = MouseListener.getOrthoX();
                this.clickY = MouseListener.getOrthoY();
                this.leftClicked = true;
            }


            currentScene.update(dt * 60);

            if(accumulateTime > 1/FPS){
                // System.out.println(1/accumulateTime);
                accumulateTime -= 1/FPS;
                glfwSwapBuffers(glfwWindow);
            }
            endTime = (float) Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
            Time.timePassed += dt;
            accumulateTime += dt;
            MouseListener.endFrame();
            leftClicked = false;
        }
        currentScene.saveExit();
    }

    public static int getWidth() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(getWindow().glfwWindow, width,height);
        return width[0];
    }

    public static int getHeight() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(getWindow().glfwWindow, width,height);
        return height[0];
    }

    public long getWindowPtr(){
        return glfwWindow;
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }
}

