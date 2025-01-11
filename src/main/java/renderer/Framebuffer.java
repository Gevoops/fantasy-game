package renderer;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {
    private int fboID;
    private Texture texture;

    public Framebuffer(int width, int height){
        // Generate framebuffer
        fboID = glGenFramebuffers();
        bind();

        // Create the texture to render the data to, and attach it to our framebuffer
        this.texture = new Texture(width,height);
        glFramebufferTexture2D(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0,GL_TEXTURE_2D, texture.getID(),0);

        // Create renderbuffer store the depth info

        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER,rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER,GL_DEPTH_ATTACHMENT,GL_RENDERBUFFER,rboID);

        assert glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE : "Error Framebuffer is not complete";

        unbind();
    }

    public void bind(){
        glBindFramebuffer(GL_FRAMEBUFFER,fboID);
    }
    public void unbind(){
        glBindFramebuffer(GL_FRAMEBUFFER,0);
    }

    public int getTextureID(){
        return this.texture.getID();
    }

    public int getFboID() {
        return fboID;
    }

    public Texture getTexture() {
        return texture;
    }

}
