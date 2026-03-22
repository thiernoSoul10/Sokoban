package Vue;

public class AnimationCoups extends Animation {
    private float startX, startY, endX, endY;
    private float duration = 0.15f;

    private float x, y;

    public AnimationCoups(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX   = endX;
        this.endY   = endY;
        this.x      = startX;
        this.y      = startY;
    }

    @Override
    protected void onUpdate(float dt) {
        if (elapsed > duration) elapsed = duration;
        float t = elapsed / duration;
        x = startX + (endX - startX) * t;
        y = startY + (endY - startY) * t;
    }

    @Override
    public boolean isFinished() {
        return elapsed >= duration;
    }

    public float getX() { return x; }
    public float getY() { return y; }
}