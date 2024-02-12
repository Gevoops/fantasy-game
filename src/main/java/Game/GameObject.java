package Game;

public class GameObject {
    private RenderObject renderOb;
    private PlayObject playOb;

    public GameObject(RenderObject renderOb, PlayObject playOb) {
        this.renderOb = renderOb;
        this.playOb = playOb;
    }
}
