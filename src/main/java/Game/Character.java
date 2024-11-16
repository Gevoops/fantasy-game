package Game;

public class Character implements Damageable{
    private RenderObject renderOb;
    private PlayObject playOb;

    public Character(RenderObject renderOb, PlayObject playOb) {
        this.renderOb = renderOb;
        this.playOb = playOb;
    }
}
