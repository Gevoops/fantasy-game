package engine;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiFreeTypeBuilderFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;

public class ImGuiLayer {
    private boolean showText = false;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private long windowPtr;

    public ImGuiLayer(long windowPtr){
        this.windowPtr = windowPtr;
    }
    public void drawGui(Scene currentScene){
        beginDraw(); // must be called at the beging of this method
        ImGui.begin("hello, world");
        currentScene.sceneImgui();
        if(ImGui.button("hello world")){
            showText = !showText;
        }
        if(showText) {
            ImGui.text("hello world");
        }
        ImGui.end();
        endDraw(); // must be called at the end of this method
    }

    public void initImGui(){

        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
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

    public void endDraw(){
        ImGui.render();
        this.getImGuiGl3().renderDrawData(ImGui.getDrawData());

        if(ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)){
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(windowPtr);
        }
    }
}
