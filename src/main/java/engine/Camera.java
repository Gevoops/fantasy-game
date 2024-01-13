package engine;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    public Matrix4f projectionMatrix, viewMatrix, isoMatrix, scaleMatrix, inIsoMatrix;
    public Vector2f viewPoint;
    public float spriteHeight = 2.0f, spriteWidth = 2.0f;
    public float scale = 1.0f;


    public Camera(Vector2f viewPoint ){
        this.viewPoint = viewPoint;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();

        //rows and columns are reversed! is it just a transpose??
        this.isoMatrix = new Matrix4f(0.5f * spriteWidth,  0.25f * spriteHeight, 0.0f, 0.0f,
                                      -0.5f * spriteWidth, 0.25f * spriteHeight, 0.0f, 0.0f,
                                      0.0f,                0.0f,                 1.0f, 0.0f,
                                      0.0f,                0.0f,                 0.0f, 1.0f);

        // this.isoMatrix = new Matrix4f(0.25f, 0.125f, 0.0f, 0.0f, -0.25f, 0.125f,0.0f,
        //                0.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f, 0.0f,0.0f, 1.0f);

        this.scaleMatrix = new Matrix4f(1.0f * scale, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f * scale,0.0f,
                0.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f, 0.0f,0.0f, 1.0f);
        adjustProjection();
       this.inIsoMatrix = new Matrix4f();
       this.isoMatrix.get(this.isoMatrix);
       this.inIsoMatrix.invert();

    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40f, 0.0f,32.0f * 21.0f, 0.0f,100.0f);


    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f,0.0f,-1.0f);
        Vector3f cameraUp = new Vector3f(0.0f,1.0f,0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(viewPoint.x,viewPoint.y,20.0f),
                                cameraFront.add(viewPoint.x,viewPoint.y, 0.f),
                                 cameraUp);
        return this.viewMatrix;
    }

    public void scaleUpdate(float zoom, Matrix4f scaleMatrix){
         scaleMatrix.m00(scaleMatrix.m00() * zoom);
        scaleMatrix.m11(scaleMatrix.m11() * zoom);
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }
    public Matrix4f getIsoMatrix(){
        return this.isoMatrix;
    }
    public Matrix4f getScaleMatrix(){
        return this.scaleMatrix;
    }

}
