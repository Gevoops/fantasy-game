package engine;

import com.sun.marlin.Version;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import renderer.Framebuffer;
import renderer.PickingTexture;
import renderer.Renderer;
import renderer.Shader;
import scenes.GameScene;
import scenes.Scene;
import scenes.WorldEditorScene;
import util.AssetPool;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static util.Settings.SCREEN_HEIGHT;
import static util.Settings.SCREEN_WIDTH;

public class Window {

    private final int width;
    private final int height;
    private final String title;
    private long glfwWindow;

    public Vector4f bgColor = new Vector4f(0.4f,0.5f, 0.7f,1.0f) ;

    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;
    private static Window instance = null;
    private Scene currentScene ;
    private static ImGuiLayer gui;




    public void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = WorldEditorScene.getInstance();
                break;
            case 1:
                currentScene = GameScene.getInstance();
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

    public static Window getInstance() {
        if(Window.instance == null) {
            Window.instance = new Window();
        }
        return Window.instance;
    }

    public Scene getScene() {
        return currentScene;
    }

    public void run() {
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
        glfwSwapInterval(1); //sets screen update time to match screen actual refresh rate
        //show window
        glfwShowWindow(glfwWindow);

        //very important or something
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        gui = new ImGuiLayer(glfwWindow);
        gui.init();

        this.pickingTexture = new PickingTexture(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.framebuffer = new Framebuffer(SCREEN_WIDTH,SCREEN_HEIGHT);

        changeScene(0);


    }

    public void gameLoop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = 0;

        Shader defaultShader = AssetPool.getShader("src/main/resources/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("src/main/resources/shaders/pickingShader.glsl");
        while (!glfwWindowShouldClose(glfwWindow)){



            glfwPollEvents();


            currentScene.update(dt * 60);


            // render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();
            glViewport(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
            glClearColor(0.0f,0f,0f,0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Renderer.setCurrentShader(pickingShader);
            currentScene.getRenderer().render();
            pickingTexture.disableWriting();
            glEnable(GL_BLEND);


            // render actual game

            framebuffer.bind();
            glClearColor(bgColor.x, bgColor.y, bgColor.z, bgColor.w);
            glClear(GL_COLOR_BUFFER_BIT);
            Renderer.setCurrentShader(defaultShader);
            currentScene.render();

            framebuffer.unbind();
            glClearColor(0,0,0,0);
            glClear(GL_COLOR_BUFFER_BIT);
            gui.drawGui(currentScene);




            glfwSwapBuffers(glfwWindow);
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
            Time.timePassed += dt;
            MouseListener.endFrame();
        }
        currentScene.saveExit();
    }

    public static int getWidth() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(getInstance().glfwWindow, width,height);
        return width[0];
    }

    public static int getHeight() {
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetWindowSize(getInstance().glfwWindow, width,height);
        return height[0];
    }

    public long getWindowPtr(){
        return glfwWindow;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public PickingTexture getPickingTexture() {
        return pickingTexture;
    }

    public static float getTargetAspectRatio(){
        return 16f/9f;
    }

    public void setFramebuffer(Framebuffer framebuffer) {
        this.framebuffer = framebuffer;
    }

    public ImGuiLayer getGui() {
        return gui;
    }
}

