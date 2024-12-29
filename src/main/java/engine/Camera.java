package engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class Camera {
    private Matrix4f projectionMatrix, invProjectionMatrix;
    private Matrix4f viewMatrix, invViewMatrix, invScaleMatrix;
    private Matrix4f scaleMatrix;
    private Vector2f viewPoint;
    private Vector2f projectionSize = new Vector2f(64.0f * 30f,64.0f * 15.0f);
    private float zoom = 1f;
    private float cameraSpeed  = 1;
    private float cameraAcceleration = 0.2f;


    public Camera(Vector2f viewPoint ){
        this.viewPoint = viewPoint;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.invViewMatrix = viewMatrix.invert();



        adjustProjection();



    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x, 0.0f,projectionSize.y, 0.0f,100.0f);

        scaleMatrix = new Matrix4f( zoom, 0.0f, 0.0f, 0.0f,
                                    0.0f,  zoom ,0.0f, 0.0f,
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
        float scale = 1;
        if(scrollDirection > 0) {
            scale = 1.05f;
        }else if (scrollDirection < 0 ){
            scale = 0.95f;
        }
        zoom *= scale;
        scaleMatrix.m00(zoom);
        scaleMatrix.m11(zoom);
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

    public Vector2f getProjectionSize() {
        return projectionSize;
    }

    public void setProjectionSize(Vector2f projectionSize) {
        this.projectionSize = projectionSize;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public Matrix4f getInvScaleMatrix() {
        return invScaleMatrix;
    }

    public Vector2f getViewPoint() {
        return viewPoint;
    }

    public void setViewPoint(Vector2f viewPoint) {
        this.viewPoint = viewPoint;
    }

    public void setViewPointX(float x) {
        this.viewPoint.x = x;
    }
    public void setViewPointY(float y) {
        this.viewPoint.y = y;
    }

    public void moveCamera(float dt){
        Vector2f move = new Vector2f(0,0);
        float acceleration = cameraAcceleration * dt ;
        float speed  = cameraSpeed * dt ;
        if(KeyListener.isKeyPressed(GLFW_KEY_A)){
            move.x -= speed;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_D)){
            move.x += speed;
        }if(KeyListener.isKeyPressed(GLFW_KEY_W)){
           move.y += speed;
        }if(KeyListener.isKeyPressed(GLFW_KEY_S)){
            move.y -= speed;
        }
        if (move.x != 0 || move.y != 0){
            cameraSpeed = cameraSpeed >= 5 ? 5 : cameraSpeed + acceleration;
            viewPoint.add(move.normalize(speed));
        }else {
            cameraSpeed = 1;
        }

    }
}
