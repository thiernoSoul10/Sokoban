package Vue;

public abstract class Animation {
    protected float elapsed = 0f;
    protected boolean animation = true;

    public void update(float dt) {
        if(!animation) return;
        elapsed += dt;
        onUpdate(dt);
    }

    public void activateAnimation(){
        animation = true;
    }
    public void DesactivateAnimation(){
        animation = false;
    }

    protected abstract void onUpdate(float dt);

    public abstract boolean isFinished();
}