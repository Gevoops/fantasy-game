package engine;

public interface MouseControllerStrategy {
    void update(float dt );


    public abstract void handleRightClick();
}
