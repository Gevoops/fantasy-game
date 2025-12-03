package renderer;

import engine.Camera;
import engine.GameObject;
import components.SpriteSheetList;
import engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.MatrixUtils;
import util.Time;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class RenderBatch {
    private static int idCounter = 0;
    private int id;

    //vertex
    // =====
    //pos              color                       texture coords  tex id
    //float, float,    float, float, float, float  float, float   float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;

    //offsets
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;

    private final int VERTEX_SIZE = 10;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private float[] vertices;
    private int[] texSlots = {0,1,2,3,4,5,6,7};


    private List<Texture> textures;
    private ArrayList<Integer> texCounterArray = new ArrayList<>();
    private int vaoID, vboID;
    private int maxBatchSize;


    public RenderBatch(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        vertices = new float[VERTEX_SIZE * 4 * maxBatchSize]; //4 = vertices per quad
        textures = new ArrayList<>();
        id = idCounter++;
    }

    public void start() {
        //Generate and bind vertex array obj
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long)vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

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

        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
        glEnableVertexAttribArray(4);

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

    public void addGameObject(GameObject go) {
        // get index and add GameObject
        if(gameObjects.contains(go)){
            System.out.println("go already present in this render batch");
            return;
        }
        int index = Collections.binarySearch(gameObjects,go,
                    Renderer.byDepth);
        index = index < 0 ? -index - 1 : index;
        gameObjects.add(index ,go);
        go.setBatch(this);
        SpriteSheetList list  =  go.getComponent(SpriteSheetList.class);
        if(list != null) {
            ArrayList<SpriteSheet> l = list.getSpriteSheets();
            for(SpriteSheet s : l){
                if(!textures.contains(s.getTexture())) {
                    textures.add(s.getTexture());
                    texCounterArray.add(1);
                } else {
                    int ind = textures.indexOf(s.getTexture());
                    int oldVal = texCounterArray.get(ind);
                    texCounterArray.set(ind,oldVal + 1);
                }
            }
        } else {
            if(!textures.contains(go.getSprite().getTexture()) && go.getSprite().getTexture() != null) {
                textures.add(go.getSprite().getTexture());
                texCounterArray.add(1);
            } else {
                int ind = textures.indexOf(go.getSprite().getTexture());
                int oldVal = texCounterArray.get(ind);
                texCounterArray.set(ind,oldVal + 1);
            }
        }


    }

    public void deleteGameObj(GameObject go) {
        SpriteSheetList list  =  go.getComponent(SpriteSheetList.class);
        if(list != null) {
            ArrayList<SpriteSheet> l = list.getSpriteSheets();
            for(SpriteSheet s : l){
                if(textures.contains(s.getTexture())) {
                    int ind = textures.indexOf(s.getTexture());
                    int oldVal = texCounterArray.get(ind);
                    texCounterArray.set(ind,oldVal - 1);
                    if(oldVal == 1){
                        texCounterArray.remove(ind);
                        textures.remove(s.getTexture());
                    }
                }
            }
        } else {
            if(!textures.contains(go.getSprite().getTexture()) && go.getSprite().getTexture() != null) {
                int ind = textures.indexOf(go.getSprite().getTexture());
                int oldVal = texCounterArray.get(ind);
                texCounterArray.set(ind,oldVal - 1);
                if(oldVal == 1){
                    texCounterArray.remove(ind);
                    textures.remove(go.getSprite().getTexture());
                }
            }
        }


        gameObjects.remove(go);
    }

    public void render() {
        for (GameObject go : gameObjects) {
            loadVertexProperties(go);
        }


        // rebuffer
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // shader
        Shader shader = Renderer.getCurrentShader();
        Camera camera = Window.getInstance().getCurrentScene().getCamera();
        shader.uploadMat4f("uProjection", MatrixUtils.toFloat(camera.getProjectionMatrix()));
        shader.uploadMat4f("uView", MatrixUtils.toFloat(camera.getViewMatrix()));
        shader.uploadMat4f("scale", MatrixUtils.toFloat(camera.getScaleMatrix()));
        shader.uploadFloat("uTime", Time.getTime());
        for(int i =0; i < textures.size(); i ++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);



        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);



        glDrawElements(GL_TRIANGLES, gameObjects.size() * 6, GL_UNSIGNED_INT, 0);

        // unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);

        glBindVertexArray(0);
        glBindBuffer(GL_ARRAY_BUFFER,0);

        for (Texture texture : textures) {
            texture.unbind();
        }

        Renderer.getCurrentShader().detach();

    }

    private void loadVertexProperties(GameObject go) {
        int index = gameObjects.indexOf(go);
        Sprite sprite = go.getSprite();

        // find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texId = 0;
        //textures slot 0 is reserved for color.
        if(sprite.getTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                if(textures.get(i).equals(sprite.getTexture())) { // assumes all textures are added
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
            vertices[offset] = (float)( go.getX()
                    + xAdd * go.getTransform().scale.x);
            vertices[offset + 1] = (float)(go.getY()
                    + yAdd * go.getTransform().scale.y);

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

            // load Entity id
            vertices[offset + 9] = go.getID();

            offset += VERTEX_SIZE;
        }
    }





    public boolean hasRoom() {
        return this.gameObjects.size() < maxBatchSize;
    }

    public boolean hasTextureRoom() {
        return  this.textures.size() < 8 ;
    }

    public int getIndex() {
        return id;
    }

    public void setIndex(int index) {
        this.id = index;
    }

    public boolean isEmpty(){
        return gameObjects.isEmpty();
    }

    public GameObject get(int index){
        return gameObjects.get(index);
    }

    public int size(){
        return gameObjects.size();
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }
}
