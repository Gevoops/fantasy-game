package engine;

import editor.GameViewWindow;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import org.lwjgl.glfw.GLFW;
import scenes.Scene;

public class ImGuiLayer {
    private boolean showText = false;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long windowPtr;

    public ImGuiLayer(long windowPtr){
        this.windowPtr = windowPtr;
    }
    public void drawGui(Scene currentScene){
        beginDraw();
        setupDockSpace();
        currentScene.sceneImGui();
        GameViewWindow.imGui();
        ImGui.end();
        render();
    }

    public void initImGui(){

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
        //io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        imGuiGlfw.init(windowPtr,true);


        // fonts
       final ImFontAtlas fontAtlas = io.getFonts();
       final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, need destroy

       fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
       fontConfig.setPixelSnapH(true);
       fontAtlas.addFontFromFileTTF("src/main/resources/fonts/segoeui.ttf",24,fontConfig);
       fontConfig.destroy();

       imGuiGl3.init( "#version 330"); //glsl version, must be last
    }

    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }

    public ImGuiImplGlfw getImGuiGlfw() {
        return imGuiGlfw;
    }

    public ImGuiImplGl3 getImGuiGl3() {
        return imGuiGl3;
    }

    public void beginDraw(){
        this.getImGuiGlfw().newFrame();
        ImGui.newFrame();
    }

    public void render(){
        ImGui.render();
        this.getImGuiGl3().renderDrawData(ImGui.getDrawData());

        if(ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)){
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(windowPtr);
        }
    }

    private void setupDockSpace(){
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;
        ImGui.setNextWindowPos(0,0, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(),Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding,0);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize,0);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                       ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                       ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace Demo", new ImBoolean(true),windowFlags );
        ImGui.popStyleVar(2);
        // DockSpace

        ImGui.dockSpace(ImGui.getID("Dockspace"));

    }
}
