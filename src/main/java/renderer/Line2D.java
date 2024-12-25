package renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {
    private Vector2f start;
    private Vector2f end;
    private Vector3f color;
    private int lifeTime;

    public Line2D(Vector2f start, Vector2f end, Vector3f color, int lifeTime) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifeTime = lifeTime;
    }
    public Line2D(Vector2f start, Vector2f end, Vector3f color) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifeTime = 1;
    }
    public Line2D(Vector2f start, Vector2f end) {
        this.start = start;
        this.end = end;
        this.color = new Vector3f(0,1,0);
        this.lifeTime = 1;
    }


    public int beginFrame(){
        this.lifeTime--;
        return this.lifeTime;
    }

    public Vector2f getStart() {
        return start;
    }

    public void setStart(Vector2f start) {
        this.start = start;
    }

    public Vector2f getEnd() {
        return end;
    }

    public void setEnd(Vector2f end) {
        this.end = end;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }
}
