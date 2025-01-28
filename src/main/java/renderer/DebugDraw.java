package renderer;

import engine.Camera;
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
    private static int MAX_LINES = 1300;

    private static List<Line2D> underLines = new ArrayList<>();
    private static List<Line2D> overLines = new ArrayList<>();
    private static List<Line2D> lines = new ArrayList<>();
    // 6 floats per vertex (x,y,z,r,g,b) , 2 vertices per line.

    private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("src/main/resources/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    private static final Vector3f DEFAULT_COLOR = new Vector3f(0,1,0);

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
        //Remove expired lines
        for (int i = 0; i < overLines.size(); i++){
            if(overLines.get(i).beginFrame() <= 0){
                overLines.remove(i);
                i--;
            }
        }
        for (int i = 0; i < underLines.size(); i++){
            if(underLines.get(i).beginFrame() <= 0){
                underLines.remove(i);
                i--;
            }
        }
    }

    public static void drawOver(){
        lines = overLines;
        draw();
    }
    public static void drawUnder(){
        lines = underLines;
        draw();
    }


    public static void draw(){
        if(!started){
            start();
        }
        glLineWidth(2f * Window.getInstance().getScene().getCamera().getZoom());
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
        Camera camera = Window.getInstance().getScene().getCamera();

        shader.use();
        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        shader.uploadMat4f("uView",camera.getViewMatrix());
        shader.uploadMat4f("scale",camera.getScaleMatrix());

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
        addLine2D(start,end,DEFAULT_COLOR,1,true);
    }

    public static void addLine2D(Vector2f start, Vector2f end,Vector3f color){
        addLine2D(start,end,color,1,true);
    }

    public static void addLine2D(Vector2f start, Vector2f end,Vector3f color,  int lifetime, boolean over){
        if (overLines.size() + underLines.size() >= MAX_LINES){
            return;
        }
        if (over){
            DebugDraw.overLines.add(new Line2D(start,end,color,lifetime));
        } else {
            DebugDraw.underLines.add(new Line2D(start,end,color,lifetime));
        }

    }

    // =============================================================
    // Box2D methods
    // =============================================================
    public static void addBox2D(Vector2f center, Vector2f dimensions, float angle, Vector3f color,  int lifetime, boolean over){
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x,min.y),new Vector2f(min.x,max.y),
                new Vector2f(max.x,max.y),new Vector2f(max.x,min.y)
        };

        if (angle != 0){
            for (Vector2f vert : vertices){
                rotate(vert,angle,center);
            }
        }

        addLine2D(vertices[0],vertices[1],color,lifetime,over);
        addLine2D(vertices[1],vertices[2],color,lifetime,over);
        addLine2D(vertices[2],vertices[3],color,lifetime,over);
        addLine2D(vertices[3],vertices[0],color,lifetime,over);

    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float angle, Vector3f color) {
        addBox2D(center,dimensions,angle,color,1,true);
    }
    public static void addBox2D(Vector2f center, Vector2f dimensions, float angle) {
        addBox2D(center,dimensions,angle,new Vector3f(0,1,0),1,true);
    }
    public static void addBox2D(Float centerX,Float centerY, Float width, Float height, float angle, Vector3f color,  int lifetime) {
        addBox2D(new Vector2f(centerX,centerY),new Vector2f(width,height),angle,color,lifetime,true);
    }
    public static void addBox2D(Float centerX,Float centerY, Float width, Float height, float angle,  int lifetime) {
        addBox2D(new Vector2f(centerX,centerY),new Vector2f(width,height),angle,DEFAULT_COLOR,lifetime,true);
    }public static void addBox2D(Float centerX,Float centerY, Float width, Float height, float angle) {
        addBox2D(new Vector2f(centerX,centerY),new Vector2f(width,height),angle,DEFAULT_COLOR,1,true);
    }


    // =============================================================
    // Circle2D methods
    // =============================================================
    public static void addCircle2D(Vector2f center,  float radius, Vector3f color,  int lifetime, boolean over){
        Vector2f[] points = new Vector2f[(int)(radius / 3)];
        float increment = 360f / points.length;
        float currentAngle = 0;

        for (int i = 0; i < points.length; i ++) {
            Vector2f tmp = new Vector2f(radius,0);
            rotate(tmp,currentAngle,new Vector2f());
            points[i] = new Vector2f(tmp.add(center));

            if (i > 0) {
                addLine2D(points[i - 1],points[i],color,lifetime,over);
            }
            currentAngle += increment;
        }
        addLine2D(points[0],points[points.length -1],color,lifetime,over);

    }


    // =============================================================
    // helper methods
    // =============================================================

    private static void rotate(Vector2f vec, float angleDeg, Vector2f origin) {
        vec.sub(origin);
        Vector3f vec1 = new Vector3f(vec.x,vec.y,0);
        vec1.rotateAxis((float) Math.toRadians(angleDeg),0f,0f,1f);
        vec.x = vec1.x;
        vec.y = vec1.y;
        vec.add(origin);
    }
}
