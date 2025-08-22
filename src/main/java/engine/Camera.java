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
    public Vector2f viewPoint;
    private Vector2f projectionSize = new Vector2f(64 * 2 * 16  ,64 * 2  * 9 );
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
        projectionMatrix.ortho(viewPoint.x, projectionSize.x, viewPoint.y,projectionSize.y, 0.0f,100.0f);

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

    public void zoom(float scrollDirection){
        if(scrollDirection == 0) return;
        zoom *= scrollDirection > 0 ? 1.05 : 0.95;
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

    public void update(float dt){
        Vector2f move = new Vector2f(0,0);
        if(KeyListener.isKeyPressed(GLFW_KEY_A)){
            move.x -= cameraSpeed;
        }
        if(KeyListener.isKeyPressed(GLFW_KEY_D)){
            move.x += cameraSpeed;
        }if(KeyListener.isKeyPressed(GLFW_KEY_W)){
           move.y += cameraSpeed;
        }if(KeyListener.isKeyPressed(GLFW_KEY_S)){
            move.y -= cameraSpeed;
        }
        if (move.x != 0 || move.y != 0){
            cameraSpeed = cameraSpeed >= 5 ? 5 : cameraSpeed + cameraAcceleration * dt;
            viewPoint.add(move.normalize(cameraSpeed * dt));
        }else {
            cameraSpeed = 1;
        }

    }
}
