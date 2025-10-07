package engine;

import org.joml.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

public class Camera {
    private Matrix4d projectionMatrix, invProjectionMatrix;
    private Matrix4d viewMatrix, invViewMatrix, invScaleMatrix;
    private Matrix4d scaleMatrix;
    public Vector2d viewPoint;
    private Vector2d projectionSize = new Vector2d(64 * 2 * 16  ,64 * 2  * 9 );
    private double zoom = 1f;


    public Camera(Vector2d viewPoint ){
        this.viewPoint = viewPoint;
        this.projectionMatrix = new Matrix4d();
        this.viewMatrix = new Matrix4d();
        this.invViewMatrix = viewMatrix.invert();

        adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(viewPoint.x, projectionSize.x, viewPoint.y,projectionSize.y, 0.0f,100.0f);

        scaleMatrix = new Matrix4d( zoom, 0.0f, 0.0f, 0.0f,
                                    0.0f,  zoom ,0.0f, 0.0f,
                                    0.0f, 0.0f,  1.0f, 0.0f,
                                    0.0f, 0.0f,0.0f, 1.0f);
        invProjectionMatrix = new Matrix4d(projectionMatrix).invert();
        invScaleMatrix = new Matrix4d(scaleMatrix).invert();
    }

    public Matrix4d getViewMatrix(){
        Vector3d cameraFront = new Vector3d(0.0f,0.0f,-1.0f);
        Vector3d cameraUp = new Vector3d(0.0f,1.0f,0.0f);
        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3d(viewPoint.x,viewPoint.y,20f),
                                cameraFront.add(viewPoint.x,viewPoint.y, 0.f),
                                 cameraUp);
        this.invViewMatrix = new Matrix4d((viewMatrix)).invert();
        return this.viewMatrix;
    }

    public void zoom(double scrollDirection){
        if(scrollDirection == 0) return;
        zoom *= scrollDirection > 0 ? 1.05 : 0.95;
        scaleMatrix.m00(zoom);
        scaleMatrix.m11(zoom);
        scaleMatrix.invert(invScaleMatrix);
    }

    public Matrix4d getProjectionMatrix(){
        return this.projectionMatrix;
    }
    public Matrix4d getScaleMatrix(){
        return this.scaleMatrix;
    }

    public Matrix4d getInvProjectionMatrix() {
        return invProjectionMatrix;
    }

    public Matrix4d getInvViewMatrix() {
        return invViewMatrix;
    }

    public Vector2d getProjectionSize() {
        return projectionSize;
    }

    public void setProjectionSize(Vector2d projectionSize) {
        this.projectionSize = projectionSize;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public Matrix4d getInvScaleMatrix() {
        return invScaleMatrix;
    }

    public Vector2d getViewPoint() {
        return viewPoint;
    }

    public void setViewPoint(Vector2d viewPoint) {
        this.viewPoint = viewPoint;
    }

    public void setViewPointX(double x) {
        this.viewPoint.x = x;
    }
    public void setViewPointY(double y) {
        this.viewPoint.y = y;
    }

    public void update(float dt){

    }
}
