package engine;

import com.sun.marlin.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
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


    public float r = 0.4f,g = 0.5f,b = 0.7f,a = 1.0f;

    private static Window window = null;

    private static Scene currentScene ;

    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new WorldEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new GameScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false: "unknown scene" + newScene;
                break;
        }

    }

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "desktop rpg";
    }

    public static Window get() {
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
        loop();

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
        glfwSwapInterval(1); //sets screen update time to match screen actual refresh rate
        //show window
        glfwShowWindow(glfwWindow);

        //very important or something
        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop() {
        double beginTime = Time.getTime();
        double endTime;
        double dt = 0;

        while (!glfwWindowShouldClose(glfwWindow)){
            //poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            currentScene.update(dt);
           glfwSwapBuffers(glfwWindow);
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
            Time.timePassed += dt;
        }
    }
}

