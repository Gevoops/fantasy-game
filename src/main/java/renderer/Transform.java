package renderer;

import org.joml.Vector2d;

public class Transform {

    public Vector2d position;
    public Vector2d scale;


    public Transform(){
        init(new Vector2d(), new Vector2d());
    }

    public Transform(Transform tr){
        init(new Vector2d(tr.position), new Vector2d(tr.scale));
    }

    public Transform(Vector2d position, Vector2d scale) {
        init(position, scale);
    }

    public void init(Vector2d position, Vector2d scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform copy() {
        return new Transform(new Vector2d(this.position), new Vector2d(this.scale));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    public boolean equals(Transform transform) {
        return (this.scale.equals(transform.scale) && this.position.equals(transform.position));
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setScale(Vector2d scale) {
        this.scale = scale;
    }
}
