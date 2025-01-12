package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_SRGB;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    private String filepath = null;
    private transient int texID;
    private int width, height;

    public Texture() {
    }

    public Texture(String filepath){
        this.filepath = filepath;
        //Generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        // set texture paramaters
        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);



        //when stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // when shrinking an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if(image != null){
            this.width = width.get(0);
            this.height = height.get(0);

            if(channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image );
            }else if(channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image );
            } else {
                assert false : "error Texture Unknown number of channels" + channels.get(0);
            }
        } else {
            assert false : "error Texture: could not load image '" + filepath + "'";
        }


        stbi_image_free(image);
    }

    public Texture(int width, int height){
        this.filepath = "Generated";
        //Generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);


        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); //a handle shrinking and stretching
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);



    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }
    public int getID() {
        return this.texID;
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D,0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getFilepath() {
        return filepath;
    }

    public boolean equals(Object ob){
        if(ob == null) return false;
        if (! (ob instanceof Texture)) return  false;
        Texture obTex = (Texture) ob;
        return  obTex.getWidth() == this.getWidth() &&
                obTex.getHeight() == this.getHeight() &&
                obTex.texID == this.texID &&
                obTex.filepath.equals(this.filepath);
    }


}
