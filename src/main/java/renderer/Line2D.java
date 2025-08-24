package renderer;

import org.joml.Vector2d;
import org.joml.Vector3f;

public class Line2D {
    private Vector2d start;
    private Vector2d end;
    private Vector3f color;
    private int lifeTime;

    public Line2D(Vector2d start, Vector2d end, Vector3f color, int lifeTime) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifeTime = lifeTime;
    }
    public Line2D(Vector2d start, Vector2d end, Vector3f color) {
        this.start = start;
        this.end = end;
        this.color = color;
        this.lifeTime = 1;
    }
    public Line2D(Vector2d start, Vector2d end) {
        this.start = start;
        this.end = end;
        this.color = new Vector3f(0,1,0);
        this.lifeTime = 1;
    }


    public int beginFrame(){
        this.lifeTime--;
        return this.lifeTime;
    }

    public Vector2d getStart() {
        return start;
    }

    public void setStart(Vector2d start) {
        this.start = start;
    }

    public Vector2d getEnd() {
        return end;
    }

    public void setEnd(Vector2d end) {
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
