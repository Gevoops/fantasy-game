package renderer;

import engine.Window;
import org.joml.Vector4f;
import util.Time;
import java.util.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    //vertex
    // =====
    //pos              color
    //float, float,    float, float, float, float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize) {
        shader = new Shader("assets/shaders/default.glsl");
        shader.compile();
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        //4 = vertices per quad
        vertices = new float[VERTEX_SIZE * 4 * maxBatchSize];

        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void start() {
        //Generate and bind vertex array obj
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateindices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

    }

    private int[] generateindices() {
        int offsetArrayIndex;
        int offset;
        //6 indices per quad, 3 per triangle
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++) {
            offsetArrayIndex = 6 * i;
            offset = 4 * i;
            // 3, 2, 0, 0, 2, 1    7, 6, 4, 4, 6, 5 ...
            //Triangle 1
            elements[offsetArrayIndex] = offset + 3;
            elements[offsetArrayIndex + 1] = offset + 2;
            elements[offsetArrayIndex + 2] = offset;

            //triangle 2
            elements[offsetArrayIndex + 3] = offset;
            elements[offsetArrayIndex + 4] = offset + 2;
            elements[offsetArrayIndex + 5] = offset + 1;
        }

        return elements;
    }

    public void addSprite (SpriteRenderer spr) {
        // Get index and add RenderObject
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        //Add properties to local vertices array
        loadVertexProperties(index);

        if(numSprites >= this.maxBatchSize) {
            hasRoom = false;
        }
    }
    public void render() {
        //for now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        //use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
        shader.uploadMat4f("iso", Window.getScene().camera().getIsoMatrix());
        shader.uploadFloat("uTime", (float) Time.getTime());
        shader.uploadMat4f("scale", Window.getScene().camera().getScaleMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();

    }

    private void loadVertexProperties(int index) {
        SpriteRenderer sprite = this.sprites[index];

        // find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // Add vertices with the appropriate properties
        //going over the coordintes of a quad
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i ++) {
            if(i == 1) {
                yAdd = 0.0f;
            } else if(i == 2) {
                xAdd = 0.0f;
            } else if (i == 3){
                yAdd = 1.0f;
            }
            // load position
            vertices[offset] = sprite.transform.position.x
                    + xAdd * sprite.transform.scale.x;
            vertices[offset + 1] = sprite.transform.position.y
                    + yAdd * sprite.transform.scale.y;

            //load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }
    }
    public boolean hasRoom() {
        return this.hasRoom;
    }
}
