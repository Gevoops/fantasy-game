package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    public Matrix4f projectionMatrix;
    public Matrix4f viewMatrix;
    public Matrix4f scaleMatrix;
    public Vector2f viewPoint;
    public static float spriteHeight = 0.5f, spriteWidth = 1.0f;
    public float scale = 1f;


    public Camera(Vector2f viewPoint ){
        this.viewPoint = viewPoint;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();



        this.scaleMatrix = new Matrix4f( scale, 0.0f, 0.0f, 0.0f, 0.0f,  scale,0.0f,
                0.0f, 0.0f, 0.0f,  1.0f, 0.0f, 0.0f, 0.0f,0.0f, 1.0f);
        adjustProjection();


    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 30f, 0.0f,32.0f * 15.0f, 0.0f,100.0f);


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
    public Matrix4f getScaleMatrix(){
        return this.scaleMatrix;
    }

}
