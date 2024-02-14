package renderer;

import Game.RenderObject;
import engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;
import util.Time;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    //vertex
    // =====
    //pos              color                       texture coords  tex id
    //float, float,    float, float, float, float  float, float   float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    //offsets
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private RenderObject[] RenderObjects;
    private int renderObNum;
    private boolean hasRoom;
    private float[] vertices;
    private int[] texSlots = {0,1,2,3,4,5,6,7};


    private List<Texture> textures;
    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;
    private int zIndex;

    public RenderBatch(int maxBatchSize, int zIndex) {
        this.zIndex = zIndex;
        shader = AssetPool.getShader("assets/shaders/default.glsl");
        this.RenderObjects = new RenderObject[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        //4 = vertices per quad
        vertices = new float[VERTEX_SIZE * 4 * maxBatchSize];

        this.renderObNum = 0;
        this.hasRoom = true;
        this.textures = new ArrayList<>();
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
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

    }

    private int[] generateIndices() {
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

    public void addRenderOb(RenderObject ob) {
        // Get index and add RenderObject
        int index = this.renderObNum;
        this.RenderObjects[index] = ob;
        this.renderObNum++;
        if(!textures.contains(ob.sprite.getTexture())) {
            textures.add(ob.sprite.getTexture());
        }
        for(SpriteSheet s : ob.spriteSheets) {
            if(!textures.contains(s.getTexture())) {
                textures.add(s.getTexture());
            }
        }
        //Add properties to local vertices array
        loadVertexProperties(index);

        if(renderObNum >= this.maxBatchSize) {
            hasRoom = false;
        }
    }

    private void loadVertexProperties(int index) {
        RenderObject ob = this.RenderObjects[index];
        Sprite sprite = ob.sprite;


        // find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texId = 0;
        //textures slot 0 is reserved for color.
        if(sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if(textures.get(i) == sprite.getTexture()) {
                    texId = i + 1;
                    break;
                }
            }
        }


        // Add vertices with the appropriate properties
        //going over the coordinates of a quad
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
            vertices[offset] = ob.transform.position.x
                    + xAdd * ob.transform.scale.x;
            vertices[offset + 1] = ob.transform.position.y
                    + yAdd * ob.transform.scale.y;

            //load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            //load texture coordinates
            vertices[offset + 6] = texCoords[i].x;
            vertices[offset + 7] = texCoords[i].y;

            //load texture id
            vertices[offset + 8] = texId;

            offset += VERTEX_SIZE;
        }
    }

    public void render() {
        boolean rebufferData = false;
        for (int i = 0; i < renderObNum; i++) {
            RenderObject go = RenderObjects[i];
            if(go != null && go.isDirty()) {
                loadVertexProperties(i);
                go.setClean();
                rebufferData = true;
            }

        }
        if(rebufferData) {
            //rebuffer only if changed
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }


        //use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
        shader.uploadFloat("uTime", (float) Time.getTime());
        shader.uploadMat4f("scale", Window.getScene().camera().getScaleMatrix());
        for(int i =0; i < textures.size(); i ++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);



        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);


        glDrawElements(GL_TRIANGLES, this.renderObNum * 6, GL_UNSIGNED_INT, 0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);


        glBindVertexArray(0);

        for (Texture texture : textures) {
            texture.unbind();
        }

        shader.detach();

    }



    public boolean hasRoom() {
        return this.hasRoom;
    }

    public boolean hasTextureRoom() {
        return  this.textures.size() < 7 ;
    }

    public boolean hasTexture(Texture texture) {
        return this.textures.contains(texture);
    }

    public int getZIndex() {
        return zIndex;
    }
}
