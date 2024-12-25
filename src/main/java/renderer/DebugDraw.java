package renderer;

import engine.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.CallbackI;
import util.AssetPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {
    private static int MAX_LINES = 1000;

    private static List<Line2D> lines = new ArrayList<>();
    // 6 floats per vertex (x,y,z,r,g,b) , 2 vertices per line.

    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("src/main/resources/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    public static void start(){
        started = true;
        //Generate the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create the vbo and buffer some memory
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER, Float.BYTES * vertexArray.length, GL_DYNAMIC_DRAW);

        //enable the vertex array attributes
        glVertexAttribPointer(0,3,GL_FLOAT,false,6 * Float.BYTES,0);
        //glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,3,GL_FLOAT,false,6 * Float.BYTES, 3*Float.BYTES);
       // glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER,0);


    }
    public static void beginFrame(){
        if(!started){
            start();
        }
        glLineWidth(2f * Window.getScene().getCamera().getZoom());
        //Remove expired lines
        for (int i = 0; i < lines.size(); i++){
            if(lines.get(i).beginFrame() <= 0){
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw(){
        if(lines.size() == 0) {return;}

        int index = 0;
        for (Line2D line : lines){
            for (int i = 0; i < 2; i++){
                Vector2f position = i == 0 ? line.getStart() : line.getEnd();
                Vector3f color = line.getColor();

                // load position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f;

                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;
                index += 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray,0, lines.size() * 6 * 2));



        //use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView",Window.getScene().getCamera().getViewMatrix());
        shader.uploadMat4f("scale",Window.getScene().getCamera().getScaleMatrix());

        // bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // draw batch
        glDrawArrays(GL_LINES,0,lines.size()  * 2);


        // disable location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);


        glBindBuffer(GL_ARRAY_BUFFER,0);


        shader.detach();
    }

    // =============================================================
    // Add Line2D methods
    // =============================================================

    public static void addLine2D(Vector2f start, Vector2f end){
        addLine2D(start,end,new Vector3f(0,1,0),1);
    }

    public static void addLine2D(Vector2f start, Vector2f end,Vector3f color){
        addLine2D(start,end,color,1);
    }

    public static void addLine2D(Vector2f start, Vector2f end,Vector3f color,  int lifetime){
        if (lines.size() >= MAX_LINES){
            return;
        }
        DebugDraw.lines.add(new Line2D(start,end,color,lifetime));
    }
}
