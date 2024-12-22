package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    public Matrix4f projectionMatrix, invProjectionMatrix;
    public Matrix4f viewMatrix, invViewMatrix, invScaleMatrix;
    public Matrix4f scaleMatrix;
    public Vector2f viewPoint;
    private final float Y_AXIS_SQUISH = 0.69f;
    private float scaleFactor = 1.15f;


    public Camera(Vector2f viewPoint ){
        this.viewPoint = viewPoint;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.invViewMatrix = viewMatrix.invert();



        adjustProjection();



    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 60f, 0.0f,32.0f * 21.0f, 0.0f,100.0f);

        scaleMatrix = new Matrix4f( scaleFactor, 0.0f, 0.0f, 0.0f,
                                    0.0f,  scaleFactor * Y_AXIS_SQUISH,0.0f, 0.0f,
                                    0.0f, 0.0f,  1.0f, 0.0f,
                                    0.0f, 0.0f,0.0f, 1.0f);
        invProjectionMatrix = new Matrix4f(projectionMatrix).invert();
        invScaleMatrix = new Matrix4f(scaleMatrix).invert();
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f,0.0f,-1.0f);
        Vector3f cameraUp = new Vector3f(0.0f,1.0f,0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(viewPoint.x,viewPoint.y,20f),
                                cameraFront.add(viewPoint.x,viewPoint.y, 0.f),
                                 cameraUp);
        this.invViewMatrix = new Matrix4f((viewMatrix)).invert();
        return this.viewMatrix;
    }

    public void scaleUpdate(float scrollDirection){
        float zoom = 1;
        if(scrollDirection > 0) {
            zoom = 1.1f;
        }else if (scrollDirection < 0 ){
            zoom = 0.9f;
        }
         scaleMatrix.m00(scaleMatrix.m00() * zoom);
        scaleMatrix.m11(scaleMatrix.m11() * zoom);
        scaleMatrix.invert(invScaleMatrix);
    }

    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }
    public Matrix4f getScaleMatrix(){
        return this.scaleMatrix;
    }

    public Matrix4f getInvProjectionMatrix() {
        return invProjectionMatrix;
    }

    public Matrix4f getInvViewMatrix() {
        return invViewMatrix;
    }
}
